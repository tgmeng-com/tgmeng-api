package com.tgmeng.common.util;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.UtilityClass;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

@UtilityClass
public class TimeUtil {
    /* 全局时区设置，系统默认时区 ZoneId.systemDefault()，指定时区 ZoneId.of("America/St_Johns") */
    public final ZoneId defaultZoneId = ZoneId.systemDefault();
    /* 默认格式化格式，所有返回格式化字符串的方法，如果传了null或者空串，则使用这个 */
    public final String defaultPattern = "yyyy-MM-dd HH:mm:ss";
    public final String defultSimplePattern = "yyyy-MM-dd";

    public final int ONE_MILLISECOND = 1;
    public final int ONE_SECOND_MILLISECOND = 1000 * ONE_MILLISECOND;
    public final int ONE_MINUTE_MILLISECOND = 60 * ONE_SECOND_MILLISECOND;
    public final int ONE_HOUR_MILLISECOND = 60 * ONE_MINUTE_MILLISECOND;
    public final int ONE_DAY_MILLISECOND = 24 * ONE_HOUR_MILLISECOND;
    public final String[] EN_MONTH = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    public final String[] ZH_MONTH = new String[]{"一月份", "二月份", "三月份", "四月份", "五月份", "六月份", "七月份", "八月份", "九月份", "十月份", "十一月份", "十二月份"};
    public final String[] EN_WEEK = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    public final String[] ZH_WEEK = new String[]{"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
    public final String[] ZH_WEEK_ABBREVIATION = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    public final String ZH = "ZH", EN = "EN";

    /**
     * @desc 获取当前年 2020
     * @return int
     * @date 2022/1/11 20:36
     * @author xiaoma
     */
    public int getCurrentTimeYear() {
        return LocalDateTime.now().atZone(defaultZoneId).getYear();
    }

    /**
     * @desc 获取当前月 7
     * @return int
     *
     * @date 2022/1/11 20:37
     * @author xiaoma
     */
    public int getCurrentTimeMonth() {
        return LocalDateTime.now().atZone(defaultZoneId).getMonthValue();
    }

    /**
     * @desc 获取当前日是星期几  3
     * @return int
     *
     * @date 2022/1/11 10:40
     * @author xiaoma
     */
    public int getCurrentTimeWeek() {
        return LocalDateTime.now().atZone(defaultZoneId).getDayOfWeek().getValue();
    }

    /**
     * @desc 获取当前日 21
     * @return int
     *
     * @date 2022/1/11 20:37
     * @author xiaoma
     */
    public int getCurrentTimeDay() {
        return LocalDateTime.now().atZone(defaultZoneId).getDayOfMonth();
    }

    /**
     * @desc 获取当前时 20
     * @return int
     *
     * @date 2022/1/11 20:37
     * @author xiaoma
     */
    public int getCurrentTimeHour() {
        return LocalDateTime.now().atZone(defaultZoneId).getHour();
    }

    /**
     * @desc 获取当前分 37
     * @return int
     *
     * @date 2022/1/11 20:37
     * @author xiaoma
     */
    public int getCurrentTimeMinute() {
        return LocalDateTime.now().atZone(defaultZoneId).getMinute();
    }

    /**
     * @desc 获取当前秒 22
     * @return int
     *
     * @date 2022/1/11 20:38
     * @author xiaoma
     */
    public int getCurrentTimeSecond() {
        return LocalDateTime.now().atZone(defaultZoneId).getSecond();
    }

    /**
     * @desc 获取当前毫秒值 1595335042000 (2020-07-21 20:38:00)
     * @return long
     *
     * @date 2022/1/11 20:35
     * @author xiaoma
     */
    public long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * @desc 获取当前毫秒值之后多少毫秒值的 毫秒值
     * @param millis 1 毫秒值
     * @return long
     *
     * @date 2022/1/11 19:29
     * @author xiaoma
     */
    public long getCurrentTimePlusTime(long millis) {
        return System.currentTimeMillis() + millis;
    }

    /**
     * @desc 获取当前毫秒值之后多长时间的 毫秒值
     * @param year 1 年
     * @param month 2 月
     * @param day 3 日
     * @param hour 4 时
     * @param minute 5 分
     * @param second 6 秒
     * @return long
     *
     * @date 2022/1/11 19:29
     * @author xiaoma
     */
    public long getCurrentTimePlusTime(int year, int month, int day, int hour, int minute, int second) {
        return LocalDateTime.now().atZone(defaultZoneId).plusYears(year).plusMonths(month).plusDays(day).plusHours(hour).plusMinutes(minute).plusSeconds(second).toInstant().toEpochMilli();
    }

    /**
     * @desc 获取当前日期 格式化 2020-07-22 00:28:17
     * @return java.lang.String
     *
     * @date 2022/1/11 0:31
     * @author xiaoma
     */
    public String getCurrentTimeFormat(String pattern) {
        return LocalDateTime.now().atZone(defaultZoneId).format(DateTimeFormatter.ofPattern(StrUtil.isEmpty(pattern) ? defaultPattern : pattern));
    }

    /**
     * @desc 获取当前日期之后多少毫秒值的 格式化
     * @param millis 1 毫秒值
     * @return java.lang.String
     *
     * @date 2022/1/11 19:32
     * @author xiaoma
     */
    public String getCurrentTimePlusTimeFormat(long millis, String pattern) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis() + millis), defaultZoneId).format(DateTimeFormatter.ofPattern(StrUtil.isEmpty(pattern) ? defaultPattern : pattern));
    }

    /**
     * @desc 获取当前日期之后多长时间的 格式化
     * @param year 1 年
     * @param month 2 月
     * @param day 3 日
     * @param hour 4 时
     * @param minute 5 分
     * @param second 6 秒
     * @return java.lang.String
     *
     * @date 2022/1/11 19:32
     * @author xiaoma
     */
    public String getCurrentTimePlusTimeFormat(int year, int month, int day, int hour, int minute, int second, String pattern) {
        return LocalDateTime.now().atZone(defaultZoneId).plusYears(year).plusMonths(month).plusDays(day).plusHours(hour).plusMinutes(minute).plusSeconds(second).format(DateTimeFormatter.ofPattern(StrUtil.isEmpty(pattern) ? defaultPattern : pattern));
    }

    /* ************************************************************************************************************************ */

    /**
     * @desc 获取指定毫秒值的 年 1995
     * @param timeMillis 1 805651200000
     * @return int
     *
     * @date 2022/1/11 1:43
     * @author xiaoma
     */
    public int getTargetTimeYear(long timeMillis) {
        return Integer.valueOf(LocalDateTime.ofInstant(Instant.ofEpochMilli(timeMillis), defaultZoneId).format(DateTimeFormatter.ofPattern("yyyy")));
    }

    /**
     * @desc 获取指定毫秒值的 月 7
     * @param timeMillis 1 805651200000
     * @return int
     *
     * @date 2022/1/11 1:47
     * @author xiaoma
     */
    public int getTargetTimeMonth(long timeMillis) {
        return Integer.valueOf(LocalDateTime.ofInstant(Instant.ofEpochMilli(timeMillis), defaultZoneId).format(DateTimeFormatter.ofPattern("MM")));
    }

    /**
     * @desc 获取指定毫秒值是 星期几 7
     * @param  timeMillis 1
     * @return int
     *
     * @date 2022/1/11 10:50
     * @author xiaoma
     */
    public int getTargetTimeWeek(long timeMillis) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timeMillis), defaultZoneId).getDayOfWeek().getValue();
    }

    /**
     * @desc 获取指定日期是 星期几 7
     * @param year 1
     * @param month 2
     * @param day 3
     * @param hour 4
     * @param minute 5
     * @param second 6
     * @return int
     *
     * @date 2022/1/11 11:01
     * @author xiaoma
     */
    public int getTargetTimeWeek(int year, int month, int day, int hour, int minute, int second) {
        return LocalDateTime.of(year, month, day, hour, minute, second).atZone(defaultZoneId).getDayOfWeek().getValue();
    }

    /**
     * @desc 获取指定毫秒值的 日 14
     * @param timeMillis 1 805651200000
     * @return int
     *
     * @date 2022/1/11 1:47
     * @author xiaoma
     */
    public int getTargetTimeDay(long timeMillis) {
        return Integer.valueOf(LocalDateTime.ofInstant(Instant.ofEpochMilli(timeMillis), defaultZoneId).format(DateTimeFormatter.ofPattern("dd")));
    }

    /**
     * @desc 获取指定毫秒值的 时 0
     * @param timeMillis 1 805651200000
     * @return int
     *
     * @date 2022/1/11 1:47
     * @author xiaoma
     */
    public int getTargetTimeHour(long timeMillis) {
        return Integer.valueOf(LocalDateTime.ofInstant(Instant.ofEpochMilli(timeMillis), defaultZoneId).format(DateTimeFormatter.ofPattern("HH")));
    }

    /**
     * @desc 获取指定毫秒值的 分 0
     * @param timeMillis 1 805651200000
     * @return int
     *
     * @date 2022/1/11 1:47
     * @author xiaoma
     */
    public int getTargetTimeMinute(long timeMillis) {
        return Integer.valueOf(LocalDateTime.ofInstant(Instant.ofEpochMilli(timeMillis), defaultZoneId).format(DateTimeFormatter.ofPattern("mm")));
    }

    /**
     * @desc 获取指定毫秒值的 秒 0
     * @param timeMillis 1 805651200000
     * @return int
     *
     * @date 2022/1/11 1:47
     * @author xiaoma
     */
    public int getTargetTimeSecond(long timeMillis) {
        return Integer.valueOf(LocalDateTime.ofInstant(Instant.ofEpochMilli(timeMillis), defaultZoneId).format(DateTimeFormatter.ofPattern("ss")));
    }

    /**
     * @desc 获取指定日期的 毫秒值 805651200000
     * @param year 1  1995
     * @param month 2 7
     * @param day 3 14
     * @param hour 4 0
     * @param minute 5 0
     * @param second 6 0
     * @return long
     *
     * @date 2022/1/11 1:17
     * @author xiaoma
     */
    public long getTargetTimeMillis(int year, int month, int day, int hour, int minute, int second) {
        return LocalDateTime.of(year, month, day, hour, minute, second).atZone(defaultZoneId).toInstant().toEpochMilli();
    }

    /**
     * @desc 获取指定日期之后多少毫秒值的 毫秒值 805651200000
     * @param year 1  1995
     * @param month 2 7
     * @param day 3 14
     * @param hour 4 0
     * @param minute 5 0
     * @param second 6 0
     * @return long
     *
     * @date 2022/1/11 1:17
     * @author xiaoma
     */
    public long getTargetTimePlusTime(int year, int month, int day, int hour, int minute, int second, long millis) {
        return LocalDateTime.of(year, month, day, hour, minute, second).atZone(defaultZoneId).toInstant().toEpochMilli() + millis;
    }

    /**
     * @desc 获取指定日期之后多少时间的 毫秒值 805651200000
     * @param year 1  1995
     * @param month 2 7
     * @param day 3 14
     * @param hour 4 0
     * @param minute 5 0
     * @param second 6 0
     * @return long
     *
     * @date 2022/1/11 1:17
     * @author xiaoma
     */
    public long getTargetTimePlusTime(int year, int month, int day, int hour, int minute, int second, int plusYear, int plusMonth, int plusDay, int plusHour, int plusMinute, int plusSecond) {
        return LocalDateTime.of(year, month, day, hour, minute, second).atZone(defaultZoneId).toInstant().toEpochMilli() +
                LocalDateTime.of(plusYear, plusMonth, plusDay, plusHour, plusMinute, plusSecond).atZone(defaultZoneId).toInstant().toEpochMilli();
    }

    /**
     * @desc 获取指定毫秒值 格式化 1995-07-14 00:00:00
     * @param timeMillis 1 805651200000
     * @return java.lang.String
     *
     * @date 2022/1/11 0:54
     * @author xiaoma
     */
    public String getTargetTimeFormat(long timeMillis, String pattern) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timeMillis), defaultZoneId).format(DateTimeFormatter.ofPattern(StrUtil.isEmpty(pattern) ? defaultPattern : pattern));
    }

    /**
     * @desc 获取指定毫秒值之后多少毫秒值的 格式化 1995-07-14 00:00:00
     * @param timeMillis 1 805651200000
     * @return java.lang.String
     *
     * @date 2022/1/11 0:54
     * @author xiaoma
     */
    public String getTargetTimePlusTimeFormat(long timeMillis, long timeMillisPlus, String pattern) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timeMillis + timeMillisPlus), defaultZoneId).format(DateTimeFormatter.ofPattern(StrUtil.isEmpty(pattern) ? defaultPattern : pattern));
    }

    /**
     * @desc 获取指定毫秒值之后多少时间的 格式化 1995-07-14 00:00:00
     * @param timeMillis 1 805651200000
     * @return java.lang.String
     *
     * @date 2022/1/11 0:54
     * @author xiaoma
     */
    public String getTargetTimePlusTimeFormat(long timeMillis, int year, int month, int day, int hour, int minute, int second, String pattern) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(
                        LocalDateTime.of(year, month, day, hour, minute, second).atZone(defaultZoneId).toInstant().toEpochMilli() + timeMillis), defaultZoneId).
                format(DateTimeFormatter.ofPattern(StrUtil.isEmpty(pattern) ? defaultPattern : pattern));
    }

    /**
     * @desc 获取指定日期的 格式化 1995-07-14 00:00:00
     * @param year 1 年 1995
     * @param month 2 月 07
     * @param day 3 日 14
     * @param hour 4 时 00
     * @param minute 5 分 00
     * @param second 6 秒 00
     * @return java.lang.String
     *
     * @date 2022/1/11 0:32
     * @author xiaoma
     */
    public String getTargetTimeFormat(int year, int month, int day, int hour, int minute, int second, String pattern) {
        return LocalDateTime.of(year, month, day, hour, minute, second).format(DateTimeFormatter.ofPattern(StrUtil.isEmpty(pattern) ? defaultPattern : pattern));
    }

    /**
     * @desc 获取指定日期之后多少毫秒值的 格式化 1995-07-14 00:00:00
     * @param year 1 年 1995
     * @param month 2 月 07
     * @param day 3 日 14
     * @param hour 4 时 00
     * @param minute 5 分 00
     * @param second 6 秒 00
     * @return java.lang.String
     *
     * @date 2022/1/11 0:32
     * @author xiaoma
     */
    public String getTargetTimePlusTimeFormat(int year, int month, int day, int hour, int minute, int second, long timeMillis, String pattern) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(
                        LocalDateTime.of(year, month, day, hour, minute, second).atZone(defaultZoneId).toInstant().toEpochMilli() + timeMillis), defaultZoneId).
                format(DateTimeFormatter.ofPattern(StrUtil.isEmpty(pattern) ? defaultPattern : pattern));
    }

    /**
     * @desc 获取指定日期之后多少时间的 格式化 1995-07-14 00:00:00
     * @param year 1
     * @param month 2
     * @param day 3
     * @param hour 4
     * @param minute 5
     * @param second 6
     * @param plusYear 7
     * @param plusMonth 8
     * @param plusDay 9
     * @param plusHour 10
     * @param plusMinute 11
     * @param plusSecond 12
     * @return java.lang.String
     *
     * @date 2020/7/24 18:53
     * @author xiaoma
     */
    public String getTargetTimePlusTimeFormat(int year, int month, int day, int hour, int minute, int second, int plusYear, int plusMonth, int plusDay, int plusHour, int plusMinute, int plusSecond, String pattern) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(
                        LocalDateTime.of(year, month, day, hour, minute, second).atZone(defaultZoneId).toInstant().toEpochMilli() +
                                LocalDateTime.of(plusYear, plusMonth, plusDay, plusHour, plusMinute, plusSecond).atZone(defaultZoneId).toInstant().toEpochMilli()), defaultZoneId).
                format(DateTimeFormatter.ofPattern(StrUtil.isEmpty(pattern) ? defaultPattern : pattern));
    }

    /**
     * description: 获取当前时间之前多久是什么时间
     * method: getTimeBeforeNow
     *
     * @author tgmeng
     * @since 2025/7/2 1:36
    */
    public String getTimeBeforeNow(int years, int months, int weeks, int days, String pattern) {
        // 获取当前时间
        LocalDateTime currentTime = LocalDateTime.now();
        // 计算目标时间（减去指定的年、月、周、天）
        LocalDateTime resultTime = currentTime
                .minusYears(years)
                .minusMonths(months)
                .minusWeeks(weeks)
                .minusDays(days);
        // 格式化结果并返回
        return resultTime.format(DateTimeFormatter.ofPattern(pattern != null && !pattern.isEmpty() ? pattern : defaultPattern));
    }

    /* ************************************************************************************************************************ */

    /**
     * @desc 获取当前年第一天的 毫秒值
     * @return long
     *
     * @date 2020/7/28 9:37
     * @author xiaoma
     */
    public long getFirstDayOfYear() {
        return LocalDateTime.now().atZone(defaultZoneId).with(TemporalAdjusters.firstDayOfYear()).withHour(0).withMinute(0).withSecond(0).withNano(0).toInstant().toEpochMilli();
    }

    /**
     * @desc 获取当前年最后一天的 毫秒值
     * @return long
     *
     * @date 2020/7/28 9:37
     * @author xiaoma
     */
    public long getLastDayOfYear() {
        return LocalDateTime.now().atZone(defaultZoneId).with(TemporalAdjusters.lastDayOfYear()).withHour(0).withMinute(0).withSecond(0).withNano(0).toInstant().toEpochMilli();
    }

    /**
     * @desc 获取当前月第一天的 毫秒值
     * @return long
     *
     * @date 2020/7/28 9:38
     * @author xiaoma
     */
    public long getFirstDayOfMonth() {
        return LocalDateTime.now().atZone(defaultZoneId).with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0).withSecond(0).withNano(0).toInstant().toEpochMilli();
    }

    /**
     * @desc 获取当前月最后一天的 毫秒值
     * @return long
     *
     * @date 2020/7/28 9:38
     * @author xiaoma
     */
    public long getLastDayOfMonth() {
        return LocalDateTime.now().atZone(defaultZoneId).with(TemporalAdjusters.lastDayOfMonth()).withHour(0).withMinute(0).withSecond(0).withNano(0).toInstant().toEpochMilli();
    }

    /**
     * @desc 获取当前月第一个周几 毫秒值
     * @param week 1
     * @return long
     *
     * @date 2020/7/28 9:52
     * @author xiaoma
     */
    public long getFirstDayWeekInMonth(int week) {
        return LocalDateTime.now().atZone(defaultZoneId).with(TemporalAdjusters.firstInMonth(DayOfWeek.of(week))).withHour(0).withMinute(0).withSecond(0).withNano(0).toInstant().toEpochMilli();
    }

    /**
     * @desc 获取当前月最后一个周几 毫秒值
     * @param week 1
     * @return long
     *
     * @date 2020/7/28 10:02
     * @author xiaoma
     */
    public long getLastDayWeekInMonth(int week) {
        return LocalDateTime.now().atZone(defaultZoneId).with(TemporalAdjusters.lastInMonth(DayOfWeek.of(week))).withHour(0).withMinute(0).withSecond(0).withNano(0).toInstant().toEpochMilli();
    }

    /**
     * @desc 下一个周几 (不包含今天,今天周二,那么此函数参数2,返回下个周二) 毫秒值
     * @param week 1
     * @return long
     *
     * @date 2020/7/28 10:18
     * @author xiaoma
     */
    public long getNextDayWeek(int week) {
        return LocalDateTime.now().atZone(defaultZoneId).with(TemporalAdjusters.next(DayOfWeek.of(week))).withHour(0).withMinute(0).withSecond(0).withNano(0).toInstant().toEpochMilli();
    }

    /**
     * @desc 下一个周几 (包含今天,今天周二,那么此函数参数2,返回今天) 毫秒值
     * @param week 1
     * @return long
     *
     * @date 2020/7/28 10:19
     * @author xiaoma
     */
    public long getNextOrSameDayWeek(int week) {
        return LocalDateTime.now().atZone(defaultZoneId).with(TemporalAdjusters.nextOrSame(DayOfWeek.of(week))).withHour(0).withMinute(0).withSecond(0).withNano(0).toInstant().toEpochMilli();
    }

    /**
     * @desc 上一个周几 (不包含今天,今天周二,那么此函数参数2,返回上个周二) 毫秒值
     * @param week 1
     * @return long
     *
     * @date 2020/7/28 10:19
     * @author xiaoma
     */
    public long getPreviousDayWeek(int week) {
        return LocalDateTime.now().atZone(defaultZoneId).with(TemporalAdjusters.previous(DayOfWeek.of(week))).withHour(0).withMinute(0).withSecond(0).withNano(0).toInstant().toEpochMilli();
    }

    /**
     * @desc 上一个周几 (包含今天,今天周二,那么此函数参数2,返回今天) 毫秒值
     * @param week 1
     * @return long
     *
     * @date 2020/7/28 10:19
     * @author xiaoma
     */
    public long getPreviousOrSameDayWeek(int week) {
        return LocalDateTime.now().atZone(defaultZoneId).with(TemporalAdjusters.previousOrSame(DayOfWeek.of(week))).withHour(0).withMinute(0).withSecond(0).withNano(0).toInstant().toEpochMilli();
    }

    /**
     * @desc 获取当前年第一天的 格式化
     * @param pattern 1
     * @return java.lang.String
     *
     * @date 2020/7/28 10:20
     * @author xiaoma
     */
    public String getFirstDayOfYear(String pattern) {
        return LocalDateTime.now().atZone(defaultZoneId).with(TemporalAdjusters.firstDayOfYear()).withHour(0).withMinute(0).withSecond(0).withNano(0).format(DateTimeFormatter.ofPattern(StrUtil.isEmpty(pattern) ? defaultPattern : pattern));
    }

    /**
     * @desc 获取当前年最后一天的 格式化
     * @param pattern 1
     * @return java.lang.String
     *
     * @date 2020/7/28 10:20
     * @author xiaoma
     */
    public String getLastDayOfYear(String pattern) {
        return LocalDateTime.now().atZone(defaultZoneId).with(TemporalAdjusters.lastDayOfYear()).withHour(0).withMinute(0).withSecond(0).withNano(0).format(DateTimeFormatter.ofPattern(StrUtil.isEmpty(pattern) ? defaultPattern : pattern));
    }

    /**
     * @desc 获取当前月第一天的 格式化
     * @param pattern 1
     * @return java.lang.String
     *
     * @date 2020/7/28 10:20
     * @author xiaoma
     */
    public String getFirstDayOfMonth(String pattern) {
        return LocalDateTime.now().atZone(defaultZoneId).with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0).withSecond(0).withNano(0).format(DateTimeFormatter.ofPattern(StrUtil.isEmpty(pattern) ? defaultPattern : pattern));
    }

    /**
     * @desc 获取当前月最后一天的 格式化
     * @param pattern 1
     * @return java.lang.String
     *
     * @date 2020/7/28 10:21
     * @author xiaoma
     */
    public String getLastDayOfMonth(String pattern) {
        return LocalDateTime.now().atZone(defaultZoneId).with(TemporalAdjusters.lastDayOfMonth()).withHour(0).withMinute(0).withSecond(0).withNano(0).format(DateTimeFormatter.ofPattern(StrUtil.isEmpty(pattern) ? defaultPattern : pattern));
    }

    /**
     * @desc 获取当前月第一个周几 格式化
     * @param week 1
     * @param pattern 2
     * @return java.lang.String
     *
     * @date 2020/7/28 9:54
     * @author xiaoma
     */
    public String getFirstDayWeekInMonth(int week, String pattern) {
        return LocalDateTime.now().atZone(defaultZoneId).with(TemporalAdjusters.firstInMonth(DayOfWeek.of(week))).withHour(0).withMinute(0).withSecond(0).withNano(0).format(DateTimeFormatter.ofPattern(StrUtil.isEmpty(pattern) ? defaultPattern : pattern));
    }

    /**
     * @desc 获取当前月最后一个周几 格式化
     * @param week 1
     * @param pattern 2
     * @return java.lang.String
     *
     * @date 2020/7/28 10:02
     * @author xiaoma
     */
    public String getLastDayWeekInMonth(int week, String pattern) {
        return LocalDateTime.now().atZone(defaultZoneId).with(TemporalAdjusters.lastInMonth(DayOfWeek.of(week))).withHour(0).withMinute(0).withSecond(0).withNano(0).format(DateTimeFormatter.ofPattern(StrUtil.isEmpty(pattern) ? defaultPattern : pattern));
    }

    /**
     * @desc 下一个周几 (不包含今天,今天周二,那么此函数参数2,返回下个周二) 格式化
     * @param week 1
     * @param pattern 2
     * @return java.lang.String
     *
     * @date 2020/7/28 10:12
     * @author xiaoma
     */
    public String getNextDayWeek(int week, String pattern) {
        return LocalDateTime.now().atZone(defaultZoneId).with(TemporalAdjusters.next(DayOfWeek.of(week))).withHour(0).withMinute(0).withSecond(0).withNano(0).format(DateTimeFormatter.ofPattern(StrUtil.isEmpty(pattern) ? defaultPattern : pattern));
    }

    /**
     * @desc 下一个周几 (包含今天,今天周二,那么此函数参数2,返回今天) 格式化
     * @param week 1
     * @param pattern 2
     * @return java.lang.String
     *
     * @date 2020/7/28 10:12
     * @author xiaoma
     */
    public String getNextOrSameDayWeek(int week, String pattern) {
        return LocalDateTime.now().atZone(defaultZoneId).with(TemporalAdjusters.nextOrSame(DayOfWeek.of(week))).withHour(0).withMinute(0).withSecond(0).withNano(0).format(DateTimeFormatter.ofPattern(StrUtil.isEmpty(pattern) ? defaultPattern : pattern));
    }

    /**
     * @desc 上一个周几 (不包含今天,今天周二,那么此函数参数2,返回上个周二) 格式化
     * @param week 1
     * @param pattern 2
     * @return java.lang.String
     *
     * @date 2020/7/28 10:15
     * @author xiaoma
     */
    public String getPreviousDayWeek(int week, String pattern) {
        return LocalDateTime.now().atZone(defaultZoneId).with(TemporalAdjusters.previous(DayOfWeek.of(week))).withHour(0).withMinute(0).withSecond(0).withNano(0).format(DateTimeFormatter.ofPattern(StrUtil.isEmpty(pattern) ? defaultPattern : pattern));
    }

    /**
     * @desc 上一个周几 (包含今天,今天周二,那么此函数参数2,返回今天) 格式化
     * @param week 1
     * @param pattern 2
     * @return java.lang.String
     *
     * @date 2020/7/28 10:15
     * @author xiaoma
     */
    public String getPreviousOrSameDayWeek(int week, String pattern) {
        return LocalDateTime.now().atZone(defaultZoneId).with(TemporalAdjusters.previousOrSame(DayOfWeek.of(week))).withHour(0).withMinute(0).withSecond(0).withNano(0).format(DateTimeFormatter.ofPattern(StrUtil.isEmpty(pattern) ? defaultPattern : pattern));
    }

    /**
     * @desc 某一日期和当前时间之间的距离，结果每个变量都等于总距离  例如：输入1996 05 05 00 00 00    输出 year=-24, month=-290, day=-8851, hour=-212440, minute=-12746444, seconds=-764786690, millis=-764786690348
     * @param year 1
     * @param month 2
     * @param day 3
     * @param hour 4
     * @param minute 5
     * @param second 6
     * @return com.ma.utils.timeutil.TimeUtil.DurationTime
     *
     * @date 2020/7/29 16:45
     * @author xiaoma
     */
    public DurationTime getDurationTimeSeparate(int year, int month, int day, int hour, int minute, int second) {
        ZonedDateTime zonedDateTimeNow = LocalDateTime.now().atZone(defaultZoneId);
        ZonedDateTime zonedDateTimeTarget = LocalDateTime.of(year, month, day, hour, minute, second).atZone(defaultZoneId);
        return getDurationTimeSeparate(zonedDateTimeNow, zonedDateTimeTarget);
    }

    /**
     * @desc 某一毫秒值和当前时间之间的距离，结果每个变量都等于总距离 例如：输入831225600000    输出 year=-24, month=-290, day=-8851, hour=-212440, minute=-12746444, seconds=-764786690, millis=-764786690348
     * @param timeMillis 1
     * @return com.ma.utils.timeutil.TimeUtil.DurationTime
     *
     * @date 2020/7/29 16:59
     * @author xiaoma
     */
    public DurationTime getDurationTimeSeparate(long timeMillis) {
        ZonedDateTime zonedDateTimeNow = LocalDateTime.now().atZone(defaultZoneId);
        ZonedDateTime zonedDateTimeTarget = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeMillis), defaultZoneId).atZone(defaultZoneId);
        return getDurationTimeSeparate(zonedDateTimeNow, zonedDateTimeTarget);
    }

    public DurationTime getDurationTimeSeparate(ZonedDateTime zonedDateTimeNow, ZonedDateTime zonedDateTimeTarget) {
        long years = ChronoUnit.YEARS.between(zonedDateTimeNow, zonedDateTimeTarget);
        long months = ChronoUnit.MONTHS.between(zonedDateTimeNow, zonedDateTimeTarget);
        long days = ChronoUnit.DAYS.between(zonedDateTimeNow, zonedDateTimeTarget);
        long hours = ChronoUnit.HOURS.between(zonedDateTimeNow, zonedDateTimeTarget);
        long minutes = ChronoUnit.MINUTES.between(zonedDateTimeNow, zonedDateTimeTarget);
        long seconds = ChronoUnit.SECONDS.between(zonedDateTimeNow, zonedDateTimeTarget);
        long millis = ChronoUnit.MILLIS.between(zonedDateTimeNow, zonedDateTimeTarget);
        return new DurationTime(years, months, days, hours, minutes, seconds, millis);
    }

    /**
     * @desc 某一时刻和当前时间之间的距离，结果里所有变量加起来等于总距离  例如：输入1996 05 05 00 00 00  输出year=-24, month=-2, day=-24, hour=-16, minute=-44, seconds=-51, millis=-346
     * @param year 1
     * @param month 2
     * @param day 3
     * @param hour 4
     * @param minute 5
     * @param second 6
     * @return com.ma.utils.timeutil.TimeUtil.DurationTime
     *
     * @date 2020/7/29 16:47
     * @author xiaoma
     */
    public DurationTime getDurationTimeUnSeparate(int year, int month, int day, int hour, int minute, int second) {
        LocalDate localDateNow = LocalDate.now(defaultZoneId);
        LocalDate localDateTarget = LocalDate.now(defaultZoneId).withYear(year).withMonth(month).withDayOfMonth(day);
        Period p = Period.between(localDateNow, localDateTarget);
        ZonedDateTime zonedDateTimeNow = LocalDateTime.now().atZone(defaultZoneId);
        ZonedDateTime zonedDateTimeTarget = LocalDateTime.of(year, month, day, hour, minute, second).atZone(defaultZoneId);
        Duration between = Duration.between(zonedDateTimeNow, zonedDateTimeTarget);
        return getDurationTimeUnSeparate(p, between);
    }

    /**
     * @desc 某一时刻和当前时间之间的距离，结果里所有变量加起来等于总距离  例如：输入831225600000  输出year=-24, month=-2, day=-24, hour=-16, minute=-44, seconds=-51, millis=-346
     * @param timeMillis 1
     * @return com.ma.utils.timeutil.TimeUtil.DurationTime
     *
     * @date 2020/7/29 17:21
     * @author xiaoma
     */
    public DurationTime getDurationTimeUnSeparate(long timeMillis) {
        LocalDate localDateNow = LocalDate.now(defaultZoneId);
        LocalDate localDateTarget = LocalDate.now(defaultZoneId).withYear(getTargetTimeYear(timeMillis)).withMonth(getTargetTimeMonth(timeMillis)).withDayOfMonth(getTargetTimeDay(timeMillis));
        Period p = Period.between(localDateNow, localDateTarget);
        ZonedDateTime zonedDateTimeNow = LocalDateTime.now().atZone(defaultZoneId);
        ZonedDateTime zonedDateTimeTarget = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeMillis), defaultZoneId).atZone(defaultZoneId);
        Duration between = Duration.between(zonedDateTimeNow, zonedDateTimeTarget);
        return getDurationTimeUnSeparate(p, between);
    }

    public DurationTime getDurationTimeUnSeparate(Period p, Duration between) {
        int years = p.getYears();
        int months = p.getMonths();
        int days = p.getDays();
        long hours = between.toHours() % 24;
        long minutes = between.toMinutes() % 60;
        long seconds = between.getSeconds() % 60;
        long millis = between.toMillis() % 1000;
        return new DurationTime(years, months, days, hours, minutes, seconds, millis);
    }

    /* ************************************************************************************************************************* */

    /**
     * @desc 获取数字对应的中文/英文月份 "一月份","一月","January","january","Jan","jan"
     * @param targetType 1 目标类型 "ZH","ch","EN","en"
     * @param month 2 月 7
     * @param abbreviation 3 是否缩写(英文缩写为前三个字母，中文缩写为前两个字:一月份变为一月)
     * @param lowerCase 4 是否小写(此值对"ZH","ch"类型没影响)
     * @return java.lang.String
     *
     * @date 2022/1/11 9:14
     * @author xiaoma
     */
    public String getMonthByNumber(String targetType, int month, boolean abbreviation, boolean lowerCase) {
        if (ZH.equals(targetType) || ZH.toLowerCase().equals(targetType)) {
            if (!abbreviation) {
                return ZH_MONTH[month - 1];
            }
            return ZH_MONTH[month - 1].substring(0, ZH_MONTH[month - 1].length() - 1);
        } else if (EN.equals(targetType) || EN.toLowerCase().equals(targetType)) {
            if (!abbreviation) {
                return !lowerCase ? EN_MONTH[month - 1] : EN_MONTH[month - 1].toLowerCase();
            }
            return !lowerCase ? EN_MONTH[month - 1].substring(0, 3) : EN_MONTH[month - 1].toLowerCase().substring(0, 3);
        }
        return "";
    }

    /**
     * @desc 获取中文/英文月份对应的数字 1
     * @param month 1 月份 "January","january","Jan","jan","一月份","一月","一"
     * @return int 1
     *
     * @date 2022/1/11 9:10
     * @author xiaoma
     */
    public int getNumberByMonth(String month) {
        for (int i = 0, j = 12; i < j; i++) {
            if (month.equals(ZH_MONTH[i]) ||
                    month.equals(ZH_MONTH[i].substring(0, ZH_MONTH[i].length() - 1)) ||
                    month.equals(ZH_MONTH[i].substring(0, ZH_MONTH[i].length() - 2)) ||
                    month.equals(EN_MONTH[i]) ||
                    month.equals(EN_MONTH[i].toLowerCase()) ||
                    month.equals(EN_MONTH[i].substring(0, 3)) ||
                    month.equals(EN_MONTH[i].substring(0, 3).toLowerCase())) {
                return i + 1;
            }
        }
        return 0;
    }

    /**
     * @desc 获取数字对应的中文/英文星期 "星期一","周一","Monday","Mon","monday","mon"
     * @param targetType 1 目标类型 "ZH","zh","EN","en"
     * @param week 2 周 1
     * @param abbreviation 3 是否缩写(英文缩写为前三个字母，中文缩写：星期一 -> 周一)
     * @param lowerCase 4 是否小写(此值对"ZH","ch"类型没影响)
     * @return java.lang.String
     *
     * @date 2022/1/11 9:40
     * @author xiaoma
     */
    public String getWeekByNumber(String targetType, int week, boolean abbreviation, boolean lowerCase) {
        if (ZH.equals(targetType) || ZH.toLowerCase().equals(targetType)) {
            if (!abbreviation) {
                return ZH_WEEK[week - 1];
            }
            return ZH_WEEK_ABBREVIATION[week - 1];
        } else if (EN.equals(targetType) || EN.toLowerCase().equals(targetType)) {
            if (!abbreviation) {
                return !lowerCase ? EN_WEEK[week - 1] : EN_WEEK[week - 1].toLowerCase();
            }
            return !lowerCase ? EN_WEEK[week - 1].substring(0, 3) : EN_WEEK[week - 1].toLowerCase().substring(0, 3);
        }
        return "";
    }

    /**
     * @desc 获取中文/英文星期对应的数字 1
     * @param week 1 周 "Monday","monday","Mon","mon","星期一","周一","一"
     * @return int 1
     *
     * @date 2022/1/11 9:54
     * @author xiaoma
     */
    public int getNumberByWeek(String week) {
        for (int i = 0, j = 7; i < j; i++) {
            if (week.equals(ZH_WEEK[i]) ||
                    week.equals(ZH_WEEK[i].substring(ZH_WEEK[i].length() - 1)) ||
                    week.equals(ZH_WEEK_ABBREVIATION[i]) ||
                    week.equals(EN_WEEK[i]) ||
                    week.equals(EN_WEEK[i].toLowerCase()) ||
                    week.equals(EN_WEEK[i].substring(0, 3)) ||
                    week.equals(EN_WEEK[i].substring(0, 3).toLowerCase())) {
                return i + 1;
            }
        }
        return 0;
    }

    /**
     * @description 将纳秒值转换为时分秒毫秒格式
     * @method getNanosFormat
     * @param millisecond 1
     * @return java.lang.String
     *
     * @author xiaoma
     * @date 2023/2/13 12:31
     * @version V1.0
     */
    public String getMillisecondFormat(long millisecond) {
        Integer s = 1000;
        Integer m = s * 60;
        Integer h = m * 60;
        Integer d = h * 24;

        Long day = millisecond / d;
        Long hour = (millisecond - day * d) / h;
        Long minute = (millisecond - day * d - hour * h) / m;
        Long second = (millisecond - day * d - hour * h - minute * m) / s;
        Long milliSecond = millisecond - day * d - hour * h - minute * m - second * s;

        StringBuffer sb = new StringBuffer();
        if(day > 0) {
            sb.append(day+"d");
        }
        if(hour > 0) {
            sb.append(hour+"h");
        }
        if(minute > 0) {
            sb.append(minute+"m");
        }
        if(second > 0) {
            sb.append(second+"s");
        }
        if(milliSecond > 0) {
            sb.append(milliSecond+"ms");
        }
        return sb.toString();
    }

    public Date getDateAfterNowSomeDays(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    @Data
    @AllArgsConstructor
    public class DurationTime {
        private long year;
        private long month;
        private long day;
        private long hour;
        private long minute;
        private long seconds;
        private long millis;
    }
}
