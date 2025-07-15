package com.tgmeng.common.enums.business;

import com.tgmeng.common.enums.enumcommon.INameValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * description: 这个网易url后缀路由用的
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
public enum SearchTypeWangYiYunEnum implements INameValueEnum<String,String> {
    BIAO_SHENG_WANG_YI_YUN("BIAO_SHENG_WANG_YI_YUN", "19723756", "网易云飙升榜", true,1),
    XIN_GE_WANG_YI_YUN("XIN_GE_WANG_YI_YUN", "3779629", "网易云新歌榜", true,2),
    YUAN_CHUANG_WANG_YI_YUN("YUAN_CHUANG_WANG_YI_YUN", "2884035", "网易云原创榜", true,2),
    RE_GE_WANG_YI_YUN("RE_GE_WANG_YI_YUN", "3778678", "网易云热歌榜", true,2);

    private final String key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
