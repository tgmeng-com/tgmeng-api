package com.tgmeng.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.RandomUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgmeng.common.bean.LicenseBean;
import com.tgmeng.common.bean.SubscriptionBean;
import com.tgmeng.common.enums.business.SearchModeEnum;
import com.tgmeng.common.enums.business.SubscriptionChannelTypeEnum;
import com.tgmeng.common.exception.ServerException;
import com.tgmeng.common.webhook.*;
import com.tgmeng.service.cachesearch.ICacheSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

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
    @Lazy
    @Autowired
    private WangYiPOPOWebHook wangYiPOPOWebHook;

    @Value("${my-config.subscription.dir}")
    private String subscriptionDir;

    @Value("${my-config.license.dir}")
    private String licenseDir;


    @Value("${my-config.subscription.max-hot-number}")
    private Integer maxHotNumber;

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final ICacheSearchService cacheSearchService;

    public void subscriptionOption() {
        // 如果当前有线程持有锁，其他线程会被阻塞，直到该锁被释放
        lock.lock();
        StopWatch stopWatch = new StopWatch(RandomUtil.randomString(10));
        stopWatch.start();
        try {
            log.info("✈️✈️✈️ 开始处理订阅");
            FileUtil.checkDirExitAndMake(licenseDir);
            File[] licenseFileList = FileUtil.getAllFilesInPath(licenseDir);
            log.info("✈️✈️ 共 {} 个订阅文件", licenseFileList.length);
            cycleFile(licenseFileList);
        } catch (Exception e) {
            log.error("订阅处理失败: {}", e.getMessage());
        } finally {
            stopWatch.stop();
            log.info("✈️✈️✈️ ✅ 订阅操作完成，耗时: {} ms", stopWatch.getTotalTimeMillis());
            lock.unlock();
        }
    }

    // 遍历订阅文件列表
    public void cycleFile(File[] licenseFileList) {
        // 使用 AtomicInteger 来保证线程安全地统计成功和失败的次数
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        // 从缓存中获取热点数据
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("word", null);
        paramMap.put("searchMode", SearchModeEnum.MO_HU_PI_PEI_ONE_MINUTES.getValue());
        List<Map<String, Object>> hotList = cacheSearchService.searchByWord(paramMap).getData();
        // 使用 CompletableFuture 来并行处理每个文件
        for (File file : licenseFileList) {
            // 提交每个文件处理的任务
            log.info("✈️ 开始处理订阅: {}", file.getName());
            try {
                startSubscriptionOption(file, hotList);
                successCount.incrementAndGet();  // 线程安全地增加成功计数
            } catch (Exception e) {
                failCount.incrementAndGet();  // 线程安全地增加失败计数
                log.error("✈️ 订阅推送异常：{},异常信息：{}", file.getName(), e.getMessage());
            }
        }
        // 打印最终统计结果
        log.info("订阅处理完成 - 成功: {}, 失败: {}, 总计: {}", successCount.get(), failCount.get(), licenseFileList.length);
    }

    // 开始遍历热点去订阅
    public void startSubscriptionOption(File licenseFile, List<Map<String, Object>> hotList) {
        try {
            File subscriptionFile = new File(subscriptionDir + licenseFile.getName().split("\\.")[0] + StringUtil.SubscriptionFileExtension);
            LicenseBean licenseBean = MAPPER.readValue(licenseFile, LicenseBean.class);
            SubscriptionBean subscriptionBean = MAPPER.readValue(subscriptionFile, SubscriptionBean.class);
            pushToChannel(licenseBean, subscriptionBean, hotList, subscriptionFile);
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    // 执行订阅操作
    public void pushToChannel(LicenseBean licenseBean, SubscriptionBean subscriptionBean, List<Map<String, Object>> hotList, File subscriptionFile) {
        // 全局关键词
        List<String> keywords = licenseBean.getSubscriptionGlobalKeywords() == null ? new ArrayList<>() : licenseBean.getSubscriptionGlobalKeywords();
        // 已发送的哈希集合
        Set<String> sentSet = subscriptionBean.getSent();
        // 存储新推送的哈希，用于后续更新文件
        Set<String> newHashes = ConcurrentHashMap.newKeySet();
        licenseBean.getSubscriptionPlatformConfigs().forEach(push -> {
            try {
                // 合并关键词
                List<String> mergedKeywords = mergeKeywords(keywords, push.getSubscriptionPlatformKeywords());
                // 获取过滤的平台
                List<String> filterPlatformNames = getFilterPlatformNames(licenseBean, push);
                // 筛选平台关键词和独立关键词合并后符合的热点，并且过滤掉已推送的
                if (CollUtil.isNotEmpty(mergedKeywords)) {
                    List<Map<String, Object>> newHotList = getNewHotList(hotList, mergedKeywords, filterPlatformNames, sentSet);
                    if (CollUtil.isNotEmpty(newHotList)) {
                        // 记录新推送的哈希
                        for (Map<String, Object> hotItem : newHotList) {
                            String hashBase64 = HashUtil.generateHash(hotItem.get("title").toString(), hotItem.get("platformName").toString());
                            newHashes.add(hashBase64);
                        }
                        sendToPlatform(push, newHotList, mergedKeywords, licenseBean.getLicenseCode());
                    }
                }
            } catch (Exception e) {
                log.error("推送异常：{}", e.getMessage());
            }
        });
        // 推送完成后，统一更新文件内容
        StopWatch stopWatch = new StopWatch(subscriptionFile.getName() + RandomUtil.randomString(10));
        stopWatch.start();
        if (!newHashes.isEmpty()) {
            // 将新推送的哈希添加到已发送的集合中
            sentSet.addAll(newHashes);
            // 更新文件内容
            updateFileContent(subscriptionBean, subscriptionFile);
            stopWatch.stop();
        } else {
            stopWatch.stop();
            log.info("✈️ 完成更新订阅文件: {}，未推送新数据", subscriptionFile.getName());
        }
    }

    // 获取筛选的平台，优先独立平台的筛选，独立平台没有的话就看全局，全局没有就返回空，代表不过滤
    private List<String> getFilterPlatformNames(LicenseBean licenseBean, LicenseBean.SubscriptionPlatformConfig subscriptionPlatformConfig) {
        List<String> subscriptionPlatformCategories = subscriptionPlatformConfig.getSubscriptionPlatformCategories();
        if (CollUtil.isNotEmpty(subscriptionPlatformCategories)) {
            return subscriptionPlatformCategories;
        }
        List<String> subscriptionGlobalCategories = licenseBean.getSubscriptionGlobalCategories();
        if (CollUtil.isNotEmpty(subscriptionGlobalCategories)) {
            return subscriptionGlobalCategories;
        }
        return new ArrayList<>();
    }

    private List<String> mergeKeywords(List<String> keywords, List<String> platformKeywords) {
        platformKeywords = platformKeywords == null ? new ArrayList<>() : platformKeywords;
        // 合并关键词
        List<String> mergedKeywords = new ArrayList<>(keywords);
        mergedKeywords.addAll(platformKeywords);
        return mergedKeywords;
    }

    private List<Map<String, Object>> getNewHotList(List<Map<String, Object>> hotList, List<String> mergedKeywords, List<String> filterPlatformNames, Set<String> sentSet) {
        return hotList.stream()
                // 过滤平台
                .filter(map -> {
                    if (CollUtil.isEmpty(filterPlatformNames)) {
                        return true; // 不过滤
                    }
                    String platformName = String.valueOf(map.get("platformName"));
                    return filterPlatformNames.contains(platformName);
                })
                // 过滤关键词
                .filter(map -> {
                    String hotTitle = String.valueOf(map.get("title"));
                    String hashBase64 = HashUtil.generateHash(hotTitle, String.valueOf(map.get("platformName")));
                    String hotTitleLower = hotTitle.toLowerCase();
                    return mergedKeywords.stream().anyMatch(keyword -> hotTitleLower.contains(keyword.toLowerCase())) && !sentSet.contains(hashBase64);
                })
                .collect(Collectors.toMap(
                        map -> HashUtil.generateHash(
                                String.valueOf(map.get("title")),
                                String.valueOf(map.get("platformName"))
                        ),
                        map -> map,
                        (existing, replacement) -> replacement,  // 保留最后一个
                        LinkedHashMap::new
                ))
                .values()
                .stream()
                .toList();
    }

    private void sendToPlatform(LicenseBean.SubscriptionPlatformConfig push, List<Map<String, Object>> newHotList, List<String> mergedKeywords, String accessKey) {
        switch (push.getType()) {
            case SubscriptionChannelTypeEnum.DINGDING:
                dingTalkWebHook.sendMessage(newHotList, push, mergedKeywords, accessKey);
                break;
            case SubscriptionChannelTypeEnum.FEISHU:
                feiShuWebHook.sendMessage(newHotList, push, mergedKeywords, accessKey);
                break;
            case SubscriptionChannelTypeEnum.TELEGRAM:
                telegramWebHook.sendMessage(newHotList, push, mergedKeywords, accessKey);
                break;
            case SubscriptionChannelTypeEnum.QIYEWEIXIN:
                qiYeWeiXinWebHook.sendMessage(newHotList, push, mergedKeywords, accessKey);
                break;
            case SubscriptionChannelTypeEnum.NTFY:
                ntfyWebHook.sendMessage(newHotList, push, mergedKeywords, accessKey);
                break;
            case SubscriptionChannelTypeEnum.GOTIFY:
                gotifyWebHook.sendMessage(newHotList, push, mergedKeywords, accessKey);
                break;
            case SubscriptionChannelTypeEnum.WANGYIPOPO:
                wangYiPOPOWebHook.sendMessage(newHotList, push, mergedKeywords, accessKey);
                break;
            default:
                break;
        }
    }

    public void updateFileContent(SubscriptionBean subscriptionBean, File file) {
        StopWatch stopWatch = new StopWatch(file.getName() + RandomUtil.randomString(10));
        stopWatch.start();
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
            stopWatch.stop();
            log.info("✈️ 完成更新订阅文件: {}，耗时: {} ms", file.getName(), stopWatch.getTotalTimeMillis());
        } catch (Exception e) {
            throw new ServerException("重写文件失败 - 文件: " + file.getName() + ", 错误: " + e.getMessage());
        }
    }
}
