package com.scsy150.mine.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.scsy150.R;
import com.scsy150.consts.MzApi;
import com.scsy150.meet.activity.MeetDetailActivity;
import com.scsy150.meet.activity.OrganizerDetailInfoActivity;
import com.scsy150.mine.activity.OrderDetailsActivity;
import com.scsy150.mine.bean.IngAppointmentBean;
import com.scsy150.util.ResourcesUtil;
import com.scsy150.util.view.CircleImageView;

/**
 * 
 */
public class IngListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<IngAppointmentBean> list;

	public IngListAdapter(Context mContext, ArrayList<IngAppointmentBean> list) {
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
		final IngAppointmentBean bean = list.get(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_ing, null);
			hold = new Holder();

			hold.item_ing_title = (TextView) convertView
					.findViewById(R.id.item_ing_title);
			hold.item_ing_img = (ImageView) convertView
					.findViewById(R.id.item_ing_img);
			hold.item_ing_time = (TextView) convertView
					.findViewById(R.id.item_ing_time);
			hold.item_ing_address = (TextView) convertView
					.findViewById(R.id.item_ing_address);
			hold.item_ing_meet = (TextView) convertView
					.findViewById(R.id.item_ing_meet);
			hold.item_ing_order = (TextView) convertView
					.findViewById(R.id.item_ing_order);

			hold.item_my_name = (TextView) convertView
					.findViewById(R.id.item_my_name);

			hold.item_object_img = (CircleImageView) convertView
					.findViewById(R.id.item_object_img);
			hold.item_my_img = (CircleImageView) convertView
					.findViewById(R.id.item_my_img);

			hold.item_object_name = (TextView) convertView
					.findViewById(R.id.item_object_name);
			hold.item_my_confirm = (TextView) convertView
					.findViewById(R.id.item_my_confirm);
			hold.item_object_confirm = (TextView) convertView
					.findViewById(R.id.item_object_confirm);

			convertView.setTag(hold);
		} else {
			hold = (Holder) convertView.getTag();
		}

		ImageLoader.getInstance().displayImage(
				MzApi.IMAGE_DOWNLOAD + bean.getMerchantimage(),
				hold.item_ing_img);
		ImageLoader.getInstance().displayImage(
				MzApi.IMAGE_DOWNLOAD + bean.getMyimage(), hold.item_my_img);
		ImageLoader.getInstance()
				.displayImage(MzApi.IMAGE_DOWNLOAD + bean.getYouimage(),
						hold.item_object_img);
		hold.item_ing_title.setText(bean.getMhname());

		hold.item_my_name.setText(bean.getMyname());
		hold.item_object_name.setText(bean.getYouname());

		if (bean.getMyapprove() == 0) {
			hold.item_my_confirm.setText(ResourcesUtil
					.getString(R.string.not_code));
		} else {
			hold.item_my_confirm.setText(ResourcesUtil
					.getString(R.string.confirmed_code));
		}
		if (bean.getYouapprove() == 0) {
			hold.item_object_confirm.setText(ResourcesUtil
					.getString(R.string.not_code));
		} else {
			hold.item_object_confirm.setText(ResourcesUtil
					.getString(R.string.confirmed_code));
		}
		hold.item_ing_time.setText(bean.getBegindate());
		hold.item_ing_address.setText(bean.getOtheraddress());
		hold.item_ing_meet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 商家详情
				Intent intent = new Intent(mContext, OrganizerDetailInfoActivity.class);
				intent.putExtra(MeetDetailActivity.MEETDETAILACTIVITY_ID, bean.getAcid());
				mContext.startActivity(intent);
			}
		});
		hold.item_ing_order.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 订单详情
				Intent intent = new Intent(mContext, OrderDetailsActivity.class);
				intent.putExtra(OrderDetailsActivity.ING_LIST_ADAPTER, bean);
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

		CircleImageView item_object_img;
		CircleImageView item_my_img;

		TextView item_my_name;
		TextView item_object_name;
		TextView item_my_confirm;
		TextView item_object_confirm;

	}

}
