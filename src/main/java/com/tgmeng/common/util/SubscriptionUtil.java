package com.tgmeng.common.util;

import cn.hutool.crypto.SecureUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgmeng.common.bean.SubscriptionBean;
import com.tgmeng.common.enums.business.SubscriptionChannelTypeEnum;
import com.tgmeng.common.exception.ServerException;
import com.tgmeng.common.webhook.DingTalkWebHook;
import com.tgmeng.common.webhook.FeiShuWebHook;
import com.tgmeng.common.webhook.TelegramWebHook;
import com.tgmeng.service.cachesearch.ICacheSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.tgmeng.common.util.StringUtil.generateRandomFileName;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionUtil {

    @Autowired
    private DingTalkWebHook dingTalkWebHook;
    @Autowired
    private FeiShuWebHook feiShuWebHook;
    @Autowired
    private TelegramWebHook telegramWebHook;

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
        for (File file : subscriptionFiles) {
            try {
                startSubscriptionOption(file);
            } catch (Exception e) {
                log.error("订阅推送异常：{},异常信息：{}",file.getName(),e.getMessage());
                continue;
            }
        }
    }

    // 开始遍历热点去订阅
    public void startSubscriptionOption(File file) {
        try {
            SubscriptionBean subscriptionBean = MAPPER.readValue(file, SubscriptionBean.class);
            List<Map<String, Object>> hotList = cacheSearchService.getCacheSearchAllByWord(null, subscriptionBean.getKeywords()).getData();
            List<Map<String, Object>> newHotList = new ArrayList<Map<String, Object>>();

            for (Map<String, Object> hotItem : hotList) {
                // 生成 MD5 二进制
                byte[] hashBinary = SecureUtil.md5().digest(
                        (hotItem.get("keyword").toString() + hotItem.get("dataCardName").toString())
                                .getBytes(StandardCharsets.UTF_8)
                );
                // 转 Base64（22 个字符左右）
                String hashBase64 = Base64.getEncoder().encodeToString(hashBinary);
                if (subscriptionBean.getSent().contains(hashBase64)) continue;
                newHotList.add(hotItem);
                updateFileContent(subscriptionBean, hashBase64, file);
            }
            if (!newHotList.isEmpty()) {
                pushToChannel(subscriptionBean, newHotList);
            }
        } catch (Exception e) {
            throw new ServerException("推送订阅失败");
        }
    }

    public void updateFileContent(SubscriptionBean subscriptionBean, String hashBase64, File file) {
        try {
            subscriptionBean.getSent().add(hashBase64); // 记录已推送,添加到记录值里
            // 超过上限，批量删除最早的 excess 条
            int excess = subscriptionBean.getSent().size() - maxHotNumber;
            if (excess > 0) {
                Iterator<String> it = subscriptionBean.getSent().iterator();
                for (int i = 0; i < excess && it.hasNext(); i++) {
                    it.next();
                    it.remove();
                }
            }
            FileUtil.writeToFile(file, subscriptionBean);
        } catch (Exception e) {
            throw new ServerException("重写文件失败");
        }
    }

    // 执行订阅操作
    public void pushToChannel(SubscriptionBean sub, List<Map<String, Object>> newHotList) {
        for (SubscriptionBean.PushConfig push : sub.getPlatforms()) {
            switch (push.getType()) {
                case SubscriptionChannelTypeEnum.DINGDING:
                    dingTalkWebHook.sendMessage(newHotList, push, sub.getKeywords());
                    break;
                case SubscriptionChannelTypeEnum.FEISHU:
                    feiShuWebHook.sendMessage(newHotList, push, sub.getKeywords());
                    break;
                case SubscriptionChannelTypeEnum.TELEGRAM:
                    telegramWebHook.sendMessage(newHotList, push, sub.getKeywords());
                    break;
                case SubscriptionChannelTypeEnum.EMAIL:
                    System.out.println("EMAIL");
                    break;
                default:
                    break;
            }
        }
    }

    // TODO 生成初始化订阅文件，这个只有站长后台手动触发，为的是保证站里订阅key是手动下发
    public List<String> generateSubscriptionFile(Integer count) {
        log.info("开始创建初始化文件，数量为：" + count);
        List<String> newFileList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            List<String> allFileNamesInPath = FileUtil.getAllFileNamesInPath(subscriptionDir);
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
