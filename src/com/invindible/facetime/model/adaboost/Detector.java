package com.invindible.facetime.model.adaboost;

import java.util.ArrayList;
import java.util.List;

/**
 * 整个训练的分类器
 * @author 李亮灿
 *
 */
public class Detector {
	private int width;		//初始检测窗口的宽度
	private int height;		//初始检测窗口的高度
	private List<StrongClassifier> strongClassifierList;	//该训练器的所有强分类器
	
	public Detector() {
		strongClassifierList = new ArrayList<StrongClassifier>();
	}
	
	/**
	 * 初始检测窗口的宽度
	 * @return
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * 初始检测窗口的宽度
	 * @param width
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	
	/**
	 * 初始检测窗口的高度
	 * @return
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * 初始检测窗口的高度
	 * @param height
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	
	/**
	 * 训练器的所有强分类器
	 * @return
	 */
	public List<StrongClassifier> getStrongClassifierList() {
		return strongClassifierList;
	}
	
	/**
	 * 训练器的所有强分类器
	 * @param strongClassifier
	 */
	public void setStrongClassifierList(List<StrongClassifier> strongClassifierList) {
		this.strongClassifierList = strongClassifierList;
	}
	
	/**
	 * 在强分类器类表后面添加一个新的强分类器
	 * @param strongClassifier
	 */
	public void addStrongClassifier(StrongClassifier strongClassifier) {
		this.strongClassifierList.add(strongClassifier);
	}
	
	/**
	 * 返回指定下标位置的强分类器
	 * @param index
	 * @return
	 */
	public StrongClassifier getStrongClassifier(int index) {
		return this.strongClassifierList.get(index);
	}
	
	
}
