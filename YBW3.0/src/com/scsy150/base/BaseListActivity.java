package com.scsy150.base;

import android.view.View;

import com.scsy150.R;
import com.scsy150.widget.pulltorefresh.PullToRefreshListView;

public abstract class BaseListActivity extends BaseActivity {
	public PullToRefreshListView mListView;
	
	@Override
	public void addViewIntoContent() {
		View view = View.inflate(this, R.layout.activity_list, null);
		mBaseContent.addView(view);
		mListView = (PullToRefreshListView) findViewById(R.id.pull_to_refresh_list);
		init();
	}
	
	public abstract void init();
}
