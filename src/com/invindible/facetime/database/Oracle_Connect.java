package com.invindible.facetime.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Oracle_Connect {

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SQLException 
	 */
	private Oracle_Connect() {
	}
	
	private static final Oracle_Connect o_c=new Oracle_Connect();
	
	private String url;
	

	public void setUrl(String url) {
		this.url = url;
	}

	private final static String user="ai_face";
	private final static String password="face_ai";
	private Connection conn;
	
	public Connection getConn() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException  {
		Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
		conn=DriverManager.getConnection(url,user,password);
		return conn;
	}

	public String getUrl() {
		return url;
	}

	public static Oracle_Connect getInstance(){
		return o_c;
	}
	
	protected void close(){
		if(this.conn!=null){
			try{
				this.conn.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
