package com.tgmeng.common.util;

import com.tgmeng.common.enums.business.ProxyTypeEnum;
import com.tgmeng.model.po.topsearch.ProxyPO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * description: 这个类用来存储代理信息，后续直接放在数据库就行(穷，没有数据库)
 * package: com.tgmeng.common.util
 * className: ProxyPoGenerateUtil
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/7/1 12:54
 */
public class ProxyUtil {

    /**
     * description: 获取系统里配置的所有代理，不管启用没启用的
     * method: getProxyAll
     *
     * @author tgmeng
     * @since 2025/7/1 13:08
     */
    public static List<ProxyPO> getProxyAll() {
        List<ProxyPO> proxyPOS = new ArrayList<>();
        proxyPOS.add(new ProxyPO()
                .setProxyUser("1")
                .setProxyPassword("2")
                .setProxyHost("3")
                .setProxyPort(8080)
                .setProxyType(ProxyTypeEnum.HTTP)
                .setEnabled(Boolean.TRUE));
        proxyPOS.add(new ProxyPO()
                .setProxyUser("33")
                .setProxyPassword("234")
                .setProxyHost("345")
                .setProxyType(ProxyTypeEnum.SOCKS)
                .setProxyPort(8080)
                .setEnabled(Boolean.FALSE));
        return proxyPOS;
    }

    /**
     * description: 找到所有启用的代理
     * method: getProxyEnabled
     *
     * @author tgmeng
     * @since 2025/7/1 13:59
    */
    public static List<ProxyPO> getProxyEnabledList() {
        return getProxyAll().stream()
                .filter(ProxyPO::getEnabled)
                .collect(Collectors.toList());
    }

    /**
     * description: 获取系统配置的代理中的启用的第一条代理
     * method: getProxyEnable
     *
     * @author tgmeng
     * @since 2025/7/1 13:23
     */
    public static ProxyPO getProxyEnabledFirst() {
        return getProxyAll().stream()
                .filter(ProxyPO::getEnabled)
                .findFirst()
                .orElse(null);
    }
}
