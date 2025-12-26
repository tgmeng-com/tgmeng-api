package com.tgmeng.common.bean;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class MachineState {

    private int minuteCount;
    private LocalDateTime minuteWindow;

    private int dayCount;
    private LocalDate day;

    private LocalDateTime lastAccess;
}
