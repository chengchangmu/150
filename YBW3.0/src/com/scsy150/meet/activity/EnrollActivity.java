package com.scsy150.meet.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.scsy150.R;
import com.scsy150.base.BaseActivity;
import com.scsy150.consts.SystemConsts;
import com.scsy150.util.LogUtil;
import com.scsy150.util.ResourcesUtil;
import com.scsy150.util.SharedPreferencesUtil;
import com.scsy150.util.view.OnClick;
import com.scsy150.util.view.ViewInject;
import com.scsy150.util.view.ViewUtils;

public class EnrollActivity extends BaseActivity implements OnClickListener {

	@ViewInject(R.id.tv_username)
	private TextView mTvUserName;
	@ViewInject(R.id.tv_user_sex)
	private TextView mTvUserSex;
	@ViewInject(R.id.tv_user_phone)
	private TextView mTvUserPhone;
	@ViewInject(R.id.tv_activity_title)
	private TextView mTvActivityTitle;
	@ViewInject(R.id.tv_activity_date)
	private TextView mTvActivityDate;
	@ViewInject(R.id.tv_activity_place)
	private TextView mTvtvActivityPlace;
	@ViewInject(R.id.tv_enroll_cost)
	private TextView mTvEnrollCost;
	@ViewInject(R.id.tv_commit_enroll)
	private TextView mTvCommitEnroll;
	
	@Override
	public void addViewIntoContent() {
		View view = View.inflate(this, R.layout.activity_enroll, null);
		setTitle(R.string.signup_immediately);
		setLeftTxt(R.string.back);
		
		mBaseContent.addView(view);
		ViewUtils.inject(this);
		
		bindDataToView();
	}
	
	public void bindDataToView() {
		SharedPreferencesUtil spUtil = new SharedPreferencesUtil(this);
		//int userId = spUtil.getInt(SystemConsts.USER_ID, 0);
		String nickName = spUtil.getString(SystemConsts.USER_NICKNAME, "未登录");
		String userName = spUtil.getString(SystemConsts.USER_NAME, "未登录");
		int userSex = spUtil.getInt(SystemConsts.USER_SEX, 2);
		Intent intent = getIntent();
		mTvUserName.setText(nickName);
		if(0 == userSex) {
			mTvUserSex.setText(this.getResources().getString(R.string.female));
			Drawable drawable= getResources().getDrawable(R.drawable.female);  
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); 
			mTvEnrollCost.setCompoundDrawables(drawable,null,null,null);//设置TextView的drawableleft  
			mTvEnrollCost.setCompoundDrawablePadding(10);
			mTvEnrollCost.setText(String.format(
					this.getResources().getString(R.string.member_cost), intent.getDoubleExtra("female_cost", 0)));
		} else if(1 == userSex) {
			mTvUserSex.setText(this.getResources().getString(R.string.male));
			Drawable drawable= getResources().getDrawable(R.drawable.male);  
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			//ResourcesUtil.getDrawable(R.drawable.male)
			mTvEnrollCost.setCompoundDrawables(drawable,null,null,null);
			mTvEnrollCost.setCompoundDrawablePadding(10);
			mTvEnrollCost.setText(String.format(
					this.getResources().getString(R.string.member_cost),intent.getDoubleExtra("male_cost", 0)));
		}
		mTvUserPhone.setText(userName);
		
		mTvActivityTitle.setText(intent.getStringExtra("activity_name"));
		mTvActivityDate.setText(intent.getStringExtra("activity_date"));
		mTvtvActivityPlace.setText(intent.getStringExtra("activity_place"));
	}
	
	@Override
	@OnClick(R.id.tv_commit_enroll)
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_commit_enroll:
			Intent confirmOrderIntent = new Intent(this, ConfirmOrderActivity.class);
			startActivity(confirmOrderIntent);
			finish();
			break;
		default:
			super.onClick(v);
			break;
		}
	}
}
