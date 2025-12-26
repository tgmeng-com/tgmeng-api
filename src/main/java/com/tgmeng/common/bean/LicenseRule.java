package com.tgmeng.common.bean;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LicenseRule {

    /** 过期时间 */
    private LocalDateTime expireAt;

    /** 最大机器数 */
    private int maxMachines;

    /** 每分钟最大次数 */
    private int minuteLimit;

    /** 每天最大次数 */
    private int dayLimit;
}
