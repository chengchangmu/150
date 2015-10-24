package com.scsy150.mine.bean;

import java.io.Serializable;

public class WalletBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6769256788628231847L;

	private int Id;
	private double Couponost;
	private int Activedays;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public double getCouponost() {
		return Couponost;
	}

	public void setCouponost(double couponost) {
		Couponost = couponost;
	}

	public int getActivedays() {
		return Activedays;
	}

	public void setActivedays(int activedays) {
		Activedays = activedays;
	}

}
