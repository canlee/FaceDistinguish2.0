package test;

import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import com.invindible.facetime.model.FaceImage;
import com.invindible.facetime.service.implement.FindVideoFaceImpl;
import com.invindible.facetime.service.interfaces.FindVideoFaceInterface;
import com.invindible.facetime.task.init.HarrCascadeParserTask;
import com.invindible.facetime.task.interfaces.Context;
import com.invindible.facetime.ui.widget.image.ImagePanel;
import com.invindible.facetime.util.Debug;
import com.invindible.facetime.util.image.ImageUtil;

public class Test4 extends JFrame implements Context {

	private ImagePanel imagePanel;
	private ImagePanel originPanel;
	
	public Test4() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1100, 500);
		setLayout(null);
		originPanel = new ImagePanel();
		originPanel.setBounds(0, 0, 500, 300);
		imagePanel = new ImagePanel();
		imagePanel.setBounds(510, 0, 500, 300);
		add(originPanel);
		add(imagePanel);
		setVisible(true);
		new HarrCascadeParserTask(this).start();
		FindVideoFaceInterface fvfi = new FindVideoFaceImpl(this, "e:/test.wmv", 0);
		fvfi.findFace();
	}
	
	@Override
	public void onRefresh(Object... objects) {
		switch ((Integer) objects[0]) {
		
		case FindVideoFaceInterface.FIND_VIDEO_FACE_SUCCESS:
			FaceImage fi = (FaceImage) objects[1];
			Debug.print("现在时间：" + fi.getTime());
			originPanel.setBufferImage(fi.getOriginImage());
			originPanel.setSize(fi.getOriginImage().getWidth(), fi.getOriginImage().getHeight());
			imagePanel.setLocation(fi.getOriginImage().getWidth() + 10, 0);
			BufferedImage img = ImageUtil.getImgByRGB(fi.getFacesRgb().get(0).getRgbMat());
			imagePanel.setBufferImage(img);
			imagePanel.setSize(img.getWidth(), img.getHeight());
			break;
			
		case FindVideoFaceInterface.FIND_VIDEO_FACE_FINISH:
			Debug.print("完成了");
			break;
			
		case FindVideoFaceInterface.OPEN_VIDEO_FAIL:
			Debug.print("打开视频失败");
			break;
		
		case HarrCascadeParserTask.PARSER_SUCCESS:
			Debug.print("读取adaboost文件成功！");
			break;

		default:
			break;
		}
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Test4();
	}

}
