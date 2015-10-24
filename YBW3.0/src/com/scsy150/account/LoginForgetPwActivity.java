package com.scsy150.account;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.scsy150.R;
import com.scsy150.base.BaseActivity;
import com.scsy150.base.BaseBusiness;
import com.scsy150.util.view.OnClick;
import com.scsy150.util.view.ViewInject;
import com.scsy150.util.view.ViewUtils;

/*
 * Copyright (C) 2015 四川硅谷科技重庆分公司
 * 版权所有
 *
 * 功能描述：忘记密码界面
 * 作者：硅谷科技
 * 创建时间：2015-08-24
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */

public class LoginForgetPwActivity extends BaseActivity {

	@ViewInject(R.id.get_verify_code)
	private TextView mDesContent;
	@ViewInject(R.id.phoneNumberInput)
	private EditText mEtPhone;
	@ViewInject(R.id.changeVerifyCode)
	private TextView mTVChangeVerify;
	private String mPhone;
	public static final String PHONE_KEY = "phoneNumKey";

	@Override
	public void addViewIntoContent() {
		View view = View.inflate(this, R.layout.activity_login_forget_pw, null);
		mBaseContent.addView(view);
		ViewUtils.inject(this);
		setLeftVisibility(View.VISIBLE);
		setTitle(R.string.login_forget_pw);
		mDesContent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPhone = mEtPhone.getText().toString();
				if (TextUtils.isEmpty(mPhone)
						|| !BaseBusiness.checkPhoneNum(mPhone)) {
					showMsg(getString(R.string.phone_num_can_not_null),
							R.string.tip);
				} else {
					getResetCode();
				}
			}

		});
	}

	@OnClick({ R.id.left_view, R.id.changeVerifyCode })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_view:
			finish();
			break;
		case R.id.changeVerifyCode:
			//TODO
			showMsg("动态获取验证码正在实现中...敬请期待",R.string.tip);
			break;
		default:
			break;
		}
	}

	private void getResetCode() {

	}

}
