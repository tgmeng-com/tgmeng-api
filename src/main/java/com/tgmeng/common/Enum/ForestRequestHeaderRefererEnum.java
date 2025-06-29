package com.tgmeng.common.Enum;

import com.tgmeng.common.Enum.Enumcommon.INameValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * description: http请求的Referer
 * package: com.tgmeng.common.Enum
 * className: ForestRequestHeaderRefererEnum
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/6/29 18:17
*/
@Getter
@AllArgsConstructor
public enum ForestRequestHeaderRefererEnum  implements INameValueEnum<String> {
    BILIBILI("BILIBILI", "https://www.bilibili.com/", "", 1),
    BAIDU("BAIDU", "https://www.baidu.com/", "", 2),
    WEIBO("WEIBO", "https://www.weibo.com/", "", 3),
    DOUYIN("DOUYIN", "https://www.douyin.com/", "", 4);

    private final String key;
    private final String value;
    private final String description;
    private final Integer sort;
}
