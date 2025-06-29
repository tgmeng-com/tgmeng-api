package com.tgmeng.model.vo.topsearch.china;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class TopSearchBaiDuResVO {
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
    }
    @Data
    public static  class ContentVO {
        private String hotScore;
        private String hotTagImg;
        private String word;
        private String url;
    }
}

