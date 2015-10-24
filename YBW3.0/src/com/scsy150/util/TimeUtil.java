package com.scsy150.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.scsy150.consts.SystemConsts;

/*
 * Copyright (C) 
 * 版权所有
 *
 * 功能描述：时间工具类
 * 作者：硅谷科技
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */
public class TimeUtil {

	public static String YYYY_MM_DD = "yyyy-MM-dd";

	/**
	 * 返回 时间key
	 * 
	 * @return
	 */
	public static String getSignKey() {
		Date date = new Date();
		return MD5Util.string2MD5(TimeUtil.formatDate(date.getTime(),
				"yyyy-MM-dd") + SystemConsts.SIGN_KEY);
	}

	/**
	 * 时间字符串格式化
	 * 
	 * @param date
	 *            需要被处理的日期字符串
	 * @param parseStr
	 *            需要被处理的日期的格式串
	 * @param formatStr
	 *            最终返回的日期字符串的格式串
	 * @return 已经格式化的日期字符串
	 */
	public static String formatDate(long date, String formatStr) {
		if (date <= 0) {
			return "";
		}
		DateFormat sdf = new SimpleDateFormat(formatStr);
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(date);
			return sdf.format(calendar.getTime());
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String getWeak(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		String week = "星期日";
		switch (calendar.get(Calendar.DAY_OF_WEEK)) {
		case 1:
			week = "星期日";
			break;
		case 2:
			week = "星期一";
			break;
		case 3:
			week = "星期二";
			break;
		case 4:
			week = "星期三";
			break;
		case 5:
			week = "星期四";
			break;
		case 6:
			week = "星期五";
			break;
		case 7:
			week = "星期六";
			break;
		default:
			break;
		}
		return week;
	}

	/**
	 * 字符时间转换成Date
	 * 
	 * @param value
	 * @param format
	 *            需要的Date格式
	 * @return
	 */
	public static Date strToDate(String value, String format) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			Date strtodate = formatter.parse(value);
			return strtodate;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static long strToMillis(String time) {
		Date date = strToDate(time, "yyyy-MM-dd");
		if (date != null) {
			return date.getTime();
		} else {
			return 0;
		}
	}

	/**
	 * 比较两个日期大小，如果第一个小于第二个返回true，
	 * 
	 * @param first
	 * @param second
	 * @return
	 */
	public static boolean compareDate(String first, String second) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Date firstDate;
		try {
			firstDate = sdf.parse(first);
			Date secondDate = sdf.parse(second);
			return firstDate.before(secondDate);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 比较两个日期的大小（前者大 返回true,比较到分钟数）
	 * 
	 * @param one
	 * @param two
	 * @return
	 */
	public static boolean compareDate(Date one, Date two) {
		if (one.getYear() > two.getYear()) {
			return true;
		} else if (one.getYear() == two.getYear()
				&& one.getMonth() > two.getMonth()) {
			return true;
		} else if (one.getYear() == two.getYear()
				&& one.getMonth() == two.getMonth()
				&& one.getDate() > two.getDate()) {
			return true;
		} else if (one.getYear() == two.getYear()
				&& one.getMonth() == two.getMonth()
				&& one.getDate() == two.getDate()
				&& one.getHours() > two.getHours()) {
			return true;
		} else if (one.getYear() == two.getYear()
				&& one.getMonth() == two.getMonth()
				&& one.getDate() == two.getDate()
				&& one.getHours() == two.getHours()
				&& one.getMinutes() > two.getMinutes()) {
			return true;
		} else {
			return false;
		}
	}

	public static String formatDate(String date, String yyyy_mm_dd) {
		SimpleDateFormat formatter = new SimpleDateFormat(yyyy_mm_dd);
		return formatter.format(new Date(date).getTime());
	}
}
