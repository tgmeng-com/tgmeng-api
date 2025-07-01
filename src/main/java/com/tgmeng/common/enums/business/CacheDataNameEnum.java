package com.tgmeng.common.enums.business;

import com.tgmeng.common.enums.enumcommon.INameValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * description: 缓存名称
 * package: com.tgmeng.common.enums.business
 * className: CacheDataNameEnum
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/7/1 14:45
*/
@Getter
@AllArgsConstructor
public enum CacheDataNameEnum implements INameValueEnum<String,String> {
    CACHE_TOP_SEARCH_BILIBILI("CACHE_TOP_SEARCH_BILIBILI", "/topsearch/bilibili", "b站数据缓存名称", true,1),
    CACHE_TOP_SEARCH_BAIDU("CACHE_TOP_SEARCH_BAIDU", "/topsearch/baidu", "百度数据缓存名称", true,2),
    CACHE_TOP_SEARCH_WEIBO("CACHE_TOP_SEARCH_WEIBO", "/topsearch/weibo", "微博数据缓存名称", true,3),
    CACHE_TOP_SEARCH_DOUYIN("CACHE_TOP_SEARCH_DOUYIN", "/topsearch/douyin", "抖音数据缓存名称", true,4);

    private final String key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
