package com.scsy150.date.fragment;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request.Method;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scsy150.R;
import com.scsy150.adapter.ViewHolder;
import com.scsy150.base.BaseBean;
import com.scsy150.consts.MzApi;
import com.scsy150.consts.SystemConsts;
import com.scsy150.date.adapter.BitMapCache;
import com.scsy150.date.adapter.DatingSitesAdater;
import com.scsy150.date.bean.DateBean;
import com.scsy150.date.bean.DatingSitesBean;
import com.scsy150.util.MD5Util;
import com.scsy150.util.ResourcesUtil;
import com.scsy150.util.ToastUtil;
import com.scsy150.volley.net.RequestAndLoadCookies;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class DatingSitesFragment extends Fragment {
	ListView mListView;
	LinkedList<DateBean> mList;
	RequestQueue mQueue;
	ImageLoader imLoader;
	DateBean dBean;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dating_sites_fragment_layout,
				null);
		mListView = (ListView) view.findViewById(R.id.lv_datePlace);
		mQueue = Volley.newRequestQueue(getActivity());
		imLoader = new ImageLoader(mQueue, new BitMapCache());

		getJsonData();
		return view;
	}

	/**
	 * json数据
	 */
	private void getJsonData() {
		RequestAndLoadCookies cookies = new RequestAndLoadCookies(
				getActivity(), Method.POST, MzApi.LOGIN_REG,
				new Listener<String>() {

					@SuppressLint("NewApi")
					@Override
					public void onResponse(String str) {
						Gson gson = new Gson();
						BaseBean item = gson.fromJson(str, BaseBean.class);
						if (item.getIsSuccess() == SystemConsts.RESPONSE_SUCCESS) {
							JSONObject jo = null;
							try {
								jo = new JSONObject(str);
								JSONArray jar = new JSONArray(jo
										.getString("Result"));
								Type listType = new TypeToken<LinkedList<DateBean>>() {
								}.getType();
								mList = gson.fromJson(jar.toString(), listType);
								setAdapter(mList);

							} catch (JSONException e) {
								e.printStackTrace();
							}

						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						ToastUtil.showCenterToast(getActivity(), ResourcesUtil
								.getString(R.string.base_load_failed_des),
								Toast.LENGTH_SHORT);

					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("M", "Get_Merchant_SetMeal_List");
				map.put("PROVINCE", "重庆市");
				map.put("CITY", "");
				map.put("BEGINPAGE", "1");
				map.put("BEGINPAGE", "1");
				map.put("EVERPAGE", "10");
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
		mQueue.add(cookies);

	}

	private void setAdapter(LinkedList<DateBean> list) {
		mListView.setAdapter(new DatingSitesAdater<DateBean>(getActivity(),
				list) {

			@Override
			public void convert(ViewHolder holder, DateBean dBean) {

				holder.setText(R.id.tv_bsName, dBean.getMhname());
				holder.setText(R.id.tv_bs_type, dBean.getMerchantType());
				holder.setText(R.id.tv_bs_adress, dBean.getCounty());
				ImageLoader.ImageListener imHead = ImageLoader
						.getImageListener(
								(ImageView) holder.getView(R.id.iv_bs_url),
								R.drawable.back, R.drawable.yhy);
				imLoader.get(MzApi.IMG_URL + dBean.getMerchantImage(), imHead);

			}
		});

	}
}
