package com.scsy150.meet.page.impl;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scsy150.R;
import com.scsy150.base.BaseActivity;
import com.scsy150.base.BaseBean;
import com.scsy150.base.YBWApplication;
import com.scsy150.consts.MzApi;
import com.scsy150.consts.SystemConsts;
import com.scsy150.meet.activity.MeetDetailActivity;
import com.scsy150.meet.bean.MeetInfoBean;
import com.scsy150.meet.page.BasePage;
import com.scsy150.meet.page.adapter.CommonAdapter;
import com.scsy150.meet.page.adapter.TopAdapter;
import com.scsy150.util.LogUtil;
import com.scsy150.util.MD5Util;
import com.scsy150.util.ResourcesUtil;
import com.scsy150.util.ToastUtil;
import com.scsy150.util.view.ViewInject;
import com.scsy150.util.view.ViewUtils;
import com.scsy150.volley.net.RequestAndLoadCookies;
import com.scsy150.widget.pulltorefresh.PullToRefreshBase;
import com.scsy150.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.scsy150.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.scsy150.widget.pulltorefresh.PullToRefreshListView;

/**
 * 
 * @author K
 * 
 */
public class HotPage extends BasePage implements OnClickListener {

	public HotPage(Activity activity) {
		super(activity);
		initData();
		messageLoop();
	}

	// 填充的假广告页
	private int[] mImageIds = { R.drawable.advert_00, R.drawable.advert_01,
			R.drawable.advert_02 };

	@ViewInject(R.id.vp_advert)
	private ViewPager mViewPager;
	@ViewInject(R.id.ll_container)
	private LinearLayout llContainer;
	@ViewInject(R.id.pull_refresh_list)
	private PullToRefreshListView refreshListView;
	private ListView mLvContainer;

	/******** 播放广告开始 ************/
	// 上一个选中圆点的位置
	private int mLastPointPos = 0;
	// 广告轮播Handler
	private Handler mHandler = null;

	/******** 播放广告结束 ************/

	/*
	 * public HotPage(Activity activity, int[] mImageIds) { super(activity);
	 * this.mImageIds = mImageIds; messageLoop(); }
	 */

	// 数据的行数：beginPage 开始行；everPage 数据行数
	private int beginPage = 1;
	private int everPage = 2;
	// 活动筛选: 默认全部
	private int merchantType = SystemConsts.MERCHANT_ALL;
	// 时间日期: 默认全部
	private int date = SystemConsts.DATE_ALL;

	private View mView;
	private View headerView;

	private ArrayList<MeetInfoBean> mMeetInfoList;

	/**
	 * 循环发送消息
	 */
	public void messageLoop() {
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

		((YBWApplication) mActivity.getApplication()).beginLocation();

		mView = View.inflate(mActivity, R.layout.layout_fragment_meet, null);
		ViewUtils.inject(this, mView);

		headerView = View.inflate(mActivity, R.layout.layout_advert, null);
		ViewUtils.inject(this, headerView);

		// 设置下拉刷新监听
		initPullToRefreshListView();

		// 添加监听事件
		findView_addListener();

		// 设置数据
		// 假数据
		/*
		 * ArrayList<String> list = new ArrayList<String>(); list.add("1Hot");
		 * list.add("2Hot"); list.add("3Hot"); list.add("4Hot");
		 * list.add("5Hot"); list.add("6Hot"); mLvContainer.setAdapter(new
		 * CommonAdapter(mActivity, list));
		 */

		return mLvContainer;
	}

	/**
	 * 初始化下拉刷新ListView
	 */
	private void initPullToRefreshListView() {
		refreshListView = (PullToRefreshListView) mView
				.findViewById(R.id.pull_refresh_list);
		refreshListView.setMode(Mode.BOTH);
		refreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshListView.getCurrentMode() == Mode.PULL_FROM_END) {
					// 上拉。加载更多
					// loadDataAndRefreshView();
					LogUtil.d("----------------", "上拉加载更多...");
				} else {
					// 下拉，直接恢复
					/*
					 * CommonUtil.runOnUIThread(new Runnable() {
					 * 
					 * @Override public void run() {
					 * refreshListView.onRefreshComplete(); } });
					 */
					LogUtil.d("----------------", "下拉加载更多...");
				}
			}
		});

		mLvContainer = refreshListView.getRefreshableView();
		mLvContainer.setDividerHeight(0);// 隐藏divider
		mLvContainer.setSelector(android.R.color.transparent);
	}

	/**
	 * 控件添加监听时间
	 */
	public void findView_addListener() {

		mLvContainer.addHeaderView(headerView);// 给listview添加头布局

		mLvContainer.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(mActivity, MeetDetailActivity.class);
				intent.putExtra("activity_id", mMeetInfoList.get(position - 2).getAcId());
				mActivity.startActivity(intent);
				mActivity.finish();
			}
		});

		mImageIds = new int[3];
		mImageIds[0] = R.drawable.advert_00;
		mImageIds[1] = R.drawable.advert_01;
		mImageIds[2] = R.drawable.advert_02;
		// 设置adapter
		mViewPager.setAdapter(new TopAdapter(mActivity, mImageIds));
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
	}

	@Override
	public void initData() {
		getDataFromServer();
		// 登录请求
		RequestAndLoadCookies stringRequest = new RequestAndLoadCookies(
				mActivity, Method.POST, MzApi.ACTIVITY_URL,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String str) {
						LogUtil.d("TAG--------------------", str);
						Gson gson = new Gson();
						BaseBean item = gson.fromJson(str, BaseBean.class);
						if (item.getIsSuccess() == SystemConsts.RESPONSE_SUCCESS) {
							JSONObject jo;
							try {
								jo = new JSONObject(str);
								Type listType = new TypeToken<ArrayList<MeetInfoBean>>() {
								}.getType();
								mMeetInfoList = gson.fromJson(
										jo.getString("Result"), listType);
								if (mMeetInfoList != null
										&& mMeetInfoList.size() > 0) {
									mLvContainer.setAdapter(new CommonAdapter(
											mActivity, mMeetInfoList));
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}

						} else {

							((BaseActivity) mActivity).removeProgressDialog();
							ToastUtil.showCenterToast(mActivity,
									item.getMessage() + "", Toast.LENGTH_SHORT);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						((BaseActivity) mActivity).removeProgressDialog();
						ToastUtil.showCenterToast(mActivity, ResourcesUtil
								.getString(R.string.base_load_failed_des),
								Toast.LENGTH_SHORT);
					}

				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();

				map.put("M", "Get_Main_Merchant_Activity_List");
				map.put("ACIVITYTYPE", SystemConsts.ACTIVITY_TYPE_HOT + "");
				map.put("LONGITUDE", 1 + "");
				map.put("LATITUDE", 1 + "");
				map.put("BEGINPAGE", beginPage + "");
				map.put("EVERPAGE", everPage + "");
				// map.put("PROVINCE", SystemConsts.CURRENT_PROVINCE);
				// map.put("COUNTY", SystemConsts.CURRENT_CITY);
				map.put("PROVINCE", "重庆市");
				map.put("COUNTY", "");
				map.put("DATE", date + "");
				map.put("MERCHANTTYPE", merchantType + "");

				String sign = "";
				try {
					sign = MD5Util.getSignature(map, SystemConsts.SIGN_KEY);
					LogUtil.d("sign------------", sign);
					map.put("sign", sign);
				} catch (IOException e) {
					e.printStackTrace();
				}

				return map;

			}

		};
		YBWApplication.mQueue.add(stringRequest);
	}

	private void getDataFromServer() {
		processData(null, false);
	}

	/**
	 * 加载下一页数据
	 */
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

	@Override
	public void pageClick(View v) {

	}
}
