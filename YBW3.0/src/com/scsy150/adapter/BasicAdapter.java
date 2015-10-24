package com.scsy150.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class BasicAdapter<T> extends BaseAdapter{
	protected ArrayList<T> list;
	protected Context context;
	
	public BasicAdapter(Context context,ArrayList<T> list){
		this.list = list;
		this.context = context;
		
	}
	
	@Override
	public int getCount() {
		return list==null?0:list.size();
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
		return null;
	}

}
