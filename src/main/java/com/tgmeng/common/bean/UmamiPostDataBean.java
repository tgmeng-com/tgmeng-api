package com.tgmeng.common.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UmamiPostDataBean {
    private Payload payload;
    private String type = "event";

    @Data
    public static class Payload {
        private String hostname = "tgmeng.com";
        private String language = "zh-CN";
        private String referrer = "tgmeng.com";
        private String screen = "1920x1080";
        private String title = "订阅推送";
        private String url = "tgmeng.com/webhook";
        private String website;
        private String name;
        private DataInfo data;
    }

    @Data
    public static class DataInfo {
        private Integer value;
        private Long timestamp = System.currentTimeMillis() / 1000;
    }
}