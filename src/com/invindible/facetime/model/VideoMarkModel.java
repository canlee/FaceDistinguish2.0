package com.invindible.facetime.model;

public class VideoMarkModel {
	private int mark; //标记识别为第几个人
	private double[] dis; //标记4个距离 分别是l2 l1 马氏 最小距离
	public int getMark() {
		return mark;
	}
	public void setMark(int mark) {
		this.mark = mark;
	}
	public double[] getDis() {
		return dis;
	}
	public void setDis(double[] dis) {
		this.dis = dis;
	}
}
