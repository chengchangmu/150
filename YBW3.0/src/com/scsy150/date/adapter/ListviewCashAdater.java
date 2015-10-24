package com.scsy150.date.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.scsy150.R;
import com.scsy150.adapter.ViewHolder;

public abstract class ListviewCashAdater<T> extends BaseAdapter {
	Context mContext;
	List<T> mList;
	LayoutInflater inflater;

	public ListviewCashAdater(Context context, List<T> list) {
		this.mContext = context;
		this.mList = list;
		inflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public T getItem(int pos) {
		return mList.get(pos);
	}

	@Override
	public long getItemId(int postion) {
		return postion;
	}

	@Override
	public View getView(int postion, View converView, ViewGroup parent) {
		ViewHolder holder = ViewHolder.get(mContext, converView, parent,
				R.layout.item_cash_youhui, postion);
		convert(holder, getItem(postion));
		return holder.getConvertView();
	};

	public abstract void convert(ViewHolder holder, T t);

}
