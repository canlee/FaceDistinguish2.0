package com.invindible.facetime.wavelet;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.invindible.facetime.util.image.ImageUtil;
import com.invindible.facetime.util.image.ImagePreProcessUtil;


public class Wavelet {
	private Wavelet(){};
	
	/**
	 * 提取单数行、列
	 * @param tmpP 像素
	 * @return
	 */
	private static int[][][] Rowreduce(int[][][] tmpP){
		int height=tmpP[0].length;
		int width=tmpP[0][0].length;
		int[][][] result=new int[3][height/2][width];
		int tp;
		for(int i=0;i<width;i++){
			tp=0;
			for(int j=0;j<height;j+=2){
				result[0][tp][i]=tmpP[0][j][i];
				result[1][tp][i]=tmpP[1][j][i];
				result[2][tp++][i]=tmpP[2][j][i];
			}
		}
		return result;
	}
	
	/**
	 * 三维矩阵转置
	 * @param tmpP 像素
	 * @return
	 */
	private static int[][][] transposition(int[][][] tmpP){
		int height=tmpP[0].length;
		int width=tmpP[0][0].length;
		int[][][] result=new int[3][width][height];
		int tmp;
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				result[0][i][j]=tmpP[0][j][i];
				result[1][i][j]=tmpP[1][j][i];
				result[2][i][j]=tmpP[2][j][i];
			}
		}	
		return result;
	}
	
	
	
	private static int[][][] rgb;
	public static BufferedImage[] Wavelet(BufferedImage[] bfi){
		BufferedImage[] bf=new BufferedImage[bfi.length];
		for(int i=0;i<bfi.length;i++){
		rgb=ImageUtil.getRGBMat(bfi[i]) ;
		int[][][]gray=ImageUtil.imgToGray(rgb); //trans to gray	
		int[][][]mid=ImagePreProcessUtil.imgToMiddleFilter(gray);  //midfilter	
		int[][][]whiteB=ImagePreProcessUtil.imgToWhiteBlance(mid); //whitebalance	
		
		int[][][]noiseFilter=ImagePreProcessUtil.imgToNoiseFilter(whiteB); //row高斯滤波	
		
		int[][][] pixel=Rowreduce(noiseFilter);//提取偶数行	
		pixel=transposition(pixel);//转置		
		pixel=ImagePreProcessUtil.imgToNoiseFilter(pixel);//col高斯滤波	
		pixel=Rowreduce(pixel);//提取偶数列	
		pixel=transposition(pixel);//转置
		bf[i]=ImageUtil.getImgByRGB(pixel);
		}
		return bf;
	}

}
