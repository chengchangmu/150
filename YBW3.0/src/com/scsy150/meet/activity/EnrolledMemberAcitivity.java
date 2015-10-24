package com.scsy150.meet.activity;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.scsy150.R;
import com.scsy150.base.BaseActivity;
import com.scsy150.meet.bean.MeetEnrolledMemberBean;
import com.scsy150.meet.page.adapter.MemberAdapter;
import com.scsy150.util.LogUtil;

public class EnrolledMemberAcitivity extends BaseActivity implements
		OnClickListener {

	private ListView mLvCommonList;
	private TextView tvMemberAll;
	private TextView tvFemaleOnly;
	private TextView tvMaleOnly;
	private View contentView;
	private ArrayList<MeetEnrolledMemberBean> mFemaleList;
	private ArrayList<MeetEnrolledMemberBean> mMaleList;
	private ArrayList<MeetEnrolledMemberBean> meetEnrolledMemberBeans;
	private MemberAdapter mMemberAdapter;

	@Override
	public void addViewIntoContent() {
		View view = LayoutInflater.from(this).inflate(R.layout.page_order_list,
				null);
		mBaseContent.addView(view);
		setTitle(R.string.signup_immediately);
		setRightDrawable(R.drawable.sex_selector);
		setLeftTxt(R.string.back);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.popup_select_sex, null);

		findView_addListener();

		prepareData();
	}

	@SuppressWarnings("unchecked")
	public void prepareData() {

		meetEnrolledMemberBeans = (ArrayList<MeetEnrolledMemberBean>) getIntent()
				.getSerializableExtra("meetEnrolledMemberBean");

		mMemberAdapter = new MemberAdapter(this, meetEnrolledMemberBeans);
		mLvCommonList.setAdapter(mMemberAdapter);

		for (MeetEnrolledMemberBean meetEnrolledMemberBean : meetEnrolledMemberBeans) {
			int sex = meetEnrolledMemberBean.getSex();
			if (0 == sex) {
				mFemaleList = new ArrayList<MeetEnrolledMemberBean>();
				mFemaleList.add(meetEnrolledMemberBean);
			} else if (1 == sex) {
				mMaleList = new ArrayList<MeetEnrolledMemberBean>();
				mMaleList.add(meetEnrolledMemberBean);
			}
		}
	}

	public void findView_addListener() {

		mLvCommonList = (ListView) findViewById(R.id.lv_common_list);

		tvMemberAll = (TextView) contentView.findViewById(R.id.tv_member_all);
		tvFemaleOnly = (TextView) contentView.findViewById(R.id.tv_female_only);
		tvMaleOnly = (TextView) contentView.findViewById(R.id.tv_male_only);

		tvMemberAll.setOnClickListener(this);
		tvFemaleOnly.setOnClickListener(this);
		tvMaleOnly.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_image:
			showPopup(v);
			break;
		case R.id.tv_member_all:
			LogUtil.d("=============", "tv_all is clicked...");
			mMemberAdapter = new MemberAdapter<MeetEnrolledMemberBean>(this,
					meetEnrolledMemberBeans);
			mLvCommonList.setAdapter(mMemberAdapter);
			mMemberAdapter.notifyDataSetChanged();
			break;
		case R.id.tv_female_only:
			LogUtil.d("=============", "tv_female is clicked...");
			mMemberAdapter = new MemberAdapter<MeetEnrolledMemberBean>(this,
					mFemaleList);
			mLvCommonList.setAdapter(mMemberAdapter);
			mMemberAdapter.notifyDataSetChanged();
			break;
		case R.id.tv_male_only:
			LogUtil.d("=============", "tv_male is clicked...");
			mMemberAdapter = new MemberAdapter<MeetEnrolledMemberBean>(this,
					mMaleList);
			mLvCommonList.setAdapter(mMemberAdapter);
			mMemberAdapter.notifyDataSetChanged();
			break;
		default:
			super.onClick(v);
			break;
		}
	}

	public void showPopup(View view) {
		final PopupWindow sexSelectWindow = new PopupWindow(contentView,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		sexSelectWindow.setTouchable(true);

		sexSelectWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 sexSelectWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});

		// 如果不设置背景，无论是点击外部区域还是Back键都无法dismiss弹框
		sexSelectWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.title_list_selector));

		// 设置好参数之后再show
		sexSelectWindow.showAsDropDown(view);
	}

}
