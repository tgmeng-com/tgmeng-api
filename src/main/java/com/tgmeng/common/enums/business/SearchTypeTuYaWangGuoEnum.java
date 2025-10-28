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
public enum SearchTypeTuYaWangGuoEnum implements INameValueEnum<String,String> {
    RE_MEN_ZUO_PIN_TU_YA_WANG_GUO("RE_MEN_ZUO_PIN_TU_YA_WANG_GUO", "hot", "涂鸦王国热门作品", true,1),
    JING_XUAN_ZUO_PIN_TU_YA_WANG_GUO("JING_XUAN_ZUO_PIN_TU_YA_WANG_GUO", "best", "涂鸦王国精选作品", true,2),
    JIN_RI_XIN_ZUO_TU_YA_WANG_GUO("JIN_RI_XIN_ZUO_TU_YA_WANG_GUO", "new", "涂鸦王国今日新作", true,2),
    FA_XIAN_XIN_ZUO_TU_YA_WANG_GUO("FA_XIAN_XIN_ZUO_TU_YA_WANG_GUO", "fx", "涂鸦王国发现新作", true,2);

    private final String key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
