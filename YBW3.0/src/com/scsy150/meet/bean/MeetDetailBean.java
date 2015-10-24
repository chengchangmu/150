package com.scsy150.meet.bean;

import java.io.Serializable;

public class MeetDetailBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4290995247187634747L;

	private int MerchantId;

	private int Acid;

	private String Name;

	private String LogoImage;

	private String AcTable;

	private String BeginDate;

	private String OtherAddress;

	private double Longitude;

	private double Latitude;

	private int Manqty;

	private int Wmanqty;

	private double ManCost;

	private double WManCost;

	private String AcDetail;

	private int IsEnd;

	private int IsFull;

	public void setMerchantId(int MerchantId) {
		this.MerchantId = MerchantId;
	}

	public int getMerchantId() {
		return this.MerchantId;
	}

	public void setAcid(int Acid) {
		this.Acid = Acid;
	}

	public int getAcid() {
		return this.Acid;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public String getName() {
		return this.Name;
	}

	public void setLogoImage(String LogoImage) {
		this.LogoImage = LogoImage;
	}

	public String getLogoImage() {
		return this.LogoImage;
	}

	public void setAcTable(String AcTable) {
		this.AcTable = AcTable;
	}

	public String getAcTable() {
		return this.AcTable;
	}

	public void setBeginDate(String BeginDate) {
		this.BeginDate = BeginDate;
	}

	public String getBeginDate() {
		return this.BeginDate;
	}

	public void setOtherAddress(String OtherAddress) {
		this.OtherAddress = OtherAddress;
	}

	public String getOtherAddress() {
		return this.OtherAddress;
	}

	public void setLongitude(double Longitude) {
		this.Longitude = Longitude;
	}

	public double getLongitude() {
		return this.Longitude;
	}

	public void setLatitude(double Latitude) {
		this.Latitude = Latitude;
	}

	public double getLatitude() {
		return this.Latitude;
	}

	public void setManqty(int Manqty) {
		this.Manqty = Manqty;
	}

	public int getManqty() {
		return this.Manqty;
	}

	public void setWmanqty(int Wmanqty) {
		this.Wmanqty = Wmanqty;
	}

	public int getWmanqty() {
		return this.Wmanqty;
	}

	public void setManCost(double ManCost) {
		this.ManCost = ManCost;
	}

	public double getManCost() {
		return this.ManCost;
	}

	public void setWManCost(double WManCost) {
		this.WManCost = WManCost;
	}

	public double getWManCost() {
		return this.WManCost;
	}

	public void setAcDetail(String AcDetail) {
		this.AcDetail = AcDetail;
	}

	public String getAcDetail() {
		return this.AcDetail;
	}

	public void setIsEnd(int IsEnd) {
		this.IsEnd = IsEnd;
	}

	public int getIsEnd() {
		return this.IsEnd;
	}

	public void setIsFull(int IsFull) {
		this.IsFull = IsFull;
	}

	public int getIsFull() {
		return this.IsFull;
	}

}
