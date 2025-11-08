package com.tgmeng.model.dto.topsearch;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class TopSearchWenZhangJueJinDTO {

    private List<DataInfo> data;

    @Data
    public static class DataInfo {

        private Content content;

        @JsonProperty("content_counter")
        private ContentCounter contentCounter;
    }

    @Data
    public static class Content {

        private String title;

        @JsonProperty("content_id")
        private String contentId;
    }

    @Data
    public static class ContentCounter {

        @JsonProperty("hot_rank")
        private Long hotRank;

    }

}
