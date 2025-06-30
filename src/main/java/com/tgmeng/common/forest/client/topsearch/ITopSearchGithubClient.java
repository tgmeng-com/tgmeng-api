package com.tgmeng.common.forest.client.topsearch;

import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.Var;
import com.tgmeng.model.dto.topsearch.TopSearchGitHubDTO;

public interface ITopSearchGithubClient {

    @Get("https://api.github.com/search/repositories?q=${url}")
    TopSearchGitHubDTO github(@Var("url") String url);
}
