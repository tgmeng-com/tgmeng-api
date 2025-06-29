package com.tgmeng.service.topsearch;

import com.tgmeng.model.vo.topsearch.TopSearchCommonVO;

import java.util.List;

public interface ITopSearchGitHubService {
    List<TopSearchCommonVO> getGitHubTopSearch();
}
