package com.tgmeng.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgmeng.model.dto.ai.config.AIPlatformConfig;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Data
@Slf4j
@Service
public class AIPlatformConfigService {
    //@Value("${AI_PLATFORM_CONFIG}")
    private String aiPlatformConfigJson;

    private List<AIPlatformConfig> aiPlatformConfigs;

    @PostConstruct
    public void init() throws IOException {
        // 使用 ObjectMapper 来解析 JSON 字符串并转换为 PlatformConfig 数组
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            aiPlatformConfigs = objectMapper.readValue(aiPlatformConfigJson, objectMapper.getTypeFactory().constructCollectionType(List.class, AIPlatformConfig.class));
        }catch (Exception e){
            log.info("AI平台配置文件解析失败："+e.getMessage());
        }
    }
}
