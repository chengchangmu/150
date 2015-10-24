package com.scsy150.account;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.google.gson.Gson;
import com.scsy150.R;
import com.scsy150.base.BaseActivity;
import com.scsy150.base.BaseBean;
import com.scsy150.base.YBWApplication;
import com.scsy150.consts.MzApi;
import com.scsy150.consts.SystemConsts;
import com.scsy150.main.MainActivity;
import com.scsy150.util.LogUtil;
import com.scsy150.util.MD5Util;
import com.scsy150.util.ResourcesUtil;
import com.scsy150.util.SharedPreferencesUtil;
import com.scsy150.util.ToastUtil;
import com.scsy150.util.view.OnClick;
import com.scsy150.util.view.ViewInject;
import com.scsy150.util.view.ViewUtils;
import com.scsy150.volley.net.RequestAndSaveCookies;

/*
 * Copyright (C) 2015 四川硅谷科技重庆分公司
 * 版权所有
 *
 * 功能描述：登录界面
 * 作者：硅谷科技
 * 创建时间：2015-08-24
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */

public class LoginActivity extends BaseActivity implements OnClickListener {

	@ViewInject(R.id.forget_pw)
	private TextView mDesContent;

	@ViewInject(R.id.reg_acc)
	private TextView mRegAcc;

	public static final String LAST_PWD = "lastPwd";
	public static final String LOGIN_PHONE = "loginPhone";
	private final int REGISTER_REQUEST_CODE = 2002;

	@ViewInject(R.id.phoneNumberInput)
	private EditText mEtName;
	@ViewInject(R.id.pwInput)
	private EditText mEtPwd;

	@ViewInject(R.id.btn_login)
	private TextView mBtnLogin;

	private String mPwd;
	private TextWatcher mNameWatcher;

	private String reStr;

	@Override
	public void addViewIntoContent() {
		View view = View.inflate(this, R.layout.activity_login, null);
		mBaseContent.addView(view);
		ViewUtils.inject(this);
		setLeftVisibility(View.GONE);
		setTitle(ResourcesUtil.getString(R.string.login_title));
		Intent intent = getIntent();
		if (intent != null) {
			mPwd = intent.getStringExtra(LAST_PWD);
			if (!TextUtils.isEmpty(mPwd)) {
				mActvityList.remove(this);
			}
		}
		initView();
		initData();
	}

	@Override
	@OnClick({ R.id.reg_acc, R.id.left_view, R.id.btn_login, R.id.forget_pw })
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.reg_acc:
			intent.setClass(LoginActivity.this, RegistStepOneActivity.class);
			startActivity(intent);
			break;
		case R.id.left_view:
			finish();
			break;
		case R.id.btn_login:
			login();
			break;
		case R.id.forget_pw:
			intent.setClass(LoginActivity.this, LoginForgetPwActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	private void initView() {
		mNameWatcher = new TextWatcher() {

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
				mEtPwd.setText("");
			}
		};
		mEtName.addTextChangedListener(mNameWatcher);
	}

	private void initData() {
		SharedPreferencesUtil spUtil = new SharedPreferencesUtil(this);
		String name = spUtil.getString(SystemConsts.USER_NAME, "");
		String pwd = spUtil.getString(SystemConsts.USER_PWD, "");
		if (!TextUtils.isEmpty(name)) {
			mEtName.setText(name);
		} else {
			Intent intent = getIntent();
			if (intent != null) {
				String phone = intent.getStringExtra(LOGIN_PHONE);
				if (!TextUtils.isEmpty(phone)) {
					mEtName.setText(phone);
				}
			}
		}
		if (!TextUtils.isEmpty(mPwd)) {
			mEtPwd.setText(mPwd);
		} else {
			mEtPwd.setText(pwd);
		}
		if (!TextUtils.isEmpty(name)) {
			Selection.selectAll(mEtName.getText());
		}

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Gson gson = new Gson();
				BaseBean item = gson.fromJson(reStr, BaseBean.class);

				if (item.getIsSuccess() == SystemConsts.RESPONSE_SUCCESS) {

					loginChat();
					try {
						JSONObject jo = new JSONObject(reStr);
						JSONObject jo1 = jo.getJSONObject("Result");
						JSONArray ja = jo1.getJSONArray("data1");
						for (int i = 0; i < ja.length(); i++) {
							JSONObject uijo = (JSONObject) ja.get(i);
							UserInfoBean ub = gson.fromJson(uijo.toString(),
									UserInfoBean.class);
							SystemConsts.userId = ub.getUserId();
							SharedPreferencesUtil spUtil = new SharedPreferencesUtil(
									LoginActivity.this);
							spUtil.putString(SystemConsts.USER_NAME, mEtName
									.getText().toString().trim());
							spUtil.putString(SystemConsts.USER_PWD, mEtPwd
									.getText().toString().trim());
							spUtil.putString(SystemConsts.USER_NICKNAME,
									ub.getNickName());
							spUtil.putString(SystemConsts.USER_HEADIMG,
									ub.getHeadImg());
							spUtil.putInt(SystemConsts.USER_ID, ub.getUserId());
							spUtil.putInt(SystemConsts.USER_SEX, ub.getSex());

						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Intent intent = new Intent();
					intent.setClass(LoginActivity.this, MainActivity.class);

					startActivity(intent);
					finish();
				}

			}
		};
	}

	private void login() {
		final String name = mEtName.getText().toString().trim();
		final String pwd = mEtPwd.getText().toString().trim();
		if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
			showMsg(ResourcesUtil.getString(R.string.phone_or_pwd_is_null),
					R.string.tip);
		} else {
			Login_new();
		}
	}

	// 登录请求
	private void Login_new() {

		RequestAndSaveCookies stringRequest = new RequestAndSaveCookies(
				LoginActivity.this, Method.POST, MzApi.LOGIN_REG,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String str) {
						LogUtil.d("TAG", str);
						Message message = new Message();
						message.what = 0;
						handler.sendMessage(message);
						reStr = str;

						removeProgressDialog();
						Gson gson = new Gson();
						BaseBean item = gson.fromJson(str, BaseBean.class);
						if (item.getIsSuccess() == SystemConsts.AUTH_ING
								|| item.getIsSuccess() == SystemConsts.AUTH_FIAL) {
							Intent intent = new Intent();
							intent.setClass(LoginActivity.this,
									RegistStatusActivity.class);
							intent.putExtra(RegistStatusActivity.STATUS,
									item.getIsSuccess());
							startActivity(intent);
						}

						ToastUtil.showCenterToast(LoginActivity.this,
								item.getMessage() + "", Toast.LENGTH_SHORT);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						removeProgressDialog();
						ToastUtil
								.showCenterToast(
										LoginActivity.this,
										ResourcesUtil
												.getString(R.string.base_load_failed_des),
										Toast.LENGTH_SHORT);
					}

				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();

				map.put("M", "Login");
				map.put("PHONENUM", mEtName.getText().toString().trim());
				map.put("LATITUDE", SystemConsts.Latitude + "");
				map.put("LONGITUDE", SystemConsts.Longitude + "");
				map.put("PASSWORD", mEtPwd.getText().toString().trim());
				map.put("PROVINCE", SystemConsts.CURRENT_PROVINCE);
				map.put("CITY", SystemConsts.CURRENT_CITY);
				map.put("COUNTY", SystemConsts.CURRENT_DISTRICT);
				map.put("ADDRESS", SystemConsts.ADDRESS);

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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REGISTER_REQUEST_CODE
				&& resultCode == Activity.RESULT_OK && data != null) {
			String phone = data.getStringExtra(LOGIN_PHONE);
			if (!TextUtils.isEmpty(phone)) {
				mEtName.setText(phone);
			}
		}
	}

	public void loginChat() {
		final String name = mEtName.getText().toString().trim();
		final String pwd = mEtPwd.getText().toString().trim();
		// 调用sdk登陆方法登陆聊天服务器
		EMChatManager.getInstance().login(name, pwd, new EMCallBack() {// 回调
					@Override
					public void onSuccess() {
						runOnUiThread(new Runnable() {
							public void run() {
								try {
									EMGroupManager.getInstance()
											.loadAllGroups();
									EMChatManager.getInstance()
											.loadAllConversations();
									YBWApplication.isLoginChat = true;
									LogUtil.d(TAG, "登陆聊天服务器成功！");
								} catch (Exception e) {

									YBWApplication.isLoginChat = false;
									LogUtil.d(TAG, "登陆聊天服务器失败！");
								}

							}
						});
					}

					@Override
					public void onProgress(int progress, String status) {

					}

					@Override
					public void onError(int code, String message) {
						YBWApplication.isLoginChat = false;
						LogUtil.d(TAG, "登陆聊天服务器失败！");
					}
				});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}

}
