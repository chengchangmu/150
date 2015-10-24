package com.scsy150.util.image;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.scsy150.R;

/**
 * 图片加载工具类
 * 
 * @author K
 * 
 */
public class MyBitmapUtils {

	private NetCacheUtils mNetUtils;
	private LocalCacheUtils mLocalUtils;
	private MemCacheUtils mMemUtils;

	public MyBitmapUtils() {
		mLocalUtils = new LocalCacheUtils();
		mMemUtils = new MemCacheUtils();
		mNetUtils = new NetCacheUtils(mLocalUtils, mMemUtils);
	}

	public void display(ImageView view, String url) {
		view.setImageResource(R.drawable.advert_00);// 设置默认图片
		// 先从内存加载
		Bitmap bitmap = mMemUtils.getMemCache(url);
		if (bitmap != null) {
			view.setImageBitmap(bitmap);
			return;
		}

		// 再从本地加载
		bitmap = mLocalUtils.getBitmapFromLocal(url);
		if (bitmap != null) {
			view.setImageBitmap(bitmap);
			mMemUtils.setMemCache(url, bitmap);// 顺便保存在内存中
			return;
		}

		// 从网络加载
		mNetUtils.getBitmapFromNet(view, url);
	}

}
