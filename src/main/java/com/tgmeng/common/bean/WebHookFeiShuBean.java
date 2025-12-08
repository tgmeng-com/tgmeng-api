package com.tgmeng.common.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class WebHookFeiShuBean {
    @JsonProperty("msg_type")
    private String msgType;
    private Content content;
    private Long timestamp;
    private String sign;

    @Data
    public static class Content {
        private Post post;
    }

    @Data
    public static class Post {
        private LangContent zh_cn;  // 中文
    }

    @Data
    public static class LangContent {
        private String title;
        private List<List<TagItem>> content;
    }

    @Data
    public static class TagItem {
        private String tag; // text / a / at
        private String text;
        private String href;    // 只有 a 用
    }

}
