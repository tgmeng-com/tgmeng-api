package com.tgmeng.common.util;

import com.tgmeng.common.forest.header.ForestRequestHeader;

public class ForestUtil {
    public static ForestRequestHeader getRandomRequestHeader(String referer, String origin) {
        String userAgent = UserAgentGeneratorUtil.generateRandomUserAgent();
        return new ForestRequestHeader()
                .setUserAgent(userAgent)
                .setAccept("application/json, text/plain, */*")
                .setAcceptEncoding("gzip, deflate, br")
                .setAcceptLanguage("zh-CN,zh;q=0.9")
                .setConnection("keep-alive")
                .setReferer(referer)
                .setOrigin(origin);
    }
    public static ForestRequestHeader getRandomRequestHeader() {
        String userAgent = UserAgentGeneratorUtil.generateRandomUserAgent();
        return new ForestRequestHeader()
                .setUserAgent(userAgent)
                .setAccept("application/json, text/plain, */*")
                .setAcceptEncoding("gzip, deflate, br")
                .setAcceptLanguage("zh-CN,zh;q=0.9")
                .setConnection("keep-alive");
    }
}
