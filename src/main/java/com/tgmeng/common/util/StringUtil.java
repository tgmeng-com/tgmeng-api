package com.tgmeng.common.util;

import java.util.StringJoiner;

public class StringUtil {
    /**
     * description: 拼接微博热搜每条具体的url
     * method: WeiBoTopSearchItemUrl
     *
     * @author tgmeng
     * @since 2025/6/29 20:58
    */
    public static String weiBoTopSearchItemUrlUtil(String wordScheme,double realpos){
        return new StringJoiner("")
                .add("https://s.weibo.com/weibo?q=")
                .add(wordScheme)
                .add("&t=31&band_rank=")
                .add(String.valueOf(realpos)).toString();
    }

    public static String douYinTopSearchItemUrlUtil(String sentenceId,String word){
        return new StringJoiner("")
                .add("https://www.douyin.com/hot/")
                .add(sentenceId)
                .add("/")
                .add(word).toString();
    }
}