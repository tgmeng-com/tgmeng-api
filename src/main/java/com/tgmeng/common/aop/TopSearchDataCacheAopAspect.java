package com.tgmeng.common.aop;

import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.enums.system.RequestFromEnum;
import com.tgmeng.common.util.CacheUtil;
import com.tgmeng.common.util.HttpRequestUtil;
import com.tgmeng.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Aspect
@Component
@Order(1)
public class TopSearchDataCacheAopAspect {

    @Autowired
    private CacheUtil cacheUtil;

    @Value("${my-config.data-cache.top-search.enabled:true}")
    private Boolean dataCacheEnabled;

    // 定义一个切点，拦截所有Controller中的方法（可以根据需要具体化）
    @Pointcut(
            "execution(* com.tgmeng.controller.topsearch..*(..)) || " +
                    "execution(* com.tgmeng.controller.cachesearch..*(..))"
    )
    public void cachePointcut() {
    }

    @Around("cachePointcut()")
    public Object aroundRequest(ProceedingJoinPoint joinPoint) throws Throwable {

        String url = HttpRequestUtil.getRequestUrl();
        if (dataCacheEnabled) {
            String source = HttpRequestUtil.getRequestHeader("X-Source");
            List<String> cacheSearchPaths = Arrays.asList(
                    // TODO 排除掉需要不走缓存的
                    "/api/cachesearch/allbyword",
                    "/api/topsearch/history/mergeparquet",
                    "/api/topsearch/history/customexcutesql",
                    "/api/topsearch/categories",
                    "/api/cachesearch/single"
            );
            if (cacheSearchPaths.stream().anyMatch(url::contains)) {
                log.info("🙋🏻‍♂️外部请求，检索数据，走正常程序:{}", url);
                return joinPoint.proceed();
            } else if (!RequestFromEnum.INTERNAL.getValue().equals(source)) {
                Object cachedData = cacheUtil.getValue(StringUtil.getUri(url));
                return ResultTemplateBean.success(cachedData);
            } else {
                return joinPoint.proceed();
            }
        } else {
            log.info("🤡缓存未开启，调用接口获取数据：{}", url);
            return joinPoint.proceed();
        }
    }
}
