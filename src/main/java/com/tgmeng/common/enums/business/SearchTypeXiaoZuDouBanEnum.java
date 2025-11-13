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
public enum SearchTypeXiaoZuDouBanEnum implements INameValueEnum<String,String> {
    MAI_ZU_DOU_BAN("MAI_ZU_DOU_BAN", "698716", "买组 & All buy & 不买不可能", true,1),
    PIN_ZU_DOU_BAN("PIN_ZU_DOU_BAN", "536786", "拼组 / 双11 / 我跟你拼了！", true,2),
    AI_MAO_SHENG_HUO_DOU_BAN("AI_MAO_SHENG_HUO_DOU_BAN", "656297", "爱猫生活", true,2),
    AI_MAO_ZAO_PEN_DOU_BAN("AI_MAO_ZAO_PEN_DOU_BAN", "700687", "爱猫澡盆", true,2),
    GOU_ZU_DOU_BAN("GOU_ZU_DOU_BAN", "716166", "豆瓣狗组", true,2),

    XIA_CHU_FANG_DOU_BAN("XIA_CHU_FANG_DOU_BAN", "596337", "下厨房", true,2),
    JIE_MAO_YE_KE_AI_DOU_BAN("JIE_MAO_YE_KE_AI_DOU_BAN", "654121", "街猫也可爱", true,2),
    WO_DE_CHENG_SHI_PAI_GEI_NI_KAN_DOU_BAN("WO_DE_CHENG_SHI_PAI_GEI_NI_KAN_DOU_BAN", "644344", "我的城市拍给你看", true,2),
    JIA_PIAN_TUI_JIAN_DOU_BAN("JIA_PIAN_TUI_JIAN_DOU_BAN", "26104", "佳片推荐", true,2),
    SHE_CHU_MAI_FANG_GONG_JIN_HUI_DOU_BAN("SHE_CHU_MAI_FANG_GONG_JIN_HUI_DOU_BAN", "677158", "社畜买房共进会", true,2),
    CUN_ZHUANG_AI_HAO_ZHE_DOU_BAN("CUN_ZHUANG_AI_HAO_ZHE_DOU_BAN", "692275", "村庄爱好者", true,2),

    YOU_YI_DE_XIAO_CHUAN_DOU_BAN("YOU_YI_DE_XIAO_CHUAN_DOU_BAN", "725960", "友谊的小船", true,2),
    SHE_HUI_XING_SI_WANG_DOU_BAN("SHE_HUI_XING_SI_WANG_DOU_BAN", "687707", "社会性死亡", true,2),
    TAI_TOU_KAN_SHU_DOU_BAN("TAI_TOU_KAN_SHU_DOU_BAN", "650372", "抬头，看树！", true,2),
    LAN_REN_SHENG_HUO_ZHI_BEI_DOU_BAN("LAN_REN_SHENG_HUO_ZHI_BEI_DOU_BAN", "735707", "懒人生活指北", true,2),
    KE_AI_SHI_WU_FEN_XIANG_DOU_BAN("KE_AI_SHI_WU_FEN_XIANG_DOU_BAN", "648102", "可爱事物分享", true,2),

    JIN_TIAN_CHUAN_SHEN_ME_DOU_BAN("JIN_TIAN_CHUAN_SHEN_ME_DOU_BAN", "707669", "今天穿什么", true,2),
    XIAO_FEI_ZHU_YI_NI_XING_ZHE_DOU_BAN("XIAO_FEI_ZHU_YI_NI_XING_ZHE_DOU_BAN", "708794", "消费主义逆行者", true,2),
    WO_MEN_DOU_BU_DONG_CHE_DOU_BAN("WO_MEN_DOU_BU_DONG_CHE_DOU_BAN", "699221", "我们都不懂车", true,2),
    WO_MEN_DOU_BU_DONG_REN_QING_SHI_GU_DOU_BAN("WO_MEN_DOU_BU_DONG_REN_QING_SHI_GU_DOU_BAN", "751711", "我们都不懂人情世故", true,2),
    DOU_BAN_NIAO_ZU_DOU_BAN("DOU_BAN_NIAO_ZU_DOU_BAN", "721001", "豆瓣鸟组", true,2),

    REN_JIAN_QING_LV_GUAN_CHA_DOU_BAN("REN_JIAN_QING_LV_GUAN_CHA_DOU_BAN", "720659", "人间情侣观察", true,2),
    ZHI_CHANG_TU_CAO_DA_HUI_DOU_BAN("ZHI_CHANG_TU_CAO_DA_HUI_DOU_BAN", "13232", "职场吐槽大会", true,2),
    JIAO_SHI_DOU_BAN("JIAO_SHI_DOU_BAN", "596508", "教师", true,2),
    SHANG_BAN_ZHE_JIAN_SHI_DOU_BAN("SHANG_BAN_ZHE_JIAN_SHI_DOU_BAN", "22692", "上班这件事", true,2);

    private final String key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
