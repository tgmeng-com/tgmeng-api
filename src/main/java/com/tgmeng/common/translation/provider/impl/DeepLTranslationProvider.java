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
 * DeepL翻译服务提供者
 * 免费额度：50万字符/月（API Free版本）
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DeepLTranslationProvider implements TranslationProvider {

    private final TranslationApi translationApi;

    @Value("${translation.deepl.authkey:}")
    private String authKey;

    @Value("${translation.deepl.enabled:false}")
    private boolean enabled;

    @Value("${translation.deepl.priority:7}")
    private int priority;

    private static final Map<String, String> LANG_CODE_MAP = new HashMap<>();

    static {
        LANG_CODE_MAP.put("zh", "ZH");
        LANG_CODE_MAP.put("en", "EN");
        LANG_CODE_MAP.put("ja", "JA");
        LANG_CODE_MAP.put("ko", "KO");
        LANG_CODE_MAP.put("fr", "FR");
        LANG_CODE_MAP.put("de", "DE");
        LANG_CODE_MAP.put("es", "ES");
        LANG_CODE_MAP.put("ru", "RU");
    }

    @Override
    public List<String> batchTranslate(List<String> sourceTexts, String targetLang) throws Exception {
        if (sourceTexts == null || sourceTexts.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> results = new ArrayList<>();
        String targetLangCode = LANG_CODE_MAP.getOrDefault(targetLang, targetLang.toUpperCase());

        // DeepL免费版不支持批量，需要逐个翻译
        for (String text : sourceTexts) {
            if (text == null || text.trim().isEmpty()) {
                results.add("");
                continue;
            }

            String translated = translateSingle(text, "auto", targetLangCode);
            results.add(translated);
            Thread.sleep(100);
        }

        return results;
    }

    private String translateSingle(String text, String from, String to) throws Exception {
        log.debug("DeepL翻译请求: text={}", text);

        TranslationResponse response = translationApi.deeplTranslate(
                authKey, text, null, to
        );

        if (!response.isSuccess()) {
            throw new Exception("DeepL翻译失败: " + response.getErrorMessage());
        }

        String result = response.getTranslatedText();
        if (result == null || result.isEmpty()) {
            throw new Exception("DeepL翻译返回结果为空");
        }

        return result;
    }

    @Override
    public String getProviderName() {
        return "DEEPL";
    }

    @Override
    public boolean isAvailable() {
        return enabled && authKey != null && !authKey.isEmpty();
    }

    @Override
    public int getPriority() {
        return priority;
    }
}