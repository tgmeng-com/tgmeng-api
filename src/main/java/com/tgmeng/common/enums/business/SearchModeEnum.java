package com.tgmeng.common.enums.business;

import com.tgmeng.common.enums.enumcommon.INameValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SearchModeEnum implements INameValueEnum<String, String> {
    MO_HU_PI_PEI_FIVE_MINUTES("", "MO_HU_PI_PEI_FIVE_MINUTES", "模糊匹配(5分钟)", true, 1),
    MO_HU_PI_PEI_TODAY("", "MO_HU_PI_PEI_TODAY", "模糊匹配(今日)", true, 2),
    MO_HU_PI_PEI_HISTORY("", "MO_HU_PI_PEI_HISTORY", "模糊匹配(历史)", true, 3),
    ZHI_WEN_PI_PEI_TODAY("", "ZHI_WEN_PI_PEI_TODAY", "指纹匹配(今日)", true, 4),
    ZHI_WEN_PI_PEI_HISTORY("", "ZHI_WEN_PI_PEI_HISTORY", "指纹匹配(历史)", true, 5);


    private final String key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
