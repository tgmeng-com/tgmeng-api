package com.tgmeng.common.util;

import com.jayway.jsonpath.ReadContext;
import com.tgmeng.common.config.RequestInfoManager;
import com.tgmeng.common.enums.business.SearchTypeGuoJiKeJiChuangXinZhongXinnum;
import com.tgmeng.common.enums.enumcommon.EnumUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tgmeng.common.util.CommonJsoupUtil.safeAttr;
import static com.tgmeng.common.util.CommonJsoupUtil.safeText;

@Slf4j
@Service
public class CommonHotPointInfoDealUtil {

    public static String getUrlAfterDealResult(RequestInfoManager.PlatformConfig platform,Element element, ReadContext itemCtx) {
        String platformInterface = platform.getInterfaceUrl();
        String hotTitleUrl = "";
        switch (platformInterface) {
            case "/api/topsearch/mit":
                Object mitUrl = itemCtx.read("$.content");
                String regex = "<a[^>]+href=[\"']([^\"']+)[\"']";
                Matcher matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(mitUrl.toString());
                if (matcher.find()) {
                    hotTitleUrl = matcher.group(1);
                }
                break;
            case "/api/topsearch/douban":
                Object doubanUrl = itemCtx.read("$.uri");
                hotTitleUrl = doubanUrl.toString().replace("douban://douban.com/search/result?q=", "https://douban.com/search?q=");
                break;
            case "/api/topsearch/douyin":
                String sentenceId = itemCtx.read("$.sentence_id");
                String douyinUrl = itemCtx.read("$.word");
                hotTitleUrl = new StringJoiner("")
                        .add("https://www.douyin.com/hot/")
                        .add(sentenceId)
                        .add("/")
                        .add(douyinUrl).toString();
                break;
            case "/api/topsearch/weibo":
                String weiboUrl = itemCtx.read("$.word");
                hotTitleUrl = new StringJoiner("")
                        .add("https://s.weibo.com/weibo?q=%23")
                        .add(weiboUrl)
                        .add("%23").toString();

                break;
            case "/api/topsearch/woshipm":
                String whoispmType = itemCtx.read("$.data.type");
                String whoispmId = itemCtx.read("$.data.id").toString();

                hotTitleUrl = new StringJoiner("")
                        .add("https://www.woshipm.com/")
                        .add(whoispmType)
                        .add("/")
                        .add(whoispmId)
                        .add(".html").toString();
                break;
            case "/api/topsearch/baidu":
                String baiduUrl = itemCtx.read("$.url");
                hotTitleUrl = baiduUrl.replace("https://m.baidu.com/", "https://baidu.com/");

                break;
            case "/api/topsearch/guojikejichuangxinzhongxin/{type}":
                String nsticUrl = safeAttr(element, "a", "href");
                hotTitleUrl = "https://www.ncsti.gov.cn/kjdt/kjrd/" + EnumUtils.getValueByKey(SearchTypeGuoJiKeJiChuangXinZhongXinnum.class,HttpRequestUtil.getRequestPathLastWord()) + nsticUrl.substring(1);
                break;

            case "/api/topsearch/kdsshanghaitoutiao":
                String id = itemCtx.read("$.id");
                hotTitleUrl = "https://www.kdslife.com/news/detail?_cid=689073ce0406f87cd1b07b66&_nid=" + id;
                break;
            default:
                hotTitleUrl = "https://tgmeng.com";
        }
        return hotTitleUrl;
    }

    public static String getHotScoreAfterDealResult(RequestInfoManager.PlatformConfig platform, Element element, ReadContext itemCtx) {
        String platformInterface = platform.getInterfaceUrl();
        String hotScore = "";
        switch (platformInterface) {
            case "/api/topsearch/tencent/{type}":
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
                break;
            case "/api/topsearch/maoyan/{type}":
                hotScore = itemCtx.read("$.label.number").toString() + itemCtx.read("$.label.text").toString();
                break;
            case "/api/topsearch/youku/{type}":
                hotScore = StringUtil.youKuTopSearchItemHotScoreUtil(itemCtx.read("$.searchIndexValue").toString()).toString();
                break;
            default:
                hotScore = "";
        }
        return hotScore;
    }

    public static String getHotTitleAfterDealResult(RequestInfoManager.PlatformConfig platform, Element element, ReadContext itemCtx) {
        String platformInterface = platform.getInterfaceUrl();

        String hotTitle = "";
        switch (platformInterface) {
            case "/api/topsearch/xiaozudouban/{type}":
                hotTitle = safeText(element, ".title > a");
                hotTitle = hotTitle.contains("】") ? hotTitle.substring(hotTitle.indexOf("】") + 1).trim() : hotTitle.trim();
                hotTitle = hotTitle.contains("｜") ? hotTitle.substring(hotTitle.indexOf("｜") + 1).trim() : hotTitle.trim();
                break;
            case "/api/topsearch/0818tuan":
                hotTitle = safeText(element, ":root");
                hotTitle = hotTitle.replaceFirst("^\\d{1,2}-\\d{1,2}\\s+\\d{1,2}:\\d{1,2}\\s+", "");
                break;
            default:
                hotTitle = "";
        }
        return hotTitle;
    }





    public static String getMaoYanJsonFromContent(String content) {
        String appDataJson = "";
        Document parse = Jsoup.parse(content);
        // 2. 获取所有 <script> 标签
        Elements scripts = parse.select("script");
        // 3. 匹配 var AppData = { ... };
        Pattern pattern = Pattern.compile("var\\s+AppData\\s*=\\s*(\\{.*?\\});", Pattern.DOTALL);
        for (var script : scripts) {
            String content1 = script.html();
            Matcher matcher = pattern.matcher(content1);
            if (matcher.find()) {
                appDataJson = matcher.group(1);
            }
        }
        return appDataJson;
    }
}
