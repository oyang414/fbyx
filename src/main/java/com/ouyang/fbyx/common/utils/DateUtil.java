package com.ouyang.fbyx.common.utils;


import com.ouyang.fbyx.common.exception.BaseRuntimeException;
import org.joda.time.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description 时间操作工具类
 * @author ouyangxingjie
 * @date 2021/6/29 21:21
 */
public class DateUtil extends DateTimeUtils {
	/**
	 * 短日期格式
	 */
	public final static String SHORTDATEFORMATER = "yyyy-MM-dd";

	public final static String SHORTTIMEFORMATER = "HH:mm:ss";
	public final static String SHORTDATETIMEFORMATER = "HHmmss";
	public final static String SHORTDATEFDORMATER = "yyyyMMdd";

	public final static String LONGDATEFORMATER = "yyyy-MM-dd HH:mm:ss";
	
	public final static String LONGDATEFORMATER_YYYYMMDDHHMM = "yyyy-MM-dd HH:mm";
	/**
	 * Solr中使用的日期格式
	 */
	public static final String SOLR_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:sss'Z'";
	
	public static final long DAYTIMESTAMP = 24 * 60 * 60 * 1000L;

	/**
	 * yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010
	 */
	public static final String LONGDATE_FORMAT="yyyyMMddHHmmss";
	private static Logger log = LoggerFactory.getLogger(DateUtil.class);

	/**
	 * @param datePattern
	 *            格式
	 * @return 返回日期格式字符串（yyyy-mm-dd HH:mm:ss）
	 */
	public static final String getDateByDatePattern(String datePattern) {
		return convertDateToString(datePattern, new Date());
	}

	/**
	 * 把时间装成指定的字符串
	 * @param pattern 转换格式
	 * @param date 日期
	 * @return 时间字符串
	 */
	public static final String convertDateToString(String pattern, Date date) {
		if (date == null){
			return null;
		}
		return new SimpleDateFormat(pattern).format(date);
	}

	/**
	 * 把时间装成指定的字符串 (yyyy-MM-dd HH:mm:ss)
	 *
	 * @param date
	 *            日期
	 * @return 时间字符串
	 */
	public static final String convertDateToString(Date date) {
		return convertDateToString(LONGDATEFORMATER, date);
	}
	/**
	 * 把时间装成指定的字符串 HHmmss，如2009年12月25日9点10分10秒表示为091010
	 *
	 * @param date
	 *            日期
	 * @return 时间字符串
	 */
	public static final String convertDateTimeShortToString(Date date) {
		return convertDateToString(SHORTDATETIMEFORMATER, date);
	}

	/**
	 * 将指定日期转成yyyy-MM-dd格式的字符串
	 * @param date 日期
	 * @return
	 * @throws
	 * @author kuangzhiqiang Email:kuangzhiqiang@co-mall.com
	 * @date 2018-05-31
	 */
	public static final String toShortString(Date date) {
		return convertDateToString(SHORTDATEFORMATER, date);
	}

	/**
	 * 把时间装成指定的字符串 yyyyMMdd，如2009年12月25日9点10分10秒表示为091010
	 *
	 * @param date
	 *            日期
	 * @return 时间字符串
	 */
	public static final String convertDateTShortToString(Date date) {
		return convertDateToString(SHORTDATEFDORMATER, date);
	}
	/**
	 * 把时间装成指定的字符串 yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010
	 *
	 * @param date
	 *            日期
	 * @return 时间字符串
	 */
	public static final String convertDateTimeToString(Date date) {
		return convertDateToString(LONGDATE_FORMAT, date);
	}

	/**
	 * 把时间字符串转成指定格式的时间
	 * @param pattern 转换格式
	 * @param source 时间字符串
	 * @return date
	 * @throws ParseException
	 */
	public static final Date convertStringToDate(String pattern, String source) {
		if (StringUtil.isBlank(source)){
			return null;
		}
		try {
			return new SimpleDateFormat(pattern).parse(source);
		} catch (ParseException pe) {
			log.info("{}",pe);
			throw new BaseRuntimeException("日期转换失败，请注意日期格式");
		}
	}

	/**
	 * 获取指定日期格式（yyyy-mm-dd ）的当天开始时间
	 * @param dateStr yyyy-mm-dd
	 * @return 带有时分秒的 yyyy-mm-dd 00:00:00
	 */
	public static final Date convertStringToBeginOfDay(String dateStr){
		if( StringUtil.isBlank(dateStr) ){
			return null;
		}
		dateStr=dateStr.trim()+" 00:00:00";
	    return convertStringToDate(LONGDATEFORMATER,dateStr);
	}

	/**
	 * 获取指定日期格式（yyyy-mm-dd ）的当天结束时间
	 * @param dateStr yyyy-mm-dd
	 * @return 带有时分秒的 yyyy-mm-dd 23:59:59
	 */
	public static final Date convertStringToEndOfDay(String dateStr){
		if( StringUtil.isBlank(dateStr) ){
			return null;
		}
		dateStr=dateStr.trim()+" 23:59:59";
		return convertStringToDate(LONGDATEFORMATER,dateStr);
	}

	public static final Date convertStringToDate(String source) {
		return convertStringToDate(LONGDATEFORMATER, source);
	}

	public static final Date convertStringToDateByLength(String source) {
		if (StringUtil.isBlank(source)){
			return null;
		}
		try {
			return new SimpleDateFormat(source.length() > 10 ? LONGDATEFORMATER : SHORTDATEFORMATER).parse(source);
		} catch (ParseException pe) {
			log.info("{}",pe);
			throw new BaseRuntimeException("日期转换失败，请注意日期格式");
		}
	}

	/**
	 * 把时间字符串装成日期
	 *
	 * @param date
	 *            可传（yyyy-mm-dd HH:mm:ss）或者（yyyy-mm-dd）
	 * @return
	 */
	public static final Date getDateTime(String date) {
		return convertStringToDate(LONGDATEFORMATER, date);
	}

	/**
	 * 判断给定日期是否未过期
	 * @param givenDate 给定日期
	 * @return
	 */
	public static final boolean isNonExpired(Date givenDate){
		Calendar calendarNow = Calendar.getInstance();
		calendarNow.setTime(calendarNow.getTime());
		Calendar calendarGiven = Calendar.getInstance();
		calendarGiven.setTime(givenDate);
		return calendarNow.before(calendarGiven);
	}

	/**
	 * 判断给定日期是否过期
	 * @param givenDate 给定日期
	 * @return
	 */
	public static final boolean isExpired(Date givenDate){
		Calendar calendarNow = Calendar.getInstance();
		calendarNow.setTime(calendarNow.getTime());
		Calendar calendarGiven = Calendar.getInstance();
		calendarGiven.setTime(givenDate);
		return calendarNow.after(calendarGiven);
	}

	/**
	 * 判断两个日期大小
	 *
	 * @param firstDate  第一个日期参数
	 * @param secondDate 第二个日期参数
     * @return int 如果 第一个日期参数大于第二个日期 返回 1
	 * 			    如果 两个日期相等 返回0
	 * 			    如果 第一个日期小于第二个日期 返回-1
     */
	public static final int compare( Date firstDate,Date secondDate ){
		return firstDate.compareTo(secondDate);
	}

	public static final Date getDate(){
		Calendar calendarNow = Calendar.getInstance();
		calendarNow.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		return calendarNow.getTime();
	}
	
	public static final Date getShortDate(){
		return convertStringToDate(SHORTDATEFORMATER, convertDateToString(getDate()));
	}

	/**
	 * 将{@link Date}类型转换为Solr可以使用的字符串
	 * @param date 要转换的日期
	 * @return
	 */
	public static String formatSolrDateString(Date date){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SOLR_DATE_FORMAT);
		return simpleDateFormat.format(date);
	}

	public static Date DateMinus(Date date,int month){
		  Calendar calendar = Calendar.getInstance();
		  calendar.setTime(date);
		  calendar.add(Calendar.MONTH, -month);
		return calendar.getTime();
	}

	/**
	 * 获取指定日期之前指定天,包含传入的那一天
	 * @param date 指定的日期
	 * @param days 指定的天数
	 * @return
	 */
	public static String getDaysBefore(Date date, int days) {
		Date td = new Date(date.getTime() - DAYTIMESTAMP * days);
		return DateUtil.convertDateToString(SHORTDATEFORMATER, td);
	}

	/**
	 * 获取指定日期之前指定天,包含传入的那一天
	 * @param dateStr 指定的日期的字符串格式
	 * @param days 指定的天数
	 * @return
	 */
	public static String getDaysBefore(String dateStr, int days) {
		Date date = convertStringToDate(SHORTDATEFORMATER, dateStr);
		return DateUtil.getDaysBefore(date, days);
	}

	/**
	 * 获取指定日期之前指定天的数组,包含传入的那一天
	 * @param date 指定的日期
	 * @param days 指定的天数
	 * @return
	 */
	public static List<String> getDaysBeforeArray(Date date, int days){
		List<String> resultList = new ArrayList<>();
		for (int i = days-1; i >= 0; i--) {
			resultList.add(getDaysBefore(date, i));
		}
		return resultList;
	}

	/**
	 * 获取指定日期之前指定天的数组,包含传入的那一天
	 * @param dateStr 指定的日期的字符串格式
	 * @param days 指定的天数
	 * @return
	 */
	public static List<String> getDaysBeforeArray(String dateStr, int days){
		List<String> resultList = new ArrayList<>();
		for (int i = days-1; i >= 0; i--) {
			resultList.add(getDaysBefore(dateStr, i));
		}
		return resultList;
	}

	/**
	 * 获取指定日期之前指定天,包含传入的那一天
	 * @param date 指定的日期
	 * @param days 指定的天数
	 * @return
	 */
	public static String getDaysAfter(Date date, int days) {
		Date td = new Date(date.getTime() + DAYTIMESTAMP * days);
		return DateUtil.convertDateToString(SHORTDATEFORMATER, td);
	}

	/**
	 * 获取指定日期之前指定天,包含传入的那一天
	 * @param dateStr 指定的日期的字符串格式
	 * @param days 指定的天数
	 * @return
	 */
	public static String getDaysAfter(String dateStr, int days) {
		Date date = convertStringToDate(SHORTDATEFORMATER, dateStr);
		return DateUtil.getDaysAfter(date, days);
	}

	/**
	 * 获取指定日期后七天日期
	 * @param date 指定的日期
	 * @return
	 */
	public static Date getSevenDaysBefore(Date date) {
		return new Date(date.getTime() + DAYTIMESTAMP * 7);
	}


	/**
	 * 获取指定日期之后指定天的数组,包含传入的那一天
	 * @param date 指定的日期的字符串格式
	 * @param days 指定的天数
	 * @return
	 */
	public static List<String> getDaysAfterArray(Date date, int days){
		List<String> resultList = new ArrayList<>();
		for (int i = 0; i < days; i++) {
			resultList.add(getDaysAfter(date, i));
		}
		return resultList;
	}

	/**
	 * 获取指定日期之后指定天的数组,包含传入的那一天
	 * @param dateStr 指定的日期的字符串格式
	 * @param days 指定的天数
	 * @return
	 */
	public static List<String> getDaysAfterArray(String dateStr, int days){
		List<String> resultList = new ArrayList<>();
		for (int i = 0; i < days; i++) {
			resultList.add(getDaysAfter(dateStr, i));
		}
		return resultList;
	}

	/**
	 * 获取当天0点
	 * @return
	 */
	public static Date atStartOfDay(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取当天23：59：59
	 * @return
	 */
	public static Date atEndOfDay(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}

	/**
	 * 根据时间字符串和秒，给时间设置指定的秒
	 * @param timeStr
	 * @param second
	 * @return
	 */
	public static String setSecond(String timeStr,int second){
		String returnStr="";
		Date d=convertStringToDate(LONGDATEFORMATER,timeStr);
		if(d!=null){
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.set(Calendar.SECOND,second);
			returnStr=convertDateToString(LONGDATEFORMATER,cal.getTime());
		}
		return returnStr;
	}

	/**
	 * 获取当时时间前一个小时时间
	 *
	 * @return
	 */
	public static Date getDayBeforeHour(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY,calendar.get(Calendar.HOUR_OF_DAY)-1);
		return calendar.getTime();
	}
	
	
	/**
	 * 剪切时间段为时间字符串数组
	 * @param beginDateStr
	 * @param endDateStr
	 * @return
	 */
	public static List<String> getStringArray(String beginDateStr,String endDateStr){
		Date beginDate = convertStringToDate(beginDateStr);
		Date endDate = convertStringToDate(endDateStr);
		long diff = endDate.getTime() - beginDate.getTime();
		long temps = 1000 * 60 * 60L;
		long hour = diff / temps;
		List<String> list = new ArrayList<String>();
		if(hour>0){		
			list.add(convertDateToString(beginDate));
			for(int i =1 ;i<hour;i++){
				Date d = new Date(beginDate.getTime()+(temps*i));
				list.add(convertDateToString(d));
			}
			list.add(convertDateToString(endDate));
		}else if(hour == 0){
			list.add(convertDateToString(beginDate));
		}
		return list;
		
	}

	/**
	 * 设置分秒为：59：59
	 * @return
	 */
	public static Date setMinAndSecond(String time){
		Calendar calendar = Calendar.getInstance();
		Date d=convertStringToDate(LONGDATEFORMATER,time);
		calendar.setTime(d);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}
}
