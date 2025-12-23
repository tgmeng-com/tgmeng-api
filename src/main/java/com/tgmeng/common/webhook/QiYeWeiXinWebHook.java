package com.tgmeng.common.webhook;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class QiYeWeiXinWebHook {
    @Autowired
    private IWebHookClient iWebHookClient;
    @Autowired
    private UmamiUtil umamiUtil;

    public void sendMessage(List<Map<String, Object>> newHotList, SubscriptionBean.PushConfig push, List<String> keywords,String accessKey) {
        StopWatch stopWatch = new StopWatch(accessKey);
        stopWatch.start();
        String webHook = getWebHook(push);
        String contentType = push.getSecret();
        log.info("🎠 开始推送企业微信：{}条，accessKey: {}", newHotList.size(),accessKey);
        List<String> content = getHotContent(newHotList, keywords, contentType);
        List<String> postJsonBody = getPostBody(content, contentType);
        sendPost(webHook, postJsonBody, newHotList.size(),accessKey);
        stopWatch.stop();
        log.info("🎉 企业微信成功推送：{}条，accessKey: {},耗时: {} ms", newHotList.size(),accessKey, stopWatch.getTotalTimeMillis());
    }

    public String getWebHook(SubscriptionBean.PushConfig push) {
        try {
            return push.getWebhook();
        } catch (Exception e) {
            throw new ServerException("webHook配置无效:" + e.getMessage());
        }
    }

    public List<String> getHotContent(List<Map<String, Object>> newHotList, List<String> keywords, String contentType) {

        List<List<Map<String, Object>>> splitNewHotList = CollUtil.split(newHotList, 20);
        List<String> jsonBodys = new ArrayList<>();

        for (List<Map<String, Object>> subNewHots : splitNewHotList) {
            if (StrUtil.equals(contentType, "markdown")) {
                StringBuilder md = new StringBuilder("## 🍭 糖果梦热榜 🍭\n\n\n");
                for (int i = 0; i < subNewHots.size(); i++) {
                    Map<String, Object> hot = subNewHots.get(i);
                    md.append(i + 1).append(". ")
                            .append("[").append(hot.get("title")).append("](")
                            .append(hot.get("url")).append(")")
                            .append("       --").append(hot.get("platformName"))
                            .append("\n\n");
                }
                md.append("\n");
                md.append("📱 共计：").append(subNewHots.size()).append(" 条\n");
                md.append("📰 订阅：").append(String.join(", ", keywords)).append("\n");
                md.append("⏰ 时间：").append(TimeUtil.getCurrentTimeFormat(TimeUtil.defaultPattern)).append("\n");
                md.append("🙋🏻‍♂️ 来源：").append("[糖果梦热榜：https://tgmeng.com](https://tgmeng.com)\n");
                jsonBodys.add(md.toString());
            } else {
                StringBuilder md = new StringBuilder("🍭 糖果梦热榜 🍭\n\n");
                for (int i = 0; i < subNewHots.size(); i++) {
                    Map<String, Object> hot = subNewHots.get(i);
                    md.append(i + 1).append(". ")
                            .append("").append(hot.get("title")).append("(")
                            .append(hot.get("url")).append(")")
                            .append("       --").append(hot.get("platformName"))
                            .append("\n");
                }
                md.append("\n");
                md.append("📱 共计：").append(subNewHots.size()).append(" 条\n");
                md.append("📰 订阅：").append(String.join(", ", keywords)).append("\n");
                md.append("⏰ 时间：").append(TimeUtil.getCurrentTimeFormat(TimeUtil.defaultPattern)).append("\n");
                md.append("🙋🏻‍♂️ 来源：").append("糖果梦热榜：https://tgmeng.com");
                jsonBodys.add(md.toString());
            }
        }
        return jsonBodys;
    }

    public List<String> getPostBody(List<String> hotContent, String contentType) {
        try {
            List<String> postBodys = new ArrayList<>();
            for (String subHotContent : hotContent) {
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode root = mapper.createObjectNode();
                ObjectNode objectNodeContent;
                if (StrUtil.equals(contentType, "markdown")) {
                    root.put("msgtype", "markdown");
                    objectNodeContent = root.putObject("markdown");
                } else {
                    root.put("msgtype", "text");
                    objectNodeContent = root.putObject("text");
                }
                objectNodeContent.put("content", subHotContent);
                postBodys.add(mapper.writeValueAsString(root));
            }
            return postBodys;
        } catch (Exception e) {
            throw new ServerException("钉钉组装请求postBody失败:" + e.getMessage());
        }
    }

    public void sendPost(String webHook, List<String> postJsonBodys, Integer count,String accessKey) {
        for (String postJsonBody : postJsonBodys) {
            ForestRequestHeader forestRequestHeader = new ForestRequestHeader().setContentType("application/json;charset=UTF-8");
            iWebHookClient.sendMessage(forestRequestHeader, webHook, postJsonBody);
        }
        umamiUtil.sendEvent(SubscriptionChannelTypeEnum.QIYEWEIXIN.getDescription(), count);
    }
}
