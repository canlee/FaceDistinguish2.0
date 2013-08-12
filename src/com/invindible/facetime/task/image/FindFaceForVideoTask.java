package com.invindible.facetime.task.image;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.invindible.facetime.CommonData;
import com.invindible.facetime.algorithm.Adaboost;
import com.invindible.facetime.algorithm.SkinColor;
import com.invindible.facetime.model.ConnectedImage;
import com.invindible.facetime.model.FaceImage;
import com.invindible.facetime.service.interfaces.FindFaceInterface;
import com.invindible.facetime.task.Task;
import com.invindible.facetime.task.interfaces.Context;
import com.invindible.facetime.util.Debug;
import com.invindible.facetime.util.image.ImagePostProcess;
import com.invindible.facetime.util.image.ImageUtil;

public class FindFaceForVideoTask extends Task {
	
	public static final int FIND_FACE_SUCCESS = FindFaceInterface.FIND_FACE_SUCCESS;
	
	private static final int MAX_SIZE = 420;
	
	private Queue<FaceImage> findQueue;
	private SkinColor skinColor;
	private Adaboost adaboost;
	private boolean isFinding;

	private Object lock;
	
	public FindFaceForVideoTask(Context context) {
		super(context);
		isFinding = false;
		findQueue = new LinkedList<FaceImage>();
		lock = new Object();
	}
	
	/**
	 * 调整找到的人脸图片为256*256大小
	 * @param ci
	 * @param originRgb
	 */
	private void scaleImage(ConnectedImage ci, int[][][] originRgb) {
		int w = originRgb[0][0].length;
		int h = originRgb[0].length;
		int topx = ci.getTopx();
		int topy = ci.getTopy();
		int bottomx = ci.getBottomx();
		int bottomy = ci.getBottomy();
		int topTmp;
		int bottomTmp;
		int subW = bottomx - topx;
		int subH = bottomy - topy;
		if(subW > subH) {
			//拉长高
			int space = subW - subH;
			topTmp = topy - space / 2;
			bottomTmp = bottomy + space - space / 2;
			if(topTmp < 0) {
				space = 0 - topTmp;
				topTmp = 0;
				bottomTmp += space;
				bottomTmp = bottomTmp < h ? bottomTmp : h - 1;
			}
			else if(bottomTmp >= h) {
				space = bottomTmp - h + 1;
				bottomTmp = h - 1;
				topTmp -= space;
				topTmp = topTmp >= 0 ? topTmp : 0;
			}
			ci.setTopy(topTmp);
			ci.setBottomy(bottomTmp);
		}
		else if(subW < subH) {
			//拉长宽
			int space = subH - subW;
			topTmp = topx - space / 2;
			bottomTmp = bottomx + space - space / 2;
			if(topTmp < 0) {
				space = 0 - topTmp;
				topTmp = 0;
				bottomTmp += space;
				bottomTmp = bottomTmp < w ? bottomTmp : w - 1;
			}
			else if(bottomTmp >= w) {
				space = bottomTmp - w + 1;
				bottomTmp = w - 1;
				topTmp -= space;
				topTmp = topTmp >= 0 ? topTmp : 0;
			}
			ci.setTopx(topTmp);
			ci.setBottomx(bottomTmp);
		}
		ImagePostProcess.getConnectedImageRgbMat(ci, originRgb);
		ci.setTopx(topx);
		ci.setTopy(topy);
		ci.setBottomx(bottomx);
		ci.setBottomy(bottomy);
		BufferedImage bitmp = ImageUtil.imageScale(
				ImageUtil.getImgByRGB(ci.getRgbMat()), 
				FindFaceInterface.FACE_SIZE, 
				FindFaceInterface.FACE_SIZE);
		ci.setRgbMat(ImageUtil.getRGBMat(bitmp));
	}
	
	
	private List<ConnectedImage> getFaces(BufferedImage img) {
		int originH = img.getHeight();
		int originW = img.getWidth();
		int[][][] originRgb = ImageUtil.getRGBMat(img);
		List<ConnectedImage> result = new ArrayList<ConnectedImage>();
		int[][][] rgb = originRgb;
		
		int max = originH > originW ? originH : originW;
		if(max > MAX_SIZE) {
			double s = (double) MAX_SIZE / max;
			img = ImageUtil.imageScale(img, s, s);
			rgb = ImageUtil.getRGBMat(img);
		}
		
		skinColor.setRgbMat(rgb);
		List<ConnectedImage> cil = skinColor.getFaces();
		for(ConnectedImage ci : cil) {
			adaboost.setRgbMat(ci.getRgbMat());
			List<ConnectedImage> adaboostResult = adaboost.getFacesForRealTime();
			//还原人脸在原图上的坐标
			for(ConnectedImage adaRe : adaboostResult) {
				adaRe.setTopx(adaRe.getTopx() + ci.getTopx());
				adaRe.setTopy(adaRe.getTopy() + ci.getTopy());
				adaRe.setBottomx(adaRe.getBottomx() + ci.getTopx());
				adaRe.setBottomy(adaRe.getBottomy() + ci.getTopy());
			}
			result.addAll(adaboostResult);
			adaboostResult = null;
		}
		Debug.print(cil.size() + ", " + result.size());
		if(result.size() > 0) {
			//修改人脸在原图中的坐标
			if(max > MAX_SIZE) {
				for(ConnectedImage ci : result) {
					ci.setTopx(
							(int) (((double) ci.getTopx() / rgb[0][0].length) * originW));
					ci.setTopy((int) (((double) ci.getTopy() / rgb[0].length) * originH));
					ci.setBottomx(
							(int) (((double) ci.getBottomx() / rgb[0][0].length) 
									* originW));
					ci.setBottomy(
							(int) (((double) ci.getBottomy() / rgb[0].length) * originH));
				}
			}
			for(ConnectedImage ci : result) {
				scaleImage(ci, originRgb);
			}
		}
		originRgb = null;
		rgb = null;
		cil = null;
		return result;
	}
	
	
	/**
	 * 把图片放入待检测队列，以查找人脸
	 * @param img
	 */
	public void find(Image img) {
		FaceImage fi = new FaceImage();
		fi.setOriginImage(ImageUtil.ImageToBufferedImage(img));
		fi.setTime(Calendar.getInstance().getTimeInMillis());
		synchronized (lock) {
			findQueue.offer(fi);
			lock.notifyAll();
		}
	}
	
	/**
	 * 把图片放入待检测队列，以查找人脸
	 * @param img
	 * @param time	图片截取的时间
	 */
	public void find(BufferedImage img, long time) {
		FaceImage fi = new FaceImage();
		fi.setOriginImage(img);
		fi.setTime(time);
		synchronized (lock) {
			findQueue.offer(fi);
			lock.notifyAll();
		}
	}
	

	@Override
	protected void doTask() {
		isFinding = true;
		synchronized (CommonData.HarrCascade.lock) {
			while(CommonData.HarrCascade.isParsering || 
					CommonData.HarrCascade.detector == null) {
				try {
					CommonData.HarrCascade.lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		skinColor = new SkinColor();
		adaboost = new Adaboost(CommonData.HarrCascade.detector);
		while(isFinding) {
			while(findQueue.isEmpty()) {
				if(!isFinding) {
					return;
				}
				synchronized (lock) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			FaceImage fi = findQueue.poll();
			Calendar s = Calendar.getInstance();
			List<ConnectedImage> result = getFaces(fi.getOriginImage());
			fi.setFacesRgb(result);
			Calendar l = Calendar.getInstance();
			Debug.print("用时：" + (l.getTimeInMillis() - s.getTimeInMillis()));
			context.onRefresh(FIND_FACE_SUCCESS, fi);
		}
	}

	@Override
	public void stopTask() {
		synchronized (lock) {
			isFinding = false;
			lock.notifyAll();
		}
	}
	
	
	/**
	 * 待检测队列的长度
	 * @return
	 */
	public int getQueueSize() {
		return findQueue.size();
	}

}
