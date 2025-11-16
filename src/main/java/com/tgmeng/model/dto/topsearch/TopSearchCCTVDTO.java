package com.tgmeng.model.dto.topsearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopSearchCCTVDTO {

    private String channelName;
    private String isLive;
    private String lvUrl;
    private Long liveSt;

    private List<DataInfo> list;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataInfo {

        private String columnBackvideourl;
        @JsonProperty("column_url")
        private String columnUrl;
        private Long endTime;
        private Long startTime;
        private String showTime;
        private String title;

    }
}
