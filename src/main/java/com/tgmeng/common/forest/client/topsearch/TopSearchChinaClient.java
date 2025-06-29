package com.tgmeng.common.forest.client.topsearch;

import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.Header;
import com.tgmeng.common.forest.header.ForestRequestHeader;
import com.tgmeng.model.dto.topsearch.TopSearchBaiDuDTO;
import com.tgmeng.model.dto.topsearch.TopSearchBilibiliDTO;
import com.tgmeng.model.dto.topsearch.TopSearchDouYinDTO;
import com.tgmeng.model.dto.topsearch.TopSearchWeiBoDTO;

//加这个只是为了不爆红
public interface TopSearchChinaClient {


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
    @Get("https://top.baidu.com/api/board?platform=wise&tab=realtime")
    TopSearchBaiDuDTO baiDu(@Header ForestRequestHeader topSearchRequestHeader);

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


}
