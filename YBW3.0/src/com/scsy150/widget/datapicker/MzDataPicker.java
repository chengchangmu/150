package com.scsy150.widget.datapicker;

import java.util.Calendar;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.scsy150.R;
import com.scsy150.util.LogUtil;

/*
 * Copyright (C) 2015 四川硅谷科技重庆分公司
 * 版权所有
 *
 * 功能描述：获取日期，时间以及自定义数据的选择dialog
 * 作者：硅谷科技
 * 创建时间：2015-08-17
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */

public class MzDataPicker {
	public static final String TAG = "MzDataPicker";
	// 可选择年月日
	private static final int TYPE_DAY_YEAR_MONTH_DAY = 0;
	// 可选择年月
	private static final int TYPE_DAY_YEAR_MONTH = 1;
	// 可选择月日
	private static final int TYPE_DAY_MONTH_DAY = 2;
	// 可选择上下午的时间
	private static final int TYPE_TIME_12HOUR = 3;
	// 可选择24小时制的时间
	private static final int TYPE_TIME_24HOUR = 4;
	// 自定义数据选择
	private static final int TYPE_CUSTOM_DATA = 5;

	private Dialog mDialog;
	private Button mBtnOK;
	private Button mBtnCancle;
	private Context mContext;
	private DateNumericAdapter mMonthAdapter;
	private DateNumericAdapter mDayAdapter;
	private DateNumericAdapter mYearAdapter;
	// 年月日滚轮控件
	private WheelView mYear;
	private WheelView mMonth;
	private WheelView mDay;
	// 标题控件
	private TextView mTitleTxt;

	// 年月日显示时的初始化值
	private int mCurYear;
	private int mCurMonth;
	private int mCurDay;
	private OnDayPickerListener mDayResultCallback;

	private StringArrayAdapter mAmOrPmAdapter;
	private DateNumericAdapter mHourAdapter;
	private DateNumericAdapter mMinuteAdapter;
	// 时间滚轮控件
	private WheelView mAmOrPm;
	private WheelView mHour;
	private WheelView mMinute;

	// 时间显示时的初始化值
	private boolean mIs24HourMode = true;
	private int mCurHour;
	private int mCurMinute;
	private OnTimePickerListener mTimeResultCallback;

	private int mPickerType = TYPE_DAY_YEAR_MONTH_DAY;

	private OnCustomDataPickerListener mCustomResultCallback;
	private StringArrayAdapter mCustomDataLeftAdapter;
	private WheelView mCustomDataViewLeft;
	private String[] mCustomDataLeft;
	private int mCurIndexLeft = 0;

	private StringArrayAdapter mCustomDataRightAdapter;
	private WheelView mCustomDataViewRight;
	private String[] mCustomDataRight;
	private int mCurIndexRight = 0;

	/**
	 * 时间选择dialog
	 * 
	 * @param context
	 * @param is24HourMode
	 * @param hour
	 *            输入始终是24小时时间形式
	 * @param minute
	 * @param timeObserver
	 *            用户选择结果的回调接口
	 */
	public static void showTimePicker(Context context, boolean is24HourMode,
			int hour, int minute, OnTimePickerListener timeObserver) {
		final MzDataPicker picker = new MzDataPicker(context,
				is24HourMode ? TYPE_TIME_24HOUR : TYPE_TIME_12HOUR,
				timeObserver);
		picker.showTimePicker(hour, minute);
	}

	/**
	 * 年月选择dialog
	 * 
	 * @param context
	 * @param year
	 * @param monthOfYear
	 * @param dayObserver
	 *            用户选择结果的回调接口
	 */
	public static void showMonthPicker(Context context, int year,
			int monthOfYear, OnDayPickerListener dayObserver) {
		final MzDataPicker picker = new MzDataPicker(context,
				TYPE_DAY_YEAR_MONTH, dayObserver);
		picker.showMonthPicker(year, monthOfYear);
	}

	/**
	 * 月日选择dialog
	 * 
	 * @param context
	 * @param monthOfYear
	 * @param dayOfMonth
	 * @param dayObserver
	 *            用户选择结果的回调接口
	 */
	public static void showDayPicker(Context context, int monthOfYear,
			int dayOfMonth, OnDayPickerListener dayObserver) {
		final MzDataPicker picker = new MzDataPicker(context,
				TYPE_DAY_MONTH_DAY, dayObserver);
		picker.showDayPicker(monthOfYear, dayOfMonth);
	}

	/**
	 * 年月日选择dialog
	 * 
	 * @param context
	 * @param year
	 * @param monthOfYear
	 * @param dayOfMonth
	 * @param dayObserver
	 *            用户选择结果的回调接口
	 */
	public static void showDayPicker(Context context, int year,
			int monthOfYear, int dayOfMonth, OnDayPickerListener dayObserver) {
		final MzDataPicker picker = new MzDataPicker(context,
				TYPE_DAY_YEAR_MONTH_DAY, dayObserver);
		picker.showDayPicker(year, monthOfYear, dayOfMonth);
	}

	/**
	 * 自定义数据选择dialog
	 * 
	 * @param context
	 * @param customData
	 * @param curIndex
	 * @param title
	 * @param dataObserver
	 *            用户选择结果的回调接口
	 */
	public static void showCustomDataPicker(Context context,
			String[] customDataLeft, int curIndexLeft,
			String[] customDataRight, int curIndexRight, String title,
			OnCustomDataPickerListener dataObserver) {
		final MzDataPicker picker = new MzDataPicker(context, customDataLeft,
				curIndexLeft, customDataRight, curIndexRight, dataObserver);
		picker.showCustomDataPicker(title);
	}

	private MzDataPicker(Context context, int pickerType,
			OnDayPickerListener dayObserver) {
		this(context, pickerType, dayObserver, null);
	}

	private MzDataPicker(Context context, int pickerType,
			OnTimePickerListener timeObserver) {
		this(context, pickerType, null, timeObserver);
	}

	private MzDataPicker(Context context, int pickerType,
			OnDayPickerListener dayObserver, OnTimePickerListener timeObserver) {
		mContext = context;
		mPickerType = pickerType;
		mDayResultCallback = dayObserver;
		mTimeResultCallback = timeObserver;
		mIs24HourMode = (mPickerType == TYPE_TIME_24HOUR);
	}

	private MzDataPicker(Context context, String[] customDataLeft,
			int curIndexLeft, String[] customDataRight, int curIndexRight,
			OnCustomDataPickerListener dataObserver) {
		mContext = context;
		mPickerType = TYPE_CUSTOM_DATA;
		mCustomDataLeft = customDataLeft;
		mCurIndexLeft = curIndexLeft;
		mCustomDataRight = customDataRight;
		mCurIndexRight = curIndexRight;
		mCustomResultCallback = dataObserver;
	}

	/**
	 * 时间选择，始终传入24小时制的时间值
	 * 
	 * @param hour
	 * @param minute
	 */
	private void showTimePicker(int hour, int minute) {
		if (mPickerType != TYPE_TIME_12HOUR && mPickerType != TYPE_TIME_24HOUR) {
			LogUtil.d(TAG, "show time picker with wrong type!");
			return;
		}
		if (mDialog == null) {
			initDialog();
		}

		// 靠底部
		Window window = mDialog.getWindow();
		window.setBackgroundDrawable(new BitmapDrawable());
		window.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		window.setGravity(Gravity.BOTTOM);

		// mTitleTxt = (TextView) window.findViewById(R.id.zditdialog_title);
		// mTitleTxt.setText("选择时间");

		mAmOrPm = (WheelView) window.findViewById(R.id.daypicker_year);
		mHour = (WheelView) window.findViewById(R.id.daypicker_month);
		mMinute = (WheelView) window.findViewById(R.id.daypicker_day);

		if (mPickerType == TYPE_TIME_24HOUR) {
			// 隐藏上午下午滚轮
			mAmOrPm.setVisibility(View.GONE);
		}

		OnWheelChangedListener listener = new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
			}
		};

		mCurHour = hour;
		mCurMinute = minute;

		// 数据边界判定
		if (mCurHour < 1) {
			mCurHour = 1;
		} else if (mCurHour > 24) {
			mCurHour = 24;
		}

		if (mCurMinute < 1) {
			mCurMinute = 1;
		} else if (mCurMinute > 59) {
			mCurMinute = 59;
		}

		if (mPickerType == TYPE_TIME_12HOUR) {
			// am or pm
			mAmOrPmAdapter = new StringArrayAdapter(mContext, mContext
					.getResources().getStringArray(R.array.am_or_pm), 0);
			mAmOrPm.setViewAdapter(mAmOrPmAdapter);
			mAmOrPm.setCurrentItem(hour > 12 ? 1 : 0);
			mAmOrPm.addChangingListener(listener);
		}

		// hour
		mHourAdapter = new DateNumericAdapter(mContext, 1, mIs24HourMode ? 24
				: 12, mCurHour);
		mHour.setViewAdapter(mHourAdapter);
		mHour.setCurrentItem(mCurHour - 1);
		mHour.addChangingListener(listener);

		// minute
		mMinuteAdapter = new DateNumericAdapter(mContext, 0, 59, mCurMinute);
		mMinute.setViewAdapter(mMinuteAdapter);
		mMinute.setCurrentItem(mCurMinute);
		mMinute.addChangingListener(listener);

		mBtnOK = (Button) window.findViewById(R.id.daypicker_ok);
		mBtnCancle = (Button) window.findViewById(R.id.daypicker_cancle);

		mBtnOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mTimeResultCallback != null) {
					mTimeResultCallback.onResult(
							mIs24HourMode ? (mHour.getCurrentItem() + 1)
									: (mAmOrPm.getCurrentItem() * 12
											+ mHour.getCurrentItem() + 1),
							(mMinute.getCurrentItem()));
					LogUtil.d(TAG, "time picker result callback");
				}
				mDialog.dismiss();
			}
		});
		mBtnCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});

		mDialog.show();
	}

	/**
	 * 构造dialog
	 */
	private void initDialog() {
		mDialog = new Dialog(mContext, R.style.common_dialog);
		mDialog.setCanceledOnTouchOutside(true);
		mDialog.setContentView(R.layout.data_picker);
		Window dialogWindow = mDialog.getWindow();
		dialogWindow.setWindowAnimations(R.style.wheelDialogWindowAnim);
		WindowManager.LayoutParams params = dialogWindow.getAttributes();
		params.dimAmount = 0.5f;
		dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		dialogWindow.setAttributes(params);
	}

	/**
	 * 选择年月
	 * 
	 * @param year
	 * @param monthOfYear
	 */
	private void showMonthPicker(int year, int monthOfYear) {
		showDayPicker(year, monthOfYear, 1);
	}

	/**
	 * 选择当年的月日
	 * 
	 * @param monthOfYear
	 * @param dayOfMonth
	 */
	private void showDayPicker(int monthOfYear, int dayOfMonth) {
		showDayPicker(Calendar.getInstance().get(Calendar.YEAR), monthOfYear,
				dayOfMonth);
	}

	/**
	 * 选择日期
	 * 
	 * @param year
	 * @param monthOfYear
	 * @param dayOfMonth
	 */
	private void showDayPicker(int year, int monthOfYear, int dayOfMonth) {
		if (mPickerType > TYPE_DAY_MONTH_DAY) {
			LogUtil.d(TAG, "show day picker with wrong type!");
			return;
		}
		if (mDialog == null) {
			initDialog();
		}

		// 靠底部
		Window window = mDialog.getWindow();
		window.setBackgroundDrawable(new BitmapDrawable());
		window.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		window.setGravity(Gravity.BOTTOM);

		mTitleTxt = (TextView) window.findViewById(R.id.zditdialog_title);
		mYear = (WheelView) window.findViewById(R.id.daypicker_year);
		mMonth = (WheelView) window.findViewById(R.id.daypicker_month);
		mDay = (WheelView) window.findViewById(R.id.daypicker_day);

		switch (mPickerType) {
		case TYPE_DAY_YEAR_MONTH:
			// 隐藏日滚轮
			mDay.setVisibility(View.GONE);
			break;
		case TYPE_DAY_MONTH_DAY:
			// 隐藏年滚轮
			mYear.setVisibility(View.GONE);
			break;
		default:
			break;
		}

		OnWheelChangedListener listener = new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateDays(mYear, mMonth, mDay);
			}
		};

		mCurYear = year;
		mCurMonth = monthOfYear;
		mCurDay = dayOfMonth - 1;

		// 数据边界判定
		if (mCurYear < 1) {
			mCurYear = 1;
		} else if (mCurYear > 10000) {
			mCurYear = 10000;
		}

		if (mCurMonth < 1) {
			mCurMonth = 1;
		} else if (mCurMonth > 12) {
			mCurMonth = 12;
		}

		if (mCurDay < 1) {
			mCurDay = 1;
		} else if (mCurDay > 31) {
			mCurDay = 31;
		}

		// month
		mMonthAdapter = new DateNumericAdapter(mContext, 1, 12, mCurMonth, "月");
		mMonth.setViewAdapter(mMonthAdapter);
		mMonth.setCurrentItem(mCurMonth);
		mMonth.addChangingListener(listener);
		mMonth.setCyclic(true);
		// year
		mYearAdapter = new DateNumericAdapter(mContext, 1, 10000, mCurYear, "年");
		mYear.setViewAdapter(mYearAdapter);
		mYear.setCurrentItem(mCurYear - 1);
		mYear.addChangingListener(listener);

		// day
		updateDays(mYear, mMonth, mDay);
		mDay.setCurrentItem(mCurDay);
		updateDays(mYear, mMonth, mDay);
		mDay.addChangingListener(listener);

		mBtnOK = (Button) window.findViewById(R.id.daypicker_ok);
		mBtnCancle = (Button) window.findViewById(R.id.daypicker_cancle);

		mBtnOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mDayResultCallback != null) {
					mDayResultCallback.onResult(mYear.getCurrentItem() + 1,
							(mMonth.getCurrentItem() + 1),
							(mDay.getCurrentItem() + 1));
					LogUtil.d(TAG, "day picker result callback");
				}
				mDialog.dismiss();
			}
		});
		mBtnCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});

		mDialog.show();
	}

	/**
	 * 根据当前的年和月来更新当前月份的最大天数
	 * 
	 * @param year
	 * @param month
	 * @param day
	 */
	private void updateDays(WheelView year, WheelView month, WheelView day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR,
				calendar.get(Calendar.YEAR) + year.getCurrentItem());
		calendar.set(Calendar.MONTH, month.getCurrentItem());

		int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		mDayAdapter = new DateNumericAdapter(mContext, 1, maxDays,
				calendar.get(Calendar.DAY_OF_MONTH) - 1, mContext
						.getResources().getString(R.string.day));
		day.setViewAdapter(mDayAdapter);
		int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
		day.setCurrentItem(curDay - 1, true);
	}

	private void showCustomDataPicker(String title) {
		if (mPickerType != TYPE_CUSTOM_DATA) {
			LogUtil.d(TAG, "show custom data picker with wrong type!");
			return;
		}
		if (mDialog == null) {
			initDialog();
		}

		// 靠底部
		Window window = mDialog.getWindow();
		window.setBackgroundDrawable(new BitmapDrawable());
		window.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		window.setGravity(Gravity.BOTTOM);

		mTitleTxt = (TextView) window.findViewById(R.id.zditdialog_title);
		mTitleTxt.setText(title);
		mCustomDataViewLeft = (WheelView) window
				.findViewById(R.id.daypicker_year);
		if (mCustomDataRight != null && mCustomDataRight.length > 0) {
			mCustomDataViewRight = (WheelView) window
					.findViewById(R.id.daypicker_month);
		} else {
			window.findViewById(R.id.daypicker_month).setVisibility(View.GONE);
		}
		window.findViewById(R.id.daypicker_day).setVisibility(View.GONE);

		OnWheelChangedListener listener = new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
			}
		};

		if (mCustomDataLeft == null || mCustomDataLeft.length < 1) {
			mCustomDataLeft = new String[] { "no data" };
			mCurIndexLeft = 0;
		}

		// custom data left
		mCustomDataLeftAdapter = new StringArrayAdapter(mContext,
				mCustomDataLeft, mCurIndexLeft);
		mCustomDataViewLeft.setViewAdapter(mCustomDataLeftAdapter);
		mCustomDataViewLeft.setCurrentItem(mCurIndexLeft);
		mCustomDataViewLeft.addChangingListener(listener);

		// custom data right
		if (mCustomDataRight != null && mCustomDataRight.length > 0) {
			mCustomDataRightAdapter = new StringArrayAdapter(mContext,
					mCustomDataRight, mCurIndexRight);
			mCustomDataViewRight.setViewAdapter(mCustomDataRightAdapter);
			mCustomDataViewRight.setCurrentItem(mCurIndexRight);
			mCustomDataViewRight.addChangingListener(listener);
		} else {
			mCustomDataViewLeft.setShadowGravity(WheelView.SHADOW_MIDDLE);
		}

		mBtnOK = (Button) window.findViewById(R.id.daypicker_ok);
		mBtnCancle = (Button) window.findViewById(R.id.daypicker_cancle);

		mBtnOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mCustomResultCallback != null) {
					final int leftIndex = mCustomDataViewLeft.getCurrentItem();
					final int rightIndex = mCustomDataViewRight != null ? mCustomDataViewRight
							.getCurrentItem() : -1;
					String rightValue = rightIndex > 0 ? mCustomDataRight[rightIndex]
							: null;
					mCustomResultCallback.onResult(leftIndex,
							mCustomDataLeft[leftIndex], rightIndex, rightValue);
					LogUtil.d(TAG, "custom data picker result callback");
				}
				mDialog.dismiss();
			}
		});
		mBtnCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});

		mDialog.show();
	}

	public interface OnDayPickerListener {
		public void onResult(int year, int monthOfYear, int dayOfMonth);
	}

	public interface OnTimePickerListener {
		public void onResult(int hour, int minute);
	}

	public interface OnCustomDataPickerListener {
		public void onResult(int leftIndex, String leftValue, int rightIndex,
				String rightValue);
	}

	/**
	 * 
	 * 数字内容的滚轮适配器
	 *
	 */
	private class DateNumericAdapter extends NumericWheelAdapter {
		private Context mContext;
		private String mSuffix;

		public DateNumericAdapter(Context context, int minValue, int maxValue,
				int current) {
			super(context, minValue, maxValue);
			mContext = context;
			setTextSize(mContext.getResources().getDimensionPixelSize(
					R.dimen.font_16px));
		}

		public DateNumericAdapter(Context context, int minValue, int maxValue,
				int current, String suffix) {
			super(context, minValue, maxValue);
			mSuffix = suffix;
			mContext = context;
			setTextSize(mContext.getResources().getDimensionPixelSize(
					R.dimen.font_16px));
		}

		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			view.setTypeface(Typeface.SANS_SERIF);
			view.setTextColor(mContext.getResources().getColor(
					R.color.common_bold_des_text));
		}

		@Override
		public CharSequence getItemText(int index) {
			if (TextUtils.isEmpty(mSuffix)) {
				return super.getItemText(index);
			} else {
				return super.getItemText(index) + mSuffix;
			}
		}

	}

	/**
	 * 
	 * 自定义文字内容的滚轮适配器
	 *
	 */
	private class StringArrayAdapter extends ArrayWheelAdapter<String> {

		private Context mContext;

		public StringArrayAdapter(Context context, String[] items, int current) {
			super(context, items);
			mContext = context;
			setTextSize(mContext.getResources().getDimensionPixelSize(
					R.dimen.font_16px));
		}

		public StringArrayAdapter(Context context, String[] items) {
			super(context, items);
			mContext = context;
			setTextSize(mContext.getResources().getDimensionPixelSize(
					R.dimen.font_16px));
		}

		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			view.setTypeface(Typeface.SANS_SERIF);
			view.setTextColor(mContext.getResources().getColor(
					R.color.common_bold_des_text));
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			return super.getItem(index, cachedView, parent);
		}
	}

}
