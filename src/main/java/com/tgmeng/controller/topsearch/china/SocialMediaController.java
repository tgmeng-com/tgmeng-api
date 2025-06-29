package com.tgmeng.controller.topsearch.china;

import com.tgmeng.model.vo.topsearch.TopSearchVO;
import com.tgmeng.service.topsearch.china.ISocialMediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * description: 社交媒体
 * package: com.tgmeng.controller.topsearch.china
 * className: SocialMedia
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/6/29 0:29
*/
@RestController
@RequiredArgsConstructor
@RequestMapping("/topsearch/china/socialmedia")
public class SocialMediaController {

    private final ISocialMediaService socialMediaService;
    /**
     * description: 百度热搜
     * method: getSocialMedia
     *
     * @author tgmeng
     * @since 2025/6/29 0:52
    */
    @RequestMapping("/baidu")
    public List<TopSearchVO> getBaiDuTopSearch() {
        return socialMediaService.getBaiDuTopSearch();
    }
    /**
     * description: B站热搜
     * method: getBilibiliTopSearch
     *
     * @author tgmeng
     * @since 2025/6/29 15:32
    */
    @RequestMapping("/bilibili")
    public List<TopSearchVO> getBilibiliTopSearch() {
        return socialMediaService.getBilibiliTopSearch();
    }

    /**
     * description: 微博热搜
     * method: getWeiBoTopSearch
     *
     * @author tgmeng
     * @since 2025/6/29 18:49
    */
    @RequestMapping("/weibo")
    public List<TopSearchVO> getWeiBoTopSearch() {
        return socialMediaService.getWeiBoTopSearch();
    }

    /**
     * description: 抖音热搜
     * method: getDouYinTopSearch
     *
     * @author tgmeng
     * @since 2025/6/29 22:39
    */
    @RequestMapping("/douyin")
    public List<TopSearchVO> getDouYinTopSearch() {
        return socialMediaService.getDouYinTopSearch();
    }
}
