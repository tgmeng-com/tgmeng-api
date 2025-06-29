package com.tgmeng.common.forest.header;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ForestRequestHeader {
    @JSONField(name = "User-Agent")
    private String UserAgent;
    @JSONField(name = "Accept-Encoding")
    private String AcceptEncoding;
    @JSONField(name = "Accept-Language")
    private String AcceptLanguage;
    private String Accept;
    private String Connection;
    private String Referer;
    private String Origin;
}
