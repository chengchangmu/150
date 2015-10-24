package com.scsy150.meet.activity;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.easemob.util.DensityUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.scsy150.R;
import com.scsy150.base.BaseActivity;
import com.scsy150.base.BaseBean;
import com.scsy150.base.YBWApplication;
import com.scsy150.consts.MzApi;
import com.scsy150.consts.SystemConsts;
import com.scsy150.date.bean.ListPersonBean;
import com.scsy150.meet.bean.MeetDetailBean;
import com.scsy150.meet.bean.MeetEnrolledMemberBean;
import com.scsy150.meet.bean.MeetPictureBean;
import com.scsy150.meet.page.adapter.MeetEnrolledBeanAdapter;
import com.scsy150.meet.page.adapter.MeetPictureBeanAdapter;
import com.scsy150.util.LogUtil;
import com.scsy150.util.MD5Util;
import com.scsy150.util.ResourcesUtil;
import com.scsy150.util.ScreenUtil;
import com.scsy150.util.ToastUtil;
import com.scsy150.util.view.HorizontalListView;
import com.scsy150.util.view.OnClick;
import com.scsy150.util.view.ViewInject;
import com.scsy150.util.view.ViewUtils;
import com.scsy150.volley.net.RequestAndLoadCookies;

public class MeetDetailActivity extends BaseActivity implements OnClickListener {

	public static final String MEETDETAILACTIVITY_ID = "activity_id";

	@ViewInject(R.id.tv_meet_detial_background)
	private ImageView mTvMeetDetialBackground; // 联谊详情背景图片
	@ViewInject(R.id.tv_handsome_meet)
	private TextView mTvHandsome; // 帅气
	@ViewInject(R.id.tv_belle_meet)
	private TextView mTvBelle; // 低价
	@ViewInject(R.id.tv_weekend_meet)
	private TextView mTvWeekend; // 周末
	@ViewInject(R.id.tv_slow_meet)
	private TextView mTvSlow; // 手慢无
	@ViewInject(R.id.tv_photo_meet)
	private TextView mTvPhoto; // 多图
	@ViewInject(R.id.tv_activity_name)
	private TextView mTvActivityName; // 活动

	@ViewInject(R.id.tv_date_meet)
	private TextView mTvDate; // 活动日期
	@ViewInject(R.id.tv_place_meet)
	private TextView mTvPlace; // 活动地点
	@ViewInject(R.id.iv_map)
	private ImageView mIvMap; // 活动地图

	@ViewInject(R.id.tv_female_number)
	private TextView mTvFemaleNumber; // 参加活动的女生
	@ViewInject(R.id.tv_male_number)
	private TextView mTvMaleNumber; // 参加活动的男生
	@ViewInject(R.id.hlv_enroll_member)
	private HorizontalListView mHlvEnrollMember; // 报名成员
	@ViewInject(R.id.iv_show_more_photos)
	private ImageView mIvShowMorePhotos; // 查看更多成员
	@ViewInject(R.id.tv_organizer)
	private TextView mTvOrganizer; // 主办方
	@ViewInject(R.id.tv_female_cost)
	private TextView mTvFemaleCost; // 女生活动费用
	@ViewInject(R.id.tv_male_cost)
	private TextView mTvMaleCost; // 男生活动费用
	@ViewInject(R.id.hlv_meet_photos)
	private HorizontalListView mHlvMeetPhotos; // 活动相册
	@ViewInject(R.id.tv_meet_description)
	private TextView mTvMeetDescription; // 活动详情
	// @ViewInject(R.id.tv_share_meet)
	private TextView mTvShare; // 分享
	// @ViewInject(R.id.tv_enroll_meet)
	private TextView mTvEnroll; // 提交报名
	private ExpandableListView vEnroll;

	@ViewInject(R.id.sv_meet_detail)
	private ScrollView mSvMeetDetail;

	private int meetDescriptionMinHeight;
	private int meetDescriptionMaxHeight;
	private boolean isMeetDescriptionExtend = false;

	private ArrayList<MeetEnrolledMemberBean> mMeetEnrolledMemberBeans;
	private ArrayList<MeetPictureBean> mMeetPictureBeans;

	// 填充假数据
	private int[] meetPhotos;
	private int[] enrollMembers;
	private List<ListPersonBean> listPersonBeans;
	private String url = "http://shopimg.kongfz.com/20090923/1449467/12773XviT8J_b.jpg";
	private SpannableString spanText;
	private String mMeetDetail = new String();
	private View meetDetailView;

	private int mActivitiId;
	private String mActivityName;
	private String mActivityBeginDate;
	private String mActivityPlace;
	private double mFemaleCost;
	private double mMaleCost;

	@Override
	public void addViewIntoContent() {
		meetDetailView = View
				.inflate(this, R.layout.activity_meet_detail, null);
		mBaseContent.addView(meetDetailView);
		ViewUtils.inject(this);

		setTitle(R.string.meet_detail);
		setLeftTxt(R.string.back);

		findView_addListener();
		prepareData();
	}

	public void findView_addListener() {
		mTvShare = (TextView) findViewById(R.id.tv_share_meet);
		mTvEnroll = (TextView) findViewById(R.id.tv_enroll_meet);
		mTvMeetDescription = (TextView) findViewById(R.id.tv_meet_description);

		mTvShare.setOnClickListener(this);
		mTvEnroll.setOnClickListener(this);
		mTvMeetDescription.setOnClickListener(this);
	}

	public void prepareData() {
		enrollMembers = new int[] {};

		// 报名成员的假数据
		/*
		 * listPersonBeans = new ArrayList<ListPersonBean>(); final
		 * ListPersonBean person = new ListPersonBean(); for (int i = 0; i < 10;
		 * i++) { person.setUrlHead(url); listPersonBeans.add(person); }
		 */
		mActivitiId = getIntent().getIntExtra(MEETDETAILACTIVITY_ID, 0);

		getDataFromServer();

	}

	public void getDataFromServer() {
		// 附近联谊
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
								Type listTypeData1 = new TypeToken<ArrayList<MeetDetailBean>>() {
								}.getType();
								Type listTypeData2 = new TypeToken<ArrayList<MeetPictureBean>>() {
								}.getType();
								Type listTypeData3 = new TypeToken<ArrayList<MeetEnrolledMemberBean>>() {
								}.getType();

								ArrayList<MeetDetailBean> meetDetailBeans = gson
										.fromJson(joResult.getString("data1"),
												listTypeData1);
								ArrayList<MeetPictureBean> meetPictureBeans = gson
										.fromJson(joResult.getString("data2"),
												listTypeData2);
								ArrayList<MeetEnrolledMemberBean> meetEnrolledMemberBeans = gson
										.fromJson(joResult.getString("data3"),
												listTypeData3);
								MeetDetailBean meetDetailBean = meetDetailBeans
										.get(0);

								bindDataToView(meetDetailBean,
										meetEnrolledMemberBeans,
										meetPictureBeans);

								mMeetEnrolledMemberBeans = meetEnrolledMemberBeans;
								mMeetPictureBeans = meetPictureBeans;
							} catch (JSONException e) {
								e.printStackTrace();
							}

						} else {

							((BaseActivity) MeetDetailActivity.this)
									.removeProgressDialog();
							ToastUtil.showCenterToast(MeetDetailActivity.this,
									item.getMessage() + "", Toast.LENGTH_SHORT);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						((BaseActivity) MeetDetailActivity.this)
								.removeProgressDialog();
						ToastUtil
								.showCenterToast(
										MeetDetailActivity.this,
										ResourcesUtil
												.getString(R.string.base_load_failed_des),
										Toast.LENGTH_SHORT);
					}

				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();
				LogUtil.d("----=========----", mActivitiId + "");
				map.put("M", "Get_Merchant_Activity");
				map.put("ACID", mActivitiId + "");

				String sign = "";
				try {
					sign = MD5Util.getSignature(map, SystemConsts.SIGN_KEY);
					map.put("sign", sign);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return map;
			}
		};
		YBWApplication.mQueue.add(stringRequest);
	}

	public void bindDataToView(MeetDetailBean meetDetailBean,
			ArrayList<MeetEnrolledMemberBean> meetEnrolledMemberBeans,
			ArrayList<MeetPictureBean> meetPictureBeans) {

		ImageLoader.getInstance().displayImage(
				MzApi.IMG_URL + meetDetailBean.getLogoImage(),
				mTvMeetDetialBackground);
		String tagString = meetDetailBean.getAcTable();
		if (!TextUtils.isEmpty(tagString)) {
			if (tagString.contains(ResourcesUtil.getString(R.string.more_male))) {
				mTvHandsome.setText(R.string.more_male);
				mTvHandsome.setVisibility(View.VISIBLE);
			}
			if (tagString.contains(ResourcesUtil
					.getString(R.string.more_female))) {
				mTvHandsome.setText(R.string.more_male);
				mTvHandsome.setVisibility(View.VISIBLE);
			}
			if (tagString.contains(ResourcesUtil
					.getString(R.string.lower_price)))
				mTvBelle.setVisibility(View.VISIBLE);
			if (tagString.contains(ResourcesUtil.getString(R.string.weekend)))
				mTvWeekend.setVisibility(View.VISIBLE);
			if (tagString.contains(ResourcesUtil
					.getString(R.string.many_people_chose)))
				mTvSlow.setVisibility(View.VISIBLE);
			if (tagString.contains(ResourcesUtil
					.getString(R.string.multiply_img)))
				mTvPhoto.setVisibility(View.VISIBLE);
		}
		mActivityName = meetDetailBean.getName();
		mTvActivityName.setText(mActivityName);
		mActivityBeginDate = meetDetailBean.getBeginDate();
		mTvDate.setText(mActivityBeginDate);
		mActivityPlace = meetDetailBean.getOtherAddress();
		mTvPlace.setText(mActivityPlace);
		// mIvMap : ImageView
		mTvFemaleNumber.setText(String.format(
				this.getResources().getString(R.string.member_number),
				meetDetailBean.getWmanqty()));
		mTvMaleNumber.setText(String.format(
				this.getResources().getString(R.string.member_number),
				meetDetailBean.getManqty()));
		// mHlvEnrollMember : HorizontalListView
		mTvOrganizer.setText(meetDetailBean.getMerchantId() + "某商家名字");
		mFemaleCost = meetDetailBean.getWManCost();
		mTvFemaleCost.setText(String.format(
				this.getResources().getString(R.string.member_cost),
				mFemaleCost));
		mMaleCost = meetDetailBean.getManCost();
		mTvMaleCost
				.setText(String.format(
						this.getResources().getString(R.string.member_cost),
						mMaleCost));
		// mHlvMeetPhotos : HorizontalListView
		mMeetDetail = meetDetailBean.getAcDetail();
		mTvMeetDescription.setText(mMeetDetail);

		// 报名成员的横向listview
		if (meetEnrolledMemberBeans != null
				& meetEnrolledMemberBeans.size() != 0) {
			mHlvEnrollMember
					.setAdapter(new MeetEnrolledBeanAdapter<MeetEnrolledMemberBean>(
							MeetDetailActivity.this, meetEnrolledMemberBeans) {
						@Override
						public void bindDataToView(MeetEnrolledMemberBean t,
								HomeHolder holder) {
							ImageLoader.getInstance().displayImage(
									MzApi.IMG_URL + t.getHeadImg(),
									holder.civ_enrolled_member_meet);
						}
					});
		}

		if (meetEnrolledMemberBeans != null
				& meetEnrolledMemberBeans.size() != 0) {
			mHlvMeetPhotos
					.setAdapter(new MeetPictureBeanAdapter<MeetPictureBean>(
							MeetDetailActivity.this, meetPictureBeans) {

						@Override
						public void bindDataToView(
								MeetPictureBean t,
								com.scsy150.meet.page.adapter.MeetPictureBeanAdapter.HomeHolder holder) {
							ImageLoader.getInstance().displayImage(
									MzApi.IMG_URL + t.getAcImg(),
									holder.picture);
						}
					});
		}

		mHlvMeetPhotos.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent meetPhotoIntent = new Intent(MeetDetailActivity.this,
						PhotoViewerActivity.class);
				meetPhotoIntent.putExtra("position", position);
				meetPhotoIntent.putExtra("meetPictureBeans", mMeetPictureBeans);
				startActivity(meetPhotoIntent);
				finish();
			}
		});

		showMeetDescription();
	}

	@Override
	@OnClick({ R.id.iv_map, R.id.tv_organizer, R.id.iv_show_more_photos,
			R.id.tv_meet_description, R.id.tv_share_meet, R.id.tv_enroll_meet })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_map:
			// 展示组织活动商家的地理位置
			LogUtil.d("map", "iv_map is clicked...");
			break;
		case R.id.tv_organizer:
			// 商家的详细信息
			Intent organizerIntent = new Intent(this,
					OrganizerDetailInfoActivity.class);
			startActivity(organizerIntent);
			finish();
			break;
		case R.id.iv_show_more_photos:
			// 报名成员
			Intent enrolledMemberIntent = new Intent(this,
					EnrolledMemberAcitivity.class);
			enrolledMemberIntent.putExtra("meetEnrolledMemberBean",
					mMeetEnrolledMemberBeans);
			startActivity(enrolledMemberIntent);
			finish();
			break;
		case R.id.tv_meet_description:
			// 活动详情
			spreadDesAnim();
			break;
		case R.id.hlv_meet_photos:
			// 活动相册,item点击事件
			break;
		case R.id.tv_share_meet:
			LogUtil.d("share", "tv_share_meet is clicked...");
			// 分享
			break;
		case R.id.tv_enroll_meet:
			// 立即报名
			Intent enrollIntent = new Intent(this, EnrollActivity.class);
			enrollIntent.putExtra("activity_name", mActivityName);
			enrollIntent.putExtra("activity_date", mActivityBeginDate);
			enrollIntent.putExtra("activity_place", mActivityPlace);
			enrollIntent.putExtra("female_cost", mFemaleCost);
			enrollIntent.putExtra("male_cost", mMaleCost);
			startActivity(enrollIntent);
			finish();
			break;
		default:
			super.onClick(v);
			break;
		}
	}

	private void showMeetDescription2() {

		float textSize = mTvMeetDescription.getTextSize();
		int screenWidth = ScreenUtil.getScreenWidth(this);
		int count = screenWidth * 2 / DensityUtil.sp2px(this, textSize);
		count = 2 * count;
		String spread = "点击展开";
		spanText = new SpannableString(spread);
		spanText.setSpan(new ForegroundColorSpan(Color.BLUE), 0, 4,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		if (count <= mMeetDetail.length()) {
			mTvMeetDescription.setText("\t" + mMeetDetail.substring(0, count));
		}
		mTvMeetDescription.append(spanText);
	}

	private void showMeetDescription() {
		showMeetDescription2();
		// 属性动画改变它的高度
		mTvMeetDescription.setMaxLines(3);
		mTvMeetDescription.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						mTvMeetDescription.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						meetDescriptionMinHeight = mTvMeetDescription
								.getMeasuredHeight();
						meetDescriptionMinHeight = mTvMeetDescription
								.getHeight();
						mTvMeetDescription.setText(mMeetDetail);

						// 还原显示所有文本
						mTvMeetDescription.setMaxLines(Integer.MAX_VALUE);// 显示全部的文本
						mTvMeetDescription.getViewTreeObserver()
								.addOnGlobalLayoutListener(
										new OnGlobalLayoutListener() {

											@Override
											public void onGlobalLayout() {
												// 移除监听
												mTvMeetDescription
														.getViewTreeObserver()
														.removeGlobalOnLayoutListener(
																this);
												meetDescriptionMaxHeight = mTvMeetDescription
														.getMeasuredHeight();

												mTvMeetDescription
														.getLayoutParams().height = meetDescriptionMinHeight;
												mTvMeetDescription
														.requestLayout();
												mSvMeetDetail.invalidate();
											}
										});
					}
				});
	}

	private void switchDescription(boolean isMeetDescriptionExtend) {

		if (!isMeetDescriptionExtend) {
			float textSize = mTvMeetDescription.getTextSize();
			int screenWidth = ScreenUtil.getScreenWidth(this);
			int count = screenWidth * 2 / DensityUtil.sp2px(this, textSize);
			count = 2 * (count - 4);
			String spread = "点击展开";
			spanText = new SpannableString(spread);
			spanText.setSpan(
					new ForegroundColorSpan(Color.rgb(0x12, 0xbc, 0xc9)), 0, 4,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			if (count <= mMeetDetail.length()) {
				mTvMeetDescription.setText("\t"
						+ mMeetDetail.substring(0, count));
			}
		} else {
			String shrink = "收起";
			spanText = new SpannableString(shrink);
			spanText.setSpan(
					new ForegroundColorSpan(Color.rgb(0x12, 0xbc, 0xc9)), 0, 2,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			mTvMeetDescription.setText("\t" + mMeetDetail.substring(0));
		}
		mTvMeetDescription.append(spanText);
	}

	/**
	 * 展开活动详情
	 */
	public void spreadDesAnim() {
		ValueAnimator meetDesAnimator;
		if (isMeetDescriptionExtend) {
			meetDesAnimator = ObjectAnimator.ofInt(meetDescriptionMaxHeight,
					meetDescriptionMinHeight);
		} else {
			meetDesAnimator = ObjectAnimator.ofInt(meetDescriptionMinHeight,
					meetDescriptionMaxHeight);
		}
		meetDesAnimator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animator) {
				int animatedValue = (Integer) animator.getAnimatedValue();
				mTvMeetDescription.getLayoutParams().height = animatedValue;
				mTvMeetDescription.requestLayout();

				if (!isMeetDescriptionExtend) {
					// 向上执行扩展动画的时候，scrollView才需要滚动
					int scrollY = animatedValue - meetDescriptionMinHeight;
					mSvMeetDetail.scrollBy(0, scrollY);
				}
			}
		});
		meetDesAnimator.setDuration(300);
		meetDesAnimator.addListener(new MeetDesAnimListener());
		meetDesAnimator.start();
	}

	class MeetDesAnimListener implements AnimatorListener {
		@Override
		public void onAnimationCancel(Animator arg0) {
		}

		@Override
		public void onAnimationEnd(Animator arg0) {
			isMeetDescriptionExtend = !isMeetDescriptionExtend;
			switchDescription(isMeetDescriptionExtend);
		}

		@Override
		public void onAnimationRepeat(Animator arg0) {
		}

		@Override
		public void onAnimationStart(Animator arg0) {
		}
	}

}
