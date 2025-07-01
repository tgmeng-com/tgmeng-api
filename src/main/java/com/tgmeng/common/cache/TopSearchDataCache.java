package com.tgmeng.common.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.tgmeng.common.enums.business.CacheDataNameEnum;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Data
@Slf4j
@Component
public class TopSearchDataCache {

    /** 最大缓存条数，默认10分钟 */
    @Value("${my-config.data-cache.top-search.expire-time:600}")
    private Long dataCacheExpireTime;
    /** 最大缓存条数，默认100条 */
    @Value("${my-config.data-cache.top-search.max-size:100}")
    private Long dataCacheMaxSize;

    private Cache<CacheDataNameEnum, Object> cache;

    //延迟初始化，是为了能够拿到上面@vlue的值。保证在spring完成了所有依赖注入之后，再来这个init
    @PostConstruct
    public void init() {
        log.info("Initializing cache with expireTime={} seconds and maxSize={}", dataCacheExpireTime, dataCacheMaxSize);
        this.cache = Caffeine.newBuilder()
                .maximumSize(dataCacheMaxSize)  // 设置缓存的最大大小，多少条数据
                .expireAfterWrite(dataCacheExpireTime, TimeUnit.SECONDS)  // 设置缓存过期时间
                .build();
    }

    // 添加数据到缓存
    public <T> void put(CacheDataNameEnum key, T value) {
        cache.put(key, value);
        log.info("新增缓存：key:{}", key);
    }

    // 从缓存中获取数据
    public <T> T get(CacheDataNameEnum key, Class<T> clazz) {
        Object value = cache.getIfPresent(key);
        if (value == null) {
            return null;
        }
        log.info("命中缓存：key:{}", key);
        return clazz.cast(value);  // 强制转换成目标类型并返回
    }

    // 清除缓存中的某个数据
    public void remove(CacheDataNameEnum key) {
        cache.invalidate(key);
        log.info("清除缓存：key:{} ", key);
    }

    // 清除所有缓存数据
    public void clear() {
        cache.invalidateAll();
        log.info("清除所有缓存");
    }
}
