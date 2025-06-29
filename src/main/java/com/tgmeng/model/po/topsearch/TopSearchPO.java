package com.tgmeng.model.po.topsearch;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * description: 热搜实体类
 * package: com.tgmeng.model.po.topsearch
 * className: TopSearchPO
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/6/29 1:31
*/
@Data
@Accessors(chain = true)
public class TopSearchPO {
    /** 热搜词id */
    private Integer id;
    /** 热搜词 */
    private String keyword;
    /** 给前端格式化的热度，比如“1.2万” */
    private Double hotScore;
    /** 热搜词的url */
    private String url;
    /** 图片 */
    private String image;
    /** 热搜词的热度 */
    private String updateTime;
}
