package com.scsy150.meet.page.impl;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import com.scsy150.R;
import com.scsy150.meet.page.BasePage;

public class ErrorPage extends BasePage {

	public ErrorPage(Activity activity) {
		super(activity);
	}

	@Override
	public View initView() {
		return LayoutInflater.from(mActivity).inflate(R.layout.page_error, null);
	}

	@Override
	public void pageClick(View v) {
		
	}

}
