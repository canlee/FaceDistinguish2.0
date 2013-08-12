package com.invindible.facetime.service.implement;

import java.util.ArrayList;
import java.util.List;

import com.invindible.facetime.model.FaceImage;
import com.invindible.facetime.service.interfaces.FindFaceInterface;
import com.invindible.facetime.service.interfaces.FindVideoFaceInterface;
import com.invindible.facetime.task.interfaces.Context;
import com.invindible.facetime.task.video.VideoPrintScreenTask;
import com.invindible.facetime.task.video.natives.VideoPrintListener;
import com.invindible.facetime.util.image.ImageUtil;

/**
 * 查找指定视频的人脸的任务
 * @author 李亮灿
 *
 */
public class FindVideoFaceImpl implements FindVideoFaceInterface, Context {
	
	/**
	 * 开启多少个线程来查找人脸
	 */
	private static final int FIND_FACE_THREAD_COUNT = 5;
	
	private VideoPrintScreenTask videoPrintScreenTask;
	private List<FindFaceInterface> findFaceTasks;
	private int videoID;
	private Context context;
	
	private int addThread; 		//要把图片插进哪个线程里找人脸
	private int imageCount;		//截图的数量
	
	private Object lock;
	
	
	/**
	 * 
	 * @param context
	 * @param videoPath	要查找视频的路径
	 * @param videoID	指定的视频id，不要与正在查找的视频id冲突
	 */
	public FindVideoFaceImpl(Context context, String videoPath, int videoID) {
		videoPrintScreenTask = new VideoPrintScreenTask(this, videoPath);
		findFaceTasks = new ArrayList<FindFaceInterface>();
		for(int i = 0; i < FIND_FACE_THREAD_COUNT; i++) {
			FindFaceInterface ffi = new FindFaceForVideoImpl(this);
			findFaceTasks.add(ffi);
		}
		this.videoID = videoID;
		this.context = context;
		this.addThread = 0;
		this.imageCount = 0;
		lock = new Object();
	}

	@Override
	public void findFace() {
		for(int i = 0; i < FIND_FACE_THREAD_COUNT; i++) {
			findFaceTasks.get(i).start();
		}
		videoPrintScreenTask.start();
	}

	@Override
	public void stop() {
		videoPrintScreenTask.stopTask();
		for(int i = 0; i < FIND_FACE_THREAD_COUNT; i++) {
			findFaceTasks.get(i).stop();
		}
	}

	@Override
	public void onRefresh(Object... objects) {
		int result = (Integer) objects[0];
		switch (result) {
		case VideoPrintListener.PRINT_RGB_MAT:
			synchronized (lock) {
				imageCount++;
				boolean judge = true;
				System.out.println(objects[2]);
				for(int i = 0; i < FIND_FACE_THREAD_COUNT; i++) {
					if(findFaceTasks.get(i).getQueueSize() == 0) {
						findFaceTasks.get(i).findFace(
								ImageUtil.getImgByRGB((int[][][]) objects[1]), 
								(Long) objects[2]);
						judge = false;
						addThread = (i + 1) % FIND_FACE_THREAD_COUNT;
						break;
					}
				}
				if(judge) {
					findFaceTasks.get(addThread).findFace(
							ImageUtil.getImgByRGB((int[][][]) objects[1]), 
							(Long) objects[2]);
					addThread = (addThread + 1) % FIND_FACE_THREAD_COUNT;
				}
				try {
					if(imageCount > FIND_FACE_THREAD_COUNT) {
						lock.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			break;
			
		case FindFaceInterface.FIND_FACE_SUCCESS:
			synchronized (lock) {
				FaceImage fi = (FaceImage) objects[1];
				if(fi.getFacesRgb().size() > 0) {
					fi.setVideoId(videoID);
					context.onRefresh(FIND_VIDEO_FACE_SUCCESS, fi);
				}
				else {
					fi = null;
				}
				imageCount--;
				if(imageCount == 0 && !videoPrintScreenTask.isRunning()) {
					stop();
					context.onRefresh(FIND_VIDEO_FACE_FINISH);
				}
				lock.notifyAll();
			}
			break;
			
		case VideoPrintListener.FIND_STREAM_FAIL:
		case VideoPrintListener.INIT_CODEC_CONTEXT_FAIL:
		case VideoPrintListener.INIT_FRAME_FAIL:
		case VideoPrintListener.OPEN_DECODEC_FAIL:
		case VideoPrintListener.OPEN_VIDEO_FAIL:
		case VideoPrintListener.HAS_STARTED:
			context.onRefresh(OPEN_VIDEO_FAIL);
			break;

		default:
			break;
		}
	}

}
