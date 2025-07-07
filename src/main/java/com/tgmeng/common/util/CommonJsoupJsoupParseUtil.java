package com.tgmeng.common.util;

import com.tgmeng.model.vo.topsearch.TopSearchCommonVO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


public class CommonJsoupJsoupParseUtil {
    public static List<TopSearchCommonVO.DataInfo> zhihu(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select(".HotList-list > section");
        for (Element element : elements) {
            String url = safeText(element,".HotItem-content > a href") ;
            String hotScore = safeText(element, ".HotItem-metrics");
            String title = safeText(element, ".HotItem-content > a");
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
}
