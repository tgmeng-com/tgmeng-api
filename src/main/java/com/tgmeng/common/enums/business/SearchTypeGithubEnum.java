package com.tgmeng.common.enums.business;

import com.tgmeng.common.enums.enumcommon.INameValueEnum;
import com.tgmeng.common.util.TimeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * description: 这个优酷排行榜url后缀路由用的
 *  其中value用来路由
 *  description用来日志打印，同时他也是返回前端的card框的分类名称。但是由于card现在是在前端弄的，没用后端的，所以这里目前只用来打印日志
 * package: com.tgmeng.common.Enum
 * className: ForestRequestHeaderOriginEnum
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/6/29 18:17
 */
@Getter
@AllArgsConstructor
public enum SearchTypeGithubEnum implements INameValueEnum<String, String> {
    ALL("all", "2007-10-01", "GitHub Star总榜", true, 2),
    DAY("day", TimeUtil.getTimeBeforeNow(0, 0, 0, 1, TimeUtil.defultSimplePattern), "近一日新仓库Star榜", true, 2),
    WEEK("week", TimeUtil.getTimeBeforeNow(0, 0, 1, 0, TimeUtil.defultSimplePattern), "近一周新仓库Star榜", true, 2),
    MONTH("month", TimeUtil.getTimeBeforeNow(0, 1, 0, 0, TimeUtil.defultSimplePattern), "近一月新仓库Star榜", true, 2),
    YEAR("year", TimeUtil.getTimeBeforeNow(1, 0, 0, 0, TimeUtil.defultSimplePattern), "近一年新仓库Star榜", true, 2),
    THREEYEAR("threeyear", TimeUtil.getTimeBeforeNow(3, 0, 0, 0, TimeUtil.defultSimplePattern), "近三年新仓库Star榜", true, 2),
    FIVEYEAR("fiveyear", TimeUtil.getTimeBeforeNow(5, 0, 0, 0, TimeUtil.defultSimplePattern), "近五年新仓库Star榜", true, 2),
    TENYEAR("tenyear", TimeUtil.getTimeBeforeNow(10, 0, 0, 0, TimeUtil.defultSimplePattern), "近十年新仓库Star榜", true, 2);


    private final String key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
