package com.scsy150.chat.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scsy150.R;
import com.scsy150.base.BaseActivity;
import com.scsy150.base.BaseBean;
import com.scsy150.chat.adpater.AuthMessageAndSureAdapter;
import com.scsy150.chat.bean.SystemMessageAndSureBean;
import com.scsy150.chat.bean.SystemMessageBean;
import com.scsy150.consts.MzApi;
import com.scsy150.consts.SystemConsts;
import com.scsy150.util.LogUtil;
import com.scsy150.util.ResourcesUtil;
import com.scsy150.util.ToastUtil;
import com.scsy150.util.view.OnClick;
import com.scsy150.util.view.ViewInject;
import com.scsy150.util.view.ViewUtils;
import com.scsy150.volley.net.RequestAndLoadCookies;
import com.scsy150.widget.pulltorefresh.PullToRefreshListView;

/*
 * Copyright (C) 2015 四川硅谷科技重庆分公司
 * 版权所有
 *
 * 功能描述：认证带确认消息
 * 作者：硅谷科技
 * 创建时间：2015-09-17
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */

public class AuthMessageAndSureActivity extends BaseActivity implements
		OnClickListener {

	public static final String DATA_KEY = "data_key";

	@ViewInject(R.id.pull_to_refresh_list)
	private PullToRefreshListView mPullToRefreshListView;

	private AuthMessageAndSureAdapter mAdapter;

	private ArrayList<SystemMessageAndSureBean> mList;

	private SystemMessageBean mBean;

	/**
	 * 页码 第1页2,3…..页
	 */
	private int Pageindex = 1;
	/**
	 * 页大小 每页要显示的记录条数
	 */
	private int Pagesize = 15;

	// Ordertype 订单类型 必填,由消息类型决定，固定传参 1 活动，2：预约,3:现场约
	private int Ordertype;

	@Override
	public void addViewIntoContent() {
		View view = View.inflate(AuthMessageAndSureActivity.this,
				R.layout.layout_fragment_system, null);
		mBaseContent.addView(view);
		ViewUtils.inject(AuthMessageAndSureActivity.this);
		setLeftVisibility(View.VISIBLE);

		mBean = (SystemMessageBean) getIntent().getSerializableExtra(DATA_KEY);

		int tmp = Integer.valueOf(mBean.getTypenum());
		/**
		 * 1 认证通知2 报名成功3 联谊活动即将开始 4 预约确认 5 现场约确认 6 商家已确认 7 加入初遇点 8 现场约失败 9 现场约成功
		 * 10 预约成功 11 退款成功 12 订单取消 13 消费码待确认
		 */
		String tmpStr = "";
		switch (tmp) {
		case 1:
			tmpStr = "认证通知";
			break;
		case 2:
			tmpStr = "报名成功";
			break;
		case 3:
			tmpStr = "联谊活动即将开始";
			break;
		case 4:
			// Ordertype 订单类型 必填,由消息类型决定，固定传参 1 活动，2：预约,3:现场约
			Ordertype = 2;
			tmpStr = "预约确认";
			break;
		case 5:
			// Ordertype 订单类型 必填,由消息类型决定，固定传参 1 活动，2：预约,3:现场约
			Ordertype = 3;
			tmpStr = "现场约确认";
			break;
		case 6:
			tmpStr = "商家已确认";
			break;
		case 7:
			tmpStr = "加入初遇点";
			break;
		case 8:
			tmpStr = "现场约失败";
			break;
		case 9:
			tmpStr = "现场约成功";
			break;
		case 10:
			tmpStr = "预约成功";
			break;
		case 11:
			tmpStr = "退款成功";
			break;
		case 12:
			tmpStr = "订单取消";
			break;
		case 13:
			tmpStr = "消费码待确认";
			break;
		}

		setTitle(tmpStr);
		if (mQueue == null) {
			mQueue = Volley.newRequestQueue(AuthMessageAndSureActivity.this);
		}

		getNetData();
	}

	private void init() {
		if (mList != null && mList.size() > 0) {
			mAdapter = new AuthMessageAndSureAdapter(
					AuthMessageAndSureActivity.this, mList);
			mPullToRefreshListView.setAdapter(mAdapter);
		}
	}

	@OnClick({ R.id.left_image })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_image:
			this.finish();
			break;
		default:
			break;
		}
	}

	public void getNetData() {
		RequestAndLoadCookies stringRequest = new RequestAndLoadCookies(
				AuthMessageAndSureActivity.this, Method.POST, MzApi.LOGIN_REG,
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
								Type listType = new TypeToken<ArrayList<SystemMessageAndSureBean>>() {
								}.getType();
								mList = gson.fromJson(jo.getString("Result"),
										listType);
								init();

							} catch (JSONException e) {
								e.printStackTrace();
							}

						} else {

							ToastUtil.showCenterToast(
									AuthMessageAndSureActivity.this,
									item.getMessage() + "", Toast.LENGTH_SHORT);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						ToastUtil.showCenterToast(
								AuthMessageAndSureActivity.this,
								ResourcesUtil
										.getString(R.string.base_load_failed_des),
								Toast.LENGTH_SHORT);
					}

				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("M", "Get_MsgGroup2");
				map.put("Pageindex", Pageindex + "");
				map.put("Pagesize", Pagesize + "");
				// Ordertype 订单类型 必填,由消息类型决定，固定传参 1 活动，2：预约,3:现场约
				map.put("Ordertype", Ordertype + "");

				return map;

			}

		};
		mQueue.add(stringRequest);

	}
}
