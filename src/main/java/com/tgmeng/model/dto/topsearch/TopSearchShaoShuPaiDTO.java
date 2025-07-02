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
public class TopSearchShaoShuPaiDTO {

    private List<ItemDTO> data;

    @Data
    public static class ItemDTO {

        private String title;
        private Long id;
        @JsonProperty("comment_count")
        private Long commentCount;
        @JsonProperty("like_count")
        private Long likeCount;
    }
}
