package com.scsy150.volley.net;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.scsy150.consts.SystemConsts;

/*
 * Copyright (C) 2015 四川硅谷科技重庆分公司
 * 版权所有
 *
 * 功能描述： 登录时才用此类，也就是第一次保存cookie
 * 作者：硅谷科技
 * 创建时间：2015-09-15
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */
public class RequestAndSaveCookies extends StringRequest {

	Context context;

	public RequestAndSaveCookies(Context context, int method, String url,
			Listener<String> listener, ErrorListener errorListener) {
		super(method, url, listener, errorListener);
		this.context = context;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("Charset", "UTF-8");
		return localHashMap;
	}

	public Response<String> parseNetworkResponse(NetworkResponse response) {
		try {

			Map<String, String> responseHeaders = response.headers;
			String rawCookies = responseHeaders.get("Set-Cookie");
			String dataString = new String(response.data, "UTF-8");
			SharedPreferences spf = context.getSharedPreferences(
					SystemConsts.COOKIES, 0);
			SharedPreferences.Editor editor = spf.edit();
			if (rawCookies != null) {
				String cookies = rawCookies.toString();
				editor.putString("cookies", cookies);
				editor.commit();
			}
			// LogUtils.d("TAG", rawCookies);
			return Response.success(dataString,
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		}
	}
}
