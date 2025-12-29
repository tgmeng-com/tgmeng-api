package com.tgmeng.common.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.tgmeng.common.config.JsonPathConfig;
import com.tgmeng.common.config.RequestInfoManager;
import com.tgmeng.common.enums.business.*;
import com.tgmeng.common.enums.enumcommon.EnumUtils;
import com.tgmeng.model.vo.topsearch.TopSearchCommonVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonJsonPathUtil {

    public static List<TopSearchCommonVO.DataInfo> getCommonResult(String content, RequestInfoManager.PlatformConfig platform) {
        if (StrUtil.equals(platform.getPlatformCategory(), PlatFormCategoryEnum.MAO_YAN.getValue())) {
            content = CommonHotPointInfoDealUtil.getMaoYanJsonFromContent(content);
        } else if (StrUtil.equals(platform.getPlatformCategory(), PlatFormCategoryEnum.CCTV.getValue())) {
            content = content.replaceAll("^setItem1\\((.*)\\);$", "$1");
        } else if (StrUtil.equals(platform.getPlatformCategory(), "zhiyuanshequ")) {
            Pattern pattern = Pattern.compile("resources:\\s*\\{\\s*data:\\s*(\\[[\\s\\S]*?\\])\\s*,\\s*pageIndex:");
            Matcher matcher = pattern.matcher(content);
            if (matcher.find()) {
                content = matcher.group(1);
            }
        }
        ReadContext ctx = JsonPath.using(JsonPathConfig.DEFAULT_CONF).parse(content);
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();

        for (RequestInfoManager.Selector selector : platform.getSelectors()) {
            List<Map<String, Object>> hotNewsList = new ArrayList<>();
            String rootSelector = selector.getRoot();
            if (StrUtil.equals(platform.getPlatformCategory(), PlatFormCategoryEnum.MANG_GUO_SHI_PIN.getValue())) {
                rootSelector = rootSelector.replace("{type}", EnumUtils.getValueByKey(SearchTypeMangGuoEnum.class, HttpRequestUtil.getRequestPathLastWord()));
            } else if (StrUtil.equals(platform.getPlatformCategory(), PlatFormCategoryEnum.YOU_KU_SHI_PIN.getValue())) {
                rootSelector = rootSelector.replace("{type}", EnumUtils.getValueByKey(SearchTypeYouKuEnum.class, HttpRequestUtil.getRequestPathLastWord()));
            } else if (StrUtil.equals(platform.getPlatformCategory(), PlatFormCategoryEnum.CCTV.getValue())) {
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
                ReadContext itemCtx = JsonPath.using(JsonPathConfig.DEFAULT_CONF).parse(element);
                String title = itemCtx.read(selector.getTitle());
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
                if (platform.getPlatformCategory().equals(PlatFormCategoryEnum.CCTV.getValue())) {
                    type = HttpRequestUtil.getRequestPathLastWord();
                } else if (platform.getPlatformCategory().equals(PlatFormCategoryEnum.MAO_YAN.getValue())) {
                    type = itemCtx.read(selector.getType());
                }
                String image = "";
                if (platform.getPlatformCategory().equals(PlatFormCategoryEnum.MAO_YAN.getValue())) {
                    image = itemCtx.read(selector.getImage());
                }
                String author = "";
                if (platform.getPlatformCategory().equals(PlatFormCategoryEnum.MAO_YAN.getValue()) ||
                        platform.getPlatformCategory().equals(PlatFormCategoryEnum.WANG_YI_YUN_YIN_YUE.getValue())) {
                    author = itemCtx.read(selector.getAuthor());
                }
                String desc = "";
                if (platform.getPlatformCategory().equals(PlatFormCategoryEnum.MAO_YAN.getValue())) {
                    desc = itemCtx.read(selector.getDesc());
                } else if (platform.getPlatformCategory().equals(PlatFormCategoryEnum.GITHUB.getValue())) {
                    desc = itemCtx.read(selector.getDesc());
                }
                String publishTime = "";
                if (platform.getPlatformCategory().equals(PlatFormCategoryEnum.MAO_YAN.getValue())) {
                    publishTime = itemCtx.read(selector.getPublishTime());
                }
                String commentCount = "";

                if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                    topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, hotScore, url, image, author, desc, type, publishTime, commentCount, startTime, endTime, showTime));
                }
            }
        }
        return topSearchCommonVOS;
    }

    public static void jsonBodyDeal(RequestInfoManager.PlatformConfig platform) {
        if (platform.getJsonBodyNeedDeal()) {
            Map<String, Object> jsonBody = platform.getJsonBody();
            String platformCategory = platform.getPlatformCategory();
            if (StrUtil.equals(platformCategory, PlatFormCategoryEnum.GAME_BASE.getValue())) {
                jsonBody.put("category", EnumUtils.getValueByKey(SearchTypeGameBaseEnum.class, HttpRequestUtil.getRequestPathLastWord()));
                platform.setJsonBody(jsonBody);
            }
        }
    }

    private static Long toLong(Object value) {
        if (value == null) return null;

        if (value instanceof Long) return (Long) value;
        if (value instanceof Integer) return ((Integer) value).longValue();
        if (value instanceof String) return Long.parseLong((String) value);

        throw new IllegalArgumentException("无法转换为 Long: " + value);
    }
}
