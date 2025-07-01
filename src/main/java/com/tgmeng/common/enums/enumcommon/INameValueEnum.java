package com.tgmeng.common.enums.enumcommon;

public interface INameValueEnum<K,V> {
    K getKey();
    V getValue();
    Integer getSort();
    Boolean getEnabled();
    String getDescription();
}
