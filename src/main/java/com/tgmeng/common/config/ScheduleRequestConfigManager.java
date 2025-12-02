package com.tgmeng.common.config;

import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ScheduleRequestConfigManager {
    private final Map<String, PlatformConfig> configs = new HashMap<>();

    @Data
    @Builder
    public static class PlatformConfig {
        // 请求周期，单位：秒。就是说多少秒执行一次定时任务去刷新缓存。主要是为了区分一些风控严格的接口
        @Builder.Default
        private Long requestCycle = 60L;
        // 请求延迟，单位：秒。就是说定时任务执行后，请求了一个接口，第二个接口隔多少秒再执行
        @Builder.Default
        private Long requestDelay = 0L;
        // 接口是否参与定时请求
        @Builder.Default
        private Boolean enabled = true;
        // 优先级（数字越大优先级越高，用于排序）
        @Builder.Default
        private Integer priority = 0;
        // 重试次数（请求失败时的重试次数）
        @Builder.Default
        private Integer retryTimes = 3;
        // 超时时间，单位：秒（单次请求的超时时间）
        @Builder.Default
        private Long timeout = 30L;
        // 描述信息
        @Builder.Default
        private String description = "";
    }

    public ScheduleRequestConfigManager() {
        initConfigs();
    }

    private void initConfigs() {
        // 微博 - 使用Builder模式创建
        configs.put("/api/topsearch/baidu", PlatformConfig.builder().build());
        configs.put("/api/topsearch/bilibili", PlatformConfig.builder().build());
        configs.put("/api/topsearch/weibo", PlatformConfig.builder().build());
        configs.put("/api/topsearch/douyin", PlatformConfig.builder().build());
        configs.put("/api/topsearch/douban", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/tencent", PlatformConfig.builder().build());
        configs.put("/api/topsearch/toutiao", PlatformConfig.builder().build());
        configs.put("/api/topsearch/wangyi", PlatformConfig.builder().build());
        configs.put("/api/topsearch/wangyiyun/biaoshengbang", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/wangyiyun/xingebang", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/wangyiyun/yuanchuangbang", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/wangyiyun/regebang", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/tiebabaidu", PlatformConfig.builder().build());
        configs.put("/api/topsearch/shaoshupai", PlatformConfig.builder().build());
        configs.put("/api/topsearch/baidu/dianshiju", PlatformConfig.builder().build());
        configs.put("/api/topsearch/baidu/xiaoshuo", PlatformConfig.builder().build());
        configs.put("/api/topsearch/baidu/dianying", PlatformConfig.builder().build());
        configs.put("/api/topsearch/baidu/youxi", PlatformConfig.builder().build());
        configs.put("/api/topsearch/baidu/qiche", PlatformConfig.builder().build());
        configs.put("/api/topsearch/baidu/regeng", PlatformConfig.builder().build());
        configs.put("/api/topsearch/baidu/caijing", PlatformConfig.builder().build());
        configs.put("/api/topsearch/baidu/minsheng", PlatformConfig.builder().build());
        configs.put("/api/topsearch/zhihu", PlatformConfig.builder().build());
        configs.put("/api/topsearch/github/stars/all", PlatformConfig.builder().requestCycle(1200L).requestDelay(10L).build());
        configs.put("/api/topsearch/github/stars/day", PlatformConfig.builder().requestCycle(1200L).requestDelay(10L).build());
        configs.put("/api/topsearch/github/stars/week", PlatformConfig.builder().requestCycle(1200L).requestDelay(10L).build());
        configs.put("/api/topsearch/github/stars/month", PlatformConfig.builder().requestCycle(1200L).requestDelay(10L).build());
        configs.put("/api/topsearch/github/stars/year", PlatformConfig.builder().requestCycle(1200L).requestDelay(10L).build());
        configs.put("/api/topsearch/github/stars/threeyear", PlatformConfig.builder().requestCycle(1200L).requestDelay(10L).build());
        configs.put("/api/topsearch/github/stars/fiveyear", PlatformConfig.builder().requestCycle(1200L).requestDelay(10L).build());
        configs.put("/api/topsearch/github/stars/tenyear", PlatformConfig.builder().requestCycle(1200L).requestDelay(10L).build());
        configs.put("/api/topsearch/global/youtube", PlatformConfig.builder().build());
        configs.put("/api/topsearch/global/huggingface/spacestrending", PlatformConfig.builder().build());
        configs.put("/api/topsearch/global/huggingface/spaceslikes", PlatformConfig.builder().build());
        configs.put("/api/topsearch/global/huggingface/modelstrending", PlatformConfig.builder().build());
        configs.put("/api/topsearch/global/huggingface/modelslikes", PlatformConfig.builder().build());
        configs.put("/api/topsearch/global/huggingface/datasetstrending", PlatformConfig.builder().build());
        configs.put("/api/topsearch/global/huggingface/datasetslikes", PlatformConfig.builder().build());
        configs.put("/api/topsearch/tencent/dianshiju", PlatformConfig.builder().build());
        configs.put("/api/topsearch/tencent/dianying", PlatformConfig.builder().build());
        configs.put("/api/topsearch/tencent/dongman", PlatformConfig.builder().build());
        configs.put("/api/topsearch/tencent/zongyi", PlatformConfig.builder().build());
        configs.put("/api/topsearch/tencent/zongbang", PlatformConfig.builder().build());
        configs.put("/api/topsearch/aiqiyi/dianshiju", PlatformConfig.builder().build());
        configs.put("/api/topsearch/aiqiyi/dianying", PlatformConfig.builder().build());
        configs.put("/api/topsearch/aiqiyi/dongman", PlatformConfig.builder().build());
        configs.put("/api/topsearch/aiqiyi/zongyi", PlatformConfig.builder().build());
        configs.put("/api/topsearch/aiqiyi/zongbang", PlatformConfig.builder().build());
        configs.put("/api/topsearch/youku/dianshiju", PlatformConfig.builder().build());
        configs.put("/api/topsearch/youku/dianying", PlatformConfig.builder().build());
        configs.put("/api/topsearch/youku/dongman", PlatformConfig.builder().build());
        configs.put("/api/topsearch/youku/zongyi", PlatformConfig.builder().build());
        configs.put("/api/topsearch/youku/zongbang", PlatformConfig.builder().build());
        configs.put("/api/topsearch/mangguo/dianshiju", PlatformConfig.builder().build());
        configs.put("/api/topsearch/mangguo/dianying", PlatformConfig.builder().build());
        configs.put("/api/topsearch/mangguo/dongman", PlatformConfig.builder().build());
        configs.put("/api/topsearch/mangguo/zongyi", PlatformConfig.builder().build());
        configs.put("/api/topsearch/mangguo/zongbang", PlatformConfig.builder().build());
        configs.put("/api/topsearch/maoyan/zhoupiaofangbang", PlatformConfig.builder().build());
        configs.put("/api/topsearch/maoyan/xiangkanbang", PlatformConfig.builder().build());
        configs.put("/api/topsearch/maoyan/goupiaopingfenbang", PlatformConfig.builder().build());
        configs.put("/api/topsearch/maoyan/top100", PlatformConfig.builder().build());
        configs.put("/api/topsearch/jinrongjie", PlatformConfig.builder().build());
        configs.put("/api/topsearch/diyicaijing", PlatformConfig.builder().build());
        configs.put("/api/topsearch/tonghuashun", PlatformConfig.builder().build());
        configs.put("/api/topsearch/huaerjiejianwen", PlatformConfig.builder().build());
        configs.put("/api/topsearch/cailianshe", PlatformConfig.builder().build());
        configs.put("/api/topsearch/gelonghui", PlatformConfig.builder().build());
        configs.put("/api/topsearch/fabu", PlatformConfig.builder().build());
        configs.put("/api/topsearch/jinshi", PlatformConfig.builder().build());
        configs.put("/api/topsearch/niuyueshibao", PlatformConfig.builder().build());
        configs.put("/api/topsearch/bbc", PlatformConfig.builder().build());
        configs.put("/api/topsearch/faguang", PlatformConfig.builder().build());
        configs.put("/api/topsearch/woshipm", PlatformConfig.builder().build());
        configs.put("/api/topsearch/youshewang", PlatformConfig.builder().build());
        configs.put("/api/topsearch/zhanku/qianlibang", PlatformConfig.builder().build());
        configs.put("/api/topsearch/zhanku/zuopinbang", PlatformConfig.builder().build());
        configs.put("/api/topsearch/zhanku/wenzhangbang", PlatformConfig.builder().build());
        configs.put("/api/topsearch/tuyawangguo/hot", PlatformConfig.builder().build());
        configs.put("/api/topsearch/tuyawangguo/best", PlatformConfig.builder().build());
        configs.put("/api/topsearch/tuyawangguo/new", PlatformConfig.builder().build());
        configs.put("/api/topsearch/tuyawangguo/fx", PlatformConfig.builder().build());
        configs.put("/api/topsearch/shejidaren", PlatformConfig.builder().build());
        configs.put("/api/topsearch/topys", PlatformConfig.builder().build());
        configs.put("/api/topsearch/archdaily", PlatformConfig.builder().build());
        configs.put("/api/topsearch/dribbble", PlatformConfig.builder().build());
        configs.put("/api/topsearch/awwwards", PlatformConfig.builder().build());
        configs.put("/api/topsearch/core77", PlatformConfig.builder().build());
        configs.put("/api/topsearch/abduzeedo", PlatformConfig.builder().build());
        configs.put("/api/topsearch/mit", PlatformConfig.builder().build());
        configs.put("/api/topsearch/eurekalert", PlatformConfig.builder().build());
        configs.put("/api/topsearch/guojikejichuangxinzhongxin/rengongzhineng", PlatformConfig.builder().build());
        configs.put("/api/topsearch/guojikejichuangxinzhongxin/yiyaojiankang", PlatformConfig.builder().build());
        configs.put("/api/topsearch/jiqizhixin", PlatformConfig.builder().build());
        configs.put("/api/topsearch/hupu", PlatformConfig.builder().build());
        configs.put("/api/topsearch/dongqiudi", PlatformConfig.builder().build());
        configs.put("/api/topsearch/xinlangtiyu", PlatformConfig.builder().build());
        configs.put("/api/topsearch/souhutiyu", PlatformConfig.builder().build());
        configs.put("/api/topsearch/tiyuwangyi", PlatformConfig.builder().build());
        configs.put("/api/topsearch/yangshitiyu", PlatformConfig.builder().build());
        configs.put("/api/topsearch/pptiyu", PlatformConfig.builder().build());
        configs.put("/api/topsearch/zhiboba", PlatformConfig.builder().build());
        configs.put("/api/topsearch/v2ex", PlatformConfig.builder().build());
        configs.put("/api/topsearch/buxingjiehupu", PlatformConfig.builder().build());
        configs.put("/api/topsearch/nga", PlatformConfig.builder().build());
        configs.put("/api/topsearch/yimusanfendi", PlatformConfig.builder().build());
        configs.put("/api/topsearch/wenzhangjuejin", PlatformConfig.builder().build());
        configs.put("/api/topsearch/hackernews", PlatformConfig.builder().build());
        configs.put("/api/topsearch/xiaozudouban/aimaozaopen", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/aimaoshenghuo", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/maizu", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/pinzu", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/gouzu", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/xiachufang", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/jiemaoyekeai", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/wodechengshipaigeinikan", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/jiapiantuijian", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/shechumaifanggongjinhui", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/cunzhuangaihaozhe", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/youyidexiaochuan", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/shehuixingsiwang", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/taitoukanshu", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/lanrenshenghuozhibei", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/keaishiwufenxiang", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/jintianchuanshenme", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/xiaofeizhuyinixingzhe", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/womendoubudongche", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/womendoubudongrenqingshigu", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/niaozu", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/renjianqinglvguancha", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/zhichangtucaodahui", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/jiaoshi", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/shangbanzhejianshi", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/youminxingkong", PlatformConfig.builder().build());
        configs.put("/api/topsearch/3dmgame", PlatformConfig.builder().build());
        configs.put("/api/topsearch/a9vg", PlatformConfig.builder().build());
        configs.put("/api/topsearch/youxituoluo", PlatformConfig.builder().build());
        configs.put("/api/topsearch/ign", PlatformConfig.builder().build());
        configs.put("/api/topsearch/gcores", PlatformConfig.builder().build());
        configs.put("/api/topsearch/youyanshe", PlatformConfig.builder().build());
        configs.put("/api/topsearch/17173", PlatformConfig.builder().build());
        configs.put("/api/topsearch/youxiawang", PlatformConfig.builder().build());
        configs.put("/api/topsearch/shengwugu", PlatformConfig.builder().build());
        configs.put("/api/topsearch/yiyaomofang", PlatformConfig.builder().build());
        configs.put("/api/topsearch/dingxiangyisheng", PlatformConfig.builder().build());
        configs.put("/api/topsearch/dingxiangyuanshequ", PlatformConfig.builder().build());
        configs.put("/api/topsearch/shengmingshibao", PlatformConfig.builder().build());
        configs.put("/api/topsearch/jiayidajiankang", PlatformConfig.builder().build());
        configs.put("/api/topsearch/guoke", PlatformConfig.builder().build());
        configs.put("/api/topsearch/jiankangshibaowang", PlatformConfig.builder().build());
        configs.put("/api/topsearch/cctv/1", PlatformConfig.builder().build());
        configs.put("/api/topsearch/cctv/2", PlatformConfig.builder().build());
        configs.put("/api/topsearch/cctv/3", PlatformConfig.builder().build());
        configs.put("/api/topsearch/cctv/4", PlatformConfig.builder().build());
        configs.put("/api/topsearch/cctv/europe", PlatformConfig.builder().build());
        configs.put("/api/topsearch/cctv/america", PlatformConfig.builder().build());
        configs.put("/api/topsearch/cctv/5", PlatformConfig.builder().build());
        configs.put("/api/topsearch/cctv/5plus", PlatformConfig.builder().build());
        configs.put("/api/topsearch/cctv/6", PlatformConfig.builder().build());
        configs.put("/api/topsearch/cctv/7", PlatformConfig.builder().build());
        configs.put("/api/topsearch/cctv/8", PlatformConfig.builder().build());
        configs.put("/api/topsearch/cctv/jilu", PlatformConfig.builder().build());
        configs.put("/api/topsearch/cctv/10", PlatformConfig.builder().build());
        configs.put("/api/topsearch/cctv/11", PlatformConfig.builder().build());
        configs.put("/api/topsearch/cctv/12", PlatformConfig.builder().build());
        configs.put("/api/topsearch/cctv/13", PlatformConfig.builder().build());
        configs.put("/api/topsearch/cctv/child", PlatformConfig.builder().build());
        configs.put("/api/topsearch/cctv/15", PlatformConfig.builder().build());
        configs.put("/api/topsearch/cctv/16", PlatformConfig.builder().build());
        configs.put("/api/topsearch/cctv/17", PlatformConfig.builder().build());
        configs.put("/api/topsearch/pengpaixinwen", PlatformConfig.builder().build());
        configs.put("/api/cachesearch/realtimesummary", PlatformConfig.builder().requestCycle(300L).build());
        configs.put("/api/cachesearch/wordcloud", PlatformConfig.builder().build());
    }

    // 获取全部已启用的key
    public List<String> getAllEnabledKeys() {
        return configs.entrySet().stream()
                .filter(entry -> entry.getValue().getEnabled())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // 根据requestCycle获取已启用的Keys
    public List<String> getAllEnabledKeysByRequestCycle(Long cycle) {
        return configs.entrySet().stream()
                .filter(entry -> entry.getValue().getEnabled()
                        && cycle.equals(entry.getValue().getRequestCycle()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // 获取所有已启用的配置
    public Map<String, PlatformConfig> getAllEnabledConfigs() {
        return configs.entrySet().stream()
                .filter(entry -> entry.getValue().getEnabled())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    // 根据requestDelay获取所有已启用的配置
    public Map<String, PlatformConfig> getAllEnabledConfigsByRequestCycle(Long cycle) {
        return configs.entrySet().stream()
                .filter(entry -> entry.getValue().getEnabled()
                        && cycle.equals(entry.getValue().getRequestCycle()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

}
