package com.tgmeng.common.schedule;

import cn.hutool.core.util.StrUtil;
import com.tgmeng.common.enums.system.RequestFromEnum;
import com.tgmeng.common.forest.client.system.ISystemLocalClient;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(
        value = "my-config.schedule.controller-api-top-search.enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class ControllerApiSchedule {

    private final ISystemLocalClient systemLocalClient;

    // 使用自定义线程池，避免使用ForkJoinPool
    private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

    // 将endpoints提取为常量，便于维护
    private static final List<String> ENDPOINTS = Arrays.asList(
            // TODO 每次新增平台后，这里添加接口地址，用于定时调用
            // 国内热搜
            "/api/topsearch/baidu",
            "/api/topsearch/bilibili",
            "/api/topsearch/weibo",
            "/api/topsearch/douyin",
            "/api/topsearch/douban",
            "/api/topsearch/tencent",
            "/api/topsearch/toutiao",
            "/api/topsearch/wangyi",
            "/api/topsearch/biaoshengwangyiyun",
            "/api/topsearch/xingegwangyiyun",
            "/api/topsearch/yuanchuangwangyiyun",
            "/api/topsearch/regewangyiyun",
            "/api/topsearch/tiebabaidu",
            "/api/topsearch/shaoshupai",
            "/api/topsearch/dianshijubaidu",
            "/api/topsearch/xiaoshuobaidu",
            "/api/topsearch/dianyingbaidu",
            "/api/topsearch/youxibaidu",
            "/api/topsearch/qichebaidu",
            "/api/topsearch/regengbaidu",
            "/api/topsearch/caijingbaidu",
            "/api/topsearch/minshengbaidu",
            "/api/topsearch/zhihu",

            // GitHub 热搜
            "/api/topsearch/github/allstars",
            "/api/topsearch/github/daystars",
            "/api/topsearch/github/weekstars",
            "/api/topsearch/github/monthstars",
            "/api/topsearch/github/yearstars",
            "/api/topsearch/github/threeyearstars",
            "/api/topsearch/github/fiveyearstars",
            "/api/topsearch/github/tenyearstars",

            // 国际热搜
            "/api/topsearch/global/youtube",
            "/api/topsearch/global/huggingfacespacestrending",
            "/api/topsearch/global/huggingfacespaceslikes",
            "/api/topsearch/global/huggingfacemodelstrending",
            "/api/topsearch/global/huggingfacemodellikes",
            "/api/topsearch/global/huggingfacedatasetstrending",
            "/api/topsearch/global/huggingfacedatasetslikes",

            // 腾讯视频
            "/api/topsearch/dianshijutengxun",
            "/api/topsearch/dianyingtengxun",
            "/api/topsearch/dongmantengxun",
            "/api/topsearch/zongyitengxun",
            "/api/topsearch/zongbangtengxun",

            // 爱奇艺
            "/api/topsearch/dianshijuaiqiyi",
            "/api/topsearch/dianyingaiqiyi",
            "/api/topsearch/dongmanaiqiyi",
            "/api/topsearch/zongyiaiqiyi",
            "/api/topsearch/zongbangaiqiyi",

            // 优酷
            "/api/topsearch/dianshijuyouku",
            "/api/topsearch/dianyingyouku",
            "/api/topsearch/dongmanyouku",
            "/api/topsearch/zongyiyouku",
            "/api/topsearch/zongbangyouku",

            // 芒果
            "/api/topsearch/dianshijumangguo",
            "/api/topsearch/dianyingmangguo",
            "/api/topsearch/dongmanmangguo",
            "/api/topsearch/zongyimangguo",
            "/api/topsearch/zongbangmangguo",

            // 猫眼
            "/api/topsearch/zhoupiaofangbangmaoyan",
            "/api/topsearch/xiangkanbangmaoyan",
            "/api/topsearch/goupiaopingfenbangmaoyan",
            "/api/topsearch/top100maoyan",

            // 金融界
            "/api/topsearch/jinrongjie",
            // 第一财经
            "/api/topsearch/diyicaijing",
            // 同花顺
            "/api/topsearch/tonghuashun",
            // 华尔街见闻
            "/api/topsearch/huaerjiejianwen",
            // 财联社
            "/api/topsearch/cailianshe",
            // 格隆汇
            "/api/topsearch/gelonghui",
            // 法布
            "/api/topsearch/fabu",
            // 金十
            "/api/topsearch/jinshi",
            // 纽约时报
            "/api/topsearch/niuyueshibao",
            // BBC
            "/api/topsearch/bbc",
            // 法广
            "/api/topsearch/faguang",
            // 大纪元
            "/api/topsearch/dajiyuan",
            // 人人都是产品囧里
            "/api/topsearch/woshipm",
            // 优设网
            "/api/topsearch/youshewang",
            // 站酷潜力榜
            "/api/topsearch/qianlibangzhanku",
            // 站酷作品榜
            "/api/topsearch/zuopinbangzhanku",
            // 站酷文章榜
            "/api/topsearch/wenzhangbangzhanku",
            // 涂鸦王国热门作品
            "/api/topsearch/remenzuopintuyawangguo",
            // 涂鸦王国精选作品
            "/api/topsearch/jingxuanzuopintuyawangguo",
            // 涂鸦王国发现新作
            "/api/topsearch/jinrixinzuotuyawangguo",
            // 涂鸦王国今日新作
            "/api/topsearch/faxianxinzuotuyawangguo",
            // 设计达人
            "/api/topsearch/shejidaren",
            // Topys
            "/api/topsearch/topys",
            // ArchDaily
            "/api/topsearch/archdaily",
            // Dribbble
            "/api/topsearch/dribbble",
            // Awwwards
            "/api/topsearch/awwwards",
            // Core77
            "/api/topsearch/core77",
            // Abduzeedo
            "/api/topsearch/abduzeedo",
            // MIT
            "/api/topsearch/mit",
            // 中国科学院
            "/api/topsearch/zhongguokexueyuan",
            // EurekAlert
            "/api/topsearch/eurekalert",
            // 中国科学院人工智能国际科技创新中心
            "/api/topsearch/rengongzhinengguojikejichuangxinzhongxin",
            // 中国科学院医药健康国际科技创新中心
            "/api/topsearch/yiyaojiankangguojikejichuangxinzhongxin",
            // 机器之心
            "/api/topsearch/jiqizhixin",
            // 虎扑
            "/api/topsearch/hupu",
            // 懂球帝
            "/api/topsearch/dongqiudi",
            // 新浪体育
            "/api/topsearch/xinlangtiyu",
            // 搜狐体育
            "/api/topsearch/souhutiyu",
            // 网易体育
            "/api/topsearch/tiyuwangyi",
            // 央视体育
            "/api/topsearch/yangshitiyu",
            // PP体育
            "/api/topsearch/pptiyu",
            // 直播吧
            "/api/topsearch/zhiboba",
            // v2ex
            "/api/topsearch/v2ex",
            // 虎扑步行街
            "/api/topsearch/buxingjiehupu",
            // nga
            "/api/topsearch/nga",
            // 一亩三分地
            "/api/topsearch/yimusanfendi",
            // 掘金文章
            "/api/topsearch/wenzhangjuejin",
            // hackernews
            "/api/topsearch/hackernews",
            // 豆瓣爱猫澡盆
            "/api/topsearch/aimaozaopendouban",
            // 豆瓣爱猫生活
            "/api/topsearch/aimaoshenghuodouban",
            // 豆瓣买组
            "/api/topsearch/maizudouban",
            // 豆瓣拼组
            "/api/topsearch/pinzudouban",
            // 豆瓣狗组
            "/api/topsearch/gouzudouban",

            //豆瓣生活
            "/api/topsearch/xiaozudouban/XIA_CHU_FANG_DOU_BAN",
            "/api/topsearch/xiaozudouban/JIE_MAO_YE_KE_AI_DOU_BAN",
            "/api/topsearch/xiaozudouban/WO_DE_CHENG_SHI_PAI_GEI_NI_KAN_DOU_BAN",
            "/api/topsearch/xiaozudouban/JIA_PIAN_TUI_JIAN_DOU_BAN",
            "/api/topsearch/xiaozudouban/SHE_CHU_MAI_FANG_GONG_JIN_HUI_DOU_BAN",
            "/api/topsearch/xiaozudouban/CUN_ZHUANG_AI_HAO_ZHE_DOU_BAN",
            "/api/topsearch/xiaozudouban/YOU_YI_DE_XIAO_CHUAN_DOU_BAN",
            "/api/topsearch/xiaozudouban/SHE_HUI_XING_SI_WANG_DOU_BAN",
            "/api/topsearch/xiaozudouban/TAI_TOU_KAN_SHU_DOU_BAN",
            "/api/topsearch/xiaozudouban/LAN_REN_SHENG_HUO_ZHI_BEI_DOU_BAN",
            "/api/topsearch/xiaozudouban/KE_AI_SHI_WU_FEN_XIANG_DOU_BAN",
            "/api/topsearch/xiaozudouban/JIN_TIAN_CHUAN_SHEN_ME_DOU_BAN",
            "/api/topsearch/xiaozudouban/XIAO_FEI_ZHU_YI_NI_XING_ZHE_DOU_BAN",
            "/api/topsearch/xiaozudouban/WO_MEN_DOU_BU_DONG_CHE_DOU_BAN",
            "/api/topsearch/xiaozudouban/WO_MEN_DOU_BU_DONG_REN_QING_SHI_GU_DOU_BAN",
            "/api/topsearch/xiaozudouban/DOU_BAN_NIAO_ZU_DOU_BAN",
            "/api/topsearch/xiaozudouban/REN_JIAN_QING_LV_GUAN_CHA_DOU_BAN",
            "/api/topsearch/xiaozudouban/ZHI_CHANG_TU_CAO_DA_HUI_DOU_BAN",
            "/api/topsearch/xiaozudouban/JIAO_SHI_DOU_BAN",
            "/api/topsearch/xiaozudouban/SHANG_BAN_ZHE_JIAN_SHI_DOU_BAN",

            // 游戏
            "/api/topsearch/youminxingkong",
            "/api/topsearch/3dmgame",
            "/api/topsearch/a9vg",
            "/api/topsearch/youxituoluo",
            "/api/topsearch/ign",
            "/api/topsearch/gcores",
            "/api/topsearch/youyanshe",
            "/api/topsearch/17173",
            "/api/topsearch/youxiawang",

            //健康
            "shengwugu",
            "yiyaomofang",
            "dingxiangyisheng",
            "dingxiangyuanshequ",
            "shengmingshibao",
            "jiayidajiankang",
            "guoke",
            "jiankangshibaowang",

            // CCTV
            "/api/topsearch/cctv/1",
            "/api/topsearch/cctv/2",
            "/api/topsearch/cctv/3",
            "/api/topsearch/cctv/4",
            "/api/topsearch/cctv/europe",
            "/api/topsearch/cctv/america",
            "/api/topsearch/cctv/5",
            "/api/topsearch/cctv/5plus",
            "/api/topsearch/cctv/6",
            "/api/topsearch/cctv/7",
            "/api/topsearch/cctv/8",
            "/api/topsearch/cctv/jilu",
            "/api/topsearch/cctv/10",
            "/api/topsearch/cctv/11",
            "/api/topsearch/cctv/12",
            "/api/topsearch/cctv/13",
            "/api/topsearch/cctv/child",
            "/api/topsearch/cctv/15",
            "/api/topsearch/cctv/16",
            "/api/topsearch/cctv/17",

            // 澎湃新闻
            "/api/topsearch/pengpaixinwen",
            // AI时报
            "/api/cachesearch/realtimesummary",
            // 词云
            "/api/cachesearch/wordcloud"

    );

    @PostConstruct
    public void init() {
        log.info("🚀 程序启动，立即执行AI时报、词云刷新任务");
        endpointsCiYunRefresh();
        endpointsAiShiBaoRefresh();
    }

    /**
     * 通用接口，都是一分钟刷新
     */
    @Scheduled(cron = "${my-config.schedule.controller-api-top-search.schedule-rate}")
    public void endpointsNormalRefresh() {
        Set<String> exclude = Set.of(
                "/api/cachesearch/wordcloud",
                "/api/cachesearch/realtimesummary",
                "/api/cachesearch/allbyword"
        );
        List<String> endpointsNormal = ENDPOINTS.stream()
                .filter(endpoint -> !exclude.contains(endpoint))
                .toList();
        scanAndInvokeControllers(endpointsNormal);
    }

    /**
     * 词云的定时刷新，目前设置的是每1分钟刷新一次
     */
    @Scheduled(cron = "${my-config.schedule.controller-api-top-search.schedule-rate-ci-yun}")
    public void endpointsCiYunRefresh() {
        List<String> endpointsCiYun = List.of(
                "/api/cachesearch/wordcloud"
        );
        scanAndInvokeControllers(endpointsCiYun);
    }

    /**
     * ai时报的定时刷新，目前设置的是每5分钟刷新一次
     */
    @Scheduled(cron = "${my-config.schedule.controller-api-top-search.schedule-rate-ai-shi-bao}")
    // ai时报的定时刷新，目前设置的是每5分钟刷新一次
    public void endpointsAiShiBaoRefresh() {
        List<String> endpointsAiShiBao = List.of(
                "/api/cachesearch/realtimesummary"
        );
        scanAndInvokeControllers(endpointsAiShiBao);
    }

    public void scanAndInvokeControllers() {
        scanAndInvokeControllers(ENDPOINTS);
    }

    public void scanAndInvokeControllers(List<String> endpoints) {
        long startTime = System.currentTimeMillis();
        // 获取当前请求的端点（如果有的话，说明是主动调用这个来刷新所有缓存，这个主要是为了避免需要主动刷新所有缓存的接口死循环调用，目前主动刷新所有缓存的有词云、搜索、AI时报）
        String currentEndpoint = getCurrentRequestEndpoint();
        // 过滤掉会陷入死循环的，比如词云主动刷新缓存，那他会调用AI时报，AI时报会刷新缓存，他又会调用词云，导致死循环
        Set<String> exclude = Set.of("/api/cachesearch/wordcloud", "/api/cachesearch/realtimesummary", "/api/cachesearch/allbyword");
        List<String> endpointsToRefresh;
        if (StrUtil.isNotEmpty(currentEndpoint)) {
            // currentEndpoint 不为空 → 排除 exclude
            endpointsToRefresh = endpoints.stream()
                    .filter(endpoint -> !exclude.contains(endpoint))
                    .toList();
        } else {
            // currentEndpoint 为空 → 不排除任何接口
            endpointsToRefresh = endpoints;
        }
        String typeMessage = StrUtil.isBlank(currentEndpoint) ? "系统定时任务缓存数据" : "系统内部主动检测缓存并刷新已失效的接口";


        log.info("🤖🤖开始:" + typeMessage + "，共{}个接口，👈👈", endpointsToRefresh.size());

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);
        AtomicInteger timeoutCount = new AtomicInteger(0);
        // 使用自定义线程池进行并行处理
        CompletableFuture<?>[] futures = endpointsToRefresh.stream()
                .map(endpoint -> CompletableFuture.runAsync(() -> {
                    try {
                        systemLocalClient.systemLocalClient(RequestFromEnum.INTERNAL.getValue(), endpoint);
                        successCount.incrementAndGet();
                        //log.info("🤖成功:" + typeMessage + ": {}", endpoint);
                    } catch (Exception e) {
                        failureCount.incrementAndGet();
                        //log.error("🤖失败:" + typeMessage + ": {}, 错误: {}", endpoint, e.getMessage());
                    }
                }, executor))
                .toArray(CompletableFuture[]::new);

        try {
            // 等待所有任务完成，设置超时时间
            CompletableFuture.allOf(futures)
                    .orTimeout(300, TimeUnit.SECONDS) // 5分钟超时
                    .join();

        } catch (CompletionException e) {
            if (e.getCause() instanceof TimeoutException) {
                // 处理超时情况
                log.warn("🤖执行超时:" + typeMessage + "，取消未完成的任务");

                // 取消所有未完成的任务
                for (CompletableFuture<?> future : futures) {
                    if (!future.isDone()) {
                        future.cancel(true);
                        timeoutCount.incrementAndGet();
                    }
                }
            } else {
                // 其他异常
                log.error("🤖执行异常:" + typeMessage + ": {}", e.getMessage(), e);
                failureCount.addAndGet(endpoints.size() - successCount.get() - timeoutCount.get());
            }
        } catch (Exception e) {
            log.error("🤖系执行异常:" + typeMessage + ": {}", e.getMessage(), e);
            failureCount.addAndGet(endpoints.size() - successCount.get() - timeoutCount.get());
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        log.info("🤖🤖完成:" + typeMessage + "👈👈 " +
                        "成功: {}, 失败: {}, 超时: {}, 总耗时: {}ms",
                successCount.get(), failureCount.get(), timeoutCount.get(), duration);
    }

    private String getCurrentRequestEndpoint() {
        try {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes instanceof ServletRequestAttributes) {
                HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
                return request.getRequestURI();
            }
        } catch (Exception e) {
            log.debug("无法获取当前请求端点，可能不在Web上下文");
        }
        return null;
    }
}