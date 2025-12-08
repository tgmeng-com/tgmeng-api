package com.tgmeng.service.cachesearch;

import com.tgmeng.common.bean.ResultTemplateBean;

import java.util.List;
import java.util.Map;

public interface ICacheSearchService {

    ResultTemplateBean<List<Map<String, Object>>> getCacheSearchAllByWord(String word, List<String> words);

    ResultTemplateBean getCacheSearchWordCloud();

    ResultTemplateBean getCacheSearchRealTimeSummary();
}
