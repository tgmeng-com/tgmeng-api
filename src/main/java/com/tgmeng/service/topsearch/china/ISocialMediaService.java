package com.tgmeng.service.topsearch.china;

import com.tgmeng.model.vo.topsearch.TopSearchVO;

import java.util.List;


public interface ISocialMediaService {
    List<TopSearchVO> getBaiDuTopSearch();

    List<TopSearchVO> getBilibiliTopSearch();

    List<TopSearchVO> getWeiBoTopSearch();

    List<TopSearchVO> getDouYinTopSearch();
}
