package com.tgmeng.common.util;

import cn.hutool.core.collection.CollectionUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * description: 这个类用来存储代理信息，后续直接放在数据库就行(穷，没有数据库)
 * package: com.tgmeng.common.util
 * className: ProxyPoGenerateUtil
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/7/1 12:54
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CacheUtil {


    private Cache<String, Object> cache;
    /** 最大缓存条数，默认100条 */
    @Value("${my-config.data-cache.top-search.max-size:100}")
    private Long dataCacheMaxSize;
    /** 通用缓存过期时间，yml里找不到就用这里的默认值600秒 */
    @Value("${my-config.data-cache.top-search.expire-time:600}")
    private Long dataCacheExpireTime;


    //延迟初始化，是为了能够拿到上面@vlue的值。保证在spring完成了所有依赖注入之后，再来这个init
    @PostConstruct
    public void init() {
        log.info("Initializing cache with expireTime={} seconds and maxSize={}", dataCacheExpireTime, dataCacheMaxSize);
        this.cache = Caffeine.newBuilder().expireAfter(new Expiry<String, Object>() {
                    @Override
                    public long expireAfterCreate(String key, Object value, long currentTime) {
                        return TimeUnit.SECONDS.toNanos(dataCacheExpireTime);
                    }
                    @Override
                    public long expireAfterUpdate(String key, Object value, long currentTime, long currentDuration) {
                        return currentDuration;
                    }
                    @Override
                    public long expireAfterRead(String key, Object value, long currentTime, long currentDuration) {
                        return currentDuration;
                    }
                }).maximumSize(dataCacheMaxSize)
                .build();
    }

    // 添加数据
    public void put(String key, Object value) {
        cache.put(key, value);
        log.info("🎁新增缓存:{}", key);
    }

    // ========== 获取Value系列 ==========

    // 获取value(单个)
    public Object getValue(String key) {
        return cache.getIfPresent(key);
    }
    //获取value(批量)
    public List<Object> getValue(List<String> keys) {
        return new ArrayList<>(cache.getAllPresent(keys).values());
    }
    // 获取value(全部)
    public Collection<Object> getValue() {
        return cache.asMap().values();
    }

    // ========== 获取Cache(Map)系列 ==========

    // 获取缓存(单个)
    public Map<String,Object> getCache(String key) {
        Object value = cache.getIfPresent(key);
        return value == null ? Collections.emptyMap() : Map.of(key, value);
    }
    // 获取缓存(多个)
    public Map<String,Object> getCache(List<String> keys) {
        return cache.getAllPresent(keys);
    }
    // 获取缓存(全部)
    public Map<String,Object> getCache() {
        return cache.asMap();
    }

    // ========== 获取Keys ==========

    // 获取全部key
    public Set<String> getKeys() {
        return cache.asMap().keySet();
    }

    // ========== 移除缓存 ==========

    // 移除缓存(单个)
    public void remove(String key) {
        cache.invalidate(key);
    }
    // 移除缓存(多个)
    public void remove(List<String> keys) {
        cache.invalidateAll(keys);
    }
    // 移除缓存(全部)
    public void remove() {
        cache.invalidateAll();
    }

    //获取所有热点缓存
    public List<String> getAllCache() {

        return null;
    }

    //获取所有热点缓存标题
    public List<String> getAllCacheTitle() {
        Collection<Object> cacheValue = getValue();
        if (CollectionUtil.isEmpty(cacheValue)) {
            return new ArrayList<>();
        }
        List<String> keywords = new ArrayList<>();
        cacheValue.forEach(t->{
            if (t instanceof Map<?, ?> map) {
                Object dataInfoObj = map.get("dataInfo");
                if (dataInfoObj instanceof List<?> dataInfoList) {
                    dataInfoList.forEach(item -> {
                        if (item instanceof Map<?, ?> itemMap) {
                            Object keyword = itemMap.get("keyword");
                            if (keyword instanceof String s) {
                                keywords.add(s);
                            }
                        }
                    });
                }
            }
        });
        return keywords;
    }
}
