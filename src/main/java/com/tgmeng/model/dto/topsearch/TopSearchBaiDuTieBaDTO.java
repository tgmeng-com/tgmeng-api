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
public class TopSearchBaiDuTieBaDTO {

    private ItemDTO data;

    @Data
    public static class ItemDTO {

        @JsonProperty("bang_topic")
        private ItemView bangTopic;
    }
    @Data
    public static  class ItemView {
        @JsonProperty("topic_list")
        private List<DataInfo> topicList;
    }
    @Data
    public static  class DataInfo {
        @JsonProperty("topic_name")
        private String topicName;
        @JsonProperty("discuss_num")
        private String discussNum;
        @JsonProperty("topic_url")
        private String topicUrl;
    }
}
