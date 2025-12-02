package com.tgmeng.common.enums.business;

import com.tgmeng.common.enums.enumcommon.INameValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * description:
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/6/29 18:17
*/
@Getter
@AllArgsConstructor
public enum SearchTypeMaoYanEnum implements INameValueEnum<String,String> {
    ZHOU_PIAO_FANG_BANG_MAO_YAN("zhoupiaofangbang", "56", "猫眼周票房榜", true,1),
    XIANG_KAN_BANG_MAO_YAN("xiangkanbang", "6", "猫眼想看榜单", true,2),
    GOU_PIAO_PING_FEN_BANG_MAO_YAN("goupiaopingfenbang", "55", "猫眼购票评分榜", true,2),
    TOP_100_MAO_YAN("top100", "39", "猫眼TOP100", true,2);

    private final String key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
