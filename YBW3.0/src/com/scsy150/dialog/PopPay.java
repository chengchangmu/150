package com.scsy150.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.scsy150.R;

public class PopPay extends PopupWindow {

	public PopPay(Context context, OnClickListener itemsOnClick,
			double AllAmount, double cash_balance) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View mMenuView = inflater.inflate(R.layout.pop_message_pay, null);
		Button confirm = (Button) mMenuView.findViewById(R.id.confirm);

		TextView pop_mp_tv_num = (TextView) mMenuView
				.findViewById(R.id.pop_mp_tv_num);
		pop_mp_tv_num.setText(String.format(
				context.getResources().getString(R.string.member_cost),
				String.valueOf(AllAmount)));

		TextView money_sale = (TextView) mMenuView
				.findViewById(R.id.money_sale);
		money_sale.setText(String.format(
				context.getResources().getString(R.string.member_cost),
				String.valueOf(AllAmount)));

		// 设置按钮监听
		confirm.setOnClickListener(itemsOnClick);
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				dismiss();
				return true;
			}
		});

	}

}
