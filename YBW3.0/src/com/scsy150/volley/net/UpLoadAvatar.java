package com.scsy150.volley.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;

import com.scsy150.consts.MzApi;
import com.scsy150.consts.SystemConsts;
import com.scsy150.util.MD5Util;
import com.scsy150.util.TimeUtil;

public class UpLoadAvatar {
	static Context context;

	public UpLoadAvatar() {
	}

	public UpLoadAvatar(Context context) {
		UpLoadAvatar.context = context;
	}

	/**
	 * 上传头像，使用此方法时，需放在异步操作或者非主线程中
	 * 
	 * @param 需要上传的图像文件
	 * @return
	 */
	public String uploadImg(File file) {
		String url = uploadFile(MzApi.UPLOADAVATAR, file);
		return url;

	}

	/**
	 * 上传相册照片，使用此方法时，需放在异步操作或者非主线程中
	 * 
	 * @param 需要上传的图像文件
	 * @return
	 */
	public String uploadPhoto(File file) {
		String url = uploadFile(MzApi.UPLOADPHOTOS, file);
		return url;

	}

	/**
	 * 通过URI下载文件，保存到file文件里
	 * 
	 * @param URI
	 * @param file
	 * @return
	 */
	private static String uploadFile(String URI, File file) {
		try {
			return uploadFile(URI, new FileInputStream(file), file.getName());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 通过URI下载文件，保存到InputStream is里去
	 * 
	 * @param URI
	 * @param is
	 * @param fname
	 * @return
	 */
	private static String uploadFile(String URI, InputStream is, String fname) {
		try {
			// LogUtils.d("TAG", "开始具体上传代码");
			String cookies = LoadCookies();
			// 以下3个字符串有必要在做urlconnection时做复杂的拼接吗？感觉好鸡肋
			// String boundary = "*****" + System.currentTimeMillis();
			// String twoHyphens = "--";
			// String end = "\r\n";
			URL url = new URL(URI);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Cookie", cookies);
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Charset", "UTF-8");

			byte[] buf = new byte[1024];
			OutputStream osw = connection.getOutputStream();
			while (is.read(buf) != -1) {
				osw.write(buf);
			}
			osw.flush();
			osw.close();
			is.close();

			// Log.i("life!", connection.getResponseCode() + "");
			String jsonObject = getResponseStr(connection);
			return jsonObject;
		} catch (Exception e) {
			// Log.i("result", "catch -------------" + e.getMessage());
			e.printStackTrace();
			return "";
		}
	}

	private static String getResponseStr(HttpURLConnection connection) {
		try {
			int length;
			InputStream ins = connection.getInputStream();
			byte[] result = new byte[1024];
			StringBuffer sb = new StringBuffer();
			while ((length = ins.read(result)) > 0) {
				String str = new String(result, 0, length);
				// Log.i("result",length+"test----"+str);
				sb.append(str);
				str = null;
			}
			String jsonObject = sb.toString().trim();
			// LogUtils.d("TAG", "返回的json"+jsonObject);
			return jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/** 从SharedPreferences读取cookies **/
	public static String LoadCookies() {
		SharedPreferences spf = context.getSharedPreferences(
				SystemConsts.COOKIES, 0);
		String cookies = spf.getString("cookies", "");
		if (!"".equals(cookies)) {
			return cookies;
		}
		return "";
	}
}
