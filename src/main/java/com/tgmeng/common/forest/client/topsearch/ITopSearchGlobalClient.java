package com.tgmeng.common.forest.client.topsearch;

import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.Header;
import com.dtflys.forest.annotation.Var;
import com.dtflys.forest.http.ForestResponse;
import com.tgmeng.common.forest.header.ForestRequestHeader;
import com.tgmeng.model.dto.topsearch.TopSearchYoutubeDTO;

public interface ITopSearchGlobalClient {
    @Get("https://www.googleapis.com/youtube/v3/videos?part=snippet,statistics&chart=mostPopular&maxResults=50&key=AIzaSyCX494YE2KiRnmGT6Y8odk4SBnlTwIWH4A")
    TopSearchYoutubeDTO topSearchYoutube(@Header ForestRequestHeader topSearchRequestHeader);

    @Get("https://huggingface.co/{type}")
    ForestResponse huggingFace(@Header ForestRequestHeader topSearchRequestHeader, @Var("type") String type);
}