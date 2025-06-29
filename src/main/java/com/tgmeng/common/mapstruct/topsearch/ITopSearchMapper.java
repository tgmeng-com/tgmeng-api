package com.tgmeng.common.mapstruct.topsearch;

import com.tgmeng.model.dto.topsearch.TopSearchDTO;
import com.tgmeng.model.vo.topsearch.TopSearchVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

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
public interface ITopSearchMapper {

    /** topSearchDTO2TopSearchVO */
    @Mapping(source = "keyword", target = "keyword")
    @Mapping(source = "hotScore", target = "hotScore")
    @Mapping(source = "url", target = "url")
    @Mapping(source = "image", target = "image")
    TopSearchVO topSearchDTO2TopSearchVO(TopSearchDTO topSearchDTO);
    /** 批量转换 */
    List<TopSearchVO> topSearchDTOList2TopSearchVOList(List<TopSearchDTO> topSearchDTOList);

}
