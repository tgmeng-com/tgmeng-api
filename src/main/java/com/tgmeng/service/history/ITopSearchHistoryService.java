package com.tgmeng.service.history;

import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.model.vo.topsearch.TopSearchCommonVO;

import java.util.Map;

public interface ITopSearchHistoryService {
    ResultTemplateBean<TopSearchCommonVO> getHotPointHistory(Map<String, String> requestBody);

    ResultTemplateBean getSuddenHeatPoint(String type);

}
