package com.invindible.facetime.model.adaboost;

/**
 * 特征矩阵
 * @author 李亮灿
 *
 */
public class Rectangle {
	
	private int x;		//矩形的左上角顶点的横坐标
	private int y;		//矩形的左上角顶点的纵坐标
	private int width;	//矩形的宽度
	private int height;	//矩形的高度
	private int weight;	//矩形的权值
	
	/**
	 * 矩形的左上角顶点的横坐标
	 * @return
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * 矩形的左上角顶点的横坐标
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * 矩形的左上角顶点的纵坐标
	 * @return
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * 矩形的左上角顶点的纵坐标
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * 矩形的宽度
	 * @return
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * 矩形的宽度
	 * @param width
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	
	/**
	 * 矩形的高度
	 * @return
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * 矩形的高度
	 * @param height
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	
	/**
	 * 矩形的权值
	 * @return
	 */
	public int getWeight() {
		return weight;
	}
	
	/**
	 * 矩形的权值
	 * @param weight
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
}
