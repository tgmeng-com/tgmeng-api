package com.tgmeng.controller.cacheSearch;

import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.service.cachesearch.ICacheSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cachesearch")
public class CacheSearchController {
    private final ICacheSearchService cacheSearchService;

    // 根据type查询所有缓存热搜数据
    @RequestMapping("/allbyword")
    public ResultTemplateBean getCacheSearchAllByWord(@RequestBody Map<String, String> requestBody) {
        return cacheSearchService.getCacheSearchAllByWord(requestBody.get("word"));
    }

    // 查询所有缓存热搜数据的词云
    @RequestMapping("/wordcloud")
    public ResultTemplateBean getCacheSearchWordCloud() {
        return cacheSearchService.getCacheSearchWordCloud();
    }
}
