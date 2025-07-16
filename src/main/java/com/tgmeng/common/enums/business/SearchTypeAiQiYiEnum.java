package com.tgmeng.common.enums.business;

import com.tgmeng.common.enums.enumcommon.INameValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * description: 这个爱奇艺排行榜url后缀路由用的
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
public enum SearchTypeAiQiYiEnum implements INameValueEnum<String,String> {
    DIAN_SHI_JU_AI_QI_YI("DIAN_SHI_JU_AI_QI_YI", "2", "爱奇艺电视剧", true,1),
    DIAN_YING_AI_QI_YI("DIAN_YING_AI_QI_YI", "1", "爱奇艺电影", true,2),
    DONG_MAN_AI_QI_YI("DONG_MAN_AI_QI_YI", "4", "爱奇艺动漫", true,2),
    ZONG_YI_AI_QI_YI("ZONG_YI_AI_QI_YI", "6", "爱奇艺综艺", true,2),
    ZONG_BANG_AI_QI_YI("ZONG_BANG_AI_QI_YI", "-1", "爱奇艺总榜", true,2);

    private final String key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
