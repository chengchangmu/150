package com.scsy150.mine;

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
import com.scsy150.mine.adapter.IngListAdapter;
import com.scsy150.mine.adapter.IngMeetListAdapter;
import com.scsy150.mine.bean.IngAppointmentBean;
import com.scsy150.mine.bean.IngMeetBean;
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
public class IngPage extends BasePage {

	private int mOrderStatus;

	public String TAG = getClass().getSimpleName();

	private ArrayList<IngMeetBean> mMeetList;
	private ArrayList<IngAppointmentBean> mList;//

	public IngPage(Activity activity, int oderStatus) {
		super(activity);
		this.mOrderStatus = oderStatus;
		
		initData();
	}

	@ViewInject(R.id.lv_common_list)
	private ListView mLvContainer;

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.page_order_list, null);
		ViewUtils.inject(this, view);
		return view;
	}

	/**
	 * 
	 * 
	 * @param OrderStatus
	 */
	private void getNetOrder(final int type) {
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
								Type listType = new TypeToken<ArrayList<IngAppointmentBean>>() {
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
				if (mOrderStatus == 2) {
					map.put("M", "Get_SetMent_NowSite");
				} else if (mOrderStatus == 1) {
					map.put("M", "Get_SetMent_UnderWay");
				}

				return map;

			}

		};
		mQueue.add(stringRequest);
	}

	@Override
	public void initData() {
		if (this.mOrderStatus != 0) {
			getNetOrder(mOrderStatus);
		} else {
			getNetMeet();
		}
	}

	private void setAdapter(ArrayList<IngAppointmentBean> list) {
		mLvContainer.setAdapter(new IngListAdapter(mActivity, list));
	}

	/**
	 * 
	 */
	private void getNetMeet() {
		RequestAndLoadCookies stringRequest = new RequestAndLoadCookies(
				mActivity, Method.POST, MzApi.LOGIN_REG,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String str) {
						Gson gson = new Gson();
						BaseBean item = gson.fromJson(str, BaseBean.class);
						if (item.getIsSuccess() == SystemConsts.RESPONSE_SUCCESS) {
							JSONObject jo;
							try {
								jo = new JSONObject(str);
								Type listType = new TypeToken<ArrayList<IngMeetBean>>() {
								}.getType();
								mMeetList = gson.fromJson(
										jo.getString("Result"), listType);
								if (mMeetList != null && mMeetList.size() > 0) {
									setMeetAdapter(mMeetList);
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
				map.put("M", "Get_Activity_UnderWay");

				return map;

			}

		};
		mQueue.add(stringRequest);
	}

	protected void setMeetAdapter(ArrayList<IngMeetBean> mList2) {
		mLvContainer.setAdapter(new IngMeetListAdapter(mActivity, mList2));
	}

	@Override
	public void pageClick(View v) {

	}
}
