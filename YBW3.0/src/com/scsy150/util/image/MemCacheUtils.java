package com.scsy150.util.image;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * 内存缓存
 * 
 * LruCache: 最近最少使用算法, 谷歌官方提供的缓存 2.3+以后,软引用不在靠谱. 谷歌建议用LruCache
 * 
 * @author K
 * 
 */
public class MemCacheUtils {

	private LruCache<String, Bitmap> mLruCache;

	public MemCacheUtils() {
		long maxMemory = Runtime.getRuntime().maxMemory();// 当前手机可使用的最大内存空间,
															// 模拟器是16M
		System.out.println("maxMemory:" + maxMemory);
		mLruCache = new LruCache<String, Bitmap>((int) (maxMemory / 8)) {

			// 计算每个图片的大小
			@Override
			protected int sizeOf(String key, Bitmap value) {
				int size = value.getRowBytes() * value.getHeight();// 计算每张图片大小
				return size;
			}
		};
	}

	public void setMemCache(String url, Bitmap bitmap) {
		mLruCache.put(url, bitmap);
	}

	public Bitmap getMemCache(String url) {
		Bitmap bitmap = mLruCache.get(url);
		return bitmap;
	}

}
