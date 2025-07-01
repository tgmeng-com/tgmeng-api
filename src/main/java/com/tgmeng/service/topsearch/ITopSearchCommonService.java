package com.tgmeng.service.topsearch;

import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.model.vo.topsearch.TopSearchCommonVO;

import java.util.List;


public interface ITopSearchCommonService {
    ResultTemplateBean getBaiDuTopSearch();

    List<TopSearchCommonVO> getBilibiliTopSearch();

    List<TopSearchCommonVO> getWeiBoTopSearch();

    List<TopSearchCommonVO> getDouYinTopSearch();
}
