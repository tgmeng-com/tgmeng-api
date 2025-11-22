package com.tgmeng.model.dto.ai.response;

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
public class AICommonChatModelResponseDTO<
        K extends AICommonChatModelResponseDTO.Choice,
        V extends AICommonChatModelResponseDTO.Usage> {

    private String id;
    private String object;
    private Long created;
    private String model;
    private List<K> choices;
    private V usage;
    @JsonProperty("system_fingerprint")
    private String systemFingerprint;


    @Data
    public static class Choice {
        private Long index;
        private Message message;
        private Object logprobs;
        @JsonProperty("finish_reason")
        private String finishReason;

    }

    @Data
    public static class Message {
        private String role;
        private String content;
    }

    @Data
    public static class Usage {
        @JsonProperty("prompt_tokens")
        private Long promptTokens;
        @JsonProperty("completion_tokens")
        private Long completionTokens;
        @JsonProperty("total_tokens")
        private Long totalTokens;
        @JsonProperty("prompt_tokens_details")
        private PromptTokensDetails promptTokensDetails;
        @JsonProperty("prompt_cache_hit_tokens")
        private Long promptCacheHitTokens;
        @JsonProperty("prompt_cache_miss_tokens")
        private Long promptCacheMissTokens;
    }

    @Data
    public static class PromptTokensDetails {
        @JsonProperty("cached_tokens")
        private Long cachedTokens;
    }
}
