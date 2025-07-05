package com.tgmeng.common.util;

import com.tgmeng.model.vo.topsearch.TopSearchCommonVO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HuggingFaceJsoupParseUtil {
    public static List<TopSearchCommonVO.DataInfo> space(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select(".pb-12 > .grid > .relative");
        for (Element element : elements) {
            String url = "https://huggingface.co" + safeAttr(element, "a", "href");
            String hotScore = safeText(element, "header > div:nth-of-type(2) span");
            String title = safeText(element, "main h4") + " " + safeText(element, "main > div > div:nth-of-type(1)");
            topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, StringUtil.stringParseToLong(hotScore), url, ""));
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> models(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select("main section:nth-of-type(2) > .relative .grid .overview-card-wrapper");
        for (Element element : elements) {
            String url = "https://huggingface.co" + safeAttr(element, "a", "href");
            String hotScore = extractLastLikeCount(Objects.requireNonNull(element.selectFirst("div:nth-of-type(1) > div:nth-of-type(1)")));
            String title = safeText(element, "h4:nth-of-type(1)");
            topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, StringUtil.stringParseToLong(hotScore), url, ""));
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> datasets(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select("main section:nth-of-type(2) > .relative .grid .overview-card-wrapper");
        for (Element element : elements) {
            String url = "https://huggingface.co" + safeAttr(element, "a", "href");
            String hotScore = extractLastLikeCount(Objects.requireNonNull(element.selectFirst("div:nth-of-type(1) > div:nth-of-type(1)")));
            String title = safeText(element, "h4:nth-of-type(1)");
            topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, StringUtil.stringParseToLong(hotScore), url, ""));
        }
        return topSearchCommonVOS;
    }










    public static String safeText(Element parent, String selector) {
        Element el = parent.selectFirst(selector);
        return el != null ? el.text() : "";
    }

    public static String safeAttr(Element parent, String selector, String attr) {
        Element el = parent.selectFirst(selector);
        return el != null ? el.attr(attr) : "";
    }

    /**
     * 从容器中提取最后一个包含数字的字符串，这里还针对models里面这个叼毛热度值专用的
     *
     * @param container 容器元素
     * @return 最后一个包含数字的字符串
     */
    public static String extractLastLikeCount(Element container) {
        List<TextNode> textNodes = container.textNodes();
        for (int i = textNodes.size() - 1; i >= 0; i--) {
            String text = textNodes.get(i).text().trim();
            if (text.matches("\\d+(\\.\\d+)?[kKmMbB]?")) {
                return text;
            }
        }
        return "";
    }

}
