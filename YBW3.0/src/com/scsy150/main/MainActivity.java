package com.scsy150.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.RequestQueue;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.scsy150.R;
import com.scsy150.account.LoginActivity;
import com.scsy150.account.ResetPwActivity;
import com.scsy150.base.BaseActivity;
import com.scsy150.base.BaseFragment;
import com.scsy150.chat.utils.Timer;
import com.scsy150.date.fragment.DateFragment;
import com.scsy150.dialog.YBWDialog;
import com.scsy150.dialog.YBWDialog.DialogOnClickListener;
import com.scsy150.util.AnimationUtil;
import com.scsy150.util.LogUtil;
import com.scsy150.util.ScreenUtil;
import com.scsy150.util.view.OnClick;
import com.scsy150.util.view.OnRadioGroupCheckedChange;
import com.scsy150.util.view.ViewInject;
import com.scsy150.util.view.ViewUtils;

public class MainActivity extends BaseActivity implements OnClickListener {
	@ViewInject(R.id.main_content)
	private LinearLayout mMainContent;
	@ViewInject(R.id.main_tab)
	private RadioGroup mButtomTab;

	private final String TAG_USER = "tagUser";
	private final String TAG_DATE = "tagDate";
	private final String TAG_MESSAGE = "tagMessage";
	private final String TAG_MINE = "tagMine";
	private FragmentManager mFragmentMrg;
	private Map<String, Fragment> mFragmentMap;
	private Fragment mCurrentFragment;
	private String wifiMac;
	private WifiInfo wifiInfo;

	public RequestQueue mQueue;
	/*
	 * public RadioGroup mainChoice; public RadioButton mainNear; public
	 * RadioButton mainHot; public RadioButton mainRecent;
	 */
	public ArrayList<RadioButton> buttonList;
	private View mainView;

	private boolean isOpen = false;
	private float mScreenWidth;

	@Override
	public void addViewIntoContent() {

		mainView = View.inflate(this, R.layout.activity_main, null);
		mBaseContent.addView(mainView);
		ViewUtils.inject(this);
		// 如果聊天没有登录成功，会自动登录聊天服务器
		// Timer.connectChat(getApplicationContext());
		init();

		getwifiInfo();

	}

	/**
	 * 拿到wifi信息
	 */
	private void getwifiInfo() {
		WifiManager mWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		if (!mWifi.isWifiEnabled()) {
			mWifi.setWifiEnabled(true);
		}
		wifiInfo = mWifi.getConnectionInfo();
		wifiMac = wifiInfo.getBSSID();// WIFI的mac地址

	}

	private void init() {
		// setLeftDrawable(R.drawable.share_selector);
		setRightDrawable(R.drawable.menu_selector);
		setLeftVisibility(View.GONE);
		mFragmentMrg = getSupportFragmentManager();
		mFragmentMap = new HashMap<String, Fragment>();
		buttonList = new ArrayList<RadioButton>();
		Fragment first = new MeetFragment(buttonList);
		mFragmentMrg.beginTransaction().add(R.id.main_content, first).commit();
		mFragmentMap.put(TAG_USER, first);
		mCurrentFragment = (BaseFragment) first;

		buttonList.add(mNear);
		buttonList.add(mHot);
		buttonList.add(mRecent);

		mScreenWidth = ScreenUtil.getScreenWidth(this);

		setContentViewBackground(R.color.layout_decor_bg);

		// 显示SlidingMenu
		super.controlSlidingMenu(SlidingMenu.TOUCHMODE_NONE);

		mMenu.setOnCloseListener(new OnCloseListener() {

			@Override
			public void onClose() {
				mBaseView.setScaleX(1.0f);
				mBaseView.setScaleY(1.0f);
				mBaseView.setTranslationX(-50f + 0.1f * mScreenWidth);
			}
		});

	}

	private void switchFragment(Fragment to) {
		if (mCurrentFragment != to) {
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			if (!to.isAdded()) { // 先判断是否被add过
				transaction.hide(mCurrentFragment).add(R.id.main_content, to)
						.commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else {
				transaction.hide(mCurrentFragment).show(to).commit(); // 隐藏当前的fragment，显示下一个
			}
			mCurrentFragment = to;
		}
	}

	@OnRadioGroupCheckedChange(R.id.main_tab)
	private void onCheckedChange(RadioGroup group, int checkedId) {
		Fragment temp = null;
		switch (checkedId) {
		/**
		 * 聚会
		 */
		case R.id.main_meet:
			mHeadLayout.setVisibility(View.VISIBLE);
			setRightVisibility(View.VISIBLE);
			temp = mFragmentMap.get(TAG_USER);
			mHeadLayout.setVisibility(View.VISIBLE);
			mNear.setText(R.string.nearby);
			mHot.setText(R.string.hot);
			mRecent.setText(R.string.newest);
			mHot.setVisibility(View.VISIBLE);
			mRgChoice.setVisibility(View.VISIBLE);
			mTvRight.setVisibility(View.VISIBLE);
			if (temp == null) {
				temp = new MeetFragment(buttonList);
				mFragmentMap.put(TAG_USER, temp);
			}
			switchFragment(temp);
			break;
		/**
		 * 约会
		 */
		case R.id.main_date:
			// TODO将拿到的wifi数据传递给frament
			if (wifiMac != null) {
				LogUtil.i("RT", wifiMac.toString() + "wifi名称："
						+ wifiInfo.getSSID().toString() + "wifi具体信息:"
						+ wifiInfo.getSupplicantState().toString() + "wifiIP:"
						+ wifiInfo.getIpAddress());
			}
			temp = mFragmentMap.get(TAG_DATE);
			setRightVisibility(View.VISIBLE);
			mHeadLayout.setVisibility(View.VISIBLE);
			mRgChoice.setVisibility(View.VISIBLE);
			mHot.setVisibility(View.GONE);
			mNear.setText(R.string.appointment);
			mRecent.setText(R.string.present);
			mTvRight.setVisibility(View.VISIBLE);
			if (temp == null) {
				temp = new DateFragment();
				mFragmentMap.put(TAG_DATE, temp);
				((DateFragment) temp).getMacinfo(wifiMac);
			}
			switchFragment(temp);
			break;
		/**
		 * 消息
		 */
		case R.id.main_message:
			temp = mFragmentMap.get(TAG_MESSAGE);
			setRightVisibility(View.GONE);
			mHot.setVisibility(View.GONE);
			mHeadLayout.setVisibility(View.VISIBLE);
			mRgChoice.setVisibility(View.VISIBLE);
			mNear.setText(R.string.system);
			mRecent.setText(R.string.conversation);
			mTvRight.setVisibility(View.INVISIBLE);
			if (temp == null) {
				temp = new MessageFragment();
				mFragmentMap.put(TAG_MESSAGE, temp);
			}
			switchFragment(temp);
			break;
		/**
		 * 我的
		 */
		case R.id.main_mine:
			temp = mFragmentMap.get(TAG_MINE);
			mHeadLayout.setVisibility(View.GONE);
			if (temp == null) {
				temp = new MineFragment();
				mFragmentMap.put(TAG_MINE, temp);
			}
			switchFragment(temp);
			break;
		default:
			break;
		}
	}

	@SuppressLint("NewApi")
	@OnClick({ R.id.left_view, R.id.right_image, R.id.near, R.id.hot,
			R.id.recent })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_view: // 分享
			break;
		case R.id.right_image: // 右侧栏
			if (!isOpen) {
				AnimationUtil.playScaleAnim(mBaseView, 1.0f, 1.0f);
			} else {
				AnimationUtil.playScaleAnim(mBaseView, 0.8f, 0.8f);
			}
			isOpen = !isOpen;
			mMenu.toggle();
			mBaseView.setScaleX(0.8f);
			mBaseView.setScaleY(0.8f);
			mBaseView.setTranslationX(50.0f);
			break;
		default:
			((BaseFragment) mCurrentFragment).onClick(v);
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			final YBWDialog dialog = new YBWDialog(this, R.string.exit,
					R.string.tip);
			dialog.setPositiveButton(R.string.cancel,
					new DialogOnClickListener() {

						@Override
						public void onClick(String firstText, String secondText) {
							dialog.dismiss();
						}
					});
			dialog.setNegativeButton(R.string.sure,
					new DialogOnClickListener() {

						@Override
						public void onClick(String firstText, String secondText) {
							MainActivity.this.finish();
							dialog.dismiss();
						}
					});
			dialog.show();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
