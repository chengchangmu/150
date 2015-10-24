package com.scsy150.date.fragment;

import android.annotation.SuppressLint;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

import com.scsy150.R;
import com.scsy150.base.BaseFragment;

public class DateFragment extends BaseFragment implements OnClickListener {
	RadioButton rb_date;
	RadioButton rb_order;
	OrderFragment orderFragment;
	NowDateFragment nowDateFragment;
	NoWifiFragment noWifiFragment;
	String MacID;

	/**
	 * frament隐藏显示分类
	 * 
	 * @param i
	 */
	@SuppressLint("CommitTransaction")
	private void selet(int i) {
		FragmentManager fm = mActivity.getSupportFragmentManager();
		FragmentTransaction frTransaction = fm.beginTransaction();
		hideFragment(frTransaction);
		switch (i) {
		case 0:
			if (orderFragment == null) {
				orderFragment = new OrderFragment();
				frTransaction.add(R.id.fr_date_content, orderFragment);
			} else {
				frTransaction.show(orderFragment);
			}

			break;
		case 1:
			// TODO 需要判断当前wifi的Mac地址是否符合商家mac地址。
			/**
			 * 拿到当前连接wifi的bssid与服务器进行判断，是否一致 如果一直，则显示加载当前的wifi下的连接的所有人
			 * 否则提示没有连接当前wifi
			 */
			if (nowDateFragment == null) {
				nowDateFragment = new NowDateFragment();
				frTransaction.add(R.id.fr_date_content, nowDateFragment);
			} else {
				frTransaction.show(nowDateFragment);
			}
			break;
		default:
			break;
		}
		frTransaction.commit();

	}

	private void hideFragment(FragmentTransaction frTransaction) {
		if (orderFragment != null) {
			frTransaction.hide(orderFragment);
		}
		if (nowDateFragment != null) {
			frTransaction.hide(nowDateFragment);
		}

	}

	/**
	 * 拿到activity传递过来的wifi数据
	 * 
	 */
	public void getMacinfo(String macId) {
		this.MacID = macId;
	}

	@Override
	public void addView() {
		View view = View
				.inflate(mActivity, R.layout.layout_fragment_date, null);
		// 初始化
		// 初始化
		View headView = View.inflate(mActivity, R.layout.app_head, null);
		rb_date = (RadioButton) headView.findViewById(R.id.near);
		rb_order = (RadioButton) headView.findViewById(R.id.hot);

		rb_date.setOnClickListener(this);
		rb_order.setOnClickListener(this);

		//选择当前的fragment
		selet(0);
		
		mContainer.addView(view);
	}

	@Override
	public void fragmentClick(View v) {
		switch (v.getId()) {
		case R.id.near:
			selet(0);
			break;
		case R.id.recent:
			selet(1);
			break;

		default:
			break;
		}
	}

}
