package com.tgmeng.model.vo.topsearch.china;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class TopSearchBilibiliResVO {
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