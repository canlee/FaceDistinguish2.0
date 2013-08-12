package com.invindible.facetime.service.interfaces;

import java.awt.Image;
import java.awt.image.BufferedImage;

import com.invindible.facetime.model.FaceImage;

/**
 * 图片人脸检测的接口
 * @author 李亮灿
 *
 */
public interface FindFaceInterface {
	
	/**
	 * 检测人脸成功
	 */
	public static final int FIND_FACE_SUCCESS = 30000;
	
	/**
	 * 获取到人脸结果的固定大小
	 */
	public static final int FACE_SIZE = 256;
	
	/**
	 * 开始检测线程
	 */
	public void start();
	
	/**
	 * 把图片存进检测列表，
	 * <br>当检测完成时会在onRefresh()方法中返回参数为FIND_FACE_SUCCESS整型
	 * <br>第二个参数为FaceImage类的对象
	 * @see	FIND_FACE_SUCCESS
	 * @see FaceImage
	 * @param image
	 */
	public void findFace(Image image);
	
	/**
	 * 把图片存进检测列表，
	 * <br>当检测完成时会在onRefresh()方法中返回参数为FIND_FACE_SUCCESS整型
	 * <br>第二个参数为FaceImage类的对象
	 * @see	FIND_FACE_SUCCESS
	 * @see FaceImage
	 * @param image
	 * @param time	记录图片的截取时间
	 */
	public void findFace(BufferedImage image, long time);
	
	/**
	 * 正在查找人脸的队列长度
	 * @return
	 */
	public int getQueueSize();
	
	/**
	 * 停止检测线程
	 */
	public void stop();
}
