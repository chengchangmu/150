package com.scsy150.meet.page.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * 顶部广告适配
 * 
 * @author K
 * 
 */
public class TopAdapter extends PagerAdapter {

	private int[] mImages;
	private Context mContext;

	public TopAdapter(Context mContext, int[] mImages) {
		this.mImages = mImages;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ImageView view = new ImageView(mContext);
		view.setScaleType(ScaleType.FIT_XY);// 设置图片缩放类型, 填充父窗体类型
		// 循环滑动的广告页
		view.setBackgroundResource(mImages[position % mImages.length]);
		container.addView(view);
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

}
