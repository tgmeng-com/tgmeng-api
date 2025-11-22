package com.tgmeng.model.dto.ai.config;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AIPlatformConfig {
    private String platform;
    private String model;
    private String key;
    private String from; // 来源，用来区别是哪位好心人提供的
}
