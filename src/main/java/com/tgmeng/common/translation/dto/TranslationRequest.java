package com.tgmeng.common.translation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 翻译请求DTO（主要用于腾讯翻译）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TranslationRequest {

    /**
     * 要翻译的文本（腾讯翻译）
     */
    @JsonProperty("SourceText")
    private String sourceText;

    /**
     * 源语言（腾讯翻译）
     */
    @JsonProperty("Source")
    private String source;

    /**
     * 目标语言（腾讯翻译）
     */
    @JsonProperty("Target")
    private String target;

    /**
     * 项目ID（腾讯翻译）
     */
    @JsonProperty("ProjectId")
    private Integer projectId;
}