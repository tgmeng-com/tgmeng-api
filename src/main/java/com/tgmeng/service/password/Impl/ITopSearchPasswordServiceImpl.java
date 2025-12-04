package com.tgmeng.service.password.Impl;

import cn.hutool.core.util.StrUtil;
import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.config.AIPlatformConfigService;
import com.tgmeng.service.password.ITopSearchPasswordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ITopSearchPasswordServiceImpl implements ITopSearchPasswordService {

    @Autowired
    private AIPlatformConfigService aiPlatformConfigService;

    @Override
    public ResultTemplateBean getAdsPassword(Map<String, String> requestBody) {
        String googleAdsClosePassword = aiPlatformConfigService.getGoogleAdsClosePassword();
        String password = requestBody.get("password");
        if (StrUtil.equals(password, googleAdsClosePassword)) {
            return ResultTemplateBean.success(true);
        }else {
            return ResultTemplateBean.success(false);
        }
    }
}
