package com.tgmeng.common.mapper.mapstruct.topsearch;

import com.tgmeng.model.dto.topsearch.TopSearchGitHubDTO;
import com.tgmeng.model.vo.topsearch.TopSearchCommonVO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
public interface ITopSearchGitHubMapper {

    /** GitHub */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "name", target = "keyword")
    @Mapping(source = "stargazersCount", target = "hotScore")
    @Mapping(source = "htmlUrl", target = "url")
    TopSearchCommonVO.DataInfo topSearchGitHubDTO2TopSearchCommonVO(TopSearchGitHubDTO.ItemDTO topSearchGitHubDTO);
}