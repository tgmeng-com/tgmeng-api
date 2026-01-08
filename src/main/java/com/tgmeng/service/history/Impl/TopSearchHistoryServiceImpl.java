package com.tgmeng.service.history.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.exception.ServerException;
import com.tgmeng.common.util.CacheUtil;
import com.tgmeng.common.util.DuckDBUtil;
import com.tgmeng.common.util.SimHashUtil;
import com.tgmeng.common.util.StringUtil;
import com.tgmeng.service.history.ITopSearchHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class TopSearchHistoryServiceImpl implements ITopSearchHistoryService {

    @Value("${my-config.history.keep-day}")
    private Integer historyDataKeepDay;

    @Value("${my-config.history.dir}")
    private String historyDataDir;

    @Value("${my-config.history.sim-hash-distance}")
    private Integer simHashDistance;

    @Value("${my-config.history.sudden-heat-point-time-window}")
    private Integer defaultSuddenHeatPointTimeWindow;

    @Value("${my-config.history.sudden-heat-point-platform-num-least}")
    private Integer suddenHeatPointPlatformNumLeast;

    @Value("${my-config.history.sudden-heat-point-result-limit}")
    private Integer suddenHeatPointResultLimit;

    @Value("${my-config.admin-password}")
    private String adminPassword;

    @Autowired
    private DuckDBUtil duckdb;
    @Autowired
    private CacheUtil cacheUtil;

    // 热点历史轨迹，指纹匹配
    @Override
    public ResultTemplateBean getHotPointHistory(Map<String, Object> requestBody) {
        String title = requestBody.get("title").toString();
        String startTime = requestBody.get("startTime").toString();
        String endTime = requestBody.get("endTime").toString();
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
    public ResultTemplateBean getWordHistory(Map<String, Object> requestBody) {
        String title = getStringParam(requestBody, "title");
        List<String> platformNames = getListParam(requestBody, "platformName");
        List<String> platformCategories = getListParam(requestBody, "platformCategory");
        List<String> platformCategoryRoots = getListParam(requestBody, "platformCategoryRoot");
        String startTime = getStringParam(requestBody, "startTime");
        String endTime = getStringParam(requestBody, "endTime");
        String keepLatest = getStringParam(requestBody, "keepLatest");  // 新增参数
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime start = LocalDateTime.parse(startTime, formatter);
            LocalDateTime end = LocalDateTime.parse(endTime, formatter);
            // 根据时间范围获取要扫描的数据范围
            String pathPattern = duckdb.buildPathPattern(start, end);
            // 动态构建条件
            StringBuilder conditions = new StringBuilder();
            if (StrUtil.isNotEmpty(title)) {
                String titleLike = "%" + title.trim() + "%";
                conditions.append(String.format("AND lower(title) LIKE lower('%s') ", titleLike));
            }
            if (CollUtil.isNotEmpty(platformNames)) {
                String inClause = buildInClause(platformNames);
                conditions.append(String.format("AND platformName IN (%s) ", inClause));
            }
            if (CollUtil.isNotEmpty(platformCategories)) {
                String inClause = buildInClause(platformCategories);
                conditions.append(String.format("AND platformCategory IN (%s) ", inClause));
            }
            if (CollUtil.isNotEmpty(platformCategoryRoots)) {
                String inClause = buildInClause(platformCategoryRoots);
                conditions.append(String.format("AND platformCategoryRoot IN (%s) ", inClause));
            }
            // 根据参数决定排序方式（保留最新还是最老）
            String orderDirection = StrUtil.isNotEmpty(keepLatest) ? "DESC" : "ASC";
            String keepStrategy = StrUtil.isNotEmpty(keepLatest) ? "最新" : "最早";


            String sql = String.format("""
            SELECT
                dataUpdateTime,
                platformName,
                platformCategory,
                platformCategoryRoot,
                title,
                url,
                sort,
                simHash
            FROM (
                SELECT DISTINCT ON (title, platformName)
                    dataUpdateTime,
                    platformName,
                    platformCategory,
                    platformCategoryRoot,
                    title,
                    url,
                    sort,
                    simHash
                FROM read_parquet('%s')
                WHERE simHash IS NOT NULL
                  AND dataUpdateTime >= '%s'
                  AND dataUpdateTime <= '%s'
                  %s
                ORDER BY title, platformName, dataUpdateTime %s  -- 去重时动态保留最新的还是最老的
            ) t
            ORDER BY dataUpdateTime DESC,sort ASC;  -- 最终结果整体降序（最新在前）
            """, pathPattern, startTime, endTime, conditions.toString(),orderDirection);

            List<Map<String, Object>> query = duckdb.query(sql);
            log.info("查询热点成功,关键词:{},平台:{},分类:{},根分类:{},去重策略:保留{},共 {} 条记录",
                    StrUtil.isEmpty(title) ? "全部" : title,
                    CollUtil.isEmpty(platformNames) ? "全部" : platformNames,
                    CollUtil.isEmpty(platformCategories) ? "全部" : platformCategories,
                    CollUtil.isEmpty(platformCategoryRoots) ? "全部" : platformCategoryRoots,
                    keepStrategy,
                    query.size());
            return ResultTemplateBean.success(query);
        } catch (Exception e) {
            log.error("查询热点失败:{},错误信息:{}", title, e.getMessage());
            throw new ServerException("error");
        }
    }

    private String getStringParam(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : null;
    }

    @SuppressWarnings("unchecked")
    private List<String> getListParam(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof List) {
            return (List<String>) value;
        }
        if (value instanceof String) {
            String str = (String) value;
            if (StrUtil.isBlank(str)) {
                return null;
            }
            return Arrays.asList(str);
        }
        return null;
    }

    private String buildInClause(List<String> values) {
        if (CollUtil.isEmpty(values)) {
            return "";
        }
        return values.stream()
                .filter(StrUtil::isNotBlank)
                .map(String::trim)
                .map(v -> "'" + v.replace("'", "''") + "'")
                .collect(Collectors.joining(", "));
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
                                platformCategoryRoot,
                                title,
                                url,
                                sort,
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
                            ORDER BY dataUpdateTime DESC,sort ASC;
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
            // 突发热点查询中需要排除的平台，因为这些平台的热点基本没有意义或者指纹干扰很大
            String EXCLUDED_PLATFORM_CATEGORIES_SQL = cacheUtil.EXCLUDED_PLATFORM_CATEGORIES.stream().map(s -> "'" + s + "'").collect(Collectors.joining(", "));
            String EXCLUDED_PLATFORM_NAMES_SQL = cacheUtil.EXCLUDED_PLATFORM_NAMES.stream().map(s -> "'" + s + "'").collect(Collectors.joining(", "));

            String sql = String.format("""
                     WITH recent_data AS (
                                         -- 获取最近时间窗口内的数据，并计算分桶ID
                                         SELECT
                                             dataUpdateTime,
                                             platformName,
                                             platformCategory,
                                             platformCategoryRoot,
                                             title,
                                             url,
                                             sort,
                                             simHash,
                                             -- 使用 SimHash 的高48位作为桶ID（可调整位数）
                                             (simHash >> 16) as bucket_id
                                         FROM read_parquet('%s')
                                         WHERE simHash IS NOT NULL
                                           AND simHash != 0
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
                                         SELECT
                                             a.simHash as original_simhash,
                                             a.title as original_title,
                                             a.platformName,
                                             a.platformCategory,
                                             a.platformCategoryRoot,
                                             a.url,
                                             a.dataUpdateTime,
                                             MIN(b.simHash) as group_id
                                         FROM dedup_data a
                                         JOIN dedup_data b
                                             ON a.bucket_id = b.bucket_id  -- 关键：只在同桶内连接
                                             AND a.simHash >= b.simHash
                                             AND BIT_COUNT(xor(a.simHash, b.simHash)) <= %d
                                         GROUP BY a.simHash, a.title, a.platformName, a.platformCategory, a.platformCategoryRoot, a.url, a.dataUpdateTime
                                     ),
                                     -- 统计每个组的平台覆盖情况，并收集所有热点详情
                                     group_stats AS (
                                         SELECT
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
                                                 platformCategoryRoot := platformCategoryRoot,
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
                                     SELECT
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
     * 合并parquet文件，参数例如
     * ./data/history/2025/12/28/
     *  2025-12-28.parquet"
     */
    @Override
    public ResultTemplateBean mergeParquetByGlob(Map<String, String> requestBody) {
        String password = String.valueOf(requestBody.get("password"));
        if (!adminPassword.equals(password)) {
            return ResultTemplateBean.success("管理员密码无效");
        }

        String sourceDir = requestBody.get("sourceDir");
        String targetFile = requestBody.get("targetFile");

        Path sourceRoot = Paths.get(sourceDir).toAbsolutePath().normalize();
        Path targetPath = sourceRoot.resolve(targetFile).normalize();
        Path tmpTargetPath = sourceRoot.resolve(targetFile + ".tmp").normalize();

        String sourceFileListPattern =
                StringUtil.replaceForFilePath(sourceRoot + "/**/*.parquet");

        try {
            log.info("开始合并 Parquet 文件");
            log.info("源路径: {}", sourceFileListPattern);
            log.info("目标文件: {}", targetPath);

            long start = System.currentTimeMillis();

            // 1️⃣ COPY 到临时文件（⚠️ 不要直接写目标文件），他妈这里一直有进程占用问题，所以咱给他妈的搞个临时文件弄一下
            String sql = String.format("""
                        COPY (
                            SELECT *
                            FROM read_parquet('%s')
                        )
                        TO '%s'
                        (FORMAT 'parquet', COMPRESSION 'zstd');
                    """, sourceFileListPattern, tmpTargetPath.toString());

            long row = duckdb.execute(sql);

            long mergeCost = System.currentTimeMillis() - start;
            log.info("Parquet 合并完成，行数: {}，耗时: {} ms", row, mergeCost);

            // 2️⃣ 等待 DuckDB 在 Windows 下释放句柄
            Thread.sleep(100);

            // 3️⃣ 原子替换目标文件（并发 SELECT 安全）
            Files.move(
                    tmpTargetPath,
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE
            );

            log.info("目标文件已原子替换: {}", targetPath);

            // 4️⃣ 删除源 parquet + crc + 空目录（排除目标文件）
            deleteSourceFilesAndEmptyDirs(sourceRoot, targetPath);

            return ResultTemplateBean.success(Map.of(
                    "源目录", sourceDir,
                    "目标文件", targetFile,
                    "合并行数", row,
                    "耗时(ms)", mergeCost
            ));
        } catch (Exception e) {
            log.error("Parquet 文件合并失败", e);

            // 清理残留 tmp
            try {
                Files.deleteIfExists(tmpTargetPath);
            } catch (Exception ignore) {
            }

            throw new ServerException("Parquet 文件合并失败: " + e.getMessage());
        }
    }

    /**
     * 删除 globPattern 匹配的所有 parquet 文件，但排除 targetFile
     */
    private void deleteSourceFilesAndEmptyDirs(Path root, Path targetPath) {
        try {
            // 1️⃣ 删除 parquet / crc（排除目标文件）
            Files.walk(root)
                    .filter(Files::isRegularFile)
                    .filter(p ->
                            p.toString().endsWith(".parquet")
                                    || p.toString().endsWith(".crc")
                    )
                    .filter(p -> !p.normalize().equals(targetPath))
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                            log.info("删除源文件: {}", p);
                        } catch (Exception e) {
                            log.warn("删除文件失败: {}, {}", p, e.getMessage());
                        }
                    });

            // 2️⃣ 删除空目录（从深到浅）
            Files.walk(root)
                    .filter(Files::isDirectory)
                    .sorted((a, b) -> b.getNameCount() - a.getNameCount())
                    .forEach(dir -> {
                        try (Stream<Path> stream = Files.list(dir)) {
                            if (stream.findAny().isEmpty()) {
                                Files.delete(dir);
                                log.info("删除空目录: {}", dir);
                            }
                        } catch (Exception ignore) {
                        }
                    });

        } catch (Exception e) {
            log.error("清理源文件失败", e);
        }
    }

    //执行sql，自己跑着用
    @Override
    public ResultTemplateBean customexcutesql(Map<String, String> requestBody) {
        String password = requestBody.get("password");
        String mode = requestBody.get("mode");
        if (adminPassword.equals(password)) {
            String sql = requestBody.get("sql");
            try {
                if ("query".equalsIgnoreCase(mode)) {
                    List<Map<String, Object>> query = duckdb.query(sql);
                    return ResultTemplateBean.success(query);
                }else if ("execute".equalsIgnoreCase(mode)) {
                    long execute = duckdb.execute(sql);
                    return ResultTemplateBean.success(execute);
                }
                return ResultTemplateBean.success("模式不合适");
            } catch (Exception e) {
                log.error("执行sql失败：{}，错误：{}", sql, e.getMessage());
                throw new ServerException("执行sql失败：" + sql + "，错误：" + e.getMessage());
            }
        }
        return ResultTemplateBean.success("管理员密码无效");
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

    private int getTimeWindow(String type) {
        return switch (type) {
            case "hour" -> 60;
            case "3hour" -> 60 * 3;
            case "6hour" -> 60 * 6;
            case "day" -> 60 * 24;
            case "10day" -> 60 * 24 * 10;
            case "month" -> 60 * 24 * 31;
            case "history" -> 60 * 24 * 31 * 12 * 50;
            default -> defaultSuddenHeatPointTimeWindow;
        };
    }
}
