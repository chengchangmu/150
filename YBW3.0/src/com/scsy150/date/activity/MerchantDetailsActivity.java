package com.scsy150.date.activity;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.AuthFailureError;
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
import com.scsy150.date.adapter.GvPicAdater;
import com.scsy150.date.bean.DateBean;
import com.scsy150.util.MD5Util;
import com.scsy150.util.ResourcesUtil;
import com.scsy150.util.ToastUtil;
import com.scsy150.volley.net.RequestAndLoadCookies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class MerchantDetailsActivity extends Activity {
	DateBean dBean;
	GridView mGView;
	ImageView iv_bsHead;
	TextView tv_bs_name;
	TextView tv_cost;
	TextView tv_bsType;
	TextView tv_score;
	TextView tv_place;
	TextView tv_SName;
	TextView tv_TCcost;
	TextView tv_FWcost;
	TextView tv_allCost;
	ImageView iv_Tchead;
	RatingBar raBar;
	String returnData;
	RequestQueue mQueue;
	ImageLoader imLoader;
	Handler handler = new Handler() {
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
						// /商家图片
						JSONArray js = jo1.getJSONArray("data2");
						Type listType = new TypeToken<LinkedList<DateBean>>() {
						}.getType();
						LinkedList<DateBean> list = gson.fromJson(
								js.toString(), listType);
						setadapter(list);

						JSONArray ja = jo1.getJSONArray("data1");
						for (int i = 0; i < ja.length(); i++) {
							JSONObject jso = new JSONObject(ja.optString(i));
							dBean.setMhname(jso.getString("Mhname"));
							dBean.setMerchantId(jso.getInt("MerchantId"));
							dBean.setMerchantImage(jso
									.optString("MerchantImage"));
							dBean.setSetMealImage(jso.optString("SetMealImage"));
							dBean.setMerchantType(jso.optString("MerchantType"));
							dBean.setScore(jso.optInt("Score"));
							dBean.setSName(jso.optString("SName"));
							dBean.setScoreFqty(jso.optInt("ScoreFqty"));
							dBean.setServiceCost(jso.optInt("ServiceCost"));
							dBean.setScost(jso.optInt("SCost"));
							dBean.setTotalCost(jso.optInt("TotalCost"));
							dBean.setOtherAddress(jso.getString("OtherAddress"));
						}
						// 商家头像
						ImageLoader.ImageListener imHead = ImageLoader
								.getImageListener(iv_bsHead, R.drawable.back,
										R.drawable.yhy);
						imLoader.get(MzApi.IMG_URL + dBean.getMerchantImage(),
								imHead);
						// 商家名称
						tv_bs_name.setText(dBean.getMhname());
						// 商家类型
						tv_bsType.setText(dBean.getMerchantType());
						// 评分
						int score = dBean.getScore() / dBean.getScoreFqty();
						tv_score.setText("" + score);
						raBar.setMax(5);
						raBar.setProgress(score);
						// 商家地址
						tv_place.setText(dBean.getOtherAddress());
						// 套餐图片
						ImageLoader.ImageListener imTCHead = ImageLoader
								.getImageListener(iv_Tchead, R.drawable.back,
										R.drawable.yhy);
						imLoader.get(MzApi.IMG_URL + dBean.getSetMealImage(),
								imTCHead);
						// 套餐名称
						tv_SName.setText(dBean.getSName());
						// 套餐价格
						tv_TCcost.setText(dBean.getScost() + "");
						// 服务费
						tv_FWcost.setText(dBean.getServiceCost() + "");
						// 套餐总价
						tv_allCost.setText(dBean.getTotalCost() + "");

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
		setContentView(R.layout.business_details_layout);
		initView();
		mQueue = Volley.newRequestQueue(MerchantDetailsActivity.this);
		imLoader = new ImageLoader(mQueue, new BitMapCache());

		getJsonDate();

	}

	private void getJsonDate() {
		RequestAndLoadCookies reCookies = new RequestAndLoadCookies(
				MerchantDetailsActivity.this,
				com.android.volley.Request.Method.POST, MzApi.LOGIN_REG,
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
										MerchantDetailsActivity.this,
										ResourcesUtil
												.getString(R.string.base_load_failed_des),
										Toast.LENGTH_SHORT);

					}
				}) {

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();

				map.put("M", "Get_Merchant_SetMeal");
				map.put("SID", "2");// dBean.getSid() + ""
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
		mQueue.add(reCookies);

	}

	/**
	 * 初始化
	 */
	private void initView() {
		mGView = (GridView) findViewById(R.id.gv_pictrue);
		iv_bsHead = (ImageView) findViewById(R.id.iv_bs_head);
		tv_bs_name = (TextView) findViewById(R.id.tv_bs_name);
		tv_bsType = (TextView) findViewById(R.id.tv_bsType);
		tv_score = (TextView) findViewById(R.id.tv_score);
		tv_place = (TextView) findViewById(R.id.tv_place);
		iv_Tchead = (ImageView) findViewById(R.id.iv_TChead);
		tv_SName = (TextView) findViewById(R.id.tv_SName);
		tv_TCcost = (TextView) findViewById(R.id.tv_TCcost);
		tv_FWcost = (TextView) findViewById(R.id.tv_FWcost);
		tv_allCost = (TextView) findViewById(R.id.tv_allCost);
		raBar = (RatingBar) findViewById(R.id.ratingBar);
		Intent intent = getIntent();
		dBean = (DateBean) intent.getExtras().getSerializable("bsBean");

	}

	private void setadapter(LinkedList<DateBean> list) {
		mGView.setAdapter(new GvPicAdater<DateBean>(
				MerchantDetailsActivity.this, list) {

			@Override
			public void convert(ViewHolder holder, DateBean t) {
				ImageLoader.ImageListener imHead = ImageLoader
						.getImageListener(
								(ImageView) (holder.getView(R.id.iv_pic)),
								R.drawable.back, R.drawable.yhy);
				imLoader.get(MzApi.IMG_URL + t.getPhotoImg(), imHead);

			}
		});

	};
}
