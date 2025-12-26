package com.tgmeng.common.enums.business;

import com.tgmeng.common.enums.enumcommon.INameValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LicenseStatusEnum implements INameValueEnum<String, String> {
    ACTIVE("", "ACTIVE", "正常", true, 1),
    EXPIRED("", "EXPIRED", "已过期", true, 2),
    DISABLED("", "DISABLED", "已禁用", true, 3);


    private final String key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
