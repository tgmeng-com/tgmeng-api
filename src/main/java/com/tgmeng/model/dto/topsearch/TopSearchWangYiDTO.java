package com.tgmeng.model.dto.topsearch;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * description: 网易新闻热榜
 * package: com.tgmeng.model.dto.topsearch
 * className: TopSearchWangYiDTO
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/7/2 20:15
*/
@Data
@Accessors(chain = true)
public class TopSearchWangYiDTO {

    private ItemDTO data;

    @Data
    public static class ItemDTO {

        private List<DataView> items;
    }
    @Data
    public static  class DataView {
        private String contentId;
        private String title;
        private String hotValue;
    }
}
