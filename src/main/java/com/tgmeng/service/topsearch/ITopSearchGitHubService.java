package com.tgmeng.service.topsearch;

import com.tgmeng.model.vo.topsearch.TopSearchGitHubVO;

import java.util.List;

public interface ITopSearchGitHubService {
    List<TopSearchGitHubVO> getGitHubTopSearch(TopSearchGitHubVO topSearchGitHubVO);
}
