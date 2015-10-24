package com.scsy150.util.image;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * 网络缓存
 * 
 * @author K
 * 
 */
public class NetCacheUtils {

	private LocalCacheUtils mLocalUtils;
	private MemCacheUtils mMemUtils;

	public NetCacheUtils(LocalCacheUtils localUtils, MemCacheUtils memUtils) {
		mLocalUtils = localUtils;
		mMemUtils = memUtils;
	}

	public void getBitmapFromNet(ImageView view, String url) {
		BitmapTask task = new BitmapTask();
		task.execute(url, view);// 启动异步任务
	}

	class BitmapTask extends AsyncTask<Object, Integer, Bitmap> {

		private ImageView mImageView;
		private String mUrl;

		// 预加载,加载前的初始化 , 在主线程运行
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		// 异步加载, 在子线程运行
		@Override
		protected Bitmap doInBackground(Object... params) {
			mUrl = (String) params[0];
			mImageView = (ImageView) params[1];
			mImageView.setTag(mUrl);

			Bitmap bitmap = download(mUrl);
			return bitmap;
		}

		// 进度更新时, 在主线程运行
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		// 当异步加载结束后, 在主线程运行
		@Override
		protected void onPostExecute(Bitmap result) {
			if (result != null) {
				String url = (String) mImageView.getTag();
				if (mUrl.equals(url)) {// 确保给imageView设置正确的图片
					System.out.println("成功下载图片!");
					mImageView.setImageBitmap(result);

					mLocalUtils.setBitmapToLocal(url, result);// 将图片保存在本地
					mMemUtils.setMemCache(url, result);// 将图片保存在内存
				}
			}
		}

	}

	/**
	 * 下载图片
	 */
	public Bitmap download(String url) {
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setReadTimeout(5000);
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");

			conn.connect();

			int responseCode = conn.getResponseCode();
			if (responseCode == 200) {
				InputStream in = conn.getInputStream();
				Bitmap bitmap = BitmapFactory.decodeStream(in);
				return bitmap;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		return null;
	}

}
