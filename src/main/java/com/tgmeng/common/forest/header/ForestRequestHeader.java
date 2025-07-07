package com.tgmeng.common.forest.header;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ForestRequestHeader {
    @JsonProperty("User-Agent")
    private String UserAgent;
    @JsonProperty("Accept-Encoding")
    private String AcceptEncoding;
    @JsonProperty("Accept-Language")
    private String AcceptLanguage;
    private String Accept;
    private String Connection;
    private String Referer;
    private String Origin;
    @JsonProperty("Cookie")
    private String Cookie;
}
