package com.tgmeng.service.topsearch;

import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.enums.business.BaiDuSearchTypeEnum;


public interface ITopSearchCommonService {
    ResultTemplateBean getBaiDuTopSearch(BaiDuSearchTypeEnum baiDuSearchTypeEnum);

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
