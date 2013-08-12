package com.invindible.facetime.service.implement;

import java.awt.Image;
import java.awt.image.BufferedImage;

import com.invindible.facetime.service.interfaces.FindFaceInterface;
import com.invindible.facetime.task.image.FindFaceForVideoTask;
import com.invindible.facetime.task.interfaces.Context;

public class FindFaceForVideoImpl implements FindFaceInterface {

	private FindFaceForVideoTask findTask;
	private Context context;
	
	public FindFaceForVideoImpl(Context context) {
		this.context = context;
	}
	
	@Override
	public void start() {
		if(findTask == null) {
			findTask = new FindFaceForVideoTask(context);
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
	public int getQueueSize() {
		if(findTask != null) {
			return findTask.getQueueSize();
		}
		return 0;
	}

	@Override
	public void stop() {
		if(findTask != null) {
			findTask.stopTask();
			findTask = null;
		}
	}

}
