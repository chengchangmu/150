package com.scsy150.base;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.scsy150.R;
import com.scsy150.dialog.ProgressDialog;
import com.scsy150.dialog.YBWDialog;
import com.scsy150.dialog.YBWDialog.DialogOnClickListener;
import com.scsy150.main.MainActivity;
import com.scsy150.util.ResourcesUtil;

/*
 * Copyright (C) 2015 四川硅谷科技重庆分公司
 * 版权所有
 *
 * 功能描述：所有activity基类
 * 作者：硅谷科技
 * 创建时间：2014-3-10
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */

public abstract class BaseActivity extends FragmentActivity implements
		OnClickListener {
	public String TAG = getClass().getSimpleName();
	public RequestQueue mQueue;
	/** 全局的加载框对象，已经完成初始化. */
	private String mProgressMessage = "数据加载中...";
	public static List<Activity> mActvityList;
	public static Activity mCurrentActivity;
	protected FrameLayout mBaseContent;
	protected LinearLayout mProgressBar;
	protected LinearLayout mFailedView;
	protected View mHeadLayout;
	private TextView mTvTitle;
	private ImageView mBtnLeft;
	protected ImageView mBtnRight;
	protected TextView mTvRight;
	private ImageView mIvMiddle;
	protected boolean isCancelable = false;
	protected List<String> mRequestList;
	/** 加载框的文字说明. */
	public ProgressDialog mProgressDialog;

	public FrameLayout mParentContent;

	private View mNetNotifyView;
	protected RadioGroup mRgChoice;
	protected RadioButton mNear;
	protected RadioButton mHot;
	protected RadioButton mRecent;
	public View mBaseView;
	public Handler handler;

	/******** SlidingMenu开始 ************/
	public SlidingMenu mMenu;
	public CanvasTransformer mTransformer;
	public float mFraction;
	public LinearLayout mBaseBottom;
	public TextView mTvShare;
	public TextView mTvEnroll;
	private TextView mTvLeft;

	/******** SlidingMenu结束 ************/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mQueue = Volley.newRequestQueue(this);
		((YBWApplication)getApplication()).beginLocation();
		mBaseView = View.inflate(this, R.layout.activity_base, null);
		setContentView(mBaseView);
		mRequestList = new ArrayList<String>();
		mParentContent = (FrameLayout) findViewById(R.id.parent_content);
		mBaseContent = (FrameLayout) findViewById(R.id.base_content);
		mProgressBar = (LinearLayout) findViewById(R.id.base_progress);
		mBaseBottom = (LinearLayout) findViewById(R.id.base_bottom);

		mProgressBar.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		mFailedView = (LinearLayout) findViewById(R.id.base_failed);
		mHeadLayout = findViewById(R.id.head);
		mTvTitle = (TextView) findViewById(R.id.middle_view);
		mIvMiddle = (ImageView) findViewById(R.id.middle_image);
		mBtnLeft = (ImageView) findViewById(R.id.left_image);
		mTvLeft = (TextView) findViewById(R.id.left_view);
		mBtnRight = (ImageView) findViewById(R.id.right_image);
		mTvRight = (TextView) findViewById(R.id.right_view);

		mIvMiddle = (ImageView) findViewById(R.id.middle_image);

		mRgChoice = (RadioGroup) findViewById(R.id.rg_choice);
		mNear = (RadioButton) findViewById(R.id.near);
		mHot = (RadioButton) findViewById(R.id.hot);
		mRecent = (RadioButton) findViewById(R.id.recent);

		/*
		 * mTvShare = (TextView) findViewById(R.id.tv_share);
		 * mTvShare.setOnClickListener(this);
		 */

		mBtnLeft.setOnClickListener(this);
		mBtnRight.setOnClickListener(this);
		mTvRight.setOnClickListener(this);
		mTvLeft.setOnClickListener(this);
		addViewIntoContent();
		if (mActvityList == null) {
			mActvityList = new ArrayList<Activity>();
		}
		mActvityList.add(this);
		mCurrentActivity = this;
		
	}

	/**
	 * 设置最根View的背景色
	 * 
	 * @param resId
	 */
	public void setContentViewBackground(int resId) {
		View contentParent = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
		contentParent.setBackgroundColor(getResources().getColor(resId));
	}

	/******** SlidingMenu开始 ************/
	public void controlSlidingMenu(int mode) {
		mMenu = new SlidingMenu(this);

		mMenu.setMode(SlidingMenu.RIGHT);
		// 设置触摸屏幕的模式
		mMenu.setTouchModeAbove(mode);
		mMenu.setShadowWidthRes(R.dimen.shadow_width);
		// menu.setShadowDrawable(R.drawable.advert_00);

		initAnimation();
		// 设置滑动菜单视图的宽度
		mMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置渐入渐出效果的值
		mMenu.setFadeDegree(0.35f);
		mMenu.setBehindScrollScale(0.0f);
		mMenu.setBehindCanvasTransformer(mTransformer);
		mMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		// 为侧滑菜单设置布局
		mMenu.setMenu(R.layout.layout_menu);
		/******** SlidingMenu结束 ************/
	}

	/**
	 * 此方法必须在子类onCreate的时候调用
	 * 
	 */
	public abstract void addViewIntoContent();

	protected void needHeader(boolean isNeed) {
		if (isNeed) {
			mHeadLayout.setVisibility(View.VISIBLE);
		} else {
			mHeadLayout.setVisibility(View.GONE);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		mCurrentActivity = this;
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mRequestList.clear();
	}

	public static void clearExit() {
		ImageLoader.getInstance().clearMemoryCache();
		for (int i = 0; i < mActvityList.size(); i++) {
			Activity temp = mActvityList.get(i);
			if (!temp.isFinishing()) {
				temp.finish();
			}
		}
	}

	public void showProgress(String handlerId, boolean cancelable) {
		mRequestList.add(handlerId);
		isCancelable = cancelable;
		mFailedView.setVisibility(View.GONE);
		mProgressBar.getBackground().setAlpha(100);
		mProgressBar.setVisibility(View.VISIBLE);
	}

	public void closeProgress() {
		isCancelable = false;
		mProgressBar.setVisibility(View.GONE);
		mFailedView.setVisibility(View.GONE);
	}

	public void showFailedView(OnClickListener listener) {
		isCancelable = false;
		mProgressBar.setVisibility(View.GONE);
		mFailedView.setVisibility(View.VISIBLE);
		mFailedView.setOnClickListener(listener);
	}

	@Override
	public void setTitle(int res) {
		if (res > 0 && getResources().getText(res) != null) {
			mTvTitle.setText(res);
			mTvTitle.setVisibility(View.VISIBLE);
			mRgChoice.setVisibility(View.GONE);
			mIvMiddle.setVisibility(View.INVISIBLE);
		} else {
			mTvTitle.setVisibility(View.INVISIBLE);
		}
	}

	public void setTitle(String title) {
		if (!TextUtils.isEmpty(title)) {
			mTvTitle.setText(title);
			mTvTitle.setVisibility(View.VISIBLE);
			mRgChoice.setVisibility(View.GONE);
			mIvMiddle.setVisibility(View.INVISIBLE);
		} else {
			mTvTitle.setVisibility(View.INVISIBLE);
		}
	}

	public void setImageTitle(int drawable) {
		if (drawable > 0 && getResources().getDrawable(drawable) != null) {
			mIvMiddle.setImageResource(drawable);
			mIvMiddle.setVisibility(View.VISIBLE);
			mTvTitle.setVisibility(View.INVISIBLE);
		} else {
			mIvMiddle.setVisibility(View.INVISIBLE);
		}
	}

	public void setLeftVisibility(int visibility) {
		mBtnLeft.setVisibility(visibility);
	}

	public void setLeftViewVisibility(int visibility) {
		mTvLeft.setVisibility(visibility);
	}

	public void setRightVisibility(int visibility) {
		mTvRight.setVisibility(visibility);
		mBtnRight.setVisibility(visibility);
	}

	public void setRightTxt(int res) {
		if (res > 0 && getResources().getText(res) != null) {
			mTvRight.setText(res);
			mTvRight.setVisibility(View.VISIBLE);
		} else {
			mTvRight.setVisibility(View.INVISIBLE);
		}
	}

	public void setLeftTxt(int res) {
		if (res > 0 && getResources().getText(res) != null) {
			mTvLeft.setText(res);
			if (R.string.back == res) {
				// 返回
				Drawable drawable = getResources().getDrawable(
						R.drawable.common_back);
				// / 这一步必须要做,否则不会显示.
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				mTvLeft.setCompoundDrawables(drawable, null, null, null);
			}
			mTvLeft.setVisibility(View.VISIBLE);
		} else {
			mTvLeft.setVisibility(View.INVISIBLE);
		}
	}

	public void setLeftDrawable(int resId) {
		// 设计要求，head bar的左边图标只有主页时左边距是36px，其他页面统一为28px，这里特殊处理一下
		if (this instanceof MainActivity) {
			RelativeLayout.LayoutParams rLp = (RelativeLayout.LayoutParams) mBtnLeft
					.getLayoutParams();
			if (rLp != null) {
				rLp.width = ResourcesUtil.getDimensionPixelSize(R.dimen.px110);
			}
		}

		mBtnLeft.setImageResource(resId);
		mBtnLeft.setVisibility(View.VISIBLE);
	}

	public void setRightDrawable(int resId) {
		mBtnRight.setImageResource(resId);
		mBtnRight.setVisibility(View.VISIBLE);
		mTvRight.setVisibility(View.GONE);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (isCancelable && keyCode == KeyEvent.KEYCODE_BACK) {
			isCancelable = false;
			closeProgress();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 显示对话框形式的加载提示
	 * 
	 * @param handler
	 *            数据访问的handler
	 * @param cancelable
	 *            是否可以取消请求
	 */
	public void showProgressDialog(final String handlerId, boolean cancelable) {
		mRequestList.add(handlerId);
		isCancelable = cancelable;
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(this);
		}
		mProgressDialog.setCancelable(isCancelable);
		mProgressDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				isCancelable = false;
			}
		});
		mProgressDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				isCancelable = false;
			}
		});
		mProgressDialog.show();
	}

	/**
	 * 取消加载对话框
	 */
	public void closeProgressDialog() {
		isCancelable = false;
		if (mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}

	public void showMsg(String msg, int titleRes) {
		if (this.isFinishing()) {
			return;
		}
		final YBWDialog dialog = new YBWDialog(this, msg, titleRes);
		dialog.setNegativeButton(R.string.sure, new DialogOnClickListener() {

			@Override
			public void onClick(String firstText, String secondText) {
				dialog.cancel();
			}
		});
		dialog.show();
	}

	public void showMsg(int msg, int title) {
		if (this.isFinishing()) {
			return;
		}
		final YBWDialog dialog = new YBWDialog(this, msg, title);
		dialog.setNegativeButton(R.string.sure, new DialogOnClickListener() {

			@Override
			public void onClick(String firstText, String secondText) {
				dialog.cancel();
			}
		});
		dialog.show();
	}

	public void showCancelableMsg(String msg, int title) {
		if (this.isFinishing()) {
			return;
		}
		final YBWDialog dialog = new YBWDialog(this, msg, title);
		dialog.setCancelable(false);
		dialog.setNegativeButton(R.string.sure, new DialogOnClickListener() {

			@Override
			public void onClick(String firstText, String secondText) {
				dialog.cancel();
				finish();
			}
		});
		dialog.show();
	}

	public void notifyNetworkInfo() {
		final int count = mParentContent.getChildCount();
		View child = mParentContent.getChildAt(count - 1);
		if (mParentContent != null
				&& (child == null || child != mNetNotifyView)) {
			mNetNotifyView = new HeaderNotifyView(this).getView();
			mParentContent.addView(mNetNotifyView, count);
		} else {
			removeNetworkInfo();
		}
	}

	public void removeNetworkInfo() {
		final int count = mParentContent.getChildCount();
		View child = mParentContent.getChildAt(count - 1);
		if (mParentContent != null && child != null && child == mNetNotifyView) {
			mParentContent.removeView(mNetNotifyView);
		}
	}

	/******** SlidingMenu开始 ************/
	/**
	 * 初始化动画效果
	 */
	private void initAnimation() {
		mTransformer = new CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				float scale = (float) (percentOpen * 0.25 + 1.0);
				canvas.scale(scale, scale, canvas.getWidth() * 3 / 4,
						canvas.getHeight() * 3 / 4);
			}

		};

		/*
		 * mTransformer = new CanvasTransformer(){
		 * 
		 * @Override public void transformCanvas(Canvas canvas, float
		 * percentOpen) { canvas.scale(percentOpen, 1, 0, 0); }
		 * 
		 * };
		 */
	}

	/******** SlidingMenu结束 ************/

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_view:
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 描述：显示进度框.
	 */
	public void showProgressDialog() {
		showProgressDialog(null);
	}

	/**
	 * 描述：显示进度框.
	 * 
	 * @param message
	 *            the message
	 */
	public void showProgressDialog(String message) {
		// 创建一个显示进度的Dialog
		if (message != null) {
			mProgressMessage = message;
		}
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(this);
			// 设置点击屏幕Dialog不消失
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.setCancelable(false);
		}
		mProgressDialog.setText(mProgressMessage);
		mProgressDialog.show();
	}

	/**
	 * 描述：移除进度框.
	 */
	public void removeProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.cancel();
		}
	}

}
