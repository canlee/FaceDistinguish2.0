package com.invindible.facetime.util.image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.invindible.facetime.model.ConnectedImage;

/**
 * 图片的后期处理帮助类
 * @author 李亮灿
 *
 */
public class ImagePostProcess {
	
	private ImagePostProcess() {;}
	
	/**
	 * 欧拉数的空洞数大于等于该数时删去该连通图
	 */
	private static final int RIGHT_BORDER = 0;
	
	/**
	 * 使用图的层次遍历找到连通图
	 * @param mat
	 * @param sign	当前连通图的标记数字
	 * @param x
	 * @param y
	 */
	private static void getConnectMat(int[][] mat, int sign, int x, int y) {
		int h = mat.length;
		int w = mat[0].length;
		Queue<MatCoordinate> cQueue = new LinkedList<MatCoordinate>();
		MatCoordinate ctmp = new MatCoordinate(x, y);
		cQueue.offer(ctmp);
		while((ctmp = cQueue.poll()) != null) {
			if(mat[ctmp.y][ctmp.x] != -1) {
				continue;
			}
			mat[ctmp.y][ctmp.x] = sign;
			for(int i = ctmp.y - 1; i <= ctmp.y + 1; i++) {
				for(int j = ctmp.x -1; j <= ctmp.x + 1; j++) {
					if(i >= 0 && j >= 0 && j < w && i < h) {
						if(mat[i][j] == -1) {
							cQueue.offer(new MatCoordinate(j, i));
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * 获取某一行中从start到end的图段数
	 * @param mat
	 * @param sign
	 * @param start
	 * @param end
	 * @param y
	 * @return
	 */
	private static int getGraphSegment(int[][] mat, int sign, int start, int end, int y) {
		if(y < 0) {
			return 0;
		}
		int result = 0;
		boolean judge = false;
		int i = start - 1 >= 0 ? start - 1 : 0;
		int e = end == mat[0].length ? end - 1 : end;
		for(i = start; i <= e; i++) {
			if(mat[y][i] == sign) {
				if(!judge) {
					judge = true;
				}
			}
			else {
				if(judge) {
					result++;
					judge = false;
				}
			}
		}
		if(judge) {
			result++;
		}
		return result;
	}
	
	
	/**
	 * 获取各个连通图的位置坐标
	 * @param connectMat
	 * @return
	 */
	private static List<ConnectedImage> getConnectImageCoordinate(int[][] connectMat) {
		int h = connectMat.length;
		int w = connectMat[0].length;
		Map<Integer, ConnectedImage> record = new HashMap<Integer, ConnectedImage>();
		for(int i = 0; i < h; i++) {
			for(int j = 0; j < w; j++) {
				if(connectMat[i][j] != 0) {
					ConnectedImage tmp = record.get(connectMat[i][j]);
					if(tmp != null) {
						if(tmp.getTopx() > j) {
							tmp.setTopx(j);
						}
						else if(tmp.getBottomx() < j) {
							tmp.setBottomx(j);
						}
						else if(tmp.getBottomy() < i) {
							tmp.setBottomy(i);
						}
					}
					else {
						tmp = new ConnectedImage();
						tmp.setTopx(j);
						tmp.setTopy(i);
						tmp.setBottomx(j);
						tmp.setBottomy(i);
						record.put(connectMat[i][j], tmp);
					}
				}
			}
		}
		List<ConnectedImage> result = new ArrayList<ConnectedImage>();
		for(Map.Entry<Integer, ConnectedImage> entry : record.entrySet()) {
			result.add(entry.getValue());
		}
		return result;
	}
	
	
	/**
	 * 查询两个连通图是否有相交
	 * @param ci1
	 * @param ci2
	 * @return	返回true表示有相交
	 */
	private static boolean isCrossing(ConnectedImage ci1, ConnectedImage ci2) {
		if((ci1.getTopx() <= ci2.getTopx() && ci1.getBottomx() >= ci2.getTopx())
				|| (ci2.getTopx() <= ci1.getTopx() && ci2.getBottomx() >= ci1.getTopx())) {
			if((ci1.getTopy() <= ci2.getBottomy() && ci1.getBottomy() >= ci2.getBottomy()) 
					|| (ci2.getTopy() <= ci1.getBottomy() && ci2.getBottomy() >= ci1.getBottomy())) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * 合并两个连通图
	 * @param ci1
	 * @param ci2
	 * @return
	 */
	private static ConnectedImage mergeConnectedImage(ConnectedImage ci1, ConnectedImage ci2) {
		ConnectedImage result = new ConnectedImage();
		result.setTopx(ci1.getTopx() <= ci2.getTopx() ? ci1.getTopx() : ci2.getTopx());
		result.setTopy(ci1.getTopy() <= ci2.getTopy() ? ci1.getTopy() : ci2.getTopy());
		result.setBottomx(ci1.getBottomx() >= ci2.getBottomx() ? ci1.getBottomx() : ci2.getBottomx());
		result.setBottomy(ci1.getBottomy() >= ci2.getBottomy() ? ci1.getBottomy() : ci2.getBottomy());
		return result;
	}
	
	
	/**
	 * 合并相交的连通图
	 * @param connectList
	 */
	public static void mergeCrossingConnectedImage(List<ConnectedImage> connectList) {
		for(int i = 0; i < connectList.size() - 1; i++) {
			ConnectedImage tmp1 = connectList.get(i);
			for(int j = i + 1; j < connectList.size(); j++) {
				ConnectedImage tmp2 = connectList.get(j);
				if(isCrossing(tmp1, tmp2)) {
					ConnectedImage newImg = mergeConnectedImage(tmp1, tmp2);
					connectList.remove(tmp1);
					connectList.remove(tmp2);
					connectList.add(newImg);
					i = -1;
					break;
				}
			}
		}
	}
	
	
	/**
	 * 在原图上截取连通图的rgb矩阵
	 * @param ci
	 * @param rgb
	 */
	public static void getConnectedImageRgbMat(ConnectedImage ci, int[][][] rgb) {
		int w = ci.getBottomx() - ci.getTopx() + 1;
		int h = ci.getBottomy() - ci.getTopy() + 1;
		int[][][] result = new int[3][h][w];
		for(int i = ci.getTopy(), y = 0; i <= ci.getBottomy(); i++, y++) {
			for(int j = ci.getTopx(), x = 0; j <= ci.getBottomx(); j++, x++) {
				result[0][y][x] = rgb[0][i][j];
				result[1][y][x] = rgb[1][i][j];
				result[2][y][x] = rgb[2][i][j];
			}
		}
		ci.setRgbMat(result);
	}
	
	
	/**
	 * 根据二值图使用8连通的方法获取其连通图
	 * @param rgbMat	二值图
	 * @return	连通图，0为黑色部分，其他数字为各个连通图的标志
	 */
	public static int[][] getConnectMat(int[][][] rgbMat) {
		int h = rgbMat[0].length;
		int w = rgbMat[0][0].length;
		int[][] result = new int[h][w];
		int sign = 1;
		for(int i = 0; i < h; i++) {
			for(int j = 0; j < w; j++) {
				if(rgbMat[0][i][j] == 255) {
					result[i][j] = -1;
				}
				else {
					result[i][j] = 0;
				}
			}
		}
		for(int i = 0; i < h; i++) {
			for(int j = 0; j < w; j++) {
				if(result[i][j] == -1) {
					getConnectMat(result, sign, j, i);
					sign++;
				}
			}
		}
//		for(int i = 0; i < h; i++) {
//			for(int j = 0; j < w; j++) {
//				if(result[i][j] == 10) {
//					rgbMat[0][i][j] = 255;
//					rgbMat[1][i][j] = 0;
//					rgbMat[2][i][j] = 0;
//				}
//			}
//		}
		return result;
	}
	
	
	/**
	 * 根据每个连通图获取其欧拉数
	 * @param connectMat	已经分好类的连通图
	 * @return	key为连通图的标志数字，value为欧拉数
	 */
	public static Map<Integer, Integer> getEulerNumber(int[][] connectMat) {
		Map<Integer, Integer> result = new HashMap<Integer, Integer>();
		int h = connectMat.length;
		int w = connectMat[0].length;
		int start = 0;
		int sign = 0;
		int segmentCount;
		for(int i = 0; i < h; i++, sign = 0) {
			for(int j = 0; j < w; j++) {
				if(connectMat[i][j] > 0) {
					if(sign == 0) {
						sign = connectMat[i][j];
						start = j;
					}
				}
				else {
					if(sign != 0) {
						segmentCount = getGraphSegment(connectMat, sign, start, j, i - 1);
						Integer count = result.get(sign);
						if(count == null) {
							count = 0;
						}
						count += 1 - segmentCount;
						result.put(sign, count);
						sign = 0;
					}
				}
			}
			if(sign != 0) {
				segmentCount = getGraphSegment(connectMat, sign, start, w, i - 1);
				Integer count = result.get(sign);
				if(segmentCount == 0 && count != null) {
					segmentCount++;
				}
				if(count == null) {
					count = 0;
				}
				count += 1 - segmentCount;
				result.put(sign, count);
			}
		}
//		for(Map.Entry<Integer, Integer> entry : result.entrySet()) {
//			System.out.println(entry.getKey() + " , " + entry.getValue());
//		}
		return result;
	}
	
	
	/**
	 * 根据图像的欧拉数把大于等于0并且小于-20的连通图都删去<br>
	 * 当全部连通图的欧拉数
	 * @param rgbMat
	 * @param connectMat
	 * @param euler
	 * @return
	 */
	public static int[][][] cutMatByEulerNumber(
			int[][][] rgbMat, int[][] connectMat, Map<Integer, Integer> euler) {
		boolean judge = false;
		for(Map.Entry<Integer, Integer> entry : euler.entrySet()) {
			if(entry.getValue() < RIGHT_BORDER) {
				judge = true;
				break;
			}
		}
		if(!judge) {
			return rgbMat;
		}
		int h = connectMat.length;
		int w = connectMat[0].length;
		int[][][] result = new int[3][h][w];
		int value;
		int tmp;
		for(int i = 0; i < h; i++) {
			for(int j = 0; j < w; j++) {
				if(connectMat[i][j] != 0) {
					tmp = euler.get(connectMat[i][j]); 
					if( tmp >= RIGHT_BORDER) {
						value = 0;
						connectMat[i][j] = 0;
					}
					else {
						value = 255;
					}
				}
				else {
					value = 0;
				}
				result[0][i][j] = value;
				result[1][i][j] = value;
				result[2][i][j] = value;
			}
		}
		return result;
	}
	
	/**
	 * 把连通图都切开
	 * @param originRgb	原图的彩色rgb值
	 * @param connectMat	连通图分类
	 * @return	各个彩色rgb值的连通图
	 */
	public static List<ConnectedImage> getConnectedImage(
			int[][][] originRgb, int[][] connectMat) {
		List<ConnectedImage> connectCoordinate = getConnectImageCoordinate(connectMat);
		//对连通图扩大24点象素
		for(ConnectedImage ci : connectCoordinate) {
			int tmp = ci.getTopx() - 24;
			tmp = tmp >= 0 ? tmp : 0;
			ci.setTopx(tmp);
			tmp = ci.getTopy() - 24;
			tmp = tmp >= 0 ? tmp : 0;
			ci.setTopy(tmp);
			tmp = ci.getBottomx() + 24;
			tmp = tmp < connectMat[0].length ? tmp : connectMat[0].length - 1;
			ci.setBottomx(tmp);
			tmp = ci.getBottomy() + 24;
			tmp = tmp < connectMat.length ? tmp : connectMat.length - 1;
			ci.setBottomy(tmp);
		}
		mergeCrossingConnectedImage(connectCoordinate);
		for(int i = 0; i < connectCoordinate.size(); i++) {
			getConnectedImageRgbMat(connectCoordinate.get(i), originRgb);
		}
		return connectCoordinate;
	}
	
	
	private static class MatCoordinate {
		private int x;
		private int y;
		
		public MatCoordinate(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
}
