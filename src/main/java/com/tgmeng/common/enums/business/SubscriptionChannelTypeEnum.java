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
public enum SubscriptionChannelTypeEnum implements INameValueEnum<String,String> {
    DINGDING("DINGDING", "DINGDING", "订阅推送-钉钉", true,1),
    TELEGRAM("TELEGRAM", "TELEGRAM", "订阅推送-TG", true,2),
    EMAIL("EMAIL", "EMAIL", "订阅推送-邮箱", true,3),
    QIYEWEIXIN("QIYEWEIXIN", "QIYEWEIXIN", "订阅推送-企业微信", true,3),
    FEISHU("FEISHU", "FEISHU", "订阅推送-飞书", true,4),
    NTFY("NTFY", "NTFY", "订阅推送-NTFY", true,4);

    private final String key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
