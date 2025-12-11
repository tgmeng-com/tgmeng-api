package com.tgmeng.common.util;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.RandomUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgmeng.common.bean.UmamiPostDataBean;
import com.tgmeng.common.forest.client.umami.IUmamiClient;
import com.tgmeng.common.forest.header.ForestRequestHeader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UmamiUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private IUmamiClient umamiClient;

    // 事件名和数据值
    public void sendEvent(String metricName, Integer value) {
        StopWatch stopWatch = new StopWatch(metricName + value + RandomUtil.randomString(10));
        stopWatch.start();
        UmamiPostDataBean umamiPostEventData = getUmamiPostEventData(metricName, value);
        ForestRequestHeader forestRequestHeader = new ForestRequestHeader();
        forestRequestHeader.setUserAgent(HttpRequestUtil.getRequestRandomUserAgent());
        umamiClient.sendEvent(forestRequestHeader, "https://umaminew.tgmeng.com/api/send", umamiPostEventData);
        stopWatch.stop();
        log.info("Umami统计数据发送完毕:{}，耗时：{} ms", metricName, stopWatch.getTotalTimeMillis());
    }

    // 拼装请求数据体
    private UmamiPostDataBean getUmamiPostEventData(String metricName, Integer value) {
        UmamiPostDataBean umamiPostDataBean = new UmamiPostDataBean();
        UmamiPostDataBean.Payload payload = new UmamiPostDataBean.Payload();
        payload.setName(metricName);
        payload.setWebsite(System.getenv("UMAMI_WEBSITE_TGMENG_COM"));

        UmamiPostDataBean.DataInfo data = new UmamiPostDataBean.DataInfo();
        data.setValue(value);

        payload.setData(data);
        umamiPostDataBean.setPayload(payload);
        return umamiPostDataBean;
    }
}
