package com.scsy150.util;

import java.util.Random;

import android.graphics.Color;

public class ColorUtil {
	/**
	 * 生成漂亮的颜色
	 * @return
	 */
	public static int generateBeautifulColor(){
		//颜色的范围是0-255，为使生成的颜色不至于太暗或太亮，所以取中间的值
		Random random = new Random();
		int red = random.nextInt(150)+50;//50-199
		int green = random.nextInt(150)+50;
		int blue = random.nextInt(150)+50;
		return Color.rgb(red, green, blue);//使用rgb混合出一种新的颜色
	}
	
	public static Object evaluateColor(float fraction, Object startValue,
			Object endValue) {
		int startInt = (Integer) startValue;
		int startA = (startInt >> 24) & 0xff;
		int startR = (startInt >> 16) & 0xff;
		int startG = (startInt >> 8) & 0xff;
		int startB = startInt & 0xff;

		int endInt = (Integer) endValue;
		int endA = (endInt >> 24) & 0xff;
		int endR = (endInt >> 16) & 0xff;
		int endG = (endInt >> 8) & 0xff;
		int endB = endInt & 0xff;

		return (int) ((startA + (int) (fraction * (endA - startA))) << 24)
				| (int) ((startR + (int) (fraction * (endR - startR))) << 16)
				| (int) ((startG + (int) (fraction * (endG - startG))) << 8)
				| (int) ((startB + (int) (fraction * (endB - startB))));
	}
}
