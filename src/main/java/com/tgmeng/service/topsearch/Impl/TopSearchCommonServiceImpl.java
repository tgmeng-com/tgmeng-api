package com.tgmeng.service.topsearch.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.dtflys.forest.http.ForestCookie;
import com.dtflys.forest.http.ForestResponse;
import com.github.houbb.opencc4j.util.ZhConverterUtil;
import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.config.RequestInfoManager;
import com.tgmeng.common.enums.business.*;
import com.tgmeng.common.enums.enumcommon.EnumUtils;
import com.tgmeng.common.enums.exception.ServerExceptionEnum;
import com.tgmeng.common.enums.system.ResponseTypeEnum;
import com.tgmeng.common.exception.ServerException;
import com.tgmeng.common.forest.client.topsearch.ITopSearchCommonClient;
import com.tgmeng.common.forest.header.ForestRequestHeader;
import com.tgmeng.common.forest.httptype.ForestRequestTypeEnum;
import com.tgmeng.common.util.*;
import com.tgmeng.model.vo.topsearch.TopSearchCommonVO;
import com.tgmeng.service.topsearch.ITopSearchCommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class TopSearchCommonServiceImpl implements ITopSearchCommonService {

    private final ITopSearchCommonClient topSearchCommonClient;
    private final RequestInfoManager requestInfoManager;

    @Override
    public ResultTemplateBean<TopSearchCommonVO> getTopSearchCommonService() {
        return getCommonResult();
    }

    public ResultTemplateBean<TopSearchCommonVO> getCommonResult() {
        // 获取请求的信息
        RequestInfoManager.PlatformConfig platform = requestInfoManager.getPlatformConfigByInterfaceUrl(HttpRequestUtil.getRequestPath());
        // url占位符处理
        Map<String, Object> urlPlaceHolderDeal = urlPlaceHolderDeal(platform);
        String platformUrl = urlPlaceHolderDeal.get("url").toString();
        ForestRequestHeader forestRequestHeader = platform.getForestRequestHeader();
        if (StrUtil.equals(platform.getPlatformCategory(), PlatFormCategoryEnum.YOU_KU_SHI_PIN.getValue())) {
            forestRequestHeader = (ForestRequestHeader) urlPlaceHolderDeal.get("forestRequestHeader");
        }
        // 处理请求
        ForestResponse forestResponse;
        switch (platform.getRequestType()) {
            case ForestRequestTypeEnum.GET:
                if (platform.getPlatformCategory().equals(PlatFormCategoryEnum.FOUR_GAMER.getValue())) {
                    byte[] forestResponseBytes = topSearchCommonClient.commonHttpGetUtilsForBytes(
                            forestRequestHeader,
                            platformUrl,
                            platform.getQueryParams());
                    forestResponse = topSearchCommonClient.commonHttpGetUtils(
                            forestRequestHeader,
                            platformUrl,
                            platform.getQueryParams());
                    forestResponse.setContent(decode(forestResponseBytes, "EUC-JP"));
                } else {
                    forestResponse = topSearchCommonClient.commonHttpGetUtils(
                            forestRequestHeader,
                            platformUrl,
                            platform.getQueryParams());
                }
                break;
            case ForestRequestTypeEnum.POST:
                if (ObjectUtil.isNotEmpty(platform.getJsonBody())) {
                    // 处理jsonBody
                    CommonJsonPathUtil.jsonBodyDeal(platform);
                    forestResponse = topSearchCommonClient.commonHttpPostUtils(
                            forestRequestHeader,
                            platformUrl,
                            platform.getJsonBody());
                } else {
                    forestResponse = topSearchCommonClient.commonHttpPostWithNoJsonBodyUtils(
                            forestRequestHeader,
                            platformUrl);
                }
                break;
            default:
                throw new ServerException("❌请求数据失败，请求类型不匹配：" + platform.getPlatformName());
        }
        String content = forestResponse.getContent();

        if (StrUtil.isBlank(content)) {
            throw new ServerException("❌请求数据失败：" + platform.getPlatformName());
        }
        // 处理结果
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        if (platform.getResponseType() == ResponseTypeEnum.DOM) {
            // DOM处理
            topSearchCommonVOS = CommonJsoupUtil.getCommonResult(content, platform);
        } else if (platform.getResponseType() == ResponseTypeEnum.INTERFACE) {
            // JSON处理
            topSearchCommonVOS = CommonJsonPathUtil.getCommonResult(content, platform);
        } else if (platform.getResponseType() == ResponseTypeEnum.RSS) {
            // RSS处理
            topSearchCommonVOS = CommonRssUtil.getCommonResult(content, platform);
        }
        convertChineseToSimple(topSearchCommonVOS, platform.getPlatformCategory());
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                topSearchCommonVOS,
                platform.getPlatformName(),
                platform.getPlatformLogo(),
                platform.getPlatformCategory(),
                platform.getPlatformCategoryRoot()
        );
        return ResultTemplateBean.success(topSearchCommonVO);
    }

    // 根据接口路径去动态替换url里面的占位符
    public Map<String, Object> urlPlaceHolderDeal(RequestInfoManager.PlatformConfig platform) {
        String url = platform.getUrl();
        String platformCategory = platform.getPlatformCategory();
        Map<String, Object> map = new HashMap<>();
        if (StrUtil.equals(platformCategory, PlatFormCategoryEnum.GITHUB.getValue())) {
            url = url.replace("{time}", EnumUtils.getValueByKey(SearchTypeGithubEnum.class, HttpRequestUtil.getRequestPathLastWord()));
        } else if (StrUtil.equals(platformCategory, PlatFormCategoryEnum.HUGGING_FACES.getValue())) {
            url = url.replace("{type}", EnumUtils.getValueByKey(SearchTypeHuggingFaceEnum.class, HttpRequestUtil.getRequestPathLastWord()));
        } else if (StrUtil.equals(platformCategory, PlatFormCategoryEnum.TENG_XUN_SHI_PIN.getValue())) {
            url = url.replace("{type}", EnumUtils.getValueByKey(SearchTypeTengXunShiPinEnum.class, HttpRequestUtil.getRequestPathLastWord()));
        } else if (StrUtil.equals(platformCategory, PlatFormCategoryEnum.AI_QI_YI_SHI_PIN.getValue())) {
            url = url.replace("{type}", EnumUtils.getValueByKey(SearchTypeAiQiYiEnum.class, HttpRequestUtil.getRequestPathLastWord()));
        } else if (StrUtil.equals(platformCategory, PlatFormCategoryEnum.MANG_GUO_SHI_PIN.getValue())) {
            url = url.replace("{type}", EnumUtils.getValueByKey(SearchTypeMangGuoEnum.class, HttpRequestUtil.getRequestPathLastWord()));
        } else if (StrUtil.equals(platformCategory, PlatFormCategoryEnum.MAO_YAN.getValue())) {
            url = url.replace("{type}", EnumUtils.getValueByKey(SearchTypeMaoYanEnum.class, HttpRequestUtil.getRequestPathLastWord()));
        } else if (StrUtil.equals(platformCategory, PlatFormCategoryEnum.YOU_KU_SHI_PIN.getValue())) {
            Map<String, Object> youkuUrlAndCookie = urlPlaceHolderDealForYouku(platform);
            url = youkuUrlAndCookie.get("url").toString();
            map.put("forestRequestHeader", youkuUrlAndCookie.get("forestRequestHeader"));
        } else if (StrUtil.equals(platformCategory, PlatFormCategoryEnum.XIAO_ZU_DOU_BAN.getValue())) {
            url = url.replace("{type}", EnumUtils.getValueByKey(SearchTypeXiaoZuDouBanEnum.class, HttpRequestUtil.getRequestPathLastWord()));
        } else if (StrUtil.equals(platformCategory, PlatFormCategoryEnum.CCTV.getValue())) {
            url = url.replace("{type}", EnumUtils.getValueByKey(SearchTypeCCTVEnum.class, HttpRequestUtil.getRequestPathLastWord()))
                    .replace("{time}", TimeUtil.getCurrentTimeFormat("yyyyMMdd"));
        } else if (StrUtil.equals(platformCategory, PlatFormCategoryEnum.TU_YA_WANG_GUO.getValue())) {
            url = url.replace("{type}", EnumUtils.getValueByKey(SearchTypeTuYaWangGuoEnum.class, HttpRequestUtil.getRequestPathLastWord()));
        } else if (StrUtil.equals(platformCategory, PlatFormCategoryEnum.GUO_JI_KE_JI_CHUANG_XIN_ZHONG_XIN.getValue())) {
            url = url.replace("{type}", EnumUtils.getValueByKey(SearchTypeGuoJiKeJiChuangXinZhongXinnum.class, HttpRequestUtil.getRequestPathLastWord()));
        } else if (StrUtil.equals(platformCategory, PlatFormCategoryEnum.WANG_YI_YUN_YIN_YUE.getValue())) {
            url = url.replace("{type}", EnumUtils.getValueByKey(SearchTypeWangYiYunEnum.class, HttpRequestUtil.getRequestPathLastWord()));
        } else if (StrUtil.equals(platformCategory, PlatFormCategoryEnum.BAI_DU.getValue())) {
            url = url.replace("{type}", EnumUtils.getValueByKey(SearchTypeBaiDuEnum.class, HttpRequestUtil.getRequestPathLastWord()));
        } else if (StrUtil.equals(platformCategory, PlatFormCategoryEnum.ZHAN_KU.getValue())) {
            url = url.replace("{type}", EnumUtils.getValueByKey(SearchTypeZhanKuEnum.class, HttpRequestUtil.getRequestPathLastWord()));
        } else if (StrUtil.equals(platformCategory, PlatFormCategoryEnum.QOOAPP.getValue())) {
            url = url.replace("{type}", EnumUtils.getValueByKey(SearchTypeQooAppEnum.class, HttpRequestUtil.getRequestPathLastWord()));
        } else if (StrUtil.equals(platformCategory, PlatFormCategoryEnum.BA_HA_MU_TE.getValue())) {
            url = url.replace("{type}", EnumUtils.getValueByKey(SearchTypeBaHaMuTeEnum.class, HttpRequestUtil.getRequestPathLastWord()));
        } else if (StrUtil.equals(platformCategory, PlatFormCategoryEnum.FOUR_GAMER.getValue())) {
            url = url.replace("{type}", EnumUtils.getValueByKey(SearchType4GamerEnum.class, HttpRequestUtil.getRequestPathLastWord()));
        } else if (StrUtil.equals(platformCategory, PlatFormCategoryEnum.NODELOC.getValue())) {
            url = url.replace("{type}", EnumUtils.getValueByKey(SearchTypeNodeLocEnum.class, HttpRequestUtil.getRequestPathLastWord()));
        }
        map.put("url", url);
        return map;
    }

    private Map<String, Object> urlPlaceHolderDealForYouku(RequestInfoManager.PlatformConfig platform) {
        try {
            ForestResponse forestResponse = topSearchCommonClient.youKuCookie(new ForestRequestHeader().setReferer("https://acz.youku.com/").setOrigin("https://acz.youku.com"));
            List<ForestCookie> cookies = forestResponse.getCookies();
            // 提取 _m_h5_tk Cookie
            String _m_h5_tk = null;
            for (ForestCookie cookie : cookies) {
                if ("_m_h5_tk".equals(cookie.getName())) {
                    _m_h5_tk = cookie.getValue();
                    break;
                }
            }
            if (_m_h5_tk == null) {
                throw new RuntimeException("Missing _m_h5_tk cookie");
            }
            // 提取 token
            String token = _m_h5_tk.split("_")[0];
            String appKey = "23774304";
            //这个data就是查询参数
            String data = "%7B%22pg%22%3A%221%22%2C%22pz%22%3A%2210%22%2C%22appScene%22%3A%22default_page%22%2C%22appCaller%22%3A%22youku-search-sdk%22%2C%22utdId%22%3A%22%22%7D";
            String decodeData = URLDecoder.decode(data, "UTF-8");
            Long time = TimeUtil.getCurrentTimeMillis();
            String signStr = new StringJoiner("&").add(token).add(time.toString()).add(appKey).add(decodeData).toString();
            String sign = DigestUtil.md5Hex(signStr);

            String userAgent = UserAgentGeneratorUtil.generateRandomUserAgent();
            String cookieString = cookies.stream()
                    .map(cookie -> cookie.getName() + "=" + cookie.getValue())
                    .collect(Collectors.joining("; "));
            cookieString += ";isI18n=false";
            ForestRequestHeader forestRequestHeader = new ForestRequestHeader()
                    .setUserAgent(userAgent)
                    .setAcceptLanguage("zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7")
                    .setXForwardedFor("114.114.114.114")
                    .setReferer(ForestRequestHeaderRefererEnum.YOU_KU.getValue())
                    .setOrigin(ForestRequestHeaderOriginEnum.YOU_KU.getValue())
                    .setCookie(cookieString);
            String url = platform.getUrl().replace("{appKey}", appKey)
                    .replace("{data}", data)
                    .replace("{time}", time.toString())
                    .replace("{sign}", sign);
            platform.setForestRequestHeader(forestRequestHeader);

            return Map.of(
                    "url", url,
                    "forestRequestHeader", forestRequestHeader
            );
        } catch (Exception e) {
            log.error("👺👺👺获取优酷平台cookie失败", e);
            throw new ServerException(ServerExceptionEnum.YOU_KU_TOP_SEARCH_EXCEPTION);
        }
    }

    private String fixEncoding(String garbled) {
        try {
            String s = new String(
                    garbled.getBytes(StandardCharsets.ISO_8859_1),
                    StandardCharsets.UTF_8
            );
            return new String(
                    s.getBytes(StandardCharsets.UTF_8),
                    StandardCharsets.ISO_8859_1
            );
        } catch (Exception e) {
            return garbled;
        }
    }

    private static String decode(byte[] bytes, String charset) {
        // 1. 解压gzip
        byte[] decompressed = decompressIfNeeded(bytes);

        // 2. 解码
        return new String(decompressed, Charset.forName(charset));
    }

    private static byte[] decompressIfNeeded(byte[] data) {
        // 检查gzip魔数：0x1f 0x8b (31, -117)
        if (data.length < 2 || data[0] != 0x1f || data[1] != (byte) 0x8b) {
            return data;
        }

        try (GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(data));
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int len;
            while ((len = gis.read(buffer)) > 0) {
                bos.write(buffer, 0, len);
            }

            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return data;
        }
    }

    // 中文转简体
    private void convertChineseToSimple(List<TopSearchCommonVO.DataInfo> topSearchCommonVOS, String platformCategory) {
        if (CollUtil.isEmpty(topSearchCommonVOS) || !StrUtil.containsAnyIgnoreCase(platformCategory, PlatFormCategoryEnum.QOOAPP.getValue(), PlatFormCategoryEnum.GAME_BASE.getValue(), PlatFormCategoryEnum.BA_HA_MU_TE.getValue())) {
            return;
        }

        for (TopSearchCommonVO.DataInfo topSearchCommonVO : topSearchCommonVOS) {
            if (ObjectUtil.isNotEmpty(topSearchCommonVO) && StrUtil.isNotBlank(topSearchCommonVO.getTitle())) {
                try {
                    topSearchCommonVO.setTitle(ZhConverterUtil.toSimple(topSearchCommonVO.getTitle()));
                } catch (Exception e) {
                    log.error("转换中文为简体失败，title={}，platformCategory={}，异常信息={}", topSearchCommonVO.getTitle(), platformCategory, e.getMessage());
                }
            }
        }
    }

    @Override
    public ResultTemplateBean getCategorys() {
        Map<String, RequestInfoManager.PlatformConfig> configs = requestInfoManager.getConfigs();
        Map<String, List<Map<String, String>>> groupedData = new LinkedHashMap<>();
        for (Map.Entry<String, RequestInfoManager.PlatformConfig> entry : configs.entrySet()) {
            RequestInfoManager.PlatformConfig platformData = entry.getValue();
            String platformCategoryRoot = platformData.getPlatformCategoryRoot();
            String platformCategory = platformData.getPlatformCategory();
            String platformName = platformData.getPlatformName();
            if (platformCategoryRoot == null || platformCategoryRoot.isEmpty()) {
                platformCategoryRoot = "其他";
            }
            Map<String, String> info = new HashMap<>();
            info.put("platformCategory", platformCategory);
            info.put("platformName", platformName);
            groupedData.computeIfAbsent(platformCategoryRoot, k -> new ArrayList<>())
                    .add(info);
        }

        List<String> sortedCategoryRoot = EnumUtils.sortValuesByEnumSort(PlatFormCategoryRootEnum.class, new ArrayList<>(groupedData.keySet()));

        List<Map<String, Object>> result = new ArrayList<>();
        for (String root : sortedCategoryRoot) {
            Map<String, Object> categoryData = new LinkedHashMap<>();
            categoryData.put("platformCategoryRoot", root);
            categoryData.put("platforms", groupedData.get(root));
            result.add(categoryData);
        }
        return ResultTemplateBean.success(result);
    }

}