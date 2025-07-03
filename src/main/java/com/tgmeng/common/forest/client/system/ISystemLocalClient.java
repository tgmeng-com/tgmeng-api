package com.tgmeng.common.forest.client.system;

import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.Var;
import com.tgmeng.model.dto.topsearch.TopSearchWeiBoDTO;

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
    @Get("http://localhost:4399{url}")
    TopSearchWeiBoDTO systemLocalClient(@Var("url") String url);
}
