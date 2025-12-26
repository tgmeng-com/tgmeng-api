package com.tgmeng.common.bean;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class LicenseState {

    /** machineId -> 状态 */
    private Map<String, MachineState> machines = new HashMap<>();
}
