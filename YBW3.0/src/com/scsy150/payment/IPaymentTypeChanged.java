package com.scsy150.payment;

import android.widget.CheckBox;

public interface IPaymentTypeChanged {
	public void onPaymentTypeChanged(CheckBox buttonView,
			boolean isChecked);
}
