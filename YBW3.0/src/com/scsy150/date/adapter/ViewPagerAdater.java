package com.scsy150.date.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.scsy150.date.fragment.OrderFragment;

public class ViewPagerAdater extends PagerAdapter {

	private List<View> list;

	public ViewPagerAdater(List<View> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		if (list != null && list.size() > 0) {

			return list.size();
		} else {

			return 0;
		}
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(list.get(position));
		OrderFragment.POSITION = position;
		return list.get(position);
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

}
