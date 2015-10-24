package com.scsy150.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RatioImageView extends ImageView{
	private float ratio ;//宽高比
	public RatioImageView(Context context) {
		this(context, null);
	}
	public RatioImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	public RatioImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		ratio = attrs.getAttributeFloatValue("http://schemas.android.com/apk/res/com.scsy150", "ratio", 0);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);//ImageView的宽度
		if(getRatio()!=0){
			float height = width/getRatio();//得到高度
			heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) height, MeasureSpec.EXACTLY);
		}
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	public float getRatio() {
		return ratio;
	}
	public void setRatio(float ratio) {
		this.ratio = ratio;
	}
	
}
