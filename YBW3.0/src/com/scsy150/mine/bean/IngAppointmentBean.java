package com.scsy150.mine.bean;

import java.io.Serializable;

public class IngAppointmentBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 933309793986896950L;

	/**
	 * 我的UserID
	 */
	private int Myid;
	/**
	 * 我的名字
	 */
	private String Myname;
	/**
	 * 我的头像
	 */
	private String Myimage;
	/**
	 * 对方UserID
	 */
	private int Youid;

	/**
	 * 对方名字
	 */
	private String Youname;
	/**
	 * 对方头像
	 */
	private String Youimage;
	/**
	 * 套餐ID
	 */
	private int Acid;
	/**
	 * 订单ID
	 */
	private int Orderid;

	/**
	 * 订单类型 3：预约
	 */
	private int Ordertype;
	/**
	 * 商家名称
	 */
	private String Mhname;

	/**
	 * 商家头像
	 */
	private String Merchantimage;

	/**
	 * 商家确认 0：未确认，1：确认
	 */
	private int Isapprove;

	/**
	 * 商家确认（你） 0：未确认，1：确认
	 */
	private int Youapprove;
	/**
	 * 商家确认（我） 0：未确认，1：确认
	 */
	private int Myapprove;

	/**
	 * 商家地址
	 */
	private String Otheraddress;
	/**
	 * 开始时间
	 */
	private String Begindate;

	public int getMyid() {
		return Myid;
	}

	public void setMyid(int myid) {
		Myid = myid;
	}

	public String getMyname() {
		return Myname;
	}

	public void setMyname(String myname) {
		Myname = myname;
	}

	public String getMyimage() {
		return Myimage;
	}

	public void setMyimage(String myimage) {
		Myimage = myimage;
	}

	public int getYouid() {
		return Youid;
	}

	public void setYouid(int youid) {
		Youid = youid;
	}

	public String getYouname() {
		return Youname;
	}

	public void setYouname(String youname) {
		Youname = youname;
	}

	public String getYouimage() {
		return Youimage;
	}

	public void setYouimage(String youimage) {
		Youimage = youimage;
	}

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

	public String getMhname() {
		return Mhname;
	}

	public void setMhname(String mhname) {
		Mhname = mhname;
	}

	public String getMerchantimage() {
		return Merchantimage;
	}

	public void setMerchantimage(String merchantimage) {
		Merchantimage = merchantimage;
	}

	public int getIsapprove() {
		return Isapprove;
	}

	public void setIsapprove(int isapprove) {
		Isapprove = isapprove;
	}

	public int getYouapprove() {
		return Youapprove;
	}

	public void setYouapprove(int youapprove) {
		Youapprove = youapprove;
	}

	public int getMyapprove() {
		return Myapprove;
	}

	public void setMyapprove(int myapprove) {
		Myapprove = myapprove;
	}

	public String getOtheraddress() {
		return Otheraddress;
	}

	public void setOtheraddress(String otheraddress) {
		Otheraddress = otheraddress;
	}

	public String getBegindate() {
		return Begindate;
	}

	public void setBegindate(String begindate) {
		Begindate = begindate;
	}


}
