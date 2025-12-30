package com.tgmeng.model.dto.ai.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class AiChatModelResponseContentTemplateDTO {
    private Result result;
    private String time;
    private Long usedTime; // 秒
    private String platform;
    private String model;
    private String from;
    private Long totalTokens;

    @Data
    public static class Result {
        private List<DataInfo> summary;
        private List<DataInfo> analyze;
        private List<DataInfo> future;
    }

    @Data
    public static class DataInfo {
        private String title;
        private String content;

    }
}
