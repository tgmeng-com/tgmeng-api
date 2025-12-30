package com.tgmeng.common.enums.enumcommon;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EnumUtils {
    public static <E extends INameValueEnum<K, V>, K, V> V getValueByKey(Class<E> enumClass, String key) {
        return Arrays.stream(enumClass.getEnumConstants()).filter(e -> e.getKey().equals(key)).findFirst().map(INameValueEnum::getValue).orElse(null);
    }

    public static <E extends INameValueEnum<K, V>, K, V> K getKeyByValue(Class<E> enumClass, V value) {
        return Arrays.stream(enumClass.getEnumConstants()).filter(e -> e.getValue().equals(value)).findFirst().map(INameValueEnum::getKey).orElse(null);
    }

    public static <E extends INameValueEnum<K, V>, K, V> List<K> getKeys(Class<E> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants()).map(INameValueEnum::getKey).collect(Collectors.toList());
    }

    public static <E extends INameValueEnum<K, V>, K, V> List<V> getValues(Class<E> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants()).map(INameValueEnum::getValue).collect(Collectors.toList());
    }

    public static <E extends INameValueEnum<K, V>, K, V> List<E> getEnumsBySort(Class<E> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants()).sorted(Comparator.comparingInt(e -> e.getSort() == null ? 0 : e.getSort())).collect(Collectors.toList());
    }

    public static <E extends INameValueEnum<K, V>, K, V> List<K> getKeysBySort(Class<E> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants()).sorted(Comparator.comparingInt(e -> e.getSort() == null ? 0 : e.getSort())).map(INameValueEnum::getKey).collect(Collectors.toList());
    }

    public static <E extends INameValueEnum<K, V>, K, V> List<V> getValuesBySort(Class<E> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants()).sorted(Comparator.comparingInt(e -> e.getSort() == null ? 0 : e.getSort())).map(INameValueEnum::getValue).collect(Collectors.toList());
    }

    public static <E extends INameValueEnum<K, V>, K, V> E getEnumByValue(Class<E> enumClass, V value) {
        return Arrays.stream(enumClass.getEnumConstants()).filter(e -> e.getValue().equals(value)).findFirst().orElse(null);
    }

    public static <E extends INameValueEnum<K, V>, K, V> E getEnumByKey(Class<E> enumClass, String key) {
        return Arrays.stream(enumClass.getEnumConstants()).filter(e -> e.getKey().equals(key)).findFirst().orElse(null);
    }

    public static <E extends INameValueEnum<K, V>, K, V> E getEnumByDescription(Class<E> enumClass, String description) {
        return Arrays.stream(enumClass.getEnumConstants()).filter(e -> e.getDescription().equals(description)).findFirst().orElse(null);
    }

    public static <E extends INameValueEnum<K, V>, K, V> boolean isExistValue(Class<E> enumClass, V value) {
        return Arrays.stream(enumClass.getEnumConstants()).anyMatch(e -> e.getValue().equals(value));
    }

    public static <E extends INameValueEnum<K, V>, K, V> boolean isExistKey(Class<E> enumClass, String name) {
        return Arrays.stream(enumClass.getEnumConstants()).anyMatch(e -> e.getKey().equals(name));
    }

    public static <E extends INameValueEnum<K, V>, K, V> List<V> sortValuesByEnumSort(Class<E> enumClass, List<V> values) {
        return values.stream()
                .sorted(Comparator.comparingInt(v -> {
                    E e = getEnumByValue(enumClass, v);
                    return e == null || e.getSort() == null
                            ? Integer.MAX_VALUE
                            : e.getSort();
                }))
                .collect(Collectors.toList());
    }
}
