package com.scsy150.account;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
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
import com.scsy150.base.BaseBusiness;
import com.scsy150.consts.MzApi;
import com.scsy150.consts.SystemConsts;
import com.scsy150.util.MD5Util;
import com.scsy150.util.MyCountTimer;
import com.scsy150.util.ResourcesUtil;
import com.scsy150.util.ToastUtil;
import com.scsy150.util.view.OnClick;
import com.scsy150.util.view.ViewInject;
import com.scsy150.util.view.ViewUtils;
import com.scsy150.volley.net.RequestAndSaveCookies;

/*
 * Copyright (C) 2015 四川硅谷科技重庆分公司
 * 版权所有
 *
 * 功能描述：注册第一步界面
 * 作者：硅谷科技
 * 创建时间：2015-08-23
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */

public class RegistStepOneActivity extends BaseActivity {

	public static final String LINCESE_URL = "http://www.baidu.com";
	public static final String PHONE_KEY = "phoneKey";
	public static final String PHONE_PWD = "phonePwd";

	@ViewInject(R.id.regist_des)
	private TextView mDesContent;

	@ViewInject(R.id.regist_next_step)
	private TextView mNextStep;

	@ViewInject(R.id.phoneNumberInput)
	private EditText mEtPhone;

	@ViewInject(R.id.regist_get_verify_code)
	private TextView mGetVerifyCode;

	@ViewInject(R.id.verificationCodeInput)
	private EditText mEtVerifyCode;

	@ViewInject(R.id.pwInput)
	private EditText mEtPwd;

	private String VerCode = "";

	@Override
	public void addViewIntoContent() {
		View view = View.inflate(this, R.layout.activity_regist_step_one, null);
		mBaseContent.addView(view);
		ViewUtils.inject(this);
		setLeftVisibility(View.VISIBLE);
		setTitle(R.string.regist_step_one);

		SpannableString spanString = new SpannableString(
				ResourcesUtil.getString(R.string.regist_des));
		ForegroundColorSpan span = new ForegroundColorSpan(
				ResourcesUtil.getColor(R.color.common_small_des_text));

		spanString.setSpan(span, 0, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		mDesContent.setText(spanString);
		mDesContent.setMovementMethod(LinkMovementMethod.getInstance());
	}

	@OnClick({ R.id.left_image, R.id.regist_get_verify_code,
			R.id.regist_next_step })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_image:
			finish();
			break;
		case R.id.regist_get_verify_code:
			try {
				getRegisterCode();
			} catch (IOException e) {
				e.printStackTrace();
			}

			break;
		case R.id.regist_next_step:
			goNext();
			break;
		default:
			break;
		}
	}

	private void getRegisterCode() throws IOException {
		final String phone = mEtPhone.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			showMsg(ResourcesUtil.getString(R.string.input_phone_num),
					R.string.tip);
		} else if (!BaseBusiness.checkPhoneNum(phone)) {
			showMsg(ResourcesUtil.getString(R.string.input_right_phone_num),
					R.string.tip);
		} else {
			getVerCode();
		}
	}

	// 登录请求
	private void getVerCode() {
		MyCountTimer timeCount = new MyCountTimer(mGetVerifyCode);// 传入了文字颜色值
		timeCount.start();
		final String phone = mEtPhone.getText().toString().trim();
		RequestAndSaveCookies stringRequest = new RequestAndSaveCookies(
				RegistStepOneActivity.this, Method.POST, MzApi.GET_NORMAL_CODE,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String str) {

						Gson gson = new Gson();
						BaseBean<Integer> item = gson.fromJson(str,
								BaseBean.class);
						if (item.getIsSuccess() == SystemConsts.RESPONSE_SUCCESS) {
							// 获取验证码成功
							VerCode = String.valueOf(item.getResult());
						}
						ToastUtil.showCenterToast(RegistStepOneActivity.this,
								item.getMessage() + "", Toast.LENGTH_SHORT);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						ToastUtil
								.showCenterToast(
										RegistStepOneActivity.this,
										ResourcesUtil
												.getString(R.string.base_load_failed_des),
										Toast.LENGTH_SHORT);
					}

				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();

				map.put("M", "ValidateMobileSend");
				map.put("PHONENUMBER", phone);
				map.put("TYPE", "register");

				String sign = "";
				try {
					sign = MD5Util.getSignature(map, SystemConsts.SIGN_KEY);
					map.put("sign", sign);
				} catch (IOException e) {
					e.printStackTrace();
				}

				return map;

			}

		};
		mQueue.add(stringRequest);
	}

	private void goNext() {
		final String password = mEtPwd.getText().toString().trim();
		String code = mEtVerifyCode.getText().toString().trim();
		String phone = mEtPhone.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			showMsg(ResourcesUtil.getString(R.string.input_phone_num),
					R.string.tip);
		} else if (!BaseBusiness.checkPhoneNum(phone)) {
			showMsg(ResourcesUtil.getString(R.string.input_right_phone_num),
					R.string.tip);
		} else if (TextUtils.isEmpty(code)) {
			showMsg(ResourcesUtil.getString(R.string.insert_identifying_code),
					R.string.tip);
		} else if (!code.equals(VerCode)) {
			showMsg(ResourcesUtil.getString(R.string.register_phone_different),
					R.string.tip);
		} else if (TextUtils.isEmpty(password)) {// 判断第一行输入框是否有为空
			showMsg(ResourcesUtil.getString(R.string.register_please_input_pw),
					R.string.error_tip_title);
		} else if (!(password.length() >= 4) || !(password.length() <= 20)) {// 判断第一行，密码是否符合4-20位字符的要求
			showMsg(ResourcesUtil.getString(R.string.register_error_length),
					R.string.error_tip_title);
		} else {
			Intent intent = new Intent();
			intent.setClass(this, RegistStepTwoActivity.class);
			intent.putExtra(RegistStepOneActivity.PHONE_KEY, phone);
			intent.putExtra(RegistStepOneActivity.PHONE_PWD, password);
			startActivity(intent);
			RegistStepOneActivity.this.finish();
		}
	}

}
