package com.invindible.facetime.algorithm;

import java.util.ArrayList;

import Jama.Matrix;

import com.invindible.facetime.algorithm.feature.Features;
import com.invindible.facetime.algorithm.feature.GetFeatureMatrix;
import com.invindible.facetime.model.LdaFeatures;

//混合马氏距离
public class MixedMahalnobisDistance {
	
	private static MixedMahalnobisDistance mmd = null;
	
	//懒汉式单例
	synchronized public static MixedMahalnobisDistance getInstance()
	{
		if(mmd == null)
		{
			mmd = new MixedMahalnobisDistance();
		}
		return mmd;
	}

	/**
	 * 计算混合马氏距离
	 * di(x) = Sqrt((x-μi)T * (Σi)-1 * (x-μi) )
	 * 其中μi表示第i类的均值(类内平均图像[length][i])
	 * Σi表示协方差矩阵
	 * （程序中用英文小写字母"u"代替符号"μ"）
	 * @paran 形参：x 为样本x的均值 [N][length]
	 */
	public static double[] calMixedMahalnobisDistance(double[] x,double[][] μi)
//	public static ArrayList<double[]> calMixedMahalnobisDistance(ArrayList<double[][]> arrX)
	{		
//		System.out.println("μi的维数为：[" + μi.length + "][" + μi[0].length +"]");
		//获取类的个数
		int cNum = LdaFeatures.getInstance().getPeopleNum();
//		System.out.println("cNum:" + cNum);
		
		//将原来的μi改成了mi
		//计算mi，将结果保存进ArrayList<double[][]> arrMi 中
		ArrayList<double[][]> arrMi = calMi();
		
		//将 mi 从ArrayList<double[][]> 转换成 double[][]，并存进单例中
		TransformMiToDouble();
		
		//额外添加的
		//计算μi
//		ArrayList<double[][]> arrμi = calμi();
		
//		将μi转成double[][]（这步并不是计算步骤，只是为了方便后期取数据，转一下格式）
//		TransformμiToDouble();
		
		//将传进来的μi从double[个数C][维数C-1] 转换成 C个  ArrayList<double[C-1][1]>，其中C为人数
		ArrayList<double[][]> arrμi = TransformμiToArr(μi);
		
		//计算Σi(α)相关数据x^
		calArrXi();
		
		//计算Σi
		calΣi();
		
		//计算Σ
		calΣ();
		
		//计算Σi(α)，将结果保存进ArrayList<double[][]> ΣiAz 中
		ArrayList<double[][]> ΣiA = calΣiA();
		
		//保存临时数据的数组
		double[] dix = new double[cNum];
		
//		//用来保存结果的队列
//		ArrayList<double[]> arrDix = new ArrayList<double[]>();
		
		//扩展x样例，从double[C-1]转成double[C-1][1]
		double[][] xExtend = new double[x.length][1];
		for(int k=0; k<x.length; k++)
		{
			xExtend[k][0] = x[k];
		}
		
//		for(int j=0; j<arrX.size(); j++)
//		{
//			double[][] x = arrX.get(j);
			for(int i=0; i<ΣiA.size(); i++)
			{
				
				//获取样本X矩阵
//				Matrix matrixX = new Matrix(x);
				Matrix matrixX = new Matrix(xExtend);
				
				//获取第i类的μi矩阵
				Matrix matrixμi = new Matrix(arrμi.get(i));
				
				//X矩阵 减去 μi矩阵
				Matrix matrixXMinusμ = matrixX.minus(matrixμi);
				
				//矩阵转置
				Matrix matrixXMinusμT = matrixXMinusμ.transpose();
				
				//获取Σi(α)矩阵
				Matrix matrixΣiA = new Matrix(ΣiA.get(i));
				
				// 求Σi(α)逆矩阵
				Matrix matrixΣiAInverse = matrixΣiA.inverse();
				
				//保存临时数据的矩阵
				Matrix matrixDix = matrixXMinusμT.times(matrixΣiAInverse).times(matrixXMinusμ);
				
				double[][] tempDix = matrixDix.getArray();
				
//				System.out.println("tempDix的维数:[" + tempDix.length +"][" + tempDix[0].length + "]");
//				System.out.println("tempDix[0][0]" + tempDix[0][0]);
				//求结果的开方
				dix[i] = Math.sqrt(tempDix[0][0]);
					
			}
//			arrDix.add(dix);
			
			//清空临时数据
//			dix = new double[cNum];
//		}
//		return arrDix;
		
		//将结果保存进单例中
		LdaFeatures.getInstance().setMixedMahalnobisDistance(dix);
		
		return dix;
		/*
		//获取协方差矩阵
		double[][] covArr = Features.getInstance().getCovarianceMatrix();
		*/
	}
	
	/**
	 * 计算最小距离
	 * 计算样本X与第i类的距离
	 */
	public static double[] calMinDistance(double[] x,double[][] μi)
	{
		//获取类的个数
		int cNum = LdaFeatures.getInstance().getPeopleNum();
		
		//这里不需要计算μi，暂时屏蔽
		//计算μi，即资料中的mi，将结果保存进ArrayList<double[][]> ΣiAz 中
//		ArrayList<double[][]> arrμi = calμi();
		
		//将传进来的μi从double[个数C][维数C-1] 转换成 C个  ArrayList<double[C-1][1]>，其中C为人数
		ArrayList<double[][]> arrμi = TransformμiToArr(μi);
		
		//暂时不需要这个格式转换
		//将μi转成double[][]（这步并不是计算步骤，只是为了方便后期取数据，转一下格式）
//		TransformμiToDouble();
		
		//扩展x样例，从double[C-1]转成double[C-1][1]
		double[][] xExtend = new double[x.length][1];
		for(int k=0; k<x.length; k++)
		{
			xExtend[k][0] = x[k];
		}
		
		//保存临时数据的数组
		double[] dix = new double[cNum];
		
		for(int i=0; i<arrμi.size(); i++)
		{
			
			//获取样本X矩阵
//			Matrix matrixX = new Matrix(x);
			Matrix matrixX = new Matrix(xExtend);
			
			//获取第i类的μi矩阵
			Matrix matrixμi = new Matrix(arrμi.get(i));
			
			//X矩阵 减去 μi矩阵
			Matrix matrixXMinusμ = matrixX.minus(matrixμi);
			
			//矩阵转置
			Matrix matrixXMinusμT = matrixXMinusμ.transpose();
			
			//保存临时数据的矩阵
			Matrix matrixDix = matrixXMinusμT.times(matrixXMinusμ);
			
			double[][] tempDix = matrixDix.getArray();
			
			//求结果的开方
			dix[i] = Math.sqrt(tempDix[0][0]);
				
		}
				
		return dix;
	}
	
	
//	/**
//	 * 拆分mi的方法
//	 * 将mi从double[][] 转换成 ArrayList<double[][]>
//	 * @return
//	 */
//	public static ArrayList<double[][]> splitMiToArr()
//	{
//		//获取mi的均值,[legnth][C]
//		double[][] mi = LdaFeatures.getInstance().getEachAveVector();
//		
//		//将mi拆分开来，分别放进arrMi中
//		ArrayList<double[][]> arrMi = new ArrayList<double[][]>();
//		for(int i=0; i<mi[0].length; i++)
//		{
//			double[][] tempMi = new double[mi.length][1];
//			for(int j=0; j<mi.length; j++)
//			{
//				tempMi[j][0] = mi[j][i];
//			}
//			arrMi.add(tempMi);
//		}
//		
//		LdaFeatures.getInstance().setArrMi(arrMi);
//		return arrMi;
//	}
	
//	/**
//	 * 计算μi
//	 */
//	public static ArrayList<double[][]> calμi(){
//		
//		int peoplenum = LdaFeatures.getInstance().getPeopleNum();
//		int photonum = LdaFeatures.getInstance().getNum();
//		
//		double[][] modelP=new double[peoplenum*photonum][peoplenum-1];
//		for(int i=0;i<peoplenum*photonum;i++){
//			modelP[i]=LDA.getInstance().calZ(icon[i]);//投影
//		}
//
//		double[][] modelMean=new double[peoplenum][peoplenum-1];
//		for(int i=0;i<peoplenum;i++){
//			for(int k=0;k<peoplenum-1;k++){
//				for(int j=0;j<photonum;j++){
//					modelMean[i][k]+=modelP[photonum*i+j][k];
//				}
//				modelMean[i][k]/=photonum;
//			}			
//		}
//		
//		return null;
//		
//	}
	
	
	/**
	 * 计算Mi
	 * 共C个[C-1][1]
	 */
	public static ArrayList<double[][]> calMi(){
		//以下μi实质上是mi
		
		//保存μi的数组,共C个[C-1][1]
		ArrayList<double[][]> arrμi = new ArrayList<double[][]>();
		
//		//将mi从double[像素][C] →转换成→ ArrayList<double[像素][1]>（队列大小为：C个）
//		ArrayList<double[][]> arrMi = splitMiToArr();
		
		//获取WpotT [C-1][length],length为128^2(width * height)
		double[][] WoptT = LdaFeatures.getInstance().getLastProjectionT();
		Matrix matrixWpotT = new Matrix(WoptT);
		
		//发现错误，应该取的是mi的差值.2013/6/9 21:30，下面改动
//		//获取mi的均值,[legnth][C]
//		double[][] mi = LdaFeatures.getInstance().getEachAveVector();
//		Matrix matrixMi = new Matrix(mi);
		
		//获取mi的差值,[length][C]
		double[][] mi = LdaFeatures.getInstance().getAveDeviationEach();
		Matrix matrixMi = new Matrix(mi);

		
		//计算μi = 矩阵(WpotT) * 矩阵(mi的均值)
		Matrix matrixμi = matrixWpotT.times(matrixMi);
		double[][] tempμi = matrixμi.getArray();
		
		//计算μi，将计算结果放进ArrayList<double[][]> arrμi中，共C个μi[C-1][1]
		for(int i=0; i<mi[0].length; i++) //μi的个数 与 mi个的个数 相同
		{
			//临时保存μi的数组[C-1][1]
			double[][] μi = new double[tempμi.length][1];
			
			//复制列向量
			for(int j=0; j<tempμi.length; j++)
			{
				μi[j][0] = tempμi[j][i];
			}
			
			//将μi加入队列中
			arrμi.add(μi);
		}
		
		//将arrμi保存进单例中
//		LdaFeatures.getInstance().setArrμi(arrμi);
		LdaFeatures.getInstance().setArrMi(arrμi);
		
		
		return arrμi;
	}
	
	/**
	 * 计算Σi(α)相关数据x^
	 * 共图片总数n个[C-1][1]
	 */
	public static ArrayList<double[][]> calArrXi(){
		
		//保存xi的数组(x^)，共图片总数n个[C-1][1]
		ArrayList<double[][]> arrXi = new ArrayList<double[][]>();
		
		//获取x
		ArrayList<double[][]> arrAveDev = LdaFeatures.getInstance().getAveDeviation();//每个图像的差值图像（每个类内图像减去本类平均图像） [像素][n]
		
		//获取WopT
		double[][] WoptT = LdaFeatures.getInstance().getLastProjectionT();
		Matrix matrixWpotT = new Matrix(WoptT);
		
		for(int i=0; i<arrAveDev.size(); i++)
		{
			Matrix aveDevTemp = new Matrix(arrAveDev.get(i));
			Matrix tempXi = matrixWpotT.times(aveDevTemp);
			double[][] xi = tempXi.getArray();
			arrXi.add(xi);
		}
		
		//保存进单例
		LdaFeatures.getInstance().setArrXi(arrXi);
		
		return arrXi;
	}
	
	/**
	 * 计算Σi
	 * 共有C个Σi[C-1][C-1],C为人数
	 */
	public static ArrayList<double[][]> calΣi()
	{
		
		//保存Σi的数组，共有C个Σi[C-1][C-1],C为人数
		ArrayList<double[][]> arrΣi = new ArrayList<double[][]>();
		
		//获取X的均值
		ArrayList<double[][]> arrXi = LdaFeatures.getInstance().getArrXi();
		
		//获取Mi
		ArrayList<double[][]> arrMi = LdaFeatures.getInstance().getArrMi();
		
		int n = arrXi.size();//图片总数n
		int num = LdaFeatures.getInstance().getNum();//每人的图片数量
		int index = 0;//mi的下标
		
//		System.out.println("啊！！！！！！！！！");
//		System.out.println("n:" + n);
//		System.out.println("num:" + num);
//		System.out.println("(n/num)-1:  " + ((n/num)-1));
		//用来存放临时计算结果的数组，将计算好的最终结果放入
		int ΣiSize;
		if( (n/num) == 1)
		{
			ΣiSize = 1;
		}
		else
		{
			ΣiSize = (n/num)-1;
		}
		double[][] Σi = new double[ΣiSize][ΣiSize];//[C-1][C-1]
		

		//用来存放临时矩阵相加结果的矩阵
		Matrix matrixΣi = new Matrix(Σi);
		
		for(int i=0; i<arrXi.size(); i++)
		{
			//计算( (xi[i]-mi[index]) * (xi[i]-mi[index])T )
			Matrix matrixXi = new Matrix(arrXi.get(i));
			Matrix matrixMi = new Matrix(arrMi.get(index));
			
			//矩阵相减，得原矩阵
			Matrix old = matrixXi.minus(matrixMi);
			
			//矩阵转置
			Matrix oldT = old.transpose();
			
			//原矩阵 和 转置矩阵 相乘
			Matrix temp = old.times(oldT);
			
			matrixΣi = matrixΣi.plus(temp);
			
			//每隔num个x矩阵，则
			//令index加一，表示取下一个mi矩阵
			//令这num个矩阵相加，并记为Σi矩阵 （num为每个人的图片数），同时加入ArrayList最终结果数组中
			//清空临时数据
			if( (i % num) == ( num - 1) )
			{
				//对矩阵中每一个数字除以n（图片总数）
				//求协方差的必须步骤
				matrixΣi.times(1/n);
				
				Σi = matrixΣi.getArray();
				arrΣi.add(Σi);
				index++;
				//清空临时数据
				Σi = new double[ΣiSize][ΣiSize];
				matrixΣi = new Matrix(Σi);
			}
		}
		//保存进单例
		LdaFeatures.getInstance().setArrΣi(arrΣi);	
		
		return arrΣi;
	}
	
	/**
	 * 计算Σ
	 * 
	 */
	public static double[][] calΣ()
	{
		//获取Σi
		ArrayList<double[][]> arrΣi = LdaFeatures.getInstance().getArrΣi();
		
		int ΣLength = arrΣi.get(0).length;
		//保存Σ的数组，共有[C-1][C-1],C为人数
		double[][] Σ = new double[ΣLength][ΣLength];
		
		//存放 矩阵相加 中间结果的矩阵
		Matrix matrixΣiAdd = new Matrix(Σ);
		
		for(int i=0; i<arrΣi.size(); i++)
		{
			Matrix matrixΣTemp = new Matrix(arrΣi.get(i));
			matrixΣiAdd = matrixΣiAdd.plus(matrixΣTemp);
		}
		Σ = matrixΣiAdd.getArray();
		
		//保存进单例
		LdaFeatures.getInstance().setΣ(Σ);
		
		return Σ;
	}
	
	/**
	 * 计算Σi(α)
	 * 共有C个Σi(α)[C-1][C-1],C为人数
	 */
	public static ArrayList<double[][]> calΣiA()
	{
		//保存Σi(α)的数组，共有C个Σi(α)[C-1][C-1],C为人数
		ArrayList<double[][]> arrΣiA = new ArrayList<double[][]>();
		
		//获取Σi
		ArrayList<double[][]> arrΣi = LdaFeatures.getInstance().getArrΣi();
		
		//获取Σ
		double[][] Σ = LdaFeatures.getInstance().getΣ();
		
		//"α"参数，在此处用英文小写字母"a"代替
		//"α"的取值是根据实验选定的
		double a = 0.05;
		
		//mi为第i类的训练样本数
		double mi = LdaFeatures.getInstance().getNum();
		
		//m为训练样本总数
		double m = (LdaFeatures.getInstance().getPeopleNum()) * mi;
		
		//计算Σi(α)，并将结果放入arrΣiA中
		Matrix matrixΣ = new Matrix(Σ);
		for(int i=0; i<arrΣi.size(); i++)
		{
			Matrix matrixΣi = new Matrix(arrΣi.get(i));
			
			double temp1 = (1-a) * mi;
			double temp2 = a * m;
			
			Matrix matrixTemp1 = matrixΣi.times(temp1);
			Matrix matrixTemp2 = matrixΣ.times(temp2);
			
			//临时存放Σi进matrixΣi中
			Matrix matrixΣiA = matrixTemp1.plus(matrixTemp2);
			matrixΣiA = matrixΣiA.times(1 / (temp1 + temp2));
			
			double[][] ΣiA = matrixΣiA.getArray();
			arrΣiA.add(ΣiA);
		}
		
		//保存进单例
		LdaFeatures.getInstance().setArrΣiA(arrΣiA);
		
		return arrΣiA;
	}
	
	/**
	 * 转换mi的格式
	 * 将mi从 ArrayList<double[][]> 转成 double[][]
	 */
	public static double[][] TransformMiToDouble()
	{
		//获取mi队列,共C个[C-1][1]
		ArrayList<double[][]> arrMi = LdaFeatures.getInstance().getArrMi();
		
		int miSize = arrMi.size();//mi的个数
		int miLength = arrMi.get(0).length;//每个mi的长度
		
		//mi数组，要保存的结果将存进这里
		double[][] mi = new double[miSize][miLength];
		
		for(int i=0; i<miSize; i++)
		{
			double[][] temp = arrMi.get(i);//[C-1][1]
			for(int j=0; j<miLength; j++)
			{
				mi[i][j] = temp[j][0];
			}
			
		}
		//将结果保存进单例中
		LdaFeatures.getInstance().setMi(mi);
		
		return mi;
		
	}
	
	
	/**
	 * 转换μi的格式
	 * 将μi从 ArrayList<double[][]> 转成 double[][]
	 */
	public static double[][] TransformμiToDouble()
	{
		//获取μi队列
		ArrayList<double[][]> arrμi = LdaFeatures.getInstance().getArrμi();
		
		int μiSize = arrμi.size();//μi的个数
		int μiLength = arrμi.get(0).length;//每个μi的长度
		
		//μi数组，要保存的结果将存进这里
		double[][] μi = new double[μiSize][μiLength];
		
		for(int i=0; i<μiSize; i++)
		{
			double[][] temp = arrμi.get(i);//[C-1][1]
			for(int j=0; j<μiLength; j++)
			{
				μi[i][j] = temp[j][0];
			}
			
		}
		//将结果保存进单例中
		LdaFeatures.getInstance().setμi(μi);
		
		return μi;
		
	}
	
	/**
	 * 转换μi的格式
	 * 将μi从double[][] 转成  ArrayList<double[][]>
	 */
	public static ArrayList<double[][]> TransformμiToArr(double[][] μi)
	{
		//保存结果的队列
		ArrayList<double[][]> arrμi = new ArrayList<double[][]>();
		
		int μiSize = μi.length;//μi的个数
		int μiLength = μi[0].length;//每个μi的长度
		
		for(int i=0; i<μiSize; i++)
		{
			double[][] temp = new double[μiLength][1];
			for(int j=0; j<μiLength; j++)
			{
				temp[j][0] = μi[i][j];
			}
			arrμi.add(temp);
		}
		
		//将结果保存进单例中
		LdaFeatures.getInstance().setArrμi(arrμi);
		
		return arrμi;
	}
	
	/**
	 * 计算总体均值图像的在(Wopt T)空间上的投影
	 * 融合PCA与LDA变换的仿生人脸识别研究，步骤5中最后一个m，用（WoptT）代替（WpcaT）
	 * 原结果为列向量[C-1][1]，现在将结果转成行向量[1][C-1]
	 */
	public static double[] calProjectionTotalMInWoptT(){
		//获取总体均值图像[length]
		
		double[] aveVector = LdaFeatures.getInstance().getAveVector();
		
		//扩展总体均值图像从double[length]转为double[length][1]
		double[][] m = new double[aveVector.length][1];
		for(int i=0; i<aveVector.length; i++)
		{
			m[i][0] = aveVector[i];
		}
		Matrix matrixM = new Matrix(m);
		
		//获取WpotT [C-1][length],length为128^2(width * height)
		double[][] WoptT = LdaFeatures.getInstance().getLastProjectionT();
		Matrix matrixWpotT = new Matrix(WoptT);
		
		//获取投影结果
		Matrix matrixProjection = matrixWpotT.times(matrixM);
		//mProjection[C-1][1]
		double[][] mProjection = matrixProjection.getArray();
		
		int mProjectionSize = mProjection[0].length;//投影的大小[1]
		int mProjectionLength = mProjection.length;//投影的维数[3]
		
		//将结果从列向量[3][1]转成行向量[1][3]
		double[] mProjectionTrans = new double[mProjectionLength];
		
		for(int i=0; i<mProjectionLength; i++)
		{
			mProjectionTrans[i] = mProjection[i][0];
		}
		
		//保存进单例(若不需要，可以去掉)
		LdaFeatures.getInstance().setProjectionTotalMInWoptT(mProjectionTrans);
		
		return mProjectionTrans;
	}
}
