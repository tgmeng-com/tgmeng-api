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
 * 小牛翻译服务提供者（统一版本）
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NiuTransTranslationProvider implements TranslationProvider {

    private final TranslationApi translationApi;

    @Value("${translation.niutrans.apikey:}")
    private String apiKey;

    @Value("${translation.niutrans.enabled:true}")
    private boolean enabled;

    @Value("${translation.niutrans.priority:1}")
    private int priority;

    private static final Map<String, String> LANG_CODE_MAP = new HashMap<>();

    static {
        LANG_CODE_MAP.put("zh", "zh");
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

        List<String> results = new ArrayList<>();

        for (String text : sourceTexts) {
            if (text == null || text.trim().isEmpty()) {
                results.add("");
                continue;
            }

            String translated = translateSingle(text, targetLang);
            results.add(translated);
            Thread.sleep(100);
        }

        return results;
    }

    private String translateSingle(String text, String to) throws Exception {
        String targetLangCode = LANG_CODE_MAP.getOrDefault(to, to);

        log.debug("小牛翻译请求: text={}", text);

        // 使用统一API
        TranslationResponse response = translationApi.niuTransTranslate(
                text, "auto", targetLangCode, apiKey
        );

        if (!response.isSuccess()) {
            String errorMsg = getErrorMessage(response.getErrorCode(), response.getErrorMessage());
            log.error("小牛翻译失败: {}", errorMsg);
            throw new Exception(errorMsg);
        }

        String result = response.getTranslatedText();
        if (result == null || result.isEmpty()) {
            throw new Exception("小牛翻译返回结果为空");
        }

        return result;
    }

    private String getErrorMessage(String errorCode, String defaultMsg) {
        if (errorCode == null) return defaultMsg;

        Map<String, String> errorMessages = new HashMap<>();
        errorMessages.put("10001", "API Key无效");
        errorMessages.put("10002", "请求次数超限");
        errorMessages.put("10003", "字符数超限");
        errorMessages.put("10004", "不支持的语言");
        errorMessages.put("10005", "文本为空");
        errorMessages.put("10006", "余额不足");

        return errorMessages.getOrDefault(errorCode, defaultMsg);
    }

    @Override
    public String getProviderName() {
        return "NIUTRANS";
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
