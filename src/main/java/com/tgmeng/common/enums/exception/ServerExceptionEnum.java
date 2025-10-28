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
    MANG_GUO_SEARCH_EXCEPTION(109, "芒果热搜接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    MAO_YAN_SEARCH_EXCEPTION(109, "猫眼接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    JIN_RONG_JIE_TOP_SEARCH_EXCEPTION(109, "金融界接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    DI_YI_CAI_JING_TOP_SEARCH_EXCEPTION(109, "第一财经接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    TONG_HUA_SHUN_TOP_SEARCH_EXCEPTION(109, "同花顺接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    HUA_ER_JIE_JIAN_WEN_TOP_SEARCH_EXCEPTION(109, "华尔街见闻接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    CAI_LIAN_SHE_TOP_SEARCH_EXCEPTION(109, "财联社接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    GE_LONG_HUI_TOP_SEARCH_EXCEPTION(109, "格隆汇接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    FA_BU_TOP_SEARCH_EXCEPTION(109, "法布接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    JIN_SHI_TOP_SEARCH_EXCEPTION(109, "金十接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    NEW_YUE_SHI_BAO_TOP_SEARCH_EXCEPTION(109, "纽约时报接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    BBC_TOP_SEARCH_EXCEPTION(109, "BBC接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    FA_GUANG_TOP_SEARCH_EXCEPTION(109, "法广接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    HUA_ER_JIE_RI_BAO_TOP_SEARCH_EXCEPTION(109, "华尔街日报接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    DA_JI_YUAN_TOP_SEARCH_EXCEPTION(109, "大纪元接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    WO_SHI_PM_TOP_SEARCH_EXCEPTION(109, "人人都是产品经理接口查询异常", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    YOU_SHE_WANG_TOP_SEARCH_EXCEPTION(109, "优设网", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    ZHAN_KU_TOP_SEARCH_EXCEPTION(109, "站酷", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    TU_YA_WANG_GUO_TOP_SEARCH_EXCEPTION(109, "涂鸦王国", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    SHE_JI_DA_REN_TOP_SEARCH_EXCEPTION(109, "设计达人", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    TOPYS_TOP_SEARCH_EXCEPTION(109, "Topys", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    ARCH_DAILY_SEARCH_EXCEPTION(109, "ArchDaily", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    DRIBBBLE_SEARCH_EXCEPTION(109, "Dribbble", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    AWWWARDS_SEARCH_EXCEPTION(109, "Awwwards", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    CORE77_SEARCH_EXCEPTION(109, "Core77", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1),
    ABDUZEEDO_SEARCH_EXCEPTION(109, "Abduzeedo", "有可能是网络问题、风控(速率等)、接口变更、数据结构变更等", true, 1);

    private final Integer key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
