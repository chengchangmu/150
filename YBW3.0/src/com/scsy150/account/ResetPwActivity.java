package com.scsy150.account;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.scsy150.R;
import com.scsy150.base.BaseActivity;
import com.scsy150.consts.SystemConsts;
import com.scsy150.dialog.YBWDialog;
import com.scsy150.dialog.YBWDialog.DialogOnClickListener;
import com.scsy150.util.ResourcesUtil;
import com.scsy150.util.view.OnClick;
import com.scsy150.util.view.ViewInject;
import com.scsy150.util.view.ViewUtils;

/*
 * Copyright (C) 2015 四川硅谷科技重庆分公司
 * 版权所有
 *
 * 功能描述：重置密码界面
 * 作者：硅谷科技
 * 创建时间：2015-08-24
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */

public class ResetPwActivity extends BaseActivity implements OnClickListener {

	@ViewInject(R.id.send_des)
	private TextView mDes;

	@ViewInject(R.id.resend_verify_code)
	private TextView mBtnSendCode;
	@ViewInject(R.id.hidePw)
	private ImageView mBtnHidePw;

	@ViewInject(R.id.verificationCodeInput)
	private EditText mEtCode;

	@ViewInject(R.id.newPwInput)
	private EditText mEtPwd;

	@ViewInject(R.id.reset_pw_submit)
	private TextView mConfirmSubmit;

	private final int MAX_COUNT_TIME = 60;
	private int mCurrentCount = MAX_COUNT_TIME;
	private String mPhoneNum = "";
	private boolean mPwHidden = true;

	@Override
	public void addViewIntoContent() {
		View view = View.inflate(this, R.layout.activity_reset_pw, null);
		mBaseContent.addView(view);
		ViewUtils.inject(this);
		setLeftVisibility(View.VISIBLE);
		setTitle(R.string.reset_pw);

		Intent intent = getIntent();
		if (intent != null && intent.hasExtra(LoginForgetPwActivity.PHONE_KEY)) {
			mPhoneNum = getIntent().getStringExtra(
					LoginForgetPwActivity.PHONE_KEY);
		}
		String showNum = mPhoneNum;
		if (!TextUtils.isEmpty(mPhoneNum) && mPhoneNum.length() >= 7) {
			StringBuffer sb = new StringBuffer(mPhoneNum);
			sb.delete(3, 7);
			sb.insert(3, "****");
			showNum = sb.toString();
		}

		String note = ResourcesUtil
				.getString(R.string.verify_code_to_user_note);
		int len = note.length();
		SpannableString spanString = new SpannableString(note + " " + showNum);
		ForegroundColorSpan span = new ForegroundColorSpan(getResources()
				.getColor(R.color.common_bold_des_text));
		spanString.setSpan(span, len, spanString.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		span = new ForegroundColorSpan(getResources().getColor(
				R.color.common_small_des_text));
		spanString.setSpan(span, 0, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		mDes.setText(spanString);

		updateSendCodeTime();
		mTimeHandler.sendEmptyMessageDelayed(MAX_COUNT_TIME, 1000);
	}

	private void updateSendCodeTime() {
		SpannableString spanString = new SpannableString(String.format(
				ResourcesUtil.getString(R.string.re_send_pw), mCurrentCount));
		ForegroundColorSpan span = new ForegroundColorSpan(getResources()
				.getColor(R.color.common_btn_red_text));
		spanString.setSpan(span, 0, mCurrentCount < 10 ? 1 : 2,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		span = new ForegroundColorSpan(getResources().getColor(
				R.color.common_btn_grey_text));
		spanString.setSpan(span, mCurrentCount < 10 ? 2 : 3,
				spanString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		mBtnSendCode.setText(spanString);
	}

	private Handler mTimeHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MAX_COUNT_TIME:
				if (mCurrentCount <= 0) {
					mBtnSendCode.setEnabled(true);
					mBtnSendCode.setText(R.string.get_code_again);
				} else {
					mBtnSendCode.setEnabled(false);
					mCurrentCount--;
					updateSendCodeTime();
					mTimeHandler.sendEmptyMessageDelayed(MAX_COUNT_TIME, 1000);
				}
				break;

			default:
				break;
			}
		}
	};

	@Override
	@OnClick({ R.id.left_view, R.id.resend_verify_code, R.id.hidePw,
			R.id.reset_pw_submit })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_view:
			finish();
			break;
		case R.id.resend_verify_code:
//			if (!TextUtils.isEmpty(mPhoneNum)) {
//				final String taskId = BaseBusiness.getNormalCode(this,
//						mPhoneNum, BaseBusiness.CODE_FORGET_PWD,
//						new RequestListener<JSONObject>() {
//							@Override
//							public void onSuccess(JSONObject result) {
//								closeProgressDialog();
//								showMsg(BaseBusiness.getResponseMsg(
//										ResetPwActivity.this, result), true);
//								mCurrentCount = MAX_COUNT_TIME;
//								mTimeHandler.sendEmptyMessageDelayed(
//										MAX_COUNT_TIME, 1000);
//							}
//
//							@Override
//							public void onFailure(int code, String msg) {
//								closeProgressDialog();
//								showMsg(BaseBusiness.getResponseMsg(
//										ResetPwActivity.this, msg), true);
//							}
//						});
//				showProgressDialog(taskId, true);
//			}
			break;
		case R.id.hidePw:
			String str = mEtPwd.getText().toString().trim();
			if (mPwHidden) {
				mBtnHidePw.setImageDrawable(getResources().getDrawable(
						R.drawable.show_pw_selector));
				mEtPwd.setTransformationMethod(HideReturnsTransformationMethod
						.getInstance());
				mPwHidden = false;
			} else {
				mBtnHidePw.setImageDrawable(getResources().getDrawable(
						R.drawable.hide_pw_selector));
				mEtPwd.setTransformationMethod(PasswordTransformationMethod
						.getInstance());
				mPwHidden = true;
			}
			mEtPwd.setText(str);
			mEtPwd.setSelection(str.length());
			break;
		case R.id.reset_pw_submit:
			resetSubmit();
			break;
		default:
			break;
		}
	}

	private void resetSubmit() {
		String code = mEtCode.getText().toString().trim();
		String pwd = mEtPwd.getText().toString().trim();
		if (TextUtils.isEmpty(code)) {
			showMsg(ResourcesUtil.getString(R.string.input_code_is_wrong), true);
		} else if (TextUtils.isEmpty(pwd)) {
			showMsg(ResourcesUtil.getString(R.string.input_new_pwd), true);
		} else if (pwd.length() < SystemConsts.MIX_PWD_LENGTH) {
			showMsg(ResourcesUtil.getString(R.string.pwd_length_error),
					R.string.tip);
		} else {
			//TDDO
		}
	}

	private void showMsg(String msg, final boolean cancelable) {
		final YBWDialog dialog = new YBWDialog(ResetPwActivity.this, msg,
				R.string.tip);
		dialog.setCancelable(cancelable);
		dialog.setNegativeButton(R.string.sure, new DialogOnClickListener() {

			@Override
			public void onClick(String firstText, String secondText) {
				if (!cancelable) {

					Intent intent = new Intent(ResetPwActivity.this,
							LoginActivity.class);
					if (!TextUtils.isEmpty(mPhoneNum)) {
						intent.putExtra(LoginActivity.LOGIN_PHONE, mPhoneNum);
					}
					startActivity(intent);
					ResetPwActivity.this.finish();
				}
				dialog.cancel();
			}
		});
		dialog.show();
	}

}
