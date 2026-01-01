package com.tgmeng.model.po.topsearch;

import com.tgmeng.common.enums.business.ProxyTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProxyConfig {

    /** proxy的用户名 */
    private String user;
    /** proxy的密码 */
    private String password;
    /** proxy的host */
    private String host;
    /** proxy的Port */
    private Integer port;
    /** proxy的类型，分http和sock */
    private ProxyTypeEnum type;
    /** 在全局开启代理的情况下，这里控制启用哪一条代理 */
    private Boolean enabled;
}
