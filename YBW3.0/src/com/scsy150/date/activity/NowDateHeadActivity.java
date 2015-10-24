package com.scsy150.date.activity;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scsy150.R;
import com.scsy150.adapter.ViewHolder;
import com.scsy150.base.BaseBean;
import com.scsy150.consts.MzApi;
import com.scsy150.consts.SystemConsts;
import com.scsy150.date.adapter.BitMapCache;
import com.scsy150.date.adapter.ListviewCashAdater;
import com.scsy150.date.bean.DateBean;
import com.scsy150.util.MD5Util;
import com.scsy150.util.ResourcesUtil;
import com.scsy150.util.ToastUtil;
import com.scsy150.volley.net.RequestAndLoadCookies;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class NowDateHeadActivity extends Activity implements OnClickListener {
	LinearLayout ll_dismis;
	PopupWindow ppList;
	PopupWindow ppCoupons;
	ImageView iv_dateText;
	ImageView iv_cash;
	LinkedList<DateBean> list;
	RequestQueue mQueue;
	ImageLoader imLoader;
	DateBean dBean;
	ListView mListview;
	CheckedTextView ck_yh;
	CheckedTextView ck_cash;

	LayoutInflater inflater;
	TextView tv_date_text;
	TextView tv_Cons;
	TextView tv_name;
	ImageView iv_Tcimg;
	TextView tv_TcName;
	TextView tv_TcCoat;
	TextView tv_xianjin;
	TextView tv_service;
	TextView tv_AllCost;
	String returnData;
	String ppdata;
	String blancedata;
	Handler handler = new Handler() {
		@SuppressLint("NewApi")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Gson gson = new Gson();
				BaseBean item = gson.fromJson(returnData, BaseBean.class);
				if (item.getIsSuccess() == SystemConsts.RESPONSE_SUCCESS) {
					JSONObject jo;
					try {
						jo = new JSONObject(returnData);
						JSONObject jo1 = jo.getJSONObject("Result");
						JSONArray ja1 = jo1.getJSONArray("data1");
						for (int i = 0; i < ja1.length(); i++) {
							JSONObject jsonD = new JSONObject(ja1.optString(i));
							// 套餐图片
							dBean.setSetMealImage(jsonD
									.optString("SetMealImage"));
							// 套餐名称
							dBean.setSName(jsonD.optString("SName"));
							// 套餐价格
							dBean.setScost(jsonD.optInt("SCost"));
							// 服务费
							dBean.setServiceCost(jsonD.optInt("ServiceCost"));
							// 套餐总价
							dBean.setTotalCost(jsonD.optInt("TotalCost"));
						}
						// 套餐图
						ImageLoader.ImageListener imTcimg = ImageLoader
								.getImageListener(iv_Tcimg, R.drawable.back,
										R.drawable.yhy);
						imLoader.get(MzApi.IMG_URL + dBean.getSetMealImage(),
								imTcimg);
						// 套餐名
						tv_TcName.setText(dBean.getSName());
						tv_TcCoat.setText("" + dBean.getScost());
						tv_service.setText("" + dBean.getServiceCost());
						tv_AllCost.setText("" + dBean.getTotalCost());

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				break;
			case 1:
				Gson gsons = new Gson();
				BaseBean items = gsons.fromJson(ppdata, BaseBean.class);
				if (items.getIsSuccess() == SystemConsts.RESPONSE_SUCCESS) {
					try {
						JSONObject jo = new JSONObject(ppdata);
						java.lang.reflect.Type listType = new TypeToken<LinkedList<DateBean>>() {
						}.getType();
						list = gsons.fromJson(jo.getString("Result"), listType);
						setadater(list);

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
				break;
			case 2:
				Gson gs = new Gson();
				BaseBean is = gs.fromJson(blancedata, BaseBean.class);
				if (is.getIsSuccess() == SystemConsts.RESPONSE_SUCCESS) {
					JSONObject jo;
					try {
						jo = new JSONObject(blancedata);
						JSONArray ja = new JSONArray(jo.getJSONObject("Result"));
						for (int i = 0; i < ja.length(); i++) {
							JSONObject js = ja.getJSONObject(i);
							dBean.setUAmount(js.optInt("UAmount"));
						}
						tv_xianjin.setText(dBean.getUAmount());

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.now_date_head_layou);
		getIntentdata();
		mQueue = Volley.newRequestQueue(NowDateHeadActivity.this);
		imLoader = new ImageLoader(mQueue, new BitMapCache());
		initview();

		ck_yh = (CheckedTextView) findViewById(R.id.ck_yh);
		ck_cash = (CheckedTextView) findViewById(R.id.ck_cash);
		ll_dismis.setOnClickListener(this);
		iv_dateText.setOnClickListener(this);
		iv_cash.setOnClickListener(this);
		ck_yh.setOnClickListener(this);
		ck_cash.setOnClickListener(this);
		getJsonData();
		getBalance();
		Log.i("RT", "sid:" + dBean.getSid());

	}

	private void getJsonData() {

		RequestAndLoadCookies cookies = new RequestAndLoadCookies(
				NowDateHeadActivity.this, Method.POST, MzApi.LOGIN_REG,
				new Listener<String>() {

					@Override
					public void onResponse(String str) {
						returnData = str;
						handler.sendEmptyMessage(0);

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						ToastUtil
								.showCenterToast(
										NowDateHeadActivity.this,
										ResourcesUtil
												.getString(R.string.base_load_failed_des),
										Toast.LENGTH_SHORT);

					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();

				map.put("M", "Get_Merchant_SetMeal");
				map.put("SID", "2");// TODO
				String sign;
				try {
					sign = MD5Util.getSignature(map, SystemConsts.SIGN_KEY);
					map.put("sign", sign);
				} catch (IOException e) {
					e.printStackTrace();
				}

				return map;
			}
		};
		mQueue.add(cookies);

	}

	private void getIntentdata() {
		Intent intent = getIntent();
		dBean = (DateBean) intent.getExtras().getSerializable("nowData");

	}

	/**
	 * 优惠券内容解析
	 * 
	 * @param list
	 */
	private void setadater(LinkedList<DateBean> list) {
		mListview.setAdapter(new ListviewCashAdater<DateBean>(
				NowDateHeadActivity.this, list) {

			@Override
			public void convert(ViewHolder holder, DateBean t) {
				holder.setText(R.id.tv_item_cash, t.getCouponost() + "元优惠券");
			}

		});

	};

	/**
	 * 初始化
	 */

	private void initview() {
		ll_dismis = (LinearLayout) findViewById(R.id.ll_dismis);
		iv_dateText = (ImageView) findViewById(R.id.iv_dateText);
		iv_cash = (ImageView) findViewById(R.id.iv_cash);
		inflater = LayoutInflater.from(NowDateHeadActivity.this);
		tv_date_text = (TextView) findViewById(R.id.tv_date_text);
		tv_Cons = (TextView) findViewById(R.id.tv_cons);
		tv_xianjin = (TextView) findViewById(R.id.tv_xianjin);

		tv_name = (TextView) findViewById(R.id.tv_name);// 约会对象
		tv_name.setText(dBean.getNickName());
		iv_Tcimg = (ImageView) findViewById(R.id.iv_TCimg);
		tv_TcName = (TextView) findViewById(R.id.tv_TcName);
		tv_TcCoat = (TextView) findViewById(R.id.tv_TCcost);
		tv_service = (TextView) findViewById(R.id.tv_TCservice);
		tv_AllCost = (TextView) findViewById(R.id.tv_TcAllCost);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_dismis:
			finish();
			break;
		case R.id.iv_dateText:
			View view = inflater.inflate(R.layout.popu_list_layout, null);
			// text
			view.findViewById(R.id.tv_one_text).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							tv_date_text.setText(R.string.date_text);
							ppList.dismiss();

						}
					});
			// text
			view.findViewById(R.id.tv_two_text).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							tv_date_text.setText(R.string.date_text_go);
							ppList.dismiss();
						}
					});
			// text
			view.findViewById(R.id.tv_three_text).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							tv_date_text.setText(R.string.date_text_drink);
							ppList.dismiss();

						}
					});
			if (ppList == null) {
				ppList = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT, true);
			}
			ppList.setOutsideTouchable(true);

			ppList.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.jx));

			if (ppList.isShowing()) {
				ppList.dismiss();
			} else {
				ppList.setWidth(500);
				ppList.showAsDropDown(v, -404, 0);
			}

			break;
		case R.id.iv_cash:
			View vp = inflater.inflate(R.layout.popu_list_coupons_layout, null);
			mListview = (ListView) vp.findViewById(R.id.listview_cash);

			// text
			// TODO
			getdata();
			if (ppCoupons == null) {
				ppCoupons = new PopupWindow(vp, LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT, true);
			}
			ppCoupons.setOutsideTouchable(true);

			ppCoupons.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.jx));

			if (ppCoupons.isShowing()) {
				ppCoupons.dismiss();
			} else {
				ppCoupons.setWidth(500);
				ppCoupons.showAsDropDown(v, -404, 0);
			}
			mListview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					dBean = (DateBean) parent.getItemAtPosition(position);
					tv_Cons.setText(dBean.getCouponost() + "元优惠券");
					ppCoupons.dismiss();

				}
			});

			break;
		case R.id.ck_yh:
			if (ck_yh.isChecked()) {
				ck_yh.toggle();
			} else {
				ck_yh.setChecked(true);
			}
			break;
		case R.id.ck_cash:
			if (ck_cash.isChecked()) {
				ck_cash.toggle();
			} else {
				ck_cash.setChecked(true);
			}
			break;

		default:
			break;
		}
	}

	/*
	 * 优惠券内容解析
	 */

	private void getdata() {
		RequestAndLoadCookies cookies = new RequestAndLoadCookies(
				NowDateHeadActivity.this, Method.POST, MzApi.LOGIN_REG,
				new Listener<String>() {

					@Override
					public void onResponse(String str) {
						ppdata = str;
						handler.sendEmptyMessage(1);

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {

						ToastUtil
								.showCenterToast(
										NowDateHeadActivity.this,
										ResourcesUtil
												.getString(R.string.base_load_failed_des),
										Toast.LENGTH_SHORT);
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();

				map.put("M", "GetYouHui");

				return map;
			}
		};
		mQueue.add(cookies);

	}

	/**
	 * 获取现金余额
	 */
	private void getBalance() {

		RequestAndLoadCookies cookies = new RequestAndLoadCookies(
				NowDateHeadActivity.this, Method.POST, MzApi.LOGIN_REG,
				new Listener<String>() {

					@Override
					public void onResponse(String str) {
						blancedata = str;
						handler.sendEmptyMessage(2);

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {

						ToastUtil
								.showCenterToast(
										NowDateHeadActivity.this,
										ResourcesUtil
												.getString(R.string.base_load_failed_des),
										Toast.LENGTH_SHORT);
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();

				map.put("M", "About_Me");

				return map;
			}
		};
		mQueue.add(cookies);

	}

}
