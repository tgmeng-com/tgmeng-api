package com.tgmeng.common.util;

import cn.hutool.core.collection.CollectionUtil;
import com.dtflys.forest.http.ForestResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgmeng.common.enums.business.AIModelEnum;
import com.tgmeng.common.enums.business.AIPlatFormEnum;
import com.tgmeng.common.enums.enumcommon.EnumUtils;
import com.tgmeng.common.forest.client.ai.IAIClient;
import com.tgmeng.model.dto.ai.config.AIPlatformConfig;
import com.tgmeng.model.dto.ai.request.AICommonChatModelRequestDTO;
import com.tgmeng.model.dto.ai.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;
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

    // 定义平台与响应类的映射
    private static final Map<AIPlatFormEnum, Class<? extends AICommonChatModelResponseDTO>> platformResponseMap = Map.of(
            AIPlatFormEnum.OPENAI, AIOpenAIChatModelResponseDTO.class,
            AIPlatFormEnum.DEEPSEEK, AIDeepSeekChatModelResponseDTO.class,
            AIPlatFormEnum.GUI_JI_LIU_DONG, AIGuiJiLiuDongChatModelResponseDTO.class,
            AIPlatFormEnum.ELYSIVER, AIOpenAIChatModelResponseDTO.class
            // TODO 可以继续添加其他平台和它们的响应类映射
    );

    /**
     * description: 获取系统里配置的所有代理，不管启用没启用的
     * method: getProxyAll
     *
     * @author tgmeng
     * @since 2025/7/1 13:08
     */
    public <T extends AICommonChatModelResponseDTO> AiChatModelResponseContentTemplateDTO aiChat(String content, List<AIPlatformConfig> aiPlatformConfigs) {
        for (AIPlatformConfig aiPlatformConfig : aiPlatformConfigs) {
            String key = aiPlatformConfig.getKey();
            AIPlatFormEnum aiPlatFormEnum = EnumUtils.getEnumByKey(AIPlatFormEnum.class, aiPlatformConfig.getPlatform());
            AIModelEnum aimodenEnum = EnumUtils.getEnumByKey(AIModelEnum.class, aiPlatformConfig.getModel());
            String from = aiPlatformConfig.getFrom();

            // 获取当前平台的响应类
            Class<? extends AICommonChatModelResponseDTO> responseClass = platformResponseMap.get(aiPlatFormEnum);
            if (responseClass == null) {
                log.error("没有为平台 {} 定义响应类", aiPlatFormEnum.getValue());
                continue; // 如果没有找到对应的响应类，跳过这个平台
            }

            long startTime = System.currentTimeMillis();  // 获取开始时间
            String platformName = aiPlatFormEnum.getKey();
            for (int attempt = 1; attempt <= MAX_RETRY_TIMES; attempt++) {
                log.info("[{}] 第{}次请求开始...", platformName, attempt);
                // 1. 创建请求
                AICommonChatModelRequestDTO aiCommonChatModelRequestDTO = createAIRequest(content, aimodenEnum);
                try {
                    // 2. 发起请求
                    ForestResponse forestResponse = aiClient.getAIMessage(aiPlatFormEnum.getValue(), key, aiCommonChatModelRequestDTO);
                    // 3. 解析响应
                    AICommonChatModelResponseDTO response = MAPPER.readValue(forestResponse.getContent(), responseClass);
                    // 4. 提取消息内容并转换
                    String messageContent = extractMessageContent(response);
                    if (messageContent == null) {
                        log.warn("{}未识别的响应类型: {}", platformName, response.getClass().getName());
                        return null;
                    }
                    // 5. 构建结果
                    List<AiChatModelResponseContentTemplateDTO.Result> resultList = MAPPER.readValue(messageContent, RESULT_TYPE);
                    AiChatModelResponseContentTemplateDTO result = new AiChatModelResponseContentTemplateDTO()
                            .setResult(resultList)
                            .setTime(TimeUtil.getCurrentTimeFormat(TimeUtil.defaultPattern))
                            .setAiPlatForm(platformName).setAiModel(aimodenEnum.getValue())
                            .setFrom(from);
                    log.info("[{}] 请求成功 ✅ 第{}次尝试 耗时: {}秒", platformName, attempt, (System.currentTimeMillis() - startTime) / 1000.0);
                    return result;
                } catch (Exception e) {
                    if (e.getCause() instanceof SocketTimeoutException) {
                        log.error(platformName + "🚨🚨🚨🚨🚨🚨请求超时: {}", e.getMessage());
                        handleRetry(attempt, platformName, "请求超时");
                    } else if (e.getCause() instanceof JsonProcessingException) {
                        log.error(platformName + "🚨🚨🚨🚨🚨🚨JSON解析失败, 原始内容可能格式错误: {}", e.getMessage());
                        handleRetry(attempt, platformName, "JSON解析失败");
                    } else {
                        log.error(platformName + "🚨🚨🚨🚨🚨🚨请求异常: {}", e.getMessage());
                        handleRetry(attempt, platformName, "未知异常");
                    }
                }
            }
            // 所有重试都失败
            double totalSeconds = (System.currentTimeMillis() - startTime) / 1000.0;
            log.error("AI总结请求最终失败，平台:{}, 模型:{}, from:{}, ❌ 已重试{}次 总耗时: {}秒", platformName, aimodenEnum.getValue(), from, MAX_RETRY_TIMES, totalSeconds);
        }
        return null;
    }

    public AICommonChatModelRequestDTO createAIRequest(String content, AIModelEnum aiModelEnum) {
        return new AICommonChatModelRequestDTO()
                .setModel(aiModelEnum.getValue())
                .setStream(false)
                .setMessages(
                        CollectionUtil.toList(new AICommonChatModelRequestDTO.Input().setRole("user")
                                .setContent(content)))
                .setInput(
                        CollectionUtil.toList(new AICommonChatModelRequestDTO.Input().setRole("user")
                                .setContent(content)))
                .setResponseFormat(new AICommonChatModelRequestDTO.ResponseFormat().setType("json_object"));
    }

    private <T extends AICommonChatModelResponseDTO> String extractMessageContent(T response) {
        return switch (response) {
            case AIDeepSeekChatModelResponseDTO r ->
                    Optional.ofNullable(r.getChoices()).filter(list -> !list.isEmpty()).map(list -> list.get(0).getMessage().getContent()).orElse(null);

            case AIGuiJiLiuDongChatModelResponseDTO r ->
                    Optional.ofNullable(r.getChoices()).filter(list -> !list.isEmpty()).map(list -> list.get(0).getMessage().getContent()).orElse(null);

            default -> null;
        };
    }

    private void handleRetry(int attempt, String platformName, String reason) {
        if (attempt < MAX_RETRY_TIMES) {
            long delayMs = RETRY_DELAY_MS * attempt; // 指数退避
            log.warn("[{}] 第{}次重试，原因：{}，延迟：{}毫秒", platformName, attempt, reason, delayMs);
            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                log.error("[{}] 重试等待被中断", platformName, ie);
            }
        } else {
            log.error("[{}] {}，已达最大重试次数({}次)", platformName, reason, MAX_RETRY_TIMES);
        }
    }
}
