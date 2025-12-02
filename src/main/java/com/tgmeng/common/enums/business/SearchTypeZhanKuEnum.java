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
public enum SearchTypeZhanKuEnum implements INameValueEnum<String,String> {
    QIAN_LI_BANG_ZHAN_KU("qianlibang", "potential#tab_anchor", "站酷潜力榜", true,1),
    WEN_ZHANG_BANG_ZHAN_KU("wenzhangbang", "top/article.do?rankType=8#tab_anchor", "站酷文章榜", true,2),
    ZUO_PIN_BANG_ZHAN_KU("zuopinbang", "top/index.do#tab_anchor", "站酷作品榜", true,2);

    private final String key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
