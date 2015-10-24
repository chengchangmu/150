package com.scsy150.mine.bean;

import java.io.Serializable;

public class IngMeetBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 933309793986896950L;

	/**
	 * 活动ID
	 */
	private int Acid;

	/**
	 * 订单ID
	 */
	private int Orderid;
	/**
	 * 订单类型 1：联谊
	 */
	private int Ordertype;
	/**
	 * 活动名称
	 */
	private String Activityname;
	/**
	 * 开始时间
	 */
	private String Begindate;
	/**
	 * 详细地址
	 */
	private String Otheraddress;

	/**
	 * 头像
	 */
	private String Logoimg;

	public int getAcid() {
		return Acid;
	}

	public void setAcid(int acid) {
		Acid = acid;
	}

	public int getOrderid() {
		return Orderid;
	}

	public void setOrderid(int orderid) {
		Orderid = orderid;
	}

	public int getOrdertype() {
		return Ordertype;
	}

	public void setOrdertype(int ordertype) {
		Ordertype = ordertype;
	}

	public String getActivityname() {
		return Activityname;
	}

	public void setActivityname(String activityname) {
		Activityname = activityname;
	}

	public String getBegindate() {
		return Begindate;
	}

	public void setBegindate(String begindate) {
		Begindate = begindate;
	}

	public String getOtheraddress() {
		return Otheraddress;
	}

	public void setOtheraddress(String otheraddress) {
		Otheraddress = otheraddress;
	}

	public String getLogoimg() {
		return Logoimg;
	}

	public void setLogoimg(String logoimg) {
		Logoimg = logoimg;
	}

}
