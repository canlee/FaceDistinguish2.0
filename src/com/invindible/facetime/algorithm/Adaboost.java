package com.invindible.facetime.algorithm;

import java.util.ArrayList;
import java.util.List;

import com.invindible.facetime.model.ConnectedImage;
import com.invindible.facetime.model.adaboost.Detector;
import com.invindible.facetime.model.adaboost.Rectangle;
import com.invindible.facetime.model.adaboost.StrongClassifier;
import com.invindible.facetime.model.adaboost.WeekClassifier;
import com.invindible.facetime.util.image.ImagePostProcess;
import com.invindible.facetime.util.image.ImageUtil;

/**
 * adaboost的检测算法
 * @author 李亮灿
 *
 */
public class Adaboost {
	
	private int[][][] rgbMat;
	private Detector detector;
	
	private double lastScale;	//上次的变换值
	private boolean isLastSuccess;	//上次的变换值是否识别成功
	
	/**
	 * 
	 * @param detector	训练器
	 */
	public Adaboost(Detector detector) {
		this.detector = detector;
		lastScale = 1.2;
		isLastSuccess = true;
	}
	
	/**
	 * 
	 * @param rgbMat	要找人脸的彩色图
	 * @param detector	训练器
	 */
	public Adaboost(int[][][] rgbMat, Detector detector) {
		this.rgbMat = rgbMat;
		this.detector = detector;
	}
	
	/**
	 * 要检测的图片
	 * @return
	 */
	public int[][][] getRgbMat() {
		return rgbMat;
	}

	/**
	 * 要检测的图片
	 * @param rgbMat
	 */
	public void setRgbMat(int[][][] rgbMat) {
		this.rgbMat = rgbMat;
	}

	public Detector getDetector() {
		return detector;
	}

	public void setDetector(Detector detector) {
		this.detector = detector;
	}
	
	/**
	 * 求积分图
	 * @param integralMat	带灰度值的数组
	 * @param squares
	 * @param w
	 * @param h
	 */
	private void getIntegralMat(int[][] integralMat, int[][] squares, int w, int h) {
		int tmp1 = 0;
		int tmp2 = 0;
		int tmp;
		squares[0][0] = integralMat[0][0] * integralMat[0][0];
		for(int i = 0; i < h; i++, tmp1 = 0, tmp2 = 0) {
			for(int j = 0; j < w; j++) {
				squares[i][j] = 0;
				tmp = integralMat[i][j];
				if(i > 0) {
					integralMat[i][j] += integralMat[i - 1][j];
					squares[i][j] = squares[i - 1][j];
				}
				integralMat[i][j] += tmp1;
				squares[i][j] += tmp2 + tmp * tmp;
				tmp1 += tmp;
				tmp2 += tmp * tmp;
			}
		}
	}
	
	/**
	 * 弱分类器检测出来的值
	 * @param wcf
	 * @param integralMat
	 * @param squares
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param scale
	 * @return
	 */
	private double getWeekClassifierValue(
			WeekClassifier wcf, int[][] integralMat, int[][] squares, 
			int i, int j, int w, int h) {
		
		double invArea = 1.0D / (w * h);
		int total_x = integralMat[i + h - 1][j + w - 1] + integralMat[i][j] - 
				integralMat[i][j + w - 1] - integralMat[i + h - 1][j];
	    int total_x2 = squares[i + h - 1][j + w - 1] + squares[i][j] - 
	    		squares[i][j + w - 1] - squares[i + h - 1][j];
	    double moy = total_x * invArea;
	    double vnorm = total_x2 * invArea - moy * moy;
	    vnorm = vnorm > 1.0D ? Math.sqrt(vnorm) : 1.0D;
	    
	    int rect_sum = 0;
	    for(Rectangle r : wcf.getRectangleList()) {
	    	int rx1 = j + (int) (((double) r.getX() / detector.getWidth()) * w);
	    	int rx2 = j + (int) (((double) (r.getX() + r.getWidth()) / detector.getWidth()) * w);
	    	int ry1 = i + (int) (((double) r.getY() / detector.getHeight()) * h);
	    	int ry2 = i + (int) (((double) (r.getY() + r.getHeight()) / detector.getHeight()) * h);

    		rect_sum += (int)((integralMat[ry2][rx2] - integralMat[ry1][rx2] - 
    				integralMat[ry2][rx1] + integralMat[ry1][rx1]) * r.getWeight());
	    }

	    double rect_sum2 = rect_sum * invArea;
	    if(rect_sum2 < wcf.getThreshold() * vnorm) {
	    	return wcf.getLeftValue();
	    }
	    else {
	    	return wcf.getRightValue();
	    }
	}
	
	/**
	 * 是否能过通过一个强分类器的检测
	 * @param scf
	 * @param integralMat
	 * @param squares
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return
	 */
	private Boolean isPassStrongClassifier(
			StrongClassifier scf, int[][] integralMat, int[][] squares,
			int i, int j, int w, int h) {
		double sum = 0.0f;
		for(WeekClassifier wcf : scf.getWeekClassifierList()) {
			sum += getWeekClassifierValue(wcf, integralMat, squares, i, j, w, h);
		}
		return sum > scf.getThreshold();
	}
	

	/**
	 * 获取原图上的人脸位置
	 * @return	如果没有人脸则返回一个空的数组
	 */
	private List<ConnectedImage> getFaces(double scale, double step) {
		int h = rgbMat[0].length;
		int w = rgbMat[0][0].length;
		//转化为灰度
		int[][] integralMat = ImageUtil.imgToGrayByTest(rgbMat);
		int[][] squares = new int[h][w];
		//计算积分图
		getIntegralMat(integralMat, squares, w, h);
		int ch = detector.getHeight();
		int cw = detector.getWidth();
		boolean judge = true;
		List<ConnectedImage> faceRect = new ArrayList<ConnectedImage>();
		for(; ch < h && cw < w; ch *= scale, cw *= scale) {
			int wstep = (int) (cw * step);
			int hstep = (int) (ch * step);
			for(int i = 0; i < h - ch; i += hstep) {
				for(int j = 0; j < w - cw; j += wstep) {
					judge = true;
					for(StrongClassifier scf : detector.getStrongClassifierList()) {
						if(!isPassStrongClassifier(
							scf, integralMat, squares, i, j, cw, ch)) {
							judge = false;
							break;
						}
					}
					if(judge) {
						//该区域为人脸
						ConnectedImage ci = new ConnectedImage();
						ci.setTopx(j);
						ci.setTopy(i);
						ci.setBottomx(j + cw - 1);
						ci.setBottomy(i + ch - 1);
						faceRect.add(ci);
					}
				}
			}
		}
		ImagePostProcess.mergeCrossingConnectedImage(faceRect);
		for(ConnectedImage tmp : faceRect) {
			ImagePostProcess.getConnectedImageRgbMat(tmp, rgbMat);
		}
		return faceRect;
	}
	
	public List<ConnectedImage> getFaces() {
		List<ConnectedImage> tmp = getFaces(1.2, 0.125);
		if(tmp.size() == 0) {
			tmp = getFaces(1.1, 0.125);
			if(tmp.size() == 0) {
				return getFaces(1.3, 0.125);
			}
		}
		return tmp;
	}
	
	/**
	 * 实时adaboost算法
	 * @return
	 */
	public List<ConnectedImage> getFacesForRealTime() {
		List<ConnectedImage> tmp;
		if(isLastSuccess) {
			tmp = getFaces(lastScale, 0.125);
		}
		else {
			lastScale += 0.1;
			if(lastScale > 1.3) {
				lastScale = 1.1;
			}
			tmp = getFaces(lastScale, 0.125);
		}
		if(tmp.size() > 0) {
			isLastSuccess = true;
		}
		else {
			isLastSuccess = false;
		}
		return tmp;
	}
	
}
