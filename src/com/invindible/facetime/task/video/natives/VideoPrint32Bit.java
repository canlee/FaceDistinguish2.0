package com.invindible.facetime.task.video.natives;

import com.invindible.facetime.util.system.DLLFactory;

public class VideoPrint32Bit extends VideoPrintJNI {
	
//	static {
//		System.loadLibrary("video_printer32");
//	}
	
	private String videoFile;
	private VideoPrintListener listener;
	
	public VideoPrint32Bit(String videoFile) {
		super();
		this.videoFile = videoFile;
		DLLFactory.loadVideo32DLL(VideoPrint32Bit.class);
	}
	
	@Override
	public VideoPrintListener getListener() {
		return listener;
	}

	@Override
	public void setListener(VideoPrintListener listener) {
		this.listener = listener;
	}

	@Override
	public String getVideoFile() {
		return videoFile;
	}

	@Override
	public void setVideoFile(String videoFile) {
		this.videoFile = videoFile;
	}



	/**
	 * 开始将指定视频截图
	 * @param space	每隔多长时间截取一张图，单位为毫秒
	 */
	@Override
	public void start(long space) {
		super.start(videoFile, space);
	}

	@Override
	public void getScreenResult(int result, int[][][] rgbMat, long time) {
		if(listener != null) {
			listener.onVideoPrint(result, rgbMat, time);
		}
	}
	
	/**
	 * 获取视频文件中某一帧
	 * @param time
	 * @return	如果成功返回rgb图像矩阵
	 */
	@Override
	public int[][][] getScreenByTime(long time) {
		if(videoFile != null) {
			return getScreenByTime(videoFile, time);
		}
		return null;
	}

}
