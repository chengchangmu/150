package com.scsy150.date.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.scsy150.R;
import com.scsy150.adapter.DateAdater;
import com.scsy150.adapter.ViewHolder;
import com.scsy150.base.BaseBean;
import com.scsy150.consts.MzApi;
import com.scsy150.consts.SystemConsts;
import com.scsy150.date.activity.NowDateHeadActivity;
import com.scsy150.date.adapter.BitMapCache;
import com.scsy150.date.adapter.NowViewPagerAdater;
import com.scsy150.date.bean.DateBean;
import com.scsy150.date.bean.NowUserBean;
import com.scsy150.util.ResourcesUtil;
import com.scsy150.util.ToastUtil;
import com.scsy150.volley.net.RequestAndLoadCookies;

public class NowDateFragment extends Fragment {
	GridView mGridView;
	LinkedList<DateBean> list;
	List<View> mViewList = null;
	ViewPager viewPager;
	NowViewPagerAdater adater;
	DateBean dBean;
	RequestQueue mQueue;
	LinkedList<DateBean> list2;
	ImageLoader imageLoader;
	String returnDate;
	TextView tv_bs_name;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Gson gson = new Gson();
				BaseBean item = gson.fromJson(returnDate, BaseBean.class);
				if (item.getIsSuccess() == SystemConsts.RESPONSE_SUCCESS) {
					JSONObject jo;
					try {
						jo = new JSONObject(returnDate);
						JSONObject jo1 = jo.getJSONObject("Result");
						JSONArray ja = jo1.getJSONArray("data2");
						Type listType = new TypeToken<LinkedList<DateBean>>() {
						}.getType();
						list = gson.fromJson(ja.toString(), listType);
						setAdaper(list);
						// DATA1 商家的信息
						JSONArray ja1 = jo1.getJSONArray("data1");
						for (int i = 0; i < ja1.length(); i++) {
							JSONObject jsonD = new JSONObject(ja1.optString(i));
							dBean = new DateBean();
							dBean.setMhname(jsonD.optString("Mhname"));// 商家名
							dBean.setSid(jsonD.optInt("Sid"));// 商家sid
						}
						// 商家名
						tv_bs_name.setText(dBean.getMhname());

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

				break;

			default:
				break;
			}

		}

	};

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.now_date_layout, null);
		// 商家名
		tv_bs_name = (TextView) view.findViewById(R.id.tv_bs_name);
		// gridview
		mGridView = (GridView) view.findViewById(R.id.gv_now);
		// volley
		mQueue = Volley.newRequestQueue(getActivity());
		imageLoader = new ImageLoader(mQueue, new BitMapCache());
		// viewpager
		viewPager = (ViewPager) view.findViewById(R.id.now_viewPager);
		// 请求数据
		getdata();
		return view;
	}

	public void showViewPager(int pos, DateBean dBean) {
		mViewList = new ArrayList<View>();
		this.dBean = dBean;
		for (int i = 0; i < list.size(); i++) {
			dBean = list.get(i);
			View vp = LayoutInflater.from(getActivity()).inflate(
					R.layout.item_nowdate_userinfo_layout, null);
			// 立即约
			vp.findViewById(R.id.bt_promptly).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(getActivity(),
									NowDateHeadActivity.class);
							Log.i("RT:", "nowdatafrgment:"
									+ NowDateFragment.this.dBean.getSid());
							intent.putExtra("nowData",
									NowDateFragment.this.dBean);
							startActivity(intent);

						}
					});

			// 头像
			ImageView iv_head = (ImageView) vp.findViewById(R.id.iv_head);
			ImageLoader.ImageListener imHead = ImageLoader.getImageListener(
					iv_head, R.drawable.back, R.drawable.yhy);
			imageLoader.get(MzApi.IMG_URL + dBean.getHeadImg(), imHead);
			// 性别
			ImageView iv_sex = (ImageView) vp.findViewById(R.id.iv_sex);
			if (dBean.getSex() == 1) {
				iv_sex.setImageResource(R.drawable.male);
			} else {
				iv_sex.setImageResource(R.drawable.female);
			}
			// 年龄
			TextView tv_age = (TextView) vp.findViewById(R.id.tv_hz_age);
			tv_age.setText(dBean.getGroupBrithday() + "后");
			// 名字
			TextView tv_name = (TextView) vp.findViewById(R.id.tv_hz_name);
			tv_name.setText(dBean.getNickName());
			// 隐藏viewpager
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
		adater = new NowViewPagerAdater(mViewList);
		viewPager.setAdapter(adater);
		viewPager.setCurrentItem(pos);

	}

	/**
	 * 带cookies现场约拿数据
	 */
	private void getdata() {
		RequestAndLoadCookies stringRequet = new RequestAndLoadCookies(
				getActivity(), com.android.volley.Request.Method.POST,
				MzApi.LOGIN_REG, new Listener<String>() {

					@Override
					public void onResponse(String str) {
						returnDate = str;
						handler.sendEmptyMessage(0);

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						ToastUtil.showCenterToast(getActivity(), ResourcesUtil
								.getString(R.string.base_load_failed_des),
								Toast.LENGTH_SHORT);
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("M", "GetEngagement");
				map.put("EEType", "1");
				map.put("beginpage", "1");
				map.put("Everpage", "15");
				map.put("MAC", "123");
				map.put("Longitude", SystemConsts.Longitude + "");
				map.put("Latitude", SystemConsts.Latitude + "");
				map.put("AgeGroup", "0");
				return map;
			}
		};
		mQueue.add(stringRequet);

	}

	private void setAdaper(LinkedList<DateBean> list) {
		/**
		 * grildview
		 */

		mGridView.setAdapter(new DateAdater<DateBean>(getActivity(), list) {

			@Override
			public void convert(final ViewHolder holder, final DateBean t) {
				// 头像volley
				ImageLoader.ImageListener imHead = ImageLoader
						.getImageListener(
								(ImageView) holder.getView(R.id.iv_date_head),
								R.drawable.back, R.drawable.yhy);
				imageLoader.get(MzApi.HEAD_SMALL_PIC_SIZE + t.getHeadImg(),
						imHead);
				// 年龄
				holder.setText(R.id.tv_age, t.getGroupBrithday() + "后");

				// 单机事件
				holder.setImgOnClick(R.id.iv_date_head, new OnClickListener() {

					@Override
					public void onClick(View v) {
						mGridView.setVisibility(View.GONE);
						viewPager.setVisibility(View.VISIBLE);
						showViewPager(holder.getmPosition(), t);

					}

				});
				// 长按事件
				holder.setImaOnLongClick(holder.getmPosition(),
						R.id.iv_date_head, new OnLongClickListener() {

							@Override
							public boolean onLongClick(View v) {

								holder.getView(R.id.ff_chosse).setVisibility(
										View.VISIBLE);

								return false;
							}
						});
				// 隐藏蒙层
				holder.setFragLayoutClick(R.id.ff_chosse,
						new OnClickListener() {

							@Override
							public void onClick(View v) {

								holder.getView(R.id.ff_chosse).setVisibility(
										View.GONE);

							}
						});

			}
		});
	};
}
