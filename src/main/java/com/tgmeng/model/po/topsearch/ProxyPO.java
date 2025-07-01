package com.tgmeng.model.po.topsearch;

import com.tgmeng.common.enums.business.ProxyTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProxyPO {

    /** proxy的用户名 */
    private String proxyUser;
    /** proxy的密码 */
    private String proxyPassword;
    /** proxy的host */
    private String proxyHost;
    /** proxy的Port */
    private Integer proxyPort;
    /** proxy的类型，分http和sock */
    private ProxyTypeEnum proxyType;
    /** 在全局开启代理的情况下，这里控制启用哪一条代理 */
    private Boolean enabled;
}
