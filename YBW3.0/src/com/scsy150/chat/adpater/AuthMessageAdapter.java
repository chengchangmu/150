package com.scsy150.chat.adpater;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.scsy150.R;
import com.scsy150.chat.bean.AuthMessageBean;

/*
 * Copyright (C) 2014 四川硅谷科技重庆分公司
 * 版权所有
 *
 * 功能描述：认证相关list的adapter
 * 作者：硅谷科技
 * 创建时间：2015-09-31
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */
public class AuthMessageAdapter extends BaseAdapter {

	private ArrayList<AuthMessageBean> list;
	private Context mContext;

	public AuthMessageAdapter(Context context, ArrayList<AuthMessageBean> list) {
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
		final AuthMessageBean bean = list.get(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(
					R.layout.item_message_content_layout, null);
			hold = new Holder();

			hold.mItemTime = (TextView) convertView
					.findViewById(R.id.item_mc_date);
			hold.mItemDesc = (TextView) convertView
					.findViewById(R.id.item_mc_desc);
			convertView.setTag(hold);
		} else {
			hold = (Holder) convertView.getTag();
		}

		hold.mItemTime.setText(bean.getCreatedate());
		hold.mItemDesc.setText(bean.getMessage());

		return convertView;
	}

	class Holder {
		TextView mItemTime;
		TextView mItemDesc;
	}

}
