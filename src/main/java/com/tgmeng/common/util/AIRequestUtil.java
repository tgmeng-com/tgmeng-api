package com.tgmeng.common.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.dtflys.forest.http.ForestResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgmeng.common.forest.client.ai.IAIClient;
import com.tgmeng.model.dto.ai.config.AIPlatformConfig;
import com.tgmeng.model.dto.ai.request.AICommonChatModelRequestDTO;
import com.tgmeng.model.dto.ai.response.AICommonChatModelResponseDTO;
import com.tgmeng.model.dto.ai.response.AiChatModelResponseContentTemplateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Optional;

/**
 * description: 这个类用来存储代理信息，后续直接放在数据库就行(穷，没有数据库)
 * package: com.tgmeng.common.util
 * className: ProxyPoGenerateUtil
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/7/1 12:54
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AIRequestUtil {

    private final IAIClient aiClient;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final TypeReference<List<AiChatModelResponseContentTemplateDTO.Result>> RESULT_TYPE = new TypeReference<List<AiChatModelResponseContentTemplateDTO.Result>>() {
    };
    // 重试配置
    private static final int MAX_RETRY_TIMES = 3;      // 最大重试次数
    private static final long RETRY_DELAY_MS = 1000;   // 重试延迟(毫秒)

    /**
     * description: 获取系统里配置的所有代理，不管启用没启用的
     * method: getProxyAll
     *
     * @author tgmeng
     * @since 2025/7/1 13:08
     */
    public <T extends AICommonChatModelResponseDTO> AiChatModelResponseContentTemplateDTO aiChat(String content, List<AIPlatformConfig> aiPlatformConfigs) {
        for (AIPlatformConfig aiPlatformConfig : aiPlatformConfigs) {
            if (ObjUtil.isNotEmpty(aiPlatformConfig)) {
                String platform = aiPlatformConfig.getPlatform();
                String api = aiPlatformConfig.getApi();
                String key = aiPlatformConfig.getKey();
                List<String> models = aiPlatformConfig.getModels();
                String from = aiPlatformConfig.getFrom();

                for (String model : models) {
                    if (StrUtil.isNotEmpty(model)) {
                        long startTime = System.currentTimeMillis();  // 获取开始时间
                        for (int attempt = 1; attempt <= MAX_RETRY_TIMES; attempt++) {
                            log.info("[{},{}] 第{}次请求开始...", platform, model, attempt);
                            // 1. 创建请求
                            AICommonChatModelRequestDTO aiCommonChatModelRequestDTO = createAIRequest(content, model);
                            try {
                                // 2. 发起请求
                                ForestResponse forestResponse = aiClient.getAIMessage(api, key, aiCommonChatModelRequestDTO);
                                // 3. 解析响应
                                AICommonChatModelResponseDTO response = MAPPER.readValue(forestResponse.getContent(), AICommonChatModelResponseDTO.class);
                                // 4. 提取消息内容并转换
                                String messageContent = extractMessageContent(response);
                                Long totalTokens = response.getUsage().getTotalTokens();
                                if (messageContent == null) {
                                    log.warn("[{},{}] 未识别的响应类型: {}", platform, model, response.getClass().getName());
                                    return null;
                                }
                                // 5. 构建结果
                                List<AiChatModelResponseContentTemplateDTO.Result> resultList = MAPPER.readValue(messageContent, RESULT_TYPE);
                                AiChatModelResponseContentTemplateDTO result = new AiChatModelResponseContentTemplateDTO()
                                        .setResult(resultList)
                                        .setTime(TimeUtil.getCurrentTimeFormat(TimeUtil.defaultPattern))
                                        .setPlatform(platform)
                                        .setModel(model)
                                        .setFrom(from)
                                        .setTotalTokens(totalTokens);
                                log.info("👄👄👄👄👄AI时报大模型分析成功：[{},{}] 请求成功 ✅ 第{}次尝试 耗时: {}秒，消耗Token: {}", platform, model, attempt, (System.currentTimeMillis() - startTime) / 1000.0, totalTokens);
                                return result;
                            } catch (Exception e) {
                                if (e.getCause() instanceof SocketTimeoutException) {
                                    log.error("[{},{}] 🚨🚨🚨🚨🚨🚨请求超时: {}", platform, model, e.getMessage());
                                    handleRetry(attempt, platform, model, "请求超时");
                                } else if (e.getCause() instanceof JsonProcessingException) {
                                    log.error("[{},{}] 🚨🚨🚨🚨🚨🚨JSON解析失败, 原始内容可能格式错误: {}", platform, model, e.getMessage());
                                    handleRetry(attempt, platform, model, "JSON解析失败");
                                } else {
                                    log.error("[{},{}] 🚨🚨🚨🚨🚨🚨请求异常: {}", platform, model, e.getMessage());
                                    handleRetry(attempt, platform, model, "未知异常");
                                }
                            }
                        }
                        // 所有重试都失败
                        double totalSeconds = (System.currentTimeMillis() - startTime) / 1000.0;
                        log.error("[{},{}] AI总结请求最终失败， from:{}, ❌ 已重试{}次 总耗时: {}秒", platform, model, from, MAX_RETRY_TIMES, totalSeconds);
                    }
                }
            }
        }
        log.error("❌❌❌❌❌❌ 所有已配置的AI平台均请求AI时报失败");
        return null;
    }

    public AICommonChatModelRequestDTO createAIRequest(String content, String model) {
        return new AICommonChatModelRequestDTO()
                .setModel(model)
                .setStream(false)
                .setMessages(
                        CollectionUtil.toList(new AICommonChatModelRequestDTO.Input().setRole("user")
                                .setContent(content)))
                .setInput(
                        CollectionUtil.toList(new AICommonChatModelRequestDTO.Input().setRole("user")
                                .setContent(content)))
                .setResponseFormat(new AICommonChatModelRequestDTO.ResponseFormat().setType("json_object"));
    }

    private String extractMessageContent(AICommonChatModelResponseDTO response) {
        return Optional.ofNullable(response.getChoices()).filter(list -> !list.isEmpty()).map(list -> list.getFirst().getMessage().getContent()).orElse(null);
    }

    private void handleRetry(int attempt, String platform, String model, String reason) {
        if (attempt < MAX_RETRY_TIMES) {
            long delayMs = RETRY_DELAY_MS * attempt; // 指数退避
            log.warn("[{},{}] 第{}次重试，原因：{}，延迟：{}毫秒", platform, model, attempt, reason, delayMs);
            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                log.error("[{},{}] 重试等待被中断", platform, model, ie);
            }
        } else {
            log.error("[{},{}] {}，已达最大重试次数({}次)", platform, model, reason, MAX_RETRY_TIMES);
        }
    }
}
