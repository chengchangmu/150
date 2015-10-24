package com.scsy150.util;

import android.content.Context;

public class NativeApi {
	private static NativeApi nativapi;

	static {
		System.loadLibrary("NativeApi");
	}

	public native String getSign(Context context, String data);

	public native String getIMEI(Context context);

	public static NativeApi getInstance() {
		return nativapi = new NativeApi();
	}
}
