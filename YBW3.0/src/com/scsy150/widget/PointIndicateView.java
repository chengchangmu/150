package com.scsy150.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.scsy150.R;
import com.scsy150.util.ResourcesUtil;

public class PointIndicateView extends LinearLayout implements
		android.support.v4.view.ViewPager.OnPageChangeListener {

	private int mPointNum = 0;
	private int mCurrentPoint = 0;
	private boolean mIsLoop = false;

	private float mInterval = 2f;// 点与点之间的间隔
	private float mSize = 12f;// 高与宽的大小

	private ViewPager mPager = null;// 外部传进来的viewpager
	private OnPageChangeListener mListen = null;

	private final int PAGER_NUM = 300;// 为了支持循环显示，欺骗系统

	public interface OnPageChangeListener {
		public void onPageScrolled(int i, float f, int j);

		public void onPageSelected(int i);

		public void onPageScrollStateChanged(int i);
	}

	public PointIndicateView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	private void initView() {
		setOrientation(HORIZONTAL);
	}

	private void initViewWhenSet() {
		if (mPointNum == 0 || mPointNum == 1) {
			setVisibility(View.INVISIBLE);
			return;
		}
		removeAllViews();
		for (int i = 0; i < mPointNum; i++) {
			addView(initPointView(), ResourcesUtil.dip2px(mSize),
					ResourcesUtil.dip2px(mSize));
		}
		setCurrentPoint(mCurrentPoint);
		setVisibility(View.VISIBLE);
	}

	private View initPointView() {
		ImageView img = new ImageView(getContext());
		img.setScaleType(ScaleType.FIT_XY);
		img.setImageDrawable(getResources()
				.getDrawable(R.drawable.point_normal));
		img.setPadding(ResourcesUtil.dip2px(mInterval),
				ResourcesUtil.dip2px(mInterval),
				ResourcesUtil.dip2px(mInterval),
				ResourcesUtil.dip2px(mInterval));
		return img;
	}

	private void setPointPosition(int position) {
		if (getVisibility() != View.VISIBLE || position + 1 > getChildCount()) {
			return;
		}
		for (int i = 0; i < getChildCount(); i++) {
			if (position == i) {
				((ImageView) getChildAt(i)).setImageDrawable(getResources()
						.getDrawable(R.drawable.point_selected));
			} else {
				((ImageView) getChildAt(i)).setImageDrawable(getResources()
						.getDrawable(R.drawable.point_normal));
			}
		}
	}

	public void setViewPager(ViewPager pager, int totalNum, int currentNum,
			boolean needLoop, OnPageChangeListener pagerListen) {
		if (pager == null) {
			return;
		}
		mIsLoop = needLoop;
		mCurrentPoint = currentNum;
		mPager = pager;
		mPager.setOnPageChangeListener(this);
		mListen = pagerListen;
		setPointNummber(totalNum);
		if (mIsLoop) {
			mPager.setCurrentItem(mPointNum * PAGER_NUM + mCurrentPoint);
		} else {
			mPager.setCurrentItem(mCurrentPoint);
		}
	}

	private void setPointNummber(int num) {
		mPointNum = num;
		initViewWhenSet();
	}

	public int getPointNummber() {
		return mPointNum;
	}

	private void setCurrentPoint(int indicate) {
		mCurrentPoint = indicate;
		setPointPosition(mCurrentPoint);
	}

	public int getCurrentPoint() {
		return mCurrentPoint;
	}

	@Override
	public void onPageScrolled(int i, float f, int j) {
		if (mListen != null) {
			if (!mIsLoop) {
				mListen.onPageScrolled(i, f, j);
			} else {
				mListen.onPageScrolled(i % mPointNum, f, j);
			}
		}
	}

	@Override
	public void onPageSelected(int i) {
		if (!mIsLoop) {
			setCurrentPoint(i);
		} else {
			setCurrentPoint(i % mPointNum);
		}
		if (mListen != null) {
			mListen.onPageSelected(i);
		}
	}

	@Override
	public void onPageScrollStateChanged(int i) {
		if (mListen != null) {
			if (!mIsLoop) {
				mListen.onPageScrollStateChanged(i);
			} else {
				mListen.onPageScrollStateChanged(i % mPointNum);
			}
		}
	}

}
