package com.tgmeng.common.schedule;

import com.tgmeng.common.forest.client.system.ISystemLocalClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
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
            "/api/topsearch/gouzudouban"




    );

    @Scheduled(cron = "${my-config.schedule.controller-api-top-search.schedule-rate}")
    public void scanAndInvokeControllers() {
        long startTime = System.currentTimeMillis();
        log.info("🤖🤖开始 系统定时缓存所有数据，共{}个接口👈👈", ENDPOINTS.size());

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        try {
            // 使用自定义线程池进行并行处理
            CompletableFuture<?>[] futures = ENDPOINTS.stream()
                    .map(endpoint -> CompletableFuture.runAsync(() -> {
                        try {
                            systemLocalClient.systemLocalClient(endpoint);
                            successCount.incrementAndGet();
                            log.info("🤖系统定时任务成功缓存: {}", endpoint);
                        } catch (Exception e) {
                            failureCount.incrementAndGet();
                            log.error("🤖系统定时任务缓存失败: {}, 错误: {}", endpoint, e.getMessage());
                        }
                    }, executor))
                    .toArray(CompletableFuture[]::new);

            // 等待所有任务完成，设置超时时间
            CompletableFuture.allOf(futures)
                    .orTimeout(300, TimeUnit.SECONDS) // 5分钟超时
                    .join();

        } catch (Exception e) {
            log.error("🤖系统定时任务执行异常: {}", e.getMessage(), e);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        log.info("🤖🤖完成 系统定时缓存所有数据👈👈 " +
                        "成功: {}, 失败: {}, 总耗时: {}ms",
                successCount.get(), failureCount.get(), duration);
    }
}