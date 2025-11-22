package com.tgmeng.model.dto.ai.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class AiChatModelResponseContentTemplateDTO {
    private List<Result> result;
    private String time;
    private String aiPlatForm;
    private String aiModel;

    @Data
    public static class Result {

        private String categroy;
        private List<DataInfo> data;
    }

    @Data
    public static class DataInfo {
        private String title;
        private String content;

    }
}
