package com.tgmeng.common.util;

import cn.hutool.core.util.StrUtil;
import com.tgmeng.model.dto.topsearch.TopSearchShaoShuPaiDTO;
import com.tgmeng.model.dto.topsearch.TopSearchZhiHuDTO;

import java.net.URI;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    /**
     * description: 拼接微博热搜每条具体的url
     * method: WeiBoTopSearchItemUrl
     *
     * @author tgmeng
     * @since 2025/6/29 20:58
     */
    public static String weiBoTopSearchItemUrlUtil(String note, Long realpos) {
        return new StringJoiner("")
                .add("https://s.weibo.com/weibo?q=%23")
                .add(note)
                .add("%23")
                //.add("&t=31&band_rank=")
                //.add(String.valueOf(realpos))
                //
                .toString();
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

    public static Long zhiHuTopSearchItemHotScoreUtil(TopSearchZhiHuDTO.DataInfo topSearchZhiHuDTO) {
        return zhiHuHotScoreConvertToNumber(topSearchZhiHuDTO.getDetail_text());
    }

    public static Long youKuTopSearchItemHotScoreUtil(String hotScore) {
        return youKuHotScoreConvertToNumber(hotScore);
    }

    public static long zhiHuHotScoreConvertToNumber(String str) {
        if (str == null || str.isEmpty()) {
            return 0;
        }

        // 提取数字部分（包括小数点）
        Pattern pattern = Pattern.compile("(\\d+(\\.\\d+)?)");
        Matcher matcher = pattern.matcher(str);

        double number = 0;
        if (matcher.find()) {
            number = Double.parseDouble(matcher.group(1));
        }

        // 计算单位倍数
        long multiplier = 1;
        if (str.contains("万")) {
            multiplier = 10000;
        } else if (str.contains("亿")) {
            multiplier = 100000000;
        }

        return (long) (number * multiplier);
    }

    public static Long youKuHotScoreConvertToNumber(String str) {
        if (str == null || str.isEmpty()) {
            return 0L;
        }

        // 提取数字部分（包括小数点）
        Pattern pattern = Pattern.compile("(\\d+(\\.\\d+)?)");
        Matcher matcher = pattern.matcher(str);

        double number = 0;
        if (matcher.find()) {
            number = Double.parseDouble(matcher.group(1));
        }

        // 计算单位倍数
        long multiplier = 1;
        if (str.toLowerCase().contains("w")) {
            multiplier = 10000;
        } else if (str.toLowerCase().contains("y")) {
            multiplier = 100000000;
        }

        return (long) (number * multiplier);
    }


    public static Long stringParseToLong(String str) {
        if(StrUtil.isBlank(str)){
            return 0L;
        }
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

    public static String getUri(String url) {
        try {
            URI uri = new URI(url);
            return uri.getPath();  // 永远只返回路径
        } catch (Exception e) {
            return url;  // URL 非法时原样返回
        }
    }
}