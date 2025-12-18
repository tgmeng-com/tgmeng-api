package com.tgmeng.common.translation.provider;

import java.util.List;

/**
 * 翻译服务提供者接口
 * 所有翻译平台的Provider都需要实现此接口
 */
public interface TranslationProvider {

    /**
     * 批量翻译
     * @param sourceTexts 源文本列表
     * @param targetLang 目标语言（zh/en/ja/ko等）
     * @return 翻译结果列表，顺序与输入一致
     * @throws Exception 翻译失败时抛出异常
     */
    List<String> batchTranslate(List<String> sourceTexts, String targetLang) throws Exception;

    /**
     * 获取提供商名称
     * @return 提供商名称（如：BAIDU、NIUTRANS、TENCENT等）
     */
    String getProviderName();

    /**
     * 检查服务是否可用
     * @return true-可用（已配置密钥且enabled=true），false-不可用
     */
    boolean isAvailable();

    /**
     * 获取优先级
     * @return 数字越小优先级越高（1最高，999最低）
     */
    int getPriority();
}