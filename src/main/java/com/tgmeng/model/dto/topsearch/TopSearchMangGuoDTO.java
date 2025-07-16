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
public class TopSearchMangGuoDTO {

    private DataInfo data;

    @Data
    public static class DataInfo {

        private List<TopList> topList;

    }

    @Data
    public static class TopList {
        private List<FinalData>  data;
        private String  label;
    }

    @Data
    public static class FinalData {
        private String  name;

    }

}
