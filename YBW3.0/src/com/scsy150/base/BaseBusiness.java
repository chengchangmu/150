package com.scsy150.base;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Copyright (C) 2015 四川硅谷科技重庆分公司
 * 版权所有
 *
 * 功能描述：基本业务类，全局公共方法
 * 作者：硅谷科技
 * 创建时间：2014-3-20
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */

public class BaseBusiness {

	/**
	 * 用户注册
	 */
	public static final int CODE_USER_REGISTER = 1;
	/**
	 * 忘记密码
	 */
	public static final int CODE_FORGET_PWD = 2;

	/**
	 * 验证手机号是否合法
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean checkPhoneNum(String phone) {
		Pattern p = Pattern.compile("^1[3,4,5,7,8]\\d{9}$");
		Matcher m = p.matcher(phone);
		return m.matches();
	}

	/**
	 * 验证手机号是否支持短信验证 180,181不支持
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean checkPhoneNumCanAuth(String phone) {
		Pattern p = Pattern.compile("^1[8][0,1]\\d{8}$");
		Matcher m = p.matcher(phone);
		return !m.matches();
	}

	/**
	 * 电话号码转换为十六进制字符串
	 * 
	 * @param numStr
	 * @return
	 */
	public static String numToHexString(String numStr) {
		String hexString = "";
		try {
			hexString = Long.toHexString(Long.valueOf(numStr));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return hexString;
	}

}
