package com.invindible.facetime.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;

import com.invindible.facetime.algorithm.LDA;
import com.invindible.facetime.algorithm.Mark;
import com.invindible.facetime.algorithm.UiAlgorithm.RegistAlgorithm;
import com.invindible.facetime.algorithm.UiAlgorithm.UiImageHandle;
import com.invindible.facetime.algorithm.feature.Features;
import com.invindible.facetime.algorithm.feature.GetFeatureMatrix;
import com.invindible.facetime.algorithm.feature.GetPcaLda;
import com.invindible.facetime.database.Oracle_Connect;
import com.invindible.facetime.database.ProjectDao;
import com.invindible.facetime.database.UserDao;
import com.invindible.facetime.model.FaceImage;
import com.invindible.facetime.model.Imageinfo;
import com.invindible.facetime.model.LdaFeatures;
import com.invindible.facetime.model.Project;
import com.invindible.facetime.model.User;
import com.invindible.facetime.model.Wopt;
import com.invindible.facetime.service.implement.CameraInterfaceImpl;
import com.invindible.facetime.service.implement.FindFaceForCameraInterfaceImpl;
import com.invindible.facetime.service.interfaces.CameraInterface;
import com.invindible.facetime.service.interfaces.FindFaceInterface;
import com.invindible.facetime.task.init.HarrCascadeParserTask;
import com.invindible.facetime.task.interfaces.Context;
import com.invindible.facetime.task.video.VideoStreamTask;
import com.invindible.facetime.util.Debug;
import com.invindible.facetime.util.image.ImageUtil;
import com.invindible.facetime.wavelet.Wavelet;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;

import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.TitledBorder;

public class FrameRegist extends JFrame implements Context{

	public static FrameRegist frameRegist;
	private JPanel contentPane;
	private JPanel panelCamera;

	private JButton btn1;
	private JButton btn2;
	private JButton btn3;
	private JButton btn4;
	private JButton btn5;
	private JButton btn6;
	private JButton btn7;
	
	private CameraInterface cif;
	public static FindFaceInterface findTask;
	
//	private int photoIndex = 1;
	private ImageIcon[] imageIcons;// = new ImageIcon[5];//5张照片
	private ImageIcon[] testIcons;//测试用照片，2张
//	private BufferedImage[] soyBufferedImages;
	private boolean[] isImageIconSelected;// = new boolean[7];//第i个照片是否要更换的标志
//	private int[] changeIndex = {1,2,3,4,5};
	private boolean startChangeSelectedIcon;// = true;//是否要更换照片的标志
	private int requestNum;// = 7;//剩余的需要更换的照片数量
//	private int testNum = 2;//测试样例的数量(默认为2)
//	private int photoNum = 5;//每个人的照片数量(默认为5)
	
//	private boolean haveSoy = false;//是否有加入“酱油”进投影Z中的标志

	/**
	 * Create the frame.
	 */
	public FrameRegist(final String userId, final String passWord, final User user) {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		setTitle("1.用户注册-照相识别");
		testIcons = new ImageIcon[2];
		imageIcons = new ImageIcon[5];
//		soyBufferedImages= new BufferedImage[5];
		isImageIconSelected = new boolean[7];
		startChangeSelectedIcon = true;
		requestNum = 7;
		
		
		for(int i=0; i<7; i++)
		{
			isImageIconSelected[i] = true;
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setBounds(100, 100, 707, 443);
		setBounds(100, 100, 921, 541);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panelCamera = new JPanel();
		panelCamera.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelCamera.setBounds(19, 41, 400, 260);
		contentPane.add(panelCamera);
		panelCamera.setLayout(null);
		

		//开启摄像头
//		new HarrCascadeParserTask(this).start();
		cif = new  CameraInterfaceImpl(this);
		cif.getCamera();
		findTask = new FindFaceForCameraInterfaceImpl(this);
		findTask.start();
		
		//new VideoStreamTask(this).start();
		/*
		ImageIcon cam = new ImageIcon("Pictures/Camera.jpg");
		JLabel lblCamera = new JLabel(ImageHandle(cam,  399, 259));
		lblCamera.setBounds(0, 0, 399, 259);
		panelCamera.add(lblCamera);
		*/
		
		
		JPanel panelButton = new JPanel();
		panelButton.setBounds(19, 409, 386, 55);
		contentPane.add(panelButton);
		panelButton.setLayout(null);
		
		JButton btnRegist = new JButton("确认注册");
		btnRegist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//若识别成功，则在方法中保存数据进数据库，之后关闭此窗口
				if (RegistAlgorithm.startDistinguish(user, imageIcons,testIcons,requestNum,isImageIconSelected,startChangeSelectedIcon)
					== true	)
				{
					//最终注册成功后，将寻找人脸的方法暂停
					findTask.stop();
					
					frameRegist.dispose();
					MainUI.frameMainUI = new MainUI();
					MainUI.frameMainUI.setVisible(true);
					
					
				}
				//若识别失败，则重新获取图片
				else
				{
					
					//为7张图片打√
//					//将数据初始化，以开始重新获取图片
//					requestNum = 7;
					btn1.doClick();
					btn2.doClick();
					btn3.doClick();
					btn4.doClick();
					btn5.doClick();
					btn6.doClick();
					btn7.doClick();
					
					startChangeSelectedIcon = true;
				}
				
			}
		});
		btnRegist.setBounds(71, 10, 110, 35);
		panelButton.add(btnRegist);
		
		JButton btnReturn = new JButton("返回主界面");
		btnReturn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//询问是否确认放弃注册，并返回主界面
				if(JOptionPane.YES_OPTION == 
						JOptionPane.showConfirmDialog(null, "放弃本次注册？", "提示", JOptionPane.YES_NO_CANCEL_OPTION))
				{
					
					frameRegist.dispose();
					MainUI.frameMainUI = new MainUI();
					MainUI.frameMainUI.setVisible(true);
					
					//点击返回后，将寻找人脸的方法暂停
					findTask.stop();
				}
				else
				{
					return;
				}
				
				
				
			}
		});
		btnReturn.setBounds(231, 10, 110, 35);
		panelButton.add(btnReturn);
		
		JPanel panelCapture = new JPanel();
		panelCapture.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "\u6CE8\u518C\u7167\u7247", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		//panelInstructions.setBounds(437, 41, 244, 259);
		panelCapture.setBounds(428, 16, 434, 297);
		contentPane.add(panelCapture);
		panelCapture.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 21, 128, 128);
		panelCapture.add(panel);
		panel.setLayout(null);
		
		btn1 = new JButton("暂无捕获头像");
		btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawNikeOnObject(btn1, 0);
				
			}
		});
		btn1.setBounds(0, 0, 128, 128);
		panel.add(btn1);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(153, 21, 128, 128);
		panelCapture.add(panel_1);
		panel_1.setLayout(null);
		
		btn2 = new JButton("暂无捕获头像");
		btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawNikeOnObject(btn2, 1);
				
			}
		});
		btn2.setBounds(0, 0, 128, 128);
		panel_1.add(btn2);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(296, 21, 128, 128);
		panelCapture.add(panel_2);
		panel_2.setLayout(null);
		
		btn3 = new JButton("暂无捕获头像");
		btn3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawNikeOnObject(btn3, 2);
				
			}
		});
		btn3.setBounds(0, 0, 128, 128);
		panel_2.add(btn3);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(10, 159, 128, 128);
		panelCapture.add(panel_3);
		panel_3.setLayout(null);
		
		btn4 = new JButton("暂无捕获头像");
		btn4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawNikeOnObject(btn4, 3);
				
			}
		});
		btn4.setBounds(0, 0, 128, 128);
		panel_3.add(btn4);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBounds(153, 159, 128, 128);
		panelCapture.add(panel_4);
		panel_4.setLayout(null);
		
		btn5 = new JButton("暂无捕获头像");
		btn5.setBounds(0, 0, 128, 128);
		panel_4.add(btn5);
		btn5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawNikeOnObject(btn5, 4);
				
			}
		});
		JLabel lblUserID = new JLabel("你的用户名：");
		lblUserID.setBounds(42, 16, 89, 15);
		contentPane.add(lblUserID);
		
		JLabel lblInstructions = new JLabel("请在截取7张图像后，点击“确认注册”\r\n\r\n。（可更换照片）");
		lblInstructions.setBounds(19, 310, 360, 85);
		contentPane.add(lblInstructions);
		
		JButton btnTakeNewPhoto = new JButton("更换选中照片");
		btnTakeNewPhoto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i=0; i<7; i++)
				{
					//若需要更换指定索引的照片
					if(isImageIconSelected[i] == true)
					{
						startChangeSelectedIcon = true;
						break;
					}
				}
				
			}
		});
		btnTakeNewPhoto.setBounds(430, 359, 116, 36);
		contentPane.add(btnTakeNewPhoto);
		
		JLabel lblUserName = new JLabel("New label");
		lblUserName.setBounds(129, 16, 54, 15);
		contentPane.add(lblUserName);
		lblUserName.setText(userId);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "\u6D4B\u8BD5\u7528\u7167\u7247\uFF08\u4E0D\u4FDD\u5B58\uFF0C\u4EC5\u4E3A\u6D4B\u8BD5\uFF09", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_5.setBounds(561, 319, 297, 170);
		contentPane.add(panel_5);
		panel_5.setLayout(null);
		
		JPanel panel_6 = new JPanel();
		panel_6.setLayout(null);
		panel_6.setBounds(10, 32, 128, 128);
		panel_5.add(panel_6);
		
		btn6 = new JButton("暂无捕获头像");
		btn6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawNikeOnObject(btn6, 5);
				
			}
		});
		btn6.setBounds(0, 0, 128, 128);
		panel_6.add(btn6);
		
		JPanel panel_7 = new JPanel();
		panel_7.setLayout(null);
		panel_7.setBounds(159, 32, 128, 128);
		panel_5.add(panel_7);
		
		btn7 = new JButton("暂无捕获头像");
		btn7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawNikeOnObject(btn7, 6);
				
			}
		});
		btn7.setBounds(0, 0, 128, 128);
		panel_7.add(btn7);
	}

	@Override
	public void onRefresh(Object... objects) {
		// TODO Auto-generated method stub
		Integer result = (Integer) objects[0];
		switch (result) {
		case VideoStreamTask.OPEN_CAMERA_SUCCESS:
			Component component = (Component) objects[1];
			System.out.println("====width:" + component.getSize().width);
			System.out.println("====height:" + component.getSize().height);
//			component.setBounds(0, 0, 314, 229);
			component.setBounds(0, 0, 387, 260);
			component.setLocation(7, 0);
			panelCamera.add(component);
			while(true) {
				Image image = cif.getHandledPictrue();
				if(image != null) {
					findTask.findFace(image);
					break;
				}
			}
			break;
			
		case FindFaceInterface.FIND_FACE_SUCCESS:
			FaceImage fi = (FaceImage) objects[1];
			if(fi.getFacesRgb().size() > 0) {
				BufferedImage img = ImageUtil.getImgByRGB(fi.getFacesRgb().get(0).getRgbMat());
//				Icon icon = (Icon) img;
//				Image image = img;
				ImageIcon imgIcon = new ImageIcon(img);
				imgIcon = UiImageHandle.ImageHandle(imgIcon, 128, 128);
				
				if(startChangeSelectedIcon == true)
				{
					for(int i=0; i<7; i++)
					{
						if(isImageIconSelected[i] == true)
						{
							isImageIconSelected[i] = false;
							switch(i)
							{
								case 0:
									this.btn1.setIcon( imgIcon);
									this.imageIcons[0] = imgIcon;
									break;
								case 1:
									this.btn2.setIcon( imgIcon);
									this.imageIcons[1] = imgIcon;
									break;
								case 2:
									this.btn3.setIcon( imgIcon);
									this.imageIcons[2] = imgIcon;
									break;
								case 3:
									this.btn4.setIcon( imgIcon);
									this.imageIcons[3] = imgIcon;
									break;
								case 4:
									this.btn5.setIcon( imgIcon);
									this.imageIcons[4] = imgIcon;
									break;
								case 5:
									this.btn6.setIcon( imgIcon);
									this.testIcons[0] = imgIcon;
									break;
								case 6:
									this.btn7.setIcon( imgIcon);
									this.testIcons[1] = imgIcon;
									break;
							}
							
							if( i == 6)
							{
								startChangeSelectedIcon = false;
								System.out.println("修改了startChangesSelected");
							}
							
							requestNum--;
							if(requestNum == 0)
							{
								startChangeSelectedIcon = false;
								System.out.println("修改了startChangesSelected");
							}
								
							break;
						}
						
					}
				}

//				//若拍照索引已经超过5，则停止截图
//				if( photoIndex < 6)
//				{
//					photoIndex++;
//				}
//				
////				imagePanel.setBufferImage(img);
//				Debug.print(img.getWidth() + ", " + img.getHeight());
//				Calendar c = Calendar.getInstance();
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
	
	//为按钮打钩
	public void drawNikeOnObject(JButton btn, int objectIndex)
	{
		
		try
		{
			//判断是否已经打钩
			//若未打钩
			if(isImageIconSelected[objectIndex] == false)
			{
				if(objectIndex < 5)
				{
					btn.setIcon(FrameWindow.drawNike(imageIcons[objectIndex]));
				}
				else
				{
					btn.setIcon(FrameWindow.drawNike(testIcons[objectIndex-5]));
				}
				isImageIconSelected[objectIndex] = true;
				requestNum++;
			}
			//若已打钩
			else
			{
				if(objectIndex < 5)
				{
					btn.setIcon(imageIcons[objectIndex]);
				}
				else
				{
					btn.setIcon(testIcons[objectIndex-5]);
				}
				isImageIconSelected[objectIndex] = false;
				requestNum--;
			}
		}
		catch(Exception ex)
		{
			;
		}
	}
	
}
