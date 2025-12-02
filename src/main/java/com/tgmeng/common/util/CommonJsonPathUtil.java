package com.tgmeng.common.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.tgmeng.common.config.JsonPathConfig;
import com.tgmeng.common.config.RequestInfoManager;
import com.tgmeng.common.enums.business.SearchTypeCCTVEnum;
import com.tgmeng.common.enums.business.SearchTypeMangGuoEnum;
import com.tgmeng.common.enums.business.SearchTypeYouKuEnum;
import com.tgmeng.common.enums.enumcommon.EnumUtils;
import com.tgmeng.model.vo.topsearch.TopSearchCommonVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonJsonPathUtil {

    public static List<TopSearchCommonVO.DataInfo> getCommonResult(String content, RequestInfoManager.PlatformConfig platform) {
        if (StrUtil.equals(platform.getPlatformCategory(), "maoyan")) {
            content = CommonHotPointInfoDealUtil.getMaoYanJsonFromContent(content);
        } else if (StrUtil.equals(platform.getPlatformCategory(), "cctv")) {
            content = content.replaceAll("^setItem1\\((.*)\\);$", "$1");
        }
        ReadContext ctx =  JsonPath.using(JsonPathConfig.DEFAULT_CONF).parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();

        for (RequestInfoManager.Selector selector : platform.getSelectors()) {
            List<Map<String, Object>> hotNewsList = new ArrayList<>();
            String rootSelector = selector.getRoot();
            if (StrUtil.equals(platform.getPlatformCategory(), "mangguo")) {
                rootSelector = rootSelector.replace("{type}", EnumUtils.getValueByKey(SearchTypeMangGuoEnum.class, HttpRequestUtil.getRequestPathLastWord()));
            } else if (StrUtil.equals(platform.getPlatformCategory(), "youku")) {
                rootSelector = rootSelector.replace("{type}", EnumUtils.getValueByKey(SearchTypeYouKuEnum.class, HttpRequestUtil.getRequestPathLastWord()));
            } else if (StrUtil.equals(platform.getPlatformCategory(), "cctv")) {
                rootSelector = rootSelector.replace("{type}", EnumUtils.getValueByKey(SearchTypeCCTVEnum.class, HttpRequestUtil.getRequestPathLastWord()));
            }
            Object read = ctx.read(rootSelector);
            // 判断类型，如果不是 List，就包装成一个单元素 List，这是因为微博置顶的那一条他是单独出来的
            if (read instanceof List) {
                hotNewsList = (List<Map<String, Object>>) read;
            } else if (read != null) {
                hotNewsList.add((Map<String, Object>) read);
            } else {
                hotNewsList = new ArrayList<>();
            }

            for (Object element : hotNewsList) {
                ReadContext itemCtx =  JsonPath.using(JsonPathConfig.DEFAULT_CONF).parse(element);
                String title = itemCtx.read(selector.getKeyword());
                String url = "";
                if (platform.getHotTitleUrlNeedDeal()) {
                    url = CommonHotPointInfoDealUtil.getUrlAfterDealResult(platform, null, itemCtx);
                } else {
                    url = platform.getHotTitleUrlPrefix() + itemCtx.read(selector.getUrl()) + platform.getHotTitleUrlAfter();
                    if (ObjectUtil.isEmpty(url)) {
                        url = "https://tgmeng.com";
                    }
                }
                String hotScore = "";
                if (platform.getHotScoreNeedDeal()) {
                    hotScore = CommonHotPointInfoDealUtil.getHotScoreAfterDealResult(platform, null, itemCtx);
                }
                if (StrUtil.isNotBlank(selector.getHotScore())) {  // 改这里
                    try {
                        hotScore = itemCtx.read(selector.getHotScore()).toString();  // 改这里
                    } catch (Exception e) {
                        hotScore = "";
                    }
                }
                Long startTime = 0L;
                if (StrUtil.isNotBlank(selector.getStartTime())) {
                    startTime = toLong(itemCtx.read(selector.getStartTime()));
                }
                Long endTime = 0L;
                if (StrUtil.isNotBlank(selector.getEndTime())) {
                    endTime = toLong(itemCtx.read(selector.getEndTime()));
                }
                String showTime = "";
                if (StrUtil.isNotBlank(selector.getStartTime())) {
                    showTime = itemCtx.read(selector.getShowTime());
                }
                String type = "";
                type = switch (platform.getPlatformCategory()) {
                    case "cctv" -> HttpRequestUtil.getRequestPathLastWord();
                    case "maoyan" -> itemCtx.read(selector.getType());
                    default -> "";
                };

                String image = "";
                image = switch (platform.getPlatformCategory()) {
                    case "maoyan" -> itemCtx.read(selector.getImage());
                    default -> "";
                };
                String author = "";
                author = switch (platform.getPlatformCategory()) {
                    case "maoyan" -> itemCtx.read(selector.getAuthor());
                    default -> "";
                };
                String desc = "";
                desc = switch (platform.getPlatformCategory()) {
                    case "maoyan" -> itemCtx.read(selector.getDesc());
                    default -> "";
                };
                String publishTime = "";
                publishTime = switch (publishTime) {
                    case "maoyan" -> itemCtx.read(selector.getPublishTime());
                    default -> "";
                };
                String commentCount = "";

                if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                    topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, hotScore, url, image, author, desc, type, publishTime, commentCount, startTime, endTime, showTime));
                }
            }
        }
        return topSearchCommonVOS;
    }

    private static Long toLong(Object value) {
        if (value == null) return null;

        if (value instanceof Long) return (Long) value;
        if (value instanceof Integer) return ((Integer) value).longValue();
        if (value instanceof String) return Long.parseLong((String) value);

        throw new IllegalArgumentException("无法转换为 Long: " + value);
    }
}
