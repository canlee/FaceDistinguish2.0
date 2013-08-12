package test;

import java.awt.FileDialog;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Properties;

import javax.swing.JFrame;

import com.invindible.facetime.task.interfaces.Context;
import com.invindible.facetime.task.video.VideoPrintScreenTask;
import com.invindible.facetime.task.video.natives.VideoPrint32Bit;
import com.invindible.facetime.task.video.natives.VideoPrintListener;
import com.invindible.facetime.ui.widget.image.ImagePanel;
import com.invindible.facetime.util.image.ImageUtil;

public class Test3 extends JFrame implements Context {

	private ImagePanel imagePanel;
	private VideoPrintScreenTask printScreenTask;
	
	public Test3() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1100, 500);
		setLayout(null);
		imagePanel = new ImagePanel();
		imagePanel.setBounds(0, 0, 500, 500);
		add(imagePanel);
		setVisible(true);
//		try {
//		    FileDialog fd = 
//		      new FileDialog(this, "Select File", FileDialog.LOAD);
//		    fd.show();
//		    String filename = fd.getDirectory() + fd.getFile();
//		    new VideoPrintScreenTask(this, filename).start();
//		  }
//		  catch (Exception e) {
//		    System.out.println(e.toString());
//		  }
		printScreenTask = new VideoPrintScreenTask(this, "e:/test.wmv");
		printScreenTask.start();
//		int[][][] rgbMat = printScreenTask.getScreenByTime(1800000);
//		BufferedImage img = ImageUtil.getImgByRGB(rgbMat);
//		imagePanel.setBufferImage(img);
//		imagePanel.setSize(img.getWidth(), img.getHeight());
		
	}
	
	@Override
	public void onRefresh(Object... objects) {
		int result = (Integer) objects[0];
		switch (result) {
		case VideoPrintListener.PRINT_RGB_MAT:
			int[][][] rgbMat = (int[][][]) objects[1];
			long time = (Long) objects[2];
//			if(time == 1000) {
				System.out.println(time);
				BufferedImage img = ImageUtil.getImgByRGB(rgbMat);
				imagePanel.setBufferImage(img);
				imagePanel.setSize(img.getWidth(), img.getHeight());
				printScreenTask.stopTask();
//			}
			break;

		default:
			break;
		}
	}
	
	public static void main(String[] args) {
		new Test3();
	}

}
