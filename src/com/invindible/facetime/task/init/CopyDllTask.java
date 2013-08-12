package com.invindible.facetime.task.init;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.invindible.facetime.task.Task;
import com.invindible.facetime.task.interfaces.Context;
import com.invindible.facetime.util.system.SystemUtil;

/**
 * 把JMF的dll复制到Windows/System32的目录下
 * <br>在程序第一次运行时执行的任务
 * @author 李亮灿
 *
 */
public class CopyDllTask extends Task {

	/**
	 * 复制dll文件到系统文件夹成功
	 */
	public static final int COPY_DLL_SUCCESS = 70000;
	
	/**
	 * 复制dll文件到系统文件夹失败
	 */
	public static final int COPY_DLL_FAIL = 70000;
	
	public CopyDllTask(Context context) {
		super(context);
	}
	
	
	@Override
	protected void doTask() {
		String dllFilePath = SystemUtil.getJavaHome() + "\\bin";
		File jmfDlls = new File("dll/jmf");
		try {
			byte[] buf = new byte[1024 * 8];
			int len = 0;
			for(File dll : jmfDlls.listFiles()) {
				File cpDll = new File(dllFilePath + "\\" + dll.getName());
				if(!cpDll.exists()) {
					cpDll.createNewFile();
					FileOutputStream out = new FileOutputStream(cpDll);
					FileInputStream in = new FileInputStream(dll);
					while((len = in.read(buf, 0, buf.length)) != -1) {
						out.write(buf, 0, len);
					}
					out.flush();
					out.close();
					in.close();
				}
			}
			context.onRefresh(COPY_DLL_SUCCESS);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			context.onRefresh(COPY_DLL_FAIL);
		} catch (IOException e) {
			e.printStackTrace();
			context.onRefresh(COPY_DLL_FAIL);
		}
	}

	/**
	 * 不实现
	 */
	@Override
	public void stopTask() {
		
	}

}
