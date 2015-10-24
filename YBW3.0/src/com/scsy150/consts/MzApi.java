package com.scsy150.consts;

import com.scsy150.util.TimeUtil;

/*
 * Copyright (C) 2015 四川硅谷科技重庆分公司
 * 版权所有
 *
 * 功能描述：一百五后台服务器提供的所有接口地址
 * 作者：硅谷科技
 * 创建时间：2015-08-25
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */

/**
 * @author Administrator
 * 
 */
public class MzApi {

	// 测试服
	public static final int SERVER_TYPE_TEST = 0;
	// 预发布
	public static final int SERVER_TYPE_PRE_RELEASE = 1;
	// 正式服
	public static final int SERVER_TYPE_RELEASE = 2;

	// 在此配置服务器类型
	public static int mServerType = SERVER_TYPE_TEST;

	public static String SERVER = "";
	public static String IMAGE_DOWNLOAD = ""; // 测试服 图片地址

	static {
		switch (mServerType) {
		case SERVER_TYPE_PRE_RELEASE:
			SERVER = "";
			IMAGE_DOWNLOAD = ""; // 预发布 图片地址
			break;
		case SERVER_TYPE_RELEASE:
			SERVER = "http://api.yibaiwu.com/";
			IMAGE_DOWNLOAD = ""; // 正式服 图片地址
			break;
		default:
			// SERVER = "http://192.168.3.250:8090/";
			SERVER = "http://192.168.3.199:150/";
			IMAGE_DOWNLOAD = "http://192.168.3.199:200/"; // 测试服

			break;
		}
	}

	public static final String IP = SERVER + "";
	public static final String PORT = "";
	public static final String HEAD_URL = IP + PORT;
	// 头像上传
	public static String UPLOADAVATAR = HEAD_URL
			+ "Handler/UploadHandler.ashx?m=UploadPhoto&type=HeadImg&sign="
			+ TimeUtil.getSignKey();
	// 相册上传
	public static String UPLOADPHOTOS = "";
	// 3.0 注册登录
	public static final String RESET_PASSWORD = HEAD_URL + ""; // 设置新密码

	/**
	 * 获取通用验证码
	 */
	public static final String GET_NORMAL_CODE = HEAD_URL
			+ "Handler/ManagerUser.ashx";
	/**
	 * 登录/注册新用户 M参数决定是登录还是注册
	 */
	public static final String LOGIN_REG = HEAD_URL + "Handler/GetData.ashx";

	public static String getTokenUri(int userId, String nickName,
			String headImageUrl) {
		return HEAD_URL + "Handler/ChatHandler.ashx?M=GetToken&userId="
				+ userId + "&userName=" + nickName + "&headImageUrl="
				+ headImageUrl;
	}
	
	
	public static final String ACTIVITY_URL = HEAD_URL + "Handler/GetData.ashx?";
	/**
	 * 预约获取约会列表信息
	 */
	public static final String GET_DATA = HEAD_URL + "Handler/GetData.ashx";

	public static String getOrderListDate(int EETYE, int beginpage,
			int Everpage, double Longitude, double Latitude, int AgeGroup,
			String city) {

		return HEAD_URL + "Handler/GetData.ashx?M=GetEngagement&EEType="
				+ EETYE + "&MAC=&beginpage=" + beginpage + "&Everpage="
				+ Everpage + "&Longitude=" + Longitude + "&Latitude="
				+ Latitude + "&AgeGroup=" + AgeGroup + "&city=" + city;
	};

	/**
	 * 获取现场约约会列表信息
	 */
	public static final String GET_NOWDATE_DATA = HEAD_URL
			+ "Handler/GetData.ashx";
	public static String IMG_URL="http://192.168.3.199:200/";
	public static String HEAD_SMALL_PIC_SIZE = "http://192.168.3.199:150/http_imgload.ashx?w=310&h=310&s=1&url=";
    public static String BIG_PIC_SIZE="http://192.168.3.199:150/http_imgload.ashx?w=700&h=500&s=1&url=";
    public static String LONG_PIC_SIZE="http://192.168.3.199:150/http_imgload.ashx?w=450&h=450&s=1&url=";
	public static final String getNowDateList(int EETYE, String Mac,
			int beginpage, int Everpage, double Longitude, double Latitude,
			int AgeGroup, String city) {
		return HEAD_URL + "Handler/GetData.ashx?M=GetEngagement&EEType="
				+ EETYE + "&MAC=" + Mac + "&beginpage=" + beginpage
				+ "&Everpage=" + Everpage + "&Longitude=" + Longitude
				+ "&Latitude=" + Latitude + "&AgeGroup=" + AgeGroup + "&city="
				+ city;
	};
	/**
	 * 
	 */

}
