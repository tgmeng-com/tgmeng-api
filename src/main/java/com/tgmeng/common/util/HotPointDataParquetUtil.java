package com.tgmeng.common.util;

import com.tgmeng.common.bean.HotPointDataParquetBean;
import com.tgmeng.service.cachesearch.ICacheSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotPointDataParquetUtil {
    @Value("${my-config.history.dir}")
    private String historyDataDir;

    private final ICacheSearchService cacheSearchService;

    public void saveToParquet() {
        try {
            String outputPath = getOutputPath();

            List<Map<String, Object>> hotList = cacheSearchService.getCacheSearchAllByWord(null, null).getData();
            ParquetUtil parquetUtil = new ParquetUtil();

            // 创建测试数据
            List<HotPointDataParquetBean> records = new ArrayList<>();
            for (Map<String, Object> hotPoint : hotList) {
                HotPointDataParquetBean record = new HotPointDataParquetBean(
                        hotPoint.get("keyword").toString(),
                        hotPoint.get("url").toString(),
                        hotPoint.get("dataUpdateTime").toString(),
                        hotPoint.get("dataCardName").toString(),
                        1L
                );
                records.add(record);
            }

            // 写入Parquet文件
            parquetUtil.writeParquet(records, outputPath);

            System.out.println("数据写入成功!");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getOutputPath() {
        int currentTimeYear = TimeUtil.getCurrentTimeYear();
        int currentTimeMonth = TimeUtil.getCurrentTimeMonth();
        int currentTimeDay = TimeUtil.getCurrentTimeDay();
        int currentTimeHour = TimeUtil.getCurrentTimeHour();
        int currentTimeMinute = TimeUtil.getCurrentTimeMinute();
        return String.format("%s/%d/%02d/%02d/%02d_%02d_%02d_%02d_%02d_hot_data.parquet", historyDataDir, currentTimeYear, currentTimeMonth, currentTimeDay, currentTimeYear, currentTimeMonth, currentTimeDay, currentTimeHour, currentTimeMinute);
    }
}
