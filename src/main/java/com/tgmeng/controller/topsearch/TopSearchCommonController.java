package com.tgmeng.controller.topsearch;

import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.enums.business.SearchTypeBaiDuEnum;
import com.tgmeng.common.enums.business.SearchTypeWangYiYunEnum;
import com.tgmeng.service.topsearch.ITopSearchCommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description: 社交媒体
 * package: com.tgmeng.controller.topsearch
 * className: TopSearchCommonController
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/6/29 0:29
*/
@RestController
@RequiredArgsConstructor
@RequestMapping("/topsearch")
public class TopSearchCommonController {

    private final ITopSearchCommonService topSearchCommonService;
    /**
     * description: 百度热搜
     * method: getBaiDuTopSearch
     *
     * @author tgmeng
     * @since 2025/6/29 0:52
    */
    @RequestMapping("/baidu")
    public ResultTemplateBean getBaiDuTopSearch() {
        return topSearchCommonService.getBaiDuTopSearch(SearchTypeBaiDuEnum.NEWS_BAIDU);
    }
    /**
     * description: B站热搜
     * method: getBilibiliTopSearch
     *
     * @author tgmeng
     * @since 2025/6/29 15:32
    */
    @RequestMapping("/bilibili")
    public ResultTemplateBean getBilibiliTopSearch() {
        return topSearchCommonService.getBilibiliTopSearch();
    }

    /**
     * description: 微博热搜
     * method: getWeiBoTopSearch
     *
     * @author tgmeng
     * @since 2025/6/29 18:49
    */
    @RequestMapping("/weibo")
    public ResultTemplateBean getWeiBoTopSearch() {
        return topSearchCommonService.getWeiBoTopSearch();
    }

    /**
     * description: 抖音热搜
     * method: getDouYinTopSearch
     *
     * @author tgmeng
     * @since 2025/6/29 22:39
    */
    @RequestMapping("/douyin")
    public ResultTemplateBean getDouYinTopSearch() {
        return topSearchCommonService.getDouYinTopSearch();
    }

    @RequestMapping("/douban")
    public ResultTemplateBean getDouBanTopSearch() {
        return topSearchCommonService.getDouBanTopSearch();
    }

    @RequestMapping("/tencent")
    public ResultTemplateBean getTencentTopSearch() {
        return topSearchCommonService.getTencentTopSearch();
    }

    @RequestMapping("/toutiao")
    public ResultTemplateBean getTouTiaoTopSearch() {
        return topSearchCommonService.getTouTiaoTopSearch();
    }

    @RequestMapping("/wangyi")
    public ResultTemplateBean getWangYiTopSearch() {
        return topSearchCommonService.getWangYiTopSearch();
    }

    @RequestMapping("/biaoshengwangyiyun")
    public ResultTemplateBean getWangYiYunBiaoShengSearch() {
        return topSearchCommonService.getWangYiYunTopSearch(SearchTypeWangYiYunEnum.BIAO_SHENG_WANG_YI_YUN);
    }

    @RequestMapping("/xingegwangyiyun")
    public ResultTemplateBean getWangYiYunXinGeSearch() {
        return topSearchCommonService.getWangYiYunTopSearch(SearchTypeWangYiYunEnum.XIN_GE_WANG_YI_YUN);
    }

    @RequestMapping("/yuanchuangwangyiyun")
    public ResultTemplateBean getWangYiYunYuanChuangSearch() {
        return topSearchCommonService.getWangYiYunTopSearch(SearchTypeWangYiYunEnum.YUAN_CHUANG_WANG_YI_YUN);
    }

    @RequestMapping("/regewangyiyun")
    public ResultTemplateBean getWangYiYunReGeSearch() {
        return topSearchCommonService.getWangYiYunTopSearch(SearchTypeWangYiYunEnum.RE_GE_WANG_YI_YUN);
    }

    @RequestMapping("/tiebabaidu")
    public ResultTemplateBean getBaiDuTieBaSearch() {
        return topSearchCommonService.getBaiDuTieBaSearch();
    }

    @RequestMapping("/shaoshupai")
    public ResultTemplateBean getShaoShuPaiSearch() {
        return topSearchCommonService.getShaoShuPaiSearch();
    }

    @RequestMapping("/dianshijubaidu")
    public ResultTemplateBean getDianShiJuBaiDuSearch() {
        return topSearchCommonService.getBaiDuTopSearch(SearchTypeBaiDuEnum.DIAN_SHI_JU_BAIDU);
    }

    @RequestMapping("/xiaoshuobaidu")
    public ResultTemplateBean getXiaoShuoBaiDuDuSearch() {
        return topSearchCommonService.getBaiDuTopSearch(SearchTypeBaiDuEnum.XIAO_SHUO_BAIDU);
    }

    @RequestMapping("/dianyingbaidu")
    public ResultTemplateBean getDianYingBaiDuSearch() {
        return topSearchCommonService.getBaiDuTopSearch(SearchTypeBaiDuEnum.DIAN_YING_BAIDU);
    }

    @RequestMapping("/youxibaidu")
    public ResultTemplateBean getYouXiBaiDuSearch() {
        return topSearchCommonService.getBaiDuTopSearch(SearchTypeBaiDuEnum.YOU_XI_BAIDU);
    }

    @RequestMapping("/qichebaidu")
    public ResultTemplateBean getCarBaiDuSearch() {
        return topSearchCommonService.getBaiDuTopSearch(SearchTypeBaiDuEnum.QI_CHE_BAIDU);
    }

    @RequestMapping("/regengbaidu")
    public ResultTemplateBean getReGengBaiDuSearch() {
        return topSearchCommonService.getBaiDuTopSearch(SearchTypeBaiDuEnum.REGENG_BAIDU);
    }

    @RequestMapping("/caijingbaidu")
    public ResultTemplateBean getCaiJingBaiDuSearch() {
        return topSearchCommonService.getBaiDuTopSearch(SearchTypeBaiDuEnum.CAIJING_BAIDU);
    }

    @RequestMapping("/minshengbaidu")
    public ResultTemplateBean getMinShengBaiDuSearch() {
        return topSearchCommonService.getBaiDuTopSearch(SearchTypeBaiDuEnum.MINSHENG_BAIDU);
    }

    @RequestMapping("/zhihu")
    public ResultTemplateBean getZhiHuSearch() {
        return topSearchCommonService.getZhiHuTopSearch();
    }

//    民生  财经   热梗  游戏  汽车
}
