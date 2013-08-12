package com.invindible.facetime.database;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class Test {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		boolean flag=ApplicationConfig.first();
		if(flag){
			ApplicationConfig.setupConfig("1521","orcl");
			ApplicationConfig.setupLink();
			OracleConfig.config("system","o123456");
			OracleConfig.configtable();
			System.out.println("ok");
		}
		else{
			ApplicationConfig.setupLink();
			System.out.println(Oracle_Connect.getInstance().getUrl());
		}
		
//		java.util.Date date=new java.util.Date();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String snow = sdf.format(date);
//		System.out.println(snow);
		
	}

}
