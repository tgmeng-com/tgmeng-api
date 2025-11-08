package com.tgmeng.common.util;

import com.dtflys.forest.http.ForestCookie;
import com.tgmeng.common.enums.business.ForestRequestHeaderOriginEnum;
import com.tgmeng.common.enums.business.ForestRequestHeaderRefererEnum;
import com.tgmeng.common.forest.header.ForestRequestHeader;

import java.util.List;
import java.util.stream.Collectors;

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
                .setPriority("u=0, i")
                //.setReferer(ForestRequestHeaderRefererEnum.BILIBILI.getValue())
                //.setOrigin(ForestRequestHeaderOriginEnum.BILIBILI.getValue())
                ;
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
                .setOrigin(ForestRequestHeaderOriginEnum.ZHI_HU.getValue());
    }

    public static ForestRequestHeader getRandomRequestHeaderForTengXunShiPin() {
        String userAgent = UserAgentGeneratorUtil.generateRandomUserAgent();
        return new ForestRequestHeader()
                .setUserAgent(userAgent)
                //.setAccept("application/json, text/plain, */*")
                //.setAcceptEncoding("gzip, deflate, br, zstd")
                //.setConnection("keep-alive")
                .setAcceptLanguage("zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7")
                .setXForwardedFor("114.114.114.114")
                .setReferer(ForestRequestHeaderRefererEnum.TENG_XUN_SHI_PIN.getValue())
                .setOrigin(ForestRequestHeaderOriginEnum.TENG_XUN_SHI_PIN.getValue());
    }

    public static ForestRequestHeader getRandomRequestHeaderForAiQiYi() {
        String userAgent = UserAgentGeneratorUtil.generateRandomUserAgent();
        return new ForestRequestHeader()
                .setUserAgent(userAgent)
                //.setAccept("application/json, text/plain, */*")
                //.setAcceptEncoding("gzip, deflate, br, zstd")
                //.setConnection("keep-alive")
                .setAcceptLanguage("zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7")
                .setXForwardedFor("114.114.114.114")
                .setReferer(ForestRequestHeaderRefererEnum.AI_QI_YI.getValue())
                .setOrigin(ForestRequestHeaderOriginEnum.AI_QI_YI.getValue());
    }

    public static ForestRequestHeader getRandomRequestHeaderForYouKu(List<ForestCookie> cookies) {
        String userAgent = UserAgentGeneratorUtil.generateRandomUserAgent();
        String cookieString = cookies.stream()
                .map(cookie -> cookie.getName() + "=" + cookie.getValue())
                .collect(Collectors.joining("; "));
        cookieString+=";isI18n=false";
        return new ForestRequestHeader()
                .setUserAgent(userAgent)
                //.setAccept("application/json, text/plain, */*")
                //.setAcceptEncoding("gzip, deflate, br, zstd")
                //.setConnection("keep-alive")
                .setAcceptLanguage("zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7")
                .setXForwardedFor("114.114.114.114")
                .setReferer(ForestRequestHeaderRefererEnum.YOU_KU.getValue())
                .setOrigin(ForestRequestHeaderOriginEnum.YOU_KU.getValue())
                .setCookie(cookieString)
                ;
    }

    public static ForestRequestHeader getRandomRequestHeaderForMangGuo() {
        String userAgent = UserAgentGeneratorUtil.generateRandomUserAgent();
        return new ForestRequestHeader()
                .setUserAgent(userAgent)
                //.setAccept("application/json, text/plain, */*")
                //.setAcceptEncoding("gzip, deflate, br, zstd")
                .setAcceptLanguage("zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7")
                //.setConnection("keep-alive")
                .setXForwardedFor("114.114.114.114")
                .setReferer(ForestRequestHeaderRefererEnum.MANG_GUO.getValue())
                .setOrigin(ForestRequestHeaderOriginEnum.MANG_GUO.getValue())
                ;
    }

    public static ForestRequestHeader getRandomRequestHeaderForJinRongJie() {
        String userAgent = UserAgentGeneratorUtil.generateRandomUserAgent();
        return new ForestRequestHeader()
                .setUserAgent(userAgent)
                .setAccept("*/*")
                //.setAcceptEncoding("gzip, deflate, br, zstd")
                //.setAcceptLanguage("zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7")
                .setConnection("keep-alive")
                //.setXForwardedFor("114.114.114.114")
                .setReferer(ForestRequestHeaderRefererEnum.JING_RONG_JIE.getValue())
                .setOrigin(ForestRequestHeaderOriginEnum.JING_RONG_JIE.getValue())
                ;
    }

    public static ForestRequestHeader getRandomRequestHeaderForGeLongHui() {
        String userAgent = UserAgentGeneratorUtil.generateRandomUserAgent();
        return new ForestRequestHeader()
                .setUserAgent(userAgent)
                //.setAccept("*/*")
                //.setAcceptEncoding("gzip, deflate, br, zstd")
                //.setAcceptLanguage("zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7")
                //.setConnection("keep-alive")
                //.setXForwardedFor("114.114.114.114")
                .setReferer(ForestRequestHeaderRefererEnum.GE_LONG_HUI.getValue())
                .setOrigin(ForestRequestHeaderOriginEnum.GE_LONG_HUI.getValue())
                ;
    }

    public static ForestRequestHeader getRandomRequestHeaderForFaGuang() {
        String userAgent = UserAgentGeneratorUtil.generateRandomUserAgent();
        return new ForestRequestHeader()
                //.setUserAgent(userAgent)
                //.setAccept("*/*")
                //.setAcceptEncoding("gzip, deflate, br, zstd")
                //.setAcceptLanguage("zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7")
                //.setConnection("keep-alive")
                //.setXForwardedFor("114.114.114.114")
                .setReferer(ForestRequestHeaderRefererEnum.FA_GUANG.getValue())
                .setOrigin(ForestRequestHeaderOriginEnum.FA_GUANG.getValue())
                ;
    }

    public static ForestRequestHeader getRandomRequestHeaderForHuaErJieRiBao() {
        String userAgent = UserAgentGeneratorUtil.generateRandomUserAgent();
        return new ForestRequestHeader()
                .setUserAgent(userAgent)
                .setAccept("*/*")
                .setAcceptEncoding("gzip, deflate, br, zstd")
                .setAcceptLanguage("zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7")
                .setConnection("keep-alive")
                //.setXForwardedFor("114.114.114.114")
                .setReferer(ForestRequestHeaderRefererEnum.HUA_ER_JIE_RI_BAO.getValue())
                .setOrigin(ForestRequestHeaderOriginEnum.HUA_ER_JIE_RI_BAO.getValue())
                ;
    }

    public static ForestRequestHeader getRandomRequestHeaderForWoShiPM() {
        String userAgent = UserAgentGeneratorUtil.generateRandomUserAgent();
        return new ForestRequestHeader()
                .setHost("www.woshipm.com")
                .setUserAgent(userAgent)
                .setAccept("*/*")
                .setAcceptEncoding("gzip, deflate, br, zstd")
                .setAcceptLanguage("zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7")
                .setConnection("keep-alive")
                //.setXForwardedFor("114.114.114.114")
                .setReferer(ForestRequestHeaderRefererEnum.WO_SHI_PM.getValue())
                .setOrigin(ForestRequestHeaderOriginEnum.WO_SHI_PM.getValue())
                ;
    }

    public static ForestRequestHeader getRandomRequestHeaderForMIT() {
        String userAgent = UserAgentGeneratorUtil.generateRandomUserAgent();
        return new ForestRequestHeader()
                .setHost("apii.web.mittrchina.com")
                .setUserAgent(userAgent)
                .setAccept("*/*")
                .setAcceptEncoding("gzip, deflate, br, zstd")
                .setAcceptLanguage("zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7")
                .setConnection("keep-alive")
                //.setXForwardedFor("114.114.114.114")
                .setReferer(ForestRequestHeaderRefererEnum.MIT.getValue())
                .setOrigin(ForestRequestHeaderOriginEnum.MIT.getValue())
                ;
    }

    public static ForestRequestHeader getRandomRequestHeaderForAwwwards() {
        String userAgent = UserAgentGeneratorUtil.generateRandomUserAgent();
        return new ForestRequestHeader()
                //.setHost("www.behance.net")
                //.setUserAgent(userAgent)
                //.setAccept("*/*")
                //.setAcceptEncoding("gzip, deflate, br, zstd")
                //.setAcceptLanguage("zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7")
                //.setConnection("keep-alive")
                //.setXForwardedFor("114.114.114.114")
                .setReferer(ForestRequestHeaderRefererEnum.AWWWARDS.getValue())
                .setOrigin(ForestRequestHeaderOriginEnum.AWWWARDS.getValue())
                ;
    }

    public static ForestRequestHeader getRandomRequestHeaderForZhongGuoKeXueYuan() {
        String userAgent = UserAgentGeneratorUtil.generateRandomUserAgent();
        return new ForestRequestHeader()
                .setHost("www.cas.cn")
                .setUserAgent(userAgent)
                //.setCookie("")
                .setAccept("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                .setAcceptEncoding("gzip, deflate, br, zstd")
                .setAcceptLanguage("zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7")
                .setConnection("keep-alive")
                //.setXForwardedFor("114.114.114.114")
                .setReferer(ForestRequestHeaderRefererEnum.ZHONG_GUO_KE_XUE_YUAN.getValue())
                .setOrigin(ForestRequestHeaderOriginEnum.ZHONG_GUO_KE_XUE_YUAN.getValue())
                //.setCacheControl("max-age=0")
                //.setSecChUa("\"Not)A;Brand\";v=\"8\", \"Chromium\";v=\"138\", \"Google Chrome\";v=\"138\"")
                //.setSecChUaMobile("?0")
                //.setSecFetchSite("")
                //.setSecFetchUser("?1")
                //.setSecChUaPlatform("Windows")
                //.setSecFetchDest("document")
                //.setSecFetchMode("navigate")
                //.setUpgradeInsecureRequests("1")
                ;
    }

    public static ForestRequestHeader getRandomRequestHeaderForEurekAlert() {
        String userAgent = UserAgentGeneratorUtil.generateRandomUserAgent();
        return new ForestRequestHeader()
                //.setHost("www.cas.cn")
                .setUserAgent(userAgent)
                //.setCookie("")
                .setAccept("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                .setAcceptEncoding("gzip, deflate, br, zstd")
                .setAcceptLanguage("zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7")
                .setConnection("keep-alive")
                .setXForwardedFor("114.114.114.114")
                .setReferer(ForestRequestHeaderRefererEnum.EUREK_ALERT.getValue())
                .setOrigin(ForestRequestHeaderOriginEnum.EUREK_ALERT.getValue())
                //.setCacheControl("max-age=0")
                //.setSecChUa("\"Not)A;Brand\";v=\"8\", \"Chromium\";v=\"138\", \"Google Chrome\";v=\"138\"")
                //.setSecChUaMobile("?0")
                //.setSecFetchSite("")
                //.setSecFetchUser("?1")
                //.setSecChUaPlatform("Windows")
                //.setSecFetchDest("document")
                //.setSecFetchMode("navigate")
                //.setUpgradeInsecureRequests("1")
                ;
    }

    public static ForestRequestHeader getRandomRequestHeaderForDongQiuDi() {
        String userAgent = UserAgentGeneratorUtil.generateRandomUserAgent();
        return new ForestRequestHeader()
                .setHost("www.dongqiudi.com")
                .setUserAgent(userAgent)
                //.setCookie("")
                //.setAccept("*/*")
                //.setAcceptEncoding("gzip, deflate, br, zstd")
                //.setAcceptLanguage("zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7")
                //.setConnection("keep-alive")
                //.setXForwardedFor("114.114.114.114")
                //.setReferer(ForestRequestHeaderRefererEnum.DONG_QIU_DI.getValue())
                //.setOrigin(ForestRequestHeaderOriginEnum.DONG_QIU_DI.getValue())
                //.setCacheControl("max-age=0")
                //.setSecChUa("\"Not)A;Brand\";v=\"8\", \"Chromium\";v=\"138\", \"Google Chrome\";v=\"138\"")
                //.setSecChUaMobile("?0")
                //.setSecFetchSite("")
                //.setSecFetchUser("?1")
                //.setSecChUaPlatform("Windows")
                //.setSecFetchDest("document")
                //.setSecFetchMode("navigate")
                //.setUpgradeInsecureRequests("1")
                ;
    }

    public static ForestRequestHeader getRandomRequestHeaderForV2EX() {
        String userAgent = UserAgentGeneratorUtil.generateRandomUserAgent();
        return new ForestRequestHeader()
                //.setHost("www.v2ex.com")
                .setUserAgent(userAgent)
                //.setCookie("")
                //.setAccept("*/*")
                //.setAcceptEncoding("gzip, deflate, br, zstd")
                //.setAcceptLanguage("zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7")
                //.setConnection("keep-alive")
                //.setXForwardedFor("114.114.114.114")
                //.setReferer(ForestRequestHeaderRefererEnum.DONG_QIU_DI.getValue())
                //.setOrigin(ForestRequestHeaderOriginEnum.DONG_QIU_DI.getValue())
                //.setCacheControl("max-age=0")
                //.setSecChUa("\"Not)A;Brand\";v=\"8\", \"Chromium\";v=\"138\", \"Google Chrome\";v=\"138\"")
                //.setSecChUaMobile("?0")
                //.setSecFetchSite("")
                //.setSecFetchUser("?1")
                //.setSecChUaPlatform("Windows")
                //.setSecFetchDest("document")
                //.setSecFetchMode("navigate")
                //.setUpgradeInsecureRequests("1")
                ;
    }

    public static ForestRequestHeader getRandomRequestHeaderForYiMuSanFenDi() {
        String userAgent = UserAgentGeneratorUtil.generateRandomUserAgent();
        return new ForestRequestHeader()
                //.setHost("www.v2ex.com")
                .setUserAgent(userAgent)
                //.setCookie("")
                //.setAccept("*/*")
                //.setAcceptEncoding("gzip, deflate, br, zstd")
                //.setAcceptLanguage("zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7")
                //.setConnection("keep-alive")
                //.setXForwardedFor("114.114.114.114")
                //.setReferer(ForestRequestHeaderRefererEnum.DONG_QIU_DI.getValue())
                //.setOrigin(ForestRequestHeaderOriginEnum.DONG_QIU_DI.getValue())
                //.setCacheControl("max-age=0")
                //.setSecChUa("\"Not)A;Brand\";v=\"8\", \"Chromium\";v=\"138\", \"Google Chrome\";v=\"138\"")
                //.setSecChUaMobile("?0")
                //.setSecFetchSite("")
                //.setSecFetchUser("?1")
                //.setSecChUaPlatform("Windows")
                //.setSecFetchDest("document")
                //.setSecFetchMode("navigate")
                //.setUpgradeInsecureRequests("1")
                ;
    }

    public static ForestRequestHeader getRandomRequestHeaderForWenZhangJueJin() {
        String userAgent = UserAgentGeneratorUtil.generateRandomUserAgent();
        return new ForestRequestHeader()
                //.setHost("www.v2ex.com")
                .setUserAgent(userAgent)
                //.setCookie("")
                //.setAccept("*/*")
                //.setAcceptEncoding("gzip, deflate, br, zstd")
                //.setAcceptLanguage("zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7")
                //.setConnection("keep-alive")
                //.setXForwardedFor("114.114.114.114")
                //.setReferer(ForestRequestHeaderRefererEnum.DONG_QIU_DI.getValue())
                //.setOrigin(ForestRequestHeaderOriginEnum.DONG_QIU_DI.getValue())
                //.setCacheControl("max-age=0")
                //.setSecChUa("\"Not)A;Brand\";v=\"8\", \"Chromium\";v=\"138\", \"Google Chrome\";v=\"138\"")
                //.setSecChUaMobile("?0")
                //.setSecFetchSite("")
                //.setSecFetchUser("?1")
                //.setSecChUaPlatform("Windows")
                //.setSecFetchDest("document")
                //.setSecFetchMode("navigate")
                //.setUpgradeInsecureRequests("1")
                ;
    }
}