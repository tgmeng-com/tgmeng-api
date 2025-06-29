package com.tgmeng.model.vo.topsearch;

import lombok.Data;
import lombok.experimental.Accessors;

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
    /** 热搜词 */
    private String keyword;
    /** 给前端格式化的热度，比如“1.2万” */
    private Long hotScore;
    /** 热搜词的url */
    private String url;
    /** 图片 */
    private String image;
    /** 热搜词的热度 */
    private String updateTime;
}
