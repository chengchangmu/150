package com.scsy150.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 可以控制ListView中事件的ViewPager
 * 
 * @author K
 * 
 */
public class OnlyHorizontalViewPager extends ViewPager {

	public OnlyHorizontalViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public OnlyHorizontalViewPager(Context context) {
		super(context);
	}

	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// 请求父控件和祖宗控件不要拦截事件
		float startX = 0;
		float startY = 0;
		float dx;  
		float dy;
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = ev.getRawX();
			startY = ev.getRawY();
			getParent().requestDisallowInterceptTouchEvent(true);
			break;
		case MotionEvent.ACTION_MOVE:
			float endX = ev.getRawX();
			float endY = ev.getRawY();
			
			dx = Math.abs(endX - startX);
			dy = Math.abs(endY - startY);
			if(dx > dy) {
				getParent().requestDisallowInterceptTouchEvent(true);
			} 
			break;

		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

}
