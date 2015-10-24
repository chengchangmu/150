package com.scsy150.mine.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
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
import com.scsy150.mine.adapter.ContactsAdapter;
import com.scsy150.mine.bean.ContactsBean;
import com.scsy150.util.LogUtil;
import com.scsy150.util.ResourcesUtil;
import com.scsy150.util.ToastUtil;
import com.scsy150.util.view.OnClick;
import com.scsy150.util.view.ViewInject;
import com.scsy150.util.view.ViewUtils;
import com.scsy150.volley.net.RequestAndLoadCookies;
import com.scsy150.widget.RefreshListView;

/*
 * Copyright (C) 2015 四川硅谷科技重庆分公司
 * 版权所有
 *
 * 功能描述：所有联系人界面
 * 作者：硅谷科技
 * 创建时间：2015-08-24
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */

public class AllContactsActivity extends BaseActivity implements
		OnClickListener {

	@ViewInject(R.id.pull_to_refresh_list)
	private RefreshListView mPullToRefreshListView;

	private ContactsAdapter mAdapter;

	@Override
	public void addViewIntoContent() {
		View view = View.inflate(this, R.layout.layout_fragment_system, null);
		mBaseContent.addView(view);
		ViewUtils.inject(this);
		setLeftVisibility(View.VISIBLE);
		setRightDrawable(R.drawable.sex_selector);
		setRightVisibility(View.VISIBLE);
		setTitle(ResourcesUtil.getString(R.string.user_option7));
		getNetData();
	}

	private void setAdapter(ArrayList<ContactsBean> list) {
		if (list != null && list.size() > 0) {
			mAdapter = new ContactsAdapter(this, list);
			mPullToRefreshListView.setAdapter(mAdapter);
		}
	}

	@Override
	@OnClick({ R.id.left_image, R.id.right_image })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_image:
			finish();
			break;
		case R.id.right_image:
			showPopup(v);
			break;
		default:
			break;
		}
	}

	public void showPopup(View view) {
		View contentView = LayoutInflater.from(AllContactsActivity.this)
				.inflate(R.layout.popup_select_sex, null);
		final PopupWindow sexSelectWindow = new PopupWindow(contentView,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		sexSelectWindow.setTouchable(true);

		sexSelectWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				LogUtil.i("mengdd", "onTouch : ");

				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 sexSelectWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});

		// 如果不设置背景，无论是点击外部区域还是Back键都无法dismiss弹框
		sexSelectWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.title_list_selector));

		// 设置好参数之后再show
		sexSelectWindow.showAsDropDown(view);
	}

	private void getNetData() {
		RequestAndLoadCookies stringRequest = new RequestAndLoadCookies(
				AllContactsActivity.this, Method.POST, MzApi.LOGIN_REG,
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
								Type listType = new TypeToken<ArrayList<ContactsBean>>() {
								}.getType();
								ArrayList<ContactsBean> list = gson.fromJson(
										jo.getString("Result"), listType);
								setAdapter(list);

							} catch (JSONException e) {
								e.printStackTrace();
							}

						} else {

							removeProgressDialog();

							ToastUtil.showCenterToast(AllContactsActivity.this,
									item.getMessage() + "", Toast.LENGTH_SHORT);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						removeProgressDialog();
						ToastUtil
								.showCenterToast(
										AllContactsActivity.this,
										ResourcesUtil
												.getString(R.string.base_load_failed_des),
										Toast.LENGTH_SHORT);
					}

				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();

				map.put("M", "Get_My_Friends");

				return map;

			}

		};
		mQueue.add(stringRequest);
	}

}
