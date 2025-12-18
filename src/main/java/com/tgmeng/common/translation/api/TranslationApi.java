package com.tgmeng.common.translation.api;

import com.dtflys.forest.annotation.*;
import com.tgmeng.common.translation.dto.TranslationRequest;
import com.tgmeng.common.translation.dto.TranslationResponse;

/**
 * 统一的翻译API接口
 * 通过不同的方法名来区分不同平台
 */
public interface TranslationApi {

    // ==================== 百度翻译 ====================

    /**
     * 百度翻译API
     */
    @Get(url = "https://fanyi-api.baidu.com/api/trans/vip/translate")
    TranslationResponse baiduTranslate(
            @Query("q") String query,
            @Query("from") String from,
            @Query("to") String to,
            @Query("appid") String appid,
            @Query("salt") String salt,
            @Query("sign") String sign
    );

    // ==================== 小牛翻译 ====================

    /**
     * 小牛翻译API
     */
    @Get(url = "https://api.niutrans.com/NiuTransServer/translation")
    TranslationResponse niuTransTranslate(
            @Query("src_text") String srcText,
            @Query("from") String from,
            @Query("to") String to,
            @Query("apikey") String apikey
    );

    // ==================== 腾讯翻译 ====================

    /**
     * 腾讯翻译API
     */
    @Post(url = "https://tmt.tencentcloudapi.com")
    //@ContentType("application/json")
    TranslationResponse tencentTranslate(
            @Header("Authorization") String authorization,
            @Header("X-TC-Action") String action,
            @Header("X-TC-Version") String version,
            @Header("X-TC-Timestamp") String timestamp,
            @Header("X-TC-Region") String region,
            @JSONBody TranslationRequest request
    );

    // ==================== 有道翻译 ====================

    /**
     * 有道翻译API
     */
    @Get(url = "https://openapi.youdao.com/api")
    TranslationResponse youdaoTranslate(
            @Query("q") String query,
            @Query("from") String from,
            @Query("to") String to,
            @Query("appKey") String appKey,
            @Query("salt") String salt,
            @Query("sign") String sign,
            @Query("signType") String signType,
            @Query("curtime") String curtime
    );

    // ==================== Google翻译 ====================

    /**
     * Google翻译API
     */
    @Post(url = "https://translation.googleapis.com/language/translate/v2")
    //@ContentType("application/json")
    TranslationResponse googleTranslate(
            @Query("key") String apiKey,
            @JSONBody GoogleTranslateRequest request
    );

    // ==================== 微软翻译 ====================

    /**
     * 微软翻译API
     */
    @Post(url = "https://api.cognitive.microsofttranslator.com/translate?api-version=3.0")
    //@ContentType("application/json")
    TranslationResponse microsoftTranslate(
            @Query("to") String to,
            @Header("Ocp-Apim-Subscription-Key") String subscriptionKey,
            @Header("Ocp-Apim-Subscription-Region") String region,
            @JSONBody java.util.List<MicrosoftTranslateRequest> request
    );

    // ==================== DeepL翻译 ====================

    /**
     * DeepL翻译API
     */
    @Post(url = "https://api-free.deepl.com/v2/translate")
    //@ContentType("application/x-www-form-urlencoded")
    TranslationResponse deeplTranslate(
            @Body("auth_key") String authKey,
            @Body("text") String text,
            @Body("source_lang") String sourceLang,
            @Body("target_lang") String targetLang
    );

    // ==================== 阿里翻译 ====================

    /**
     * 阿里翻译API
     */
    @Post(url = "https://mt.aliyuncs.com/")
    //@ContentType("application/x-www-form-urlencoded")
    TranslationResponse aliTranslate(
            @Body("Action") String action,
            @Body("Format") String format,
            @Body("Version") String version,
            @Body("AccessKeyId") String accessKeyId,
            @Body("Signature") String signature,
            @Body("SignatureMethod") String signatureMethod,
            @Body("Timestamp") String timestamp,
            @Body("SignatureVersion") String signatureVersion,
            @Body("SignatureNonce") String signatureNonce,
            @Body("SourceLanguage") String sourceLanguage,
            @Body("TargetLanguage") String targetLanguage,
            @Body("SourceText") String sourceText,
            @Body("FormatType") String formatType,
            @Body("Scene") String scene
    );

    // ==================== 请求体定义 ====================

    @lombok.Data
    @lombok.Builder
    class GoogleTranslateRequest {
        private java.util.List<String> q;
        private String source;
        private String target;
        private String format;
    }

    @lombok.Data
    @lombok.AllArgsConstructor
    class MicrosoftTranslateRequest {
        private String text;
    }
}