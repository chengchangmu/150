package com.scsy150.common;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.scsy150.R;
import com.scsy150.base.BaseActivity;
import com.scsy150.base.BaseBean;
import com.scsy150.consts.MzApi;
import com.scsy150.consts.SystemConsts;
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
 * 功能描述：一个内容一个按钮通用界面
 * 作者：硅谷科技
 * 创建时间：2015-09-12
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */

public class ContentActivity extends BaseActivity {

	@ViewInject(R.id.input_num)
	private TextView mInputNum;
	@ViewInject(R.id.input)
	private EditText mInput;
	@ViewInject(R.id.ok)
	private TextView mOk;
	public static final String CONTENT_TITLE = "content_title";
	public static final String CONTENT_INPUT_HINT = "content_input_hint";
	public static final String CONTENT_INPUT_NUM = "content_input_num";
	public static final String CONTENT_INPUT = "content_input";
	public static final String CONTENT_TYPE = "content_type";

	/**
	 * 修改个性签名
	 */
	public static final int CONTENT_TYPE_SIGN = 1000;
	/**
	 * 修改昵称
	 */
	public static final int CONTENT_TYPE_NICKNAME = 1001;
	private Intent mIntent;
	private int maxLen;
	private String mSign;
	private int mType;

	@Override
	public void addViewIntoContent() {
		View view = View.inflate(this, R.layout.activity_content, null);
		mBaseContent.addView(view);
		ViewUtils.inject(this);
		setLeftVisibility(View.VISIBLE);
		mIntent = getIntent();
		init();
	}

	public void init() {

		// 设置标题
		setTitle(mIntent.getStringExtra(CONTENT_TITLE));
		mType = mIntent.getIntExtra(CONTENT_TYPE, 0);
		mSign = mIntent.getStringExtra(CONTENT_INPUT);
		if (TextUtils.isEmpty(mSign)) {
			// 设置输入提示
			mInput.setHint(mIntent.getStringExtra(CONTENT_INPUT_HINT));
		} else {
			//
			mInput.setText(mSign);
		}

		maxLen = mIntent.getIntExtra(CONTENT_INPUT_NUM, 0);
		// 设置输入多少个字
		mInputNum.setText(String.format(
				getResources().getString(R.string.input_num), maxLen));

		// 在代码中使用InputFilter 进行过滤
		mInput.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
				maxLen) }); // 即限定最大输入字符数为20
		mInput.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				Editable editable = mInput.getText();
				int len = editable.length();

				if (len > maxLen) {
					int selEndIndex = Selection.getSelectionEnd(editable);
					String str = editable.toString();
					// 截取新字符串
					String newStr = str.substring(0, maxLen);
					mInput.setText(newStr);
					editable = mInput.getText();

					// 新字符串的长度
					int newLen = editable.length();
					// 旧光标位置超过字符串长度
					if (selEndIndex > newLen) {
						selEndIndex = editable.length();
					}
					// 设置新光标所在的位置
					Selection.setSelection(editable, selEndIndex);

				}

				mInputNum.setText(String.format(
						getResources().getString(R.string.input_num), maxLen
								- len));

			}
		});

	}

	@OnClick({ R.id.left_image, R.id.ok })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_image:

			finish();
			break;
		case R.id.ok:
			editInfo(mType);
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * UpdateType 修改字段 必填(签名：Talk/昵称：NickName/生日:Brithday) UpdateValue 修改值
	 * 
	 * 
	 * @param type
	 */
	private void editInfo(int type) {

		RequestAndLoadCookies stringRequest = new RequestAndLoadCookies(
				ContentActivity.this, Method.POST, MzApi.LOGIN_REG,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String str) {
						LogUtil.d("TAG", str);
						removeProgressDialog();
						Gson gson = new Gson();
						BaseBean item = gson.fromJson(str, BaseBean.class);

						ToastUtil.showCenterToast(ContentActivity.this,
								item.getMessage() + "", Toast.LENGTH_SHORT);
						if (item.getIsSuccess() == SystemConsts.RESPONSE_SUCCESS) {
							int tmp = 0;
							switch (mType) {
							case CONTENT_TYPE_SIGN:
								tmp = CONTENT_TYPE_SIGN;
								break;
							case CONTENT_TYPE_NICKNAME:
								tmp = CONTENT_TYPE_NICKNAME;
								break;
							}
							mIntent.putExtra(CONTENT_INPUT, mInput.getText()
									.toString());
							ContentActivity.this.setResult(tmp, mIntent);
							ContentActivity.this.finish();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						removeProgressDialog();
						ToastUtil
								.showCenterToast(
										ContentActivity.this,
										ResourcesUtil
												.getString(R.string.base_load_failed_des),
										Toast.LENGTH_SHORT);
					}

				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();
				// UpdateType 修改字段 必填(签名：Talk/昵称：NickName/生日:Brithday)
				// UpdateValue 修改值
				map.put("M", "UpdateUserInfo");
				if (mType == CONTENT_TYPE_SIGN) {
					/**
					 * 修改个性签名
					 */
					map.put("UPDATETYPE", "Talk");
				} else if (mType == CONTENT_TYPE_NICKNAME) {
					/**
					 * 修改昵称
					 */
					map.put("UPDATETYPE", "NickName");
				}
				map.put("UPDATEVALUE", mInput.getText().toString());

				return map;

			}

		};
		mQueue.add(stringRequest);
	}
}
