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
    GITHUB_TOP_SEARCH_EXCEPTION(151, "GitHub热搜接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    YOUTUBE_TOP_SEARCH_EXCEPTION(152, "Youtube热搜接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    DOUBAN_TOP_SEARCH_EXCEPTION(104, "豆瓣热搜接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    TENCENT_TOP_SEARCH_EXCEPTION(105, "腾讯新闻热搜接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    TOUTIAO_TOP_SEARCH_EXCEPTION(106, "头条新闻热搜接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    WANGYI_TOP_SEARCH_EXCEPTION(107, "网易新闻热搜接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    WANGYIYUN_TOP_SEARCH_EXCEPTION(108, "网易云新闻热搜接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    BAIDUTIEBA_TOP_SEARCH_EXCEPTION(109, "百度贴吧热搜接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    SHAOSHUPAI_TOP_SEARCH_EXCEPTION(109, "少数派热搜接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    HUGGING_FACE_TOP_SEARCH_EXCEPTION(109, "huggingFace热搜接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    ZHI_HU_TOP_SEARCH_EXCEPTION(109, "知乎热搜接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    TENG_XUN_SHI_PIN_TOP_SEARCH_EXCEPTION(109, "腾讯视频热搜接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    AI_QI_YI_TOP_SEARCH_EXCEPTION(109, "爱奇艺热搜接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    YOU_KU_TOP_SEARCH_EXCEPTION(109, "优酷热搜接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    MANG_GUO_SEARCH_EXCEPTION(109, "芒果热搜接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1);
    private final Integer key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
