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
public class TopSearchWoShiPMDTO {

    @JsonProperty("RESULT")
    private List<DataInfo> result;


    @Data
    public static class DataInfo {
        private Items data;
    }

    @Data
    public static class Items {
        private String articleTitle;
        private String type;
        private Long id;
    }
}
