package com.scsy150.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class OnlyUpListView extends ListView {

	public OnlyUpListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public OnlyUpListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public OnlyUpListView(Context context) {
		super(context);
	}
	
	/**
	 * 事件分发
	 * 
	 * 1. 向下滑动请求父控件不拦截
	 */

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// 请求父控件和祖宗控件不要拦截事件
		//getParent().requestDisallowInterceptTouchEvent(true);
		float dx;
		float dy;
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			getParent().requestDisallowInterceptTouchEvent(true);
			break;
		case MotionEvent.ACTION_MOVE:
			getParent().requestDisallowInterceptTouchEvent(true);
			break;

		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
}
