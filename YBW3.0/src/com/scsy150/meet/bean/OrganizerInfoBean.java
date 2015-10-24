package com.scsy150.meet.bean;

public class OrganizerInfoBean {
	private String Mhname;

	private double Score;

	private String HeadImg;

	private String bkgdImg;

	private int IsAttention;

	public void setMhname(String Mhname) {
		this.Mhname = Mhname;
	}

	public String getMhname() {
		return this.Mhname;
	}

	public void setScore(double Score) {
		this.Score = Score;
	}

	public double getScore() {
		return this.Score;
	}

	public void setHeadImg(String HeadImg) {
		this.HeadImg = HeadImg;
	}

	public String getHeadImg() {
		return this.HeadImg;
	}

	public void setBkgdImg(String bkgdImg) {
		this.bkgdImg = bkgdImg;
	}

	public String getBkgdImg() {
		return this.bkgdImg;
	}

	public void setIsAttention(int IsAttention) {
		this.IsAttention = IsAttention;
	}

	public int getIsAttention() {
		return this.IsAttention;
	}

	@Override
	public String toString() {
		return "OrganizerInfoBean [Mhname=" + Mhname + ", Score=" + Score
				+ ", HeadImg=" + HeadImg + ", bkgdImg=" + bkgdImg
				+ ", IsAttention=" + IsAttention + "]";
	}
	
}
