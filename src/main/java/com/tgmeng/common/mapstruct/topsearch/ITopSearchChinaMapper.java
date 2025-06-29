package com.tgmeng.common.mapstruct.topsearch;

import com.tgmeng.model.vo.topsearch.TopSearchVO;
import com.tgmeng.model.vo.topsearch.china.TopSearchBaiDuResVO;
import com.tgmeng.model.vo.topsearch.china.TopSearchBilibiliResVO;
import com.tgmeng.model.vo.topsearch.china.TopSearchWeiBoResVO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

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
    @Mapping(source = "hotScore", target = "hotScore",qualifiedByName = "stringToDouble")
    @Mapping(source = "url", target = "url")
    @Mapping(source = "img", target = "image")
    TopSearchVO topSearchBaiDuResVOContentVO2TopSearchVO(TopSearchBaiDuResVO.ContentVO topSearchBaiDuResVOcontentVO);
    /** 批量转换 */
    List<TopSearchVO> topSearchBaiDuResVOContentVOList2TopSearchVOList(List<TopSearchBaiDuResVO.ContentVO> topSearchBaiDuResVOcontentVOList);


    /** 哔哩哔哩 */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "title", target = "keyword")
    @Mapping(source = "stat.view", target = "hotScore")
    @Mapping(source = "short_link_v2", target = "url")
    @Mapping(source = "pic", target = "image")
    TopSearchVO topSearchBilibiliResVODataVO2TopSearchVO(TopSearchBilibiliResVO.DataVO topSearchBilibiliResVODataVO);


    /** 微博 */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "note", target = "keyword")
    @Mapping(source = "num", target = "hotScore")
    @Mapping(source = "url", target = "url")
    TopSearchVO topSearchWeiBoResVODataVO2TopSearchVO(TopSearchWeiBoResVO.DataVO topSearchWeiBoResVODataVO);


    @Named("stringToDouble")
    default double stringToDouble(String score) {
        try {
            return Double.parseDouble(score);
        } catch (Exception e) {
            return 0.0;
        }
    }


}
