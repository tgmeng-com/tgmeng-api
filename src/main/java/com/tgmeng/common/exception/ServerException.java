package com.tgmeng.common.exception;

import com.tgmeng.common.enums.exception.ServerExceptionEnum;
import org.springframework.http.HttpStatus;

/**
 * description: 服务器异常，我用来处理程序中非人为抛出的异常 500
 * package: com.tgmeng.common.exception
 * className: ServerException
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/7/1 10:38
 */
public class ServerException extends BossException {
    private static final HttpStatus HTTP_STATUS = HttpStatus.OK;

    /**
     * description: 系统未捕获的异常，这个只给AdviceHandler用，自己代码里不要主动用这个，用下面其他带参数的
     * method: ServerException
     *
     * @author tgmeng
     * @since 2025/7/1 12:03
    */
    public ServerException() {
        super(HTTP_STATUS, ServerExceptionEnum.SYSTEM_EXCEPTION.getKey(), ServerExceptionEnum.SYSTEM_EXCEPTION.getValue(), null);
    }

    public ServerException(String message) {
        super(HTTP_STATUS, ServerExceptionEnum.SYSTEM_EXCEPTION.getKey(), message, null);
    }

    /**
     * description: 最常用的一个
     * method: ServerException
     *
     * @author tgmeng
     * @since 2025/7/1 12:04
    */
    public ServerException(ServerExceptionEnum exceptionEnum) {
        super(HTTP_STATUS, exceptionEnum.getKey(), exceptionEnum.getValue(), null);
    }

    /**
     * description: 异常请求里返回的时候还给带个数据
     * method: ServerException
     *
     * @author tgmeng
     * @since 2025/7/1 12:04
    */
    public ServerException(ServerExceptionEnum exceptionEnum, Object data) {
        super(HTTP_STATUS, exceptionEnum.getKey(), exceptionEnum.getValue(), data);
    }

    /**
     * description: 自定义http异常码,是指http整个的，不是我们定义的数据结构里的code
     * method: ServerException
     *
     * @author tgmeng
     * @since 2025/7/1 12:04
    */
    public ServerException(HttpStatus httpStatus, ServerExceptionEnum exceptionEnum) {
        super(httpStatus, exceptionEnum.getKey(), exceptionEnum.getValue(), null);
    }

    /**
     * description: 自定义http异常码,是指http整个的，不是我们定义的数据结构里的code，并且返回数据
     * method: ServerException
     *
     * @author tgmeng
     * @since 2025/7/1 12:05
    */
    public ServerException(HttpStatus httpStatus, ServerExceptionEnum exceptionEnum, Object data) {
        super(httpStatus, exceptionEnum.getKey(), exceptionEnum.getValue(), data);
    }
}
