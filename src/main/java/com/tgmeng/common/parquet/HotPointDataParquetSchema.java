package com.tgmeng.common.parquet;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;

public class HotPointDataParquetSchema {
    public static Schema getSchema() {
        return SchemaBuilder.record("HotPointDataParquetBean")
                .namespace("com.tgmeng.hotdata")
                .fields()
                .requiredString("url")
                .requiredString("title")
                .requiredString("platformName")
                .requiredString("platformCategory")
                .requiredString("dataUpdateTime")
                .requiredLong("simHash")
                .endRecord();
    }
}