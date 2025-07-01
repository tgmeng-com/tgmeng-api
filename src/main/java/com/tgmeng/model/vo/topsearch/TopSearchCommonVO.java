package com.tgmeng.model.vo.topsearch;

import com.tgmeng.common.util.TimeUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * description: 热搜实体类
 * package: com.tgmeng.model.vo.topsearch
 * className: TopSearchCommonVO
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/6/29 1:31
*/
@Data
@Accessors(chain = true)
public class TopSearchCommonVO {

    /** 数据卡片名称 比如百度*/
    private String dataCardName;
    /** 数据卡片logo 比如百度的logo地址*/
    private String dataCardLogo;
    /** 数据卡片分类 比如新闻*/
    private String dataCardCategory;
    private Long dataSize;
    private String dataUpdateTime ;
    private List<DataInfo> dataInfo;

    @Data
    public static  class DataInfo {
        /** 热搜词 */
        private String keyword;
        /** 给前端格式化的热度，比如“1.2万” */
        private Long hotScore;
        /** 热搜词的url */
        private String url;
        /** 图片 */
        private String image;
    }

    public TopSearchCommonVO(List<TopSearchCommonVO.DataInfo> dataInfo,String dataCardName,String dataCardLogo,String dataCardCategory){
        this.dataInfo = dataInfo;
        this.dataCardName = dataCardName;
        this.dataCardLogo = dataCardLogo;
        this.dataCardCategory = dataCardCategory;
        this.setDataSize((long)dataInfo.size());
        this.setDataUpdateTime(TimeUtil.getCurrentTimeFormat("yyyy-MM-dd HH:mm:ss"));
    }
}
