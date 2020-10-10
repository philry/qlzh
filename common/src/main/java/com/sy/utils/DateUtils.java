package com.sy.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 * 
 * @author ruoyi
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils
{
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    
    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     * 
     * @return Date() 当前日期
     */
    public static Date getNowDate()
    {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     * 
     * @return String
     */
    public static String getDate()
    {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime()
    {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow()
    {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format)
    {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date)
    {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date)
    {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts)
    {
        try
        {
            return new SimpleDateFormat(format).parse(ts);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            return parseDate(str.toString(), parsePatterns);
        }
        catch (ParseException e)
        {
            return null;
        }
    }
    
    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate()
    {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate)
    {
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
    
    public static String getLastMonthEnd() {
    	Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, -1);
		c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
		Date lastDateOfPrevMonth = c.getTime();
		return parseDateToStr(YYYY_MM_DD, lastDateOfPrevMonth);
    }
    
    public static String getPrevDay(Date now) {
    	Calendar c = Calendar.getInstance();
    	c.setTime(now);
		c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
		return parseDateToStr(YYYY_MM_DD, c.getTime());
    }
    
    public static String getPrevDay(String now) {
    	Date nowDay = parseDate(now);
    	return getPrevDay(nowDay);
    }
    
    public static String getNextDay(Date now) {
    	Calendar c = Calendar.getInstance();
    	c.setTime(now);
		c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);
		return parseDateToStr(YYYY_MM_DD, c.getTime());
    }
    
    public static Date getNextDay(String now) {
    	Date nowDay = parseDate(now);
    	return parseDate(getNextDay(nowDay));
    }


    
    public static String getTimeFromDate(Date date) {
    	String dateStr = parseDateToStr(YYYY_MM_DD_HH_MM_SS, date);
    	return dateStr.split(" ")[1];
    }
    
    public static Date getOneDayLastTime(Date date) {
    	Calendar c = Calendar.getInstance();
    	c.setTime(date);
    	c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
    	return c.getTime();
    }
    
    public static Date getOneDayLastTime(String date) {
    	Calendar c = Calendar.getInstance();
    	c.setTime(parseDate(date));
    	c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
    	return c.getTime();
    }

    public static String getMonthFirstDay(){
        Calendar cal_1= Calendar.getInstance();//获取当前日期
        String day = parseDateToStr(YYYY_MM_DD, cal_1.getTime());
        System.out.println(day.substring(8,10));
        if(day.substring(8,10)=="01"){
            return day;
        }else {
            cal_1.add(Calendar.MONTH, 0);
            cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天

            return parseDateToStr(YYYY_MM_DD, cal_1.getTime());
        }

    }

    public static long getTimes(Date date1, Date date2){

        return (date2.getTime()-date1.getTime())/(1000);
    }


    public static String getStringTime(Date date){


        return parseDateToStr(YYYY_MM_DD_HH_MM_SS,date);
    }



}
