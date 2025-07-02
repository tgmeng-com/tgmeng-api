package com.tgmeng.controller.topsearch;

import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.service.topsearch.ITopSearchCommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description: 社交媒体
 * package: com.tgmeng.controller.topsearch
 * className: TopSearchCommonController
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/6/29 0:29
*/
@RestController
@RequiredArgsConstructor
@RequestMapping("/topsearch")
public class TopSearchCommonController {

    private final ITopSearchCommonService topSearchCommonService;
    /**
     * description: 百度热搜
     * method: getBaiDuTopSearch
     *
     * @author tgmeng
     * @since 2025/6/29 0:52
    */
    @RequestMapping("/baidu")
    public ResultTemplateBean getBaiDuTopSearch() {
        return topSearchCommonService.getBaiDuTopSearch();
    }
    /**
     * description: B站热搜
     * method: getBilibiliTopSearch
     *
     * @author tgmeng
     * @since 2025/6/29 15:32
    */
    @RequestMapping("/bilibili")
    public ResultTemplateBean getBilibiliTopSearch() {
        return topSearchCommonService.getBilibiliTopSearch();
    }

    /**
     * description: 微博热搜
     * method: getWeiBoTopSearch
     *
     * @author tgmeng
     * @since 2025/6/29 18:49
    */
    @RequestMapping("/weibo")
    public ResultTemplateBean getWeiBoTopSearch() {
        return topSearchCommonService.getWeiBoTopSearch();
    }

    /**
     * description: 抖音热搜
     * method: getDouYinTopSearch
     *
     * @author tgmeng
     * @since 2025/6/29 22:39
    */
    @RequestMapping("/douyin")
    public ResultTemplateBean getDouYinTopSearch() {
        return topSearchCommonService.getDouYinTopSearch();
    }

    @RequestMapping("/douban")
    public ResultTemplateBean getDouBanTopSearch() {
        return topSearchCommonService.getDouBanTopSearch();
    }

    @RequestMapping("/tencent")
    public ResultTemplateBean getTencentTopSearch() {
        return topSearchCommonService.getTencentTopSearch();
    }

    @RequestMapping("/toutiao")
    public ResultTemplateBean getTouTiaoTopSearch() {
        return topSearchCommonService.getTouTiaoTopSearch();
    }
}
