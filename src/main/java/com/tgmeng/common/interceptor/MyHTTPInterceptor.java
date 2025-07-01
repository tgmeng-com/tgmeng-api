package com.tgmeng.common.interceptor;

import com.dtflys.forest.http.ForestProxy;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.interceptor.Interceptor;
import com.tgmeng.common.enums.business.ProxyTypeEnum;
import com.tgmeng.common.util.ProxyUtil;
import com.tgmeng.model.po.topsearch.ProxyPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * description: 全局请求代理拦截器，判断开启了全局代理的话，就会使用代理请求(正向代理)
 * 这个也就是所有forest的请求都会适用
 * package: com.tgmeng.common.interceptor
 * className: MyHTTPInterceptor
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/7/1 13:25
*/
@Slf4j
@Component
public class MyHTTPInterceptor implements Interceptor {

    /** 全局代理是否开启，查不到这条配置就默认关闭 */
    @Value("${my-config.proxy.top-search.enabled:false}")
    private Boolean proxyEnabled;

    /**
     * description: 请求开始前，判断并进行代理设置
     * method: beforeExecute
     *
     * @author tgmeng
     * @since 2025/7/1 13:42
    */
    @Override
    public boolean beforeExecute(ForestRequest req) {
        //判断是否开启了全局代理
        if (proxyEnabled){
            //获取启用所有启用的代理
            List<ProxyPO> proxyEnabledList = ProxyUtil.getProxyEnabledList();
            if (proxyEnabledList.isEmpty()) {
                log.info("没有启用http请求代理");
                return true;
            }
            //随机取其中一个代理
            ProxyPO proxyEnabledRandomChoose = proxyEnabledList.get(ThreadLocalRandom.current().nextInt(proxyEnabledList.size()));
            log.info("启用了http请求代理，本次请求选择：{}", proxyEnabledRandomChoose.toString());
            //判断代理类型是HTTP或者SOCKS
            ProxyTypeEnum proxyType = proxyEnabledRandomChoose.getProxyType();
            switch (proxyType) {
                case HTTP:
                    req.proxy(ForestProxy.http(proxyEnabledRandomChoose.getProxyHost(), proxyEnabledRandomChoose.getProxyPort())
                            .username(proxyEnabledRandomChoose.getProxyUser())
                            .password(proxyEnabledRandomChoose.getProxyPassword()));
                    break;
                case SOCKS:
                    req.proxy(ForestProxy.socks(proxyEnabledRandomChoose.getProxyHost(), proxyEnabledRandomChoose.getProxyPort())
                            .username(proxyEnabledRandomChoose.getProxyUser())
                            .password(proxyEnabledRandomChoose.getProxyPassword()));
                    break;
                default:
                    //不是HTTP也不是SOCKS，这里先忽略
                    return true;
            }
        }
        return true;
    }
}
