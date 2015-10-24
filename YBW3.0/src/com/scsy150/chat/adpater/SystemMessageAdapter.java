package com.scsy150.chat.adpater;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.scsy150.R;
import com.scsy150.chat.activity.AuthMessageActivity;
import com.scsy150.chat.activity.AuthMessageAndSureActivity;
import com.scsy150.chat.bean.SystemMessageBean;

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
public class SystemMessageAdapter extends BaseAdapter {

	private ArrayList<SystemMessageBean> list;
	private Context mContext;

	public SystemMessageAdapter(Context context,
			ArrayList<SystemMessageBean> list) {
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
		// TODO Auto-generated method stub
		Holder hold = null;
		final SystemMessageBean bean = list.get(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_message_layout, null);
			hold = new Holder();

			hold.mItemTitle = (TextView) convertView
					.findViewById(R.id.item_title);
			hold.mItemTime = (TextView) convertView
					.findViewById(R.id.item_time);
			hold.mItemDesc = (TextView) convertView
					.findViewById(R.id.item_desc);
			convertView.setTag(hold);
		} else {
			hold = (Holder) convertView.getTag();
		}

		hold.mItemTitle.setText(bean.getTitle());
		hold.mItemTime.setText(bean.getCreatedate());
		hold.mItemDesc.setText(bean.getMessage());

		/**
		 * 1 认证通知2 报名成功3 联谊活动即将开始 4 预约确认 5 现场约确认 6 商家已确认 7 加入初遇点 8 现场约失败 9 现场约成功
		 * 10 预约成功 11 退款成功 12 订单取消 13 消费码待确认
		 */
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = null;
				int tmp = Integer.valueOf(bean.getTypenum());
				switch (tmp) {
				case 1:
				case 2:
				case 3:
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
				case 11:
				case 12:
				case 13:
					intent = new Intent(mContext, AuthMessageActivity.class);
					break;
				case 4:
				case 5:
					intent = new Intent(mContext,
							AuthMessageAndSureActivity.class);
					break;
				}

				if (intent != null) {
					intent.putExtra(AuthMessageActivity.DATA_KEY, bean);
					mContext.startActivity(intent);
				}

			}
		});

		return convertView;
	}

	class Holder {
		TextView mItemTitle;
		TextView mItemTime;
		TextView mItemDesc;
	}

}
