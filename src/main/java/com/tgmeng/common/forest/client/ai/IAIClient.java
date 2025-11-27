package com.tgmeng.common.forest.client.ai;

import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.annotation.Var;
import com.dtflys.forest.http.ForestResponse;
import com.tgmeng.model.dto.ai.request.AICommonChatModelRequestDTO;

public interface IAIClient {
    @Post(
            url = "{url}",
            connectTimeout = 300000, //调用大模型超时时间设置为5分钟
            readTimeout = 300000,
            headers = {
                    "Content-Type: application/json",
                    "Authorization: Bearer {key}"
            })
    ForestResponse getAIMessage(@Var("url") String url, @Var("key") String key, @JSONBody AICommonChatModelRequestDTO aiCommonDTO);
}




