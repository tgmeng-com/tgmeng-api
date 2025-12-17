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
public enum SearchType4GamerEnum implements INameValueEnum<String,String> {
    PC("pc", "pc", "pc", true,1),
    XBOX("xbox", "xbox", "xbox", true,2),
    PS("ps", "ps", "ps5/ps4", true,3),
    SWITCH("switch", "switch", "switch", true,4),
    SMART_PHONE("smartphone", "smartphone", "smartphone", true,4),
    VR("vr", "vr", "vr", true,4),
    HARDWARE("hardware", "hardware", "hardware", true,4),
    ARCADE("arcade", "arcade", "arcade", true,4),
    ANALOG("analog", "analog", "analog", true,4),
    WII("wii", "wii", "wii", true,4),
    VITA("vita", "vita", "vita", true,4),
    NDS("nds", "nds", "nds", true,4);

    private final String key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
