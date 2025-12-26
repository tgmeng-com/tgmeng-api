package com.tgmeng.common.webhook;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.StrUtil;
import com.dtflys.forest.http.ForestResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgmeng.common.bean.LicenseBean;
import com.tgmeng.common.enums.business.SubscriptionChannelTypeEnum;
import com.tgmeng.common.exception.ServerException;
import com.tgmeng.common.forest.client.webhook.IWebHookClient;
import com.tgmeng.common.forest.header.ForestRequestHeader;
import com.tgmeng.common.util.TimeUtil;
import com.tgmeng.common.util.UmamiUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class WangYiPOPOWebHook {
    @Autowired
    private IWebHookClient iWebHookClient;
    @Autowired
    private UmamiUtil umamiUtil;

    ObjectMapper mapper = new ObjectMapper();

    public void sendMessage(List<Map<String, Object>> newHotList, LicenseBean.SubscriptionPlatformConfig push, List<String> keywords, String accessKey) {
        StopWatch stopWatch = new StopWatch(accessKey);
        stopWatch.start();
        String webHook = getWebHook(push);
        log.info("🎠 开始推送网易POPO：{}条，accessKey: {}", newHotList.size(), accessKey);
        List<String> content = getHotContent(newHotList, keywords);
        List<String> postJsonBody = getPostBody(content, push);
        sendPost(webHook, postJsonBody, newHotList.size(), accessKey);
        stopWatch.stop();
        log.info("🎉 网易POPO成功推送：{}条，accessKey: {},耗时: {} ms", newHotList.size(), accessKey, stopWatch.getTotalTimeMillis());
    }

    public String getWebHook(LicenseBean.SubscriptionPlatformConfig push) {
        try {
            String webhook = push.getWebhook();
            if (StrUtil.isNotBlank(webhook) && StrUtil.isNotBlank(push.getSecret())) {
                return webhook;
            } else {
                throw new ServerException("webHook配置无效");
            }
        } catch (Exception e) {
            throw new ServerException("webHook配置无效:" + e.getMessage());
        }
    }

    public List<String> getHotContent(List<Map<String, Object>> newHotList, List<String> keywords) {

        List<List<Map<String, Object>>> splitNewHotList = CollUtil.split(newHotList, 20);
        List<String> jsonBodys = new ArrayList<>();

        for (List<Map<String, Object>> subNewHots : splitNewHotList) {
            // 哥们说不要这个开头了标题了
            //StringBuilder md = new StringBuilder("🍭 糖果梦热榜 🍭\n\n");
            StringBuilder md = new StringBuilder();
            for (int i = 0; i < subNewHots.size(); i++) {
                Map<String, Object> hot = subNewHots.get(i);
                //md.append(i + 1).append(". ")
                //        .append("").append(hot.get("title")).append("(")
                //        .append(hot.get("url")).append(")")
                //        .append("       --").append(hot.get("platformName"))
                //        .append("\n");

                // 哥们说不要超链接了
                md.append(i + 1).append(". ")
                        .append("").append(hot.get("title"))
                        .append("    --").append(hot.get("platformName"))
                        .append("\n");
            }
            md.append("\n");
            // 哥们不要这个共计和订阅词了
            //md.append("📱 共计：").append(subNewHots.size()).append(" 条\n");
            //md.append("📰 订阅：").append(String.join(", ", keywords)).append("\n");
            md.append("⏰ 时间：").append(TimeUtil.getCurrentTimeFormat(TimeUtil.defaultPattern)).append("\n");
            md.append("🙋🏻‍♂️ 来源：").append("🍭 糖果梦热榜 - https://tgmeng.com 🍭");
            jsonBodys.add(md.toString());
        }
        return jsonBodys;
    }

    public List<String> getPostBody(List<String> hotContent, LicenseBean.SubscriptionPlatformConfig push) {
        try {
            List<String> postBodys = new ArrayList<>();
            for (String subHotContent : hotContent) {
                Map<String, Object> req = new HashMap<>();
                String timeStamp = String.valueOf(TimeUtil.getCurrentTimeMillis());
                req.put("signData", getSignData(push,timeStamp));
                req.put("message", subHotContent);
                req.put("timestamp", timeStamp);
                postBodys.add(mapper.writeValueAsString(req));
            }
            return postBodys;
        } catch (Exception e) {
            throw new ServerException("网易POPO组装请求postBody失败:" + e.getMessage());
        }
    }

    public void sendPost(String webHook, List<String> postJsonBodys, Integer count, String accessKey) {
        for (String postJsonBody : postJsonBodys) {
            ForestRequestHeader forestRequestHeader = new ForestRequestHeader().setContentType("application/json;charset=UTF-8");
            try {
                ForestResponse forestResponse = iWebHookClient.sendMessage(forestRequestHeader, webHook, postJsonBody);
                System.out.println(forestResponse);
            } catch (Exception e) {
                log.error("网易POPO推送失败：{}", e.getMessage());
            }
        }
        umamiUtil.sendEvent(SubscriptionChannelTypeEnum.WANGYIPOPO.getDescription(), count);
    }

    private String getSignData(LicenseBean.SubscriptionPlatformConfig push, String timestamp) {
        try {
            String secret = push.getSecret();
            String sign = "";
            if (StrUtil.isNotBlank(push.getWebhook()) && StrUtil.isNotBlank(secret)) {
                String stringToSign = timestamp + "\n" + secret;
                Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(new SecretKeySpec(stringToSign.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
                byte[] signData = mac.doFinal(new byte[]{});
                sign = new String(Base64.encodeBase64(signData));
                return sign;
            } else {
                throw new ServerException("webHook配置无效");
            }
        } catch (Exception e) {
            throw new ServerException("webHook配置无效:" + e.getMessage());
        }
    }
}
