package com.scsy150.adapter;

import java.util.HashMap;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.scsy150.meet.page.BasePage;

public class MeetDetailAdapter<T extends BasePage> extends PagerAdapter{

	private HashMap<Integer, T> meetDetailPager;
	
	public MeetDetailAdapter() {
		super();
	}

	public MeetDetailAdapter(HashMap<Integer, T> meetDetailPager) {
		super();
		this.meetDetailPager = meetDetailPager;
	}
	
	@Override
	public int getCount() {
		//返回元素的个数
		return meetDetailPager.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		//保证展示对象的统一性
		return view == obj;
	}

	/**
	 * 初始化广告页面
	 */
	@Override
	public View instantiateItem(ViewGroup container, int position) {
		T t = meetDetailPager.get(position);
		View view = t.mRootView;
		try {
            if (view.getParent() == null) {
                ((ViewPager) container).addView(view, 0);
            } else {
                /*
                 * 很难理解新添加进来的view会自动绑定一个父类，由于一个儿子view不能与两个父类相关，
                 * 所以得解绑不这样做否则会产生 viewpager java.lang.IllegalStateException:
                 * The specified child already has a parent. You must call
                 * removeView() on the child's parent first.
                 */
            	((ViewGroup)view.getParent()).removeView(view);
        		container.addView(view, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
		
		return view;
	}  
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		if (meetDetailPager.get(position) != null) {
            ((ViewPager) container).removeView((View) object);
        }
	}

}
