package com.tgmeng.model.dto.ai.response;

import lombok.Data;
import lombok.experimental.Accessors;

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
public class AIDeepSeekChatModelResponseDTO extends AICommonChatModelResponseDTO<
        AIDeepSeekChatModelResponseDTO.Choice,
        AIDeepSeekChatModelResponseDTO.Usage> {

    @Data
    public static class Choice extends AICommonChatModelResponseDTO.Choice {
    }

    @Data
    public static class Usage extends AICommonChatModelResponseDTO.Usage {
    }
}
