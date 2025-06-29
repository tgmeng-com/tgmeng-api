package com.tgmeng.service.topsearch.china.Impl;

import com.tgmeng.common.Enum.ForestRequestHeaderOriginEnum;
import com.tgmeng.common.Enum.ForestRequestHeaderRefererEnum;
import com.tgmeng.common.forest.client.topsearch.TopSearchChinaClient;
import com.tgmeng.common.mapstruct.topsearch.ITopSearchChinaMapper;
import com.tgmeng.common.util.ForestUtil;
import com.tgmeng.model.vo.topsearch.TopSearchVO;
import com.tgmeng.model.vo.topsearch.china.TopSearchBaiDuResVO;
import com.tgmeng.model.vo.topsearch.china.TopSearchBilibiliResVO;
import com.tgmeng.model.vo.topsearch.china.TopSearchWeiBoResVO;
import com.tgmeng.service.topsearch.china.ISocialMediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ISocialMediaServiceImpl implements ISocialMediaService {

    private final TopSearchChinaClient topSearchChinaClient;
    private final ITopSearchChinaMapper topSearchChinaMapper;

    /**
     * description: 百度热搜
     * method: getBaiDuTopSearch
     *
     * @author tgmeng
     * @since 2025/6/29 15:17
    */
    @Override
    public List<TopSearchVO> getBaiDuTopSearch() {
        List<TopSearchVO> topSearchVOS = new ArrayList<>();
        try {
            TopSearchBaiDuResVO baiDu = topSearchChinaClient.baiDu(ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.BAIDU.getValue(), ForestRequestHeaderOriginEnum.BAIDU.getValue()));
            topSearchVOS = Optional.ofNullable(baiDu.getData())
                    .map(TopSearchBaiDuResVO.DataVO::getCards)
                    .orElse(Collections.emptyList())
                    .stream()
                    .filter(cardsVO -> cardsVO.getContent() != null)
                    .flatMap(cardsVO -> cardsVO.getContent().stream())
                    .map(topSearchChinaMapper::topSearchBaiDuResVOContentVO2TopSearchVO)
                    //这里不用排序，因为百度的热搜排序不是按照score排，是按照他返回的顺序排的
                    //.sorted(Comparator.comparing(TopSearchVO::getHotScore).reversed())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取百度热搜失败",e);
            return topSearchVOS;
        }
        return topSearchVOS;
    }

    /**
     * description: b站热搜
     * method: getBilibiliTopSearch
     *
     * @author tgmeng
     * @since 2025/6/29 15:37
    */
    @Override
    public List<TopSearchVO> getBilibiliTopSearch() {
        List<TopSearchVO> topSearchVOS = new ArrayList<>();
        try {
            TopSearchBilibiliResVO bilibili = topSearchChinaClient.bilibili(ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.BILIBILI.getValue(), ForestRequestHeaderOriginEnum.BILIBILI.getValue()));
            System.out.println(bilibili);
            topSearchVOS = Optional.ofNullable(bilibili.getData())
                    .map(TopSearchBilibiliResVO.DataView::getList)
                    .orElse(Collections.emptyList())
                    .stream().map(topSearchChinaMapper::topSearchBilibiliResVODataVO2TopSearchVO)
                    .toList();
        } catch (Exception e) {
            log.error("获取B站热搜失败",e);
            return topSearchVOS;
        }
        return topSearchVOS;
    }

    /**
     * description: 微博热搜
     * method: getWeiBoTopSearch
     *
     * @author tgmeng
     * @since 2025/6/29 19:24
    */
    @Override
    public List<TopSearchVO> getWeiBoTopSearch() {
        List<TopSearchVO> topSearchVOS = new ArrayList<>();
        try {
            TopSearchWeiBoResVO topSearchWeiBoResVO = topSearchChinaClient.weiBo(ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.WEIBO.getValue(), ForestRequestHeaderOriginEnum.WEIBO.getValue()));
            topSearchVOS = Optional.ofNullable(topSearchWeiBoResVO.getData())
                    .map(TopSearchWeiBoResVO.DataView::getBand_list)
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(t->t.setUrl("https://s.weibo.com/weibo?q="+t.getWordScheme()+"&t=31&band_rank="+t.getRealpos()+"&Refer=top"))
                    .map(topSearchChinaMapper::topSearchWeiBoResVODataVO2TopSearchVO)
                    .toList();
        } catch (Exception e) {
            log.error("获取微博热搜失败",e);
            return topSearchVOS;
        }
        return topSearchVOS;
    }
}
