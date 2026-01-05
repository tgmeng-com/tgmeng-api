package com.tgmeng.service.cachesearch;

import com.tgmeng.common.bean.ResultTemplateBean;

import java.util.List;
import java.util.Map;

public interface ICacheSearchService {

    ResultTemplateBean<List<Map<String, Object>>> searchByWord(Map<String, String> requestBody);

    ResultTemplateBean getCacheSearchWordCloud();

    ResultTemplateBean getCacheSearchRealTimeSummary();

    ResultTemplateBean getTgmengHotSearch();
}
