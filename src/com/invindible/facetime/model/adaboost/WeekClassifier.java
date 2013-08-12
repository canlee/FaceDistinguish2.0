package com.invindible.facetime.model.adaboost;

import java.util.ArrayList;
import java.util.List;

/**
 * 弱分类器
 * @author 李亮灿
 *
 */
public class WeekClassifier {
	
	private List<Rectangle> rectangleList;		//特征矩阵模板
	private double threshold;					//弱矩阵的阈值
	private double leftValue;					//左值
	private double rightValue;					//右值
	
	public WeekClassifier() {
		this.rectangleList = new ArrayList<Rectangle>();
	}
	
	/**
	 * 弱矩阵的阈值
	 * @return
	 */
	public double getThreshold() {
		return threshold;
	}
	
	/**
	 * 弱矩阵的阈值
	 * @param threshold
	 */
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}
	
	/**
	 * 左值
	 * @return
	 */
	public double getLeftValue() {
		return leftValue;
	}
	
	/**
	 * 左值
	 * @param leftValue
	 */
	public void setLeftValue(double leftValue) {
		this.leftValue = leftValue;
	}
	
	/**
	 * 右值
	 * @return
	 */
	public double getRightValue() {
		return rightValue;
	}
	
	/**
	 * 右值
	 * @param rightValue
	 */
	public void setRightValue(double rightValue) {
		this.rightValue = rightValue;
	}

	/**
	 * 特征矩阵模板
	 * @return
	 */
	public List<Rectangle> getRectangleList() {
		return rectangleList;
	}

	/**
	 * 特征矩阵模板
	 * @param rectangle
	 */
	public void setRectangleList(List<Rectangle> rectangle) {
		this.rectangleList = rectangle;
	}
	
	/**
	 * 添加特征矩阵模板到列表最后
	 * @param rect
	 */
	public void addRectangle(Rectangle rect) {
		this.rectangleList.add(rect);
	}
	
	/**
	 * 获取指定下标的矩阵列表中的模板
	 * @param index
	 * @return
	 */
	public Rectangle getRectangle(int index) {
		return this.rectangleList.get(index);
	}
	
}
