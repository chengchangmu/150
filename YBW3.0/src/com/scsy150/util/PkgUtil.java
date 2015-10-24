package com.scsy150.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

/*
 * Copyright (C) 
 * 版权所有
 *
 * 功能描述：软件包相关的工具类
 * 作者：硅谷科技
 * 创建时间：2015-08-26
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */
public class PkgUtil {

	/**
	 * 获取软件版本名称
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppVersionName(Context context) {
		PackageInfo info;
		try {
			info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取package名称
	 * 
	 * @param context
	 * @return
	 */
	public static String getPackageName(Context context) {
		PackageInfo info;
		try {
			info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return info.packageName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取软件版本code
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		PackageInfo info;
		try {
			info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
