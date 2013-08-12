package com.invindible.facetime.algorithm;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;


import com.invindible.facetime.algorithm.feature.Features;
import com.invindible.facetime.algorithm.feature.GetFeatureMatrix;
import com.invindible.facetime.model.LdaFeatures;
import com.invindible.facetime.util.image.ImageUtil;

import Jama.Matrix;
import Jama.SingularValueDecomposition;

public class LDA {
	public static LDA lda = null;
	private static int height = 128;//等比例图片处理时的高
	private static int width = 128;//等比例图片处理时的宽
	
	//懒汉式单例
	synchronized public static LDA getInstance(){
		if(lda == null)
		{
			lda = new LDA();
		}
		return lda;
	}
	
	/**
	 * 计算每类的类内平均图像Xi(m i)
	 * 形参vec中存放的数据为C人（每人N张图）合成的二维数据
	 * 形参num为每个人的图片数量（例如：一人3张图片，则num = 3）  
	 */
	public static double[][] calEachAveVector(double[][] vec,int num) {
		//num应该从单例中或从形参中获取，提前规定好每个人有多少张图（N）
		//int num = 3;//每个人N张图
		int index = 0;//下标
		double temp = 0;//临时数据 
		
		int length = vec.length;//length指向量的维数，X={1,2,3,...,length}
		int n = vec[0].length;//n指向量的个数，X1,X2,X3...Xn;
		double[][] vAve = new double[length][n/num];//初始化类内平均向量(的维数)
		
		
		//将人的数量保存进单例中
		int peopleNum = n / num;
		LdaFeatures.getInstance().setPeopleNum(peopleNum);
		
		//将num保存进单例中(其实可以不用的)
		LdaFeatures.getInstance().setNum(num);
		
		
		//构建平均向量的值
		for(int i=0; i<length; i++)
		{
			index = 0;//换行，列下标清零
			for(int j=0; j<n; j++)
			{
				temp += vec[i][j];
				if( (j % num) == (num - 1)) //若刚好凑够num张，则赋值
				{
					temp  = temp / num;//取平均值
					vAve[i][index] = temp;
					index++;//列下标加一
					temp = 0;//temp清零
				}
			}
		}
		LdaFeatures.getInstance().setEachAveVector(vAve);
		return vAve;		
	}
	
	/**
	 * 计算所有训练图像的总体平均图像m
	 * 形参vec中存放的数据为C人（每人N张图）合成的二维数据
	 */
	public static double[] calAveVector(double[][] vec) {
		int length = vec.length;//length指向量的维数，X={1,2,3,...,length}
		int n = vec[0].length;//n指向量的个数，X1,X2,X3...Xn;
		double[] vAve = new double[length];//初始化平均向量(的维数)
		
		//构建平均向量的值
		for(int i=0; i<length; i++)
		{
			double temp = 0;
			for(int j=0; j<n; j++)
			{
				temp += vec[i][j];
			}
			temp = temp / n;
			vAve[i] = temp;
		}
		LdaFeatures.getInstance().setAveVector(vAve);
		return vAve;		
	}
	
	/**
	 * 计算每个图像的差值图像（每个类内图像减去本类平均图像）
	 * 形参vec中存放的数据为C人（每人N张图）合成的二维数据
	 */
	public static ArrayList<double[][]> calAveDeviation(double[][] vec) {
		ArrayList<double[][]> arrResult = new ArrayList<double[][]>();//存放结果的数组
		
		//获取 类内平均图像 [像素][C个人]
		//当每人只有1张图片时，类内平均图像要设置成全0
		//以便计算 每个图像的差值图像
		double[][] eachAveVector = LdaFeatures.getInstance().getEachAveVector();
		int length = vec.length;//length指向量的维数，X={1,2,3,...,length}
		int n = vec[0].length;//n指向量的个数，X1,X2,X3...Xn;
		double[][] resultTemp = new double[length][n];
		int index = 0;//下标
		int num = LdaFeatures.getInstance().getNum();
		
		//--------------------------当每人照片只有1张时特殊处理-------------------------------
		//若每个人只有1张图片，则将 "类内平均图像mi" 暂时设定为[4096][C]全0
		//暂时设定的结果不保存进单例中，单例中的mi仍然是正常的mi
		if( num == 1)
		{
			for(int i=0; i<eachAveVector[0].length; i++)
			{
				for(int j=0; j< eachAveVector.length; j++)
				{
					eachAveVector[j][i] = 0;
				}
			}
		}
		
		for(int i=0; i<length; i++)
		{
//			System.out.println("i:" + i);
			index = 0;
			for(int j=0; j<n; j++)
			{
//				System.out.println("index:" + index);
//				System.out.println("j:" + j);
				resultTemp[i][j] = vec[i][j] - eachAveVector[i][index];
				if( (j % num) == (num - 1) )
				{
					index++;
//					System.out.println("满足条件时,j:" + j + "num:" + num);
				}
			}
		}
		
		//将结果的double[][]类型保存进单例中
		LdaFeatures.getInstance().setAveDeviationDouble(resultTemp);
		
		//将每副图的数据保存进temp，再装入ArrayList中
		for(int i=0; i<n; i++)
		{
			double[][] temp = new double[length][1];
			for(int j=0; j<length; j++)
			{
				temp[j][0] = resultTemp[j][i];
			}
			arrResult.add(temp);
		}
		
//		Matrix tempMat = new Matrix(resultTemp);
//		Matrix tempMatT = tempMat.transpose();
//		resultTemp = tempMatT.getArray();
//		for(int i=0; i<resultTemp.length; i++)
//		{
//			double[][] result = new double[resultTemp.length][1];
//			arrResult.add(result);
//		}
		LdaFeatures.getInstance().setAveDeviation(arrResult);
		return arrResult;
	}
	
	/**
	 * 计算每类的差值图像
	 */
	public static double[][] calAveDeviationEach(){
		double[] aveVector = LdaFeatures.getInstance().getAveVector();//总体平均图像[像素]
		double[][] eachAveVector = LdaFeatures.getInstance().getEachAveVector();//类内平均图像 [像素][C个人]
		int length = eachAveVector.length;
		int n = eachAveVector[0].length;
		double[][] result = new double[length][n];
		
		for(int i=0; i<length; i++)
		{
			for(int j=0; j<n; j++)
			{
				result[i][j] = eachAveVector[i][j] - aveVector[j];
			}
		}
		LdaFeatures.getInstance().setAveDeviationEach(result);
		return result;
	}
	
	/**
	 * 计算投影：中心化后的图像投影到Wpca （x）
	 */
	public static ArrayList<double[][]> calProjectionAveDeviation(){
		ArrayList<double[][]> arrResult = new ArrayList<double[][]>();//保存结果的数组
		
		ArrayList<double[][]> arrAveDev = LdaFeatures.getInstance().getAveDeviation();//每个图像的差值图像（每个类内图像减去本类平均图像） [像素][n]
		double[][] Wpca = Features.getInstance().getResultFeatureVector();
		
		Matrix pca = new Matrix(Wpca);
		Matrix pcaT = pca.transpose();
		
		for(int i=0; i<arrAveDev.size(); i++)
		{
			Matrix aveDevTemp = new Matrix(arrAveDev.get(i));
			Matrix resultMatrix = pcaT.times(aveDevTemp);
			double[][] resultTemp = resultMatrix.getArray();
			arrResult.add(resultTemp);
		}
		LdaFeatures.getInstance().setProjectionAveDeviation(arrResult);
		return arrResult;
	}
	
	/**
	 * 计算投影：中心化后的类内平均图像投影到Wpca (mi)
	 */
	public static ArrayList<double[][]> calProjectionAveDeviationEach() {
		ArrayList<double[][]> arrResult = new ArrayList<double[][]>();//存放结果的数组
		
		double[][] eachAveVector = LdaFeatures.getInstance().getAveDeviationEach();//类内平均图像[像素][C个人]
		double[][] Wpca = Features.getInstance().getResultFeatureVector();
		
		Matrix pca = new Matrix(Wpca);
		Matrix pcaT = pca.transpose();
		Matrix aveDev = new Matrix(eachAveVector);
		Matrix resultMatrix = pcaT.times(aveDev);
		
		double[][] resultTemp = resultMatrix.getArray();
		//将结果存进ArrayList
		for(int i=0; i<resultTemp[0].length; i++)
		{
			double[][] result = new double[resultTemp.length][1];
			//复制列向量，并装入ArrayList
			for(int j=0; j<resultTemp.length; j++)
			{
				result[j][0] = resultTemp[j][i];
			}
			arrResult.add(result);
		}
		LdaFeatures.getInstance().setProjectionAveDeviationEach(arrResult);
		return arrResult;
	}
	
	/**
	 * 计算投影：总体平均图像投影到Wpca (m)
	 */
	public static double[][] calProjectionAveVector() {
		double[] aveVector = LdaFeatures.getInstance().getAveVector();//总体平均图像[像素]
		double[][] Wpca = Features.getInstance().getResultFeatureVector();
		
		//将平均图像扩展一下
		double[][] aveVectorExtend = new double[aveVector.length][1];
		for(int i=0; i<aveVector.length; i++)
		{
			aveVectorExtend[i][0] = aveVector[i];
		}
		
		Matrix pca = new Matrix(Wpca);
		Matrix pcaT = pca.transpose();
		Matrix aveDev = new Matrix(aveVectorExtend);
		Matrix resultMatrix = pcaT.times(aveDev);
		
		double[][] result = resultMatrix.getArray();
		LdaFeatures.getInstance().setProjectionAveVector(result);
		return result;
	}
	
	/**
	 * 计算类内散布矩阵Si
	 */
	public static ArrayList<double[][]> calScatterMatrixSi()
	{
		//获取(x) n个[128^2][1]，n为图片的总数=C人数*num每个人的图片数量
		ArrayList<double[][]> arrProjectionAveDeviation = 
				LdaFeatures.getInstance().getProjectionAveDeviation();
		
		//获取(mi) C个[128^2][1]，C为人数
		ArrayList<double[][]> arrProjectionAveDeviationEach = 
				LdaFeatures.getInstance().getProjectionAveDeviationEach();
		
		int xSize = arrProjectionAveDeviation.size();//图片总数n
		int num = LdaFeatures.getInstance().getNum();
		int index =0;//mi的下标
		
		//用来存放临时计算结果的数组，将计算好的最终结果放入ArrayList中
		double[][] resultTemp = new double[xSize][xSize];
		
		//用来存放临时矩 阵相加 结果的矩阵
		Matrix matrixTemp = new Matrix(resultTemp);
		
		//用来存放最终结果的数组
		ArrayList<double[][]> result= new ArrayList<double[][]>();
		
		for(int i=0; i<arrProjectionAveDeviation.size(); i++)
		{
			//计算( (x-mi)*(x-mi)T )
			Matrix x = new Matrix(arrProjectionAveDeviation.get(i));
			Matrix mi = new Matrix(arrProjectionAveDeviationEach.get(index));
			
			//矩阵相减，得原矩阵
			Matrix temp = x.minus(mi);
			
			//矩阵转置
			Matrix tempT = temp.transpose();
			
			//原矩阵 和 转置矩阵 相乘
			Matrix resultMatrix = temp.times(tempT);
			
			//矩阵相加，以构成Si
			matrixTemp = matrixTemp.plus(resultMatrix);
			
			//每隔num个x矩阵，则
			//令index加一，表示取下一个mi矩阵
			//令这num个矩阵相加，并记为Si矩阵 （num为每个人的图片数），同时加入ArrayList最终结果数组中
			//清空临时数据
			if( (i % num) == ( num - 1) )
			{
				resultTemp = matrixTemp.getArray();
				result.add(resultTemp);
				
//				System.out.println("resultTemp:" + resultTemp[0][0]);
//				System.out.println("i:" + i);
//				System.out.println("matrixTemp");
//				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//				matrixTemp.print(6, 4);
//				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				
				index++;
				//清空临时数据
				resultTemp = new double[xSize][xSize];
				matrixTemp = new Matrix(resultTemp);
			}
		}
		
		LdaFeatures.getInstance().setSi(result);
		return result;
	}
	
	/**
	 * 计算总的类内散布矩阵Sw
	 */
	public static double[][] calScatterMatrixSw()
	{
		ArrayList<double[][]> si = LdaFeatures.getInstance().getSi();
		
		int swSize = si.size();
//		int swSize = si.get(0).length;
		int swLength = si.get(0).length;
		//存放最终结果的数组
		double[][] result = new double[swLength][swLength];
		
		//存放 矩阵相加 中间结果的矩阵
		Matrix matrixResult = new Matrix(result);
//		System.out.println("swSize:" + swSize);
		
		for( int i=0; i<swSize; i++)
		{
			Matrix matrixTemp = new Matrix(si.get(i));
//			System.out.println("行:" +matrixTemp.getRowDimension());
//			System.out.println("列："+matrixTemp.getColumnDimension());
			matrixResult = matrixResult.plus(matrixTemp);
//			System.out.println("matrixTemp");
//			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//			matrixTemp.print(6, 4);
//			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		}
		result = matrixResult.getArray();
		

		
		LdaFeatures.getInstance().setSw(result);
		return result;
	}
	
	/**
	 * 计算类间散布矩阵Sb
	 */
	public static double[][] calScatterMatrixSb()
	{
		ArrayList<double[][]> mi = LdaFeatures.getInstance().getProjectionAveDeviationEach();
		int miSize = mi.size();
		
		double[][] m = LdaFeatures.getInstance().getProjectionAveVector();
		Matrix matrixM = new Matrix(m);
		
		int num = LdaFeatures.getInstance().getNum();
		
		//存放最终结果的数组
		double[][] result = new double[m.length][m[0].length];
		
		//存放 矩阵相加 中间结果的数组
		double[][] forMatrix = new double[m.length][m.length];//为了创建矩阵而设立的数组
		Matrix matrixResult = new Matrix(forMatrix);
		
		//开始计算
		for( int i=0; i<miSize; i++)
		{
			double[][] miTemp = mi.get(i);
			Matrix matrixMi = new Matrix(miTemp);
			
			Matrix matrixTemp = matrixMi.minus(matrixM);//矩阵相减
			Matrix matrixTempT = matrixTemp.transpose();//矩阵转置
			
			//矩阵相乘，并乘以num
			Matrix ma = matrixTemp.times(matrixTempT);
			ma = ma.times(num);
			
//			System.out.println("i:" + i);
//			System.out.println("ma行：" + ma.getRowDimension());
//			System.out.println("ma列:" + ma.getColumnDimension());
//			
//			System.out.println("matrixResult行:" + matrixResult.getRowDimension());
//			System.out.println("matrixResult列:" + matrixResult.getColumnDimension());
			
			matrixResult = matrixResult.plus(ma);

		}
		result = matrixResult.getArray();
		
//		System.out.println("sb行:" + matrixResult.getRowDimension());
//		System.out.println("sb列:" + matrixResult.getColumnDimension());
		
		LdaFeatures.getInstance().setSb(result);
		return result;
	}
	
	/**
	 * 计算投影 Sw'
	 */
	public static double[][] calSwProjection()
	{
		//存放结果的数组
		double[][] result = new double[1][1];
		
		double[][] sw = LdaFeatures.getInstance().getSw();
		
		double[][] wPca = Features.getInstance().getResultFeatureVector();//获取投影Wpca
		
		Matrix matrixWPca = new Matrix(wPca);
		Matrix matrixWPcaT = matrixWPca.transpose();//矩阵转置
		Matrix matrixSw = new Matrix(sw);
		
		System.out.println("matrixWpcaT行：" + matrixWPcaT.getRowDimension());
		System.out.println("matrixWpcaT列:" + matrixWPcaT.getColumnDimension());
		System.out.println("Sw行：" + matrixSw.getRowDimension());
		System.out.println("Sw列:" + matrixSw.getColumnDimension());
		System.out.println("matrixWpca行：" + matrixWPca.getRowDimension());
		System.out.println("matrixWpca列:" + matrixWPca.getColumnDimension());
		
		//WpcaT * Sw * Wpca
		Matrix matrixResult = matrixWPcaT.times(matrixSw).times(matrixWPca);

		
		result = matrixResult.getArray();
		
		LdaFeatures.getInstance().setSwProjection(result);
		return result;
	}
	
	/**
	 * 计算投影 Sb'
	 */
	public static double[][] calSbProjection()
	{
		//存放结果的数组
		double[][] result = new double[1][1];
		
		double[][] sb = LdaFeatures.getInstance().getSb();
		
		double[][] wPca = Features.getInstance().getResultFeatureVector();//获取投影Wpca
		
		Matrix matrixWPca = new Matrix(wPca);
		Matrix matrixWPcaT = matrixWPca.transpose();//矩阵转置
		Matrix matrixSb = new Matrix(sb);
		
		//WpcaT * Sb * Wpca
		Matrix matrixResult = matrixWPcaT.times(matrixSb).times(matrixWPca);
		
		result = matrixResult.getArray();
		
		LdaFeatures.getInstance().setSbProjection(result);
		return result;
	}
	
	
	/**
	 * 求Sw'的逆矩阵
	 */
	public static double[][] calSwInverse()
	{
		double[][] sw = LdaFeatures.getInstance().getSw(); //获取Sw矩阵
//		double[][] sw = LdaFeatures.getInstance().getSwProjection(); //获取Sw'矩阵
		Matrix matrixSw = new Matrix(sw);
//		System.out.println("matrixSw行：" + matrixSw.getRowDimension());
//		System.out.println("matrixSw列:" + matrixSw.getColumnDimension());
//		
//		System.out.println("--------------------------------------");
//		matrixSw.print(8, 6);
//		System.out.println("--------------------------------------");
		
		Matrix swInverse = matrixSw.inverse(); //求逆矩阵
		double[][] result = swInverse.getArray();
		LdaFeatures.getInstance().setSwInverse(result);
		return result;
	}
	
	/**
	 * 计算矩阵 (Sw')-1 * Sb'
	 * (Sw'即Sw的逆矩阵)
	 */
	public static double[][] calSwITimesSb()
	{
		double[][] SwI = LdaFeatures.getInstance().getSwInverse();//获取(Sw')-1
		double[][] Sb = LdaFeatures.getInstance().getSb();//获取Sb
		
		Matrix maSwI = new Matrix(SwI);
		Matrix maSb = new Matrix(Sb);
		
		Matrix maResult = maSwI.times(maSb); //矩阵相乘
		
		double[][] result = maResult.getArray();
		
		LdaFeatures.getInstance().setSwITimesSb(result);//保存结果进单例
		return result;
	}
	
	/**
	 * 通过SVD定理，求矩阵 Sw'*Sb 的特征值
	 */
	public static double[] calLDAEigenValue()
	{
		//获取 Sw'*Sb 矩阵
		double[][] swITimesSb = LdaFeatures.getInstance().getSwITimesSb();
		Matrix matrixSwITSb = new Matrix(swITimesSb);
		
		SingularValueDecomposition s = matrixSwITSb.svd();
		Matrix svalues = new Matrix(s.getSingularValues(), 1);
		
		double[] result = svalues.getRowPackedCopy();
		LdaFeatures.getInstance().setLDAEigenValue(result);
		return result;
	}
	
	/**
	 * 通过SVD定理，求矩阵 Sw'*Sb 的特征向量
	 */
	public static double[][] calLDAFeatureVector()
	{
		double[][] swITimesSb = LdaFeatures.getInstance().getSwITimesSb();
		Matrix matrixSwITSb = new Matrix(swITimesSb);
		SingularValueDecomposition s = matrixSwITSb.svd();
		
		double[][] result = s.getV().getArray();
		LdaFeatures.getInstance().setLDAFeatureVector(result);
		return result;
	}
	
	/**
	 * 保留 前C-1个 特征向量
	 */
	public static double[][] keepLDAFV()
	{
		double[][] LDAFeatureVector = LdaFeatures.getInstance().getLDAFeatureVector();
		
		int keepNum = LdaFeatures.getInstance().getPeopleNum() - 1;//C-1。C即人数，类的数量。
		
		//保留前C-1列
		double[][] keep = new double[LDAFeatureVector.length][keepNum];
		for(int i=0; i<LDAFeatureVector.length; i++)
		{
			for(int j=0; j<keepNum; j++)
			{
				keep[i][j] = LDAFeatureVector[i][j];
			}
		}
		
		LdaFeatures.getInstance().setKeepFV(keep);
		return keep;
	}
	
	/**
	 * 计算最终投影矩阵
	 */
	public static double[][] calLastProjection()
	{
//		double[][] wPca = Features.getInstance().getFeatureVector();//获取投影Wpca
		double[][] wPca = Features.getInstance().getResultFeatureVector();//获取投影Wpca
		double[][] wFld = LdaFeatures.getInstance().getKeepFV();//获取投影Wfld(Wlda保留前C-1个特征向量)
		
		Matrix maWPca = new Matrix(wPca);
		Matrix maWLda = new Matrix(wFld);
		
		Matrix maLastProjection = maWPca.times(maWLda);//矩阵相乘
		
//		//---------------------------测试语句----------------------------------
//		System.out.println("maWpca:" + maWPca.getRowDimension() + " " + maWPca.getColumnDimension());
//		System.out.println("maWLda:" + maWLda.getRowDimension() + " " + maWLda.getColumnDimension());
//		//--------------------------------------------------------------------
		
		//将原矩阵存进单例
		double[][] result = maLastProjection.getArray();
		LdaFeatures.getInstance().setLastProjection(result);
		
		//矩阵转置,从W转成Wt（涉及到2份资料之间的差别）
		maLastProjection = maLastProjection.transpose();
		
		double[][] resultT = maLastProjection.getArray();
		
		//将转置矩阵存进
		LdaFeatures.getInstance().setLastProjectionT(resultT);
		
		
		return result;
	}
	
	/**
	 * 获取指定图片的特征向量Z
	 */
	public static double[] calZ(BufferedImage bImg)
	{
//		icon = GetFeatureMatrix.ImageHandle(icon, width, height);//图片等比例处理
//		Image img = icon.getImage();
//		BufferedImage bimg = ImageUtil.ImageToBufferedImage(img);
		//获取第i张图片的像素，并转成double[]
		int[] imgVec = GetFeatureMatrix.getPixes(bImg);//获取像素
		double[] imgVecD = GetFeatureMatrix.intToDouble(imgVec);//将int[]转成double[]
		
		double[] m = LdaFeatures.getInstance().getAveVector();
		
		//获取(x-m)
		double[][] temp = new double[imgVecD.length][1];
		for(int i=0; i<imgVecD.length; i++)
		{
			temp[i][0] = imgVecD[i] - m[i];
		}
		
		Matrix maTemp = new Matrix(temp);
		
		double[][] lastProjection = LdaFeatures.getInstance().getLastProjectionT();//获取WoptT
//		System.out.println("Wopt的维数为:[" + lastProjection.length + "][" + lastProjection[0].length + "]");
		Matrix maWopt = new Matrix(lastProjection);
		
		
//		//------------------测试语句--------------------
//		System.out.println("Wpot的维数:");
//		System.out.println("Wpot:[" + maWopt.getRowDimension() + "][" + maWopt.getColumnDimension() + "]");
//		System.out.println("(x-m)的维数:");
//		System.out.println("(x-m):[" + maTemp.getRowDimension() + "][" + maTemp.getColumnDimension() + "]");
//		//测试语句--------------------------------------
		
		
		//计算Z: Wopt * (x-m)
		Matrix maResult = maWopt.times(maTemp);
		
		//resultBefore是一个[C-1][1]的数组
		double[][] resultBefore = maResult.getArray();
		
		//result要将resultBefore转成[1][C-1]的数组
		double[] result = new double[resultBefore.length];
		for(int i=0; i<resultBefore.length; i++)
		{
			result[i] = resultBefore[i][0];
		}
		
		LdaFeatures.getInstance().setZ(result);
		return result;
	}
	
	/**
	 * 计算Wpot矩阵的特征值
	 */
	public static double[] calWpotEigenValue()
	{
		//获取Wopt矩阵
		double[][] wopt = LdaFeatures.getInstance().getLastProjection();
		
		Matrix matrixWopt = new Matrix(wopt);
		
		SingularValueDecomposition s = matrixWopt.svd();
		Matrix svalues = new Matrix(s.getSingularValues(), 1);
		
		double[] result = svalues.getRowPackedCopy();
		LdaFeatures.getInstance().setWoptEigenValue(result);
		return result;
		
	}
}
