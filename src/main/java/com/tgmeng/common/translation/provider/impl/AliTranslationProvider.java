package com.tgmeng.common.translation.provider.impl;

import com.tgmeng.common.translation.api.TranslationApi;
import com.tgmeng.common.translation.dto.TranslationResponse;
import com.tgmeng.common.translation.provider.TranslationProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 阿里翻译服务提供者
 * 免费额度：100万字符/月（新用户试用）
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliTranslationProvider implements TranslationProvider {

    private final TranslationApi translationApi;

    @Value("${translation.ali.accesskeyid:}")
    private String accessKeyId;

    @Value("${translation.ali.accesskeysecret:}")
    private String accessKeySecret;

    @Value("${translation.ali.enabled:false}")
    private boolean enabled;

    @Value("${translation.ali.priority:8}")
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

            String translated = translateSingle(text, "auto", targetLang);
            results.add(translated);
            Thread.sleep(100);
        }

        return results;
    }

    private String translateSingle(String text, String from, String to) throws Exception {
        String targetLangCode = LANG_CODE_MAP.getOrDefault(to, to);

        // 生成时间戳和签名
        String timestamp = generateTimestamp();
        String signatureNonce = UUID.randomUUID().toString();

        // 构建参数Map
        Map<String, String> params = new TreeMap<>();
        params.put("Action", "TranslateGeneral");
        params.put("Format", "JSON");
        params.put("Version", "2018-10-12");
        params.put("AccessKeyId", accessKeyId);
        params.put("SignatureMethod", "HMAC-SHA1");
        params.put("Timestamp", timestamp);
        params.put("SignatureVersion", "1.0");
        params.put("SignatureNonce", signatureNonce);
        params.put("SourceLanguage", from);
        params.put("TargetLanguage", targetLangCode);
        params.put("SourceText", text);
        params.put("FormatType", "text");
        params.put("Scene", "general");

        // 生成签名
        String signature = generateSignature(params);

        log.debug("阿里翻译请求: text={}", text);

        TranslationResponse response = translationApi.aliTranslate(
                "TranslateGeneral",
                "JSON",
                "2018-10-12",
                accessKeyId,
                signature,
                "HMAC-SHA1",
                timestamp,
                "1.0",
                signatureNonce,
                from,
                targetLangCode,
                text,
                "text",
                "general"
        );

        if (!response.isSuccess()) {
            throw new Exception("阿里翻译失败: " + response.getErrorMessage());
        }

        String result = response.getTranslatedText();
        if (result == null || result.isEmpty()) {
            throw new Exception("阿里翻译返回结果为空");
        }

        return result;
    }

    /**
     * 生成时间戳
     */
    private String generateTimestamp() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(new Date());
    }

    /**
     * 生成签名
     */
    private String generateSignature(Map<String, String> params) throws Exception {
        // 1. 构建规范化请求字符串
        StringBuilder canonicalizedQueryString = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (canonicalizedQueryString.length() > 0) {
                canonicalizedQueryString.append("&");
            }
            canonicalizedQueryString.append(percentEncode(entry.getKey()))
                    .append("=")
                    .append(percentEncode(entry.getValue()));
        }

        // 2. 构建待签名字符串
        String stringToSign = "POST&" +
                percentEncode("/") + "&" +
                percentEncode(canonicalizedQueryString.toString());

        // 3. 计算签名
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(
                (accessKeySecret + "&").getBytes(StandardCharsets.UTF_8),
                "HmacSHA1"
        ));
        byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(signData);
    }

    /**
     * URL编码
     */
    private String percentEncode(String value) throws Exception {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.name())
                .replace("+", "%20")
                .replace("*", "%2A")
                .replace("%7E", "~");
    }

    @Override
    public String getProviderName() {
        return "ALI";
    }

    @Override
    public boolean isAvailable() {
        return enabled &&
                accessKeyId != null && !accessKeyId.isEmpty() &&
                accessKeySecret != null && !accessKeySecret.isEmpty();
    }

    @Override
    public int getPriority() {
        return priority;
    }
}