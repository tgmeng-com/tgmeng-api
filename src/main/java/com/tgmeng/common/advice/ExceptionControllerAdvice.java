package com.tgmeng.common.advice;

import com.tgmeng.common.bean.ExceptionBean;
import com.tgmeng.common.exception.BossException;
import com.tgmeng.common.exception.ServerException;
import com.tgmeng.common.mapper.exception.ExceptionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Log4j2
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionControllerAdvice {
    private final ExceptionMapper exceptionMapper;

    @ExceptionHandler(BossException.class)
    public ResponseEntity<ExceptionBean> handleBossException(BossException exception) {
        log.info("捕获到自定义异常:{}", exception.getMessage());
        ExceptionBean exceptionBean = exceptionMapper.exception2Bean(exception);
        return new ResponseEntity<>(exceptionBean, exception.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionBean> handleException(Exception exception) {
        log.error("未捕获的异常:{}", exception.getMessage());
        ServerException serverException = new ServerException();
        ExceptionBean exceptionBean = exceptionMapper.exception2Bean(serverException);
        return new ResponseEntity<>(exceptionBean, serverException.getHttpStatus());
    }
}
