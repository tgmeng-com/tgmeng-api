package com.tgmeng.common.util;

import cn.hutool.core.util.StrUtil;
import com.tgmeng.common.config.RequestInfoManager;
import com.tgmeng.model.vo.topsearch.TopSearchCommonVO;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CommonJsoupUtil {

    public static List<TopSearchCommonVO.DataInfo> getCommonResult(String content, RequestInfoManager.PlatformConfig platform) {
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        if (StrUtil.equals(platform.getPlatformCategory(), "HuggingFaces")) {
            topSearchCommonVOS = HuggingFaceJsoupParseUtil.parseContent(content);
            return topSearchCommonVOS;
        } else {
            Document parse = Jsoup.parse(content);
            for (RequestInfoManager.Selector selector : platform.getSelectors()) {
                Elements rootElements = parse.select(selector.getRoot());
                for (Element element : rootElements) {
                    // 热点url
                    String url = "";
                    if (platform.getHotTitleUrlNeedDeal()){
                        url = CommonHotPointInfoDealUtil.getUrlAfterDealResult(platform, element, null);
                    }else{
                        url = platform.getHotTitleUrlPrefix() + safeAttr(element, selector.getUrl(), "href") + platform.getHotTitleUrlAfter();
                    }
                    // 热点标题
                    String title = "";
                    if (platform.getHotTitleNeedDeal()){
                        title = CommonHotPointInfoDealUtil.getHotTitleAfterDealResult(platform, element, null);
                    }else {
                        title = safeText(element, selector.getKeyword());
                    }
                    String publishTime = safeText(element, selector.getPublishTime());
                    String commentCount = StrUtil.isNotBlank(safeText(element, selector.getCommentCount())) ? safeText(element, selector.getCommentCount()) : "0";
                    String hotScore = "";
                    if (platform.getHotScoreNeedDeal()) {
                        hotScore = CommonHotPointInfoDealUtil.getHotScoreAfterDealResult(platform, element, null);
                    } else {
                        hotScore = safeText(element, selector.getHotScore()).toString();
                    }

                    if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                        topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, hotScore, url, "", "", "", "", publishTime, commentCount, null, null, ""));
                    }
                }
            }
            return topSearchCommonVOS;
        }
    }

    public static String safeText(Element parent, String selector) {
        if (StrUtil.isNotBlank(selector)) {
            Element el = parent.selectFirst(selector);
            return el != null ? el.text() : "";
        }
        return "";

    }

    public static String safeAttr(Element parent, String selector, String attr) {
        if (StrUtil.isNotBlank(selector)) {
            Element el = parent.selectFirst(selector);
            return el != null ? el.attr(attr) : "";
        }
        return "";
    }
}
