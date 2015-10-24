/**
 * Copyright (C) 2015 四川硅谷科技重庆分公司
 * 版权所有
 *
 * 功能描述：
 * 作者：硅谷科技
 * 创建时间：2015-08-31
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */
package com.scsy150.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.scsy150.R;


public class UserSelectAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private int[] drawables;
	private String[] texts;
	
	public UserSelectAdapter(Context context, int[] drawable, String[] text){
		mInflater = LayoutInflater.from(context);
		drawables = drawable;
		texts = text;
	}
	
	@Override
	public int getCount() {
		return drawables.length;
	}

	@Override
	public Object getItem(int position) {
		return texts[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if(convertView == null){
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.layout_user_select, null);
			holder.image = (ImageView) convertView.findViewById(R.id.select_image);
			holder.text = (TextView) convertView.findViewById(R.id.select_text);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		if(holder != null){
			holder.image.setImageResource(drawables[position]);
			holder.text.setText(texts[position]);
		}
		return convertView;
	}
	
	class Holder {
		public ImageView image;
		public TextView text;
	}
}
