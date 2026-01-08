package com.tgmeng.service.history;

import com.tgmeng.common.bean.ResultTemplateBean;

import java.util.Map;

public interface ITopSearchHistoryService {

    // 指纹匹配热点，韩明距离
    ResultTemplateBean getHotPointHistory(Map<String, Object> requestBody);

    ResultTemplateBean getSuddenHeatPoint(String type);

    // 模糊匹配热点，sql like
    ResultTemplateBean getWordHistory(Map<String, Object> requestBody);

    ResultTemplateBean mergeParquetByGlob(Map<String, String> requestBody);

    ResultTemplateBean customexcutesql(Map<String, String> requestBody);
}
