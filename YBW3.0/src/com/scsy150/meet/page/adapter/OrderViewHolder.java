package com.scsy150.meet.page.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.scsy150.R;

public class OrderViewHolder {
	TextView tvOrderName;
	TextView tvOrderState;
	ImageView ivOrderImg;
	TextView tvOrderTitle;
	TextView tvOrderRealPayment;
	Button btnOrderEvaluation;

	public OrderViewHolder(View convertView) {
		tvOrderName = (TextView) convertView
				.findViewById(R.id.tv_order_name);
		tvOrderState = (TextView) convertView.findViewById(R.id.tv_order_state);
		ivOrderImg = (ImageView) convertView.findViewById(R.id.iv_order_img);
		tvOrderTitle = (TextView) convertView.findViewById(R.id.tv_order_title);
		tvOrderRealPayment = (TextView) convertView
				.findViewById(R.id.order_real_payment);
		btnOrderEvaluation = (Button) convertView
				.findViewById(R.id.order_evaluation);
	}

	public static OrderViewHolder getHolder(View convertView) {
		OrderViewHolder holder = (OrderViewHolder) convertView.getTag();
		if (holder == null) {
			holder = new OrderViewHolder(convertView);
			convertView.setTag(holder);
		}
		return holder;
	}
}
