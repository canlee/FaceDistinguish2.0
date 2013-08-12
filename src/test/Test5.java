package test;

import com.invindible.facetime.task.init.CopyDllTask;
import com.invindible.facetime.task.interfaces.Context;

public class Test5 implements Context {

	public static void main(String[] args) {
		new CopyDllTask(new Test5()).start();
	}

	@Override
	public void onRefresh(Object... objects) {
		
	}
	
}
