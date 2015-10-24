package com.scsy150.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.scsy150.R;

/**
 * 
 * @author 硅谷科技
 * @see 右边带有清除数据功能按钮的EditText,该EditText不能设置drawableRight
 */
public class EditTextDel extends EditText {
	private Drawable mDelDrawable;
	
	public EditTextDel(Context context) {
		super(context);
		init();
	}

	public EditTextDel(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public EditTextDel(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init(){
		mDelDrawable = getResources().getDrawable(R.drawable.btn_close_red_gray_selector);
		addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				setDrawable(isFocused());
			}
		});
		setDrawable(isFocused());
	}
	
	private void setDrawable(boolean hasWindowFocus){
		Drawable[] drawable = getCompoundDrawables();
		if(length() > 0 && hasWindowFocus){
			setCompoundDrawablesWithIntrinsicBounds(drawable[0], drawable[1], mDelDrawable, drawable[3]);
		} else {
			setCompoundDrawablesWithIntrinsicBounds(drawable[0], drawable[1], null, drawable[3]);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(mDelDrawable != null && event.getAction() == MotionEvent.ACTION_UP){
			int x = (int) event.getRawX();
			int y = (int) event.getRawY();
			Rect rect = new Rect();
			getGlobalVisibleRect(rect);
			if(isFocused()){
				int height = rect.bottom - rect.top;
				rect.top = rect.bottom - height / 2 - mDelDrawable.getIntrinsicHeight() / 2 - 25;
				rect.bottom = rect.top + mDelDrawable.getIntrinsicHeight() + 25;
				rect.left = rect.right - mDelDrawable.getIntrinsicWidth() - 25;
				rect.right = rect.right + 25;
				if(rect.contains(x, y)){
					setText("");
				}
			}
			setDrawable(isFocused());
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
		setDrawable(focused);
	}
	
}
