package com.tgmeng.model.dto.topsearch;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * description: B站热搜官方返回的VO
 * package: com.tgmeng.model.vo.topsearch
 * className: TopSearchBilibiliDTO
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/6/29 22:41
*/
@Data
@Accessors(chain = true)
public class TopSearchBilibiliDTO {
    private DataView data;

    @Data
    public static  class DataView {
        private List<DataVO> list;
    }

    @Data
    public static  class DataVO {
        /** 热搜词 */
        private String title;
        /** 图片 */
        private String pic;
        /** 热搜词的url */
        private String short_link_v2;
        private Stat stat;
    }

    @Data
    public static  class Stat {
        private String view;
    }
}