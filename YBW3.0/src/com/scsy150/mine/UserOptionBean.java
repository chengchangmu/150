package com.scsy150.mine;

import java.io.Serializable;

import android.graphics.drawable.Drawable;

public class UserOptionBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3661858159832695095L;
	private Drawable pic;

	public Drawable getPic() {
		return pic;
	}

	public void setPic(Drawable pic) {
		this.pic = pic;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;

}
