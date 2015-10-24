package com.scsy150.dialog;

import android.view.View.OnClickListener;

import com.scsy150.base.BaseBean;


public class DialogBean extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -145588054436772818L;
	
	public String Title;
	public int TitleRes;
	public String Content;
	public int  ContentRes;
	public String LeftTxt;
	public int LeftRes;
	public String RightTxt;
	public int RightRes;
	public OnClickListener LeftListener;
	public OnClickListener RightListener;
}
