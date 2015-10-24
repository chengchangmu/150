package com.scsy150.meet.page.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scsy150.R;
import com.scsy150.base.BaseBean;
import com.scsy150.consts.MzApi;
import com.scsy150.consts.SystemConsts;
import com.scsy150.meet.activity.MeetDetailActivity;
import com.scsy150.meet.page.BasePage;
import com.scsy150.meet.page.adapter.OrderListAdapter;
import com.scsy150.mine.bean.OrderBean;
import com.scsy150.util.LogUtil;
import com.scsy150.util.ResourcesUtil;
import com.scsy150.util.ToastUtil;
import com.scsy150.util.view.ViewInject;
import com.scsy150.util.view.ViewUtils;
import com.scsy150.volley.net.RequestAndLoadCookies;

/**
 * 页签封装
 * 
 * @author K
 * 
 */
public class OrderAllPage extends BasePage {

	private int mOrderStatus = 4;

	public String TAG = getClass().getSimpleName();

	private ArrayList<OrderBean> mList;// 0：待付款，1：已付款，2：完成，3:取消,4:全部
	private int[] status = { 4, 0, 3, 1, 2 };

	public OrderAllPage(Activity activity, int oderStatus) {
		super(activity);
		mOrderStatus = status[oderStatus];

		initData();
	}

	@ViewInject(R.id.lv_common_list)
	private ListView mLvContainer;

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.page_order_list, null);
		ViewUtils.inject(this, view);
		if (mQueue == null) {
			mQueue = Volley.newRequestQueue(mActivity);
		}
		mLvContainer.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(mActivity, MeetDetailActivity.class);
				mActivity.startActivity(intent);
				mActivity.finish();
			}
		});
		return view;
	}

	/**
	 * 0：待付款，1：已付款，2：完成，3:取消,4:全部
	 * 
	 * @param OrderStatus
	 */
	private void getNetOrder() {
		RequestAndLoadCookies stringRequest = new RequestAndLoadCookies(
				mActivity, Method.POST, MzApi.LOGIN_REG,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String str) {
						LogUtil.d(TAG, "----------" + str);
						Gson gson = new Gson();
						BaseBean item = gson.fromJson(str, BaseBean.class);
						if (item.getIsSuccess() == SystemConsts.RESPONSE_SUCCESS) {
							JSONObject jo;
							try {
								jo = new JSONObject(str);
								Type listType = new TypeToken<ArrayList<OrderBean>>() {
								}.getType();
								mList = gson.fromJson(jo.getString("Result"),
										listType);
								if (mList != null && mList.size() > 0) {
									setAdapter(mList);
								}

							} catch (JSONException e) {
								e.printStackTrace();
							}

						} else {

							ToastUtil.showCenterToast(mActivity,
									item.getMessage() + "", Toast.LENGTH_SHORT);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						ToastUtil.showCenterToast(mActivity, ResourcesUtil
								.getString(R.string.base_load_failed_des),
								Toast.LENGTH_SHORT);
					}

				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();

				map.put("M", "Get_User_Order_List");
				map.put("ORDERSTATUS", mOrderStatus + "");
				map.put("BEGINPAGE", 0 + "");
				map.put("EVERPAGE", "15");

				return map;

			}

		};
		mQueue.add(stringRequest);
	}

	@Override
	public void initData() {
		getNetOrder();
	}

	private void setAdapter(ArrayList<OrderBean> list) {
		mLvContainer.setAdapter(new OrderListAdapter(mActivity, list));
	}

	@Override
	public void pageClick(View v) {

	}
}
