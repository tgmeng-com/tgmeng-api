package com.tgmeng.controller.topsearch;

import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.enums.business.*;
import com.tgmeng.common.enums.enumcommon.EnumUtils;
import com.tgmeng.service.topsearch.ITopSearchCommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping("/dianshijutengxun")
    public ResultTemplateBean getDianShiJuTengXunShiPinSearch() {
        return topSearchCommonService.getTengXunShiPinTopSearch(SearchTypeTengXunShiPinEnum.DIAN_SHI_JU_TENG_XUN);
    }

    @RequestMapping("/dianyingtengxun")
    public ResultTemplateBean getDianYingTengXunShiPinSearch() {
        return topSearchCommonService.getTengXunShiPinTopSearch(SearchTypeTengXunShiPinEnum.DIAN_YING_TENG_XUN);
    }

    @RequestMapping("/dongmantengxun")
    public ResultTemplateBean getDongManTengXunShiPinSearch() {
        return topSearchCommonService.getTengXunShiPinTopSearch(SearchTypeTengXunShiPinEnum.DONG_MAN_TENG_XUN);
    }

    @RequestMapping("/zongyitengxun")
    public ResultTemplateBean getZongYiTengXunShiPinSearch() {
        return topSearchCommonService.getTengXunShiPinTopSearch(SearchTypeTengXunShiPinEnum.ZONG_YI_TENG_XUN);
    }

    @RequestMapping("/zongbangtengxun")
    public ResultTemplateBean getZongBangTengXunShiPinSearch() {
        return topSearchCommonService.getTengXunShiPinTopSearch(SearchTypeTengXunShiPinEnum.ZONG_BANG_TENG_XUN);
    }

    @RequestMapping("/dianshijuaiqiyi")
    public ResultTemplateBean getDianShiJuAiQiYiSearch() {
        return topSearchCommonService.getAiQiYiTopSearch(SearchTypeAiQiYiEnum.DIAN_SHI_JU_AI_QI_YI);
    }

    @RequestMapping("/dianyingaiqiyi")
    public ResultTemplateBean getDianYingAiQiYiSearch() {
        return topSearchCommonService.getAiQiYiTopSearch(SearchTypeAiQiYiEnum.DIAN_YING_AI_QI_YI);
    }

    @RequestMapping("/dongmanaiqiyi")
    public ResultTemplateBean getDongManAiQiYiSearch() {
        return topSearchCommonService.getAiQiYiTopSearch(SearchTypeAiQiYiEnum.DONG_MAN_AI_QI_YI);
    }

    @RequestMapping("/zongyiaiqiyi")
    public ResultTemplateBean getZongYiAiQiYiSearch() {
        return topSearchCommonService.getAiQiYiTopSearch(SearchTypeAiQiYiEnum.ZONG_YI_AI_QI_YI);
    }

    @RequestMapping("/zongbangaiqiyi")
    public ResultTemplateBean getZongBangAiQiYiSearch() {
        return topSearchCommonService.getAiQiYiTopSearch(SearchTypeAiQiYiEnum.ZONG_BANG_AI_QI_YI);
    }

    @RequestMapping("/dianshijuyouku")
    public ResultTemplateBean getDianShiJuYouKuSearch() {
        return topSearchCommonService.getYouKuTopSearch(SearchTypeYouKuEnum.DIAN_SHI_JU_YOU_KU);
    }

    @RequestMapping("/dianyingyouku")
    public ResultTemplateBean getDianYingYouKuSearch() {
        return topSearchCommonService.getYouKuTopSearch(SearchTypeYouKuEnum.DIAN_YING_YOU_KU);
    }

    @RequestMapping("/dongmanyouku")
    public ResultTemplateBean getDongManYouKuSearch() {
        return topSearchCommonService.getYouKuTopSearch(SearchTypeYouKuEnum.DONG_MAN_YOU_KU);
    }

    @RequestMapping("/zongyiyouku")
    public ResultTemplateBean getZongYiYouKuSearch() {
        return topSearchCommonService.getYouKuTopSearch(SearchTypeYouKuEnum.ZONG_YI_YOU_KU);
    }

    @RequestMapping("/zongbangyouku")
    public ResultTemplateBean getZongBangYouKuSearch() {
        return topSearchCommonService.getYouKuTopSearch(SearchTypeYouKuEnum.ZONG_BANG_YOU_KU);
    }

    @RequestMapping("/dianshijumangguo")
    public ResultTemplateBean getDianShiJuMangGuoSearch() {
        return topSearchCommonService.getMangGuoTopSearch(SearchTypeMangGuoEnum.DIAN_SHI_JU_MANG_GUO);
    }

    @RequestMapping("/dianyingmangguo")
    public ResultTemplateBean getDianYingMangGuoSearch() {
        return topSearchCommonService.getMangGuoTopSearch(SearchTypeMangGuoEnum.DIAN_YING_MANG_GUO);
    }

    @RequestMapping("/dongmanmangguo")
    public ResultTemplateBean getDongManMangGuoSearch() {
        return topSearchCommonService.getMangGuoTopSearch(SearchTypeMangGuoEnum.DONG_MAN_MANG_GUO);
    }

    @RequestMapping("/zongyimangguo")
    public ResultTemplateBean getZongYiMangGuoSearch() {
        return topSearchCommonService.getMangGuoTopSearch(SearchTypeMangGuoEnum.ZONG_YI_MANG_GUO);
    }

    @RequestMapping("/zongbangmangguo")
    public ResultTemplateBean getZongBangMangGuoSearch() {
        return topSearchCommonService.getMangGuoTopSearch(SearchTypeMangGuoEnum.ZONG_BANG_MANG_GUO);
    }

    @RequestMapping("/zhoupiaofangbangmaoyan")
    public ResultTemplateBean getZhouPiaoFangBangMaoYanSearch() {
        return topSearchCommonService.getMaoYanTopSearch(SearchTypeMaoYanEnum.ZHOU_PIAO_FANG_BANG_MAO_YAN);
    }

    @RequestMapping("/xiangkanbangmaoyan")
    public ResultTemplateBean getXiangkanBangMaoYanSearch() {
        return topSearchCommonService.getMaoYanTopSearch(SearchTypeMaoYanEnum.XIANG_KAN_BANG_MAO_YAN);
    }

    @RequestMapping("/goupiaopingfenbangmaoyan")
    public ResultTemplateBean getGouPiaoPingFenBangMaoYanSearch() {
        return topSearchCommonService.getMaoYanTopSearch(SearchTypeMaoYanEnum.GOU_PIAO_PING_FEN_BANG_MAO_YAN);
    }

    @RequestMapping("/top100maoyan")
    public ResultTemplateBean getTop100MaoYanSearch() {
        return topSearchCommonService.getMaoYanTopSearch(SearchTypeMaoYanEnum.TOP_100_MAO_YAN);
    }

    @RequestMapping("/jinrongjie")
    public ResultTemplateBean getJinRongJieSearch() {
        return topSearchCommonService.getJinRongJieSearch();
    }

    @RequestMapping("/diyicaijing")
    public ResultTemplateBean getDiYiCaiJingSearch() {
        return topSearchCommonService.getDiYiCaiJingSearch();
    }

    @RequestMapping("/tonghuashun")
    public ResultTemplateBean getTongHuaShunSearch() {
        return topSearchCommonService.getTongHuaShunSearch();
    }

    @RequestMapping("/huaerjiejianwen")
    public ResultTemplateBean getHuaErJieJianWenSearch() {
        return topSearchCommonService.getHuaErJieJianWenSearch();
    }

    @RequestMapping("/cailianshe")
    public ResultTemplateBean getCaiLianSheSearch() {
        return topSearchCommonService.getCaiLianSheSearch();
    }

    @RequestMapping("/gelonghui")
    public ResultTemplateBean getGeLongHuiSearch() {
        return topSearchCommonService.getGeLongHuiSearch();
    }

    @RequestMapping("/fabu")
    public ResultTemplateBean getFaBuSearch() {
        return topSearchCommonService.getFaBuSearch();
    }

    @RequestMapping("/jinshi")
    public ResultTemplateBean getJinShiSearch() {
        return topSearchCommonService.getJinShiSearch();
    }

    @RequestMapping("/niuyueshibao")
    public ResultTemplateBean getNewYueShiBaoSearch() {
        return topSearchCommonService.getNewYueShiBaoSearch();
    }

    @RequestMapping("/bbc")
    public ResultTemplateBean getBBCSearch() {
        return topSearchCommonService.getBBCSearch();
    }

    @RequestMapping("/faguang")
    public ResultTemplateBean getFaGuangSearch() {
        return topSearchCommonService.getFaGuangSearch();
    }

    @RequestMapping("/huaerjieribao")
    public ResultTemplateBean getHuaErJieRiBaoSearch() {
        return topSearchCommonService.getHuaErJieRiBaoSearch();
    }

    @RequestMapping("/dajiyuan")
    public ResultTemplateBean getDaJiYuanSearch() {
        return topSearchCommonService.getDaJiYuanSearch();
    }

    @RequestMapping("/woshipm")
    public ResultTemplateBean getWoShiPMSearch() {
        return topSearchCommonService.getWoShiPMSearch();
    }

    @RequestMapping("/youshewang")
    public ResultTemplateBean getYouSheWangSearch() {
        return topSearchCommonService.getYouSheWangSearch();
    }

    @RequestMapping("/wenzhangbangzhanku")
    public ResultTemplateBean getWenZhangBangZhanKuSearch() {
        return topSearchCommonService.getZhanKuSearch(SearchTypeZhanKuEnum.WEN_ZHANG_BANG_ZHAN_KU);
    }

    @RequestMapping("/qianlibangzhanku")
    public ResultTemplateBean getQianLiBangZhanKuSearch() {
        return topSearchCommonService.getZhanKuSearch(SearchTypeZhanKuEnum.QIAN_LI_BANG_ZHAN_KU);
    }

    @RequestMapping("/zuopinbangzhanku")
    public ResultTemplateBean getZuoPinBangZhanKuSearch() {
        return topSearchCommonService.getZhanKuSearch(SearchTypeZhanKuEnum.ZUO_PIN_BANG_ZHAN_KU);
    }

    @RequestMapping("/remenzuopintuyawangguo")
    public ResultTemplateBean getReMenZuoPinTuYaWangGuoSearch() {
        return topSearchCommonService.getZhanTuYaWangGuoSearch(SearchTypeTuYaWangGuoEnum.RE_MEN_ZUO_PIN_TU_YA_WANG_GUO);
    }

    @RequestMapping("/jingxuanzuopintuyawangguo")
    public ResultTemplateBean getJingXuanZuoPinTuYaWangGuoSearch() {
        return topSearchCommonService.getZhanTuYaWangGuoSearch(SearchTypeTuYaWangGuoEnum.JING_XUAN_ZUO_PIN_TU_YA_WANG_GUO);
    }

    @RequestMapping("/jinrixinzuotuyawangguo")
    public ResultTemplateBean getJinRiXinZuoTuYaWangGuoSearch() {
        return topSearchCommonService.getZhanTuYaWangGuoSearch(SearchTypeTuYaWangGuoEnum.JIN_RI_XIN_ZUO_TU_YA_WANG_GUO);
    }

    @RequestMapping("/faxianxinzuotuyawangguo")
    public ResultTemplateBean getFaXianXinZuoTuYaWangGuoSearch() {
        return topSearchCommonService.getZhanTuYaWangGuoSearch(SearchTypeTuYaWangGuoEnum.FA_XIAN_XIN_ZUO_TU_YA_WANG_GUO);
    }

    @RequestMapping("/shejidaren")
    public ResultTemplateBean getSheJiDaRenSearch() {
        return topSearchCommonService.getSheJiDaRenSearch();
    }

    @RequestMapping("/topys")
    public ResultTemplateBean getTopysSearch() {
        return topSearchCommonService.getTopysSearch();
    }

    @RequestMapping("/archdaily")
    public ResultTemplateBean getArchDailySearch() {
        return topSearchCommonService.getArchDailySearch();
    }

    @RequestMapping("/dribbble")
    public ResultTemplateBean getDribbbleSearch() {
        return topSearchCommonService.getDribbbleSearch();
    }

    @RequestMapping("/awwwards")
    public ResultTemplateBean getAwwwardsSearch() {
        return topSearchCommonService.getAwwwardsSearch();
    }

    @RequestMapping("/core77")
    public ResultTemplateBean getCore77Search() {
        return topSearchCommonService.getCore77Search();
    }

    @RequestMapping("/abduzeedo")
    public ResultTemplateBean getAbduzeedoSearch() {
        return topSearchCommonService.getAbduzeedoSearch();
    }

    @RequestMapping("/mit")
    public ResultTemplateBean getMITSearch() {
        return topSearchCommonService.getMITSearch();
    }

    @RequestMapping("/zhongguokexueyuan")
    public ResultTemplateBean getZhongGuoKeXueYuanSearch() {
        return topSearchCommonService.getZhongGuoKeXueYuanSearch();
    }

    @RequestMapping("/eurekalert")
    public ResultTemplateBean getEurekAlertSearch() {
        return topSearchCommonService.getEurekAlertSearch();
    }

    @RequestMapping("/rengongzhinengguojikejichuangxinzhongxin")
    public ResultTemplateBean getRenGongZhiNengGuoJiKeJiChuangXinZhongXinSearch() {
        return topSearchCommonService.getGuoJiKeJiChuangXinZhongXinSearch(SearchTypeGuoJiKeJiChuangXinZhongXinnum.REN_GONG_ZHI_NENG_GUO_JI_KE_JI_CHUANG_XIN_ZHONG_XIN);
    }

    @RequestMapping("/yiyaojiankangguojikejichuangxinzhongxin")
    public ResultTemplateBean getYiYaoJianKangGuoJiKeJiChuangXinZhongXinSearch() {
        return topSearchCommonService.getGuoJiKeJiChuangXinZhongXinSearch(SearchTypeGuoJiKeJiChuangXinZhongXinnum.YI_YAO_JIAN_KANG_GUO_JI_KE_JI_CHUANG_XIN_ZHONG_XIN);
    }

    @RequestMapping("/jiqizhixin")
    public ResultTemplateBean getJiQiZhiXinSearch() {
        return topSearchCommonService.getJiQiZhiXinSearch();
    }

    @RequestMapping("/hupu")
    public ResultTemplateBean getHuPuSearch() {
        return topSearchCommonService.getHuPuSearch();
    }

    @RequestMapping("/dongqiudi")
    public ResultTemplateBean getDongQiuDiSearch() {
        return topSearchCommonService.getDongQiuDiSearch();
    }

    @RequestMapping("/xinlangtiyu")
    public ResultTemplateBean getXinLangTiYuSearch() {
        return topSearchCommonService.getXinLangTiYuSearch();
    }

    @RequestMapping("/souhutiyu")
    public ResultTemplateBean getSouHuTiYuSearch() {
        return topSearchCommonService.getSouHuTiYuSearch();
    }

    @RequestMapping("/tiyuwangyi")
    public ResultTemplateBean getWangYiTiYuSearch() {
        return topSearchCommonService.getWangYiTiYuSearch();
    }

    @RequestMapping("/yangshitiyu")
    public ResultTemplateBean getYangShiTiYuSearch() {
        return topSearchCommonService.getYangShiTiYuSearch();
    }

    @RequestMapping("/pptiyu")
    public ResultTemplateBean getPPTiYuSearch() {
        return topSearchCommonService.getPPTiYuSearch();
    }

    @RequestMapping("/zhiboba")
    public ResultTemplateBean getZhiBoBaSearch() {
        return topSearchCommonService.getZhiBoBaSearch();
    }

    @RequestMapping("/v2ex")
    public ResultTemplateBean getV2exSearch() {
        return topSearchCommonService.getV2exSearch();
    }

    @RequestMapping("/buxingjiehupu")
    public ResultTemplateBean getBuXingJieHuPuSearch() {
        return topSearchCommonService.getBuXingJieHuPuSearch();
    }

    @RequestMapping("/nga")
    public ResultTemplateBean getNgaPuSearch() {
        return topSearchCommonService.getNgaPuSearch();
    }

    // 前端暂时不展示
    @RequestMapping("/yimusanfendi")
    public ResultTemplateBean getYiMuSanFenDiSearch() {
        return topSearchCommonService.getYiMuSanFenDiSearch();
    }

    @RequestMapping("/wenzhangjuejin")
    public ResultTemplateBean getWenZhangJueJinSearch() {
        return topSearchCommonService.getWenZhangJueJinSearch();
    }

    @RequestMapping("/hackernews")
    public ResultTemplateBean getHackerNewsSearch() {
        return topSearchCommonService.getHackerNewsSearch();
    }

    @RequestMapping("/gouzudouban")
    public ResultTemplateBean getGouZuDouBanSearch() {
        return topSearchCommonService.getXiaoZuDouBanSearch(SearchTypeXiaoZuDouBanEnum.GOU_ZU_DOU_BAN);
    }

    @RequestMapping("/maizudouban")
    public ResultTemplateBean getMaiZuDouBanSearch() {
        return topSearchCommonService.getXiaoZuDouBanSearch(SearchTypeXiaoZuDouBanEnum.MAI_ZU_DOU_BAN);
    }

    @RequestMapping("/pinzudouban")
    public ResultTemplateBean getPinZuDouBanSearch() {
        return topSearchCommonService.getXiaoZuDouBanSearch(SearchTypeXiaoZuDouBanEnum.PIN_ZU_DOU_BAN);
    }

    @RequestMapping("/aimaoshenghuodouban")
    public ResultTemplateBean getAiMaoShengHuoDouBanSearch() {
        return topSearchCommonService.getXiaoZuDouBanSearch(SearchTypeXiaoZuDouBanEnum.AI_MAO_SHENG_HUO_DOU_BAN);
    }

    @RequestMapping("/aimaozaopendouban")
    public ResultTemplateBean getAiMaoZaoPenDouBanSearch() {
        return topSearchCommonService.getXiaoZuDouBanSearch(SearchTypeXiaoZuDouBanEnum.AI_MAO_ZAO_PEN_DOU_BAN);
    }

    @RequestMapping("/lishidijiashenmezhidemai")
    public ResultTemplateBean getLiShiDiJiaShenMeZhiDeMaiSearch() {
        return topSearchCommonService.getLiShiDiJiaShenMeZhiDeMaiSearch(SearchTypeShenMeZhiDeMaiEnum.LI_SHI_DI_JIA_SHEN_ME_ZHI_DE_MAI);
    }

    @RequestMapping("/xiaozudouban/{type}")
    public ResultTemplateBean getAiMaoZaoPenDouBanCommonSearch(@PathVariable("type") String type) {
        return topSearchCommonService.getAiMaoZaoPenDouBanCommonSearch(EnumUtils.getEnumByKey(SearchTypeXiaoZuDouBanEnum.class, type));
    }

    @RequestMapping("/youminxingkong")
    public ResultTemplateBean getYouMinXingKongSearch() {
        return topSearchCommonService.getYouMinXingKongSearch();
    }

    @RequestMapping("/3dmgame")
    public ResultTemplateBean getThreeDmGameSearch() {
        return topSearchCommonService.getThreeDmGameSearch();
    }

    @RequestMapping("/a9vg")
    public ResultTemplateBean getA9VGSearch() {
        return topSearchCommonService.getA9VGSearch();
    }

    @RequestMapping("/youxituoluo")
    public ResultTemplateBean getYouXiTuoLuoSearch() {
        return topSearchCommonService.getYouXiTuoLuoSearch();
    }

    @RequestMapping("/ign")
    public ResultTemplateBean getIGNSearch() {
        return topSearchCommonService.getIGNSearch();
    }

    @RequestMapping("/gcores")
    public ResultTemplateBean getGcoresSearch() {
        return topSearchCommonService.getGcoresSearch();
    }

    @RequestMapping("/youyanshe")
    public ResultTemplateBean getYouYanSheSearch() {
        return topSearchCommonService.getYouYanSheSearch();
    }

    @RequestMapping("/17173")
    public ResultTemplateBean get17173Search() {
        return topSearchCommonService.get17173Search();
    }

    @RequestMapping("/youxiawang")
    public ResultTemplateBean getYouXiaWangSearch() {
        return topSearchCommonService.getYouXiaWangSearch();
    }

    @RequestMapping("/shengwugu")
    public ResultTemplateBean getShengWuGuSearch() {
        return topSearchCommonService.getShengWuGuSearch();
    }

    @RequestMapping("/yiyaomofang")
    public ResultTemplateBean getYiYaoMoFangSearch() {
        return topSearchCommonService.getYiYaoMoFangSearch();
    }

    @RequestMapping("/dingxiangyisheng")
    public ResultTemplateBean getDingXiangYiShengSearch() {
        return topSearchCommonService.getDingXiangYiShengSearch();
    }

    @RequestMapping("/dingxiangyuanshequ")
    public ResultTemplateBean getDingXiangYuanSheQuSearch() {
        return topSearchCommonService.getDingXiangYuanSheQuSearch();
    }

    @RequestMapping("/shengmingshibao")
    public ResultTemplateBean getShengMingShiBaoSearch() {
        return topSearchCommonService.getShengMingShiBaoSearch();
    }

    @RequestMapping("/jiayidajiankang")
    public ResultTemplateBean getJiaYiDaJianKangSearch() {
        return topSearchCommonService.getJiaYiDaJianKangSearch();
    }

    @RequestMapping("/guoke")
    public ResultTemplateBean getGuoKeSearch() {
        return topSearchCommonService.getGuoKeSearch();
    }

    @RequestMapping("/jiankangshibaowang")
    public ResultTemplateBean getJianKangShiBaoWangSearch() {
        return topSearchCommonService.getJianKangShiBaoWangSearch();
    }

    @RequestMapping("/cctv/{type}")
    public ResultTemplateBean getCCTVCommonSearch(@PathVariable("type") String type) {
        return topSearchCommonService.getCCTVCommonSearch(EnumUtils.getEnumByKey(SearchTypeCCTVEnum.class, type));
    }

}
