package com.tgmeng.common.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.tgmeng.common.enums.business.PlatFormCategoryEnum;
import com.tgmeng.common.enums.business.PlatFormCategoryRootEnum;
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
        log.info("🙋🏻‍♂️查询缓存:{}", key);
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

    // 这个是根据平台分类去排除，这些平台噪点大，意义不大，排除掉，比如ai总结、突发热点的时候用
    public Set<String> EXCLUDED_PLATFORM_CATEGORIES_ROOT = Set.of(
            //PlatFormCategoryRootEnum.YANG_MAO.getValue(),
            PlatFormCategoryRootEnum.DIAN_SHI.getValue(),
            //PlatFormCategoryRootEnum.SHENG_HUO.getValue(),
            PlatFormCategoryRootEnum.YING_YIN.getValue()
            //PlatFormCategoryRootEnum.YOU_XI.getValue()
            //PlatFormCategoryRootEnum.JIAN_KANG.getValue(),
            //PlatFormCategoryRootEnum.SHE_JI.getValue()
            //PlatFormCategoryRootEnum.SHE_QU.getValue()
            //PlatFormCategoryRootEnum.TI_YU.getValue()
    );
    public Set<String> EXCLUDED_PLATFORM_CATEGORIES = Set.of(
            PlatFormCategoryEnum.BAI_DU.getValue(),
            PlatFormCategoryEnum.GITHUB.getValue(),
            PlatFormCategoryEnum.HUGGING_FACES.getValue(),
            PlatFormCategoryEnum.ZHAN_KU.getValue(),
            PlatFormCategoryEnum.TU_YA_WANG_GUO.getValue(),
            PlatFormCategoryEnum.MAO_YAN.getValue(),
            PlatFormCategoryEnum.TENG_XUN_SHI_PIN.getValue(),
            PlatFormCategoryEnum.AI_QI_YI_SHI_PIN.getValue(),
            PlatFormCategoryEnum.MANG_GUO_SHI_PIN.getValue(),
            PlatFormCategoryEnum.YOU_KU_SHI_PIN.getValue(),
            PlatFormCategoryEnum.WANG_YI_YUN_YIN_YUE.getValue(),
            PlatFormCategoryEnum.FOUR_GAMER.getValue(),
            PlatFormCategoryEnum.CCTV.getValue()

    );
    // 这个是根据平台名称去排除
    public Set<String> EXCLUDED_PLATFORM_NAMES = Set.of(
            "电视猫",
            "微信读书",
            "HACKER_NEWS",
            "腾讯设计开放平台",
            "Abduzeedo",
            "Core77",
            "Dribbble",
            "Awwwards",
            "Youtube"
    );
}
