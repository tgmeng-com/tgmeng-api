package com.tgmeng.common.enums.business;

import com.tgmeng.common.enums.enumcommon.INameValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * description: 运算符枚举
 * package: com.tgmeng.common.Enum
 * className: ForestRequestHeaderOriginEnum
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/6/29 18:17
*/
@Getter
@AllArgsConstructor
public enum AIModelEnum implements INameValueEnum<String,String> {
    GPT_35("GPT_35", "gpt-3.5-turbo", "", true,1),
    GPT_4("GPT_4", "gpt-4", "", true,2),
    DEEPSEEK_CHAT("DEEPSEEK_CHAT", "deepseek-chat", "DeepSeek非思考模式", true,3),
    DEEPSEEK_REASONER("DEEPSEEK_REASONER", "deepseek-reasoner", "DeepSeek思考模式", true,4),
    QWEN_32B("QWEN_32B", "Qwen/QwQ-32B", "", true,4);

    private final String key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
