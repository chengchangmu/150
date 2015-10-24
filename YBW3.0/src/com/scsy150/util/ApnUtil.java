package com.scsy150.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/*
 * Copyright (C) 
 * 版权所有
 *
 * 功能描述：网络接入点相关的工具类
 * 作者：硅谷科技
 * 创建时间：2015-08-26
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */
public class ApnUtil {
	public static final int TYPE_UNKNOWN = 0x000;
	public static final int TYPE_NET = 0x001;
	public static final int TYPE_WAP = 0x002;
	public static final int TYPE_WIFI = 0x004;

	public static final int T_APN_CMWAP = 0x008;
	public static final int T_APN_3GWAP = 0x010;
	public static final int T_APN_UNIWAP = 0x020;
	public static final int T_APN_CTWAP = 0x040;
	public static final int T_APN_CTNET = 0x080;
	public static final int T_APN_UNINET = 0x100;
	public static final int T_APN_3GNET = 0x200;
	public static final int T_APN_CMNET = 0x400;

	public static final String APN_UNKNOWN = "N/A";
	public static final String APN_NET = "Net";
	public static final String APN_WAP = "Wap";
	public static final String APN_WIFI = "Wlan";
	private static String APN_WIFI_NAME_WITH_SSID = "";

	private static int sApnType = TYPE_WIFI;

	// 统计使用的APN类型
	private static int sApnTypeS = TYPE_WIFI;

	private static ApnProxyInfo sApnProxyInfo = new ApnProxyInfo();

	public static class ApnProxyInfo {
		public String apnProxy = null;
		public int apnPort = 80;
		public byte apnProxyType = 0;
		public boolean apnUseProxy = false;
	}

	// 代理方式
	// 电信代理
	public static final byte PROXY_TYPE_CM = 0;
	// 移动(联通)代理
	public static final byte PROXY_TYPE_CT = 1;

	// 代理地址
	private static final String PROXY_CTWAP = "10.0.0.200";

	// APN 名称
	public final static String APN_CMWAP = "cmwap";
	public final static String APN_CMNET = "cmnet";
	public final static String APN_3GWAP = "3gwap";
	public final static String APN_3GNET = "3gnet";
	public final static String APN_UNIWAP = "uniwap";
	public final static String APN_UNINET = "uninet";
	public final static String APN_CTWAP = "ctwap";
	public final static String APN_CTNET = "ctnet";
	public final static String APN_777 = "#777";

	public synchronized static int getApnType(Context ctx, boolean shouldRefrash) {
		if (shouldRefrash) {
			init(ctx);
		}
		return sApnType;
	}

	public synchronized static int getApnType(Context ctx) {
		return getApnType(ctx, true);
	}

	public synchronized static int getApnTypeS(Context ctx,
			boolean shouldRefrash) {
		if (shouldRefrash) {
			init(ctx);
		}
		return sApnTypeS;
	}

	public synchronized static int getApnTypeS(Context ctx) {
		return getApnTypeS(ctx, true);
	}

	public synchronized static ApnProxyInfo getApnProxyInfo(Context ctx) {
		init(ctx);
		return sApnProxyInfo;
	}

	/**
	 * apn设置初始化
	 */
	private static void init(Context ctx) {
		ConnectivityManager manager = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();

		try {
			int type = -1;
			sApnType = TYPE_UNKNOWN;
			sApnTypeS = TYPE_UNKNOWN;

			String extraInfo = null;
			if (networkInfo != null) {
				type = networkInfo.getType();
				extraInfo = networkInfo.getExtraInfo();
				if (extraInfo == null) {
					sApnType = TYPE_UNKNOWN;
					sApnTypeS = TYPE_UNKNOWN;
				} else
					extraInfo = extraInfo.trim().toLowerCase();
			}

			if (type == ConnectivityManager.TYPE_WIFI) {
				sApnType = TYPE_WIFI;
				sApnTypeS = TYPE_WIFI;
				sApnProxyInfo.apnUseProxy = false;
				APN_WIFI_NAME_WITH_SSID = APN_WIFI + getWifiBSSID(ctx);
			} else {
				// 判断是 wap 模式还是 net 模式
				if (extraInfo == null) {
					sApnType = TYPE_UNKNOWN;
					sApnTypeS = TYPE_UNKNOWN;
				} else if (extraInfo.contains(APN_CMWAP)) {
					sApnType = TYPE_WAP;
					sApnTypeS = T_APN_CMWAP;
				} else if (extraInfo.contains(APN_UNIWAP)) {
					sApnType = TYPE_WAP;
					sApnTypeS = T_APN_UNIWAP;
				} else if (extraInfo.contains(APN_3GWAP)) {
					sApnType = TYPE_WAP;
					sApnTypeS = T_APN_3GWAP;
				} else if (extraInfo.contains(APN_CTWAP)) {
					sApnType = TYPE_WAP;
					sApnTypeS = T_APN_CTWAP;
				} else if (extraInfo.contains(APN_CMNET)) {
					sApnType = TYPE_NET;
					sApnTypeS = T_APN_CMNET;
				} else if (extraInfo.contains(APN_UNINET)) {
					sApnType = TYPE_NET;
					sApnTypeS = T_APN_UNINET;
				} else if (extraInfo.contains(APN_3GNET)) {
					sApnType = TYPE_NET;
					sApnTypeS = T_APN_3GNET;
				} else if (extraInfo.contains(APN_CTNET)) {
					sApnType = TYPE_NET;
					sApnTypeS = T_APN_CTNET;
				} else if (extraInfo.contains(APN_777)) {
					sApnType = TYPE_UNKNOWN;
					sApnTypeS = TYPE_UNKNOWN;
				} else {
					sApnType = TYPE_UNKNOWN;
					sApnTypeS = TYPE_UNKNOWN;
				}

				sApnProxyInfo.apnUseProxy = false;
				if (isProxyMode(sApnType)) {
					sApnProxyInfo.apnProxy = android.net.Proxy.getDefaultHost();
					sApnProxyInfo.apnPort = android.net.Proxy.getDefaultPort();

					if (sApnProxyInfo.apnProxy != null)
						sApnProxyInfo.apnProxy = sApnProxyInfo.apnProxy.trim();

					if (!TextUtils.isEmpty(sApnProxyInfo.apnProxy)) {
						sApnProxyInfo.apnUseProxy = true;
						sApnType = TYPE_WAP;

						// 判断是否电信代理
						if (PROXY_CTWAP.equals(sApnProxyInfo.apnProxy)) {
							sApnProxyInfo.apnProxyType = PROXY_TYPE_CT;
							sApnTypeS = T_APN_CTWAP;
						} else {
							sApnProxyInfo.apnProxyType = PROXY_TYPE_CM;

							// 移动，联通的代理情况无法分辨，上报N/A
							// getApnTypeS() = TYPE_UNKNOWN;
						}
					} else {
						sApnProxyInfo.apnUseProxy = false;
						sApnType = TYPE_NET;

						// #777非代理时为电信Net
						if (extraInfo != null && extraInfo.contains(APN_777)) {
							sApnTypeS = T_APN_CTNET;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断是否有网络
	 * 
	 * @return true connected
	 */
	public static boolean isNetworkConnected(Context ctx) {
		ConnectivityManager manager = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager == null)
			return false;

		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		if (networkInfo == null)
			return false;

		if (networkInfo.isConnected() || networkInfo.isAvailable())
			return true;

		return false;
	}

	/**
	 * 获取当前接入点是否wifi
	 * 
	 * @return
	 */
	public static boolean isWifiMode(Context ctx) {
		ConnectivityManager manager = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager != null) {
			NetworkInfo networkInfo = manager.getActiveNetworkInfo();

			if (networkInfo != null) {
				int type = networkInfo.getType();

				if (type == ConnectivityManager.TYPE_WIFI) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获取WIFI的BSSID
	 */
	public static String getWifiBSSID(Context ctx) {
		if (sApnTypeS == TYPE_WIFI) {
			WifiManager manager = (WifiManager) ctx
					.getSystemService(Context.WIFI_SERVICE);
			if (manager != null) {
				WifiInfo wifiInfo = manager.getConnectionInfo();
				if (wifiInfo != null)
					return wifiInfo.getBSSID();
			}
		}

		return null;
	}

	/**
	 * 获取WIFI的SSID
	 */
	public static String getWifiSSID(Context ctx) {
		if (sApnTypeS == TYPE_WIFI) {
			WifiManager manager = (WifiManager) ctx
					.getSystemService(Context.WIFI_SERVICE);
			if (manager != null) {
				WifiInfo wifiInfo = manager.getConnectionInfo();
				if (wifiInfo != null)
					return wifiInfo.getSSID();
			}
		}
		return null;
	}

	public static boolean is3GOr2GMode(Context ctx) {
		return is3GMode(ctx) || is2GMode(ctx);
	}

	/**
	 * 是否是3G网络模式
	 * 
	 * @return
	 */
	public static boolean is3GMode(Context ctx) {
		ConnectivityManager manager = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager != null) {
			NetworkInfo networkInfo = manager.getActiveNetworkInfo();

			if (networkInfo == null) {
				return false;
			}

			int type = networkInfo.getType();
			if (type == ConnectivityManager.TYPE_WIFI) {
				return false;
			}

			if (type == ConnectivityManager.TYPE_MOBILE) {
				int subType = networkInfo.getSubtype();
				switch (subType) {
				case TelephonyManager.NETWORK_TYPE_GPRS:
				case TelephonyManager.NETWORK_TYPE_EDGE:
				case TelephonyManager.NETWORK_TYPE_CDMA:
					return false;
				default:
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 是否是2G网络模式
	 * 
	 * @return
	 */
	public static boolean is2GMode(Context ctx) {
		ConnectivityManager manager = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager != null) {
			NetworkInfo networkInfo = manager.getActiveNetworkInfo();

			if (networkInfo == null) {
				return false;
			}

			int type = networkInfo.getType();
			if (type == ConnectivityManager.TYPE_WIFI) {
				return false;
			}

			if (type == ConnectivityManager.TYPE_MOBILE) {
				int subType = networkInfo.getSubtype();
				switch (subType) {
				case TelephonyManager.NETWORK_TYPE_GPRS:
				case TelephonyManager.NETWORK_TYPE_EDGE:
				case TelephonyManager.NETWORK_TYPE_CDMA:
					// 2G
					return true;
				default:
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * 是否代理模式
	 * 
	 * @param apnType
	 * @return
	 */
	private static boolean isProxyMode(int apnType) {
		return apnType == TYPE_WAP || apnType == TYPE_UNKNOWN;
	}

	public static String getApnName(int apnType) {
		switch (apnType) {
		/*
		 * case TYPE_WAP: return APN_WAP; case TYPE_NET: return APN_NET;
		 */
		case TYPE_WIFI:
			return APN_WIFI;
		case T_APN_3GNET:
			return APN_3GNET;
		case T_APN_3GWAP:
			return APN_3GWAP;
		case T_APN_CMNET:
			return APN_CMNET;
		case T_APN_CMWAP:
			return APN_CMWAP;
		case T_APN_CTNET:
			return APN_CTNET;
		case T_APN_CTWAP:
			return APN_CTWAP;
		case T_APN_UNINET:
			return APN_UNINET;
		case T_APN_UNIWAP:
			return APN_UNIWAP;
			/*
			 * case TYPE_UNKNOWN: return APN_UNKNOWN;
			 */
		default:
			return APN_UNKNOWN;
		}
	}

	public static String getApnNameWithBSSID(int apnType) {
		if (apnType != TYPE_WIFI)
			return getApnName(apnType);
		else
			return APN_WIFI_NAME_WITH_SSID;
	}

	/**
	 * Indicates whether network connectivity is possible.
	 * 
	 * @return
	 */
	public static boolean isNetworkAvailable(Context ctx) {
		ConnectivityManager manager = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (manager == null)
			return false;

		NetworkInfo networkInfo = manager.getActiveNetworkInfo();

		if (networkInfo == null) {
			return false;
		}
		if (networkInfo.isConnected() || networkInfo.isAvailable()) {
			return true;
		}
		return false;
	}

	/**
	 * 根据apn字符串，返回int形式的apn类型
	 * 
	 * @param apnName
	 * @return
	 */
	public static int getType(String apnName) {
		int result = TYPE_UNKNOWN;
		if (!TextUtils.isEmpty(apnName)) {
			String apnLowserCase = apnName.trim().toLowerCase();
			if (apnLowserCase.indexOf("wifi") != -1
					|| apnLowserCase.indexOf("wlan") != -1) {
				result = TYPE_WIFI;
			} else if (apnLowserCase.indexOf("net") != -1) {
				result = TYPE_NET;
			} else if (apnLowserCase.indexOf("wap") != -1) {
				result = TYPE_WAP;
			}
		}
		return result;
	}

	/**
	 * 获取网络类型
	 * 
	 * @param content
	 * @return
	 */
	public static final int NETWORD_UNKNOW = 200;
	public static final int NETWORK_WIFI = 201;
	public static final int NETWORK_2G = 202;
	public static final int NETWORK_3G = 203;
	public static final int NETWORK_4G = 204;

	public static int getNetType(Context content) {
		ConnectivityManager cManager = (ConnectivityManager) content
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		int type = NETWORD_UNKNOW;
		if (netInfo != null) {
			switch (netInfo.getType()) {
			case ConnectivityManager.TYPE_WIFI:
				type = NETWORK_WIFI;
				break;
			case ConnectivityManager.TYPE_MOBILE:
				TelephonyManager telMgr = (TelephonyManager) content
						.getSystemService(Context.TELEPHONY_SERVICE);
				switch (telMgr.getNetworkType()) {
				case TelephonyManager.NETWORK_TYPE_GPRS:
				case TelephonyManager.NETWORK_TYPE_EDGE:
				case TelephonyManager.NETWORK_TYPE_CDMA:
				case TelephonyManager.NETWORK_TYPE_1xRTT:
				case TelephonyManager.NETWORK_TYPE_IDEN:
					type = NETWORK_2G;
					break;
				case TelephonyManager.NETWORK_TYPE_UMTS:
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
				case TelephonyManager.NETWORK_TYPE_EVDO_A:
				case TelephonyManager.NETWORK_TYPE_HSDPA:
				case TelephonyManager.NETWORK_TYPE_HSUPA:
				case TelephonyManager.NETWORK_TYPE_HSPA:
				case TelephonyManager.NETWORK_TYPE_EVDO_B:
				case TelephonyManager.NETWORK_TYPE_EHRPD:
				case TelephonyManager.NETWORK_TYPE_HSPAP:
					type = NETWORK_3G;
					break;
				case TelephonyManager.NETWORK_TYPE_LTE:
					type = NETWORK_4G;
				default:
					type = NETWORD_UNKNOW;
					break;
				}
				break;
			default:
				type = NETWORD_UNKNOW;
				break;
			}
		}
		return type;
	}

	/**
	 * 获取网络类型名称
	 * 
	 * @param content
	 * @return
	 */
	public static String getNetTypeName(Context content) {
		String type = "";
		switch (getNetType(content)) {
		case NETWORD_UNKNOW:
			type = "unknow";
			break;
		case NETWORK_WIFI:
			type = "wifi";
			break;
		case NETWORK_2G:
			type = "2G";
			break;
		case NETWORK_3G:
			type = "3G";
			break;
		case NETWORK_4G:
			type = "4G";
			break;
		default:
			break;
		}
		return type;
	}
}
