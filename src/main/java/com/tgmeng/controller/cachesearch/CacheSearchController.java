package com.tgmeng.controller.cachesearch;

import com.tgmeng.common.annotation.LicenseRequired;
import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.enums.business.LicenseFeatureEnum;
import com.tgmeng.service.cachesearch.ICacheSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor

@RequestMapping("/cachesearch")
public class CacheSearchController {
    private final ICacheSearchService cacheSearchService;

    // 根据type查询所有缓存热搜数据
    @RequestMapping("/allbyword")
    @LicenseRequired(feature = LicenseFeatureEnum.SEARCH)
    public ResultTemplateBean<List<Map<String, Object>>> getCacheSearchAllByWord(@RequestBody Map<String, String> requestBody) {
        return cacheSearchService.searchByWord(requestBody);
    }

    // 查询所有缓存热搜数据的词云
    @RequestMapping("/wordcloud")
    public ResultTemplateBean getCacheSearchWordCloud() {
        return cacheSearchService.getCacheSearchWordCloud();
    }

    // 实时简报
    @RequestMapping("/realtimesummary/{type}")
    @LicenseRequired(feature = LicenseFeatureEnum.AI_SUMMARY)
    public ResultTemplateBean getCacheSearchRealTimeSummary(@PathVariable("type") String type) {
        return cacheSearchService.getCacheSearchRealTimeSummary();
    }

    // 热搜列表
    @RequestMapping("/tgmenghotsearch/{type}")
    public ResultTemplateBean getTgmengHotSearch(@PathVariable("type") String type) {
        return cacheSearchService.getTgmengHotSearch();
    }
}
