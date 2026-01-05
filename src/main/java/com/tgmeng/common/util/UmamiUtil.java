package com.tgmeng.common.util;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.RandomUtil;
import com.tgmeng.common.bean.UmamiPostDataBean;
import com.tgmeng.common.forest.client.umami.IUmamiClient;
import com.tgmeng.common.forest.header.ForestRequestHeader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UmamiUtil {

    @Value("${my-config.umami-website-tgmeng-com}")
    private String umamiWebsiteTgmengCom;

    @Autowired
    private IUmamiClient umamiClient;

    // 事件名和数据值
    public void sendEvent(String eventName, String metricName, Integer value, String targetUser) {
        StopWatch stopWatch = new StopWatch(metricName + value + RandomUtil.randomString(10));
        stopWatch.start();
        UmamiPostDataBean umamiPostEventData = getUmamiPostEventData(eventName, metricName, value, targetUser);
        ForestRequestHeader forestRequestHeader = new ForestRequestHeader();
        forestRequestHeader.setUserAgent(HttpRequestUtil.getRequestRandomUserAgent());
        umamiClient.sendEvent(forestRequestHeader, "https://umaminew.tgmeng.com/api/send", umamiPostEventData);
        stopWatch.stop();
        log.info("Umami统计数据发送完毕: {}，耗时: {} ms", metricName, stopWatch.getTotalTimeMillis());
    }

    // 拼装请求数据体
    private UmamiPostDataBean getUmamiPostEventData(String eventName, String metricName, Integer value, String targetUser) {
        UmamiPostDataBean umamiPostDataBean = new UmamiPostDataBean();
        UmamiPostDataBean.Payload payload = new UmamiPostDataBean.Payload();
        payload.setName(eventName);
        payload.setWebsite(umamiWebsiteTgmengCom);

        UmamiPostDataBean.DataInfo data = new UmamiPostDataBean.DataInfo();
        data.setValue(value);
        data.setPlatForm(metricName);
        data.setTargetUser(targetUser);

        payload.setData(data);
        umamiPostDataBean.setPayload(payload);
        return umamiPostDataBean;
    }
}
