package com.tgmeng.common.annotation;

import com.tgmeng.common.enums.business.LicenseFeatureEnum;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
// 定义授权注解
public @interface LicenseRequired {
    /** 功能标识 */
    LicenseFeatureEnum feature();
}
