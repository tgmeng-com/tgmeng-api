package com.tgmeng.common.forest.client.topsearch;

import com.dtflys.forest.annotation.*;
import com.dtflys.forest.http.ForestResponse;
import com.tgmeng.common.forest.header.ForestRequestHeader;

import java.util.Map;

//加这个只是为了不爆红
public interface ITopSearchCommonClient {

    // 重要
    @Get("https://acs.youku.com/h5/mtop.youku.soku.yksearch/2.0/?appKey=23774304")
    ForestResponse youKuCookie(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("{url}")
    ForestResponse commonHttpGetUtils(@Header ForestRequestHeader topSearchRequestHeader, @Var("url") String url, @Query Map<String, Object> queryParam);

    @Get("{url}")
    byte[] commonHttpGetUtilsForBytes(@Header ForestRequestHeader topSearchRequestHeader, @Var("url") String url, @Query Map<String, Object> queryParam);

    @Post("{url}")
    ForestResponse commonHttpPostUtils(@Header ForestRequestHeader topSearchRequestHeader, @Var("url") String url, @JSONBody Map<String, Object> data);

    @Post("{url}")
    ForestResponse commonHttpPostWithNoJsonBodyUtils(@Header ForestRequestHeader topSearchRequestHeader, @Var("url") String url);

}