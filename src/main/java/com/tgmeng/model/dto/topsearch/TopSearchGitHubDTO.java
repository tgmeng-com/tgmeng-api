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
public class TopSearchGitHubDTO {
    @JsonProperty("total_count")
    private Long totalCount;

    private List<ItemDTO> items;

    @Data
    public static class ItemDTO {

        private String name;

        @JsonProperty("html_url")
        private String htmlUrl;

        private String description;

        @JsonProperty("stargazers_count")
        private Long stargazersCount;


    }
}
