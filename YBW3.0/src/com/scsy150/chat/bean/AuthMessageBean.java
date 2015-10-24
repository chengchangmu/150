package com.scsy150.chat.bean;

import java.io.Serializable;

public class AuthMessageBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7455238214369164981L;
	private int RowNo;
	private String Title;
	private String Message;
	private String Createdate;

	public int getRowNo() {
		return RowNo;
	}

	public void setRowNo(int rowNo) {
		RowNo = rowNo;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public String getCreatedate() {
		return Createdate;
	}

	public void setCreatedate(String createdate) {
		Createdate = createdate;
	}

}
