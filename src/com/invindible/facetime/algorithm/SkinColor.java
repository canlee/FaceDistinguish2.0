package com.invindible.facetime.algorithm;

import java.util.List;
import java.util.Map;

import com.invindible.facetime.model.ConnectedImage;
import com.invindible.facetime.util.image.ImagePostProcess;
import com.invindible.facetime.util.image.ImagePreProcessUtil;
import com.invindible.facetime.util.image.MorphologyProcessUtil;
import com.invindible.facetime.util.image.SkinColorCutUtil;

/**
 * 肤色分割算法
 * @author 李亮灿
 *
 */
public class SkinColor {

	private int[][][] rgbMat;
	
	public SkinColor() {
	}
	
	public SkinColor(int[][][] rgbMat) {
		this.rgbMat = rgbMat;
	}

	public int[][][] getRgbMat() {
		return rgbMat;
	}

	public void setRgbMat(int[][][] rgbMat) {
		this.rgbMat = rgbMat;
	}
	
	public List<ConnectedImage> getFaces() {
		int[][][] rgb = ImagePreProcessUtil.imgToWhiteBlance(rgbMat);
		//肤色分割
		double[][] probability = SkinColorCutUtil.getProbability(rgb);
		rgb = SkinColorCutUtil.getSkinGray(probability);
		probability = null;
		int threshold = SkinColorCutUtil.getThreshold(rgb);
		rgb = SkinColorCutUtil.imgToBinary(rgb, threshold);
		//中值滤波
		rgb = ImagePreProcessUtil.imgToMiddleFilter(rgb);
		//形态学开关
		rgb = MorphologyProcessUtil.imageOpen(rgb);
		rgb = MorphologyProcessUtil.imageClose(rgb);
		//删除不合格的连通图
		int[][] connectMat = ImagePostProcess.getConnectMat(rgb);
		Map<Integer, Integer> euler = ImagePostProcess.getEulerNumber(connectMat);
		rgb = ImagePostProcess.cutMatByEulerNumber(rgb, connectMat, euler);
		return ImagePostProcess.getConnectedImage(rgbMat, connectMat);
	}
}
