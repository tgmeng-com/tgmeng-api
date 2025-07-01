package com.tgmeng.service.topsearch.Impl;

import com.tgmeng.common.forest.client.topsearch.ITopSearchGithubClient;
import com.tgmeng.common.mapper.mapstruct.topsearch.ITopSearchGitHubMapper;
import com.tgmeng.model.dto.topsearch.TopSearchGitHubDTO;
import com.tgmeng.model.vo.topsearch.TopSearchGitHubVO;
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
 * @since 2025/6/30 3:22
*/
@Service
@Slf4j
@RequiredArgsConstructor
public class TopSearchGitHubServiceImpl implements ITopSearchGitHubService {

    private final ITopSearchGithubClient topSearchGithubClient;
    private final ITopSearchGitHubMapper topSearchGitHubMapper;

    @Override
    public List<TopSearchGitHubVO> getGitHubTopSearch(TopSearchGitHubVO topSearchGitHubVO) {
        String topSearchGithubUrl =  getRequestParams(topSearchGitHubVO);
        TopSearchGitHubDTO github = topSearchGithubClient.github(topSearchGithubUrl);
        return topSearchGitHubMapper.topSearchGitHubDTOS2TopSearchGitHubVOS(github.getItems());
    }
    private static String getRequestParams(TopSearchGitHubVO topSearchGitHubVO) {
        StringBuilder urlBuilder = new StringBuilder();

        // 基本的查询条件
        if (topSearchGitHubVO.getStars() != null) {
            urlBuilder.append("stars:").append(topSearchGitHubVO.getStarsOperator().getValue()).append(topSearchGitHubVO.getStars()).append("+");
        }
        if (topSearchGitHubVO.getForks() != null) {
            urlBuilder.append("forks:").append(topSearchGitHubVO.getForksOperator().getValue()).append(topSearchGitHubVO.getForks()).append("+");
        }

        if (topSearchGitHubVO.getCreated() != null) {
            urlBuilder.append("created:").append(topSearchGitHubVO.getCreatedOperator().getValue()).append(topSearchGitHubVO.getCreated()).append("+");
        }
        // 其他过滤条件，例如大小、更新时间等
        if (topSearchGitHubVO.getSize() != null) {
            urlBuilder.append("size:").append(topSearchGitHubVO.getSizeOperator().getValue()).append(topSearchGitHubVO.getSize()).append("+");
        }

        // 删除最后的 "+"，如果有的话
        if (!urlBuilder.isEmpty() &&urlBuilder.charAt(urlBuilder.length() - 1) == '+') {
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }
        if (topSearchGitHubVO.getLanguage() != null) {
            urlBuilder.append("language:").append(topSearchGitHubVO.getLanguage()).append("+");
        }
        // 添加排序字段
        if (topSearchGitHubVO.getSort()!= null) {
            urlBuilder.append("&sort=").append(topSearchGitHubVO.getSort());
        }
        //添加排序方式
        if (topSearchGitHubVO.getOrder()!= null) {
            urlBuilder.append("&order=").append(topSearchGitHubVO.getOrder());
        }
        // 添加分页信息
        if (topSearchGitHubVO.getPage() != null && topSearchGitHubVO.getPage() > 0) {
            urlBuilder.append("&page=").append(topSearchGitHubVO.getPage());
        }

        if (topSearchGitHubVO.getPerPage() != null && topSearchGitHubVO.getPerPage() > 0) {
            urlBuilder.append("&per_page=").append(topSearchGitHubVO.getPerPage());
        }

        return urlBuilder.toString();
    }
}
