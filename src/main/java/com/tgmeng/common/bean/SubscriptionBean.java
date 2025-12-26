package com.tgmeng.common.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriptionBean {
    private String licenseCode;
    @JsonDeserialize(as = LinkedHashSet.class)
    private Set<String> sent = new LinkedHashSet<>();
}
