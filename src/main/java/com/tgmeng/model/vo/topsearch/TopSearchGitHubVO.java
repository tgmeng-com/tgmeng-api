package com.tgmeng.model.vo.topsearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tgmeng.common.Enum.OperatorEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * description: github热榜VO
 * package: com.tgmeng.model.vo.topsearch
 * className: TopSearchGitHubVO
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/6/30 2:37
*/
@Data
@Accessors(chain = true)
@JsonIgnoreProperties(allowSetters = true, value = {
        "stars","starsOperator","forks","forksOperator","size","sizeOperator","created","createdOperator",
        "updated","updatedOperator","issues","issuesOperator","pulls","pullsOperator","commits",
        "commitsOperator","in","language","keyword","sort","order"})
public class TopSearchGitHubVO {
    /** 用于过滤具有特定数量的 Stars 的仓库 */
    private String stars = "0";
    /** 运算符*/
    private OperatorEnum starsOperator = OperatorEnum.GREATER_THAN_EQUAL;
    /** 用于过滤具有特定数量的 Forks 的仓库 */
    private String forks = "0";
    private OperatorEnum forksOperator = OperatorEnum.GREATER_THAN_EQUAL;
    /** 用于过滤具有特定大小的仓库。仓库大小是以 KB 为单位的 */
    private String size = "0";
    private OperatorEnum sizeOperator = OperatorEnum.GREATER_THAN_EQUAL;
    /** 用于根据 仓库创建时间 筛选项目。格式为 YYYY-MM-DD */
    private String created = "1970-01-01";
    private OperatorEnum createdOperator = OperatorEnum.GREATER_THAN_EQUAL;
    /** 用于根据 仓库的最后更新时间 筛选项目 */
    private String updated = "1970-01-01";
    private OperatorEnum updatedOperator = OperatorEnum.GREATER_THAN_EQUAL;
    /** 用于过滤具有特定数量 Issue 的仓库 */
    private String issues = "0";
    private OperatorEnum issuesOperator = OperatorEnum.GREATER_THAN_EQUAL;
    /** 用于过滤具有特定数量 Pull Request 的仓库 */
    private String pulls = "0";
    private OperatorEnum pullsOperator = OperatorEnum.GREATER_THAN_EQUAL;
    /** 用于过滤具有特定数量 提交 的仓库 */
    private String commits = "0";
    private OperatorEnum commitsOperator = OperatorEnum.GREATER_THAN_EQUAL;
    /** 用于指定搜索字段，例如 搜索在仓库名称、描述或 README 文件中 */
    private String in;
    /** 用于过滤 特定编程语言 的仓库 */
    private String language;
    /** 热搜词 */
    private String keyword;
    /** 默认排序字段 */
    private String sort = "stars";
    /** 默认排序方式 */
    private String order = "desc";
    // 默认页码
    private Integer page = 1;
    // 默认每页 30 条数据
    private Integer perPage = 30;


    /** 项目地址 */
    private String projectName ;
    /** 项目url */
    private String projectUrl;
    /** 作者头像 */
    private String avatarUrl;
    /** Star 数 */
    private String stargazersCount ;
    /** Fork  数 */
    private String forksCount ;
    /** 关注了这个项目 数 */
    private String watchersCount ;
    /**  Open Issues 数 */
    private String openIssuesCount ;
    /** 最新更新时间 数 */
    private String updatedAt ;

}
