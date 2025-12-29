package com.tgmeng.common.util;

import com.tgmeng.common.bean.HotPointDataParquetBean;
import com.tgmeng.common.enums.business.SearchModeEnum;
import com.tgmeng.service.cachesearch.ICacheSearchService;
import com.tgmeng.service.history.ITopSearchHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;


@Slf4j
@Service
@RequiredArgsConstructor
public class HotPointDataParquetUtil {
    @Value("${my-config.history.dir}")
    private String historyDataDir;

    @Value("${my-config.history.keep-day}")
    private Integer historyDataKeepDay;

    private final ICacheSearchService cacheSearchService;
    private final ITopSearchHistoryService topSearchHistoryService;

    public void saveToParquet() {
        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("word", null);
            paramMap.put("searchMode", SearchModeEnum.MO_HU_PI_PEI_ONE_MINUTES.getValue());
            List<Map<String, Object>> hotList = cacheSearchService.searchByWord(paramMap).getData();
            ParquetUtil parquetUtil = new ParquetUtil();

            List<HotPointDataParquetBean> records = new ArrayList<>();
            for (Map<String, Object> hotPoint : hotList) {
                HotPointDataParquetBean record = new HotPointDataParquetBean(
                        hotPoint.get("url").toString(),
                        hotPoint.get("title").toString(),
                        hotPoint.get("platformName").toString(),
                        hotPoint.get("platformCategory").toString(),
                        hotPoint.get("platformCategoryRoot").toString(),
                        hotPoint.get("dataUpdateTime").toString(),
                        SimHashUtil.calculateSimHash(hotPoint.get("title").toString()));
                records.add(record);
            }

            // 写入Parquet文件
            String outputPath = getOutputPath();
            parquetUtil.writeParquet(records, outputPath);

            System.out.println("数据写入成功!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getOutputPath() {
        int currentTimeYear = TimeUtil.getCurrentTimeYear();
        int currentTimeMonth = TimeUtil.getCurrentTimeMonth();
        int currentTimeDay = TimeUtil.getCurrentTimeDay();
        int currentTimeHour = TimeUtil.getCurrentTimeHour();
        int currentTimeMinute = TimeUtil.getCurrentTimeMinute();
        return String.format("%s/%d/%02d/%02d/%02d/%02d.parquet", historyDataDir, currentTimeYear, currentTimeMonth, currentTimeDay, currentTimeHour, currentTimeMinute);
    }

    // 合并昨天的数据到一个parquet中
    public void mergeYesterdaySchedule() {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("password", System.getenv("ADMIN_PASSWORD"));
        paramMap.put("sourceDir", historyDataDir + TimeUtil.getYesterdayDateString("yyyy/MM/dd"));
        paramMap.put("targetFile", TimeUtil.getYesterdayDateString() + ".parquet");
        topSearchHistoryService.mergeParquetByGlob(paramMap);
    }

    // 删除数天前的历史数据
    public void cleanForParquet() {
        LocalDate cutoffDate = LocalDate.now().minusDays(historyDataKeepDay);
        log.info("⚠️ 开始删除 {} 天前的历史热点数据文件，截止日期: {}", historyDataKeepDay, cutoffDate);

        try (Stream<Path> paths = Files.walk(Paths.get(historyDataDir))) {
            // 收集所有目录，从深到浅排序（先删除子目录）
            List<Path> directories = paths
                    .filter(Files::isDirectory)
                    .filter(p -> !p.equals(Paths.get(historyDataDir))) // 排除根目录
                    .sorted(Comparator.reverseOrder()) // 从深到浅
                    .toList();

            int deletedCount = 0;

            for (Path dirPath : directories) {
                try {
                    String pathStr = dirPath.toString();
                    String[] parts = pathStr.replace(historyDataDir, "")
                            .replace("\\", "/")
                            .split("/");

                    // 查找年/月/日模式
                    if (parts.length >= 3) {
                        String year = null, month = null, day = null;

                        // 从后往前找日期部分
                        for (int i = parts.length - 1; i >= 2; i--) {
                            if (parts[i].matches("\\d{2}") &&
                                    parts[i - 1].matches("\\d{2}") &&
                                    parts[i - 2].matches("\\d{4}")) {
                                year = parts[i - 2];
                                month = parts[i - 1];
                                day = parts[i];
                                break;
                            }
                        }
                        if (year != null && month != null && day != null) {
                            LocalDate folderDate = LocalDate.of(
                                    Integer.parseInt(year),
                                    Integer.parseInt(month),
                                    Integer.parseInt(day)
                            );

                            // 如果文件夹日期早于截止日期
                            if (folderDate.isBefore(cutoffDate)) {
                                File folder = dirPath.toFile();
                                if (FileUtil.deleteFolder(folder)) {
                                    deletedCount++;
                                    log.info("✅ 删除: {}", pathStr);
                                } else {
                                    log.info("❌ 删除失败: {}", pathStr);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("❌ 处理目录失败: {},{}", dirPath, e.getMessage());
                }
            }
            cleanEmptyDirectories();
            System.out.println("✅ 清理完成，共删除 " + deletedCount + " 个目录");

        } catch (IOException e) {
            log.error("❌ 清理失败: {}", e.getMessage());
        }
    }

    // 清理空文件夹
    private void cleanEmptyDirectories() {
        try (Stream<Path> paths = Files.walk(Paths.get(historyDataDir))) {
            List<Path> directories = paths
                    .filter(Files::isDirectory)
                    .filter(p -> !p.equals(Paths.get(historyDataDir))) // 排除根目录
                    .sorted(Comparator.reverseOrder()) // 从深到浅
                    .toList();

            for (Path dirPath : directories) {
                File dir = dirPath.toFile();
                File[] files = dir.listFiles();

                // 如果目录为空，删除它
                if (files != null && files.length == 0) {
                    if (dir.delete()) {
                        log.info("  ✓ 删除空目录: {}", dirPath.toString().replace(historyDataDir, ""));
                    }
                }
            }
        } catch (IOException e) {
            log.error("清理空目录失败：{}", e.getMessage());
        }
    }
}
