package com.scsy150.date.bean;

import java.io.Serializable;

public class NowUserBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 80368748403856451L;
	private int UserId;// userId
	private String NickName;// 用户名称
	private String HeadImg;// 用户头像
	private String BkgdImg;// 背景图
	private int Sex;// 性别
	private int GroupBrithday;// 年龄段
	private String Talk;// 签名
	private String RecordImg;// 录音
	private double Distance;// 相聚多少米
	private int IsFriend;// 好友
	private int IsAttention;// 是否喜欢
	private int PhotoId;// 用户id
	private String PhotoImage;// 相册url
	private String Estimate;// 评价标签

	public int getUserId() {
		return UserId;
	}

	public void setUserId(int userId) {
		UserId = userId;
	}

	public String getNickName() {
		return NickName;
	}

	public void setNickName(String nickName) {
		NickName = nickName;
	}

	public String getHeadImg() {
		return HeadImg;
	}

	public void setHeadImg(String headImg) {
		HeadImg = headImg;
	}

	public String getBkgdImg() {
		return BkgdImg;
	}

	public void setBkgdImg(String bkgdImg) {
		BkgdImg = bkgdImg;
	}

	public int getSex() {
		return Sex;
	}

	public void setSex(int sex) {
		Sex = sex;
	}

	public int getGroupBrithday() {
		return GroupBrithday;
	}

	public void setGroupBrithday(int groupBrithday) {
		GroupBrithday = groupBrithday;
	}

	public String getTalk() {
		return Talk;
	}

	public void setTalk(String talk) {
		Talk = talk;
	}

	public String getRecordImg() {
		return RecordImg;
	}

	public void setRecordImg(String recordImg) {
		RecordImg = recordImg;
	}

	public double getDistance() {
		return Distance;
	}

	public void setDistance(double distance) {
		Distance = distance;
	}

	public int getIsFriend() {
		return IsFriend;
	}

	public void setIsFriend(int isFriend) {
		IsFriend = isFriend;
	}

	public int getIsAttention() {
		return IsAttention;
	}

	public void setIsAttention(int isAttention) {
		IsAttention = isAttention;
	}

	public int getPhotoId() {
		return PhotoId;
	}

	public void setPhotoId(int photoId) {
		PhotoId = photoId;
	}

	public String getPhotoImage() {
		return PhotoImage;
	}

	public void setPhotoImage(String photoImage) {
		PhotoImage = photoImage;
	}

	public String getEstimate() {
		return Estimate;
	}

	public void setEstimate(String estimate) {
		Estimate = estimate;
	}

}
