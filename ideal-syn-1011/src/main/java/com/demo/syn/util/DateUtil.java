package com.demo.syn.util;


import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * 日期工具类
 *
 * @author sunwen
 */
public final class DateUtil {
    /**
     * 年月日时分秒(无下划线) yyyyMMddHHmmss
     */
    public static final String dtLong = "yyyyMMddHHmmss";

    /**
     * 年月日时分秒(无下划线) yyyyMMddHHmmss
     */
    public static final String COMMON_DATE_TIME_FORMAT = "yyyyMMddHHmmss";

    /**
     * 年月日时分秒(无下划线) yyyyMMddHHmmssSSS
     */
    public static final String EXT_DATE_TIME_FORMAT = "yyyyMMddHHmmssSSS";

    /**
     * 完整时间 yyyy-MM-dd HH:mm:ss
     */
    public static final String SIMPLE_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 年月日(无下划线) yyyyMMdd
     */
    public static final String ISO_DATE_FORMAT = "yyyyMMdd";

    /**
     * Basic ISO 8601 Time dateFormat HHmmssSSSzzz i.e., 143212333-500 for 2 pm 32
     * min 12 secs 333 mills -5 hours from GMT 24 hour clock
     */
    public static final String ISO_TIME_FORMAT = "HHmmssSSSzzz";
    /**
     * Basic ISO 8601 Time dateFormat HH:mm:ss,SSSzzz i.e., 14:32:12,333-500 for 2
     * pm 32 min 12 secs 333 mills -5 hours from GMT 24 hour clock
     */
    public static final String ISO_EXPANDED_TIME_FORMAT = "HH:mm:ss,SSSzzz";
    /**
     * 年月日(无下划线) yyyy-MM-dd
     */
    public static final String ISO_EXPANDED_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 年月(无下划线) yyyyMM
     */
    public static final String month = "yyyyMM";

    /**
     * DCP卡实例专用时间格式
     */
    public static final String DCP_DATE_FORMAT = "yyyyMMddHH:mm:ss";
    /**
     * 一秒的毫秒数
     */
    public static final long ONE_SECOND = 1000;
    /**
     * 一分钟的毫秒数
     */
    public static final long ONE_MINUTE = 60 * ONE_SECOND;
    /**
     * 一小时的毫秒数
     */
    public static final long ONE_HOUR = 60 * ONE_MINUTE;
    /**
     * 一天的毫秒数
     */
    public static final long ONE_DAY = 24 * ONE_HOUR;
    /**
     * 一周的毫秒数
     */
    public static final long ONE_WEEK = 7 * ONE_DAY;


    public static final DateFormatSymbols dateFormatSymbles;

    private static final String[][] foo;

    static {
        foo = new String[0][];
        dateFormatSymbles = new DateFormatSymbols();
        dateFormatSymbles.setZoneStrings(foo);
    }


    /**
     * 返回系统当前时间(精确到毫秒),作为一个唯一的订单编号
     *
     * @return 以yyyyMMddHHmmss为格式的当前系统时间
     */
    public static String getyyyyMMddHHmmss() {
        return getFormatDate(COMMON_DATE_TIME_FORMAT);
    }

    public static String getyyyyMMddHHmmssSSS() {
        return getFormatDate(EXT_DATE_TIME_FORMAT);
    }

    /**
     * 获取系统当期年月日(精确到天)，格式：yyyy-MM-dd
     *
     * @return
     */
    public static String getToday() {
        return getFormatDate(ISO_EXPANDED_DATE_FORMAT);
    }

    /**
     * 获取系统当前日期(精确到毫秒)，格式：yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getNowDate() {
        return getFormatDate(SIMPLE_DATE_TIME_FORMAT);
    }

    /**
     * 获取系统当期年月日(精确到天)，格式：yyyyMMdd
     *
     * @return
     */
    public static String getDate() {
        return getFormatDate(ISO_DATE_FORMAT);
    }

    /**
     * 产生随机的三位数
     *
     * @return
     */
//    public static String getThree() {
//        Random rad = new Random();
//        return rad.nextInt(1000) + "";
//    }

    /**
     * 字符串转date
     *
     * @param date
     * @return
     */
    public static Date getDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(SIMPLE_DATE_TIME_FORMAT);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    /**
     * 获取指定格式的当天时间字串
     * @param formatStr
     * @return
     */
    public static String getFormatDate(String formatStr) {
        return format(new Date(), formatStr);
    }

    /**
     * 将Date类型转换为指定格式的字符串
     *
     * @param date 日期对象
     * @param pattern 日期格式
     * @return 日期字符串
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            date = new Date();
        }
        if (pattern == null || "".equals(pattern) || "null".equalsIgnoreCase(pattern)) {
            pattern = SIMPLE_DATE_TIME_FORMAT;
        }
        return new SimpleDateFormat(pattern).format(date);
    }
    public static String format(Date date){
        return format(date, SIMPLE_DATE_TIME_FORMAT);
    }

    /**
     * 当前月份
     *
     * @return
     */
    public static String getMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        return sdf.format(new Date());
    }

    /**
     * 获取上月月份
     *
     * @return
     */
    public static String getLastMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        return sdf.format(cal.getTime());
    }

    /**
     * 获取下月月份
     *
     * @return
     */
    public static String getNextMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        return sdf.format(cal.getTime());
    }

    /**
     * 根据指定格式, 将字符串转换为Date
     *
     * @param date
     * @param dateFormat
     * @return
     */
    public static Date getDate(String date, String dateFormat) {

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    /**
     * @param date1
     * @param date2
     * @param formatStr yyyy-MM-dd hh:mm:ss
     * @return 1=DATE1>DATE2,-1=DATE1<DATE2,0相等
     */
    public static int compareDate(String date1, String date2, String formatStr) {
        DateFormat df = new SimpleDateFormat(formatStr);
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return -2;
    }

    /**
     * 计算两个日期相差的天数
     *
     * @param firstDate
     * @param secondDate
     * @return
     */
    public static int caculate2Days(Date firstDate, Date secondDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(firstDate);
        int dayNum1 = calendar.get(Calendar.HOUR_OF_DAY);
        calendar.setTime(secondDate);
        int dayNum2 = calendar.get(Calendar.HOUR_OF_DAY);
        return Math.abs(dayNum1 - dayNum2);
    }

    /**
     * 计算两个日期相差的秒数
     *
     * @param startDate
     * @param secondDate
     * @return
     */
    public static int calLastedTime(Date startDate, Date secondDate) {
        long a = secondDate.getTime();
        long b = startDate.getTime();
        int c = (int) ((a - b) / 1000);
        return c;
    }

    /**
     * 取次月的第一天
     */
    public static Date getFirstDayOfNextMonth() {
        Calendar sCal = Calendar.getInstance();
        sCal.set(Calendar.MONTH, sCal.get(Calendar.MONTH) + 1);
        sCal.set(Calendar.DAY_OF_MONTH, 1);
        sCal.set(Calendar.HOUR_OF_DAY, 0);
        sCal.set(Calendar.MINUTE, 0);
        sCal.set(Calendar.SECOND, 0);
        sCal.set(Calendar.MILLISECOND, 0);
        return sCal.getTime();
    }

    /**
     * 返回距离当天日期指定天数的日期
     * 格式yyyy-MM-dd
     *
     * @param day
     * @return
     */
    public static String yesTedayDate(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, day);
        SimpleDateFormat format = new SimpleDateFormat(ISO_EXPANDED_DATE_FORMAT);
        return format.format(calendar.getTime());
    }

    /**
     * 功能描述: 获取距离当天的日期<br> 把日期往后或往前增加几天
     *
     * @param day 整数往后推,负数往前移动
     * @return yyyy-MM-dd HH:mm:ss
     * @author 10120359
     */
    public static String getDateA(String dateStr, int day) {
        Date date = getDate(dateStr);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        // 把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime();
        return format(date, SIMPLE_DATE_TIME_FORMAT);
    }

    /**
     * @param data 要加减的日期
     * @param type add相加 subtract相减
     * @param x    天数
     * @return 返回格式yyyy-mm-dd
     */
    public static String dataAddAndSubtract(String data, String type, int x) {
        String str = "2016-02-23 00:00:00";
        Date d = getDate(str);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        String returndata = "";
        if ("add".equals(type)) {
            returndata = df.format(new Date(d.getTime() + x * 24 * 60 * 60 * 1000));
        }
        if ("subtract".equals(type)) {
            returndata = df.format(new Date(d.getTime() - x * 24 * 60 * 60 * 1000));
        }

        return returndata;
    }

    /**
     * 获取当前的年份
     *
     * @return
     */
    public static String getYear() {
        Calendar a = Calendar.getInstance();
        // 得到年
        return String.valueOf(a.get(Calendar.YEAR));
    }

    /**
     * 获取当前是本月的第几天
     *
     * @return
     */
    public static int getDay() {
        Calendar a = Calendar.getInstance();
        return a.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 转换日期格式
     *
     * @param date
     * @return
     * @throws Exception
     */
    public static String setDate(String date) {
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d = null;
        try {
            d = sim.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 可根据不同样式请求显示不同日期格式，要显示星期可以添加E参数
        SimpleDateFormat f4 = new SimpleDateFormat("yyyy年MM月dd日");
        return f4.format(d);
    }

    /**
     * 把 long 型日期转 String
     *
     *
     * @param date   long型日期；
     * @param format 日期格式；
     * @return
     */
    public static String longToString(long date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        // 前面的lSysTime是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
        Date dt2 = new Date(date);
        // 得到精确到秒的表示：08/31/2006 21:08:00
        return sdf.format(dt2);
    }

    /**
     * 把秒转成成HH:mm:ss
     *
     * @param time
     * @return
     */
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99) return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }
    /**
     * 字符串转“yyyy-MM-dd hh:mm:ss”时间
     * @param date
     * @return
     */
    public static Date parse(String date){

        return parse(date,null);
    }

    /**
     * 字符串转指定格式时间
     * @param date
     * @param pattern
     * @return
     */
    public static Date parse(String date,String pattern){
        String fomat = pattern;
        if(StringUtils.isEmpty(pattern)){
            fomat = SIMPLE_DATE_TIME_FORMAT;
        }
        SimpleDateFormat sim = new SimpleDateFormat(fomat);
        try {
            return sim.parse(date);
        }catch (ParseException e){
            e.printStackTrace();
            return null;
        }
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + i;
        else
            retStr = "" + i;
        return retStr;
    }


    public static java.sql.Date isoToSQLDate(String dateString, boolean expanded) throws ParseException {
        SimpleDateFormat formatter;

        if (expanded)
            formatter = new SimpleDateFormat(ISO_EXPANDED_DATE_FORMAT, dateFormatSymbles);
        else
            formatter = new SimpleDateFormat(ISO_DATE_FORMAT, dateFormatSymbles);

        return new java.sql.Date(formatter.parse(dateString).getTime());
    }

    public static String getFirstDayOfMonth() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        // 设置为1号,当前日期既为本月第一天
        c.set(Calendar.DAY_OF_MONTH, 1);
        DateFormat df = new SimpleDateFormat(SIMPLE_DATE_TIME_FORMAT);

        return df.format(c.getTime());
    }

    public static String getFileUploadDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(new Date());
    }

    /**
     * 获取当天剩余秒数
     * @return
     */
    public static int getSecondsLeftToday(){
        Calendar midnight = Calendar.getInstance();
        Date currentDate = new Date();
        midnight.setTime(currentDate);
        midnight.add(Calendar.DAY_OF_MONTH,1);
        midnight.set(Calendar.HOUR_OF_DAY,0);
        midnight.set(Calendar.MINUTE,0);
        midnight.set(Calendar.SECOND,0);
        midnight.set(Calendar.MILLISECOND,0);
        Long seconds = (midnight.getTime().getTime()-currentDate.getTime()) / 1000;
        return seconds.intValue();
    }


    /**
     * yangshilei:获取某段时间开始往后几个月的日期
     */
    public static Date afterMonthDate(Date beginDay,Integer afterMonth){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(beginDay);
        calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH) + afterMonth);
        Date time = calendar.getTime();
        return time;
    }

    /**
     * 获取两个日期相差的月数
     * d1 开始时间
     * d2 结束时间
     */
    public static int getMonthDiff(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH);
        int month2 = c2.get(Calendar.MONTH);
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        // 获取年的差值 
        int yearInterval = year1 - year2;
        // 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数
        if (month1 < month2 || month1 == month2 && day1 < day2) {
            yearInterval--;
        }
        // 获取月数差值
        int monthInterval = (month1 + 12) - month2;
        if (day1 < day2) {
            monthInterval--;
        }
        monthInterval %= 12;
        int monthsDiff = Math.abs(yearInterval * 12 + monthInterval);
        return monthsDiff;
    }

    public static void main(String[] args) throws ParseException {
        /*String date1 = "2019-10-01";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date begin = format.parse(date1);

        Integer integer = DateUtil.getMonthDiff(begin, new Date());
        System.out.println(integer);*/
        System.out.println(getDay());
    }
}
