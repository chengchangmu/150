package com.scsy150.mine.bean;

import java.io.Serializable;

public class FirstPointBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5217208585331551662L;
	/**
	 * 商家ID
	 */
	private int Merchantid;
	/**
	 * 商家名称
	 */
	private String Mhname;
	/**
	 * 商家头像
	 */
	private String Headimg;

	/**
	 * 距离
	 */
	private String Dicname;
	/**
	 * 参与人数
	 */
	private int Fqty;
	public int getMerchantid() {
		return Merchantid;
	}
	public void setMerchantid(int merchantid) {
		Merchantid = merchantid;
	}
	public String getMhname() {
		return Mhname;
	}
	public void setMhname(String mhname) {
		Mhname = mhname;
	}
	public String getHeadimg() {
		return Headimg;
	}
	public void setHeadimg(String headimg) {
		Headimg = headimg;
	}
	public String getDicname() {
		return Dicname;
	}
	public void setDicname(String dicname) {
		Dicname = dicname;
	}
	public int getFqty() {
		return Fqty;
	}
	public void setFqty(int fqty) {
		Fqty = fqty;
	}

}
