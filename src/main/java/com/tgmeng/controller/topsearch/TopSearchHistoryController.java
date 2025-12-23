package com.tgmeng.controller.topsearch;

import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.service.history.ITopSearchHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * description: 突发热点、历史热点追踪等
 * package: com.tgmeng.controller.topsearch
 * className: TopSearchGitHubController
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/6/30 2:54
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/topsearch/history")
public class TopSearchHistoryController {
    private final ITopSearchHistoryService topSearchHistoryService;

    // 热点历史数据,指纹查询
    @RequestMapping("/hotpoint")
    public ResultTemplateBean getHotPointHistory(@RequestBody Map<String, String> requestBody) {
        return topSearchHistoryService.getHotPointHistory(requestBody);
    }

    @RequestMapping("/suddenheatpoint/{type}")
    public ResultTemplateBean getSuddenHeatPoint(@PathVariable("type") String type) {
        return topSearchHistoryService.getSuddenHeatPoint(type);
    }

    // 历史数据查询，模糊匹配
    @RequestMapping("/getwordhistory")
    public ResultTemplateBean getWordHistory(@RequestBody Map<String, String> requestBody) {
        return topSearchHistoryService.getWordHistory(requestBody);
    }
}
