package com.tgmeng.common.mapstruct.topsearch;

import com.tgmeng.common.util.StringUtil;
import com.tgmeng.model.dto.topsearch.TopSearchBaiDuDTO;
import com.tgmeng.model.dto.topsearch.TopSearchBilibiliDTO;
import com.tgmeng.model.dto.topsearch.TopSearchDouYinDTO;
import com.tgmeng.model.vo.topsearch.TopSearchVO;
import com.tgmeng.model.dto.topsearch.TopSearchWeiBoDTO;
import org.mapstruct.*;

import java.util.List;

/**
 * description: TopSearch的Mapper转换接口
 * package: com.tgmeng.mapper.mapstruct.topsearch
 * className: ITopSearchChinaMapper
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/6/29 1:38
*/
@Mapper(componentModel = "spring")
public interface ITopSearchChinaMapper {

    /** 百度 */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "word", target = "keyword")
    @Mapping(source = "hotScore", target = "hotScore",qualifiedByName = "stringToLong")
    @Mapping(source = "url", target = "url")
    @Mapping(source = "img", target = "image")
    TopSearchVO topSearchBaiDuResVOContentVO2TopSearchVO(TopSearchBaiDuDTO.ContentVO topSearchBaiDuResVOcontentVO);
    /** 批量转换 */
    List<TopSearchVO> topSearchBaiDuResVOContentVOList2TopSearchVOList(List<TopSearchBaiDuDTO.ContentVO> topSearchBaiDuResVOcontentVOList);


    /** 哔哩哔哩 */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "title", target = "keyword")
    @Mapping(source = "stat.view", target = "hotScore")
    @Mapping(source = "short_link_v2", target = "url")
    @Mapping(source = "pic", target = "image")
    TopSearchVO topSearchBilibiliResVODataVO2TopSearchVO(TopSearchBilibiliDTO.DataVO topSearchBilibiliResVODataVO);


    /** 微博 */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "note", target = "keyword")
    @Mapping(source = "num", target = "hotScore")
    @Mapping(source = "url", target = "url")
    TopSearchVO topSearchWeiBoResVODataVO2TopSearchVO(TopSearchWeiBoDTO.DataVO topSearchWeiBoResVODataVO);

    /** 抖音 */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "word", target = "keyword")
    @Mapping(source = "hotValue", target = "hotScore")
    TopSearchVO topSearchDouYinResVODataVO2TopSearchVO(TopSearchDouYinDTO.DataVO topSearchDouYinResVODataVO);
    @AfterMapping
    default void topSearchDouYinResVODataVO2TopSearchVOAfter(TopSearchDouYinDTO.DataVO topSearchDouYinResVODataVO, @MappingTarget TopSearchVO topSearchVO) {
        // @MappingTarget : 表示传来的carVO对象是已经赋值过的
        topSearchVO.setUrl(StringUtil.douYinTopSearchItemUrlUtil(topSearchDouYinResVODataVO.getSentenceId(),topSearchDouYinResVODataVO.getWord()));

    }

    @Named("stringToLong")
    default Long stringToLong(String score) {
        try {
            return Long.parseLong(score);
        } catch (Exception e) {
            return 0L;
        }
    }


}
