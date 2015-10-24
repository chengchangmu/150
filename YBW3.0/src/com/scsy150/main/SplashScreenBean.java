package com.scsy150.main;

import com.scsy150.base.BaseBean;


public class SplashScreenBean extends BaseBean{
	private static final long serialVersionUID = 1L;
	public String PictureUrl;//图片URL
	public String Link;//目标页面的URL
	public String StartTime;//开始生效时间，如果为null，则不限制
	public String EndTime;//过期时间，如果为null，代表永远不过期
}
