package com.scsy150.date.activity;

import com.scsy150.R;
import com.scsy150.date.bean.DateBean;
import com.scsy150.date.fragment.UserInfoFragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class UserInfoAcitivity extends Activity {
	UserInfoFragment userInfoFragment;
	DateBean dBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_infomation_layout);
		dBean = new DateBean();
		// fragment
		Intent intent = getIntent();
		dBean = (DateBean) intent.getExtras().getSerializable("data");
		FragmentManager fm = getFragmentManager();
		FragmentTransaction tran = fm.beginTransaction();
		if (userInfoFragment == null) {
			userInfoFragment = new UserInfoFragment();
			userInfoFragment.getdateBean(dBean);
			tran.add(R.id.ff_userInfo, userInfoFragment);
		} else {
			tran.show(userInfoFragment);
		}
		tran.commit();
		init();
		TextView tv_name = (TextView) findViewById(R.id.tv_userName);
		tv_name.setText(dBean.getNickName());

	}

	/**
	 * 初始化
	 */
	private void init() {
		findViewById(R.id.bt_back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

	}

}
