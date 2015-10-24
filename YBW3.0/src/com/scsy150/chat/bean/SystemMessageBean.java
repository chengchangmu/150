package com.scsy150.chat.bean;

import java.io.Serializable;

public class SystemMessageBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1093363545532670674L;
	/**
	 * 1 认证通知2 报名成功3 联谊活动即将开始 4 预约确认 5 现场约确认 6 商家已确认 7 加入初遇点 8 现场约失败 9 现场约成功 10
	 * 预约成功 11 退款成功 12 订单取消 13 消费码待确认
	 */
	private String Typenum;

	private String Title;
	private String Message;
	private String Createdate;
	private String Fk_Id;

	public String getTypenum() {
		return Typenum;
	}

	public void setTypenum(String typenum) {
		Typenum = typenum;
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

	public String getFk_Id() {
		return Fk_Id;
	}

	public void setFk_Id(String fk_Id) {
		Fk_Id = fk_Id;
	}

}
