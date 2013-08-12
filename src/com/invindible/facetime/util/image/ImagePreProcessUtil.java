package com.invindible.facetime.util.image;

import java.util.Arrays;

import com.invindible.facetime.util.math.MathUtil;

/**
 * 图片的预处帮助类
 * @author 李亮灿
 *
 */
public class ImagePreProcessUtil {
	
	private ImagePreProcessUtil() {;}
	
	/**
	 * 计算平均值
	 * @param yCbCr
	 * @param count
	 * @param type	1为Cr的均值，2为Cb的均值
	 * @return
	 */
	private static int getEX(int[][] yCbCr, int count, int type) {
		int tmp = 0;
		for(int i = 0; i < count; i++) {
			tmp += yCbCr[type][i];
		}
		return tmp / count;
	}
	
	
	/**
	 * 计算方差
	 * @param yCbCr
	 * @param count
	 * @param ex	平均值
	 * @param type	1为Cb的均值，2为Cr的均值
	 * @return
	 */
	private static int getDX(int[][] yCbCr, int count, int ex, int type) {
		int[][] tmp = new int[1][12];
		for(int i = 0; i < count; i++) {
			tmp[0][i] = yCbCr[type][i] * yCbCr[type][i];
		}
		return getEX(tmp, count, 0) - ex * ex;
	}
	
	
	/**
	 * 1、图片的补光处理，使用自动白平衡技术
	 * @param imgRGB	图片的彩色RGB值
	 * @return	返回的数组是[0][height][width] = R 
	 * 			<br>[1][height][width] = G 
	 * 			<br>[2][height][width] = B 
	 */
	public static int[][][] imgToWhiteBlance(int[][][] imgRGB) {
		int h = imgRGB[0].length;
		int w = imgRGB[0][0].length;
		int[][][] yCbCr = ImageUtil.RGBToYCbCr(imgRGB);
		int[][] RL = new int[h][w];				//参考白色点
		int[][] subMat = new int[3][12];		//字块
		int mb;			//Cb的平均值
		int mr;			//Cr的平均值
		int db;			//Cb的方差
		int dr;			//Cr的方差
		int count;
		// 把图像分成3*4个块
		for(int i = 0; i < h; i += 4) {
			for(int j = 0; j < w; j += 3) {
				count = 0;
				for(int x = i; x < i + 4 && x < h; x++) {
					for(int y = j; y < j + 3 && y < w; y++) {
						subMat[0][count] = yCbCr[0][x][y];
						subMat[1][count] = yCbCr[1][x][y];
						subMat[2][count] = yCbCr[2][x][y];
						count++;
					}
				}
				//计算均值
				mb = getEX(subMat, count, 1);
				mr = getEX(subMat, count, 2);
				db = getDX(subMat, count, mb, 1);
				dr = getDX(subMat, count, mr, 2);
				// 选择参考白色点
				for(int x = i; x < i + 4 && x < h; x++) {
					for(int y = j; y < j + 3 && y < w; y++) {
						if((yCbCr[1][x][y] - (mb + db * MathUtil.sign(mb))) < Math.abs(1.5 * db) && 
							(yCbCr[2][x][y] - (1.5 * mr + dr * MathUtil.sign(mr))) < 
								Math.abs(1.5 * dr)) {
							RL[x][y] = yCbCr[0][x][y];
						}
						else {
							RL[x][y] = 0;
						}
					}
				}
			}
		}
		int tmp[] = MathUtil.twoDToOneD(RL);
		Arrays.sort(tmp);
		int index = (int) (w * h * 0.9);
		int luMin  = tmp[index];
		//计算参考白点的RGB的均值
		count = 0;
		long r = 0;
		long g = 0;
		long b = 0;
		double ymax = -1;	//亮度最大值
		for(int i = 0; i < h; i++) {
			for(int j = 0; j < w; j++) {
				if(yCbCr[0][i][j] > ymax) {
					ymax = yCbCr[0][i][j];
				}
				if(RL[i][j] >= luMin) {
					count++;
					r += imgRGB[0][i][j];
					g += imgRGB[1][i][j];
					b += imgRGB[2][i][j];
				}
			}
		}
		int rav = (int) (r / count);
		int gav = (int) (g / count);
		int bav = (int) (b / count);
		//调整增益
		double rgain = ymax / rav;
		double ggain = ymax / gav;
		double bgain = ymax / bav;
		int[][][] result = new int[3][h][w];
		//将原来的RGB数值提高到增益
		for(int i = 0; i < h; i++) {
			for(int j = 0; j < w; j++) {
				result[0][i][j] = (int) (imgRGB[0][i][j] * rgain);
				result[1][i][j] = (int) (imgRGB[1][i][j] * ggain);
				result[2][i][j] = (int) (imgRGB[2][i][j] * bgain);
				result[0][i][j] = result[0][i][j] <= 255 ? result[0][i][j] : 255;
				result[1][i][j] = result[1][i][j] <= 255 ? result[1][i][j] : 255;
				result[2][i][j] = result[2][i][j] <= 255 ? result[2][i][j] : 255;
			}
		}
		yCbCr = null;
		RL = null;
		subMat = null;
		tmp = null;
		return result;
	}
	
	
	/**
	 * 2、图片的高斯滤波处理。使用3 * 3 的滑动模板
	 * @param imgRGB	图片的彩色RGB值
	 * @return	返回的数组是[0][height][width] = R 
	 * 			<br>[1][height][width] = G 
	 * 			<br>[2][height][width] = B 
	 */
	public static int[][][] imgToNoiseFilter(int[][][] imgRGB) {
		int h = imgRGB[0].length;
		int w = imgRGB[0][0].length;
		if(h > 2 && w > 2) {
			int[][][] result = new int[3][h][w];
			int[] subRGB = new int[]{1, 2, 1, 2, 1, 2, 1, 2, 1};
			int mid = 0;
			for(int i = 0; i < h; i++) {
				for(int j = 0; j < w; j++) {
					if(i != 0 && i != h-1 && j != 0 && j != w - 1) {
						//提取3*3的矩阵
						for(int c = 0;c < 3;c++, mid = 0) {
							for(int x1 = i - 1, x2 = 0;x2 < 3;x2++) {
								for(int y1 = j - 1, y2 = 0;y2 < 3;y2++) {
									mid += subRGB[x2 * 3 + y2] * imgRGB[c][x1][y1];
								}
							}
							mid /= 13;
							result[c][i][j] = mid;
						}
					}
					else {
						result[0][i][j] = imgRGB[0][i][j];
						result[1][i][j] = imgRGB[1][i][j];
						result[2][i][j] = imgRGB[2][i][j];
					}
				}
			}
			return result;
		}
		return imgRGB;
	}
	
	
	/**
	 * 将二值图片进行中值滤波处理，使用3 * 3的结构元素
	 * @param brgbMat	二值图
	 * @return
	 */
	public static int[][][] imgToMiddleFilter(int[][][] brgbMat) {
		int h = brgbMat[0].length;
		int w = brgbMat[0][0].length;
		if(h > 2 && w > 2) {
			int[][][] result = new int[3][h][w];
			int[] struct = new int[9]; 
			for(int i = 0; i < h; i++) {
				for(int j = 0; j < w; j++) {
					if(i != 0 && i != h-1 && j != 0 && j != w - 1) {
						//提取3*3的矩阵
						for(int y = i - 1, c = 0; y <= i + 1; y++) {
							for(int x = j - 1; x <= j + 1; x++) {
								struct[c++] = brgbMat[0][y][x];
							}
						}
						Arrays.sort(struct);
						result[0][i][j] = struct[4];
						result[1][i][j] = struct[4];
						result[2][i][j] = struct[4];
					}
					else {
						result[0][i][j] = brgbMat[0][i][j];
						result[1][i][j] = brgbMat[1][i][j];
						result[2][i][j] = brgbMat[2][i][j];
					}
				}
			}
			struct = null;
			return result;
		}
		return brgbMat;
	}
	
}
