package com.tgmeng.service.license.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgmeng.common.aop.context.LicenseContext;
import com.tgmeng.common.bean.LicenseBean;
import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.config.LicenseRateLimiter;
import com.tgmeng.common.enums.business.LicenseFeatureEnum;
import com.tgmeng.common.enums.exception.ServerExceptionEnum;
import com.tgmeng.common.exception.ServerException;
import com.tgmeng.common.util.FileUtil;
import com.tgmeng.common.util.LicenseUtil;
import com.tgmeng.common.util.StringUtil;
import com.tgmeng.common.util.TimeUtil;
import com.tgmeng.service.license.ILicenseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LicenseServiceImpl implements ILicenseService {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private LicenseUtil licenseUtil;

    @Autowired
    private LicenseRateLimiter licenseRateLimiter;

    /** 校验入口 */
    @Override
    public void check(String licenseCode, String machineId, LicenseFeatureEnum feature) {
        try {
            doCheck(licenseCode, machineId, feature);
        } catch (Exception e) {
            throw new ServerException(ServerExceptionEnum.LICENSE_CHECK_EXCEPTION, "授权码校验异常: " + e.getMessage(), null);
        }
    }

    // 核心校验逻辑
    private void doCheck(String licenseCode, String machineId, LicenseFeatureEnum feature) {
        LicenseBean licenseBean = licenseUtil.loadLicense(licenseCode);
        licenseUtil.checkStatus(licenseBean);
        licenseUtil.checkExpire(licenseBean);
        licenseUtil.checkBoundMachineCount(licenseBean, machineId);
        licenseUtil.checkFeatures(licenseBean, feature);
        licenseRateLimiter.checkQPM(licenseCode, licenseBean.getLimitPerMinute());
        licenseRateLimiter.checkQPD(licenseCode, licenseBean.getLimitPerDay());

        // 记录使用日志
        licenseUtil.log(licenseCode, machineId, feature);
    }

    @Override
    public ResultTemplateBean initLicenseFile(Map<String, Object> requestBody) {
        String adminPassword = System.getenv("ADMIN_PASSWORD");
        String password = requestBody.get("password").toString();
        if (adminPassword.equals(password)) {
            int count = Integer.parseInt(requestBody.get("count").toString());
            String expireTime = requestBody.get("expireTime").toString();
            StringUtil.validateTimeIsDefaultPattern(expireTime, TimeUtil.defaultPattern);
            List<String> featureStrList = (List<String>) requestBody.get("features");
            List<LicenseFeatureEnum> features = featureStrList.stream()
                    .map(LicenseFeatureEnum::valueOf)
                    .collect(Collectors.toList());
            List<String> newFileList = licenseUtil.initLicenseFile(count, expireTime, features);
            return ResultTemplateBean.success(newFileList);
        }
        return ResultTemplateBean.success("管理员密码无效");
    }

    @Override
    public ResultTemplateBean getLicenseConfig() {
        try {
            File file = new File(licenseUtil.getLicenseFilePathByCode(LicenseContext.getLicenseCode()));
            LicenseBean licenseBean = MAPPER.readValue(file, LicenseBean.class);
            log.info("获取密钥信息成功:{}", licenseBean);
            return ResultTemplateBean.success(licenseBean);
        } catch (Exception e) {
            log.info("获取密钥信息失败，密钥:{},失败信息:{}", LicenseContext.getLicenseCode(), e.getMessage());
            return ResultTemplateBean.success(null);
        }
    }

    @Override
    public ResultTemplateBean updateSubscriptionConfig(LicenseBean newLicenseBean) {
        try {
            File file = new File(licenseUtil.getLicenseFilePathByCode(LicenseContext.getLicenseCode()));
            LicenseBean licenseBean = MAPPER.readValue(file, LicenseBean.class);
            licenseBean.getSubscriptionGlobalKeywords().removeIf(s -> s == null || s.trim().isEmpty());
            // 全局关键词
            licenseBean.setSubscriptionGlobalKeywords(newLicenseBean.getSubscriptionGlobalKeywords());
            // 全局分类选择
            licenseBean.setSubscriptionGlobalCategories(newLicenseBean.getSubscriptionGlobalCategories());
            // 平台配置
            licenseBean.setSubscriptionPlatformConfigs(newLicenseBean.getSubscriptionPlatformConfigs());
            FileUtil.writeToFile(file, licenseBean);
            log.info("更新订阅配置成功:{}", newLicenseBean);
            return ResultTemplateBean.success(true);
        } catch (Exception e) {
            log.info("更新订阅配置失败，配置:{},失败信息:{}", newLicenseBean, e.getMessage());
            return ResultTemplateBean.success(null);
        }
    }
}
