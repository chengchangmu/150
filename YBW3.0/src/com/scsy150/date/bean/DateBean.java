package com.scsy150.date.bean;

import java.io.Serializable;

public class DateBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -891584424085836499L;

	/**
	 * 
	 */
	public int IsSuccess;
	private int UserId;// userID

	private int Calling; /* 0 */
	private int GroupBrithday; /* 0 */
	private double distance; /* 157425.177 */
	private String LastLoginTime; /* 2015/9/1 17:13:41 */
	private String headImg; /* HeadImg/2015012711471619525646.jpg */
	private String NickName; /* 猫语 */
	private double Latitude; /* 0 */
	private double Longitude; /* 0 */
	private int Sex; /* 0 */
	private int MerchantId;// 商家id
	private int sid;// 套餐
	private String Mhname;// 商家名称
	private String SName;// 套餐名称
	private int scost;// 套餐价格
	private int serviceCost;// 服务费
	private int totalCost;// 合计费用
	private String MerchantImage;// 商家头像
	private String SetMealImage;// 套餐图片
	private String MerchantType;// 商家类型
	private int Score;// 商家评分
	private int ScoreFqty;// 评分人数
	private String OtherAddress;// 商家地址
	private String County;// 地区
	private String PhotoImg;// 商家环境图片
	private int Id;//订单id
	private int Couponost;//优惠券
	private int Activedays;//有效期
	private int UAmount;

	public int getUAmount() {
		return UAmount;
	}

	public void setUAmount(int uAmount) {
		UAmount = uAmount;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public int getCouponost() {
		return Couponost;
	}

	public void setCouponost(int couponost) {
		Couponost = couponost;
	}

	public int getActivedays() {
		return Activedays;
	}

	public void setActivedays(int activedays) {
		Activedays = activedays;
	}

	public String getPhotoImg() {
		return PhotoImg;
	}

	public void setPhotoImg(String photoImg) {
		PhotoImg = photoImg;
	}

	public String getCounty() {
		return County;
	}

	public void setCounty(String county) {
		County = county;
	}

	public String getMerchantImage() {
		return MerchantImage;
	}

	public void setMerchantImage(String merchantImage) {
		MerchantImage = merchantImage;
	}

	public String getSetMealImage() {
		return SetMealImage;
	}

	public void setSetMealImage(String setMealImage) {
		SetMealImage = setMealImage;
	}

	public String getMerchantType() {
		return MerchantType;
	}

	public void setMerchantType(String merchantType) {
		MerchantType = merchantType;
	}

	public int getScore() {
		return Score;
	}

	public void setScore(int score) {
		Score = score;
	}

	public int getScoreFqty() {
		return ScoreFqty;
	}

	public void setScoreFqty(int scoreFqty) {
		ScoreFqty = scoreFqty;
	}

	public String getOtherAddress() {
		return OtherAddress;
	}

	public void setOtherAddress(String otherAddress) {
		OtherAddress = otherAddress;
	}

	public int getUserId() {
		return UserId;
	}

	public void setUserId(int userId) {
		UserId = userId;
	}

	public int getMerchantId() {
		return MerchantId;
	}

	public void setMerchantId(int merchantId) {
		MerchantId = merchantId;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public String getMhname() {
		return Mhname;
	}

	public void setMhname(String mhname) {
		Mhname = mhname;
	}

	public String getSName() {
		return SName;
	}

	public void setSName(String sName) {
		SName = sName;
	}

	public int getScost() {
		return scost;
	}

	public void setScost(int scost) {
		this.scost = scost;
	}

	public int getServiceCost() {
		return serviceCost;
	}

	public void setServiceCost(int serviceCost) {
		this.serviceCost = serviceCost;
	}

	public int getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(int totalCost) {
		this.totalCost = totalCost;
	}

	public int getCalling() {
		return Calling;
	}

	public void setCalling(int calling) {
		Calling = calling;
	}

	public int getGroupBrithday() {
		return GroupBrithday;
	}

	public void setGroupBrithday(int groupBrithday) {
		GroupBrithday = groupBrithday;
	}

	public String getLastLoginTime() {
		return LastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		LastLoginTime = lastLoginTime;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getNickName() {
		return NickName;
	}

	public void setNickName(String nickName) {
		NickName = nickName;
	}

	public int getSex() {
		return Sex;
	}

	public void setSex(int sex) {
		Sex = sex;
	}

	public int getIsSuccess() {
		return IsSuccess;
	}

	public void setIsSuccess(int isSuccess) {
		IsSuccess = isSuccess;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getLatitude() {
		return Latitude;
	}

	public void setLatitude(double latitude) {
		Latitude = latitude;
	}

	public double getLongitude() {
		return Longitude;
	}

	public void setLongitude(double longitude) {
		Longitude = longitude;
	}

}
