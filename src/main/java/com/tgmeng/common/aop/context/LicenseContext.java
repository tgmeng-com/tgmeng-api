package com.tgmeng.common.aop.context;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class LicenseContext {

    private static final String CODE_KEY = "CURRENT_LICENSE_CODE";
    private static final String MACHINE_KEY = "CURRENT_MACHINE_ID";

    public static String getLicenseCode() {
        return getAttribute(CODE_KEY);
    }

    public static String getMachineId() {
        return getAttribute(MACHINE_KEY);
    }

    @SuppressWarnings("unchecked")
    private static <T> T getAttribute(String key) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? (T) attributes.getAttribute(key, RequestAttributes.SCOPE_REQUEST) : null;
    }
}
