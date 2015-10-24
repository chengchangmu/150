package com.scsy150.account;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scsy150.R;
import com.scsy150.base.BaseActivity;
import com.scsy150.base.BaseBean;
import com.scsy150.consts.MzApi;
import com.scsy150.consts.SystemConsts;
import com.scsy150.util.BitmapUtil;
import com.scsy150.util.ResourcesUtil;
import com.scsy150.util.SharedPreferencesUtil;
import com.scsy150.util.ToastUtil;
import com.scsy150.util.view.CircleImageView;
import com.scsy150.util.view.OnClick;
import com.scsy150.util.view.ViewInject;
import com.scsy150.util.view.ViewUtils;
import com.scsy150.volley.net.UpLoadAvatar;

/*
 * Copyright (C) 2015 四川硅谷科技重庆分公司
 * 版权所有
 *
 * 功能描述：注册之后的审核状态
 * 作者：硅谷科技
 * 创建时间：2015-09-7
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */

public class RegistStatusActivity extends BaseActivity implements
		OnClickListener {
	public final static String STATUS = "Status";

	@ViewInject(R.id.forget_pw)
	private TextView mDesContent;

	@ViewInject(R.id.head_status)
	private CircleImageView mHeadStatus;

	@ViewInject(R.id.re_upload)
	private TextView mReUpload;

	@ViewInject(R.id.audit_desc)
	private TextView mAuditDesc;

	@ViewInject(R.id.audit_ing)
	private TextView mAuditIng;

	@ViewInject(R.id.audit_fail)
	private TextView mAuditFail;

	private String head_url;
	private Bitmap bitmap;
	private int mStatus;

	private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
	private static final int PHOTO_REQUEST_CUT = 3;// 结果
	/* 头像名称 */
	private static final String PHOTO_FILE_NAME = "150_head.jpg";
	private File tempFile;
	private UpLoadAvatar ula;

	@Override
	public void addViewIntoContent() {
		View view = View.inflate(this, R.layout.activity_registed_status, null);
		mBaseContent.addView(view);
		ViewUtils.inject(this);
		setLeftVisibility(View.VISIBLE);
		setTitle(ResourcesUtil.getString(R.string.audit_info_title));
		ula = new UpLoadAvatar(this);
		Intent intent = getIntent();
		mStatus = intent.getIntExtra(STATUS, 0);
		SharedPreferencesUtil spUtil = new SharedPreferencesUtil(
				RegistStatusActivity.this);
		head_url = MzApi.IMAGE_DOWNLOAD
				+ spUtil.getString(SystemConsts.USER_HEADIMG,
						SystemConsts.USER_HEADIMG);
		if (mStatus == SystemConsts.AUTH_ING) {
			// ImageLoader.getInstance().displayImage(head_url, mHeadStatus);
			mReUpload.setVisibility(View.GONE);
			mAuditFail.setVisibility(View.GONE);
		}
		if (mStatus == SystemConsts.AUTH_FIAL) {
			mAuditDesc.setVisibility(View.GONE);
			mAuditIng.setVisibility(View.GONE);
		}

	}

	@Override
	@OnClick({ R.id.re_upload, R.id.left_image })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.re_upload:
			camera();
			break;
		case R.id.left_image:
			finish();
			break;
		default:
			break;
		}
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
				Toast.makeText(RegistStatusActivity.this, "未找到存储卡，无法存储照片！", 0)
						.show();
			}

		} else if (requestCode == PHOTO_REQUEST_CUT) {
			try {
				bitmap = data.getParcelableExtra("data");
				this.mHeadStatus.setImageBitmap(bitmap);
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

			ToastUtil.showCenterToast(RegistStatusActivity.this, "头像上传中",
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
				ToastUtil.showCenterToast(RegistStatusActivity.this, "上传成功!",
						Toast.LENGTH_SHORT);
				mReUpload.setVisibility(View.GONE);
				mAuditFail.setVisibility(View.GONE);
				mAuditDesc.setVisibility(View.VISIBLE);
				mAuditIng.setVisibility(View.VISIBLE);
			} else {
				ToastUtil.showCenterToast(RegistStatusActivity.this, "上传失败!",
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
