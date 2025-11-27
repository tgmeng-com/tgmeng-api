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
        configs.put("/api/topsearch/biaoshengwangyiyun", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xingegwangyiyun", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/yuanchuangwangyiyun", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/regewangyiyun", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/tiebabaidu", PlatformConfig.builder().build());
        configs.put("/api/topsearch/shaoshupai", PlatformConfig.builder().build());
        configs.put("/api/topsearch/dianshijubaidu", PlatformConfig.builder().build());
        configs.put("/api/topsearch/xiaoshuobaidu", PlatformConfig.builder().build());
        configs.put("/api/topsearch/dianyingbaidu", PlatformConfig.builder().build());
        configs.put("/api/topsearch/youxibaidu", PlatformConfig.builder().build());
        configs.put("/api/topsearch/qichebaidu", PlatformConfig.builder().build());
        configs.put("/api/topsearch/regengbaidu", PlatformConfig.builder().build());
        configs.put("/api/topsearch/caijingbaidu", PlatformConfig.builder().build());
        configs.put("/api/topsearch/minshengbaidu", PlatformConfig.builder().build());
        configs.put("/api/topsearch/zhihu", PlatformConfig.builder().build());
        configs.put("/api/topsearch/github/allstars", PlatformConfig.builder().requestCycle(1200L).requestDelay(10L).build());
        configs.put("/api/topsearch/github/daystars", PlatformConfig.builder().requestCycle(1200L).requestDelay(10L).build());
        configs.put("/api/topsearch/github/weekstars", PlatformConfig.builder().requestCycle(1200L).requestDelay(10L).build());
        configs.put("/api/topsearch/github/monthstars", PlatformConfig.builder().requestCycle(1200L).requestDelay(10L).build());
        configs.put("/api/topsearch/github/yearstars", PlatformConfig.builder().requestCycle(1200L).requestDelay(10L).build());
        configs.put("/api/topsearch/github/threeyearstars", PlatformConfig.builder().requestCycle(1200L).requestDelay(10L).build());
        configs.put("/api/topsearch/github/fiveyearstars", PlatformConfig.builder().requestCycle(1200L).requestDelay(10L).build());
        configs.put("/api/topsearch/github/tenyearstars", PlatformConfig.builder().requestCycle(1200L).requestDelay(10L).build());
        configs.put("/api/topsearch/global/youtube", PlatformConfig.builder().build());
        configs.put("/api/topsearch/global/huggingfacespacestrending", PlatformConfig.builder().build());
        configs.put("/api/topsearch/global/huggingfacespaceslikes", PlatformConfig.builder().build());
        configs.put("/api/topsearch/global/huggingfacemodelstrending", PlatformConfig.builder().build());
        configs.put("/api/topsearch/global/huggingfacemodellikes", PlatformConfig.builder().build());
        configs.put("/api/topsearch/global/huggingfacedatasetstrending", PlatformConfig.builder().build());
        configs.put("/api/topsearch/global/huggingfacedatasetslikes", PlatformConfig.builder().build());
        configs.put("/api/topsearch/dianshijutengxun", PlatformConfig.builder().build());
        configs.put("/api/topsearch/dianyingtengxun", PlatformConfig.builder().build());
        configs.put("/api/topsearch/dongmantengxun", PlatformConfig.builder().build());
        configs.put("/api/topsearch/zongyitengxun", PlatformConfig.builder().build());
        configs.put("/api/topsearch/zongbangtengxun", PlatformConfig.builder().build());
        configs.put("/api/topsearch/dianshijuaiqiyi", PlatformConfig.builder().build());
        configs.put("/api/topsearch/dianyingaiqiyi", PlatformConfig.builder().build());
        configs.put("/api/topsearch/dongmanaiqiyi", PlatformConfig.builder().build());
        configs.put("/api/topsearch/zongyiaiqiyi", PlatformConfig.builder().build());
        configs.put("/api/topsearch/zongbangaiqiyi", PlatformConfig.builder().build());
        configs.put("/api/topsearch/dianshijuyouku", PlatformConfig.builder().build());
        configs.put("/api/topsearch/dianyingyouku", PlatformConfig.builder().build());
        configs.put("/api/topsearch/dongmanyouku", PlatformConfig.builder().build());
        configs.put("/api/topsearch/zongyiyouku", PlatformConfig.builder().build());
        configs.put("/api/topsearch/zongbangyouku", PlatformConfig.builder().build());
        configs.put("/api/topsearch/dianshijumangguo", PlatformConfig.builder().build());
        configs.put("/api/topsearch/dianyingmangguo", PlatformConfig.builder().build());
        configs.put("/api/topsearch/dongmanmangguo", PlatformConfig.builder().build());
        configs.put("/api/topsearch/zongyimangguo", PlatformConfig.builder().build());
        configs.put("/api/topsearch/zongbangmangguo", PlatformConfig.builder().build());
        configs.put("/api/topsearch/zhoupiaofangbangmaoyan", PlatformConfig.builder().build());
        configs.put("/api/topsearch/xiangkanbangmaoyan", PlatformConfig.builder().build());
        configs.put("/api/topsearch/goupiaopingfenbangmaoyan", PlatformConfig.builder().build());
        configs.put("/api/topsearch/top100maoyan", PlatformConfig.builder().build());
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
        configs.put("/api/topsearch/dajiyuan", PlatformConfig.builder().build());
        configs.put("/api/topsearch/woshipm", PlatformConfig.builder().build());
        configs.put("/api/topsearch/youshewang", PlatformConfig.builder().build());
        configs.put("/api/topsearch/qianlibangzhanku", PlatformConfig.builder().build());
        configs.put("/api/topsearch/zuopinbangzhanku", PlatformConfig.builder().build());
        configs.put("/api/topsearch/wenzhangbangzhanku", PlatformConfig.builder().build());
        configs.put("/api/topsearch/remenzuopintuyawangguo", PlatformConfig.builder().build());
        configs.put("/api/topsearch/jingxuanzuopintuyawangguo", PlatformConfig.builder().build());
        configs.put("/api/topsearch/jinrixinzuotuyawangguo", PlatformConfig.builder().build());
        configs.put("/api/topsearch/faxianxinzuotuyawangguo", PlatformConfig.builder().build());
        configs.put("/api/topsearch/shejidaren", PlatformConfig.builder().build());
        configs.put("/api/topsearch/topys", PlatformConfig.builder().build());
        configs.put("/api/topsearch/archdaily", PlatformConfig.builder().build());
        configs.put("/api/topsearch/dribbble", PlatformConfig.builder().build());
        configs.put("/api/topsearch/awwwards", PlatformConfig.builder().build());
        configs.put("/api/topsearch/core77", PlatformConfig.builder().build());
        configs.put("/api/topsearch/abduzeedo", PlatformConfig.builder().build());
        configs.put("/api/topsearch/mit", PlatformConfig.builder().build());
        configs.put("/api/topsearch/zhongguokexueyuan", PlatformConfig.builder().build());
        configs.put("/api/topsearch/eurekalert", PlatformConfig.builder().build());
        configs.put("/api/topsearch/rengongzhinengguojikejichuangxinzhongxin", PlatformConfig.builder().build());
        configs.put("/api/topsearch/yiyaojiankangguojikejichuangxinzhongxin", PlatformConfig.builder().build());
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
        configs.put("/api/topsearch/aimaozaopendouban", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/aimaoshenghuodouban", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/maizudouban", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/pinzudouban", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/gouzudouban", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/XIA_CHU_FANG_DOU_BAN", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/JIE_MAO_YE_KE_AI_DOU_BAN", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/WO_DE_CHENG_SHI_PAI_GEI_NI_KAN_DOU_BAN", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/JIA_PIAN_TUI_JIAN_DOU_BAN", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/SHE_CHU_MAI_FANG_GONG_JIN_HUI_DOU_BAN", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/CUN_ZHUANG_AI_HAO_ZHE_DOU_BAN", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/YOU_YI_DE_XIAO_CHUAN_DOU_BAN", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/SHE_HUI_XING_SI_WANG_DOU_BAN", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/TAI_TOU_KAN_SHU_DOU_BAN", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/LAN_REN_SHENG_HUO_ZHI_BEI_DOU_BAN", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/KE_AI_SHI_WU_FEN_XIANG_DOU_BAN", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/JIN_TIAN_CHUAN_SHEN_ME_DOU_BAN", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/XIAO_FEI_ZHU_YI_NI_XING_ZHE_DOU_BAN", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/WO_MEN_DOU_BU_DONG_CHE_DOU_BAN", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/WO_MEN_DOU_BU_DONG_REN_QING_SHI_GU_DOU_BAN", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/DOU_BAN_NIAO_ZU_DOU_BAN", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/REN_JIAN_QING_LV_GUAN_CHA_DOU_BAN", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/ZHI_CHANG_TU_CAO_DA_HUI_DOU_BAN", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/JIAO_SHI_DOU_BAN", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
        configs.put("/api/topsearch/xiaozudouban/SHANG_BAN_ZHE_JIAN_SHI_DOU_BAN", PlatformConfig.builder().requestCycle(300L).requestDelay(10L).build());
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
