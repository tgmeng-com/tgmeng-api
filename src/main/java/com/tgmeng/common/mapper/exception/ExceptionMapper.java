package com.tgmeng.common.mapper.exception;

import com.tgmeng.common.bean.ExceptionBean;
import com.tgmeng.common.exception.BossException;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExceptionMapper {
    ExceptionBean exception2Bean(BossException exception);
}
