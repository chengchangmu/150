package com.scsy150.date.bean;

public class DatingSitesBean {

	private String bs_headUrl;//商家头像
	private String DatingSitesName;// 商家名称
	private int expenditure;// 消费额
	private String bs_type;// 商家类型
	private String bs_adress;// 商家地址

	public String getDatingSitesName() {
		return DatingSitesName;
	}

	public void setDatingSitesName(String datingSitesName) {
		DatingSitesName = datingSitesName;
	}

	public int getExpenditure() {
		return expenditure;
	}

	public void setExpenditure(int expenditure) {
		this.expenditure = expenditure;
	}

	public String getBs_type() {
		return bs_type;
	}

	public void setBs_type(String bs_type) {
		this.bs_type = bs_type;
	}

	public String getBs_adress() {
		return bs_adress;
	}

	public void setBs_adress(String bs_adress) {
		this.bs_adress = bs_adress;
	}

}
