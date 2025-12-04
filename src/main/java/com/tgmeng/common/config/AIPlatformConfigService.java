package com.tgmeng.common.config;

import cn.hutool.core.util.StrUtil;
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

    private List<AIPlatformConfig> aiPlatformConfigs;
    private String googleAdsClosePassword;

    @PostConstruct
    public void init() throws IOException {
        getAIPlatformConfigFunc();
        getGoogleAdsClosePasswordFunc();
    }

    private void getAIPlatformConfigFunc(){
        // 直接从系统环境变量读取
        String aiPlatformConfigJson = System.getenv("AI_PLATFORM_CONFIG");
        if (StrUtil.isEmpty(aiPlatformConfigJson)) {
            log.warn("未读取到 AI_PLATFORM_CONFIG 环境变量");
            return;
        }else{
            log.info("读取到 AI_PLATFORM_CONFIG 环境变量：{}", aiPlatformConfigJson);
        }

        // 使用 ObjectMapper 来解析 JSON 字符串并转换为 PlatformConfig 数组
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            aiPlatformConfigs = objectMapper.readValue(aiPlatformConfigJson, objectMapper.getTypeFactory().constructCollectionType(List.class, AIPlatformConfig.class));
            log.info("AI平台配置文件解析成功：{}", aiPlatformConfigs);
        }catch (Exception e){
            log.error("AI平台配置文件解析失败："+e.getMessage());
        }
    }

    // 关闭谷歌广告的密码
    private void getGoogleAdsClosePasswordFunc(){
        googleAdsClosePassword = System.getenv("GOOGLE_ADS_CLOSE_PASSWORD");
        if (StrUtil.isEmpty(googleAdsClosePassword)) {
            log.warn("未读取到 GOOGLE_ADS_CLOSE_PASSWORD 环境变量");
            return;
        }else{
            log.info("读取到 AI_PLATFORM_CONFIG 环境变量: {}", googleAdsClosePassword);
        }
    }
}
