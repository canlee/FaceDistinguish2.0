package com.invindible.facetime.model;

import java.util.ArrayList;

import com.invindible.facetime.algorithm.feature.Features;

public class LdaFeatures {
	private static LdaFeatures ldaFeatures= null;
	
	private int peopleNum;//人数，即类的数量
	private int num;//每个人拥有的图片数（例如：一人3张图片，则num = 3）
	
	private double[][] eachAveVector;//类内平均图像 [像素][C个人]
	private double[] aveVector;//总体平均图像[像素]
	private ArrayList<double[][]> aveDeviation;//每个图像的差值图像（每个类内图像减去本类平均图像） [像素][n]
	private double[][] aveDeviationDouble;//每个图像的差值图像,double型 [像素][n]
	private double[][] aveDeviationEach;//每类的差值图像 [像素][n/num]
	
	private ArrayList<double[][]> projectionAveDeviation;//投影：中心化后的图像投影到Wpca [n][n]
	private ArrayList<double[][]> projectionAveDeviationEach;//投影：中心化后的类内平均图像投影到Wpca [像素][c个人]
	private double[][] projectionAveVector;//投影：总体平均图像投影到Wpca
	
	private ArrayList<double[][]> si;//类内散布矩阵Si(Scatter Matrix)
	private double[][] sw;//总的类内散布矩阵Sw
	private double[][] sb;//类间散布矩阵Sb
	
	private double[][] swProjection;//矩阵Sw'
	private double[][] sbProjection;//矩阵Sb'
	
	private double[][] swInverse;//矩阵Sw的逆矩阵
	private double[][] swITimesSb;//矩阵Sw-1*Sb
	private double[] LDAEigenValue;//矩阵Sw-1*Sb的特征值
	private double[][] LDAFeatureVector;//矩阵Sw-1*Sb的特征向量
	private double[][] keepFV;//保留前C-1个特征向量
	private double[][] lastProjection;//Wpot,最终投影矩阵： 投影矩阵Wpca*投影矩阵Wlda;
	private double[][] lastProjectionT;//WpotT，最终投影矩阵的转置
	
	private double[] z;//一幅图像的人脸特征向量
	private double[] woptEigenValue;//Wopt矩阵的特征值
	
	private ArrayList<double[][]> arrMi;//ArrayList形式的，中心化后的类内平均图像在(WoptT)上的投影，共C个[C-1][1],求混合马氏距离用到的数据
	private double[][] mi;//arrMi的double[C][C-1]版,行向量，C为人数
	private ArrayList<double[][]> arrμi;//μi 的队列，求混合马氏距离用到的数据,共人数C个[C-1][1]
	private double[][] μi;//μi 的数组形式，求混合马氏距离用到的数据,[C][C-1],C为人数
	private ArrayList<double[][]> arrXi;//xi 的队列，求混合马氏距离用到的数据,共图片总数n个[C-1][1]
	private ArrayList<double[][]> arrΣi;//Σi的队列，求混合马氏距离用到的数据,共有C个Σi[C-1][C-1],C为人数
	private double[][] Σ;//Σ的数组，求混合马氏距离用到的数据,[C-1][C-1]
	private ArrayList<double[][]> arrΣiA;//ΣiA的队列，求混合马氏距离用到的数据,共有C个Σi(α)[C-1][C-1],C为人数
	private double[] mixedMahalnobisDistance;//混合马氏距离
	private double[] projectionTotalMInWoptT;//总体均值图像的在(Wopt T)空间上的投影(原结果为列向量[C-1][1],现转成行向量[1][C-1])
	
	private LdaFeatures() {}
	
	//懒汉式单例
	synchronized public static LdaFeatures getInstance(){
		if(ldaFeatures == null)
		{
			ldaFeatures = new LdaFeatures();
		}
		return ldaFeatures;
	}

	public double[][] getEachAveVector() {
		return eachAveVector;
	}

	public void setEachAveVector(double[][] eachAveVector) {
		this.eachAveVector = eachAveVector;
	}

	public double[] getAveVector() {
		return aveVector;
	}

	public void setAveVector(double[] aveVector) {
		this.aveVector = aveVector;
	}

	
	public int getPeopleNum() {
		return peopleNum;
	}

	public void setPeopleNum(int peopleNum) {
		this.peopleNum = peopleNum;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public ArrayList<double[][]> getAveDeviation() {
		return aveDeviation;
	}

	public void setAveDeviation(ArrayList<double[][]> aveDeviation) {
		this.aveDeviation = aveDeviation;
	}

	public double[][] getAveDeviationEach() {
		return aveDeviationEach;
	}

	public void setAveDeviationEach(double[][] aveDeviationEach) {
		this.aveDeviationEach = aveDeviationEach;
	}

	public ArrayList<double[][]> getProjectionAveDeviation() {
		return projectionAveDeviation;
	}

	public void setProjectionAveDeviation(
			ArrayList<double[][]> projectionAveDeviation) {
		this.projectionAveDeviation = projectionAveDeviation;
	}

	public ArrayList<double[][]> getProjectionAveDeviationEach() {
		return projectionAveDeviationEach;
	}

	public void setProjectionAveDeviationEach(
			ArrayList<double[][]> projectionAveDeviationEach) {
		this.projectionAveDeviationEach = projectionAveDeviationEach;
	}

	public double[][] getProjectionAveVector() {
		return projectionAveVector;
	}

	public void setProjectionAveVector(double[][] projectionAveVector) {
		this.projectionAveVector = projectionAveVector;
	}

	public ArrayList<double[][]> getSi() {
		return si;
	}

	public void setSi(ArrayList<double[][]> si) {
		this.si = si;
	}

	public double[][] getSw() {
		return sw;
	}

	public void setSw(double[][] sw) {
		this.sw = sw;
	}

	public double[][] getSb() {
		return sb;
	}

	public void setSb(double[][] sb) {
		this.sb = sb;
	}

	
	public double[][] getSwProjection() {
		return swProjection;
	}

	public void setSwProjection(double[][] swProjection) {
		this.swProjection = swProjection;
	}

	public double[][] getSbProjection() {
		return sbProjection;
	}

	public void setSbProjection(double[][] sbProjection) {
		this.sbProjection = sbProjection;
	}

	public double[][] getSwInverse() {
		return swInverse;
	}

	public void setSwInverse(double[][] swInverse) {
		this.swInverse = swInverse;
	}

	public double[][] getSwITimesSb() {
		return swITimesSb;
	}

	public void setSwITimesSb(double[][] swITimesSb) {
		this.swITimesSb = swITimesSb;
	}

	public double[] getLDAEigenValue() {
		return LDAEigenValue;
	}

	public void setLDAEigenValue(double[] lDAEigenValue) {
		LDAEigenValue = lDAEigenValue;
	}

	public double[][] getLDAFeatureVector() {
		return LDAFeatureVector;
	}

	public void setLDAFeatureVector(double[][] lDAFeatureVector) {
		LDAFeatureVector = lDAFeatureVector;
	}

	public double[][] getKeepFV() {
		return keepFV;
	}

	public void setKeepFV(double[][] keepFV) {
		this.keepFV = keepFV;
	}

	public double[][] getLastProjection() {
		return lastProjection;
	}

	public void setLastProjection(double[][] lastProjection) {
		this.lastProjection = lastProjection;
	}

	public double[] getZ() {
		return z;
	}

	public void setZ(double[] z) {
		this.z = z;
	}

	public double[][] getLastProjectionT() {
		return lastProjectionT;
	}

	public void setLastProjectionT(double[][] lastProjectionT) {
		this.lastProjectionT = lastProjectionT;
	}

	public double[] getWoptEigenValue() {
		return woptEigenValue;
	}

	public void setWoptEigenValue(double[] woptEigenValue) {
		this.woptEigenValue = woptEigenValue;
	}

	public ArrayList<double[][]> getArrMi() {
		return arrMi;
	}

	public void setArrMi(ArrayList<double[][]> arrMi) {
		this.arrMi = arrMi;
	}

	public ArrayList<double[][]> getArrμi() {
		return arrμi;
	}

	public void setArrμi(ArrayList<double[][]> arrμi) {
		this.arrμi = arrμi;
	}

	public ArrayList<double[][]> getArrXi() {
		return arrXi;
	}

	public void setArrXi(ArrayList<double[][]> arrXi) {
		this.arrXi = arrXi;
	}

	public ArrayList<double[][]> getArrΣi() {
		return arrΣi;
	}

	public void setArrΣi(ArrayList<double[][]> arrΣi) {
		this.arrΣi = arrΣi;
	}

	public double[][] getΣ() {
		return Σ;
	}

	public void setΣ(double[][] σ) {
		Σ = σ;
	}

	public ArrayList<double[][]> getArrΣiA() {
		return arrΣiA;
	}

	public void setArrΣiA(ArrayList<double[][]> arrΣiA) {
		this.arrΣiA = arrΣiA;
	}

	public double[] getMixedMahalnobisDistance() {
		return mixedMahalnobisDistance;
	}

	public void setMixedMahalnobisDistance(double[] mixedMahalnobisDistance) {
		this.mixedMahalnobisDistance = mixedMahalnobisDistance;
	}

	public double[][] getμi() {
		return μi;
	}

	public void setμi(double[][] μi) {
		this.μi = μi;
	}

	public double[] getProjectionTotalMInWoptT() {
		return projectionTotalMInWoptT;
	}

	public void setProjectionTotalMInWoptT(double[] projectionTotalMInWoptT) {
		this.projectionTotalMInWoptT = projectionTotalMInWoptT;
	}

	public double[][] getMi() {
		return mi;
	}

	public void setMi(double[][] mi) {
		this.mi = mi;
	}

	public double[][] getAveDeviationDouble() {
		return aveDeviationDouble;
	}

	public void setAveDeviationDouble(double[][] aveDeviationDouble) {
		this.aveDeviationDouble = aveDeviationDouble;
	}
	
	
	
	
}
