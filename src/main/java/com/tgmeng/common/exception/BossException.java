package com.tgmeng.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * description: 异常处理父类，其他异常处理的类继承该类
 * package: com.tgmeng.common.exception
 * className: BossException
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/7/1 10:36
*/
public abstract class BossException extends RuntimeException{
    @Getter
    private Integer code;
    @Getter
    private Object data;
    @Getter
    private HttpStatus httpStatus;

    BossException(HttpStatus httpStatus, Integer code, String message,Object data) {
        super(message);
        this.code = code != null ? code : -1;
        this.data = data;
        this.httpStatus = httpStatus;
    }
}