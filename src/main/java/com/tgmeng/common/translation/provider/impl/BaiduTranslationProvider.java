package com.tgmeng.common.translation.provider.impl;

import com.tgmeng.common.translation.api.TranslationApi;
import com.tgmeng.common.translation.dto.TranslationResponse;
import com.tgmeng.common.translation.provider.TranslationProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 百度翻译服务提供者（统一版本）
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BaiduTranslationProvider implements TranslationProvider {

    private final TranslationApi translationApi;

    @Value("${translation.baidu.appid:}")
    private String appId;

    @Value("${translation.baidu.secret:}")
    private String secret;

    @Value("${translation.baidu.enabled:true}")
    private boolean enabled;

    @Value("${translation.baidu.priority:3}")
    private int priority;

    private static final Map<String, String> LANG_CODE_MAP = new HashMap<>();

    static {
        LANG_CODE_MAP.put("zh", "zh");
        LANG_CODE_MAP.put("en", "en");
        LANG_CODE_MAP.put("ja", "jp");
        LANG_CODE_MAP.put("ko", "kor");
        LANG_CODE_MAP.put("fr", "fra");
        LANG_CODE_MAP.put("de", "de");
        LANG_CODE_MAP.put("es", "spa");
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

            String translated = translateSingle(text, "auto", targetLang);
            results.add(translated);
            Thread.sleep(100);
        }

        return results;
    }

    private String translateSingle(String text, String from, String to) throws Exception {
        String targetLangCode = LANG_CODE_MAP.getOrDefault(to, to);
        String salt = String.valueOf(System.currentTimeMillis());
        String sign = generateSign(text, salt);

        log.debug("百度翻译请求: text={}", text);

        // 使用统一API
        TranslationResponse response = translationApi.baiduTranslate(
                text, from, targetLangCode, appId, salt, sign
        );

        if (!response.isSuccess()) {
            String errorMsg = getErrorMessage(response.getErrorCode(), response.getErrorMessage());
            log.error("百度翻译失败: {}", errorMsg);
            throw new Exception(errorMsg);
        }

        String result = response.getTranslatedText();
        if (result == null) {
            throw new Exception("百度翻译返回结果为空");
        }

        return result;
    }

    private String generateSign(String query, String salt) {
        try {
            String signStr = appId + query + salt + secret;
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(signStr.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("生成签名失败", e);
        }
    }

    private String getErrorMessage(String errorCode, String defaultMsg) {
        if (errorCode == null) return defaultMsg;

        Map<String, String> errorMessages = new HashMap<>();
        errorMessages.put("52001", "请求超时");
        errorMessages.put("52002", "系统错误");
        errorMessages.put("52003", "未授权用户");
        errorMessages.put("54001", "签名错误");
        errorMessages.put("54003", "访问频率受限");
        errorMessages.put("54004", "账户余额不足");

        return errorMessages.getOrDefault(errorCode, defaultMsg);
    }

    @Override
    public String getProviderName() {
        return "BAIDU";
    }

    @Override
    public boolean isAvailable() {
        return enabled && appId != null && !appId.isEmpty() &&
                secret != null && !secret.isEmpty();
    }

    @Override
    public int getPriority() {
        return priority;
    }
}