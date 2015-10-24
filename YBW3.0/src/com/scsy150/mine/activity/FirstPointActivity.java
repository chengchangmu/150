package com.scsy150.mine.activity;

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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scsy150.R;
import com.scsy150.base.BaseActivity;
import com.scsy150.base.BaseBean;
import com.scsy150.consts.MzApi;
import com.scsy150.consts.SystemConsts;
import com.scsy150.mine.adapter.FirstPointAdapter;
import com.scsy150.mine.bean.FirstPointBean;
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
 * 功能描述：我的之初遇点界面
 * 作者：硅谷科技
 * 创建时间：2015-08-24
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */

public class FirstPointActivity extends BaseActivity implements OnClickListener {

	@ViewInject(R.id.pull_to_refresh_list)
	private PullToRefreshListView mPullToRefreshListView;
	private FirstPointAdapter mAdapter;

	@Override
	public void addViewIntoContent() {
		View view = View.inflate(this, R.layout.layout_fragment_system, null);
		mBaseContent.addView(view);
		ViewUtils.inject(this);
		setLeftVisibility(View.VISIBLE);
		setTitle(ResourcesUtil.getString(R.string.user_option8));
		getNetData();
	}

	private void setAdapter(ArrayList<FirstPointBean> list) {
		if (list != null && list.size() > 0) {
			mAdapter = new FirstPointAdapter(
					FirstPointActivity.this.getApplicationContext(), list);
			mPullToRefreshListView.setAdapter(mAdapter);
		}
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
				FirstPointActivity.this, Method.POST, MzApi.LOGIN_REG,
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
								Type listType = new TypeToken<ArrayList<FirstPointBean>>() {
								}.getType();
								ArrayList<FirstPointBean> list = gson.fromJson(
										jo.getString("Result"), listType);
								setAdapter(list);

							} catch (JSONException e) {
								e.printStackTrace();
							}

						} else {

							removeProgressDialog();

							ToastUtil.showCenterToast(FirstPointActivity.this,
									item.getMessage() + "", Toast.LENGTH_SHORT);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						removeProgressDialog();
						ToastUtil
								.showCenterToast(
										FirstPointActivity.this,
										ResourcesUtil
												.getString(R.string.base_load_failed_des),
										Toast.LENGTH_SHORT);
					}

				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();

				map.put("M", "get_chuyu");

				return map;

			}

		};
		mQueue.add(stringRequest);
	}

}
