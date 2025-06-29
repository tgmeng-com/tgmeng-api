package com.tgmeng.model.dto.topsearch;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * description: 百度热搜官方返回的VO
 * package: com.tgmeng.model.vo.topsearch.china
 * className: TopSearchBaiDuResVO
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/6/29 22:44
*/
@Data
@Accessors(chain = true)
public class TopSearchBaiDuDTO {
    private boolean success;
    private DataVO data;

    @Data
    public static  class DataVO {
        private List<CardsVO> cards;
        private String curBoardName;
        private String logid;
    }
    @Data
    public static  class CardsVO {
        private String text;
        private String updateTime;
        private List<ContentVO> content;
        private List<ContentVO> topContent;
    }
    @Data
    public static  class ContentVO {
        private String hotScore;
        private String img;
        private String word;
        private String url;
    }
}

