package com.scsy150.account;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.google.gson.Gson;
import com.scsy150.R;
import com.scsy150.base.BaseActivity;
import com.scsy150.base.BaseBean;
import com.scsy150.consts.MzApi;
import com.scsy150.consts.SystemConsts;
import com.scsy150.util.BitmapUtil;
import com.scsy150.util.DevicesUtil;
import com.scsy150.util.MD5Util;
import com.scsy150.util.ResourcesUtil;
import com.scsy150.util.ToastUtil;
import com.scsy150.util.view.CircleImageView;
import com.scsy150.util.view.OnClick;
import com.scsy150.util.view.ViewInject;
import com.scsy150.util.view.ViewUtils;
import com.scsy150.volley.net.RequestAndSaveCookies;
import com.scsy150.volley.net.UpLoadAvatar;
import com.scsy150.widget.datapicker.MzDataPicker;
import com.scsy150.widget.datapicker.MzDataPicker.OnCustomDataPickerListener;
import com.scsy150.widget.datapicker.MzDataPicker.OnDayPickerListener;

/*
 * Copyright (C) 2015 四川硅谷科技重庆分公司
 * 版权所有
 *
 * 功能描述：注册时添加个人信息页面
 * 作者：硅谷科技
 * 创建时间：2015-08-24
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */

public class RegistStepTwoActivity extends BaseActivity {

	@ViewInject(R.id.nameInput)
	private EditText mNameValue;

	@ViewInject(R.id.sexValue)
	private TextView mSexValue;

	@ViewInject(R.id.birthdayValue)
	private TextView mBirthdayValue;

	@ViewInject(R.id.regist_next_step)
	private TextView mNextStep;

	@ViewInject(R.id.ct_nor)
	private CheckedTextView mCtNor;

	@ViewInject(R.id.ct_student)
	private CheckedTextView mStudent;

	@ViewInject(R.id.head_reg)
	private CircleImageView mHead;

	private Bitmap bitmap;

	private String mPhone;
	private String mPwd;

	private String name = "";
	private int sex = 0;
	private int type = 2;
	private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
	private static final int PHOTO_REQUEST_CUT = 3;// 结果
	/* 头像名称 */
	private static final String PHOTO_FILE_NAME = "150_head.jpg";
	private File tempFile;
	private UpLoadAvatar ula;
	private String headimg_url;

	@Override
	public void addViewIntoContent() {
		View view = View.inflate(this,
				R.layout.activity_regist_add_personal_info, null);
		mBaseContent.addView(view);
		ViewUtils.inject(this);
		setLeftVisibility(View.VISIBLE);
		setTitle(R.string.regist_add_personal_info);
		setRightVisibility(View.GONE);

		ula = new UpLoadAvatar(this);
		mPhone = getIntent().getStringExtra(RegistStepOneActivity.PHONE_KEY);
		mPwd = getIntent().getStringExtra(RegistStepOneActivity.PHONE_PWD);

		// 点击状态后变更相反，如选中变为未选中，未选中的变为选中
		mCtNor.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mStudent.isChecked()) {
					mStudent.setChecked(false);
				}
				mCtNor.toggle();
			}
		});

		mStudent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mCtNor.isChecked()) {
					mCtNor.setChecked(false);
				}
				mStudent.toggle();
			}
		});
	}

	@OnClick({ R.id.head_reg, R.id.left_image, R.id.sexValue,
			R.id.birthdayValue, R.id.regist_next_step })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_reg:
			camera();
			break;
		case R.id.left_image:
			goToMainPage();
			break;
		case R.id.sexValue:
			showSexPicker();
			break;
		case R.id.birthdayValue:
			showBirthdayPicker();
			break;
		case R.id.regist_next_step:
			try {
				submitInfo();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

	private void goToMainPage() {
		RegistStepTwoActivity.this.finish();
	}

	private void showSexPicker() {
		MzDataPicker.showCustomDataPicker(this,
				ResourcesUtil.getStringArray(R.array.regist_sex), 0, null, -1,
				"", new OnCustomDataPickerListener() {
					@Override
					public void onResult(int leftIndex, String leftValue,
							int rightIndex, String rightValue) {
						mSexValue.setText(leftValue);
					}
				});
	}

	/**
	 * 环信用户注册
	 * 
	 * @param username
	 * @param pwd
	 */
	public void regChat() {
		new Thread(new Runnable() {
			public void run() {
				try {
					// 调用sdk注册方法
					EMChatManager.getInstance().createAccountOnServer(mPhone,
							mPwd);
				} catch (final EaseMobException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	private void showBirthdayPicker() {
		// 使用类iphone风格的时间选择控件
		final Calendar startCalendar = Calendar.getInstance();
		MzDataPicker.showDayPicker(this, startCalendar.get(Calendar.YEAR),
				startCalendar.get(Calendar.MONTH),
				startCalendar.get(Calendar.DAY_OF_MONTH),
				new OnDayPickerListener() {
					@Override
					public void onResult(int year, int monthOfYear,
							int dayOfMonth) {
						String choce_time = year + "-";
						if (monthOfYear < 10) {
							choce_time += "0" + monthOfYear;
						} else {
							choce_time += monthOfYear;
						}
						choce_time += "-";
						if (dayOfMonth < 10) {
							choce_time += "0" + dayOfMonth;
						} else {
							choce_time += dayOfMonth;
						}
						mBirthdayValue.setText(choce_time);
					}

				});
	}

	public void submitInfo() throws IOException {
		name = mNameValue.getText().toString().trim();
		if (mSexValue.getText().equals(ResourcesUtil.getString(R.string.gg))) {
			sex = 1;
		}
		if (mCtNor.isChecked()) {
			type = 1;
		}

		if (TextUtils.isEmpty(name)) {
			showMsg(getString(R.string.regist_name_is_null), R.string.tip);
		} else if (!mCtNor.isChecked() && !mStudent.isChecked()) {
			showMsg(getString(R.string.select_user), R.string.tip);
		} else {
			// TODO CCM
			Login_new();
		}
	}

	// 登录请求
	private void Login_new() {
		RequestAndSaveCookies stringRequest = new RequestAndSaveCookies(
				RegistStepTwoActivity.this, Method.POST, MzApi.LOGIN_REG,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String str) {
						Gson gson = new Gson();
						BaseBean item = gson.fromJson(str, BaseBean.class);
						if (item.getIsSuccess() == SystemConsts.RESPONSE_SUCCESS) {
							regChat();
							// 注册成功，跳转审核状态界面
							Intent intent = new Intent(
									RegistStepTwoActivity.this,
									RegistStatusActivity.class);

							intent.putExtra(RegistStatusActivity.STATUS,
									SystemConsts.AUTH_ING);
							startActivity(intent);
							RegistStepTwoActivity.this.finish();
						} else {

							removeProgressDialog();
							ToastUtil.showCenterToast(
									RegistStepTwoActivity.this,
									item.getMessage() + "", Toast.LENGTH_SHORT);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						removeProgressDialog();
						ToastUtil
								.showCenterToast(
										RegistStepTwoActivity.this,
										ResourcesUtil
												.getString(R.string.base_load_failed_des),
										Toast.LENGTH_SHORT);
					}

				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();

				map.put("M", "signup");
				map.put("PHONENUM", mPhone + "");
				map.put("PASSWORD", mPwd);
				map.put("NICKNAME", name);
				map.put("BIRTHDAY", mBirthdayValue.getText().toString());
				map.put("PHONEID",
						DevicesUtil.getIMEI(RegistStepTwoActivity.this));
				map.put("DEVICE_TYPE",
						DevicesUtil.getPhoneName()
								+ DevicesUtil.getSystemVersion());
				map.put("LATITUDE", SystemConsts.Latitude + "");
				map.put("LONGITUDE", SystemConsts.Longitude + "");
				map.put("PROVINCE", SystemConsts.CURRENT_PROVINCE);
				map.put("CITY", SystemConsts.CURRENT_CITY);
				map.put("COUNTY", SystemConsts.CURRENT_DISTRICT);
				map.put("ADDRESS", SystemConsts.ADDRESS);
				map.put("SEX", sex + "");//
				map.put("USERTYPE", type + "");
				map.put("HEADIMG", headimg_url);

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

	/*
	 * 从相机获取
	 */
	public void camera() {
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		// 判断存储卡是否可以用，可用进行存储
		if (BitmapUtil.hasSdcard()) {
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File(Environment
							.getExternalStorageDirectory(), PHOTO_FILE_NAME)));
		}
		startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
	}

	/**
	 * Bitmap对象保存味图片文件
	 * 
	 * @param bitmap
	 */
	public File saveBitmapFile(Bitmap bitmap) {
		File file = new File(Environment.getExternalStorageDirectory(),
				PHOTO_FILE_NAME);// 将要保存图片的路径
		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PHOTO_REQUEST_CAMERA) {
			if (BitmapUtil.hasSdcard()) {
				tempFile = new File(Environment.getExternalStorageDirectory(),
						PHOTO_FILE_NAME);
				crop(Uri.fromFile(tempFile));
			} else {
				Toast.makeText(RegistStepTwoActivity.this, "未找到存储卡，无法存储照片！", 0)
						.show();
			}

		} else if (requestCode == PHOTO_REQUEST_CUT) {
			try {
				bitmap = data.getParcelableExtra("data");
				this.mHead.setImageBitmap(bitmap);
				tempFile = saveBitmapFile(bitmap);
				// 开始上传头像
				new AvatarUploadAsync().execute(tempFile);

				// 删除图片
				// boolean delete = tempFile.delete();
				// System.out.println("delete = " + delete);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	class AvatarUploadAsync extends AsyncTask<File, Integer, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			ToastUtil.showCenterToast(RegistStepTwoActivity.this, "头像上传中",
					Toast.LENGTH_SHORT);
		}

		@Override
		protected String doInBackground(File... params) {
			String json = ula.uploadImg(params[0]);
			if (TextUtils.isEmpty(json)) {
				return "上传失败";
			} else {
				Gson gson = new Gson();
				BaseBean item = gson.fromJson(json, BaseBean.class);
				if (item.getIsSuccess() == SystemConsts.RESPONSE_SUCCESS) {
					headimg_url = (String) item.getResult();
					return item.getMessage();
				}
			}
			return "0";
		}

		@Override
		protected void onPostExecute(String result) {

			// super.onPostExecute(result);
			if (!"0".equals(result)) {
				ToastUtil.showCenterToast(RegistStepTwoActivity.this, "上传成功!",
						Toast.LENGTH_SHORT);
			} else {
				ToastUtil.showCenterToast(RegistStepTwoActivity.this, "上传失败!",
						Toast.LENGTH_SHORT);
			}
			// 删除图片
			// if (imgFile.exists()) {
			// imgFile.delete();
			// }
		}
	}

	/**
	 * 剪切图片
	 * 
	 * @param uri
	 */
	private void crop(Uri uri) {
		// 裁剪图片意图
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// 裁剪框的比例，1：1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 裁剪后输出图片的尺寸大小
		intent.putExtra("outputX", 480);
		intent.putExtra("outputY", 480);
		// 图片格式
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("noFaceDetection", true);// 取消人脸识别
		intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}
}
