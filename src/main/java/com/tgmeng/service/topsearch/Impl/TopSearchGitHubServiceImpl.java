package com.tgmeng.service.topsearch.Impl;

import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.enums.business.DataInfoCardEnum;
import com.tgmeng.common.enums.business.ForestRequestHeaderOriginEnum;
import com.tgmeng.common.enums.business.ForestRequestHeaderRefererEnum;
import com.tgmeng.common.enums.exception.ServerExceptionEnum;
import com.tgmeng.common.exception.ServerException;
import com.tgmeng.common.forest.client.topsearch.ITopSearchGithubClient;
import com.tgmeng.common.mapper.mapstruct.topsearch.ITopSearchGitHubMapper;
import com.tgmeng.common.util.ForestUtil;
import com.tgmeng.model.dto.topsearch.TopSearchGitHubDTO;
import com.tgmeng.model.vo.topsearch.TopSearchCommonVO;
import com.tgmeng.service.topsearch.ITopSearchGitHubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public ResultTemplateBean getGithubSortByAllStars() {
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchGitHubDTO topSearchGitHubDTO = topSearchGithubClient.allStars(ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.GITHUB.getValue(), ForestRequestHeaderOriginEnum.GITHUB.getValue()));
            //添加热榜
            topSearchCommonVOS.addAll(topSearchGitHubDTO.getItems()
                    .stream()
                    .map(topSearchGitHubMapper::topSearchGitHubDTO2TopSearchCommonVO)
                    .toList())
            ;
        } catch (Exception e) {
            log.error("获取GITHUB热搜失败",e);
            throw new ServerException(ServerExceptionEnum.GITHUB_TOP_SEARCH_EXCEPTION);
        }
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS, DataInfoCardEnum.GITHUB.getKey(), DataInfoCardEnum.GITHUB.getValue(),DataInfoCardEnum.GITHUB.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
    }
}
