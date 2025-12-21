package com.tgmeng.service.history.Impl;

import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.exception.ServerException;
import com.tgmeng.common.util.DuckDBUtil;
import com.tgmeng.common.util.SimHashUtil;
import com.tgmeng.common.util.TimeUtil;
import com.tgmeng.service.history.ITopSearchHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class TopSearchHistoryServiceImpl implements ITopSearchHistoryService {

    @Value("${my-config.history.keep-day}")
    private Integer historyDataKeepDay;

    @Value("${my-config.history.sim-hash-distance}")
    private Integer simHashDistance;

    @Autowired
    private DuckDBUtil duckdb;


    // 热点历史轨迹
    @Override
    public ResultTemplateBean getHotPointHistory(Map<String, String> requestBody) {
        String title = requestBody.get("title");
        String startTime = TimeUtil.getTimeBeforeNow(0, 0, 0, historyDataKeepDay, "yyyy-MM-dd HH:mm:ss");
        String endTime = TimeUtil.getCurrentTimeFormat("yyyy-MM-dd HH:mm:ss");
        List<Map<String, Object>> result = trackHotspotHistory(title, startTime, endTime);
        return ResultTemplateBean.success(result);
    }

    // 突发热点
    @Override
    public ResultTemplateBean getSuddenHeatPoint() {
        List<Map<String, Object>> result = suddenHeatPoint(60, 2);
        return ResultTemplateBean.success(result);
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
                                SELECT 
                                    *,
                                    ROW_NUMBER() OVER (PARTITION BY simHash ORDER BY dataUpdateTime ASC) as rn
                                FROM read_parquet('%s')
                                WHERE simHash IS NOT NULL
                                  AND dataUpdateTime >= '%s'
                                  AND dataUpdateTime <= '%s'
                                  AND BIT_COUNT(xor(simHash, %d)) <= %d
                            ) t
                            WHERE rn = 1
                            ORDER BY dataUpdateTime DESC
                    """, simHash, pathPattern, startTime, endTime, simHash, simHashDistance);

            return duckdb.query(sql);
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
    public List<Map<String, Object>> suddenHeatPoint(int minutes, int minPlatforms) {
        try {
            LocalDateTime endTime = LocalDateTime.now();
            LocalDateTime startTime = endTime.minusMinutes(minutes);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String startStr = startTime.format(formatter);
            String endStr = endTime.format(formatter);

            String pathPattern = duckdb.buildPathPattern(startTime, endTime);

            String sql = String.format("""
                    WITH recent AS (
                        SELECT
                            title,
                            platformName,
                            dataUpdateTime,
                            simHash,
                            CAST(dataUpdateTime AS TIMESTAMP) AS update_ts
                        FROM read_parquet('%s')
                        WHERE simHash IS NOT NULL
                          AND dataUpdateTime >= '%s'
                          AND dataUpdateTime <= '%s'
                    ),
                    similar_pairs AS (
                        SELECT
                            a.simHash AS sim1,
                            b.simHash AS sim2,
                            BIT_COUNT(xor(a.simHash, b.simHash)) AS distance
                        FROM recent a
                        JOIN recent b
                          ON a.platformName < b.platformName
                         AND BIT_COUNT(xor(a.simHash, b.simHash)) <= 15
                    ),
                    representatives AS (
                        SELECT
                            simHash,
                            MIN(update_ts) AS first_time,
                            ARG_MIN(title, update_ts) AS representative_title
                        FROM recent
                        GROUP BY simHash
                    ),
                    clusters AS (
                        SELECT
                            r.simHash,
                            COUNT(DISTINCT r.platformName) AS platform_count,
                            MIN(r.update_ts) AS first_time,
                            MAX(r.update_ts) AS latest_appear_time,
                            ANY_VALUE(representative_title) AS representative_title
                        FROM recent r
                        JOIN similar_pairs s
                          ON r.simHash = s.sim1 OR r.simHash = s.sim2
                        JOIN representatives rep
                          ON r.simHash = rep.simHash
                        GROUP BY r.simHash
                    )
                    SELECT
                        representative_title,
                        platform_count,
                        first_time,
                        latest_appear_time
                    FROM clusters
                    WHERE platform_count >= %d
                    ORDER BY platform_count DESC, first_time ASC
                    LIMIT 50
                    """, pathPattern, startStr, endStr, minPlatforms);

            return duckdb.query(sql);

        } catch (Exception e) {
            log.error("突发热点查询异常: {}", e.getMessage(), e);
            throw new ServerException("突发热点查询异常");
        }
    }
}
