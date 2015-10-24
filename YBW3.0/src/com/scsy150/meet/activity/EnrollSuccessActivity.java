package com.scsy150.meet.activity;

import android.content.Intent;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextPaint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.scsy150.R;
import com.scsy150.R.id;
import com.scsy150.R.layout;
import com.scsy150.R.string;
import com.scsy150.base.BaseActivity;
import com.scsy150.mine.activity.OrderListActivity;

public class EnrollSuccessActivity extends BaseActivity implements OnClickListener {

	@Override
	public void addViewIntoContent() {
		View view = View.inflate(this, R.layout.activity_enroll_success, null);
		setTitle(R.string.order_success);
		setLeftTxt(R.string.back);
		mBaseContent.addView(view);
		
		//设置字体加粗
		TextView mTvOrderInformation = (TextView)findViewById(R.id.tv_order_information);
		TextPaint tp = mTvOrderInformation.getPaint(); 
		tp.setFakeBoldText(true); 
		
		Button mBtnCallOrganizer = (Button) findViewById(R.id.btn_call_organizer);
		Button mBtnListOrder = (Button) findViewById(R.id.btn_list_order);
		
		mBtnCallOrganizer.setOnClickListener(this);
		mBtnListOrder.setOnClickListener(this);
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_call_organizer:
			//联系商家
            Intent telIntent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+"10086"));  
            startActivity(telIntent); 
            finish();
			break;
		case R.id.btn_list_order:
			//订单列表
			Intent listIntent = new Intent(this, OrderListActivity.class);  
            startActivity(listIntent);
            finish();
			break;
		default:
			super.onClick(v);
			break;
		}
	}
}
