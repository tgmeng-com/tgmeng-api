package com.tgmeng.service.license;

import com.tgmeng.common.bean.LicenseBean;
import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.enums.business.LicenseFeatureEnum;

import java.util.Map;

public interface ILicenseService {

    void check(String licenseCode, String machineId, LicenseFeatureEnum feature);

    ResultTemplateBean initLicenseFile(Map<String, Object> requestBody);

    ResultTemplateBean getLicenseConfig();

    ResultTemplateBean updateSubscriptionConfig(LicenseBean licenseBean);
}
