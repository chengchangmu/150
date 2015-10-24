package com.scsy150.meet.bean;

import java.io.Serializable;

public class MeetPictureBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2258580138867997994L;

	private int PhonoId;

	private String AcImg;

	public void setPhonoId(int PhonoId) {
		this.PhonoId = PhonoId;
	}

	public int getPhonoId() {
		return this.PhonoId;
	}

	public void setAcImg(String AcImg) {
		this.AcImg = AcImg;
	}

	public String getAcImg() {
		return this.AcImg;
	}
}
