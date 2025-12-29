package com.tgmeng.service.cachesearch.Impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.config.AIPlatformConfigService;
import com.tgmeng.common.enums.business.SearchModeEnum;
import com.tgmeng.common.exception.ServerException;
import com.tgmeng.common.util.*;
import com.tgmeng.model.dto.ai.config.AIPlatformConfig;
import com.tgmeng.model.dto.ai.response.AiChatModelResponseContentTemplateDTO;
import com.tgmeng.service.cachesearch.ICacheSearchService;
import com.tgmeng.service.history.ITopSearchHistoryService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CacheSearchServiceImpl implements ICacheSearchService {

    @Value("${my-config.history.keep-day}")
    private Integer historyDataKeepDay;

    @Autowired
    private AIPlatformConfigService aiPlatformConfigService;
    @Autowired
    private ITopSearchHistoryService topSearchHistoryService;

    private final AIRequestUtil aiRequestUtil;
    private final CacheUtil cacheUtil;


    @Override
    public ResultTemplateBean<List<Map<String, Object>>> searchByWord(Map<String, String> requestBody) {
        String word = requestBody.get("word");
        String searchMode = requestBody.get("searchMode");
        if (StrUtil.isBlank(searchMode)) {
            throw new ServerException("searchMode empty error");
        }
        // 模糊匹配五分钟的，直接从内存里拿
        if (StrUtil.equals(searchMode, SearchModeEnum.MO_HU_PI_PEI_ONE_MINUTES.getValue())) {
            List<Map<String, Object>> result = getCacheSearchAllByWord(word);
            return ResultTemplateBean.success(result);
        }
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("title", word);
        paramMap.put("endTime", TimeUtil.getCurrentTimeFormat(TimeUtil.defaultPattern));
        // 模糊匹配当天
        if (StrUtil.equals(searchMode, SearchModeEnum.MO_HU_PI_PEI_TODAY.getValue())) {
            paramMap.put("startTime", TimeUtil.getTodayStartTime(TimeUtil.defaultPattern));
            return topSearchHistoryService.getWordHistory(paramMap);
        }
        // 模糊匹配历史
        if (StrUtil.equals(searchMode, SearchModeEnum.MO_HU_PI_PEI_HISTORY.getValue())) {
            paramMap.put("startTime", TimeUtil.getTimeBeforeNow(0, 0, 0, historyDataKeepDay, TimeUtil.defaultPattern));
            return topSearchHistoryService.getWordHistory(paramMap);
        }
        // 指纹匹配当天
        if (StrUtil.equals(searchMode, SearchModeEnum.ZHI_WEN_PI_PEI_TODAY.getValue())) {
            paramMap.put("startTime", TimeUtil.getTodayStartTime(TimeUtil.defaultPattern));
            return topSearchHistoryService.getHotPointHistory(paramMap);
        }
        // 指纹匹配历史
        if (StrUtil.equals(searchMode, SearchModeEnum.ZHI_WEN_PI_PEI_HISTORY.getValue())) {
            paramMap.put("startTime", TimeUtil.getTimeBeforeNow(0, 0, 0, historyDataKeepDay, TimeUtil.defaultPattern));
            return topSearchHistoryService.getHotPointHistory(paramMap);
        }
        return ResultTemplateBean.success(new ArrayList<>());
    }
    /**
     * 按关键词检索5分钟内的缓存数据
     */
    public List<Map<String, Object>> getCacheSearchAllByWord(String word) {

        Collection<Object> cacheValue = cacheUtil.getValue();
        if (CollectionUtil.isEmpty(cacheValue)) {
            return new ArrayList<>();
        }
        List<Map<String, Object>> resultList = new ArrayList<>();

        cacheValue.forEach(t -> {
            if (t instanceof Map<?, ?> map) {
                Object dataInfoObj = map.get("dataInfo");
                if (dataInfoObj instanceof List<?> dataInfoList) {
                    dataInfoList.forEach(item -> {
                        if (item instanceof Map<?, ?> itemMap) {
                            Object title = itemMap.get("title");
                            Object url = itemMap.get("url");
                            if (title instanceof String s){
                                s = title.toString().trim().toLowerCase();   // 确保 title 转成小写字符串
                                String lowerCaseWord = null;
                                if (StrUtil.isNotBlank(word)){
                                    lowerCaseWord = word.trim().toLowerCase();
                                }
                                // 匹配关键词
                                if (StrUtil.isBlank(lowerCaseWord) || StrUtil.contains(s,lowerCaseWord)){
                                    HashMap<String, Object> resultMap = new HashMap<>();
                                    resultMap.put("title", title);
                                    resultMap.put("platformName", map.get("platformName"));
                                    resultMap.put("url", url);
                                    resultMap.put("dataUpdateTime", map.get("dataUpdateTime"));
                                    resultMap.put("platformCategory", map.get("platformCategory"));
                                    resultMap.put("platformCategoryRoot", map.get("platformCategoryRoot"));
                                    resultList.add(resultMap);
                                }
                            }
                        }
                    });
                }
            }
        });
        resultList.sort((map1, map2) ->
                map2.get("dataUpdateTime").toString().compareTo(map1.get("dataUpdateTime").toString())
        );
        return resultList;
    }

    /**
     * 生成热点词云
     */
    @Override
    public ResultTemplateBean getCacheSearchWordCloud() {
        try {
            // 2. 收集所有 title 文本
            List<String> allOriginalTitles = cacheUtil.getAllCacheTitle();
            if (allOriginalTitles.isEmpty()) {
                return ResultTemplateBean.success(Collections.emptyList());
            }

            // 3. 使用 HanLP 分词 + 词频统计
            Map<String, Integer> keywordFrequencyMap = new HashMap<>();

            for (String title : allOriginalTitles) {
                List<String> meaningfulWords = HanLPUtil.tokenizeToWords(title);
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
        // 2. 收集所有 title 文本
        List<String> allOriginalTitles = cacheUtil.getAllCacheTitle();
        if (allOriginalTitles.isEmpty()) {
            return ResultTemplateBean.success(Collections.emptyList());
        }
        // 交给AI处理，根据所有热点标题，生成一个实时简报
        String content = FileUtil.readFileToStringFromClasspath("template/AISummaryTemplate.txt") + allOriginalTitles;
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
