package com.tgmeng.common.Enum.Enumcommon;

public interface INameValueEnum<T> {
    String getKey();
    T getValue();
    Integer getSort();
    String getDescription();
}
