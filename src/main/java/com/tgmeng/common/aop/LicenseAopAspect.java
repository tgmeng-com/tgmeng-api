package com.tgmeng.common.aop;

import com.tgmeng.common.annotation.LicenseRequired;
import com.tgmeng.common.enums.business.LicenseFeatureEnum;
import com.tgmeng.common.enums.exception.ServerExceptionEnum;
import com.tgmeng.common.enums.system.RequestFromEnum;
import com.tgmeng.common.exception.ServerException;
import com.tgmeng.common.util.HttpRequestUtil;
import com.tgmeng.service.license.ILicenseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
@Order(0)
public class LicenseAopAspect {

    @Value("${my-config.license.enabled}")
    private Boolean licenseEnabled;

    @Autowired
    private ILicenseService licenseService;

    @Around("@annotation(licenseRequired)")
    public Object checkLicense(ProceedingJoinPoint pjp, LicenseRequired licenseRequired) throws Throwable {
        if (licenseEnabled) {
            LicenseFeatureEnum feature = licenseRequired.feature();
            // 获取 HTTP 请求
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                throw new RuntimeException("非Web请求上下文，无法进行许可校验");
            }

            if (!RequestFromEnum.INTERNAL.getValue().equals(HttpRequestUtil.getRequestHeader("X-Source"))) {
                HttpServletRequest request = attributes.getRequest();

                String licenseCode = request.getHeader("X-License-Code");
                String machineId = request.getHeader("X-Machine-Id");

                if (licenseCode == null || machineId == null) {
                    throw new ServerException(ServerExceptionEnum.LICENSE_CHECK_EXCEPTION, "缺少授权信息", null);
                }
                // 存到上下文里去，后面controller、service等任何地方就他妈直接取，重构真尼玛麻烦，一天天净返工了
                attributes.setAttribute("CURRENT_LICENSE_CODE", licenseCode, RequestAttributes.SCOPE_REQUEST);
                attributes.setAttribute("CURRENT_MACHINE_ID", machineId, RequestAttributes.SCOPE_REQUEST);

                // 核心校验
                licenseService.check(licenseCode, machineId, feature);
            }
        }
        // 执行方法
        return pjp.proceed();
    }
}