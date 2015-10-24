package com.scsy150.meet.bean;

import java.io.Serializable;

public class MeetEnrolledMemberBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1087433111665105574L;

	private int UserId;

	private String NickName;
	
	private String HeadImg;

	private int Sex;

	private String Brithday;

	private String AgePassage;

	public void setUserId(int UserId) {
		this.UserId = UserId;
	}

	public int getUserId() {
		return this.UserId;
	}

	public void setNickName(String NickName) {
		this.NickName = NickName;
	}

	public String getNickName() {
		return this.NickName;
	}
	
	public String getHeadImg() {
		return HeadImg;
	}

	public void setHeadImg(String headImg) {
		HeadImg = headImg;
	}

	public void setSex(int Sex) {
		this.Sex = Sex;
	}

	public int getSex() {
		return this.Sex;
	}

	public void setBrithday(String Brithday) {
		this.Brithday = Brithday;
	}

	public String getBrithday() {
		return this.Brithday;
	}

	public void setAgePassage(String AgePassage) {
		this.AgePassage = AgePassage;
	}

	public String getAgePassage() {
		return this.AgePassage;
	}
}
