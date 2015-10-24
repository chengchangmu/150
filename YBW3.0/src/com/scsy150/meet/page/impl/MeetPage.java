/*package com.scsy150.meet.page.impl;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scsy150.R;
import com.scsy150.meet.activity.MeetDetailActivity;
import com.scsy150.meet.page.BasePage;
import com.scsy150.util.view.ViewInject;
import com.scsy150.util.view.ViewUtils;
import com.scsy150.widget.HorizontalViewPager;
import com.scsy150.widget.RefreshListView;
import com.scsy150.widget.RefreshListView.OnRefreshListener;

*//**
 * @author K
 * 
 *//*
public class MeetPage extends BasePage {

	private int[] mImageIds;

	@ViewInject(R.id.vp_advert)
	private HorizontalViewPager mViewPager;
	
	 * @ViewInject(R.id.pull_scroll) private PullToRefreshScrollView
	 * mScrollView;
	 
	@ViewInject(R.id.ll_container)
	private LinearLayout llContainer;

	*//******** 播放广告开始 ************//*
	// 能够刷新的listview,用PullToRefresh与listview冲突
	@ViewInject(R.id.lv_container)
	private RefreshListView mLvContainer; 

	// 上一个选中圆点的位置
	private int mLastPointPos = 0;
	// 广告轮播Handler
	private Handler mHandler = null;

	*//******** 播放广告结束 ************//*

	public MeetPage(Activity activity, int[] mImageIds) {
		super(activity);
		this.mImageIds = mImageIds;
		if (mHandler == null) {// 只有第一次进来才发更新广告的消息
			mHandler = new Handler() {
				public void handleMessage(android.os.Message msg) {
					int currentItem = mViewPager.getCurrentItem();
					currentItem++;

					mViewPager.setCurrentItem(currentItem);
					mHandler.sendEmptyMessageDelayed(0, 3000);
				};
			};

			mHandler.sendEmptyMessageDelayed(0, 3000);// 延时3秒,更新广告条位置
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public View initView() {
		View view = View
				.inflate(mActivity, R.layout.layout_fragment_meet, null);
		ViewUtils.inject(this, view);

		View headerView = View.inflate(mActivity, R.layout.layout_advert, null);
		ViewUtils.inject(this, headerView);

		mLvContainer.addHeaderView(headerView);// 给listview添加头布局

		// 设置下拉刷新监听
		mLvContainer.setRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				getDataFromServer();
			}

			@Override
			public void loadMore() {
				getMoreDataFromServer();
			}

		});

		mLvContainer.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				System.out.println("pos:" + position);
			}
		});

		mImageIds = new int[3];
		mImageIds[0] = R.drawable.advert_00;
		mImageIds[1] = R.drawable.advert_01;
		mImageIds[2] = R.drawable.advert_02;
		// 设置adapter
		mViewPager.setAdapter(new TopAdapter(mImageIds));
		// 设置ViewPager的初始位置
		mViewPager.setCurrentItem(mImageIds.length * 1000);

		// 设置页面切换监听
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// 当前的位置
				int currentPos = position % mImageIds.length;

				llContainer.getChildAt(currentPos).setEnabled(true);// 被选中的圆点设置为红色
				llContainer.getChildAt(mLastPointPos).setEnabled(false);// 上一次被选中的圆点设置为灰色

				mLastPointPos = currentPos;// 给上一个圆点位置赋值
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});

		for (int i = 0; i < mImageIds.length; i++) {
			ImageView imageView = new ImageView(mActivity);
			imageView.setImageResource(R.drawable.shape_circle_selector);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			if (i != 0) {
				params.leftMargin = 5;// 从第二个圆点开始设置左边距
				imageView.setEnabled(false);// 从第二个圆点开始都设置为不可用
			}

			imageView.setLayoutParams(params);// 给ImageView设置布局参数

			llContainer.addView(imageView);
		}

		mViewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (mHandler != null) {
						mHandler.removeCallbacksAndMessages(null);
					}
					break;
				case MotionEvent.ACTION_CANCEL:
					if (mHandler != null) {
						mHandler.sendEmptyMessageDelayed(0, 3000);
					}
					break;
				case MotionEvent.ACTION_UP:
					if (mHandler != null) {
						mHandler.sendEmptyMessageDelayed(0, 3000);
					}
					break;

				default:
					break;
				}

				return false;
			}

		});

		
		 * mScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>()
		 * {
		 * 
		 * @Override public void onRefresh(PullToRefreshBase<ScrollView>
		 * refreshView) { ToastUtil.showToast(mActivity, "刷新咯",
		 * Toast.LENGTH_LONG); new Handler().postDelayed(new Runnable() {
		 * 
		 * @Override public void run() { // 从服务器上获得更多数据 // getDataFromServer();
		 * 
		 * ToastUtil.showToast(mActivity, "刷新完成咯", Toast.LENGTH_LONG);
		 * mScrollView.onRefreshComplete(); } }, 5000); } });
		 

		// 给listview设置数据
		// 假数据
		ArrayList<String> list = new ArrayList<String>();
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.add("6");
		mLvContainer.setAdapter(new CommonAdapter(list));

		// return mScrollView;
		return mLvContainer;
	}

	@Override
	public void initData() {
		getDataFromServer();
	}

	private void getDataFromServer() {
		processData(null, false);
	}

	*//**
	 * 加载下一页数据
	 *//*
	private void getMoreDataFromServer() {

	}

	private void processData(String result, boolean isMore) {

		if (!isMore) {
			if (mHandler == null) {// 只有第一次进来才发更新广告的消息
				mHandler = new Handler() {
					public void handleMessage(android.os.Message msg) {
						int currentItem = mViewPager.getCurrentItem();
						currentItem++;

						mViewPager.setCurrentItem(currentItem);
						mHandler.sendEmptyMessageDelayed(0, 3000);
					};
				};

				mHandler.sendEmptyMessageDelayed(0, 3000);// 延时3秒,更新广告条位置
			}
		} else {
		}
	}

	*//**
	 * 顶部广告适配
	 * 
	 * @author K
	 * 
	 *//*
	class TopAdapter extends PagerAdapter {

		int[] mImages;

		public TopAdapter(int[] mImages) {
			this.mImages = mImages;
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
			ImageView view = new ImageView(mActivity);
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

	*//**
	 * 普通数据适配
	 *//*
	class CommonAdapter extends BaseAdapter {

		private ArrayList<String> list;

		// private MyBitmapUtils mBitmapUtils;

		public CommonAdapter(ArrayList<String> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(mActivity,
						R.layout.item_meet_layout, null);
			}
			ViewHolder holder = ViewHolder.getHolder(convertView);

			holder.tvTitle.setText("七夕百人交友联谊party报名中，  紧紧相约一起嗨！" + position);
			return convertView;
		}

	}

	static class ViewHolder {
		ImageView ivActivityPhotos;
		TextView tvTitle, tvDistance, tvNumber, tvDate;

		public ViewHolder(View convertView) {
			ivActivityPhotos = (ImageView) convertView
					.findViewById(R.id.iv_activityPhotos);
			tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
			tvDistance = (TextView) convertView.findViewById(R.id.tv_distance);
			tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
			tvDate = (TextView) convertView.findViewById(R.id.tv_date);
		}

		public static ViewHolder getHolder(View convertView) {
			ViewHolder holder = (ViewHolder) convertView.getTag();
			if (holder == null) {
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}
			return holder;
		}
	}

	@Override
	public void pageClick(View v) {
		
	}
}
*/