package com.tgmeng.common.webhook;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgmeng.common.bean.SubscriptionBean;
import com.tgmeng.common.enums.business.SubscriptionChannelTypeEnum;
import com.tgmeng.common.exception.ServerException;
import com.tgmeng.common.forest.client.webhook.IWebHookClient;
import com.tgmeng.common.forest.header.ForestRequestHeader;
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
public class GotifyWebHook {

    // TODO 这个里面推送的数据格式还没有确认是否完全正确，所以先不做。

    @Autowired
    private IWebHookClient iWebHookClient;
    @Autowired
    private UmamiUtil umamiUtil;

    ObjectMapper MAPPER = new ObjectMapper();

    public void sendMessage(List<Map<String, Object>> newHotList, SubscriptionBean.PushConfig push, List<String> keywords) {
        String webHook = getWebHook(push);
        log.info("🎠开始推送Gotify：{}条", newHotList.size());
        List<String> content = getHotContent(newHotList, keywords);
        List<String> postJsonBody = getPostBody(content);
        sendPost(webHook, postJsonBody, newHotList.size());
    }

    public String getWebHook(SubscriptionBean.PushConfig push) {
        try {
            String webhook = push.getWebhook();
            if (StrUtil.isNotBlank(webhook)) {
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
            StringBuilder md = new StringBuilder("### 🍭 糖果梦热榜 🍭\n<br>");
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

                Map<String,Object> postbody = new HashMap();
                postbody.put("message", subHotContent);
                postbody.put("title", "糖果梦热榜");
                postBodys.add(MAPPER.writeValueAsString(postbody));
            }
            return postBodys;

        } catch (Exception e) {
            throw new ServerException("Gotify组装请求postBody失败:" + e.getMessage());
        }
    }

    public void sendPost(String webHook, List<String> postJsonBodys, Integer count) {
        for (String postJsonBody : postJsonBodys) {
            ForestRequestHeader forestRequestHeader = new ForestRequestHeader().setContentType("application/json;charset=UTF-8");
            iWebHookClient.sendMessage(forestRequestHeader, webHook, postJsonBody);
        }
        log.info("Gotify成功推送：{}条", count);
        umamiUtil.sendEvent(SubscriptionChannelTypeEnum.GOTIFY.getDescription(), count);
    }
}
