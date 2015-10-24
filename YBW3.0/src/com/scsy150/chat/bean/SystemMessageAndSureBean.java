package com.scsy150.chat.bean;

import java.io.Serializable;

public class SystemMessageAndSureBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1587556556136366513L;

	/**
	 * 见面时间
	 */
	private String SeeDate;

	/**
	 * 商家名称
	 */
	private String Mhname;
	/**
	 * 付款比例
	 */
	private Integer PayProportion;
	/**
	 * 发起人预约时间
	 */
	private String BeginDate;
	/**
	 * 用户名称
	 */
	private String NickName;

	/**
	 * 对方ID
	 */
	private int UserId;
	/**
	 * 发送时间
	 */
	private String CreateDate;
	/**
	 * 说明
	 */
	private String Introductions;
	/**
	 * 年龄段
	 */
	private int GroupBrithday;
	/**
	 * 状态
	 */
	private Integer Status;
	/**
	 * 活动名称
	 */
	private String SName;
	/**
	 * 套餐类型
	 */
	private Integer OrderType;
	/**
	 * 套餐ID
	 */
	private Integer Sid;
	/**
	 * 用户头像
	 */
	private String HeadImg;

	private int Sex;

	/**
	 * 套餐总价格
	 */
	private double TotalCost;
	/**
	 * 需付款金额
	 */
	private double allAmount;

	public double getTotalCost() {
		return TotalCost;
	}

	public void setTotalCost(double totalCost) {
		TotalCost = totalCost;
	}

	public String getMhname() {
		return Mhname;
	}

	public void setMhname(String mhname) {
		Mhname = mhname;
	}

	public Integer getPayProportion() {
		return PayProportion;
	}

	public void setPayProportion(Integer payProportion) {
		PayProportion = payProportion;
	}

	public String getBeginDate() {
		return BeginDate;
	}

	public void setBeginDate(String beginDate) {
		BeginDate = beginDate;
	}

	public String getNickName() {
		return NickName;
	}

	public void setNickName(String nickName) {
		NickName = nickName;
	}

	public double getAllAmount() {
		return allAmount;
	}

	public void setAllAmount(double allAmount) {
		this.allAmount = allAmount;
	}

	public int getUserId() {
		return UserId;
	}

	public void setUserId(int userId) {
		UserId = userId;
	}

	public String getCreateDate() {
		return CreateDate;
	}

	public void setCreateDate(String createDate) {
		CreateDate = createDate;
	}

	public String getIntroductions() {
		return Introductions;
	}

	public void setIntroductions(String introductions) {
		Introductions = introductions;
	}

	public int getGroupBrithday() {
		return GroupBrithday;
	}

	public void setGroupBrithday(int groupBrithday) {
		GroupBrithday = groupBrithday;
	}

	public Integer getStatus() {
		return Status;
	}

	public void setStatus(Integer status) {
		Status = status;
	}

	public String getSName() {
		return SName;
	}

	public void setSName(String sName) {
		SName = sName;
	}

	public Integer getOrderType() {
		return OrderType;
	}

	public void setOrderType(Integer orderType) {
		OrderType = orderType;
	}

	public Integer getSid() {
		return Sid;
	}

	public void setSid(Integer sid) {
		Sid = sid;
	}

	public String getHeadImg() {
		return HeadImg;
	}

	public void setHeadImg(String headImg) {
		HeadImg = headImg;
	}

	public String getSeeDate() {
		return SeeDate;
	}

	public void setSeeDate(String seeDate) {
		SeeDate = seeDate;
	}

	public int getSex() {
		return Sex;
	}

	public void setSex(int sex) {
		Sex = sex;
	}

}
