package com.invindible.facetime.util.math;

public class MathUtil {
	
	/**
	 * 
	 * @param num
	 * @return	当num>0，sign(num)=1;
				<br>当num=0，sign(num)=0;
				<br>当num<0， sign(num)=-1
	 */
	public static int sign(int num) {
		if(num > 0) {
			return 1;
		}
		else if(num == 0) {
			return 0;
		}
		else {
			return -1;
		}
	}
	
	/**
	 * 二维数组转化为一维数组
	 * @param num
	 * @return
	 */
	public static int[] twoDToOneD(int[][] num) {
		int h = num.length;
		int w = num[0].length;
		int[] tmp = new int[h * w];
		int x = 0;
		for(int i = 0; i < h; i++) {
			for(int j = 0; j < w; j++) {
				tmp[x++] = num[i][j];
			}
		}
		return tmp;
	}
	
	/**
	 * 矩阵相减
	 * @param a
	 * @param b
	 * @return	a - b
	 */
	public static double[][] matrixSub(double[][] a, double[][] b) {
		int h = a.length;
		int w = a[0].length;
		if(h != b.length || w != b[0].length) {
			throw new NumberFormatException();
		}
		double[][] result = new double[h][w];
		for(int i = 0; i < h; i++) {
			for(int j = 0; j < w; j++) {
				result[i][j] = a[i][j] - b[i][j];
			}
		}
		return result;
	}
	
	/**
	 * 矩阵相乘
	 * @param a
	 * @param b
	 * @return	a * b
	 */
	public static double[][] matrixMult(double[][] a, double[][] b) {
		int h = a.length;
		int w = b[0].length;
		if(b.length != a[0].length) {
			throw new NumberFormatException();
		}
		double[][] result = new double[h][w];
		double tmp;
		for(int i = 0; i < h; i++) {
			for(int j = 0; j < w; j++) {
				tmp = 0;
				for(int k = 0; k < a[0].length; k++) {
					tmp += a[i][k] * b[k][j];
				}
				result[i][j] = tmp;
			}
		}
		return result;
 	}
	
	/**
	 * 求转置矩阵
	 * @param a
	 * @return
	 */
	public static double[][] matrixTranspose(double[][] a) {
		int h = a.length;
		int w = a[0].length;
		double[][] result = new double[w][h];
		for(int i = 0; i < h; i++) {
			for(int j = 0; j < w; j++) {
				result[j][i] = a[i][j];
			}
		}
		return result;
	}
	
}
