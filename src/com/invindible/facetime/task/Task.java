package com.invindible.facetime.task;

import com.invindible.facetime.task.interfaces.Context;

/**
 * 后台多线程任务类
 * 
 * @author canlee
 * 
 */
public abstract class Task extends Thread {

	protected boolean isRunning;
	protected Context context;

	public Task(Context context) {
		isRunning = false;
		this.context = context;
	}

	/**
	 * 任务是否正在运行
	 * 
	 * @return
	 */
	public boolean isRunning() {
		return isRunning;
	}
	
	/**
	 * 返回上下文
	 * @return
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * 该任务要做的事情
	 */
	protected abstract void doTask();

	/**
	 * 停止任务,这个要看子类是否实现该功能
	 */
	public abstract void stopTask();

	@Override
	public void run() {
		super.run();
		isRunning = true;
		doTask();
		isRunning = false;
	}

}
