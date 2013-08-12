package com.invindible.facetime.service.interfaces;

import java.awt.Image;

public interface CameraInterface {

	/**
	 * 获取开启摄像头的部件(Component) 
	 */
	public void getCamera();
	
	/**
	 * 获取处理后的图片
	 */
	public Image getHandledPictrue();
	
	/**
	 * 关闭摄像头
	 */
	public void closeCamera();
	
}
