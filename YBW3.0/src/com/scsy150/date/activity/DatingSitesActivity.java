package com.scsy150.date.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.scsy150.R;
import com.scsy150.date.fragment.DatingSitesFragment;

public class DatingSitesActivity extends Activity {
	DatingSitesFragment datingSitesFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dating_sites_layout);

		FragmentManager fm = getFragmentManager();
		FragmentTransaction tran = fm.beginTransaction();
		if (datingSitesFragment == null) {
			datingSitesFragment = new DatingSitesFragment();
			tran.add(R.id.ff_place, datingSitesFragment);

		} else {
			tran.show(datingSitesFragment);
		}
		tran.commit();

		initView();

	}

	private void initView() {

		findViewById(R.id.bt_back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
	}
}
