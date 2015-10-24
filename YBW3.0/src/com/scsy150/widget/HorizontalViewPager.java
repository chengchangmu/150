package com.scsy150.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 请求父控件不要拦截事件的ViewPager
 * 
 * @author K
 * 
 */
public class HorizontalViewPager extends ViewPager {

	private float startX;
	private float startY;
	private float dx;
	private float dy;

	public HorizontalViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HorizontalViewPager(Context context) {
		super(context);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// 请求父控件和祖宗控件不要拦截事件
		getParent().requestDisallowInterceptTouchEvent(true);
		return super.dispatchTouchEvent(ev);
	}
	
	/* 
	 * 不处理点击事件
	 */
	/*@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = event.getX();
			startY = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			float endX = event.getX();
			float endY = event.getY();
			dx = Math.abs(endX - startX);
			dy = Math.abs(endY - startY);
			break;
		case MotionEvent.ACTION_UP:
			if(dx < 5) {
				return false;
			}else {
				return super.onTouchEvent(event);
			}
			//break;
		default:
			break;
		}
		
		return false;
	}*/
}
