package com.scsy150.meet.bean;

public class ActivityOfOrganizerBean {
	private int MerchantId;

	private int Acid;

	private String Name;

	private String LogoImage;

	private String County;

	private String AcTable;

	private String BeginDate;

	private double distance;

	private int fqty;

	private int Entrys;

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

	public void setCounty(String County) {
		this.County = County;
	}

	public String getCounty() {
		return this.County;
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

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getDistance() {
		return this.distance;
	}

	public void setFqty(int fqty) {
		this.fqty = fqty;
	}

	public int getFqty() {
		return this.fqty;
	}

	public void setEntrys(int Entrys) {
		this.Entrys = Entrys;
	}

	public int getEntrys() {
		return this.Entrys;
	}
}
