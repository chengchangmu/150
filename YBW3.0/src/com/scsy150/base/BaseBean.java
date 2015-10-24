package com.scsy150.base;

import java.io.Serializable;

public class BaseBean<E> implements Serializable {

	private static final long serialVersionUID = -3487370318362864922L;
	private int IsSuccess;
	private String Message;

	private E Result;

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public E getResult() {
		return Result;
	}

	public void setResult(E result) {
		Result = result;
	}

	public int getIsSuccess() {
		return IsSuccess;
	}

	public void setIsSuccess(int isSuccess) {
		IsSuccess = isSuccess;
	}
}
