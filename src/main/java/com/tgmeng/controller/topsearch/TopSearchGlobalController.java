package com.tgmeng.controller.topsearch;

import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.service.topsearch.ITopSearchGlobalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description: GitHub热搜Controller
 * package: com.tgmeng.controller.topsearch
 * className: TopSearchGitHubController
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/6/30 2:54
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/topsearch/global")
public class TopSearchGlobalController {
    private final ITopSearchGlobalService topSearchGlobalService;

    @RequestMapping("/youtube")
    public ResultTemplateBean getYoutubeTopSearchAllStars() {
        return topSearchGlobalService.getYoutubeSortByTopSearch();
    }



}
