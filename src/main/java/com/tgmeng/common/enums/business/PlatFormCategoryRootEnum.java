package com.tgmeng.common.enums.business;

import com.tgmeng.common.enums.enumcommon.INameValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

// 这个是平台的根分类，主要是为了过滤用
@Getter
@AllArgsConstructor
public enum PlatFormCategoryRootEnum implements INameValueEnum<String, String> {
    XIN_WEN("", "新闻", "", true, 1),
    YANG_MAO("", "羊毛", "", true, 2),
    MEI_TI("", "媒体", "", true, 3),
    DIAN_SHI("", "电视", "", true, 4),
    SHENG_HUO("", "生活", "", true, 5),
    SHE_QU("", "社区", "", true, 6),
    CAI_JING("", "财经", "", true, 7),
    TI_YU("", "体育", "", true, 8),
    KE_JI("", "科技", "", true, 9),
    SHE_JI("", "设计", "", true, 10),
    YING_YIN("", "影音", "", true, 11),
    YOU_XI("", "游戏", "", true, 12),
    JIAN_KANG("", "健康", "", true, 13);

    private final String key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
