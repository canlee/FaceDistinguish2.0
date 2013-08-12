package test;

import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Calendar;

import javax.swing.JFrame;

import com.invindible.facetime.model.FaceImage;
import com.invindible.facetime.service.implement.CameraInterfaceImpl;
import com.invindible.facetime.service.implement.FindFaceForCameraInterfaceImpl;
import com.invindible.facetime.service.interfaces.CameraInterface;
import com.invindible.facetime.service.interfaces.FindFaceInterface;
import com.invindible.facetime.task.image.FindFaceTask;
import com.invindible.facetime.task.init.HarrCascadeParserTask;
import com.invindible.facetime.task.interfaces.Context;
import com.invindible.facetime.task.video.VideoStreamTask;
import com.invindible.facetime.ui.widget.image.ImagePanel;
import com.invindible.facetime.util.Debug;
import com.invindible.facetime.util.image.ImageUtil;

public class TestUi extends JFrame implements Context {
	
	private ImagePanel imagePanel;
//	private ImagePanel originPanel;
	
	private CameraInterface cif;
	private FindFaceInterface findTask;
	
	public TestUi() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1100, 500);
		setLayout(null);
//		originPanel = new ImagePanel();
//		originPanel.setBounds(0, 0, 500, 300);
		imagePanel = new ImagePanel();
		imagePanel.setBounds(510, 0, 500, 300);
//		add(originPanel);
		add(imagePanel);
//		BufferedImage image = ImageUtil.getImageFromFile("Pictures/test/12.jpg");
//		originPanel.setSize(image.getWidth(), image.getHeight());
//		originPanel.setBufferImage(image);
		setVisible(true);
		new HarrCascadeParserTask(this).start();
		cif = new CameraInterfaceImpl(this);
		cif.getCamera();
		findTask = new FindFaceForCameraInterfaceImpl(this);
		findTask.start();
	}

	@Override
	public void onRefresh(Object... objects) {
		Integer result = (Integer) objects[0];
		switch (result) {
		case VideoStreamTask.OPEN_CAMERA_SUCCESS:
			Component component = (Component) objects[1];
			component.setBounds(0, 0, 500, 300);
			add(component);
			setVisible(true);
//			while(true) {
//				Image image = cif.getHandledPictrue();
//				if(image != null) {
//					findTask.findFace(image);
//					break;
//				}
//			}
			break;
			 
		case FindFaceInterface.FIND_FACE_SUCCESS:
			FaceImage fi = (FaceImage) objects[1];
			if(fi.getFacesRgb().size() > 0) {
				BufferedImage img = ImageUtil.getImgByRGB(fi.getFacesRgb().get(0).getRgbMat());
				imagePanel.setBufferImage(img);
				Debug.print(img.getWidth() + ", " + img.getHeight());
				Calendar c = Calendar.getInstance();
//				ImageUtil.saveImage(img, "E:/" + c.getTimeInMillis() + ".jpg");
			}
//			imagePanel.setBounds(originPanel.getWidth() + 10, 0, img.getWidth(), img.getHeight());
			findTask.findFace(cif.getHandledPictrue());
			break;
			
		case HarrCascadeParserTask.PARSER_SUCCESS:
			Debug.print("读取adaboost文件成功！");
			break;
			
		default:
			break;
		}
	}
	
	public static void main(String[] args) {
		new TestUi();
	}

}
