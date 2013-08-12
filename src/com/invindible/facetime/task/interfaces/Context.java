package com.invindible.facetime.task.interfaces;

/**
 * 联系上下文
 * 
 * @author canlee
 * 
 */
public interface Context {

	/**
	 * 某个后台的任务调用传来更新
	 * 
	 * @param objects
	 */
	public void onRefresh(Object... objects);

}
