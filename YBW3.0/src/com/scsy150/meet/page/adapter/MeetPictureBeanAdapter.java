package com.scsy150.meet.page.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.scsy150.R;
import com.scsy150.adapter.BasicAdapter;
import com.scsy150.widget.RoundedImageView;

public abstract class MeetPictureBeanAdapter<T> extends BasicAdapter<T> {

	public MeetPictureBeanAdapter(Context context, ArrayList<T> list) {
		super(context, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = View.inflate(context, R.layout.item_meet_picture, null);
		}
		HomeHolder holder = HomeHolder.getHolder(convertView);
		
		//设置数据
		T t = (T) list.get(position);
		bindDataToView(t, holder);
		return convertView;
	}
	
	
	public abstract void bindDataToView(T t, HomeHolder holder);


	public static class HomeHolder{
		public RoundedImageView picture;
		
		public HomeHolder(View convertView){
			picture = (RoundedImageView) convertView.findViewById(R.id.iv_meet_picture);
		}
		
		public static HomeHolder getHolder(View convertView){
			HomeHolder holder = (HomeHolder) convertView.getTag();
			if(holder==null){
				holder = new HomeHolder(convertView);
				convertView.setTag(holder);
			}
			return holder;
		}
	}

}
