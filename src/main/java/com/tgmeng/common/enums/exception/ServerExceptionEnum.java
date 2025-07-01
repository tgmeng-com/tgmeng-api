package com.tgmeng.common.enums.exception;

import com.tgmeng.common.enums.enumcommon.INameValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * description: 异常码和描述
 * package: com.tgmeng.common.Enum.Enumcommon
 * className: CodeMessageExceptionEnum
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/7/1 10:38
*/
@Getter
@AllArgsConstructor
public enum ServerExceptionEnum implements INameValueEnum<Integer, String> {
    SYSTEM_EXCEPTION(666, "系统未主动捕获到的异常", "FUCK YOU", true, 1),
    BAIDU_TOP_SEARCH_EXCEPTION(101, "百度热搜接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    DOUYIN_TOP_SEARCH_EXCEPTION(102, "抖音热搜接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    WEIBO_TOP_SEARCH_EXCEPTION(103, "微博热搜接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    BILIBILI_TOP_SEARCH_EXCEPTION(104, "Bilibili热搜接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    GITHUB_TOP_SEARCH_EXCEPTION(151, "GitHub热搜接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1);
    private final Integer key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
