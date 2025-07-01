package com.tgmeng.service.topsearch;

import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.enums.business.DataInfoCardEnum;

public interface ITopSearchGitHubService {
    ResultTemplateBean getGithubSortByAllStars(String time, DataInfoCardEnum dataInfoCardEnum);
}
