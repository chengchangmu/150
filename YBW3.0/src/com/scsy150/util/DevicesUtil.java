package com.scsy150.util;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.os.Build;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class DevicesUtil {

	public static String getIMEI(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId();
		if (null == imei)
			return "";
		else
			return imei;
	}

	// sim卡唯一标识
	public static String getSIMEI(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String simei = tm.getSubscriberId();
		if (null == simei)
			return "";
		else
			return simei;
	}

	/**
	 * 获取手机号码
	 * 
	 * @param context
	 * @return
	 */
	public static String getPhoneNum(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String num = tm.getLine1Number();
		if (null == num)
			return "";
		else
			return num;
	}

	public static String getPhoneProvider(Context context) {
		String simei = getSIMEI(context);
		// IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
		if (simei.startsWith("4600001")) {
			return "中国联通";
		} else if (simei.startsWith("4600002")) {
			return "中国移动";
		} else if (simei.startsWith("4600003")) {
			return "中国电信";
		} else {
			return "";
		}
	}

	/**
	 * 获取手机型号
	 * 
	 * @return
	 */
	public static String getPhoneType() {
		return android.os.Build.MODEL;
	}

	/**
	 * 获取系统版本
	 * 
	 * @return
	 */
	public static String getSystemVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 获取系统版本Level
	 * 
	 * @return
	 */
	public static int getSystemVersionLevel() {
		return android.os.Build.VERSION.SDK_INT;
	}

	/**
	 * 手机品牌
	 * 
	 * @return
	 */
	public static String getPhoneName() {
		return Build.BRAND;
	}

	/**
	 * 获取当前系统可用内存
	 * 
	 * @param ctx
	 * @return
	 */
	public static String getAvailMemory(Context ctx) {
		ActivityManager am = (ActivityManager) ctx
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		// mi.availMem; 当前系统的可用内存
		return Formatter.formatFileSize(ctx, mi.availMem);// 将获取的内存大小规格化
	}

	/**
	 * 判断是否是平板
	 * 
	 * @param ctx
	 * @return
	 */
	public static boolean isPadType(Context ctx) {
		TelephonyManager telMg = (TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (telMg.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据view显示输入法
	 * 
	 * @param ctx
	 * @param view
	 */
	public static void showIMM(Context ctx, View view) {
		InputMethodManager imm = (InputMethodManager) ctx
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 隐藏输入法
	 * 
	 * @param ctx
	 * @param token
	 */
	public static void hideIMM(Context ctx, IBinder token) {
		InputMethodManager imm = (InputMethodManager) ctx
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(token,
				InputMethodManager.RESULT_UNCHANGED_SHOWN);
	}

}
