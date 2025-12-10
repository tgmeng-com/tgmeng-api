package com.tgmeng.common.util;

import cn.hutool.crypto.SecureUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgmeng.common.bean.SubscriptionBean;
import com.tgmeng.common.enums.business.SubscriptionChannelTypeEnum;
import com.tgmeng.common.exception.ServerException;
import com.tgmeng.common.webhook.*;
import com.tgmeng.service.cachesearch.ICacheSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.tgmeng.common.util.StringUtil.generateRandomFileName;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionUtil {

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


    public void subscriptionOption() {
        FileUtil.checkDirExitAndMake(subscriptionDir);
        File[] subscriptionFileList = FileUtil.getAllFilesInPath(subscriptionDir);
        cycleFile(subscriptionFileList);
    }

    // 遍历订阅文件列表
    public void cycleFile(File[] subscriptionFiles) {
        int successCount = 0;
        int failCount = 0;
        for (File file : subscriptionFiles) {
            try {
                startSubscriptionOption(file);
                successCount++;
            } catch (Exception e) {
                failCount++;
                log.error("✈️订阅推送异常：{},异常信息：{}", file.getName(), e.getMessage());
            }
        }
        log.info("订阅处理完成 - 成功: {}, 失败: {}, 总计: {}", successCount, failCount, subscriptionFiles.length);
    }

    // 开始遍历热点去订阅
    public void startSubscriptionOption(File file) {
        try {
            SubscriptionBean subscriptionBean = MAPPER.readValue(file, SubscriptionBean.class);
            List<Map<String, Object>> hotList = cacheSearchService.getCacheSearchAllByWord(null, null).getData();
            pushToChannel(subscriptionBean, hotList, file);
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    // 执行订阅操作
    public void pushToChannel(SubscriptionBean subscriptionBean, List<Map<String, Object>> hotList, File file) {
        // 全局关键词
        List<String> keywords = subscriptionBean.getKeywords();
        // 已发送的哈希集合
        Set<String> sentSet = new HashSet<>(subscriptionBean.getSent());
        for (SubscriptionBean.PushConfig push : subscriptionBean.getPlatforms()) {
            try {
                // 独立关键词
                List<String> platformKeywords = push.getPlatformKeywords();
                // 合并关键词
                List<String> mergedKeywords = new ArrayList<>(keywords);
                mergedKeywords.addAll(platformKeywords);

                // 筛选平台关键词和独立关键词合并后符合的热点，并且过滤掉已推送的
                List<Map<String, Object>> newHotList = hotList.stream()
                        .filter(map -> {
                            String keyWord = String.valueOf(map.get("keyword"));
                            String hashBase64 = generateHash(keyWord, String.valueOf(map.get("dataCardName")));
                            return mergedKeywords.stream().anyMatch(keyWord::contains) && !sentSet.contains(hashBase64);
                        }).toList();

                if (!newHotList.isEmpty()) {
                    // 记录新推送的哈希，用于后续更新文件
                    List<String> newHashes = new ArrayList<>();
                    for (Map<String, Object> hotItem : newHotList) {
                        String hashBase64 = generateHash(hotItem.get("keyword").toString(), hotItem.get("dataCardName").toString());
                        newHashes.add(hashBase64);
                    }
                    sentSet.addAll(newHashes);

                    updateFileContent(subscriptionBean, file);
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
            } catch (Exception e) {
                log.error("推送异常：{}", e.getMessage());
            }
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

    public void updateFileContent(SubscriptionBean subscriptionBean,File file) {
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
