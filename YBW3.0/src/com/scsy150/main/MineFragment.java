package com.scsy150.main;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.scsy150.R;
import com.scsy150.base.BaseBean;
import com.scsy150.base.BaseFragment;
import com.scsy150.common.ImageViewActivity;
import com.scsy150.consts.MzApi;
import com.scsy150.consts.SystemConsts;
import com.scsy150.dialog.Pop;
import com.scsy150.mine.UserOptionAdater;
import com.scsy150.mine.activity.AllContactsActivity;
import com.scsy150.mine.activity.AuthActivity;
import com.scsy150.mine.activity.FirstPointActivity;
import com.scsy150.mine.activity.InfoActivity;
import com.scsy150.mine.activity.IngActivity;
import com.scsy150.mine.activity.OrderListActivity;
import com.scsy150.mine.activity.SettingActivity;
import com.scsy150.mine.activity.WalletActivity;
import com.scsy150.util.BitmapUtil;
import com.scsy150.util.LogUtil;
import com.scsy150.util.ResourcesUtil;
import com.scsy150.util.SharedPreferencesUtil;
import com.scsy150.util.ToastUtil;
import com.scsy150.util.view.CircleImageView;
import com.scsy150.util.view.OnClick;
import com.scsy150.util.view.ViewInject;
import com.scsy150.util.view.ViewUtils;
import com.scsy150.volley.net.UpLoadAvatar;

@SuppressLint("NewApi")
public class MineFragment extends BaseFragment implements OnClickListener {

	@ViewInject(R.id.user_option)
	private GridView mGridView;

	@ViewInject(R.id.bitmap)
	private FrameLayout mBitmapFL;

	@ViewInject(R.id.head_img)
	private CircleImageView mHead;

	private String[] name = { ResourcesUtil.getString(R.string.user_option1),
			ResourcesUtil.getString(R.string.user_option2),
			ResourcesUtil.getString(R.string.user_option3),
			ResourcesUtil.getString(R.string.user_option4),
			ResourcesUtil.getString(R.string.user_option5),
			ResourcesUtil.getString(R.string.user_option6),
			ResourcesUtil.getString(R.string.user_option7),
			ResourcesUtil.getString(R.string.user_option8),
			ResourcesUtil.getString(R.string.user_option9) };

	private int[] icon = { R.drawable.user_option1, R.drawable.user_option2,
			R.drawable.user_option3, R.drawable.user_option4,
			R.drawable.user_option5, R.drawable.user_option6,
			R.drawable.user_option7, R.drawable.user_option8,
			R.drawable.user_option9 };

	private UserOptionAdater mAdapter;

	private String[] txtsArray = {
			ResourcesUtil.getString(R.string.mine_head_look),
			ResourcesUtil.getString(R.string.mine_head_select),
			ResourcesUtil.getString(R.string.mine_head_dopick) };

	// 为弹出窗口实现监听类
	private OnClickListener mPopClick = new OnClickListener() {

		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
			/**
			 * 查看大图
			 */
			case R.id.btn_look:
				Intent intent = new Intent(mActivity, ImageViewActivity.class);
				intent.putExtra(ImageViewActivity.IMAGE_PATH_KEY, mPathList);
				intent.putExtra(ImageViewActivity.IMAGE_POSITION_KEY, 0);
				startActivity(intent);
				LogUtil.d(TAG, "查看大图------");
				break;
			/**
			 * 选择认证照片
			 */
			case R.id.btn_select_pic:
				LogUtil.d(TAG, "选择认证照片------");
				break;
			/**
			 * 拍照
			 */
			case R.id.btn_take_photo:
				camera();
				LogUtil.d(TAG, "拍照------");
				break;
			default:
				break;
			}

		}

	};
	// 自定义的弹出框类
	private Pop menuWindow;

	private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
	private static final int PHOTO_REQUEST_CUT = 3;// 结果
	/* 头像名称 */
	private static final String PHOTO_FILE_NAME = "150_head.jpg";
	private File tempFile;
	private UpLoadAvatar ula;
	private String head_url;
	private Bitmap bitmap;

	private ArrayList<String> mPathList;

	@Override
	public void addView() {
		View view = View
				.inflate(mActivity, R.layout.layout_fragment_mine, null);
		mContainer.addView(view);
		ViewUtils.inject(this, view);
		mPathList = new ArrayList<String>();

		SharedPreferencesUtil spUtil = new SharedPreferencesUtil(mActivity);
		head_url = MzApi.IMAGE_DOWNLOAD
				+ spUtil.getString(SystemConsts.USER_HEADIMG,
						SystemConsts.USER_HEADIMG);
		mPathList.add(head_url);
		ImageLoader.getInstance().displayImage(head_url, mHead);

		init();
	}

	private void init() {
		ula = new UpLoadAvatar(mActivity);

		mAdapter = new UserOptionAdater(mActivity, name, icon);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Intent intent = null;
				switch (arg2) {
				case 0:
					intent = new Intent(getActivity(), WalletActivity.class);
					break;
				case 1:
					intent = new Intent(getActivity(), AuthActivity.class);
					break;
				case 2:

					intent = new Intent(getActivity(), IngActivity.class);
					break;
				case 3:
					intent = new Intent(getActivity(), OrderListActivity.class);
					break;
				case 4:
					ToastUtil.showCenterToast(mActivity, "正在加班加点开发中",
							Toast.LENGTH_SHORT);
					break;
				case 5:
					ToastUtil.showCenterToast(mActivity, "正在加班加点开发中",
							Toast.LENGTH_SHORT);
					break;
				case 6:
					intent = new Intent(getActivity(),
							AllContactsActivity.class);
					break;
				case 7:
					intent = new Intent(getActivity(), FirstPointActivity.class);
					break;
				case 8:

					intent = new Intent(getActivity(), InfoActivity.class);

					break;
				default:

					break;
				}
				if (intent != null) {
					startActivity(intent);
				}

			}
		});

	}

	@OnClick({ R.id.head_img, R.id.setting })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_img: //
			// 实例化SelectPicPopupWindow
			menuWindow = new Pop(mActivity, txtsArray, mPopClick);
			// 显示窗口
			menuWindow.showAtLocation(v, Gravity.CENTER, 0, 0); // 设置layout在PopupWindow中显示的位置
			break;
		case R.id.setting:
			Intent intent = new Intent(getActivity(), SettingActivity.class);
			startActivity(intent);
			break;
		default:

			break;
		}
	}

	@Override
	public void fragmentClick(View v) {
		// TODO Auto-generated method stub

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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PHOTO_REQUEST_CAMERA) {
			if (BitmapUtil.hasSdcard()) {
				tempFile = new File(Environment.getExternalStorageDirectory(),
						PHOTO_FILE_NAME);
				crop(Uri.fromFile(tempFile));
			} else {
				Toast.makeText(mActivity, "未找到存储卡，无法存储照片！", 0).show();
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

			ToastUtil.showCenterToast(mActivity, "头像上传中", Toast.LENGTH_SHORT);
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
				ToastUtil.showCenterToast(mActivity, "上传成功!",
						Toast.LENGTH_SHORT);
			} else {
				ToastUtil.showCenterToast(mActivity, "上传失败!",
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
