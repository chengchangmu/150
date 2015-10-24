package com.scsy150.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.scsy150.R;

public class ToastUtil {
	private static Toast mToast = null;
	public static DisplayImageOptions options() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_launcher)
				// 设置正在加载图片
				// .showImageOnLoading(R.drawable.ic_stub) //1.8.7新增
				.showImageForEmptyUri(R.drawable.ic_launcher)
				// 地址错误
				.showImageOnFail(R.drawable.ic_launcher)
				// 设置加载失败图片
				.cacheInMemory(true).cacheOnDisc(true)
				.displayer(new RoundedBitmapDisplayer(100)) // 设置图片角度,0为方形，360为圆角
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 拉伸图片
				.build();
		return options;
	}
	public static void showToast(Context context, String text, int duration) {
		try {
			if (mToast == null) {
				mToast = Toast.makeText(context, text, duration);
			} else {
				mToast.setText(text);
				mToast.setDuration(duration);
			}
			mToast.show();
		} catch (Exception e) {

		}
	}

	public static void showToast(Context context, int resId, int duration) {
		if (mToast == null) {
			mToast = Toast.makeText(context, resId, duration);
		} else {
			mToast.setText(resId);
			mToast.setDuration(duration);
		}
		mToast.show();
	}

	public static void cancel() {
		if (mToast != null) {
			mToast.cancel();
		}
	}

	public static void showView(Context ctx, View view, int duration,
			int gravity) {
		if (mToast == null) {
			mToast = new Toast(ctx);
		}
		mToast.setView(view);
		mToast.setGravity(gravity, 0, 0);
		mToast.setDuration(duration);
		mToast.show();
		mToast = null;
	}

	public static void showCenterToast(Context ctx, CharSequence res,
			int duration) {
		if (mToast == null) {
			mToast = Toast.makeText(ctx, res, duration);
		}
		mToast.setText(res);
		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.setDuration(duration);
		mToast.show();
		mToast = null;
	}

	public static void showCustomViewToast(Context ctx, int resId, String text,
			int duration) {
		View v = LayoutInflater.from(ctx).inflate(R.layout.custom_toast, null,
				false);
		ImageView iv = (ImageView) v.findViewById(R.id.toast_img);
		iv.setImageDrawable(ResourcesUtil.getDrawable(resId));
		TextView tv = (TextView) v.findViewById(R.id.toast_content);
		tv.setText(text);
		showView(ctx, v, Toast.LENGTH_LONG, Gravity.CENTER);
	}
}
