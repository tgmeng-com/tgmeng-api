package com.tgmeng.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgmeng.model.dto.ai.config.AIPlatformConfig;
import com.tgmeng.model.po.topsearch.ProxyConfig;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Data
@Slf4j
@Service
public class SystemConfigService {

    private List<AIPlatformConfig> aiPlatformConfigs;
    private List<ProxyConfig> proxyConfigs;

    @Value("${my-config.ai.ai-platform-config}")
    private String aiPlatformConfig;

    @Value("${my-config.proxy.config}")
    private String proxyConfig;

    @PostConstruct
    public void init() throws IOException {
        getAIPlatformConfigFunc();
        getProxyConfigFunc();
    }

    private void getAIPlatformConfigFunc() {
        // 使用 ObjectMapper 来解析 JSON 字符串并转换为 PlatformConfig 数组
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            aiPlatformConfigs = objectMapper.readValue(aiPlatformConfig, objectMapper.getTypeFactory().constructCollectionType(List.class, AIPlatformConfig.class));
            log.info("AI平台配置解析成功：{}", aiPlatformConfigs);
        } catch (Exception e) {
            log.error("AI平台配置解析失败：" + e.getMessage());
        }
    }

    private void getProxyConfigFunc() {
        // 使用 ObjectMapper 来解析 JSON 字符串并转换为 ProxyPO 数组
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            proxyConfigs = objectMapper.readValue(proxyConfig, objectMapper.getTypeFactory().constructCollectionType(List.class, ProxyConfig.class));
            proxyConfigs = proxyConfigs.stream()
                    .filter(ProxyConfig::getEnabled)
                    .toList();
            log.info("代理配置解析成功：{}", proxyConfigs);
        } catch (Exception e) {
            log.error("代理配置解析失败：" + e.getMessage());
        }
    }
}
