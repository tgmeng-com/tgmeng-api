package com.tgmeng.service.topsearch;

import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.enums.business.SearchTypeHuggingFaceEnum;

public interface ITopSearchGlobalService {


    ResultTemplateBean getYoutubeSortByTopSearch();

    ResultTemplateBean getHuggingFaceTopSearch(SearchTypeHuggingFaceEnum searchTypeHuggingFaceEnum);
}
