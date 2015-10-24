/**
 * author 桑毅
 * time 2014-3-14 10:18
 * 用于创建支付平台类(线程安全类)
 * */

package com.scsy150.payment;

public class PaymentPlatformFactory {

	public final static int ALIPAY = 1;//支付宝
	public final static int TENPAY = 2;//财付通
	public final static int UNIONPAY = 3;//银联(百付天下)
		
//	public static IPayOperate createPaymentPlatform(int platformID){
//		IPayOperate platform = null; 
//		switch (platformID){
//		case PaymentPlatformFactory.ALIPAY:
//			platform = new AlipayPlatform();
//			break;
//			
//		case PaymentPlatformFactory.TENPAY:
//			platform = new TenpayPlatform();
//			break;
//			
//		case PaymentPlatformFactory.UNIONPAY:
//			platform = new UnionpayPlatform();
//			break;
//			
//		default:
//			//throw new IllegalArgumentException();			
//		}
//		return platform;
//	}
	
}
