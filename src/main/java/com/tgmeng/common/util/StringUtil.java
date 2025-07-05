package com.tgmeng.common.util;

import com.tgmeng.model.dto.topsearch.TopSearchShaoShuPaiDTO;

import java.util.StringJoiner;

public class StringUtil {
    /**
     * description: 拼接微博热搜每条具体的url
     * method: WeiBoTopSearchItemUrl
     *
     * @author tgmeng
     * @since 2025/6/29 20:58
     */
    public static String weiBoTopSearchItemUrlUtil(String wordScheme, Long realpos) {
        return new StringJoiner("")
                .add("https://s.weibo.com/weibo?q=")
                .add(wordScheme)
                .add("&t=31&band_rank=")
                .add(String.valueOf(realpos)).toString();
    }

    public static String douYinTopSearchItemUrlUtil(String sentenceId, String word) {
        return new StringJoiner("")
                .add("https://www.douyin.com/hot/")
                .add(sentenceId)
                .add("/")
                .add(word).toString();
    }

    public static String youtubeTopSearchItemUrlUtil(String sentenceId) {
        return new StringJoiner("")
                .add("https://www.youtube.com/watch?v=")
                .add(sentenceId)
                .toString();
    }

    public static String wangYiTopSearchItemUrlUtil(String contentId) {
        return new StringJoiner("")
                .add("https://c.m.163.com/news/a/")
                .add(contentId)
                .add(".html")
                .toString();
    }

    public static String wangYiYunTopSearchItemUrlUtil(Long id) {
        return new StringJoiner("")
                .add("https://music.163.com/#/song?id=")
                .add(id.toString())
                .toString();
    }

    public static String shaoShuPaiTopSearchItemUrlUtil(Long id) {
        return new StringJoiner("")
                .add("https://sspai.com/post/")
                .add(id.toString())
                .toString();
    }

    public static Long shaoShuPaiTopSearchItemHotScoreUtil(TopSearchShaoShuPaiDTO.ItemDTO topSearchShaoShuPaiItemDTO) {
        return topSearchShaoShuPaiItemDTO.getLikeCount() + topSearchShaoShuPaiItemDTO.getCommentCount();
    }

    public static Long stringParseToLong(String str) {
        str = str.trim().toLowerCase();
        if (str.endsWith("k")) {
            return (long)(Double.parseDouble(str.replace("k", "")) * 1_000);
        } else if (str.endsWith("m")) {
            return (long)(Double.parseDouble(str.replace("m", "")) * 1_000_000);
        } else if (str.endsWith("b")) {
            return (long)(Double.parseDouble(str.replace("b", "")) * 1_000_000_000);
        } else {
            return Long.parseLong(str.replaceAll("[^\\d]", ""));
        }
    }
}