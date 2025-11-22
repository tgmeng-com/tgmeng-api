package com.tgmeng.model.dto.ai.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * description: GitHub热榜DTO
 * package: com.tgmeng.model.dto.topsearch
 * className: TopSearchGitHubDTO
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/6/30 2:37
 */
@Data
@Accessors(chain = true)
public class AICommonChatModelRequestDTO {

    private String model;

    private Boolean stream;

    private List<Input> input;

    private List<Input> messages;

    @JsonProperty("response_format")
    private ResponseFormat responseFormat;

    @Data
    public static class Input {
        private String role;
        private String content;
    }

    @Data
    public static class ResponseFormat {
        private String type;
    }
}
