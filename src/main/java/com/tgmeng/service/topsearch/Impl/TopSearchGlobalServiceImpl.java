package com.tgmeng.service.topsearch.Impl;

import cn.hutool.core.util.StrUtil;
import com.dtflys.forest.http.ForestResponse;
import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.enums.business.DataInfoCardEnum;
import com.tgmeng.common.enums.business.ForestRequestHeaderOriginEnum;
import com.tgmeng.common.enums.business.ForestRequestHeaderRefererEnum;
import com.tgmeng.common.enums.business.SearchTypeHuggingFaceEnum;
import com.tgmeng.common.enums.exception.ServerExceptionEnum;
import com.tgmeng.common.exception.ServerException;
import com.tgmeng.common.forest.client.topsearch.ITopSearchGlobalClient;
import com.tgmeng.common.mapper.mapstruct.topsearch.ITopSearchCommonMapper;
import com.tgmeng.common.mapper.mapstruct.topsearch.ITopSearchGlobalMapper;
import com.tgmeng.common.util.ForestUtil;
import com.tgmeng.common.util.HuggingFaceJsoupParseUtil;
import com.tgmeng.model.dto.topsearch.TopSearchYoutubeDTO;
import com.tgmeng.model.vo.topsearch.TopSearchCommonVO;
import com.tgmeng.service.topsearch.ITopSearchGlobalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TopSearchGlobalServiceImpl implements ITopSearchGlobalService {

    private final ITopSearchGlobalClient topSearchGlobalClient;
    private final ITopSearchGlobalMapper topSearchGlobalMapper;
    private final ITopSearchCommonMapper topSearchCommonMapper;


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
            log.error("👺👺👺获取Youtube热搜失败👺👺👺", e);
            throw new ServerException(ServerExceptionEnum.YOUTUBE_TOP_SEARCH_EXCEPTION);
        }
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS, DataInfoCardEnum.YOUTUBE.getKey(), DataInfoCardEnum.YOUTUBE.getValue(), DataInfoCardEnum.YOUTUBE.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
    }

    @Override
    public ResultTemplateBean getHuggingFaceTopSearch(SearchTypeHuggingFaceEnum searchTypeHuggingFaceEnum) {
        try {
            ForestResponse forestResponse = topSearchGlobalClient.huggingFace(
                    ForestUtil.getRandomRequestHeader(
                            ForestRequestHeaderRefererEnum.HUGGING_FACE.getValue(),
                            ForestRequestHeaderOriginEnum.HUGGING_FACE.getValue()),
                    searchTypeHuggingFaceEnum.getValue()
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.HUGGING_FACE_TOP_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
            switch (searchTypeHuggingFaceEnum) {
                case SPACE_TRENDING_HUGGING_FACE:
                case SPACE_LIKES_HUGGING_FACE:
                    topSearchCommonVOS = HuggingFaceJsoupParseUtil.space(content);
                    break;
                case MODELS_TRENDING_HUGGING_FACE:
                case MODELS_LIKES_HUGGING_FACE:
                    topSearchCommonVOS = com.tgmeng.common.util.HuggingFaceJsoupParseUtil.models(content);
                    break;
                case DATASETS_TRENDING_HUGGING_FACE:
                case DATASETS_LIKES_HUGGING_FACE:
                    topSearchCommonVOS = com.tgmeng.common.util.HuggingFaceJsoupParseUtil.datasets(content);
                    break;
                default:
                    topSearchCommonVOS = Collections.emptyList();
                    break;
            }

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    searchTypeHuggingFaceEnum.getDescription(),
                    DataInfoCardEnum.HUGGING_FACE.getValue(),
                    DataInfoCardEnum.HUGGING_FACE.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);

        } catch (Exception e) {
            log.error("👺👺👺获取huggingface失败👺👺👺：平台；{}", searchTypeHuggingFaceEnum.getKey(), e);
            throw new ServerException(ServerExceptionEnum.HUGGING_FACE_TOP_SEARCH_EXCEPTION);
        }
    }
}
