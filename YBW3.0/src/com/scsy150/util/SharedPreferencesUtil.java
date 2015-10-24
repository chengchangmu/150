package com.scsy150.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.scsy150.consts.SystemConsts;

public class SharedPreferencesUtil {
	
	SharedPreferences mShared = null;
	
	public SharedPreferencesUtil(Context ctx) {
		mShared = ctx.getSharedPreferences(SystemConsts.APP_CONFIGURE, Context.MODE_PRIVATE);
	}
	
	
	public boolean getBoolean(String key, boolean defValue) {
		return mShared.getBoolean(key, defValue);
	}
	
	public void putBoolean(String key, boolean value) {
		mShared.edit().putBoolean(key, value).commit();
	}
	
	
	public float getFloat(String key, float defValue) {
		return mShared.getFloat(key, defValue);
	}
	
	public void putFloat(String key, float value) {
		mShared.edit().putFloat(key, value).commit();
	}
	
	
	public long getLong(String key, long defValue) {
		return mShared.getLong(key, defValue);
	}
	
	public void putLong(String key, long value) {
		mShared.edit().putLong(key, value).commit();
	}
	
	
	public int getInt(String key, int defValue) {
		return mShared.getInt(key, defValue);
	}
	
	public void putInt(String key, int value) {
		mShared.edit().putInt(key, value).commit();
	}
	
	public String getString(String key, String defValue) {
		return mShared.getString(key, defValue);
	}
	
	public void putString(String key, String value) {
		mShared.edit().putString(key, value).commit();
	}
}
