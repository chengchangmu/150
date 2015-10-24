package com.scsy150.mine.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scsy150.R;
import com.scsy150.base.BaseActivity;
import com.scsy150.base.BaseBean;
import com.scsy150.consts.MzApi;
import com.scsy150.consts.SystemConsts;
import com.scsy150.mine.adapter.ImageGridAdapter;
import com.scsy150.mine.bean.PicBean;
import com.scsy150.util.BitmapUtil;
import com.scsy150.util.LogUtil;
import com.scsy150.util.ResourcesUtil;
import com.scsy150.util.ToastUtil;
import com.scsy150.util.view.OnClick;
import com.scsy150.util.view.ViewInject;
import com.scsy150.util.view.ViewUtils;
import com.scsy150.volley.net.RequestAndLoadCookies;
import com.scsy150.volley.net.UpLoadAvatar;

/*
 * Copyright (C) 2015 四川硅谷科技重庆分公司
 * 版权所有
 *
 * 功能描述：我的之认证界面
 * 作者：硅谷科技
 * 创建时间：2015-09-24
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */

public class AuthActivity extends BaseActivity implements OnClickListener {

	@ViewInject(R.id.auth_grid)
	private GridView mGrid;

	@ViewInject(R.id.auth_upload)
	private TextView mUpload;

	private ImageGridAdapter mImageGridAdapter;

	private Bitmap bitmap;

	private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
	private static final int PHOTO_REQUEST_CUT = 3;// 结果
	/* 头像名称 */
	private static final String PHOTO_FILE_NAME = "150_head.jpg";
	private File tempFile;
	private UpLoadAvatar ula;

	@Override
	public void addViewIntoContent() {
		View view = View.inflate(this, R.layout.activity_mine_auth, null);
		mBaseContent.addView(view);
		ViewUtils.inject(this);
		setLeftVisibility(View.VISIBLE);
		setRightTxt(R.string.auth_student);
		setTitle(ResourcesUtil.getString(R.string.user_option2));
		ula = new UpLoadAvatar(this);
		initData();
	}

	private void initData() {
		getPic();
	}

	@Override
	@OnClick({ R.id.left_image,R.id.auth_upload })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_image:
			finish();
			break;

		case R.id.auth_upload:
			camera();
			break;
		default:
			break;
		}
	}

	public void getPic() {
		RequestAndLoadCookies stringRequest = new RequestAndLoadCookies(
				AuthActivity.this, Method.POST, MzApi.LOGIN_REG,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String str) {
						LogUtil.d(TAG, str);
						Gson gson = new Gson();
						BaseBean item = gson.fromJson(str, BaseBean.class);
						if (item.getIsSuccess() == SystemConsts.RESPONSE_SUCCESS) {
							JSONObject jo;
							try {
								jo = new JSONObject(str);
								Type listType = new TypeToken<LinkedList<PicBean>>() {
								}.getType();
								LinkedList<PicBean> list = gson.fromJson(
										jo.getString("Result"), listType);

								mImageGridAdapter = new ImageGridAdapter(
										AuthActivity.this, list);
								mGrid.setAdapter(mImageGridAdapter);

							} catch (JSONException e) {
								e.printStackTrace();
							}

						} else {

							removeProgressDialog();

							ToastUtil.showCenterToast(AuthActivity.this,
									item.getMessage() + "", Toast.LENGTH_SHORT);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						removeProgressDialog();
						ToastUtil
								.showCenterToast(
										AuthActivity.this,
										ResourcesUtil
												.getString(R.string.base_load_failed_des),
										Toast.LENGTH_SHORT);
					}

				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();
				// Phototype 相册类型 选填：不传为全部，0普通相册 1,认证相册
				map.put("M", "GetMyPhoto");
				map.put("Phototype", "1");
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
				Toast.makeText(AuthActivity.this, "未找到存储卡，无法存储照片！", 0).show();
			}

		} else if (requestCode == PHOTO_REQUEST_CUT) {
			try {
				bitmap = data.getParcelableExtra("data");
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

			ToastUtil.showCenterToast(AuthActivity.this, "图片上传中",
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
					return item.getMessage();
				}
			}
			return "0";
		}

		@Override
		protected void onPostExecute(String result) {

			// super.onPostExecute(result);
			if (!"0".equals(result)) {
				ToastUtil.showCenterToast(AuthActivity.this, "上传成功!",
						Toast.LENGTH_SHORT);
			} else {
				ToastUtil.showCenterToast(AuthActivity.this, "上传失败!",
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
