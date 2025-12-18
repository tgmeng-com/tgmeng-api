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
 * 微软翻译服务提供者
 * 免费额度：200万字符/月
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MicrosoftTranslationProvider implements TranslationProvider {

    private final TranslationApi translationApi;

    @Value("${translation.microsoft.subscriptionkey:}")
    private String subscriptionKey;

    @Value("${translation.microsoft.region:eastasia}")
    private String region;

    @Value("${translation.microsoft.enabled:false}")
    private boolean enabled;

    @Value("${translation.microsoft.priority:5}")
    private int priority;

    private static final Map<String, String> LANG_CODE_MAP = new HashMap<>();

    static {
        LANG_CODE_MAP.put("zh", "zh-Hans");
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

        // 构建请求体
        List<TranslationApi.MicrosoftTranslateRequest> requestList = new ArrayList<>();
        for (String text : sourceTexts) {
            requestList.add(new TranslationApi.MicrosoftTranslateRequest(text));
        }

        log.debug("微软翻译请求: 数量={}", sourceTexts.size());

        TranslationResponse response = translationApi.microsoftTranslate(
                targetLangCode, subscriptionKey, region, requestList
        );

        if (!response.isSuccess()) {
            throw new Exception("微软翻译失败: " + response.getErrorMessage());
        }

        // 提取翻译结果
        List<String> results = new ArrayList<>();
        if (response.getMicrosoftTranslations() != null) {
            for (TranslationResponse.MicrosoftTranslation translation : response.getMicrosoftTranslations()) {
                if (translation.getTranslations() != null && !translation.getTranslations().isEmpty()) {
                    results.add(translation.getTranslations().get(0).getText());
                }
            }
        }

        return results;
    }

    @Override
    public String getProviderName() {
        return "MICROSOFT";
    }

    @Override
    public boolean isAvailable() {
        return enabled && subscriptionKey != null && !subscriptionKey.isEmpty();
    }

    @Override
    public int getPriority() {
        return priority;
    }
}