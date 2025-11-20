package com.tgmeng.service.cachesearch.Impl;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.cache.TopSearchDataCache;
import com.tgmeng.common.schedule.ControllerApiSchedule;
import com.tgmeng.model.vo.topsearch.TopSearchCommonVO;
import com.tgmeng.service.cachesearch.ICacheSearchService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CacheSearchServiceImpl implements ICacheSearchService {

    @Autowired
    private TopSearchDataCache topSearchDataCache;

    @Autowired
    private ControllerApiSchedule controllerApiSchedule;

    /**
     * 按关键词检索缓存
     */
    @Override
    public ResultTemplateBean getCacheSearchAllByWord(String word) {

        controllerApiSchedule.scanAndInvokeControllers();  // 刷新缓存

        List<TopSearchCommonVO> allCacheSearchData =
                topSearchDataCache.getAll(TopSearchCommonVO.class);

        List<TopSearchCommonVO> collect = allCacheSearchData.stream()
                .map(vo -> {
                    if (vo.getDataInfo() == null) return null;

                    List<TopSearchCommonVO.DataInfo> filtered = vo.getDataInfo().stream()
                            .filter(data -> data.getKeyword() != null &&
                                    data.getKeyword().contains(word))
                            .collect(Collectors.toList());

                    return filtered.isEmpty() ? null :
                            new TopSearchCommonVO(filtered, vo.getDataCardName(),
                                    vo.getDataCardLogo(), vo.getDataCardCategory());
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return ResultTemplateBean.success(collect);
    }

    /**
     * 生成热点词云
     */
    @Override
    public ResultTemplateBean getCacheSearchWordCloud() {
        try {
            log.info("开始生成热搜词云");

            controllerApiSchedule.scanAndInvokeControllers();

            // 1. 获取所有缓存
            List<TopSearchCommonVO> allCacheSearchData =
                    topSearchDataCache.getAll(TopSearchCommonVO.class);

            if (allCacheSearchData == null || allCacheSearchData.isEmpty()) {
                return ResultTemplateBean.success(Collections.emptyList());
            }

            // 2. 收集所有 keyword 文本
            List<String> allOriginalKeywords = allCacheSearchData.stream()
                    .filter(vo -> vo.getDataInfo() != null)
                    .flatMap(vo -> vo.getDataInfo().stream())
                    .map(TopSearchCommonVO.DataInfo::getKeyword)
                    .filter(k -> k != null && !k.trim().isEmpty())
                    .collect(Collectors.toList());

            if (allOriginalKeywords.isEmpty()) {
                return ResultTemplateBean.success(Collections.emptyList());
            }

            log.info("共处理 {} 个原始关键词", allOriginalKeywords.size());

            // 3. 使用 HanLP 分词 + 词频统计
            Map<String, Integer> keywordFrequencyMap = new HashMap<>();

            for (String keyword : allOriginalKeywords) {

                List<Term> terms = HanLP.segment(keyword);

                List<String> meaningfulWords = terms.stream()
                        .filter(term -> !isPunctuation(term.nature.toString()))
                        .filter(term -> !isStopWord(term.word))
                        .filter(term -> isMeaningfulWord(term.nature.toString()))
                        .filter(term -> term.word.length() > 1)
                        .map(term -> term.word)
                        .collect(Collectors.toList());

                for (String word : meaningfulWords) {
                    keywordFrequencyMap.put(word,
                            keywordFrequencyMap.getOrDefault(word, 0) + 1);
                }
            }

            log.info("分词后得到 {} 个有效词汇", keywordFrequencyMap.size());

            // 4. 生成词云数据（取前 500 个）
            List<WordCloudData> cloud = generateWordCloudData(keywordFrequencyMap);

            log.info("词云生成完成，共 {} 个词汇", cloud.size());

            return ResultTemplateBean.success(cloud);

        } catch (Exception e) {
            log.error("生成词云时发生异常", e);
            return ResultTemplateBean.success("生成词云失败，请稍后重试");
        }
    }

    /*------------------------- 辅助方法 -------------------------*/

    private boolean isPunctuation(String nature) {
        return nature.startsWith("w");
    }

    private boolean isStopWord(String word) {
        Set<String> stopWords = Set.of("的", "了", "在", "是", "我", "有", "和", "就",
                "不", "人", "都", "一", "一个", "上", "也", "很",
                "到", "说", "要", "去", "你", "会", "着", "没有",
                "看", "好", "自己", "这");
        return stopWords.contains(word);
    }

    private boolean isMeaningfulWord(String nature) {
        return nature.startsWith("n") ||  // 名词
                nature.startsWith("v") ||  // 动词
                nature.startsWith("a") ||  // 形容词
                nature.startsWith("nr") ||
                nature.startsWith("ns") ||
                nature.startsWith("nt") ||
                nature.startsWith("nz");
    }

    /**
     * 按词频排序并取前 500
     */
    private List<WordCloudData> generateWordCloudData(Map<String, Integer> freq) {

        List<Map.Entry<String, Integer>> topEntries = freq.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(500)
                .toList();  // Java 16+

        List<WordCloudData> list = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : topEntries) {
            list.add(new WordCloudData(
                    entry.getKey(),
                    entry.getValue(),
                    calculateFontSize(entry.getValue())
            ));
        }

        return list;
    }

    private int calculateFontSize(int frequency) {
        int minSize = 12;
        int maxSize = 40;
        int maxFreq = 100; // 频率超过100按100算

        return (int) ((double) Math.min(frequency, maxFreq) / maxFreq *
                (maxSize - minSize) + minSize);
    }

    /*------------------------- 词云数据对象 -------------------------*/

    @Data
    public static class WordCloudData {
        private String word;
        private int frequency;
        private int fontSize;

        public WordCloudData(String word, int frequency, int fontSize) {
            this.word = word;
            this.frequency = frequency;
            this.fontSize = fontSize;
        }
    }
}
