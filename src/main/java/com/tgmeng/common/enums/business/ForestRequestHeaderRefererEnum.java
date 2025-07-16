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
    TENG_XUN_SHI_PIN("TENG_XUN_SHI_PIN", "https://v.qq.com/", "", true,5);

    private final String key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
