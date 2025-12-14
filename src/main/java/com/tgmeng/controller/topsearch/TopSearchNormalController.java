package com.tgmeng.controller.topsearch;

import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.model.vo.topsearch.TopSearchCommonVO;
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
public class TopSearchNormalController {

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
        return topSearchCommonService.getTopSearchCommonService();
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
        return topSearchCommonService.getTopSearchCommonService();
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
        return topSearchCommonService.getTopSearchCommonService();
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
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/douban")
    public ResultTemplateBean getDouBanTopSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/tencent")
    public ResultTemplateBean getTencentTopSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/toutiao")
    public ResultTemplateBean getTouTiaoTopSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/wangyi")
    public ResultTemplateBean getWangYiTopSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/wangyiyun/{type}")
    public ResultTemplateBean getWangYiYunBiaoShengSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/xingegwangyiyun")
    public ResultTemplateBean getWangYiYunXinGeSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/yuanchuangwangyiyun")
    public ResultTemplateBean getWangYiYunYuanChuangSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/regewangyiyun")
    public ResultTemplateBean getWangYiYunReGeSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/tiebabaidu")
    public ResultTemplateBean getBaiDuTieBaSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/shaoshupai")
    public ResultTemplateBean getShaoShuPaiSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/baidu/{type}")
    public ResultTemplateBean getDianShiJuBaiDuSearch(@PathVariable("type") String type) {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/zhihu")
    public ResultTemplateBean getZhiHuSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/tencent/{type}")
    public ResultTemplateBean getDianShiJuTengXunShiPinSearch(@PathVariable("type") String type) {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/aiqiyi/{type}")
    public ResultTemplateBean getDianShiJuAiQiYiSearch(@PathVariable("type") String type) {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/youku/{type}")
    public ResultTemplateBean getDianShiJuYouKuSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/mangguo/{type}")
    public ResultTemplateBean getDianShiJuMangGuoSearch(@PathVariable("type") String type) {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/maoyan/{type}")
    public ResultTemplateBean getZhouPiaoFangBangMaoYanSearch(@PathVariable("type") String type) {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/jinrongjie")
    public ResultTemplateBean getJinRongJieSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/diyicaijing")
    public ResultTemplateBean getDiYiCaiJingSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/tonghuashun")
    public ResultTemplateBean getTongHuaShunSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/huaerjiejianwen")
    public ResultTemplateBean getHuaErJieJianWenSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/cailianshe")
    public ResultTemplateBean getCaiLianSheSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/gelonghui")
    public ResultTemplateBean getGeLongHuiSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/fabu")
    public ResultTemplateBean getFaBuSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/jinshi")
    public ResultTemplateBean getJinShiSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/niuyueshibao")
    public ResultTemplateBean getNewYueShiBaoSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/bbc")
    public ResultTemplateBean getBBCSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/faguang")
    public ResultTemplateBean getFaGuangSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/huaerjieribao")
    public ResultTemplateBean getHuaErJieRiBaoSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/woshipm")
    public ResultTemplateBean getWoShiPMSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/youshewang")
    public ResultTemplateBean getYouSheWangSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/zhanku/{type}")
    public ResultTemplateBean getWenZhangBangZhanKuSearch(@PathVariable("type") String type) {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/qianlibangzhanku")
    public ResultTemplateBean getQianLiBangZhanKuSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/zuopinbangzhanku")
    public ResultTemplateBean getZuoPinBangZhanKuSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/tuyawangguo/{type}")
    public ResultTemplateBean getReMenZuoPinTuYaWangGuoSearch(@PathVariable("type") String type) {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/shejidaren")
    public ResultTemplateBean getSheJiDaRenSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/topys")
    public ResultTemplateBean getTopysSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/archdaily")
    public ResultTemplateBean getArchDailySearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/dribbble")
    public ResultTemplateBean getDribbbleSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/awwwards")
    public ResultTemplateBean getAwwwardsSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/core77")
    public ResultTemplateBean getCore77Search() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/abduzeedo")
    public ResultTemplateBean getAbduzeedoSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/mit")
    public ResultTemplateBean getMITSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/eurekalert")
    public ResultTemplateBean getEurekAlertSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/guojikejichuangxinzhongxin/{type}")
    public ResultTemplateBean getRenGongZhiNengGuoJiKeJiChuangXinZhongXinSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }


    @RequestMapping("/jiqizhixin")
    public ResultTemplateBean getJiQiZhiXinSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/hupu")
    public ResultTemplateBean getHuPuSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/dongqiudi")
    public ResultTemplateBean getDongQiuDiSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/xinlangtiyu")
    public ResultTemplateBean getXinLangTiYuSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/souhutiyu")
    public ResultTemplateBean getSouHuTiYuSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/tiyuwangyi")
    public ResultTemplateBean getWangYiTiYuSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/yangshitiyu")
    public ResultTemplateBean getYangShiTiYuSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/pptiyu")
    public ResultTemplateBean getPPTiYuSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/zhiboba")
    public ResultTemplateBean getZhiBoBaSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/v2ex")
    public ResultTemplateBean getV2exSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/buxingjiehupu")
    public ResultTemplateBean getBuXingJieHuPuSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/nga")
    public ResultTemplateBean getNgaPuSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/wenzhangjuejin")
    public ResultTemplateBean getWenZhangJueJinSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/hackernews")
    public ResultTemplateBean getHackerNewsSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/xiaozudouban/{type}")
    public ResultTemplateBean getAiMaoZaoPenDouBanCommonSearch(@PathVariable("type") String type) {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/youminxingkong")
    public ResultTemplateBean getYouMinXingKongSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/3dmgame")
    public ResultTemplateBean getThreeDmGameSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/a9vg")
    public ResultTemplateBean getA9VGSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/youxituoluo")
    public ResultTemplateBean getYouXiTuoLuoSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/ign")
    public ResultTemplateBean getIGNSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/gcores")
    public ResultTemplateBean getGcoresSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/youyanshe")
    public ResultTemplateBean getYouYanSheSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/17173")
    public ResultTemplateBean get17173Search() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/youxiawang")
    public ResultTemplateBean getYouXiaWangSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/shengwugu")
    public ResultTemplateBean getShengWuGuSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/yiyaomofang")
    public ResultTemplateBean getYiYaoMoFangSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/dingxiangyisheng")
    public ResultTemplateBean getDingXiangYiShengSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/shengmingshibao")
    public ResultTemplateBean getShengMingShiBaoSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/jiayidajiankang")
    public ResultTemplateBean getJiaYiDaJianKangSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/guoke")
    public ResultTemplateBean getGuoKeSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/jiankangshibaowang")
    public ResultTemplateBean getJianKangShiBaoWangSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/cctv/{type}")
    public ResultTemplateBean getCCTVCommonSearch(@PathVariable("type") String type) {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/pengpaixinwen")
    public ResultTemplateBean<TopSearchCommonVO> getPengPaiXinWenCommonSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/zhitongcaijing")
    public ResultTemplateBean<TopSearchCommonVO> getZhiTongCaiJing() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/wuaipojie")
    public ResultTemplateBean<TopSearchCommonVO> getWuAiPojieSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/shuimushequ")
    public ResultTemplateBean<TopSearchCommonVO> getShuimushEquSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/chongbuluo")
    public ResultTemplateBean<TopSearchCommonVO> getChongBuluoSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/xianzhishequ")
    public ResultTemplateBean<TopSearchCommonVO> getXianzhishequSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/kdsshanghaitoutiao")
    public ResultTemplateBean<TopSearchCommonVO> getKdsshanghaitoutiaoSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/kanxue")
    public ResultTemplateBean<TopSearchCommonVO> getKanxueSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/tongxinrenjiayuan")
    public ResultTemplateBean<TopSearchCommonVO> getTongXinrenjiayuanSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/emacschina")
    public ResultTemplateBean<TopSearchCommonVO> getEmacsChinaSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/rubychina")
    public ResultTemplateBean<TopSearchCommonVO> getRubyChinaSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/kaidiwang")
    public ResultTemplateBean<TopSearchCommonVO> getkaidiwangSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/zhiwubuyankuajingdianshangshequ")
    public ResultTemplateBean<TopSearchCommonVO> getZhiWuBuyankuajingdianshangshequSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/kaiyuanzixun")
    public ResultTemplateBean<TopSearchCommonVO> getKiyuanzixunSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/jingguanzhijia")
    public ResultTemplateBean<TopSearchCommonVO> getjingguanzhijiaSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/36ke")
    public ResultTemplateBean<TopSearchCommonVO> get36keSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/itzhijia")
    public ResultTemplateBean<TopSearchCommonVO> getitzhijiaSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/jikegongyuan")
    public ResultTemplateBean<TopSearchCommonVO> getjikegongyuanSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/readhub")
    public ResultTemplateBean<TopSearchCommonVO> getreadhub() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/taimeiti")
    public ResultTemplateBean<TopSearchCommonVO> gettaimeitiSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/zhongguancunzaixian")
    public ResultTemplateBean<TopSearchCommonVO> getzhongguancunzaixianSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/landianwang")
    public ResultTemplateBean<TopSearchCommonVO> getlandianwangSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/chuangyebang")
    public ResultTemplateBean<TopSearchCommonVO> getchuangyebangSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/iheima")
    public ResultTemplateBean<TopSearchCommonVO> getiheimaSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/leifengwang")
    public ResultTemplateBean<TopSearchCommonVO> getleifengwangSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/quantianhoukeji")
    public ResultTemplateBean<TopSearchCommonVO> getquantianhoukejiSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/wulianwangzhiku")
    public ResultTemplateBean<TopSearchCommonVO> getwulianwangzhikuSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/kuaikeji")
    public ResultTemplateBean<TopSearchCommonVO> getkuaikejiSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/techweb")
    public ResultTemplateBean<TopSearchCommonVO> gettechwebSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/lixiangshenghuoshiyanshi")
    public ResultTemplateBean<TopSearchCommonVO> getlixiangshenghuoshiyanshiSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/duozhi")
    public ResultTemplateBean<TopSearchCommonVO> getduozhiSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/jiemodui")
    public ResultTemplateBean<TopSearchCommonVO> getjiemoduiSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/aimeiwang")
    public ResultTemplateBean<TopSearchCommonVO> getaimeiwangSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/zhanzhangzhijia")
    public ResultTemplateBean<TopSearchCommonVO> getzhanzhangzhijiaSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/lieyunwang")
    public ResultTemplateBean<TopSearchCommonVO> getlieyunwangSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/weixindushu")
    public ResultTemplateBean<TopSearchCommonVO> getweixindushuSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/acfun")
    public ResultTemplateBean<TopSearchCommonVO> getacfunSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/meimanbaike")
    public ResultTemplateBean<TopSearchCommonVO> getmeimanbaikeSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/shiguangwang")
    public ResultTemplateBean<TopSearchCommonVO> getshiguangwangSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/jiandan")
    public ResultTemplateBean<TopSearchCommonVO> getjiandanSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/dianwanbang")
    public ResultTemplateBean<TopSearchCommonVO> getdianwanbangSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/dianshimao")
    public ResultTemplateBean<TopSearchCommonVO> getdianshimaoSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/zhongguoxinwenwang")
    public ResultTemplateBean<TopSearchCommonVO> getzhongguoxinwenwangSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/zaker")
    public ResultTemplateBean<TopSearchCommonVO> getzakerSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/xinjingbao")
    public ResultTemplateBean<TopSearchCommonVO> getxinjingbaoSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/xingdaohuanqiu")
    public ResultTemplateBean<TopSearchCommonVO> getxingdaohuanqiuSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/21jingjiwang")
    public ResultTemplateBean<TopSearchCommonVO> get21jingjiwangSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/dongfangcaifuwang")
    public ResultTemplateBean<TopSearchCommonVO> getdongfangcaifuwangSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/mbazhiku")
    public ResultTemplateBean<TopSearchCommonVO> getmbazhikuSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/jingjiguanchawang")
    public ResultTemplateBean<TopSearchCommonVO> getjingjiguanchawangSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/shidaizaixian")
    public ResultTemplateBean<TopSearchCommonVO> getshidaizaixianSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/jingsecaijing")
    public ResultTemplateBean<TopSearchCommonVO> getjingsecaijingSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/xinlangcaijing")
    public ResultTemplateBean<TopSearchCommonVO> getxinlangcaijingSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/kuaijitoutiao")
    public ResultTemplateBean<TopSearchCommonVO> getkuaijitoutiaoSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/laohucaijing")
    public ResultTemplateBean<TopSearchCommonVO> getlaohucaijingSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/blockbeats")
    public ResultTemplateBean<TopSearchCommonVO> getblockbeats() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/huitongcaijing")
    public ResultTemplateBean<TopSearchCommonVO> gethuitongcaijingSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/meijingwang")
    public ResultTemplateBean<TopSearchCommonVO> getmeijingwangSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/xuangutong")
    public ResultTemplateBean<TopSearchCommonVO> getxuangutongSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/chaincatcher")
    public ResultTemplateBean<TopSearchCommonVO> getchaincatcherSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/kechuangbanribao")
    public ResultTemplateBean<TopSearchCommonVO> getkechuangbanribaoSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/tengxunshejikaifangpingtai")
    public ResultTemplateBean<TopSearchCommonVO> gettengxunshejikaifangpingtai() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/aliyunshequ")
    public ResultTemplateBean<TopSearchCommonVO> getAliyunSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/tengxunyunshequ")
    public ResultTemplateBean<TopSearchCommonVO> getTengxunyunSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }

    @RequestMapping("/meituanshequ")
    public ResultTemplateBean<TopSearchCommonVO> getMeituanshequSearch() {
        return topSearchCommonService.getTopSearchCommonService();
    }















}
