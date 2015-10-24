package com.scsy150.meet.page;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import android.app.Activity;
import android.text.TextUtils;

/**
 * 根据全路径类名获取具有缓存功能的Pager实例的工厂
 * 
 * @author Administrator
 */
public class PageFactory {

	private static HashMap<String, BasePage> pagerCache = new HashMap<String, BasePage>();
	private static final String PCK_NAME = "com.scsy150.page.impl";

	@SuppressWarnings("unchecked")
	public static <T extends BasePage> T getImpl(Activity activity, String pagerName) {
		// 如果为null,则返回一个错误页面
		if (TextUtils.isEmpty(pagerName))
			pagerName = "ErrorPage";
		String FullPath = PCK_NAME + "." + pagerName;
		BasePage basePage = pagerCache.get(FullPath);
		try {
			if (basePage == null) {
				Class clazz = Class.forName(FullPath);
				/*if("OrderAllPage".equals(pagerName)) {
					return (T) clazz.newInstance();
				}*/
				Constructor constructor = clazz.getConstructor(Activity.class);
				basePage = (T) constructor.newInstance(activity);
				pagerCache.put(FullPath, (T)basePage);
			}
			return (T) basePage;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 默认错误页面
		// return (T) pagerCache.get(FullPath);
		return null;
	}

}