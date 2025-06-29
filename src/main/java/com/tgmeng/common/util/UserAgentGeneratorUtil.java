package com.tgmeng.common.util;

import java.util.Random;

/**
 * description: user-agent,为了适应微博热搜接口，剔除了所有的移动端设备
 * package: com.tgmeng.common.util
 * className: UserAgentGeneratorUtil
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/6/29 20:38
*/
public class UserAgentGeneratorUtil {
    // 微博成功请求的操作系统版本
    private static final String[] WINDOWS_VERSIONS = {
            "Windows NT 10.0; Win64; x64",
            "Windows NT 6.3; Win64; x64",
            "Windows NT 6.1; WOW64"
    };

    private static final String[] MAC_VERSIONS = {
            "Macintosh; Intel Mac OS X 10_15_7",
            "Macintosh; Intel Mac OS X 11_3_1",
            "Macintosh; Intel Mac OS X 12_5"
    };

    private static final String[] BROWSERS = {"Chrome", "Edge", "Firefox"};

    private static final Random RANDOM = new Random();

    public static String generateRandomUserAgent() {
        String os;
        boolean isMobile = false;  // 默认设置为桌面设备

        // 强制选择桌面设备
        int osType = RANDOM.nextInt(2); // 微博大部分使用 Windows 和 Mac 系统
        switch (osType) {
            case 0 -> os = WINDOWS_VERSIONS[RANDOM.nextInt(WINDOWS_VERSIONS.length)];
            default -> os = MAC_VERSIONS[RANDOM.nextInt(MAC_VERSIONS.length)];
        }

        // 随机选择浏览器（排除较老版本的 Firefox）
        String browser = BROWSERS[RANDOM.nextInt(BROWSERS.length)];

        // 生成随机版本号
        int major = 100 + RANDOM.nextInt(20);  // 主版本号（100-120）
        int minor = 0 + RANDOM.nextInt(10);   // 次版本号（0-9）
        int build = 1000 + RANDOM.nextInt(5000); // 构建版本号

        // 构建 User-Agent
        String userAgent;
        switch (browser) {
            case "Chrome" -> {
                userAgent = String.format(
                        "Mozilla/5.0 (%s) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/%d.%d.%d Safari/537.36",
                        os, major, minor, build);
            }
            case "Edge" -> {
                userAgent = String.format(
                        "Mozilla/5.0 (%s) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/%d.%d.%d Safari/537.36 Edg/%d.%d",
                        os, major, minor, build, major, minor);
            }
            case "Firefox" -> {
                // 限制 Firefox 版本在较新的范围
                userAgent = String.format(
                        "Mozilla/5.0 (%s; rv:%d.0) Gecko/20100101 Firefox/%d.0",
                        os, major, major);
            }
            default -> userAgent = "Mozilla/5.0";
        }
        return userAgent;
    }
}
