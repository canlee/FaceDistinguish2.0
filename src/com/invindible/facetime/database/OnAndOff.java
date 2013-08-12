package com.invindible.facetime.database;

import java.io.IOException;

public class OnAndOff  {
	
	private OnAndOff(){}
	
	private static final OnAndOff oao=new OnAndOff();
	
	/**
	 * 单例模式 返回OnAndOff实例
	 */
	public static OnAndOff getInstance() {
		return oao;
	}

	
	/**
	 * 启动oracle数据库
	 */
	public void Start(){
		try {
			Runtime.getRuntime().exec("lsnrctl start");
			Runtime.getRuntime().exec("net start oracleserviceorcl");
			System.out.print("success!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭oracle数据库
	 */
	
	public void Stop(){
		try {
			Runtime.getRuntime().exec("net stop oracleserviceorcl");
			Runtime.getRuntime().exec("lsnrctl stop");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
