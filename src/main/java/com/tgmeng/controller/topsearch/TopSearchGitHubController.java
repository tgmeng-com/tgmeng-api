package com.tgmeng.controller.topsearch;

import com.tgmeng.model.vo.topsearch.TopSearchCommonVO;
import com.tgmeng.service.topsearch.ITopSearchGitHubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
@RequestMapping("/topsearch")
public class TopSearchGitHubController {

    private final ITopSearchGitHubService topSearchGitHubService;

    /**
     * description: 百度热搜
     * method: getBGitHubTopSearch
     *
     * @author tgmeng
     * @since 2025/6/29 0:52
    */
    @RequestMapping("/github")
    public List<TopSearchCommonVO> getBGitHubTopSearch() {
        return topSearchGitHubService.getGitHubTopSearch();
    }
}
