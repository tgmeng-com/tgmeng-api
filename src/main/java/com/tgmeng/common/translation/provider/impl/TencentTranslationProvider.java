package com.tgmeng.common.translation.provider.impl;

import com.tgmeng.common.translation.api.TranslationApi;
import com.tgmeng.common.translation.dto.TranslationRequest;
import com.tgmeng.common.translation.dto.TranslationResponse;
import com.tgmeng.common.translation.provider.TranslationProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *  腾讯翻译服务提供者
 *  免费额度：500万字符/月
 *  默认接口请求频率限制：5次/秒。
 */

/**
 * 腾讯翻译服务提供者（统一版本）
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TencentTranslationProvider implements TranslationProvider {

    private final TranslationApi translationApi;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${translation.tencent.secretid:}")
    private String secretId;

    @Value("${translation.tencent.secretkey:}")
    private String secretKey;

    @Value("${translation.tencent.region:ap-guangzhou}")
    private String region;

    @Value("${translation.tencent.enabled:true}")
    private boolean enabled;

    @Value("${translation.tencent.priority:2}")
    private int priority;

    private static final String SERVICE = "tmt";
    private static final String VERSION = "2018-03-21";
    private static final String ACTION = "TextTranslate";
    private static final String HOST = "tmt.tencentcloudapi.com";

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
            Thread.sleep(200);
        }

        return results;
    }

    private String translateSingle(String text, String to) throws Exception {
        String targetLangCode = LANG_CODE_MAP.getOrDefault(to, to);

        TranslationRequest request = TranslationRequest.builder()
                .sourceText(text)
                .source("auto")
                .target(targetLangCode)
                .projectId(0)
                .build();

        long timestamp = System.currentTimeMillis() / 1000;
        String authorization = generateAuthorization(request, timestamp);

        log.debug("腾讯翻译请求: text={}", text);

        // 使用统一API
        TranslationResponse response = translationApi.tencentTranslate(
                authorization, ACTION, VERSION,
                String.valueOf(timestamp), region, request
        );

        if (!response.isSuccess()) {
            String errorMsg = response.getErrorMessage();
            log.error("腾讯翻译失败: {}", errorMsg);
            throw new Exception("腾讯翻译失败: " + errorMsg);
        }

        String result = response.getTranslatedText();
        if (result == null || result.isEmpty()) {
            throw new Exception("腾讯翻译返回结果为空");
        }

        return result;
    }

    private String generateAuthorization(TranslationRequest request, long timestamp) throws Exception {
        String requestBody = objectMapper.writeValueAsString(request);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String date = sdf.format(new Date(timestamp * 1000));

        String httpRequestMethod = "POST";
        String canonicalUri = "/";
        String canonicalQueryString = "";
        String canonicalHeaders = "content-type:application/json\nhost:" + HOST + "\n";
        String signedHeaders = "content-type;host";
        String hashedRequestPayload = sha256Hex(requestBody);
        String canonicalRequest = httpRequestMethod + "\n" + canonicalUri + "\n" +
                canonicalQueryString + "\n" + canonicalHeaders + "\n" +
                signedHeaders + "\n" + hashedRequestPayload;

        String algorithm = "TC3-HMAC-SHA256";
        String credentialScope = date + "/" + SERVICE + "/tc3_request";
        String hashedCanonicalRequest = sha256Hex(canonicalRequest);
        String stringToSign = algorithm + "\n" + timestamp + "\n" +
                credentialScope + "\n" + hashedCanonicalRequest;

        byte[] secretDate = hmacSha256(("TC3" + secretKey).getBytes(StandardCharsets.UTF_8), date);
        byte[] secretService = hmacSha256(secretDate, SERVICE);
        byte[] secretSigning = hmacSha256(secretService, "tc3_request");
        String signature = bytesToHex(hmacSha256(secretSigning, stringToSign));

        return algorithm + " Credential=" + secretId + "/" + credentialScope +
                ", SignedHeaders=" + signedHeaders + ", Signature=" + signature;
    }

    private String sha256Hex(String s) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] d = md.digest(s.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(d);
    }

    private byte[] hmacSha256(byte[] key, String msg) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "HmacSHA256");
        mac.init(secretKeySpec);
        return mac.doFinal(msg.getBytes(StandardCharsets.UTF_8));
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    @Override
    public String getProviderName() {
        return "TENCENT";
    }

    @Override
    public boolean isAvailable() {
        return enabled && secretId != null && !secretId.isEmpty() &&
                secretKey != null && !secretKey.isEmpty();
    }

    @Override
    public int getPriority() {
        return priority;
    }
}