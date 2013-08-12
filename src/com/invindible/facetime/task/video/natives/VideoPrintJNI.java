package com.invindible.facetime.task.video.natives;

/**
 * 利用ffmpeg接口截屏本地方法
 * @author 李亮灿
 *
 */
public abstract class VideoPrintJNI {
	
	/**
	 * 是否开始，如果开始后设置为 false 则停止截图
	 */
	protected boolean isStart;
	
	public VideoPrintJNI() {
		isStart = false;
	}

	/**
	 * 开始将指定视频截图，结果回调在getScreenResult方法中
	 * @param videoFile	视频文件路径
	 * @param space	每隔多少时间截取一次，单位为毫秒
	 * @see getScreenResult
	 */
	protected native void start(String videoFile, long space);
	
	/**
	 * 停止视频截取
	 */
	public void stop() {
		isStart = false;
	}
	
	/**
	 * 截图的回调方法
	 * @param result
	 * @param rgbMat
	 * @param time	该帧在视频中的时间戳，单位为毫秒
	 */
	public abstract void getScreenResult(int result, int[][][] rgbMat, long time);
	
	/**
	 * 获取视频文件中某一帧
	 * @param videoFile
	 * @param time
	 * @return	如果成功返回rgb图像矩阵
	 */
	protected native int[][][] getScreenByTime(String videoFile, long time);
	
	/**
	 * 获取视频文件中某一帧
	 * @param time
	 * @return	如果成功返回rgb图像矩阵
	 */
	public abstract int[][][] getScreenByTime(long time);
	
	/**
	 * 开始将指定视频截图
	 * @param space	每隔多长时间截取一张图，单位为毫秒
	 */
	public abstract void start(long space);
	
	public abstract VideoPrintListener getListener();

	public abstract void setListener(VideoPrintListener listener);
	
	public abstract String getVideoFile();

	public abstract void setVideoFile(String videoFile);
}
