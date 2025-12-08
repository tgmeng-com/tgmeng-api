package com.tgmeng.common.forest.client.umami;

import com.dtflys.forest.annotation.Header;
import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.annotation.Var;
import com.dtflys.forest.http.ForestResponse;
import com.tgmeng.common.bean.UmamiPostDataBean;
import com.tgmeng.common.forest.header.ForestRequestHeader;

//加这个只是为了不爆红
public interface IUmamiClient {

    // 重要
    @Post("{url}")
    ForestResponse sendEvent(@Header ForestRequestHeader topSearchRequestHeader, @Var("url") String url, @JSONBody UmamiPostDataBean umamiPostDataBean);

}