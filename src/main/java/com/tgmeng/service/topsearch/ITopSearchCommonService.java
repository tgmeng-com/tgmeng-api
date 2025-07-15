package com.tgmeng.service.topsearch;

import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.enums.business.SearchTypeBaiDuEnum;
import com.tgmeng.common.enums.business.SearchTypeWangYiYunEnum;


public interface ITopSearchCommonService {
    ResultTemplateBean getBaiDuTopSearch(SearchTypeBaiDuEnum searchTypeBaiDuEnum);

    ResultTemplateBean getBilibiliTopSearch();

    ResultTemplateBean getWeiBoTopSearch();

    ResultTemplateBean getDouYinTopSearch();

    ResultTemplateBean getDouBanTopSearch();

    ResultTemplateBean getTencentTopSearch();

    ResultTemplateBean getTouTiaoTopSearch();

    ResultTemplateBean getWangYiTopSearch();

    ResultTemplateBean getWangYiYunTopSearch(SearchTypeWangYiYunEnum searchTypeWangYiYunEnum);

    ResultTemplateBean getBaiDuTieBaSearch();

    ResultTemplateBean getShaoShuPaiSearch();

    ResultTemplateBean getZhiHuTopSearch();
}
