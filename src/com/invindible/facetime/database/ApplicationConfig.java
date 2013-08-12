package com.invindible.facetime.database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ApplicationConfig {
	/**
	 * 检查是否第一次运行
	 * @return
	 * @throws IOException
	 */
	public static boolean first() throws IOException{
		File f=new File("oracle\\config");
		if(!f.exists()){
			f.createNewFile();
			return true;
		}
		else
			{
			BufferedReader br=new BufferedReader(new FileReader(f));
			String tmp=br.readLine();
			System.out.println(tmp);
			if(tmp==null||tmp.equals(""))
				return true;
			else
				return false;
			}
	}
	
	/**
	 * 建立配置文件
	 * @param port
	 * @param databasename
	 * @throws IOException
	 */
	public static void setupConfig(String port,String databasename) throws IOException{
		File f=new File("oracle\\config");
		BufferedWriter bw=new BufferedWriter(new FileWriter(f));
		bw.write("1\n");
		bw.write(port+"\n");
		bw.write(databasename+"\n");
		bw.flush();
		bw.close();
	}
	
	/** 
	 * 建立数据连接
	 * @throws IOException
	 */
	public static void setupLink() throws IOException{
		File f=new File("oracle\\config");
		BufferedReader br=new BufferedReader(new FileReader(f));
		br.readLine();
		String port=br.readLine();
		String databasename=br.readLine();
		br.close();
		String url="jdbc:oracle:thin:@localhost"+":"+port+":"+databasename;
		Oracle_Connect.getInstance().setUrl(url);
	}
	
}
