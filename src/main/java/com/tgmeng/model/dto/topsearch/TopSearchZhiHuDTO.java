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
public class TopSearchZhiHuDTO {

    private List<DataInfo> data;

    @Data
    public static class DataInfo {

        private ItemDTO target;

        private String detail_text;
    }

    @Data
    public static class ItemDTO {
        private String id;
        private String title;

    }
}
