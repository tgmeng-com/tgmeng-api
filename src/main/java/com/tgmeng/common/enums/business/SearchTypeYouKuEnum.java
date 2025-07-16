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
public enum SearchTypeYouKuEnum implements INameValueEnum<String,String> {
    DIAN_SHI_JU_YOU_KU("DIAN_SHI_JU_YOU_KU", "电视剧", "优酷电视剧", true,1),
    DIAN_YING_YOU_KU("DIAN_YING_YOU_KU", "电影", "优酷电影", true,2),
    DONG_MAN_YOU_KU("DONG_MAN_YOU_KU", "动漫", "优酷动漫", true,2),
    ZONG_YI_YOU_KU("ZONG_YI_YOU_KU", "综艺", "优酷综艺", true,2),
    ZONG_BANG_YOU_KU("ZONG_BANG_YOU_KU", "热门搜索", "优酷总榜", true,2);

    private final String key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
