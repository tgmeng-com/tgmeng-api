package com.tgmeng.common.interceptor;

import cn.hutool.core.collection.CollectionUtil;
import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.cache.TopSearchDataCache;
import com.tgmeng.common.enums.business.CacheDataNameEnum;
import com.tgmeng.common.enums.enumcommon.EnumUtils;
import com.tgmeng.model.vo.topsearch.TopSearchCommonVO;
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

@Slf4j
@Aspect
@Component
public class TopSearchDataCacheInterceptor {

    @Autowired
    private TopSearchDataCache topSearchDataCache;

    @Value("${my-config.data-cache.top-search.enabled:true}")
    private Boolean dataCacheEnabled;

    // 定义一个切点，拦截所有Controller中的方法（可以根据需要具体化）
    @Pointcut("execution(* com.tgmeng.controller.topsearch..*(..))")
    public void cachePointcut() {
    }

    @Around("cachePointcut()")
    public Object aroundRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        String url = getRequestUrl();
        if (dataCacheEnabled){
            //获取缓存
            Object cachedData = getCachedData(url);
            if (cachedData != null) {
                log.info("❤️返回缓存：{}", ((TopSearchCommonVO)cachedData).getDataCardName());
                return ResultTemplateBean.success(cachedData);
            } else {
                log.info("🤡缓存未命中，调用接口获取数据：{}", url);
                // 执行接口请求数据
                Object result = joinPoint.proceed();
                // 新增缓存
                if(result != null&&CollectionUtil.isNotEmpty((((ResultTemplateBean<TopSearchCommonVO>)result).getData().getDataInfo()))) {
                    cacheData(url, ((ResultTemplateBean<TopSearchCommonVO>) result).getData());
                }
                return result;
            }
        }else {
            log.info("🤡缓存未开启，调用接口获取数据：{}", url);
            // 执行接口请求数据
            return joinPoint.proceed();
        }

    }

    // 获取缓存数据
    private Object getCachedData(String url) {
        for (String key : EnumUtils.getKeys(CacheDataNameEnum.class)) {
            if (url.contains(EnumUtils.getValueByKey(CacheDataNameEnum.class, key))) {
                return topSearchDataCache.get(EnumUtils.getEnumByKey(CacheDataNameEnum.class, key), TopSearchCommonVO.class);
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
}
