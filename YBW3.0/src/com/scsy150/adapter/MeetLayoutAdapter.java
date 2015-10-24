package com.scsy150.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scsy150.R;
import com.scsy150.meet.bean.MeetInfoBean;

public class MeetLayoutAdapter extends BasicAdapter<MeetInfoBean> {

	public MeetLayoutAdapter(Context context, ArrayList<MeetInfoBean> list) {
		super(context, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = View.inflate(context, R.layout.item_meet_layout, null);
		}
		HomeHolder holder = HomeHolder.getHolder(convertView);
		
		//设置数据
		MeetInfoBean meetInfo = list.get(position);
		holder.tvActivityTitle.setText(meetInfo.getAcTable());
		holder.tvDistance.setText(meetInfo.getDistance() + "");
		holder.tvNumber.setText(meetInfo.getAcName());
		holder.tvDate.setText(meetInfo.getBeginDate());
		holder.tvActivityTitle.setText(meetInfo.getAcName());
		/*ImageLoader.
		holder.ivActivityPhotos.setIMA(meetInfo);*/
		
		return convertView;
	}
	
	
	static class HomeHolder{
		ImageView ivActivityPhotos;
		TextView tvActivityTitle,tvDistance,tvNumber,tvDate;
		
		public HomeHolder(View convertView){
			ivActivityPhotos = (ImageView) convertView.findViewById(R.id.iv_activityPhotos);
			tvActivityTitle = (TextView) convertView.findViewById(R.id.tv_activity_title);
			tvDistance = (TextView) convertView.findViewById(R.id.tv_distance);
			tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
			tvDate = (TextView) convertView.findViewById(R.id.tv_date);
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
