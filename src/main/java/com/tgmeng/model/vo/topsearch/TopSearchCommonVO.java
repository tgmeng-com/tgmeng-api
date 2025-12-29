package com.tgmeng.model.vo.topsearch;

import com.tgmeng.common.util.TimeUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private String platformName;
    /** 数据卡片logo 比如百度的logo地址*/
    private String platformLogo;
    /** 数据卡片分类 比如新闻*/
    private String platformCategory;
    private String platformCategoryRoot;
    private Long dataSize;
    private String dataUpdateTime ;
    private List<DataInfo> dataInfo;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static  class DataInfo<T> {
        /** 热搜词 */
        private String title;
        /** 给前端格式化的热度，比如“1.2万” */
        private T hotScore;
        /** 热搜词的url */
        private String url;
        /** 图片 */
        private String image;
        /** 作者/演员 */
        private String author;
        /** 描述 */
        private String desc;
        /** 类型 */
        private String type;
        /** 发表/上映时间 */
        private String publishTime;
        /** 评论数 */
        private String commentCount;

        private Long startTime;
        private Long endTime;
        private String showTime;

    }

    public TopSearchCommonVO(List<TopSearchCommonVO.DataInfo> dataInfo,String platformName,String platformLogo,String platformCategory,String platformCategoryRoot){
        this.platformName = platformName;
        this.platformLogo = platformLogo;
        this.platformCategory = platformCategory;
        this.platformCategoryRoot = platformCategoryRoot;
        this.setDataSize((long)dataInfo.size());
        this.setDataUpdateTime(TimeUtil.getCurrentTimeFormat("yyyy-MM-dd HH:mm:ss"));
        this.dataInfo = dataInfo;
    }
}
