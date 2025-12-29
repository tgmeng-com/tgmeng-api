package com.tgmeng.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class HotPointDataParquetBean {
    // 原始数据
    private String url;
    private String title;
    private String platformName;
    private String platformCategory;
    private String platformCategoryRoot;
    private String dataUpdateTime;

    // 计算数据
    private Long simHash;
}
