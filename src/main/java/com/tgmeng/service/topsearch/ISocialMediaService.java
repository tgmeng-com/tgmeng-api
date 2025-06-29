package com.tgmeng.service.topsearch;

import com.tgmeng.model.vo.topsearch.TopSearchCommonVO;

import java.util.List;


public interface ISocialMediaService {
    List<TopSearchCommonVO> getBaiDuTopSearch();

    List<TopSearchCommonVO> getBilibiliTopSearch();

    List<TopSearchCommonVO> getWeiBoTopSearch();

    List<TopSearchCommonVO> getDouYinTopSearch();
}
