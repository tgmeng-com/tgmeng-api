package com.tgmeng.model.dto.topsearch;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * description: 豆瓣热搜官方返回的DTO
 * package: com.tgmeng.model.dto.topsearch
 * className: TopSearchDouBanDTO
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/7/2 14:12
*/
@Data
@Accessors(chain = true)
public class TopSearchDouBanDTO {
        private String name;
        private String score;
        private String uri;
        @JSONField(name = "trend_flag")
        private String trendFlag;
}