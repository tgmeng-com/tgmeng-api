package com.tgmeng.common.interceptor;

import cn.hutool.core.util.StrUtil;
import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.cache.TopSearchDataCache;
import com.tgmeng.common.enums.business.CacheDataNameEnum;
import com.tgmeng.common.enums.enumcommon.EnumUtils;
import com.tgmeng.common.enums.system.RequestFromEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Aspect
@Component
public class TopSearchDataCacheInterceptor {

    @Autowired
    private TopSearchDataCache topSearchDataCache;

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
        String url = getRequestUrl();
        if (dataCacheEnabled) {
            //获取缓存
            Object cachedData = getCachedData(url);
            String source = getRequestHeader("X-Source");
            // 内部请求的处理逻辑，内部请求到数据的时候不用缓存
            if (RequestFromEnum.INTERNAL.getValue().equals(source)) {
                // 内部请求的特定接口列表，不走缓存，比如AI时报和词云，都是后台主动去定时刷新的
                Set<String> noCacheInternalEndpoints = Set.of(
                        "/api/cachesearch/wordcloud",
                        "/api/cachesearch/realtimesummary"
                        // 可以继续加其他内部接口
                );
                if (noCacheInternalEndpoints.stream().anyMatch(url::contains)) {
                    // 不返回缓存，直接调用接口
                    log.info("🙆🏻内部强制刷新缓存，即使数据已存在，接口:{}", url);
                    return joinPointProceedAndCacheData(joinPoint,url);
                } else {
                    // 内部请求普通接口，优先返回缓存
                    if (cachedData != null) {
                        log.info("❤️内部请求，命中缓存：{}", url);
                        return cachedData;
                    } else {
                        log.info("🙋🏻内部请求，缓存未命中，调用接口获取数据：{}", url);
                        return joinPointProceedAndCacheData(joinPoint,url);
                    }
                }
            } else {
                // 客户端请求的处理逻辑
                if (cachedData != null) {
                    log.info("❤️客户端请求，命中缓存，返回缓存：{}", url);
                    return ResultTemplateBean.success(cachedData);
                } else {
                    log.info("🙋🏻客户端请求，未命中缓存，调用接口获取数据：{}", url);
                    // 执行接口请求数据
                    return joinPointProceedAndCacheData(joinPoint,url);
                }
            }
        } else {
            log.info("🤡缓存未开启，调用接口获取数据：{}", url);
            // 执行接口请求数据
            return joinPoint.proceed();
        }
    }

    // 获取缓存数据
    private Object getCachedData(String url) {
        for (String key : EnumUtils.getKeys(CacheDataNameEnum.class)) {
            if (url.contains(EnumUtils.getValueByKey(CacheDataNameEnum.class, key))) {
                return topSearchDataCache.get(EnumUtils.getEnumByKey(CacheDataNameEnum.class, key));
            }
        }
        return null;
    }

    // 新增缓存数据
    private void cacheData(String url, Object data) {
        for (String key : EnumUtils.getKeys(CacheDataNameEnum.class)) {
            if (url.contains(EnumUtils.getValueByKey(CacheDataNameEnum.class, key))) {
                topSearchDataCache.put(EnumUtils.getEnumByKey(CacheDataNameEnum.class, key), data);
            }
        }
    }

    // 获取当前请求的url全路径
    private String getRequestUrl() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;
        String url = "";
        if (request != null) {
            url = request.getRequestURL().toString();
        }
        return url;
    }

    private String getRequestHeader(String headerName) {
        // 获取当前请求的 HttpServletRequest
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getHeader(headerName);  // 获取指定请求头
    }

    private <T> boolean shouldCache(Object result) {
        if (!(result instanceof ResultTemplateBean<?> templateBean)) {
            return false;
        }
        return isDataNotEmpty(templateBean);
    }

    private boolean isDataNotEmpty(ResultTemplateBean<?> templateBean) {
        Object data = templateBean.getData();
        if (data == null) {
            return false;
        }

        // 1) 如果 data 有 getDataInfo() 方法 → 优先用它判断
        try {
            Method method = data.getClass().getMethod("getDataInfo");
            Object dataInfo = method.invoke(data);
            if (dataInfo instanceof List<?>) {
                return !((List<?>) dataInfo).isEmpty();
            }
            // 如果 getDataInfo 不是 list，就只要不为空
            return dataInfo != null;
        } catch (NoSuchMethodException ignore) {
            // 没有 getDataInfo 方法，继续往下判断
        } catch (Exception e) {
            return false;
        }

        // 2) data 是 List
        if (data instanceof List<?> list) {
            return !list.isEmpty();
        }

        if (data instanceof String str) {
            return StrUtil.isNotEmpty(str);
        }

        // 3) data 是 Map
        if (data instanceof Map<?, ?> map) {
            return !map.isEmpty();
        }

        // 4) data 是数组
        if (data.getClass().isArray()) {
            return Array.getLength(data) > 0;
        }


        // 5) 其他类型，只要不为 null 就缓存
        return true;
    }

    private Object joinPointProceedAndCacheData(ProceedingJoinPoint joinPoint, String url) throws Throwable {
        Object result = joinPoint.proceed();
        // 新增缓存
        if (shouldCache(result)) {
            // 如果 result 是 ResultTemplateBean
            if (result instanceof ResultTemplateBean<?> templateBean) {
                if (isDataNotEmpty(templateBean)) {
                    cacheData(url, templateBean.getData());
                }
            } else {
                // result 本身就是 VO，直接缓存 VO
                cacheData(url, result);
            }
        }
        return result;
    }
}
