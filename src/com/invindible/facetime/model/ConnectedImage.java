package com.invindible.facetime.model;

/**
 * 连通图实体类
 * @author 李亮灿
 *
 */
public class ConnectedImage {

	private int topx;
	private int topy;
	private int bottomx;
	private int bottomy;
	private int[][][] rgbMat;
	
	public ConnectedImage() {
		this.topx = -1;
		this.topy = -1;
		this.bottomx = -1;
		this.bottomy = -1;
		rgbMat = null;
	}

	/**
	 * 连通图在原图的左上角的x坐标
	 * @return
	 */
	public int getTopx() {
		return topx;
	}

	/**
	 * 连通图在原图的左上角的x坐标
	 * @param topx
	 */
	public void setTopx(int topx) {
		this.topx = topx;
	}

	/**
	 * 连通图在原图的左上角的y坐标
	 * @return
	 */
	public int getTopy() {
		return topy;
	}

	/**
	 * 连通图在原图的左上角的y坐标
	 * @param topy
	 */
	public void setTopy(int topy) {
		this.topy = topy;
	}

	/**
	 * 连通图在原图的右下角的x坐标
	 * @return
	 */
	public int getBottomx() {
		return bottomx;
	}

	/**
	 * 连通图在原图的右下角的x坐标
	 * @param bottomx
	 */
	public void setBottomx(int bottomx) {
		this.bottomx = bottomx;
	}

	/**
	 * 连通图在原图的右下角的y坐标
	 * @return
	 */
	public int getBottomy() {
		return bottomy;
	}

	/**
	 * 连通图在原图的右下角的y坐标
	 * @param bottomy
	 */
	public void setBottomy(int bottomy) {
		this.bottomy = bottomy;
	}

	/**
	 * 整个连通图的rgb图像
	 * @return
	 */
	public int[][][] getRgbMat() {
		return rgbMat;
	}

	/**
	 * 整个连通图的rgb图像
	 * @param rgbMat
	 */
	public void setRgbMat(int[][][] rgbMat) {
		this.rgbMat = rgbMat;
	}
	
}
