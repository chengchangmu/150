package com.scsy150.date.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.AuthFailureError;
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
import com.scsy150.date.adapter.ListviewCashAdater;
import com.scsy150.date.bean.DateBean;
import com.scsy150.date.util.TimePopupWindow;
import com.scsy150.date.util.TimePopupWindow.OnTimeSelectListener;
import com.scsy150.date.util.TimePopupWindow.Type;
import com.scsy150.util.ResourcesUtil;
import com.scsy150.util.ToastUtil;
import com.scsy150.volley.net.RequestAndLoadCookies;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class DateHeadActivity extends Activity implements OnClickListener {
	PopupWindow ppList;
	LinkedList<DateBean> list;
	PopupWindow ppCoupons;
	LinearLayout ll_kongbai;
	TextView tv_date_text;
	TextView tv_userCouPons;
	TextView tv_firsTime;
	TextView tv_twoTime;
	ListView mListview;
	TextView tv_threeTime;
	TextView tv_fourTime;
	TextView tv_bName;
	LayoutInflater inflater;
	CheckedTextView ck_y;
	CheckedTextView ck_Conpons;
	TimePopupWindow pwTime;
	TimePopupWindow pwTwo;
	TimePopupWindow pwThree;
	TimePopupWindow pwFour;
	Calendar calendar;
	DateBean dBean;
	RequestQueue mqQueue;
	String returnData;

	int year;
	int month;
	int day;
	int hours;
	int minute;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Gson gson = new Gson();
				BaseBean item = gson.fromJson(returnData, BaseBean.class);
				if (item.getIsSuccess() == SystemConsts.RESPONSE_SUCCESS) {
					try {
						JSONObject jo = new JSONObject(returnData);
						java.lang.reflect.Type listType = new TypeToken<LinkedList<DateBean>>() {
						}.getType();
						list = gson.fromJson(jo.getString("Result"), listType);
						setadater(list);

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
				break;

			default:
				break;
			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.date_head_layout);

		initview();

		initOneTime();
		initTwoTime();
		initThreeTime();
		initFourTime();

		ck_y = (CheckedTextView) findViewById(R.id.ct_yh);
		ck_Conpons = (CheckedTextView) findViewById(R.id.ck_cash);
		inflater = LayoutInflater.from(DateHeadActivity.this);
		ll_kongbai.setOnClickListener(this);
		ck_y.setOnClickListener(this);
		ck_Conpons.setOnClickListener(this);

		// 更换商家
		findViewById(R.id.tv_chagePlace).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(DateHeadActivity.this,
								DatingSitesActivity.class);
						startActivity(intent);

					}
				});
		// 商家详情
		findViewById(R.id.tv_bs_details).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent intent = new Intent(DateHeadActivity.this,
								MerchantDetailsActivity.class);
						intent.putExtra("bsBean", dBean);
						startActivity(intent);
					}
				});
		// 说明
		findViewById(R.id.iv_dateText).setOnClickListener(
				new OnClickListener() {

					@SuppressLint("NewApi")
					@Override
					public void onClick(View v) {

						View vw = inflater.inflate(R.layout.popu_list_layout,
								null);
						// 说明
						vw.findViewById(R.id.tv_one_text).setOnClickListener(
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										tv_date_text
												.setText(R.string.date_text);
										ppList.dismiss();

									}
								});

						//
						vw.findViewById(R.id.tv_two_text).setOnClickListener(
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										tv_date_text
												.setText(R.string.date_text_go);
										ppList.dismiss();

									}
								});
						//
						vw.findViewById(R.id.tv_three_text).setOnClickListener(
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										tv_date_text
												.setText(R.string.date_text_drink);
										ppList.dismiss();

									}
								});

						if (ppList == null) {
							ppList = new PopupWindow(vw,
									LayoutParams.WRAP_CONTENT,
									LayoutParams.WRAP_CONTENT, true);
						}
						ppList.setOutsideTouchable(true);

						ppList.setBackgroundDrawable(getResources()
								.getDrawable(R.drawable.jx));

						if (ppList.isShowing()) {
							ppList.dismiss();
						} else {
							ppList.setWidth(500);
							ppList.showAsDropDown(v, -404, 0);
						}

					}
				});

		// 优惠券说明
		findViewById(R.id.iv_youhui).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				View viewCoupons = inflater.inflate(
						R.layout.popu_list_coupons_layout, null);
				mListview = (ListView) viewCoupons
						.findViewById(R.id.listview_cash);

				RequestAndLoadCookies cookies = new RequestAndLoadCookies(
						DateHeadActivity.this, Method.POST, MzApi.LOGIN_REG,
						new Listener<String>() {

							@Override
							public void onResponse(String str) {
								returnData = str;
								handler.sendEmptyMessage(0);

							}
						}, new ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError arg0) {

								ToastUtil.showCenterToast(
										DateHeadActivity.this,
										ResourcesUtil
												.getString(R.string.base_load_failed_des),
										Toast.LENGTH_SHORT);
							}
						}) {
					@Override
					protected Map<String, String> getParams()
							throws AuthFailureError {
						HashMap<String, String> map = new HashMap<String, String>();

						map.put("M", "GetYouHui");

						return map;
					}
				};
				mqQueue.add(cookies);

				// TODO

				if (ppCoupons == null) {
					ppCoupons = new PopupWindow(viewCoupons,
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT, true);
				}
				ppCoupons.setOutsideTouchable(true);

				ppCoupons.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.jx));

				if (ppCoupons.isShowing()) {
					ppCoupons.dismiss();
				} else {
					ppCoupons.setWidth(500);
					ppCoupons.showAsDropDown(v, -404, 0);
				}
				// 设置优惠券点击事件
				mListview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						dBean = (DateBean) parent.getItemAtPosition(position);
						tv_userCouPons.setText(dBean.getCouponost() + "元优惠券");
						ppCoupons.dismiss();
					}
				});
			}

		});

	}

	// 初始化
	private void initview() {
		mqQueue = Volley.newRequestQueue(DateHeadActivity.this);
		ll_kongbai = (LinearLayout) findViewById(R.id.ll_kongbai);
		tv_date_text = (TextView) findViewById(R.id.tv_date_text);
		tv_userCouPons = (TextView) findViewById(R.id.tv_userCoupons);
		tv_firsTime = (TextView) findViewById(R.id.tv_first_time);
		tv_twoTime = (TextView) findViewById(R.id.tv_two_time);
		tv_threeTime = (TextView) findViewById(R.id.tv_three_time);
		tv_fourTime = (TextView) findViewById(R.id.tv_four_time);

		// 拿到传过来商家信息的值
		Intent intent = getIntent();
		dBean = (DateBean) intent.getExtras().getSerializable("bsData");
		// 商家信息,商家名
		tv_bName = (TextView) findViewById(R.id.tv_bName);
		tv_bName.setText(dBean.getMhname());
		// 套餐信息
		TextView tv_scost = (TextView) findViewById(R.id.tv_scost);
		tv_scost.setText(dBean.getScost() + "");
		// TODO
		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		hours = calendar.get(Calendar.HOUR_OF_DAY);
		minute = calendar.get(Calendar.MINUTE);
	}

	// 第四个被选中的时间
	private void initFourTime() {
		pwFour = new TimePopupWindow(this, Type.HOURS_MINS);
		pwFour.setOnTimeSelectListener(new OnTimeSelectListener() {

			@Override
			public void onTimeSelect(Date date) {
				tv_fourTime.setText(getHourTime(date));
			}
		});
		tv_fourTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pwFour.showAtLocation(tv_fourTime, Gravity.BOTTOM, 0, 0);
			}
		});

	}

	// 第三个被选中的时间
	private void initThreeTime() {
		pwThree = new TimePopupWindow(this, Type.HOURS_MINS);
		pwThree.setOnTimeSelectListener(new OnTimeSelectListener() {

			@Override
			public void onTimeSelect(Date date) {
				tv_threeTime.setText(getHourTime(date));
			}
		});
		tv_threeTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pwThree.showAtLocation(tv_threeTime, Gravity.BOTTOM, 0, 0);
			}
		});

	}

	// 第二个被选中的时间
	private void initTwoTime() {
		pwTwo = new TimePopupWindow(this, Type.HOURS_MINS);
		pwTwo.setOnTimeSelectListener(new OnTimeSelectListener() {

			@Override
			public void onTimeSelect(Date date) {
				tv_twoTime.setText(getHourTime(date));
			}
		});
		//
		tv_twoTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pwTwo.showAtLocation(tv_twoTime, Gravity.BOTTOM, 0, 0,
						new Date());

			}
		});

	}

	// 第一个时间
	private void initOneTime() {
		pwTime = new TimePopupWindow(this, Type.YEAR_MONTH_DAY);
		// 时间选择
		pwTime.setOnTimeSelectListener(new OnTimeSelectListener() {

			@Override
			public void onTimeSelect(Date date) {
				tv_firsTime.setText(getTime(date));

			}

		});
		tv_firsTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				pwTime.setRange(year, month, day, hours, minute);
				pwTime.showAtLocation(tv_firsTime, Gravity.BOTTOM, 0, 0,
						new Date());

			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_kongbai:
			finish();

			break;
		case R.id.ct_yh:
			if (ck_y.isChecked()) {
				ck_y.toggle();
			} else {
				ck_y.setChecked(true);
			}
			break;
		case R.id.ck_cash:
			if (ck_Conpons.isChecked()) {
				ck_Conpons.toggle();
			} else {
				ck_Conpons.setChecked(true);
			}
			break;

		default:
			break;
		}

	}

	// 时间选择器当前时间
	public static String getTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("MM-dd");
		return format.format(date);
	}

	// 时间选择小时和分
	public static String getHourTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		return format.format(date);
	}

	private void setadater(LinkedList<DateBean> list) {

		mListview.setAdapter(new ListviewCashAdater<DateBean>(
				DateHeadActivity.this, list) {

			@Override
			public void convert(ViewHolder holder, DateBean t) {
				holder.setText(R.id.tv_item_cash, t.getCouponost() + "元优惠券");
			}

		});

	};

}
