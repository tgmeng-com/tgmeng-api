package com.tgmeng.common.webhook;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgmeng.common.bean.SubscriptionBean;
import com.tgmeng.common.enums.business.SubscriptionChannelTypeEnum;
import com.tgmeng.common.exception.ServerException;
import com.tgmeng.common.forest.client.webhook.IWebHookClient;
import com.tgmeng.common.util.TimeUtil;
import com.tgmeng.common.util.UmamiUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TelegramWebHook {
    @Autowired
    private IWebHookClient iWebHookClient;
    @Autowired
    private UmamiUtil umamiUtil;

    ObjectMapper mapper = new ObjectMapper();

    public void sendMessage(List<Map<String, Object>> newHotList, SubscriptionBean.PushConfig push,  List<String> keywords) {
        String webHook = getWebHook(push);
        log.info("🎠开始推送Telegram");
        List<String> content = getHotContent(newHotList, keywords);
        List<String> postJsonBody = getPostBody(content,push);
        sendPost(webHook, postJsonBody,newHotList.size());
    }

    public String getWebHook(SubscriptionBean.PushConfig push) {
        try {
            String webhook = "https://api.telegram.org/bot" + push.getWebhook() + "/sendMessage";
            if (StrUtil.isNotBlank(webhook) && StrUtil.isNotBlank(push.getSecret())) {
                return webhook;
            }else {
                throw new ServerException("webHook配置无效");
            }
        } catch (Exception e) {
            throw new ServerException("webHook配置无效");
        }
    }

    public List<String> getHotContent(List<Map<String, Object>> newHotList, List<String> keywords) {

        List<List<Map<String, Object>>> splitNewHotList = CollUtil.split(newHotList, 20);
        List<String> jsonBodys = new ArrayList<>();

        for (List<Map<String, Object>> subNewHots : splitNewHotList) {
            StringBuilder md = new StringBuilder();
            md.append("<b>🍭 糖果梦热榜 🍭</b>\n\n");
            for (int i = 0; i < subNewHots.size(); i++) {
                Map<String, Object> hot = subNewHots.get(i);
                String keyword = hot.get("keyword").toString();
                String url = hot.get("url").toString();
                String source = hot.get("dataCardName").toString();

                md.append(i + 1)
                        .append(". <a href=\"").append(url).append("\">")
                        .append(keyword)
                        .append("</a>  — ").append(source)
                        .append("\n");
            }
            md.append("\n");
            md.append("📱 共计：").append(subNewHots.size()).append(" 条\n");
            md.append("📰 订阅：").append(String.join(", ", keywords)).append("\n");
            md.append("⏰ 时间：").append(TimeUtil.getCurrentTimeFormat(TimeUtil.defaultPattern)).append("\n");
            md.append("🙋🏻‍♂️ 来源：<a href=\"https://tgmeng.com\">糖果梦热榜：https://tgmeng.com</a>");
            jsonBodys.add(md.toString());
        }
        return jsonBodys;
    }

    public List<String> getPostBody(List<String> hotContent,SubscriptionBean.PushConfig push) {
        try {
            List<String> postBodys = new ArrayList<>();
            for (String subHotContent : hotContent) {
                Map<String, Object> req = new HashMap<>();
                req.put("chat_id", push.getSecret());
                req.put("text", subHotContent);
                req.put("parse_mode", "HTML");
                postBodys.add(mapper.writeValueAsString(req));
            }
            return postBodys;
        } catch (Exception e) {
            throw new ServerException("Telegram组装请求postBody失败");
        }
    }

    public void sendPost(String webHook, List<String> postJsonBodys,Integer count) {
        for (String postJsonBody : postJsonBodys) {
            iWebHookClient.sendMessage(webHook, postJsonBody);
        }
        umamiUtil.sendEvent(SubscriptionChannelTypeEnum.TELEGRAM.getDescription(), count);
    }
}
