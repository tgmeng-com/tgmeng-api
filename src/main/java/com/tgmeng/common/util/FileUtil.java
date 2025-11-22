package com.tgmeng.common.util;

import cn.hutool.core.io.resource.ResourceUtil;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {
    /** 读取类路径下的json文件（推荐） */
    public static String readFileToStringFromClasspath(String path) {
        return ResourceUtil.readUtf8Str(path);   // hutool 方式，最简
    }

    /** 读取任意路径的json文件 */
    public static String readFileToStringFromAbsolutePath(String absolutePath) {
        try {
            return Files.readString(Paths.get(absolutePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
