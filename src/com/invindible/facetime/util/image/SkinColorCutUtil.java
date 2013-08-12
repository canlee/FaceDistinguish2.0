package com.invindible.facetime.util.image;

import com.invindible.facetime.util.Debug;
import com.invindible.facetime.util.math.MathUtil;

/**
 * 肤色分割帮助类
 * @author 李亮灿
 *
 */
public class SkinColorCutUtil {
	
	private SkinColorCutUtil() {;}
	
	/**
	 * (Cb, Cr)的协方差的逆矩阵<br>
	 * 
	 */
	private static final double[][] C_INVERSE = {
												 {0.0062, -0.0003}, 
												 {-0.0003, 0.0033}
												};
//	private static final double[][] C_INVERSE = {
//												 {0.0112, -0.0015}, 
//												 {-0.0015, 0.0041}
//												};
	
	
	/**
	 * (Cb, Cr)的数学期望
	 */
	private static final double[][] M = {
										 { 117.4361, 156.5599 }
										};
//	private static final double[][] M = {
//		 								 { 112.1987, 151.3993 }
//										};
	
	/**
	 * 将肤色相似度分为L个级别
	 */
	private static final int L = 256;
	
	
	private static void resetTable(int[][] PkTable, int[][] KPkTable) {
		for(int i = 0; i < L; i++) {
			for(int j = 0; j < L; j++) {
				PkTable[i][j] = -1;
				KPkTable[i][j] = -1;
			}
		}
	}
	
	
	/**
	 * 计算pk[start]一直叠加到pk[end]
	 * @param pk
	 * @param start
	 * @param end
	 * @return
	 */
	private static int getAddPk(int[][] PkTable, int[] pk, int start, int end) {
		if(start == end) {
			return pk[start];
		}
		else {
			if(PkTable[start][end] != -1) {
				return PkTable[start][end];
			}
			else {
				int tmp = pk[start] + getAddPk(PkTable, pk, start + 1, end);
				PkTable[start][end] = tmp;
				return tmp;
			}
		}
	}
	
	
	/**
	 * 计算start * pk[start]一直叠加到start * pk[end]
	 * @param pk
	 * @param start
	 * @param end
	 * @return
	 */
	private static int getAddKPk(int[][] KPkTable, int[] pk, int start, int end) {
		if(start == end) {
			return start * pk[start];
		}
		else {
			if(KPkTable[start][end] != -1) {
				return KPkTable[start][end];
			}
			else {
				int tmp = start * pk[start] + getAddKPk(KPkTable, pk, start + 1, end);
				KPkTable[start][end] = tmp;
				return tmp;
			}
		}
	}
	
	
	/**
	 * 根据某一像素值去计算该值是肤色的概率
	 * <br>使用二维高斯模型方程求解
	 * @param yCbCr	某一像素的YCbCr格式的数值[0] = Y, [1] = Cb, [2] = Cr
	 * @return
	 */
	private static double getProbabilitySkin(int[] yCbCr) {
		double[][] x = new double[][]{{yCbCr[1], yCbCr[2]}};
		double[][] tmp = MathUtil.matrixSub(x, M);
		double[][] tmp2 = MathUtil.matrixMult(tmp, C_INVERSE);
		tmp = MathUtil.matrixTranspose(tmp);
		tmp = MathUtil.matrixMult(tmp2, tmp);
		x = null;
		tmp2 = null;
		return Math.exp(tmp[0][0] * -0.5);
	}
	
	/**
	 * 1、求出给出的YCbCr矩阵求出每个像素肤色的概率
	 * @param yCbCr	数组是[0][height][width] = Y 
	 * 			<br>[1][height][width] = Cb 
	 * 			<br>[2][height][width] = Cr 
	 * @return
	 */
	private static double[][] getProbabilitySkin(int[][][] yCbCr) {
		int h = yCbCr[0].length;
		int w = yCbCr[0][0].length;
		double[][] result = new double[h][w];
		int[] tmp = new int[3];
		for(int i = 0; i < h; i++) {
			for(int j = 0; j < w; j++) {
				tmp[0] = yCbCr[0][i][j];
				tmp[1] = yCbCr[1][i][j];
				tmp[2] = yCbCr[2][i][j];
				result[i][j] = getProbabilitySkin(tmp);
			}
		}
		tmp = null;
		return result;
	}
	
//	/**
//	 * 把肤色概率分为L个级别<br>
//	 * 求出L个阶段的肤色相似度灰度图的各个阶段的像素点数
//	 * @param probability	所有像素为肤色像素的概率
//	 * @return	各阶段的像素的数量
//	 */
//	private static int[] getProbabilityCount(double[][] probability) {
//		int h = probability.length;
//		int w = probability[0].length;
//		int[] result = new int[L];
//		for(int i = 0; i < L; i++) {
//			result[i] = 0;
//		}
//		double left;
//		double right;
//		for(int i = 0; i < h; i++) {
//			for(int j = 0; j < w; j++) {
//				for(int k = 0; k < L; k++) {
//					left = (double) k / L;
//					right = (double) (k + 1) / L;
//					if(left <= probability[i][j] && right >= probability[i][j]) {
//						result[k]++;
//						break;
//					}
//				}
//			}
//		}
//		return result;
//	}
	
	/**
	 * 把肤色概率分为L个级别<br>
	 * 求出L个阶段的肤色相似度灰度图的各个阶段的像素点数
	 * @param grayMat	肤色相似灰度图
	 * @return	各阶段的像素的数量
	 */
	private static int[] getProbabilityCount(int[][][] grayMat) {
		int h = grayMat[0].length;
		int w = grayMat[0][0].length;
		int[] result = new int[L];
		for(int i = 0; i < L; i++) {
			result[i] = 0;
		}
		for(int i = 0; i < h; i++) {
			for(int j = 0; j < w; j++) {
				result[grayMat[0][i][j]]++;
			}
		}
		return result;
	}
	
	
	/**
	 * w0<br>
	 * 求出当阈值为threshold时，肤色区域所占的比例
	 * @param pk	各阶段的像素数量
	 * @param size	图片的像素总数量
	 * @param threshold	阈值
	 * @return
	 */
	private static double getSkinSize(int[][] PKTable, int[] pk, int size, int threshold) {
		int count = getAddPk(PKTable, pk, threshold, L - 1);
		return (double) count / size;
	}
	
	
	/**
	 * w1<br>
	 * 求出当阈值为threshold时，非肤色区域所占的比例
	 * @param pk	各阶段的像素数量
	 * @param size	图片的像素总数量
	 * @param threshold	阈值
	 * @return
	 */
	private static double getBackGroundSize(int[][] PKTable, int[] pk, int size, int threshold) {
		int count = getAddPk(PKTable, pk, 0, threshold - 1);
		return (double) count / size;
	}
	
	
	/**
	 * u0<br>
	 * 求出当阈值为threshold时，肤色区域的均值
	 * @param pk	各阶段的像素数量
	 * @param threshold	阈值
	 * @return
	 */
	private static double getSkinAve(int[][] KPKTable, int[][] PKTable, int[] pk, int threshold) {
		int kpkCount = getAddKPk(KPKTable, pk, threshold, L - 1);
		int pkCount = getAddPk(PKTable, pk, threshold, L - 1);
		if(pkCount == 0) {
			return 0;
		}
		return (double) kpkCount / pkCount;
	}
	
	
	/**
	 * u1<br>
	 * 求出当阈值为threshold时，非肤色区域的均值
	 * @param pk	各阶段的像素数量
	 * @param threshold	阈值
	 * @return
	 */
	private static double getBackgroundAve(
			int[][] KPKTable, int[][] PKTable, int[] pk, int threshold) {
		int kpkCount = getAddKPk(KPKTable, pk, 0, threshold - 1);
		int pkCount = getAddPk(PKTable, pk, 0, threshold - 1);
		if(pkCount == 0) {
			return 0;
		}
		return (double) kpkCount / pkCount;
	}
	
	
	/**
	 * 
	 * O0^2<br>
	 * 求出当阈值为threshold时，肤色区域的总方差
	 * @param pk
	 * @param threshold
	 * @return
	 */
	private static double getSkinC(int[][] KPKTable, int[][] PKTable, int[] pk, int threshold) {
		double count = 0;
		double tmp;
		double u0 = getSkinAve(KPKTable, PKTable, pk, threshold);
		for(int i = threshold; i < L; i++) {
			tmp = i - u0;
			count += Math.pow(tmp, 2) * pk[i];
		}
		return count;
	}
	
	
	/**
	 * 
	 * O1^2<br>
	 * 求出当阈值为threshold时，非肤色区域的总方差
	 * @param pk
	 * @param threshold
	 * @return
	 */
	private static double getBackgroundC(
			int[][] KPKTable, int[][] PKTable, int[] pk, int threshold) {
		double count = 0;
		double tmp;
		double u1 = getBackgroundAve(KPKTable, PKTable, pk, threshold);
		for(int i = 0; i < threshold; i++) {
			tmp = i - u1;
			count += Math.pow(tmp, 2) * pk[i];
		}
		return count;
	}
	
	
	private static double getThreshold(
			int[][] KPKTable, int[][] PKTable, int[]pk, int size, int threshold) {
		double w0 = getSkinSize(PKTable, pk, size, threshold);
		double u0 = getSkinAve(KPKTable, PKTable, pk, threshold);
		double w1 = getBackGroundSize(PKTable, pk, size, threshold);
		double u1 = getBackgroundAve(KPKTable, PKTable, pk, threshold);
//		double u = w0 * u0 + w1 * u1;
		double o02 = getSkinC(KPKTable, PKTable, pk, threshold);
		double o12 = getBackgroundC(KPKTable, PKTable, pk, threshold);
//		double d2 = w0 * (u0 - u) * (u0 - u) + w1 * (u1 - u) * (u1 - u);
		double tmp = u0 - u1;
		double d2 = w0 * w1 * Math.pow(tmp, 2);
		double o2 = w0 * o02 + w1 * o12;
		double result = 0;
		if(o2 != 0) {
			result = d2 /o2;
		}
		return result;
	}
	
	
	/**
	 * 1、求出给出的YCbCr矩阵求出每个像素肤色的概率
	 * @param rgbMat	数组是[0][height][width] = Y 
	 * 			<br>[1][height][width] = Cb 
	 * 			<br>[2][height][width] = Cr 
	 * @return	
	 */
	public static double[][] getProbability(int[][][] rgbMat) {
		int[][][] yCbCr = ImageUtil.RGBToYCbCr(rgbMat);
		double[][] probability = getProbabilitySkin(yCbCr);
		yCbCr = null;
		return probability;
	}
	
	
	/**
	 * 2、根据概率转换成肤色相似度图
	 * @param probability	每个像素点为肤色的概率
	 * @return
	 */
	public static int[][][] getSkinGray(double[][] probability) {
		int h = probability.length;
		int w = probability[0].length;
		int[][][] result = new int[3][h][w];
		int tmp;
		for(int i = 0; i < h; i++) {
			for(int j = 0; j < w; j++) {
				tmp = (int) (probability[i][j] * 255);
				result[0][i][j] = tmp;
				result[1][i][j] = tmp;
				result[2][i][j] = tmp;
			}
		}
		return result;
	}
	
	
//	/**
//	 * 3、获取肤色分割的动态阈值
//	 * @param probability	每个像素的肤色概率
//	 * @return
//	 */
//	public static int getThreshold(double[][] probability) {
//		resetTable();
//		int[] pk = getProbabilityCount(probability);
//		for(int i = 0; i < L; i++) {
//			System.out.println(pk[i]);
//		}
//		int size = probability.length * probability[0].length;
//		double max = -1;
//		int maxArg = -1;
//		double tmp;
//		for(int i = 1; i < L; i++) {
//			tmp = getThreshold(pk, size, i);
////			System.out.println(tmp);
//			if(tmp > max) {
//				max = tmp;
//				maxArg = i;
//			}
//		}
////		System.out.println(maxArg);
//		return maxArg;
//	}
	
	/**
	 * 3、获取肤色分割的动态阈值
	 * @param probability	每个像素的肤色概率
	 * @return
	 */
	public static int getThreshold(int[][][] grayMat) {
		int[][] PkTable = new int[L][L];
		int[][] KPkTable = new int[L][L];
		resetTable(PkTable, KPkTable);
		int[] pk = getProbabilityCount(grayMat);
		int size = grayMat[0].length * grayMat[0][0].length;
		double max = -1;
		int maxArg = -1;
		double tmp;
		for(int i = 1; i < L; i++) {
			tmp = getThreshold(KPkTable, PkTable, pk, size, i);
//			System.out.println(tmp);
			if(tmp > max) {
				max = tmp;
				maxArg = i;
			}
		}
		PkTable = null;
		KPkTable = null;
		pk = null;
		Debug.print("阈值：" + maxArg);
		return maxArg;
	}
	
	
	/**
	 * 4、灰度图进行阈值二值化处理
	 * @param imgRGB	要处理的肤色相似度灰度图
	 * @param threshold	阈值0~255
	 * @return	返回的数组是[0][height][width] = R 
	 * 			<br>[1][height][width] = G 
	 * 			<br>[2][height][width] = B 
	 */
	public static int[][][] imgToBinary(int[][][] imgRGB, int threshold) {
		int h = imgRGB[0].length;
		int w = imgRGB[0][0].length;
		int[][][] result = new int[3][h][w];
		for(int i = 0; i < h; i++) {
			for(int j = 0; j < w; j++) {
				if(imgRGB[0][i][j] >= threshold) {
					result[0][i][j] = 255;
					result[1][i][j] = 255;
					result[2][i][j] = 255;
				}
				else {
					result[0][i][j] = 0;
					result[1][i][j] = 0;
					result[2][i][j] = 0;
				}
			}
		}
		return result;
	}
	
}
