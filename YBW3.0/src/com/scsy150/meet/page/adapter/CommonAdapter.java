package com.scsy150.meet.page.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.scsy150.R;
import com.scsy150.adapter.BasicAdapter;
import com.scsy150.consts.MzApi;
import com.scsy150.meet.bean.MeetInfoBean;
import com.scsy150.util.ResourcesUtil;

public class CommonAdapter extends BasicAdapter<MeetInfoBean> {

	private Context mContext;

	public CommonAdapter(Context context, ArrayList<MeetInfoBean> list) {
		super(context, list);
		mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View
					.inflate(context, R.layout.item_meet_layout, null);
		}
		HomeHolder holder = HomeHolder.getHolder(convertView);

		// 设置数据
		MeetInfoBean meetInfo = list.get(position);
		int distance = (int) meetInfo.getDistance();
		if (distance > 1000) {
			holder.tvDistance.setText(String.format(mContext.getResources()
					.getString(R.string.kilometer), distance / 1000));
		} else {
			holder.tvDistance.setText(String.format(mContext.getResources()
					.getString(R.string.kilometer), distance));
		}
		holder.tvNumber.setText(String.format(mContext.getResources()
				.getString(R.string.member_number), meetInfo.getFqty()));
		if (!TextUtils.isEmpty(meetInfo.getBeginDate())) {
			holder.tvDate.setText(meetInfo.getBeginDate().substring(5, 8));
		}
		holder.tvActivityTitle.setText(meetInfo.getAcName());
		ImageLoader.getInstance().displayImage(
				MzApi.IMAGE_DOWNLOAD + meetInfo.getLogoImage(),
				holder.ivActivityPhotos);
		String tagString = meetInfo.getAcTable();
		if (tagString.contains(ResourcesUtil.getString(R.string.more_male))) {
			holder.tv_handsome.setText(R.string.more_male);
			holder.tv_handsome.setVisibility(View.VISIBLE);
		}
		if (tagString.contains(ResourcesUtil
				.getString(R.string.more_female))) {
			holder.tv_handsome.setText(R.string.more_male);
			holder.tv_handsome.setVisibility(View.VISIBLE);
		}
		if (tagString.contains(ResourcesUtil
				.getString(R.string.lower_price)))
			holder.tv_belle.setVisibility(View.VISIBLE);
		if (tagString.contains(ResourcesUtil.getString(R.string.weekend)))
			holder.tv_weekend.setVisibility(View.VISIBLE);
		if (tagString.contains(ResourcesUtil
				.getString(R.string.many_people_chose)))
			holder.tv_slow.setVisibility(View.VISIBLE);
		if (tagString.contains(ResourcesUtil
				.getString(R.string.multiply_img)))
			holder.tv_photo.setVisibility(View.VISIBLE);
		
		return convertView;
	}

	static class HomeHolder {
		ImageView ivActivityPhotos;
		TextView tvActivityTitle, tvDistance, tvNumber, tvDate;
		TextView tv_handsome, tv_belle, tv_weekend, tv_slow, tv_photo;

		public HomeHolder(View convertView) {
			ivActivityPhotos = (ImageView) convertView
					.findViewById(R.id.iv_activityPhotos);
			tvActivityTitle = (TextView) convertView
					.findViewById(R.id.tv_activity_title);
			tvDistance = (TextView) convertView.findViewById(R.id.tv_distance);
			tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
			tvDate = (TextView) convertView.findViewById(R.id.tv_date);

			tv_handsome = (TextView) convertView.findViewById(R.id.tv_handsome);
			tv_belle = (TextView) convertView.findViewById(R.id.tv_belle);
			tv_weekend = (TextView) convertView.findViewById(R.id.tv_weekend);
			tv_slow = (TextView) convertView.findViewById(R.id.tv_slow);
			tv_photo = (TextView) convertView.findViewById(R.id.tv_photo);
		}

		public static HomeHolder getHolder(View convertView) {
			HomeHolder holder = (HomeHolder) convertView.getTag();
			if (holder == null) {
				holder = new HomeHolder(convertView);
				convertView.setTag(holder);
			}
			return holder;
		}
	}
}