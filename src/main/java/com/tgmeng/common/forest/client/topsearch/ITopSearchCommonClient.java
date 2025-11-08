package com.tgmeng.common.forest.client.topsearch;

import com.dtflys.forest.annotation.*;
import com.dtflys.forest.http.ForestResponse;
import com.tgmeng.common.forest.header.ForestRequestHeader;
import com.tgmeng.model.dto.topsearch.*;

import java.util.List;

//加这个只是为了不爆红
public interface ITopSearchCommonClient {

    /**
     * description: weiBo热搜
     * 可视化url为：https://s.weibo.com/top/summary 可对比参照
     * method: weiBo
     *
     * @author tgmeng
     * @since 2025/6/29 20:46
     */
    @Get("https://weibo.com/ajax/statuses/hot_band")
    TopSearchWeiBoDTO weiBo(@Header ForestRequestHeader topSearchRequestHeader);

    /**
     * description: bilibili热搜
     * 可视化url为：https://www.bilibili.com/v/popular/rank/all 可对比参照
     * method: bilibili
     *
     * @author tgmeng
     * @since 2025/6/29 20:45
     */
    @Get("https://api.bilibili.com/x/web-interface/ranking/v2?rid=0&type=all")
    TopSearchBilibiliDTO bilibili(@Header ForestRequestHeader topSearchRequestHeader);


    /**
     * description: baiDu热搜
     * 可视化url为：https://top.baidu.com/board?tab=realtime 可对比参照
     * method: baiDu
     *
     * @author tgmeng
     * @since 2025/6/29 20:46
     */
    @Get("https://top.baidu.com/api/board?platform=wise&tab={type}")
    TopSearchBaiDuDTO baiDu(@Header ForestRequestHeader topSearchRequestHeader, @Var("type") String type);

    /**
     * description: 抖音热搜
     * 可视化url为：https://www.douyin.com/hot 可对比参照
     * method: douYin
     *
     * @author tgmeng
     * @since 2025/6/29 22:37
     */
    @Get("https://www-hj.douyin.com/aweme/v1/web/hot/search/list/")
    TopSearchDouYinDTO douYin(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://m.douban.com/rexxar/api/v2/chart/hot_search_board?count=10&start=0")
    List<TopSearchDouBanDTO> douBan(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://i.news.qq.com/gw/event/pc_hot_ranking_list?ids_hash=&offset=0&page_size=51&appver=15.5_qqnews_7.1.60&rank_id=hot")
    TopSearchTencentDTO tencent(@Header ForestRequestHeader topSearchRequestHeader);

    /**
     * description: 由于头条的程序员脑子有病，返回的这几把json这么大，fastjson都解析不了，换jackson
     * method: toutiao
     *
     * @author tgmeng
     * @since 2025/7/2 18:34
     */
    @Get("https://www.toutiao.com/hot-event/hot-board/?origin=toutiao_pc")
    TopSearchTouTiaoDTO toutiao(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://gw.m.163.com/nc-main/api/v1/hqc/no-repeat-hot-list?source=hotTag")
    TopSearchWangYiDTO wangyi(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://music.163.com/api/playlist/detail?id={type}")
    TopSearchWangYiYunDTO wangyiyun(@Header ForestRequestHeader topSearchRequestHeader, @Var("type") String type);

    @Get("https://tieba.baidu.com/hottopic/browse/topicList")
    TopSearchBaiDuTieBaDTO baidutieba(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://sspai.com/api/v1/article/tag/page/get?limit=100&offset=0&tag=%E7%83%AD%E9%97%A8%E6%96%87%E7%AB%A0&released=false")
    TopSearchShaoShuPaiDTO shaoshupai(@Header ForestRequestHeader topSearchRequestHeader);

    //https://api.zhihu.com/topstory/hot-list?limit=10&reverse_order=0
    //https://www.zhihu.com/api/v3/feed/topstory/hot-list-web?limit={top_n}
    //这是好不容易找到的两个可以用的接口
    @Get("https://api.zhihu.com/topstory/hot-list?limit=100&reverse_order=0")
    TopSearchZhiHuDTO zhiHu(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://v.qq.com/biu/ranks/?t=hotsearch&channel={type}")
    ForestResponse tengXunShiPin(@Header ForestRequestHeader topSearchRequestHeader, @Var("type") String type);

    @Get("https://mesh.if.iqiyi.com/portal/pcw/rankList/comSecRankList?category_id={type}")
    TopSearchAiQiYiDTO aiQiYi(@Header ForestRequestHeader topSearchRequestHeader, @Var("type") String type);

    // 重要
    @Get("https://acs.youku.com/h5/mtop.youku.soku.yksearch/2.0/?appKey=23774304")
    ForestResponse youKuCookie(@Header ForestRequestHeader topSearchRequestHeader);

    //@Get("https://acs.youku.com/h5/mtop.youku.soku.yksearch/2.0/?appKey=23774304&data=%7B%22pg%22%3A%221%22%2C%22pz%22%3A%2210%22%2C%22appScene%22%3A%22default_page%22%2C%22appCaller%22%3A%22youku-search-sdk%22%2C%22utdId%22%3A%22%22%7D&t=1752703423430&sign=14f6d656bb0d77843fb3bbd620c2626e")
    @Get("https://acs.youku.com/h5/mtop.youku.soku.yksearch/2.0/?appKey={appKey}&data={data}&t={time}&sign={sign}")
    TopSearchYouKuDTO youKu(@Header ForestRequestHeader topSearchRequestHeader, @Var("appKey") String appKey,@Var("data") String data,@Var("time") String time, @Var("sign") String sign);

    @Get("https://mobileso.bz.mgtv.com/pc/suggest/v1")
    TopSearchMangGuoDTO mangGuo(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://m.maoyan.com/asgard/board?year=0&term=0&id={type}")
    ForestResponse maoYan(@Header ForestRequestHeader topSearchRequestHeader, @Var("type") String type);

    @Post("https://gateway.jrj.com/jrj-news/news/queryNewsList")
    TopSearchJingRongJieDTO jingRongJie(@Header ForestRequestHeader topSearchRequestHeader,@JSONBody("sortBy") Integer sortBy, @JSONBody("pageSize") Integer pageSize, @JSONBody("channelNum") String channelNum);

    @Get("https://www.yicai.com")
    ForestResponse diYiCaiJing(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://news.10jqka.com.cn/")
    ForestResponse tongHuaShun(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://api-one-wscn.awtmt.com/apiv1/content/information-flow?channel=global&accept=article&cursor=&limit=20&action=upglide")
    TopSearchHuaErJieJianWenDTO huaErJieJianWen(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://www.cls.cn/")
    ForestResponse caiLianShe(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://www.gelonghui.com/")
    ForestResponse getLongHui(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://www.fastbull.com/cn/news")
    ForestResponse getFaBu(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://xnews.jin10.com/")
    ForestResponse getJinShi(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://m.cn.nytimes.com/world/")
    ForestResponse getNewYueShiBao(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://www.bbc.com/zhongwen/simp/popular/read")
    ForestResponse getBBC(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://www.rfi.fr/cn")
    ForestResponse getFaGuang(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://cn.wsj.com/zh-hans/news/world?mod=nav_top_section")
    ForestResponse getHuaErJieRiBao(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://www.epochtimes.com/gb/nsc418.htm")
    ForestResponse getDaJiYuan(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://www.woshipm.com/api2/app/article/popular/daily")
    TopSearchWoShiPMDTO getWoShiPM(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://hot.uisdc.com/posts")
    ForestResponse getYouSheWang(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://www.zcool.com.cn/{type}")
    ForestResponse getZhanKu(@Header ForestRequestHeader topSearchRequestHeader, @Var("type") String type);

    @Get("https://www.gracg.com/showwork/{type}")
    ForestResponse getTuYaWangGuo(@Header ForestRequestHeader topSearchRequestHeader, @Var("type") String type);

    @Get("https://www.shejidaren.com/")
    ForestResponse getSheJiDaRen(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://www.topys.cn/category/12")
    ForestResponse getTopys(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://www.archdaily.cn/cn?ad_source=jv-header")
    ForestResponse getArchDaily(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://dribbble.com/")
    ForestResponse getDribbble(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://www.awwwards.com/websites/sites_of_the_day/")
    ForestResponse getAwwwards(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://www.core77.com/")
    ForestResponse getCore77(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://abduzeedo.com/")
    ForestResponse getAbduzeedo(@Header ForestRequestHeader topSearchRequestHeader);

    @Post("https://apii.web.mittrchina.com/flash")
    TopSearchMITDTO getMIT(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://www.cas.cn/syky")
    ForestResponse getZhongGuoKeXueYuan(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://www.eurekalert.org/language/chinese/home")
    ForestResponse getEurekAlert(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://www.ncsti.gov.cn/kjdt/kjrd/{type}")
    ForestResponse getGuoJiKeJiChuangXinZhongXin(@Header ForestRequestHeader topSearchRequestHeader, @Var("type") String type);

    @Get("https://www.jiqizhixin.com/api/article_library/articles.json?sort=time&page=1&per=12")
    TopSearchJiQiZhiXinDTO getJiQiZhiXin(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://www.hupu.com/")
    ForestResponse getHuPu(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://www.dongqiudi.com/")
    ForestResponse getDongQiuDi(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://sports.sina.com.cn/")
    ForestResponse getXinLangTiYu(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://sports.sohu.com/")
    ForestResponse getSouHuTiYu(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://sports.163.com/")
    ForestResponse getWangYiTiYu(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://sports.cctv.com/")
    ForestResponse getYangShiTiYu(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://www.ppsport.com/")
    ForestResponse getPPTiYu(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://www.zhibo8.com/")
    ForestResponse getZhiBoBa(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://www.v2ex.com/?tab=hot")
    ForestResponse getV2EX(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://bbs.hupu.com/all-gambia")
    ForestResponse getBuXingJieHuPu(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://nga.cn/")
    ForestResponse getNga(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://www.1point3acres.com/bbs/")
    ForestResponse getYiMuSanFenDi(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://api.juejin.cn/content_api/v1/content/article_rank?category_id=1&type=hot")
    TopSearchWenZhangJueJinDTO getWenZhangJueJin(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://news.ycombinator.com/")
    ForestResponse getHackerNews(@Header ForestRequestHeader topSearchRequestHeader);

}