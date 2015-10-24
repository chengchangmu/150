package com.scsy150.chat.adpater;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.scsy150.R;
import com.scsy150.chat.bean.SystemMessageAndSureBean;
import com.scsy150.consts.MzApi;
import com.scsy150.dialog.PopPay;
import com.scsy150.util.ResourcesUtil;
import com.scsy150.util.view.CircleImageView;

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
public class AuthMessageAndSureAdapter extends BaseAdapter {

	// 自定义的弹出框类
	private PopPay menuWindow;

	private ArrayList<SystemMessageAndSureBean> list;
	private Context mContext;

	public AuthMessageAndSureAdapter(Context context,
			ArrayList<SystemMessageAndSureBean> list) {
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
		final SystemMessageAndSureBean bean = list.get(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(
					R.layout.item_system_appointment_layout, null);
			hold = new Holder();

			hold.item_sys_time = (TextView) convertView
					.findViewById(R.id.item_sys_time);
			hold.item_sys_mer_name = (TextView) convertView
					.findViewById(R.id.item_sys_mer_name);
			hold.item_sys_name = (TextView) convertView
					.findViewById(R.id.item_sys_name);
			hold.item_sys_total = (TextView) convertView
					.findViewById(R.id.item_sys_total);
			hold.item_sys_desc = (TextView) convertView
					.findViewById(R.id.item_sys_desc);
			hold.item_sys_img = (CircleImageView) convertView
					.findViewById(R.id.item_sys_img);
			hold.item_sys_each_name = (TextView) convertView
					.findViewById(R.id.item_sys_each_name);
			hold.item_sys_each_value = (TextView) convertView
					.findViewById(R.id.item_sys_each_value);
			hold.item_sys_rfeuse = (TextView) convertView
					.findViewById(R.id.item_sys_rfeuse);
			hold.item_sys_agree = (TextView) convertView
					.findViewById(R.id.item_sys_agree);
			hold.item_sys_operation_failure = (TextView) convertView
					.findViewById(R.id.item_sys_operation_failure);

			hold.item_sys_do = (LinearLayout) convertView
					.findViewById(R.id.item_sys_do);

			convertView.setTag(hold);
		} else {
			hold = (Holder) convertView.getTag();
		}

		hold.item_sys_time.setText(bean.getBeginDate());
		hold.item_sys_mer_name.setText(bean.getMhname());
		hold.item_sys_name.setText(bean.getSName());
		hold.item_sys_total.setText(bean.getAllAmount() + "");

		hold.item_sys_desc.setText(bean.getIntroductions());
		if (!TextUtils.isEmpty(bean.getHeadImg())) {
			ImageLoader.getInstance()
					.displayImage(MzApi.IMAGE_DOWNLOAD + bean.getHeadImg(),
							hold.item_sys_img);
		}

		hold.item_sys_each_name.setText(bean.getNickName());
		hold.item_sys_each_value.setText(bean.getGroupBrithday() + "");
		if (bean.getSex() == 0) {
			Drawable drawable = mContext.getResources().getDrawable(
					R.drawable.female);
			// / 这一步必须要做,否则不会显示.
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			hold.item_sys_each_value.setCompoundDrawables(drawable, null, null,
					null);
		} else {
			Drawable drawable = mContext.getResources().getDrawable(
					R.drawable.male);
			// / 这一步必须要做,否则不会显示.
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			hold.item_sys_each_value.setCompoundDrawables(drawable, null, null,
					null);
		}

		if (bean.getStatus() == 0) {

			hold.item_sys_rfeuse.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

				}
			});

			hold.item_sys_agree.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (bean.getAllAmount() == 0) {
					} else {
						// 实例化SelectPicPopupWindow
						menuWindow = new PopPay(mContext, mPopClick,bean.getAllAmount(),1);
						// 显示窗口
						menuWindow.showAtLocation(v, Gravity.CENTER, 0, 0); // 设置layout在PopupWindow中显示的位置

					}
				}
			});
		}
		if (bean.getStatus() == 3) {
			hold.item_sys_operation_failure.setText(ResourcesUtil
					.getString(R.string.alipay_status_pay));
			hold.item_sys_operation_failure
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

						}
					});
		}

		// 状态 0:初始 1：拒绝，2：接受，3：等待付款，4：失效

		switch (bean.getStatus()) {
		case 0:
			hold.item_sys_operation_failure.setVisibility(View.GONE);
			break;
		case 1:
			hold.item_sys_operation_failure.setText(ResourcesUtil
					.getString(R.string.rejected));
			hold.item_sys_do.setVisibility(View.GONE);
			break;
		case 2:
			hold.item_sys_operation_failure.setText(ResourcesUtil
					.getString(R.string.agreed));
			hold.item_sys_do.setVisibility(View.GONE);
			break;
		case 3:
			hold.item_sys_do.setVisibility(View.GONE);
			break;
		case 4:
			hold.item_sys_operation_failure.setText(ResourcesUtil
					.getString(R.string.alipay_status_pay));
			hold.item_sys_do.setVisibility(View.GONE);
			break;
		}

		return convertView;
	}

	class Holder {
		TextView item_sys_time;
		TextView item_sys_mer_name;
		TextView item_sys_name;
		TextView item_sys_total;
		TextView item_sys_desc;
		CircleImageView item_sys_img;
		TextView item_sys_each_name;
		TextView item_sys_each_value;
		TextView item_sys_rfeuse;
		TextView item_sys_agree;
		TextView item_sys_operation_failure;
		LinearLayout item_sys_do;
	}

	// 为弹出窗口实现监听类
	private OnClickListener mPopClick = new OnClickListener() {

		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
			/**
			 * 确定
			 */
			case R.id.confirm:
				break;
			default:
				break;
			}

		}

	};

}
