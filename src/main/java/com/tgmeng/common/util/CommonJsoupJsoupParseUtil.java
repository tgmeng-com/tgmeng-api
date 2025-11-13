package com.tgmeng.common.util;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgmeng.common.enums.business.SearchTypeGuoJiKeJiChuangXinZhongXinnum;
import com.tgmeng.common.enums.business.SearchTypeShenMeZhiDeMaiEnum;
import com.tgmeng.common.enums.business.SearchTypeXiaoZuDouBanEnum;
import com.tgmeng.common.enums.exception.ServerExceptionEnum;
import com.tgmeng.common.exception.ServerException;
import com.tgmeng.model.dto.topsearch.TopSearchMaoYanDTO;
import com.tgmeng.model.vo.topsearch.TopSearchCommonVO;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class CommonJsoupJsoupParseUtil {
    public static List<TopSearchCommonVO.DataInfo> tengXunShiPin(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select(".table_list > li");
        for (Element element : elements.subList(1, elements.size())) {
            String url = safeAttr(element, " div:nth-of-type(1) > a", "href");
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
            topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, StringUtil.stringParseToLong(hotScore), url, "", "", "", "", "", ""));
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> maoYan(String content) {
        try {
            Document parse = Jsoup.parse(content);
            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
            // 2. 获取所有 <script> 标签
            Elements scripts = parse.select("script");
            // 3. 匹配 var AppData = { ... };
            Pattern pattern = Pattern.compile("var\\s+AppData\\s*=\\s*(\\{.*?\\});", Pattern.DOTALL);
            for (var script : scripts) {
                String content1 = script.html();
                Matcher matcher = pattern.matcher(content1);
                if (matcher.find()) {
                    String appDataJson = matcher.group(1);
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    TopSearchMaoYanDTO topSearchMaoYanDTO = mapper.readValue(appDataJson, TopSearchMaoYanDTO.class);
                    for (TopSearchMaoYanDTO.Movies movie : topSearchMaoYanDTO.getData().getData().getMovies()) {
                        topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(movie.getNm(),
                                movie.getLabel().getNumber() + movie.getLabel().getText(),
                                "https://www.maoyan.com/films/" + movie.getId(),
                                movie.getBackGroundImg(),
                                movie.getStar(),
                                movie.getShortDec(),
                                movie.getCat(),
                                movie.getPubDesc(),
                                ""));
                    }
                    break;
                }
            }
            return topSearchCommonVOS;
        } catch (Exception e) {
            log.error("👺👺👺获取猫眼榜单失败👺👺👺", e);
            throw new ServerException(ServerExceptionEnum.MAO_YAN_SEARCH_EXCEPTION);
        }
    }

    public static List<TopSearchCommonVO.DataInfo> diYiCaiJing(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select("#headlist > a");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, "a", "href");
            String title = safeText(element, "h2");
            topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", "https://www.yicai.com" + url, "", "", "", "", "", ""));
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> tongHuaShun(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = Objects.requireNonNull(parse.select(".list-con").first()).select("ul").select("li");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, "a", "href");
            String title = safeText(element, "a");
            topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", url, "", "", "", "", "", ""));
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> caiLianShe(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select(".home-article-ranking-box > div");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, "a", "href");
            String title = safeText(element, "a");
            topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", "https://www.cls.cn" + url, "", "", "", "", "", ""));
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getLongHui(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = Objects.requireNonNull(Objects.requireNonNull(parse.select("#hot-article").first()).select("ul").first()).select("li");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, "a", "href");
            String title = safeText(element, "a");
            topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", "https://www.gelonghui.com" + url, "", "", "", "", "", ""));
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getFaBu(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select(".news-top a");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, "a", "href");
            String title = safeText(element, "h4");
            topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", "https://www.fastbull.com" + url, "", "", "", "", "", ""));
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getJinShi(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select(".jin10-news-index-list > .jin10-news-list > div");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, ".jin10-news-list-item-info > a", "href");
            String title = safeText(element, "p");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", url, "", "", "", "", "", ""));
            }
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getNewYueShiBao(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select(".article-list > li");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, "a", "href");
            String title = safeText(element, "h2");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", url, "", "", "", "", "", ""));
            }
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getBBC(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select(".bbc-wb0d4t > li");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, "a", "href");
            String title = safeText(element, "a");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", url, "", "", "", "", "", ""));
            }
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getFaGuang(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select(".o-banana-split .article__title");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, "a", "href");
            String title = safeText(element, "a");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", "https://www.rfi.fr" + url, "", "", "", "", "", ""));
            }
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getHuaErJieRiBao(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select(".css-1rznr30-CardLink");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, "", "href");
            String title = safeText(element, "");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", "https://www.rfi.fr" + url, "", "", "", "", "", ""));
            }
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getDaJiYuan(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select(".left_col > div");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, ".text > .title > a", "href");
            String title = safeText(element, ".text > .title > a");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", url, "", "", "", "", "", ""));
            }
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getYouSheWang(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select(".p-items > .p-item");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, "h2 > a", "href");
            String title = safeText(element, "h2 > a");
            String hotScore = safeText(element, ".meta-views");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, hotScore, url, "", "", "", "", "", ""));
            }
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getZhanKu(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select(".eTkjTE > section");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, ".rankCard", "href");
            String title = safeText(element, ".cnxaX > a");
            String hotScore = safeText(element, ".rankBox > div:nth-of-type(4) .kFzKPV");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, hotScore, url, "", "", "", "", "", ""));
            }
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getTuYaWangGuo(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select(".px-2 > div");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, ".cursor-hand", "href");
            String title = safeText(element, ".show-works-title");
            String hotScore = safeText(element, ".show-works-views > span");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, hotScore, "https://www.gracg.com" + url, "", "", "", "", "", ""));
            }
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getSheJiDaRen(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select(".indexbody .post");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, "h2 > a", "href");
            String title = safeText(element, "h2 > a");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", url, "", "", "", "", "", ""));
            }
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getTopys(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select(".article-box-item");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, ".title", "href");
            String title = safeText(element, ".title");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", "https://www.topys.cn" + url, "", "", "", "", "", ""));
            }
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getArchDaily(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select(".afd-post-stream");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, "h3 a", "href");
            String title = safeText(element, "h3 span");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", "https://www.archdaily.cn" + url, "", "", "", "", "", ""));
            }
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getDribbble(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select("#main > ol > li");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, ".shot-thumbnail-link", "href");
            String title = safeText(element, ".display-name");
            String hotScore = safeText(element, ".js-shot-views-count");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, hotScore, "https://dribbble.com" + url, "", "", "", "", "", ""));
            }
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getAwwwards(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select(".grid-cards > li");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, ".figure-rollover__link", "href");
            String title = safeText(element, ".avatar-name__title");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", "https://www.awwwards.com" + url, "", "", "", "", "", ""));
            }
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getCore77(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select(".post_list_prime li");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, "h1 > a", "href");
            String title = safeText(element, "h1 > a");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", url, "", "", "", "", "", ""));
            }
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getAbduzeedo(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select(".posts > article");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, "span > h2 > a", "href");
            String title = safeText(element, "span > h2 > a");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", "https://abduzeedo.com" + url, "", "", "", "", "", ""));
            }
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getZhongGuoKeXueYuan(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select("#content > li");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, "a", "href");
            String title = safeText(element, "a");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", "https://www.cas.cn/syky" + url, "", "", "", "", "", ""));
            }
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getEurekAlert(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select("#main-content > .post");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, "a", "href");
            String title = safeText(element, "h2");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", "https://www.eurekalert.org" + url, "", "", "", "", "", ""));
            }
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getGuoJiKeJiChuangXinZhongXin(String content, SearchTypeGuoJiKeJiChuangXinZhongXinnum searchTypeGuoJiKeJiChuangXinZhongXinnum) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select(".news_box > ul:eq(2) > li");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, "a", "href");
            String title = safeText(element, "h2");
            title = title.replaceAll("^\\d{4}-\\d{2}-\\d{2}\\s*", "");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", "https://www.ncsti.gov.cn/kjdt/kjrd/" + searchTypeGuoJiKeJiChuangXinZhongXinnum.getValue() + url.substring(1), "", "", "", "", "", ""));
            }
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getHuPu(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select(".test-img-list-model > div");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, ".list-item-title", "href");
            String title = safeText(element, ".list-item-title");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", url, "", "", "", "", "", ""));
            }
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getDongQiuDi(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select(".top-center > p");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, "a", "href");
            String title = safeText(element, "a");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", "https://www.dongqiudi.com" + url, "", "", "", "", "", ""));
            }
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getXinLangTiYu(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select("#hot-search-list > div");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, "a", "href");
            String title = safeText(element, "a");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", url, "", "", "", "", "", ""));
            }
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getSouHuTiYu(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select(".feed-group > a");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, "a", "href");
            String title = safeText(element, "a");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", url.startsWith("//") ? "https:" + url : url, "", "", "", "", "", ""));
            }
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getWangYiTiYu(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select(".channel_news_body li");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, "a", "href");
            String title = safeText(element, "a");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", url.startsWith("//") ? "https:" + url : url, "", "", "", "", "", ""));
            }
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getYangShiTiYu(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select("#plantingtext li");
        for (Element element : elements.subList(0, elements.size() - 1)) {
            String url = safeAttr(element, "a", "href");
            String title = safeText(element, "a");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", url, "", "", "", "", "", ""));
            }
        }
        Elements elements1 = parse.select("#SUBD1563933227337761 a");
        for (Element element : elements1.subList(0, elements1.size())) {
            String url = safeAttr(element, "a", "href");
            String title = safeText(element, "a");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url) && title.length() > 4) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", url, "", "", "", "", "", ""));
            }
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getPPTiYu(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select(".info-panel-wrap > div");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, ".tw-link", "href");
            String title = safeText(element, ".tw-link");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", "https://www.ppsport.com" + url, "", "", "", "", "", ""));
            }
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getZhiBoBa(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select(".zuqiu-news .list-item");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, "a", "href");
            String title = safeText(element, "a");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", url, "", "", "", "", "", ""));
            }
        }

        Elements elements1 = parse.select(".lanqiu-news .list-item");
        for (Element element : elements1.subList(0, elements1.size())) {
            String url = safeAttr(element, "a", "href");
            String title = safeText(element, "a");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url) && title.length() > 4) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", url, "", "", "", "", "", ""));
            }
        }

        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getV2EX(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select(".cell.item");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, ".topic-link", "href");
            String title = safeText(element, ".topic-link");
            String hotScore = safeText(element, ".count_livid");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, hotScore, "https://www.v2ex.com" + url, "", "", "", "", "", ""));
            }
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getBuXingJieHuPu(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select(".text-list-model > div");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, "a", "href");
            String title = safeText(element, ".t-info");
            String hotScore = safeText(element, ".t-lights") + "·" + safeText(element, ".t-replies");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, hotScore, "https://bbs.hupu.com" + url, "", "", "", "", "", ""));
            }
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getNGA(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select("#topic_ladder_cat_3 > li");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, "a", "href");
            String title = safeText(element, "a");
            //String hotScore = safeText(element, ".t-lights")+"·"+safeText(element, ".t-replies");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", "https:" + url, "", "", "", "", "", ""));
            }
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getYiMuSanFenDi(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select("#portal_block_439_content li");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, "a:first-of-type", "href");
            String title = safeText(element, "a:first-of-type");
            //String hotScore = safeText(element, ".t-lights")+"·"+safeText(element, ".t-replies");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", "https://www.1point3acres.com/bbs/" + url, "", "", "", "", "", ""));
            }
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getHackerNews(String content) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select(".athing.submission");
        for (Element element : elements.subList(0, elements.size())) {
            String url = safeAttr(element, ".titleline > a:first-of-type", "href");
            String title = safeText(element, ".titleline > a:first-of-type");
            if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", url, "", "", "", "", "", ""));
            }
        }

        Elements elementsScore = parse.select(".score");
        for (int i = 0; i < elementsScore.size() && i < topSearchCommonVOS.size(); i++) {
            Element element = elementsScore.get(i);
            String hotScore = safeText(element, ".score");
            if (StrUtil.isNotBlank(hotScore)) {
                topSearchCommonVOS.get(i).setHotScore(hotScore);
            }
        }

        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getXiaoZuDouBan(String content, SearchTypeXiaoZuDouBanEnum searchTypeXiaoZuDouBanEnum) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select(".olt tr");
        for (Element element : elements) {
            if (element.select("span[title=置顶]").isEmpty()) {
                String url = safeAttr(element, ".title > a", "href");
                String title = safeText(element, ".title > a");
                title = title.contains("】") ? title.substring(title.indexOf("】") + 1).trim() : title.trim();
                title = title.contains("｜") ? title.substring(title.indexOf("】") + 1).trim() : title.trim();
                String publishTime = safeText(element, ".time");
                String commentCount = StrUtil.isNotBlank(safeText(element, ".r-count")) ? safeText(element, ".r-count") : "0";
                if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                    topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", url, "", "", "", "", publishTime, commentCount));
                }
            }
        }
        return topSearchCommonVOS;
    }

    public static List<TopSearchCommonVO.DataInfo> getShenmeZhiDeMai(String content, SearchTypeShenMeZhiDeMaiEnum searchTypeShenMeZhiDeMaiEnum) {
        Document parse = Jsoup.parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        Elements elements = parse.select(".olt tr");
        for (Element element : elements) {
            if (element.select("span[title=置顶]").isEmpty()) {
                String url = safeAttr(element, ".title > a", "href");
                String title = safeText(element, ".title > a");
                title = title.contains("】") ? title.substring(title.indexOf("】") + 1).trim() : title.trim();
                String publishTime = safeText(element, ".time");
                String commentCount = StrUtil.isNotBlank(safeText(element, ".r-count")) ? safeText(element, ".r-count") : "0";
                if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                    topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", url, "", "", "", "", publishTime, commentCount));
                }
            }
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
