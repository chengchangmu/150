package com.scsy150.util;

import android.view.View;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

public class AnimationUtil {

	public static void playTranslateAnim(View view, float x, float y) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", x, y);  
		//animator.setDuration(5000);  
		animator.start();  
	}
	
	public static void playScaleAnim(View view, float x, float y) {
		ViewHelper.setScaleX(view, x);
		ViewHelper.setScaleY(view, y);
	}
}
