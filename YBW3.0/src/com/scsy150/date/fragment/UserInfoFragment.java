package com.scsy150.date.fragment;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.sdp.Media;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
import com.scsy150.account.RegistStepTwoActivity;
import com.scsy150.adapter.ViewHolder;
import com.scsy150.base.BaseBean;
import com.scsy150.base.YBWApplication;
import com.scsy150.consts.MzApi;
import com.scsy150.consts.SystemConsts;
import com.scsy150.date.activity.PhotoActivity;
import com.scsy150.date.adapter.BitMapCache;
import com.scsy150.date.adapter.EstimateAdater;
import com.scsy150.date.adapter.HzPicAdater;
import com.scsy150.date.bean.DateBean;
import com.scsy150.date.bean.NowUserBean;
import com.scsy150.util.MD5Util;
import com.scsy150.util.ResourcesUtil;
import com.scsy150.util.ToastUtil;
import com.scsy150.util.view.HorizontalListView;
import com.scsy150.volley.net.RequestAndLoadCookies;

public class UserInfoFragment extends Fragment {

	ImageView iv_sound;
	ImageView iv_goPhoto;
	ImageView iv_background;
	ImageLoader imageLoader;
	DateBean dateBean;
	NowUserBean nBean;
	GridView mGView;
	HorizontalListView hz_img;
	LinkedList<NowUserBean> list;
	RequestQueue mQueue;
	TextView tv_talk;
	TextView tv_isFriend;
	String returnData;
	private ArrayList<NowUserBean> mListData1;
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

						JSONArray ja = jo1.getJSONArray("data3");
						Type listType = new TypeToken<LinkedList<NowUserBean>>() {
						}.getType();
						list = gson.fromJson(ja.toString(), listType);
						setAdater(list);
						// 拿到data1的数据
						JSONArray ja1 = jo1.getJSONArray("data1");
						for (int i = 0; i < ja1.length(); i++) {
							JSONObject job = new JSONObject(ja1.getString(0));
							nBean = new NowUserBean();
							nBean.setTalk(job.optString("Talk"));
							nBean.setIsFriend(job.optInt("IsFriend"));
							nBean.setUserId(job.getInt("UserId"));
						}
						// 签名
						tv_talk.setText(nBean.getTalk());
						// 好友
						if (nBean.getIsFriend() == 1) {
							tv_isFriend.setVisibility(View.VISIBLE);
						}
						// data2
						JSONArray js = jo1.getJSONArray("data2");
						Type listType1 = new TypeToken<LinkedList<NowUserBean>>() {
						}.getType();
						LinkedList<NowUserBean> list = gson.fromJson(
								js.toString(), listType1);
						setHzPhAdater(list);

					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else {
					ToastUtil.showCenterToast(getActivity(), item.getMessage()
							+ "", Toast.LENGTH_SHORT);
				}

				break;

			default:
				break;
			}
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.userinfo_layout, null);
		YBWApplication.getInstance().beginLocation();

		mGView = (GridView) view.findViewById(R.id.gv_Estimate);
		hz_img = (HorizontalListView) view.findViewById(R.id.hz_photoImg);
		iv_background = (ImageView) view.findViewById(R.id.iv_backgroud);
		// volley
		mQueue = Volley.newRequestQueue(getActivity());
		imageLoader = new ImageLoader(mQueue, new BitMapCache());
		iv_goPhoto = (ImageView) view.findViewById(R.id.iv_goPhoto);
		// 姓名
		TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
		tv_name.setText(dateBean.getNickName());
		// 性别
		ImageView iv_gender = (ImageView) view.findViewById(R.id.iv_gender);
		if (dateBean.getSex() == 1) {
			iv_gender.setImageResource(R.drawable.male);
		} else {
			iv_gender.setImageResource(R.drawable.female);
		}
		// 年龄
		TextView tv_age = (TextView) view.findViewById(R.id.tv_age);
		tv_age.setText(dateBean.getGroupBrithday() + "后");
		// 距离
		TextView tv_dis = (TextView) view.findViewById(R.id.tv_dis);
		tv_dis.setText("" + dateBean.getDistance());
		getJsonStr();
		// 签名
		tv_talk = (TextView) view.findViewById(R.id.tv_talk);
		// 好友
		tv_isFriend = (TextView) view.findViewById(R.id.tv_isFriend);
		// json

		// 相册点击查看
		iv_goPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity(), PhotoActivity.class);
				intent.putExtra("photoData", nBean);
				startActivity(intent);

			}
		});
		// 读取声音
		view.findViewById(R.id.iv_sound).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// Uri uri = Uri.parse(path);
						// MediaPlayer mp = new MediaPlayer().create(
						// getActivity(), uri);
						// mp.start();

					}
				});

		// 头像volley

		ImageLoader.ImageListener imHead = ImageLoader.getImageListener(
				iv_background, R.drawable.back, R.drawable.yhy);
		imageLoader.get(MzApi.IMG_URL + dateBean.getHeadImg(), imHead);
		Log.i("RT", "userInfo:" + dateBean.getUserId());
		return view;
	}

	/**
	 * api
	 */
	private void getJsonStr() {
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
				map.put("YOUID", dateBean.getUserId() + "");
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

	/**
	 * 获取data
	 * 
	 * @param dBean
	 */
	public void getdateBean(DateBean dBean) {
		this.dateBean = dBean;
	}

	/**
	 * 根号7数据
	 * 
	 * @param list
	 */

	private void setAdater(LinkedList<NowUserBean> list) {
		mGView.setAdapter(new EstimateAdater<NowUserBean>(getActivity(), list) {

			@Override
			public void convert(ViewHolder holder, NowUserBean t) {
				holder.setText(R.id.tv_Estimate, t.getEstimate());
				TextView tv_es = holder.getView(R.id.tv_Estimate);
				int cou = holder.getmPosition();
				if (cou % 6 == 0 || cou == 0) {
					tv_es.setBackgroundColor(getResources().getColor(
							R.color.pink));
				}
				if (cou % 6 == 1 || cou == 1) {
					tv_es.setBackgroundColor(getResources().getColor(
							R.color.green));

				}
				if (cou % 6 == 2 || cou == 2) {
					tv_es.setBackgroundColor(getResources().getColor(
							R.color.zise));

				}
				if (cou % 6 == 3 || cou == 3) {
					tv_es.setBackgroundColor(getResources().getColor(
							R.color.lanse));
				}
				if (cou % 6 == 4 || cou == 4) {
					tv_es.setBackgroundColor(getResources().getColor(
							R.color.orage));
				}
				if (cou % 6 == 5 || cou == 5) {
					tv_es.setBackgroundColor(getResources().getColor(
							R.color.theme_color));

				}
			}
		});

	}

	// TODO
	private void setHzPhAdater(LinkedList<NowUserBean> list) {
		hz_img.setAdapter(new HzPicAdater<NowUserBean>(getActivity(), list) {

			@Override
			public void convert(ViewHolder holder, NowUserBean t) {
				ImageLoader.ImageListener imHead = ImageLoader
						.getImageListener(
								(ImageView) holder.getView(R.id.iv_hz_phimg),
								R.drawable.back, R.drawable.yhy);
				imageLoader.get(MzApi.IMG_URL + t.getPhotoImage(), imHead);

			}
		});

	};

}
