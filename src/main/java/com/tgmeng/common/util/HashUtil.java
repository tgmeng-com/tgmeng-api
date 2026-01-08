package com.tgmeng.common.util;

import cn.hutool.crypto.SecureUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Data
@Slf4j
public class HashUtil {
    public static String generateHash(String title, String platformName) {
        // 生成 MD5 二进制
        byte[] hashBinary = SecureUtil.md5().digest(
                (title + platformName).getBytes(StandardCharsets.UTF_8)
        );
        // 转 Base64（22 个字符左右）
        return Base64.getEncoder().encodeToString(hashBinary);
    }
}
