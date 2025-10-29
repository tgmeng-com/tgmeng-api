package com.tgmeng.common.enums.business;

import com.tgmeng.common.enums.enumcommon.INameValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * description: http请求的Origin
 * package: com.tgmeng.common.Enum
 * className: ForestRequestHeaderOriginEnum
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/6/29 18:17
*/
@Getter
@AllArgsConstructor
public enum ForestRequestHeaderOriginEnum implements INameValueEnum<String,String> {
    BILIBILI("BILIBILI", "https://www.bilibili.com", "", true,1),
    BAIDU("BAIDU", "https://www.baidu.com", "", true,2),
    WEIBO("WEIBO", "https://www.weibo.com", "", true,3),
    DOUYIN("DOUYIN", "https://www.douyin.com", "", true,4),
    GITHUB("GITHUB", "https://api.github.com", "", true,4),
    YOUTUBE("YOUTUBE", "https://www.youtube.com", "", true,4),
    DOUBAN("DOUBAN", "https://www.douban.com", "", true,4),
    TENCENT("TENCENT", "https://news.qq.com", "", true,4),
    TOUTIAO("TOUTIAO", "https://www.toutiao.com", "", true,4),
    WANGYI("WANGYI", "https://wp.m.163.com", "", true,4),
    WANGYIYUN("WANGYIYUN", "https://music.163.com", "", true,4),
    BAIDUTIEBA("WANGYIYUN", "https://tieba.baidu.com", "", true,4),
    SHAOSHUPAI("SHAOSHUPAI", "https://sspai.com", "", true,4),
    HUGGING_FACE("HUGGING_FACE", "https://huggingface.co", "", true,4),
    ZHI_HU("ZHI_HU", "https://www.zhihu.com", "", true,4),
    TENG_XUN_SHI_PIN("TENG_XUN_SHI_PIN", "https://v.qq.com", "", true,4),
    AI_QI_YI("AI_QI_YI", "https://www.iqiyi.com", "", true,4),
    YOU_KU("YOU_KU", "https://acz.youku.com", "", true,4),
    MANG_GUO("MANG_GUO", "https://so.mgtv.com", "", true,4),
    MAO_YAN("MAO_YAN", "https://m.maoyan.com/asgard/board/aggregation", "", true,4),
    JING_RONG_JIE("JING_RONG_JIE", "https://finance.jrj.com.cn", "", true,4),
    DI_YI_CAI_JING("DI_YI_CAI_JING", "https://www.yicai.com/brief", "", true,4),
    TONG_HUA_SHUN("TONG_HUA_SHUN", "https://news.10jqka.com.cn", "", true,4),
    HUA_ER_JIE_JIAN_WEN("HUA_ER_JIE_JIAN_WEN", "https://wallstreetcn.com", "",true,5),
    CAI_LIAN_SHE("CAI_LIAN_SHE", "https://www.cls.cn/detail/2179425", "",true,5),
    GE_LONG_HUI("GE_LONG_HUI", "https://www.google.com", "",true,5),
    FA_BU("FA_BU", "https://www.fastbull.com/cn", "",true,5),
    JIN_SHI("JIN_SHI", "https://www.jin10.com", "",true,5),
    NEW_YUE_SHI_BAO("NEW_YUE_SHI_BAO", "https://m.cn.nytimes.com", "",true,5),
    BBC("BBC", "https://www.bbc.com", "",true,5),
    FA_GUANG("FA_GUANG", "https://www.rfi.fr", "",true,5),
    HUA_ER_JIE_RI_BAO("HUA_ER_JIE_RI_BAO", "https://cn.wsj.com/zh-hans/news/china?mod=nav_top_section", "",true,5),
    DA_JI_YUAN("DA_JI_YUAN", "https://www.epochtimes.com/gb/instant-news.htm", "",true,5),
    WO_SHI_PM("WO_SHI_PM", "https://www.woshipm.com", "",true,5),
    YOU_SHE_WANG("YOU_SHE_WANG", "https://hot.uisdc.com", "",true,5),
    ZHAN_KU("ZHAN_KU", "https://www.zcool.com.cn", "",true,5),
    TU_YA_WANG_GUO("TU_YA_WANG_GUO", "https://www.gracg.com", "",true,5),
    SHE_JI_DA_REN("SHE_JI_DA_REN", "https://www.shejidaren.com", "",true,5),
    TOPYS("TOPYS", "https://www.topys.cn", "",true,5),
    ARCH_DAILY("ARCH_DAILY", "https://www.archdaily.cn", "",true,5),
    DRIBBBLE("DRIBBBLE", "https://dribbble.com", "",true,5),
    AWWWARDS("AWWWARDS", "https://www.behance.net", "",true,5),
    CORE77("CORE77", "https://www.core77.com", "",true,5),
    ABDUZEEDO("ABDUZEEDO", "https://abduzeedo.com", "",true,5),
    MIT("MIT", "https://www.mittrchina.com", "",true,5),
    ZHONG_GUO_KE_XUE_YUAN("ZHONG_GUO_KE_XUE_YUAN", "https://www.cas.cn", "",true,5),
    EUREK_ALERT("EUREK_ALERT", "https://www.eurekalert.org", "",true,5),
    GUO_JI_KE_JI_CHUANG_XIN_ZHONG_XIN("GUO_JI_KE_JI_CHUANG_XIN_ZHONG_XIN", "https://www.ncsti.gov.cn/kjdt", "",true,5),
    JI_QI_ZHI_XIN("JI_QI_ZHI_XIN", "https://www.jiqizhixin.com", "",true,5);

    private final String key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
