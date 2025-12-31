package com.tgmeng.model.dto.ai.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * ai突发热点的数据结构，这东西主要作用是验证ai返回的结果是否符合我们的数据结构
 */
@Data
@Accessors(chain = true)
public class AiChatModelResponseMessageContentForSuddenHeatpointDTO {
    private List<DataInfo> data;

    @Data
    public static class DataInfo {
        private String title;
        private Long count;
        //private List<HostPoints> hostPoints;
        private String summary;
        private String analyze;
        private String future;
    }

    @Data
    public static class HostPoints {
        private String title;
        private String url;
        private String platformName;
        private String dataUpdateTime;
    }
}