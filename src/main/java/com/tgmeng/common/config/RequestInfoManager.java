package com.tgmeng.common.config;

import cn.hutool.core.util.ObjectUtil;
import com.tgmeng.common.enums.business.PlatFormCategoryEnum;
import com.tgmeng.common.enums.system.ResponseTypeEnum;
import com.tgmeng.common.exception.ServerException;
import com.tgmeng.common.forest.header.ForestRequestHeader;
import com.tgmeng.common.forest.httptype.ForestRequestTypeEnum;
import com.tgmeng.common.util.HttpRequestUtil;
import com.tgmeng.common.util.UserAgentGeneratorUtil;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RequestInfoManager {
    private final Map<String, PlatformConfig> configs = new HashMap<>();

    // ==================== 数据模型 ====================

    @Data
    @Builder
    public static class PlatformConfig {
        private String url;
        private String platformName;
        private String platformCategory;
        private String interfaceUrl;
        private ResponseTypeEnum responseType;
        @Builder.Default
        private String hotTitleUrlPrefix = "";
        @Builder.Default
        private String hotTitleUrlAfter = "";
        private String hotScoreUrlPrefix;
        @Builder.Default
        private Boolean hotTitleNeedDeal = false;
        @Builder.Default
        private Boolean hotTitleUrlNeedDeal = false;
        @Builder.Default
        private Boolean hotScoreNeedDeal = false;
        @Builder.Default
        private String platformLogo = "";
        @Builder.Default
        private ForestRequestTypeEnum requestType = ForestRequestTypeEnum.GET;
        @Builder.Default
        private Map<String, Object> jsonBody = new HashMap<>();
        @Builder.Default
        private Boolean jsonBodyNeedDeal = false;
        @Builder.Default
        private Map<String, Object> queryParams = new HashMap<>();
        private ForestRequestHeader forestRequestHeader;
        @Builder.Default
        private Boolean enabled = true;
        @Builder.Default
        private Integer timeoutSeconds = 10;
        @Builder.Default
        private Integer retryCount = 2;
        @Builder.Default
        private Boolean useProxy = false;
        @Builder.Default
        private String charset = "UTF-8";
        @Builder.Default
        private Integer rateLimitPerMinute = 60;
        private List<Selector> selectors;
    }

    @Data
    @Builder
    public static class Selector {
        private String root;
        @Builder.Default
        private String title = "";
        @Builder.Default
        private String url = "";
        @Builder.Default
        private String hotScore = "";
        @Builder.Default
        private String image = "";
        @Builder.Default
        private String author = "";
        @Builder.Default
        private String desc = "";
        @Builder.Default
        private String type = "";
        @Builder.Default
        private String publishTime = "";
        @Builder.Default
        private String commentCount = "";
        @Builder.Default
        private String startTime = "";
        @Builder.Default
        private String endTime = "";
        @Builder.Default
        private String showTime = "";
    }

    // ==================== 配置入口 ====================

    private FluentConfig addDomConfig(String url) {
        return new FluentConfig(url, this, ResponseTypeEnum.DOM);
    }

    private FluentConfig addJsonConfig(String url) {
        return new FluentConfig(url, this, ResponseTypeEnum.INTERFACE);
    }

    private FluentConfig addRssConfig(String url) {
        return new FluentConfig(url, this, ResponseTypeEnum.RSS);
    }


    // ==================== 万能构建器 ====================

    @lombok.experimental.Accessors(chain = true, fluent = true)
    private static class FluentConfig {
        private final RequestInfoManager manager;
        private final PlatformConfig.PlatformConfigBuilder config;
        private final List<Selector> selectors = new ArrayList<>();
        private Selector.SelectorBuilder currentSelector;

        public FluentConfig(String url, RequestInfoManager manager, ResponseTypeEnum type) {
            this.manager = manager;
            this.config = PlatformConfig.builder().url(url).responseType(type);
        }

        // ========== 平台配置方法 ==========
        public FluentConfig platformName(String v) {
            config.platformName(v);
            return this;
        }

        public FluentConfig platformCategory(String v) {
            config.platformCategory(v);
            return this;
        }

        public FluentConfig interfaceUrl(String v) {
            config.interfaceUrl(v);
            return this;
        }

        public FluentConfig platformLogo(String v) {
            config.platformLogo(v);
            return this;
        }

        public FluentConfig hotTitleUrlPrefix(String v) {
            config.hotTitleUrlPrefix(v);
            return this;
        }

        public FluentConfig hotTitleUrlAfter(String v) {
            config.hotTitleUrlAfter(v);
            return this;
        }

        public FluentConfig hotScoreUrlPrefix(String v) {
            config.hotScoreUrlPrefix(v);
            return this;
        }

        public FluentConfig hotTitleNeedDeal(boolean v) {
            config.hotTitleNeedDeal(v);
            return this;
        }

        public FluentConfig hotTitleUrlNeedDeal(Boolean v) {
            config.hotTitleUrlNeedDeal(v);
            return this;
        }

        public FluentConfig hotScoreNeedDeal(Boolean v) {
            config.hotScoreNeedDeal(v);
            return this;
        }

        public FluentConfig jsonBodyNeedDeal(Boolean v) {
            config.jsonBodyNeedDeal(v);
            return this;
        }

        public FluentConfig requestType(ForestRequestTypeEnum v) {
            config.requestType(v);
            return this;
        }

        public FluentConfig jsonBody(Map<String, Object> v) {
            config.jsonBody(v);
            return this;
        }

        public FluentConfig queryParams(Map<String, Object> v) {
            config.queryParams(v);
            return this;
        }

        public FluentConfig forestRequestHeader(ForestRequestHeader v) {
            config.forestRequestHeader(v);
            return this;
        }

        public FluentConfig enabled(Boolean v) {
            config.enabled(v);
            return this;
        }

        public FluentConfig timeoutSeconds(Integer v) {
            config.timeoutSeconds(v);
            return this;
        }

        public FluentConfig retryCount(Integer v) {
            config.retryCount(v);
            return this;
        }

        public FluentConfig useProxy(Boolean v) {
            config.useProxy(v);
            return this;
        }

        public FluentConfig charset(String v) {
            config.charset(v);
            return this;
        }

        public FluentConfig rateLimitPerMinute(Integer v) {
            config.rateLimitPerMinute(v);
            return this;
        }

        // ========== 选择器配置方法 ==========
        public FluentConfig addArea() {
            finishCurrentSelector();
            currentSelector = Selector.builder();
            return this;
        }

        public FluentConfig rootSelector(String v) {
            currentSelector.root(v);
            return this;
        }

        public FluentConfig titleSelector(String v) {
            currentSelector.title(v);
            return this;
        }

        public FluentConfig urlSelector(String v) {
            currentSelector.url(v);
            return this;
        }

        public FluentConfig hotScoreSelector(String v) {
            currentSelector.hotScore(v);
            return this;
        }

        public FluentConfig imageSelector(String v) {
            currentSelector.image(v);
            return this;
        }

        public FluentConfig authorSelector(String v) {
            currentSelector.author(v);
            return this;
        }

        public FluentConfig descSelector(String v) {
            currentSelector.desc(v);
            return this;
        }

        public FluentConfig typeSelector(String v) {
            currentSelector.type(v);
            return this;
        }

        public FluentConfig publishTimeSelector(String v) {
            currentSelector.publishTime(v);
            return this;
        }

        public FluentConfig commentCountSelector(String v) {
            currentSelector.commentCount(v);
            return this;
        }

        public FluentConfig startTimeSelector(String v) {
            currentSelector.startTime(v);
            return this;
        }

        public FluentConfig endTimeSelector(String v) {
            currentSelector.endTime(v);
            return this;
        }

        public FluentConfig showTimeSelector(String v) {
            currentSelector.showTime(v);
            return this;
        }

        private void finishCurrentSelector() {
            if (currentSelector != null) {
                selectors.add(currentSelector.build());
                currentSelector = null;
            }
        }

        public void register() {
            finishCurrentSelector();
            PlatformConfig built = config.build();
            config.forestRequestHeader(manager.getCommonRequestHeader(
                    built.getForestRequestHeader(), built.getUrl()));
            config.selectors(selectors);
            manager.configs.put(built.getUrl(), config.build());
        }
    }

    // ==================== 公共方法 ====================

    public Map<String, PlatformConfig> getConfigs() {
        return configs;
    }

    public RequestInfoManager() {
        initConfigs();
    }

    // 根据接口获取对应平台
    public PlatformConfig getPlatformConfigByInterfaceUrl(String interfaceUrl) {
        return configs.values().stream()
                .filter(config -> {
                    String template = config.getInterfaceUrl(); // 带 {xxx}
                    String regex = templateToRegex(template);
                    return interfaceUrl.matches(regex);
                })
                .findFirst()
                .orElseThrow(() -> new ServerException(
                        "未找到接口URL: " + interfaceUrl + " 对应的平台配置"
                ));
    }

    // 公共请求头
    public ForestRequestHeader getCommonRequestHeader(ForestRequestHeader forestRequestHeader, String url) {
        if (ObjectUtil.isEmpty(forestRequestHeader)) {
            return new ForestRequestHeader()
                    .setUserAgent(HttpRequestUtil.getRequestRandomUserAgent())
                    .setReferer(HttpRequestUtil.getRequestReferer(url))
                    .setOrigin(HttpRequestUtil.getRequestOrigin(url));
        } else {
            return forestRequestHeader;
        }
    }

    public static String templateToRegex(String template) {
        // 将 {xxx} 替换成 ([^/]+)
        String regex = template.replaceAll("\\{[^/]+?}", "([^/]+)");
        return "^" + regex + "$";
    }

    private void initConfigs() {
        // ========== DOM 解析平台 ========== TODO每次新加平台在这里加一下配置

        // 智通财经 - 单个区域
        addDomConfig("https://www.zhitongcaijing.com/content/recommend.html")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("智通财经")
                .platformCategory(PlatFormCategoryEnum.CAI_JING.getValue())
                .hotScoreUrlPrefix("https://www.zhitongcaijing.com")
                .interfaceUrl("/api/topsearch/zhitongcaijing")
                .addArea()
                .rootSelector("#news-article-box > .shadow-wrap-box")
                .titleSelector(".info-item-content-title a")
                .urlSelector(".info-item-content-title a")

                .register();

        // 示例：多个区域配置
        // addDomConfig("https://example.com/news")
        //         .platformName("示例网站")
        //         .platformCategory("新闻")
        //         .interfaceUrl("/api/topsearch/example")
        //         // 区域1：主要新闻
        //         .addArea()
        //             .rootSelector("#main-news > .news-item")
        //             .titleSelector(".title a")
        //             .urlSelector("a")
        //             .descSelector(".description")
        //             .hotScoreSelector(".hot-value")
        //             
        //         // 区域2：热门推荐
        //         .addArea()
        //             .rootSelector("#hot-recommend > .hot-item")
        //             .titleSelector(".hot-title")
        //             .urlSelector("a")
        //             .hotScoreSelector(".score")
        //             .imageSelector("img")
        //             
        //         // 区域3：编辑精选
        //         .addArea()
        //             .rootSelector(".editor-choice .article")
        //             .titleSelector("h3")
        //             .urlSelector("a")
        //             .authorSelector(".author")
        //             .publishTimeSelector(".time")
        //             
        //         .register();

        // ========== JSON 解析平台 ==========

        // 澎湃新闻
        addJsonConfig("https://cache.thepaper.cn/contentapi/wwwIndex/rightSidebar")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("澎湃新闻")
                .platformCategory(PlatFormCategoryEnum.XIN_WEN.getValue())
                .interfaceUrl("/api/topsearch/pengpaixinwen")
                .hotTitleUrlPrefix("https://www.thepaper.cn/newsDetail_forward_")
                .addArea()
                .rootSelector("$.data.hotNews")
                .titleSelector("$.name")
                .urlSelector("$.contId")

                .register();

        addDomConfig("https://www.jksb.com.cn/newslist/posid/8")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("健康时报网")
                .platformCategory(PlatFormCategoryEnum.JIAN_KANG.getValue())
                .interfaceUrl("/api/topsearch/jiankangshibaowang")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("#list_url > li")
                .titleSelector("h1 > a")
                .urlSelector("h1 > a")
                .register();

        addDomConfig("https://www.guokr.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("果壳")
                .platformCategory(PlatFormCategoryEnum.JIAN_KANG.getValue())
                .interfaceUrl("/api/topsearch/guoke")
                .hotTitleUrlPrefix("https://www.guokr.com")
                .addArea()
                .rootSelector(".FeedFloat__FeedFloatWrap-zt5yna-0 > a, .FeedFloat__FeedFloatWrap-zt5yna-0 > * > a")
                .titleSelector(".eAYHrP")
                .urlSelector("a")
                .register();

        addDomConfig("https://www.familydoctor.cn/article/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("家医大健康")
                .platformCategory(PlatFormCategoryEnum.JIAN_KANG.getValue())
                .interfaceUrl("/api/topsearch/jiayidajiankang")
                .hotTitleUrlPrefix("https://www.familydoctor.cn")
                .addArea()
                .rootSelector(".news-item")
                .titleSelector(".news-title > a")
                .urlSelector(".news-title > a")
                .register();

        addDomConfig("https://www.lifetimes.cn/news")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("生命时报")
                .platformCategory(PlatFormCategoryEnum.JIAN_KANG.getValue())
                .interfaceUrl("/api/topsearch/shengmingshibao")
                .hotTitleUrlPrefix("https:")
                .addArea()
                .rootSelector(".sUl > li")
                .titleSelector(".oP-txt a")
                .urlSelector(".oP-txt a")
                .register();

        addDomConfig("https://dxy.com/articles")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("丁香医生")
                .platformCategory(PlatFormCategoryEnum.JIAN_KANG.getValue())
                .interfaceUrl("/api/topsearch/dingxiangyisheng")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".article-card")
                .titleSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://bydrug.pharmcube.com/news")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("医药魔方")
                .platformCategory(PlatFormCategoryEnum.JIAN_KANG.getValue())
                .interfaceUrl("/api/topsearch/yiyaomofang")
                .hotTitleUrlPrefix("https://bydrug.pharmcube.com")
                .addArea()
                .rootSelector(".ant-spin-container > div")
                .titleSelector(".mf-font-600")
                .urlSelector(".mf-font-600")
                .register();

        addDomConfig("https://www.bioon.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("生物谷")
                .platformCategory(PlatFormCategoryEnum.JIAN_KANG.getValue())
                .interfaceUrl("/api/topsearch/shengwugu")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".composs-blog-list > .item")
                .titleSelector("h2 > a")
                .urlSelector("h2 > a")
                .register();

        addDomConfig("https://www.ali213.net/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("游侠网")
                .platformCategory(PlatFormCategoryEnum.YUO_XI.getValue())
                .interfaceUrl("/api/topsearch/youxiawang")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".s1-m-li.rdzx-ul a")
                .titleSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://news.17173.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("17173")
                .platformCategory(PlatFormCategoryEnum.YUO_XI.getValue())
                .interfaceUrl("/api/topsearch/17173")
                .hotTitleUrlPrefix("https:")
                .addArea()
                .rootSelector(".ptlist.ptlist-news.adNewsIndexYaoWen > li")
                .titleSelector(".tit > a")
                .urlSelector(".tit > a")
                .register();

        addDomConfig("https://www.yystv.cn/docs")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("游研社")
                .platformCategory(PlatFormCategoryEnum.YUO_XI.getValue())
                .interfaceUrl("/api/topsearch/youyanshe")
                .hotTitleUrlPrefix("https://www.yystv.cn")
                .addArea()
                .rootSelector(".articles-item")
                .titleSelector("h2")
                .urlSelector("a")
                .register();

        addDomConfig("https://www.gcores.com/news")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("GCORES")
                .platformCategory(PlatFormCategoryEnum.YUO_XI.getValue())
                .interfaceUrl("/api/topsearch/gcores")
                .hotTitleUrlPrefix("https://www.gcores.com")
                .addArea()
                .rootSelector(".col.mb-5")
                .titleSelector("h3")
                .urlSelector("a")
                .register();

        addDomConfig("https://www.ign.com.cn/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("IGN")
                .platformCategory(PlatFormCategoryEnum.YUO_XI.getValue())
                .interfaceUrl("/api/topsearch/ign")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".broll.wrap h3")
                .titleSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://www.youxituoluo.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("游戏陀螺")
                .platformCategory(PlatFormCategoryEnum.YUO_XI.getValue())
                .interfaceUrl("/api/topsearch/youxituoluo")
                .hotTitleUrlPrefix("https://www.youxituoluo.com")
                .addArea()
                .rootSelector(".article_list > li")
                .titleSelector(".title")
                .urlSelector(".title")
                .register();

        addDomConfig("https://www.a9vg.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("A9VG")
                .platformCategory(PlatFormCategoryEnum.YUO_XI.getValue())
                .interfaceUrl("/api/topsearch/a9vg")
                .hotTitleUrlPrefix("https://www.a9vg.com")
                .addArea()
                .rootSelector(".a9-plain-list_item")
                .titleSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://www.3dmgame.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("3DMGAME")
                .platformCategory(PlatFormCategoryEnum.YUO_XI.getValue())
                .interfaceUrl("/api/topsearch/3dmgame")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".Indexbox2-2 > .switchbox a")
                .titleSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://www.gamersky.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("游民星空")
                .platformCategory(PlatFormCategoryEnum.YUO_XI.getValue())
                .interfaceUrl("/api/topsearch/youminxingkong")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".bgx a")
                .titleSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://news.ycombinator.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("HACKER_NEWS")
                .platformCategory(PlatFormCategoryEnum.SHE_QU.getValue())
                .interfaceUrl("/api/topsearch/hackernews")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".athing.submission")
                .titleSelector(".titleline > a:first-of-type")
                .urlSelector(".titleline > a:first-of-type")
                .register();

        addJsonConfig("https://api.juejin.cn/content_api/v1/content/article_rank?category_id=1&type=hot")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("掘金文章")
                .platformCategory(PlatFormCategoryEnum.SHE_QU.getValue())
                .interfaceUrl("/api/topsearch/wenzhangjuejin")
                .hotTitleUrlPrefix("https://juejin.cn/post/")
                .addArea()
                .rootSelector("$.data")
                .titleSelector("$.content.title")
                .urlSelector("$.content.content_id")
                .hotScoreSelector("$.content_counter.hot_rank")
                .register();

        addDomConfig("https://nga.cn/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("NGA")
                .platformCategory(PlatFormCategoryEnum.SHE_QU.getValue())
                .interfaceUrl("/api/topsearch/nga")
                .hotTitleUrlPrefix("https:")
                .addArea()
                .rootSelector("#topic_ladder_cat_3 > li")
                .titleSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://bbs.hupu.com/all-gambia")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("步行街虎扑")
                .platformCategory(PlatFormCategoryEnum.SHE_QU.getValue())
                .interfaceUrl("/api/topsearch/buxingjiehupu")
                .hotTitleUrlPrefix("https://bbs.hupu.com")
                .addArea()
                .rootSelector(".text-list-model > div")
                .titleSelector(".t-title")
                .urlSelector("a")
                .hotScoreSelector(".t-replies")
                .register();

        addDomConfig("https://www.v2ex.com/?tab=hot")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("V2EX")
                .platformCategory(PlatFormCategoryEnum.SHE_QU.getValue())
                .interfaceUrl("/api/topsearch/v2ex")
                .hotTitleUrlPrefix("https://www.v2ex.com")
                .addArea()
                .rootSelector(".cell.item")
                .titleSelector(".topic-link")
                .urlSelector(".topic-link")
                .hotScoreSelector(".t-count_livid")
                .register();

        addDomConfig("https://www.zhibo8.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("直播吧")
                .platformCategory(PlatFormCategoryEnum.TI_YU.getValue())
                .interfaceUrl("/api/topsearch/zhiboba")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".zuqiu-news .list-item")
                .titleSelector("a")
                .urlSelector("a")
                .addArea()
                .rootSelector(".lanqiu-news .list-item")
                .titleSelector("a")
                .urlSelector("a")

                .register();

        addDomConfig("https://www.ppsport.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("PP体育")
                .platformCategory(PlatFormCategoryEnum.TI_YU.getValue())
                .interfaceUrl("/api/topsearch/pptiyu")
                .hotTitleUrlPrefix("https://www.ppsport.com")
                .addArea()
                .rootSelector(".info-panel-wrap > div")
                .titleSelector(".tw-link")
                .urlSelector(".tw-link")
                .register();

        addDomConfig("https://sports.cctv.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("央视体育")
                .platformCategory(PlatFormCategoryEnum.TI_YU.getValue())
                .interfaceUrl("/api/topsearch/yangshitiyu")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("#plantingtext li")
                .titleSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://sports.163.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("网易体育")
                .platformCategory(PlatFormCategoryEnum.TI_YU.getValue())
                .interfaceUrl("/api/topsearch/tiyuwangyi")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".channel_news_body li")
                .titleSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://sports.sohu.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("搜狐体育")
                .platformCategory(PlatFormCategoryEnum.TI_YU.getValue())
                .interfaceUrl("/api/topsearch/souhutiyu")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".feed-group > a")
                .titleSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://sports.sina.com.cn/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("新浪体育")
                .platformCategory(PlatFormCategoryEnum.TI_YU.getValue())
                .interfaceUrl("/api/topsearch/xinlangtiyu")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("#hot-search-list > div")
                .titleSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://www.dongqiudi.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("懂球帝")
                .platformCategory(PlatFormCategoryEnum.TI_YU.getValue())
                .interfaceUrl("/api/topsearch/dongqiudi")
                .hotTitleUrlPrefix("https://www.dongqiudi.com")
                .addArea()
                .rootSelector(".top-center > p")
                .titleSelector("a")
                .urlSelector("a")

                .register();

        addDomConfig("https://www.hupu.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("虎扑体育")
                .platformCategory(PlatFormCategoryEnum.TI_YU.getValue())
                .interfaceUrl("/api/topsearch/hupu")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".test-img-list-model > div")
                .titleSelector(".list-item-title")
                .urlSelector(".list-item-title")
                .register();

        addJsonConfig("https://www.jiqizhixin.com/api/article_library/articles.json?sort=time&page=1&per=12")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("机器之心")
                .platformCategory(PlatFormCategoryEnum.KE_JI.getValue())
                .interfaceUrl("/api/topsearch/jiqizhixin")
                .hotTitleUrlPrefix("https://www.jiqizhixin.com/articles/")
                .addArea()
                .rootSelector("$.articles")
                .titleSelector("$.title")
                .urlSelector("$.slug")
                .register();

        addDomConfig("https://www.eurekalert.org/language/chinese/home")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("EurekAlert")
                .platformCategory(PlatFormCategoryEnum.KE_JI.getValue())
                .interfaceUrl("/api/topsearch/eurekalert")
                .hotTitleUrlPrefix("https://www.eurekalert.org")
                .addArea()
                .rootSelector("#main-content > .post")
                .titleSelector("h2")
                .urlSelector("a")
                .register();

        addJsonConfig("https://apii.web.mittrchina.com/flash")
                .requestType(ForestRequestTypeEnum.POST)
                .platformName("MIT")
                .platformCategory(PlatFormCategoryEnum.KE_JI.getValue())
                .interfaceUrl("/api/topsearch/mit")
                .hotTitleUrlPrefix("")
                .hotTitleUrlNeedDeal(true)
                .addArea()
                .rootSelector("$.data.items")
                .titleSelector("$.name")
                .forestRequestHeader(new ForestRequestHeader()
                        .setHost("apii.web.mittrchina.com")
                        .setAccept("*/*")
                        .setAcceptEncoding("gzip, deflate, br, zstd")
                        .setAcceptLanguage("zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7")
                        .setConnection("keep-alive")
                        .setUserAgent(HttpRequestUtil.getRequestRandomUserAgent())
                        .setReferer(HttpRequestUtil.getRequestReferer("https://www.mittrchina.com/"))
                        .setOrigin(HttpRequestUtil.getRequestOrigin("https://www.mittrchina.com"))
                )
                .register();

        addDomConfig("https://abduzeedo.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("Abduzeedo")
                .platformCategory(PlatFormCategoryEnum.SHE_JI.getValue())
                .interfaceUrl("/api/topsearch/abduzeedo")
                .hotTitleUrlPrefix("https://abduzeedo.com")
                .addArea()
                .rootSelector(".posts > article")
                .titleSelector("span > h2 > a")
                .urlSelector("span > h2 > a")
                .register();

        addDomConfig("https://www.core77.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("Core77")
                .platformCategory(PlatFormCategoryEnum.SHE_JI.getValue())
                .interfaceUrl("/api/topsearch/core77")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".post_list_prime li")
                .titleSelector("h1 > a")
                .urlSelector("h1 > a")
                .register();

        addDomConfig("https://www.awwwards.com/websites/sites_of_the_day/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("Awwwards")
                .platformCategory(PlatFormCategoryEnum.SHE_JI.getValue())
                .interfaceUrl("/api/topsearch/awwwards")
                .hotTitleUrlPrefix("https://www.awwwards.com")
                .addArea()
                .rootSelector(".grid-cards > li")
                .titleSelector(".avatar-name__title")
                .urlSelector(".figure-rollover__link")

                .forestRequestHeader(new ForestRequestHeader()
                        .setReferer(HttpRequestUtil.getRequestReferer("https://www.behance.net/"))
                        .setOrigin(HttpRequestUtil.getRequestOrigin("https://www.behance.net"))
                )
                .register();

        addDomConfig("https://dribbble.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("Dribbble")
                .platformCategory(PlatFormCategoryEnum.SHE_JI.getValue())
                .interfaceUrl("/api/topsearch/dribbble")
                .hotTitleUrlPrefix("https://dribbble.com")
                .addArea()
                .rootSelector("#main > ol > li")
                .titleSelector(".display-name")
                .urlSelector(".shot-thumbnail-link")
                .hotScoreSelector(".js-shot-views-count")
                .register();

        addDomConfig("https://www.archdaily.cn/cn?ad_source=jv-header")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("ArchDaily")
                .platformCategory(PlatFormCategoryEnum.SHE_JI.getValue())
                .interfaceUrl("/api/topsearch/archdaily")
                .hotTitleUrlPrefix("https://www.archdaily.cn")
                .addArea()
                .rootSelector(".afd-post-stream")
                .titleSelector("h3 span")
                .urlSelector("h3 a")
                .register();

        addDomConfig("https://www.topys.cn/category/12")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("TOPYS")
                .platformCategory(PlatFormCategoryEnum.SHE_JI.getValue())
                .interfaceUrl("/api/topsearch/topys")
                .hotTitleUrlPrefix("https://www.topys.cn")
                .addArea()
                .rootSelector(".article-box-item")
                .titleSelector(".title")
                .urlSelector(".title")
                .register();

        addDomConfig("https://hot.uisdc.com/posts")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("优设网")
                .platformCategory(PlatFormCategoryEnum.SHE_JI.getValue())
                .interfaceUrl("/api/topsearch/youshewang")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".p-items > .p-item")
                .titleSelector("h2 > a")
                .urlSelector("h2 > a")
                .hotScoreSelector(".meta-views")
                .register();

        addJsonConfig("https://www.woshipm.com/api2/app/article/popular/daily")
                .requestType(ForestRequestTypeEnum.GET)
                .hotTitleUrlNeedDeal(true)
                .platformName("人人都是产品经理")
                .platformCategory(PlatFormCategoryEnum.SHE_JI.getValue())
                .interfaceUrl("/api/topsearch/woshipm")
                .hotTitleUrlPrefix("https://www.woshipm.com/")
                .addArea()
                .rootSelector("$.RESULT")
                .titleSelector("$.data.articleTitle")
                .urlSelector("$.data.type")
                .forestRequestHeader(new ForestRequestHeader()
                        .setHost("www.woshipm.com")
                        .setUserAgent(UserAgentGeneratorUtil.generateRandomUserAgent())
                        .setAccept("*/*")
                        .setAcceptEncoding("gzip, deflate, br, zstd")
                        .setAcceptLanguage("zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7")
                        .setConnection("keep-alive")
                        .setReferer("https://www.woshipm.com/")
                        .setOrigin("https://www.woshipm.com")
                )
                .register();

        addDomConfig("https://cn.wsj.com/zh-hans/news/world?mod=nav_top_section")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("华尔街日报")
                .platformCategory(PlatFormCategoryEnum.XIN_WEN.getValue())
                .interfaceUrl("/api/topsearch/huaerjieribao")
                .hotTitleUrlPrefix("https://www.rfi.fr")
                .addArea()
                .rootSelector(".css-1rznr30-CardLink")
                .titleSelector("")
                .urlSelector("")
                .forestRequestHeader(new ForestRequestHeader()
                        .setUserAgent(UserAgentGeneratorUtil.generateRandomUserAgent())
                        .setAccept("*/*")
                        .setAcceptEncoding("gzip, deflate, br, zstd")
                        .setAcceptLanguage("zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7")
                        .setConnection("keep-alive")
                        .setReferer("https://cn.wsj.com/zh-hans/news/china?mod=nav_top_section/")
                        .setOrigin("https://cn.wsj.com/zh-hans/news/china?mod=nav_top_section")
                )
                .register();

        addDomConfig("https://www.rfi.fr/cn")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("法广")
                .platformCategory(PlatFormCategoryEnum.XIN_WEN.getValue())
                .interfaceUrl("/api/topsearch/faguang")
                .hotTitleUrlPrefix("https://www.rfi.fr")
                .addArea()
                .rootSelector(".o-banana-split .article__title")
                .titleSelector("a")
                .urlSelector("a")

                .forestRequestHeader(new ForestRequestHeader()
                        .setReferer("https://www.rfi.fr/cn/")
                        .setOrigin("https://www.rfi.fr/cn")
                )
                .register();

        addDomConfig("https://www.bbc.com/zhongwen/simp/popular/read")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("BBC")
                .platformCategory(PlatFormCategoryEnum.XIN_WEN.getValue())
                .interfaceUrl("/api/topsearch/bbc")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("main[role='main']  li[role='listitem']")
                .titleSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://m.cn.nytimes.com/world/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("纽约时报")
                .platformCategory(PlatFormCategoryEnum.XIN_WEN.getValue())
                .interfaceUrl("/api/topsearch/niuyueshibao")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".article-list > li")
                .titleSelector("h2")
                .urlSelector("a")
                .register();

        addDomConfig("https://xnews.jin10.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("金十")
                .platformCategory(PlatFormCategoryEnum.CAI_JING.getValue())
                .interfaceUrl("/api/topsearch/jinshi")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".jin10-news-index-list > .jin10-news-list > div")
                .titleSelector("p")
                .urlSelector(".jin10-news-list-item-info > a")
                .register();

        addDomConfig("https://www.fastbull.com/cn/news")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("法布")
                .platformCategory(PlatFormCategoryEnum.CAI_JING.getValue())
                .interfaceUrl("/api/topsearch/fabu")
                .hotTitleUrlPrefix("https://www.fastbull.com")
                .addArea()
                .rootSelector(".news-top a")
                .titleSelector("h4")
                .urlSelector("a")
                .register();

        addDomConfig("https://www.gelonghui.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("格隆汇")
                .platformCategory(PlatFormCategoryEnum.CAI_JING.getValue())
                .interfaceUrl("/api/topsearch/gelonghui")
                .hotTitleUrlPrefix("https://www.gelonghui.com")
                .addArea()
                .rootSelector("#hot-article ul:first-of-type li")
                .titleSelector("a")
                .urlSelector("a")
                .forestRequestHeader(new ForestRequestHeader()
                        .setUserAgent(UserAgentGeneratorUtil.generateRandomUserAgent())
                        .setReferer("https://www.google.com/")
                        .setOrigin(("https://www.google.com"))
                )
                .register();

        addDomConfig("https://www.cls.cn/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("财联社")
                .platformCategory(PlatFormCategoryEnum.CAI_JING.getValue())
                .interfaceUrl("/api/topsearch/cailianshe")
                .hotTitleUrlPrefix("https://www.cls.cn")
                .addArea()
                .rootSelector(".home-article-ranking-box > div")
                .titleSelector("a")
                .urlSelector("a")
                .register();

        addJsonConfig("https://api-one-wscn.awtmt.com/apiv1/content/information-flow?channel=global&accept=article&cursor=&limit=20&action=upglide")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("华尔街见闻")
                .platformCategory(PlatFormCategoryEnum.CAI_JING.getValue())
                .interfaceUrl("/api/topsearch/huaerjiejianwen")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.items")
                .titleSelector("$.resource.title")
                .urlSelector("$.resource.uri")
                .register();

        addDomConfig("https://news.10jqka.com.cn/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("同花顺")
                .platformCategory(PlatFormCategoryEnum.CAI_JING.getValue())
                .interfaceUrl("/api/topsearch/tonghuashun")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".list-con ul li")
                .titleSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://www.yicai.com")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("第一财经")
                .platformCategory(PlatFormCategoryEnum.CAI_JING.getValue())
                .interfaceUrl("/api/topsearch/diyicaijing")
                .hotTitleUrlPrefix("https://www.yicai.com")
                .addArea()
                .rootSelector("#headlist > a")
                .titleSelector("h2")
                .urlSelector("a")
                .register();

        addJsonConfig("https://gateway.jrj.com/jrj-news/news/queryNewsList")
                .requestType(ForestRequestTypeEnum.POST)
                .platformName("金融界")
                .platformCategory(PlatFormCategoryEnum.CAI_JING.getValue())
                .interfaceUrl("/api/topsearch/jinrongjie")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.data")
                .titleSelector("$.title")
                .urlSelector("$.pcInfoUrl")
                .jsonBody(
                        new HashMap<>(Map.of(
                                "sortBy", 1,
                                "pageSize", 30,
                                "channelNum", "103"
                        ))
                )
                .register();

        addJsonConfig("https://api.zhihu.com/topstory/hot-list?limit=100&reverse_order=0")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("知乎")
                .platformCategory(PlatFormCategoryEnum.SHE_QU.getValue())
                .interfaceUrl("/api/topsearch/zhihu")
                .hotTitleUrlPrefix("https://www.zhihu.com/question/")
                .addArea()
                .rootSelector("$.data")
                .titleSelector("$.target.title")
                .urlSelector("$.target.id")
                .register();

        addJsonConfig("https://sspai.com/api/v1/article/tag/page/get?limit=100&offset=0&tag=%E7%83%AD%E9%97%A8%E6%96%87%E7%AB%A0&released=false")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("少数派")
                .platformCategory(PlatFormCategoryEnum.SHE_QU.getValue())
                .interfaceUrl("/api/topsearch/shaoshupai")
                .hotTitleUrlPrefix("https://sspai.com/post/")
                .addArea()
                .rootSelector("$.data")
                .titleSelector("$.title")
                .urlSelector("$.id")
                .hotScoreSelector("$.comment_count")
                .register();

        addJsonConfig("https://tieba.baidu.com/hottopic/browse/topicList")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("百度贴吧")
                .platformCategory(PlatFormCategoryEnum.SHE_QU.getValue())
                .interfaceUrl("/api/topsearch/tiebabaidu")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.bang_topic.topic_list")
                .titleSelector("$.topic_name")
                .urlSelector("$.topic_url")
                .hotScoreSelector("$.discuss_num")
                .register();

        addJsonConfig("https://gw.m.163.com/nc-main/api/v1/hqc/no-repeat-hot-list?source=hotTag")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("网易新闻")
                .platformCategory(PlatFormCategoryEnum.XIN_WEN.getValue())
                .interfaceUrl("/api/topsearch/wangyi")
                .hotTitleUrlPrefix("https://c.m.163.com/news/a/")
                .hotTitleUrlAfter(".html")
                .addArea()
                .rootSelector("$.data.items")
                .titleSelector("$.title")
                .urlSelector("$.contentId")
                .hotScoreSelector("$.hotValue")
                .register();

        addJsonConfig("https://www.toutiao.com/hot-event/hot-board/?origin=toutiao_pc")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("头条新闻")
                .platformCategory(PlatFormCategoryEnum.XIN_WEN.getValue())
                .interfaceUrl("/api/topsearch/toutiao")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.fixed_top_data")
                .titleSelector("$.Title")
                .urlSelector("$.Url")
                .addArea()
                .rootSelector("$.data")
                .titleSelector("$.Title")
                .urlSelector("$.Url")
                .hotScoreSelector("$.HotValue")
                .register();

        addJsonConfig("https://i.news.qq.com/gw/event/pc_hot_ranking_list?ids_hash=&offset=0&page_size=51&appver=15.5_qqnews_7.1.60&rank_id=hot")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("腾讯新闻")
                .platformCategory(PlatFormCategoryEnum.XIN_WEN.getValue())
                .interfaceUrl("/api/topsearch/tencent")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.idlist[0].newslist[1:]")
                .titleSelector("$.title")
                .urlSelector("$.url")
                .hotScoreSelector("$.readCount")
                .register();

        addJsonConfig("https://m.douban.com/rexxar/api/v2/chart/hot_search_board?count=10&start=0")
                .requestType(ForestRequestTypeEnum.GET)
                .hotTitleUrlNeedDeal(true)
                .platformName("豆瓣")
                .platformCategory(PlatFormCategoryEnum.SHE_QU.getValue())
                .interfaceUrl("/api/topsearch/douban")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$")
                .titleSelector("$.name")
                .hotScoreSelector("$.score")
                .register();

        addJsonConfig("https://www-hj.douyin.com/aweme/v1/web/hot/search/list/")
                .requestType(ForestRequestTypeEnum.GET)
                .hotTitleUrlNeedDeal(true)
                .platformName("抖音")
                .platformCategory(PlatFormCategoryEnum.MEI_TI.getValue())
                .interfaceUrl("/api/topsearch/douyin")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.word_list")
                .titleSelector("$.word")
                .hotScoreSelector("$.hot_value")

                .register();

        addJsonConfig("https://weibo.com/ajax/statuses/hot_band")
                .requestType(ForestRequestTypeEnum.GET)
                .hotTitleUrlNeedDeal(true)
                .platformName("微博")
                .platformCategory(PlatFormCategoryEnum.MEI_TI.getValue())
                .interfaceUrl("/api/topsearch/weibo")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.hotgov")
                .titleSelector("$.word")
                .urlSelector("$.url")
                .addArea()
                .rootSelector("$.data.band_list")
                .titleSelector("$.note")
                .hotScoreSelector("$.num")

                .register();

        addJsonConfig("https://api.bilibili.com/x/web-interface/ranking/v2?rid=0&type=all")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("B站")
                .platformCategory(PlatFormCategoryEnum.MEI_TI.getValue())
                .interfaceUrl("/api/topsearch/bilibili")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.list")
                .titleSelector("$.title")
                .urlSelector("$.short_link_v2")
                .hotScoreSelector("$.stat.view")
                .forestRequestHeader(new ForestRequestHeader()
                        .setUserAgent(UserAgentGeneratorUtil.generateRandomUserAgent())
                        .setAcceptEncoding("gzip, deflate, br, zstd")
                        .setAcceptLanguage("zh-CN,zh;q=0.9")
                        .setPriority("u=0, i")
                )
                .register();

        // 这里把百度里面的新闻单独写一下，是为了在排除的时候，可以保留这个
        addJsonConfig("https://top.baidu.com/api/board?platform=wise&tab=realtime")
                .requestType(ForestRequestTypeEnum.GET)
                .hotTitleUrlNeedDeal(true)
                .platformName("百度新闻")
                .platformCategory(PlatFormCategoryEnum.XIN_WEN.getValue())
                .interfaceUrl("/api/topsearch/baidu")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.cards[0].topContent")
                .titleSelector("$.word")
                .urlSelector("$.url")
                .addArea()
                .rootSelector("$.data.cards[0].content[0].content")
                .titleSelector("$.word")
                .urlSelector("$.url")
                .addArea()
                .rootSelector("$.data.cards[0].content[0].content")
                .titleSelector("$.title")
                .urlSelector("$.url")
                .register();

        addJsonConfig("https://top.baidu.com/api/board?platform=wise&tab=internation_news")
                .requestType(ForestRequestTypeEnum.GET)
                .hotTitleUrlNeedDeal(true)
                .platformName("百度国际")
                .platformCategory(PlatFormCategoryEnum.XIN_WEN.getValue())
                .interfaceUrl("/api/topsearch/baiduguoji")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.cards[0].topContent")
                .titleSelector("$.word")
                .urlSelector("$.url")
                .addArea()
                .rootSelector("$.data.cards[0].content[0].content")
                .titleSelector("$.word")
                .urlSelector("$.url")
                .addArea()
                .rootSelector("$.data.cards[0].content[0].content")
                .titleSelector("$.title")
                .urlSelector("$.url")
                .register();

        addJsonConfig("https://top.baidu.com/api/board?platform=wise&tab=new_entertainment")
                .requestType(ForestRequestTypeEnum.GET)
                .hotTitleUrlNeedDeal(true)
                .platformName("百度文娱")
                .platformCategory(PlatFormCategoryEnum.MEI_TI.getValue())
                .interfaceUrl("/api/topsearch/baiduwenyu")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.cards[0].topContent")
                .titleSelector("$.word")
                .urlSelector("$.url")
                .addArea()
                .rootSelector("$.data.cards[0].content[0].content")
                .titleSelector("$.word")
                .urlSelector("$.url")
                .addArea()
                .rootSelector("$.data.cards[0].content[0].content")
                .titleSelector("$.title")
                .urlSelector("$.url")
                .register();

        addJsonConfig("https://top.baidu.com/api/board?platform=wise&tab=finance")
                .requestType(ForestRequestTypeEnum.GET)
                .hotTitleUrlNeedDeal(true)
                .platformName("百度财经")
                .platformCategory(PlatFormCategoryEnum.CAI_JING.getValue())
                .interfaceUrl("/api/topsearch/baiducaijing")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.cards[0].topContent")
                .titleSelector("$.word")
                .urlSelector("$.url")
                .addArea()
                .rootSelector("$.data.cards[0].content[0].content")
                .titleSelector("$.word")
                .urlSelector("$.url")
                .addArea()
                .rootSelector("$.data.cards[0].content[0].content")
                .titleSelector("$.title")
                .urlSelector("$.url")
                .register();

        addJsonConfig("https://top.baidu.com/api/board?platform=wise&tab=sports")
                .requestType(ForestRequestTypeEnum.GET)
                .hotTitleUrlNeedDeal(true)
                .platformName("百度体育")
                .platformCategory(PlatFormCategoryEnum.TI_YU.getValue())
                .interfaceUrl("/api/topsearch/baidutiyu")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.cards[0].topContent")
                .titleSelector("$.word")
                .urlSelector("$.url")
                .addArea()
                .rootSelector("$.data.cards[0].content[0].content")
                .titleSelector("$.word")
                .urlSelector("$.url")
                .addArea()
                .rootSelector("$.data.cards[0].content[0].content")
                .titleSelector("$.title")
                .urlSelector("$.url")
                .register();

        addJsonConfig("https://top.baidu.com/api/board?platform=wise&tab=livelihood")
                .requestType(ForestRequestTypeEnum.GET)
                .hotTitleUrlNeedDeal(true)
                .platformName("百度民生")
                .platformCategory(PlatFormCategoryEnum.MEI_TI.getValue())
                .interfaceUrl("/api/topsearch/baiduminsheng")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.cards[0].topContent")
                .titleSelector("$.word")
                .urlSelector("$.url")
                .addArea()
                .rootSelector("$.data.cards[0].content[0].content")
                .titleSelector("$.word")
                .urlSelector("$.url")
                .addArea()
                .rootSelector("$.data.cards[0].content[0].content")
                .titleSelector("$.title")
                .urlSelector("$.url")
                .register();

        addJsonConfig("https://top.baidu.com/api/board?platform=wise&tab={type}")
                .requestType(ForestRequestTypeEnum.GET)
                .hotTitleUrlNeedDeal(true)
                .platformName("百度")
                .platformCategory(PlatFormCategoryEnum.BAI_DU.getValue())
                .interfaceUrl("/api/topsearch/baidu/{type}")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.cards[0].content[0].content")
                .titleSelector("$.word")
                .urlSelector("$.url")
                .addArea()
                .rootSelector("$.data.cards[0].content[0].content")
                .titleSelector("$.title")
                .urlSelector("$.url")
                .register();

        addJsonConfig("https://music.163.com/api/playlist/detail?id={type}")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("网易云音乐")
                .platformCategory(PlatFormCategoryEnum.WANG_YI_YUN_YIN_YUE.getValue())
                .interfaceUrl("/api/topsearch/wangyiyun/{type}")
                .hotTitleUrlPrefix("https://music.163.com/#/song?id=")
                .addArea()
                .rootSelector("$.result.tracks")
                .titleSelector("$.name")
                .urlSelector("$.id")
                .hotScoreSelector("$.popularity")
                .register();

        addDomConfig("https://v.qq.com/biu/ranks/?t=hotsearch&channel={type}")
                .requestType(ForestRequestTypeEnum.GET)
                .hotScoreNeedDeal(true)
                .platformName("腾讯视频")
                .platformCategory(PlatFormCategoryEnum.TENG_XUN_SHI_PIN.getValue())
                .interfaceUrl("/api/topsearch/tencent/{type}")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".table_list > li")
                .titleSelector("div:nth-of-type(1) > a")
                .urlSelector("div:nth-of-type(1) > a")
                .forestRequestHeader(new ForestRequestHeader()
                        .setUserAgent(UserAgentGeneratorUtil.generateRandomUserAgent())
                        .setAcceptLanguage("zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7")
                        .setXForwardedFor("114.114.114.114")
                        .setReferer("https://v.qq.com/")
                        .setOrigin("https://v.qq.com")
                )
                .register();

        addJsonConfig("https://mesh.if.iqiyi.com/portal/pcw/rankList/comSecRankList?category_id={type}")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("爱奇艺视频")
                .platformCategory(PlatFormCategoryEnum.AI_QI_YI_SHI_PIN.getValue())
                .interfaceUrl("/api/topsearch/aiqiyi/{type}")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.items[0].contents")
                .titleSelector("$.title")
                .urlSelector("$.pageUrl")
                .hotScoreSelector("$.mainIndex")
                .register();

        addJsonConfig("https://mobileso.bz.mgtv.com/pc/suggest/v1")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("芒果视频")
                .platformCategory(PlatFormCategoryEnum.MANG_GUO_SHI_PIN.getValue())
                .interfaceUrl("/api/topsearch/mangguo/{type}")
                .hotTitleUrlPrefix("https://so.mgtv.com/so?k=")
                .addArea()
                .rootSelector("$.data.topList[?(@.label=='{type}')].data[*]")
                .titleSelector("$.name")
                .urlSelector("$.name")
                .register();

        addJsonConfig("https://m.maoyan.com/asgard/board?year=0&term=0&id={type}")
                .requestType(ForestRequestTypeEnum.GET)
                .hotScoreNeedDeal(true)
                .platformName("猫眼")
                .platformCategory(PlatFormCategoryEnum.MAO_YAN.getValue())
                .interfaceUrl("/api/topsearch/maoyan/{type}")
                .hotTitleUrlPrefix("https://www.maoyan.com/films/")
                .addArea()
                .rootSelector("$.data.data.movies")
                .titleSelector("$.nm")
                .urlSelector("$.id")
                .imageSelector("$.img")
                .authorSelector("$.star")
                .descSelector("$.shortDec")
                .typeSelector("$.cat")
                .publishTimeSelector("$.pubDesc")
                .register();

        addDomConfig("https://www.zcool.com.cn/{type}")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("站酷")
                .platformCategory(PlatFormCategoryEnum.ZHAN_KU.getValue())
                .interfaceUrl("/api/topsearch/zhanku/{type}")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".eTkjTE > section")
                .titleSelector(".cnxaX > a")
                .urlSelector(".rankCard")
                .hotScoreSelector(".rankBox > div:nth-of-type(4) .kFzKPV")
                .register();

        addDomConfig("https://www.gracg.com/showwork/{type}")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("涂鸦王国")
                .platformCategory(PlatFormCategoryEnum.TU_YA_WANG_GUO.getValue())
                .interfaceUrl("/api/topsearch/tuyawangguo/{type}")
                .hotTitleUrlPrefix("https://www.gracg.com")
                .addArea()
                .rootSelector(".px-2 > div")
                .titleSelector(".show-works-title")
                .urlSelector(".cursor-hand")
                .hotScoreSelector(".show-works-views > span")
                .register();

        addDomConfig("https://www.ncsti.gov.cn/kjdt/kjrd/{type}")
                .requestType(ForestRequestTypeEnum.GET)
                .hotTitleUrlNeedDeal(true)
                .platformName("人工智能国际科技创新中心")
                .platformCategory(PlatFormCategoryEnum.GUO_JI_KE_JI_CHUANG_XIN_ZHONG_XIN.getValue())
                .interfaceUrl("/api/topsearch/guojikejichuangxinzhongxin/{type}")
                .addArea()
                .rootSelector(".news_box > ul:eq(2) > li")
                .titleSelector("h2")
                .register();

        addJsonConfig("https://api.github.com/search/repositories?q=stars:>0+created:>={time}&sort=stars&order=desc&page=1&per_page=100")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("GitHub")
                .platformCategory(PlatFormCategoryEnum.GITHUB.getValue())
                .interfaceUrl("/api/topsearch/github/stars/{type}")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.items")
                .titleSelector("$.name")
                .urlSelector("$.html_url")
                .hotScoreSelector("$.stargazers_count")
                .descSelector("$.description")

                .register();

        addDomConfig("https://huggingface.co/{type}")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("HuggingFaces")
                .platformCategory(PlatFormCategoryEnum.HUGGING_FACES.getValue())
                .interfaceUrl("/api/topsearch/global/huggingface/{type}")
                .hotTitleUrlPrefix("")
                .register();

        addJsonConfig("https://www.googleapis.com/youtube/v3/videos?part=snippet,statistics&chart=mostPopular&maxResults=50&key=AIzaSyCX494YE2KiRnmGT6Y8odk4SBnlTwIWH4A")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("Youtube")
                .platformCategory(PlatFormCategoryEnum.MEI_TI.getValue())
                .interfaceUrl("/api/topsearch/global/youtube")
                .hotTitleUrlPrefix("https://www.youtube.com/watch?v=")
                .addArea()
                .rootSelector("$.items")
                .titleSelector("$.snippet.title")
                .urlSelector("$.id")
                .hotScoreSelector("$.statistics.viewCount")

                .register();

        addJsonConfig("https://acs.youku.com/h5/mtop.youku.soku.yksearch/2.0/?appKey={appKey}&data={data}&t={time}&sign={sign}")
                .hotScoreNeedDeal(true)
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("优酷视频")
                .platformCategory(PlatFormCategoryEnum.YOU_KU_SHI_PIN.getValue())
                .interfaceUrl("/api/topsearch/youku/{type}")
                .hotTitleUrlPrefix("https://v.youku.com/v_show/id_XNjQ1NDI4ODY2MA==.html?s=")
                .addArea()
                .rootSelector("$.data.nodes[0].nodes[0].data.tabDataMap.{type}.nodes[0].nodes[*].data")
                .titleSelector("$.title")
                .urlSelector("$.encodeShowId")
                .register();

        addDomConfig("https://www.douban.com/group/{type}")
                .hotTitleNeedDeal(true)
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("小组豆瓣")
                .platformCategory(PlatFormCategoryEnum.XIAO_ZU_DOU_BAN.getValue())
                .interfaceUrl("/api/topsearch/xiaozudouban/{type}")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".olt tr:not(:has(span[title=置顶]))")
                .titleSelector(".title > a")
                .urlSelector(".title > a")
                .publishTimeSelector(".time")
                .commentCountSelector(".r-count")
                .forestRequestHeader(
                        new ForestRequestHeader()
                                .setUserAgent(UserAgentGeneratorUtil.generateRandomUserAgent())
                )
                .register();

        addJsonConfig("https://api.cntv.cn/epg/getEpgInfoByChannelNew?c=cctv{type}&serviceId=tvcctv&d={time}&t=jsonp&cb=setItem1")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("央视电视台")
                .platformCategory(PlatFormCategoryEnum.CCTV.getValue())
                .interfaceUrl("/api/topsearch/cctv/{type}")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.cctv{type}.list")
                .titleSelector("$.title")
                .hotScoreSelector("$.startTime")
                .urlSelector("$.columnBackvideourl")
                .startTimeSelector("$.startTime")
                .endTimeSelector("$.endTime")
                .showTimeSelector("$.showTime")
                .register();

        addDomConfig("https://www.newsmth.net/nForum/mainpage?ajax")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("水木社区")
                .platformCategory(PlatFormCategoryEnum.SHE_QU.getValue())
                .interfaceUrl("/api/topsearch/shuimushequ")
                .hotTitleUrlPrefix("https://www.newsmth.net")
                .addArea()
                .rootSelector("#top10 > ul > li")
                .titleSelector("a:nth-of-type(2)")
                .urlSelector("a:nth-of-type(2)")
                .register();

        addDomConfig("https://www.chongbuluo.com/forum.php?mod=guide&view=hot")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("虫部落")
                .platformCategory(PlatFormCategoryEnum.SHE_QU.getValue())
                .interfaceUrl("/api/topsearch/chongbuluo")
                .hotTitleUrlPrefix("https://www.chongbuluo.com/")
                .addArea()
                .rootSelector(".bm_c tbody")
                .titleSelector(".common > a")
                .urlSelector(".common > a")
                .hotScoreSelector(".common > .xi1")
                .register();

        addDomConfig("https://xz.aliyun.com/news")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("先知社区")
                .platformCategory(PlatFormCategoryEnum.SHE_QU.getValue())
                .interfaceUrl("/api/topsearch/xianzhishequ")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".hot_news2 > ul > li")
                .titleSelector("div > a")
                .urlSelector("div > a")
                .register();

        addJsonConfig("https://service.kdslife.com/v1/news?channel_id=689073ce0406f87cd1b07b66&page=1&page_size=60")
                .requestType(ForestRequestTypeEnum.GET)
                .hotTitleUrlNeedDeal(true)
                .platformName("KDS上海头条")
                .platformCategory(PlatFormCategoryEnum.SHE_QU.getValue())
                .interfaceUrl("/api/topsearch/kdsshanghaitoutiao")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.list")
                .titleSelector("$.title")
                .hotScoreSelector("$.views")
                .register();

        addDomConfig("https://www.txrjy.com/forum.php")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("通信人家园")
                .platformCategory(PlatFormCategoryEnum.SHE_QU.getValue())
                .interfaceUrl("/api/topsearch/tongxinrenjiayuan")
                .hotTitleUrlPrefix("https://www.txrjy.com/")
                .addArea()
                .rootSelector("#review li")
                .titleSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://emacs-china.org/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("Emacs China")
                .platformCategory(PlatFormCategoryEnum.SHE_QU.getValue())
                .interfaceUrl("/api/topsearch/emacschina")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".topic-list .topic-list-item")
                .titleSelector(".link-top-line > a")
                .urlSelector(".link-top-line > a")
                .hotScoreSelector(".views span")
                .register();

        addDomConfig("https://ruby-china.org/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("Ruby China")
                .platformCategory(PlatFormCategoryEnum.SHE_QU.getValue())
                .interfaceUrl("/api/topsearch/rubychina")
                .hotTitleUrlPrefix("https://ruby-china.org")
                .addArea()
                .rootSelector(".card-body.item-list > div")
                .titleSelector(".title.media-heading > a")
                .urlSelector(".title.media-heading > a")
                .hotScoreSelector(".count")
                .register();

        addDomConfig("https://www.9kd.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("凯迪网")
                .platformCategory(PlatFormCategoryEnum.SHE_QU.getValue())
                .interfaceUrl("/api/topsearch/kaidiwang")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".hot-news-list-wrap > ul >li")
                .titleSelector("a > p")
                .urlSelector("a")
                .register();

        addDomConfig("https://www.wearesellers.com/headline/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("知无不言跨境电商社区")
                .platformCategory(PlatFormCategoryEnum.SHE_QU.getValue())
                .interfaceUrl("/api/topsearch/zhiwubuyankuajingdianshangshequ")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".tab_new.tab-list-explore > .conlist > dl")
                .titleSelector("dd > h2 > a")
                .urlSelector("dd > h2 > a")
                .register();

        addDomConfig("https://www.oschina.net/news")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("开源资讯")
                .platformCategory(PlatFormCategoryEnum.SHE_QU.getValue())
                .interfaceUrl("/api/topsearch/kaiyuanzixun")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".section.articles-list > .items > .item")
                .titleSelector(".content > a")
                .urlSelector(".content > a")
                .register();

        addDomConfig("https://bbs.pinggu.org/z_index.php?type=3")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("经管之家")
                .platformCategory(PlatFormCategoryEnum.SHE_QU.getValue())
                .interfaceUrl("/api/topsearch/jingguanzhijia")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("#ct > .mn tbody > tr:not(:first-child)")
                .titleSelector("th > a")
                .urlSelector("th > a")
                .hotScoreSelector(".num > a")
                .register();

        addDomConfig("https://www.36kr.com/hot-list/catalog")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("36氪")
                .platformCategory(PlatFormCategoryEnum.KE_JI.getValue())
                .interfaceUrl("/api/topsearch/36ke")
                .hotTitleUrlPrefix("https://www.36kr.com")
                .addArea()
                .rootSelector(".main-wrapper > .list-wrapper > .list-section-wrapper:nth-of-type(1) > .article-list > .article-wrapper")
                .titleSelector(".article-item-info > p > a")
                .urlSelector(".article-item-info > p > a")
                .hotScoreSelector(".kr-flow-bar-hot > span")
                .register();

        addDomConfig("https://m.ithome.com/rankm/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("IT之家")
                .platformCategory(PlatFormCategoryEnum.KE_JI.getValue())
                .interfaceUrl("/api/topsearch/itzhijia")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".rank-box:nth-of-type(2) > div")
                .titleSelector(".plc-title")
                .urlSelector("a")
                .hotScoreSelector(".review-num")
                .register();

        addDomConfig("https://readhub.cn/hot")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("ReadHub")
                .platformCategory(PlatFormCategoryEnum.KE_JI.getValue())
                .interfaceUrl("/api/topsearch/readhub")
                .hotTitleUrlPrefix("https://readhub.cn")
                .addArea()
                .rootSelector(".style_tabs__HaLYn > .style_tabs__HaLYn > .mantine-Tabs-panel > ol > li")
                .titleSelector("div > div:nth-of-type(2) > a")
                .urlSelector("div > div:nth-of-type(2) > a")
                .register();

        addDomConfig("https://www.tmtpost.com/hot")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("钛媒体")
                .platformCategory(PlatFormCategoryEnum.KE_JI.getValue())
                .interfaceUrl("/api/topsearch/taimeiti")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".content > .item").titleSelector("._tit")
                .urlSelector("._tit")
                .hotScoreSelector(".action_reads")
                .register();

        addDomConfig("https://www.zol.com.cn/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("中关村在线")
                .platformCategory(PlatFormCategoryEnum.KE_JI.getValue())
                .interfaceUrl("/api/topsearch/zhongguancunzaixian")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("#today_news_1 li")
                .titleSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://www.landiannews.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("蓝点网")
                .platformCategory(PlatFormCategoryEnum.KE_JI.getValue())
                .interfaceUrl("/api/topsearch/landianwang")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".article-list.post-list-layout.post > ul >li")
                .titleSelector(".title")
                .urlSelector(".title")
                .hotScoreSelector(".icon-view")
                .register();

        addDomConfig("https://www.cyzone.cn/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("创业邦")
                .platformCategory(PlatFormCategoryEnum.KE_JI.getValue())
                .interfaceUrl("/api/topsearch/chuangyebang")
                .hotTitleUrlPrefix("https://www.cyzone.cn")
                .addArea()
                .rootSelector("#pane-recommend .article-item")
                .titleSelector(".item-title")
                .urlSelector(".item-title")
                .register();

        addDomConfig("https://www.iheima.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("i黑马")
                .platformCategory(PlatFormCategoryEnum.KE_JI.getValue())
                .interfaceUrl("/api/topsearch/iheima")
                .hotTitleUrlPrefix("https://www.iheima.com")
                .addArea()
                .rootSelector(".mainleft .item-wrap.clearfix")
                .titleSelector(".title")
                .urlSelector(".title")
                .register();

        addDomConfig("https://www.leiphone.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("雷锋网")
                .platformCategory(PlatFormCategoryEnum.KE_JI.getValue())
                .interfaceUrl("/api/topsearch/leifengwang")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".lph-pageList.index-pageList > .list li")
                .titleSelector("h3 > a")
                .urlSelector("h3 > a")
                .register();

        addDomConfig("https://awtmt.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("全天候科技")
                .platformCategory(PlatFormCategoryEnum.KE_JI.getValue())
                .interfaceUrl("/api/topsearch/quantianhoukeji")
                .hotTitleUrlPrefix("https://awtmt.com")
                .addArea()
                .rootSelector(".article-list > div")
                .titleSelector(".article-title")
                .urlSelector(".article-title")
                .register();

        addDomConfig("https://www.mydrivers.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("快科技")
                .platformCategory(PlatFormCategoryEnum.KE_JI.getValue())
                .interfaceUrl("/api/topsearch/kuaikeji")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("#newlist_1 > h4 > ul > li")
                .titleSelector("a")
                .urlSelector("a")
                .addArea()
                .rootSelector("#news_content_1 li")
                .titleSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://www.toodaylab.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("理想生活实验室")
                .platformCategory(PlatFormCategoryEnum.KE_JI.getValue())
                .interfaceUrl("/api/topsearch/lixiangshenghuoshiyanshi")
                .hotTitleUrlPrefix("https://www.toodaylab.com")
                .addArea()
                .rootSelector(".hot-list.clearfix > a")
                .titleSelector("p")
                .urlSelector(":root")
                .register();

        addDomConfig("http://www.duozhi.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("多知")
                .platformCategory(PlatFormCategoryEnum.KE_JI.getValue())
                .interfaceUrl("/api/topsearch/duozhi")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".list-post > .post-item")
                .titleSelector(".post-inner > a")
                .urlSelector(".post-inner > a")
                .register();

        addDomConfig("https://www.jiemodui.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("芥末堆")
                .platformCategory(PlatFormCategoryEnum.KE_JI.getValue())
                .interfaceUrl("/api/topsearch/jiemodui")
                .hotTitleUrlPrefix("https://www.jiemodui.com")
                .addArea()
                .rootSelector(".itemList > a")
                .titleSelector("h3")
                .urlSelector(":root")
                .register();

        addDomConfig("https://www.iimedia.cn/c1088")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("艾媒网")
                .platformCategory(PlatFormCategoryEnum.KE_JI.getValue())
                .interfaceUrl("/api/topsearch/aimeiwang")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".article-hot-list .hot-item")
                .titleSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://www.chinaz.com/news/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("站长之家")
                .platformCategory(PlatFormCategoryEnum.KE_JI.getValue())
                .interfaceUrl("/api/topsearch/zhanzhangzhijia")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".cloumn-bd.section.hots-wrapper  .hots-wrapper__bd  li")
                .titleSelector("h4 > a")
                .urlSelector("h4 > a")
                .register();

        addDomConfig("https://lieyunpro.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("猎云网")
                .platformCategory(PlatFormCategoryEnum.KE_JI.getValue())
                .interfaceUrl("/api/topsearch/lieyunwang")
                .hotTitleUrlPrefix("https://lieyunpro.com")
                .addArea()
                .rootSelector(".article-container > div")
                .titleSelector("h2 > a")
                .urlSelector("h2 > a")
                .register();

        addDomConfig("https://weread.qq.com/web/category/all")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("微信读书")
                .platformCategory(PlatFormCategoryEnum.MEI_TI.getValue())
                .interfaceUrl("/api/topsearch/weixindushu")
                .hotTitleUrlPrefix("https://weread.qq.com")
                .addArea()
                .rootSelector(".ranking_content_bookList > li")
                .titleSelector(".wr_bookList_item_title")
                .urlSelector("a")
                .hotScoreSelector(".wr_bookList_item_readingCountText")
                .register();

        addJsonConfig("https://www.acfun.cn/rest/pc-direct/rank/channel?channelId=&subChannelId=&rankLimit=30&rankPeriod=DAY")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("acfun")
                .platformCategory(PlatFormCategoryEnum.MEI_TI.getValue())
                .interfaceUrl("/api/topsearch/acfun")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.rankList")
                .titleSelector("$.contentTitle")
                .urlSelector("$.shareUrl")
                .hotScoreSelector("$.viewCount")
                .register();

        addDomConfig("https://www.mmfen.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("美漫百科")
                .platformCategory(PlatFormCategoryEnum.MEI_TI.getValue())
                .interfaceUrl("/api/topsearch/meimanbaike")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".main-inner.group .pad.group article")
                .titleSelector(".post-title > a")
                .urlSelector(".post-title > a")
                .register();

        addDomConfig("http://news.mtime.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("时光网")
                .platformCategory(PlatFormCategoryEnum.MEI_TI.getValue())
                .interfaceUrl("/api/topsearch/shiguangwang")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".left-cont.fix > li")
                .titleSelector("h4 > a")
                .urlSelector("h4 > a")
                .register();

        addDomConfig("https://jandan.net/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("煎蛋")
                .platformCategory(PlatFormCategoryEnum.MEI_TI.getValue())
                .interfaceUrl("/api/topsearch/jiandan")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".post-list > div")
                .titleSelector("h2 > a")
                .urlSelector("h2 > a")
                .register();

        addDomConfig("https://www.vgover.com/news")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("电玩帮")
                .platformCategory(PlatFormCategoryEnum.YUO_XI.getValue())
                .interfaceUrl("/api/topsearch/dianwanbang")
                .hotTitleUrlPrefix("https://www.vgover.com")
                .addArea()
                .rootSelector(".vg-list.vg-list--8 > li")
                .titleSelector("h3 > a")
                .urlSelector("h3 > a")
                .register();

        addDomConfig("https://www.tvmao.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("电视猫")
                .platformCategory(PlatFormCategoryEnum.MEI_TI.getValue())
                .interfaceUrl("/api/topsearch/dianshimao")
                .hotTitleUrlPrefix("https://www.tvmao.com")
                .addArea()
                .rootSelector(".rtive.f_drama")
                .titleSelector("a")
                .urlSelector("a")
                .addArea()
                .rootSelector(".r_hot_drama > li")
                .titleSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://www.chinanews.com.cn/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("中国新闻网")
                .platformCategory(PlatFormCategoryEnum.XIN_WEN.getValue())
                .interfaceUrl("/api/topsearch/zhongguoxinwenwang")
                .hotTitleUrlPrefix("https:")
                .addArea()
                .rootSelector("#YwNes > .news-left:nth-of-type(2) .rdph-list2:nth-of-type(2) li")
                .titleSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://www.myzaker.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("ZAKER")
                .platformCategory(PlatFormCategoryEnum.XIN_WEN.getValue())
                .interfaceUrl("/api/topsearch/zaker")
                .hotTitleUrlPrefix("https:")
                .addArea()
                .rootSelector(".seo-list > div")
                .titleSelector("h2")
                .urlSelector(".article-wrap > a")
                .register();

        addDomConfig("https://www.bjnews.com.cn/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("新京报")
                .platformCategory(PlatFormCategoryEnum.XIN_WEN.getValue())
                .interfaceUrl("/api/topsearch/xinjingbao")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".hotComment.ranking > ul > li")
                .titleSelector("h3 > a")
                .urlSelector("h3 > a")
                .register();

        addDomConfig("https://www.stnn.cc/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("星岛环球")
                .platformCategory(PlatFormCategoryEnum.XIN_WEN.getValue())
                .interfaceUrl("/api/topsearch/xingdaohuanqiu")
                .hotTitleUrlPrefix("https://www.stnn.cc")
                .addArea()
                .rootSelector(".n_left_l > .hs li")
                .titleSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://www.21jingji.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("21经济网")
                .platformCategory(PlatFormCategoryEnum.CAI_JING.getValue())
                .interfaceUrl("/api/topsearch/21jingjiwang")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".hot-list > li")
                .titleSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://finance.eastmoney.com/yaowen.html")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("东方财富网")
                .platformCategory(PlatFormCategoryEnum.CAI_JING.getValue())
                .interfaceUrl("/api/topsearch/dongfangcaifuwang")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("#artitileList1 > ul > li")
                .titleSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://news.mbalib.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("MBA智库")
                .platformCategory(PlatFormCategoryEnum.CAI_JING.getValue())
                .interfaceUrl("/api/topsearch/mbazhiku")
                .hotTitleUrlPrefix("https://news.mbalib.com")
                .addArea()
                .rootSelector(".latest-article")
                .titleSelector("h3 > a")
                .urlSelector("h3 > a")
                .register();

        addDomConfig("https://www.eeo.com.cn/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("经济观察网")
                .platformCategory(PlatFormCategoryEnum.CAI_JING.getValue())
                .interfaceUrl("/api/topsearch/jingjiguanchawang")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".box_R_item.line.news > div li")
                .titleSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://www.time-weekly.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("时代在线")
                .platformCategory(PlatFormCategoryEnum.CAI_JING.getValue())
                .interfaceUrl("/api/topsearch/shidaizaixian")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".t4_left > a")
                .titleSelector(".t4_block_text")
                .urlSelector(":root")
                .register();

        addDomConfig("https://www.jinse.cn/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("金色财经")
                .platformCategory(PlatFormCategoryEnum.CAI_JING.getValue())
                .interfaceUrl("/api/topsearch/jingsecaijing")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".js-main__l .js-article_item")
                .titleSelector(".js-article_item__title")
                .urlSelector(".js-article_item__title")
                .register();

        addDomConfig("https://finance.sina.com.cn/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("新浪财经")
                .platformCategory(PlatFormCategoryEnum.CAI_JING.getValue())
                .interfaceUrl("/api/topsearch/xinlangcaijing")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("#blk_hdline_01 a")
                .titleSelector(":root")
                .urlSelector(":root")
                .rootSelector(".m-p1-mb1-list.m-list-container a")
                .titleSelector(":root")
                .urlSelector(":root")
                .register();

        addJsonConfig("https://apim.ningmengyun.com/api/News/AppNewsListV3?index=0&pageSize=20&column=1010")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("会计头条")
                .platformCategory(PlatFormCategoryEnum.CAI_JING.getValue())
                .interfaceUrl("/api/topsearch/kuaijitoutiao")
                .hotTitleUrlPrefix("https://www.kuaijitoutiao.com/article/")
                .addArea()
                .rootSelector("$.data.data")
                .titleSelector("$.appNews.title")
                .urlSelector("$.appNews.articleID")
                .register();

        addDomConfig("http://www.laohucaijing.com/news_index/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("老虎财经")
                .platformCategory(PlatFormCategoryEnum.CAI_JING.getValue())
                .interfaceUrl("/api/topsearch/laohucaijing")
                .hotTitleUrlPrefix("http://www.laohucaijing.com")
                .addArea()
                .rootSelector(".Recommend a")
                .titleSelector(":root")
                .urlSelector(":root")
                .addArea()
                .rootSelector(".content_list h3")
                .titleSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://www.theblockbeats.info/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("BlockBeats")
                .platformCategory(PlatFormCategoryEnum.CAI_JING.getValue())
                .interfaceUrl("/api/topsearch/blockbeats")
                .hotTitleUrlPrefix("https://www.theblockbeats.info")
                .addArea()
                .rootSelector(".home-page-ctr-contain  .title-item")
                .titleSelector(":root")
                .urlSelector(":root")
                .register();

        addDomConfig("https://news.fx678.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("汇通财经")
                .platformCategory(PlatFormCategoryEnum.CAI_JING.getValue())
                .interfaceUrl("/api/topsearch/huitongcaijing")
                .hotTitleUrlPrefix("https://news.fx678.com")
                .addArea()
                .rootSelector(".col4_r_txts.l  li")
                .titleSelector("a")
                .urlSelector("a")
                .register();

        addJsonConfig("https://www.nbd.com.cn/news-rank-nr-h5/rank_index/news")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("每经网")
                .platformCategory(PlatFormCategoryEnum.CAI_JING.getValue())
                .interfaceUrl("/api/topsearch/meijingwang")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.list")
                .titleSelector("$.title")
                .urlSelector("$.url")
                .hotScoreSelector("$.score_num")
                .register();

        addDomConfig("https://xuangutong.com.cn/jingxuan")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("选股通")
                .platformCategory(PlatFormCategoryEnum.CAI_JING.getValue())
                .interfaceUrl("/api/topsearch/xuangutong")
                .hotTitleUrlPrefix("https://xuangutong.com.cn")
                .addArea()
                .rootSelector(".article-wrapper_iIO5l")
                .titleSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://www.chaincatcher.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("Chain Catcher")
                .platformCategory(PlatFormCategoryEnum.CAI_JING.getValue())
                .interfaceUrl("/api/topsearch/chaincatcher")
                .hotTitleUrlPrefix("https://www.chaincatcher.com")
                .addArea()
                .rootSelector(".left_area.pr-2.col-lg-9.col-12 .article_left")
                .titleSelector("a > h3")
                .urlSelector("a")
                .register();

        addDomConfig("https://www.chinastarmarket.cn/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("科创板日报")
                .platformCategory(PlatFormCategoryEnum.CAI_JING.getValue())
                .interfaceUrl("/api/topsearch/kechuangbanribao")
                .hotTitleUrlPrefix("https://www.chinastarmarket.cn")
                .addArea()
                .rootSelector(".d-f.m-t-30.m-b-30")
                .titleSelector(".d-b.o-h.f-s-20.f-w-b.l-h-15.c-000.w-s-n.t-o-el.list-link")
                .urlSelector(".d-b.o-h.f-s-20.f-w-b.l-h-15.c-000.w-s-n.t-o-el.list-link")
                .register();

        addDomConfig("https://topd.tencent.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("腾讯设计开放平台")
                .platformCategory(PlatFormCategoryEnum.SHE_JI.getValue())
                .interfaceUrl("/api/topsearch/tengxunshejikaifangpingtai")
                .hotTitleUrlPrefix("https://topd.tencent.com")
                .addArea()
                .rootSelector(".li-work")
                .titleSelector(".title")
                .urlSelector(".title")
                .register();

        addDomConfig("https://developer.aliyun.com/indexFeed/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("阿里云社区")
                .platformCategory(PlatFormCategoryEnum.KE_JI.getValue())
                .interfaceUrl("/api/topsearch/aliyunshequ")
                .hotTitleUrlPrefix("https://developer.aliyun.com")
                .addArea()
                .rootSelector(".feed-list > li")
                .titleSelector("a")
                .urlSelector("a")
                .hotScoreSelector(".feed-article-count")
                .register();

        addDomConfig("https://cloud.tencent.com/developer")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("腾讯云社区")
                .platformCategory(PlatFormCategoryEnum.KE_JI.getValue())
                .interfaceUrl("/api/topsearch/tengxunyunshequ")
                .hotTitleUrlPrefix("https://cloud.tencent.com")
                .addArea()
                .rootSelector(".mod-article-list > .cdc-article-panel-response2__list > div")
                .titleSelector(".cdc-article-panel-response2__title")
                .urlSelector(".cdc-article-panel-response2__title")
                .hotScoreSelector(".cdc-icon__number")
                .register();

        addDomConfig("https://tech.meituan.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("美团社区")
                .platformCategory(PlatFormCategoryEnum.KE_JI.getValue())
                .interfaceUrl("/api/topsearch/meituanshequ")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("#J_main-container > .row.post-container-wrapper .post-container")
                .titleSelector(".post-title > a")
                .urlSelector(".post-title > a")
                .register();

        addDomConfig("http://www.0818tuan.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .hotTitleNeedDeal(true)
                .platformName("0818团")
                .platformCategory(PlatFormCategoryEnum.YANG_MAO.getValue())
                .interfaceUrl("/api/topsearch/0818tuan")
                .hotTitleUrlPrefix("http://www.0818tuan.com")
                .addArea()
                .rootSelector(".col-md-8:nth-of-type(1) > .panel.panel-default > .list-group > a")
                .titleSelector(":root")
                .urlSelector(":root")
                .register();


        addDomConfig("https://news.qoo-app.com/category/news-zh/{type}")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("QooApp")
                .platformCategory(PlatFormCategoryEnum.QOOAPP.getValue())
                .interfaceUrl("/api/topsearch/qooapp/{type}")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".generate-columns-container > article")
                .titleSelector(".entry-title > a")
                .urlSelector(".entry-title > a")
                .register();

        addDomConfig("https://gnn.gamer.com.tw/index.php?k={type}")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("巴哈姆特")
                .platformCategory(PlatFormCategoryEnum.BA_HA_MU_TE.getValue())
                .interfaceUrl("/api/topsearch/bahamute/{type}")
                .hotTitleUrlPrefix("https:")
                .addArea()
                .rootSelector(".BH-lbox.GN-lbox2 > .GN-lbox2B")
                .titleSelector(".GN-lbox2D")
                .urlSelector(".GN-lbox2D > a")

                .forestRequestHeader(new ForestRequestHeader()
                        .setUserAgent(UserAgentGeneratorUtil.generateRandomUserAgent())
                        .setHost("gnn.gamer.com.tw")
                )
                .register();

        addDomConfig("https://www.4gamer.net/{type}")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("4Gamer")
                .platformCategory(PlatFormCategoryEnum.FOUR_GAMER.getValue())
                .interfaceUrl("/api/topsearch/4gamer/{type}")
                .hotTitleUrlPrefix("https://www.4gamer.net/")
                // 置顶的3个
                .addArea()
                .rootSelector(".top_article_top3 > dd")
                .titleSelector("a")
                .urlSelector("a")
                // 下面两个是专门针对pc分类
                .addArea()
                .rootSelector("#NEWS_SELECT_DAY_1 .V2_article_container")
                .titleSelector("h2 > a")
                .urlSelector("h2 > a")
                .addArea()
                .rootSelector("#NEWS_SELECT_DAY_2 .V2_article_container")
                .titleSelector("h2 > a")
                .urlSelector("h2 > a")
                // 除pc外的其他分类
                .addArea()
                .rootSelector("#NEWS_SELECT_RECENT_50 .V2_article_container")
                .titleSelector("h2 > a")
                .urlSelector("h2 > a")
                .register();

        addJsonConfig("https://api.gamebase.com.tw/api/news/getNewsList")
                .requestType(ForestRequestTypeEnum.POST)
                .jsonBodyNeedDeal(true)
                .platformName("gamebase")
                .platformCategory(PlatFormCategoryEnum.GAME_BASE.getValue())
                .interfaceUrl("/api/topsearch/gamebase/{type}")
                .hotTitleUrlPrefix("https://news.gamebase.com.tw/news/detail/")
                .addArea()
                .rootSelector("$.return_msg.list")
                .titleSelector("$.news_title")
                .urlSelector("$.news_no")
                .jsonBody(
                        new HashMap<>(Map.of(
                                "GB_type", "newsList",
                                "category", "",
                                "page", 1
                        ))
                )
                .register();

        addDomConfig("https://www.nodeloc.com/{type}")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("nodeloc")
                .platformCategory(PlatFormCategoryEnum.NODELOC.getValue())
                .interfaceUrl("/api/topsearch/nodeloc/{type}")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".topic-list-item")
                .titleSelector(".title.raw-link.raw-topic-link")
                .urlSelector(".title.raw-link.raw-topic-link")
                .hotScoreSelector(".views")
                .register();

        addJsonConfig("https://hub.baai.ac.cn/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("智源社区")
                .platformCategory(PlatFormCategoryEnum.KE_JI.getValue())
                .interfaceUrl("/api/topsearch/zhiyuanshequ")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$")
                .titleSelector("$.story_info.title")
                .urlSelector("$.story_info.url")
                .register();

        addDomConfig("https://www.qbitai.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("量子位")
                .platformCategory(PlatFormCategoryEnum.KE_JI.getValue())
                .interfaceUrl("/api/topsearch/liangziwei")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".article_list > .picture_text")
                .titleSelector("h4 > a")
                .urlSelector("h4 > a")
                .register();

        addDomConfig("https://aiera.com.cn/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("新智元")
                .platformCategory(PlatFormCategoryEnum.KE_JI.getValue())
                .interfaceUrl("/api/topsearch/xinzhiyuan")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".entries > article")
                .titleSelector("h2 > a")
                .urlSelector("h2 > a")
                .register();








    }
}