package com.scsy150.consts;

/*
 * Copyright (C) 2015 四川硅谷科技重庆分公司
 * 版权所有
 *
 * 功能描述：系统级别的公用常量
 * 作者：硅谷科技
 * 创建时间：2015-08-25
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */

public class SystemConsts {

	// IsSuccess状态代码
	/**
	 * 0 失败（一般需要弹出对应错误提示消息）
	 */
	public static final int RESPONSE_FAIL = 0;
	/**
	 * 1 执行成功（根据需求确定是否提示消息）
	 */
	public static final int RESPONSE_SUCCESS = 1;
	/**
	 * 9 未登录（提示并返回重新登录界面）
	 */
	public static final int RESPONSE_NO_LOGIN = 9;

	/**
	 * 5 头像认证失败，请重新上传头像认证！
	 */
	public static final int AUTH_FIAL = 5;
	/**
	 * 4 头像认证中......
	 */
	public static final int AUTH_ING = 4;
	/**
	 * 5 头像认证失败，请重新上传头像认证！
	 */
	public static final int AUTH = 5;

	/**
	 * 2 用户不存在，请注册！
	 */
	public static final int LOGIN_NO_USER = 2;
	/**
	 * 3密码错误！
	 */
	public static final int LOGIN_PWD_ERROR = 3;

	/**
	 * 配置文件名以及key
	 */
	// 保存下的cookies文件名
	public static final String COOKIES = "Cookies";
	// 登录注册成功后存本地
	public static final String USER_INFO = "10010";

	public static int userId;
	public final static String SIGN_KEY = "yibaiwukey!QAZ2wsx20150820";// sign
																		// 校验私钥
	public final static String APP_CONFIGURE = "fileconfigure";// 配置文件名
	public final static String USER_NAME = "userName"; // 用户账号
	public final static String USER_PWD = "userPwd"; // 用户密码
	public final static String USER_ID = "userId"; // 用户ID
	public final static String USER_SEX = "user_sex"; // 用户性别
	public final static String USER_HEADIMG = "user_headimg"; // 用户头像
	public final static String USER_NICKNAME = "user_nickname"; // 用户昵称

	public final static String USERINFO_KEY = "login"; // 登录成功后返回的JSON
	public final static String MAP_KEY = "13b9a9636bda19e15446b5f5f57641cc";
	public final static String IS_AUTO_CLEAR_CACHE = "isAutoClearCache"; // 是否自动清理缓存
	public final static String TOKEN_KEY = "httpToken";
	public static String token = "";
	public static int MIX_PWD_LENGTH = 4;

	public static double Latitude;
	public static double Longitude;
	public final static int locationType = 2; // 1:gps 2:baidu 3:google

	public static final String API_LEAVEL = "3.0.1"; // 服务端接口版本
	public static String CLIENT_VERSION = "3.0"; // 客户端版本

	public static final String PUSH_BRODCAST = "com.scsy150.push.broadcast";

	public final static String CURRENT_PROVINCE = "province";
	public final static String CURRENT_CITY = "city";
	public final static String CURRENT_DISTRICT = "district";
	public static String ADDRESS = "address";

	public final static String GUID_CONFIGURE = "loadconfigure";// 配置文件名Key账号登录导航
	
	/*
	 * 联谊接口用到的常量
	 */
	// 活动分类：1:附近，2：热门，3：最新
	public final static int ACTIVITY_TYPE_NEAR = 1;
	public final static int ACTIVITY_TYPE_HOT = 2;
	public final static int ACTIVITY_TYPE_RECENT = 3;
	
	// 活动类型：0:不限，1：校园活动， 2：交友活动
	public final static int MERCHANT_ALL = 0;
	public final static int MERCHANT_SCHOOL = 1;
	public final static int MERCHANT_SOCIAL = 2;
	
	// 活动时间：0: 全部,1：今天，2：明天，3：本周，4：本月(不过滤为0)
	public final static int DATE_ALL = 0;
	public final static int DATE_TODAY = 1;
	public final static int DATE_TOMORROW = 2;
	public final static int DATE_THIS_WEEK = 3;
	public final static int DATE_THIS_MONTH = 4;
	
	// 活动筛选: 0:不限,1：校园活动,2：交友聚会
	public final static int MERCHANT_TYPE_ALL = 0;
	public final static int MERCHANT_TYPE_SCHOOL = 1;
	public final static int MERCHANT_TYPE_MEET = 2;
	
	// 活动价格：
	public final static int FIFTY_YUAN = 50;
	public final static int ONE_HUNDRED_YUAN = 100;
	public final static int TWO_HUNDRED_YUAN = 200;
	public final static int FIVE_HUNDRED_YUAN = 500;
	public final static int TEN_HUNDRED_YUAN = 1000;
	
}
