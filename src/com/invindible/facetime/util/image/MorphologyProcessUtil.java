package com.invindible.facetime.util.image;

/**
 *  图像形态学的处理。主要是开和关
 * @author 李亮灿
 *
 */
public class MorphologyProcessUtil {
	
	private MorphologyProcessUtil() {;}
	
	/**
	 * 图片的腐蚀
	 * @param rgbMat	图片的二值图
	 * @param rect	处理腐蚀的结构元素正方矩形的边长大小， 取奇数
	 * @return
	 */
	public static int[][][] imageErosion(int[][][] rgbMat, int rect) {
		int h = rgbMat[0].length;
		int w = rgbMat[0][0].length;
		int border = rect / 2;
		int[][][] result = new int[3][h][w];
		int tmp;
		for(int i = 0; i < h; i++) {
			for(int j = 0; j < w; j++) {
				if(i >= border && j >= border && i < h - border && j < w - border) {
					if(rgbMat[0][i][j] == 0) {
						result[0][i][j] = 0;
						result[1][i][j] = 0;
						result[2][i][j] = 0;
					}
					else {
						tmp = 255;
						for(int y = i - border; y <= i + border; y++) {
							for(int x = j - border; x <= j + border; x++) {
								if(rgbMat[0][y][x] == 0) {
									tmp = 0;
									break;
								}
							}
						}
						result[0][i][j] = tmp;
						result[1][i][j] = tmp;
						result[2][i][j] = tmp;
					}
				}
				else {
					result[0][i][j] = rgbMat[0][i][j];
					result[1][i][j] = rgbMat[1][i][j];
					result[2][i][j] = rgbMat[2][i][j];
				}
			}
		}
		return result;
	}
	
	
	/**
	 * 图片的膨胀
	 * @param rgbMat	图片的二值图
	 * @param rect	处理膨胀的结构元素正方矩形的边长大小， 取奇数
	 * @return
	 */
	public static int[][][] imageDilation(int[][][] rgbMat, int rect) {
		int h = rgbMat[0].length;
		int w = rgbMat[0][0].length;
		int border = rect / 2;
		int[][][] result = new int[3][h][w];
		int tmp;
		for(int i = 0; i < h; i++) {
			for(int j = 0; j < w; j++) {
				if(i >= border && j >= border && i < h - border && j < w - border) {
					if(rgbMat[0][i][j] == 0) {
						result[0][i][j] = 0;
						result[1][i][j] = 0;
						result[2][i][j] = 0;
					}
					else {
						tmp = 0;
						for(int y = i - border; y <= i + border; y++) {
							for(int x = j - border; x <= j + border; x++) {
								if(rgbMat[0][y][x] == 255) {
									tmp = 255;
									break;
								}
							}
						}
						result[0][i][j] = tmp;
						result[1][i][j] = tmp;
						result[2][i][j] = tmp;
					}
				}
				else {
					result[0][i][j] = rgbMat[0][i][j];
					result[1][i][j] = rgbMat[1][i][j];
					result[2][i][j] = rgbMat[2][i][j];
				}
			}
		}
		return result;
	}
	
	
	/**
	 * 图片的开运算。也就是先腐蚀后膨胀，使用3 * 3的结构元素
	 * @param rgbMat	图片的二值图
	 * @return 
	 */
	public static int[][][] imageOpen(int[][][] rgbMat) {
		int[][][] result = imageErosion(rgbMat, 3);
		result = imageDilation(result, 3);
		return result;
	}
	
	
	/**
	 * 图片的闭运算。也就是先膨胀后腐蚀，使用3 * 3的结构元素
	 * @param rgbMat	图片的二值图
	 * @return
	 */
	public static int[][][] imageClose(int[][][] rgbMat) {
		int[][][] result = imageDilation(rgbMat, 3);
		result = imageErosion(result, 3);
		return result;
	}
	
}
