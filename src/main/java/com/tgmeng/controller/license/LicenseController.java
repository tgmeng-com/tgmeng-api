package com.tgmeng.controller.license;

import com.tgmeng.common.annotation.LicenseRequired;
import com.tgmeng.common.bean.LicenseBean;
import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.enums.business.LicenseFeatureEnum;
import com.tgmeng.service.license.ILicenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/topsearch/license")
public class LicenseController {

    private final ILicenseService licenseService;

    // 新增初始化授权码
    @RequestMapping("/addlicensecode")
    public ResultTemplateBean initFile(@RequestBody Map<String, Object> requestBody) {
        return licenseService.initLicenseFile(requestBody);
    }

    // 查询密钥基本信息
    @RequestMapping("/getLicenseConfig")
    @LicenseRequired(feature = LicenseFeatureEnum.BASIC)
    public ResultTemplateBean getLicenseConfig() {
        return licenseService.getLicenseConfig();
    }

    // 查询订阅配置信息
    @RequestMapping("/getSubscriptionConfig")
    @LicenseRequired(feature = LicenseFeatureEnum.SUBSCRIPTION)
    public ResultTemplateBean getSubscriptionConfig() {
        return licenseService.getLicenseConfig();
    }

    // 更新订阅推送配置
    @RequestMapping("/updateSubscriptionConfig")
    @LicenseRequired(feature = LicenseFeatureEnum.SUBSCRIPTION)
    public ResultTemplateBean updateSubscriptionConfig(@RequestBody LicenseBean licenseBean) {
        return licenseService.updateSubscriptionConfig(licenseBean);
    }
}
