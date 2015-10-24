package com.scsy150.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.scsy150.R;

public class NormalDialog extends Dialog {
	private TextView mTvTitle;
	private TextView mTvContent;
	private Button mBtnLeft;
	private Button mBtnRight;
	private DialogBean mBean;
	
	public NormalDialog(Context context, DialogBean bean) {
		super(context, R.style.common_dialog);
		mBean = bean;
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_dialog);
		setCanceledOnTouchOutside(false);
		mTvTitle = (TextView) findViewById(R.id.dialog_title);
		mTvContent = (TextView) findViewById(R.id.dialog_content);
		mBtnLeft = (Button) findViewById(R.id.dialog_left_btn);
		mBtnRight = (Button) findViewById(R.id.dialog_right_btn);
		init();
	}
	
	private void init(){
		if(mBean != null){
			if(mBean.TitleRes != 0){
				mTvTitle.setText(mBean.TitleRes);
			} else if(!TextUtils.isEmpty(mBean.Title)){
				mTvTitle.setText(mBean.Title);
			}
			if(mBean.ContentRes != 0 ){
				mTvContent.setText(mBean.ContentRes);
			} else if(!TextUtils.isEmpty(mBean.Content)){
				mTvContent.setText(mBean.Content);
			}
			if(mBean.LeftListener != null){
				if(mBean.LeftRes != 0){
					mBtnLeft.setText(mBean.LeftRes);
				} else if(!TextUtils.isEmpty(mBean.LeftTxt)){
					mBtnLeft.setText(mBean.LeftTxt);
				}
				mBtnLeft.setOnClickListener(mBean.LeftListener);
			} else {
				mBtnLeft.setVisibility(View.GONE);
			}
			if(mBean.RightListener != null){
				if(mBean.RightRes != 0){
					mBtnRight.setText(mBean.RightRes);
				} else if(!TextUtils.isEmpty(mBean.RightTxt)){
					mBtnRight.setText(mBean.RightTxt);
				}
				mBtnRight.setOnClickListener(mBean.RightListener);
			} else {
				mBtnRight.setVisibility(View.GONE);
			}
		}
	}
}
