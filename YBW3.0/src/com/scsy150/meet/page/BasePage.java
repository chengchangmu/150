package com.scsy150.meet.page;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * 注意在子类继承的构造函数一定要调用initData()方法，不然就会出现只预加载一个界面的数据
 * 
 * @author CCM
 * 
 */
public abstract class BasePage implements OnClickListener {

	public Activity mActivity;
	public View mRootView;

	public RequestQueue mQueue;

	public BasePage() {
	}

	public BasePage(Activity activity) {
		mActivity = activity;
		mRootView = initView();
		if (mQueue == null) {
			mQueue = Volley.newRequestQueue(activity);
		}
	}

	@Override
	public void onClick(View v) {
		pageClick(v);
	}

	public abstract View initView();

	public abstract void pageClick(View v);

	public void initData() {

	}
}
