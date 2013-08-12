package com.invindible.facetime.task.video.natives;

import com.invindible.facetime.util.system.DLLFactory;

public class VideoPrint64bit extends VideoPrintJNI {

//	static {
//		System.loadLibrary("video_printer64");
//	}
	
	private String videoFile;
	private VideoPrintListener listener;
	
	public VideoPrint64bit(String videoFile) {
		super();
		this.videoFile = videoFile;
		DLLFactory.loadVideo64DLL(VideoPrint64bit.class);
	}
	
	@Override
	public void getScreenResult(int result, int[][][] rgbMat, long time) {
		if(listener != null) {
			listener.onVideoPrint(result, rgbMat, time);
		}
	}

	@Override
	public int[][][] getScreenByTime(long time) {
		if(videoFile != null) {
			return getScreenByTime(videoFile, time);
		}
		return null;
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

}
