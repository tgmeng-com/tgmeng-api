package com.tgmeng.model.dto.topsearch;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * description: GitHub热榜DTO
 * package: com.tgmeng.model.dto.topsearch
 * className: TopSearchGitHubDTO
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/6/30 2:37
 */
@Data
@Accessors(chain = true)
public class TopSearchYoutubeDTO {

    private List<ItemDTO> items;

    @Data
    public static class ItemDTO {

        private String id;
        private SnippetView snippet;
        private StatisticsView statistics;
    }
    @Data
    public static  class SnippetView {
        private String title;
        private String description;
    }
    @Data
    public static  class StatisticsView {
        private String viewCount;
        private String likeCount;
        private String favoriteCount;
        private String commentCount;
    }
}
