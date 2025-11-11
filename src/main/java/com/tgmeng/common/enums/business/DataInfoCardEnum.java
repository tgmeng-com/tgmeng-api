package com.tgmeng.common.enums.business;

import com.tgmeng.common.enums.enumcommon.INameValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * description: 数据卡片对应的一些信息，包括分类，logo啥的
 *
 * 这个枚举里的所有数据，目前前台都是用不到的，写着只是为了后续扩展和日志台的信息记录
 *
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
    BILIBILI("B站", "https://r2-trend.tgmeng.com/tgmeng-trend/bilibili.png", "媒体", true,1),
    BAIDU("百度", "https://r2-trend.tgmeng.com/tgmeng-trend/baidu.png", "新闻", true,2),
    WEIBO("微博", "https://r2-trend.tgmeng.com/tgmeng-trend/weibo.png", "社交", true,3),
    DOU_YIN("抖音", "https://r2-trend.tgmeng.com/tgmeng-trend/douyin.png", "媒体", true,4),
    GITHUB_ALL_STAR("GitHub Star总榜", "https://r2-trend.tgmeng.com/tgmeng-trend/github.png", "GitHub", true,5),
    GITHUB_DAY_STAR("近一日新仓库Star榜", "https://r2-trend.tgmeng.com/tgmeng-trend/github.png", "GitHub", true,5),
    GITHUB_WEEK_STAR("近一周新仓库Star榜", "https://r2-trend.tgmeng.com/tgmeng-trend/github.png", "GitHub", true,5),
    GITHUB_MONTH_STAR("近一月新仓库Star榜", "https://r2-trend.tgmeng.com/tgmeng-trend/github.png", "GitHub", true,5),
    GITHUB_YEAR_STAR("近一年新仓库Star榜", "https://r2-trend.tgmeng.com/tgmeng-trend/github.png", "GitHub", true,5),
    GITHUB_THREE_YEAR_STAR("近三年新仓库Star榜", "https://r2-trend.tgmeng.com/tgmeng-trend/github.png", "GitHub", true,5),
    GITHUB_FIVE_YEAR_STAR("近五年新仓库Star榜", "https://r2-trend.tgmeng.com/tgmeng-trend/github.png", "GitHub", true,5),
    GITHUB_TEN_YEAR_STAR("近十年新仓库Star榜", "https://r2-trend.tgmeng.com/tgmeng-trend/github.png", "GitHub", true,5),
    YOUTUBE("Youtube", "https://r2-trend.tgmeng.com/tgmeng-trend/youtube.png", "媒体", true,5),
    DOU_BAN("豆瓣", "https://r2-trend.tgmeng.com/tgmeng-trend/douban.png", "社交", true,5),
    TENCENT("腾讯", "https://r2-trend.tgmeng.com/tgmeng-trend/tencent.png", "新闻", true,5),
    TOU_TIAO("头条", "https://r2-trend.tgmeng.com/tgmeng-trend/toutiao.png", "新闻", true,5),
    WANG_YI("网易", "https://r2-trend.tgmeng.com/tgmeng-trend/wangyi.png", "新闻", true,5),
    WANG_YI_YUN("网易云", "https://r2-trend.tgmeng.com/tgmeng-trend/wangyiyun.png", "媒体", true,5),
    BAI_DU_TIE_BA("百度贴吧", "https://r2-trend.tgmeng.com/tgmeng-trend/baidutieba.png", "社交", true,5),
    SHAO_SHU_PAI("少数派", "https://r2-trend.tgmeng.com/tgmeng-trend/shaoshupai.png", "社交", true,5),
    HUGGING_FACE("HuggingFace", "https://r2-trend.tgmeng.com/tgmeng-trend/huggingface.png", "IT", true,5),
    ZHI_HU("知乎", "https://r2-trend.tgmeng.com/tgmeng-trend/zhihu.png", "IT", true,5),
    TENG_XUN_SHI_PIN("腾讯视频", "https://r2-trend.tgmeng.com/tgmeng-trend/teng-xun-shi-pin.png", "影视", true,5),
    AI_QI_YI("爱奇艺", "https://r2-trend.tgmeng.com/tgmeng-trend/ai-qi-yi.png", "影视", true,5),
    YOU_KU("优酷", "https://r2-trend.tgmeng.com/tgmeng-trend/you-ku.png", "影视", true,5),
    MANG_GUO("芒果", "https://r2-trend.tgmeng.com/tgmeng-trend/mang-guo.png", "影视", true,5),
    JING_RONG_JIE("金融界", "https://r2-trend.tgmeng.com/tgmeng-trend/jin-rong-jie.png", "财经", true,5),
    DI_YI_CAI_JING("第一财经", "https://r2-trend.tgmeng.com/tgmeng-trend/di-yi-cai-jing.png", "财经", true,5),
    TONG_HUA_SHUN("同花顺", "https://r2-trend.tgmeng.com/tgmeng-trend/tong-hua-shun.png", "财经", true,5),
    HUA_ER_JIE_JIAN_WEN("华尔街见闻", "https://r2-trend.tgmeng.com/tgmeng-trend/hua-er-jie-jian-wen.png", "财经", true,5),
    CAI_LIAN_SHE("财联社", "https://r2-trend.tgmeng.com/tgmeng-trend/cai-lian-she.png", "财经", true,5),
    GE_LONG_HUI("格隆汇", "https://r2-trend.tgmeng.com/tgmeng-trend/ge-long-hui.png", "财经", true,5),
    FA_BU("法布", "https://r2-trend.tgmeng.com/tgmeng-trend/fa-bu.png", "财经", true,5),
    JIN_SHI("金十", "https://r2-trend.tgmeng.com/tgmeng-trend/jin-shi.png", "财经", true,5),
    NEW_YUE_SHI_BAO("纽约时报", "https://r2-trend.tgmeng.com/tgmeng-trend/niu-yue-shi-bao.png", "新闻", true,5),
    BBC("BBC", "https://r2-trend.tgmeng.com/tgmeng-trend/bbc.png", "新闻", true,5),
    FA_GUANG("法广", "https://r2-trend.tgmeng.com/tgmeng-trend/fa-guang.png", "新闻", true,5),
    HUA_ER_JIE_RI_BAO("华尔街日报", "https://r2-trend.tgmeng.com/tgmeng-trend/hua-er-jie-ri-bao.png", "新闻", true,5),
    DA_JI_YUAN("大纪元", "https://r2-trend.tgmeng.com/tgmeng-trend/da-ji-yuan.png", "新闻", true,5),
    WO_SHI_PM("人人都是产品经理", "https://r2-trend.tgmeng.com/tgmeng-trend/wo-shi-pm.png", "设计", true,5),
    YOU_SHE_WANG("优设网", "https://r2-trend.tgmeng.com/tgmeng-trend/you-she-wang.png", "设计", true,5),
    ZHAN_KU("站酷", "https://r2-trend.tgmeng.com/tgmeng-trend/zhan-ku.png", "设计", true,5),
    TU_YA_WANG_GUO("涂鸦王国", "https://r2-trend.tgmeng.com/tgmeng-trend/tu-ya-wang-guo.png", "设计", true,5),
    SHE_JI_DA_REN("设计达人", "https://r2-trend.tgmeng.com/tgmeng-trend/she-ji-da-ren.png", "设计", true,5),
    TOPYS("TOPYS", "https://r2-trend.tgmeng.com/tgmeng-trend/she-ji-da-ren.png", "设计", true,5),
    ARCH_DAILY("ArchDaily", "https://r2-trend.tgmeng.com/tgmeng-trend/she-ji-da-ren.png", "设计", true,5),
    DRIBBBLE("Dribbble", "https://r2-trend.tgmeng.com/tgmeng-trend/she-ji-da-ren.png", "设计", true,5),
    AWWWARDS("Awwwards", "https://r2-trend.tgmeng.com/tgmeng-trend/she-ji-da-ren.png", "设计", true,5),
    CORE77("Core77", "https://r2-trend.tgmeng.com/tgmeng-trend/she-ji-da-ren.png", "设计", true,5),
    ABDUZEEDO("Abduzeedo", "https://r2-trend.tgmeng.com/tgmeng-trend/she-ji-da-ren.png", "设计", true,5),
    MIT("MIT", "https://r2-trend.tgmeng.com/tgmeng-trend/she-ji-da-ren.png", "设计", true,5),
    ZHONG_GUO_KE_XUE_YUAN("中国科学院", "https://r2-trend.tgmeng.com/tgmeng-trend/she-ji-da-ren.png", "科技", true,5),
    EUREK_ALERT("EurekAlert", "https://r2-trend.tgmeng.com/tgmeng-trend/she-ji-da-ren.png", "科技", true,5),
    GUO_JI_KE_JI_CHUANG_XIN_ZHONG_XIN("国际科技创新中心", "https://r2-trend.tgmeng.com/tgmeng-trend/she-ji-da-ren.png", "科技", true,5),
    JI_QI_ZHI_XIN("机器之心", "https://r2-trend.tgmeng.com/tgmeng-trend/she-ji-da-ren.png", "科技", true,5),
    HU_PU("机器之心", "https://r2-trend.tgmeng.com/tgmeng-trend/she-ji-da-ren.png", "体育", true,5),
    DONG_QIU_DI("懂球帝", "https://r2-trend.tgmeng.com/tgmeng-trend/she-ji-da-ren.png", "体育", true,5),
    XIN_LANG_TI_YU("新浪体育", "https://r2-trend.tgmeng.com/tgmeng-trend/she-ji-da-ren.png", "体育", true,5),
    SOU_HU_TI_YU("搜狐体育", "https://r2-trend.tgmeng.com/tgmeng-trend/she-ji-da-ren.png", "体育", true,5),
    WANG_YI_TI_YU("网易体育", "https://r2-trend.tgmeng.com/tgmeng-trend/she-ji-da-ren.png", "体育", true,5),
    YANG_SHI_TI_YU("央视体育", "https://r2-trend.tgmeng.com/tgmeng-trend/she-ji-da-ren.png", "体育", true,5),
    PP_TI_YU("PP体育", "https://r2-trend.tgmeng.com/tgmeng-trend/she-ji-da-ren.png", "体育", true,5),
    ZHI_BO_BA("直播吧", "https://r2-trend.tgmeng.com/tgmeng-trend/she-ji-da-ren.png", "体育", true,5),
    V2EX("V2EX", "https://r2-trend.tgmeng.com/tgmeng-trend/she-ji-da-ren.png", "社区", true,5),
    BU_XING_JIE_HU_PU("步行街虎扑", "https://r2-trend.tgmeng.com/tgmeng-trend/she-ji-da-ren.png", "社区", true,5),
    NGA("NGA", "https://r2-trend.tgmeng.com/tgmeng-trend/she-ji-da-ren.png", "社区", true,5),
    YI_MU_SAN_FEN_DI("一亩三分地", "https://r2-trend.tgmeng.com/tgmeng-trend/she-ji-da-ren.png", "社区", true,5),
    WEN_ZHANG_JUE_JIN("掘金文章", "https://r2-trend.tgmeng.com/tgmeng-trend/she-ji-da-ren.png", "社区", true,5),
    HACKER_NEWS("HACKER_NEWS", "https://r2-trend.tgmeng.com/tgmeng-trend/she-ji-da-ren.png", "社区", true,5),
    XIAO_ZU_DOU_BAN("小组豆瓣", "https://r2-trend.tgmeng.com/tgmeng-trend/she-ji-da-ren.png", "羊毛", true,5);



    /** 这里key用作平台名称了，论枚举的灵活性，哈哈哈 */
    private final String key;
    private final String value;
    /** 这个里面的描述，我用作分类了 */
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
