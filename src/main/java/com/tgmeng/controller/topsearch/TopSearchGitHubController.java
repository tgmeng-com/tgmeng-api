package com.tgmeng.controller.topsearch;

import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.service.topsearch.ITopSearchGitHubService;
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
@RequestMapping("/topsearch/github")
public class TopSearchGitHubController {

    private final ITopSearchGitHubService topSearchGitHubService;

    @RequestMapping("/allstars")
    public ResultTemplateBean getBGitHubTopSearch() {
        return topSearchGitHubService.getGithubSortByAllStars();
    }

}
