package com.scsy150.meet.activity;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.scsy150.R;
import com.scsy150.base.BaseActivity;
import com.scsy150.base.BaseBean;
import com.scsy150.base.YBWApplication;
import com.scsy150.consts.MzApi;
import com.scsy150.consts.SystemConsts;
import com.scsy150.meet.bean.MeetDetailBean;
import com.scsy150.meet.bean.MeetInfoBean;
import com.scsy150.meet.bean.OrganizerInfoBean;
import com.scsy150.meet.page.adapter.CommonAdapter;
import com.scsy150.util.BitmapUtil;
import com.scsy150.util.LogUtil;
import com.scsy150.util.MD5Util;
import com.scsy150.util.ResourcesUtil;
import com.scsy150.util.ToastUtil;
import com.scsy150.util.view.CircleImageView;
import com.scsy150.util.view.ViewInject;
import com.scsy150.util.view.ViewUtils;
import com.scsy150.volley.net.RequestAndLoadCookies;

public class OrganizerDetailInfoActivity extends BaseActivity {

	@ViewInject(R.id.tv_organizer)
	// 主办方
	private TextView mTvOrganizer;
	@ViewInject(R.id.tv_rating)
	// 评分
	private TextView mTvRating;
	@ViewInject(R.id.rb_organizer)
	// 评分控件
	private RatingBar mRbOrganizer;
	@ViewInject(R.id.civ_head_img)
	// logo图
	private CircleImageView mCivHeadImg;
	@ViewInject(R.id.iv_organizer_background)
	// 背景图
	private ImageView mIvOrganizerBackground;
	@ViewInject(R.id.tv_attention)
	// 关注
	private TextView mTvAttention;
	@ViewInject(R.id.tv_first_meet_place)
	// 初遇点
	private TextView mTvFirstMeetPlace;
	@ViewInject(R.id.lv_organizer_activity)
	// 商家下的活动列
	private ListView mLvOrganizerActivity;

	// 数据的行数：beginPage 开始行；everPage 数据行数
	private int beginPage = 1;
	private int everPage = 2;

	@Override
	public void addViewIntoContent() {
		View view = View.inflate(this, R.layout.activity_organizer_detail_info,
				null);
		setTitle(R.string.organizer2);
		setLeftTxt(R.string.back);
		mBaseContent.addView(view);

		ViewUtils.inject(this);

		ImageView mIvOrganizerBackground = (ImageView) view
				.findViewById(R.id.iv_organizer_background);
		Drawable drawable = getResources().getDrawable(
				R.drawable.organizer_background);
		mIvOrganizerBackground.setImageDrawable(drawable);
		// blur(mBitmap, mIvOrganizerBackground);

		getDataFromServer();
	}

	public void getDataFromServer() {
		RequestAndLoadCookies stringRequest = new RequestAndLoadCookies(this,
				Method.POST, MzApi.ACTIVITY_URL,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String str) {
						LogUtil.d("TAG--------------------", str);
						Gson gson = new Gson();
						BaseBean item = gson.fromJson(str, BaseBean.class);
						if (item.getIsSuccess() == SystemConsts.RESPONSE_SUCCESS) {
							JSONObject jo;
							try {
								jo = new JSONObject(str);
								JSONObject joResult = jo
										.getJSONObject("Result");
								Type listTypeData1 = new TypeToken<ArrayList<OrganizerInfoBean>>() {
								}.getType();
								Type listTypeData2 = new TypeToken<ArrayList<MeetInfoBean>>() {
								}.getType();
								ArrayList<OrganizerInfoBean> organizerInfoBeans = gson
										.fromJson(joResult.getString("data1"),
												listTypeData1);
								final ArrayList<MeetInfoBean> meetInfoBeans = gson
										.fromJson(joResult.getString("data2"),
												listTypeData2);
								// 将数据绑定到界面
								if (organizerInfoBeans != null
										& organizerInfoBeans.size() > 0) {
									OrganizerInfoBean organizerInfoBean = organizerInfoBeans
											.get(0);
									if (organizerInfoBean != null) {
										mTvOrganizer.setText(organizerInfoBean
												.getMhname());
										mTvRating.setText(organizerInfoBean
												.getScore() + "");
										if (organizerInfoBean.getHeadImg() != null
												& organizerInfoBean
														.getHeadImg() != "")
											ImageLoader
													.getInstance()
													.displayImage(
															MzApi.IMG_URL
																	+ organizerInfoBean
																			.getHeadImg(),
															mCivHeadImg);
										if (organizerInfoBean.getBkgdImg() != null
												& organizerInfoBean
														.getBkgdImg() != "")
											ImageLoader
													.getInstance()
													.displayImage(
															MzApi.IMG_URL
																	+ organizerInfoBean
																			.getBkgdImg(),
															mIvOrganizerBackground);
									}
								}
								/*
								 * @ViewInject(R.id.rb_organizer) // 评分控件
								 * private RatingBar mRbOrganizer;
								 * 
								 * @ViewInject(R.id.tv_attention) // 关注 private
								 * TextView mTvAttention;
								 * 
								 * @ViewInject(R.id.tv_first_meet_place) // 初遇点
								 * private TextView mTvFirstMeetPlace;
								 */
								if (meetInfoBeans != null
										&& meetInfoBeans.size() > 0) {
									mLvOrganizerActivity
											.setAdapter(new CommonAdapter(
													OrganizerDetailInfoActivity.this,
													meetInfoBeans));
									mLvOrganizerActivity
											.setOnItemClickListener(new OnItemClickListener() {

												@Override
												public void onItemClick(
														AdapterView<?> parent,
														View view,
														int position, long id) {
													MeetInfoBean meetInfoBean = meetInfoBeans
															.get(position);
													Intent intent = new Intent(
															OrganizerDetailInfoActivity.this,
															MeetDetailActivity.class);
													intent.putExtra(
															"activity_id",
															meetInfoBean
																	.getAcId());
													OrganizerDetailInfoActivity.this
															.startActivity(intent);
													OrganizerDetailInfoActivity.this
															.finish();
												}
											});
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}

						} else {

							((BaseActivity) OrganizerDetailInfoActivity.this)
									.removeProgressDialog();
							ToastUtil.showCenterToast(
									OrganizerDetailInfoActivity.this,
									item.getMessage() + "", Toast.LENGTH_SHORT);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						((BaseActivity) OrganizerDetailInfoActivity.this)
								.removeProgressDialog();
						ToastUtil.showCenterToast(
								OrganizerDetailInfoActivity.this,
								ResourcesUtil
										.getString(R.string.base_load_failed_des),
								Toast.LENGTH_SHORT);
					}

				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();

				map.put("M", "Get_Merchant_Activity_List");
				map.put("MERCHANTID", SystemConsts.ACTIVITY_TYPE_HOT + "");
				map.put("LONGITUDE", 1 + "");
				map.put("LATITUDE", 1 + "");
				map.put("BEGINPAGE", beginPage + "");
				map.put("EVERPAGE", everPage + "");

				String sign = "";
				try {
					sign = MD5Util.getSignature(map, SystemConsts.SIGN_KEY);
					LogUtil.d("sign------------", sign);
					map.put("sign", sign);
				} catch (IOException e) {
					e.printStackTrace();
				}

				return map;

			}

		};
		YBWApplication.mQueue.add(stringRequest);
	}

	@SuppressLint("NewApi")
	private void blur(Bitmap bkg, View view) {
		long startMs = System.currentTimeMillis();
		float scaleFactor = 8;// 图片缩放比例；
		float radius = 20;// 模糊程度

		Bitmap overlay = Bitmap.createBitmap(
				(int) (view.getMeasuredWidth() / scaleFactor),
				(int) (view.getMeasuredHeight() / scaleFactor),
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(overlay);
		canvas.translate(-view.getLeft() / scaleFactor, -view.getTop()
				/ scaleFactor);
		canvas.scale(1 / scaleFactor, 1 / scaleFactor);
		Paint paint = new Paint();
		paint.setFlags(Paint.FILTER_BITMAP_FLAG);
		canvas.drawBitmap(bkg, 0, 0, paint);

		overlay = BitmapUtil.doBlur(overlay, (int) radius, true);
		view.setBackground(new BitmapDrawable(getResources(), overlay));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
			super.onClick(v);
			break;
		}
	}

}
