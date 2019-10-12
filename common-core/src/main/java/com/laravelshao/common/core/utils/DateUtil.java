package com.laravelshao.common.core.utils;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author qinghua.shao
 * @date 2019/10/11
 * @since 1.0.0
 */
public class DateUtil extends DateUtils {

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String MM_DD_HH_MM = "MM-dd HH:mm";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String MM_DD = "MM-dd";
    public static final String YYYY = "yyyy";
    public static final String HH_MM_SS = "HH:mm:ss";
    public static final String YYYYMMDD_HH_MM = "yyyyMMdd hh:mm";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddhhmmss";

    /**
     * date 2 string : yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String date2String(Date date) {
        return date2String(date, YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * string 2 date : yyyy-MM-dd HH:mm:ss
     *
     * @param str
     * @return
     */
    public static Date string2Date(String str) {
        return string2Date(str, YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 把date转换为pattern格式日期字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String date2String(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String str = format.format(date);
        return str;
    }

    /**
     * 把字符串转换pattern格式的日期
     *
     * @param str
     * @param pattern
     * @return
     */
    public static Date string2Date(String str, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.parse(str);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 获取当前时间的前{day}天日期:格式 yyyy-MM-dd HH:mm:ss
     *
     * @param day
     * @return
     */
    public static Date addDays2Zero(int day) {
        Date date = new Date();
        date = addDays(date, day);
        String date2String = date2String(date, YYYY_MM_DD);
        date2String = date2String + " 00:00:00";
        Date result = string2Date(date2String);
        return result;
    }

    /**
     * 获取2个日期相差的天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int daydiff(Date startDate, Date endDate) {

        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(startDate);

        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(endDate);

        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        return day2 - day1;
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Date now() {
        return new Date();
    }
}