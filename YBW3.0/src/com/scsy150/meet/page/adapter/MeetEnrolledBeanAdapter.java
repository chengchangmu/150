package com.scsy150.meet.page.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.scsy150.R;
import com.scsy150.adapter.BasicAdapter;

public abstract class MeetEnrolledBeanAdapter<T> extends BasicAdapter<T> {

	public MeetEnrolledBeanAdapter(Context context, ArrayList<T> list) {
		super(context, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = View.inflate(context, R.layout.item_circle, null);
		}
		HomeHolder holder = HomeHolder.getHolder(convertView);
		
		//设置数据
		T t = (T) list.get(position);
		bindDataToView(t, holder);
		return convertView;
	}
	
	
	public abstract void bindDataToView(T t, HomeHolder holder);


	public static class HomeHolder{
		public ImageView civ_enrolled_member_meet;
		
		public HomeHolder(View convertView){
			civ_enrolled_member_meet = (ImageView) convertView.findViewById(R.id.civ_enrolled_member_meet);
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
