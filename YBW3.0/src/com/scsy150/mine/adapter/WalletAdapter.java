package com.scsy150.mine.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.scsy150.R;
import com.scsy150.mine.bean.WalletBean;

/*
 * Copyright (C) 2014 四川硅谷科技重庆分公司
 * 版权所有
 *
 * 功能描述：list的adapter
 * 作者：硅谷科技
 * 创建时间：2015-09-31
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */
public class WalletAdapter extends BaseAdapter {

	private ArrayList<WalletBean> list;
	private Context mContext;

	public WalletAdapter(Context context, ArrayList<WalletBean> list) {
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
		WalletBean bean = list.get(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_mine_wallet, null);
			hold = new Holder();

			hold.mItemTitle = (TextView) convertView
					.findViewById(R.id.item_title);
			hold.mItemNum = (TextView) convertView.findViewById(R.id.item_num);
			convertView.setTag(hold);
		} else {
			hold = (Holder) convertView.getTag();
		}

		hold.mItemTitle.setText((String.format(mContext.getResources()
				.getString(R.string.wallet_item_title), String.valueOf(bean
				.getCouponost()))));
		hold.mItemNum.setText((String.format(
				mContext.getResources().getString(R.string.wallet_item_num),
				String.valueOf(bean.getActivedays()))));

		return convertView;
	}

	class Holder {
		TextView mItemTitle;
		TextView mItemNum;
	}

}
