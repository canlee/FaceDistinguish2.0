package com.invindible.facetime.algorithm.feature;

import Jama.Matrix;

public class testing {

	public static void main(String[] args)
	{
//		double a = 9.4;
//		int a2 = Integer.parseInt(new java.text.DecimalFormat("0").format(a));
//		System.out.println(a2);
//		Matrix a =Matrix.random(1, 128*128);
//		double[][] temp = new double[1][1];
//		temp = a.getArray();
//		System.out.println("矩阵a维数是：" + "[" + temp.length + "]" + "[" + temp[0].length+ "]");
////		a.print(6, 2);
//		
//		Matrix b = Matrix.random(128*128	, 10);
//		double[][] temp2 = new double[1][1];
//		temp2 = b.getArray();
//		System.out.println("矩阵b维数是：" + "[" + temp2.length + "]" + "[" + temp2[0].length+ "]");
////		b.print(6, 2);
//		
//		Matrix c= a.times(b);
//		double[][] temp3 = new double[1][1];
//		temp3 = c.getArray();
//		System.out.println("矩阵c = 矩阵a * 矩阵b后：");
//		System.out.println("矩阵c维数是：" + "[" + temp3.length + "]" + "[" + temp3[0].length+ "]");
////		double[][] temp3 = new double[1][10];
//		System.out.println("计算结果是：______________________");
//		c.print(6, 2);
		
		double[][] a = {{3,6,9} ,{6,9,12}};
		Matrix ma = new Matrix(a);
		ma.print(4, 2);
//		ma = ma.times(10);
		ma = ma.transpose();
		ma.print(4, 2);
	}
}
