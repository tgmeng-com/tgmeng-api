package com.tgmeng.common.enums.business;

import com.tgmeng.common.enums.enumcommon.INameValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * description: http请求的Referer
 * package: com.tgmeng.common.Enum
 * className: ForestRequestHeaderRefererEnum
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/6/29 18:17
*/
@Getter
@AllArgsConstructor
public enum ForestRequestHeaderRefererEnum  implements INameValueEnum<String,String> {
    BILIBILI("BILIBILI", "https://www.bilibili.com/", "", true,1),
    BAIDU("BAIDU", "https://www.baidu.com/", "", true,2),
    WEIBO("WEIBO", "https://www.weibo.com/", "", true,3),
    DOUYIN("DOUYIN", "https://www.douyin.com/", "", true,4),
    GITHUB("GITHUB", "https://api.github.com/", "", true,4),
    YOUTUBE("YOUTUBE", "https://www.youtube.com/", "", true,5),
    DOUBAN("DOUBAN", "https://www.douban.com/gallery/", "", true,5),
    TENCENT("TENCENT", "https://news.qq.com/", "", true,5),
    TOUTIAO("TOUTIAO", "https://www.toutiao.com/", "", true,5),
    WANGYI("WANGYI", "https://wp.m.163.com/", "", true,5),
    WANGYIYUN("WANGYIYUN", "https://music.163.com/", "", true,5),
    BAIDUTIEBA("BAIDUTIEBA", "https://tieba.baidu.com/", "", true,5),
    SHAOSHUPAI("SHAOSHUPAI", "https://sspai.com/", "", true,5),
    HUGGING_FACE("HUGGING_FACE", "https://huggingface.co/", "", true,5),
    ZHI_HU("ZHI_HU", "https://www.zhihu.com/", "", true,5),
    TENG_XUN_SHI_PIN("TENG_XUN_SHI_PIN", "https://v.qq.com/", "", true,5),
    AI_QI_YI("AI_QI_YI", "https://www.iqiyi.com/", "", true,5),
    YOU_KU("YOU_KU", "https://acz.youku.com/", "", true,5),
    MANG_GUO("MANG_GUO", "https://so.mgtv.com/", "", true,5),
    MAO_YAN("MAO_YAN", "https://m.maoyan.com/asgard/board/aggregation/", "", true,5),
    JING_RONG_JIE("JING_RONG_JIE", "https://finance.jrj.com.cn/", "", true,5),
    DI_YI_CAI_JING("DI_YI_CAI_JING", "https://www.yicai.com/brief/", "", true,5),
    TONG_HUA_SHUN("TONG_HUA_SHUN", "https://news.10jqka.com.cn/", "", true,5),
    HUA_ER_JIE_JIAN_WEN("HUA_ER_JIE_JIAN_WEN", "https://wallstreetcn.com/", "",true,5),
    CAI_LIAN_SHE("CAI_LIAN_SHE", "https://www.cls.cn/detail/2179425/", "",true,5),
    GE_LONG_HUI("GE_LONG_HUI", "https://www.google.com/", "",true,5),
    FA_BU("FA_BU", "https://www.fastbull.com/cn/", "",true,5),
    JIN_SHI("JIN_SHI", "https://www.jin10.com/", "",true,5);

    private final String key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
