package com.scsy150.common;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.scsy150.R;
import com.scsy150.base.BaseActivity;
import com.scsy150.util.view.ViewUtils;
import com.scsy150.widget.PointIndicateView;
import com.scsy150.widget.ScaleViewPager;
import com.scsy150.widget.scaleimage.PhotoView;
import com.scsy150.widget.scaleimage.PhotoViewAttacher;

/*
 * Copyright (C) 2015 四川硅谷科技重庆分公司
 * 版权所有
 *
 * 功能描述：图片查看工具，只需要传入URL的list和首先显示哪张图片即可
 * 作者：硅谷科技
 * 创建时间：2014-3-20
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */

public class ImageViewActivity extends BaseActivity implements OnClickListener {
	public static final String IMAGE_POSITION_KEY = "imagePositionKey";
	public static final String IMAGE_PATH_KEY = "imagePathKey";
	private List<String> mPathList;

	@Override
	public void addViewIntoContent() {
		View view = View.inflate(this, R.layout.activity_image_viewer, null);
		mBaseContent.addView(view);
		ViewUtils.inject(this);
		needHeader(false);
		init();
	}

	private void init() {
		PointIndicateView pointContent = (PointIndicateView) findViewById(R.id.image_pager_points);
		ScaleViewPager mPager = (ScaleViewPager) findViewById(R.id.image_view_pager);

		Intent intent = getIntent();
		if (intent != null) {
			mPathList = intent.getStringArrayListExtra(IMAGE_PATH_KEY);
			int position = intent.getIntExtra(IMAGE_POSITION_KEY, 0);
			if (mPathList != null && mPathList.size() != 0) {
				ImagePagerAdapter adapter = new ImagePagerAdapter(this,
						mPathList);
				mPager.setAdapter(adapter);
				pointContent.setViewPager(mPager, mPathList.size(), position,
						true, null);
			} else {
				showMsg(getString(R.string.image_view_error),
						R.string.error_tip_title);
			}
		} else {
			showMsg(getString(R.string.image_view_error),
					R.string.error_tip_title);
		}
	}

	private class ImagePagerAdapter extends PagerAdapter {
		private Context mContext;
		private List<String> mList;

		public ImagePagerAdapter(Context ctx, List<String> data) {
			mContext = ctx;
			mList = data;
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			View view = LayoutInflater.from(mContext).inflate(
					R.layout.layout_image_viewer, null);
			PhotoView imageView = (PhotoView) view
					.findViewById(R.id.drag_image_viewer);

			ImageLoader.getInstance().displayImage(
					mList.get(position % mList.size()), imageView);
			imageView.setClickListener(new PhotoViewAttacher.onClickListener() {

				@Override
				public void onClick() {
					ImageViewActivity.this.finish();
				}
			});
			((ViewPager) container).addView(view, 0);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			if (container != null) {
				((ViewPager) container).removeView((View) object);
			}
		}

		@Override
		public int getCount() {
			if (mList != null && mList.size() > 1) {
				return Integer.MAX_VALUE;
			} else {
				return mList == null ? 0 : mList.size();
			}
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	};

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

}
