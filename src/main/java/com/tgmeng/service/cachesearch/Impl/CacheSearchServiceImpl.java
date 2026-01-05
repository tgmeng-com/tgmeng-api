package com.tgmeng.service.cachesearch.Impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.enums.business.PlatFormCategoryEnum;
import com.tgmeng.common.enums.business.PlatFormCategoryRootEnum;
import com.tgmeng.common.enums.business.SearchModeEnum;
import com.tgmeng.common.enums.enumcommon.EnumUtils;
import com.tgmeng.common.exception.ServerException;
import com.tgmeng.common.mapper.mapstruct.topsearch.ITopSearchCommonMapper;
import com.tgmeng.common.util.*;
import com.tgmeng.model.dto.ai.response.AICommonChatModelResponseCustomDTO;
import com.tgmeng.model.dto.ai.response.AiChatModelResponseMessageContentForHotSearchDTO;
import com.tgmeng.model.dto.ai.response.AiChatModelResponseMessageContentForRealtimesummaryDTO;
import com.tgmeng.model.vo.topsearch.TopSearchCommonVO;
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

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Value("${my-config.history.keep-day}")
    private Integer historyDataKeepDay;

    @Autowired
    private ITopSearchHistoryService topSearchHistoryService;

    @Autowired
    ITopSearchCommonMapper iTopSearchCommonMapper;


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
        Map<String, String> paramMap = new HashMap<>();
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
                            if (title instanceof String s) {
                                s = title.toString().trim().toLowerCase();   // 确保 title 转成小写字符串
                                String lowerCaseWord = null;
                                if (StrUtil.isNotBlank(word)) {
                                    lowerCaseWord = word.trim().toLowerCase();
                                }
                                // 匹配关键词
                                if ((StrUtil.isBlank(lowerCaseWord) || StrUtil.contains(s, lowerCaseWord)) && ObjUtil.isNotEmpty(url)) {
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
            List<String> allOriginalTitles = getCacheTitleByCategory(null);
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

    // 每分钟的全局实时简报
    @Override
    public ResultTemplateBean getCacheSearchRealTimeSummary() {
        try {
            PlatFormCategoryRootEnum categoryRootEnum = EnumUtils.getEnumByKey(PlatFormCategoryRootEnum.class, HttpRequestUtil.getRequestPathLastWord());
            List<String> allOriginalTitles = getCacheTitleByCategory(categoryRootEnum);
            if (allOriginalTitles.isEmpty()) {
                return ResultTemplateBean.success(Collections.emptyList());
            }
            // 输入的内容：实时简报模板+热点数据
            String content = FileUtil.readFileToStringFromClasspath("template/AISummaryTemplate.txt") + String.join(System.lineSeparator(), allOriginalTitles);
            //String content = "帮我写一个笑话";
            AICommonChatModelResponseCustomDTO result = aiRequestUtil.aiChat(content);
            // 处理结果
            AiChatModelResponseMessageContentForRealtimesummaryDTO data = MAPPER.readValue(result.getMessageContent(), AiChatModelResponseMessageContentForRealtimesummaryDTO.class);
            result.setData(data);
            result.setMessageContent(null);
            return ResultTemplateBean.success(result);
        } catch (Exception e) {
            throw new ServerException("AI简报处理失败：" + e.getMessage());
        }
    }

    // 获得糖果热榜
    @Override
    public ResultTemplateBean getTgmengHotSearch() {
        try {
            List<String> allOriginalTitles = getCacheTitleByCategory(null);
            if (allOriginalTitles.isEmpty()) {
                return ResultTemplateBean.success(Collections.emptyList());
            }
            PlatFormCategoryEnum platFormCategoryEnum = EnumUtils.getEnumByKey(PlatFormCategoryEnum.class, HttpRequestUtil.getRequestPathLastWord());
            String templateName = "";
            switch (platFormCategoryEnum) {
                case PlatFormCategoryEnum.TGMENG_ALL:
                    templateName = "AIHotSearchTemplateForAll";
                    break;
                case PlatFormCategoryEnum.TGMENG_TECHNOLOGY:
                    templateName = "AIHotSearchTemplateForTechnology";
                    break;
                case PlatFormCategoryEnum.TGMENG_FINANCE:
                    templateName = "AIHotSearchTemplateForFinance";
                    break;
                case PlatFormCategoryEnum.TGMENG_ENTERTAINMENT:
                    templateName = "AIHotSearchTemplateForEntertainment";
                    break;
                case PlatFormCategoryEnum.TGMENG_CAR:
                    templateName = "AIHotSearchTemplateForCar";
                    break;
                case PlatFormCategoryEnum.TGMENG_SPORTS:
                    templateName = "AIHotSearchTemplateForSports";
                    break;
                case PlatFormCategoryEnum.TGMENG_GAME:
                    templateName = "AIHotSearchTemplateForGame";
                    break;
                case PlatFormCategoryEnum.TGMENG_LIVELIHOOD:
                    templateName = "AIHotSearchTemplateForLivelihood";
                    break;
                default:
                    templateName = "AIHotSearchTemplateForAll";
                    break;
            }
            String content = FileUtil.readFileToStringFromClasspath("template/" + templateName + ".txt") + String.join(System.lineSeparator(), allOriginalTitles);
            AICommonChatModelResponseCustomDTO result = aiRequestUtil.aiChat(content);
            AiChatModelResponseMessageContentForHotSearchDTO data = MAPPER.readValue(result.getMessageContent(), AiChatModelResponseMessageContentForHotSearchDTO.class);
            // 转换为热点数据结构
            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
            topSearchCommonVOS = iTopSearchCommonMapper.tgmengHotSearch2TopSearchCommonVos(data.getData());
            TopSearchCommonVO topSearchResult = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    "糖果梦",
                    "",
                    platFormCategoryEnum.getValue(),
                    PlatFormCategoryRootEnum.TGMENG.getValue()
            );
            return ResultTemplateBean.success(topSearchResult);
        } catch (Exception e) {
            throw new ServerException("热搜词列表处理：" + e.getMessage());
        }
    }

    // 根据分类获取热点标题，参数为null则传回所有热点标题(排除掉了噪点比较大的一些平台)
    public List<String> getCacheTitleByCategory(PlatFormCategoryRootEnum categoryRootEnum) {
        //获取缓存里所有数据
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("word", null);
        paramMap.put("searchMode", SearchModeEnum.MO_HU_PI_PEI_ONE_MINUTES.getValue());
        List<Map<String, Object>> hotList = searchByWord(paramMap).getData();
        List<String> titles = new ArrayList<>();
        // 参数为null，返回全局数据，不包含噪点大的平台
        if (null == categoryRootEnum) {
            Set<String> EXCLUDED_PLATFORM_CATEGORIES = cacheUtil.EXCLUDED_PLATFORM_CATEGORIES;
            Set<String> EXCLUDED_PLATFORM_CATEGORIES_ROOT = cacheUtil.EXCLUDED_PLATFORM_CATEGORIES_ROOT;
            Set<String> EXCLUDED_PLATFORM_NAMES = cacheUtil.EXCLUDED_PLATFORM_NAMES;
            List<Map<String, Object>> filteredList = hotList.stream()
                    .filter(item -> {
                        String platformName = Objects.toString(item.get("platformName"), "");
                        String platformCategory = Objects.toString(item.get("platformCategory"), "");
                        String platformCategoryRoot = Objects.toString(item.get("platformCategoryRoot"), "");
                        return !EXCLUDED_PLATFORM_NAMES.contains(platformName)
                                && !EXCLUDED_PLATFORM_CATEGORIES.contains(platformCategory)
                                && !EXCLUDED_PLATFORM_CATEGORIES_ROOT.contains(platformCategoryRoot);
                    }).toList();
            // 取出里面的title
            titles = filteredList.stream()
                    .map(map -> (String) map.get("title"))
                    .toList();
        } else {
            // 返回单独分类下的所有标题
            List<Map<String, Object>> filteredList = hotList.stream()
                    .filter(item -> {
                        String platformCategoryRoot = Objects.toString(item.get("platformCategoryRoot"), "");
                        return StrUtil.equals(platformCategoryRoot, categoryRootEnum.getValue());
                    }).toList();
            titles = filteredList.stream()
                    .map(map -> (String) map.get("title"))
                    .toList();
        }
        return titles;
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
