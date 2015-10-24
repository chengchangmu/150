package com.scsy150.chat;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout.LayoutParams;
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
import com.scsy150.base.BaseFragment;
import com.scsy150.chat.adpater.SystemMessageAdapter;
import com.scsy150.chat.bean.SystemMessageBean;
import com.scsy150.consts.MzApi;
import com.scsy150.consts.SystemConsts;
import com.scsy150.util.LogUtil;
import com.scsy150.util.ResourcesUtil;
import com.scsy150.util.ToastUtil;
import com.scsy150.util.view.OnClick;
import com.scsy150.util.view.ViewInject;
import com.scsy150.util.view.ViewUtils;
import com.scsy150.volley.net.RequestAndLoadCookies;
import com.scsy150.widget.pulltorefresh.PullToRefreshListView;

public class SystemFragment extends BaseFragment implements OnClickListener {

	@ViewInject(R.id.pull_to_refresh_list)
	private PullToRefreshListView mPullToRefreshListView;

	private ArrayList<SystemMessageBean> mList;
	private SystemMessageAdapter mAdapter;
	private String mResponseStr;

	@Override
	public void addView() {
		View view = View.inflate(getActivity(),
				R.layout.layout_fragment_system, null);
		DisplayMetrics dm = getResources().getDisplayMetrics();
		LayoutParams params = new LayoutParams(dm.widthPixels, dm.heightPixels);
		view.setLayoutParams(params);
		mContainer.addView(view);
		ViewUtils.inject(this, view);
		if (mQueue == null) {
			mQueue = Volley.newRequestQueue(mActivity);
		}
		getNetData();
	}

	private void init() {
		if (mList != null && mList.size() > 0) {
			mAdapter = new SystemMessageAdapter(mActivity, mList);
			mPullToRefreshListView.setAdapter(mAdapter);
		}
	}

	@OnClick({ R.id.left_image })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_image:

			break;
		default:
			break;
		}
	}

	public void getNetData() {
		RequestAndLoadCookies stringRequest = new RequestAndLoadCookies(
				mActivity, Method.POST, MzApi.LOGIN_REG,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String str) {
						LogUtil.d(TAG, str);
						mResponseStr = str;
						Gson gson = new Gson();
						BaseBean item = gson.fromJson(str, BaseBean.class);
						if (item.getIsSuccess() == SystemConsts.RESPONSE_SUCCESS) {
							JSONObject jo;
							try {
								jo = new JSONObject(str);
								Type listType = new TypeToken<ArrayList<SystemMessageBean>>() {
								}.getType();
								mList = gson.fromJson(jo.getString("Result"),
										listType);
								init();

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

				map.put("M", "Get_Me_Msg_List");

				return map;

			}

		};
		mQueue.add(stringRequest);

	}

	@Override
	public void fragmentClick(View v) {
		// TODO Auto-generated method stub

	}

}
