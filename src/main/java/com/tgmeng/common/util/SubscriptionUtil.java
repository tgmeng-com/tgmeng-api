package com.tgmeng.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.crypto.SecureUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgmeng.common.bean.SubscriptionBean;
import com.tgmeng.common.enums.business.SubscriptionChannelTypeEnum;
import com.tgmeng.common.exception.ServerException;
import com.tgmeng.common.webhook.*;
import com.tgmeng.service.cachesearch.ICacheSearchService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import static com.tgmeng.common.util.StringUtil.generateRandomFileName;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionUtil {

    // 定义一个 ReentrantLock 锁
    private final ReentrantLock lock = new ReentrantLock();

    @Autowired
    private ThreadPoolTaskExecutor executor;
    @Lazy
    @Autowired
    private DingTalkWebHook dingTalkWebHook;
    @Lazy
    @Autowired
    private FeiShuWebHook feiShuWebHook;
    @Lazy
    @Autowired
    private TelegramWebHook telegramWebHook;
    @Lazy
    @Autowired
    private QiYeWeiXinWebHook qiYeWeiXinWebHook;
    @Lazy
    @Autowired
    private NtfyWebHook ntfyWebHook;
    @Lazy
    @Autowired
    private GotifyWebHook gotifyWebHook;

    @Value("${my-config.subscription.dir}")
    private String subscriptionDir;
    @Value("${my-config.subscription.file-suffix}")
    private String fileSuffix;

    @Value("${my-config.subscription.max-hot-number}")
    private Integer maxHotNumber;

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final ICacheSearchService cacheSearchService;


    @PostConstruct
    public void init() {
        log.info("✅ ControllerApiSchedule 已加载，配置检查通过！");
    }


    public void subscriptionOption() {
        // 如果当前有线程持有锁，其他线程会被阻塞，直到该锁被释放
        lock.lock();
        try {
            // 订阅操作的逻辑
            FileUtil.checkDirExitAndMake(subscriptionDir);
            File[] subscriptionFileList = FileUtil.getAllFilesInPath(subscriptionDir);
            cycleFile(subscriptionFileList);
        } catch (Exception e) {
            log.error("订阅处理失败: {}", e.getMessage());
        } finally {
            // 确保在操作完成后释放锁
            lock.unlock();
        }
    }

    // 遍历订阅文件列表
    public void cycleFile(File[] subscriptionFiles) {
        // 使用 AtomicInteger 来保证线程安全地统计成功和失败的次数
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        // 从缓存中获取热点数据
        List<Map<String, Object>> hotList = cacheSearchService.getCacheSearchAllByWord(null, null).getData();
        // 使用 CompletableFuture 来并行处理每个文件
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (File file : subscriptionFiles) {
            // 打印线程池状态
            java.util.concurrent.ThreadPoolExecutor tp = executor.getThreadPoolExecutor();
            log.info("🧵线程池状态 - 核心: {}, 最大: {}, 当前: {}, 活跃: {}, 队列: {}",
                    tp.getCorePoolSize(), tp.getMaximumPoolSize(), tp.getPoolSize(), tp.getActiveCount(), tp.getQueue().size());

            // 提交每个文件处理的任务
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    startSubscriptionOption(file, hotList);
                    successCount.incrementAndGet();  // 线程安全地增加成功计数
                } catch (Exception e) {
                    failCount.incrementAndGet();  // 线程安全地增加失败计数
                    log.error("✈️订阅推送异常：{},异常信息：{}", file.getName(), e.getMessage());
                }
            }, executor);  // 提交任务到线程池

            futures.add(future);
        }

        // 等待所有任务完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // 打印最终统计结果
        log.info("订阅处理完成 - 成功: {}, 失败: {}, 总计: {}", successCount.get(), failCount.get(), subscriptionFiles.length);
    }

    // 开始遍历热点去订阅
    public void startSubscriptionOption(File file, List<Map<String, Object>> hotList) {
        try {
            SubscriptionBean subscriptionBean = MAPPER.readValue(file, SubscriptionBean.class);
            pushToChannel(subscriptionBean, hotList, file);
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    // 执行订阅操作
    public void pushToChannel(SubscriptionBean subscriptionBean, List<Map<String, Object>> hotList, File file) {
        // 全局关键词
        List<String> keywords = subscriptionBean.getKeywords() == null ? new ArrayList<>() : subscriptionBean.getKeywords();
        // 已发送的哈希集合
        Set<String> sentSet = subscriptionBean.getSent();
        // 存储新推送的哈希，用于后续更新文件
        Set<String> newHashes = new LinkedHashSet<>();

        // 使用 CompletableFuture 并行处理每个平台
        List<CompletableFuture<Void>> futures = subscriptionBean.getPlatforms().stream()
                .map(push -> CompletableFuture.runAsync(() -> {
                    try {
                        // 合并关键词
                        List<String> mergedKeywords = mergeKeywords(keywords, push.getPlatformKeywords());
                        // 筛选平台关键词和独立关键词合并后符合的热点，并且过滤掉已推送的
                        if (CollUtil.isNotEmpty(mergedKeywords)) {
                            List<Map<String, Object>> newHotList = getNewHotList(hotList, mergedKeywords, sentSet);
                            if (CollUtil.isNotEmpty(newHotList)) {
                                // 记录新推送的哈希
                                for (Map<String, Object> hotItem : newHotList) {
                                    String hashBase64 = generateHash(hotItem.get("keyword").toString(), hotItem.get("dataCardName").toString());
                                    newHashes.add(hashBase64);
                                }
                                sendToPlatform(push, newHotList, mergedKeywords);
                            }
                        }
                    } catch (Exception e) {
                        log.error("推送异常：{}", e.getMessage());
                    }
                }))
                .toList();

        // 等待所有的推送任务完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // 推送完成后，统一更新文件内容
        if (!newHashes.isEmpty()) {
            // 将新推送的哈希添加到已发送的集合中
            sentSet.addAll(newHashes);
            // 更新文件内容
            updateFileContent(subscriptionBean, file);
        }
    }

    private List<String> mergeKeywords(List<String> keywords, List<String> platformKeywords) {
        platformKeywords = platformKeywords == null ? new ArrayList<>() : platformKeywords;
        // 合并关键词
        List<String> mergedKeywords = new ArrayList<>(keywords);
        mergedKeywords.addAll(platformKeywords);
        return mergedKeywords;
    }

    private List<Map<String, Object>> getNewHotList(List<Map<String, Object>> hotList, List<String> mergedKeywords, Set<String> sentSet) {
        return hotList.stream()
                .filter(map -> {
                    String keyWord = String.valueOf(map.get("keyword"));
                    String hashBase64 = generateHash(keyWord, String.valueOf(map.get("dataCardName")));
                    return mergedKeywords.stream().anyMatch(keyWord::contains) && !sentSet.contains(hashBase64);
                }).toList();
    }

    private void sendToPlatform(SubscriptionBean.PushConfig push, List<Map<String, Object>> newHotList, List<String> mergedKeywords) {
        switch (push.getType()) {
            case SubscriptionChannelTypeEnum.DINGDING:
                dingTalkWebHook.sendMessage(newHotList, push, mergedKeywords);
                break;
            case SubscriptionChannelTypeEnum.FEISHU:
                feiShuWebHook.sendMessage(newHotList, push, mergedKeywords);
                break;
            case SubscriptionChannelTypeEnum.TELEGRAM:
                telegramWebHook.sendMessage(newHotList, push, mergedKeywords);
                break;
            case SubscriptionChannelTypeEnum.QIYEWEIXIN:
                qiYeWeiXinWebHook.sendMessage(newHotList, push, mergedKeywords);
                break;
            case SubscriptionChannelTypeEnum.NTFY:
                ntfyWebHook.sendMessage(newHotList, push, mergedKeywords);
                break;
            //case SubscriptionChannelTypeEnum.GOTIFY:
            //    gotifyWebHook.sendMessage(newHotList, push, mergedKeywords);
            //    break;
            default:
                break;
        }
    }

    private String generateHash(String keyword, String dataCardName) {
        // 生成 MD5 二进制
        byte[] hashBinary = SecureUtil.md5().digest(
                (keyword + dataCardName).getBytes(StandardCharsets.UTF_8)
        );
        // 转 Base64（22 个字符左右）
        return Base64.getEncoder().encodeToString(hashBinary);
    }

    public void updateFileContent(SubscriptionBean subscriptionBean, File file) {
        try {
            Set<String> sent = subscriptionBean.getSent();
            int excess = sent.size() - maxHotNumber;
            if (excess > 0) {
                Iterator<String> it = sent.iterator();
                for (int i = 0; i < excess && it.hasNext(); i++) {
                    it.next();
                    it.remove();
                }
                log.debug("清理了 {} 条最早的记录", excess);
            }
            FileUtil.writeToFile(file, subscriptionBean);
        } catch (Exception e) {
            throw new ServerException("重写文件失败 - 文件: " + file.getName() + ", 错误: " + e.getMessage());
        }
    }

    // TODO 生成初始化订阅文件，这个只有站长后台手动触发，为的是保证站里订阅key是手动下发
    public List<String> generateSubscriptionFile(Integer count) {
        log.info("开始创建初始化文件，数量为：" + count);
        List<String> newFileList = new ArrayList<>();
        Set<String> allFileNamesInPath = new HashSet<>(FileUtil.getAllFileNamesInPath(subscriptionDir));
        for (int i = 0; i < count; i++) {
            String fileName = generateRandomFileName();
            if (allFileNamesInPath.contains(fileName)) {
                continue;
            }
            try {
                String jsonContent = FileUtil.readFileToStringFromClasspath("template/SubscriptionInitTemplate.txt");
                SubscriptionBean subscriptionBean = MAPPER.readValue(jsonContent, SubscriptionBean.class);
                subscriptionBean.setAccessKey(fileName.split("\\.")[0]);
                // 创建文件并写入内容
                FileUtil.createFileAndWriteInitContent(subscriptionDir, fileName, MAPPER.writeValueAsString(subscriptionBean));
            } catch (Exception e) {
                throw new ServerException("创建文件失败，文件名:" + fileName + " 异常信息:" + e);
            }
            newFileList.add(fileName);
            log.info("第{}个文件创建成功：{}", i, fileName);
        }
        log.info("所有文件创建成功，共{}个", newFileList.size());
        return newFileList;
    }
}
