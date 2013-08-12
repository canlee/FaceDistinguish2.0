//package com.invindible.facetime.algorithm.feature;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//
//import javax.swing.ImageIcon;
//
//import com.invindible.facetime.algorithm.LDA;
//import com.invindible.facetime.algorithm.MixedMahalnobisDistance;
//import com.invindible.facetime.algorithm.feature.Features;
//import com.invindible.facetime.model.LdaFeatures;
//
//
//public class testing4forLDA {
//
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		
//		int photonum=5; //每人呢照片数量
//		int peoplenum=5; //人数
//		int testnum=2; //测试图片数
//		
//		
//		//设置每个人的照片数量
////		GetPcaLda.getInstance().setNum(5);
//		GetPcaLda.getInstance().setNum(photonum);
//		
////		ImageIcon[] icon = new ImageIcon[40];
//		ImageIcon[] icon = new ImageIcon[peoplenum*photonum];
//		
////		String source = "C:\\Users\\Administrator\\Desktop\\faceAfter\\";
//		String source = "C:\\Users\\Administrator\\Desktop\\1\\";
//		
//		int temp = 0;
//		for(int i=1; i<=peoplenum; i++)
//		{
//			String source2 = "after"+i+"-";
////			for(int j=0; j<5; j++)
//			for(int j=1; j<=photonum; j++)
//			{
//				String target = j+ ".jpg";
////				icon[i*5 + j] = new ImageIcon(source + source2 + target);
//				icon[temp++] = new ImageIcon(source + source2 + target);
////				System.out.println(source + source2 + target);
//			}
//		}
//		
//		GetPcaLda.getResult(icon);
//		
//		
////		//以下---------------------------最佳投影矩阵的输出----------------------------------
////		double[][] dou = LdaFeatures.getInstance().getLastProjection();
////		System.out.println("最佳投影矩阵的维数为：");
////		System.out.println("dou.length:" + dou.length + "dou[0].length:" + dou[0].length);
////		System.out.println("最佳投影矩阵如下：");
////		for(int i=0; i<dou.length; i++)
////		{
////			for(int j=0; j<dou[0].length; j++)
////			{
////				System.out.print(dou[i][j] + " ");
////			}
////			System.out.println();
////		}
////		//以上---------------------------最佳投影矩阵的输出----------------------------------
//		
//		//以下---------------------------Sw-1*Sb的输出----------------------------------
////		double[][] LDAFeatureVector = LdaFeatures.getInstance().getLDAFeatureVector();
////		System.out.println("保留前：");
////		System.out.println("LDAFeatureVector的维数:");
////		System.out.println("[" + LDAFeatureVector.length + "][" + LDAFeatureVector[0].length + "]");
////		for(int i=0; i<LDAFeatureVector.length; i++)
////		{
////			for(int j=0; j<LDAFeatureVector[0].length; j++)
////			{
////				System.out.print(LDAFeatureVector[i][j] + " ");
////			}
////			System.out.println();
////		}
//		
////		double[][] keepFV = LdaFeatures.getInstance().getKeepFV();
////		System.out.println("保留前：");
////		System.out.println("keepFV的维数:");
////		System.out.println("[" + keepFV.length + "][" + keepFV[0].length + "]");
////		for(int i=0; i<keepFV.length; i++)
////		{
////			for(int j=0; j<keepFV[0].length; j++)
////			{
////				System.out.print(keepFV[i][j] + " ");
////			}
////			System.out.println();
////		}	
//		
//
//		double[][] modelP=new double[peoplenum*photonum][peoplenum-1];
//		for(int i=0;i<peoplenum*photonum;i++){
//			modelP[i]=LDA.getInstance().calZ(icon[i]);//投影
//		}
////		for(int i=0;i<24;i++){
////			for(int j=0;j<7;j++){
////				System.out.println("modelP"+i+" "+j+" :"+modleP[i][j]);
////			}
////		}
//		
//		double[][] modelMean=new double[peoplenum][peoplenum-1];
//		
//		double[] allMean=new double[peoplenum-1];
//		for(int i=0;i<peoplenum;i++){
//			for(int k=0;k<peoplenum-1;k++){
//				for(int j=0;j<photonum;j++){
//					modelMean[i][k]+=modelP[photonum*i+j][k];
//				}
//				allMean[k]+=modelMean[i][k];
//				modelMean[i][k]/=photonum;
//			}			
//		}
//		
//		for(int i=0;i<peoplenum-1;i++)
//			allMean[i]/=peoplenum*photonum;
//		
//		double value=value(modelMean,allMean);
//		
//		double l1value=l1value(modelMean,allMean);
//		
//		for(int j=1;j<=20;j++){
//			
//			long startMili=System.currentTimeMillis();// 当前时间对应的毫秒数	
//			boolean flag=true;
//			int[] identify=new int[8];
//		ImageIcon[] icon1 = new ImageIcon[testnum];
//		for(int z=0;z<testnum;z++)
//			icon1[z]=new ImageIcon("C:\\Users\\Administrator\\Desktop\\1\\after"+j+"-"+(photonum+1+z)+".jpg");
//		double[][] testP=new double[testnum][peoplenum-1]; //测试的投影
//		for(int i=0;i<testnum;i++)
//			testP[i]=LDA.getInstance().calZ(icon1[i]);
//		double[] testMean=new double[peoplenum-1];
//		
//		for(int i=0;i<peoplenum-1;i++){
//				for(int z=0;z<testnum;z++){
//					testMean[i]+=testP[z][i];
//				}
//				testMean[i]/=testnum;
//			}	
//		
//		for(int b=0;b<=testnum;b++){
//		
//		double[] madis=MixedMahalnobisDistance.calMixedMahalnobisDistance(testMean,modelMean);
//		
//		double[] mindis=MixedMahalnobisDistance.calMinDistance(testMean, modelMean);
////		for(int i=0;i<mindis.length;i++)
////		{
////			System.out.println(mindis[i]);
////		}
//		
////		double[][] projectMean=LdaFeatures.getInstance().getMi();
////		
////		double[] project=MixedMahalnobisDistance.calProjectionTotalMInWoptT();
//		
//		int matmp=mark(Double.MAX_VALUE,madis);
//		
//		int mintmp=mark(Double.MAX_VALUE,mindis);
//		
//		//double[][] modelMean=LdaFeatures.getInstance().getμi();
//		
//		
//	
//		System.out.println(j+"-----------------------------------------------------------");
//		
//		boolean l2,l1;
//		double[] facedis;
//		if(b==testnum)
//		{
//			l2=inL2(testMean, allMean, value);
//			facedis=L2Form(modelP, testMean);
//		}
//		else
//		{
//			l2=inL2(testP[b], allMean, value);
//			facedis=L2Form(modelP, testP[b]);
//		}
//		
//		
////		for(int k=0;k<testnum;k++){
////			System.out.println(k+"!!!!!!!!!!!!!!!!!!!");
//					//l2 form
//		//	double[] facedis=L2Form(modelP, testP[k]);
//		int tmp=mark(value, facedis);
//		
//		
//		double[] l1dis;
//		if(b==testnum)
//		{
//			l1=inL1(testMean, allMean, l1value);
//			l1dis=L1Form(modelP, testMean); 
//		}
//		else
//		{
//			l1=inL1(testP[b], allMean, l1value);
//			l1dis=L1Form(modelP, testP[b]); 
//		}
//		
//		System.out.println("l1 :"+l1+" l2:"+l2);
//		
//		if(l1==false||l2==false)
//			flag=false;
//		
//		
//		      //l1 form
////		double[] l1dis=L1Form(modelP, testP[k]);
//		int l1tmp=mark(l1value, l1dis);
////		int recordtmp=-1;
//		
//		
//		int tmpsecond;
//		identify[0]=(tmp/photonum+1);
//		if(tmp!=-1){
//			tmpsecond=second(tmp,facedis);
//			identify[1]=tmpsecond/photonum+1;
//			//System.out.println(Math.abs(facedis[second]/facedis[tmp]));
//			if(Math.abs(facedis[tmpsecond]/facedis[tmp])<5)
//			{System.out.println("l2 第二小比第一小比值小于5:"+(tmp/photonum+1));}
//			else
//				flag=false;
//			
//			//int third=third(tmp,second,facedis);					
//			System.out.println("l2 1~2:"+(tmp/photonum+1)+" "+(tmpsecond/photonum+1));
//			//System.out.println("l2 与均值的距离="+tmpdis+" facedis="+facedis[tmp]+" ratio="+tmpdis/facedis[tmp]);
//		}
//		
//		int tmpl1second;
//		identify[2]=l1tmp/photonum+1;
//		if(l1tmp!=-1){	
//		tmpl1second=second(l1tmp, l1dis);
//		identify[3]=tmpl1second/photonum+1;
//		//System.out.println(Math.abs(l1dis[l1second]/l1dis[l1tmp]));
//			if(Math.abs(l1dis[tmpl1second]/l1dis[l1tmp])<5)
//			System.out.println("l1 第二小比第一小比值小于5:"+(l1tmp/photonum+1));			
//			else
//				flag=false;
//
//		//	int third=third(l1tmp,l1second,facedis);					
//			System.out.println("l1 1~2:"+(l1tmp/photonum+1)+" "+(tmpl1second/photonum+1));
//				//System.out.println("l1 与均值距离="+l1tmpdis+" l1dis="+l1dis[tmp]+" ratio="+l1tmpdis/l1dis[tmp]);
//		}
//		
//		
//		System.out.println("ma:"+(matmp+1));
//		identify[4]=matmp+1;
//		
//		System.out.println("min:"+(mintmp+1));
//		identify[5]=mintmp+1;
////		recordtmp=(l1tmp/photonum+1)==(tmp/photonum+1)?(l1tmp/photonum+1):-1;
//		
//		double[][] pmodelMean=new double[peoplenum][peoplenum-1]; //测试的投影
//		for(int a=0;a<peoplenum;a++){
//			//pmodelMean[a]=LDA.getInstance().calZ(icon)
//		}
//		
//		//value=value(modelMean);
//		double[] l2facedis;
//		if(b==testnum){
//			l2facedis=L2Form(modelMean, testMean);		//l2 form
//		}
//		else
//			l2facedis=L2Form(modelMean, testP[b]);
//		
//		//double[] l2facedis=L2Form(modelMean, testP[k]);
//		 tmp=mark(value, l2facedis);
//		 
//		 //value=l1value(modelMean);
//		double[] l1facedis;      //l1 form
//		if(b==testnum)
//			l1facedis=L1Form(modelMean, testMean); 
////		 double[] l1facedis=L1Form(modelMean, testP[k]);
//		else
//			l1facedis=L1Form(modelMean, testP[b]); 
//		l1tmp=mark(l1value, l1facedis);
//		int secondrecord=-1;
//		
//			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~"); 
//			identify[6]=tmp+1;
//			 if(tmp!=-1){
//					int second=second(tmp,l2facedis);
//					if(l2facedis[tmp]<0.55*l2facedis[second])
//					{System.out.println("l2小于0.55比值:"+(tmp+1));}
//					else
//						flag=false;
//					//int third=third(tmp,second,l2facedis);					
//					System.out.println("l2 1~2:"+(tmp+1)+" "+(second+1));
//					//System.out.println("l2 与均值的距离="+tmpdis+" facedis="+facedis[tmp]+" ratio="+tmpdis/facedis[tmp]);
//					
//				}
//			 identify[7]=l1tmp+1;
//			 if(l1tmp!=-1){	
//					int l1second=second(l1tmp, l1facedis);
//						if(l1facedis[l1tmp]<0.65*l1facedis[l1second])
//						System.out.println("l1于0.65比值:"+(l1tmp+1));			
//						else
//							flag=false;
//
//						//int third=third(l1tmp,l1second,l1facedis);					
//						System.out.println("l1 1~2:"+(l1tmp+1)+" "+(l1second+1));
//							//System.out.println("l1 与均值距离="+l1tmpdis+" l1dis="+l1dis[tmp]+" ratio="+l1tmpdis/l1dis[tmp]);
//					}
//			for(int c=0;c<identify.length;c++){
//				for(int d=c+1;d<identify.length;d++){
//					if(identify[c]!=identify[d])
//					{
//						flag=false;
//						break;
//					}
//				}
//				if(!flag)
//					break;
//			}
////			for(int c=0;c<identify.length;c++){
////				System.out.println(identify[c]);
////			}
//			if(flag)
//				System.out.println("判别结果为"+identify[0]);
//			else
//				System.out.println("判别失败");
//			flag=true;
//			 System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~"); 	
//			 long endMili=System.currentTimeMillis();
//				System.out.println("总耗时为："+(endMili-startMili)+"毫秒");
//		System.out.println("-----------------------------------------------------------");
//		}
//		}
//		
////		}
//				
//	}
//	
//	
//	private static boolean inL1(double[] testMean,double[] projectMean,double value){
//		double tmp=0;
//		for(int i=0;i<projectMean.length;i++)
//		{
//			tmp+=Math.abs(testMean[i]-projectMean[i]);
//		}
////		System.out.println("tmp value"+tmp+" "+value);
//		if (tmp<value)
//			return true;
//		else
//			return false;
//	}
//	
//	
//	private static boolean inL2(double[] testMean,double[] projectMean,double value){
//		double tmp=0;
//		for(int i=0;i<projectMean.length;i++)
//		{
//			tmp+=Math.pow(testMean[i]-projectMean[i], 2);
//		}
////		System.out.println("tmp value"+tmp+" "+value);
//		if (tmp<value)
//			return true;
//		else
//			return false;
//	}
//	
//	private static double calculate(double[] modelMean,double[] Mean){
//		int a=modelMean.length;
//		double tmp=0;
//		for(int i=0;i<a;i++){			
//				tmp+=Math.pow(modelMean[i]-Mean[i], 2);
//		}
//		//tmp=Math.pow(tmp, 0.5);
//		return tmp;
//	}
//	
//	
//	private static double value(double[][] modelMean,double[] Mean){
//		int n=modelMean.length;
//		double tmp;
//		double max=0;
//		for(int i=0;i<n;i++){
//			tmp=0;
//			tmp=calculate(modelMean[i],Mean);
//			if(i==0)
//				max=tmp;
//			else
//				max=tmp>max?tmp:max;
//		}
//		return max;
//	}
//	
//	
//	
//	private static int second(int min,double[] dis){
//		double tmp=Double.MAX_VALUE;
//		//System.out.println(tmp);
//		int second=0;
//		for(int i=0;i<dis.length;i++){
//			//System.out.println(dis[i]);
//			if(tmp>dis[i]&&i!=min)
//				{
//					tmp=dis[i];
//					second=i;
//				}
//		}
//		return second;
//	}
//	
//	
//	
//	private static double[] L1Form(double[][] facebase,double[] face){
//		int n=facebase.length;   //人脸库数量
//		int m=facebase[0].length; //每张人脸的维数
//		double tmp;
//		double[] dis=new double[n];
//		for(int i=0;i<n;i++){
//			tmp=0;
//			for(int j=0;j<m;j++){
//				tmp+=Math.abs(facebase[i][j]-face[j]);
//			}
//			dis[i]=tmp;
//		}
//		return dis;
//	}
//	
//	/**
//	 * L2范式
//	 * @param facebase  人脸库
//	 * @param face		待识别图像
//	 * @return
//	 */
//	private static double[] L2Form(double[][] facebase,double[] face){
//		int n=facebase.length;   //人脸库数量
//		int m=facebase[0].length; //每张人脸的维数
//		double tmp;
//		double[] dis=new double[facebase.length]; //记录待识别图像与人脸库之间的距离
//		for(int i=0;i<n;i++){
//			tmp=0;
//			for(int j=0;j<m;j++){
//				tmp+=Math.pow(face[j]-facebase[i][j], 2);
//			}			
//			dis[i]=tmp;
//		}
//		return dis;
//	}
//	
//	
//	
//	/**
//	 * 距离阈值
//	 * @param yi 人脸库
//	 * @param yk 
//	 * @return
//	 */
//	
//	private static double calculate(double[][] yi,double[] yk){
//		int a=yi.length;
//		int k=yk.length;
//		double max=0,tmp;
//		for(int i=0;i<a;i++){
//			tmp=0;
//			for(int j=0;j<k;j++){
//				tmp+=Math.pow(yi[i][j]-yk[j], 2);
//			}
//			if(i==0)
//				max=tmp;
//			else
//				max=max>tmp?max:tmp;
//		}
//		max=max/2;
//		return max;
//	}
//	
//	/**
//	 * l2距离阈值
//	 * @param facebase 数据库投影向量
//	 * @return
//	 */
//	private static double value(double[][] facebase){
//		double c=0;
//		double tmp;
//		for(int i=0;i<facebase.length;i++){
//				tmp=0;
//				tmp=calculate(facebase,facebase[i]);
//				if(i==0)
//					c=tmp;
//				else
//					c=c>tmp?c:tmp;						
//		}
//		return c;
//	}
//	
//	
//	
//	private static double calculatel1(double[] modelMean,double[] Mean){
//		int a=modelMean.length;
//		double tmp=0;
//		for(int i=0;i<a;i++){			
//				tmp+=Math.abs(modelMean[i]-Mean[i]);
//		}
//		//tmp=Math.pow(tmp, 0.5);
//		return tmp;
//	}
//	
//	private static double l1value(double[][] modelMean,double[] Mean){
//		double c=0;
//		double tmp;
//		for(int i=0;i<modelMean.length;i++){
//				tmp=0;
//				tmp=calculatel1(modelMean[i],Mean);
//				if(i==0)
//					c=tmp;
//				else
//					c=c>tmp?c:tmp;						
//		}
//		return c;
//	}
//	
//	
//	/**
//	 * 识别阶段
//	 * @param value 距离阈值
//	 * @return
//	 */
//	private static int mark(double value,double[] distance){
//		int i=distance.length;
//		int mark=0;
//		double tmp=0;
//		for(int j=0;j<i;j++){
//			if(j==0)
//				tmp=distance[j];
//			else
//				{
//				
//				if(tmp>distance[j]){
//					tmp=distance[j];
//					mark=j;
//					}	
//				
//				}
//		}
//		if(tmp<value)
//			return mark;
//		else
//			return -1;
//	}
//	
//	
//		
//	
//
//}
