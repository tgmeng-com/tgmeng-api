package com.tgmeng.service.topsearch.Impl;

import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.enums.business.ForestRequestHeaderOriginEnum;
import com.tgmeng.common.enums.business.ForestRequestHeaderRefererEnum;
import com.tgmeng.common.enums.exception.ServerExceptionEnum;
import com.tgmeng.common.exception.ServerException;
import com.tgmeng.common.forest.client.topsearch.ITopSearchCommonClient;
import com.tgmeng.common.mapper.mapstruct.topsearch.ITopSearchCommonMapper;
import com.tgmeng.common.util.ForestUtil;
import com.tgmeng.common.util.StringUtil;
import com.tgmeng.model.dto.topsearch.TopSearchBaiDuDTO;
import com.tgmeng.model.dto.topsearch.TopSearchWeiBoDTO;
import com.tgmeng.model.vo.topsearch.TopSearchCommonVO;
import com.tgmeng.model.dto.topsearch.TopSearchBilibiliDTO;
import com.tgmeng.model.dto.topsearch.TopSearchDouYinDTO;
import com.tgmeng.service.topsearch.ITopSearchCommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class TopSearchCommonServiceImpl implements ITopSearchCommonService {

    private final ITopSearchCommonClient topSearchCommonClient;
    private final ITopSearchCommonMapper topSearchCommonMapper;

    /**
     * description: 百度热搜
     * method: getBaiDuTopSearch
     *
     * @author tgmeng
     * @since 2025/6/29 15:17
    */
    @Override
    public ResultTemplateBean getBaiDuTopSearch() {
        List<TopSearchCommonVO> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchBaiDuDTO topSearchBaiDuDTO = topSearchCommonClient.baiDu(ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.BAIDU.getValue(), ForestRequestHeaderOriginEnum.BAIDU.getValue()));
            topSearchCommonVOS = Optional.ofNullable(topSearchBaiDuDTO.getData())
                    .map(TopSearchBaiDuDTO.DataVO::getCards)
                    .orElse(Collections.emptyList())
                    .stream()
                    .filter(cardsVO -> cardsVO.getContent() != null || cardsVO.getTopContent() != null)
                    .flatMap(cardsVO ->
                            Stream.concat(
                                    //把置顶的那一个叼毛数据也合并进来
                                    cardsVO.getTopContent() != null ? cardsVO.getTopContent().stream() : Stream.empty(),
                                    cardsVO.getContent() != null ? cardsVO.getContent().stream() : Stream.empty()
                            ))
                    .map(topSearchCommonMapper::topSearchBaiDuDTOContentVO2TopSearchCommonVO)
                    //这里不用排序，因为百度的热搜排序不是按照score排，是按照他返回的顺序排的
                    //.sorted(Comparator.comparing(TopSearchCommonVO::getHotScore).reversed())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取百度热搜失败",e);
            throw new ServerException(ServerExceptionEnum.BAIDU_TOP_SEARCH_EXCEPTION);
        }
        return ResultTemplateBean.success(topSearchCommonVOS);
    }

    /**
     * description: b站热搜
     * method: getBilibiliTopSearch
     *
     * @author tgmeng
     * @since 2025/6/29 15:37
    */
    @Override
    public ResultTemplateBean getBilibiliTopSearch() {
        List<TopSearchCommonVO> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchBilibiliDTO topSearchBilibiliDTO = topSearchCommonClient.bilibili(ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.BILIBILI.getValue(), ForestRequestHeaderOriginEnum.BILIBILI.getValue()));
            System.out.println(topSearchBilibiliDTO);
            topSearchCommonVOS = Optional.ofNullable(topSearchBilibiliDTO.getData())
                    .map(TopSearchBilibiliDTO.DataView::getList)
                    .orElse(Collections.emptyList())
                    .stream().map(topSearchCommonMapper::topSearchBilibiliDTODataVO2TopSearchCommonVO)
                    .toList();
        } catch (Exception e) {
            log.error("获取B站热搜失败",e);
            throw new ServerException(ServerExceptionEnum.BILIBILI_TOP_SEARCH_EXCEPTION);
        }
        return ResultTemplateBean.success(topSearchCommonVOS);
    }

    /**
     * description: 微博热搜
     * method: getWeiBoTopSearch
     *
     * @author tgmeng
     * @since 2025/6/29 19:24
    */
    @Override
    public ResultTemplateBean getWeiBoTopSearch() {
        List<TopSearchCommonVO> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchWeiBoDTO topSearchWeiBoDTO = topSearchCommonClient.weiBo(ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.WEIBO.getValue(), ForestRequestHeaderOriginEnum.WEIBO.getValue()));
            //添加置顶
            topSearchCommonVOS.add(new TopSearchCommonVO().setKeyword(topSearchWeiBoDTO.getData().getHotgov().getWord()).setUrl(topSearchWeiBoDTO.getData().getHotgov().getUrl()));
            //添加热榜
            topSearchCommonVOS.addAll(Optional.ofNullable(topSearchWeiBoDTO.getData())
                    .map(TopSearchWeiBoDTO.DataView::getBand_list)
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(t->{
                        Long realPos = Optional.ofNullable(t.getRealpos()).orElse(0L);
                        t.setUrl(StringUtil.weiBoTopSearchItemUrlUtil(t.getWordScheme(), realPos));
                        return t;})
                    .map(topSearchCommonMapper::topSearchWeiBoDTODataVO2TopSearchCommonVO)
                    .toList())
                    ;
        } catch (Exception e) {
            log.error("获取微博热搜失败",e);
            throw new ServerException(ServerExceptionEnum.WEIBO_TOP_SEARCH_EXCEPTION);
        }
        return ResultTemplateBean.success(topSearchCommonVOS);
    }

    /**
     * description: 抖音热搜
     * method: getDouYinTopSearch
     *
     * @author tgmeng
     * @since 2025/6/29 22:40
    */
    @Override
    public ResultTemplateBean getDouYinTopSearch() {
        List<TopSearchCommonVO> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchDouYinDTO topSearchDouYinDTO = topSearchCommonClient.douYin(ForestUtil.getRandomRequestHeaderForDouYin());
            topSearchCommonVOS = Optional.ofNullable(topSearchDouYinDTO.getData())
                    .map(TopSearchDouYinDTO.DataView::getWordList)
                    .orElse(Collections.emptyList())
                    .stream().map(topSearchCommonMapper::topSearchDouYinDTODataVO2TopSearchCommonVO)
                    .toList();
        } catch (Exception e) {
            log.error("获取抖音热搜失败",e);
            throw new ServerException(ServerExceptionEnum.DOUYIN_TOP_SEARCH_EXCEPTION);
        }
        return ResultTemplateBean.success(topSearchCommonVOS);
    }
}
