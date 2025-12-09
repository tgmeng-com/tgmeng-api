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
    @JsonProperty("Accept-Charset")
    private String AcceptCharset;
    private String Accept;
    private String Connection;
    private String Referer;
    private String Origin;
    @JsonProperty("Cookie")
    private String Cookie;
    @JsonProperty("X-Forwarded-For")
    private String XForwardedFor;
    private String priority;
    private String Host;

    @JsonProperty("Cache-Control")
    private String CacheControl;
    @JsonProperty("Sec-Ch-Ua")
    private String SecChUa;
    @JsonProperty("Sec-Ch-Ua-Mobile")
    private String SecChUaMobile;
    @JsonProperty("Sec-Ch-Ua-Platform")
    private String SecChUaPlatform;
    @JsonProperty("Sec-Fetch-Dest")
    private String SecFetchDest;
    @JsonProperty("Sec-Fetch-Mode")
    private String SecFetchMode;
    @JsonProperty("Sec-Fetch-Site")
    private String SecFetchSite;
    @JsonProperty("Sec-Fetch-User")
    private String SecFetchUser;
    @JsonProperty("Upgrade-Insecure-Requests")
    private String UpgradeInsecureRequests;
    private String Markdown;
    @JsonProperty("Content-Type")
    private String ContentType;

}
