package com.invindible.facetime.algorithm.feature;

import com.invindible.facetime.util.image.*;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import Jama.Matrix;
import Jama.SingularValueDecomposition;

public class GetFeatureMatrix {
	public static GetFeatureMatrix gfm = null;
	
	GetFeatureMatrix() {}
	
	//懒汉式单例
	synchronized public static GetFeatureMatrix getInstance(){
		if(gfm == null)
		{
			gfm = new GetFeatureMatrix();
		}
		return gfm;
	}
	
	//ArrayList<double[]> arr = new ArrayList<double[]>();
	double[] v1 = new double[100];
	private static int nOld;
	private static int height = 64;//等比例图片处理时的高
	private static int width = 64;//等比例图片处理时的宽
	/**
	 * 将int[]转化成double[]
	 */
	public static double[] intToDouble(int[] intArr) {
		double[] doubleArr = new double[intArr.length];
		for(int i=0; i<intArr.length; i++)
		{
			doubleArr[i] = (double)intArr[i];
		}
		return doubleArr;
	}
	
	
	/**
	 * 将double[]转化成int[]
	 */
	public static int[] doubleToInt(double[] doubleArr) {
		int[] intArr = new int[doubleArr.length];
		for(int i=0; i<doubleArr.length; i++)
		{
			intArr[i] = (int)doubleArr[i];
		}
		return intArr;
	}
	
	
	/**
	 * 将二维数组转化为一维数组
	 * @param double[][] num
	 * @return double[]
	 */
	public static double[] twoDToOneD(double[][] num) {
		int h = num.length; //行数
		int w = num[0].length; //列数
		double[] result = new double[h * w];
		int x = 0;
		for(int i = 0; i < h; i++) {
			for(int j = 0; j < w; j++) {
				result[x++] = num[i][j];
			}
		}
		return result;
	}
	
	/**
	 * 获取某张图的所有的像素信息
	 * @param bimg
	 * @return
	 */
	public static int[] getPixes(BufferedImage bimg) {
        int w = bimg.getWidth();
        int h = bimg.getHeight();
        int[] rgbs = new int[h * w];
        bimg.getRGB(0, 0, w, h, rgbs, 0, w);
        return rgbs;
    }
	
	
	/**
	 * 根据图片的像素来获取图片
	 * @param width
	 * @param height
	 * @param pixels
	 * @return
	 */
	public static Image getImgByPixels(int width, int height, int[] pixels) {
		MemoryImageSource source;
        Image image = null;
        source = new MemoryImageSource(width, height, pixels, 0, width);
        image = Toolkit.getDefaultToolkit().createImage(source);
        
        nOld = width; //将原来的N*N中的N记录下来
        
        return image;
	}
	
	/**
	 * 计算平均人脸图像
	 * 形参vec中存放的数据为N副人脸图像（一维）合成的二维数据
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
		Features.getInstance().setAveVector(vAve);
		return vAve;		
	}
	
	/**计算一张图片的均差，仅针对一个人的图像做计算
	 * 形参icon为1副人脸图像（ImageIcon类型）
	 */
	public static double[] calAveDeviationOneObject(ImageIcon icon)
	{
		icon = ImageHandle(icon, width, height);//图片等比例处理
		Image img = icon.getImage();
		BufferedImage bimg = ImageUtil.ImageToBufferedImage(img);
		//获取第i张图片的像素，并转成double[]
		int[] imgVec = getPixes(bimg);//获取像素
		double[] imgVecD = intToDouble(imgVec);//将int[]转成double[]
		
		double[] result = calAveDeviationOneObject(imgVecD);
		
		return result;
	}

	
	/**计算一张图片的均差，仅针对一个人的图像做计算
	 * 形参vec中存放的数据为1副人脸图像（一维）
	 */
	public static double[] calAveDeviationOneObject(double[] vec)
	{
		double[] newVec = new double[vec.length];
		double[] vAve = Features.getInstance().getAveVector();//获取平均人脸图像
		
		//计算这张图片的均差
		for(int i=0; i<vec.length; i++)
		{
			newVec[i] = vec[i] - vAve[i];
		}
		
		return newVec;
	}
	
	/**
	 * 计算每张图片的均差
	 * 形参vec中存放的数据为N副人脸图像（一维）组成的二维向量
	 * 形参vAve为平均人脸图像的向量
	 */
	public static double[][] calAveDeviation(double[][] vec, double[] vAve) {
		int length = vec.length;//length指向量的维数，X={1,2,3,...,length}
		int n = vec[0].length;//n指向量的个数，X1,X2,X3...Xn;
		double[][] newVec = new double[length][n];
		
		//计算每张图片的均差
		for(int i=0; i<length; i++)
		{
			for(int j=0; j<n; j++)
			{
				newVec[i][j] = vec[i][j] - vAve[i];
			}
		}
		
		Features.getInstance().setAveDev(newVec);
		
		return newVec;//newVec为每幅图像的均差，同时也可以表示成一个矩阵。维数为：n*length;
	}
	
	/**
	 * 计算协方差矩阵
	 * 	传入形参为 <方法：计算每张图片的均差> 的返回值
	 * (其实这并不是协方差矩阵，若需要协方差矩阵，再用该矩阵除以N即可)
	 */
	public static double[][] calCovarianceMatrix(double[][] vec) {
		int length = vec.length;//length指向量的维数，X={1,2,3,...,length}
		int n = vec[0].length;//n指向量的个数，X1,X2,X3...Xn;
		
		
		Matrix vecOld = new Matrix(vec);
		
//		System.out.println("vecOld:");
//		vecOld.print(6, 2);
		
//		System.out.println();
		Matrix vecTrans = vecOld.transpose();//获取转置矩阵
//		System.out.println("vecTrans:");
//		vecTrans.print(6, 2);
		
		
		double[][] covArr = new double[nOld][nOld];//初始化协方差矩阵
		
		//构造协方差矩阵
//		Matrix cov = vecOld.times(vecTrans);
		Matrix cov = vecTrans.times(vecOld);
		
		covArr = cov.getArray();
		
		//由于最终计算并不需要协方差矩阵，仅仅要的是一部分，所以不用除以N
		//除以N
//		for(int i=0; i<n; i++)
//		{
//			for(int j=0; j<length; j++)
//			{
//				covArr[i][j] = covArr[i][j] / n;
//			}
//		}
		
		Features.getInstance().setCovarianceMatrix(covArr);
		
		return covArr;
	}
	
	/**
	 * 通过SVD定理，计算矩阵的特征值  
	 * 传入形参为 <方法：计算协方差矩阵> 的返回值
	 */
	public static double[] calEigenValue(double[][] vec) {
		//double[] eigenValue;
		Matrix vecMat = new Matrix(vec);
		SingularValueDecomposition s = vecMat.svd();
		Matrix svalues = new Matrix(s.getSingularValues(), 1);
		
		double[] result = svalues.getRowPackedCopy();
		Features.getInstance().setEigenValue(result);
		return result;
	}
	
	/**
	 * 通过SVD定理，计算矩阵的特征向量
	 * 传入形参为 <方法：计算协方差矩阵> 的返回值
	 */
	public static double[][] calFeatureVector(double[][] vec) {
		Matrix vecMat = new Matrix(vec);
		SingularValueDecomposition s = vecMat.svd();
		
		double[][] featureVector = s.getV().getArray();
		Features.getInstance().setFeatureVector(featureVector);
		return featureVector;
	}
	
	/**
	 * 将X'T*X'的特征向量 转换成 X'*X'T的特征向量 的方法
	 * @param eigenValue为特征值
	 * @param featureVec为特征向量
	 */
	public static double[][] changeFeatureVector()
	{
		double[][] AveDeviation = Features.getInstance().getAveDev();//每幅图的均差
		double[] eigenValue = Features.getInstance().getEigenValue();//特征值
		double[][] featureVec = Features.getInstance().getFeatureVector();//特征向量
		
//		System.out.println("width*height:" + width*height);//4096
//		System.out.println("featureVec[0].length" + featureVec[0].length);//10
		double[][] resultFeatureVector = new double[width*height][featureVec[0].length]; 
		
		for(int i=0; i<featureVec[0].length; i++)
		{
			//将N副图片的特征向量[N][N]分离提取出来，提取第i张图片的特征向量[N][1]
			double[][] temp = new double[featureVec.length][1];
			for(int j=0;j<featureVec.length; j++)
			{
				temp[j][0] = featureVec[j][i];
			}
			
			Matrix xOld = new Matrix(AveDeviation);
			Matrix featureVecOfOneObject = new Matrix(temp);
//			System.out.println("[" + xOld.getRowDimension() + "]" +
//					"[" + xOld.getColumnDimension() +"]");//[16384][10]
//			System.out.println("[" + featureVecOfOneObject.getRowDimension() + "]" +
//					"[" + featureVecOfOneObject.getColumnDimension() +"]");//[10][1]
			
			Matrix resultMatrix = xOld.times(featureVecOfOneObject);
//			System.out.println("[" + resultMatrix.getRowDimension() + "]" +
//					"[" + resultMatrix.getColumnDimension() +"]");
			
			double factor = 1 / (Math.sqrt(eigenValue[i]));
			resultMatrix.times(factor);
			
			double[][] temp2 = new double[1][1];
			temp2 = resultMatrix.getArray();
			
//			System.out.println("temp2.length:" + temp2.length);//16384-------------
//			System.out.println("temp2[0].length:" + temp2[0].length);//1
			//将构成的[N][1] 复制到[N][i]中
			for(int j=0; j<temp2.length; j++)
			{
				resultFeatureVector[j][i] = temp2[j][0];
			}
		}
		
		Features.getInstance().setResultFeatureVector(resultFeatureVector);
		return resultFeatureVector;
	}
	
	
	/**
	 * 图片等比例处理方法,width和height为宽度和高度
	 * @param imageicon
	 * @param width
	 * @param height
	 * @return
	 */
	public static ImageIcon ImageHandle(ImageIcon imageicon,int width,int height){
		Image image = imageicon.getImage();
		Image smallimage = image.getScaledInstance(width, height, image.SCALE_FAST);
		ImageIcon smallicon = new ImageIcon(smallimage);
		return smallicon;
	}
	
	/**
	 * 取图片特征值和特征向量方法
	 * 结果：[特征值]和[特征向量]保存在Features的[EigenValue]和[featureVector]内
	 * @param args 传入N张图片
	 * @return 返回空
	 */
	public static void imageToResult(ImageIcon[] icon) {
		int pixLength = width * height;//图片一维数组长度
		
//		Image[] img = new Image[icon.length];
//		BufferedImage[] bimg = new BufferedImage[icon.length];
		double[][] vec = new double[pixLength][icon.length];
//		System.out.println(icon.length + "!@#:" + pixLength);
		for(int i=0; i<icon.length; i++)
		{
			icon[i] = ImageHandle(icon[i], width, height);//图片等比例处理
			Image img = icon[i].getImage();
			BufferedImage bimg = ImageUtil.ImageToBufferedImage(img);
			
			//获取第i张图片的像素，并转成double[]
			int[] imgVec = getPixes(bimg);//获取像素
			
//			for(int z =0; z<imgVec.length; z++)
//			System.out.print(imgVec[z]);
//			System.out.println("_____________________________________");
			
			double[] imgVecD = intToDouble(imgVec);//将int[]转成double[]
			
			//构造vec[][]，即N副人脸图像（一维）合成的二维数据
			for(int j=0; j<imgVecD.length; j++)
			{
				vec[j][i] = imgVecD[j];
			}
		}

		//计算平均人脸图像
		double[] aveVec = calAveVector(vec);
		
		//计算每张图片的均差
		double[][] aveDev = calAveDeviation(vec, aveVec);
		
		//计算协方差矩阵
		double[][] covMat = calCovarianceMatrix(aveDev);		
		
		//通过SVD定理，计算矩阵的特征值  
		double[] eigenValue = calEigenValue(covMat);
//		Features.getInstance().setEigenValue(eigenValue);
		
		//通过SVD定理，计算矩阵的特征向量
		double[][] featureVector = calFeatureVector(covMat);
//		Features.getInstance().setFeatureVector(featureVector);
	
		//通过公式变换，求取真正的特征向量
		changeFeatureVector();
	}
	
	/**
	 * 输出特征脸
	 * 根据特征向量得出Image类型
	 * @param args
	 */
	public static Image[] getFeatureFaces()
	{
		double[][] rfVec = Features.getInstance().getResultFeatureVector();
		Matrix mat = new Matrix(rfVec);
		mat = mat.transpose();
		rfVec = mat.getArray();
		
		Image[] img = new Image[rfVec.length];
		for(int i=0; i<rfVec.length; i++)
		{
			System.out.println("i:" + i);
			//这一步可能会有压缩损失
			//最好找一个方法，直接从double[]转换成Image
			
			//将double[]转换成int[]
			int[][] rfVecInt = new int[rfVec.length][rfVec[0].length];
//			System.out.println("[" + rfVec.length + "]" + "[" + rfVec[0].length + "]");
			for(int j=0; j<rfVec[0].length; j++)
			{

//				rfVecInt[i][j] = (int)rfVec[i][j];//无四舍五入
				rfVecInt[i][j] = Integer.parseInt(new java.text.DecimalFormat("0").format(rfVec[i][j]));//四舍五入
//				System.out.print(rfVecInt[i][j] + " ");
				
				//----------------整个if语句是测试语句，要删除
//				if(i==9)
//				{
//					System.out.println("Yes!");
//					rfVecInt[i][j] = Integer.parseInt(new java.text.DecimalFormat("0").format(rfVec[i][j]));//四舍五入
//				}
				//----------------以上为测试语句
			}
			System.out.println("i:" + i);
			img[i] = getImgByPixels(width, height, rfVecInt[i]);
//			System.out.println();
//			img[i] = getImgByPixels(128, 128, rfVecInt[i]);
		}
//		System.out.println("img length:" + img.length);
		
		return img;
	}
	
	public static void main(String[] args)
	{
		//创建图片数组
		ImageIcon[] icon = new ImageIcon[10];
		String source = "Pictures/monkey-test/after";
		for(int i=0; i<6; i++)
		{
			String target = "-" + (i+1) + ".jpg";
			icon[i] = new ImageIcon(source + target);
		}
		icon[6] = new ImageIcon(source + "1.jpg");
		for(int i=7; i<10; i++)
		{
			String target = (i-4) + ".jpg";
			icon[i] = new ImageIcon(source + target);
		}
				
		//计算图片数组的特征值和特征向量
		//将结果保存在Features类中
		imageToResult(icon);
		
		//测试---------------------------
		//将特征向量转换成图片
		getFeatureFaces();
		
		//从单例中获取数据
		double[] eigenValue = Features.getInstance().getEigenValue();
		double[][] featureVector = Features.getInstance().getFeatureVector();
		
		System.out.println("特征值的个数：" + eigenValue.length);
		
		System.out.println("特征向量的维数：" +
				"[" + Features.getInstance().getResultFeatureVector().length + "]" +
				"[" + Features.getInstance().getResultFeatureVector()[0].length + "]");
//		System.out.println("特征向量的维数：" + 
//				"[" + featureVector.length +"]" + "[" + featureVector[0].length+ "]");
		
		//输出测试
		System.out.println("特征值：");
		for(int i=0; i<eigenValue.length; i++) {
			System.out.print(eigenValue[i] + " ");
		}
		System.out.println();
		System.out.println("---------分割线------------");
		
//		System.out.println("特征向量：");
//		double[][] result = Features.getInstance().getResultFeatureVector();
//		for(int i=0; i<result.length; i++)
//		{
//			for(int j=0; j<result[i].length; j++)
//			{
//				System.out.print(result[i][j] + " ");
//			}
//			System.out.println();
//		}
		
//		System.out.println("特征向量：");
//		for(int i=0; i<featureVector.length; i++)
//		{
//			for(int j=0; j<featureVector[i].length; j++)
//			{
//				System.out.print(featureVector[i][j] + " ");
//			}
//			System.out.println();
//		}
		
//		//可以将这里封装成一个方法
//		//形参为图片数组，返回值为double[][]
//		ImageIcon icon = new ImageIcon("Pictures/monkey-test/after-1.jpg");
//		icon = ImageHandle(icon, width, height);
//		Image img = icon.getImage();
//		BufferedImage bimg = ImageUtil.ImageToBufferedImage(img);
//		int[] imgVec = getPixes(bimg);
//		double[] imgVecD = intToDouble(imgVec);
//		
////		System.out.println("length:" + imgVec.length);
//		
//		//先用一张图片测试，这里实际应该为N张图片
//		double[][] imgVecD2 = new double[imgVecD.length][1];
//		for(int i=0; i<imgVecD.length; i++)
//		{
//			imgVecD2[i][0] = imgVecD[i];//构造列向量
//		}
////		imgVecD2[0] = imgVecD;
//		
//		//计算平均人脸图像
//		double[] aveVec = calAveVector(imgVecD2);
//		
//		//计算每张图片的均差
//		double[][] aveDev = calAveDeviation(imgVecD2, aveVec);
//		
//		//计算协方差矩阵
//		double[][] covMat = calCovarianceMatrix(aveDev);		
//		
//		//通过SVD定理，计算矩阵的特征值  
//		double[] eigenValue = calEigenValue(covMat);
//		
//		//通过SVD定理，计算矩阵的特征向量
//		double[][] featureVector = calFeatureVector(covMat);
//		
//		//输出测试
//		System.out.println("特征值：");
//		for(int i=0; i<eigenValue.length; i++) {
//			System.out.print(eigenValue[i] + " ");
//		}
//		System.out.println();
//		System.out.println("---------分割线------------");
//		
//		System.out.println("特征向量：");
//		for(int i=0; i<featureVector.length; i++)
//		{
//			for(int j=0; j<featureVector[i].length; j++)
//			{
//				System.out.print(featureVector[i][j] + " ");
//			}
//			System.out.println();
//		}
//		
		//---------------------真.分割线---------------------------------
		
//		 double [][] array = { {4,6,0,3},{-3,-5,0,3},{-3,-6,1,3}};
//		 System.out.println(array.length);
//		 System.out.println(array[0].length);
//		 
//		 
//		 Matrix a = new Matrix(array);
//		 double[][] ar = {{3,-2,1},{0,0,0}};
//		 double[][] ar2 = {{1,0,-1},{0,0,0}};
//		 
//		 Matrix a = new Matrix(ar);
//		 Matrix a2 = new Matrix(ar2);
//		 a2 = a2.transpose();
//		
//		 Matrix a3 = a.times(a2);
//		 a3.print(4, 1);
		 
//		 double[][] ar = a.getArray();
//		 double[][] ar = a.transpose().getArray();
//		 System.out.println(ar[0][0] + " " +ar[0][1]+" " +ar[0][2]);
//		 System.out.println(ar[1][0] + " " +ar[1][1]+" " +ar[1][2]);
//		 System.out.println(ar[2][0] + " " +ar[2][1]+" " +ar[2][2]);
//		 System.out.println(ar[3][0] + " " +ar[3][1]+" " +ar[3][2]);
		 
//		 System.out.println(ar[0][0] + " " +ar[0][1]+" " +ar[0][2]+" " +ar[0][3]);
//		 System.out.println(ar[1][0] + " " +ar[1][1]+" " +ar[1][2]+" " +ar[1][3]);
//		 System.out.println(ar[2][0] + " " +ar[2][1]+" " +ar[2][2]+" " +ar[2][3]);
//		 double[] ar = array[0];
//		 System.out.println(ar[0] + " " +ar[1]+" " +ar[2]+" " +ar[3]);
//		 double[] arr = {4,2,0,8};
//		 double[][] ar = new double[3][4];
//		 ar[0] = arr;
//		 System.out.println(ar[0][0] + " " +ar[0][1]+" " +ar[0][2]+" " +ar[0][3]);
		
		// create M-by-N matrix that doesn't have full rank
//	      int M = 8, N = 5;
//	      Matrix B = Matrix.random(5, 3);
//	      Matrix A = Matrix.random(M, N).times(B).times(B.transpose());
//	      System.out.print("A = ");
//	      A.print(9, 6);
//
//	      // compute the singular vallue decomposition
//	      System.out.println("A = U S V^T");
//	      System.out.println();
//	      SingularValueDecomposition s = A.svd();
//	      System.out.print("U = ");
//	      Matrix U = s.getU();
//	      U.print(9, 6);
//	      System.out.print("Sigma = ");
//	      Matrix S = s.getS();
//	      S.print(9, 6);
//	      System.out.print("V = ");
//	      Matrix V = s.getV(); //特征向量
//	      V.print(9, 6);
//	      System.out.println("rank = " + s.rank());
//	      System.out.println("condition number = " + s.cond());
//	      System.out.println("2-norm = " + s.norm2());
//
//	      // print out singular values
//	      System.out.print("singular values = ");
//	      Matrix svalues = new Matrix(s.getSingularValues(), 1); //特征值
//	      svalues.print(9, 6);
	}
	

}
