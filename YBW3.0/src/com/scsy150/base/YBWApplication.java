package com.scsy150.base;

import java.io.File;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.scsy150.chat.YBWHXSDKHelper;
import com.scsy150.chat.domain.User;
import com.scsy150.consts.SystemConsts;
import com.scsy150.util.FileUtil;
import com.scsy150.util.LogUtil;
import com.scsy150.util.PkgUtil;
import com.scsy150.util.ResourcesUtil;
import com.scsy150.util.SharedPreferencesUtil;

public class YBWApplication extends Application {
	public static Context applicationContext;
	private static YBWApplication instance;

	// volley访问只需要一个队列对象即可
	public static RequestQueue mQueue;

	// login user name
	public final String PREF_USERNAME = "username";
	/**
	 * 当前用户nickname,为了苹果推送不是userid而是昵称
	 */
	public static String currentUserNick = "";
	public static YBWHXSDKHelper hxSDKHelper = new YBWHXSDKHelper();

	public static boolean isLoginChat = false;

	// 账号在别处登录
	public static boolean isConflict = false;
	// 账号被移除
	private static boolean isCurrentAccountRemoved = false;

	@Override
	public void onCreate() {
		super.onCreate();

		// true开启调试日志false关闭
		LogUtil.setDebugMode(true);

		applicationContext = this;

		// volley只需要一个请求对象
		mQueue = Volley.newRequestQueue(this);

		instance = this;
		ResourcesUtil.init(getResources());
		getSdcardpath();

		// 图片加载器初始化
		initImageLoader(this);
		SystemConsts.CLIENT_VERSION = PkgUtil
				.getAppVersionName(getApplicationContext());

		/**
		 * this function will initialize the HuanXin SDK
		 * 
		 * @return boolean true if caller can continue to call HuanXin related
		 *         APIs after calling onInit, otherwise false.
		 * 
		 *         环信初始化SDK帮助函数
		 *         返回true如果正确初始化，否则false，如果返回为false，请在后续的调用中不要调用任何和环信相关的代码
		 * 
		 *         for example: 例子：
		 * 
		 *         public class YBWHXSDKHelper extends HXSDKHelper
		 * 
		 *         HXHelper = new YBWHXSDKHelper();
		 *         if(HXHelper.onInit(context)){ // do HuanXin related work }
		 */
		// 初始化环信SDK,一定要先调用init()
		EMChat.getInstance().init(applicationContext);
		hxSDKHelper.onInit(applicationContext);
	}

	public static YBWApplication getInstance() {
		return instance;
	}

	/**
	 * 获取内存中好友user list
	 * 
	 * @return
	 */
	public Map<String, User> getContactList() {
		return hxSDKHelper.getContactList();
	}

	/**
	 * 设置好友user list到内存中
	 * 
	 * @param contactList
	 */
	public void setContactList(Map<String, User> contactList) {
		hxSDKHelper.setContactList(contactList);
	}

	/**
	 * 获取当前登陆用户名
	 * 
	 * @return
	 */
	public String getUserName() {
		return hxSDKHelper.getHXId();
	}

	/**
	 * 获取密码
	 * 
	 * @return
	 */
	public String getPassword() {
		return hxSDKHelper.getPassword();
	}

	/**
	 * 设置用户名
	 * 
	 * @param user
	 */
	public void setUserName(String username) {
		hxSDKHelper.setHXId(username);
	}

	/**
	 * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
	 * 内部的自动登录需要的密码，已经加密存储了
	 * 
	 * @param pwd
	 */
	public void setPassword(String pwd) {
		hxSDKHelper.setPassword(pwd);
	}

	/**
	 * 退出登录,清空数据
	 */
	public void logout(final EMCallBack emCallBack) {
		// 先调用sdk logout，在清理app中自己的数据
		hxSDKHelper.logout(emCallBack);
	}

	public void beginLocation() {
		final LocationClient mLocationClient = new LocationClient(this);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setCoorType("gcj02");
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
		mLocationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				double lat = location.getLatitude();
				double lng = location.getLongitude();

				if (lat != 0 && lng != 0) {
					SystemConsts.ADDRESS = location.getAddrStr();
					SystemConsts.Latitude = lat;
					SystemConsts.Longitude = lng;
					SharedPreferencesUtil spUtil = new SharedPreferencesUtil(
							getApplicationContext());
					String city = location.getCity();
					String province = location.getProvince();
					String district = location.getDistrict();
					if (!TextUtils.isEmpty(province)) {
						spUtil.putString(SystemConsts.CURRENT_PROVINCE,
								province);
					}
					if (!TextUtils.isEmpty(city)) {
						spUtil.putString(SystemConsts.CURRENT_CITY, city);
					}
					if (!TextUtils.isEmpty(district)) {
						spUtil.putString(SystemConsts.CURRENT_DISTRICT,
								district);
					}
					LogUtil.i(
							"location result",
							location.getLatitude() + "----"
									+ location.getLongitude() + "--"
									+ location.getAddrStr() + "---"
									+ location.getLocType());
					mLocationClient.unRegisterLocationListener(this);
					mLocationClient.stop();
				} else {
					mLocationClient.requestLocation();
				}
			}
		});
		mLocationClient.start();
	}

	@SuppressLint("InlinedApi")
	private void getSdcardpath() {
		StorageManager sm = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
		// 获取sdcard的路径：外置和内置
		try {
			String[] paths = (String[]) sm.getClass()
					.getMethod("getVolumePaths", null).invoke(sm, null);
			if (paths != null) {
				if (paths.length != 0) {
					for (int i = 0; i < paths.length; i++) {
						if (!FileUtil.SDCARDPATH.equals(paths[i].trim())) {
							if (Environment.getExternalStorageState().equals(
									Environment.MEDIA_REMOVED)) {
								FileUtil.SDCARDPATH = paths[i].trim();
							} else {

							}
						}
					}
				}
			}
			FileUtil.prepareDir();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public static void initImageLoader(Context context) {
		File cacheDir = StorageUtils.getOwnCacheDirectory(applicationContext,
				"scsy150/Cache");
		LogUtil.d("cacheDir－－－－－－－－－", cacheDir.getPath().toString());
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.memoryCacheExtraOptions(480, 800)
				// max width, max height，即保存的每个缓存文件的最大长宽
				.discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null)
				// Can slow ImageLoader, use it carefully (Better don't use
				// it)/设置缓存的详细信息，最好不要设置这个
				.threadPoolSize(3)
				// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
				// You can pass your own memory cache
				// implementation/你可以通过自己的内存缓存实现
				.memoryCacheSize(2 * 1024 * 1024)
				.discCacheSize(50 * 1024 * 1024)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				// 将保存的时候的URI名称用MD5 加密
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCacheFileCount(100)
				// 缓存的文件数量
				.discCache(new UnlimitedDiscCache(cacheDir))
				// 自定义缓存路径
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.imageDownloader(
						new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout
																				// (5
																				// s),
																				// readTimeout
																				// (30
																				// s)超时时间
				.writeDebugLogs() // Remove for release app
				.build();// 开始构建
		// Initialize ImageLoader with configuration.

		ImageLoader.getInstance().init(config);
	}

}
