package com.scsy150.meet.activity;

import java.util.ArrayList;

import com.scsy150.R;
import com.scsy150.meet.bean.MeetInfoBean;
import com.scsy150.meet.page.adapter.CommonAdapter;
import com.scsy150.util.LogUtil;
import com.scsy150.widget.pulltorefresh.PullToRefreshBase;
import com.scsy150.widget.pulltorefresh.PullToRefreshListView;
import com.scsy150.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.scsy150.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

public class DialogActivity extends Activity implements OnClickListener {
	private LinearLayout layout01, layout02, layout03;
	private PullToRefreshListView refreshListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dialog);
		refreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		initPullToRefreshListView();
		//initView();
	}

	/**
	 * 初始化下拉刷新ListView
	 */
	private void initPullToRefreshListView() {
		refreshListView.setMode(Mode.BOTH);
		refreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshListView.getCurrentMode() == Mode.PULL_FROM_START) {
					// 上拉。加载更多
					// loadDataAndRefreshView();
				  	LogUtil.d("----------------", "上拉加载更多...");
				  	refreshListView.onRefreshComplete();
				} else if (refreshListView.getCurrentMode() == Mode.PULL_FROM_END) {
					// 下拉，直接恢复  
					/*
					 * CommonUtil.runOnUIThread(new Runnable() {
					 * 
					 * @Override public void run() {
					 * refreshListView.onRefreshComplete(); } });
					 */
					LogUtil.d("----------------", "下拉加载更多...");
					refreshListView.onRefreshComplete();
				}
			}
		});

		ListView mLvContainer = refreshListView.getRefreshableView();
		mLvContainer.setDividerHeight(0);// 隐藏divider
		mLvContainer.setSelector(android.R.color.transparent);
		
		ArrayList<MeetInfoBean> list = new ArrayList<MeetInfoBean>();
		list.add(new MeetInfoBean());
		mLvContainer.setAdapter(new CommonAdapter(
				this, list));
	}
	
	private void initView() {
		layout01 = (LinearLayout) findViewById(R.id.llayout01);
		layout02 = (LinearLayout) findViewById(R.id.llayout02);
		layout03 = (LinearLayout) findViewById(R.id.llayout03);

		layout01.setOnClickListener(this);
		layout02.setOnClickListener(this);
		layout03.setOnClickListener(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	@Override
	public void onClick(View v) {

	}
}
