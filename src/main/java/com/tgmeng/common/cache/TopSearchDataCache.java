package com.tgmeng.common.cache;

import cn.hutool.core.util.StrUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.tgmeng.common.enums.business.CacheDataNameEnum;
import com.tgmeng.model.vo.topsearch.TopSearchCommonVO;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Data
@Slf4j
@Component
public class TopSearchDataCache {

    /** 通用缓存过期时间，yml里找不到就用这里的默认值600秒 */
    @Value("${my-config.data-cache.top-search.expire-time:600}")
    private Long dataCacheExpireTime;
    /** GitHub缓存过期时间，yml里找不到就用这里的默认值1800秒 */
    @Value("${my-config.data-cache.top-search.expire-time-github-api-data:1800}")
    private Long dataCacheExpireTimeGithubApiData;
    /** GitHub缓存过期时间浮动范围，yml里找不到就用这里的默认值600秒 */
    @Value("${my-config.data-cache.top-search.expire-time-github-api-data-random-range:600}")
    private Long dataCacheExpireTimeGithubApiDataRandomRange;

    /** 网易云音乐缓存过期时间，yml里找不到就用这里的默认值600秒 */
    @Value("${my-config.data-cache.top-search.expire-time-wang-yi-yun-api-data:600}")
    private Long dataCacheExpireTimeWangYiYunApiData;
    /** 网易云音乐缓存过期时间浮动范围，yml里找不到就用这里的默认值300秒 */
    @Value("${my-config.data-cache.top-search.expire-time-wang-yi-yun-api-data-random-range:300}")
    private Long dataCacheExpireTimeWangYiYunApiDataRandomRange;

    /** 最大缓存条数，默认100条 */
    @Value("${my-config.data-cache.top-search.max-size:100}")
    private Long dataCacheMaxSize;

    private Cache<CacheDataNameEnum, Object> cache;

    //延迟初始化，是为了能够拿到上面@vlue的值。保证在spring完成了所有依赖注入之后，再来这个init
    @PostConstruct
    public void init() {
        log.info("Initializing cache with expireTime={} seconds and maxSize={}", dataCacheExpireTime, dataCacheMaxSize);
        this.cache = Caffeine.newBuilder().expireAfter(new Expiry<CacheDataNameEnum, Object>() {
                    @Override
                    public long expireAfterCreate(CacheDataNameEnum key, Object value, long currentTime) {
                        // 根据不同的枚举类型设置不同的过期时间
                        if(StrUtil.contains(key.getKey(),"CACHE_TOP_SEARCH_GITHUB")){
                            // github缓存的过期时间浮动区间
                            Random random = new Random();
                            Long randomOffset = random.nextLong(2 * dataCacheExpireTimeGithubApiDataRandomRange + 1) - dataCacheExpireTimeGithubApiDataRandomRange;
                            return TimeUnit.SECONDS.toNanos(dataCacheExpireTimeGithubApiData+randomOffset);
                        }else if(StrUtil.contains(key.getKey(),"CACHE_TOP_SEARCH_WANG_YI_YUN")){
                            // 网易云缓存的过期时间浮动区间
                            Random random = new Random();
                            Long randomOffset = random.nextLong(2 * dataCacheExpireTimeWangYiYunApiDataRandomRange + 1) - dataCacheExpireTimeWangYiYunApiDataRandomRange;
                            return TimeUnit.SECONDS.toNanos(dataCacheExpireTimeWangYiYunApiData+randomOffset);
                        } else {
                            return TimeUnit.SECONDS.toNanos(dataCacheExpireTime);
                        }
                    }

                    @Override
                    public long expireAfterUpdate(CacheDataNameEnum key, Object value, long currentTime, long currentDuration) {
                        return currentDuration;
                    }

                    @Override
                    public long expireAfterRead(CacheDataNameEnum key, Object value, long currentTime, long currentDuration) {
                        return currentDuration;
                    }
                }).maximumSize(dataCacheMaxSize)
                .build();
    }

    // 添加数据到缓存
    public <T> void put(CacheDataNameEnum key, T value) {
        cache.put(key, value);
        log.info("🎁新增缓存:{}", ((TopSearchCommonVO) value).getDataCardName());
    }

    // 从缓存中获取数据
    public <T> T get(CacheDataNameEnum key, Class<T> clazz) {
        Object value = cache.getIfPresent(key);
        if (value == null) {
            return null;
        }
        //log.info("命中缓存：key:{}", key);
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
