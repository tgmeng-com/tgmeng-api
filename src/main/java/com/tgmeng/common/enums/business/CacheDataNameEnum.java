package com.tgmeng.common.enums.business;

import com.tgmeng.common.enums.enumcommon.INameValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * description: 缓存名称
 * package: com.tgmeng.common.enums.business
 * className: CacheDataNameEnum
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/7/1 14:45
*/
@Getter
@AllArgsConstructor
public enum CacheDataNameEnum implements INameValueEnum<String,String> {
    CACHE_TOP_SEARCH_BILIBILI("CACHE_TOP_SEARCH_BILIBILI", "/topsearch/bilibili", "b站数据缓存名称", true,1),
    CACHE_TOP_SEARCH_BAIDU("CACHE_TOP_SEARCH_BAIDU", "/topsearch/baidu", "百度数据缓存名称", true,2),
    CACHE_TOP_SEARCH_WEIBO("CACHE_TOP_SEARCH_WEIBO", "/topsearch/weibo", "微博数据缓存名称", true,3),
    CACHE_TOP_SEARCH_DOU_YIN("CACHE_TOP_SEARCH_DOU_YIN", "/topsearch/douyin", "抖音数据缓存名称", true,4),
    CACHE_TOP_SEARCH_GITHUB_ALL_STARS("CACHE_TOP_SEARCH_GITHUB_ALL_STARS", "/topsearch/github/allstars", "GITHUB全部Star数据缓存名称", true,4),
    CACHE_TOP_SEARCH_GITHUB_DAY_STARS("CACHE_TOP_SEARCH_GITHUB_DAY_STARS", "/topsearch/github/daystars", "GITHUB近一日Star数据缓存名称", true,4),
    CACHE_TOP_SEARCH_GITHUB_WEEK_STARS("CACHE_TOP_SEARCH_GITHUB_WEEK_STARS", "/topsearch/github/weekstars", "GITHUB近一周Star数据缓存名称", true,4),
    CACHE_TOP_SEARCH_GITHUB_MONTH_STARS("CACHE_TOP_SEARCH_GITHUB_MONTH_STARS", "/topsearch/github/monthstars", "GITHUB近一月Star数据缓存名称", true,4),
    CACHE_TOP_SEARCH_GITHUB_YEAR_STARS("CACHE_TOP_SEARCH_GITHUB_YEAR_STARS", "/topsearch/github/yearstars", "GITHUB近一年Star数据缓存名称", true,4),
    CACHE_TOP_SEARCH_GITHUB_THREE_YEAR_STARS("CACHE_TOP_SEARCH_GITHUB_THREE_YEAR_STARS", "/topsearch/github/threeyearstars", "GITHUB近三年Star数据缓存名称", true,4),
    CACHE_TOP_SEARCH_GITHUB_FIVE_YEAR_STARS("CACHE_TOP_SEARCH_GITHUB_FIVE_YEAR_STARS", "/topsearch/github/fiveyearstars", "GITHUB近五年Star数据缓存名称", true,4),
    CACHE_TOP_SEARCH_GITHUB_TEN_YEAR_STARS("CACHE_TOP_SEARCH_GITHUB_TEN_YEAR_STARS", "/topsearch/github/tenyearstars", "GITHUB近十年Star数据缓存名称", true,4),
    CACHE_TOP_SEARCH_YOUTUBE("CACHE_TOP_SEARCH_YOUTUBE", "/topsearch/global/youtube", "", true,4),
    CACHE_TOP_SEARCH_TIE_BA_BAIDU("CACHE_TOP_SEARCH_TIE_BA_BAIDU", "/topsearch/tiebabaidu", "", true,4),
    CACHE_TOP_SEARCH_DOU_BAN("CACHE_TOP_SEARCH_DOU_BAN", "/topsearch/douban", "", true,4),
    CACHE_TOP_SEARCH_TENCENT("CACHE_TOP_SEARCH_TENCENT", "/topsearch/tencent", "", true,4),
    CACHE_TOP_SEARCH_TOU_TIAO("CACHE_TOP_SEARCH_TOU_TIAO", "/topsearch/toutiao", "", true,4),
    CACHE_TOP_SEARCH_WANG_YI("CACHE_TOP_SEARCH_WANG_YI", "/topsearch/wangyi", "", true,4),
    CACHE_TOP_SEARCH_WANG_YI_YUN_BIAO_SHENG("CACHE_TOP_SEARCH_WANG_YI_YUN_BIAO_SHENG", "/topsearch/biaoshengwangyiyun", "", true,4),
    CACHE_TOP_SEARCH_WANG_YI_YUN_XIN_GE("CACHE_TOP_SEARCH_WANG_YI_YUN_XIN_GE", "/topsearch/xingegwangyiyun", "", true,4),
    CACHE_TOP_SEARCH_WANG_YI_YUN_YUAN_CHUANG("CACHE_TOP_SEARCH_WANG_YI_YUN_YUAN_CHUANG", "/topsearch/yuanchuangwangyiyun", "", true,4),
    CACHE_TOP_SEARCH_WANG_YI_YUN_RE_GE("CACHE_TOP_SEARCH_WANG_YI_YUN_RE_GE", "/topsearch/regewangyiyun", "", true,4),
    CACHE_TOP_SEARCH_SHAO_SHU_PAI("CACHE_TOP_SEARCH_SHAO_SHU_PAI", "/topsearch/shaoshupai", "", true,4),
    CACHE_TOP_SEARCH_DIAN_SHI_JU_BAIDU("CACHE_TOP_SEARCH_DIAN_SHI_JU_BAIDU", "/topsearch/dianshijubaidu", "", true,4),
    CACHE_TOP_SEARCH_DIAN_YING_BAIDU("CACHE_TOP_SEARCH_DIAN_YING_BAIDU", "/topsearch/dianyingbaidu", "", true,4),
    CACHE_TOP_SEARCH_XIAO_SHUO_BAIDU("CACHE_TOP_SEARCH_XIAO_SHUO_BAIDU", "/topsearch/xiaoshuobaidu", "", true,4),
    CACHE_TOP_SEARCH_YOU_XI_BAIDU("CACHE_TOP_SEARCH_YOU_XI_BAIDU", "/topsearch/youxibaidu", "", true,4),
    CACHE_TOP_SEARCH_QI_CHE_BAIDU("CACHE_TOP_SEARCH_QI_CHE_BAIDU", "/topsearch/qichebaidu", "", true,4),
    CACHE_TOP_SEARCH_RE_GENG_BAIDU("CACHE_TOP_SEARCH_RE_GENG_BAIDU", "/topsearch/regengbaidu", "", true,4),
    CACHE_TOP_SEARCH_CAI_JING_BAIDU("CACHE_TOP_SEARCH_CAI_JING_BAIDU", "/topsearch/caijingbaidu", "", true,4),
    CACHE_TOP_SEARCH_MIN_SHENG_BAIDU("CACHE_TOP_SEARCH_MIN_SHENG_BAIDU", "/topsearch/minshengbaidu", "", true,4),
    CACHE_TOP_SEARCH_HUGGING_FACE_SPACES_TRENDING("CACHE_TOP_SEARCH_HUGGING_FACE_SPACES_TRENDING", "/topsearch/global/huggingfacespacestrending", "", true,4),
    CACHE_TOP_SEARCH_HUGGING_FACE_SPACES_LIKES("CACHE_TOP_SEARCH_HUGGING_FACE_SPACES_LIKES", "/topsearch/global/huggingfacespaceslikes", "", true,4),
    CACHE_TOP_SEARCH_HUGGING_FACE_MODELS_TRENDING("CACHE_TOP_SEARCH_HUGGING_FACE_MODELS_TRENDING", "/topsearch/global/huggingfacemodelstrending", "", true,4),
    CACHE_TOP_SEARCH_HUGGING_FACE_MODELS_LIKES("CACHE_TOP_SEARCH_HUGGING_FACE_MODELS_LIKES", "/topsearch/global/huggingfacemodellikes", "", true,4),
    CACHE_TOP_SEARCH_HUGGING_FACE_DATASETS_TRENDING("CACHE_TOP_SEARCH_HUGGING_FACE_DATASETS_TRENDING", "/topsearch/global/huggingfacedatasetstrending", "", true,4),
    CACHE_TOP_SEARCH_HUGGING_FACE_DATASETS_LIKES("CACHE_TOP_SEARCH_HUGGING_FACE_DATASETS_LIKES", "/topsearch/global/huggingfacedatasetslikes", "", true,4),
    CACHE_TOP_SEARCH_ZHI_HU_DATASETS_LIKES("CACHE_TOP_SEARCH_ZHI_HU_DATASETS_LIKES", "/topsearch/zhihu", "", true,4),

    CACHE_TOP_SEARCH_DIAN_SHI_JU_TENG_XUN_SHI_PIN("CACHE_TOP_SEARCH_DIAN_SHI_JU_TENG_XUN_SHI_PIN", "/topsearch/dianshijutengxun", "", true,4),
    CACHE_TOP_SEARCH_DIAN_YING_TENG_XUN_SHI_PIN("CACHE_TOP_SEARCH_DIAN_YING_TENG_XUN_SHI_PIN", "/topsearch/dianyingtengxun", "", true,4),
    CACHE_TOP_SEARCH_DONG_MAN_TENG_XUN_SHI_PIN("CACHE_TOP_SEARCH_DONG_MAN_TENG_XUN_SHI_PIN", "/topsearch/dongmantengxun", "", true,4),
    CACHE_TOP_SEARCH_ZONG_YI_TENG_XUN_SHI_PIN("CACHE_TOP_SEARCH_ZONG_YI_TENG_XUN_SHI_PIN", "/topsearch/zongyitengxun", "", true,4),
    CACHE_TOP_SEARCH_ZONG_BANG_TENG_XUN_SHI_PIN("CACHE_TOP_SEARCH_ZONG_BANG_TENG_XUN_SHI_PIN", "/topsearch/zongbangtengxun", "", true,4),

    CACHE_TOP_SEARCH_DIAN_SHI_JU_AI_QI_YI("CACHE_TOP_SEARCH_DIAN_SHI_JU_AI_QI_YI", "/topsearch/dianshijuaiqiyi", "", true,4),
    CACHE_TOP_SEARCH_DIAN_YING_AI_QI_YI("CACHE_TOP_SEARCH_DIAN_YING_AI_QI_YI", "/topsearch/dianyingaiqiyi", "", true,4),
    CACHE_TOP_SEARCH_DONG_MAN_AI_QI_YI("CACHE_TOP_SEARCH_DONG_MAN_AI_QI_YI", "/topsearch/dongmanaiqiyi", "", true,4),
    CACHE_TOP_SEARCH_ZONG_YI_AI_QI_YI("CACHE_TOP_SEARCH_ZONG_YI_AI_QI_YI", "/topsearch/zongyiaiqiyi", "", true,4),
    CACHE_TOP_SEARCH_ZONG_BANG_AI_QI_YI("CACHE_TOP_SEARCH_ZONG_BANG_AI_QI_YI", "/topsearch/zongbangaiqiyi", "", true,4),

    CACHE_TOP_SEARCH_DIAN_SHI_JU_YOU_KU("CACHE_TOP_SEARCH_DIAN_SHI_JU_YOU_KU", "/topsearch/dianshijuyouku", "", true,4),
    CACHE_TOP_SEARCH_DIAN_YING_YOU_KU("CACHE_TOP_SEARCH_DIAN_YING_YOU_KU", "/topsearch/dianyingyouku", "", true,4),
    CACHE_TOP_SEARCH_DONG_MAN_YOU_KU("CACHE_TOP_SEARCH_DONG_MAN_YOU_KU", "/topsearch/dongmanyouku", "", true,4),
    CACHE_TOP_SEARCH_ZONG_YI_YOU_KU("CACHE_TOP_SEARCH_ZONG_YI_YOU_KU", "/topsearch/zongyiyouku", "", true,4),
    CACHE_TOP_SEARCH_ZONG_BANG_YOU_KU("CACHE_TOP_SEARCH_ZONG_BANG_YOU_KU", "/topsearch/zongbangyouku", "", true,4),

    CACHE_TOP_SEARCH_DIAN_SHI_JU_MANG_GUO("CACHE_TOP_SEARCH_DIAN_SHI_JU_MANG_GUO", "/topsearch/dianshijumangguo", "", true,4),
    CACHE_TOP_SEARCH_DIAN_YING_MANG_GUO("CACHE_TOP_SEARCH_DIAN_YING_MANG_GUO", "/topsearch/dianyingmangguo", "", true,4),
    CACHE_TOP_SEARCH_DONG_MAN_MANG_GUO("CACHE_TOP_SEARCH_DONG_MAN_MANG_GUO", "/topsearch/dongmanmangguo", "", true,4),
    CACHE_TOP_SEARCH_ZONG_YI_MANG_GUO("CACHE_TOP_SEARCH_ZONG_YI_MANG_GUO", "/topsearch/zongyimangguo", "", true,4),
    CACHE_TOP_SEARCH_ZONG_BANG_MANG_GUO("CACHE_TOP_SEARCH_ZONG_BANG_MANG_GUO", "/topsearch/zongbangmangguo", "", true,4),

    CACHE_TOP_SEARCH_ZHOU_PIAO_FANG_BANG_MAO_YAN("CACHE_TOP_SEARCH_ZHOU_PIAO_FANG_BANG_MAO_YAN", "/topsearch/zhoupiaofangbangmaoyan", "", true,4),
    CACHE_TOP_SEARCH_XIANG_KAN_BANG_MAO_YAN("CACHE_TOP_SEARCH_XIANG_KAN_BANG_MAO_YAN", "/topsearch/xiangkanbangmaoyan", "", true,4),
    CACHE_TOP_SEARCH_GOU_PIAO_PING_FEN_BANG_MAO_YAN("CACHE_TOP_SEARCH_GOU_PIAO_PING_FEN_BANG_MAO_YAN", "/topsearch/goupiaopingfenbangmaoyan", "", true,4),
    CACHE_TOP_SEARCH_TOP_100_MAO_YAN("CACHE_TOP_SEARCH_TOP_100_MAO_YAN", "/topsearch/top100maoyan", "", true,4),

    CACHE_TOP_SEARCH_TOP_JIN_RONG_JIE("CACHE_TOP_SEARCH_TOP_JIN_RONG_JIE", "/topsearch/jinrongjie", "", true,4),
    CACHE_TOP_SEARCH_TOP_DI_YI_CAI_JING("CACHE_TOP_SEARCH_TOP_DI_YI_CAI_JING", "/topsearch/diyicaijing", "", true,4),
    CACHE_TOP_SEARCH_TOP_TONG_HUA_SHUN("CACHE_TOP_SEARCH_TOP_TONG_HUA_SHUN", "/topsearch/tonghuashun", "", true,4),
    CACHE_TOP_SEARCH_TOP_HUA_ER_JIE_JIAN_WEN("CACHE_TOP_SEARCH_TOP_HUA_ER_JIE_JIAN_WEN", "/topsearch/huaerjiejianwen", "", true,4),
    CACHE_TOP_SEARCH_TOP_CAI_LIAN_SHE("CACHE_TOP_SEARCH_TOP_CAI_LIAN_SHE", "/topsearch/cailianshe", "", true,4),
    CACHE_TOP_SEARCH_TOP_GE_LONG_HUI("CACHE_TOP_SEARCH_TOP_GE_LONG_HUI", "/topsearch/gelonghui", "", true,4),
    CACHE_TOP_SEARCH_TOP_FA_BU("CACHE_TOP_SEARCH_TOP_FA_BU", "/topsearch/fabu", "", true,4),
    CACHE_TOP_SEARCH_TOP_JIN_SHI("CACHE_TOP_SEARCH_TOP_JIN_SHI", "/topsearch/jinshi", "", true,4),
    CACHE_TOP_SEARCH_TOP_NEW_YUE_SHI_BAO("CACHE_TOP_SEARCH_TOP_NEW_YUE_SHI_BAO", "/topsearch/niuyueshibao", "", true,4),
    CACHE_TOP_SEARCH_TOP_BBC("CACHE_TOP_SEARCH_TOP_BBC", "/topsearch/bbc", "", true,4),
    CACHE_TOP_SEARCH_TOP_FA_GUANG("CACHE_TOP_SEARCH_TOP_FA_GUANG", "/topsearch/faguang", "", true,4),
    CACHE_TOP_SEARCH_TOP_DA_JI_YUAN("CACHE_TOP_SEARCH_TOP_DA_JI_YUAN", "/topsearch/dajiyuan", "", true,4);

    private final String key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
