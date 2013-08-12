package com.invindible.facetime.algorithm;

public class Identify {
	static int mark(double value,double[] distance){
		int i=distance.length;
		int mark=0;
		double tmp=0;
		for(int j=0;j<i;j++){
			if(j==0)
				tmp=distance[j];
			else
				{
				
				if(tmp>distance[j]){
					tmp=distance[j];
					mark=j;
					}	
				
				}
		}
		if(tmp<value)
			return mark;
		else
			return -1;
	}
}
