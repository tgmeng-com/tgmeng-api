package com.tgmeng.common.enums.business;

import com.tgmeng.common.enums.enumcommon.INameValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * description: 运算符枚举
 * package: com.tgmeng.common.Enum
 * className: ForestRequestHeaderOriginEnum
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/6/29 18:17
*/
@Getter
@AllArgsConstructor
public enum BaiDuSearchTypeEnum implements INameValueEnum<String,String> {
    DIAN_SHI_JU_BAIDU("DIAN_SHI_JU_BAIDU", "teleplay", "百度电视剧url后缀", true,1),
    XIAO_SHUO_BAIDU("XIAO_SHUO_BAIDU", "novel", "百度小说url后缀", true,2),
    DIAN_YING_BAIDU("DIAN_YING_BAIDU", "movie", "百度电影剧url后缀", true,2),
    NEWS_BAIDU("NEWS_BAIDU", "realtime", "百度热搜url后缀", true,2);

    private final String key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
