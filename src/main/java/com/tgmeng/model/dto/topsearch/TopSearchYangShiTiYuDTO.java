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
public class TopSearchYangShiTiYuDTO {

    private DataInfo data;

    @Data
    public static class DataInfo {

        private List<ItemDTO> list;

    }

    @Data
    public static class ItemDTO {
        private String title;
        private String url;

    }
}
