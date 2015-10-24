package com.scsy150.mine.bean;

import java.io.Serializable;

public class PicBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2645535659944406286L;
	private int Photoid;
	private String Photoimage;
	private int Isapprove;

	public int getPhotoid() {
		return Photoid;
	}

	public void setPhotoid(int photoid) {
		Photoid = photoid;
	}

	public String getPhotoimage() {
		return Photoimage;
	}

	public void setPhotoimage(String photoimage) {
		Photoimage = photoimage;
	}

	public int getIsapprove() {
		return Isapprove;
	}

	public void setIsapprove(int isapprove) {
		Isapprove = isapprove;
	}

}
