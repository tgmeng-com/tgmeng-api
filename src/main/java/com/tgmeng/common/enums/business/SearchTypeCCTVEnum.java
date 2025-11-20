package com.tgmeng.common.enums.business;

import com.tgmeng.common.enums.enumcommon.INameValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * description: 这个优酷排行榜url后缀路由用的
 *  其中value用来路由
 *  description用来日志打印，同时他也是返回前端的card框的分类名称。但是由于card现在是在前端弄的，没用后端的，所以这里目前只用来打印日志
 * package: com.tgmeng.common.Enum
 * className: ForestRequestHeaderOriginEnum
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/6/29 18:17
 */
@Getter
@AllArgsConstructor
public enum SearchTypeCCTVEnum implements INameValueEnum<String, String> {
    CCTV1("1", "1", "CCTV1 综合", true, 2),
    CCTV2("2", "2", "CCTV2 财经", true, 2),
    CCTV3("3", "3", "CCTV3 综艺", true, 2),
    CCTV4("4", "4", "CCTV4 亚洲", true, 2),
    CCTV5("5", "5", "CCTV5 体育", true, 2),
    CCTV6("6", "6", "CCTV6 电影", true, 2),
    CCTV7("7", "7", "CCTV7 国防教育", true, 2),
    CCTV8("8", "8", "CCTV8 电视剧", true, 2),
    CCTV9("jilu", "jilu", "CCTV9 纪录", true, 2),
    CCTV10("10", "10", "CCTV10 科教", true, 2),
    CCTV11("11", "11", "CCTV11 戏曲", true, 2),
    CCTV12("12", "12", "CCTV12 社会与法", true, 2),
    CCTV13("13", "13", "CCTV13 新闻", true, 2),
    CCTV14("child", "child", "CCTV14 少儿", true, 2),
    CCTV15("15", "15", "CCTV15 音乐", true, 2),
    CCTV5_PLUS("5plus", "5plus", "CCTV5 体育赛事", true, 2),
    CCTV16("16", "16", "CCTV16 奥林匹克", true, 2),
    CCTV17("17", "17", "CCTV17 农业与村", true, 2),
    CCTV4_EUROPE("europe", "europe", "CCTV4 欧洲", true, 2),
    CCTV4_AMERICA("america", "america", "CCTV4 美洲", true, 2);


    private final String key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
