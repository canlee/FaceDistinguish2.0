package com.invindible.facetime.util.system;

import java.util.Properties;

public class SystemUtil {

	/**
	 * 获取当前系统的位数，根据所装的jdk位数确定。
	 * @return
	 */
	public static int getSystemBit() {
		Properties props=System.getProperties();
		String bitStr = props.getProperty("sun.arch.data.model");
		return Integer.parseInt(bitStr);
	}
	
	/**
	 * 获取当前系统的jdk安装路径
	 * @return
	 */
	public static String getJavaHome() {
		return System.getProperty("java.home");
	}
	
	/**
	 * 获取当前的运行文件的路径
	 * @return
	 */
	public static String getCurrentPath() {
		return System.getProperty("user.dir");
	}
	
}
