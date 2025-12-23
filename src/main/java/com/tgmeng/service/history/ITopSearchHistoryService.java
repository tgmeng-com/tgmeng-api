package com.tgmeng.service.history;

import com.tgmeng.common.bean.ResultTemplateBean;

import java.util.Map;

public interface ITopSearchHistoryService {
    ResultTemplateBean getHotPointHistory(Map<String, String> requestBody);

    ResultTemplateBean getSuddenHeatPoint(String type);

    ResultTemplateBean getWordHistory(Map<String, String> requestBody);
}
