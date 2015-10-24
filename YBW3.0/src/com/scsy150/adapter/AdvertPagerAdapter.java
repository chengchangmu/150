package com.scsy150.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class AdvertPagerAdapter extends PagerAdapter {
	
	private int[] mImages;
	private Context mContext;
	
	public AdvertPagerAdapter() {
		super();
	}

	public AdvertPagerAdapter(int[] images, Context context) {
		super();
		mImages = images;
		mContext = context;
	}
	
	@Override
	public int getCount() {
		//返回元素的个数
		return Integer.MAX_VALUE;
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
	public Object instantiateItem(ViewGroup container, int position) {
		ImageView view = new ImageView(mContext.getApplicationContext());
		//循环滑动的广告页
		view.setBackgroundResource(mImages[position % mImages.length]);
		container.addView(view);
		return view;
	}  
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		//ViewPager默认缓存前一张和后一张，加上当前为三张，自动调用该方法销毁多余的图片
		container.removeView((View)object);
	}

}
