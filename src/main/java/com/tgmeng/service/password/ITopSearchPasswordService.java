package com.tgmeng.service.password;

import com.tgmeng.common.bean.ResultTemplateBean;

import java.util.Map;

public interface ITopSearchPasswordService {
    ResultTemplateBean getAdsPassword(Map<String, String> requestBody);
}
