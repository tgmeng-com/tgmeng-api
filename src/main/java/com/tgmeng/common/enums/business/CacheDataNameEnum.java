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
    // TODO 每次新增平台后，这里添加缓存名称，用于缓存数据
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
    CACHE_TOP_SEARCH_TOP_DA_JI_YUAN("CACHE_TOP_SEARCH_TOP_DA_JI_YUAN", "/topsearch/dajiyuan", "", true,4),
    CACHE_TOP_SEARCH_TOP_WO_SHI_PM("CACHE_TOP_SEARCH_TOP_WO_SHI_PM", "/topsearch/woshipm", "", true,4),
    CACHE_TOP_SEARCH_TOP_YOU_SHE_WANG("CACHE_TOP_SEARCH_TOP_YOU_SHE_WANG", "/topsearch/youshewang", "", true,4),

    CACHE_TOP_SEARCH_TOP_QIAN_LI_BANG_ZHAN_KU("CACHE_TOP_SEARCH_TOP_QIAN_LI_BANG_ZHAN_KU", "/topsearch/qianlibangzhanku", "", true,4),
    CACHE_TOP_SEARCH_TOP_WEN_ZHANG_BANG_ZHAN_KU("CACHE_TOP_SEARCH_TOP_WEN_ZHANG_BANG_ZHAN_KU", "/topsearch/wenzhangbangzhanku", "", true,4),
    CACHE_TOP_SEARCH_TOP_ZUO_PIN_BANG_ZHAN_KU("CACHE_TOP_SEARCH_TOP_ZUO_PIN_BANG_ZHAN_KU", "/topsearch/zuopinbangzhanku", "", true,4),

    CACHE_TOP_SEARCH_TOP_RE_MEN_ZUO_PIN_TU_YA_WANG_GUO("CACHE_TOP_SEARCH_TOP_RE_MEN_ZUO_PIN_TU_YA_WANG_GUO", "/topsearch/remenzuopintuyawangguo", "", true,4),
    CACHE_TOP_SEARCH_TOP_JING_XUAN_ZUO_PIN_TU_YA_WANG_GUO("CACHE_TOP_SEARCH_TOP_JING_XUAN_ZUO_PIN_TU_YA_WANG_GUO", "/topsearch/jingxuanzuopintuyawangguo", "", true,4),
    CACHE_TOP_SEARCH_TOP_FA_XIAN_XIN_ZUO_TU_YA_WANG_GUO("CACHE_TOP_SEARCH_TOP_FA_XIAN_XIN_ZUO_TU_YA_WANG_GUO", "/topsearch/jinrixinzuotuyawangguo", "", true,4),
    CACHE_TOP_SEARCH_TOP_JIN_RI_XIN_ZUO_TU_YA_WANG_GUO("CACHE_TOP_SEARCH_TOP_JIN_RI_XIN_ZUO_TU_YA_WANG_GUO", "/topsearch/faxianxinzuotuyawangguo", "", true,4),
    CACHE_TOP_SEARCH_TOP_SHE_JI_DA_REN("CACHE_TOP_SEARCH_TOP_SHE_JI_DA_REN", "/topsearch/shejidaren", "", true,4),
    CACHE_TOP_SEARCH_TOP_TOPYS("CACHE_TOP_SEARCH_TOP_TOPYS", "/topsearch/topys", "", true,4),
    CACHE_TOP_SEARCH_TOP_ARCH_DAILY("CACHE_TOP_SEARCH_TOP_ARCH_DAILY", "/topsearch/archdaily", "", true,4),
    CACHE_TOP_SEARCH_TOP_DRIBBBLE("CACHE_TOP_SEARCH_TOP_DRIBBBLE", "/topsearch/dribbble", "", true,4),
    CACHE_TOP_SEARCH_TOP_AWWWARDS("CACHE_TOP_SEARCH_TOP_AWWWARDS", "/topsearch/awwwards", "", true,4),
    CACHE_TOP_SEARCH_TOP_CORE77("CACHE_TOP_SEARCH_TOP_CORE77", "/topsearch/core77", "", true,4),
    CACHE_TOP_SEARCH_TOP_ABDUZEEDO("CACHE_TOP_SEARCH_TOP_ABDUZEEDO", "/topsearch/abduzeedo", "", true,4),
    CACHE_TOP_SEARCH_TOP_MIT("CACHE_TOP_SEARCH_TOP_MIT", "/topsearch/mit", "", true,4),
    CACHE_TOP_SEARCH_TOP_ZHONG_GUO_KE_XUE_YUAN("CACHE_TOP_SEARCH_TOP_ZHONG_GUO_KE_XUE_YUAN", "/topsearch/zhongguokexueyuan", "", true,4),
    CACHE_TOP_SEARCH_TOP_EUREK_ALERT("CACHE_TOP_SEARCH_TOP_EUREK_ALERT", "/topsearch/eurekalert", "", true,4),
    CACHE_TOP_SEARCH_TOP_REN_GONG_ZHI_NENG_GUO_JI_KE_JI_CHUANG_XIN_ZHONG_XIN("CACHE_TOP_SEARCH_TOP_REN_GONG_ZHI_NENG_GUO_JI_KE_JI_CHUANG_XIN_ZHONG_XIN", "/topsearch/rengongzhinengguojikejichuangxinzhongxin", "", true,4),
    CACHE_TOP_SEARCH_TOP_YI_YAO_JIAN_KANG_GUO_JI_KE_JI_CHUANG_XIN_ZHONG_XIN("CACHE_TOP_SEARCH_TOP_YI_YAO_JIAN_KANG_GUO_JI_KE_JI_CHUANG_XIN_ZHONG_XIN", "/topsearch/yiyaojiankangguojikejichuangxinzhongxin", "", true,4),
    CACHE_TOP_SEARCH_TOP_JI_QI_ZHI_XIN("CACHE_TOP_SEARCH_TOP_JI_QI_ZHI_XIN", "/topsearch/jiqizhixin", "", true,4),
    CACHE_TOP_SEARCH_TOP_HU_PU("CACHE_TOP_SEARCH_TOP_HU_PU", "/topsearch/hupu", "", true,4),
    CACHE_TOP_SEARCH_TOP_DONG_QIU_DI("CACHE_TOP_SEARCH_TOP_DONG_QIU_DI", "/topsearch/dongqiudi", "", true,4),
    CACHE_TOP_SEARCH_TOP_XIN_LANG_TI_YU("CACHE_TOP_SEARCH_TOP_XIN_LANG_TI_YU", "/topsearch/xinlangtiyu", "", true,4),
    CACHE_TOP_SEARCH_TOP_SOU_HU_TI_YU("CACHE_TOP_SEARCH_TOP_SOU_HU_TI_YU", "/topsearch/souhutiyu", "", true,4),
    CACHE_TOP_SEARCH_TOP_TI_YU_WANG_YI("CACHE_TOP_SEARCH_TOP_TI_YU_WANG_YI", "/topsearch/tiyuwangyi", "", true,4),
    CACHE_TOP_SEARCH_TOP_YANG_SHI_TI_YU("CACHE_TOP_SEARCH_TOP_YANG_SHI_TI_YU", "/topsearch/yangshitiyu", "", true,4),
    CACHE_TOP_SEARCH_TOP_PP_TI_YU("CACHE_TOP_SEARCH_TOP_PP_TI_YU", "/topsearch/pptiyu", "", true,4),
    CACHE_TOP_SEARCH_TOP_ZHI_BO_BA("CACHE_TOP_SEARCH_TOP_ZHI_BO_BA", "/topsearch/zhiboba", "", true,4),

    CACHE_TOP_SEARCH_TOP_V2EX("CACHE_TOP_SEARCH_TOP_V2EX", "/topsearch/v2ex", "", true,4),
    CACHE_TOP_SEARCH_TOP_BU_XING_JIE_HU_PU("CACHE_TOP_SEARCH_TOP_BU_XING_JIE_HU_PU", "/topsearch/buxingjiehupu", "", true,4),
    CACHE_TOP_SEARCH_TOP_NGA("CACHE_TOP_SEARCH_TOP_NGA", "/topsearch/nga", "", true,4),
    CACHE_TOP_SEARCH_TOP_YI_MU_SAN_FEN_DI("CACHE_TOP_SEARCH_TOP_YI_MU_SAN_FEN_DI", "/topsearch/yimusanfendi", "", true,4),
    CACHE_TOP_SEARCH_TOP_WEN_ZHANG_JUE_JIN("CACHE_TOP_SEARCH_TOP_WEN_ZHANG_JUE_JIN", "/topsearch/wenzhangjuejin", "", true,4),
    CACHE_TOP_SEARCH_TOP_HACKER_NEWS("CACHE_TOP_SEARCH_TOP_HACKER_NEWS", "/topsearch/hackernews", "", true,4),
    CACHE_TOP_SEARCH_TOP_MAI_ZU_DOU_BAN("CACHE_TOP_SEARCH_TOP_MAI_ZU_DOU_BAN", "/topsearch/maizudouban", "", true,4),
    CACHE_TOP_SEARCH_TOP_PIN_ZU_DOU_BAN("CACHE_TOP_SEARCH_TOP_PIN_ZU_DOU_BAN", "/topsearch/pinzudouban", "", true,4),
    CACHE_TOP_SEARCH_TOP_AI_MAO_SHENG_HUO_DOU_BAN("CACHE_TOP_SEARCH_TOP_AI_MAO_SHENG_HUO_DOU_BAN", "/topsearch/aimaoshenghuodouban", "", true,4),
    CACHE_TOP_SEARCH_TOP_AI_MAO_ZAO_PEN_DOU_BAN("CACHE_TOP_SEARCH_TOP_AI_MAO_ZAO_PEN_DOU_BAN", "/topsearch/aimaozaopendouban", "", true,4),
    CACHE_TOP_SEARCH_TOP_GOU_ZU_DOU_BAN("CACHE_TOP_SEARCH_TOP_GOU_ZU_DOU_BAN", "/topsearch/gouzudouban", "", true,4),

    CACHE_TOP_SEARCH_TOP_XIA_CHU_FANG_DOU_BAN("CACHE_TOP_SEARCH_TOP_XIA_CHU_FANG_DOU_BAN", "/topsearch/xiaozudouban/XIA_CHU_FANG_DOU_BAN", "", true,4),
    CACHE_TOP_SEARCH_TOP_JIE_MAO_YE_KE_AI_DOU_BAN("CACHE_TOP_SEARCH_TOP_JIE_MAO_YE_KE_AI_DOU_BAN", "/topsearch/xiaozudouban/JIE_MAO_YE_KE_AI_DOU_BAN", "", true,4),
    CACHE_TOP_SEARCH_TOP_WO_DE_CHENG_SHI_PAI_GEI_NI_KAN_DOU_BAN("CACHE_TOP_SEARCH_TOP_WO_DE_CHENG_SHI_PAI_GEI_NI_KAN_DOU_BAN", "/topsearch/xiaozudouban/WO_DE_CHENG_SHI_PAI_GEI_NI_KAN_DOU_BAN", "", true,4),
    CACHE_TOP_SEARCH_TOP_JIA_PIAN_TUI_JIAN_DOU_BAN("CACHE_TOP_SEARCH_TOP_JIA_PIAN_TUI_JIAN_DOU_BAN", "/topsearch/xiaozudouban/JIA_PIAN_TUI_JIAN_DOU_BAN", "", true,4),
    CACHE_TOP_SEARCH_TOP_SHE_CHU_MAI_FANG_GONG_JIN_HUI_DOU_BAN("CACHE_TOP_SEARCH_TOP_SHE_CHU_MAI_FANG_GONG_JIN_HUI_DOU_BAN", "/topsearch/xiaozudouban/SHE_CHU_MAI_FANG_GONG_JIN_HUI_DOU_BAN", "", true,4),
    CACHE_TOP_SEARCH_TOP_CUN_ZHUANG_AI_HAO_ZHE_DOU_BAN("CACHE_TOP_SEARCH_TOP_CUN_ZHUANG_AI_HAO_ZHE_DOU_BAN", "/topsearch/xiaozudouban/CUN_ZHUANG_AI_HAO_ZHE_DOU_BAN", "", true,4),
    CACHE_TOP_SEARCH_TOP_YOU_YI_DE_XIAO_CHUAN_DOU_BAN("CACHE_TOP_SEARCH_TOP_YOU_YI_DE_XIAO_CHUAN_DOU_BAN", "/topsearch/xiaozudouban/YOU_YI_DE_XIAO_CHUAN_DOU_BAN", "", true,4),
    CACHE_TOP_SEARCH_TOP_SHE_HUI_XING_SI_WANG_DOU_BAN("CACHE_TOP_SEARCH_TOP_SHE_HUI_XING_SI_WANG_DOU_BAN", "/topsearch/xiaozudouban/SHE_HUI_XING_SI_WANG_DOU_BAN", "", true,4),
    CACHE_TOP_SEARCH_TOP_TAI_TOU_KAN_SHU_DOU_BAN("CACHE_TOP_SEARCH_TOP_TAI_TOU_KAN_SHU_DOU_BAN", "/topsearch/xiaozudouban/TAI_TOU_KAN_SHU_DOU_BAN", "", true,4),
    CACHE_TOP_SEARCH_TOP_LAN_REN_SHENG_HUO_ZHI_BEI_DOU_BAN("CACHE_TOP_SEARCH_TOP_LAN_REN_SHENG_HUO_ZHI_BEI_DOU_BAN", "/topsearch/xiaozudouban/LAN_REN_SHENG_HUO_ZHI_BEI_DOU_BAN", "", true,4),
    CACHE_TOP_SEARCH_TOP_KE_AI_SHI_WU_FEN_XIANG_DOU_BAN("CACHE_TOP_SEARCH_TOP_KE_AI_SHI_WU_FEN_XIANG_DOU_BAN", "/topsearch/xiaozudouban/KE_AI_SHI_WU_FEN_XIANG_DOU_BAN", "", true,4),
    CACHE_TOP_SEARCH_TOP_JIN_TIAN_CHUAN_SHEN_ME_DOU_BAN("CACHE_TOP_SEARCH_TOP_JIN_TIAN_CHUAN_SHEN_ME_DOU_BAN", "/topsearch/xiaozudouban/JIN_TIAN_CHUAN_SHEN_ME_DOU_BAN", "", true,4),
    CACHE_TOP_SEARCH_TOP_XIAO_FEI_ZHU_YI_NI_XING_ZHE_DOU_BAN("CACHE_TOP_SEARCH_TOP_XIAO_FEI_ZHU_YI_NI_XING_ZHE_DOU_BAN", "/topsearch/xiaozudouban/XIAO_FEI_ZHU_YI_NI_XING_ZHE_DOU_BAN", "", true,4),
    CACHE_TOP_SEARCH_TOP_WO_MEN_DOU_BU_DONG_CHE_DOU_BAN("CACHE_TOP_SEARCH_TOP_WO_MEN_DOU_BU_DONG_CHE_DOU_BAN", "/topsearch/xiaozudouban/WO_MEN_DOU_BU_DONG_CHE_DOU_BAN", "", true,4),
    CACHE_TOP_SEARCH_TOP_WO_MEN_DOU_BU_DONG_REN_QING_SHI_GU_DOU_BAN("CACHE_TOP_SEARCH_TOP_WO_MEN_DOU_BU_DONG_REN_QING_SHI_GU_DOU_BAN", "/topsearch/xiaozudouban/WO_MEN_DOU_BU_DONG_REN_QING_SHI_GU_DOU_BAN", "", true,4),
    CACHE_TOP_SEARCH_TOP_DOU_BAN_NIAO_ZU_DOU_BAN("CACHE_TOP_SEARCH_TOP_DOU_BAN_NIAO_ZU_DOU_BAN", "/topsearch/xiaozudouban/DOU_BAN_NIAO_ZU_DOU_BAN", "", true,4),
    CACHE_TOP_SEARCH_TOP_REN_JIAN_QING_LV_GUAN_CHA_DOU_BAN("CACHE_TOP_SEARCH_TOP_REN_JIAN_QING_LV_GUAN_CHA_DOU_BAN", "/topsearch/xiaozudouban/REN_JIAN_QING_LV_GUAN_CHA_DOU_BAN", "", true,4),
    CACHE_TOP_SEARCH_TOP_ZHI_CHANG_TU_CAO_DA_HUI_DOU_BAN("CACHE_TOP_SEARCH_TOP_ZHI_CHANG_TU_CAO_DA_HUI_DOU_BAN", "/topsearch/xiaozudouban/ZHI_CHANG_TU_CAO_DA_HUI_DOU_BAN", "", true,4),
    CACHE_TOP_SEARCH_TOP_JIAO_SHI_DOU_BAN("CACHE_TOP_SEARCH_TOP_JIAO_SHI_DOU_BAN", "/topsearch/xiaozudouban/JIAO_SHI_DOU_BAN", "", true,4),
    CACHE_TOP_SEARCH_TOP_SHANG_BAN_ZHE_JIAN_SHI_DOU_BAN("CACHE_TOP_SEARCH_TOP_SHANG_BAN_ZHE_JIAN_SHI_DOU_BAN", "/topsearch/xiaozudouban/SHANG_BAN_ZHE_JIAN_SHI_DOU_BAN", "", true,4),


    CACHE_TOP_SEARCH_YOU_MIN_XING_KONG("CACHE_TOP_SEARCH_YOU_MIN_XING_KONG", "/topsearch/youminxingkong", "", true,4),
    CACHE_TOP_SEARCH_THREE_DM_GAME("CACHE_TOP_SEARCH_THREE_DM_GAME", "/topsearch/3dmgame", "", true,4),
    CACHE_TOP_SEARCH_A9VG("CACHE_TOP_SEARCH_A9VG", "/topsearch/a9vg", "", true,4),
    CACHE_TOP_SEARCH_YOU_XI_TUO_LUO("CACHE_TOP_SEARCH_YOU_XI_TUO_LUO", "/topsearch/youxituoluo", "", true,4),
    CACHE_TOP_SEARCH_IGN("CACHE_TOP_SEARCH_IGN", "/topsearch/ign", "", true,4),
    CACHE_TOP_SEARCH_GCORES("CACHE_TOP_SEARCH_GCORES", "/topsearch/gcores", "", true,4),
    CACHE_TOP_SEARCH_YOU_YAN_SHE("CACHE_TOP_SEARCH_YOU_YAN_SHE", "/topsearch/youyanshe", "", true,4),
    CACHE_TOP_SEARCH_YI_QI_YI_QI_SAN("CACHE_TOP_SEARCH_YI_QI_YI_QI_SAN", "/topsearch/17173", "", true,4),
    CACHE_TOP_SEARCH_YOU_XIA_WANG("CACHE_TOP_SEARCH_YOU_XIA_WANG", "/topsearch/youxiawang", "", true,4),

    SHENG_WU_GU("SHENG_WU_GU", "/topsearch/shengwugu", "", true,4),
    YI_YAO_MO_FANG("YI_YAO_MO_FANG", "/topsearch/yiyaomofang", "", true,4),
    DING_XIANG_YI_SHENG("DING_XIANG_YI_SHENG", "/topsearch/dingxiangyisheng", "", true,4),
    DING_XIANG_YUAN_SHU_QU("DING_XIANG_YUAN_SHU_QU", "/topsearch/dingxiangyuanshequ", "", true,4),
    SHENG_MING_SHI_BAO("SHENG_MING_SHI_BAO", "/topsearch/shengmingshibao", "", true,4),
    JIA_YI_DA_JIAN_KANG("JIA_YI_DA_JIAN_KANG", "/topsearch/jiayidajiankang", "", true,4),
    GUO_KE("GUO_KE", "/topsearch/guoke", "", true,4),
    JIAN_KANG_SHI_BAO_WANG("JIAN_KANG_SHI_BAO_WANG", "/topsearch/jiankangshibaowang", "", true,4),

    PENG_PAI_XIN_WEN("PENG_PAI_XIN_WEN", "/topsearch/pengpaixinwen", "", true,4),


    CCTV1("CCTV1", "/topsearch/cctv/1", "", true,4),
    CCTV2("CCTV2", "/topsearch/cctv/2", "", true,4),
    CCTV3("CCTV3", "/topsearch/cctv/3", "", true,4),
    CCTV4("CCTV4", "/topsearch/cctv/4", "", true,4),
    CCTV4_EUROPE("CCTV4_EUROPE", "/topsearch/cctv/europe", "", true,4),
    CCTV4_AMERICA("CCTV4_AMERICA", "/topsearch/cctv/america", "", true,4),
    CCTV5("CCTV5", "/topsearch/cctv/5", "", true,4),
    CCTV5_PLUS("CCTV5_PLUS", "/topsearch/cctv/5plus", "", true,4),
    CCTV6("CCTV6", "/topsearch/cctv/6", "", true,4),
    CCTV7("CCTV7", "/topsearch/cctv/7", "", true,4),
    CCTV8("CCTV8", "/topsearch/cctv/8", "", true,4),
    CCTV9("CCTV9", "/topsearch/cctv/jilu", "", true,4),
    CCTV10("CCTV10", "/topsearch/cctv/10", "", true,4),
    CCTV11("CCTV11", "/topsearch/cctv/11", "", true,4),
    CCTV12("CCTV12", "/topsearch/cctv/12", "", true,4),
    CCTV13("CCTV13", "/topsearch/cctv/13", "", true,4),
    CCTV14("CCTV14", "/topsearch/cctv/child", "", true,4),
    CCTV15("CCTV15", "/topsearch/cctv/15", "", true,4),
    CCTV16("CCTV16", "/topsearch/cctv/16", "", true,4),
    CCTV17("CCTV17", "/topsearch/cctv/17", "", true,4),
    WORD_CLOUD("WORD_CLOUD", "/cachesearch/wordcloud", "", true,4),
    REALTIME_SUMMARY("REALTIME_SUMMARY", "/cachesearch/realtimesummary", "", true,4);


    private final String key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
