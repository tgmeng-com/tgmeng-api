package com.tgmeng.controller.topsearch;

import com.tgmeng.common.annotation.LicenseRequired;
import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.enums.business.LicenseFeatureEnum;
import com.tgmeng.service.history.ITopSearchHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // 突发热点查询
    @RequestMapping("/suddenheatpoint/{type}")
    @LicenseRequired(feature = LicenseFeatureEnum.SUDDEN_NEWS)
    public ResultTemplateBean getSuddenHeatPoint(@PathVariable("type") String type) {
        return topSearchHistoryService.getSuddenHeatPoint(type);
    }
}
