package com.tgmeng.service.cachesearch;

import com.tgmeng.common.bean.ResultTemplateBean;

public interface ICacheSearchService {

    ResultTemplateBean getCacheSearchAllByWord(String word);

    ResultTemplateBean getCacheSearchWordCloud();
}
