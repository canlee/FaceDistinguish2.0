package com.invindible.facetime.algorithm.feature;

import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import com.invindible.facetime.algorithm.LDA;
import com.invindible.facetime.model.LdaFeatures;
import com.invindible.facetime.util.image.ImageUtil;

public class GetPcaLda {

	private static GetPcaLda gpl = null;
	private static int height = 64;//等比例图片处理时的高
	private static int width = 64;//等比例图片处理时的宽
	private static int num = 5;//每个人的图片数量，在视频计算时，务必先设置此值,计算完毕后设置回5
	
	//懒汉式单例
	synchronized public static GetPcaLda getInstance(){
		if(gpl == null)
		{
			gpl = new GetPcaLda();
		}
		return gpl;
	}
	
	public static int getNum() {
		return num;
	}

	/**
	 * 为PCA、LDA算法，设置每个人的图片数量
	 * 计算前一定要记得设置
	 * @param num
	 */
	public static void setNum(int num) {
		GetPcaLda.num = num;
	}

	/**
	 * 获取最佳投影矩阵
	 * 同时计算PCA矩阵和LDA矩阵的特征值、特征向量
	 * 形参为：图片类型
	 * Pca结果保存至Features类中，Lda结果保存至LDAFeatures类中
	 * 最佳投影矩阵保存至LDAFeatures类中
	 */
	public static void getResult(BufferedImage[] bImg)
	{
		int pixLength = width * height;//图片一维数组长度
		
		double[][] vec = new double[pixLength][bImg.length];//bImg.length为图片数量
//		System.out.println("pixLength:" + pixLength);
//		System.out.println("icon.length:" + icon.length);
		for(int i=0; i<bImg.length; i++)
		{
//			System.out.println("i:" + i);
//			icon[i] = GetFeatureMatrix.ImageHandle(icon[i], width, height);//图片等比例处理
			
//			System.out.println("过了");
//			Image img = icon[i].getImage();
//			BufferedImage bimg = ImageUtil.ImageToBufferedImage(img);
			
			//获取第i张图片的像素，并转成double[]
			int[] imgVec = GetFeatureMatrix.getPixes(bImg[i]);//获取像素
			
			double[] imgVecD = GetFeatureMatrix.intToDouble(imgVec);//将int[]转成double[]
			
			//构造vec[][]，即N副人脸图像（一维）合成的二维数据
			for(int j=0; j<imgVecD.length; j++)
			{
				vec[j][i] = imgVecD[j];
			}
		}

		//-------------------------以下为：PCA部分--------------------------
		//计算平均人脸图像
		double[] aveVec = GetFeatureMatrix.calAveVector(vec);
		
		//计算每张图片的均差
		double[][] aveDev = GetFeatureMatrix.calAveDeviation(vec, aveVec);
		
		//计算协方差矩阵
		double[][] covMat = GetFeatureMatrix.calCovarianceMatrix(aveDev);		
		
		//通过SVD定理，计算矩阵的特征值  
		double[] eigenValue = GetFeatureMatrix.calEigenValue(covMat);
		
		//通过SVD定理，计算矩阵的特征向量
		double[][] featureVector = GetFeatureMatrix.calFeatureVector(covMat);
	
		//通过公式变换，求取真正的特征向量
		GetFeatureMatrix.changeFeatureVector();
		//-------------------------以上为：PCA部分--------------------------
		
		//-------------------------以下为：LDA部分--------------------------
		//计算每类的类内平均图像Xi(m i)
		LDA.calEachAveVector(vec, num);
		
		//计算所有训练图像的总体平均图像m
		LDA.calAveVector(vec);
		
		//计算每个图像的差值图像（每个类内图像减去本类平均图像）
		LDA.calAveDeviation(vec);
		
		//计算每类的差值图像
		LDA.calAveDeviationEach();
		
		//计算投影：中心化后的图像投影到Wpca （x）
		LDA.calProjectionAveDeviation();
		
		//计算投影：中心化后的类内平均图像投影到Wpca (mi)
		LDA.calProjectionAveDeviationEach();
		
		//计算投影：总体平均图像投影到Wpca (m)
		LDA.calProjectionAveVector();
		
		//计算类内散布矩阵Si
		LDA.calScatterMatrixSi();
		
		//计算总的类内散布矩阵Sw
		LDA.calScatterMatrixSw();
		
		//计算类间散布矩阵Sb
		LDA.calScatterMatrixSb();
		
//		//计算投影 Sw'
//		LDA.calSwProjection();
//		
//		//计算投影 Sb'
//		LDA.calSbProjection();
		
		//求Sw的逆矩阵
		LDA.calSwInverse();
		
		//计算矩阵 Sw-1*Sb(Sw-1即Sw的逆矩阵)
		LDA.calSwITimesSb();
		
		//通过SVD定理，求矩阵 Sw-1*Sb 的特征值
		LDA.calLDAEigenValue();
		
		//通过SVD定理，求矩阵 Sw-1*Sb 的特征向量
		LDA.calLDAFeatureVector();
		
		//保留 前C-1个 特征向量
		LDA.keepLDAFV();
		
		//计算最终投影矩阵
		LDA.calLastProjection();
	}
	
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//	}

}
