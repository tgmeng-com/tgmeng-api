package com.tgmeng.common.util;

import com.tgmeng.common.enums.business.ForestRequestHeaderOriginEnum;
import com.tgmeng.common.enums.business.ForestRequestHeaderRefererEnum;
import com.tgmeng.common.forest.header.ForestRequestHeader;

public class ForestUtil {
    public static ForestRequestHeader getRandomRequestHeader(String referer, String origin) {
        String userAgent = UserAgentGeneratorUtil.generateRandomUserAgent();
        return new ForestRequestHeader()
                .setUserAgent(userAgent)
                //.setAccept("application/json, text/plain, */*")
                .setAcceptEncoding("gzip, deflate, br, zstd")
                .setAcceptLanguage("zh-CN,zh;q=0.9")
                .setConnection("keep-alive")
                .setReferer(referer)
                .setOrigin(origin);
    }
    public static ForestRequestHeader getRandomRequestHeaderForDouYin() {
        String userAgent = UserAgentGeneratorUtil.generateRandomUserAgent();
        return new ForestRequestHeader()
                .setUserAgent(userAgent)
                //.setAccept("application/json, text/plain, */*")
                //.setAcceptEncoding("gzip, deflate, br")
                //.setAcceptLanguage("zh-CN,zh;q=0.9")
                .setConnection("keep-alive")
                .setReferer(ForestRequestHeaderRefererEnum.DOUYIN.getValue())
                .setOrigin(ForestRequestHeaderOriginEnum.DOUYIN.getValue());
    }
    public static ForestRequestHeader getRandomRequestHeaderForDouBan() {
        String userAgent = UserAgentGeneratorUtil.generateRandomUserAgent();
        return new ForestRequestHeader()
                .setUserAgent(userAgent)
                .setConnection("keep-alive")
                .setReferer(ForestRequestHeaderRefererEnum.DOUBAN.getValue())
                .setOrigin(ForestRequestHeaderOriginEnum.DOUBAN.getValue());
    }
    public static ForestRequestHeader getRandomRequestHeaderForTouTiao() {
        String userAgent = UserAgentGeneratorUtil.generateRandomUserAgent();
        return new ForestRequestHeader()
                .setUserAgent(userAgent)
                .setConnection("keep-alive")
                .setReferer(ForestRequestHeaderRefererEnum.TOUTIAO.getValue())
                .setOrigin(ForestRequestHeaderOriginEnum.TOUTIAO.getValue());
    }

    public static ForestRequestHeader getRandomRequestHeaderForWangYi() {
        String userAgent = UserAgentGeneratorUtil.generateRandomUserAgent();
        return new ForestRequestHeader()
                .setUserAgent(userAgent)
                .setConnection("keep-alive")
                .setReferer(ForestRequestHeaderRefererEnum.TOUTIAO.getValue())
                .setOrigin(ForestRequestHeaderOriginEnum.TOUTIAO.getValue());
    }

    public static ForestRequestHeader getRandomRequestHeaderForWangYiYun() {
        String userAgent = UserAgentGeneratorUtil.generateRandomUserAgent();
        return new ForestRequestHeader()
                .setUserAgent(userAgent)
                .setConnection("keep-alive")
                .setReferer(ForestRequestHeaderRefererEnum.TOUTIAO.getValue())
                .setOrigin(ForestRequestHeaderOriginEnum.TOUTIAO.getValue());
    }

    public static ForestRequestHeader getRandomRequestHeaderForBilibili() {
        String userAgent = UserAgentGeneratorUtil.generateRandomUserAgent();
        return new ForestRequestHeader()
                .setUserAgent(userAgent)
                .setAcceptEncoding("gzip, deflate, br, zstd")
                .setAcceptLanguage("zh-CN,zh;q=0.9")
                .setConnection("keep-alive")
                .setReferer(ForestRequestHeaderRefererEnum.BILIBILI.getValue())
                .setOrigin(ForestRequestHeaderOriginEnum.BILIBILI.getValue());
    }

    public static ForestRequestHeader getRandomRequestHeaderForZhiHu() {
        String userAgent = UserAgentGeneratorUtil.generateRandomUserAgent();
        return new ForestRequestHeader()
                .setUserAgent(userAgent)
                //.setAccept("application/json, text/plain, */*")
                //.setAcceptEncoding("gzip, deflate, br, zstd")
                //.setAcceptLanguage("zh-CN,zh;q=0.9")
                //.setConnection("keep-alive")
                .setReferer(ForestRequestHeaderRefererEnum.ZHI_HU.getValue())
                .setOrigin(ForestRequestHeaderRefererEnum.ZHI_HU.getValue());
    }

    public static ForestRequestHeader getRandomRequestHeaderForAiQiYi() {
        String userAgent = UserAgentGeneratorUtil.generateRandomUserAgent();
        return new ForestRequestHeader()
                .setUserAgent(userAgent)
                //.setAccept("application/json, text/plain, */*")
                //.setAcceptEncoding("gzip, deflate, br, zstd")
                //.setAcceptLanguage("zh-CN,zh;q=0.9")
                //.setConnection("keep-alive")
                .setReferer(ForestRequestHeaderRefererEnum.AI_QI_YI.getValue())
                .setOrigin(ForestRequestHeaderRefererEnum.AI_QI_YI.getValue());
    }

}
