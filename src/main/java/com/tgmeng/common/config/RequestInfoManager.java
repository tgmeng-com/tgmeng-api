package com.tgmeng.common.config;

import cn.hutool.core.util.ObjectUtil;
import com.tgmeng.common.enums.system.ResponseTypeEnum;
import com.tgmeng.common.exception.ServerException;
import com.tgmeng.common.forest.header.ForestRequestHeader;
import com.tgmeng.common.forest.httptype.ForestRequestTypeEnum;
import com.tgmeng.common.util.HttpRequestUtil;
import com.tgmeng.common.util.TimeUtil;
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
        private String keyword = "";
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

        public FluentConfig keywordSelector(String v) {
            currentSelector.keyword(v);
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
                .platformCategory("财经")
                .hotScoreUrlPrefix("https://www.zhitongcaijing.com")
                .interfaceUrl("/api/topsearch/zhitongcaijing")
                .addArea()
                .rootSelector("#news-article-box > .shadow-wrap-box")
                .keywordSelector(".info-item-content-title a")
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
        //             .keywordSelector(".title a")
        //             .urlSelector("a")
        //             .descSelector(".description")
        //             .hotScoreSelector(".hot-value")
        //             
        //         // 区域2：热门推荐
        //         .addArea()
        //             .rootSelector("#hot-recommend > .hot-item")
        //             .keywordSelector(".hot-title")
        //             .urlSelector("a")
        //             .hotScoreSelector(".score")
        //             .imageSelector("img")
        //             
        //         // 区域3：编辑精选
        //         .addArea()
        //             .rootSelector(".editor-choice .article")
        //             .keywordSelector("h3")
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
                .platformCategory("新闻")
                .interfaceUrl("/api/topsearch/pengpaixinwen")
                .hotTitleUrlPrefix("https://www.thepaper.cn/newsDetail_forward_")
                .addArea()
                .rootSelector("$.data.hotNews")
                .keywordSelector("$.name")
                .urlSelector("$.contId")

                .register();

        addDomConfig("https://www.jksb.com.cn/newslist/posid/8")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("健康时报")
                .platformCategory("健康")
                .interfaceUrl("/api/topsearch/jiankangshibaowang")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("#list_url > li")
                .keywordSelector("h1 > a")
                .urlSelector("h1 > a")

                .register();

        addDomConfig("https://www.guokr.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("果壳")
                .platformCategory("健康")
                .interfaceUrl("/api/topsearch/guoke")
                .hotTitleUrlPrefix("https://www.guokr.com")
                .addArea()
                .rootSelector(".FeedFloat__FeedFloatWrap-zt5yna-0 > a, .FeedFloat__FeedFloatWrap-zt5yna-0 > * > a")
                .keywordSelector(".eAYHrP")
                .urlSelector("a")

                .register();

        addDomConfig("https://www.familydoctor.cn/article/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("家医大健康")
                .platformCategory("健康")
                .interfaceUrl("/api/topsearch/jiayidajiankang")
                .hotTitleUrlPrefix("https://www.familydoctor.cn")
                .addArea()
                .rootSelector(".news-item")
                .keywordSelector(".news-title > a")
                .urlSelector(".news-title > a")

                .register();

        addDomConfig("https://www.lifetimes.cn/news")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("生命时报")
                .platformCategory("健康")
                .interfaceUrl("/api/topsearch/shengmingshibao")
                .hotTitleUrlPrefix("https:")
                .addArea()
                .rootSelector(".sUl > li")
                .keywordSelector(".oP-txt a")
                .urlSelector(".oP-txt a")

                .register();

        addDomConfig("https://www.dxy.cn/bbs/newweb/pc/home?tab=3")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("丁香园社区")
                .platformCategory("健康")
                .interfaceUrl("/api/topsearch/dingxiangyuanshequ")
                .hotTitleUrlPrefix("https://www.dxy.cn")
                .addArea()
                .rootSelector(".HotPost_wrapper__7S2FG")
                .keywordSelector(".HotPost_titleWrapper__PBxkk")
                .urlSelector(".HotPost_titleWrapper__PBxkk")

                .register();

        addDomConfig("https://dxy.com/articles")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("丁香医生")
                .platformCategory("健康")
                .interfaceUrl("/api/topsearch/dingxiangyisheng")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".article-card")
                .keywordSelector("a")
                .urlSelector("a")

                .register();

        addDomConfig("https://bydrug.pharmcube.com/news")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("医药魔方")
                .platformCategory("健康")
                .interfaceUrl("/api/topsearch/yiyaomofang")
                .hotTitleUrlPrefix("https://bydrug.pharmcube.com")
                .addArea()
                .rootSelector(".ant-spin-container > div")
                .keywordSelector(".mf-font-600")
                .urlSelector(".mf-font-600")

                .register();

        addDomConfig("https://www.bioon.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("生物谷")
                .platformCategory("健康")
                .interfaceUrl("/api/topsearch/shengwugu")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".composs-blog-list > .item")
                .keywordSelector("h2 > a")
                .urlSelector("h2 > a")

                .register();

        addDomConfig("https://www.ali213.net/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("游侠网")
                .platformCategory("游戏")
                .interfaceUrl("/api/topsearch/youxiawang")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".s1-m-li.rdzx-ul a")
                .keywordSelector("a")
                .urlSelector("a")

                .register();

        addDomConfig("https://news.17173.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("17173")
                .platformCategory("游戏")
                .interfaceUrl("/api/topsearch/17173")
                .hotTitleUrlPrefix("https:")
                .addArea()
                .rootSelector(".ptlist.ptlist-news.adNewsIndexYaoWen > li")
                .keywordSelector(".tit > a")
                .urlSelector(".tit > a")

                .register();

        addDomConfig("https://www.yystv.cn/docs")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("游研社")
                .platformCategory("游戏")
                .interfaceUrl("/api/topsearch/youyanshe")
                .hotTitleUrlPrefix("https://www.yystv.cn")
                .addArea()
                .rootSelector(".articles-item")
                .keywordSelector("h2")
                .urlSelector("a")

                .register();

        addDomConfig("https://www.gcores.com/news")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("GCORES")
                .platformCategory("游戏")
                .interfaceUrl("/api/topsearch/gcores")
                .hotTitleUrlPrefix("https://www.gcores.com")
                .addArea()
                .rootSelector(".col.mb-5")
                .keywordSelector("h3")
                .urlSelector("a")

                .register();

        addDomConfig("https://www.ign.com.cn/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("IGN")
                .platformCategory("游戏")
                .interfaceUrl("/api/topsearch/ign")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".broll.wrap h3")
                .keywordSelector("a")
                .urlSelector("a")

                .register();

        addDomConfig("https://www.youxituoluo.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("游戏陀螺")
                .platformCategory("游戏")
                .interfaceUrl("/api/topsearch/youxituoluo")
                .hotTitleUrlPrefix("https://www.youxituoluo.com")
                .addArea()
                .rootSelector(".article_list > li")
                .keywordSelector(".title")
                .urlSelector(".title")

                .register();

        addDomConfig("https://www.a9vg.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("A9VG")
                .platformCategory("游戏")
                .interfaceUrl("/api/topsearch/a9vg")
                .hotTitleUrlPrefix("https://www.a9vg.com")
                .addArea()
                .rootSelector(".a9-plain-list_item")
                .keywordSelector("a")
                .urlSelector("a")

                .register();

        addDomConfig("https://www.3dmgame.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("3DMGAME")
                .platformCategory("游戏")
                .interfaceUrl("/api/topsearch/3dmgame")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".Indexbox2-2 > .switchbox a")
                .keywordSelector("a")
                .urlSelector("a")

                .register();

        addDomConfig("https://www.gamersky.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("游民星空")
                .platformCategory("游戏")
                .interfaceUrl("/api/topsearch/youminxingkong")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".bgx a")
                .keywordSelector("a")
                .urlSelector("a")

                .register();

        addDomConfig("https://news.ycombinator.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("HACKER_NEWS")
                .platformCategory("社区")
                .interfaceUrl("/api/topsearch/hackernews")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".athing.submission")
                .keywordSelector(".titleline > a:first-of-type")
                .urlSelector(".titleline > a:first-of-type")

                .register();

        addJsonConfig("https://api.juejin.cn/content_api/v1/content/article_rank?category_id=1&type=hot")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("掘金文章")
                .platformCategory("社区")
                .interfaceUrl("/api/topsearch/wenzhangjuejin")
                .hotTitleUrlPrefix("https://juejin.cn/post/")
                .addArea()
                .rootSelector("$.data")
                .keywordSelector("$.content.title")
                .urlSelector("$.content.content_id")
                .hotScoreSelector("$.content_counter.hot_rank")

                .register();

        addDomConfig("https://www.1point3acres.com/bbs/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("一亩三分地")
                .platformCategory("社区")
                .interfaceUrl("/api/topsearch/yimusanfendi")
                .hotTitleUrlPrefix("https://www.1point3acres.com/bbs/")
                .addArea()
                .rootSelector("#portal_block_439_content li")
                .keywordSelector("a:first-of-type")
                .urlSelector("a:first-of-type")

                .register();

        addDomConfig("https://nga.cn/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("NGA")
                .platformCategory("社区")
                .interfaceUrl("/api/topsearch/nga")
                .hotTitleUrlPrefix("https:")
                .addArea()
                .rootSelector("#topic_ladder_cat_3 > li")
                .keywordSelector("a")
                .urlSelector("a")

                .register();

        addDomConfig("https://bbs.hupu.com/all-gambia")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("步行街虎扑")
                .platformCategory("社区")
                .interfaceUrl("/api/topsearch/buxingjiehupu")
                .hotTitleUrlPrefix("https://bbs.hupu.com")
                .addArea()
                .rootSelector(".text-list-model > div")
                .keywordSelector(".t-title")
                .urlSelector("a")
                .hotScoreSelector(".t-replies")

                .register();

        addDomConfig("https://www.v2ex.com/?tab=hot")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("V2EX")
                .platformCategory("社区")
                .interfaceUrl("/api/topsearch/v2ex")
                .hotTitleUrlPrefix("https://www.v2ex.com")
                .addArea()
                .rootSelector(".cell.item")
                .keywordSelector(".topic-link")
                .urlSelector(".topic-link")
                .hotScoreSelector(".t-count_livid")

                .register();

        addDomConfig("https://www.zhibo8.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("直播吧")
                .platformCategory("体育")
                .interfaceUrl("/api/topsearch/zhiboba")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".zuqiu-news .list-item")
                .keywordSelector("a")
                .urlSelector("a")

                .addArea()
                .rootSelector(".lanqiu-news .list-item")
                .keywordSelector("a")
                .urlSelector("a")

                .register();

        addDomConfig("https://www.ppsport.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("PP体育")
                .platformCategory("体育")
                .interfaceUrl("/api/topsearch/pptiyu")
                .hotTitleUrlPrefix("https://www.ppsport.com")
                .addArea()
                .rootSelector(".info-panel-wrap > div")
                .keywordSelector(".tw-link")
                .urlSelector(".tw-link")

                .register();

        addDomConfig("https://sports.cctv.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("央视体育")
                .platformCategory("体育")
                .interfaceUrl("/api/topsearch/yangshitiyu")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("#plantingtext li")
                .keywordSelector("a")
                .urlSelector("a")

                .register();

        addDomConfig("https://sports.163.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("网易体育")
                .platformCategory("体育")
                .interfaceUrl("/api/topsearch/tiyuwangyi")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".channel_news_body li")
                .keywordSelector("a")
                .urlSelector("a")

                .register();

        addDomConfig("https://sports.sohu.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("搜狐体育")
                .platformCategory("体育")
                .interfaceUrl("/api/topsearch/souhutiyu")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".feed-group > a")
                .keywordSelector("a")
                .urlSelector("a")

                .register();

        addDomConfig("https://sports.sina.com.cn/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("新浪体育")
                .platformCategory("体育")
                .interfaceUrl("/api/topsearch/xinlangtiyu")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("#hot-search-list > div")
                .keywordSelector("a")
                .urlSelector("a")

                .register();

        addDomConfig("https://www.dongqiudi.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("懂球帝")
                .platformCategory("体育")
                .interfaceUrl("/api/topsearch/dongqiudi")
                .hotTitleUrlPrefix("https://www.dongqiudi.com")
                .addArea()
                .rootSelector(".top-center > p")
                .keywordSelector("a")
                .urlSelector("a")

                .register();

        addDomConfig("https://www.hupu.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("虎扑")
                .platformCategory("体育")
                .interfaceUrl("/api/topsearch/hupu")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".test-img-list-model > div")
                .keywordSelector(".list-item-title")
                .urlSelector(".list-item-title")

                .register();

        addJsonConfig("https://www.jiqizhixin.com/api/article_library/articles.json?sort=time&page=1&per=12")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("机器之心")
                .platformCategory("科技")
                .interfaceUrl("/api/topsearch/jiqizhixin")
                .hotTitleUrlPrefix("https://www.jiqizhixin.com/articles/")
                .addArea()
                .rootSelector("$.articles")
                .keywordSelector("$.title")
                .urlSelector("$.slug")

                .register();

        addDomConfig("https://www.eurekalert.org/language/chinese/home")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("EurekAlert")
                .platformCategory("科技")
                .interfaceUrl("/api/topsearch/eurekalert")
                .hotTitleUrlPrefix("https://www.eurekalert.org")
                .addArea()
                .rootSelector("#main-content > .post")
                .keywordSelector("h2")
                .urlSelector("a")

                .register();

        addJsonConfig("https://apii.web.mittrchina.com/flash")
                .requestType(ForestRequestTypeEnum.POST)
                .platformName("MIT")
                .platformCategory("科技")
                .interfaceUrl("/api/topsearch/mit")
                .hotTitleUrlPrefix("")
                .hotTitleUrlNeedDeal(true)
                .addArea()
                .rootSelector("$.data.items")
                .keywordSelector("$.name")
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
                .platformCategory("设计")
                .interfaceUrl("/api/topsearch/abduzeedo")
                .hotTitleUrlPrefix("https://abduzeedo.com")
                .addArea()
                .rootSelector(".posts > article")
                .keywordSelector("span > h2 > a")
                .urlSelector("span > h2 > a")

                .register();

        addDomConfig("https://www.core77.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("Core77")
                .platformCategory("设计")
                .interfaceUrl("/api/topsearch/core77")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".post_list_prime li")
                .keywordSelector("h1 > a")
                .urlSelector("h1 > a")

                .register();

        addDomConfig("https://www.awwwards.com/websites/sites_of_the_day/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("Awwwards")
                .platformCategory("设计")
                .interfaceUrl("/api/topsearch/awwwards")
                .hotTitleUrlPrefix("https://www.awwwards.com")
                .addArea()
                .rootSelector(".grid-cards > li")
                .keywordSelector(".avatar-name__title")
                .urlSelector(".figure-rollover__link")

                .forestRequestHeader(new ForestRequestHeader()
                        .setReferer(HttpRequestUtil.getRequestReferer("https://www.behance.net/"))
                        .setOrigin(HttpRequestUtil.getRequestOrigin("https://www.behance.net"))
                )
                .register();

        addDomConfig("https://dribbble.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("Dribbble")
                .platformCategory("设计")
                .interfaceUrl("/api/topsearch/dribbble")
                .hotTitleUrlPrefix("https://dribbble.com")
                .addArea()
                .rootSelector("#main > ol > li")
                .keywordSelector(".display-name")
                .urlSelector(".shot-thumbnail-link")
                .hotScoreSelector(".js-shot-views-count")

                .register();

        addDomConfig("https://www.archdaily.cn/cn?ad_source=jv-header")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("ArchDaily")
                .platformCategory("设计")
                .interfaceUrl("/api/topsearch/archdaily")
                .hotTitleUrlPrefix("https://www.archdaily.cn")
                .addArea()
                .rootSelector(".afd-post-stream")
                .keywordSelector("h3 span")
                .urlSelector("h3 a")

                .register();

        addDomConfig("https://www.topys.cn/category/12")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("TOPYS")
                .platformCategory("设计")
                .interfaceUrl("/api/topsearch/topys")
                .hotTitleUrlPrefix("https://www.topys.cn")
                .addArea()
                .rootSelector(".article-box-item")
                .keywordSelector(".title")
                .urlSelector(".title")

                .register();

        addDomConfig("https://www.shejidaren.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("设计达人")
                .platformCategory("设计")
                .interfaceUrl("/api/topsearch/shejidaren")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".indexbody .post")
                .keywordSelector("h2 > a")
                .urlSelector("h2 > a")

                .register();

        addDomConfig("https://hot.uisdc.com/posts")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("优设网")
                .platformCategory("设计")
                .interfaceUrl("/api/topsearch/youshewang")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".p-items > .p-item")
                .keywordSelector("h2 > a")
                .urlSelector("h2 > a")
                .hotScoreSelector(".meta-views")

                .register();

        addJsonConfig("https://www.woshipm.com/api2/app/article/popular/daily")
                .requestType(ForestRequestTypeEnum.GET)
                .hotTitleUrlNeedDeal(true)
                .platformName("人人都是产品经理")
                .platformCategory("设计")
                .interfaceUrl("/api/topsearch/woshipm")
                .hotTitleUrlPrefix("https://www.woshipm.com/")
                .addArea()
                .rootSelector("$.RESULT")
                .keywordSelector("$.data.articleTitle")
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
                .platformCategory("新闻")
                .interfaceUrl("/api/topsearch/huaerjieribao")
                .hotTitleUrlPrefix("https://www.rfi.fr")
                .addArea()
                .rootSelector(".css-1rznr30-CardLink")
                .keywordSelector("")
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
                .platformCategory("新闻")
                .interfaceUrl("/api/topsearch/faguang")
                .hotTitleUrlPrefix("https://www.rfi.fr")
                .addArea()
                .rootSelector(".o-banana-split .article__title")
                .keywordSelector("a")
                .urlSelector("a")

                .forestRequestHeader(new ForestRequestHeader()
                        .setReferer("https://www.rfi.fr/cn/")
                        .setOrigin("https://www.rfi.fr/cn")
                )
                .register();

        addDomConfig("https://www.bbc.com/zhongwen/simp/popular/read")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("BBC")
                .platformCategory("新闻")
                .interfaceUrl("/api/topsearch/bbc")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("main[role='main']  li[role='listitem']")
                .keywordSelector("a")
                .urlSelector("a")

                .register();

        addDomConfig("https://m.cn.nytimes.com/world/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("纽约时报")
                .platformCategory("新闻")
                .interfaceUrl("/api/topsearch/niuyueshibao")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".article-list > li")
                .keywordSelector("h2")
                .urlSelector("a")

                .register();

        addDomConfig("https://xnews.jin10.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("金十")
                .platformCategory("财经")
                .interfaceUrl("/api/topsearch/jinshi")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".jin10-news-index-list > .jin10-news-list > div")
                .keywordSelector("p")
                .urlSelector(".jin10-news-list-item-info > a")

                .register();

        addDomConfig("https://www.fastbull.com/cn/news")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("法布")
                .platformCategory("财经")
                .interfaceUrl("/api/topsearch/fabu")
                .hotTitleUrlPrefix("https://www.fastbull.com")
                .addArea()
                .rootSelector(".news-top a")
                .keywordSelector("h4")
                .urlSelector("a")

                .register();

        addDomConfig("https://www.gelonghui.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("格隆汇")
                .platformCategory("财经")
                .interfaceUrl("/api/topsearch/gelonghui")
                .hotTitleUrlPrefix("https://www.gelonghui.com")
                .addArea()
                .rootSelector("#hot-article ul:first-of-type li")
                .keywordSelector("a")
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
                .platformCategory("财经")
                .interfaceUrl("/api/topsearch/cailianshe")
                .hotTitleUrlPrefix("https://www.cls.cn")
                .addArea()
                .rootSelector(".home-article-ranking-box > div")
                .keywordSelector("a")
                .urlSelector("a")

                .register();

        addJsonConfig("https://api-one-wscn.awtmt.com/apiv1/content/information-flow?channel=global&accept=article&cursor=&limit=20&action=upglide")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("华尔街见闻")
                .platformCategory("财经")
                .interfaceUrl("/api/topsearch/huaerjiejianwen")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.items")
                .keywordSelector("$.resource.title")
                .urlSelector("$.resource.uri")

                .register();

        addDomConfig("https://news.10jqka.com.cn/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("同花顺")
                .platformCategory("财经")
                .interfaceUrl("/api/topsearch/tonghuashun")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".list-con ul li")
                .keywordSelector("a")
                .urlSelector("a")

                .register();

        addDomConfig("https://www.yicai.com")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("第一财经")
                .platformCategory("财经")
                .interfaceUrl("/api/topsearch/diyicaijing")
                .hotTitleUrlPrefix("https://www.yicai.com")
                .addArea()
                .rootSelector("#headlist > a")
                .keywordSelector("h2")
                .urlSelector("a")

                .register();

        addJsonConfig("https://gateway.jrj.com/jrj-news/news/queryNewsList")
                .requestType(ForestRequestTypeEnum.POST)
                .platformName("金融界")
                .platformCategory("财经")
                .interfaceUrl("/api/topsearch/jinrongjie")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.data")
                .keywordSelector("$.title")
                .urlSelector("$.pcInfoUrl")

                .jsonBody(
                        Map.of(
                                "sortBy", 1,
                                "pageSize", 30,
                                "channelNum", "103"
                        )
                )
                .register();

        addJsonConfig("https://api.zhihu.com/topstory/hot-list?limit=100&reverse_order=0")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("知乎")
                .platformCategory("社区")
                .interfaceUrl("/api/topsearch/zhihu")
                .hotTitleUrlPrefix("https://www.zhihu.com/question/")
                .addArea()
                .rootSelector("$.data")
                .keywordSelector("$.target.title")
                .urlSelector("$.target.id")

                .register();

        addJsonConfig("https://sspai.com/api/v1/article/tag/page/get?limit=100&offset=0&tag=%E7%83%AD%E9%97%A8%E6%96%87%E7%AB%A0&released=false")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("少数派")
                .platformCategory("社区")
                .interfaceUrl("/api/topsearch/shaoshupai")
                .hotTitleUrlPrefix("https://sspai.com/post/")
                .addArea()
                .rootSelector("$.data")
                .keywordSelector("$.title")
                .urlSelector("$.id")
                .hotScoreSelector("$.comment_count")

                .register();

        addJsonConfig("https://tieba.baidu.com/hottopic/browse/topicList")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("百度贴吧")
                .platformCategory("社区")
                .interfaceUrl("/api/topsearch/tiebabaidu")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.bang_topic.topic_list")
                .keywordSelector("$.topic_name")
                .urlSelector("$.topic_url")
                .hotScoreSelector("$.discuss_num")
                .register();

        addJsonConfig("https://gw.m.163.com/nc-main/api/v1/hqc/no-repeat-hot-list?source=hotTag")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("网易")
                .platformCategory("新闻")
                .interfaceUrl("/api/topsearch/wangyi")
                .hotTitleUrlPrefix("https://c.m.163.com/news/a/")
                .hotTitleUrlAfter(".html")
                .addArea()
                .rootSelector("$.data.items")
                .keywordSelector("$.title")
                .urlSelector("$.contentId")
                .hotScoreSelector("$.hotValue")
                .register();

        addJsonConfig("https://www.toutiao.com/hot-event/hot-board/?origin=toutiao_pc")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("头条")
                .platformCategory("新闻")
                .interfaceUrl("/api/topsearch/toutiao")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.fixed_top_data")
                .keywordSelector("$.Title")
                .urlSelector("$.Url")
                .addArea()
                .rootSelector("$.data")
                .keywordSelector("$.Title")
                .urlSelector("$.Url")
                .hotScoreSelector("$.HotValue")
                .register();

        addJsonConfig("https://i.news.qq.com/gw/event/pc_hot_ranking_list?ids_hash=&offset=0&page_size=51&appver=15.5_qqnews_7.1.60&rank_id=hot")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("腾讯")
                .platformCategory("新闻")
                .interfaceUrl("/api/topsearch/tencent")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.idlist[0].newslist[1:]")
                .keywordSelector("$.title")
                .urlSelector("$.url")
                .hotScoreSelector("$.readCount")
                .register();

        addJsonConfig("https://m.douban.com/rexxar/api/v2/chart/hot_search_board?count=10&start=0")
                .requestType(ForestRequestTypeEnum.GET)
                .hotTitleUrlNeedDeal(true)
                .platformName("豆瓣")
                .platformCategory("新闻")
                .interfaceUrl("/api/topsearch/douban")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$")
                .keywordSelector("$.name")
                .hotScoreSelector("$.score")
                .register();

        addJsonConfig("https://www-hj.douyin.com/aweme/v1/web/hot/search/list/")
                .requestType(ForestRequestTypeEnum.GET)
                .hotTitleUrlNeedDeal(true)
                .platformName("抖音")
                .platformCategory("媒体")
                .interfaceUrl("/api/topsearch/douyin")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.word_list")
                .keywordSelector("$.word")
                .hotScoreSelector("$.hot_value")

                .register();

        addJsonConfig("https://weibo.com/ajax/statuses/hot_band")
                .requestType(ForestRequestTypeEnum.GET)
                .hotTitleUrlNeedDeal(true)
                .platformName("微博")
                .platformCategory("媒体")
                .interfaceUrl("/api/topsearch/weibo")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.hotgov")
                .keywordSelector("$.word")
                .urlSelector("$.url")

                .addArea()
                .rootSelector("$.data.band_list")
                .keywordSelector("$.note")
                .hotScoreSelector("$.num")

                .register();

        addJsonConfig("https://api.bilibili.com/x/web-interface/ranking/v2?rid=0&type=all")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("B站")
                .platformCategory("媒体")
                .interfaceUrl("/api/topsearch/bilibili")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.list")
                .keywordSelector("$.title")
                .urlSelector("$.short_link_v2")
                .hotScoreSelector("$.stat.view")

                .forestRequestHeader(new ForestRequestHeader()
                        .setUserAgent(UserAgentGeneratorUtil.generateRandomUserAgent())
                        .setAcceptEncoding("gzip, deflate, br, zstd")
                        .setAcceptLanguage("zh-CN,zh;q=0.9")
                        .setPriority("u=0, i")
                )
                .register();

        addJsonConfig("https://top.baidu.com/api/board?platform=wise&tab=realtime")
                .requestType(ForestRequestTypeEnum.GET)
                .hotTitleUrlNeedDeal(true)
                .platformName("百度")
                .platformCategory("新闻")
                .interfaceUrl("/api/topsearch/baidu")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.cards[0].topContent")
                .keywordSelector("$.word")
                .urlSelector("$.url")
                .hotScoreSelector("$.hotScore")

                .addArea()
                .rootSelector("$.data.cards[0].content")
                .keywordSelector("$.word")
                .urlSelector("$.url")
                .hotScoreSelector("$.hotScore")

                .register();

        addJsonConfig("https://top.baidu.com/api/board?platform=wise&tab={type}")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("百度电视剧")
                .platformCategory("baidu")
                .interfaceUrl("/api/topsearch/baidu/{type}")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.cards[0].content")
                .keywordSelector("$.word")
                .urlSelector("$.url")
                .hotScoreSelector("$.hotScore")

                .register();

        addJsonConfig("https://top.baidu.com/api/board?platform=wise&tab=novel")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("百度小说")
                .platformCategory("影视")
                .interfaceUrl("/api/topsearch/xiaoshuobaidu")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.cards[0].content")
                .keywordSelector("$.word")
                .urlSelector("$.url")
                .hotScoreSelector("$.hotScore")

                .register();

        addJsonConfig("https://top.baidu.com/api/board?platform=wise&tab=movie")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("百度电影")
                .platformCategory("影视")
                .interfaceUrl("/api/topsearch/dianyingbaidu")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.cards[0].content")
                .keywordSelector("$.word")
                .urlSelector("$.url")
                .hotScoreSelector("$.hotScore")

                .register();

        addJsonConfig("https://top.baidu.com/api/board?platform=wise&tab=game")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("百度游戏")
                .platformCategory("影视")
                .interfaceUrl("/api/topsearch/youxibaidu")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.cards[0].content")
                .keywordSelector("$.word")
                .urlSelector("$.url")
                .hotScoreSelector("$.hotScore")

                .register();

        addJsonConfig("https://top.baidu.com/api/board?platform=wise&tab=car")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("百度汽车")
                .platformCategory("影视")
                .interfaceUrl("/api/topsearch/qichebaidu")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.cards[0].content")
                .keywordSelector("$.word")
                .urlSelector("$.url")
                .hotScoreSelector("$.hotScore")

                .register();

        addJsonConfig("https://top.baidu.com/api/board?platform=wise&tab=phrase")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("百度热梗")
                .platformCategory("影视")
                .interfaceUrl("/api/topsearch/regengbaidu")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.cards[0].content")
                .keywordSelector("$.word")
                .urlSelector("$.url")
                .hotScoreSelector("$.hotScore")

                .register();

        addJsonConfig("https://top.baidu.com/api/board?platform=wise&tab=finance")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("百度财经")
                .platformCategory("影视")
                .interfaceUrl("/api/topsearch/caijingbaidu")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.cards[0].content")
                .keywordSelector("$.word")
                .urlSelector("$.url")
                .hotScoreSelector("$.hotScore")

                .register();

        addJsonConfig("https://top.baidu.com/api/board?platform=wise&tab=livelihood")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("百度民生")
                .platformCategory("影视")
                .interfaceUrl("/api/topsearch/minshengbaidu")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.cards[0].content")
                .keywordSelector("$.word")
                .urlSelector("$.url")
                .hotScoreSelector("$.hotScore")

                .register();


        addJsonConfig("https://music.163.com/api/playlist/detail?id=19723756")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("网易云飙升榜")
                .platformCategory("影音")
                .interfaceUrl("/api/topsearch/biaoshengwangyiyun")
                .hotTitleUrlPrefix("https://music.163.com/#/song?id=")
                .addArea()
                .rootSelector("$.result.tracks")
                .keywordSelector("$.name")
                .urlSelector("$.id")
                .hotScoreSelector("$.popularity")

                .register();

        addJsonConfig("https://music.163.com/api/playlist/detail?id={type}")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("网易云新歌榜")
                .platformCategory("wangyiyun")
                .interfaceUrl("/api/topsearch/wangyiyun/{type}")
                .hotTitleUrlPrefix("https://music.163.com/#/song?id=")
                .addArea()
                .rootSelector("$.result.tracks")
                .keywordSelector("$.name")
                .urlSelector("$.id")
                .hotScoreSelector("$.popularity")

                .register();

        addJsonConfig("https://music.163.com/api/playlist/detail?id=2884035")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("网易云原创榜")
                .platformCategory("影音")
                .interfaceUrl("/api/topsearch/yuanchuangwangyiyun")
                .hotTitleUrlPrefix("https://music.163.com/#/song?id=")
                .addArea()
                .rootSelector("$.result.tracks")
                .keywordSelector("$.name")
                .urlSelector("$.id")
                .hotScoreSelector("$.popularity")

                .register();

        addJsonConfig("https://music.163.com/api/playlist/detail?id=3778678")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("网易云热歌榜")
                .platformCategory("影音")
                .interfaceUrl("/api/topsearch/regewangyiyun")
                .hotTitleUrlPrefix("https://music.163.com/#/song?id=")
                .addArea()
                .rootSelector("$.result.tracks")
                .keywordSelector("$.name")
                .urlSelector("$.id")
                .hotScoreSelector("$.popularity")

                .register();

        addDomConfig("https://v.qq.com/biu/ranks/?t=hotsearch&channel={type}")
                .requestType(ForestRequestTypeEnum.GET)
                .hotScoreNeedDeal(true)
                .platformName("腾讯视频")
                .platformCategory("tencent")
                .interfaceUrl("/api/topsearch/tencent/{type}")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".table_list > li")
                .keywordSelector("div:nth-of-type(1) > a")
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
                .platformName("爱奇艺电视剧")
                .platformCategory("aiqiyi")
                .interfaceUrl("/api/topsearch/aiqiyi/{type}")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.items[0].contents")
                .keywordSelector("$.title")
                .urlSelector("$.pageUrl")
                .hotScoreSelector("$.mainIndex")

                .register();

        addJsonConfig("https://mobileso.bz.mgtv.com/pc/suggest/v1")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("芒果电视剧")
                .platformCategory("mangguo")
                .interfaceUrl("/api/topsearch/mangguo/{type}")
                .hotTitleUrlPrefix("https://so.mgtv.com/so?k=")
                .addArea()
                .rootSelector("$.data.topList[?(@.label=='{type}')].data[*]")
                .keywordSelector("$.name")
                .urlSelector("$.name")

                .register();

        addJsonConfig("https://m.maoyan.com/asgard/board?year=0&term=0&id={type}")
                .requestType(ForestRequestTypeEnum.GET)
                .hotScoreNeedDeal(true)
                .platformName("猫眼 榜单")
                .platformCategory("maoyan")
                .interfaceUrl("/api/topsearch/maoyan/{type}")
                .hotTitleUrlPrefix("https://www.maoyan.com/films/")
                .addArea()
                .rootSelector("$.data.data.movies")
                .keywordSelector("$.nm")
                .urlSelector("$.id")
                .imageSelector("$.img")
                .authorSelector("$.star")
                .descSelector("$.shortDec")
                .typeSelector("$.cat")
                .publishTimeSelector("$.pubDesc")
                .register();

        addDomConfig("https://www.zcool.com.cn/{type}")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("站酷文章榜")
                .platformCategory("zhanku")
                .interfaceUrl("/api/topsearch/zhanku/{type}")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".eTkjTE > section")
                .keywordSelector(".cnxaX > a")
                .urlSelector(".rankCard")
                .hotScoreSelector(".rankBox > div:nth-of-type(4) .kFzKPV")

                .register();

        addDomConfig("https://www.zcool.com.cn/potential#tab_anchor")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("站酷潜力榜")
                .platformCategory("设计")
                .interfaceUrl("/api/topsearch/zhankuqianlibang")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".eTkjTE > section")
                .keywordSelector(".cnxaX > a")
                .urlSelector(".rankCard")
                .hotScoreSelector(".rankBox > div:nth-of-type(4) .kFzKPV")

                .register();

        addDomConfig("https://www.zcool.com.cn/top/index.do#tab_anchor")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("站酷作品榜")
                .platformCategory("设计")
                .interfaceUrl("/api/topsearch/zhankuzuopinbang")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".eTkjTE > section")
                .keywordSelector(".cnxaX > a")
                .urlSelector(".rankCard")
                .hotScoreSelector(".rankBox > div:nth-of-type(4) .kFzKPV")

                .register();

        addDomConfig("https://www.gracg.com/showwork/{type}")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("涂鸦王国热门作品")
                .platformCategory("tuyawangguo")
                .interfaceUrl("/api/topsearch/tuyawangguo/{type}")
                .hotTitleUrlPrefix("https://www.gracg.com")
                .addArea()
                .rootSelector(".px-2 > div")
                .keywordSelector(".show-works-title")
                .urlSelector(".cursor-hand")
                .hotScoreSelector(".show-works-views > span")
                .register();

        addDomConfig("https://www.ncsti.gov.cn/kjdt/kjrd/{type}")
                .requestType(ForestRequestTypeEnum.GET)
                .hotTitleUrlNeedDeal(true)
                .platformName("人工智能国际科技创新中心")
                .platformCategory("guojikejichuangxinzhongxin")
                .interfaceUrl("/api/topsearch/guojikejichuangxinzhongxin/{type}")
                .addArea()
                .rootSelector(".news_box > ul:eq(2) > li")
                .keywordSelector("h2")

                .register();

        addJsonConfig("https://api.github.com/search/repositories?q=stars:>0+created:>={time}&sort=stars&order=desc&page=1&per_page=100")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("GitHub Star榜单")
                .platformCategory("GitHub")
                .interfaceUrl("/api/topsearch/github/stars/{type}")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.items")
                .keywordSelector("$.name")
                .urlSelector("$.html_url")
                .hotScoreSelector("$.stargazers_count")

                .register();

        addDomConfig("https://huggingface.co/{type}")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("HuggingFaces 榜单")
                .platformCategory("HuggingFaces")
                .interfaceUrl("/api/topsearch/global/huggingface/{type}")
                .hotTitleUrlPrefix("")
                .register();

        addJsonConfig("https://www.googleapis.com/youtube/v3/videos?part=snippet,statistics&chart=mostPopular&maxResults=50&key=AIzaSyCX494YE2KiRnmGT6Y8odk4SBnlTwIWH4A")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("Youtube榜单")
                .platformCategory("YouTube")
                .interfaceUrl("/api/topsearch/global/youtube")
                .hotTitleUrlPrefix("https://www.youtube.com/watch?v=")
                .addArea()
                .rootSelector("$.items")
                .keywordSelector("$.snippet.title")
                .urlSelector("$.id")
                .hotScoreSelector("$.statistics.viewCount")

                .register();

        addJsonConfig("https://acs.youku.com/h5/mtop.youku.soku.yksearch/2.0/?appKey={appKey}&data={data}&t={time}&sign={sign}")
                .hotScoreNeedDeal(true)
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("优酷榜单")
                .platformCategory("youku")
                .interfaceUrl("/api/topsearch/youku/{type}")
                .hotTitleUrlPrefix("https://v.youku.com/v_show/id_XNjQ1NDI4ODY2MA==.html?s=")
                .addArea()
                .rootSelector("$.data.nodes[0].nodes[0].data.tabDataMap.{type}.nodes[0].nodes[*].data")
                .keywordSelector("$.title")
                .urlSelector("$.encodeShowId")

                .register();

        addDomConfig("https://www.douban.com/group/{type}")
                .hotTitleNeedDeal(true)
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("小组豆瓣")
                .platformCategory("xiaozudouban")
                .interfaceUrl("/api/topsearch/xiaozudouban/{type}")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".olt tr:not(:has(span[title=置顶]))")
                .keywordSelector(".title > a")
                .urlSelector(".title > a")
                .publishTimeSelector(".time")
                .commentCountSelector(".r-count")
                .forestRequestHeader(
                        new ForestRequestHeader()
                                .setUserAgent(UserAgentGeneratorUtil.generateRandomUserAgent())
                )
                .register();

        addJsonConfig("https://api.cntv.cn/epg/getEpgInfoByChannelNew?c=cctv{type}&serviceId=tvcctv&d="+ TimeUtil.getCurrentTimeFormat("yyyyMMdd")+"&t=jsonp&cb=setItem1")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("央视电视台")
                .platformCategory("cctv")
                .interfaceUrl("/api/topsearch/cctv/{type}")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.cctv{type}.list")
                .keywordSelector("$.title")
                .hotScoreSelector("$.startTime")
                .urlSelector("$.columnBackvideourl")
                .startTimeSelector("$.startTime")
                .endTimeSelector("$.endTime")
                .showTimeSelector("$.showTime")
                .register();

        addDomConfig("https://www.52pojie.cn/forum.php?mod=guide&view=hot")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("吾爱破解")
                .platformCategory("社区")
                .interfaceUrl("/api/topsearch/wuaipojie")
                .hotTitleUrlPrefix("https://www.52pojie.cn/")
                .addArea()
                .rootSelector(".bm_c tbody")
                .keywordSelector(".xst")
                .hotScoreSelector(".xi1")
                .urlSelector(".xst")
                .forestRequestHeader(new ForestRequestHeader()
                        .setUserAgent(UserAgentGeneratorUtil.generateRandomUserAgent())
                        .setAcceptLanguage("zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7")
                        .setXForwardedFor("114.114.114.114")
                        .setReferer("https://www.52pojie.cn/")
                        .setOrigin("https://www.52pojie.cn")
                )
                .register();

        addDomConfig("https://www.newsmth.net/nForum/mainpage?ajax")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("水木社区")
                .platformCategory("社区")
                .interfaceUrl("/api/topsearch/shuimushequ")
                .hotTitleUrlPrefix("https://www.newsmth.net")
                .addArea()
                .rootSelector("#top10 > ul > li")
                .keywordSelector("a:nth-of-type(2)")
                .urlSelector("a:nth-of-type(2)")
                .register();

        addDomConfig("https://www.chongbuluo.com/forum.php?mod=guide&view=hot")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("虫部落")
                .platformCategory("社区")
                .interfaceUrl("/api/topsearch/chongbuluo")
                .hotTitleUrlPrefix("https://www.chongbuluo.com/")
                .addArea()
                .rootSelector(".bm_c tbody")
                .keywordSelector(".common > a")
                .urlSelector(".common > a")
                .hotScoreSelector(".common > .xi1")
                .register();

        addDomConfig("https://xz.aliyun.com/news")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("先知社区")
                .platformCategory("社区")
                .interfaceUrl("/api/topsearch/xianzhishequ")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".hot_news2 > ul > li")
                .keywordSelector("div > a")
                .urlSelector("div > a")
                .register();

        addJsonConfig("https://service.kdslife.com/v1/news?channel_id=689073ce0406f87cd1b07b66&page=1&page_size=60")
                .requestType(ForestRequestTypeEnum.GET)
                .hotTitleUrlNeedDeal(true)
                .platformName("KDS上海头条")
                .platformCategory("社区")
                .interfaceUrl("/api/topsearch/kdsshanghaitoutiao")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.list")
                .keywordSelector("$.title")
                .hotScoreSelector("$.views")
                .register();

        addDomConfig("https://bbs.kanxue.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("看雪")
                .platformCategory("社区")
                .interfaceUrl("/api/topsearch/kanxue")
                .hotTitleUrlPrefix("https://bbs.kanxue.com/")
                .addArea()
                .rootSelector(".bbs_home_page_three_col > div:nth-of-type(2) > div:nth-of-type(2) > div")
                .keywordSelector(".bbs_home_page_list_title")
                .urlSelector(".bbs_home_page_list_title")
                .register();

        addDomConfig("https://www.txrjy.com/forum.php")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("通信人家园")
                .platformCategory("社区")
                .interfaceUrl("/api/topsearch/tongxinrenjiayuan")
                .hotTitleUrlPrefix("https://www.txrjy.com/")
                .addArea()
                .rootSelector("#review li")
                .keywordSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://emacs-china.org/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("Emacs China")
                .platformCategory("社区")
                .interfaceUrl("/api/topsearch/emacschina")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".topic-list .topic-list-item")
                .keywordSelector(".link-top-line > a")
                .urlSelector(".link-top-line > a")
                .hotScoreSelector(".views span")
                .register();

        addDomConfig("https://ruby-china.org/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("Ruby China")
                .platformCategory("社区")
                .interfaceUrl("/api/topsearch/rubychina")
                .hotTitleUrlPrefix("https://ruby-china.org")
                .addArea()
                .rootSelector(".card-body.item-list > div")
                .keywordSelector(".title.media-heading > a")
                .urlSelector(".title.media-heading > a")
                .hotScoreSelector(".count")
                .register();

        addDomConfig("https://www.9kd.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("凯迪网")
                .platformCategory("社区")
                .interfaceUrl("/api/topsearch/kaidiwang")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".hot-news-list-wrap > ul >li")
                .keywordSelector("a > p")
                .urlSelector("a")
                .register();

        addDomConfig("https://www.wearesellers.com/headline/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("知无不言跨境电商社区")
                .platformCategory("社区")
                .interfaceUrl("/api/topsearch/zhiwubuyankuajingdianshangshequ")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".tab_new.tab-list-explore > .conlist > dl")
                .keywordSelector("dd > h2 > a")
                .urlSelector("dd > h2 > a")
                .register();

        addDomConfig("https://www.oschina.net/news")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("开源资讯")
                .platformCategory("社区")
                .interfaceUrl("/api/topsearch/kaiyuanzixun")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".section.articles-list > .items > .item")
                .keywordSelector(".content > a")
                .urlSelector(".content > a")
                .register();

        addDomConfig("https://bbs.pinggu.org/z_index.php?type=3")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("经管之家")
                .platformCategory("社区")
                .interfaceUrl("/api/topsearch/jingguanzhijia")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("#ct > .mn tbody > tr:not(:first-child)")
                .keywordSelector("th > a")
                .urlSelector("th > a")
                .hotScoreSelector(".num > a")
                .register();

        addDomConfig("https://www.36kr.com/hot-list/catalog")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("36氪")
                .platformCategory("科技")
                .interfaceUrl("/api/topsearch/36ke")
                .hotTitleUrlPrefix("https://www.36kr.com")
                .addArea()
                .rootSelector(".main-wrapper > .list-wrapper > .list-section-wrapper:nth-of-type(1) > .article-list > .article-wrapper")
                .keywordSelector(".article-item-info > p > a")
                .urlSelector(".article-item-info > p > a")
                .hotScoreSelector(".kr-flow-bar-hot > span")
                .register();

        addDomConfig("https://m.ithome.com/rankm/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("IT之家")
                .platformCategory("科技")
                .interfaceUrl("/api/topsearch/itzhijia")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".rank-box:nth-of-type(2) > div")
                .keywordSelector(".plc-title")
                .urlSelector("a")
                .hotScoreSelector(".review-num")
                .register();

        addDomConfig("https://www.geekpark.net/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("极客公园")
                .platformCategory("科技")
                .interfaceUrl("/api/topsearch/jikegongyuan")
                .hotTitleUrlPrefix("https://www.geekpark.net")
                .addArea()
                .rootSelector(".main-content > .container > .article-list > .article-item")
                .keywordSelector(".article-info > a:nth-of-type(2)")
                .urlSelector(".article-info > a:nth-of-type(2)")
                .register();

        addDomConfig("https://readhub.cn/hot")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("ReadHub")
                .platformCategory("科技")
                .interfaceUrl("/api/topsearch/readhub")
                .hotTitleUrlPrefix("https://readhub.cn")
                .addArea()
                .rootSelector(".style_tabs__HaLYn > .style_tabs__HaLYn > .mantine-Tabs-panel > ol > li")
                .keywordSelector("div > div:nth-of-type(2) > a")
                .urlSelector("div > div:nth-of-type(2) > a")
                .register();

        addDomConfig("https://www.tmtpost.com/hot")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("钛媒体")
                .platformCategory("科技")
                .interfaceUrl("/api/topsearch/taimeiti")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".content > .item")
                .keywordSelector("._tit")
                .urlSelector("._tit")
                .hotScoreSelector(".action_reads")
                .register();

        addDomConfig("https://www.zol.com.cn/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("中关村在线")
                .platformCategory("科技")
                .interfaceUrl("/api/topsearch/zhongguancunzaixian")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("#today_news_1 li")
                .keywordSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://www.landiannews.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("蓝点网")
                .platformCategory("科技")
                .interfaceUrl("/api/topsearch/landianwang")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".article-list.post-list-layout.post > ul >li")
                .keywordSelector(".title")
                .urlSelector(".title")
                .hotScoreSelector(".icon-view")
                .register();

        addDomConfig("https://www.cyzone.cn/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("创业邦")
                .platformCategory("科技")
                .interfaceUrl("/api/topsearch/chuangyebang")
                .hotTitleUrlPrefix("https://www.cyzone.cn")
                .addArea()
                .rootSelector("#pane-recommend .article-item")
                .keywordSelector(".item-title")
                .urlSelector(".item-title")
                .register();

        addDomConfig("https://www.iheima.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("i黑马")
                .platformCategory("科技")
                .interfaceUrl("/api/topsearch/iheima")
                .hotTitleUrlPrefix("https://www.iheima.com")
                .addArea()
                .rootSelector(".mainleft .item-wrap.clearfix")
                .keywordSelector(".title")
                .urlSelector(".title")
                .register();

        addDomConfig("https://www.leiphone.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("雷锋网")
                .platformCategory("科技")
                .interfaceUrl("/api/topsearch/leifengwang")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".lph-pageList.index-pageList > .list li")
                .keywordSelector("h3 > a")
                .urlSelector("h3 > a")
                .register();

        addDomConfig("https://awtmt.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("全天候科技")
                .platformCategory("科技")
                .interfaceUrl("/api/topsearch/quantianhoukeji")
                .hotTitleUrlPrefix("https://awtmt.com")
                .addArea()
                .rootSelector(".article-list > div")
                .keywordSelector(".article-title")
                .urlSelector(".article-title")
                .register();

        addDomConfig("https://www.iot101.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("物联网智库")
                .platformCategory("科技")
                .interfaceUrl("/api/topsearch/wulianwangzhiku")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("#newsList > .news-item-box")
                .keywordSelector(".title > a")
                .urlSelector(".title > a")
                .register();

        addDomConfig("https://www.mydrivers.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("快科技")
                .platformCategory("科技")
                .interfaceUrl("/api/topsearch/kuaikeji")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("#newlist_1 > h4 > ul > li")
                .keywordSelector("a")
                .urlSelector("a")
                .addArea()
                .rootSelector("#news_content_1 li")
                .keywordSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://www.techweb.com.cn/roll/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("TechWeb")
                .platformCategory("科技")
                .interfaceUrl("/api/topsearch/techweb")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".newslist li")
                .keywordSelector(".tit > a")
                .urlSelector(".tit > a")
                .register();

        addDomConfig("https://www.toodaylab.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("理想生活实验室")
                .platformCategory("科技")
                .interfaceUrl("/api/topsearch/lixiangshenghuoshiyanshi")
                .hotTitleUrlPrefix("https://www.toodaylab.com")
                .addArea()
                .rootSelector(".hot-list.clearfix > a")
                .keywordSelector("p")
                .urlSelector(":root")
                .register();

        addDomConfig("http://www.duozhi.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("多知")
                .platformCategory("科技")
                .interfaceUrl("/api/topsearch/duozhi")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".list-post > .post-item")
                .keywordSelector(".post-inner > a")
                .urlSelector(".post-inner > a")
                .register();

        addDomConfig("https://www.jiemodui.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("芥末堆")
                .platformCategory("科技")
                .interfaceUrl("/api/topsearch/jiemodui")
                .hotTitleUrlPrefix("https://www.jiemodui.com")
                .addArea()
                .rootSelector(".itemList > a")
                .keywordSelector("h3")
                .urlSelector(":root")
                .register();

        addDomConfig("https://www.iimedia.cn/c1088")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("艾媒网")
                .platformCategory("科技")
                .interfaceUrl("/api/topsearch/aimeiwang")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".article-hot-list .hot-item")
                .keywordSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://www.chinaz.com/news/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("站长之家")
                .platformCategory("科技")
                .interfaceUrl("/api/topsearch/zhanzhangzhijia")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".cloumn-bd.section.hots-wrapper  .hots-wrapper__bd  li")
                .keywordSelector("h4 > a")
                .urlSelector("h4 > a")
                .register();

        addDomConfig("https://lieyunpro.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("猎云网")
                .platformCategory("科技")
                .interfaceUrl("/api/topsearch/lieyunwang")
                .hotTitleUrlPrefix("https://lieyunpro.com")
                .addArea()
                .rootSelector(".article-container > div")
                .keywordSelector("h2 > a")
                .urlSelector("h2 > a")
                .register();

        addDomConfig("https://weread.qq.com/web/category/all")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("微信读书")
                .platformCategory("媒体")
                .interfaceUrl("/api/topsearch/weixindushu")
                .hotTitleUrlPrefix("https://weread.qq.com")
                .addArea()
                .rootSelector(".ranking_content_bookList > li")
                .keywordSelector(".wr_bookList_item_title")
                .urlSelector("a")
                .hotScoreSelector(".wr_bookList_item_readingCountText")
                .register();

        addJsonConfig("https://www.acfun.cn/rest/pc-direct/rank/channel?channelId=&subChannelId=&rankLimit=30&rankPeriod=DAY")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("acfun")
                .platformCategory("媒体")
                .interfaceUrl("/api/topsearch/acfun")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.rankList")
                .keywordSelector("$.contentTitle")
                .urlSelector("$.shareUrl")
                .hotScoreSelector("$.viewCount")
                .register();

        addDomConfig("https://www.mmfen.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("美漫百科")
                .platformCategory("媒体")
                .interfaceUrl("/api/topsearch/meimanbaike")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".main-inner.group .pad.group article")
                .keywordSelector(".post-title > a")
                .urlSelector(".post-title > a")
                .register();

        addDomConfig("http://news.mtime.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("时光网")
                .platformCategory("媒体")
                .interfaceUrl("/api/topsearch/shiguangwang")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".left-cont.fix > li")
                .keywordSelector("h4 > a")
                .urlSelector("h4 > a")
                .register();

        addDomConfig("https://jandan.net/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("煎蛋")
                .platformCategory("媒体")
                .interfaceUrl("/api/topsearch/jiandan")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".post-list > div")
                .keywordSelector("h2 > a")
                .urlSelector("h2 > a")
                .register();

        addDomConfig("https://www.vgover.com/news")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("电玩帮")
                .platformCategory("媒体")
                .interfaceUrl("/api/topsearch/dianwanbang")
                .hotTitleUrlPrefix("https://www.vgover.com")
                .addArea()
                .rootSelector(".vg-list.vg-list--8 > li")
                .keywordSelector("h3 > a")
                .urlSelector("h3 > a")
                .register();

        addDomConfig("https://www.tvmao.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("电视猫")
                .platformCategory("媒体")
                .interfaceUrl("/api/topsearch/dianshimao")
                .hotTitleUrlPrefix("https://www.tvmao.com")
                .addArea()
                .rootSelector(".rtive.f_drama")
                .keywordSelector("a")
                .urlSelector("a")
                .addArea()
                .rootSelector(".r_hot_drama > li")
                .keywordSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://www.chinanews.com.cn/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("中国新闻网")
                .platformCategory("媒体")
                .interfaceUrl("/api/topsearch/zhongguoxinwenwang")
                .hotTitleUrlPrefix("https:")
                .addArea()
                .rootSelector("#YwNes > .news-left:nth-of-type(2) .rdph-list2:nth-of-type(2) li")
                .keywordSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://www.myzaker.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("ZAKER")
                .platformCategory("媒体")
                .interfaceUrl("/api/topsearch/zaker")
                .hotTitleUrlPrefix("https:")
                .addArea()
                .rootSelector(".seo-list > div")
                .keywordSelector("h2")
                .urlSelector(".article-wrap > a")
                .register();

        addDomConfig("https://www.bjnews.com.cn/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("新京报")
                .platformCategory("媒体")
                .interfaceUrl("/api/topsearch/xinjingbao")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".hotComment.ranking > ul > li")
                .keywordSelector("h3 > a")
                .urlSelector("h3 > a")
                .register();

        addDomConfig("https://www.stnn.cc/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("星岛环球")
                .platformCategory("媒体")
                .interfaceUrl("/api/topsearch/xingdaohuanqiu")
                .hotTitleUrlPrefix("https://www.stnn.cc")
                .addArea()
                .rootSelector(".n_left_l > .hs li")
                .keywordSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://www.21jingji.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("21经济网")
                .platformCategory("财经")
                .interfaceUrl("/api/topsearch/21jingjiwang")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".hot-list > li")
                .keywordSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://finance.eastmoney.com/yaowen.html")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("东方财富网")
                .platformCategory("财经")
                .interfaceUrl("/api/topsearch/dongfangcaifuwang")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("#artitileList1 > ul > li")
                .keywordSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://news.mbalib.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("MBA智库")
                .platformCategory("财经")
                .interfaceUrl("/api/topsearch/mbazhiku")
                .hotTitleUrlPrefix("https://news.mbalib.com")
                .addArea()
                .rootSelector(".latest-article")
                .keywordSelector("h3 > a")
                .urlSelector("h3 > a")
                .register();

        addDomConfig("https://www.eeo.com.cn/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("经济观察网")
                .platformCategory("财经")
                .interfaceUrl("/api/topsearch/jingjiguanchawang")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".box_R_item.line.news > div li")
                .keywordSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://www.time-weekly.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("时代在线")
                .platformCategory("财经")
                .interfaceUrl("/api/topsearch/shidaizaixian")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".t4_left > a")
                .keywordSelector(".t4_block_text")
                .urlSelector(":root")
                .register();

        addDomConfig("https://www.jinse.cn/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("金色财经")
                .platformCategory("财经")
                .interfaceUrl("/api/topsearch/jingsecaijing")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector(".js-main__l .js-article_item")
                .keywordSelector(".js-article_item__title")
                .urlSelector(".js-article_item__title")
                .register();

        addDomConfig("https://finance.sina.com.cn/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("新浪财经")
                .platformCategory("财经")
                .interfaceUrl("/api/topsearch/xinlangcaijing")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("#blk_hdline_01 a")
                .keywordSelector(":root")
                .urlSelector(":root")
                .rootSelector(".m-p1-mb1-list.m-list-container a")
                .keywordSelector(":root")
                .urlSelector(":root")
                .register();

        addJsonConfig("https://apim.ningmengyun.com/api/News/AppNewsListV3?index=0&pageSize=20&column=1010")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("会计头条")
                .platformCategory("财经")
                .interfaceUrl("/api/topsearch/kuaijitoutiao")
                .hotTitleUrlPrefix("https://www.kuaijitoutiao.com/article/")
                .addArea()
                .rootSelector("$.data.data")
                .keywordSelector("$.appNews.title")
                .urlSelector("$.appNews.articleID")
                .register();

        addDomConfig("http://www.laohucaijing.com/news_index/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("老虎财经")
                .platformCategory("财经")
                .interfaceUrl("/api/topsearch/laohucaijing")
                .hotTitleUrlPrefix("http://www.laohucaijing.com")
                .addArea()
                .rootSelector(".Recommend a")
                .keywordSelector(":root")
                .urlSelector(":root")
                .addArea()
                .rootSelector(".content_list h3")
                .keywordSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://www.theblockbeats.info/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("BlockBeats")
                .platformCategory("财经")
                .interfaceUrl("/api/topsearch/blockbeats")
                .hotTitleUrlPrefix("https://www.theblockbeats.info")
                .addArea()
                .rootSelector(".home-page-ctr-contain  .title-item")
                .keywordSelector(":root")
                .urlSelector(":root")
                .register();

        addDomConfig("https://news.fx678.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("汇通财经")
                .platformCategory("财经")
                .interfaceUrl("/api/topsearch/huitongcaijing")
                .hotTitleUrlPrefix("https://news.fx678.com")
                .addArea()
                .rootSelector(".col4_r_txts.l  li")
                .keywordSelector("a")
                .urlSelector("a")
                .register();

        addJsonConfig("https://www.nbd.com.cn/news-rank-nr-h5/rank_index/news")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("每经网")
                .platformCategory("财经")
                .interfaceUrl("/api/topsearch/meijingwang")
                .hotTitleUrlPrefix("")
                .addArea()
                .rootSelector("$.data.list")
                .keywordSelector("$.title")
                .urlSelector("$.url")
                .hotScoreSelector("$.score_num")
                .register();

        addDomConfig("https://xuangutong.com.cn/jingxuan")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("选股通")
                .platformCategory("财经")
                .interfaceUrl("/api/topsearch/xuangutong")
                .hotTitleUrlPrefix("https://xuangutong.com.cn")
                .addArea()
                .rootSelector(".article-wrapper_iIO5l")
                .keywordSelector("a")
                .urlSelector("a")
                .register();

        addDomConfig("https://www.chaincatcher.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("Chain Catcher")
                .platformCategory("财经")
                .interfaceUrl("/api/topsearch/chaincatcher")
                .hotTitleUrlPrefix("https://www.chaincatcher.com")
                .addArea()
                .rootSelector(".left_area.pr-2.col-lg-9.col-12 .article_left")
                .keywordSelector("a > h3")
                .urlSelector("a")
                .register();

        addDomConfig("https://www.chinastarmarket.cn/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("科创板日报")
                .platformCategory("财经")
                .interfaceUrl("/api/topsearch/kechuangbanribao")
                .hotTitleUrlPrefix("https://www.chinastarmarket.cn")
                .addArea()
                .rootSelector(".d-f.m-t-30.m-b-30")
                .keywordSelector(".d-b.o-h.f-s-20.f-w-b.l-h-15.c-000.w-s-n.t-o-el.list-link")
                .urlSelector(".d-b.o-h.f-s-20.f-w-b.l-h-15.c-000.w-s-n.t-o-el.list-link")
                .register();

        addDomConfig("https://topd.tencent.com/")
                .requestType(ForestRequestTypeEnum.GET)
                .platformName("腾讯设计开放平台")
                .platformCategory("设计")
                .interfaceUrl("/api/topsearch/tengxunshejikaifangpingtai")
                .hotTitleUrlPrefix("https://topd.tencent.com")
                .addArea()
                .rootSelector(".li-work")
                .keywordSelector(".title")
                .urlSelector(".title")
                .register();













    }
}