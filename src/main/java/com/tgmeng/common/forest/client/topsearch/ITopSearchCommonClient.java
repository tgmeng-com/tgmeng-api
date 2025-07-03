package com.tgmeng.common.forest.client.topsearch;

import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.Header;
import com.dtflys.forest.annotation.Var;
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
    TopSearchBaiDuDTO baiDu(@Header ForestRequestHeader topSearchRequestHeader,@Var("type") String type);

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

    @Get("https://music.163.com/api/playlist/detail?id=3778678")
    TopSearchWangYiYunDTO wangyiyun(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://tieba.baidu.com/hottopic/browse/topicList")
    TopSearchBaiDuTieBaDTO baidutieba(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://sspai.com/api/v1/article/tag/page/get?limit=100&offset=0&tag=%E7%83%AD%E9%97%A8%E6%96%87%E7%AB%A0&released=false")
    TopSearchShaoShuPaiDTO shaoshupai(@Header ForestRequestHeader topSearchRequestHeader);

}
