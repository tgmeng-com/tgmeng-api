package com.tgmeng.controller.password;

import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.service.password.ITopSearchPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
@RequestMapping("/topsearch/password")
public class TopSearchPasswordController {

    private final ITopSearchPasswordService topSearchPasswordService;

    // 关闭广告的密码
    @RequestMapping("/ads")
    public ResultTemplateBean getAdsPassword(@RequestBody Map<String, String> requestBody) {
        return topSearchPasswordService.getAdsPassword(requestBody);
    }
}
