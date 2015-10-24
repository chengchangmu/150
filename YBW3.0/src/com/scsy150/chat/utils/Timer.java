package com.scsy150.chat.utils;

import android.content.Context;
import android.os.Handler;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.scsy150.base.YBWApplication;
import com.scsy150.consts.SystemConsts;
import com.scsy150.util.LogUtil;
import com.scsy150.util.SharedPreferencesUtil;

public class Timer {
	private final static String TAG = "Timer";
	private static Handler handler = new Handler();
	private static int TIME = 180 * 1000;// 3分钟

	/**
	 * 没有连接，几分钟后自动连接
	 */
	public static void connectChat(final Context context) {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// handler自带方法实现定时器
				try {
					handler.postDelayed(this, TIME);
					loginChat(context);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		if (!YBWApplication.isLoginChat) {
			handler.postDelayed(runnable, TIME); // 每隔3分钟执行
		}
	}

	public static void loginChat(Context context) {

		SharedPreferencesUtil spUtil = new SharedPreferencesUtil(context);
		String name = spUtil.getString(SystemConsts.USER_NAME, "");
		String pwd = spUtil.getString(SystemConsts.USER_PWD, "");
		// 调用sdk登陆方法登陆聊天服务器
		EMChatManager.getInstance().login(name, pwd, new EMCallBack() {// 回调
					@Override
					public void onSuccess() {

						new Runnable() {
							public void run() {
								EMGroupManager.getInstance().loadAllGroups();
								EMChatManager.getInstance()
										.loadAllConversations();
								YBWApplication.isLoginChat = true;
								LogUtil.d(TAG, "登陆聊天服务器成功！");
							}
						};
					}

					@Override
					public void onProgress(int progress, String status) {

					}

					@Override
					public void onError(int code, String message) {
						YBWApplication.isLoginChat = false;
						LogUtil.d(TAG, "登陆聊天服务器失败！");
					}
				});
	}

}
