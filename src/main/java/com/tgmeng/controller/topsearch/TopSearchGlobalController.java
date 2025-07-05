package com.tgmeng.controller.topsearch;

import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.enums.business.SearchTypeHuggingFaceEnum;
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


    @RequestMapping("/huggingfacespacestrending")
    public ResultTemplateBean getHuggingFaceTopSearchSpacesTrending() {
        return topSearchGlobalService.getHuggingFaceTopSearch(SearchTypeHuggingFaceEnum.SPACE_TRENDING_HUGGING_FACE);
    }

    @RequestMapping("/huggingfacespaceslikes")
    public ResultTemplateBean getHuggingFaceTopSearchSpacesLikes() {
        return topSearchGlobalService.getHuggingFaceTopSearch(SearchTypeHuggingFaceEnum.SPACE_LIKES_HUGGING_FACE);
    }

    @RequestMapping("/huggingfacemodelstrending")
    public ResultTemplateBean getHuggingFaceTopSearchModelTrending() {
        return topSearchGlobalService.getHuggingFaceTopSearch(SearchTypeHuggingFaceEnum.MODELS_TRENDING_HUGGING_FACE);
    }

    @RequestMapping("/huggingfacemodellikes")
    public ResultTemplateBean getHuggingFaceTopSearchModelLikes() {
        return topSearchGlobalService.getHuggingFaceTopSearch(SearchTypeHuggingFaceEnum.MODELS_LIKES_HUGGING_FACE);
    }

    @RequestMapping("/huggingfacedatasetstrending")
    public ResultTemplateBean getHuggingFaceTopSearchDatasetTrending() {
        return topSearchGlobalService.getHuggingFaceTopSearch(SearchTypeHuggingFaceEnum.DATASETS_TRENDING_HUGGING_FACE);
    }

    @RequestMapping("/huggingfacedatasetslikes")
    public ResultTemplateBean getHuggingFaceTopSearchDatasetLikes() {
        return topSearchGlobalService.getHuggingFaceTopSearch(SearchTypeHuggingFaceEnum.DATASETS_LIKES_HUGGING_FACE);
    }
}
