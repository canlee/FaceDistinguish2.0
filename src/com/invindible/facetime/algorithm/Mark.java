package com.invindible.facetime.algorithm;

public class Mark {
	public static boolean domark(double[][] test,double[][] model,double[] testMean,double[][] modelMean,double[] allMean){
		int target=modelMean.length;
		System.out.println("target "+target);
		
		int[] record=mark(test,model,testMean,modelMean,allMean);
		int sum=0;
		for(int i=0;i<record.length;i++){
			if(record[i]==target)
				sum++;
		}
		System.out.println("sum:"+sum);
		if(sum>=2)
			return true;
		else
			return false;
		
	}
	
	public static int[] mark(double[][] test,double[][] model,double[] testMean,double[][] modelMean,double[] allMean){
		double value=L2Form.value(model,allMean);		//l2 domain 
		double l1value=L1Form.l1value(model,allMean);   //l1 domain			
		double[] madis=MixedMahalnobisDistance.calMixedMahalnobisDistance(testMean,modelMean);	 //ma distance	
		double[] mindis=MixedMahalnobisDistance.calMinDistance(testMean, modelMean);  //min distance
		int matmp=Identify.mark(Double.MAX_VALUE,madis);	//ma domain
		int mintmp=Identify.mark(Double.MAX_VALUE,mindis);  //min domain
		
//		double[] facedis;  //l2 distance
//		double[] l1dis;   //l1 distance
		int[] record=new int[3];
		
		System.out.println("l1+l2 value:"+value+" "+l1value);
		
		for(int i=0;i<=test.length;i++){
			boolean l2,l1;
			int[] identify=new int [4];
			int error1=0; //error1 l2 l1
			if(i==test.length){
				l2=L2Form.inL2(testMean, allMean, value);
				l1=L1Form.inL1(testMean, allMean, l1value);	//whether in or not
				if(!l1||!l2)
				{
					record[i]=-1;
					System.out.println("不在范围内");
					continue;
				}
//				facedis=L2Form.L2Form(model, testMean);  //calculate l2 distance
//				l1dis=L1Form.L1Form(model, testMean);   //calculate l1 distance
			}
			else
			{
				l2=L2Form.inL2(test[i], allMean, value);
				l1=L1Form.inL1(test[i], allMean, l1value);
				if(!l1||!l2) 
				{
					record[i]=-1;
					System.out.println("不在范围内");
					continue;
				}
//				facedis=L2Form.L2Form(model, test[i]);  //calculate l2 distance
//				l1dis=L1Form.L1Form(model, test[i]);   //calculate l1 distance
			}			
//			int tmp=Identify.mark(value, facedis);  //l2 find out the nearest
//			int l1tmp=Identify.mark(l1value, l1dis);  //l1 find out the nearest
//			int tmpsecond;
			
			//---------------------------与总体比较
//			if(tmp!=-1){
//				tmpsecond=second(tmp,facedis);  //l2 find out the second nearest
//				identify[0]=tmp/5+1;
//				if(tmpsecond!=-1){
//					if(Math.abs(facedis[tmpsecond]/facedis[tmp])<5)
//					{;}
//					else if(tmp/5+1!=tmpsecond/5+1)
//						{
//							error2++;				
//						}
//					}
//			}
//			else
//			{
//				record[i]=-1;
//				System.out.println("l2不在范围内");
//				continue;
//			}
//			
//			
//			int tmpl1second;
//			if(l1tmp!=-1){	 //l1  find out the second nearest
//			tmpl1second=second(l1tmp, l1dis);
//			identify[2]=l1tmp/5+1;
//			if(tmpl1second!=-1){
//				if(Math.abs(l1dis[tmpl1second]/l1dis[l1tmp])<5)
//				;			
//				else if(l1tmp/5+1!=tmpl1second/5+1)
//					{
//					error1++;					
//					}
//				}
//			}
//			else
//			{
//				record[i]=-1;
//				System.out.println("l1不在范围内");
//				continue;
//			}
			
			//---------------------------与总体比较
			
			
			
			//-----------------------与平均比较
			double[] l2facedis;
			double[] l1facedis;      //l1 form
			if(i==test.length){
				l2facedis=L2Form.L2Form(modelMean, testMean);		//l2 form
				l1facedis=L1Form.L1Form(modelMean, testMean); 
			}
			else
				{
					l2facedis=L2Form.L2Form(modelMean, test[i]);
					l1facedis=L1Form.L1Form(modelMean, test[i]);
				}
			int tmp=Identify.mark(value, l2facedis);
			int l1tmp=Identify.mark(l1value, l1facedis);
			
			 if(tmp!=-1){
					int second=second(tmp,l2facedis);
					identify[0]=tmp+1;
					if(second!=-1){
					if(l2facedis[tmp]<0.55*l2facedis[second])
					{;}
					else
						error1++;					
				}
			 }
			 else
			 {
					record[i]=-1;
					System.out.println("l2均值不在范围内");
					continue;
			}
			 
			 if(l1tmp!=-1){
					int second=second(tmp,l1facedis);
					identify[1]=l1tmp+1;
					if(second!=-1){
					if(l1facedis[l1tmp]<0.55*l1facedis[second])
					{;}
					else
						error1++;					
				}
			 }
			 else
			 {
					record[i]=-1;
					System.out.println("l1均值不在范围内");
					continue;
				}
			 
			 identify[2]=matmp+1;
			 identify[3]=mintmp+1;
			 
			 for(int a=0;a<l2facedis.length;a++){
				 System.out.println("l2:"+l2facedis[a]);
				 System.out.println("l1:"+l1facedis[a]);
				 System.out.println("ma:"+madis[a]);
				 System.out.println("min:"+mindis[a]);
			 }
			 
			 
			//-----------------------与平均比较
			 
			 for(int c=0;c<identify.length;c++){
					for(int d=c+1;d<identify.length;d++){
						if(identify[c]!=identify[d])
						{
							record[i]=-1;
							System.out.println("4个值不等");
							break;
						}
					}
					if(record[i]==-1)
						{
							break;
						}
					else if(c==identify.length-1){
						record[i]=identify[c];
					}
				}
			 if(error1>=2)
				 record[i]=-1;
			 System.out.println("record "+i+" :"+record[i]);
			
		}
		return record;
	}
	
	public static int identify(double[][] test,double[][] model,double[] testMean,double[][] modelMean,double[] allMean){	
		
		int[] record=mark(test,model,testMean,modelMean,allMean);	
		int[] tmp=new int[modelMean.length];
		for(int i=0;i<record.length;i++){
			if(record[i]!=-1)
			tmp[record[i]-1]++;
		}
		
		int max=tmp[0];
		int index=0;
		for(int i=1;i<tmp.length;i++){
			if(max<tmp[i]){
				index=i;
				max=tmp[i];
			}
		}
		System.out.println("index+max:"+index+" "+max);
		if(max>=2)
			return index+1;
		else
			return -1;
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
