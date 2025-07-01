package com.tgmeng.model.dto.topsearch;

import com.alibaba.fastjson2.annotation.JSONField;
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
    @JSONField(name = "total_count")
    private Long totalCount;

    private List<ItemDTO> items;

    @Data
    public static class ItemDTO {

        private String name;

        @JSONField(name = "html_url")
        private String htmlUrl;

        private String description;

        @JSONField(name = "stargazers_count")
        private Long stargazersCount;


    }
}
