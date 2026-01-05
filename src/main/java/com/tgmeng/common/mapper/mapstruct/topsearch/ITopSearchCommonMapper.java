package com.tgmeng.common.mapper.mapstruct.topsearch;

import com.tgmeng.model.dto.ai.response.AiChatModelResponseMessageContentForHotSearchDTO;
import com.tgmeng.model.vo.topsearch.TopSearchCommonVO;
import org.mapstruct.BeanMapping;
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
public interface ITopSearchCommonMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "title", target = "title")
    @Mapping(source = "hotScore", target = "hotScore")
    TopSearchCommonVO.DataInfo tgmengHotSearch2TopSearchCommonVo(AiChatModelResponseMessageContentForHotSearchDTO.DataInfo dataInfo);

    /** 批量转换 */
    List<TopSearchCommonVO.DataInfo> tgmengHotSearch2TopSearchCommonVos(List<AiChatModelResponseMessageContentForHotSearchDTO.DataInfo> dataInfos);

}
