package com.scsy150.mine.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.scsy150.R;
import com.scsy150.consts.MzApi;
import com.scsy150.meet.activity.MeetDetailActivity;
import com.scsy150.mine.activity.OrderDetailsActivity;
import com.scsy150.mine.bean.IngAppointmentBean;
import com.scsy150.mine.bean.IngMeetBean;
import com.scsy150.util.view.CircleImageView;

/**
 * 
 */
public class IngMeetListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<IngMeetBean> list;

	public IngMeetListAdapter(Context mContext, ArrayList<IngMeetBean> list) {
		this.list = list;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder hold = null;
		final IngMeetBean bean = list.get(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_ing_meet, null);
			hold = new Holder();

			hold.item_ing_title = (TextView) convertView
					.findViewById(R.id.item_ing_title);
			hold.item_ing_img = (CircleImageView) convertView
					.findViewById(R.id.item_ing_img);
			hold.item_ing_time = (TextView) convertView
					.findViewById(R.id.item_ing_time);
			hold.item_ing_address = (TextView) convertView
					.findViewById(R.id.item_ing_address);
			hold.item_ing_meet = (TextView) convertView
					.findViewById(R.id.item_ing_meet);
			hold.item_ing_order = (TextView) convertView
					.findViewById(R.id.item_ing_order);
			convertView.setTag(hold);
		} else {
			hold = (Holder) convertView.getTag();
		}

		ImageLoader.getInstance().displayImage(
				MzApi.IMAGE_DOWNLOAD + bean.getLogoimg(), hold.item_ing_img);
		hold.item_ing_title.setText(bean.getActivityname());
		hold.item_ing_time.setText(bean.getBegindate());
		hold.item_ing_address.setText(bean.getOtheraddress());
		hold.item_ing_meet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 联谊详情
				Intent intent = new Intent(mContext, MeetDetailActivity.class);
				intent.putExtra(MeetDetailActivity.MEETDETAILACTIVITY_ID, bean.getAcid());
				mContext.startActivity(intent);

			}
		});
		hold.item_ing_order.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 订单详情
				Intent intent = new Intent(mContext, OrderDetailsActivity.class);

				intent.putExtra(OrderDetailsActivity.ING_MEET_LIST_ADAPTER,
						bean);
				mContext.startActivity(intent);
			}
		});

		return convertView;

	}

	class Holder {
		ImageView item_ing_img;
		TextView item_ing_title;
		TextView item_ing_time;
		TextView item_ing_address;
		TextView item_ing_meet;
		TextView item_ing_order;

	}

}
