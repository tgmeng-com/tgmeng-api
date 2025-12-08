package com.tgmeng.common.webhook;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tgmeng.common.bean.SubscriptionBean;
import com.tgmeng.common.enums.business.SubscriptionChannelTypeEnum;
import com.tgmeng.common.exception.ServerException;
import com.tgmeng.common.forest.client.webhook.IWebHookClient;
import com.tgmeng.common.util.TimeUtil;
import com.tgmeng.common.util.UmamiUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DingTalkWebHook {
    @Autowired
    private IWebHookClient iWebHookClient;
    @Autowired
    private UmamiUtil umamiUtil;

    public void sendMessage(List<Map<String, Object>> newHotList, SubscriptionBean.PushConfig push, List<String> keywords) {
        String webHook = getWebHook(push);
        log.info("🎠开始推送钉钉");
        List<String> content = getHotContent(newHotList, keywords);
        List<String> postJsonBody = getPostBody(content);
        sendPost(webHook, postJsonBody,newHotList.size());
    }

    public String getWebHook(SubscriptionBean.PushConfig push) {
        try {
            String webhook = push.getWebhook();
            String secret = push.getSecret();
            long timestamp = System.currentTimeMillis();
            String sign = "";
            if (StrUtil.isNotBlank(webhook) && StrUtil.isNotBlank(secret)) {
                String stringToSign = timestamp + "\n" + secret;
                Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
                byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
                sign = URLEncoder.encode(Base64.getEncoder().encodeToString(signData), "UTF-8");
                webhook += "&timestamp=" + timestamp + "&sign=" + sign;
            }else {
                throw new ServerException("webHook配置无效");
            }
            return webhook;
        } catch (Exception e) {
            throw new ServerException("获取webhook地址失败");
        }
    }

    public List<String> getHotContent(List<Map<String, Object>> newHotList, List<String> keywords) {

        List<List<Map<String, Object>>> splitNewHotList = CollUtil.split(newHotList, 20);
        List<String> jsonBodys = new ArrayList<>();

        for (List<Map<String, Object>> subNewHots : splitNewHotList) {
            StringBuilder md = new StringBuilder("### 🍭糖果梦热榜🍭\n<br>");
            for (int i = 0; i < subNewHots.size(); i++) {
                Map<String, Object> hot = subNewHots.get(i);
                md.append(i + 1).append(". ")
                        .append("[").append(hot.get("keyword")).append("](")
                        .append(hot.get("url")).append(")")
                        .append("       --").append(hot.get("dataCardName"))
                        .append("<br>");
            }
            md.append("<br>");
            md.append("📱 共计：").append(subNewHots.size()).append(" 条<br>");
            md.append("📰 订阅：").append(String.join(", ", keywords)).append("<br>");
            md.append("⏰ 时间：").append(TimeUtil.getCurrentTimeFormat(TimeUtil.defaultPattern)).append("<br>");
            md.append("🙋🏻‍♂️ 来源：").append("[糖果梦热榜：https://tgmeng.com](https://tgmeng.com)<br>");
            jsonBodys.add(md.toString());
        }
        return jsonBodys;
    }

    public List<String> getPostBody(List<String> hotContent) {
        try {
            List<String> postBodys = new ArrayList<>();
            for (String subHotContent : hotContent) {
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode root = mapper.createObjectNode();
                root.put("msgtype", "markdown");
                ObjectNode markdown = root.putObject("markdown");
                markdown.put("title", "实时热榜");
                markdown.put("text", subHotContent);
                postBodys.add(mapper.writeValueAsString(root));
            }
            return postBodys;

        } catch (Exception e) {
            throw new ServerException("组装请求postBody失败");
        }
    }

    public void sendPost(String webHook, List<String> postJsonBodys,Integer count) {
        for (String postJsonBody : postJsonBodys) {
            iWebHookClient.sendMessage(webHook, postJsonBody);
        }
        umamiUtil.sendEvent(SubscriptionChannelTypeEnum.DINGDING.getDescription(), count);
    }
}
