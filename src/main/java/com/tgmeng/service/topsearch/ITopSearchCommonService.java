package com.tgmeng.service.topsearch;

import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.enums.business.*;


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

    ResultTemplateBean getTengXunShiPinTopSearch(SearchTypeTengXunShiPinEnum searchTypeTengXunShiPinEnum);

    ResultTemplateBean getAiQiYiTopSearch(SearchTypeAiQiYiEnum searchTypeAiQiYiEnum);

    ResultTemplateBean getYouKuTopSearch(SearchTypeYouKuEnum searchTypeYouKuEnum);

    ResultTemplateBean getMangGuoTopSearch(SearchTypeMangGuoEnum searchTypeMangGuoEnum);

    ResultTemplateBean getMaoYanTopSearch(SearchTypeMaoYanEnum searchTypeMaoYanEnum);

    ResultTemplateBean getJinRongJieSearch();

    ResultTemplateBean getDiYiCaiJingSearch();

    ResultTemplateBean getTongHuaShunSearch();

    ResultTemplateBean getHuaErJieJianWenSearch();

    ResultTemplateBean getCaiLianSheSearch();

    ResultTemplateBean getGeLongHuiSearch();

    ResultTemplateBean getFaBuSearch();

    ResultTemplateBean getJinShiSearch();
}
