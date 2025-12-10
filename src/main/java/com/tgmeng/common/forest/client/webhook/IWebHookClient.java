package com.tgmeng.common.forest.client.webhook;

import com.dtflys.forest.annotation.Body;
import com.dtflys.forest.annotation.Header;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.annotation.Var;
import com.dtflys.forest.http.ForestResponse;
import com.tgmeng.common.forest.header.ForestRequestHeader;

/**
 * description: 这个用来做系统内部的一些调用，比如定时任务等等
 * package: com.tgmeng.common.forest.client.system
 * className: ISystemClient
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/7/3 13:37
 */
public interface IWebHookClient {
    @Post(
            url = "{url}")
    ForestResponse sendMessage(@Header ForestRequestHeader topSearchRequestHeader, @Var("url") String url, @Body String jsonBody);
}
