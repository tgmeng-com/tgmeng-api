package com.tgmeng.common.mapper.mapstruct.topsearch;

import com.tgmeng.common.util.StringUtil;
import com.tgmeng.model.dto.topsearch.TopSearchYoutubeDTO;
import com.tgmeng.model.vo.topsearch.TopSearchCommonVO;
import org.mapstruct.*;

/**
 * description: TopSearch的Mapper转换接口
 * package: com.tgmeng.mapper.mapstruct.topsearch
 * className: ITopSearchGitHubMapper
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/6/29 1:38
*/

@Mapper(componentModel = "spring")
public interface ITopSearchGlobalMapper {

    /** GitHub */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "snippet.title", target = "keyword")
    @Mapping(source = "statistics.viewCount", target = "hotScore")
    @Mapping(source = "id", target = "url")
    TopSearchCommonVO.DataInfo topSearchYoutubeDTO2TopSearchCommonVO(TopSearchYoutubeDTO.ItemDTO topSearchYoutubeDTO);

    @AfterMapping
    default void topSearchYoutubeDTODataVO2TopSearchCommonVOAfter(TopSearchYoutubeDTO.ItemDTO topSearchYoutubeDTO, @MappingTarget TopSearchCommonVO.DataInfo topSearchCommonVO) {
        // @MappingTarget : 表示传来的carVO对象是已经赋值过的
        topSearchCommonVO.setUrl(StringUtil.youtubeTopSearchItemUrlUtil(topSearchYoutubeDTO.getId()));

    }

}