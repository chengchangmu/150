package com.scsy150.meet.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.scsy150.R;
import com.scsy150.consts.MzApi;
import com.scsy150.meet.bean.MeetPictureBean;
import com.scsy150.meet.fragment.PhotoFragment;
import com.scsy150.widget.ScaleViewPager;

public class PhotoViewerActivity extends FragmentActivity {
	
	private ScaleViewPager viewPager;
	private ArrayList<MeetPictureBean> mMeetPictureBeans;

	/** 得到上一个界面点击图片的位置 */
	private int position = 0;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_viewer);
		Intent intent = getIntent();
		position = intent.getIntExtra("position", 0);
		mMeetPictureBeans = (ArrayList<MeetPictureBean>) intent.getSerializableExtra("meetPictureBeans");
		initViewPager();
	}

	private void initViewPager() {

		viewPager = (ScaleViewPager) findViewById(R.id.svp_photo_viewer);
		ViewPagerAdapter adapter = new ViewPagerAdapter(
				getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		// 跳转到第几个界面
		viewPager.setCurrentItem(position);

	}

	private class ViewPagerAdapter extends FragmentStatePagerAdapter {

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			MeetPictureBean meetPictureBean = mMeetPictureBeans.get(position);
			return new PhotoFragment(MzApi.IMG_URL + meetPictureBean.getAcImg());
		}

		@Override
		public int getCount() {
			return mMeetPictureBeans.size();
		}

	}
}
