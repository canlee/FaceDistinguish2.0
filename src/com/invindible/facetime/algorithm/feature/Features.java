package com.invindible.facetime.algorithm.feature;

import Jama.Matrix;

public class Features {
	private static Features features = null;
	
	private double[] eigenValue;//特征值
	private double[][] featureVector;//特征向量(公式转换，计算过程的，非真正结果，即:X'T * X。其中X是一个矩阵，X'T是X的转置矩阵)
	private double[][] aveDev;//每幅图的均差
	private double[][] resultFeatureVector;//特征向量（真正的结果，即:X * X'T。其中X是一个矩阵，X'T是X的转置矩阵）
	private double[] aveVector;//平均人脸图像(Xave)
	private double[][] covarianceMatrix;//协方差矩阵
	
	private Features() {}
	
	//懒汉式单例
	synchronized public static Features getInstance(){
		if(features == null)
		{
			features = new Features();
		}
		return features;
	}

	public double[] getEigenValue() {
		return eigenValue;
	}

	public void setEigenValue(double[] eigenValue) {
		this.eigenValue = eigenValue;
	}

	public double[][] getFeatureVector() {
		return featureVector;
	}

	public void setFeatureVector(double[][] featureVector) {
		this.featureVector = featureVector;
	}

	public double[][] getAveDev() {
		return aveDev;
	}

	public void setAveDev(double[][] aveDev) {
		this.aveDev = aveDev;
	}

	public double[][] getResultFeatureVector() {
		return resultFeatureVector;
	}

	public void setResultFeatureVector(double[][] resultFeatureVector) {
		this.resultFeatureVector = resultFeatureVector;
	}
	
	
	
	public double[] getAveVector() {
		return aveVector;
	}

	public void setAveVector(double[] aveVector) {
		this.aveVector = aveVector;
	}

	
	public double[][] getCovarianceMatrix() {
		return covarianceMatrix;
	}

	public void setCovarianceMatrix(double[][] covarianceMatrix) {
		this.covarianceMatrix = covarianceMatrix;
	}

	/**
	 * 矩阵相乘的方法
	 */
	public static double[][] matrixMultiply(double[][] matrix1, double[][] matrix2)
	{
		double[][] matrixResult = new double[1][1];
		Matrix m1 = new Matrix(matrix1);
		Matrix m2 = new Matrix(matrix2);
		Matrix m = m1.times(m2);
		matrixResult = m.getArray();
		return matrixResult;
	}
	
	/**
	 * 矩阵转置的方法
	 */
	public static double[][] matrixTrans(double[][] matrix)
	{
		double[][] matrixTrans = new double[1][1];
		Matrix m1 = new Matrix(matrix);
		Matrix m2 = m1.transpose();
		matrixTrans = m2.getArray();
		return matrixTrans;
	}
	

}
