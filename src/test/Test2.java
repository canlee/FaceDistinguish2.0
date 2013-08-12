package test;

import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import com.invindible.facetime.model.FaceImage;
import com.invindible.facetime.service.implement.FindFaceInterfaceImpl;
import com.invindible.facetime.service.interfaces.FindFaceInterface;
import com.invindible.facetime.task.init.HarrCascadeParserTask;
import com.invindible.facetime.task.interfaces.Context;
import com.invindible.facetime.ui.widget.image.ImagePanel;
import com.invindible.facetime.util.Debug;
import com.invindible.facetime.util.image.ImageUtil;

public class Test2 extends JFrame implements Context {
 
	private ImagePanel imagePanel;
	private ImagePanel originPanel;
	
	private FindFaceInterface findTask;
	
	public Test2() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1100, 500);
		setLayout(null);
		originPanel = new ImagePanel();
		originPanel.setBounds(0, 0, 500, 300);
		imagePanel = new ImagePanel();
		imagePanel.setBounds(510, 0, 500, 300);
		add(originPanel);
		add(imagePanel);
		BufferedImage image = ImageUtil.getImageFromFile("Pictures/test/21.jpg");
		originPanel.setSize(image.getWidth(), image.getHeight());
		originPanel.setBufferImage(image);
		setVisible(true);
		new HarrCascadeParserTask(this).start();
		findTask = new FindFaceInterfaceImpl(this);
		findTask.start();
		findTask.findFace(image);
	}

	@Override
	public void onRefresh(Object... objects) {
		Integer result = (Integer) objects[0];
		switch (result) {
			case FindFaceInterface.FIND_FACE_SUCCESS:
				FaceImage fi = (FaceImage) objects[1];
				if(fi.getFacesRgb().size() > 0) {
					BufferedImage img = ImageUtil.getImgByRGB(fi.getFacesRgb().get(0).getRgbMat());
					imagePanel.setBufferImage(img);
					Debug.print(img.getWidth() + ", " + img.getHeight());
					imagePanel.setBounds(originPanel.getWidth() + 10, 0, img.getWidth(), img.getHeight());
				}
				else {
					Debug.print("no face");
				}
				break;
				
			case HarrCascadeParserTask.PARSER_SUCCESS:
				Debug.print("读取adaboost文件成功！");
				break;
				
			default:
				break;
			}
	}
	
	public static void main(String[] args) {
		new Test2();
	}
	
}
