package com.tgmeng.service.topsearch;

import com.tgmeng.common.bean.ResultTemplateBean;


public interface ITopSearchCommonService {
    ResultTemplateBean getBaiDuTopSearch();

    ResultTemplateBean getBilibiliTopSearch();

    ResultTemplateBean getWeiBoTopSearch();

    ResultTemplateBean getDouYinTopSearch();
}
