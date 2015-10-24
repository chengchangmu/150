package com.scsy150.meet.page.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.scsy150.R;
import com.scsy150.consts.MzApi;
import com.scsy150.mine.activity.OrderDetailsActivity;
import com.scsy150.mine.bean.OrderBean;
import com.scsy150.util.ResourcesUtil;
import com.scsy150.util.ToastUtil;

/**
 * 普通数据适配
 */
public class OrderListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<OrderBean> list;

	public OrderListAdapter(Context mContext, ArrayList<OrderBean> list) {
		this.list = list;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return list.size();
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
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.item_order, null);
		}
		OrderViewHolder holder = OrderViewHolder.getHolder(convertView);
		final OrderBean bean = list.get(position);

		holder.tvOrderName.setText(bean.getMerName());
		holder.tvOrderState.setText(returnStatus(bean.getStatus()));
		holder.tvOrderRealPayment.setText(String.format(mContext.getResources()
				.getString(R.string.order_money), String.valueOf(bean
				.getCashAmount())));

		ImageLoader.getInstance().displayImage(
				MzApi.IMAGE_DOWNLOAD + bean.getFImage(), holder.ivOrderImg);
		holder.tvOrderTitle.setText(bean.getAciName());

		holder.btnOrderEvaluation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ToastUtil.showCenterToast(mContext, "1111", Toast.LENGTH_SHORT);
			}
		});

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, OrderDetailsActivity.class);

				intent.putExtra(OrderDetailsActivity.ORDER_DETAILS_DATA, bean);
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}

	public String returnStatus(int status) {
		String str = "";
		/**
		 * 订单状态 0：待付款，1：已付款，2：完成，3:取消
		 */
		switch (status) {
		case 0:
			str = ResourcesUtil.getString(R.string.alipay_status_pay);
			break;
		case 1:
			str = ResourcesUtil.getString(R.string.alipay_status_ed);
			break;
		case 2:
			str = ResourcesUtil.getString(R.string.alipay_status_ok);
			break;
		case 3:
			str = ResourcesUtil.getString(R.string.alipay_status_cancel);
			break;
		}
		return str;
	}
}
