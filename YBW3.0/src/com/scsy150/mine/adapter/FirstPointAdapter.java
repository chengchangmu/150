package com.scsy150.mine.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.scsy150.R;
import com.scsy150.chat.activity.ChatActivity;
import com.scsy150.consts.MzApi;
import com.scsy150.mine.bean.FirstPointBean;
import com.scsy150.util.view.CircleImageView;

/*
 * Copyright (C) 2014 四川硅谷科技重庆分公司
 * 版权所有
 *
 * 功能描述：系统消息list的adapter
 * 作者：硅谷科技
 * 创建时间：2015-09-31
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */
public class FirstPointAdapter extends BaseAdapter {

	private ArrayList<FirstPointBean> list;
	private Context mContext;

	public FirstPointAdapter(Context context, ArrayList<FirstPointBean> list) {
		this.mContext = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder hold = null;
		FirstPointBean bean = list.get(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_mine_firstpoint, null);
			hold = new Holder();

			hold.mItemTitle = (TextView) convertView
					.findViewById(R.id.item_title);
			hold.img = (CircleImageView) convertView
					.findViewById(R.id.item_img);
			hold.mItemNum = (TextView) convertView.findViewById(R.id.item_num);
			hold.mItemDesc = (TextView) convertView
					.findViewById(R.id.item_desc);
			convertView.setTag(hold);
		} else {
			hold = (Holder) convertView.getTag();
		}
		if (!TextUtils.isEmpty(bean.getMhname())) {
			hold.mItemTitle.setText(bean.getMhname());
		}
		if (!TextUtils.isEmpty(bean.getDicname())) {
			hold.mItemDesc.setText(bean.getDicname());
		}
		if (!TextUtils.isEmpty(bean.getHeadimg())) {
			ImageLoader.getInstance().displayImage(
					MzApi.IMAGE_DOWNLOAD + bean.getHeadimg(), hold.img);
		}

		hold.mItemNum.setText(bean.getFqty() + "");

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, ChatActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
				i.putExtra("userId", "15236454850");
				mContext.startActivity(i);
			}
		});

		return convertView;
	}

	class Holder {
		TextView mItemTitle;
		TextView mItemNum;
		TextView mItemDesc;
		CircleImageView img;
	}

}
