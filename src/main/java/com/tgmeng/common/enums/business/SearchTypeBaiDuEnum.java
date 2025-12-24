package com.tgmeng.common.enums.business;

import com.tgmeng.common.enums.enumcommon.INameValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * description: 这个百度url后缀路由用的
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
public enum SearchTypeBaiDuEnum implements INameValueEnum<String,String> {
    DIAN_SHI_JU_BAIDU("dianshiju", "teleplay", "百度电视剧", true,1),
    XIAO_SHUO_BAIDU("xiaoshuo", "novel", "百度小说", true,2),
    DIAN_YING_BAIDU("dianying", "movie", "百度电影", true,2),
    NEWS_BAIDU("resou", "realtime", "百度热搜", true,2),
    QI_CHE_BAIDU("qiche", "car", "百度汽车", true,2),
    REGENG_BAIDU("regeng", "phrase", "百度热梗", true,2),
    DUAN_JU_BAIDU("duanju", "drama", "百度短剧", true,2);



    private final String key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
