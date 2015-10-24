package com.scsy150.date.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scsy150.R;
import com.scsy150.adapter.DateAdater;
import com.scsy150.adapter.ListPersonAdapter;
import com.scsy150.adapter.ViewHolder;
import com.scsy150.base.BaseBean;
import com.scsy150.base.YBWApplication;
import com.scsy150.consts.MzApi;
import com.scsy150.consts.SystemConsts;
import com.scsy150.date.activity.DateHeadActivity;
import com.scsy150.date.activity.UserInfoAcitivity;
import com.scsy150.date.adapter.BitMapCache;
import com.scsy150.date.adapter.ViewPagerAdater;
import com.scsy150.date.bean.DateBean;
import com.scsy150.date.bean.ListPersonBean;
import com.scsy150.util.LogUtil;
import com.scsy150.util.ResourcesUtil;
import com.scsy150.util.ToastUtil;
import com.scsy150.util.view.HorizontalListView;
import com.scsy150.volley.net.RequestAndLoadCookies;

public class OrderFragment extends Fragment {

	public static int POSITION;

	private final int DATELIST = 0;
	DateBean dateBean;
	GridView mGridView;
	HorizontalListView mHorListview;
	List<DateBean> mList;
	ListPersonAdapter<DateBean> lAdapter;
	List<View> mViewList = null;
	Button bt_nowDate;
	PopupWindow mPopupWindow;
	TextView tv_changPlace;
	PopupWindow ppList;
	ViewPagerAdater adater;
	LinearLayout l;
	ImageView iv_head;
	View vp;
	FrameLayout ff_chose;
	LinkedList<DateBean> list;
	String returnDate;
	ImageLoader imageLoader;
	ViewPager viewPager;
	RequestQueue mQueue;
	boolean isChose = false;

	RequestAndLoadCookies cookies;

	Handler handler = new Handler() {

		@SuppressLint("NewApi")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case DATELIST:
				Gson gson = new Gson();
				BaseBean item = gson.fromJson(returnDate, BaseBean.class);
				if (item.getIsSuccess() == SystemConsts.RESPONSE_SUCCESS) {
					try {

						JSONObject jo = new JSONObject(returnDate);
						JSONObject jo1 = jo.getJSONObject("Result");
						JSONArray ja = jo1.getJSONArray("data2");
						Type listType = new TypeToken<LinkedList<DateBean>>() {
						}.getType();
						list = gson.fromJson(ja.toString(), listType);
						setAdapter(list);
						// 商家信息
						JSONArray jsonBs = jo1.getJSONArray("data1");
						for (int i = 0; i < jsonBs.length(); i++) {
							JSONObject jsob = new JSONObject(
									jsonBs.optString(i));
							dateBean = new DateBean();
							dateBean.setMerchantId(jsob.getInt("MerchantId"));// 商家id
							dateBean.setSid(jsob.getInt("sid"));// 套餐id
							dateBean.setMhname(jsob.optString("Mhname"));// 商家名称
							dateBean.setScost(jsob.optInt("scost"));// 套餐价格
							dateBean.setSName(jsob.optString("SName"));// 套餐名
							dateBean.setServiceCost(jsob.getInt("serviceCost"));// 服务费
							dateBean.setTotalCost(jsob.optInt("totalCost"));// 合计费用
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else {

					ToastUtil.showCenterToast(mContext, item.getMessage() + "",
							Toast.LENGTH_SHORT);
				}

				break;
			case 1:
				setTopadater(mList);
				lAdapter.notifyDataSetChanged();
				break;
			case 2:
				lAdapter.notifyDataSetChanged();
				break;

			default:
				break;
			}

		}

	};

	private Context mContext;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater
				.inflate(R.layout.order_fragment_layout, null);

		YBWApplication.getInstance().beginLocation();
		mContext = getActivity().getApplicationContext();
		mList = new ArrayList<>();
		// volley
		mQueue = Volley.newRequestQueue(getActivity());
		imageLoader = new ImageLoader(mQueue, new BitMapCache());
		getData();
		mGridView = (GridView) view.findViewById(R.id.gv_date);
		ff_chose = (FrameLayout) view.findViewById(R.id.ff_chosse);
		bt_nowDate = (Button) view.findViewById(R.id.bt_nowDate);
		mHorListview = (HorizontalListView) view.findViewById(R.id.hl_listview);
		viewPager = (ViewPager) view.findViewById(R.id.viewPager);

		showpopuwindow();// 显示发送请求

		return view;
	}

	/**
	 * 中间显示详细信息的viewpager
	 */

	private void showViewPager(int pos, DateBean dBean) {
		this.dateBean = list.get(POSITION);
		mViewList = new ArrayList<View>();

		for (int i = 0; i < list.size(); i++) {
			vp = LayoutInflater.from(getActivity()).inflate(
					R.layout.item_hz_userinfo_layout, null);
			dBean = list.get(i);

			// 头像
			ImageView iv_head = (ImageView) vp.findViewById(R.id.iv_head);
			ImageLoader.ImageListener img = ImageLoader.getImageListener(
					iv_head, R.drawable.advert_01, R.drawable.advert_00);
			imageLoader.get(MzApi.BIG_PIC_SIZE + dBean.getHeadImg(), img);
			// 多少人约
			TextView tv_calling = (TextView) vp.findViewById(R.id.tv_Calling);
			tv_calling.setText("当前有" + dBean.getCalling() + "人在约TA");
			// 性別
			ImageView iv_sex = (ImageView) vp.findViewById(R.id.iv_sex);
			TextView tv_gender = (TextView) vp.findViewById(R.id.tv_hz_gender);
			if (dBean.getSex() == 1) {
				tv_gender.setText(R.string.man);
				iv_sex.setImageResource(R.drawable.male);

			} else {
				tv_gender.setText(R.string.woman);
				iv_sex.setImageResource(R.drawable.female);
			}

			// 姓名
			TextView tv_name = (TextView) vp.findViewById(R.id.tv_hz_name);
			tv_name.setText(dBean.getNickName());
			// 查看资料
			Button bt_seeInfo = (Button) vp.findViewById(R.id.bt_seeInfo);
			bt_seeInfo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(),
							UserInfoAcitivity.class);
					intent.putExtra("data", dateBean);
					startActivity(intent);
				}
			});
			// 距离
			TextView tv_distance = (TextView) vp.findViewById(R.id.tv_distance);
			tv_distance.setText(dBean.getDistance() + "");
			// 加入约单 TODO
			final Button bt_addList = (Button) vp.findViewById(R.id.bt_addList);
			bt_addList.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					LogUtil.d("------POSITION", POSITION + "");
					dateBean = list.get(POSITION);
					if (mList.size() > 10) {
						ToastUtil.showCenterToast(mContext, "您的约单人数已满10人",
								Toast.LENGTH_SHORT);
						bt_addList.setClickable(false);
					} else {

						int tmp = 0;
						if (mList != null && mList.size() > 0) {
							for (int j = 0; j < mList.size(); j++) {

								if (mList.get(j).getUserId() == dateBean
										.getUserId()) {
									ToastUtil.showCenterToast(mContext,
											"您的约单已存在此用户", Toast.LENGTH_SHORT);
									bt_addList.setText("已加入约单");
									bt_addList
											.setBackgroundColor(getResources()
													.getColor(
															R.color.grey_deep_font));
									tmp = 1;
								}
							}
						}

						if (tmp == 0) {
							dateBean.getUserId();
							dateBean.getHeadImg();
							mList.add(0, list.get(POSITION - 1));
						}
						bt_addList.setText("已加入约单");
						bt_addList.setBackgroundColor(getResources().getColor(
								R.color.grey_deep_font));
						bt_addList.setClickable(false);
						handler.sendEmptyMessage(1);

					}

				}
			});

			// 退出
			ImageView iv_back = (ImageView) vp.findViewById(R.id.iv_back);
			iv_back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mGridView.setVisibility(View.VISIBLE);
					viewPager.setVisibility(View.GONE);
				}
			});

			mViewList.add(vp);
		}

		adater = new ViewPagerAdater(mViewList);
		viewPager.setAdapter(adater);
		viewPager.setCurrentItem(pos);
	}

	/**
	 * 立即约
	 */

	private void showpopuwindow() {
		bt_nowDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						DateHeadActivity.class);
				intent.putExtra("bsData", dateBean);
				startActivity(intent);
			}
		});

	}

	public void getData() {
		RequestAndLoadCookies stringRequest = new RequestAndLoadCookies(
				mContext, Method.POST, MzApi.LOGIN_REG,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String str) {
						returnDate = str;
						handler.sendEmptyMessage(DATELIST);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						ToastUtil.showCenterToast(mContext, ResourcesUtil
								.getString(R.string.base_load_failed_des),
								Toast.LENGTH_SHORT);
					}

				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();

				map.put("M", "GetEngagement");
				map.put("EEType", "0");
				map.put("beginpage", "1");
				map.put("Everpage", "15");
				map.put("Longitude", SystemConsts.Longitude + "");
				map.put("Latitude", SystemConsts.Latitude + "");
				map.put("AgeGroup", "0");
				return map;

			}

		};
		mQueue.add(stringRequest);

	}

	protected void setAdapter(LinkedList<DateBean> list2) {
		// 设置数据
		mGridView.setAdapter(new DateAdater<DateBean>(mContext, list2) {

			@Override
			public void convert(final ViewHolder holder, final DateBean t) {

				// 头像volley
				ImageLoader.ImageListener imHead = ImageLoader
						.getImageListener(
								(ImageView) holder.getView(R.id.iv_date_head),
								R.drawable.back, R.drawable.yhy);
				imageLoader.get(MzApi.HEAD_SMALL_PIC_SIZE + t.getHeadImg(),
						imHead);
				// 热度
				if (t.getCalling() >= 10) {
					holder.getView(R.id.iv_hot).setVisibility(View.VISIBLE);
				}

				// 年龄
				holder.setText(R.id.tv_age, t.getGroupBrithday() + "后");
				// 头像点击事件
				holder.setImgOnClick(R.id.iv_date_head, new OnClickListener() {

					@Override
					public void onClick(View v) {
						mGridView.setVisibility(View.GONE);
						viewPager.setVisibility(View.VISIBLE);
						// 显示当前viewpager的默认位置
						showViewPager(holder.getmPosition(), t);
					}
				});
				// 长按事件
				// iv_head = holder.getView(R.id.iv_date_head);
				// iv_head.setOnLongClickListener(new OnLongClickListener() {
				// @Override
				// public boolean onLongClick(View v) {
				// holder.getView(R.id.ff_chosse).setVisibility(
				// View.VISIBLE);
				// if (!mList.contains(dateBean)) {
				// dateBean.getUserId();
				// dateBean.getHeadImg();
				// mList.add(0, dateBean);
				// handler.sendEmptyMessage(4);
				//
				// } else {
				// ToastUtil.showCenterToast(mContext, "您的约单已存在此用户",
				// Toast.LENGTH_SHORT);
				//
				// }
				//
				// return false;
				// }
				// });

				// 隐藏蒙层
				holder.setFragLayoutClick(R.id.ff_chosse,
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								holder.getView(R.id.ff_chosse).setVisibility(
										View.GONE);
								dateBean.getUserId();
								dateBean.getHeadImg();
								mList.remove(dateBean);
								handler.sendEmptyMessage(2);

							}
						});
			}
		});

	}

	private void setTopadater(List<DateBean> mList) {
		// 上方listview
		lAdapter = new ListPersonAdapter<DateBean>(getActivity(), mList) {

			@Override
			public void convert(ViewHolder holder, DateBean t) {

				com.nostra13.universalimageloader.core.ImageLoader
						.getInstance()
						.displayImage(
								MzApi.IMAGE_DOWNLOAD + t.getHeadImg(),
								(ImageView) (holder.getView(R.id.cv_datePerson)));

				holder.setImgOnClick(R.id.cv_datePerson, new OnClickListener() {

					@Override
					public void onClick(View v) {

						// 跳转到用户资料页
					}
				});

			}
		};
		mHorListview.setAdapter(lAdapter);
	};

}
