package com.tgmeng.common.enums.business;

import com.tgmeng.common.enums.enumcommon.INameValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * description: 这个芒果排行榜url后缀路由用的
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
public enum SearchTypeMangGuoEnum implements INameValueEnum<String,String> {
    DIAN_SHI_JU_MANG_GUO("DIAN_SHI_JU_MANG_GUO", "电视剧", "芒果电视剧", true,1),
    DIAN_YING_MANG_GUO("DIAN_YING_MANG_GUO", "电影", "芒果电影", true,2),
    DONG_MAN_MANG_GUO("DONG_MAN_MANG_GUO", "动漫", "芒果动漫", true,2),
    ZONG_YI_MANG_GUO("ZONG_YI_MANG_GUO", "综艺", "芒果综艺", true,2),
    ZONG_BANG_MANG_GUO("ZONG_BANG_MANG_GUO", "热门内容", "芒果总榜", true,2);

    private final String key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
