package com.tgmeng.model.dto.topsearch;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * description: 微博热搜官方返回的VO
 * package: com.tgmeng.model.vo.topsearch.china
 * className: TopSearchWeiBoResVO
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/6/29 22:44
*/
@Data
@Accessors(chain = true)
public class TopSearchWeiBoDTO {
    private DataView data;

    @Data
    public static  class DataView {
        private Hotgov hotgov;
        private List<DataVO> band_list;
    }

    @Data
    public static  class Hotgov {
        /** 热搜词 */
        private String word;
        /** 热搜词的url */
        private String url;
    }

    @Data
    public static  class DataVO {
        /** 热搜词 */
        private String note;
        /** 热搜词的url 需要自己拼*/
        /** 格式: https://s.weibo.com/weibo?q=  word_scheme   &t=31&band_rank= realpos    &Refer=top*/
        private String url;
        /** 热搜词的热度 */
        private Long num;
        /** 排序的序号 用来拼接url的 */
        private Long realpos;
        /** 热搜词 用来拼接url的 */
        @JSONField(name = "word_scheme")
        private String wordScheme;
    }
}