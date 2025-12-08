package com.tgmeng.service.subscription.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.bean.SubscriptionBean;
import com.tgmeng.common.util.FileUtil;
import com.tgmeng.common.util.SubscriptionUtil;
import com.tgmeng.service.subscription.ITopSearchSubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ITopSearchSubscriptionServiceImpl implements ITopSearchSubscriptionService {

    @Autowired
    private SubscriptionUtil subscriptionUtil;

    @Value("${my-config.subscription.dir}")
    private String subscriptionDir;
    @Value("${my-config.subscription.file-suffix}")
    private String fileSuffix;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public ResultTemplateBean initSubscriptionFile(Map<String, String> requestBody) {
        String initSubscriptionFilePassword = System.getenv("SUBSCRIPTION_FILE_INIT_PASSWORD");
        String password = requestBody.get("password");
        if (initSubscriptionFilePassword.equals(password)) {
            List<String> newFileList = subscriptionUtil.generateSubscriptionFile(Integer.parseInt(requestBody.get("count")));
            return ResultTemplateBean.success(newFileList);
        }
        return new ResultTemplateBean();
    }

    public Boolean checkAccessKey(SubscriptionBean subscriptionBean) {
        String accessKey = subscriptionBean.getAccessKey();
        String regex = "^[A-Z0-9]{4}-[A-Z0-9]{4}-[A-Z0-9]{4}-[A-Z0-9]{4}-[A-Z0-9]{4}-[A-Z0-9]{4}$";
        if (accessKey != null && !accessKey.matches(regex)) {
            log.info("订阅密钥无效:{}", accessKey);
            return false;
        }
        List<String> allFileNamesInPath = FileUtil.getAllFileNamesInPath(subscriptionDir);
        if (allFileNamesInPath.contains(subscriptionBean.getAccessKey() + fileSuffix)) {
            log.info("订阅密钥有效:{}", accessKey);
            return true;
        } else {
            log.info("订阅密钥无效:{}", accessKey);
            return false;
        }
    }

    @Override
    public ResultTemplateBean getSubscriptionConfig(SubscriptionBean newSubscriptionBean) {
        if (!checkAccessKey(newSubscriptionBean)) {
            return ResultTemplateBean.success(null);
        }
        try {
            File file = new File(subscriptionDir + newSubscriptionBean.getAccessKey() + fileSuffix);
            SubscriptionBean subscriptionBean = MAPPER.readValue(file, SubscriptionBean.class);
            Map<String, Object> map = new HashMap<>();
            map.put("accessKey", subscriptionBean.getAccessKey());
            map.put("keywords", subscriptionBean.getKeywords());
            map.put("platforms", subscriptionBean.getPlatforms());
            log.info("获取订阅配置成功:{}", map);
            return ResultTemplateBean.success(map);
        } catch (Exception e) {
            log.info("获取订阅配置失败，密钥:{},失败信息:{}", newSubscriptionBean.getAccessKey(), e.getMessage());
            return ResultTemplateBean.success(null);
        }
    }

    @Override
    public ResultTemplateBean updateSubscriptionConfig(SubscriptionBean newSubscriptionBean) {
        if (!checkAccessKey(newSubscriptionBean)) {
            return ResultTemplateBean.success(null);
        }
        try {
            File file = new File(subscriptionDir + newSubscriptionBean.getAccessKey() + fileSuffix);
            SubscriptionBean subscriptionBean = MAPPER.readValue(file, SubscriptionBean.class);
            newSubscriptionBean.getKeywords().removeIf(s -> s == null || s.trim().isEmpty());
            subscriptionBean.setKeywords(newSubscriptionBean.getKeywords());
            subscriptionBean.setPlatforms(newSubscriptionBean.getPlatforms());
            FileUtil.writeToFile(file, subscriptionBean);
            log.info("更新订阅配置成功:{}", newSubscriptionBean);
            return ResultTemplateBean.success(true);
        } catch (Exception e) {
            log.info("更新订阅配置失败，配置:{},失败信息:{}", newSubscriptionBean, e.getMessage());
            return ResultTemplateBean.success(null);
        }
    }
}
