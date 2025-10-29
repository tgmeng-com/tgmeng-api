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

    ResultTemplateBean getNewYueShiBaoSearch();

    ResultTemplateBean getBBCSearch();

    ResultTemplateBean getFaGuangSearch();

    ResultTemplateBean getHuaErJieRiBaoSearch();

    ResultTemplateBean getDaJiYuanSearch();

    ResultTemplateBean getWoShiPMSearch();

    ResultTemplateBean getYouSheWangSearch();

    ResultTemplateBean getZhanKuSearch(SearchTypeZhanKuEnum searchTypeZhanKuEnum);

    ResultTemplateBean getZhanTuYaWangGuoSearch(SearchTypeTuYaWangGuoEnum searchTypeTuYaWangGuoEnum);

    ResultTemplateBean getSheJiDaRenSearch();

    ResultTemplateBean getTopysSearch();

    ResultTemplateBean getArchDailySearch();

    ResultTemplateBean getDribbbleSearch();

    ResultTemplateBean getAwwwardsSearch();

    ResultTemplateBean getCore77Search();

    ResultTemplateBean getAbduzeedoSearch();

    ResultTemplateBean getMITSearch();

    ResultTemplateBean getZhongGuoKeXueYuanSearch();

    ResultTemplateBean getEurekAlertSearch();

    ResultTemplateBean getGuoJiKeJiChuangXinZhongXinSearch(SearchTypeGuoJiKeJiChuangXinZhongXinnum searchTypeGuoJiKeJiChuangXinZhongXinnum);

    ResultTemplateBean getJiQiZhiXinSearch();
}
