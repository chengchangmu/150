package com.scsy150.main;

import java.util.HashMap;
import java.util.Map;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.scsy150.R;
import com.scsy150.base.BaseFragment;
import com.scsy150.chat.SystemFragment;
import com.scsy150.chat.activity.ChatAllHistoryFragment;
import com.scsy150.util.LogUtil;
import com.scsy150.util.view.ViewUtils;

public class MessageFragment extends BaseFragment implements OnClickListener {

	private final String TAG_SYS = "tagSys";
	private final String TAG_CHAT = "tagChat";
	private FragmentManager mFragmentMrg;
	private Map<String, Fragment> mFragmentMap;
	private Fragment mCurrentFragment;

	private void switchFragment(Fragment to) {
		if (mCurrentFragment != to) {
			FragmentTransaction transaction = mActivity
					.getSupportFragmentManager().beginTransaction();
			if (!to.isAdded()) { // 先判断是否被add过
				transaction.hide(mCurrentFragment).add(R.id.fr_message_content, to)
						.commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else {
				transaction.hide(mCurrentFragment).show(to).commit(); // 隐藏当前的fragment，显示下一个
			}
			mCurrentFragment = to;
		}
	}

	@Override
	public void addView() {

		View view = View.inflate(mActivity,
				R.layout.layout_fragment_message, null);
		mContainer.addView(view);
		ViewUtils.inject(mActivity, view);

		init();

	}

	private void init() {
		mFragmentMrg = mActivity.getSupportFragmentManager();
		mFragmentMap = new HashMap<String, Fragment>();
		Fragment first = new SystemFragment();
		mFragmentMrg.beginTransaction().add(R.id.fr_message_content, first).commit();
		mFragmentMap.put(TAG_SYS, first);
		mCurrentFragment = (BaseFragment) first;

	}

	@Override
	public void fragmentClick(View v) {
		Fragment temp = null;
		switch (v.getId()) {
		/**
		 * 系统
		 */
		case R.id.near:
			temp = mFragmentMap.get(TAG_SYS);
			if (temp == null) {
				temp = new SystemFragment();
				mFragmentMap.put(TAG_SYS, temp);
			}
			switchFragment(temp);
			LogUtil.d("-----------------", "系统模块11111111");
			break;
		/**
		 * 会话
		 */
		case R.id.recent:
			temp = mFragmentMap.get(TAG_CHAT);
			if (temp == null) {
				temp = new ChatAllHistoryFragment();
				mFragmentMap.put(TAG_CHAT, temp);
			}
			switchFragment(temp);
			LogUtil.d("-----------------", "会话模块999999999");
			break;

		default:
			break;
		}
	}

}
