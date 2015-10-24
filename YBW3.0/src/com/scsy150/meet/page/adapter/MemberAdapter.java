package com.scsy150.meet.page.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.scsy150.R;
import com.scsy150.adapter.BasicAdapter;
import com.scsy150.consts.MzApi;
import com.scsy150.meet.bean.MeetEnrolledMemberBean;

/**
 * 报名成员适配
 */
public class MemberAdapter<T> extends BasicAdapter<T> {

	private Context mContext;
	private ArrayList<T> mList;

	@SuppressWarnings("unchecked")
	public MemberAdapter(Context context, ArrayList<T> list) {
		super(context, list);
		this.mList = list;
		this.mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.item_member_layout,
					null);
		}
		MemberViewHolder holder = MemberViewHolder.getHolder(convertView);
		
		MeetEnrolledMemberBean t = (MeetEnrolledMemberBean)mList.get(position);
		ImageLoader.getInstance().displayImage(MzApi.IMG_URL + t.getHeadImg(), holder.ivHeaderImg);
		holder.tvNickName.setText(t.getNickName());
		holder.tvAgeRange.setText(String.format(mContext.getResources().getString(R.string.member_age_range), t.getAgePassage()));
		
		int sex = t.getSex();
		if(0 == sex) {
			// 女
			holder.ivHeaderImg.setImageResource(R.drawable.female);
		} else if(1 == sex) {
			// 男
			holder.ivHeaderImg.setImageResource(R.drawable.male);
		}

		return convertView;
	}

	public static class MemberViewHolder {
		private ImageView ivHeaderImg, ivSex;
		private TextView tvNickName, tvAgeRange;

		public MemberViewHolder(View convertView) {
			ivHeaderImg = (ImageView) convertView
					.findViewById(R.id.civ_head_img);
			tvNickName = (TextView) convertView.findViewById(R.id.tv_nick_name);
			tvAgeRange = (TextView) convertView.findViewById(R.id.tv_info);
			ivSex = (ImageView) convertView.findViewById(R.id.iv_sex);
		}

		public static MemberViewHolder getHolder(View convertView) {
			MemberViewHolder holder = (MemberViewHolder) convertView.getTag();
			if (holder == null) {
				holder = new MemberViewHolder(convertView);
				convertView.setTag(holder);
			}
			return holder;
		}
	}

}
