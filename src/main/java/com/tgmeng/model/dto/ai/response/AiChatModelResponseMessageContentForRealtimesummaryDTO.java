package com.tgmeng.model.dto.ai.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * ai实时简报的数据结构，这东西主要作用是验证ai返回的结果是否符合我们的数据结构
 */
@Data
@Accessors(chain = true)
public class AiChatModelResponseMessageContentForRealtimesummaryDTO {
    private List<DataInfo> summary;
    private List<DataInfo> analyze;
    private List<DataInfo> future;

    @Data
    public static class DataInfo {
        private String title;
        private String content;
    }
}
