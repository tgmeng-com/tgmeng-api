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
import java.util.*;

/**
 * 有道翻译服务提供者
 * 免费额度：新用户赠50元体验金
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class YoudaoTranslationProvider implements TranslationProvider {

    private final TranslationApi translationApi;

    @Value("${translation.youdao.appkey:}")
    private String appKey;

    @Value("${translation.youdao.secret:}")
    private String secret;

    @Value("${translation.youdao.enabled:false}")
    private boolean enabled;

    @Value("${translation.youdao.priority:6}")
    private int priority;

    private static final Map<String, String> LANG_CODE_MAP = new HashMap<>();

    static {
        LANG_CODE_MAP.put("zh", "zh-CHS");
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

            String translated = translateSingle(text, "auto", targetLang);
            results.add(translated);
            Thread.sleep(100);
        }

        return results;
    }

    private String translateSingle(String text, String from, String to) throws Exception {
        String targetLangCode = LANG_CODE_MAP.getOrDefault(to, to);

        String salt = UUID.randomUUID().toString();
        String curtime = String.valueOf(System.currentTimeMillis() / 1000);
        String sign = generateSign(text, salt, curtime);

        log.debug("有道翻译请求: text={}", text);

        TranslationResponse response = translationApi.youdaoTranslate(
                text, from, targetLangCode, appKey, salt, sign, "v3", curtime
        );

        if (!response.isSuccess()) {
            String errorMsg = getErrorMessage(response.getErrorCode(), response.getErrorMessage());
            log.error("有道翻译失败: {}", errorMsg);
            throw new Exception(errorMsg);
        }

        String result = response.getTranslatedText();
        if (result == null || result.isEmpty()) {
            throw new Exception("有道翻译返回结果为空");
        }

        return result;
    }

    /**
     * 生成签名（v3版本）
     */
    private String generateSign(String query, String salt, String curtime) {
        try {
            String input = truncate(query);
            String signStr = appKey + input + salt + curtime + secret;

            MessageDigest md = MessageDigest.getInstance("SHA-256");
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

    /**
     * 截取查询字符串用于签名
     */
    private String truncate(String q) {
        if (q == null) {
            return null;
        }
        int len = q.length();
        return len <= 20 ? q : (q.substring(0, 10) + len + q.substring(len - 10, len));
    }

    private String getErrorMessage(String errorCode, String defaultMsg) {
        if (errorCode == null) return defaultMsg;

        Map<String, String> errorMessages = new HashMap<>();
        errorMessages.put("101", "缺少必填的参数");
        errorMessages.put("102", "不支持的语言类型");
        errorMessages.put("103", "翻译文本过长");
        errorMessages.put("104", "不支持的API类型");
        errorMessages.put("105", "不支持的签名类型");
        errorMessages.put("106", "不支持的响应类型");
        errorMessages.put("107", "不支持的传输加密类型");
        errorMessages.put("108", "appKey无效");
        errorMessages.put("109", "batchLog格式不正确");
        errorMessages.put("110", "无相关服务的有效实例");
        errorMessages.put("111", "开发者账号无效");
        errorMessages.put("401", "账户已经欠费");

        return errorMessages.getOrDefault(errorCode, defaultMsg);
    }

    @Override
    public String getProviderName() {
        return "YOUDAO";
    }

    @Override
    public boolean isAvailable() {
        return enabled && appKey != null && !appKey.isEmpty() &&
                secret != null && !secret.isEmpty();
    }

    @Override
    public int getPriority() {
        return priority;
    }
}