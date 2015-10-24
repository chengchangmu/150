package com.scsy150.mine.activity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.scsy150.R;
import com.scsy150.base.BaseActivity;
import com.scsy150.util.ResourcesUtil;
import com.scsy150.util.view.OnClick;
import com.scsy150.util.view.ViewInject;
import com.scsy150.util.view.ViewUtils;

/*
 * Copyright (C) 2015 四川硅谷科技重庆分公司
 * 版权所有
 *
 * 功能描述：我的之设置界面
 * 作者：硅谷科技
 * 创建时间：2015-08-24
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */

public class SettingActivity extends BaseActivity implements OnClickListener {

	@ViewInject(R.id.setting_opt2)
	private LinearLayout mOpt2;
	@ViewInject(R.id.setting_opt3)
	private LinearLayout mOpt3;
	@ViewInject(R.id.setting_opt4)
	private LinearLayout mOpt4;
	@ViewInject(R.id.setting_opt5)
	private LinearLayout mOpt5;
	@ViewInject(R.id.setting_opt6)
	private LinearLayout mOpt6;
	@ViewInject(R.id.setting_opt7)
	private LinearLayout mOpt7;
	@ViewInject(R.id.setting_opt8)
	private LinearLayout mOpt8;

	@ViewInject(R.id.exit)
	private TextView mExit;

	@ViewInject(R.id.swtich)
	private ToggleButton mSwitch;

	@Override
	public void addViewIntoContent() {
		View view = View.inflate(this, R.layout.activity_mine_setting, null);
		mBaseContent.addView(view);
		ViewUtils.inject(this);
		setLeftVisibility(View.VISIBLE);
		setTitle(ResourcesUtil.getString(R.string.user_option9));
		initData();
	}

	private void initData() {
		mSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					// 选中
				} else {
					// 未选中
				}
			}
		});
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

}
