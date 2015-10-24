package com.scsy150.util.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.scsy150.util.MD5Util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**
 * 本地缓存
 * 
 * @author K
 * 
 */
public class LocalCacheUtils {

	private String LOCAL_CACHE_PATH = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/img_cache";

	/**
	 * 读本地缓存
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap getBitmapFromLocal(String url) {
		try {
			String fileName = MD5Util.convertMD5(url);
			File file = new File(LOCAL_CACHE_PATH, fileName);

			if (file.exists()) {
				// 图片缓存存在
				Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(
						file));
				return bitmap;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 将图片保存在本地
	 * 
	 * @param url
	 * @param bitmap
	 */
	public void setBitmapToLocal(String url, Bitmap bitmap) {
		try {
			String fileName = MD5Util.convertMD5(url);
			File file = new File(LOCAL_CACHE_PATH, fileName);

			File parent = file.getParentFile();// 获取所在文件夹
			if (!parent.exists()) {
				parent.mkdirs();// 创建文件夹
			}

			bitmap.compress(CompressFormat.JPEG, 100,
					new FileOutputStream(file));// 将图片压缩存储到本地
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
