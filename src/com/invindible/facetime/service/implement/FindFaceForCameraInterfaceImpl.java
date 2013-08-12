package com.invindible.facetime.service.implement;

import java.awt.Image;
import java.awt.image.BufferedImage;

import com.invindible.facetime.service.interfaces.FindFaceInterface;
import com.invindible.facetime.task.image.FindFaceForCameraTask;
import com.invindible.facetime.task.interfaces.Context;

/**
 * 用于摄像头实时人脸检测的任务
 * @author 李亮灿
 *
 */
public class FindFaceForCameraInterfaceImpl implements FindFaceInterface {

	private FindFaceForCameraTask findTask;
	private Context context;
	
	public FindFaceForCameraInterfaceImpl(Context context) {
		this.context = context;
	}

	@Override
	public void start() {
		if(findTask == null) {
			findTask = new FindFaceForCameraTask(context);
			findTask.start();
		}
	}

	@Override
	public void findFace(Image image) {
		if(findTask != null) {
			findTask.find(image);
		}
	}
	
	@Override
	public void findFace(BufferedImage image, long time) {
		if(findTask != null) {
			findTask.find(image, time);
		}
	}

	@Override
	public void stop() {
		if(findTask != null) {
			findTask.stopTask();
			findTask = null;
		}
	}

	@Override
	public int getQueueSize() {
		if(findTask != null) {
			return findTask.getQueueSize();
		}
		return 0;
	}
	
}
