package com.scsy150.date.fragment;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
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
import com.scsy150.date.adapter.PhotoAdapter;
import com.scsy150.date.bean.NowUserBean;
import com.scsy150.date.bean.PhotoBean;
import com.scsy150.util.MD5Util;
import com.scsy150.util.ResourcesUtil;
import com.scsy150.util.ToastUtil;
import com.scsy150.volley.net.RequestAndLoadCookies;

public class PhotoFragment extends Fragment {
	GridView mGridView;
	ImageLoader imageLoader;
	LinkedList<NowUserBean> list;
	NowUserBean nbBean;
	RequestQueue mQueue;
	String returnData;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Gson gson = new Gson();
				BaseBean item = gson.fromJson(returnData, BaseBean.class);
				if (item.getIsSuccess() == SystemConsts.RESPONSE_SUCCESS) {
					JSONObject jo;

					try {
						jo = new JSONObject(returnData);
						JSONObject jo1 = jo.getJSONObject("Result");
						JSONArray ja = jo1.getJSONArray("data2");
						Type listType = new TypeToken<LinkedList<NowUserBean>>() {
						}.getType();
						list = gson.fromJson(ja.toString(), listType);
						setadapter(list);
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

				break;

			default:
				break;
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.photo_fragment_layout, null);
		// volley
		mQueue = Volley.newRequestQueue(getActivity());
		imageLoader = new ImageLoader(mQueue, new BitMapCache());
		mGridView = (GridView) view.findViewById(R.id.gv_photo);
		getJsonData();

		return view;
	}

	private void getJsonData() {
		RequestAndLoadCookies reqCookies = new RequestAndLoadCookies(
				getActivity(), com.android.volley.Request.Method.POST,
				MzApi.LOGIN_REG, new Listener<String>() {

					@Override
					public void onResponse(String str) {
						returnData = str;
						handler.sendEmptyMessage(0);

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
				map.put("M", "About_You");
				map.put("YOUID", "6500");
				map.put("LONGITUDE", SystemConsts.Longitude + "");
				map.put("LATITUDE", SystemConsts.Latitude + "");
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
		mQueue.add(reqCookies);

	}

	private void setadapter(LinkedList<NowUserBean> list) {
		mGridView
				.setAdapter(new PhotoAdapter<NowUserBean>(getActivity(), list) {

					@Override
					public void convert(ViewHolder holder, NowUserBean t) {
						// 头像volley
						ImageLoader.ImageListener imHead = ImageLoader
								.getImageListener((ImageView) holder
										.getView(R.id.iv_photo),
										R.drawable.back, R.drawable.yhy);
						imageLoader.get(MzApi.IMG_URL+t.getPhotoImage(), imHead);

					}

				});
	}

	public void getBean(NowUserBean nbBean) {
		this.nbBean = nbBean;
	};
}
