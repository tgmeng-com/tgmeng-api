package com.tgmeng.common.translation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 统一的翻译响应DTO
 * 适配所有翻译平台的响应格式
 */
@Data
public class TranslationResponse {

    // ==================== 百度翻译字段 ====================
    @JsonProperty("from")
    private String from;

    @JsonProperty("to")
    private String to;

    @JsonProperty("trans_result")
    private List<BaiduTransResult> transResult;

    @JsonProperty("error_code")
    private String errorCode;

    @JsonProperty("error_msg")
    private String errorMsg;

    // ==================== 小牛翻译字段 ====================
    @JsonProperty("tgt_text")
    private String tgtText;

    @JsonProperty("apiType")
    private String apiType;

    // ==================== 腾讯翻译字段 ====================
    @JsonProperty("Response")
    private TencentResponse response;

    // ==================== Google翻译字段 ====================
    @JsonProperty("data")
    private GoogleData data;

    // ==================== 有道翻译字段 ====================
    @JsonProperty("translation")
    private List<String> translation;

    // ==================== 微软翻译字段 ====================
    // 微软返回数组，需要特殊处理
    private List<MicrosoftTranslation> microsoftTranslations;

    // ==================== DeepL翻译字段 ====================
    @JsonProperty("translations")
    private List<DeepLTranslation> translations;

    // ==================== 阿里翻译字段 ====================
    @JsonProperty("Data")
    private AliData aliData;

    @JsonProperty("Code")
    private String code;

    @JsonProperty("Message")
    private String message;

    // ==================== 百度翻译结果 ====================
    @Data
    public static class BaiduTransResult {
        @JsonProperty("src")
        private String src;

        @JsonProperty("dst")
        private String dst;
    }

    // ==================== 腾讯翻译响应 ====================
    @Data
    public static class TencentResponse {
        @JsonProperty("TargetText")
        private String targetText;

        @JsonProperty("Source")
        private String source;

        @JsonProperty("Target")
        private String target;

        @JsonProperty("RequestId")
        private String requestId;

        @JsonProperty("Error")
        private TencentError error;
    }

    @Data
    public static class TencentError {
        @JsonProperty("Code")
        private String code;

        @JsonProperty("Message")
        private String message;
    }

    // ==================== Google翻译响应 ====================
    @Data
    public static class GoogleData {
        @JsonProperty("translations")
        private List<GoogleTranslation> translations;
    }

    @Data
    public static class GoogleTranslation {
        @JsonProperty("translatedText")
        private String translatedText;

        @JsonProperty("detectedSourceLanguage")
        private String detectedSourceLanguage;
    }

    // ==================== 微软翻译响应 ====================
    @Data
    public static class MicrosoftTranslation {
        @JsonProperty("translations")
        private List<MicrosoftTranslationItem> translations;
    }

    @Data
    public static class MicrosoftTranslationItem {
        @JsonProperty("text")
        private String text;

        @JsonProperty("to")
        private String to;
    }

    // ==================== DeepL翻译响应 ====================
    @Data
    public static class DeepLTranslation {
        @JsonProperty("detected_source_language")
        private String detectedSourceLanguage;

        @JsonProperty("text")
        private String text;
    }

    // ==================== 阿里翻译响应 ====================
    @Data
    public static class AliData {
        @JsonProperty("Translated")
        private String translated;

        @JsonProperty("WordCount")
        private String wordCount;
    }

    // ==================== 通用方法 ====================

    /**
     * 判断是否成功（适配所有平台）
     */
    public boolean isSuccess() {
        // 百度翻译
        if (transResult != null && !transResult.isEmpty()) {
            return errorCode == null || "52000".equals(errorCode);
        }

        // 小牛翻译
        if (tgtText != null) {
            return "0".equals(errorCode) || errorCode == null;
        }

        // 腾讯翻译
        if (response != null) {
            return response.getError() == null;
        }

        // Google翻译
        if (data != null && data.getTranslations() != null) {
            return !data.getTranslations().isEmpty();
        }

        // 有道翻译
        if (translation != null && !translation.isEmpty()) {
            return errorCode == null || "0".equals(errorCode);
        }

        // 微软翻译
        if (microsoftTranslations != null && !microsoftTranslations.isEmpty()) {
            return true;
        }

        // DeepL翻译
        if (translations != null && !translations.isEmpty()) {
            return true;
        }

        // 阿里翻译
        if (aliData != null && aliData.getTranslated() != null) {
            return "200".equals(code);
        }

        return false;
    }

    /**
     * 获取翻译结果（适配所有平台）
     */
    public String getTranslatedText() {
        // 百度翻译
        if (transResult != null && !transResult.isEmpty()) {
            return transResult.get(0).getDst();
        }

        // 小牛翻译
        if (tgtText != null) {
            return tgtText;
        }

        // 腾讯翻译
        if (response != null && response.getTargetText() != null) {
            return response.getTargetText();
        }

        // Google翻译
        if (data != null && data.getTranslations() != null && !data.getTranslations().isEmpty()) {
            return data.getTranslations().get(0).getTranslatedText();
        }

        // 有道翻译
        if (translation != null && !translation.isEmpty()) {
            return translation.get(0);
        }

        // 微软翻译
        if (microsoftTranslations != null && !microsoftTranslations.isEmpty()) {
            List<MicrosoftTranslationItem> items = microsoftTranslations.get(0).getTranslations();
            if (items != null && !items.isEmpty()) {
                return items.get(0).getText();
            }
        }

        // DeepL翻译
        if (translations != null && !translations.isEmpty()) {
            return translations.get(0).getText();
        }

        // 阿里翻译
        if (aliData != null && aliData.getTranslated() != null) {
            return aliData.getTranslated();
        }

        return null;
    }

    /**
     * 获取错误信息（适配所有平台）
     */
    public String getErrorMessage() {
        // 百度/小牛/有道翻译
        if (errorMsg != null) {
            return errorMsg;
        }

        // 腾讯翻译
        if (response != null && response.getError() != null) {
            return response.getError().getMessage();
        }

        // 阿里翻译
        if (message != null) {
            return message;
        }

        return "未知错误";
    }

    /**
     * 获取错误码（适配所有平台）
     */
    public String getErrorCode() {
        // 百度/小牛翻译
        if (errorCode != null) {
            return errorCode;
        }

        // 腾讯翻译
        if (response != null && response.getError() != null) {
            return response.getError().getCode();
        }

        return null;
    }
}