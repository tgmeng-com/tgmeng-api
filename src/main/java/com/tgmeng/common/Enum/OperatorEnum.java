package com.tgmeng.common.Enum;

import com.tgmeng.common.Enum.Enumcommon.INameValueEnum;
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
public enum OperatorEnum implements INameValueEnum<String> {
    GREATER_THAN("GREATER_THAN", ">", "", 1),
    LESS_THAN("LESS_THAN", "<", "", 2),
    EQUAL("EQUAL", "=", "", 3),
    GREATER_THAN_EQUAL("GREATER_THAN_EQUAL", ">=", "", 4),
    LESS_THAN_EQUAL("LESS_THAN_EQUAL", "<=", "", 5),
    NOT_EQUAL("NOT_EQUAL", "=", "", 6),
    RANGE("RANGE", "..", "", 7);

    private final String key;
    private final String value;
    private final String description;
    private final Integer sort;
}
