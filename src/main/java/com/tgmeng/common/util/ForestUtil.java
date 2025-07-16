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
                //.setConnection("keep-alive")
                .setAcceptLanguage("zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7")
                .setXForwardedFor("114.114.114.114")
                .setReferer(ForestRequestHeaderRefererEnum.YOU_KU.getValue())
                .setOrigin(ForestRequestHeaderOriginEnum.YOU_KU.getValue())
                .setCookie("isI18n=false; __ysuid=1751425904161Ock; __ayft=1752626298446; __aysid=1752626298446T67; __ayscnt=1; xlly_s=1; mtop_partitioned_detect=1; HISTORY_KEY=%5B%22%E6%8E%92%E8%A1%8C%E6%A6%9C%22%2C%22%E6%8E%92%E8%A1%8C%22%2C%22%E8%97%8F%E6%B5%B7%E4%BC%A0%22%2C%22%E5%87%A1%E4%BA%BA%E4%BF%AE%E4%BB%99%E4%BC%A0%E5%A5%87%22%5D; __ayvstp=2; __aysvstp=2; login_index=3_1752686273279; tfstk=gwxxvyZm2bc0QmxAEKuuItxitlHlH4vVFIJQj1f05QdJp_2citA0fUd6aZffIm5_BQ9FGKcZ08YyMFB1gfgfP3Rv_l_ql-tWyIJCoGx9QlB6CCkVsxuk3KSNfXc3XDv23Suq8thlfA95jOQLfiuH3KSaU8Vs-U94BpayFGs61g15Its_5O_seg6FdGZf5oa7N_55frZ_c065B91_fCsseL1PCG161ZMJFjdSM_efslLvkKMPCbXzfltRHagkh_ZGe3BAk69XDlZ_RtQAOK1SGzDFha9NWhyaXw9X8CWBGSGO_3pWceOIZrfXJOpHWKibVix2he_6Afz69idORn_8CApcXESRFHMg8_xRiCtAPvqdSgthRiTozjbGD9d60IeTfB9kKn7yXXF5_EXFVOKZdPCO5g5MxHdWtWfdIrM-ePzNlTowwPWj4FrvBTCnHiUa7akPe6D-RPa_1Y6RtxEa7P7yv; _m_h5_tk=bd4624bdd87545a6b1039d3ab8c6a74c_1752694137607; _m_h5_tk_enc=a99bdb562e9540b22718929c0401cc72; __arpvid=1752689278690Xw3ULJ-1752689278697; __aypstp=45; __ayspstp=45; isg=BH9_ApypK8E3by_4UU-CqaYZDlMJZNMGU_O5ERFMFy51IJ-iGTU6VvxzYvDeeKt-")
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
}