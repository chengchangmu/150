package com.scsy150.mine.activity;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.scsy150.R;
import com.scsy150.base.BaseActivity;
import com.scsy150.meet.page.BasePage;
import com.scsy150.mine.IngPage;
import com.scsy150.util.ResourcesUtil;
import com.scsy150.util.view.ViewInject;
import com.scsy150.util.view.ViewUtils;
import com.viewpagerindicator.TabPageIndicator;

/*
 * Copyright (C) 2015 四川硅谷科技重庆分公司
 * 版权所有
 *
 * 功能描述：我的之进行中界面
 * 作者：硅谷科技
 * 创建时间：2015-09-24
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */
public class IngActivity extends BaseActivity implements OnClickListener {
	@ViewInject(R.id.tpi_indicator)
	private TabPageIndicator mIndicator;

	@ViewInject(R.id.vp_order_list)
	private ViewPager mViewPager;

	private ArrayList<BasePage> orderPageList;
	private String[] mTitles = { ResourcesUtil.getString(R.string.fellowship),
			ResourcesUtil.getString(R.string.appointment),
			ResourcesUtil.getString(R.string.present) };
	// 联谊
	// 预约
	// 现场约
	private int[] status = { 0, 1, 2 };

	@Override
	public void addViewIntoContent() {
		View view = View.inflate(this, R.layout.activity_order_list, null);
		mBaseContent.addView(view);
		ViewUtils.inject(this);
		
		
		setTitle(R.string.ing);
		setLeftTxt(R.string.back);

		initData();

		mViewPager.setAdapter(new OrderListPageAdapter());

		mIndicator.setViewPager(mViewPager);// 在Viewpager数据加载后再设置
		// mIndicator.setOnPageChangeListener(this);// 当ViewPager和Indicator绑定时,
		// 页面滑动监听需要由Indicator设置
	}

	public void initData() {

		orderPageList = new ArrayList<BasePage>();
		for (int i = 0; i < mTitles.length; i++) {
			IngPage orderPage = new IngPage(this, status[i]);
			orderPageList.add(orderPage);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
			super.onClick(v);
			break;
		}
	}

	class OrderListPageAdapter extends PagerAdapter {

		// 返回Indicator的页面标题
		@Override
		public CharSequence getPageTitle(int position) {
			return mTitles[position];
		}

		@Override
		public int getCount() {
			return orderPageList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BasePage pager = orderPageList.get(position);
			View view = pager.mRootView;// 获取布局对象
			container.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}
}
