package com.tgmeng.model.dto.topsearch;

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
public class TopSearchTencentDTO {

    private List<ItemView> idlist;

    @Data
    public static class ItemView {

        private List<ItemInfo> newslist;
    }
    @Data
    public static  class ItemInfo {
        private String title;
        private String url;
        private String readCount;
    }
}
