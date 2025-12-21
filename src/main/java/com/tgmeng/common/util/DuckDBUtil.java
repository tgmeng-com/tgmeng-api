package com.tgmeng.common.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * DuckDB 工具类 - 极简版
 */
@Data
@Slf4j
@Service
public class DuckDBUtil implements AutoCloseable {

    private Connection connection;
    private final String dataPath;

    public DuckDBUtil(@Value("${my-config.history.dir:data/defaultPath}") String dataPath) {
        this.dataPath = dataPath;
        try {
            Class.forName("org.duckdb.DuckDBDriver");
            this.connection = DriverManager.getConnection("jdbc:duckdb:");
            System.out.println("✅ DuckDB 连接成功: " + dataPath);
        } catch (Exception e) {
            throw new RuntimeException("❌ DuckDB 初始化失败", e);
        }
    }

    /**
     * 执行查询
     * @param sql SQL语句，例如：SELECT * FROM read_parquet('path/*.parquet') WHERE id = ?
     * @param params 参数
     */
    public List<Map<String, Object>> query(String sql, Object... params) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            ResultSet rs = stmt.executeQuery();
            List<Map<String, Object>> list = new ArrayList<>();
            ResultSetMetaData meta = rs.getMetaData();
            int count = meta.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= count; i++) {
                    row.put(meta.getColumnName(i), rs.getObject(i));
                }
                list.add(row);
            }
            return list;
        }
    }

    // 根据传入的时间范围，得到需要扫描的文件范围
    public String buildPathPattern(LocalDateTime start, LocalDateTime end) {
        int startHour = start.getHour();
        int startDay = start.getDayOfMonth();
        int startMonth = start.getMonthValue();
        int startYear = start.getYear();

        int endHour = end.getHour();
        int endDay = end.getDayOfMonth();
        int endMonth = end.getMonthValue();
        int endYear = end.getYear();

        // 同一小时
        if (startYear == endYear && startMonth == endMonth && startDay == endDay && startHour == endHour) {
            return StringUtil.replaceForFilePath(
                    String.format("%s/%d/%02d/%02d/%02d/*.parquet", dataPath, startYear, startMonth, startDay, startHour)
            );
        }

        // 如果是同一天
        if (startYear == endYear && startMonth == endMonth && startDay == endDay) {
            return StringUtil.replaceForFilePath(
                    String.format("%s/%d/%02d/%02d/**/*.parquet", dataPath, startYear, startMonth, startDay)
            );
        }

        // 如果是同一月
        if (startYear == endYear && startMonth == endMonth) {
            return StringUtil.replaceForFilePath(
                    String.format("%s/%d/%02d/**/*.parquet", dataPath, startYear, startMonth)
            );
        }

        // 如果是同一年
        if (startYear == endYear ) {
            return StringUtil.replaceForFilePath(
                    String.format("%s/%d/**/*.parquet", dataPath, startYear)
            );
        }

        // 跨月或跨年，扫描所有
        return StringUtil.replaceForFilePath(
                String.format("%s/**/*.parquet", dataPath)
        );
    }

    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}