package com.scsy150.main;

import java.util.ArrayList;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import com.scsy150.R;
import com.scsy150.account.LoginActivity;
import com.scsy150.base.BaseActivity;
import com.scsy150.base.YBWApplication;
import com.scsy150.consts.SystemConsts;
import com.scsy150.util.ApnUtil;
import com.scsy150.util.FileUtil;
import com.scsy150.util.SharedPreferencesUtil;
import com.scsy150.util.ToastUtil;

public class GuideActivity extends BaseActivity {
	private ViewPager mViewPager = null;
	private ArrayList<View> mPageList = null;
	private Intent mIntent;
	private boolean isShown;
	private SharedPreferencesUtil sharedpreferencesutil;
	public static final String AUTO_LOGIN = "autoLogin";

	@Override
	public void addViewIntoContent() {
		FileUtil.prepareDir();
		((YBWApplication) getApplication()).beginLocation();
		mIntent = new Intent();
		sharedpreferencesutil = new SharedPreferencesUtil(GuideActivity.this);
		isShown = sharedpreferencesutil.getBoolean(SystemConsts.GUID_CONFIGURE,
				false);
		if (isShown == true) {
			final int phone = sharedpreferencesutil.getInt(
					SystemConsts.USER_ID, 0);
			final String pwd = sharedpreferencesutil.getString(
					SystemConsts.USER_PWD, "");
			setContentView(R.layout.activity_splash_screen);

			if ((0 != phone) && !TextUtils.isEmpty(pwd)) {
				autoLogin();
			} else {
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						startMain(false);
					}
				}, 2000);
			}
		} else {
			setContentView(R.layout.guide);
			initView();
		}
	}

	public void autoLogin() {
		if (ApnUtil.isNetworkAvailable(this)) {
			startLogin();
			return;
		}
	}

	private void setSplah(final SplashScreenBean bean, final boolean isLogin) {
		AlphaAnimation animation = new AlphaAnimation(0, 1);
		animation.setDuration(800);
		animation.setFillAfter(true);
		new CountDownTimer(4000, 2000) {

			@Override
			public void onTick(long arg0) {

			}

			@Override
			public void onFinish() {
				if (isLogin) {
					mIntent.setClass(GuideActivity.this, MainActivity.class);
				} else {
					mIntent.setClass(GuideActivity.this, LoginActivity.class);
				}
				startActivity(mIntent);
				GuideActivity.this.overridePendingTransition(
						R.anim.enter_from_top, R.anim.out_from_bottom);
				GuideActivity.this.finish();
			}
		}.start();
	}

	public void startMain(boolean isLogin) {
		setSplah(null, isLogin);
	}

	public void startLogin() {
		ToastUtil.showToast(GuideActivity.this, R.string.auto_login_failed,
				Toast.LENGTH_LONG);
		startMain(false);
	}

	private void initView() {
		LayoutInflater inflater = getLayoutInflater();
		mViewPager = (ViewPager) findViewById(R.id.guidePages);
		mPageList = new ArrayList<View>();
		mPageList.add(inflater.inflate(R.layout.guide_view1, null));
		mPageList.add(inflater.inflate(R.layout.guide_view2, null));
		mPageList.add(inflater.inflate(R.layout.guide_view3, null));

		mPageList.get(2).findViewById(R.id.btn_enter)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mIntent.setClass(GuideActivity.this,
								LoginActivity.class);
						startActivity(mIntent);
						GuideActivity.this.finish();
						sharedpreferencesutil.putBoolean(
								SystemConsts.GUID_CONFIGURE, true);
					}
				});
		mViewPager.setAdapter(new GuidePageAdapter());
	}

	class GuidePageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mPageList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mPageList.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mPageList.get(arg1));
			return mPageList.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

		@Override
		public void finishUpdate(View arg0) {
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && isShown) {

			GuideActivity.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
