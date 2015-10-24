package com.scsy150.meet.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.scsy150.R;

public class PhotoFragment extends Fragment {
	private String mImgPath;

	public PhotoFragment(String imgPath) {

		this.mImgPath = imgPath;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.viewpager_scale, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
		ImageView imageView = (ImageView) view
				.findViewById(R.id.svp_meet_photos);
		ImageLoader.getInstance().displayImage(mImgPath, imageView);
	}
}
