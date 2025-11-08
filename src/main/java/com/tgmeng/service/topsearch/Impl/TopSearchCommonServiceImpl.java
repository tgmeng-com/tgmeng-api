package com.tgmeng.service.topsearch.Impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.dtflys.forest.http.ForestCookie;
import com.dtflys.forest.http.ForestResponse;
import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.cache.TopSearchDataCache;
import com.tgmeng.common.enums.business.*;
import com.tgmeng.common.enums.exception.ServerExceptionEnum;
import com.tgmeng.common.exception.ServerException;
import com.tgmeng.common.forest.client.topsearch.ITopSearchCommonClient;
import com.tgmeng.common.mapper.mapstruct.topsearch.ITopSearchCommonMapper;
import com.tgmeng.common.util.CommonJsoupJsoupParseUtil;
import com.tgmeng.common.util.ForestUtil;
import com.tgmeng.common.util.StringUtil;
import com.tgmeng.common.util.TimeUtil;
import com.tgmeng.model.dto.topsearch.*;
import com.tgmeng.model.vo.topsearch.TopSearchCommonVO;
import com.tgmeng.service.topsearch.ITopSearchCommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class TopSearchCommonServiceImpl implements ITopSearchCommonService {

    private final ITopSearchCommonClient topSearchCommonClient;
    private final ITopSearchCommonMapper topSearchCommonMapper;
    private final TopSearchDataCache topSearchDataCache;

    /**
     * description: 百度热搜
     * method: getBaiDuTopSearch
     *
     * @author tgmeng
     * @since 2025/6/29 15:17
     */
    @Override
    public ResultTemplateBean getBaiDuTopSearch(SearchTypeBaiDuEnum searchTypeBaiDuEnum) {
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchBaiDuDTO topSearchBaiDuDTO = topSearchCommonClient.baiDu(ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.BAIDU.getValue(), ForestRequestHeaderOriginEnum.BAIDU.getValue()), searchTypeBaiDuEnum.getValue());
            topSearchCommonVOS = Optional.ofNullable(topSearchBaiDuDTO.getData())
                    .map(TopSearchBaiDuDTO.DataVO::getCards)
                    .orElse(Collections.emptyList())
                    .stream()
                    .filter(cardsVO -> cardsVO.getContent() != null || cardsVO.getTopContent() != null)
                    .flatMap(cardsVO ->
                            Stream.concat(
                                    //把置顶的那一个叼毛数据也合并进来
                                    cardsVO.getTopContent() != null ? cardsVO.getTopContent().stream() : Stream.empty(),
                                    cardsVO.getContent() != null ? cardsVO.getContent().stream() : Stream.empty()
                            ))
                    .map(topSearchCommonMapper::topSearchBaiDuDTOContentVO2TopSearchCommonVO)
                    //这里不用排序，因为百度的热搜排序不是按照score排，是按照他返回的顺序排的
                    //.sorted(Comparator.comparing(topSearchCommonVOS::getHotScore).reversed())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("👺👺👺获取百度热搜失败👺👺👺：平台；{}", searchTypeBaiDuEnum.getKey(), e);
            throw new ServerException(ServerExceptionEnum.BAIDU_TOP_SEARCH_EXCEPTION);
        }
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS, searchTypeBaiDuEnum.getDescription(), DataInfoCardEnum.BAIDU.getValue(), DataInfoCardEnum.BAIDU.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
    }

    /**
     * description: b站热搜
     * method: getBilibiliTopSearch
     *
     * @author tgmeng
     * @since 2025/6/29 15:37
     */
    @Override
    public ResultTemplateBean getBilibiliTopSearch() {
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchBilibiliDTO topSearchBilibiliDTO = topSearchCommonClient.bilibili(ForestUtil.getRandomRequestHeaderForBilibili());
            topSearchCommonVOS = Optional.ofNullable(topSearchBilibiliDTO.getData())
                    .map(TopSearchBilibiliDTO.DataView::getList)
                    .orElse(Collections.emptyList())
                    .stream().map(topSearchCommonMapper::topSearchBilibiliDTODataVO2TopSearchCommonVO)
                    .toList();
        } catch (Exception e) {
            log.error("👺👺👺获取B站热搜失败👺👺👺", e);
            throw new ServerException(ServerExceptionEnum.BILIBILI_TOP_SEARCH_EXCEPTION);
        }
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS, DataInfoCardEnum.BILIBILI.getKey(), DataInfoCardEnum.BILIBILI.getValue(), DataInfoCardEnum.BILIBILI.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
    }

    /**
     * description: 微博热搜
     * method: getWeiBoTopSearch
     *
     * @author tgmeng
     * @since 2025/6/29 19:24
     */
    @Override
    public ResultTemplateBean getWeiBoTopSearch() {
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchWeiBoDTO topSearchWeiBoDTO = topSearchCommonClient.weiBo(ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.WEIBO.getValue(), ForestRequestHeaderOriginEnum.WEIBO.getValue()));
            //添加置顶
            topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo().setKeyword(topSearchWeiBoDTO.getData().getHotgov().getWord()).setUrl(topSearchWeiBoDTO.getData().getHotgov().getUrl()));
            //添加热榜
            topSearchCommonVOS.addAll(Optional.ofNullable(topSearchWeiBoDTO.getData())
                    .map(TopSearchWeiBoDTO.DataView::getBandList)
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(t -> {
                        Long realPos = Optional.ofNullable(t.getRealpos()).orElse(0L);
                        t.setUrl(StringUtil.weiBoTopSearchItemUrlUtil(t.getNote(), realPos));
                        return t;
                    })
                    .map(topSearchCommonMapper::topSearchWeiBoDTODataVO2TopSearchCommonVO)
                    .toList())
            ;
        } catch (Exception e) {
            log.error("👺👺👺获取微博热搜失败👺👺👺", e);
            throw new ServerException(ServerExceptionEnum.WEIBO_TOP_SEARCH_EXCEPTION);
        }
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS, DataInfoCardEnum.WEIBO.getKey(), DataInfoCardEnum.WEIBO.getValue(), DataInfoCardEnum.WEIBO.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
    }

    /**
     * description: 抖音热搜
     * method: getDouYinTopSearch
     *
     * @author tgmeng
     * @since 2025/6/29 22:40
     */
    @Override
    public ResultTemplateBean getDouYinTopSearch() {
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchDouYinDTO topSearchDouYinDTO = topSearchCommonClient.douYin(ForestUtil.getRandomRequestHeaderForDouYin());
            topSearchCommonVOS = Optional.ofNullable(topSearchDouYinDTO.getData())
                    .map(TopSearchDouYinDTO.DataView::getWordList)
                    .orElse(Collections.emptyList())
                    .stream().map(topSearchCommonMapper::topSearchDouYinDTODataVO2TopSearchCommonVO)
                    .toList();
        } catch (Exception e) {
            log.error("👺👺👺获取抖音热搜失败👺👺👺", e);
            throw new ServerException(ServerExceptionEnum.DOUYIN_TOP_SEARCH_EXCEPTION);
        }
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS, DataInfoCardEnum.DOU_YIN.getKey(), DataInfoCardEnum.DOU_YIN.getValue(), DataInfoCardEnum.DOU_YIN.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
    }

    @Override
    public ResultTemplateBean getDouBanTopSearch() {
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            List<TopSearchDouBanDTO> topSearchDouBanDTO = topSearchCommonClient.douBan(ForestUtil.getRandomRequestHeaderForDouBan());
            topSearchCommonVOS.addAll(topSearchCommonMapper.topSearchDouBanDTODataVO2TopSearchCommonVOS(topSearchDouBanDTO));
        } catch (Exception e) {
            log.error("👺👺👺获取豆瓣热搜失败👺👺👺", e);
            throw new ServerException(ServerExceptionEnum.DOUBAN_TOP_SEARCH_EXCEPTION);
        }
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS, DataInfoCardEnum.DOU_BAN.getKey(), DataInfoCardEnum.DOU_BAN.getValue(), DataInfoCardEnum.DOU_BAN.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
    }

    @Override
    public ResultTemplateBean getTencentTopSearch() {
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchTencentDTO tencent = topSearchCommonClient.tencent(ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.TENCENT.getValue(), ForestRequestHeaderOriginEnum.TENCENT.getValue()));
            topSearchCommonVOS = Optional.ofNullable(tencent.getIdlist().getFirst())
                    .map(TopSearchTencentDTO.ItemView::getNewslist)
                    .orElse(Collections.emptyList())
                    .stream().map(topSearchCommonMapper::topSearchTencentDTOItemInfoTopSearchCommonVO)
                    .toList();
            // 移除第一条数据,他不是有效数据（如果存在）
            if (!topSearchCommonVOS.isEmpty()) {
                topSearchCommonVOS = new ArrayList<>(topSearchCommonVOS);
                topSearchCommonVOS.removeFirst();
            }
        } catch (Exception e) {
            log.error("👺👺👺获取腾讯新闻热搜失败👺👺👺", e);
            throw new ServerException(ServerExceptionEnum.TENCENT_TOP_SEARCH_EXCEPTION);
        }
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS, DataInfoCardEnum.TENCENT.getKey(), DataInfoCardEnum.TENCENT.getValue(), DataInfoCardEnum.TENCENT.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
    }

    @Override
    public ResultTemplateBean getTouTiaoTopSearch() {
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchTouTiaoDTO toutiao = topSearchCommonClient.toutiao(ForestUtil.getRandomRequestHeaderForTouTiao());
            topSearchCommonVOS = Stream.concat(
                    Optional.ofNullable(toutiao)
                            .map(TopSearchTouTiaoDTO::getFixedTopData)
                            .orElse(Collections.emptyList())
                            .stream()
                            .map(topSearchCommonMapper::topSearchTouTiaoDTOItemInfoTopSearchCommonVO),

                    Optional.ofNullable(toutiao)
                            .map(TopSearchTouTiaoDTO::getData)
                            .orElse(Collections.emptyList())
                            .stream()
                            .map(topSearchCommonMapper::topSearchTouTiaoDTOItemInfoTopSearchCommonVO)
            ).toList();
        } catch (Exception e) {
            log.error("👺👺👺获取今日头条热搜失败👺👺👺", e);
            throw new ServerException(ServerExceptionEnum.TOUTIAO_TOP_SEARCH_EXCEPTION);
        }
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS, DataInfoCardEnum.TOU_TIAO.getKey(), DataInfoCardEnum.TOU_TIAO.getValue(), DataInfoCardEnum.TOU_TIAO.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
    }

    @Override
    public ResultTemplateBean getWangYiTopSearch() {
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchWangYiDTO wangyi = topSearchCommonClient.wangyi(ForestUtil.getRandomRequestHeaderForWangYi());
            topSearchCommonVOS = Optional.ofNullable(wangyi.getData())
                    .map(TopSearchWangYiDTO.ItemDTO::getItems)
                    .orElse(Collections.emptyList())
                    .stream().map(topSearchCommonMapper::topSearchWangYiDTOItemInfoTopSearchCommonVO)
                    .toList();
        } catch (Exception e) {
            log.error("👺👺👺获取网易新闻热搜失败👺👺👺", e);
            throw new ServerException(ServerExceptionEnum.WANGYI_TOP_SEARCH_EXCEPTION);
        }
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS, DataInfoCardEnum.WANG_YI.getKey(), DataInfoCardEnum.WANG_YI.getValue(), DataInfoCardEnum.WANG_YI.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
    }

    @Override
    public ResultTemplateBean getWangYiYunTopSearch(SearchTypeWangYiYunEnum searchTypeWangYiYunEnum) {
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchWangYiYunDTO wangyiyun = topSearchCommonClient.wangyiyun(ForestUtil.getRandomRequestHeaderForWangYiYun(), searchTypeWangYiYunEnum.getValue());
            topSearchCommonVOS = Optional.ofNullable(wangyiyun.getResult())
                    .map(TopSearchWangYiYunDTO.DataDTO::getTracks)
                    .orElse(Collections.emptyList())
                    .stream()
                    .limit(15)
                    .map(topSearchCommonMapper::topSearchWangYiYunDTOItemInfoTopSearchCommonVO)
                    .toList();
        } catch (Exception e) {
            log.error("👺👺👺获取网易云音乐榜失败👺👺👺", e);
            throw new ServerException(ServerExceptionEnum.WANGYIYUN_TOP_SEARCH_EXCEPTION);
        }
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS, searchTypeWangYiYunEnum.getDescription(), DataInfoCardEnum.WANG_YI_YUN.getValue(), DataInfoCardEnum.WANG_YI_YUN.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
    }

    @Override
    public ResultTemplateBean getBaiDuTieBaSearch() {
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchBaiDuTieBaDTO baidutieba = topSearchCommonClient.baidutieba(ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.BAIDUTIEBA.getValue(), ForestRequestHeaderOriginEnum.BAIDUTIEBA.getValue()));
            topSearchCommonVOS = Optional.ofNullable(baidutieba.getData().getBangTopic())
                    .map(TopSearchBaiDuTieBaDTO.ItemView::getTopicList)
                    .orElse(Collections.emptyList())
                    .stream().map(topSearchCommonMapper::topSearchBaiDuTieBaDTOItemInfoTopSearchCommonVO)
                    .toList();
        } catch (Exception e) {
            log.error("👺👺👺获取百度贴吧热搜失败👺👺👺", e);
            throw new ServerException(ServerExceptionEnum.BAIDUTIEBA_TOP_SEARCH_EXCEPTION);
        }
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS, DataInfoCardEnum.BAI_DU_TIE_BA.getKey(), DataInfoCardEnum.BAI_DU_TIE_BA.getValue(), DataInfoCardEnum.BAI_DU_TIE_BA.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
    }

    @Override
    public ResultTemplateBean getShaoShuPaiSearch() {
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchShaoShuPaiDTO shaoshupai = topSearchCommonClient.shaoshupai(ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.SHAOSHUPAI.getValue(), ForestRequestHeaderOriginEnum.SHAOSHUPAI.getValue()));
            topSearchCommonVOS = Optional.ofNullable(shaoshupai)
                    .map(TopSearchShaoShuPaiDTO::getData)
                    .orElse(Collections.emptyList())
                    .stream().map(topSearchCommonMapper::topSearchShaoShuPaiDTOItemInfoTopSearchCommonVO)
                    .toList();
        } catch (Exception e) {
            log.error("👺👺👺获取少数派热搜失败👺👺👺", e);
            throw new ServerException(ServerExceptionEnum.SHAOSHUPAI_TOP_SEARCH_EXCEPTION);
        }
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS, DataInfoCardEnum.SHAO_SHU_PAI.getKey(), DataInfoCardEnum.SHAO_SHU_PAI.getValue(), DataInfoCardEnum.SHAO_SHU_PAI.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
    }

    @Override
    public ResultTemplateBean getZhiHuTopSearch() {
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchZhiHuDTO zhihu = topSearchCommonClient.zhiHu(ForestUtil.getRandomRequestHeaderForZhiHu());
            if (ObjectUtil.isNotNull(zhihu)) {
                zhihu.getData().forEach(t -> {
                    topSearchCommonVOS.add(topSearchCommonMapper.topSearchZhiHuDTOItemInfoTopSearchCommonVO(t.getTarget(), t));
                });
            } else {
                log.error("👺👺👺获取知乎热搜失败👺👺👺");
            }
        } catch (Exception e) {
            log.error("👺👺👺获取少数派热搜失败👺👺👺", e);
            throw new ServerException(ServerExceptionEnum.ZHI_HU_TOP_SEARCH_EXCEPTION);
        }
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS, DataInfoCardEnum.ZHI_HU.getKey(), DataInfoCardEnum.ZHI_HU.getValue(), DataInfoCardEnum.ZHI_HU.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
    }

    @Override
    public ResultTemplateBean getTengXunShiPinTopSearch(SearchTypeTengXunShiPinEnum searchTypeTengXunShiPinEnum) {
        try {
            ForestResponse forestResponse = topSearchCommonClient.tengXunShiPin(
                    ForestUtil.getRandomRequestHeaderForTengXunShiPin(),
                    searchTypeTengXunShiPinEnum.getValue()
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.TENG_XUN_SHI_PIN_TOP_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.tengXunShiPin(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.TENG_XUN_SHI_PIN.getKey(),
                    DataInfoCardEnum.TENG_XUN_SHI_PIN.getValue(),
                    DataInfoCardEnum.TENG_XUN_SHI_PIN.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);

        } catch (Exception e) {
            log.error("👺👺👺获取腾讯视频失败👺👺👺：平台；{}", DataInfoCardEnum.TENG_XUN_SHI_PIN.getKey(), e);
            throw new ServerException(ServerExceptionEnum.HUGGING_FACE_TOP_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getAiQiYiTopSearch(SearchTypeAiQiYiEnum searchTypeAiQiYiEnum) {
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchAiQiYiDTO topSearchAiQiYiDTO = topSearchCommonClient.aiQiYi(ForestUtil.getRandomRequestHeaderForAiQiYi(), searchTypeAiQiYiEnum.getValue());
            for (TopSearchAiQiYiDTO.Contents content : topSearchAiQiYiDTO.getData().getItems().getFirst().getContents()) {
                TopSearchCommonVO.DataInfo dataInfo = topSearchCommonMapper.topSearchAiQiYiDTOContentVO2TopSearchCommonVO(content);
                topSearchCommonVOS.add(dataInfo);
            }

        } catch (Exception e) {
            log.error("👺👺👺获取爱奇艺失败👺👺👺：平台；{}", searchTypeAiQiYiEnum.getKey(), e);
            throw new ServerException(ServerExceptionEnum.AI_QI_YI_TOP_SEARCH_EXCEPTION);
        }
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS, searchTypeAiQiYiEnum.getDescription(), DataInfoCardEnum.AI_QI_YI.getValue(), DataInfoCardEnum.AI_QI_YI.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
    }

    @Override
    public ResultTemplateBean getYouKuTopSearch(SearchTypeYouKuEnum searchTypeYouKuEnum) {
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            ForestResponse forestResponse = topSearchCommonClient.youKuCookie(ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.YOU_KU.getValue(), ForestRequestHeaderOriginEnum.YOU_KU.getValue()));
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
            TopSearchYouKuDTO topSearchYouKuDTO = topSearchCommonClient.youKu(ForestUtil.getRandomRequestHeaderForYouKu(cookies), appKey, data, time.toString(), sign);
            List<TopSearchYouKuDTO.FinalDataArray> youKuData = new ArrayList<>();
            TopSearchYouKuDTO.TabDataMap youKuTabDataMap = topSearchYouKuDTO.getData().getNodes().getFirst().getNodes().getFirst().getData().getTabDataMap();
            switch (searchTypeYouKuEnum) {
                case SearchTypeYouKuEnum.DIAN_SHI_JU_YOU_KU:
                    youKuData = youKuTabDataMap.getDianShiJu().getNodes().getFirst().getNodes();
                    break;
                case SearchTypeYouKuEnum.DIAN_YING_YOU_KU:
                    youKuData = youKuTabDataMap.getDianYing().getNodes().getFirst().getNodes();
                    break;
                case SearchTypeYouKuEnum.DONG_MAN_YOU_KU:
                    youKuData = youKuTabDataMap.getDongMan().getNodes().getFirst().getNodes();
                    break;
                case SearchTypeYouKuEnum.ZONG_YI_YOU_KU:
                    youKuData = youKuTabDataMap.getZongYi().getNodes().getFirst().getNodes();
                    break;
                case SearchTypeYouKuEnum.ZONG_BANG_YOU_KU:
                    youKuData = youKuTabDataMap.getReMenSouSuo().getNodes().getFirst().getNodes();
                    break;
                default:
                    youKuData = youKuTabDataMap.getReMenSouSuo().getNodes().getFirst().getNodes();
                    break;
            }

            for (TopSearchYouKuDTO.FinalDataArray content : youKuData) {
                TopSearchCommonVO.DataInfo dataInfo = topSearchCommonMapper.topSearchYouKuDTOContentVO2TopSearchCommonVO(content.getData());
                topSearchCommonVOS.add(dataInfo);
            }

        } catch (Exception e) {
            log.error("👺👺👺获取优酷失败👺👺👺：平台；{}", searchTypeYouKuEnum.getKey(), e);
            throw new ServerException(ServerExceptionEnum.YOU_KU_TOP_SEARCH_EXCEPTION);
        }
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS, searchTypeYouKuEnum.getDescription(), DataInfoCardEnum.YOU_KU.getValue(), DataInfoCardEnum.YOU_KU.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
    }

    @Override
    public ResultTemplateBean getMangGuoTopSearch(SearchTypeMangGuoEnum searchTypeMangGuoEnum) {
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchMangGuoDTO topSearchMangGuoDTO = topSearchCommonClient.mangGuo(ForestUtil.getRandomRequestHeaderForMangGuo());
            List<TopSearchMangGuoDTO.FinalData> mangGuoData = new ArrayList<>();
            switch (searchTypeMangGuoEnum) {
                case SearchTypeMangGuoEnum.DIAN_SHI_JU_MANG_GUO:
                    mangGuoData = topSearchMangGuoDTO.getData().getTopList().stream().filter(t -> t.getLabel().equals(SearchTypeMangGuoEnum.DIAN_SHI_JU_MANG_GUO.getValue())).toList().getFirst().getData();
                    break;
                case SearchTypeMangGuoEnum.DIAN_YING_MANG_GUO:
                    mangGuoData = topSearchMangGuoDTO.getData().getTopList().stream().filter(t -> t.getLabel().equals(SearchTypeMangGuoEnum.DIAN_YING_MANG_GUO.getValue())).toList().getFirst().getData();
                    break;
                case SearchTypeMangGuoEnum.DONG_MAN_MANG_GUO:
                    mangGuoData = topSearchMangGuoDTO.getData().getTopList().stream().filter(t -> t.getLabel().equals(SearchTypeMangGuoEnum.DONG_MAN_MANG_GUO.getValue())).toList().getFirst().getData();
                    break;
                case SearchTypeMangGuoEnum.ZONG_YI_MANG_GUO:
                    mangGuoData = topSearchMangGuoDTO.getData().getTopList().stream().filter(t -> t.getLabel().equals(SearchTypeMangGuoEnum.ZONG_YI_MANG_GUO.getValue())).toList().getFirst().getData();
                    break;
                case SearchTypeMangGuoEnum.ZONG_BANG_MANG_GUO:
                    mangGuoData = topSearchMangGuoDTO.getData().getTopList().stream().filter(t -> t.getLabel().equals(SearchTypeMangGuoEnum.ZONG_BANG_MANG_GUO.getValue())).toList().getFirst().getData();
                    break;
                default:
                    mangGuoData = topSearchMangGuoDTO.getData().getTopList().stream().filter(t -> t.getLabel().equals(SearchTypeMangGuoEnum.ZONG_BANG_MANG_GUO.getValue())).toList().getFirst().getData();
                    break;
            }

            for (TopSearchMangGuoDTO.FinalData content : mangGuoData) {
                TopSearchCommonVO.DataInfo dataInfo = topSearchCommonMapper.topSearchMangGuoDTOContentVO2TopSearchCommonVO(content);
                topSearchCommonVOS.add(dataInfo);
            }

        } catch (Exception e) {
            log.error("👺👺👺获取优酷失败👺👺👺：平台；{}", searchTypeMangGuoEnum.getKey(), e);
            throw new ServerException(ServerExceptionEnum.MANG_GUO_SEARCH_EXCEPTION);
        }

        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS, searchTypeMangGuoEnum.getDescription(), DataInfoCardEnum.MANG_GUO.getValue(), DataInfoCardEnum.MANG_GUO.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
    }

    @Override
    public ResultTemplateBean getMaoYanTopSearch(SearchTypeMaoYanEnum searchTypeMaoYanEnum) {
        try {
            ForestResponse forestResponse = topSearchCommonClient.maoYan(
                    ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.MAO_YAN.getValue(), ForestRequestHeaderOriginEnum.MAO_YAN.getValue()),
                    searchTypeMaoYanEnum.getValue()
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.TENG_XUN_SHI_PIN_TOP_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.maoYan(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.TENG_XUN_SHI_PIN.getKey(),
                    DataInfoCardEnum.TENG_XUN_SHI_PIN.getValue(),
                    DataInfoCardEnum.TENG_XUN_SHI_PIN.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);

        } catch (Exception e) {
            log.error("👺👺👺获取腾讯视频失败👺👺👺：平台；{}", DataInfoCardEnum.TENG_XUN_SHI_PIN.getKey(), e);
            throw new ServerException(ServerExceptionEnum.HUGGING_FACE_TOP_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getJinRongJieSearch() {
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchJingRongJieDTO topSearchJingRongJieDTO = topSearchCommonClient.jingRongJie(
                    //ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.JING_RONG_JIE.getValue(), ForestRequestHeaderOriginEnum.JING_RONG_JIE.getValue()),
                    ForestUtil.getRandomRequestHeaderForJinRongJie(),
                    1,
                    30,
                    "103");
            for (TopSearchJingRongJieDTO.ItemDTO content : topSearchJingRongJieDTO.getData().getData()) {
                TopSearchCommonVO.DataInfo dataInfo = topSearchCommonMapper.topSearchJingRongJieDTOContentVO2TopSearchCommonVO(content);
                topSearchCommonVOS.add(dataInfo);
            }

        } catch (Exception e) {
            log.error("👺👺👺获取热点数据失败👺👺👺：平台；{}", DataInfoCardEnum.JING_RONG_JIE.getKey(), e);
            throw new ServerException(ServerExceptionEnum.JIN_RONG_JIE_TOP_SEARCH_EXCEPTION);
        }
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS, DataInfoCardEnum.JING_RONG_JIE.getKey(), DataInfoCardEnum.JING_RONG_JIE.getValue(), DataInfoCardEnum.JING_RONG_JIE.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
    }

    @Override
    public ResultTemplateBean getDiYiCaiJingSearch() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.diYiCaiJing(
                    ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.DI_YI_CAI_JING.getValue(), ForestRequestHeaderOriginEnum.DI_YI_CAI_JING.getValue())
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.DI_YI_CAI_JING_TOP_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.diYiCaiJing(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.DI_YI_CAI_JING.getKey(),
                    DataInfoCardEnum.DI_YI_CAI_JING.getValue(),
                    DataInfoCardEnum.DI_YI_CAI_JING.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);

        } catch (Exception e) {
            log.error("👺👺👺获取第一财经失败👺👺👺：平台；{}", DataInfoCardEnum.DI_YI_CAI_JING.getKey(), e);
            throw new ServerException(ServerExceptionEnum.DI_YI_CAI_JING_TOP_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getTongHuaShunSearch() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.tongHuaShun(
                    ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.TONG_HUA_SHUN.getValue(), ForestRequestHeaderOriginEnum.TONG_HUA_SHUN.getValue())
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.TONG_HUA_SHUN_TOP_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.tongHuaShun(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.TONG_HUA_SHUN.getKey(),
                    DataInfoCardEnum.TONG_HUA_SHUN.getValue(),
                    DataInfoCardEnum.TONG_HUA_SHUN.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);

        } catch (Exception e) {
            log.error("👺👺👺获取第一财经失败👺👺👺：平台；{}", DataInfoCardEnum.TONG_HUA_SHUN.getKey(), e);
            throw new ServerException(ServerExceptionEnum.TONG_HUA_SHUN_TOP_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getHuaErJieJianWenSearch() {
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchHuaErJieJianWenDTO topSearchHuaErJieJianWenDTO = topSearchCommonClient.huaErJieJianWen(
                    ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.HUA_ER_JIE_JIAN_WEN.getValue(), ForestRequestHeaderOriginEnum.HUA_ER_JIE_JIAN_WEN.getValue()));
            for (TopSearchHuaErJieJianWenDTO.Resource content : topSearchHuaErJieJianWenDTO.getData().getItems()) {
                TopSearchCommonVO.DataInfo dataInfo = topSearchCommonMapper.topSearchHuaErJieJianWenDTOContentVO2TopSearchCommonVO(content.getResource());
                topSearchCommonVOS.add(dataInfo);
            }

        } catch (Exception e) {
            log.error("👺👺👺获取热点数据失败👺👺👺：平台；{}", DataInfoCardEnum.HUA_ER_JIE_JIAN_WEN.getKey(), e);
            throw new ServerException(ServerExceptionEnum.HUA_ER_JIE_JIAN_WEN_TOP_SEARCH_EXCEPTION);
        }
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS, DataInfoCardEnum.HUA_ER_JIE_JIAN_WEN.getKey(), DataInfoCardEnum.HUA_ER_JIE_JIAN_WEN.getValue(), DataInfoCardEnum.HUA_ER_JIE_JIAN_WEN.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
    }

    @Override
    public ResultTemplateBean getCaiLianSheSearch() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.caiLianShe(
                    ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.CAI_LIAN_SHE.getValue(), ForestRequestHeaderOriginEnum.CAI_LIAN_SHE.getValue())
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.CAI_LIAN_SHE_TOP_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.caiLianShe(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.CAI_LIAN_SHE.getKey(),
                    DataInfoCardEnum.CAI_LIAN_SHE.getValue(),
                    DataInfoCardEnum.CAI_LIAN_SHE.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);

        } catch (Exception e) {
            log.error("👺👺👺获取财联社失败👺👺👺：平台；{}", DataInfoCardEnum.CAI_LIAN_SHE.getKey(), e);
            throw new ServerException(ServerExceptionEnum.CAI_LIAN_SHE_TOP_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getGeLongHuiSearch() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getLongHui(
                    ForestUtil.getRandomRequestHeaderForGeLongHui()
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.GE_LONG_HUI_TOP_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getLongHui(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.GE_LONG_HUI.getKey(),
                    DataInfoCardEnum.GE_LONG_HUI.getValue(),
                    DataInfoCardEnum.GE_LONG_HUI.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);

        } catch (Exception e) {
            log.error("👺👺👺获取格隆汇失败👺👺👺：平台；{}", DataInfoCardEnum.GE_LONG_HUI.getKey(), e);
            throw new ServerException(ServerExceptionEnum.GE_LONG_HUI_TOP_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getFaBuSearch() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getFaBu(
                    ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.FA_BU.getValue(), ForestRequestHeaderOriginEnum.FA_BU.getValue())
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.FA_BU_TOP_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getFaBu(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.FA_BU.getKey(),
                    DataInfoCardEnum.FA_BU.getValue(),
                    DataInfoCardEnum.FA_BU.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);

        } catch (Exception e) {
            log.error("👺👺👺获取法布失败👺👺👺：平台；{}", DataInfoCardEnum.FA_BU.getKey(), e);
            throw new ServerException(ServerExceptionEnum.FA_BU_TOP_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getJinShiSearch() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getJinShi(
                    ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.JIN_SHI.getValue(), ForestRequestHeaderOriginEnum.JIN_SHI.getValue())
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.JIN_SHI_TOP_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getJinShi(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.JIN_SHI.getKey(),
                    DataInfoCardEnum.JIN_SHI.getValue(),
                    DataInfoCardEnum.JIN_SHI.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);

        } catch (Exception e) {
            log.error("👺👺👺获取法布失败👺👺👺：平台；{}", DataInfoCardEnum.JIN_SHI.getKey(), e);
            throw new ServerException(ServerExceptionEnum.JIN_SHI_TOP_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getNewYueShiBaoSearch() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getNewYueShiBao(
                    ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.NEW_YUE_SHI_BAO.getValue(), ForestRequestHeaderOriginEnum.NEW_YUE_SHI_BAO.getValue())
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.NEW_YUE_SHI_BAO_TOP_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getNewYueShiBao(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.NEW_YUE_SHI_BAO.getKey(),
                    DataInfoCardEnum.NEW_YUE_SHI_BAO.getValue(),
                    DataInfoCardEnum.NEW_YUE_SHI_BAO.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);

        } catch (Exception e) {
            log.error("👺👺👺获取纽约时报失败👺👺👺：平台；{}", DataInfoCardEnum.NEW_YUE_SHI_BAO.getKey(), e);
            throw new ServerException(ServerExceptionEnum.NEW_YUE_SHI_BAO_TOP_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getBBCSearch() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getBBC(
                    ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.BBC.getValue(), ForestRequestHeaderOriginEnum.BBC.getValue())
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.BBC_TOP_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getBBC(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.BBC.getKey(),
                    DataInfoCardEnum.BBC.getValue(),
                    DataInfoCardEnum.BBC.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);

        } catch (Exception e) {
            log.error("👺👺👺获取BBC失败👺👺👺：平台；{}", DataInfoCardEnum.BBC.getKey(), e);
            throw new ServerException(ServerExceptionEnum.BBC_TOP_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getFaGuangSearch() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getFaGuang(
                    ForestUtil.getRandomRequestHeaderForFaGuang()
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.FA_GUANG_TOP_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getFaGuang(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.FA_GUANG.getKey(),
                    DataInfoCardEnum.FA_GUANG.getValue(),
                    DataInfoCardEnum.FA_GUANG.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);

        } catch (Exception e) {
            log.error("👺👺👺获取BBC失败👺👺👺：平台；{}", DataInfoCardEnum.FA_GUANG.getKey(), e);
            throw new ServerException(ServerExceptionEnum.FA_GUANG_TOP_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getHuaErJieRiBaoSearch() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getHuaErJieRiBao(
                    ForestUtil.getRandomRequestHeaderForHuaErJieRiBao()
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.HUA_ER_JIE_RI_BAO_TOP_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getHuaErJieRiBao(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.HUA_ER_JIE_RI_BAO.getKey(),
                    DataInfoCardEnum.HUA_ER_JIE_RI_BAO.getValue(),
                    DataInfoCardEnum.HUA_ER_JIE_RI_BAO.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);

        } catch (Exception e) {
            log.error("👺👺👺获取BBC失败👺👺👺：平台；{}", DataInfoCardEnum.HUA_ER_JIE_RI_BAO.getKey(), e);
            throw new ServerException(ServerExceptionEnum.HUA_ER_JIE_RI_BAO_TOP_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getDaJiYuanSearch() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getDaJiYuan(
                    ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.DA_JI_YUAN.getValue(), ForestRequestHeaderOriginEnum.DA_JI_YUAN.getValue())
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.DA_JI_YUAN_TOP_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getDaJiYuan(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.DA_JI_YUAN.getKey(),
                    DataInfoCardEnum.DA_JI_YUAN.getValue(),
                    DataInfoCardEnum.DA_JI_YUAN.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);

        } catch (Exception e) {
            log.error("👺👺👺获取BBC失败👺👺👺：平台；{}", DataInfoCardEnum.DA_JI_YUAN.getKey(), e);
            throw new ServerException(ServerExceptionEnum.DA_JI_YUAN_TOP_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getWoShiPMSearch() {
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchWoShiPMDTO topSearchWoShiPMDTO = topSearchCommonClient.getWoShiPM(
                    ForestUtil.getRandomRequestHeaderForWoShiPM());
            for (TopSearchWoShiPMDTO.DataInfo content : topSearchWoShiPMDTO.getResult()) {
                TopSearchCommonVO.DataInfo dataInfo = topSearchCommonMapper.topSearchWoShiPMDTOContentVO2TopSearchCommonVO(content.getData());
                topSearchCommonVOS.add(dataInfo);
            }

        } catch (Exception e) {
            log.error("👺👺👺获取人人都是产品经理失败👺👺👺：平台；{}", DataInfoCardEnum.WO_SHI_PM.getKey(), e);
            throw new ServerException(ServerExceptionEnum.WO_SHI_PM_TOP_SEARCH_EXCEPTION);
        }
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS,
                DataInfoCardEnum.WO_SHI_PM.getKey(),
                DataInfoCardEnum.WO_SHI_PM.getValue(),
                DataInfoCardEnum.WO_SHI_PM.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
    }

    @Override
    public ResultTemplateBean getYouSheWangSearch() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getYouSheWang(
                    ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.YOU_SHE_WANG.getValue(), ForestRequestHeaderOriginEnum.YOU_SHE_WANG.getValue())
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.YOU_SHE_WANG_TOP_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getYouSheWang(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.YOU_SHE_WANG.getKey(),
                    DataInfoCardEnum.YOU_SHE_WANG.getValue(),
                    DataInfoCardEnum.YOU_SHE_WANG.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);

        } catch (Exception e) {
            log.error("👺👺👺获取BBC失败👺👺👺：平台；{}", DataInfoCardEnum.YOU_SHE_WANG.getKey(), e);
            throw new ServerException(ServerExceptionEnum.YOU_SHE_WANG_TOP_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getZhanKuSearch(SearchTypeZhanKuEnum searchTypeZhanKuEnum) {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getZhanKu(
                    ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.ZHAN_KU.getValue(), ForestRequestHeaderOriginEnum.ZHAN_KU.getValue()),
                    searchTypeZhanKuEnum.getValue()
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.ZHAN_KU_TOP_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getZhanKu(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.ZHAN_KU.getKey(),
                    DataInfoCardEnum.ZHAN_KU.getValue(),
                    DataInfoCardEnum.ZHAN_KU.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);
        } catch (Exception e) {
            log.error("👺👺👺获取站酷失败👺👺👺：平台；{}", DataInfoCardEnum.ZHAN_KU.getKey(), e);
            throw new ServerException(ServerExceptionEnum.ZHAN_KU_TOP_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getZhanTuYaWangGuoSearch(SearchTypeTuYaWangGuoEnum searchTypeTuYaWangGuoEnum) {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getTuYaWangGuo(
                    ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.TU_YA_WANG_GUO.getValue(), ForestRequestHeaderOriginEnum.TU_YA_WANG_GUO.getValue()),
                    searchTypeTuYaWangGuoEnum.getValue()
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.TU_YA_WANG_GUO_TOP_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getTuYaWangGuo(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.TU_YA_WANG_GUO.getKey(),
                    DataInfoCardEnum.TU_YA_WANG_GUO.getValue(),
                    DataInfoCardEnum.TU_YA_WANG_GUO.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);
        } catch (Exception e) {
            log.error("👺👺👺获取涂鸦王国失败👺👺👺：平台；{}", DataInfoCardEnum.TU_YA_WANG_GUO.getKey(), e);
            throw new ServerException(ServerExceptionEnum.TU_YA_WANG_GUO_TOP_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getSheJiDaRenSearch() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getSheJiDaRen(
                    ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.SHE_JI_DA_REN.getValue(), ForestRequestHeaderOriginEnum.SHE_JI_DA_REN.getValue())
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.SHE_JI_DA_REN_TOP_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getSheJiDaRen(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.SHE_JI_DA_REN.getKey(),
                    DataInfoCardEnum.SHE_JI_DA_REN.getValue(),
                    DataInfoCardEnum.SHE_JI_DA_REN.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);

        } catch (Exception e) {
            log.error("👺👺👺获取BBC失败👺👺👺：平台；{}", DataInfoCardEnum.SHE_JI_DA_REN.getKey(), e);
            throw new ServerException(ServerExceptionEnum.SHE_JI_DA_REN_TOP_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getTopysSearch() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getTopys(
                    ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.TOPYS.getValue(), ForestRequestHeaderOriginEnum.TOPYS.getValue())
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.TOPYS_TOP_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getTopys(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.TOPYS.getKey(),
                    DataInfoCardEnum.TOPYS.getValue(),
                    DataInfoCardEnum.TOPYS.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);

        } catch (Exception e) {
            log.error("👺👺👺获取BBC失败👺👺👺：平台；{}", DataInfoCardEnum.TOPYS.getKey(), e);
            throw new ServerException(ServerExceptionEnum.TOPYS_TOP_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getArchDailySearch() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getArchDaily(
                    ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.ARCH_DAILY.getValue(), ForestRequestHeaderOriginEnum.ARCH_DAILY.getValue())
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.ARCH_DAILY_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getArchDaily(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.ARCH_DAILY.getKey(),
                    DataInfoCardEnum.ARCH_DAILY.getValue(),
                    DataInfoCardEnum.ARCH_DAILY.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);

        } catch (Exception e) {
            log.error("👺👺👺获取ArchDaily失败👺👺👺：平台；{}", DataInfoCardEnum.ARCH_DAILY.getKey(), e);
            throw new ServerException(ServerExceptionEnum.ARCH_DAILY_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getDribbbleSearch() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getDribbble(
                    ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.DRIBBBLE.getValue(), ForestRequestHeaderOriginEnum.DRIBBBLE.getValue())
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.DRIBBBLE_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getDribbble(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.DRIBBBLE.getKey(),
                    DataInfoCardEnum.DRIBBBLE.getValue(),
                    DataInfoCardEnum.DRIBBBLE.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);

        } catch (Exception e) {
            log.error("👺👺👺获取Dribbble失败👺👺👺：平台；{}", DataInfoCardEnum.DRIBBBLE.getKey(), e);
            throw new ServerException(ServerExceptionEnum.DRIBBBLE_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getAwwwardsSearch() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getAwwwards(
                    ForestUtil.getRandomRequestHeaderForAwwwards()
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.AWWWARDS_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getAwwwards(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.AWWWARDS.getKey(),
                    DataInfoCardEnum.AWWWARDS.getValue(),
                    DataInfoCardEnum.AWWWARDS.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);

        } catch (Exception e) {
            log.error("👺👺👺获取Awwwards失败👺👺👺：平台；{}", DataInfoCardEnum.AWWWARDS.getKey(), e);
            throw new ServerException(ServerExceptionEnum.AWWWARDS_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getCore77Search() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getCore77(
                    ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.CORE77.getValue(), ForestRequestHeaderOriginEnum.CORE77.getValue())
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.CORE77_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getCore77(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.CORE77.getKey(),
                    DataInfoCardEnum.CORE77.getValue(),
                    DataInfoCardEnum.CORE77.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);

        } catch (Exception e) {
            log.error("👺👺👺获取Core77失败👺👺👺：平台；{}", DataInfoCardEnum.CORE77.getKey(), e);
            throw new ServerException(ServerExceptionEnum.CORE77_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getAbduzeedoSearch() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getAbduzeedo(
                    ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.ABDUZEEDO.getValue(), ForestRequestHeaderOriginEnum.ABDUZEEDO.getValue())
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.ABDUZEEDO_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getAbduzeedo(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.ABDUZEEDO.getKey(),
                    DataInfoCardEnum.ABDUZEEDO.getValue(),
                    DataInfoCardEnum.ABDUZEEDO.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);

        } catch (Exception e) {
            log.error("👺👺👺获取Core77失败👺👺👺：平台；{}", DataInfoCardEnum.ABDUZEEDO.getKey(), e);
            throw new ServerException(ServerExceptionEnum.ABDUZEEDO_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getMITSearch() {
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchMITDTO topSearchMITDTO = topSearchCommonClient.getMIT(
                    ForestUtil.getRandomRequestHeaderForMIT());
            for (TopSearchMITDTO.ItemDTO content : topSearchMITDTO.getData().getItems()) {
                TopSearchCommonVO.DataInfo dataInfo = topSearchCommonMapper.topSearchMITDTOContentVO2TopSearchCommonVO(content);
                topSearchCommonVOS.add(dataInfo);
            }

        } catch (Exception e) {
            log.error("👺👺👺获取MIT失败👺👺👺：平台；{}", DataInfoCardEnum.MIT.getKey(), e);
            throw new ServerException(ServerExceptionEnum.MIT_SEARCH_EXCEPTION);
        }
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS,
                DataInfoCardEnum.MIT.getKey(),
                DataInfoCardEnum.MIT.getValue(),
                DataInfoCardEnum.MIT.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
    }

    @Override
    public ResultTemplateBean getZhongGuoKeXueYuanSearch() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getZhongGuoKeXueYuan(
                    ForestUtil.getRandomRequestHeaderForZhongGuoKeXueYuan()
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.ZHONG_GUO_KE_XUE_YUAN_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getZhongGuoKeXueYuan(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.ZHONG_GUO_KE_XUE_YUAN.getKey(),
                    DataInfoCardEnum.ZHONG_GUO_KE_XUE_YUAN.getValue(),
                    DataInfoCardEnum.ZHONG_GUO_KE_XUE_YUAN.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);

        } catch (Exception e) {
            log.error("👺👺👺获取中国科学院失败👺👺👺：平台；{}", DataInfoCardEnum.ZHONG_GUO_KE_XUE_YUAN.getKey(), e);
            throw new ServerException(ServerExceptionEnum.ZHONG_GUO_KE_XUE_YUAN_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getEurekAlertSearch() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getEurekAlert(
                    ForestUtil.getRandomRequestHeaderForEurekAlert()
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.EUREK_ALERT_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getEurekAlert(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.EUREK_ALERT.getKey(),
                    DataInfoCardEnum.EUREK_ALERT.getValue(),
                    DataInfoCardEnum.EUREK_ALERT.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);

        } catch (Exception e) {
            log.error("👺👺👺获取EurekAlert失败👺👺👺：平台；{}", DataInfoCardEnum.EUREK_ALERT.getKey(), e);
            throw new ServerException(ServerExceptionEnum.EUREK_ALERT_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getGuoJiKeJiChuangXinZhongXinSearch(SearchTypeGuoJiKeJiChuangXinZhongXinnum searchTypeGuoJiKeJiChuangXinZhongXinnum) {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getGuoJiKeJiChuangXinZhongXin(
                    ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.GUO_JI_KE_JI_CHUANG_XIN_ZHONG_XIN.getValue(), ForestRequestHeaderOriginEnum.GUO_JI_KE_JI_CHUANG_XIN_ZHONG_XIN.getValue()),
                    searchTypeGuoJiKeJiChuangXinZhongXinnum.getValue()
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.GUO_JI_KE_JI_CHUANG_XIN_ZHONG_XIN_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getGuoJiKeJiChuangXinZhongXin(content, searchTypeGuoJiKeJiChuangXinZhongXinnum);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.GUO_JI_KE_JI_CHUANG_XIN_ZHONG_XIN.getKey(),
                    DataInfoCardEnum.GUO_JI_KE_JI_CHUANG_XIN_ZHONG_XIN.getValue(),
                    DataInfoCardEnum.GUO_JI_KE_JI_CHUANG_XIN_ZHONG_XIN.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);
        } catch (Exception e) {
            log.error("👺👺👺获取国际科技创新中心失败👺👺👺：平台；{}", DataInfoCardEnum.GUO_JI_KE_JI_CHUANG_XIN_ZHONG_XIN.getKey(), e);
            throw new ServerException(ServerExceptionEnum.GUO_JI_KE_JI_CHUANG_XIN_ZHONG_XIN_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getJiQiZhiXinSearch() {
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchJiQiZhiXinDTO jiQiZhiXinDTO = topSearchCommonClient.getJiQiZhiXin(
                    ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.JI_QI_ZHI_XIN.getValue(), ForestRequestHeaderOriginEnum.JI_QI_ZHI_XIN.getValue()));
            for (TopSearchJiQiZhiXinDTO.DataInfo content : jiQiZhiXinDTO.getArticles()) {
                TopSearchCommonVO.DataInfo dataInfo = topSearchCommonMapper.topSearchJiQiZhiXinDTOContentVO2TopSearchCommonVO(content);
                topSearchCommonVOS.add(dataInfo);
            }

        } catch (Exception e) {
            log.error("👺👺👺获取机器之心失败👺👺👺：平台；{}", DataInfoCardEnum.JI_QI_ZHI_XIN.getKey(), e);
            throw new ServerException(ServerExceptionEnum.JI_QI_ZHI_XIN_SEARCH_EXCEPTION);
        }
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS,
                DataInfoCardEnum.JI_QI_ZHI_XIN.getKey(),
                DataInfoCardEnum.JI_QI_ZHI_XIN.getValue(),
                DataInfoCardEnum.JI_QI_ZHI_XIN.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
    }

    @Override
    public ResultTemplateBean getHuPuSearch() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getHuPu(
                    ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.HU_PU.getValue(), ForestRequestHeaderOriginEnum.HU_PU.getValue())
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.HU_PU_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getHuPu(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.HU_PU.getKey(),
                    DataInfoCardEnum.HU_PU.getValue(),
                    DataInfoCardEnum.HU_PU.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);
        } catch (Exception e) {
            log.error("👺👺👺获取虎扑失败👺👺👺：平台；{}", DataInfoCardEnum.HU_PU.getKey(), e);
            throw new ServerException(ServerExceptionEnum.HU_PU_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getDongQiuDiSearch() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getDongQiuDi(
                    ForestUtil.getRandomRequestHeaderForDongQiuDi()
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.DONG_QIU_DI_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getDongQiuDi(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.DONG_QIU_DI.getKey(),
                    DataInfoCardEnum.DONG_QIU_DI.getValue(),
                    DataInfoCardEnum.DONG_QIU_DI.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);
        } catch (Exception e) {
            log.error("👺👺👺获取懂球帝失败👺👺👺：平台；{}", DataInfoCardEnum.DONG_QIU_DI.getKey(), e);
            throw new ServerException(ServerExceptionEnum.DONG_QIU_DI_SEARCH_EXCEPTION);
        }

    }

    @Override
    public ResultTemplateBean getXinLangTiYuSearch() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getXinLangTiYu(
                    ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.XIN_LANG_TI_YU.getValue(), ForestRequestHeaderOriginEnum.XIN_LANG_TI_YU.getValue())
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.XIN_LANG_TI_YU_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getXinLangTiYu(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.XIN_LANG_TI_YU.getKey(),
                    DataInfoCardEnum.XIN_LANG_TI_YU.getValue(),
                    DataInfoCardEnum.XIN_LANG_TI_YU.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);
        } catch (Exception e) {
            log.error("👺👺👺获取新浪体育失败👺👺👺：平台；{}", DataInfoCardEnum.XIN_LANG_TI_YU.getKey(), e);
            throw new ServerException(ServerExceptionEnum.XIN_LANG_TI_YU_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getSouHuTiYuSearch() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getSouHuTiYu(
                    ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.SOU_HU_TI_YU.getValue(), ForestRequestHeaderOriginEnum.SOU_HU_TI_YU.getValue())
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.SOU_HU_TI_YU_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getSouHuTiYu(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.SOU_HU_TI_YU.getKey(),
                    DataInfoCardEnum.SOU_HU_TI_YU.getValue(),
                    DataInfoCardEnum.SOU_HU_TI_YU.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);
        } catch (Exception e) {
            log.error("👺👺👺获取搜狐体育失败👺👺👺：平台；{}", DataInfoCardEnum.SOU_HU_TI_YU.getKey(), e);
            throw new ServerException(ServerExceptionEnum.SOU_HU_TI_YU_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getWangYiTiYuSearch() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getWangYiTiYu(
                    ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.WANG_YI_TI_YU.getValue(), ForestRequestHeaderOriginEnum.WANG_YI_TI_YU.getValue())
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.WANG_YI_TI_YU_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getWangYiTiYu(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.WANG_YI_TI_YU.getKey(),
                    DataInfoCardEnum.WANG_YI_TI_YU.getValue(),
                    DataInfoCardEnum.WANG_YI_TI_YU.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);
        } catch (Exception e) {
            log.error("👺👺👺获取网易体育失败👺👺👺：平台；{}", DataInfoCardEnum.WANG_YI_TI_YU.getKey(), e);
            throw new ServerException(ServerExceptionEnum.WANG_YI_TI_YU_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getYangShiTiYuSearch() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getYangShiTiYu(
                    ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.YANG_SHI_TI_YU.getValue(), ForestRequestHeaderOriginEnum.YANG_SHI_TI_YU.getValue())
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.YANG_SHI_TI_YU_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getYangShiTiYu(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.YANG_SHI_TI_YU.getKey(),
                    DataInfoCardEnum.YANG_SHI_TI_YU.getValue(),
                    DataInfoCardEnum.YANG_SHI_TI_YU.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);
        } catch (Exception e) {
            log.error("👺👺👺获取央视体育失败👺👺👺：平台；{}", DataInfoCardEnum.YANG_SHI_TI_YU.getKey(), e);
            throw new ServerException(ServerExceptionEnum.YANG_SHI_TI_YU_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getPPTiYuSearch() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getPPTiYu(
                    ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.PP_TI_YU.getValue(), ForestRequestHeaderOriginEnum.PP_TI_YU.getValue())
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.PP_TI_YU_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getPPTiYu(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.PP_TI_YU.getKey(),
                    DataInfoCardEnum.PP_TI_YU.getValue(),
                    DataInfoCardEnum.PP_TI_YU.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);
        } catch (Exception e) {
            log.error("👺👺👺获取PP体育失败👺👺👺：平台；{}", DataInfoCardEnum.PP_TI_YU.getKey(), e);
            throw new ServerException(ServerExceptionEnum.PP_TI_YU_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getZhiBoBaSearch() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getZhiBoBa(
                    ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.ZHI_BO_BA.getValue(), ForestRequestHeaderOriginEnum.ZHI_BO_BA.getValue())
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.ZHI_BO_BA_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getZhiBoBa(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.ZHI_BO_BA.getKey(),
                    DataInfoCardEnum.ZHI_BO_BA.getValue(),
                    DataInfoCardEnum.ZHI_BO_BA.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);
        } catch (Exception e) {
            log.error("👺👺👺获取直播吧失败👺👺👺：平台；{}", DataInfoCardEnum.ZHI_BO_BA.getKey(), e);
            throw new ServerException(ServerExceptionEnum.ZHI_BO_BA_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getV2exSearch() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getV2EX(
                    ForestUtil.getRandomRequestHeaderForV2EX()
                    //ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.V2EX.getValue(), ForestRequestHeaderOriginEnum.V2EX.getValue())
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.V2EX_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getV2EX(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.V2EX.getKey(),
                    DataInfoCardEnum.V2EX.getValue(),
                    DataInfoCardEnum.V2EX.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);
        } catch (Exception e) {
            log.error("👺👺👺获取V2EX失败👺👺👺：平台；{}", DataInfoCardEnum.V2EX.getKey(), e);
            throw new ServerException(ServerExceptionEnum.V2EX_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getBuXingJieHuPuSearch() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getBuXingJieHuPu(
                    //ForestUtil.getRandomRequestHeaderForV2EX()
                    ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.BU_XING_JIE_HU_PU.getValue(), ForestRequestHeaderOriginEnum.BU_XING_JIE_HU_PU.getValue())
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.BU_XING_JIE_HU_PU_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getBuXingJieHuPu(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.BU_XING_JIE_HU_PU.getKey(),
                    DataInfoCardEnum.BU_XING_JIE_HU_PU.getValue(),
                    DataInfoCardEnum.BU_XING_JIE_HU_PU.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);
        } catch (Exception e) {
            log.error("👺👺👺获取步行街虎扑失败👺👺👺：平台；{}", DataInfoCardEnum.BU_XING_JIE_HU_PU.getKey(), e);
            throw new ServerException(ServerExceptionEnum.BU_XING_JIE_HU_PU_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getNgaPuSearch() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getNga(
                    //ForestUtil.getRandomRequestHeaderForV2EX()
                    ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.NGA.getValue(), ForestRequestHeaderOriginEnum.NGA.getValue())
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.NGA_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getNGA(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.NGA.getKey(),
                    DataInfoCardEnum.NGA.getValue(),
                    DataInfoCardEnum.NGA.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);
        } catch (Exception e) {
            log.error("👺👺👺获取NGA失败👺👺👺：平台；{}", DataInfoCardEnum.NGA.getKey(), e);
            throw new ServerException(ServerExceptionEnum.NGA_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getYiMuSanFenDiSearch() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getYiMuSanFenDi(
                    ForestUtil.getRandomRequestHeaderForYiMuSanFenDi()
                    //ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.YI_MU_SAN_FEN_DI.getValue(), ForestRequestHeaderOriginEnum.YI_MU_SAN_FEN_DI.getValue())
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.YI_MU_SAN_FEN_DI_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getYiMuSanFenDi(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.YI_MU_SAN_FEN_DI.getKey(),
                    DataInfoCardEnum.YI_MU_SAN_FEN_DI.getValue(),
                    DataInfoCardEnum.YI_MU_SAN_FEN_DI.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);
        } catch (Exception e) {
            log.error("👺👺👺获取一亩三分地失败👺👺👺：平台；{}", DataInfoCardEnum.YI_MU_SAN_FEN_DI.getKey(), e);
            throw new ServerException(ServerExceptionEnum.YI_MU_SAN_FEN_DI_SEARCH_EXCEPTION);
        }
    }

    @Override
    public ResultTemplateBean getWenZhangJueJinSearch() {
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            TopSearchWenZhangJueJinDTO wenZhangJueJin = topSearchCommonClient.getWenZhangJueJin(
                    ForestUtil.getRandomRequestHeaderForWenZhangJueJin());
            for (TopSearchWenZhangJueJinDTO.DataInfo content : wenZhangJueJin.getData()) {
                TopSearchCommonVO.DataInfo dataInfo = topSearchCommonMapper.topSearchWenZhangJueJinDTOContentVO2TopSearchCommonVO(content.getContent(),content.getContentCounter());
                topSearchCommonVOS.add(dataInfo);
            }

        } catch (Exception e) {
            log.error("👺👺👺获取掘金文章失败👺👺👺：平台；{}", DataInfoCardEnum.WEN_ZHANG_JUE_JIN.getKey(), e);
            throw new ServerException(ServerExceptionEnum.JUE_JIN_WEN_ZHANG_SEARCH_EXCEPTION);
        }
        TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(topSearchCommonVOS,
                DataInfoCardEnum.WEN_ZHANG_JUE_JIN.getKey(),
                DataInfoCardEnum.WEN_ZHANG_JUE_JIN.getValue(),
                DataInfoCardEnum.WEN_ZHANG_JUE_JIN.getDescription());
        return ResultTemplateBean.success(topSearchCommonVO);
    }

    @Override
    public ResultTemplateBean getHackerNewsSearch() {
        try {
            ForestResponse forestResponse = topSearchCommonClient.getHackerNews(
                    //ForestUtil.getRandomRequestHeaderForYiMuSanFenDi()
                    ForestUtil.getRandomRequestHeader(ForestRequestHeaderRefererEnum.HACKER_NEWS.getValue(), ForestRequestHeaderOriginEnum.HACKER_NEWS.getValue())
            );

            String content = forestResponse.getContent();
            if (StrUtil.isBlank(content)) {
                throw new ServerException(ServerExceptionEnum.HACKER_NEWS_SEARCH_EXCEPTION);
            }

            List<TopSearchCommonVO.DataInfo> topSearchCommonVOS;
            topSearchCommonVOS = CommonJsoupJsoupParseUtil.getHackerNews(content);

            TopSearchCommonVO topSearchCommonVO = new TopSearchCommonVO(
                    topSearchCommonVOS,
                    DataInfoCardEnum.HACKER_NEWS.getKey(),
                    DataInfoCardEnum.HACKER_NEWS.getValue(),
                    DataInfoCardEnum.HACKER_NEWS.getDescription()
            );

            return ResultTemplateBean.success(topSearchCommonVO);
        } catch (Exception e) {
            log.error("👺👺👺获取HACKER_NEWS失败👺👺👺：平台；{}", DataInfoCardEnum.HACKER_NEWS.getKey(), e);
            throw new ServerException(ServerExceptionEnum.HACKER_NEWS_SEARCH_EXCEPTION);
        }
    }
}