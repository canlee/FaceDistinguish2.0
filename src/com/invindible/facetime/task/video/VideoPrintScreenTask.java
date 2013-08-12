package com.invindible.facetime.task.video;

import com.invindible.facetime.task.Task;
import com.invindible.facetime.task.interfaces.Context;
import com.invindible.facetime.task.video.natives.VideoPrint32Bit;
import com.invindible.facetime.task.video.natives.VideoPrint64bit;
import com.invindible.facetime.task.video.natives.VideoPrintJNI;
import com.invindible.facetime.task.video.natives.VideoPrintListener;
import com.invindible.facetime.util.system.SystemUtil;

/**
 * 视频截图任务
 * @author 李亮灿
 *
 */
public class VideoPrintScreenTask extends Task implements VideoPrintListener {

	private VideoPrintJNI videoPrinter;
	
	public VideoPrintScreenTask(Context context) {
		super(context);
		init(null);
	}

	public VideoPrintScreenTask(Context context, String videoPath) {
		super(context);
		init(videoPath);
	}
	
	private void init(String videoPath) {
		if(SystemUtil.getSystemBit() == 32) {
			videoPrinter = new VideoPrint32Bit(videoPath);
		}
		else {
			videoPrinter = new VideoPrint64bit(videoPath);
		}
		videoPrinter.setListener(this);
	}
	
	public String getVideoPath() {
		return videoPrinter.getVideoFile();
	}

	public void setVideoPath(String videoPath) {
		videoPrinter.setVideoFile(videoPath);
	}

	@Override
	protected void doTask() {
		videoPrinter.start(1000);
	}

	@Override
	public void stopTask() {
		videoPrinter.stop();
	}
	
	
	/**
	 * 获取视频文件中某一帧
	 * @param time
	 * @return	如果成功返回rgb图像矩阵
	 */
	public int[][][] getScreenByTime(long time) {
		return videoPrinter.getScreenByTime(time);
	}
	

	@Override
	public void onVideoPrint(int result, int[][][] rgbMat, long time) {
		context.onRefresh(result, rgbMat, time);
	}

}
