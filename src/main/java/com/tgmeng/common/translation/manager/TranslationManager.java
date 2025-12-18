package com.tgmeng.common.translation.manager;

import com.tgmeng.common.translation.provider.TranslationProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 翻译管理器
 * 负责管理所有翻译Provider，实现自动重试和切换
 */

/**
 * 🎯 8个平台对比
 * 平台  免费额度  优先级建议  特点  需要外网
 * 小牛翻译  600万/月  1  🏆 额度最大  ❌     https://niutrans.com/  qps 5   每天20万
 * 腾讯翻译  500万/月  2  🥈 稳定可靠  ❌     https://cloud.tencent.com/ qps 5    每月500万
 * 百度翻译  100万/月  3  国内老牌  ❌        https://fanyi-api.baidu.com/   qps 10   每月100万
 * Google翻译  50万/月  4  🏆 质量最好  ✅   https://cloud.google.com/translate   需要绑卡
 * 微软翻译  200万/月  5  额度充足  ✅        https://portal.azure.com/
 * 有道翻译  50元体验金  6  国内平台  ❌       https://www.deepl.com/pro-api   需要绑卡
 * DeepL翻译  50万/月  7  🏆 欧洲语言最好  ✅ https://ai.youdao.com/  体验金50
 * 阿里翻译  100万/月试用  8阿里云生态  ❌     https://www.aliyun.com/product/ai/base_alimt  每月100万  50qps
 */
@Slf4j
@Component
public class TranslationManager {

    /**
     * 每个Provider的最大重试次数
     */
    private static final int MAX_RETRY = 2;

    /**
     * 所有的翻译Provider（Spring自动注入）
     */
    @Autowired
    private List<TranslationProvider> translationProviders;

    /**
     * 单个文本翻译（带自动重试和切换）
     * @param text 要翻译的文本
     * @param to 目标语言
     * @return 翻译结果
     */
    public String translate(String text, String to) {
        List<TranslationProvider> availableProviders = getAvailableProviders();

        if (availableProviders.isEmpty()) {
            log.error("没有可用的翻译服务，请检查配置，返回原始文本");
            return text;
        }

        log.info("开始翻译，可用服务数量: {}", availableProviders.size());

        // 遍历所有可用的Provider
        for (TranslationProvider provider : availableProviders) {
            try {
                // 构造单个元素的列表进行批量翻译
                List<String> results = batchTranslateWithRetry(provider, List.of(text), to);

                if (results != null && !results.isEmpty()) {
                    log.info("翻译成功，使用服务: {}", provider.getProviderName());
                    return results.getFirst();
                }
            } catch (Exception e) {
                log.warn("{}翻译失败: {}", provider.getProviderName(), e.getMessage());
            }
        }
        log.error("所有翻译服务均不可用，返回原始文本");
        return text;
    }

    /**
     * 批量翻译（带自动重试和切换）
     * @param texts 要翻译的文本列表
     * @param to 目标语言
     * @return 翻译结果列表，顺序与输入一致
     */
    public List<String> batchTranslate(List<String> texts, String to) {
        List<TranslationProvider> availableProviders = getAvailableProviders();

        if (availableProviders.isEmpty()) {
            log.error("没有可用的翻译服务，请检查配置，返回原始文本");
            return texts;
        }

        log.info("开始批量翻译，数量: {}, 目标语言: {}, 可用服务: {}",
                texts.size(), to,
                availableProviders.stream()
                        .map(TranslationProvider::getProviderName)
                        .collect(Collectors.joining(", "))
        );

        // 遍历所有可用的Provider
        for (TranslationProvider provider : availableProviders) {
            List<String> results = batchTranslateWithRetry(provider, texts, to);
            if (results != null) {
                log.info("批量翻译成功，使用服务: {}", provider.getProviderName());
                return results;
            }
        }
        log.error("所有翻译服务均不可用，返回原始文本");
        return texts;
    }

    /**
     * 使用指定Provider进行批量翻译，带重试机制
     * @param provider 翻译Provider
     * @param texts 文本列表
     * @param targetLang 目标语言
     * @return 翻译结果，失败返回null
     */
    private List<String> batchTranslateWithRetry(
            TranslationProvider provider,
            List<String> texts,
            String targetLang) {

        for (int attempt = 0; attempt <= MAX_RETRY; attempt++) {
            try {
                log.info("使用 {} 翻译，第 {} 次尝试", provider.getProviderName(), attempt + 1);
                List<String> results = provider.batchTranslate(texts, targetLang);
                log.info("{} 翻译成功", provider.getProviderName());
                return results;
            } catch (Exception e) {
                log.warn("{} 翻译失败，第 {} 次尝试，错误: {}", provider.getProviderName(), attempt + 1, e.getMessage());
                if (attempt == MAX_RETRY) {
                    log.error("{} 重试 {} 次后仍失败，切换到下一个服务", provider.getProviderName(), MAX_RETRY);
                } else {
                    // 重试前等待一下
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        return null;
    }

    /**
     * 获取所有可用的翻译Provider（按优先级排序）
     * @return 可用的Provider列表
     */
    private List<TranslationProvider> getAvailableProviders() {
        return translationProviders.stream()
                .filter(TranslationProvider::isAvailable)
                .sorted(Comparator.comparingInt(TranslationProvider::getPriority))
                .collect(Collectors.toList());
    }

    /**
     * 获取所有Provider的状态信息（用于监控）
     * @return Provider状态列表
     */
    public List<ProviderStatus> getProviderStatus() {
        return translationProviders.stream()
                .map(provider -> new ProviderStatus(
                        provider.getProviderName(),
                        provider.isAvailable(),
                        provider.getPriority()
                ))
                .sorted(Comparator.comparingInt(ProviderStatus::getPriority))
                .collect(Collectors.toList());
    }

    /**
     * Provider状态信息
     */
    @Data
    @AllArgsConstructor
    public static class ProviderStatus {
        private String name;
        private boolean available;
        private int priority;
    }
}
