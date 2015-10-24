package com.scsy150.main;

import java.util.ArrayList;
import java.util.HashMap;

import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

import com.scsy150.R;
import com.scsy150.adapter.MeetDetailAdapter;
import com.scsy150.base.BaseFragment;
import com.scsy150.meet.page.BasePage;
import com.scsy150.meet.page.impl.HotPage;
import com.scsy150.meet.page.impl.NearPage;
import com.scsy150.meet.page.impl.RecentPage;
import com.scsy150.widget.DepthPageTransformer;
import com.scsy150.widget.ViewPagerCompat;

public class MeetFragment extends BaseFragment implements OnClickListener {

	private HashMap<Integer, BasePage> mDetailPagerMap;
	// 填充的假广告页
	private int[] mImageIds = { R.drawable.advert_00, R.drawable.advert_01,
			R.drawable.advert_02 };
	public ViewPagerCompat mMeetViewPager;

	ArrayList<RadioButton> buttonList;

	public MeetFragment() {
	}
	public MeetFragment(ArrayList<RadioButton> buttonList) {
		this.buttonList = buttonList;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void addView() {
		mMeetViewPager = (ViewPagerCompat) View.inflate(mActivity,
				R.layout.viewpager, null);
		mDetailPagerMap = new HashMap<Integer, BasePage>();
		/*
		 * mDetailPagerMap.put(0, PageFactory.getImpl(mActivity, "NearPage"));
		 * mDetailPagerMap.put(1, PageFactory.getImpl(mActivity, "HotPage"));
		 * mDetailPagerMap.put(2, PageFactory.getImpl(mActivity, "RecentPage"));
		 */
		mDetailPagerMap.put(0, new NearPage(mActivity));
		mDetailPagerMap.put(1, new HotPage(mActivity));
		mDetailPagerMap.put(2, new RecentPage(mActivity));
		mMeetViewPager.setAdapter(new MeetDetailAdapter(mDetailPagerMap));
		mMeetViewPager.setCurrentItem(0);
		mContainer.addView(mMeetViewPager);

		mMeetViewPager.setPageTransformer(true, new DepthPageTransformer());

		mMeetViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					buttonList.get(0).setVisibility(View.VISIBLE);
					buttonList.get(1).setVisibility(View.VISIBLE);
					buttonList.get(2).setVisibility(View.VISIBLE);
					buttonList.get(0).setChecked(true);
					buttonList.get(1).setChecked(false);
					buttonList.get(2).setChecked(false);
					break;
				case 1:
					buttonList.get(0).setChecked(false);
					buttonList.get(1).setChecked(true);
					buttonList.get(2).setChecked(false);
					break;
				case 2:
					buttonList.get(0).setChecked(false);
					buttonList.get(1).setChecked(false);
					buttonList.get(2).setChecked(true);
					break;

				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	@Override
	public void fragmentClick(View v) {
		switch (v.getId()) {
		case R.id.near:
			mMeetViewPager.setCurrentItem(0);
			break;
		case R.id.hot:
			mMeetViewPager.setCurrentItem(1);
			break;
		case R.id.recent:
			mMeetViewPager.setCurrentItem(2);
			break;
		default:
			break;
		}
	}

}
