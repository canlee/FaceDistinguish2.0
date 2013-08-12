package com.invindible.facetime.task.video.natives;

public interface VideoPrintListener {
	
	public static final int SUCCESS = 0;
	public static final int OPEN_VIDEO_FAIL = 1;
	public static final int FIND_STREAM_FAIL = 2;
	public static final int INIT_CODEC_CONTEXT_FAIL = 3;
	public static final int OPEN_DECODEC_FAIL = 4;

	public static final int HAS_STARTED = 10;

	public static final int INIT_FRAME_FAIL = 20;

	public static final int HAS_NEXT_FRAME = 30;
	public static final int END_FRAME = 31;

	public static final int PRINT_RGB_MAT = 40;

	/**
	 * 当截取视频某一帧后的结果返回时调用该接口
	 * @param result
	 * @param rgbMat	图片rgb矩阵
	 * @param time		图片在视频中的时间，毫秒
	 */
	public void onVideoPrint(int result, int[][][] rgbMat, long time);
	
}
