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
public class TopSearchYouKuDTO {

    private DataInfo data;

    @Data
    public static class DataInfo {

        private List<Nodes> nodes;

    }

    @Data
    public static class Nodes {
        private List<SubNodes>  nodes;

    }

    @Data
    public static class SubNodes {
        private DataInfos data;
    }

    @Data
    public static class DataInfos {
        private TabDataMap tabDataMap;
    }


    @Data
    public static class TabDataMap {
        @JsonProperty("综艺")
        private RealCategroy zongYi;
        @JsonProperty("电影")
        private RealCategroy dianYing;
        @JsonProperty("文化")
        private RealCategroy wenHua;
        @JsonProperty("热门搜索")
        private RealCategroy reMenSouSuo;
        @JsonProperty("少儿")
        private RealCategroy shaoEr;
        @JsonProperty("动漫")
        private RealCategroy dongMan;
        @JsonProperty("纪录片")
        private RealCategroy jiLuPian;
        @JsonProperty("电视剧")
        private RealCategroy dianShiJu;
        @JsonProperty("微短剧")
        private RealCategroy WeiDuanJu;
    }

    @Data
    public static class RealCategroy {
        private List<DataNodes> nodes;
    }

    @Data
    public static class DataNodes {
        private List<FinalDataArray> nodes;
    }

    @Data
    public static class FinalDataArray {
        private FinalData data;
    }

    @Data
    public static class FinalData {
        private String title;
        private String searchIndexValue;
        private String encodeShowId;
    }
}
