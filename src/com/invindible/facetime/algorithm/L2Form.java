package com.invindible.facetime.algorithm;

public class L2Form {
	 static boolean inL2(double[] testMean,double[] projectMean,double value){
		double tmp=0;
		for(int i=0;i<projectMean.length;i++)
		{
			tmp+=Math.pow(testMean[i]-projectMean[i], 2);
		}
		System.out.println("l2:tmp value"+tmp+" "+value);
		if (tmp<value)
			return true;
		else
			return false;
	}
	  static double[] L2Form(double[][] facebase,double[] face){
			int n=facebase.length;   //人脸库数量
			int m=facebase[0].length; //每张人脸的维数
			double tmp;
			double[] dis=new double[facebase.length]; //记录待识别图像与人脸库之间的距离
			for(int i=0;i<n;i++){
				tmp=0;
				for(int j=0;j<m;j++){
					tmp+=Math.pow(face[j]-facebase[i][j], 2);
				}			
				dis[i]=tmp;
			}
			return dis;
		}
	  
	   static double value(double[][] modelMean,double[] Mean){
			int n=modelMean.length;
			double tmp;
			double max=0;
			for(int i=0;i<n;i++){
				tmp=0;
				tmp=calculate(modelMean[i],Mean);
				if(i==0)
					max=tmp;
				else
					max=tmp>max?tmp:max;
			}
			return max;
		}
	   
		 static double calculate(double[] modelMean,double[] Mean){
			int a=modelMean.length;
			double tmp=0;
			for(int i=0;i<a;i++){			
					tmp+=Math.pow(modelMean[i]-Mean[i], 2);
			}
			//tmp=Math.pow(tmp, 0.5);
			return tmp;
		}
}
