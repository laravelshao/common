package com.laravelshao.common.core.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * LocalDate 工具类
 *
 * @author qinghua.shao
 * @date 2019/10/11
 * @since 1.0.0
 */
public class LocalDateUtils {

    /**
     * 系统当前时区
     */
    private static final ZoneId ZONE = ZoneId.systemDefault();

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DATETIME_NOSPLIT_FORMAT = "yyyyMMddHHmmss";

    public static final String DATETIME_STAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * 将当前时间按照指定日期格式转换为字符串
     *
     * @param format
     * @return
     */
    public static String getNowDateTimeToString(String format) {
        if (isBlank(format)) {
            format = DATETIME_FORMAT;
        }
        return localDateTime2String(LocalDateTime.now(), format);
    }

    /**
     * 将LocalDateTime转为String
     *
     * @param localDateTime
     * @param format
     * @return
     */
    public static String localDateTime2String(LocalDateTime localDateTime, String format) {
        if (isBlank(format)) {
            format = DATETIME_FORMAT;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(formatter);
    }

    /**
     * 将String转为LocalDateTime
     *
     * @param time
     * @param format
     * @return
     */
    public static LocalDateTime string2LocalDateTime(String time, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(time, formatter);
    }

    /**
     * 将Long转为LocalDateTime
     *
     * @param timestamp
     * @return
     */
    public static LocalDateTime timestamp2LocalDateTime(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZONE);
    }

    /**
     * 将Date转换成LocalDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime date2LocalDateTime(Date date) {
        Instant instant = date.toInstant();
        return LocalDateTime.ofInstant(instant, ZONE);
    }

    /**
     * 将LocalDateTime转成Long
     *
     * @param localDateTime
     * @return
     */
    public static long localDateTime2TimeStamp(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZONE).toInstant();
        return instant.toEpochMilli();
    }

    /**
     * 将Date转换成LocalDate
     *
     * @param date
     * @return
     */
    public static LocalDate date2LocalDate(Date date) {
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZONE);
        return localDateTime.toLocalDate();
    }

    /**
     * 将LocalDate转成Long
     *
     * @param localDate
     * @return
     */
    public static long localDate2TimeStamp(LocalDate localDate) {
        Instant instant = localDate.atStartOfDay(ZONE).toInstant();
        return instant.toEpochMilli();
    }

    /**
     * 将Date转换成LocalTime
     *
     * @param date
     * @return
     */
    public static LocalTime date2LocalTime(Date date) {
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZONE);
        return localDateTime.toLocalTime();
    }

    /**
     * 将LocalDate转换成Date
     *
     * @param localDate
     * @return
     */
    public static Date localDate2Date(LocalDate localDate) {
        Instant instant = localDate.atStartOfDay().atZone(ZONE).toInstant();
        return Date.from(instant);
    }

    /**
     * 将LocalDateTime转换成Date
     *
     * @param localDateTime
     * @return
     */
    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZONE).toInstant();
        return Date.from(instant);
    }

    public static void main(String[] args) {
        System.out.println(string2LocalDateTime("2019-04-25 17:10:53.851", DATETIME_STAMP_FORMAT));
    }
}
