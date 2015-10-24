package com.scsy150.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.scsy150.R;

public class HeaderNotifyView {
	private View mView;

	public HeaderNotifyView(Context context) {
		mView = LayoutInflater.from(context).inflate(
				R.layout.layout_header_tip, null);
	}

	public HeaderNotifyView(Context context, int msg) {
		mView = LayoutInflater.from(context).inflate(
				R.layout.layout_header_tip, null);
		TextView txt = (TextView) mView.findViewById(R.id.header_tip);
		txt.setText(msg);
	}

	public View getView() {
		return mView;
	}
}
