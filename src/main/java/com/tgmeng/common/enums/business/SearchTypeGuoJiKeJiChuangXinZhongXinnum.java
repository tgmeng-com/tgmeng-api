package com.tgmeng.common.enums.business;

import com.tgmeng.common.enums.enumcommon.INameValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * description: 这个优酷排行榜url后缀路由用的
 *  其中value用来路由
 *  description用来日志打印，同时他也是返回前端的card框的分类名称。但是由于card现在是在前端弄的，没用后端的，所以这里目前只用来打印日志
 * package: com.tgmeng.common.Enum
 * className: ForestRequestHeaderOriginEnum
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/6/29 18:17
*/
@Getter
@AllArgsConstructor
public enum SearchTypeGuoJiKeJiChuangXinZhongXinnum implements INameValueEnum<String,String> {
    REN_GONG_ZHI_NENG_GUO_JI_KE_JI_CHUANG_XIN_ZHONG_XIN("REN_GONG_ZHI_NENG_GUO_JI_KE_JI_CHUANG_XIN_ZHONG_XIN", "rgzn_kjrd", "人工智能国际科技创新中心", true,1),
    YI_YAO_JIAN_KANG_GUO_JI_KE_JI_CHUANG_XIN_ZHONG_XIN("YI_YAO_JIAN_KANG_GUO_JI_KE_JI_CHUANG_XIN_ZHONG_XIN", "yyjk_kjrd", "医药健康国际科技创新中心", true,2);

    private final String key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
