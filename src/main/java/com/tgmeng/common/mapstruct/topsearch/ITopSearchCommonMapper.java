package com.tgmeng.common.mapstruct.topsearch;

import com.tgmeng.common.util.StringUtil;
import com.tgmeng.model.dto.topsearch.TopSearchBaiDuDTO;
import com.tgmeng.model.dto.topsearch.TopSearchBilibiliDTO;
import com.tgmeng.model.dto.topsearch.TopSearchDouYinDTO;
import com.tgmeng.model.dto.topsearch.TopSearchWeiBoDTO;
import com.tgmeng.model.vo.topsearch.TopSearchCommonVO;
import org.mapstruct.*;

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
    @Mapping(source = "hotScore", target = "hotScore",qualifiedByName = "stringToLong")
    @Mapping(source = "url", target = "url")
    @Mapping(source = "img", target = "image")
    TopSearchCommonVO topSearchBaiDuDTOContentVO2TopSearchCommonVO(TopSearchBaiDuDTO.ContentVO topSearchBaiDuDTOcontentVO);

    /** 哔哩哔哩 */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "title", target = "keyword")
    @Mapping(source = "stat.view", target = "hotScore")
    @Mapping(source = "short_link_v2", target = "url")
    @Mapping(source = "pic", target = "image")
    TopSearchCommonVO topSearchBilibiliDTODataVO2TopSearchCommonVO(TopSearchBilibiliDTO.DataVO topSearchBilibiliDTODataVO);

    /** 微博 */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "note", target = "keyword")
    @Mapping(source = "num", target = "hotScore")
    @Mapping(source = "url", target = "url")
    TopSearchCommonVO topSearchWeiBoDTODataVO2TopSearchCommonVO(TopSearchWeiBoDTO.DataVO topSearchWeiBoDTODataVO);

    /** 抖音 */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "word", target = "keyword")
    @Mapping(source = "hotValue", target = "hotScore")
    TopSearchCommonVO topSearchDouYinDTODataVO2TopSearchCommonVO(TopSearchDouYinDTO.DataVO topSearchDouYinDTODataVO);
    @AfterMapping
    default void topSearchDouYinDTODataVO2TopSearchCommonVOAfter(TopSearchDouYinDTO.DataVO topSearchDouYinDTODataVO, @MappingTarget TopSearchCommonVO topSearchCommonVO) {
        // @MappingTarget : 表示传来的carVO对象是已经赋值过的
        topSearchCommonVO.setUrl(StringUtil.douYinTopSearchItemUrlUtil(topSearchDouYinDTODataVO.getSentenceId(),topSearchDouYinDTODataVO.getWord()));

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
