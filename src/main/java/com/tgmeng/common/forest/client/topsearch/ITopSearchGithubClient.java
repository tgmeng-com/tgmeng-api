package com.tgmeng.common.forest.client.topsearch;

import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.Header;
import com.dtflys.forest.annotation.Var;
import com.tgmeng.common.forest.header.ForestRequestHeader;
import com.tgmeng.model.dto.topsearch.TopSearchGitHubDTO;

public interface ITopSearchGithubClient {


    @Get("https://api.github.com/search/repositories?q=stars:>0+created:>={time}&sort=stars&order=desc&page=1&per_page=100")
    TopSearchGitHubDTO allStars(@Header ForestRequestHeader topSearchRequestHeader,@Var("time") String time);

}