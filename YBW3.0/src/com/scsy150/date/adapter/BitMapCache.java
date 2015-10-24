package com.scsy150.date.adapter;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class BitMapCache implements ImageCache {

	private LruCache<String, Bitmap> mcaCache;

	public BitMapCache() {

		int max = 10 * 1024 * 1024;
		mcaCache = new LruCache<String, Bitmap>(max) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight();
			}

		};

	}

	@Override
	public Bitmap getBitmap(String url) {

		return mcaCache.get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {

		mcaCache.put(url, bitmap);
	}

}
