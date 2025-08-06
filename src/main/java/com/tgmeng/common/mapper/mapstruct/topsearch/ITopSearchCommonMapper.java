package com.tgmeng.common.mapper.mapstruct.topsearch;

import com.tgmeng.common.util.StringUtil;
import com.tgmeng.model.dto.topsearch.*;
import com.tgmeng.model.vo.topsearch.TopSearchCommonVO;
import org.mapstruct.*;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * description: TopSearch的Mapper转换接口
 * package: com.tgmeng.mapper.mapstruct.topsearch
 * className: ITopSearchMapper
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/6/29 1:38
 */


@Mapper(componentModel = "spring")
public interface ITopSearchCommonMapper {

    /** 百度 */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "word", target = "keyword")
    @Mapping(source = "hotScore", target = "hotScore", qualifiedByName = "stringToLong")
    @Mapping(source = "url", target = "url")
    @Mapping(source = "img", target = "image")
    TopSearchCommonVO.DataInfo topSearchBaiDuDTOContentVO2TopSearchCommonVO(TopSearchBaiDuDTO.ContentVO topSearchBaiDuDTOcontentVO);

    @AfterMapping
    default void topSearchBaiDuDTODataVO2TopSearchCommonVOAfter(TopSearchBaiDuDTO.ContentVO topSearchBaiDuDTOContentVO, @MappingTarget TopSearchCommonVO.DataInfo topSearchCommonVO) {
        topSearchCommonVO.setUrl(topSearchBaiDuDTOContentVO.getUrl().replace("https://m.baidu.com/", "https://baidu.com/"));
    }

    /** 哔哩哔哩 */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "title", target = "keyword")
    @Mapping(source = "stat.view", target = "hotScore")
    @Mapping(source = "short_link_v2", target = "url")
    @Mapping(source = "pic", target = "image")
    TopSearchCommonVO.DataInfo topSearchBilibiliDTODataVO2TopSearchCommonVO(TopSearchBilibiliDTO.DataVO topSearchBilibiliDTODataVO);

    /** 微博 */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "note", target = "keyword")
    @Mapping(source = "num", target = "hotScore")
    @Mapping(source = "url", target = "url")
    TopSearchCommonVO.DataInfo topSearchWeiBoDTODataVO2TopSearchCommonVO(TopSearchWeiBoDTO.DataVO topSearchWeiBoDTODataVO);

    /** 抖音 */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "word", target = "keyword")
    @Mapping(source = "hotValue", target = "hotScore")
    TopSearchCommonVO.DataInfo topSearchDouYinDTODataVO2TopSearchCommonVO(TopSearchDouYinDTO.DataVO topSearchDouYinDTODataVO);

    @AfterMapping
    default void topSearchDouYinDTODataVO2TopSearchCommonVOAfter(TopSearchDouYinDTO.DataVO topSearchDouYinDTODataVO, @MappingTarget TopSearchCommonVO.DataInfo topSearchCommonVO) {
        topSearchCommonVO.setUrl(StringUtil.douYinTopSearchItemUrlUtil(topSearchDouYinDTODataVO.getSentenceId(), topSearchDouYinDTODataVO.getWord()));
    }


    /** 豆瓣 */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "name", target = "keyword")
    @Mapping(source = "score", target = "hotScore")
    @Mapping(source = "uri", target = "url")
    TopSearchCommonVO.DataInfo topSearchDouBanDTODataVO2TopSearchCommonVO(TopSearchDouBanDTO topSearchDouBanDTO);

    @AfterMapping
    default void topSearchDouBanDTODataInfo2TopSearchCommonVOAfter(TopSearchDouBanDTO topSearchDouBanDTO, @MappingTarget TopSearchCommonVO.DataInfo topSearchCommonVO) {
        topSearchCommonVO.setUrl(topSearchDouBanDTO.getUri().replace("douban://douban.com/search/result?q=", "https://douban.com/search?q="));
    }

    /** 批量转换 */
    List<TopSearchCommonVO.DataInfo> topSearchDouBanDTODataVO2TopSearchCommonVOS(List<TopSearchDouBanDTO> topSearchDouBanDTO);

    /** 腾讯 */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "title", target = "keyword")
    @Mapping(source = "readCount", target = "hotScore")
    @Mapping(source = "url", target = "url")
    TopSearchCommonVO.DataInfo topSearchTencentDTOItemInfoTopSearchCommonVO(TopSearchTencentDTO.ItemInfo topSearchDTencentDTO);

    /** 头条 */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "title", target = "keyword")
    @Mapping(source = "hotValue", target = "hotScore")
    @Mapping(source = "url", target = "url")
    TopSearchCommonVO.DataInfo topSearchTouTiaoDTOItemInfoTopSearchCommonVO(TopSearchTouTiaoDTO.ItemInfo topSearchTouTiaoDTO);

    /** 网易 */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "title", target = "keyword")
    @Mapping(source = "hotValue", target = "hotScore")
    @Mapping(source = "contentId", target = "url")
    TopSearchCommonVO.DataInfo topSearchWangYiDTOItemInfoTopSearchCommonVO(TopSearchWangYiDTO.DataView topSearchWangYiDTO);

    @AfterMapping
    default void topSearchWangYiDTODataVO2TopSearchCommonVOAfter(TopSearchWangYiDTO.DataView topSearchWangYiDTO, @MappingTarget TopSearchCommonVO.DataInfo topSearchCommonVO) {
        topSearchCommonVO.setUrl(StringUtil.wangYiTopSearchItemUrlUtil(topSearchWangYiDTO.getContentId()));
    }

    /** 网易云 */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "name", target = "keyword")
    @Mapping(source = "popularity", target = "hotScore")
    @Mapping(source = "id", target = "url")
    TopSearchCommonVO.DataInfo topSearchWangYiYunDTOItemInfoTopSearchCommonVO(TopSearchWangYiYunDTO.DataInfo topSearchWangYiYunDTO);

    @AfterMapping
    default void topSearchWangYiYunDTOItemInfoTopSearchCommonVOAfter(TopSearchWangYiYunDTO.DataInfo topSearchWangYiYunDTO, @MappingTarget TopSearchCommonVO.DataInfo topSearchCommonVO) {
        topSearchCommonVO.setUrl(StringUtil.wangYiYunTopSearchItemUrlUtil(topSearchWangYiYunDTO.getId()));
        topSearchCommonVO.setImage(
                new StringJoiner(" ")
                        .add(topSearchWangYiYunDTO.getArtists().stream().map(TopSearchWangYiYunDTO.Artists::getName).collect(Collectors.joining("/")))
                        .toString());
    }

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "topicName", target = "keyword")
    @Mapping(source = "discussNum", target = "hotScore")
    @Mapping(source = "topicUrl", target = "url")
    TopSearchCommonVO.DataInfo topSearchBaiDuTieBaDTOItemInfoTopSearchCommonVO(TopSearchBaiDuTieBaDTO.DataInfo topSearchBaiDuTieBaDTO);


    /** 少数派 */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "title", target = "keyword")
    @Mapping(source = "commentCount", target = "hotScore")
    @Mapping(source = "id", target = "url")
    TopSearchCommonVO.DataInfo topSearchShaoShuPaiDTOItemInfoTopSearchCommonVO(TopSearchShaoShuPaiDTO.ItemDTO shaoShuPaiDTO);

    @AfterMapping
    default void topSearchShaoShuPaiDTOItemInfoTopSearchCommonVOAfter(TopSearchShaoShuPaiDTO.ItemDTO topSearchShaoShuPaiItemDTO, @MappingTarget TopSearchCommonVO.DataInfo topSearchCommonVO) {
        topSearchCommonVO.setUrl(StringUtil.shaoShuPaiTopSearchItemUrlUtil(topSearchShaoShuPaiItemDTO.getId()));
        topSearchCommonVO.setHotScore(StringUtil.shaoShuPaiTopSearchItemHotScoreUtil(topSearchShaoShuPaiItemDTO));
    }

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "zhiHuDTO.title", target = "keyword")
    @Mapping(source = "zhiHuDTO.id", target = "url")
    TopSearchCommonVO.DataInfo topSearchZhiHuDTOItemInfoTopSearchCommonVO(TopSearchZhiHuDTO.ItemDTO zhiHuDTO, TopSearchZhiHuDTO.DataInfo dataInfo);

    @AfterMapping
    default void topSearchZhiHuDTOItemInfoTopSearchCommonVOAfter(TopSearchZhiHuDTO.ItemDTO zhiHuDTO, TopSearchZhiHuDTO.DataInfo dataInfo, @MappingTarget TopSearchCommonVO.DataInfo topSearchCommonVO) {
        topSearchCommonVO.setHotScore(StringUtil.zhiHuTopSearchItemHotScoreUtil(dataInfo));
        topSearchCommonVO.setUrl("https://www.zhihu.com/question/" + zhiHuDTO.getId());
    }

    @Named("stringToLong")
    default Long stringToLong(String score) {
        try {
            return Long.parseLong(score);
        } catch (Exception e) {
            return 0L;
        }
    }

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "title", target = "keyword")
    @Mapping(source = "pageUrl", target = "url")
    @Mapping(source = "mainIndex", target = "hotScore")
    TopSearchCommonVO.DataInfo topSearchAiQiYiDTOContentVO2TopSearchCommonVO(TopSearchAiQiYiDTO.Contents aiQiYiContents);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "title", target = "keyword")
    @Mapping(source = "encodeShowId", target = "url")
        //@Mapping(source = "searchIndexValue", target = "hotScore")
    TopSearchCommonVO.DataInfo topSearchYouKuDTOContentVO2TopSearchCommonVO(TopSearchYouKuDTO.FinalData youKuDTO);

    @AfterMapping
    default void topSearchYouKuDTOContentVO2TopSearchCommonVOAfter(TopSearchYouKuDTO.FinalData youKuDTO, @MappingTarget TopSearchCommonVO.DataInfo topSearchCommonVO) {
        topSearchCommonVO.setHotScore(StringUtil.youKuTopSearchItemHotScoreUtil(youKuDTO.getSearchIndexValue()));
        topSearchCommonVO.setUrl("https://v.youku.com/v_show/id_XNjQ1NDI4ODY2MA==.html?s=" + youKuDTO.getEncodeShowId());
    }

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "name", target = "keyword")
    TopSearchCommonVO.DataInfo topSearchMangGuoDTOContentVO2TopSearchCommonVO(TopSearchMangGuoDTO.FinalData mangGuoDTO);

    @AfterMapping
    default void topSearchMangGuoDTOContentVO2TopSearchCommonVOAfter(TopSearchMangGuoDTO.FinalData mangGuoDTO, @MappingTarget TopSearchCommonVO.DataInfo topSearchCommonVO) {
        topSearchCommonVO.setHotScore(null);
        topSearchCommonVO.setUrl("https://so.mgtv.com/so?k=" + mangGuoDTO.getName());
    }

}
