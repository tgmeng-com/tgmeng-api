package com.tgmeng.common.enums.system;

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
public enum ResponseTypeEnum implements INameValueEnum<String,String> {
    DOM("DOM", "DOM", "请求第三方返回的DOM，然后这种就用jsoup去解析", true,1),
    INTERFACE("INTERFACE", "INTERFACE", "请求第三方接口，返回的json或者啥", true,2);

    private final String key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
