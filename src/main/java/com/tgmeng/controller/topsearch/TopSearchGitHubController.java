package com.tgmeng.controller.topsearch;

import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.enums.business.DataInfoCardEnum;
import com.tgmeng.common.util.TimeUtil;
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
    public ResultTemplateBean getBGitHubTopSearchAllStars() {
        return topSearchGitHubService.getGithubSortByAllStars("2007-10-01", DataInfoCardEnum.GITHUB_ALL_STAR);
    }

    @RequestMapping("/daystars")
    public ResultTemplateBean getBGitHubTopSearchDayStars() {
        return topSearchGitHubService.getGithubSortByAllStars(TimeUtil.getTimeBeforeNow(0, 0, 0, 1, TimeUtil.defultSimplePattern),DataInfoCardEnum.GITHUB_DAY_STAR);
    }

    @RequestMapping("/weekstars")
    public ResultTemplateBean getBGitHubTopSearchWeektars() {
        return topSearchGitHubService.getGithubSortByAllStars(TimeUtil.getTimeBeforeNow(0, 0, 1, 0, TimeUtil.defultSimplePattern),DataInfoCardEnum.GITHUB_WEEK_STAR);
    }

    @RequestMapping("/monthstars")
    public ResultTemplateBean getBGitHubTopSearchMonthStars() {
        return topSearchGitHubService.getGithubSortByAllStars(TimeUtil.getTimeBeforeNow(0, 1, 0, 0, TimeUtil.defultSimplePattern),DataInfoCardEnum.GITHUB_MONTH_STAR);
    }

    @RequestMapping("/yearstars")
    public ResultTemplateBean getBGitHubTopSearchYearStars() {
        return topSearchGitHubService.getGithubSortByAllStars(TimeUtil.getTimeBeforeNow(1, 0, 0, 0, TimeUtil.defultSimplePattern),DataInfoCardEnum.GITHUB_YEAR_STAR);
    }

    @RequestMapping("/threeyearstars")
    public ResultTemplateBean getBGitHubTopSearchThreeYearStars() {
        return topSearchGitHubService.getGithubSortByAllStars(TimeUtil.getTimeBeforeNow(2, 0, 0, 0, TimeUtil.defultSimplePattern),DataInfoCardEnum.GITHUB_THREE_YEAR_STAR);
    }

    @RequestMapping("/fiveyearstars")
    public ResultTemplateBean getBGitHubTopSearchFiveYearStars() {
        return topSearchGitHubService.getGithubSortByAllStars(TimeUtil.getTimeBeforeNow(2, 0, 0, 0, TimeUtil.defultSimplePattern),DataInfoCardEnum.GITHUB_FIVE_YEAR_STAR);
    }

    @RequestMapping("/tenyearstars")
    public ResultTemplateBean getBGitHubTopSearchTenYearStars() {
        return topSearchGitHubService.getGithubSortByAllStars(TimeUtil.getTimeBeforeNow(2, 0, 0, 0, TimeUtil.defultSimplePattern),DataInfoCardEnum.GITHUB_TEN_YEAR_STAR);
    }

}
