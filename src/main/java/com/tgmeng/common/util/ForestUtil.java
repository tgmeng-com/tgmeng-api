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
                .setOrigin(ForestRequestHeaderOriginEnum.ZHI_HU.getValue());
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
                .setOrigin(ForestRequestHeaderOriginEnum.AI_QI_YI.getValue());
    }

    public static ForestRequestHeader getRandomRequestHeaderForYouKu() {
        String userAgent = UserAgentGeneratorUtil.generateRandomUserAgent();
        return new ForestRequestHeader()
                .setUserAgent(userAgent)
                //.setAccept("application/json, text/plain, */*")
                //.setAcceptEncoding("gzip, deflate, br, zstd")
                //.setAcceptLanguage("zh-CN,zh;q=0.9")
                //.setConnection("keep-alive")
                .setReferer(ForestRequestHeaderRefererEnum.YOU_KU.getValue())
                .setOrigin(ForestRequestHeaderOriginEnum.YOU_KU.getValue())
                .setCookie("isI18n=false; __ysuid=1751425904161Ock; __ayft=1752626298446; __aysid=1752626298446T67; __ayscnt=1; xlly_s=1; mtop_partitioned_detect=1; HISTORY_KEY=%5B%22%E6%8E%92%E8%A1%8C%E6%A6%9C%22%2C%22%E6%8E%92%E8%A1%8C%22%2C%22%E8%97%8F%E6%B5%B7%E4%BC%A0%22%2C%22%E5%87%A1%E4%BA%BA%E4%BF%AE%E4%BB%99%E4%BC%A0%E5%A5%87%22%5D; __ayvstp=2; __aysvstp=2; __arpvid=1752675714499mbfaBL-1752675714503; __aypstp=32; __ayspstp=32; tfstk=gvA-vdVoMmmoTTdRoUk0-gDcsyg09xYPl38_tMjudnKvWi4lqaxuOKKB0Tj5-bSQJnTNVUmEarv22pF5LMRuRnRwBW6rFzOXH381E6Ap8WQBRHuPx4kDzU5FOcmg9fYyzMH81KocO2TftwKaBOJ9zU5UurqIIKTzJFgMs61BAssf-a1QV6NIks_OJ6N5d7wbciSfO765OrsfRwFCRH1IkEsVR6sBATgvljyx2iU5xWpJnc6Vn8ZLBRdAyTILtiFdOINGeGT5DWi6MaFRfUsYOWI9gkS9kUm8_EfyOhQMqjNJXeTwdtdLccIeMEOWdEr-l1LvndXJH0NdmQbdC66YRWTvPiXfr1G_DZJXrpOy2yFCzQAGpGWxRWWhGCX6C3UE7E15RHWwtcPPVeTw_dftwl7JhFCA4rAM6ntgjGQ3Fq3YLJW5uX-YLaG2L7LdkG0RwJyF3q7AjqH_dJZIlZIiyWyULtRO.; _m_h5_tk=b2c82c5b852e13755389f937a54909c6_1752683163059; _m_h5_tk_enc=cbe8d49f8666c0c5358cd44ff0c87f7d; isg=BB4eoMf0-j4aTS5_qCQzJk8Sb7Rg3-JZyuj41simZmFc67_FMGtnafnI4_dnU9px")
                ;
    }

}