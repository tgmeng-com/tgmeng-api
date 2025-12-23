package com.tgmeng.service.history.Impl;

import cn.hutool.core.util.StrUtil;
import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.enums.business.PlatFormCategoryEnum;
import com.tgmeng.common.exception.ServerException;
import com.tgmeng.common.util.DuckDBUtil;
import com.tgmeng.common.util.SimHashUtil;
import com.tgmeng.service.history.ITopSearchHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TopSearchHistoryServiceImpl implements ITopSearchHistoryService {

    @Value("${my-config.history.keep-day}")
    private Integer historyDataKeepDay;

    @Value("${my-config.history.sim-hash-distance}")
    private Integer simHashDistance;

    @Value("${my-config.history.sudden-heat-point-time-window}")
    private Integer defaultSuddenHeatPointTimeWindow;

    @Value("${my-config.history.sudden-heat-point-platform-num-least}")
    private Integer suddenHeatPointPlatformNumLeast;

    @Value("${my-config.history.sudden-heat-point-result-limit}")
    private Integer suddenHeatPointResultLimit;

    @Autowired
    private DuckDBUtil duckdb;

    // 突发热点查询中需要排除的平台，因为这些平台的热点基本没有意义或者指纹干扰很大
    // 这个是根据平台分类去排除
    private static final Set<String> EXCLUDED_PLATFORM_CATEGORIES = Set.of(
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
    private static final Set<String> EXCLUDED_PLATFORM_NAMES = Set.of(
            "电视猫",
            "微信读书",
            "HACKER_NEWS",
            "腾讯设计开放平台",
            "Abduzeedo",
            "Core77",
            "Dribbble",
            "Awwwards"
    );
    private static final String EXCLUDED_PLATFORM_CATEGORIES_SQL = EXCLUDED_PLATFORM_CATEGORIES.stream().map(s -> "'" + s + "'").collect(Collectors.joining(", "));
    private static final String EXCLUDED_PLATFORM_NAMES_SQL = EXCLUDED_PLATFORM_NAMES.stream().map(s -> "'" + s + "'").collect(Collectors.joining(", "));


    // 热点历史轨迹，指纹匹配
    @Override
    public ResultTemplateBean getHotPointHistory(Map<String, String> requestBody) {
        String title = requestBody.get("title");
        String startTime = requestBody.get("startTime");
        String endTime = requestBody.get("endTime");
        if (StrUtil.isBlank(title) || StrUtil.isBlank(startTime) || StrUtil.isBlank(endTime)) {
            throw new ServerException("param empty error");
        }

        List<Map<String, Object>> result = trackHotspotHistory(title, startTime, endTime);
        return ResultTemplateBean.success(result);
    }

    // 突发热点
    @Override
    public ResultTemplateBean getSuddenHeatPoint(String type) {
        List<Map<String, Object>> result = suddenHeatPoint(getTimeWindow(type), suddenHeatPointPlatformNumLeast, suddenHeatPointResultLimit);
        return ResultTemplateBean.success(result);
    }

    // 历史数据，模糊匹配
    @Override
    public ResultTemplateBean getWordHistory(Map<String, String> requestBody) {
        String title = requestBody.get("title");
        if (StrUtil.isEmpty(title)) {
            throw new ServerException("word empty error");
        }
        String startTime = requestBody.get("startTime");
        String endTime = requestBody.get("endTime");
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime start = LocalDateTime.parse(startTime, formatter);
            LocalDateTime end = LocalDateTime.parse(endTime, formatter);
            // 根据时间范围获取要扫描的数据范围
            String pathPattern = duckdb.buildPathPattern(start, end);
            String titleLike = "%" + title.trim() + "%";
            ;
            String sql = String.format("""
                    SELECT
                        dataUpdateTime,
                        platformName,
                        platformCategory,
                        title,
                        url,
                        simHash
                    FROM (
                        SELECT DISTINCT ON (title, platformName)
                            dataUpdateTime,
                            platformName,
                            platformCategory,
                            title,
                            url,
                            simHash
                        FROM read_parquet('%s')
                        WHERE simHash IS NOT NULL
                          AND dataUpdateTime >= '%s'
                          AND dataUpdateTime <= '%s'
                          AND lower(title) LIKE lower('%s')
                        ORDER BY title, platformName, dataUpdateTime ASC  -- 去重时保留最早的
                    ) t
                    ORDER BY dataUpdateTime DESC;  -- 最终结果整体降序（最新在前）
                    """, pathPattern, startTime, endTime, titleLike);
            List<Map<String, Object>> query = duckdb.query(sql);
            log.info("查询热点成功，关键词：{}，共 {} 条记录", title, query.size());
            return ResultTemplateBean.success(query);
        } catch (Exception e) {
            log.error("查询热点失败：{}，错误信息：{}", title, e.getMessage());
            throw new ServerException("error");
        }
    }

    private int getTimeWindow(String type) {
        return switch (type) {
            case "hour" -> 60;
            case "3hour" -> 60 * 3;
            case "6hour" -> 60 * 6;
            case "day" -> 60 * 24;
            case "10day" -> 60 * 24 * 10;
            default -> defaultSuddenHeatPointTimeWindow;
        };
    }

    /**
     * 根据 SimHash 追踪热点历史轨迹
     * @param startTime 开始时间（格式：2025-12-20T00:00:00）
     * @param endTime 结束时间（格式：2025-12-20T23:59:59）
     * @return 历史轨迹列表
     */
    public List<Map<String, Object>> trackHotspotHistory(String title, String startTime, String endTime) {
        try {
            long simHash = SimHashUtil.calculateSimHash(title);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime start = LocalDateTime.parse(startTime, formatter);
            LocalDateTime end = LocalDateTime.parse(endTime, formatter);
            // 根据时间范围获取要扫描的数据范围
            String pathPattern = duckdb.buildPathPattern(start, end);
            String sql = String.format("""
                            SELECT
                                dataUpdateTime,
                                platformName,
                                platformCategory,
                                title,
                                url,
                                simHash,
                                BIT_COUNT(xor(simHash, %d)) as distance
                            FROM (
                                SELECT DISTINCT ON (simHash, platformName)  -- 按 simHash 和平台去重
                                    *,
                                    BIT_COUNT(xor(simHash, %d)) as distance
                                FROM read_parquet('%s')
                                WHERE simHash IS NOT NULL
                                  AND dataUpdateTime >= '%s'
                                  AND dataUpdateTime <= '%s'
                                  AND BIT_COUNT(xor(simHash, %d)) <= %d
                                ORDER BY simHash, platformName, dataUpdateTime ASC  -- 每个平台保留最早的
                            ) t
                            ORDER BY dataUpdateTime DESC
                    """, simHash, simHash, pathPattern, startTime, endTime, simHash, simHashDistance);
            List<Map<String, Object>> query = duckdb.query(sql);
            log.info("查询历史热点轨迹成功，标题：{}，共 {} 条记录", title, query.size());
            return query;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServerException("热点历史追踪查询异常");
        }
    }


    /**
     * 查询突发热点（在多个平台同时出现）
     * @param minutes 时间窗口（分钟）
     * @param minPlatforms 最少出现的平台数
     * @return 突发热点列表
     */
    public List<Map<String, Object>> suddenHeatPoint(int minutes, int minPlatforms, int limit) {
        try {
            LocalDateTime endTime = LocalDateTime.now();
            LocalDateTime startTime = endTime.minusMinutes(minutes);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String startStr = startTime.format(formatter);
            String endStr = endTime.format(formatter);

            String pathPattern = duckdb.buildPathPattern(startTime, endTime);

            String sql = String.format("""
                     WITH recent_data AS (
                                         -- 获取最近时间窗口内的数据，并计算分桶ID
                                         SELECT\s
                                             dataUpdateTime,
                                             platformName,
                                             platformCategory,
                                             title,
                                             url,
                                             simHash,
                                             -- 使用 SimHash 的高48位作为桶ID（可调整位数）
                                             (simHash >> 16) as bucket_id
                                         FROM read_parquet('%s')
                                         WHERE simHash IS NOT NULL
                                           AND dataUpdateTime >= '%s'
                                           AND dataUpdateTime <= '%s'
                                           AND platformCategory NOT IN (%s)
                                           AND platformName NOT IN (%s)
                                     ),
                                     -- 同平台去重（保留最新的）
                                     dedup_data AS (
                                         SELECT DISTINCT ON (platformName, simHash)
                                             *
                                         FROM recent_data
                                         ORDER BY platformName, simHash, dataUpdateTime DESC
                                     ),
                                     -- 只在同一桶内进行 SimHash 聚类
                                     hotspot_groups AS (
                                         SELECT\s
                                             a.simHash as original_simhash,
                                             a.title as original_title,
                                             a.platformName,
                                             a.platformCategory,
                                             a.url,
                                             a.dataUpdateTime,
                                             MIN(b.simHash) as group_id
                                         FROM dedup_data a
                                         JOIN dedup_data b\s
                                             ON a.bucket_id = b.bucket_id  -- 关键：只在同桶内连接
                                             AND BIT_COUNT(xor(a.simHash, b.simHash)) <= %d
                                         GROUP BY a.simHash, a.title, a.platformName, a.platformCategory, a.url, a.dataUpdateTime
                                     ),
                                     -- 统计每个组的平台覆盖情况，并收集所有热点详情
                                     group_stats AS (
                                         SELECT\s
                                             group_id,
                                             COUNT(DISTINCT platformName) as platform_count,
                                             COUNT(*) as total_count,
                                             LIST(DISTINCT platformName) as platforms,
                                             MIN(dataUpdateTime) as first_seen,
                                             MAX(dataUpdateTime) as last_seen,
                                             -- 收集该组所有热点的详细信息
                                             LIST(STRUCT_PACK(
                                                 title := original_title,
                                                 url := url,
                                                 platformName := platformName,
                                                 platformCategory := platformCategory,
                                                 dataUpdateTime := dataUpdateTime
                                             ) ORDER BY dataUpdateTime DESC ) as hotspot_list
                                         FROM hotspot_groups
                                         GROUP BY group_id
                                     ),
                                     -- 获取每个组的代表性标题
                                     representative_titles AS (
                                         SELECT DISTINCT ON (group_id)
                                             group_id,
                                             original_title as title,
                                             url,
                                             platformName as sample_platform,
                                             dataUpdateTime
                                         FROM hotspot_groups
                                         ORDER BY group_id, dataUpdateTime DESC
                                     )
                                     -- 最终结果
                                     SELECT\s
                                         rt.title,
                                         rt.url,
                                         rt.sample_platform,
                                         gs.platform_count,
                                         gs.total_count,
                                         gs.platforms,
                                         gs.hotspot_list,
                                         gs.first_seen,
                                         gs.last_seen,
                                         rt.group_id as simhash_group
                                     FROM group_stats gs
                                     JOIN representative_titles rt ON gs.group_id = rt.group_id
                                     WHERE gs.platform_count >= %d
                                     ORDER BY gs.platform_count DESC, gs.total_count DESC
                                     LIMIT %d
                    """, pathPattern, startStr, endStr, EXCLUDED_PLATFORM_CATEGORIES_SQL, EXCLUDED_PLATFORM_NAMES_SQL, simHashDistance, minPlatforms, limit);
            List<Map<String, Object>> query = duckdb.query(sql);

            List<Map<String, Object>> result = query.stream().map(row -> {
                Map<String, Object> processedRow = new HashMap<>(row);

                // 处理 platforms（LIST 类型）
                Object platforms = row.get("platforms");
                if (platforms != null) {
                    processedRow.put("platforms", convertToList(platforms));
                }

                // 处理 hotspot_list（LIST<STRUCT> 类型）
                Object hotspotList = row.get("hotspot_list");
                if (hotspotList != null) {
                    processedRow.put("hotspot_list", convertToList(hotspotList));
                }

                return processedRow;
            }).toList();
            log.info("查询突发热点成功，共 {} 条记录", result.size());
            return result;
        } catch (Exception e) {
            log.error("突发热点查询异常: {}", e.getMessage(), e);
            throw new ServerException("突发热点查询异常");
        }
    }

    /**
     * 将 DuckDB 的复杂类型转换为普通 List
     */
    private List<Object> convertToList(Object obj) {
        if (obj == null) {
            return new java.util.ArrayList<>();
        }

        List<Object> result = new java.util.ArrayList<>();

        try {
            // 处理 DuckDBArray 类型
            if (obj.getClass().getName().contains("DuckDBArray")) {
                // 获取 getArray() 方法
                java.lang.reflect.Method getArrayMethod = obj.getClass().getMethod("getArray");
                Object array = getArrayMethod.invoke(obj);

                if (array != null && array.getClass().isArray()) {
                    int length = java.lang.reflect.Array.getLength(array);
                    for (int i = 0; i < length; i++) {
                        Object item = java.lang.reflect.Array.get(array, i);
                        // 如果是 STRUCT，转换为 Map
                        if (item != null && item.getClass().getName().contains("Struct")) {
                            result.add(convertStructToMap(item));
                        } else {
                            result.add(item);
                        }
                    }
                }
            }
            // 如果已经是普通 List
            else if (obj instanceof List) {
                result.addAll((List<?>) obj);
            }
            // 如果是普通数组
            else if (obj.getClass().isArray()) {
                int length = java.lang.reflect.Array.getLength(obj);
                for (int i = 0; i < length; i++) {
                    result.add(java.lang.reflect.Array.get(obj, i));
                }
            }
        } catch (Exception e) {
            log.warn("转换 List 失败: {}, 类型: {}", e.getMessage(), obj.getClass().getName());
        }

        return result;
    }

    /**
     * 将 DuckDB 的 STRUCT 类型转换为 Map
     */
    private Map<String, Object> convertStructToMap(Object struct) {
        Map<String, Object> map = new java.util.HashMap<>();

        if (struct == null) {
            return map;
        }

        try {
            // 尝试调用 getMap() 方法（如果 STRUCT 有这个方法）
            try {
                java.lang.reflect.Method getMapMethod = struct.getClass().getMethod("getMap");
                Object mapObj = getMapMethod.invoke(struct);
                if (mapObj instanceof Map) {
                    Map<?, ?> sourceMap = (Map<?, ?>) mapObj;
                    for (Map.Entry<?, ?> entry : sourceMap.entrySet()) {
                        map.put(String.valueOf(entry.getKey()), entry.getValue());
                    }
                    return map;
                }
            } catch (NoSuchMethodException e) {
                // 没有 getMap 方法，继续用反射
            }

            // 通过反射获取 STRUCT 的所有字段
            java.lang.reflect.Method[] methods = struct.getClass().getMethods();
            for (java.lang.reflect.Method method : methods) {
                String methodName = method.getName();
                // 查找 getter 方法
                if (methodName.startsWith("get")
                        && !methodName.equals("getClass")
                        && !methodName.equals("getMap")
                        && method.getParameterCount() == 0) {

                    String fieldName = methodName.substring(3);
                    // 转换首字母为小写
                    if (fieldName.length() > 0) {
                        fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
                    }

                    Object value = method.invoke(struct);
                    map.put(fieldName, value);
                }
            }
        } catch (Exception e) {
            log.warn("转换 STRUCT 失败: {}, 类型: {}", e.getMessage(), struct.getClass().getName());
        }

        return map;
    }
}
