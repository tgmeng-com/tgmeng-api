package com.tgmeng.common.forest.client.topsearch;

import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.Header;
import com.tgmeng.common.forest.header.ForestRequestHeader;
import com.tgmeng.model.vo.topsearch.china.TopSearchBaiDuResVO;
import com.tgmeng.model.vo.topsearch.china.TopSearchBilibiliResVO;
import com.tgmeng.model.vo.topsearch.china.TopSearchWeiBoResVO;

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
    TopSearchWeiBoResVO weiBo(@Header ForestRequestHeader topSearchRequestHeader);

    /**
     * description: bilibili热搜
     * 可视化url为：https://www.bilibili.com/v/popular/rank/all 可对比参照
     * method: bilibili
     *
     * @author tgmeng
     * @since 2025/6/29 20:45
    */
    @Get("https://api.bilibili.com/x/web-interface/ranking/v2?rid=0&type=all")
    TopSearchBilibiliResVO bilibili(@Header ForestRequestHeader topSearchRequestHeader);

    /**
     * description: baiDu热搜
     * 可视化url为：https://top.baidu.com/board?tab=realtime 可对比参照
     * method: baiDu
     *
     * @author tgmeng
     * @since 2025/6/29 20:46
    */
    @Get("https://top.baidu.com/api/board?platform=wise&tab=realtime")
    TopSearchBaiDuResVO baiDu(@Header ForestRequestHeader topSearchRequestHeader);


}
