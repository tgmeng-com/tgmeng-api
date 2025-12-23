package com.tgmeng.common.webhook;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.StrUtil;
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
public class NtfyWebHook {
    @Autowired
    private IWebHookClient iWebHookClient;
    @Autowired
    private UmamiUtil umamiUtil;

    public void sendMessage(List<Map<String, Object>> newHotList, SubscriptionBean.PushConfig push, List<String> keywords,String accessKey) {
        StopWatch stopWatch = new StopWatch(accessKey);
        stopWatch.start();
        String webHook = getWebHook(push);
        log.info("🎠 开始推送NTFY：{}条，accessKey: {}", newHotList.size(),accessKey);
        List<String> content = getHotContent(newHotList, keywords);
        List<String> postJsonBody = getPostBody(content);
        sendPost(webHook, postJsonBody, newHotList.size(),accessKey);
        stopWatch.stop();
        log.info("🎉 NTFY成功推送：{}条，accessKey: {},耗时: {} ms", newHotList.size(),accessKey, stopWatch.getTotalTimeMillis());
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
            StringBuilder md = new StringBuilder("### 🍭 糖果梦热榜 🍭\n");
            for (int i = 0; i < subNewHots.size(); i++) {
                Map<String, Object> hot = subNewHots.get(i);
                md.append(i + 1).append(". ")
                        .append("[").append(hot.get("title")).append("](")
                        .append(hot.get("url")).append(")")
                        .append("       --").append(hot.get("platformName"))
                        .append("\n");
            }
            md.append("\n");
            md.append("📱 共计：").append(subNewHots.size()).append(" 条\n");
            md.append("📰 订阅：").append(String.join(", ", keywords)).append("\n");
            md.append("⏰ 时间：").append(TimeUtil.getCurrentTimeFormat(TimeUtil.defaultPattern)).append("\n");
            md.append("🙋🏻‍♂️ 来源：").append("[糖果梦热榜：https://tgmeng.com](https://tgmeng.com)");
            jsonBodys.add(md.toString());
        }
        return jsonBodys;
    }

    public List<String> getPostBody(List<String> hotContent) {
        try {
            return hotContent;

        } catch (Exception e) {
            throw new ServerException("NTFY组装请求postBody失败:" + e.getMessage());
        }
    }

    public void sendPost(String webHook, List<String> postJsonBodys, Integer count,String accessKey) {
        for (String postJsonBody : postJsonBodys) {
            ForestRequestHeader forestRequestHeader = new ForestRequestHeader()
                    .setContentType("application/json;charset=UTF-8")
                    .setMarkdown("yes");
            iWebHookClient.sendMessage(forestRequestHeader, webHook, postJsonBody);
        }
        umamiUtil.sendEvent(SubscriptionChannelTypeEnum.NTFY.getDescription(), count);
    }
}
