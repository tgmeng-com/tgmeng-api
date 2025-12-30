package com.tgmeng.common.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tgmeng.common.enums.business.LicenseFeatureEnum;
import com.tgmeng.common.enums.business.LicenseStatusEnum;
import com.tgmeng.common.enums.business.SubscriptionChannelTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LicenseBean {
    // 许可证
    private String licenseCode;
    // 状态
    private LicenseStatusEnum status;
    // 授权功能
    private List<LicenseFeatureEnum> features;
    // 发放时间
    private String startTime;
    // 过期时间
    private String expireTime;
    // 最大设备数
    private Integer maxMachines;
    // 设备列表
    private List<String> machineIds;
    // 每分钟调用次数限制
    private Integer limitPerMinute;
    // 每天调用次数限制
    private Integer limitPerDay;
    // 订阅推送全局关键词
    private List<String> subscriptionGlobalKeywords;
    // 订阅的全局分类选择
    private List<String> subscriptionGlobalCategories;
    // 订阅推送平台配置
    private List<SubscriptionPlatformConfig> subscriptionPlatformConfigs;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SubscriptionPlatformConfig {
        private SubscriptionChannelTypeEnum type;
        private String webhook;
        private String secret;
        private List<String> subscriptionPlatformKeywords;
        // 订阅的独立分类选择
        private List<String> subscriptionPlatformCategories;
        private String remark;
    }
}