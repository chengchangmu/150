package com.scsy150.util;

import android.content.Context;
import android.view.WindowManager;

/**
 * 屏幕相关工具类
 * @author K
 *
 */
public class ScreenUtil {
	
	private static WindowManager mWm;

	public static int getScreenWidth(Context context) {
		mWm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		return mWm.getDefaultDisplay().getWidth();
	}
	
	public static int getScreenHeight(Context context) {
		mWm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		return mWm.getDefaultDisplay().getHeight();
	}
}
