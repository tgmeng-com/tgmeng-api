package com.tgmeng.common.forest.client.topsearch;

import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.Header;
import com.tgmeng.common.forest.header.ForestRequestHeader;
import com.tgmeng.model.vo.topsearch.china.TopSearchBaiDuResVO;
import com.tgmeng.model.vo.topsearch.china.TopSearchBilibiliResVO;
import com.tgmeng.model.vo.topsearch.china.TopSearchWeiBoResVO;

public interface TopSearchChinaClient {

    @Get("https://weibo.com/ajax/statuses/hot_band")
    TopSearchWeiBoResVO weiBo(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://api.bilibili.com/x/web-interface/ranking/v2?rid=0&type=all")
    TopSearchBilibiliResVO bilibili(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://top.baidu.com/api/board?platform=wise&tab=realtime")
    TopSearchBaiDuResVO baiDu(@Header ForestRequestHeader topSearchRequestHeader);


}
