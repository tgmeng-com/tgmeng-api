package com.tgmeng.service.cachesearch.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.config.AIPlatformConfigService;
import com.tgmeng.common.forest.client.ai.IAIClient;
import com.tgmeng.common.util.AIRequestUtil;
import com.tgmeng.common.util.CacheUtil;
import com.tgmeng.common.util.FileUtil;
import com.tgmeng.model.dto.ai.config.AIPlatformConfig;
import com.tgmeng.model.dto.ai.response.AiChatModelResponseContentTemplateDTO;
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
    private AIPlatformConfigService aiPlatformConfigService;

    private final IAIClient aiClient;
    private final AIRequestUtil aiRequestUtil;
    private final CacheUtil cacheUtil;

    /**
     * 按关键词检索缓存
     */
    @Override
    public ResultTemplateBean<List<Map<String, Object>>> getCacheSearchAllByWord(String word,List<String>  words) {

        Collection<Object> cacheValue = cacheUtil.getValue();
        if (CollectionUtil.isEmpty(cacheValue)) {
            return ResultTemplateBean.success(new ArrayList<>());
        }
        List<Map<String, Object>> resultList = new ArrayList<>();

        cacheValue.forEach(t -> {
            if (t instanceof Map<?, ?> map) {
                Object dataInfoObj = map.get("dataInfo");
                if (dataInfoObj instanceof List<?> dataInfoList) {
                    dataInfoList.forEach(item -> {
                        if (item instanceof Map<?, ?> itemMap) {
                            Object keyword = itemMap.get("keyword");
                            Object url = itemMap.get("url");
                            if (keyword instanceof String s){
                                s = keyword.toString().trim().toLowerCase();   // 确保 keyword 转成小写字符串
                                // 合并 words 和 word
                                List<String> merged = new ArrayList<>();
                                if (CollUtil.isNotEmpty(words)) {
                                    // 统一小写
                                    merged.addAll(words.stream()
                                            .filter(StrUtil::isNotBlank)
                                            .map(String::trim)
                                            .map(String::toLowerCase)
                                            .toList()
                                    );
                                }
                                if (StrUtil.isNotBlank(word)) {
                                    merged.add(word.trim().toLowerCase());
                                }
                                // 最终数组
                                String[] wordsMerged = merged.toArray(new String[0]);

                                // 匹配关键词
                                if (wordsMerged.length == 0 || StrUtil.containsAny(s,wordsMerged)){
                                    HashMap<String, Object> resultMap = new HashMap<>();
                                    resultMap.put("keyword", keyword);
                                    resultMap.put("dataCardName", map.get("dataCardName"));
                                    resultMap.put("url", url);
                                    resultList.add(resultMap);
                                }
                            }
                        }
                    });
                }
            }
        });
        return ResultTemplateBean.success(resultList);
    }

    /**
     * 生成热点词云
     */
    @Override
    public ResultTemplateBean getCacheSearchWordCloud() {
        try {
            // 2. 收集所有 keyword 文本
            List<String> allOriginalKeywords = cacheUtil.getAllCacheTitle();
            if (allOriginalKeywords.isEmpty()) {
                return ResultTemplateBean.success(Collections.emptyList());
            }

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
            // 4. 生成词云数据（取前 500 个）
            List<WordCloudData> cloud = generateWordCloudData(keywordFrequencyMap);
            return ResultTemplateBean.success(cloud);
        } catch (Exception e) {
            log.error("生成词云时发生异常", e);
            return ResultTemplateBean.success("");
        }
    }

    @Override
    public ResultTemplateBean getCacheSearchRealTimeSummary() {
        // 2. 收集所有 keyword 文本
        List<String> allOriginalKeywords = cacheUtil.getAllCacheTitle();
        if (allOriginalKeywords.isEmpty()) {
            return ResultTemplateBean.success(Collections.emptyList());
        }
        // 交给AI处理，根据所有热点标题，生成一个实时简报
        String content = FileUtil.readFileToStringFromClasspath("template/AISummaryTemplate.txt") + allOriginalKeywords;
        //String content = "帮我写一个笑话";

        // 测试数据
        //AIPlatformConfig aiPlatformConfig = new AIPlatformConfig()
        //        .setPlatform("NVIDIA")
        //        .setApi("https://integrate.api.nvidia.com/v1/chat/completions")
        //        .setKey("nvapi-tg8HLdWOQZ8NWJ0PgoLR")
        //        .setFrom("LinuxDo公益站 黑与白站长大佬推荐")
        //        .setModels(List.of(
        //                "deepseek-ai/deepseek-v3.1",
        //                "openai/gpt-oss-120b"));
        //List<AIPlatformConfig> aiPlatformConfigs = new ArrayList<>(List.of(aiPlatformConfig));

        // 生产数据
        List<AIPlatformConfig> aiPlatformConfigs = aiPlatformConfigService.getAiPlatformConfigs();

        AiChatModelResponseContentTemplateDTO aiChatResult = aiRequestUtil.aiChat(content, aiPlatformConfigs);
        return ResultTemplateBean.success(aiChatResult);
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
                .limit(1000)
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
