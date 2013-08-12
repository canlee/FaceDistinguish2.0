package com.invindible.facetime.algorithm;

public class L1Form {
	  static double l1value(double[][] modelMean,double[] Mean){
		double c=0;
		double tmp;
		for(int i=0;i<modelMean.length;i++){
				tmp=0;
				tmp=calculatel1(modelMean[i],Mean);
				if(i==0)
					c=tmp;
				else
					c=c>tmp?c:tmp;						
		}
		return c;
	}
	
	 static double calculatel1(double[] modelMean,double[] Mean){
		int a=modelMean.length;
		double tmp=0;
		for(int i=0;i<a;i++){			
				tmp+=Math.abs(modelMean[i]-Mean[i]);
		}
		//tmp=Math.pow(tmp, 0.5);
		return tmp;
	}
	 
	  static boolean inL1(double[] testMean,double[] projectMean,double value){
			double tmp=0;
			for(int i=0;i<projectMean.length;i++)
			{
				tmp+=Math.abs(testMean[i]-projectMean[i]);
			}
			System.out.println("l1:tmp value"+tmp+" "+value);
			if (tmp<value)
				return true;
			else
				return false;
		}
	 

		 static double[] L1Form(double[][] facebase,double[] face){
			int n=facebase.length;   //人脸库数量
			int m=facebase[0].length; //每张人脸的维数
			double tmp;
			double[] dis=new double[n];
			for(int i=0;i<n;i++){
				tmp=0;
				for(int j=0;j<m;j++){
					tmp+=Math.abs(facebase[i][j]-face[j]);
				}
				dis[i]=tmp;
			}
			return dis;
		}
}
