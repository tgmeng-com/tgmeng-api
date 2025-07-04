package com.tgmeng.service.topsearch.Impl;

import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.enums.business.DataInfoCardEnum;
import com.tgmeng.common.enums.business.ForestRequestHeaderOriginEnum;
import com.tgmeng.common.enums.business.ForestRequestHeaderRefererEnum;
import com.tgmeng.common.enums.exception.ServerExceptionEnum;
import com.tgmeng.common.exception.ServerException;
import com.tgmeng.common.forest.client.topsearch.ITopSearchGlobalClient;
import com.tgmeng.common.mapper.mapstruct.topsearch.ITopSearchGlobalMapper;
import com.tgmeng.common.util.ForestUtil;
import com.tgmeng.model.dto.topsearch.TopSearchYoutubeDTO;
import com.tgmeng.model.vo.topsearch.TopSearchCommonVO;
import com.tgmeng.service.topsearch.ITopSearchGlobalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TopSearchGlobalServiceImpl implements ITopSearchGlobalService {

    private final ITopSearchGlobalClient topSearchGlobalClient;
    private final ITopSearchGlobalMapper topSearchGlobalMapper;


    @Override
    public ResultTemplateBean getYoutubeSortByTopSearch() {
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchYoutubeDTO topSearchYoutubeDTO = topSearchGlobalClient
                    .topSearchYoutube(ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.YOUTUBE.getValue(), ForestRequestHeaderOriginEnum.YOUTUBE.getValue()));
            //添加热榜
            topSearchCommonVOS.addAll(topSearchYoutubeDTO.getItems()
                    .stream()
                    .map(topSearchGlobalMapper::topSearchYoutubeDTO2TopSearchCommonVO)
                    .toList())
            ;
        } catch (Exception e) {
            log.error("👺👺👺获取Youtube热搜失败👺👺👺",e);
            throw new ServerException(ServerExceptionEnum.YOUTUBE_TOP_SEARCH_EXCEPTION);
        }
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS, DataInfoCardEnum.YOUTUBE.getKey(), DataInfoCardEnum.YOUTUBE.getValue(),DataInfoCardEnum.YOUTUBE.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
    }
}
