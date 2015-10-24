package com.scsy150.payment;

import java.util.ArrayList;

import android.widget.CheckBox;

public class PaymentTypeSelectManager implements IPaymentTypeChanged {

	private final int tag = this.hashCode();

	private ArrayList<CheckBox> mCheckBoxList;
	private CheckBox mCurrentSelect;

	public PaymentTypeSelectManager() {
		mCheckBoxList = new ArrayList<CheckBox>();
	}

	public void addPaymentTypeCheckBox(CheckBox checkbox) {
		checkbox.setChecked(false);
		mCheckBoxList.add(checkbox);
		checkbox.setTag(tag, mCheckBoxList.indexOf(checkbox));
	}

	public String getSelectItem() {
		if (mCurrentSelect == null) {
			return null;
		}
		return (String) mCurrentSelect.getTag();
	}

	@Override
	public void onPaymentTypeChanged(CheckBox buttonView, boolean isChecked) {
		if (isChecked) {
			mCurrentSelect = buttonView;
			for (CheckBox box : mCheckBoxList) {
				if (!buttonView.getTag(tag).equals(box.getTag(tag))) {
					if (box.isChecked()) {
						box.setChecked(false);
					}
				}
			}
		}
	}
}
