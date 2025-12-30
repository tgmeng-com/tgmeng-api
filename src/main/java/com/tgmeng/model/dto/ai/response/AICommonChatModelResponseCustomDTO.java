package com.tgmeng.model.dto.ai.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * description: ai模型返回结果后提取自己需要的信息
 * package: com.tgmeng.model.dto.topsearch
 * className: TopSearchGitHubDTO
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/6/30 2:37
 */
@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AICommonChatModelResponseCustomDTO {
    private String messageContent;
    private String time;
    private Long usedTime;
    private String platform;
    private String model;
    private String from;
    private Long totalTokens;
}
