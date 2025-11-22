package com.tgmeng.common.util;

import com.tgmeng.common.cache.TopSearchDataCache;
import com.tgmeng.common.enums.business.CacheDataNameEnum;
import com.tgmeng.common.schedule.ControllerApiSchedule;
import com.tgmeng.model.vo.topsearch.TopSearchCommonVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * description: 这个类用来存储代理信息，后续直接放在数据库就行(穷，没有数据库)
 * package: com.tgmeng.common.util
 * className: ProxyPoGenerateUtil
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/7/1 12:54
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CacheUtil {

    private final ControllerApiSchedule controllerApiSchedule;

    private final TopSearchDataCache topSearchDataCache;

    /**
     * description: 刷新缓存，这个是缓存存在的不管，不在的就去请求，为的是保证数据基本都在缓存里有
     * method: refreshCache
     *
     * @author tgmeng
     * @since 2025/11/20 22:17
     */

    public void refreshCache() {
        controllerApiSchedule.scanAndInvokeControllers();
    }

    public List<TopSearchCommonVO> getAllCache() {
        //排除掉词云和ai时报这两个对真正热点数据无关的缓存
        Set<CacheDataNameEnum> excludeEnums = Set.of(
                CacheDataNameEnum.WORD_CLOUD,
                CacheDataNameEnum.REALTIME_SUMMARY
        );

        return topSearchDataCache.getAll(TopSearchCommonVO.class, excludeEnums);
    }

    public List<String> getAllCacheTitle(List<TopSearchCommonVO> allCacheSearchData) {
        return allCacheSearchData.stream()
                .filter(vo -> vo.getDataInfo() != null)
                .flatMap(vo -> vo.getDataInfo().stream())
                .map(TopSearchCommonVO.DataInfo::getKeyword)
                .filter(k -> k != null && !k.trim().isEmpty())
                .collect(Collectors.toList());
    }


}
