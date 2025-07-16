package com.tgmeng.common.util;

import com.tgmeng.model.vo.topsearch.TopSearchCommonVO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


public class CommonJsoupJsoupParseUtil {
    public static List<TopSearchCommonVO.DataInfo> tengXunShiPin(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select(".table_list > li");
        for (Element element : elements.subList(1, elements.size())) {
            String url = safeAttr(element," div:nth-of-type(1) > a","href") ;
            String hotScore = "";
            Element spanElement = element.selectFirst("div:nth-of-type(2) span");
            if (spanElement != null) {
                String style = spanElement.attr("style");
                if (style != null && style.contains("width")) {
                    String[] stylePairs = style.split(";");
                    for (String pair : stylePairs) {
                        if (pair.trim().startsWith("width")) {
                            String widthValue = pair.split(":", 2)[1].trim().replace("%", ""); // 例如 "36.41441428782205"
                            try {
                                // 转换为 double
                                double widthDouble = Double.parseDouble(widthValue);
                                // 格式化保留 6 位（包括小数点）
                                String formattedWidth = String.format("%.4f", widthDouble); // 例如 "36.4144"
                                // 乘以 10000，转换为 long
                                Long hotScoreTemp = Math.round(Double.parseDouble(formattedWidth) * 10000); // 例如 364144
                                hotScore = hotScoreTemp.toString(); // 保留字符串形式用于 width 参数
                            } catch (NumberFormatException e) {
                                System.err.println("解析 width 失败: " + widthValue + ", 错误: " + e.getMessage());
                            }
                            break;
                        }
                    }
                }
            }
            String title = safeText(element, "div:nth-of-type(1) > a ");
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
