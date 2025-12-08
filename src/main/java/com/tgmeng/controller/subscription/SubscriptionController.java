package com.tgmeng.controller.subscription;

import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.bean.SubscriptionBean;
import com.tgmeng.service.subscription.ITopSearchSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/topsearch/subscription")
public class SubscriptionController {

    private final ITopSearchSubscriptionService topSearchCommonService;

    @RequestMapping("/initfile")
    public ResultTemplateBean initFile(@RequestBody Map<String, String> requestBody) {
        return topSearchCommonService.initSubscriptionFile(requestBody);
    }

    @RequestMapping("/getSubscriptionConfig")
    public ResultTemplateBean getSubscriptionConfig(@RequestBody SubscriptionBean subscriptionBean) {
        return topSearchCommonService.getSubscriptionConfig(subscriptionBean);
    }

    @RequestMapping("/updateSubscriptionConfig")
    public ResultTemplateBean updateSubscriptionConfig(@RequestBody SubscriptionBean subscriptionBean) {
        return topSearchCommonService.updateSubscriptionConfig(subscriptionBean);
    }
}
