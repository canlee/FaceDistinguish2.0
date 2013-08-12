package com.invindible.facetime.model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * 保存的图片数据有：
 * <br>原图片的rgb矩阵；
 * <br>如果是从摄像头中截取的图片则有截取的时间；
 * <br>截取到的人脸的经过预处理的rgb矩阵数组；
 * <br>截取到的人脸在原矩阵中的位置；
 * @author 李亮灿
 *
 */
public class FaceImage {

	private Integer videoId;		//如果图片是从视频流那里截来的，则用此id来记录是哪个视频的
	private BufferedImage originImage;	//原始图片
	private long time;				//截取图片时的时间
	
	private List<ConnectedImage> facesRgb;	//人脸图的位置
	
	public FaceImage() {
		facesRgb = new ArrayList<ConnectedImage>();
		videoId = null;
	}
	
	
	/**
	 * 如果图片是从视频流那里截来的，则用此id来记录是哪个视频的
	 * @return
	 */
	public Integer getVideoId() {
		return videoId;
	}
	
	/**
	 * 如果图片是从视频流那里截来的，则用此id来记录是哪个视频的
	 * @param videoId
	 */
	public void setVideoId(int videoId) {
		this.videoId = videoId;
	}

	/**
	 * 原始图片
	 * @return
	 */
	public BufferedImage getOriginImage() {
		return originImage;
	}

	/**
	 * 原始图片
	 * @param originImage
	 */
	public void setOriginImage(BufferedImage originImage) {
		this.originImage = originImage;
	}

	/**
	 * 截取图片时的时间
	 * @return
	 */
	public long getTime() {
		return time;
	}

	/**
	 * 截取图片时的时间
	 * @param time
	 */
	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * 找到的人脸的图的位置，找到的人脸未经过预处理的rgb矩阵数组
	 * <br>rgb矩阵为256*256大小的图像
	 * @return
	 */
	public List<ConnectedImage> getFacesRgb() {
		return facesRgb;
	}

	/**
	 * 找到的人脸的图的位置，找到的人脸未经过预处理的rgb矩阵数组
	 * @param facesRgb
	 */
	public void setFacesRgb(List<ConnectedImage> facesRgb) {
		this.facesRgb = facesRgb;
	}

}
