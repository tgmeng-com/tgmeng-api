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
public class TopSearchMaoYanDTO {

    private DataInfo data;

    @Data
    public static class DataInfo {
        private DataInfoOne data;
    }

    @Data
    public static class DataInfoOne {
        private List<Movies> movies;
    }

    @Data
    public static class Movies {
        private String nm;
        private String pubDesc;
        private String shortDec;
        private String star;
        private String cat;
        private Label label;
        private String id;
        private String sc;
        private String backGroundImg;

    }

    @Data
    public static class Label {
        private String number;
        private String text;
    }
}
