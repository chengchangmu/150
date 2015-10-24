package com.scsy150.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.scsy150.R;

/*
 * Copyright (C) 2015 四川硅谷科技重庆分公司
 * 版权所有
 *
 * 功能描述：move from 2.4 trunk,通用dialog的list adapter
 * 作者：硅谷科技
 * 创建时间：2015-08-24
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */

public class DialogListAdapter extends BaseAdapter {
	private String[] txtList;
	private int[] imageList;
	private LayoutInflater mInflater;
	private boolean mIsAlignLeft;
	private YBWDialog mDialog;

	public DialogListAdapter(Context context, YBWDialog dialog, String[] txts,
			int[] images, boolean isAlignLeft) {
		txtList = txts;
		imageList = images;
		mInflater = LayoutInflater.from(context);
		mIsAlignLeft = isAlignLeft;
		mDialog = dialog;
	}

	@Override
	public int getCount() {
		return txtList.length;
	}

	@Override
	public Object getItem(int position) {
		return txtList[position];
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// cancel();
		// listItemClickListener.onItemClick(position, listTexts[position]);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder hold;
		if (convertView == null) {
			if (mIsAlignLeft) {
				convertView = mInflater
						.inflate(R.layout.dialog_list_item, null);
			} else {
				convertView = mInflater.inflate(
						R.layout.dialog_list_item_align_center, null);
			}
			hold = new Holder();
			hold.leftImage = (ImageView) convertView
					.findViewById(R.id.dialog_item_image);
			hold.rightText = (TextView) convertView
					.findViewById(R.id.dialog_item_text);
			if (!mIsAlignLeft) {
				hold.rightText.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mDialog.onItemClick(null, null, position, 0);
					}

				});
			}
			convertView.setTag(hold);
		} else {
			hold = (Holder) convertView.getTag();
		}
		if (hold.leftImage != null) {
			if (imageList != null && imageList.length != 0) {
				hold.leftImage.setImageResource(imageList[position]);
				hold.leftImage.setVisibility(View.VISIBLE);
			} else {
				hold.leftImage.setVisibility(View.GONE);
			}
		}
		hold.rightText.setText(txtList[position]);
		return convertView;
	}

	class Holder {
		ImageView leftImage;
		TextView rightText;
	}
}
