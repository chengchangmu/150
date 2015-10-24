package com.scsy150.mine.activity;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scsy150.R;
import com.scsy150.base.BaseActivity;
import com.scsy150.base.BaseBean;
import com.scsy150.consts.MzApi;
import com.scsy150.consts.SystemConsts;
import com.scsy150.mine.bean.IngAppointmentBean;
import com.scsy150.mine.bean.IngMeetBean;
import com.scsy150.mine.bean.OrderBean;
import com.scsy150.mine.bean.OrderDetailsBean;
import com.scsy150.util.LogUtil;
import com.scsy150.util.MD5Util;
import com.scsy150.util.ResourcesUtil;
import com.scsy150.util.ToastUtil;
import com.scsy150.util.view.OnClick;
import com.scsy150.util.view.ViewInject;
import com.scsy150.util.view.ViewUtils;
import com.scsy150.volley.net.RequestAndLoadCookies;

/*
 * Copyright (C) 2015 四川硅谷科技重庆分公司
 * 版权所有
 *
 * 功能描述：订单详情
 * 作者：硅谷科技
 * 创建时间：2015-09-24
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */

public class OrderDetailsActivity extends BaseActivity implements
		OnClickListener {

	public static String ORDER_DETAILS_DATA = "order_details_data";

	public static String ING_LIST_ADAPTER = "inglistadapter";
	private IngAppointmentBean mIngAppointmentBean;

	public static String ING_MEET_LIST_ADAPTER = "ingmeetlistadapter";
	private IngMeetBean mIngMeetBean;

	@ViewInject(R.id.order_evaluation)
	private Button mEvaluation;

	@ViewInject(R.id.order_details_img)
	private ImageView mDetailsImg;

	@ViewInject(R.id.order_details_num)
	private TextView mDetailsNum;
	@ViewInject(R.id.order_details_status)
	private TextView mDetailsStatus;
	@ViewInject(R.id.order_details_type)
	private TextView mDetailsType;
	@ViewInject(R.id.order_details_date)
	private TextView mDetailsDate;
	@ViewInject(R.id.order_details_mer_name)
	private TextView mDetailsMerName;
	@ViewInject(R.id.order_details_name)
	private TextView mDetailsName;
	@ViewInject(R.id.order_real_payment)
	private TextView mDetailsRealPay;
	@ViewInject(R.id.order_details_pay_mode)
	private TextView mDetailsPayMode;
	@ViewInject(R.id.order_details_refund_date)
	private TextView mDetailsRefundDate;
	@ViewInject(R.id.order_details_total)
	private TextView mDetailsTotal;
	@ViewInject(R.id.order_details_value)
	private TextView mDetailsValue;
	@ViewInject(R.id.order_details_rapy_ratio)
	private TextView mDetailsRapyRatio;
	@ViewInject(R.id.order_details_refund)
	private TextView mDetailsRefund;

	private String mResponseStr;
	private OrderBean mBean;

	@Override
	public void addViewIntoContent() {
		View view = View.inflate(this, R.layout.order_details, null);
		mBaseContent.addView(view);
		ViewUtils.inject(this);
		setLeftVisibility(View.VISIBLE);
		setTitle(ResourcesUtil.getString(R.string.order_details_title));
		if (mQueue == null) {
			mQueue = Volley.newRequestQueue(mCurrentActivity);
		}
		handler = new Handler(new Callback() {

			@Override
			public boolean handleMessage(Message msg) {
				Gson gson = new Gson();
				BaseBean item = gson.fromJson(mResponseStr, BaseBean.class);
				if (item.getIsSuccess() == SystemConsts.RESPONSE_SUCCESS) {
					JSONObject jo;
					try {
						jo = new JSONObject(mResponseStr);
						Type listType = new TypeToken<ArrayList<OrderDetailsBean>>() {
						}.getType();
						ArrayList<OrderDetailsBean> list = gson.fromJson(
								jo.getString("Result"), listType);
						if (list != null && list.size() > 0) {
							setData(list.get(0));
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else {

					ToastUtil.showCenterToast(OrderDetailsActivity.this,
							item.getMessage() + "", Toast.LENGTH_SHORT);
				}
				return false;
			}
		});
		initData();
	}

	protected void setData(OrderDetailsBean orderDetailsBean) {

		mDetailsNum.setText(orderDetailsBean.getOrderid() + "");
		mDetailsStatus.setText(orderDetailsBean.getStatus() + "");
		mDetailsType.setText(orderDetailsBean.getOrderType() + "");
		mDetailsDate.setText(orderDetailsBean.getCreateDate());
		mDetailsMerName.setText(orderDetailsBean.getMhname());
		mDetailsName.setText(orderDetailsBean.getASName());
		mDetailsRealPay.setText(orderDetailsBean.getOrderType() + "");
		mDetailsPayMode.setText(orderDetailsBean.getOrderType() + "");
		mDetailsRefundDate.setText(orderDetailsBean.getCancelDate());
		mDetailsTotal.setText(orderDetailsBean.getAllAmount() + "");
		mDetailsValue.setText(orderDetailsBean.getCouponAmount() + "");
		mDetailsRapyRatio.setText(orderDetailsBean.getPayProportion() + "");
		mDetailsRefund.setText(orderDetailsBean.getPayProportion() + "");

	}

	private void initData() {
		try {
			mBean = (OrderBean) getIntent().getSerializableExtra(
					ORDER_DETAILS_DATA);
		} catch (Exception e) {
		}
		try {
			mIngAppointmentBean = (IngAppointmentBean) getIntent()
					.getSerializableExtra(ING_LIST_ADAPTER);
		} catch (Exception e) {
		}

		try {
			mIngMeetBean = (IngMeetBean) getIntent().getSerializableExtra(
					ING_MEET_LIST_ADAPTER);
		} catch (Exception e) {
		}

		getNetOrder();
	}

	@Override
	@OnClick({ R.id.left_image })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_image:
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * 
	 * @param OrderStatus
	 */
	private void getNetOrder() {
		RequestAndLoadCookies stringRequest = new RequestAndLoadCookies(
				OrderDetailsActivity.this, Method.POST, MzApi.LOGIN_REG,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String str) {
						LogUtil.d(TAG, "----------" + str);
						mResponseStr = str;
						handler.sendEmptyMessage(0);

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						ToastUtil
								.showCenterToast(
										OrderDetailsActivity.this,
										ResourcesUtil
												.getString(R.string.base_load_failed_des),
										Toast.LENGTH_SHORT);
					}

				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();

				map.put("M", "Get_User_Order");
				if (mIngAppointmentBean != null) {
					map.put("ORDERID", mIngAppointmentBean.getOrderid() + "");
					map.put("ORDERTYPE", mIngAppointmentBean.getOrdertype()
							+ "");
				}

				if (mIngMeetBean != null) {
					map.put("ORDERID", mIngMeetBean.getOrderid() + "");
					map.put("ORDERTYPE", mIngMeetBean.getOrdertype() + "");
				}

				if (mBean != null) {
					map.put("ORDERID", mBean.getOrderid() + "");
					map.put("ORDERTYPE", mBean.getOrderType() + "");
				}
				String sign = "";
				try {
					sign = MD5Util.getSignature(map, SystemConsts.SIGN_KEY);
					map.put("sign", sign);
				} catch (IOException e) {
					e.printStackTrace();
				}

				return map;

			}

		};
		mQueue.add(stringRequest);
	}

}
