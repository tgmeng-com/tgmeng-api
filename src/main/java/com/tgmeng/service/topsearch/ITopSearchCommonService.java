package com.tgmeng.service.topsearch;

import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.enums.business.SearchTypeBaiDuEnum;


public interface ITopSearchCommonService {
    ResultTemplateBean getBaiDuTopSearch(SearchTypeBaiDuEnum searchTypeBaiDuEnum);

    ResultTemplateBean getBilibiliTopSearch();

    ResultTemplateBean getWeiBoTopSearch();

    ResultTemplateBean getDouYinTopSearch();

    ResultTemplateBean getDouBanTopSearch();

    ResultTemplateBean getTencentTopSearch();

    ResultTemplateBean getTouTiaoTopSearch();

    ResultTemplateBean getWangYiTopSearch();

    ResultTemplateBean getWangYiYunTopSearch();

    ResultTemplateBean getBaiDuTieBaSearch();

    ResultTemplateBean getShaoShuPaiSearch();

}
