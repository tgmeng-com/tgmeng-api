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
public enum AIPlatFormEnum implements INameValueEnum<String,String> {
    OPENAI("OPENAI", "https://api.openai.com/v1/chat/completions", "open ai", true,1),
    GUI_JI_LIU_DONG("GUI_JI_LIU_DONG", "https://api.siliconflow.cn/v1/chat/completions", "硅基流动", true,1),
    DEEPSEEK("DEEPSEEK", "https://api.deepseek.com/chat/completions", "", true,2),
    GROK("GROK", "https://api.grok.cn/v1/chat/completions", "", true,3),
    CLAUDE("CLAUDE", "https://api.claude.cn/v1/chat/completions", "", true,4),
    MISTRAL("MISTRAL", "", "", true,4),
    LLAMA("LLAMA", "", "", true,4),
    ERNIE("ERNIE", "", "百度文心一言", true,4),
    HUN_YUAN("HUN_YUAN", "", "腾讯混元", true,4),
    DOU_BAO("DOU_BAO", "", "字节豆包", true,4),
    QIAN_WEN("QIANWEN", "", "阿里通义千问", true,4),
    PANGU("CLAUDE", "", "华为盘古", true,4),
    GEMINI("GEMINI", "", "Google", true,5);

    private final String key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
