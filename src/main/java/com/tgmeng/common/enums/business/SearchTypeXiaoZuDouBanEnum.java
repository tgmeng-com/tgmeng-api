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
public enum SearchTypeXiaoZuDouBanEnum implements INameValueEnum<String,String> {
    MAI_ZU_DOU_BAN("MAI_ZU_DOU_BAN", "698716", "买组 & All buy & 不买不可能 豆瓣", true,1),
    PIN_ZU_DOU_BAN("PIN_ZU_DOU_BAN", "536786", "拼组 / 双11 / 我跟你拼了！ 豆瓣", true,2),
    AI_MAO_SHENG_HUO_DOU_BAN("AI_MAO_SHENG_HUO_DOU_BAN", "656297", "爱猫生活 豆瓣", true,2),
    AI_MAO_ZAO_PEN_DOU_BAN("AI_MAO_ZAO_PEN_DOU_BAN", "700687", "爱猫澡盆 豆瓣", true,2),
    GOU_ZU_DOU_BAN("GOU_ZU_DOU_BAN", "716166", "豆瓣狗组 豆瓣", true,2);

    private final String key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
