package com.invindible.facetime.service.interfaces;

public interface FindVideoFaceInterface {

	/**
	 * 查找视频中的某一个人脸成功
	 * <br>在onRefresh中返回的参数是faceImage类型
	 */
	public static final int FIND_VIDEO_FACE_SUCCESS = 40000;
	
	/**
	 * 打开视频失败
	 */
	public static final int OPEN_VIDEO_FAIL = 40001;
	
	/**
	 * 查找完成结束
	 */
	public static final int FIND_VIDEO_FACE_FINISH = 40002;
	
	/**
	 * 查找一个视频中的所有人脸，
	 * <br>查找的结果会在onRefresh中一张一张的返回结果，结果为FaceImage类型的数据
	 */
	public void findFace();
	
	/**
	 * 停止查找人脸
	 */
	public void stop();
	
}
