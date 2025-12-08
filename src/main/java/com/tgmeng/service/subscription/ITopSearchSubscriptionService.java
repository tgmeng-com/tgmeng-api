package com.tgmeng.service.subscription;

import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.bean.SubscriptionBean;

import java.util.Map;

public interface ITopSearchSubscriptionService {
    ResultTemplateBean initSubscriptionFile(Map<String, String> requestBody);

    ResultTemplateBean getSubscriptionConfig(SubscriptionBean subscriptionBean);

    ResultTemplateBean updateSubscriptionConfig(SubscriptionBean subscriptionBean);
}
