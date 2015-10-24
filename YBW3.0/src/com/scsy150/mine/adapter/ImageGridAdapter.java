package com.scsy150.mine.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.scsy150.R;
import com.scsy150.consts.MzApi;
import com.scsy150.mine.bean.PicBean;

public class ImageGridAdapter extends BaseAdapter {

	Activity act;
	List<PicBean> dataList;

	public ImageGridAdapter(Activity act, List<PicBean> list) {
		this.act = act;
		dataList = list;
	}

	public int getCount() {
		return dataList == null ? 1 : dataList.size() + 1;// 返回gridview数目加1

	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	class Holder {
		private ImageView iv;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final Holder holder;
		// 原先的正常数据的显示，操作等
		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(act, R.layout.item_image_grid, null);
			holder.iv = (ImageView) convertView.findViewById(R.id.image);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		if (dataList != null && position < dataList.size()) {

			final PicBean item = dataList.get(position);

			holder.iv.setTag(item.getPhotoid());

			ImageLoader.getInstance().displayImage(
					MzApi.IMAGE_DOWNLOAD + item.getPhotoimage(), holder.iv);

		} else {
			// 手动增加的这个Item的显示和功能实现
			holder.iv.setBackgroundResource(R.drawable.gd);
		}

		return convertView;

	}
}
