package com.scsy150.widget.pulltorefresh;

import com.scsy150.util.LogUtil;



public class PullUtils {

	static final String LOG_TAG = "PullToRefresh";

	public static void warnDeprecation(String depreacted, String replacement) {
		LogUtil.w(LOG_TAG, "You're using the deprecated " + depreacted + " attr, please switch over to " + replacement);
	}

}
