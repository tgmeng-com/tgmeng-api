package com.tgmeng.service.topsearch.Impl;

import com.tgmeng.model.vo.topsearch.TopSearchCommonVO;
import com.tgmeng.service.topsearch.ITopSearchGitHubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description: GitHub热榜Service
 * package: com.tgmeng.service.topsearch.Impl
 * className: TopSearchGitHubServiceImpl
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/6/30 3:12
*/
@Service
@Slf4j
@RequiredArgsConstructor
public class TopSearchGitHubServiceImpl implements ITopSearchGitHubService {
    @Override
    public List<TopSearchCommonVO> getGitHubTopSearch() {
        return List.of();
    }
}
