package com.tgmeng.common.util;

import cn.hutool.core.util.StrUtil;
import com.tgmeng.common.config.RequestInfoManager;
import com.tgmeng.common.enums.business.PlatFormCategoryEnum;
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
        if (StrUtil.equals(platform.getPlatformCategory(), PlatFormCategoryEnum.HUGGING_FACES.getValue())) {
            topSearchCommonVOS = HuggingFaceJsoupParseUtil.parseContent(content);
            return topSearchCommonVOS;
        } else {
            Document parse = Jsoup.parse(content);
            for (RequestInfoManager.Selector selector : platform.getSelectors()) {
                Elements rootElements = parse.select(selector.getRoot());
                for (int i = 0; i < rootElements.size(); i++) {
                    // 热点url
                    String url = "";
                    if (platform.getHotTitleUrlNeedDeal()) {
                        url = CommonHotPointInfoDealUtil.getUrlAfterDealResult(platform, rootElements.get(i), null);
                    } else {
                        url = platform.getHotTitleUrlPrefix() + safeAttr(rootElements.get(i), selector.getUrl(), "href") + platform.getHotTitleUrlAfter();
                    }
                    // 热点标题
                    String title = "";
                    if (platform.getHotTitleNeedDeal()) {
                        title = CommonHotPointInfoDealUtil.getHotTitleAfterDealResult(platform, rootElements.get(i), null);
                    } else {
                        title = safeText(rootElements.get(i), selector.getTitle());
                    }
                    String publishTime = safeText(rootElements.get(i), selector.getPublishTime());
                    String commentCount = StrUtil.isNotBlank(safeText(rootElements.get(i), selector.getCommentCount())) ? safeText(rootElements.get(i), selector.getCommentCount()) : "0";
                    String hotScore = "";
                    if (platform.getHotScoreNeedDeal()) {
                        hotScore = CommonHotPointInfoDealUtil.getHotScoreAfterDealResult(platform, rootElements.get(i), null);
                    } else {
                        hotScore = safeText(rootElements.get(i), selector.getHotScore()).toString();
                    }

                    if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                        topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, hotScore, url, "", "", "", "", publishTime, commentCount, null, null, "", i + 1));
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
