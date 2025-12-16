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
    private String url;
    private String title;
    private String timestamp;
    private String platformName;
    private Long rank;
}
