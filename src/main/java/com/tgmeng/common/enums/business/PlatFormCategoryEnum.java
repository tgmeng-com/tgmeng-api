package com.tgmeng.common.enums.business;

import com.tgmeng.common.enums.enumcommon.INameValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

// 这里是平台的二级分类，主要是为了过滤用
@Getter
@AllArgsConstructor
public enum PlatFormCategoryEnum implements INameValueEnum<String, String> {
    // 下面是一些单平台多榜单的，后续主要用在过滤榜单用，比如突发热点等
    BAI_DU("", "百度", "", true, 14),
    WANG_YI_YUN_YIN_YUE("", "网易云音乐", "", true, 15),
    GITHUB("", "GitHub", "", true, 14),
    HUGGING_FACES("", "HuggingFaces", "", true, 14),
    TENG_XUN_SHI_PIN("", "腾讯视频", "", true, 14),
    AI_QI_YI_SHI_PIN("", "爱奇艺视频", "", true, 14),
    MANG_GUO_SHI_PIN("", "芒果视频", "", true, 14),
    MAO_YAN("", "猫眼", "", true, 14),
    YOU_KU_SHI_PIN("", "优酷视频", "", true, 14),
    XIAO_ZU_DOU_BAN("", "小组豆瓣", "", true, 14),
    CCTV("", "CCTV", "", true, 14),
    TU_YA_WANG_GUO("", "涂鸦王国", "", true, 14),
    GUO_JI_KE_JI_CHUANG_XIN_ZHONG_XIN("", "国际科技创新中心", "", true, 14),
    ZHAN_KU("", "站酷", "", true, 14),
    QOOAPP("", "QooApp", "", true, 14),
    BA_HA_MU_TE("", "巴哈姆特", "", true, 14),
    FOUR_GAMER("", "4Gamer", "", true, 14),
    GAME_BASE("", "GameBase", "", true, 14),
    NODELOC("", "Nodeloc", "", true, 14),
    TGMENG_ALL("all", "糖果梦综合", "", true, 14),
    TGMENG_TECHNOLOGY("technology", "糖果梦科技", "", true, 14),
    TGMENG_FINANCE("finance", "糖果梦财经", "", true, 14),
    TGMENG_ENTERTAINMENT("entertainment", "糖果梦娱乐", "", true, 14),
    TGMENG_CAR("car", "糖果梦汽车", "", true, 14),
    TGMENG_SPORTS("sports", "糖果梦体育", "", true, 14),
    TGMENG_GAME("game", "糖果梦游戏", "", true, 14),
    TGMENG_LIVELIHOOD("livelihood", "糖果梦民生", "", true, 14);

    private final String key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
