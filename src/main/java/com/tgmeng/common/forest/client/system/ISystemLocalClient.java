package com.tgmeng.common.forest.client.system;

import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.Header;
import com.dtflys.forest.annotation.Var;
import com.tgmeng.common.bean.ResultTemplateBean;

/**
 * description: 这个用来做系统内部的一些调用，比如定时任务等等
 * package: com.tgmeng.common.forest.client.system
 * className: ISystemClient
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/7/3 13:37
*/
public interface ISystemLocalClient {
    @Get(
            url = "http://127.0.0.1:4399{url}",
            connectTimeout = 300000, //
            readTimeout = 300000
    )
    ResultTemplateBean systemLocalClient(@Header("X-Source") String contentType, @Var("url") String url);
}
