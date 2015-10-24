package com.scsy150.mine.bean;

import java.io.Serializable;

public class OrderDetailsBean implements Serializable {

	/**
	 * 
	 */
	public static final long serialVersionUID = 973064268811903978L;

	/**
	 * 订单ID
	 */
	public int Orderid;
	/**
	 * 订单号
	 */
	public String OrderNum;
	/**
	 * 消费码
	 */
	public String ConsumerCcode;
	/**
	 * 订单创建时间
	 */
	public String CreateDate;
	/**
	 * 商家ID
	 */
	public int MerchantId;
	/**
	 * 商家电话号码
	 */
	public String PhoneNum;
	/**
	 * 活动或套餐ID 服务器根据传入的参数判断，订单类型，1联谊，2：约会
	 */
	public int AcOrSid;
	/**
	 * 商家名称
	 */
	public String Mhname;

	/**
	 * 套餐/活动名称 服务器根据传入的参数判断，订单类型，1 联谊，2：约会
	 */
	public String ASName;
	/**
	 * 活动logo
	 */
	public String FImage;
	/**
	 * 订单状态 0：待付款，1：已付款，2：完成，3:取消
	 */
	public int Status;
	/**
	 * 订单类型 1 联谊，2：约会
	 */
	public int OrderType;
	/**
	 * 约会说明 约会，联谊不存在
	 */
	public String Introductions;
	/**
	 * 约会对象 约会，联谊不存在
	 */
	public String NickName;
	/**
	 * 是否已评价 0：未评价，1：已评价
	 */
	public int IsEstimate;
	/**
	 * 支付方式
	 */
	public String OtherPayType;

	/**
	 * 支付日期
	 */
	public String PayDate;
	/**
	 * 订单总额
	 */
	public double allAmount;
	/**
	 * 优惠券金额
	 */
	public double CouponAmount;
	/**
	 * 付款比例 约会，联谊不存在
	 */
	public int PayProportion;
	/**
	 * 实付款
	 */
	public double CashAmount;
	/**
	 * 退款时间（取消时间） 约会，联谊不存在
	 */
	public String CancelDate;
	public int getOrderid() {
		return Orderid;
	}
	public void setOrderid(int orderid) {
		Orderid = orderid;
	}
	public String getOrderNum() {
		return OrderNum;
	}
	public void setOrderNum(String orderNum) {
		OrderNum = orderNum;
	}
	public String getConsumerCcode() {
		return ConsumerCcode;
	}
	public void setConsumerCcode(String consumerCcode) {
		ConsumerCcode = consumerCcode;
	}
	public String getCreateDate() {
		return CreateDate;
	}
	public void setCreateDate(String createDate) {
		CreateDate = createDate;
	}
	public int getMerchantId() {
		return MerchantId;
	}
	public void setMerchantId(int merchantId) {
		MerchantId = merchantId;
	}
	public String getPhoneNum() {
		return PhoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		PhoneNum = phoneNum;
	}
	public int getAcOrSid() {
		return AcOrSid;
	}
	public void setAcOrSid(int acOrSid) {
		AcOrSid = acOrSid;
	}
	public String getMhname() {
		return Mhname;
	}
	public void setMhname(String mhname) {
		Mhname = mhname;
	}
	public String getASName() {
		return ASName;
	}
	public void setASName(String aSName) {
		ASName = aSName;
	}
	public String getFImage() {
		return FImage;
	}
	public void setFImage(String fImage) {
		FImage = fImage;
	}
	public int getStatus() {
		return Status;
	}
	public void setStatus(int status) {
		Status = status;
	}
	public int getOrderType() {
		return OrderType;
	}
	public void setOrderType(int orderType) {
		OrderType = orderType;
	}
	public String getIntroductions() {
		return Introductions;
	}
	public void setIntroductions(String introductions) {
		Introductions = introductions;
	}
	public String getNickName() {
		return NickName;
	}
	public void setNickName(String nickName) {
		NickName = nickName;
	}
	public int getIsEstimate() {
		return IsEstimate;
	}
	public void setIsEstimate(int isEstimate) {
		IsEstimate = isEstimate;
	}
	public String getOtherPayType() {
		return OtherPayType;
	}
	public void setOtherPayType(String otherPayType) {
		OtherPayType = otherPayType;
	}
	public String getPayDate() {
		return PayDate;
	}
	public void setPayDate(String payDate) {
		PayDate = payDate;
	}
	public double getAllAmount() {
		return allAmount;
	}
	public void setAllAmount(double allAmount) {
		this.allAmount = allAmount;
	}
	public double getCouponAmount() {
		return CouponAmount;
	}
	public void setCouponAmount(double couponAmount) {
		CouponAmount = couponAmount;
	}
	public int getPayProportion() {
		return PayProportion;
	}
	public void setPayProportion(int payProportion) {
		PayProportion = payProportion;
	}
	public double getCashAmount() {
		return CashAmount;
	}
	public void setCashAmount(double cashAmount) {
		CashAmount = cashAmount;
	}
	public String getCancelDate() {
		return CancelDate;
	}
	public void setCancelDate(String cancelDate) {
		CancelDate = cancelDate;
	}


}
