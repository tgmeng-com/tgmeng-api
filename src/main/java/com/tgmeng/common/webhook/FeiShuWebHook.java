package com.tgmeng.common.webhook;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgmeng.common.bean.SubscriptionBean;
import com.tgmeng.common.bean.WebHookFeiShuBean;
import com.tgmeng.common.enums.business.SubscriptionChannelTypeEnum;
import com.tgmeng.common.exception.ServerException;
import com.tgmeng.common.forest.client.webhook.IWebHookClient;
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
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FeiShuWebHook {
    @Autowired
    private IWebHookClient iWebHookClient;
    @Autowired
    private UmamiUtil umamiUtil;

    ObjectMapper mapper = new ObjectMapper();

    public void sendMessage(List<Map<String, Object>> newHotList, SubscriptionBean.PushConfig push, List<String> keywords) {
        String webHook = getWebHook(push);
        log.info("🎠开始推送飞书：{}条",newHotList.size());
        List<List<List<WebHookFeiShuBean.TagItem>>> content = getHotContent(newHotList, keywords);
        List<String> postJsonBody = getPostBody(push, content);
        sendPost(webHook, postJsonBody, newHotList.size());
    }

    public String getWebHook(SubscriptionBean.PushConfig push) {
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

    public String getSign(SubscriptionBean.PushConfig push, Long timestamp) {
        try {
            String secret = push.getSecret();
            String sign = "";
            if (secret != null && !secret.isEmpty()) {
                String stringToSign = timestamp + "\n" + secret;
                //使用HmacSHA256算法计算签名
                Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(new SecretKeySpec(stringToSign.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
                byte[] signData = mac.doFinal(new byte[]{});
                sign = new String(Base64.encodeBase64(signData));
            }
            return sign;
        } catch (Exception e) {
            throw new ServerException("获取飞书签名失败:" + e.getMessage());
        }
    }

    public List<List<List<WebHookFeiShuBean.TagItem>>> getHotContent(List<Map<String, Object>> newHotList, List<String> keywords) {
        List<List<Map<String, Object>>> splitNewHotList = CollUtil.split(newHotList, 20);

        List<List<List<WebHookFeiShuBean.TagItem>>> allContentRows = new ArrayList<>();

        for (List<Map<String, Object>> subNewHots : splitNewHotList) {
            List<List<WebHookFeiShuBean.TagItem>> contentRows = new ArrayList<>();
            for (int i = 0; i < subNewHots.size(); i++) {
                Map<String, Object> hot = subNewHots.get(i);
                List<WebHookFeiShuBean.TagItem> row = new ArrayList<>();
                row.add(getFeiShuWebhookItem("text", (i + 1) + ". ", ""));
                row.add(getFeiShuWebhookItem("a", hot.get("keyword").toString(), hot.get("url").toString()));
                row.add(getFeiShuWebhookItem("text", " -- " + hot.get("dataCardName"), ""));
                contentRows.add(row);
            }

            List<WebHookFeiShuBean.TagItem> emptyRow = new ArrayList<>();
            emptyRow.add(getFeiShuWebhookItem("text", "", ""));
            contentRows.add(emptyRow);

            // 底部信息
            List<WebHookFeiShuBean.TagItem> row1 = new ArrayList<>();
            row1.add(getFeiShuWebhookItem("text", "📱 共计：" + subNewHots.size() + " 条", ""));
            contentRows.add(row1);

            List<WebHookFeiShuBean.TagItem> row2 = new ArrayList<>();
            row2.add(getFeiShuWebhookItem("text", "📰 订阅：" + String.join(", ", keywords), ""));
            contentRows.add(row2);

            List<WebHookFeiShuBean.TagItem> row3 = new ArrayList<>();
            row3.add(getFeiShuWebhookItem("text", "⏰ 时间：" + TimeUtil.getCurrentTimeFormat(TimeUtil.defaultPattern), ""));
            contentRows.add(row3);

            List<WebHookFeiShuBean.TagItem> row4 = new ArrayList<>();
            row4.add(getFeiShuWebhookItem("text", "🙋🏻‍♂️ 来源：", ""));
            row4.add(getFeiShuWebhookItem("a", "糖果梦热榜", "https://tgmeng.com"));
            contentRows.add(row4);

            allContentRows.add(contentRows);
        }
        return allContentRows;
    }

    public List<String> getPostBody(SubscriptionBean.PushConfig push, List<List<List<WebHookFeiShuBean.TagItem>>> allContentRows) {
        try {

            List<String> result = new ArrayList<>();
            for (List<List<WebHookFeiShuBean.TagItem>> allContentRow : allContentRows) {
                WebHookFeiShuBean message = new WebHookFeiShuBean();
                long timestamp = System.currentTimeMillis() / 1000;
                message.setTimestamp(timestamp);
                message.setSign(getSign(push, timestamp));
                message.setMsgType("post");
                WebHookFeiShuBean.Content content = new WebHookFeiShuBean.Content();
                WebHookFeiShuBean.Post post = new WebHookFeiShuBean.Post();
                WebHookFeiShuBean.LangContent lang = new WebHookFeiShuBean.LangContent();
                lang.setTitle("🍭 糖果梦热榜 🍭");
                lang.setContent(allContentRow);
                post.setZh_cn(lang);
                content.setPost(post);
                message.setContent(content);
                result.add(mapper.writeValueAsString(message));
            }
            return result;
        } catch (Exception e) {
            throw new ServerException("飞书组装请求postBody失败:" + e.getMessage());
        }
    }

    public void sendPost(String webHook, List<String> postJsonBodys, Integer count) {
        for (String postJsonBody : postJsonBodys) {
            iWebHookClient.sendMessage(webHook, postJsonBody);
        }
        log.info("飞书成功推送：{}条", count);
        umamiUtil.sendEvent(SubscriptionChannelTypeEnum.FEISHU.getDescription(), count);
    }

    public WebHookFeiShuBean.TagItem getFeiShuWebhookItem(String tag, String text, String href) {
        WebHookFeiShuBean.TagItem blank = new WebHookFeiShuBean.TagItem();
        blank.setTag(tag);
        blank.setText(text);
        blank.setHref(href);
        return blank;
    }
}
