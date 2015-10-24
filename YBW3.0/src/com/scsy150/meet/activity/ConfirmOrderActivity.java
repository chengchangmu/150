package com.scsy150.meet.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.scsy150.R;
import com.scsy150.R.id;
import com.scsy150.R.layout;
import com.scsy150.R.string;
import com.scsy150.base.BaseActivity;

public class ConfirmOrderActivity extends BaseActivity implements OnClickListener {

	@Override
	public void addViewIntoContent() {
		View view = View.inflate(this, R.layout.activity_confirm_order, null);
		setTitle(R.string.signup_immediately);
		setLeftTxt(R.string.back);
		mBaseContent.addView(view);
		
		TextView mTvConfirmOrder = (TextView)findViewById(R.id.tv_confirm_order);
		mTvConfirmOrder.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_confirm_order:
			//确认付款
			Intent enrollSuccessIntent = new Intent(this, EnrollSuccessActivity.class);
			startActivity(enrollSuccessIntent);
			finish();
			break;
		default:
			super.onClick(v);
			break;
		}
	}
	
}
