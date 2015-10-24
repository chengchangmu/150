package com.scsy150.mine.bean;

import java.io.Serializable;

public class OrderBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8106059360717402816L;

	/**
	 * 订单状态 0：待付款，1：已付款，2：完成，3:取消
	 */
	private int Status;
	/**
	 * 商家ID
	 */
	private int MerchantId;

	/**
	 * 订单 订单类型，1 活动，2：约会
	 */
	private int OrderType;
	/**
	 * 金额
	 */
	private double CashAmount;

	/**
	 * Logo路径
	 */
	private String FImage;

	/**
	 * 发布的活动/套餐名称
	 */
	private String AciName;

	/**
	 * 商家名称
	 */
	private String MerName;

	/**
	 * 订单ID
	 */
	private int Orderid;

	/**
	 * 活动/套餐ID
	 */
	private int AcOrSid;
	/**
	 * 是否已评价 0：否，1：是
	 */
	private int IsEstimate;

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public int getMerchantId() {
		return MerchantId;
	}

	public void setMerchantId(int merchantId) {
		MerchantId = merchantId;
	}

	public int getOrderType() {
		return OrderType;
	}

	public void setOrderType(int orderType) {
		OrderType = orderType;
	}

	public double getCashAmount() {
		return CashAmount;
	}

	public void setCashAmount(double cashAmount) {
		CashAmount = cashAmount;
	}

	public String getFImage() {
		return FImage;
	}

	public void setFImage(String fImage) {
		FImage = fImage;
	}

	public String getAciName() {
		return AciName;
	}

	public void setAciName(String aciName) {
		AciName = aciName;
	}

	public String getMerName() {
		return MerName;
	}

	public void setMerName(String merName) {
		MerName = merName;
	}

	public int getOrderid() {
		return Orderid;
	}

	public void setOrderid(int orderid) {
		Orderid = orderid;
	}

	public int getAcOrSid() {
		return AcOrSid;
	}

	public void setAcOrSid(int acOrSid) {
		AcOrSid = acOrSid;
	}

	public int getIsEstimate() {
		return IsEstimate;
	}

	public void setIsEstimate(int isEstimate) {
		IsEstimate = isEstimate;
	}

}
