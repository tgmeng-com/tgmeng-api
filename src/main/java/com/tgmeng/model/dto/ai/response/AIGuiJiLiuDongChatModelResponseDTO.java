package com.tgmeng.model.dto.ai.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * description: 硅基流动
 * package: com.tgmeng.model.dto.topsearch
 * className: TopSearchGitHubDTO
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/6/30 2:37
 */
@Data
@Accessors(chain = true)
public class AIGuiJiLiuDongChatModelResponseDTO extends AICommonChatModelResponseDTO<
        AIGuiJiLiuDongChatModelResponseDTO.Choice,
        AIGuiJiLiuDongChatModelResponseDTO.Usage> {

    @Data
    public static class Choice extends AICommonChatModelResponseDTO.Choice {
        private Message message;
    }

    @Data
    public static class Message extends AICommonChatModelResponseDTO.Message {
        @JsonProperty("reasoning_content")
        private String reasoningContent;
    }

    @Data
    public static class Usage extends AICommonChatModelResponseDTO.Usage {
        @JsonProperty("completion_tokens_details")
        private CompletionTokensDetails completionTokensDetails;
    }

    @Data
    public static class CompletionTokensDetails {
        @JsonProperty("reasoning_tokens")
        private Long reasoningTokens;
    }
}
