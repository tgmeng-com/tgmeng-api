package com.tgmeng.model.dto.ai.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * description: ai模型返回结果后提取自己需要的信息，也就是到时候要给到前端的数据结构
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
    // 这个是ai直接返回的结构里的message，我们拿到后，用对象转一下，看结构是否符合，然后把转换后的对象设置到下面的jsonObjectMessageContent中去，然后再把这个字段清空，保证给前端传的少一点
    private String messageContent;
    private String time;
    private Long usedTime;
    private String platform;
    private String model;
    private String from;
    private Long totalTokens;
    // 这个是将messageContent转成对象之后设置进来，也是为了校验ai生成的东西符不符合我们的数据结构
    private Object data;
}
