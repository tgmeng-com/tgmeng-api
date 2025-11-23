package com.tgmeng.model.dto.ai.config;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class AIPlatformConfig {
    private String platform;
    private String api;
    private List<String> models;
    private String key;
    private String from; // 来源，用来区别是哪位好心人提供的
}
