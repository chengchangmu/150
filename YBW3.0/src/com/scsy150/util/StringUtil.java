package com.scsy150.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Copyright (C) 
 * 版权所有
 *
 * 功能描述：字符串相关的工具类
 * 作者：硅谷科技
 * 创建时间：2015-08-27
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */
public class StringUtil {

	public static String inputStreamToString(InputStream in) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int count = -1;
		try {
			while ((count = in.read(buffer)) != -1) {
				out.write(buffer, 0, count);
			}
			return new String(buffer, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	/** 
     * 半角转换为全角 
     *  
     * @param input 
     * @return 
     */  
    public static String ToDBC(String input) {  
        char[] c = input.toCharArray();  
        for (int i = 0; i < c.length; i++) {  
            if (c[i] == 12288) {  
                c[i] = (char) 32;  
                continue;  
            }  
            if (c[i] > 65280 && c[i] < 65375)  
                c[i] = (char) (c[i] - 65248);  
        }  
        return new String(c);  
    }
    
    /** 
     * 去除特殊字符或将所有中文标号替换为英文标号 
     *  
     * @param str 
     * @return 
     */  
    public static String stringFilter(String str) {  
        str = str.replaceAll("【", "[").replaceAll("】", "]")  
                .replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号  
        String regEx = "[『』]"; // 清除掉特殊字符  
        Pattern p = Pattern.compile(regEx);  
        Matcher m = p.matcher(str);  
        return m.replaceAll("").trim();  
    }
	
}
