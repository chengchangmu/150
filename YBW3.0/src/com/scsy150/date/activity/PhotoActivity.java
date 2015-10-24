package com.scsy150.date.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.scsy150.R;
import com.scsy150.date.bean.NowUserBean;
import com.scsy150.date.fragment.PhotoFragment;

public class PhotoActivity extends Activity {
	PhotoFragment photoFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_layout);
		FragmentManager fManager = getFragmentManager();
		FragmentTransaction fTransaction = fManager.beginTransaction();
		if (photoFragment == null) {
			photoFragment = new PhotoFragment();
			fTransaction.add(R.id.ll_photo, photoFragment);
			Intent intent = getIntent();
			NowUserBean nbBean = (NowUserBean) intent.getExtras()
					.getSerializable("photoData");
			photoFragment.getBean(nbBean);

		} else {
			fTransaction.show(photoFragment);
		}
		fTransaction.commit();

		initview();

	}

	private void initview() {

		findViewById(R.id.bt_back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();

			}
		});

	}
}
