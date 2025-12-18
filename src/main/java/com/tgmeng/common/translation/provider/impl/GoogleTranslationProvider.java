package com.tgmeng.common.translation.provider.impl;

import com.tgmeng.common.translation.api.TranslationApi;
import com.tgmeng.common.translation.dto.TranslationResponse;
import com.tgmeng.common.translation.provider.TranslationProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Google翻译服务提供者
 * 免费额度：50万字符/月 + 新用户$300（90天）
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleTranslationProvider implements TranslationProvider {

    private final TranslationApi translationApi;

    @Value("${translation.google.apikey:}")
    private String apiKey;

    @Value("${translation.google.enabled:false}")
    private boolean enabled;

    @Value("${translation.google.priority:4}")
    private int priority;

    private static final Map<String, String> LANG_CODE_MAP = new HashMap<>();

    static {
        LANG_CODE_MAP.put("zh", "zh-CN");
        LANG_CODE_MAP.put("en", "en");
        LANG_CODE_MAP.put("ja", "ja");
        LANG_CODE_MAP.put("ko", "ko");
        LANG_CODE_MAP.put("fr", "fr");
        LANG_CODE_MAP.put("de", "de");
        LANG_CODE_MAP.put("es", "es");
        LANG_CODE_MAP.put("ru", "ru");
    }

    @Override
    public List<String> batchTranslate(List<String> sourceTexts, String targetLang) throws Exception {
        if (sourceTexts == null || sourceTexts.isEmpty()) {
            return new ArrayList<>();
        }

        String targetLangCode = LANG_CODE_MAP.getOrDefault(targetLang, targetLang);

        // Google支持真正的批量翻译
        TranslationApi.GoogleTranslateRequest request = TranslationApi.GoogleTranslateRequest.builder()
                .q(sourceTexts)
                .source("auto")
                .target(targetLangCode)
                .format("text")
                .build();

        log.debug("Google翻译请求: 数量={}", sourceTexts.size());

        TranslationResponse response = translationApi.googleTranslate(apiKey, request);

        if (!response.isSuccess()) {
            throw new Exception("Google翻译失败: " + response.getErrorMessage());
        }

        // 提取所有翻译结果
        List<String> results = new ArrayList<>();
        if (response.getData() != null && response.getData().getTranslations() != null) {
            for (TranslationResponse.GoogleTranslation translation : response.getData().getTranslations()) {
                results.add(translation.getTranslatedText());
            }
        }

        return results;
    }

    @Override
    public String getProviderName() {
        return "GOOGLE";
    }

    @Override
    public boolean isAvailable() {
        return enabled && apiKey != null && !apiKey.isEmpty();
    }

    @Override
    public int getPriority() {
        return priority;
    }
}