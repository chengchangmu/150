package com.scsy150.mine.bean;

import java.io.Serializable;

public class InfoBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2604608214172220847L;

	/**
	 * 用户编号
	 */
	private String UserNum;
	/**
	 * 用户电话号码
	 */
	private String PhoneNum;

	/**
	 * 用户名称
	 */
	private String NickName;
	/**
	 * 用户头像
	 */
	private String HeadImg;
	/**
	 * 用户背景图
	 */
	private String BkgdImg;

	/**
	 * 性别
	 */
	private int Sex;
	/**
	 * 出生日期
	 */
	private String Brithday;
	/**
	 * 学校
	 */
	private String School;
	/**
	 * 签名
	 */
	private String Talk;
	/**
	 * 录音
	 */
	private String RecordImg;
	/**
	 * 账户金额
	 */
	private double UAmount;
	/**
	 * 账户积分
	 */
	private int Integral;
	public String getUserNum() {
		return UserNum;
	}
	public void setUserNum(String userNum) {
		UserNum = userNum;
	}
	public String getPhoneNum() {
		return PhoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		PhoneNum = phoneNum;
	}
	public String getNickName() {
		return NickName;
	}
	public void setNickName(String nickName) {
		NickName = nickName;
	}
	public String getHeadImg() {
		return HeadImg;
	}
	public void setHeadImg(String headImg) {
		HeadImg = headImg;
	}
	public String getBkgdImg() {
		return BkgdImg;
	}
	public void setBkgdImg(String bkgdImg) {
		BkgdImg = bkgdImg;
	}
	public int getSex() {
		return Sex;
	}
	public void setSex(int sex) {
		Sex = sex;
	}
	public String getBrithday() {
		return Brithday;
	}
	public void setBrithday(String brithday) {
		Brithday = brithday;
	}
	public String getSchool() {
		return School;
	}
	public void setSchool(String school) {
		School = school;
	}
	public String getTalk() {
		return Talk;
	}
	public void setTalk(String talk) {
		Talk = talk;
	}
	public String getRecordImg() {
		return RecordImg;
	}
	public void setRecordImg(String recordImg) {
		RecordImg = recordImg;
	}
	public double getUAmount() {
		return UAmount;
	}
	public void setUAmount(double uAmount) {
		UAmount = uAmount;
	}
	public int getIntegral() {
		return Integral;
	}
	public void setIntegral(int integral) {
		Integral = integral;
	}

}
