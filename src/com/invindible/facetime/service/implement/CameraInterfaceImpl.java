package com.invindible.facetime.service.implement;

import java.awt.Image;

import com.invindible.facetime.service.interfaces.CameraInterface;
import com.invindible.facetime.task.interfaces.Context;
import com.invindible.facetime.task.video.VideoStreamTask;

public class CameraInterfaceImpl implements CameraInterface {
	
	/**
	 * 打开摄像头成功
	 */
	public static final int OPEN_CARERA_SUCCESS = VideoStreamTask.OPEN_CAMERA_SUCCESS;
	
	/**
	 * 打开摄像头失败
	 */
	public static final int OPEN_CAMERA_FAIL = VideoStreamTask.OPEN_CAMERA_FAIL;
	
	private VideoStreamTask videoTask;
	private boolean hasStart;		//记录有否运行过该任务
	
	public CameraInterfaceImpl(Context context) {
		videoTask = new VideoStreamTask(context);
		hasStart = false;
	}

	/**
	 * 异步打开摄像头<br>
	 * 如果打开摄像头成功，则在第一个参数上返回OPEN_CARERA_SUCCESS，<br>
	 * 在第二个参数上返回显示视频的控件Component
	 * <br>如果打开失败则返回OPEN_CAMERA_FAIL
	 * @see OPEN_CARERA_SUCCESS
	 * @see OPEN_CAMERA_FAIL
	 */
	@Override
	public void getCamera() {
		if(!videoTask.isRunning()) {
			if(hasStart) {
				videoTask = new VideoStreamTask(videoTask.getContext());
			}
			videoTask.start();
			hasStart = true;
		}
	}

	/**
	 * 从摄像头的影像中截取当前的图片
	 * <br> 如果没有打开摄像头，则返回null
	 */
	@Override
	public Image getHandledPictrue() {
		return videoTask.getCurrentImage();
	}

	@Override
	public void closeCamera() {
		videoTask.closeCamera();
	}
	
}
