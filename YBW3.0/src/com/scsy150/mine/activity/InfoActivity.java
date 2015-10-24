package com.scsy150.mine.activity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scsy150.R;
import com.scsy150.base.BaseActivity;
import com.scsy150.base.BaseBean;
import com.scsy150.common.ContentActivity;
import com.scsy150.consts.MzApi;
import com.scsy150.consts.SystemConsts;
import com.scsy150.dialog.Pop;
import com.scsy150.mine.adapter.ImageGridAdapter;
import com.scsy150.mine.bean.InfoBean;
import com.scsy150.mine.bean.PicBean;
import com.scsy150.util.LogUtil;
import com.scsy150.util.ResourcesUtil;
import com.scsy150.util.SoundMeter;
import com.scsy150.util.TimeUtil;
import com.scsy150.util.ToastUtil;
import com.scsy150.util.view.OnClick;
import com.scsy150.util.view.ViewInject;
import com.scsy150.util.view.ViewUtils;
import com.scsy150.volley.net.RequestAndLoadCookies;
import com.scsy150.widget.datapicker.MzDataPicker;
import com.scsy150.widget.datapicker.MzDataPicker.OnDayPickerListener;

/*
 * Copyright (C) 2015 四川硅谷科技重庆分公司
 * 版权所有
 *
 * 功能描述：我的之个人资料界面
 * 作者：硅谷科技
 * 创建时间：2015-08-24
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */

public class InfoActivity extends BaseActivity implements OnClickListener {

	@ViewInject(R.id.info_nickname)
	private LinearLayout mInfoNickName;

	@ViewInject(R.id.info_birthday)
	private LinearLayout mInfoBirthday;

	@ViewInject(R.id.nickname)
	private TextView mNickName;

	@ViewInject(R.id.voice)
	private ImageView mVoice;

	@ViewInject(R.id.my_sign)
	private TextView mMySign;

	@ViewInject(R.id.birthday)
	private TextView mBirthday;

	@ViewInject(R.id.grid)
	private GridView mGrid;

	private InfoBean mInfoBean;

	private ImageGridAdapter mImageGridAdapter;

	@Override
	public void addViewIntoContent() {
		View view = View.inflate(this, R.layout.activity_mine_info, null);
		mBaseContent.addView(view);
		ViewUtils.inject(this);
		if (mQueue == null) {
			mQueue = Volley.newRequestQueue(mCurrentActivity);
		}
		setLeftVisibility(View.VISIBLE);
		setTitle(ResourcesUtil.getString(R.string.user_option3));
		init();
	}

	private void init() {
		getPic();
		getNetData();
	}

	private void initData(InfoBean bean) {

		mNickName.setText(bean.getNickName());
		mMySign.setText(bean.getTalk());
		mBirthday.setText(TimeUtil.formatDate(bean.getBrithday(),
				TimeUtil.YYYY_MM_DD));

		if (TextUtils.isEmpty(bean.getHeadImg())) {
			mVoice.setBackgroundResource(R.drawable.info_voice_n);
		} else {
			mVoice.setBackgroundResource(R.drawable.info_voice_h);
		}
	}

	private void showBirthdayPicker() {
		// 使用类iphone风格的时间选择控件
		final Calendar startCalendar = Calendar.getInstance();
		MzDataPicker.showDayPicker(this, startCalendar.get(Calendar.YEAR),
				startCalendar.get(Calendar.MONTH),
				startCalendar.get(Calendar.DAY_OF_MONTH),
				new OnDayPickerListener() {
					@Override
					public void onResult(int year, int monthOfYear,
							int dayOfMonth) {
						String choce_time = year + "-";
						if (monthOfYear < 10) {
							choce_time += "0" + monthOfYear;
						} else {
							choce_time += monthOfYear;
						}
						choce_time += "-";
						if (dayOfMonth < 10) {
							choce_time += "0" + dayOfMonth;
						} else {
							choce_time += dayOfMonth;
						}
						mBirthday.setText(choce_time);
					}

				});
	}

	@Override
	@OnClick({ R.id.left_image, R.id.voice, R.id.my_sign, R.id.info_nickname,
			R.id.info_birthday })
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.info_birthday:
			showBirthdayPicker();
			break;

		case R.id.info_nickname:

			intent = new Intent(this, ContentActivity.class);
			intent.putExtra(ContentActivity.CONTENT_TITLE,
					ResourcesUtil.getString(R.string.info_nickname));
			intent.putExtra(ContentActivity.CONTENT_INPUT_HINT,
					ResourcesUtil.getString(R.string.input_nickname_hint));
			intent.putExtra(ContentActivity.CONTENT_INPUT_NUM, 20);
			intent.putExtra(ContentActivity.CONTENT_TYPE,
					ContentActivity.CONTENT_TYPE_NICKNAME);
			intent.putExtra(ContentActivity.CONTENT_INPUT, mNickName.getText()
					.toString());
			startActivityForResult(intent,
					ContentActivity.CONTENT_TYPE_NICKNAME);
			break;
		case R.id.my_sign:
			intent = new Intent(this, ContentActivity.class);
			intent.putExtra(ContentActivity.CONTENT_TYPE,
					ContentActivity.CONTENT_TYPE_SIGN);
			intent.putExtra(ContentActivity.CONTENT_TITLE,
					ResourcesUtil.getString(R.string.info_sign));
			intent.putExtra(ContentActivity.CONTENT_INPUT_HINT,
					ResourcesUtil.getString(R.string.input_sign_hint));
			intent.putExtra(ContentActivity.CONTENT_INPUT_NUM, 50);
			intent.putExtra(ContentActivity.CONTENT_INPUT, mMySign.getText()
					.toString());
			startActivityForResult(intent, ContentActivity.CONTENT_TYPE_SIGN);
			break;
		case R.id.left_image:
			finish();
			break;
		case R.id.voice:
			intent = new Intent(this, VoiceActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			if (requestCode == ContentActivity.CONTENT_TYPE_NICKNAME) {
				mNickName.setText(data
						.getStringExtra(ContentActivity.CONTENT_INPUT));
			} else if (requestCode == ContentActivity.CONTENT_TYPE_SIGN) {
				mMySign.setText(data
						.getStringExtra(ContentActivity.CONTENT_INPUT));
			}
		}
	}

	public void getNetData() {
		RequestAndLoadCookies stringRequest = new RequestAndLoadCookies(
				InfoActivity.this, Method.POST, MzApi.LOGIN_REG,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String str) {
						LogUtil.d(TAG, str);
						Gson gson = new Gson();
						BaseBean item = gson.fromJson(str, BaseBean.class);
						if (item.getIsSuccess() == SystemConsts.RESPONSE_SUCCESS) {
							JSONObject jo;
							try {
								jo = new JSONObject(str);
								Type listType = new TypeToken<LinkedList<InfoBean>>() {
								}.getType();
								LinkedList<InfoBean> list = gson.fromJson(
										jo.getString("Result"), listType);
								mInfoBean = list.get(0);
								initData(mInfoBean);

							} catch (JSONException e) {
								e.printStackTrace();
							}

						} else {

							removeProgressDialog();

							ToastUtil.showCenterToast(InfoActivity.this,
									item.getMessage() + "", Toast.LENGTH_SHORT);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						removeProgressDialog();
						ToastUtil
								.showCenterToast(
										InfoActivity.this,
										ResourcesUtil
												.getString(R.string.base_load_failed_des),
										Toast.LENGTH_SHORT);
					}

				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();

				map.put("M", "About_Me");

				return map;

			}

		};
		mQueue.add(stringRequest);
	}

	public void getPic() {
		RequestAndLoadCookies stringRequest = new RequestAndLoadCookies(
				InfoActivity.this, Method.POST, MzApi.LOGIN_REG,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String str) {
						LogUtil.d(TAG, str);
						Gson gson = new Gson();
						BaseBean item = gson.fromJson(str, BaseBean.class);
						if (item.getIsSuccess() == SystemConsts.RESPONSE_SUCCESS) {
							JSONObject jo;
							try {
								jo = new JSONObject(str);
								Type listType = new TypeToken<LinkedList<PicBean>>() {
								}.getType();
								LinkedList<PicBean> list = gson.fromJson(
										jo.getString("Result"), listType);

								mImageGridAdapter = new ImageGridAdapter(
										InfoActivity.this, list);
								mGrid.setAdapter(mImageGridAdapter);

							} catch (JSONException e) {
								e.printStackTrace();
							}

						} else {

							removeProgressDialog();

							ToastUtil.showCenterToast(InfoActivity.this,
									item.getMessage() + "", Toast.LENGTH_SHORT);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						removeProgressDialog();
						ToastUtil
								.showCenterToast(
										InfoActivity.this,
										ResourcesUtil
												.getString(R.string.base_load_failed_des),
										Toast.LENGTH_SHORT);
					}

				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();

				map.put("M", "GetMyPhoto");

				return map;

			}

		};
		mQueue.add(stringRequest);

	}

}
