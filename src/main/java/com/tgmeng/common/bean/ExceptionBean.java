package com.tgmeng.common.bean;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * description: 异常结果返回实体类
 * package: com.tgmeng.common.bean
 * className: ExceptionBean
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/7/1 10:29
*/
@Data
@Accessors(chain = true)
public class ExceptionBean {
    private Integer code;
    private String message;
    private Object data;
}
