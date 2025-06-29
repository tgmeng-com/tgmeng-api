package com.tgmeng.model.vo.topsearch;

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
public class TopSearchGitHubVO {
    /** 用于过滤具有特定数量的 Stars 的仓库 */
    private String stars;
    /** 用于过滤具有特定数量的 Forks 的仓库 */
    private String forks;
    /** 用于过滤具有特定大小的仓库。仓库大小是以 KB 为单位的 */
    private String size;
    /** 用于根据 仓库创建时间 筛选项目。格式为 YYYY-MM-DD */
    private String created;
    /** 用于根据 仓库的最后更新时间 筛选项目 */
    private String updated;
    /** 用于指定搜索字段，例如 搜索在仓库名称、描述或 README 文件中 */
    private String in;
    /** 用于过滤 特定编程语言 的仓库 */
    private String language;
    /** 用于过滤具有特定数量 Issue 的仓库 */
    private String issues;
    /** 用于过滤具有特定数量 Pull Request 的仓库 */
    private String pulls;
    /** 用于过滤具有特定数量 提交 的仓库 */
    private String commits;
}
