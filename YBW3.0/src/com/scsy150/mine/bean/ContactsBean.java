package com.scsy150.mine.bean;

import java.io.Serializable;

public class ContactsBean implements Serializable {

	/**
	 * 
	 */
	public static final long serialVersionUID = -4631422771648417422L;
	/**
	 * 用户ID
	 */
	public int Userid;
	/**
	 * 用户名称
	 */
	public String Nickname;
	/**
	 * 性别 0：女，1：男
	 */
	public int Sex;
	/**
	 * 年龄段 80,90后等等
	 */
	public int Age;
	/**
	 * 签名
	 */
	public String Talk;

	/**
	 * 头像
	 */
	public String Headimg;

	public int getUserid() {
		return Userid;
	}

	public void setUserid(int userid) {
		Userid = userid;
	}

	public String getNickname() {
		return Nickname;
	}

	public void setNickname(String nickname) {
		Nickname = nickname;
	}

	public int getSex() {
		return Sex;
	}

	public void setSex(int sex) {
		Sex = sex;
	}


	public String getTalk() {
		return Talk;
	}

	public void setTalk(String talk) {
		Talk = talk;
	}


	public int getAge() {
		return Age;
	}

	public void setAge(int age) {
		Age = age;
	}

	public String getHeadimg() {
		return Headimg;
	}

	public void setHeadimg(String headimg) {
		Headimg = headimg;
	}
	
	

}
