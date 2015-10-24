package com.scsy150.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
/*
 * Copyright (C) 2015 四川硅谷科技重庆分公司
 * 版权所有
 *
 * 功能描述：此viewpager用于查看图片时可缩放，但缩小时用普通viewpager会有异常
 * 作者：硅谷科技
 * 创建时间：2014-3-26
 *
 * 修改人：
 * 修改描述：
 * 修改日期
*/
public class ScaleViewPager extends ViewPager {

	public ScaleViewPager(Context context){
		super(context);
	}

	public ScaleViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		try {
			return super.onInterceptTouchEvent(ev);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return false;
		}
	}

}
