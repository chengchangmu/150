package com.scsy150.meet.bean;

import java.io.Serializable;

public class MeetInfoBean implements Serializable {

	private static final long serialVersionUID = 2708914693332586720L;

	private int MerchantId;

	private int AcId;

	private String AcName;

	private String LogoImage;

	private String AcTable;

	private String BeginDate;

	private String EndDate;

	private double Distance;

	private int Fqty;

	public void setMerchantId(int MerchantId) {
		this.MerchantId = MerchantId;
	}

	public int getMerchantId() {
		return this.MerchantId;
	}

	public void setAcId(int AcId) {
		this.AcId = AcId;
	}

	public int getAcId() {
		return this.AcId;
	}

	public void setAcName(String AcName) {
		this.AcName = AcName;
	}

	public String getAcName() {
		return this.AcName;
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

	public void setEndDate(String EndDate) {
		this.EndDate = EndDate;
	}

	public String getEndDate() {
		return this.EndDate;
	}

	public void setDistance(double Distance) {
		this.Distance = Distance;
	}

	public double getDistance() {
		return this.Distance;
	}

	public void setFqty(int Fqty) {
		this.Fqty = Fqty;
	}

	public int getFqty() {
		return this.Fqty;
	}

	@Override
	public String toString() {
		return "MeetInfoBean [MerchantId=" + MerchantId + ", AcId=" + AcId
				+ ", AcName=" + AcName + ", LogoImage=" + LogoImage
				+ ", AcTable=" + AcTable + ", BeginDate=" + BeginDate
				+ ", EndDate=" + EndDate + ", Distance=" + Distance + ", Fqty="
				+ Fqty + "]";
	}

}
