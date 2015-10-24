package com.scsy150.mine.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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
import com.scsy150.consts.MzApi;
import com.scsy150.consts.SystemConsts;
import com.scsy150.mine.adapter.WalletAdapter;
import com.scsy150.mine.bean.InfoBean;
import com.scsy150.mine.bean.WalletBean;
import com.scsy150.util.LogUtil;
import com.scsy150.util.ResourcesUtil;
import com.scsy150.util.ToastUtil;
import com.scsy150.util.view.OnClick;
import com.scsy150.util.view.ViewInject;
import com.scsy150.util.view.ViewUtils;
import com.scsy150.volley.net.RequestAndLoadCookies;

/*
 * Copyright (C) 2015 四川硅谷科技重庆分公司
 * 版权所有
 *
 * 功能描述：我的之钱包界面
 * 作者：硅谷科技
 * 创建时间：2015-08-24
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */

public class WalletActivity extends BaseActivity implements OnClickListener {

	@ViewInject(R.id.setting_opt2)
	private LinearLayout mOpt2;

	@ViewInject(R.id.setting_opt4)
	private LinearLayout mOpt4;

	@ViewInject(R.id.wallet_money)
	private TextView mMoney;
	@ViewInject(R.id.wallet_gold)
	private TextView mGold;

	@ViewInject(R.id.wallet_list)
	private ListView mList;

	private WalletAdapter mAdapter;

	@Override
	public void addViewIntoContent() {
		View view = View.inflate(this, R.layout.activity_mine_wallet, null);
		mBaseContent.addView(view);
		ViewUtils.inject(this);
		setLeftVisibility(View.VISIBLE);
		setTitle(ResourcesUtil.getString(R.string.user_option1));
		initData();
	}

	private void initData() {
		getNetDataGold();
		getNetData();
	}

	@Override
	@OnClick({ R.id.left_image })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_image:
			finish();
			break;
		default:
			break;
		}
	}

	private void getNetData() {
		RequestAndLoadCookies stringRequest = new RequestAndLoadCookies(
				WalletActivity.this, Method.POST, MzApi.LOGIN_REG,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String str) {
						LogUtil.d(TAG, str);
						Gson gson = new Gson();
						BaseBean item = gson.fromJson(str, BaseBean.class);
						if (item.getIsSuccess() == SystemConsts.RESPONSE_SUCCESS) {
							JSONObject jo;
							try {
								jo = new JSONObject(str);
								Type listType = new TypeToken<ArrayList<WalletBean>>() {
								}.getType();
								ArrayList<WalletBean> list = gson.fromJson(
										jo.getString("Result"), listType);
								initData(list);

							} catch (JSONException e) {
								e.printStackTrace();
							}

						} else {

							removeProgressDialog();

							ToastUtil.showCenterToast(WalletActivity.this,
									item.getMessage() + "", Toast.LENGTH_SHORT);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						removeProgressDialog();
						ToastUtil
								.showCenterToast(
										WalletActivity.this,
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
		mQueue.add(stringRequest);
	}

	private void initData(ArrayList<WalletBean> list) {
		if (list != null && list.size() > 0) {
			mAdapter = new WalletAdapter(
					WalletActivity.this.getApplicationContext(), list);
			mList.setAdapter(mAdapter);
		}

	}

	private void getNetDataGold() {
		RequestAndLoadCookies stringRequest = new RequestAndLoadCookies(
				WalletActivity.this, Method.POST, MzApi.LOGIN_REG,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String str) {
						LogUtil.d(TAG, str);
						Gson gson = new Gson();
						BaseBean item = gson.fromJson(str, BaseBean.class);
						if (item.getIsSuccess() == SystemConsts.RESPONSE_SUCCESS) {
							JSONObject jo;
							try {
								jo = new JSONObject(str);
								Type listType = new TypeToken<LinkedList<InfoBean>>() {
								}.getType();
								LinkedList<InfoBean> list = gson.fromJson(
										jo.getString("Result"), listType);
								InfoBean bean = list.get(0);
								setData(bean);

							} catch (JSONException e) {
								e.printStackTrace();
							}

						} else {

							removeProgressDialog();

							ToastUtil.showCenterToast(WalletActivity.this,
									item.getMessage() + "", Toast.LENGTH_SHORT);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						removeProgressDialog();
						ToastUtil
								.showCenterToast(
										WalletActivity.this,
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
		mQueue.add(stringRequest);
	}

	private void setData(InfoBean bean) {

		mMoney.setText(bean.getUAmount() + "");
		mGold.setText(bean.getIntegral() + "");
	}

}
