package com.tgmeng.service.topsearch.Impl;

import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.cache.TopSearchDataCache;
import com.tgmeng.common.enums.business.BaiDuSearchTypeEnum;
import com.tgmeng.common.enums.business.DataInfoCardEnum;
import com.tgmeng.common.enums.business.ForestRequestHeaderOriginEnum;
import com.tgmeng.common.enums.business.ForestRequestHeaderRefererEnum;
import com.tgmeng.common.enums.exception.ServerExceptionEnum;
import com.tgmeng.common.exception.ServerException;
import com.tgmeng.common.forest.client.topsearch.ITopSearchCommonClient;
import com.tgmeng.common.mapper.mapstruct.topsearch.ITopSearchCommonMapper;
import com.tgmeng.common.util.ForestUtil;
import com.tgmeng.common.util.StringUtil;
import com.tgmeng.model.dto.topsearch.*;
import com.tgmeng.model.vo.topsearch.TopSearchCommonVO;
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
    private final TopSearchDataCache topSearchDataCache;

    /**
     * description: 百度热搜
     * method: getBaiDuTopSearch
     *
     * @author tgmeng
     * @since 2025/6/29 15:17
    */
    @Override
    public ResultTemplateBean getBaiDuTopSearch(BaiDuSearchTypeEnum baiDuSearchTypeEnum) {
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchBaiDuDTO topSearchBaiDuDTO = topSearchCommonClient.baiDu(ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.BAIDU.getValue(), ForestRequestHeaderOriginEnum.BAIDU.getValue()), baiDuSearchTypeEnum.getValue());
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
                    //.sorted(Comparator.comparing(topSearchCommonVOS::getHotScore).reversed())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取百度热搜失败",e);
            throw new ServerException(ServerExceptionEnum.BAIDU_TOP_SEARCH_EXCEPTION);
        }
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS,DataInfoCardEnum.BAIDU.getKey(), DataInfoCardEnum.BAIDU.getValue(),DataInfoCardEnum.BAIDU.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
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
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchBilibiliDTO topSearchBilibiliDTO = topSearchCommonClient.bilibili(ForestUtil.getRandomRequestHeaderForBilibili());
            topSearchCommonVOS = Optional.ofNullable(topSearchBilibiliDTO.getData())
                    .map(TopSearchBilibiliDTO.DataView::getList)
                    .orElse(Collections.emptyList())
                    .stream().map(topSearchCommonMapper::topSearchBilibiliDTODataVO2TopSearchCommonVO)
                    .toList();
        } catch (Exception e) {
            log.error("获取B站热搜失败",e);
            throw new ServerException(ServerExceptionEnum.BILIBILI_TOP_SEARCH_EXCEPTION);
        }
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS, DataInfoCardEnum.BILIBILI.getKey(), DataInfoCardEnum.BILIBILI.getValue(),DataInfoCardEnum.BILIBILI.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
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
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchWeiBoDTO topSearchWeiBoDTO = topSearchCommonClient.weiBo(ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.WEIBO.getValue(), ForestRequestHeaderOriginEnum.WEIBO.getValue()));
            //添加置顶
            topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo().setKeyword(topSearchWeiBoDTO.getData().getHotgov().getWord()).setUrl(topSearchWeiBoDTO.getData().getHotgov().getUrl()));
            //添加热榜
            topSearchCommonVOS.addAll(Optional.ofNullable(topSearchWeiBoDTO.getData())
                    .map(TopSearchWeiBoDTO.DataView::getBandList)
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
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS, DataInfoCardEnum.WEIBO.getKey(), DataInfoCardEnum.WEIBO.getValue(),DataInfoCardEnum.WEIBO.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
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
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
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
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS, DataInfoCardEnum.DOUYIN.getKey(), DataInfoCardEnum.DOUYIN.getValue(),DataInfoCardEnum.DOUYIN.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
    }

    @Override
    public ResultTemplateBean getDouBanTopSearch() {
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            List<TopSearchDouBanDTO> topSearchDouBanDTO = topSearchCommonClient.douBan(ForestUtil.getRandomRequestHeaderForDouBan());
            topSearchCommonVOS.addAll(topSearchCommonMapper.topSearchDouBanDTODataVO2TopSearchCommonVOS(topSearchDouBanDTO));
        } catch (Exception e) {
            log.error("获取豆瓣热搜失败",e);
            throw new ServerException(ServerExceptionEnum.DOUBAN_TOP_SEARCH_EXCEPTION);
        }
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS, DataInfoCardEnum.DOUBAN.getKey(), DataInfoCardEnum.DOUBAN.getValue(),DataInfoCardEnum.DOUBAN.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
    }

    @Override
    public ResultTemplateBean getTencentTopSearch() {
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchTencentDTO tencent = topSearchCommonClient.tencent(ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.TENCENT.getValue(), ForestRequestHeaderOriginEnum.TENCENT.getValue()));
            topSearchCommonVOS = Optional.ofNullable(tencent.getIdlist().getFirst())
                    .map(TopSearchTencentDTO.ItemView::getNewslist)
                    .orElse(Collections.emptyList())
                    .stream().map(topSearchCommonMapper::topSearchTencentDTOItemInfoTopSearchCommonVO)
                    .toList();
            // 移除第一条数据,他不是有效数据（如果存在）
            if (!topSearchCommonVOS.isEmpty()) {
                topSearchCommonVOS = new ArrayList<>(topSearchCommonVOS);
                topSearchCommonVOS.removeFirst();
            }
        } catch (Exception e) {
            log.error("获取腾讯新闻热搜失败",e);
            throw new ServerException(ServerExceptionEnum.TENCENT_TOP_SEARCH_EXCEPTION);
        }
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS, DataInfoCardEnum.TENCENT.getKey(), DataInfoCardEnum.TENCENT.getValue(),DataInfoCardEnum.TENCENT.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
    }

    @Override
    public ResultTemplateBean getTouTiaoTopSearch() {
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchTouTiaoDTO toutiao = topSearchCommonClient.toutiao(ForestUtil.getRandomRequestHeaderForTouTiao());
            topSearchCommonVOS = Stream.concat(
                    Optional.ofNullable(toutiao)
                            .map(TopSearchTouTiaoDTO::getFixedTopData)
                            .orElse(Collections.emptyList())
                            .stream()
                            .map(topSearchCommonMapper::topSearchTouTiaoDTOItemInfoTopSearchCommonVO),

                    Optional.ofNullable(toutiao)
                            .map(TopSearchTouTiaoDTO::getData)
                            .orElse(Collections.emptyList())
                            .stream()
                            .map(topSearchCommonMapper::topSearchTouTiaoDTOItemInfoTopSearchCommonVO)
            ).toList();
        } catch (Exception e) {
            log.error("获取今日头条热搜失败",e);
            throw new ServerException(ServerExceptionEnum.TOUTIAO_TOP_SEARCH_EXCEPTION);
        }
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS, DataInfoCardEnum.TOUTIAO.getKey(), DataInfoCardEnum.TOUTIAO.getValue(),DataInfoCardEnum.TOUTIAO.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
    }

    @Override
    public ResultTemplateBean getWangYiTopSearch() {
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchWangYiDTO wangyi = topSearchCommonClient.wangyi(ForestUtil.getRandomRequestHeaderForWangYi());
            topSearchCommonVOS = Optional.ofNullable(wangyi.getData())
                    .map(TopSearchWangYiDTO.ItemDTO::getItems)
                    .orElse(Collections.emptyList())
                    .stream().map(topSearchCommonMapper::topSearchWangYiDTOItemInfoTopSearchCommonVO)
                    .toList();
        } catch (Exception e) {
            log.error("获取网易新闻热搜失败",e);
            throw new ServerException(ServerExceptionEnum.WANGYI_TOP_SEARCH_EXCEPTION);
        }
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS, DataInfoCardEnum.WANGYI.getKey(), DataInfoCardEnum.WANGYI.getValue(),DataInfoCardEnum.WANGYI.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
    }

    @Override
    public ResultTemplateBean getWangYiYunTopSearch() {
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchWangYiYunDTO wangyiyun = topSearchCommonClient.wangyiyun(ForestUtil.getRandomRequestHeaderForWangYiYun());
            topSearchCommonVOS = Optional.ofNullable(wangyiyun.getResult())
                    .map(TopSearchWangYiYunDTO.DataDTO::getTracks)
                    .orElse(Collections.emptyList())
                    .stream().map(topSearchCommonMapper::topSearchWangYiYunDTOItemInfoTopSearchCommonVO)
                    .toList();
        } catch (Exception e) {
            log.error("获取网易新闻热搜失败",e);
            throw new ServerException(ServerExceptionEnum.WANGYIYUN_TOP_SEARCH_EXCEPTION);
        }
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS, DataInfoCardEnum.WANGYIYUN.getKey(), DataInfoCardEnum.WANGYIYUN.getValue(),DataInfoCardEnum.WANGYIYUN.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
    }

    @Override
    public ResultTemplateBean getBaiDuTieBaSearch() {
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchBaiDuTieBaDTO baidutieba = topSearchCommonClient.baidutieba(ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.BAIDUTIEBA.getValue(), ForestRequestHeaderOriginEnum.BAIDUTIEBA.getValue()));
            topSearchCommonVOS = Optional.ofNullable(baidutieba.getData().getBangTopic())
                    .map(TopSearchBaiDuTieBaDTO.ItemView::getTopicList)
                    .orElse(Collections.emptyList())
                    .stream().map(topSearchCommonMapper::topSearchBaiDuTieBaDTOItemInfoTopSearchCommonVO)
                    .toList();
        } catch (Exception e) {
            log.error("获取网易新闻热搜失败",e);
            throw new ServerException(ServerExceptionEnum.BAIDUTIEBA_TOP_SEARCH_EXCEPTION);
        }
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS, DataInfoCardEnum.BAIDUTIEBA.getKey(), DataInfoCardEnum.BAIDUTIEBA.getValue(),DataInfoCardEnum.BAIDUTIEBA.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
    }

    @Override
    public ResultTemplateBean getShaoShuPaiSearch() {
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchShaoShuPaiDTO shaoshupai = topSearchCommonClient.shaoshupai(ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.SHAOSHUPAI.getValue(), ForestRequestHeaderOriginEnum.SHAOSHUPAI.getValue()));
            topSearchCommonVOS = Optional.ofNullable(shaoshupai)
                    .map(TopSearchShaoShuPaiDTO::getData)
                    .orElse(Collections.emptyList())
                    .stream().map(topSearchCommonMapper::topSearchShaoShuPaiDTOItemInfoTopSearchCommonVO)
                    .toList();
        } catch (Exception e) {
            log.error("少数派热搜失败",e);
            throw new ServerException(ServerExceptionEnum.SHAOSHUPAI_TOP_SEARCH_EXCEPTION);
        }
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS, DataInfoCardEnum.SHAOSHUPAI.getKey(), DataInfoCardEnum.SHAOSHUPAI.getValue(),DataInfoCardEnum.SHAOSHUPAI.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
    }
}