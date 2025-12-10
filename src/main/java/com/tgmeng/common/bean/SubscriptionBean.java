package com.tgmeng.common.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tgmeng.common.enums.business.SubscriptionChannelTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriptionBean implements Serializable {
    private static final long serialVersionUID = -8143566284567789026L;
    private String accessKey;
    private List<String> keywords;
    private List<PushConfig> platforms;
    private Set<String> sent = new LinkedHashSet<>();

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PushConfig {
        private SubscriptionChannelTypeEnum type;
        private String webhook;
        private String secret;
        private List<String> platformKeywords;
        private String remark;
    }
}
