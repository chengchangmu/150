package com.scsy150.dialog;

import java.util.Calendar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.scsy150.R;
import com.scsy150.util.TimeUtil;

public class TimeDialog extends Dialog {
	private Button mBtnLeft;
	private Button mBtnRight;
	private DatePicker mDatePicker;
	private long mTime;
	private onFinishListener mListener;
	
	public void setFinishListener(onFinishListener l){
		mListener = l;
	}
	
	public interface onFinishListener{
		public void onFinish(String time);
	}
	/**
	 * 
	 * @param context
	 * @param time
	 */
	public TimeDialog(Context context, String time) {
		super(context, R.style.common_dialog);
		if(!TextUtils.isEmpty(time)){
			mTime = TimeUtil.strToMillis(time);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_time_picker);
		setCanceledOnTouchOutside(false);
		mBtnLeft = (Button) findViewById(R.id.left_Button);
		mBtnRight = (Button) findViewById(R.id.right_Button);
		mDatePicker = (DatePicker) findViewById(R.id.date_picker);
		init();
	}
	
	private void init(){
		final Calendar calendar = Calendar.getInstance();
		if(mTime > 0){
			calendar.setTimeInMillis(mTime);
		}
		mDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH),
				new DatePicker.OnDateChangedListener() {
					@Override
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						calendar.set(year, monthOfYear, dayOfMonth);
					}
				});
		mBtnLeft.setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		mBtnRight.setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mDatePicker.clearFocus();
				String choce_time = mDatePicker.getYear() + "-"
						+ (mDatePicker.getMonth() + 1) + "-"
						+ mDatePicker.getDayOfMonth();
				if(mListener != null){
					mListener.onFinish(choce_time);
				}
				dismiss();
			}
		});
	}
}
