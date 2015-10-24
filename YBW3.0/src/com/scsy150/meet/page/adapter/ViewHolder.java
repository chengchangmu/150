package com.scsy150.meet.page.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scsy150.R;

public class ViewHolder {
	ImageView ivActivityPhotos;
	TextView tvTitle, tvDistance, tvNumber, tvDate;

	public ViewHolder(View convertView) {
		ivActivityPhotos = (ImageView) convertView
				.findViewById(R.id.iv_activityPhotos);
		tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
		tvDistance = (TextView) convertView.findViewById(R.id.tv_distance);
		tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
		tvDate = (TextView) convertView.findViewById(R.id.tv_date);
	}

	public static ViewHolder getHolder(View convertView) {
		ViewHolder holder = (ViewHolder) convertView.getTag();
		if (holder == null) {
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}
		return holder;
	}
}

