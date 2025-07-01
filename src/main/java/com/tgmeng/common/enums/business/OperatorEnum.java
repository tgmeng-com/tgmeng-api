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
public enum OperatorEnum implements INameValueEnum<String,String> {
    GREATER_THAN("GREATER_THAN", ">", "", true,1),
    LESS_THAN("LESS_THAN", "<", "", true,2),
    EQUAL("EQUAL", "=", "", true,3),
    GREATER_THAN_EQUAL("GREATER_THAN_EQUAL", ">=", "", true,4),
    LESS_THAN_EQUAL("LESS_THAN_EQUAL", "<=", "", true,5),
    NOT_EQUAL("NOT_EQUAL", "=", "", true,6),
    RANGE("RANGE", "..", "", true,7);

    private final String key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
