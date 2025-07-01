package com.tgmeng.common.mapper.mapstruct.topsearch;

import com.tgmeng.model.dto.topsearch.TopSearchGitHubDTO;
import com.tgmeng.model.vo.topsearch.TopSearchGitHubVO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

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
    @Mapping(source = "name", target = "projectName")
    @Mapping(source = "htmlUrl", target = "projectUrl")
    @Mapping(source = "owner.avatarUrl", target = "avatarUrl") // 头像在 Owner 类中
    @Mapping(source = "stargazersCount", target = "stargazersCount")
    @Mapping(source = "forksCount", target = "forksCount")
    @Mapping(source = "watchersCount", target = "watchersCount")
    @Mapping(source = "openIssuesCount", target = "openIssuesCount")
    @Mapping(source = "updatedAt", target = "updatedAt")
    TopSearchGitHubVO topSearchGitHubDTO2TopSearchGitHubVO(TopSearchGitHubDTO.ItemDTO topSearchGitHubDTO);
    /** 批量转换 */
    List<TopSearchGitHubVO> topSearchGitHubDTOS2TopSearchGitHubVOS(List<TopSearchGitHubDTO.ItemDTO> topSearchGitHubDTO);
}