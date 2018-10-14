package com.bootdo.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用空间换时间，由于DateFormat创建的代价比较高，加上非线程安全， 我们为每种格式创建一个ThreadLocal<DateFormat>
 * @author jonzhou
 */
public class DateUtil { 
    private static final ThreadLocal<TimeZone> threadLocal = new ThreadLocal<TimeZone>() {
        protected synchronized TimeZone initialValue() {
            return TimeZone.getDefault();
        }
    };
    public final static String DEFULT_DATE_FORMATTER = "yyyy-MM-dd HH:mm:ss";
    /**
     * 日期格式化yyyyMMddHHmmss
     */
    public static String FORMAT_DATE = "yyyyMMddHHmmss";

    /**
     * 日期格式化yyyy-MM-dd HH:mm:ss
     */
    public static String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    public static String FORMAT_DATE_Min = "yyyy-MM-dd HH:mm";

    public static String FORMAT_DATE_Hour = "yyyy-MM-dd HH";

    /**
     * 日期格式化yyyy-MM-dd HH:mm:ss:SSS
     */
    public static String FORMAT_DATE_TIME_S = "yyyy-MM-dd HH:mm:ss:SSS";

    /**
     * yyyyMMddHHmmssSSS
     */
    public static String FORMAT_DATE_TIME_S1 = "yyyyMMddHHmmssSSS";
    /**
     * yyyyMMddHHmmss
     */
    public static String FORMAT_DATE_TIME_S2 = "yyyyMMddHHmmss";
    
    /**
     * 日期格式化yyyy-MM-dd HH:mm:ss.SSS
     */
    public static String FORMAT_DATE_TIME_S4 = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式化 yyyy-MM-dd
     */
    public static String FORMAT_DATE1 = "yyyy-MM-dd";

    public static String FORMAT_DATE2 = "yyyyMMdd";
    
    public static String FORMAT_DATE3 = "yyyy/MM/dd";

    public static String FORMAT_DATE4 = "yyyy年MM月dd日";

    /**
     * 日期格式化 yyyy
     */
    public static String FORMAT_YEAR = "yyyy";

    public static String FORMAT_Mon = "yyyy-MM";

    public static String FORMAT_Mon2 = "yyyyMM";

    public static String FORMAT_Mon_Date = "MMdd";

    public static String FORMAT_TIME_HOUR = "HH:mm:ss";
    
    public static String FORMAT_TIME_HOUR_SECEND = "HH:mm";
    // 一天的开始时间
    public static String DAY_START_TIME = " 00:00:00";
    // 一天的结束时间
    public static String DAY_END_TIME = " 23:59:59";

    /**
     * 设置当前线程的TimeZone
     * @param l
     */
    public static void setTimeZone(TimeZone l) {
        threadLocal.set(l);
    }

    /**
     * 获取当前线程的TimeZone
     * @return
     */
    public static TimeZone getTimeZone() {
        return threadLocal.get();
    }

    private static final Map<String, ThreadLocal<DateFormat>> ms = new ConcurrentHashMap<String, ThreadLocal<DateFormat>>();

    private DateUtil(final String format) {}

    private static DateFormat getDateFormat(final String format) {
        ThreadLocal<DateFormat> l = ms.get(format);
        if (l == null) {
            l = new ThreadLocal<DateFormat>() {
                protected synchronized DateFormat initialValue() {
                    return new SimpleDateFormat(format);
                }
            };
            ms.put(format, l);
        }
        return l.get();
    }

    public static String format(String format, Date date) {
        if (date == null) {
            return "";
        }
        return getDateFormat(format).format(date);
    }

    public static Date formatDate(String format, Date date) {
        if (date == null) {
            return null;
        }
        try {
            return parse(format, format(format, date));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static String format(Date date) {
        return format("yyyy-MM-dd HH:mm:ss:SSS", date);
    }

    public static void print(Date date) {
        System.out.println(format(date));
    }

    public static String formatNormal(Date date) {
        return format(FORMAT_DATE_TIME, date);
    }

    public static Date parse(String format, String date) throws ParseException {

        return getDateFormat(format).parse(date);
    }

    /**
     * 获取x年x月的最大天数
     * @param year
     * @param month
     * @return
     */
    public static int getMaxDayOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, --month);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 日前增加指定单位的数量
     * @param sDate
     * @param unit
     * @param amount
     * @return
     */
    public static Date add(Date sDate, DateUnits unit, int amount) {
        Calendar tDate = Calendar.getInstance();
        tDate.setTime(sDate);
        switch (unit) {
            case Y: // 年
                tDate.add(Calendar.YEAR, amount);
                return tDate.getTime();
            case M: // 月
                tDate.add(Calendar.MONTH, amount);
                return tDate.getTime();
            case D: // 日
                tDate.add(Calendar.DATE, amount);
                return tDate.getTime();
            case H: // 小时
                tDate.add(Calendar.HOUR, amount);
                return tDate.getTime();
            case MI: // 分钟
                tDate.add(Calendar.MINUTE, amount);
                return tDate.getTime();
            case S: // 秒
                tDate.add(Calendar.SECOND, amount);
                return tDate.getTime();
            default:
                break;
        }
        return null;
    }

    /**
     * 秒转换成 时分秒格式字符串 HH:mm:ss
     * @param seconds
     * @return
     */
    public static String second2HMS(long seconds) {
        if (seconds == 0) return "";
        long h1 = seconds / 60;
        return h1 / 60 + ":" + h1 % 60 + ":" + seconds % 60;

    }
    
    /**
	 * 秒转换成 时分秒格式字符串 HH小时mm分钟ss秒
	 * 
	 * @param seconds
	 * @return
	 */
	public static String second2HMSMan(long seconds) {
		if (seconds == 0)
			return "";
		long h1 = seconds / 60;
		return h1 / 60 + "小时" + h1 % 60 + "分钟" + seconds % 60 + "秒";

	}

    /**
     * 间隔多少秒的绝对值
     * @param sDate
     * @param eDate
     * @return
     * @throws ParseException
     */
    public static long betweenAbsSecond(Date sDate, Date eDate) {
        if (sDate == null || eDate == null) return 0;

        if (sDate == null || eDate == null) return 0;
        return Math.abs((int) ((eDate.getTime() - sDate.getTime()) / 1000));
    }
    
    /**
     * 间隔多少秒，可能为负值
     * @param sDate
     * @param eDate
     * @return
     * @throws ParseException
     */
    public static long betweenSecond(Date sDate, Date eDate) {
        if (sDate == null || eDate == null) return 0;

        if (sDate == null || eDate == null) return 0;
        return (eDate.getTime() - sDate.getTime()) / 1000;
    }

    /**
     * 两个日期间隔多少天（1~3号返回2） eDate-sDate 返回相隔天数的绝对值
     * @param sDate
     * @param eDate
     */
    public static int betweenAbsDays(Date sDate, Date eDate) throws ParseException {
        if (sDate == null || eDate == null) return 0;
        Date start = getDateFormat(FORMAT_DATE1).parse(format(FORMAT_DATE1, sDate));
        Date end = getDateFormat(FORMAT_DATE1).parse(format(FORMAT_DATE1, eDate));
        return Math.abs((int) ((end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24L)));
    }

    /**
     * 两个日期间隔多少天（1~3号返回2） eDate - sDate 可能为负数
     * @param sDate
     * @param eDate
     */
    public static int betweenDays(Date sDate, Date eDate) throws ParseException {
        if (sDate == null || eDate == null) return 0;
        Date start = getDateFormat(FORMAT_DATE1).parse(format(FORMAT_DATE1, sDate));
        Date end = getDateFormat(FORMAT_DATE1).parse(format(FORMAT_DATE1, eDate));
        return (int) ((end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24L));
    }

    public static int betweenYears(Date sDate, Date eDate) throws ParseException {
        if (sDate == null || eDate == null) return 0;
        Date start = getDateFormat(FORMAT_DATE1).parse(format(FORMAT_DATE1, sDate));
        Date end = getDateFormat(FORMAT_DATE1).parse(format(FORMAT_DATE1, eDate));
        return Math.abs((int) ((end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24 * 365L)));
    }

    public static int betweenMonth(Date sDate, Date eDate) throws ParseException {
        if (sDate == null || eDate == null) return 0;

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(sDate);
        c2.setTime(eDate);

        int year = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
        // 开始日期若小月结束日期
        if (year < 0) {
            year = -year;
        }

        return year * 12 + c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
    }



    /**
     * 获取年的第一天
     * @param date
     * @return
     */
    public static Date getStartYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public static Date getEndYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);

        return cal.getTime();
    }

    /**
     * 获取给定时间的当年最早时间
     */
    public static Date getStartOfYear(Date date) {
        Calendar cal = Calendar.getInstance(StringUtil.getLocale());
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_YEAR, cal.getActualMinimum(Calendar.DAY_OF_YEAR));
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND));
        return cal.getTime();
    }

    /**
     * 获取给定时间的当年最后时间
     */
    public static Date getEndOfYear(Date date) {
        Calendar cal = Calendar.getInstance(StringUtil.getLocale());
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_YEAR, cal.getActualMaximum(Calendar.DAY_OF_YEAR));
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));
        return cal.getTime();
    }

    /**
     * 获取给定时间的当月最早时间
     */
    public static Date getStartOfMonth(Date date) {
        Calendar cal = Calendar.getInstance(StringUtil.getLocale());
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND));
        return cal.getTime();
    }

    /**
     * 获取给定时间的当月最后时间
     */
    public static Date getEndOfMonth(Date date) {
        Calendar cal = Calendar.getInstance(StringUtil.getLocale());
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));
        return cal.getTime();
    }

    /**
     * 获取给定时间的当天最早时间
     */
    public static Date getStartOfDay(Date date) {
        Calendar cal = Calendar.getInstance(StringUtil.getLocale());
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND));
        return cal.getTime();
    }

    /**
     * 获取给定时间的当天最后时间
     */
    public static Date getEndOfDay(Date date) {
        Calendar cal = Calendar.getInstance(StringUtil.getLocale());
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));
        return cal.getTime();
    }

    /**
     * 获取给定时间的小时最后时间
     */
    public static Date getEndOfHour(Date date) {
        Calendar cal = Calendar.getInstance(StringUtil.getLocale());
        cal.setTime(date);
        cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));
        return cal.getTime();
    }

    /**
     * 获取给定时间的5分钟最早时间
     */
    public static Date getStartOf5M(Date date) {
        Calendar cal = Calendar.getInstance(StringUtil.getLocale());
        cal.setTime(date);
        // 获取往前的第一个5分钟的节点
        int minNum = (cal.get(Calendar.MINUTE) / 5) * 5;
        cal.set(Calendar.MINUTE, minNum);
        cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND));
        return cal.getTime();
    }

    /**
     * 获取给定时间的5分钟最后时间
     */
    public static Date getEndOf5M(Date date) {
        // 获取最近的一个5分钟的开始时间
        Calendar cal = Calendar.getInstance(StringUtil.getLocale());
        Date latestStartOf5M = getStartOf5M(date);
        cal.setTime(latestStartOf5M);
        // 增加4分钟59秒999毫秒
        cal.add(Calendar.MINUTE, 4);
        cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));
        return cal.getTime();
    }

    /**
     * 获取给定时间的分钟最后时间
     */
    public static Date getEndOfMinute(Date date) {
        Calendar cal = Calendar.getInstance(StringUtil.getLocale());
        cal.setTime(date);
        cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));
        return cal.getTime();
    }

    /**
     * 获取给定时间的秒最后时间
     */
    public static Date getEndOfSecond(Date date) {
        Calendar cal = Calendar.getInstance(StringUtil.getLocale());
        cal.setTime(date);
        cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));
        return cal.getTime();
    }

    /**
     * 获取date本月第一天
     * @param date
     * @return
     */
    public static Date getMonthFirstDay(Date date) {
        // 获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        return c.getTime();

    }

    /**
     * 获取date本月第一天
     * @param date
     * @return yyyy-MM-dd
     */
    public static String getMonthFirstDayStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // 获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        String first = format.format(c.getTime());
        return first;

    }

    /**
     * 获取日期本月最后一天
     * @param date
     * @return yyyy-MM-dd
     */
    public static Date getMonthLastDay(Date date) {
        // 获取当前月最后一天
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        return ca.getTime();
    }

    /**
     * 返回日期所在月份最后一天
     * @param date
     * @return yyyy-MM-dd
     */
    public static String getMonthLastDayStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // 获取当前月最后一天
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = format.format(ca.getTime());
        return last;
    }

    /**
     * 获取指定月份的最后一天
     * @param year
     * @param mon
     * @return
     */
    public static Date getlastDayOfMon(int year, int mon) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, mon + 1);// 先设置为下个月到1号，然后时间减掉1天，得到本月的最后一天
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);

        return DateUtil.formatDate(DateUtil.FORMAT_DATE1, cal.getTime());
    }

    /**
     * 是否是月份的最后一天
     * @param date
     * @return
     */
    public static boolean isLastDayOfMon(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int mon = cal.get(Calendar.MONTH);
        cal.set(Calendar.MONTH, mon + 1);// 先设置为下个月到1号，然后时间减掉1天，得到本月的最后一天
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return day == cal.get(Calendar.DAY_OF_MONTH);
    }

    public static Date getDate(int year, int mon, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, mon);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        return cal.get(Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        return cal.get(Calendar.MONTH) + 1;
    }

    public static int getDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取日期所在周 第一天
     * @param date
     * @return
     */
    public static String getWeekFirstDay(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // 获取本周一的日期
        String first = df.format(cal.getTime());
        System.out.println("********得到本周一的日期*******" + df.format(cal.getTime()));
        return first;

    }

    public static Date getAfter(Date date, int field, int days) {
        Calendar startC = Calendar.getInstance();
        startC.setTime(date);
        startC.add(field, days);
        return startC.getTime();
    }

    /**
     * 获取日期所在周 最后一天
     * @param date
     * @return
     */
    public static String getWeekLastDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        // 这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        // 增加一个星期，才是我们中国人理解的本周日的日期
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        String last = df.format(cal.getTime());
        System.out.println("********得到本周天的日期*******" + df.format(cal.getTime()));
        return last;
    }
    
    /**
     * 获取当前时间的前(后)多少年时间
     * @param date
     * @param num 负数为前多少年，正数为后多少年时间
     * @return
     */
    public static Date getBeforeOrAfterYear(Date date, Integer num){
    	 Calendar cal = Calendar.getInstance(StringUtil.getLocale());
    	 cal.setTime(date);
    	 cal.add(Calendar.YEAR, num);
    	 return DateUtil.formatDate(DateUtil.FORMAT_DATE_TIME, cal.getTime());
    }
    
    /**
     * 获取当前时间的前(后)多少天时间
     * @param date
     * @param num 负数为前多少天，正数为后多少天时间
     * @return
     */
    public static Date getBeforeOrAfterDay(Date date, Integer num){
         Calendar cal = Calendar.getInstance(StringUtil.getLocale());
         cal.setTime(date);
         cal.add(Calendar.DATE, num);
         return DateUtil.formatDate(DateUtil.FORMAT_DATE_TIME, cal.getTime());
    }

    /**
     * 获取零点时间字符串<br>
     * default formatter:yyyy-MM-dd HH:mm:ss
     * @param time
     * @return yyyy-MM-dd 00:00:00
     * @throws ParseException
     */
    public static String getDate(String time) throws ParseException {
        return getDate(time, DEFULT_DATE_FORMATTER);
    }

    /**
     * 获取零点时间字符串<br>
     * @param time
     * @param formatter
     *        default:yyyy-MM-dd HH:mm:ss
     * @return yyyy-MM-dd 00:00:00
     * @throws ParseException
     */
    public static String getDate(String time, String formatter) throws ParseException {
        if (CommonUtil.isEmpty(formatter)) formatter = DEFULT_DATE_FORMATTER;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parse(formatter, time));
        String date = DateUtil.format(formatter, getDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)));
        return date;
    }

    public static String getDate(Date time, String formatter) throws ParseException {
        if (CommonUtil.isEmpty(formatter)) formatter = DEFULT_DATE_FORMATTER;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        String date = DateUtil.format(formatter, getDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)));
        return date;
    }

    /**
     * 返回格式yyyyMMddHHmmssSSS
     * @return
     */
    public static String getCurrentDateTimeStr() {
        try {
            return getDate(new Date(), FORMAT_DATE_TIME_S1);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }



    public static String getCurrentDateTimeStr(String format) {
        if (format == null || "".equals(format)) format = FORMAT_DATE_TIME_S2;
        return format(format, new Date());

    }

    /**
     * 取得当前时间
     * @return 当前时间
     */
    public static Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }
    
    /**
     * <p>Description: 获取当前系统时间，毫秒为000; eg:2018-06-06 10:47:50.000</p>  
     *
     * @return Date
     * @author Shuaibing.zhao
     * @date 2018年6月6日 上午11:00:55
     */
    public static Date getCurrentDateOfMS() {
        Calendar cal = Calendar.getInstance(StringUtil.getLocale());
        Date date = cal.getTime();
        cal.setTime(date);
        cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND));
        return cal.getTime();
    }

    public static boolean isSameDay(Date date1, Date date2) {
    	if(date1 == null || date2 == null){
    		return false;
    	}
        Date day1 = DateUtil.formatDate(DateUtil.FORMAT_DATE1, date1);
        Date day2 = DateUtil.formatDate(DateUtil.FORMAT_DATE1, date2);

        return day1.equals(day2);
    }

    /**
     * 此方法将给出的和日期格式化成本地日期形式的字符串。(只含有年月日)
     * @return 本地日期形式的字符串。例：1983-12-29
     * @since 0.1
     */
    public static String formatDate2NN(Date date) {
        StringBuffer dateBuffer = new StringBuffer("");
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            dateBuffer.append(calendar.get(Calendar.YEAR) + "-");
            dateBuffer.append((calendar.get(Calendar.MONTH) + 1) + "-");
            dateBuffer.append(calendar.get(Calendar.DAY_OF_MONTH));
        }
        return dateBuffer.toString();
    }

    /**
     * 此方法将给出的和日期格式化成本地日期形式的字符串。(只含有年月日)
     * 月，日小于10补0
     * @return 本地日期形式的字符串。例：1983-12-29
     * @since 0.1
     */
    public static String formatDate2NNEnhance(Date date) {
        StringBuffer dateBuffer = new StringBuffer("");
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            dateBuffer.append(calendar.get(Calendar.YEAR) + "-");

            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String monthStr = formatNum(month) + "-";
            String dayStr = formatNum(day);
            dateBuffer.append(monthStr);
            dateBuffer.append(dayStr);
        }
        return dateBuffer.toString();
    }

    /**
     * 此方法将给出的和日期格式化成本地日期形式的字符串。(含有年月日时分)
     * @return 本地日期形式的字符串。例：1983-12-29 08:05
     * @since 0.1
     */
    public static String formatDateTime2NN(Date date) {
        StringBuffer dateBuffer = new StringBuffer("");
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            dateBuffer.append(calendar.get(Calendar.YEAR) + "-");
            dateBuffer.append((calendar.get(Calendar.MONTH) + 1) + "-");
            dateBuffer.append(calendar.get(Calendar.DAY_OF_MONTH) + " ");
            dateBuffer.append(formatNumber(calendar.get(Calendar.HOUR_OF_DAY), 2, '0') + ":");
            dateBuffer.append(formatNumber(calendar.get(Calendar.MINUTE), 2, '0'));
        }
        return dateBuffer.toString();
    }

    /**
     * 此方法将给出的和日期格式化成本地日期形式的字符串。 （含有年月日时分秒)
     * @return 本地日期形式的字符串。例：1983-12-29 08:05:12
     * @since 0.1
     */
    public static String formatFullDateTime2NN(Date date) {
        StringBuffer dateBuffer = new StringBuffer("");
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            dateBuffer.append(calendar.get(Calendar.YEAR) + "-");
            dateBuffer.append((calendar.get(Calendar.MONTH) + 1) + "-");
            dateBuffer.append(calendar.get(Calendar.DAY_OF_MONTH) + " ");
            dateBuffer.append(formatNumber(calendar.get(Calendar.HOUR_OF_DAY), 2, '0') + ":");
            dateBuffer.append(formatNumber(calendar.get(Calendar.MINUTE), 2, '0') + ":");
            dateBuffer.append(formatNumber(calendar.get(Calendar.SECOND), 2, '0'));
        }
        return dateBuffer.toString();
    }

    /**
     * @param temp
     * @return java.lang.String
     * @roseuid 44D6EB7800CF
     */
    private static String formatNum(int temp) {
        String reStr;
        if (temp < 10) {
            reStr = "0" + temp;
        } else {
            reStr = "" + temp;
        }
        return reStr;
    }

    private static String formatNumber(int number, int destLength, char paddedChar) {
        String oldString = String.valueOf(number);
        StringBuffer newString = new StringBuffer("");
        int oldLength = oldString.length();
        if (oldLength > destLength) {
            newString.append(oldString.substring(oldLength - destLength));
        } else if (oldLength == destLength) {
            newString.append(oldString);
        } else {
            for (int i = 0; i < destLength - oldLength; i++) {
                newString.append(paddedChar);
            }
            newString.append(oldString);
        }
        return newString.toString();
    }

    /**
     * 随机指定范围内N个不重复的数
     * 最简单最基本的方法
     * @param min 指定范围最小值
     * @param max 指定范围最大值
     * @param n 随机数个数
     */
    public static String[] randomCommon(int min, int max, int n) {
        if (n > (max - min + 1) || max < min) {
            return null;
        }
        String[] resultStr = new String[n];
        int[] result = new int[n];
        int count = 0;
        while (count < n) {
            int num = (int) (Math.random() * (max - min)) + min;
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if (num == result[j]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                result[count] = num;
                String temp = String.valueOf(num);
                while (temp.length() < 4) {
                    temp = "0" + temp;

                }
                resultStr[count] = temp;
                count++;
            }
        }

        return resultStr;
    }

    public static void main(String[] args) throws InterruptedException {
//        int max = 9999;
//        int min = 0;
//        int n = 0;
//        String[] random = randomCommon(min, max, n);
//        for (int i = 0; i < random.length; i++) {
//            System.out.println(random[i]);
//        }
    	System.out.println(DateUtil.format(FORMAT_DATE4, DateUtil.getCurrentDate()));

    }
    
    public static String getDatePoor(Date endDate, Date nowDate) {
    	 
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }
    
}
