package com.tgmeng.common.enums.business;

import com.tgmeng.common.enums.enumcommon.INameValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * description: 数据卡片对应的一些信息，包括分类，logo啥的
 * package: com.tgmeng.common.enums.business
 * className: DataInfoCardEnum
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/7/1 18:15
*/
@Getter
@AllArgsConstructor
public enum DataInfoCardEnum implements INameValueEnum<String,String> {
    BILIBILI("B站", "https://r2-trend.tgmeng.com/tgmeng-trend/bilibili.png", "娱乐", true,1),
    BAIDU("百度", "https://r2-trend.tgmeng.com/tgmeng-trend/baidu.png", "新闻", true,2),
    WEIBO("微博", "https://r2-trend.tgmeng.com/tgmeng-trend/weibo.png", "影音", true,3),
    DOUYIN("抖音", "https://r2-trend.tgmeng.com/tgmeng-trend/douyin.png", "操蛋", true,4);

    /** 这里key用作平台名称了，论枚举的灵活性，哈哈哈 */
    private final String key;
    private final String value;
    /** 这个里面的描述，我用作分类了 */
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
