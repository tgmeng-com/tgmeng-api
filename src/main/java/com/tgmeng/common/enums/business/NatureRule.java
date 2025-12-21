package com.tgmeng.common.enums.business;

import com.tgmeng.common.enums.enumcommon.INameValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NatureRule implements INameValueEnum<String, Integer> {
    PERSON("nr", 5, "人名", true, 1),
    PLACE("ns", 5, "地名", true, 2),
    ORG("nt", 5, "组织机构名", true, 3),
    PROPER("nz", 5, "其他专有名词", true, 4),
    TIME("t", 3, "时间词", true, 5),
    NOUN("n", 2, "普通名词", true, 6),
    VERB("v", 1, "动词", true, 7);

    private final String key;
    private final Integer value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
