package com.scsy150.account;

import java.io.Serializable;

public class UserInfoBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3661858159832695095L;
	private int UserId;
	private String UserNum;
	private String NickName;
	private String HeadImg;
	private int Sex;




	public int getUserId() {
		return UserId;
	}

	public void setUserId(int userId) {
		UserId = userId;
	}

	public int getSex() {
		return Sex;
	}

	public void setSex(int sex) {
		Sex = sex;
	}

	public String getUserNum() {
		return UserNum;
	}

	public void setUserNum(String userNum) {
		UserNum = userNum;
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



}
