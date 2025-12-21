package com.tgmeng.common.util;

import com.tgmeng.common.bean.HotPointDataParquetBean;
import com.tgmeng.common.parquet.HotPointDataParquetSchema;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;

import java.io.File;
import java.util.List;

@Slf4j
public class ParquetUtil {

    static {
        try {
            // 获取项目根目录
            String projectDir = System.getProperty("user.dir");
            File hadoopDir = new File(projectDir, ".hadoop");
            File binDir = new File(hadoopDir, "bin");
            // 创建目录
            binDir.mkdirs();
            // 设置 Hadoop Home
            System.setProperty("hadoop.home.dir", hadoopDir.getAbsolutePath());
            System.out.println("✅ Hadoop 目录: " + hadoopDir.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("⚠️ 初始化 Hadoop 环境失败: " + e.getMessage());
        }
    }

    private final Schema schema;
    private final Configuration conf;

    public ParquetUtil() {
        System.out.println("📝 开始初始化 HotPointDataParquetUtil");

        this.schema = HotPointDataParquetSchema.getSchema();
        System.out.println("📋 Schema: " + schema);

        this.conf = new Configuration();
        conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
        conf.setBoolean("dfs.permissions.enabled", false);
        conf.set("fs.permissions.umask-mode", "000");

        System.out.println("✅ HotPointDataParquetUtil 初始化完成");
    }

    // 写入本地
    public void writeParquet(List<HotPointDataParquetBean> records, String outputPath) throws Exception {
        writeParquetWithConf(records, outputPath, conf);
    }

    // 通用写入方法
    private void writeParquetWithConf(List<HotPointDataParquetBean> records,
                                      String outputPath,
                                      Configuration configuration) throws Exception {
        Path path = new Path(outputPath);

        try (ParquetWriter<GenericRecord> writer = AvroParquetWriter
                .<GenericRecord>builder(path)
                .withSchema(schema)
                .withConf(configuration)  // 使用传入的配置
                .withCompressionCodec(CompressionCodecName.ZSTD)
                .withDictionaryEncoding(true)
                .withRowGroupSize(128 * 1024 * 1024)
                .withPageSize(1024 * 1024)
                .build()) {

            for (HotPointDataParquetBean record : records) {
                GenericRecord avroRecord = convertToAvroRecord(record);
                writer.write(avroRecord);
            }

            System.out.println("✅ 成功写入 " + records.size() + " 条记录到 " + outputPath);
        }
    }

    private GenericRecord convertToAvroRecord(HotPointDataParquetBean record) {
        GenericRecord avroRecord = new GenericData.Record(schema);
        avroRecord.put("url", record.getUrl());
        avroRecord.put("title", record.getTitle());
        avroRecord.put("platformName", record.getPlatformName());
        avroRecord.put("platformCategory", record.getPlatformCategory());
        avroRecord.put("dataUpdateTime", record.getDataUpdateTime());
        avroRecord.put("simHash", record.getSimHash());
        return avroRecord;
    }
}