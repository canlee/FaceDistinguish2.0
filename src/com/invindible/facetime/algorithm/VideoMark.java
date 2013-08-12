package com.invindible.facetime.algorithm;

import com.invindible.facetime.model.VideoMarkModel;

public class VideoMark {
	public static VideoMarkModel mark(double[][] test,double[][] model,double[] allMean){
		double value=L2Form.value(model,allMean);		//l2 domain 
		double l1value=L1Form.l1value(model,allMean);   //l1 domain			
		double[] madis=MixedMahalnobisDistance.calMixedMahalnobisDistance(test[0],model);	 //ma distance	
		double[] mindis=MixedMahalnobisDistance.calMinDistance(test[0], model);  //min distance
		int matmp=Identify.mark(Double.MAX_VALUE,madis);	//ma domain
		int mintmp=Identify.mark(Double.MAX_VALUE,mindis);  //min domain
		
		double[] facedis;  //l2 distance
		double[] l1dis;   //l1 distance
		
		 
		int[] record=new int[1];
			boolean l2,l1;
			int[] identify=new int [4];
			int error1=0;
			l2=L2Form.inL2(test[0], allMean, value);
			l1=L1Form.inL1(test[0], allMean, l1value);	//whether in or not
			if(!l1||!l2)
			{
				//record[0]=-1;
				System.out.println("不在范围内");
				return null;
			}
			facedis=L2Form.L2Form(model, test[0]);  //calculate l2 distance
			l1dis=L1Form.L1Form(model, test[0]);   //calculate l1 distance
			
			int tmp=Identify.mark(value, facedis);  //l2 find out the nearest
			int l1tmp=Identify.mark(l1value, l1dis);  //l1 find out the nearest
			
			double[] tmpdis=new double[4];
			
			int tmpsecond;
			
			//---------------------------与总体比较
			if(tmp!=-1){
				tmpsecond=second(tmp,facedis);  //l2 find out the second nearest
				identify[0]=tmp+1;
				tmpdis[0]=facedis[tmp];
				if(tmpsecond!=-1){
					System.out.println("l2比值："+Math.abs(facedis[tmpsecond]/facedis[tmp]));
					if(Math.abs(facedis[tmpsecond]/facedis[tmp])>3.5)
					{error1++;}
			}
			}
			else
			{
				//record[0]=-1;
				//identify[0]=-1;
				System.out.println("l2不在范围内");
				return null;
			}
			
			
			int tmpl1second;
			if(l1tmp!=-1){	 //l1  find out the second nearest
			tmpl1second=second(l1tmp, l1dis);
			identify[1]=l1tmp+1;
			tmpdis[1]=l1dis[l1tmp];
			if(tmpl1second!=-1){
				System.out.println("l1比值："+Math.abs(l1dis[tmpl1second]/l1dis[l1tmp]));
				if(Math.abs(l1dis[tmpl1second]/l1dis[l1tmp])>2.5)
					error1++;			
			}
			}
			else
			{
				//record[0]=-1;
				//identify[1]=-1;
				System.out.println("l1不在范围内");
				return null;
			}			
			 identify[2]=(matmp==-1)?-1:(matmp+1);
			 tmpdis[2]=(matmp==-1)?Double.MAX_VALUE:madis[matmp];
			 identify[3]=(mintmp==-1)?-1:(mintmp+1);
			 tmpdis[3]=(mintmp==-1)?Double.MAX_VALUE:mindis[mintmp];
			 
			 for(int c=0;c<identify.length;c++){
				 System.out.println(identify[c]);
			 }
			 
			 for(int c=0;c<identify.length;c++){
					for(int d=c+1;d<identify.length;d++){
						if(identify[c]!=identify[d])
						{
							record[0]=-1;
							System.out.println("4个值不等");
							return null;
						}
					}

					if(c==identify.length-1){
						record[0]=identify[c];
					}
				}
			 
			 
			 
			 
			 System.out.println("error1:"+error1);	
			 if(error1>=2)
				 {			
				 return null;
				 }
			 System.out.println("record :"+record[0]);
			VideoMarkModel vmm=new VideoMarkModel();
			vmm.setMark(record[0]);
			vmm.setDis(tmpdis);
			return vmm;
			 
	}
	
	private static int second(int min,double[] dis){
		double tmp=Double.MAX_VALUE;
		//System.out.println(tmp);
		int second=0;
		for(int i=0;i<dis.length;i++){
			//System.out.println(dis[i]);
			if(tmp>dis[i]&&i!=min)
				{
					tmp=dis[i];
					second=i;
				}
		}
		return second;
	}
}
