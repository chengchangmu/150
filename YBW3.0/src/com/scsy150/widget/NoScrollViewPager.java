package com.scsy150.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 不能左右滑动的ViewPager
 * 
 * @author K
 * 
 */
public class NoScrollViewPager extends ViewPager {

	public NoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoScrollViewPager(Context context) {
		super(context);
	}

	// 事件拦截, 返回false,不拦截事件,保证ViewPager下嵌套的子ViewPager可以响应滑动事件
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// 在触摸事件中什么都不做, 从而禁用Viewpager的滑动效果
		return true;
	}

}
