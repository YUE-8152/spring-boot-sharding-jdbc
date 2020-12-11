package com.cardlan.sharding.jdbc.common.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.joda.time.DateTime;

import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * 日期时间工具类
 *
 * @author YX
 */
public class DateUtils {
    public static final String DATE_FORMAT = "yyyyMMdd";
    /**
     * 默认的是日期时间格式：yyyy-MM-dd HH:mm:ss
     */
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 默认的是日期格式：yyyy-MM-dd
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    /**
     * 年月格式：yyyyMM
     */
    public static final String YM_FORMATTER = "yyyyMM";

    public static SimpleDateFormat standard = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 时间：2020-01-01 00:00:00
     * 用于分表查询的默认开始时间
     **/
    private static final String START_DATE_TIME = "2020-01-01 00:00:00";
    /**
     * 当前时间，格式：yyyy-MM-dd HH:mm:ss
     **/
    private static final String CURRENT_DATE_TIME = DateUtils.getCurrentDateTime();
    /**
     * 当前时间前7天，格式：yyyy-MM-dd HH:mm:ss
     **/
    private static final String BEFORE_7_DAYS = DateUtils.plusDay(DateUtils.getCurrentDateTime(), -7);
    /**
     * 当前时间前15天，格式：yyyy-MM-dd HH:mm:ss
     **/
    private static final String BEFORE_15_DAYS = DateUtils.plusDay(DateUtils.getCurrentDateTime(), -15);
    /**
     * 当前时间前1个月，格式：yyyy-MM-dd HH:mm:ss
     **/
    private static final String BEFORE_1_MONTH = DateUtils.plusMonth(DateUtils.getCurrentDateTime(), -1);
    /**
     * 当前时间前2个月，格式：yyyy-MM-dd HH:mm:ss
     **/
    private static final String BEFORE_2_MONTH = DateUtils.plusMonth(DateUtils.getCurrentDateTime(), -2);
    /**
     * 当前时间前3个月，格式：yyyy-MM-dd HH:mm:ss
     **/
    private static final String BEFORE_3_MONTH = DateUtils.plusMonth(DateUtils.getCurrentDateTime(), -3);
    /**
     * 当前时间前4个月，格式：yyyy-MM-dd HH:mm:ss
     **/
    private static final String BEFORE_4_MONTH = DateUtils.plusMonth(DateUtils.getCurrentDateTime(), -4);
    /**
     * 当前时间前5个月，格式：yyyy-MM-dd HH:mm:ss
     **/
    private static final String BEFORE_5_MONTH = DateUtils.plusMonth(DateUtils.getCurrentDateTime(), -5);
    /**
     * 当前时间前6个月，格式：yyyy-MM-dd HH:mm:ss
     **/
    private static final String BEFORE_6_MONTH = DateUtils.plusMonth(DateUtils.getCurrentDateTime(), -6);
    /**
     * 当前时间前7个月，格式：yyyy-MM-dd HH:mm:ss
     **/
    private static final String BEFORE_7_MONTH = DateUtils.plusMonth(DateUtils.getCurrentDateTime(), -7);
    /**
     * 当前时间前8个月，格式：yyyy-MM-dd HH:mm:ss
     **/
    private static final String BEFORE_8_MONTH = DateUtils.plusMonth(DateUtils.getCurrentDateTime(), -8);
    /**
     * 当前时间前9个月，格式：yyyy-MM-dd HH:mm:ss
     **/
    private static final String BEFORE_9_MONTH = DateUtils.plusMonth(DateUtils.getCurrentDateTime(), -9);
    /**
     * 当前时间前10个月，格式：yyyy-MM-dd HH:mm:ss
     **/
    private static final String BEFORE_10_MONTH = DateUtils.plusMonth(DateUtils.getCurrentDateTime(), -10);
    /**
     * 当前时间前11个月，格式：yyyy-MM-dd HH:mm:ss
     **/
    private static final String BEFORE_11_MONTH = DateUtils.plusMonth(DateUtils.getCurrentDateTime(), -11);

    /**
     * 返回传入字符串格式的当前日期字符串 示例：getCurrentDate("yyyy-MM-dd")
     */
    public static String getCurrentDate(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date();
        return sdf.format(date);
    }

    /**
     * @Title: addDay
     * @author: xiao
     * @Description: 以传入的时间至时间戳相差的天数
     * @param: @param date
     * @param: @param day
     * @param: @return
     * @return: Date
     * @throws:
     */
    public static Date addDay(Date date, int day) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(5, day);
        date = calendar.getTime();
        return date;
    }

    /**
     * 根据时间戳 返回 yyyy-MM-dd HH:mm:ss格式的字符串
     */
    public static String getStandardDateStr(long timeSheet) {
        return standard.format(timeSheet);
    }

    /**
     * 根据时间戳 返回指定格式的字符串
     */
    public static String getCustomDateStr(String format, long timeSheet) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(timeSheet);
    }

    /**
     * 获取当前日期
     */
    public static Date getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        Date currDate = cal.getTime();

        return currDate;
    }

    /**
     * 功能: 将日期对象按照某种格式进行转换，返回转换后的字符串
     *
     * @param date    日期对象
     * @param pattern 转换格式 例：yyyy-MM-dd
     */
    public static String DateToString(Date date, String pattern) {
        String strDateTime = null;
        SimpleDateFormat formater = new SimpleDateFormat(pattern);
        strDateTime = date == null ? null : formater.format(date);
        return strDateTime;
    }

    /**
     * 获取当前日期和时间
     *
     * @return
     */
    public static String getCurrentDateTime() {
        return DateToString(getCurrentDate(), DEFAULT_DATE_TIME_FORMAT);
    }

    /**
     * 将日期对象按照某种格式转换，返回字符串
     *
     * @param date
     * @param patten
     * @return
     */
    public static String DateToSting(Date date, String patten) {

        String strDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat(patten);
        strDate = date == null ? null : sdf.format(date);

        return strDate;
    }

    /**
     * 获取当前不带符号的日期，如20150101
     *
     * @return
     */
    public static String getCurrentDateNoSign() {
        return DateToSting(getCurrentDate(), DATE_FORMAT);
    }

    /**
     * 获取制定的时间格式
     *
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDate(String timestr) {
        String dateString = null;
        try {
            Date currentTime = strToDateLong(timestr);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateString = formatter.format(currentTime);

        } catch (Exception e) {
            dateString = timestr;
        }
        return dateString;
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     * @param strDate
     * @return
     */
    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }


    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate, String patten) {
        SimpleDateFormat formatter = new SimpleDateFormat(patten);
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }


    public static String strConvertStr(String strDate, String oldPatten, String newPatten) {
        String format = "";
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(oldPatten);
            Date parse = formatter.parse(strDate);
            SimpleDateFormat formatter2 = new SimpleDateFormat(newPatten);
            format = formatter2.format(parse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return format;
    }

    public static Integer getBetweenMinutes(String a, String b) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate;
        Date endDate;
        try {
            startDate = format.parse(a);
            endDate = format.parse(b);
            try {
                long ss = startDate.getTime() - endDate.getTime();
//                if (startDate.before(endDate)) {
//                    ss = endDate.getTime() - startDate.getTime();
//                } else {
//                    ss = startDate.getTime() - endDate.getTime();
//                }
                return Integer.valueOf((int) (ss / (60 * 1000)));
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        } catch (ParseException e1) {
            e1.printStackTrace();
            return 0;
        }
    }

    /**
     * 增加日期的天数。失败返回null。
     *
     * @param date      日期字符串
     * @param dayAmount 增加数量。可为负数
     * @return 增加天数后的日期字符串
     */
    public static String plusDay(String date, int dayAmount) {
        date = date.replace(" ", "T");//把空格替换成T
        return new DateTime(date).plusDays(dayAmount).toString("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 增加日期的天数。失败返回null。
     *
     * @param date      日期
     * @param dayAmount 增加数量。可为负数
     * @return 增加天数后的日期
     */
    public static Date plusDay(Date date, int dayAmount) {
        return new DateTime(date).plusDays(dayAmount).toDate();
    }

    /**
     * 增加日期的月数。
     *
     * @param date:
     * @param monthAmount:
     * @return: java.lang.String
     * @Author: YUE
     * @Date: 2020/12/4 15:14
     **/
    public static String plusMonth(String date, int monthAmount) {
        date = date.replace(" ", "T");//把空格替换成T
        return new DateTime(date).plusMonths(monthAmount).toString("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 增加日期的月数。
     *
     * @param date:
     * @param monthAmount:
     * @return: java.util.Date
     * @Author: YUE
     * @Date: 2020/12/4 15:14
     **/
    public static Date plusMonth(Date date, int monthAmount) {
        return new DateTime(date).plusMonths(monthAmount).toDate();
    }


    /**
     * 将秒转化成xx天xx时xx分xx秒
     *
     * @param mss:
     * @return: java.lang.String
     * @Author: YUE
     * @Date: 2020/10/20 10:00
     **/
    public static String formatSecondsToDateTime(long mss) {
        String DateTimes = null;
        long days = mss / (60 * 60 * 24);
        long hours = (mss % (60 * 60 * 24)) / (60 * 60);
        long minutes = (mss % (60 * 60)) / 60;
        long seconds = mss % 60;
        if (days > 0) {
            DateTimes = days + "天" + hours + "小时" + minutes + "分钟"
                    + seconds + "秒";
        } else if (hours > 0) {
            DateTimes = hours + "小时" + minutes + "分钟"
                    + seconds + "秒";
        } else if (minutes > 0) {
            DateTimes = minutes + "分钟"
                    + seconds + "秒";
        } else {
            DateTimes = seconds + "秒";
        }
        return DateTimes;
    }

    /**
     * 获取当前时间到凌晨12点的秒数
     *
     * @return: java.lang.Long
     * @Author: YUE
     * @Date: 2020/10/20 10:09
     **/
    public static Long getSecondsNextEarlyMorning() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }

    /**
     * 两个时间之间间隔的秒数
     *
     * @param date1:
     * @param date2:
     * @return: java.lang.Long
     * @Author: YUE
     * @Date: 2020/10/30 13:36
     **/
    public static Long getSecondsBetweenDate(String date1, String date2) {
        Date d1 = strToDate(date1, DEFAULT_DATE_TIME_FORMAT);
        Date d2 = strToDate(date2, DEFAULT_DATE_TIME_FORMAT);
        long l = d1.getTime() - d2.getTime();
        if (l > 0) {
            return l / 1000;
        } else {
            return (d2.getTime() - d1.getTime()) / 1000;
        }
    }

    /**
     * 两个时间之间间隔的秒数
     *
     * @param date1:
     * @param date2:
     * @return: java.lang.Long
     * @Author: YUE
     * @Date: 2020/10/30 13:36
     **/
    public static Long getSecondsBetweenDate(Date date1, Date date2) {
        long l = date1.getTime() - date2.getTime();
        if (l > 0) {
            return l / 1000;
        } else {
            return (date2.getTime() - date1.getTime()) / 1000;
        }
    }

    /**
     * 比较两日期大小
     *
     * @param date      日期字符串
     * @param otherDate 另一个日期字符串
     * @return 比较两日期大小。如果date>otherDate则返回true,否则false
     */
    public static Boolean compareDate(String date, String otherDate) {
        date = date.replace(" ", "T");//把空格替换成T
        otherDate = otherDate.replace(" ", "T");//把空格替换成T
        return compareDate(getDate(date), getDate(otherDate));
    }

    /**
     * 比较两日期大小
     *
     * @param date      日期
     * @param otherDate 另一个日期
     * @return 比较两日期大小。如果date>otherDate则返回true,否则false
     */
    public static Boolean compareDate(Date date, Date otherDate) {
        Boolean flag = false;
        Date dateTmp = getDateTime(date);
        Date otherDateTmp = getDateTime(otherDate);
        if (dateTmp != null && otherDateTmp != null) {
            long time = dateTmp.getTime() - otherDateTmp.getTime();
            if (time >= 0) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 日期格式化 yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return DATE
     */
    private static Date getDateTime(Date date) {
        try {
            return new DateTime(date).toDate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 日期格式化
     *
     * @param date
     * @return
     */
    private static Date getDate(String date) {
        try {
            date = date.replace(" ", "T");//把空格替换成T
            return new DateTime(date).toDate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 日期格式化yyyy-MM-dd
     *
     * @param date
     * @return DATE
     */
    private static Date getDate(Date date) {
        try {
            return new DateTime(getDateToString(date)).toDate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 日期格式化yyyy-MM-dd
     *
     * @param date
     * @return String
     */
    private static String getDateToString(Date date) {
        return new DateTime(date).toString("yyyy-MM-dd");
    }

    /**
     * 比较两日期是否在同一天
     *
     * @param date      日期字符串
     * @param otherDate 另一个日期字符串
     * @return 比较两日期大小。如果date==otherDate则返回true,否则false
     */
    public static Boolean isSameDay(String date, String otherDate) {
        date = date.replace(" ", "T");//把空格替换成T
        otherDate = otherDate.replace(" ", "T");//把空格替换成T
        return isSameDay(getDate(date), getDate(otherDate));
    }

    /**
     * 比较两日期是否在同一天
     *
     * @param date      日期
     * @param otherDate 另一个日期
     * @return 比较两日期大小。如果date==otherDate则返回true,否则false
     */
    public static Boolean isSameDay(Date date, Date otherDate) {
        Boolean flag = false;
        Date dateTmp = getDate(date);
        Date otherDateTmp = getDate(otherDate);
        if (dateTmp != null && otherDateTmp != null) {
            long time = dateTmp.getTime() - otherDateTmp.getTime();
            if (time == 0) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 指定日期加上指定天数
     *
     * @param date:
     * @param days:
     * @return: java.lang.String
     * @Author: YUE
     * @Date: 2020/10/30 16:33
     **/
    public static String datePlusDays(String date, int days) {
        Date date1 = strToDate(date, "yyyy-MM-dd");
        return datePlusDays(date1, days);
    }

    /**
     * 指定日期加上指定天数
     *
     * @param date:
     * @param days:
     * @return: java.lang.String
     * @Author: YUE
     * @Date: 2020/10/30 16:33
     **/
    public static String datePlusDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return DateToSting(cal.getTime(), "yyyy-MM-dd");
    }


    public static String dateToStringBeginOrEnd(Date date, Boolean flag) {
        String time = null;
        SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar1 = Calendar.getInstance();
        //获取某一天的0点0分0秒 或者 23点59分59秒
        if (flag == true) {
            calendar1.setTime(date);
            calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH),
                    0, 0, 0);
            Date beginOfDate = calendar1.getTime();
            time = dateformat1.format(beginOfDate);
            System.out.println(time);
        } else {
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(date);
            calendar1.set(calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH),
                    23, 59, 59);
            Date endOfDate = calendar1.getTime();
            time = dateformat1.format(endOfDate);
            System.out.println(time);
        }
        return time;
    }

    /**
     * 获取指定时间的凌晨和最后一秒
     *
     * @param date:
     * @param flag: true 凌晨， false 夜晚
     * @return: java.lang.String
     * @Author: YUE
     * @Date: 2020/10/30 18:09
     **/
    public static String dateToStringBeginOrEnd(String date, Boolean flag) {
        Date date1 = strToDate(date, "yyyy-MM-dd HH:mm:ss");
        String time = null;
        SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar1 = Calendar.getInstance();
        //获取某一天的0点0分0秒 或者 23点59分59秒
        if (flag == true) {
            calendar1.setTime(date1);
            calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH),
                    0, 0, 0);
            Date beginOfDate = calendar1.getTime();
            time = dateformat1.format(beginOfDate);
        } else {
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(date1);
            calendar1.set(calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH),
                    23, 59, 59);
            Date endOfDate = calendar1.getTime();
            time = dateformat1.format(endOfDate);
        }
        return time;
    }

    /**
     * @Description: yyyyMMddHHmmss字符串转化为日期
     * @Param: [time]
     * @return: java.util.Date
     * @Author: cx
     * @Date: 2018/5/17
     */
    public static Date getStringTimeToDate(String time, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Date转换为LocalDate
     */
    public static LocalDate dateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDate();
    }

    /**
     * LocalDate转换为Date
     */
    public static Date localDateToDate(LocalDate localDate) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDate.atStartOfDay(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * 日期格式化yyyyMM
     *
     * @param date
     * @return
     */
    public static String getDateToStringYM(Date date) {
        return new DateTime(date).toString(YM_FORMATTER);
    }

    public static void main(String[] args) {

        System.out.println(dateToStringBeginOrEnd("2020-10-20 23:23:00", true));
    }
}