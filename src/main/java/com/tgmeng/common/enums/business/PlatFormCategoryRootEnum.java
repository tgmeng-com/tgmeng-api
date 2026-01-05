package com.tgmeng.common.enums.business;

import com.tgmeng.common.enums.enumcommon.INameValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

// 这个是平台的根分类，主要是为了过滤用
@Getter
@AllArgsConstructor
public enum PlatFormCategoryRootEnum implements INameValueEnum<String, String> {
    TGMENG("tgmeng", "糖果梦", "", true, 0),
    XIN_WEN("news", "新闻", "", true, 1),
    YANG_MAO("wool", "羊毛", "", true, 2),
    MEI_TI("media", "媒体", "", true, 3),
    DIAN_SHI("tv", "电视", "", true, 4),
    SHENG_HUO("life", "生活", "", true, 5),
    SHE_QU("community", "社区", "", true, 6),
    CAI_JING("finance", "财经", "", true, 7),
    TI_YU("sports", "体育", "", true, 8),
    KE_JI("technology", "科技", "", true, 9),
    SHE_JI("design", "设计", "", true, 10),
    YING_YIN("audiovideo", "影音", "", true, 11),
    YOU_XI("game", "游戏", "", true, 12),
    JIAN_KANG("health", "健康", "", true, 13);

    private final String key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
