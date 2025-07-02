package com.tgmeng.model.dto.topsearch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * description: 腾讯新闻热点
 * package: com.tgmeng.model.dto.topsearch
 * className: TopSearchGitHubDTO
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/6/30 2:37
 */
@Data
@Accessors(chain = true)
public class TopSearchTouTiaoDTO {

    private List<ItemInfo> data;
    @JsonProperty("fixed_top_data")
    private List<ItemInfo> fixedTopData;

    @Data
    public static  class ItemInfo {
        @JsonProperty("HotValue")
        private String hotValue;
        @JsonProperty("Url")
        private String url;
        @JsonProperty("Title")
        private String title;
    }
}
