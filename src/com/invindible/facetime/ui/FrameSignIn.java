package com.invindible.facetime.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.image.BufferedImage;

import javax.swing.JButton;

import com.invindible.facetime.algorithm.LDA;
import com.invindible.facetime.algorithm.Mark;
import com.invindible.facetime.algorithm.UiAlgorithm.UiImageHandle;
import com.invindible.facetime.algorithm.feature.Features;
import com.invindible.facetime.database.ApplicationConfig;
import com.invindible.facetime.database.Oracle_Connect;
import com.invindible.facetime.database.ProjectDao;
import com.invindible.facetime.database.UserDao;
import com.invindible.facetime.model.FaceImage;
import com.invindible.facetime.model.LdaFeatures;
import com.invindible.facetime.model.Project;
import com.invindible.facetime.model.User;
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

import javax.swing.border.BevelBorder;
import java.awt.Color;
import javax.swing.border.LineBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

public class FrameSignIn extends JFrame implements Context{

	static FrameSignIn frameSignIn;
	
	private JPanel contentPane;
	
	private JPanel panelCamera;
	private JPanel panelCapture;
	private JPanel panelResult;
	private JButton btnSignIn;
	private JLabel labelResult;
	private JButton buttonCapture1;
	private JButton buttonCapture2;
	
	static CameraInterface cif;
	static FindFaceInterface findTask;
	
	private ImageIcon[] userImageIcons;
	private ImageIcon[] imageIconCaptures;//保存摄像头捕获的头像
	private ImageIcon imageIconResult;//保存识别的头像
	private boolean[] isImageIconSelected;//第i个照片是否要更换的标志
	private boolean changePhoto;//是否要更换照片的标志
	private int requestNum;//剩余的需要更换的照片数量
	private Connection conn = null;
	private JButton buttonStart;
	
	private int userIdForSign;
	private ImageIcon[] imageFind;
	private JTextField txtUserId;
	
	private String userAccount;
	private JPasswordField passwordField;

//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					FrameSignIn frame = new FrameSignIn();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public FrameSignIn() {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		setTitle("2.登陆签到-识别/登陆");
//		try {
//			ApplicationConfig.setupLink();
//		} catch (IOException e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		}
		userImageIcons = new ImageIcon[5];
		imageIconCaptures = new ImageIcon[2];
		imageIconResult = new ImageIcon();
		isImageIconSelected = new boolean[2];
		imageFind = new ImageIcon[5];
		changePhoto = true;
		requestNum = 2;
		
		for(int i=0; i<2; i++)
		{
			isImageIconSelected[i] = true;
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 790, 534);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panelCamera = new JPanel();
		panelCamera.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelCamera.setBounds(27, 24, 400, 260);
		contentPane.add(panelCamera);
		panelCamera.setLayout(null);
		
		JLabel label_1 = new JLabel("可用 人脸识别/账户密码 登陆");
		label_1.setBounds(189, 293, 232, 85);
		contentPane.add(label_1);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(20, 315, 159, 55);
		panel_1.setLayout(null);
		contentPane.add(panel_1);
		
		JButton buttonReturn = new JButton("返回主界面");
		buttonReturn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				findTask.stop();
				frameSignIn.dispose();
				MainUI.frameMainUI = new MainUI();
				MainUI.frameMainUI.setVisible(true);
			}
		});
		buttonReturn.setBounds(22, 10, 110, 35);
		panel_1.add(buttonReturn);
		
		JPanel panelCaptureBox = new JPanel();
		panelCaptureBox.setBorder(new TitledBorder(null, "\u6355\u6349\u7684\u5934\u50CF", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		panelCaptureBox.setBounds(437, 24, 312, 211);
		contentPane.add(panelCaptureBox);
		panelCaptureBox.setLayout(null);
		
		panelCapture = new JPanel();
		panelCapture.setBounds(10, 43, 128, 128);
		panelCaptureBox.add(panelCapture);
		panelCapture.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelCapture.setLayout(null);
		
		buttonCapture1 = new JButton("暂无捕获头像");
		buttonCapture1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawNikeOnObject(buttonCapture1, 0);
			}
		});
		buttonCapture1.setBounds(0, 0, 128, 128);
		panelCapture.add(buttonCapture1);
		
		JButton btnCapture = new JButton("重新捕捉");
		btnCapture.setBounds(99, 176, 105, 27);
		panelCaptureBox.add(btnCapture);
		btnCapture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i=0; i<2; i++)
				{
					if(isImageIconSelected[i] == true)
					{
						changePhoto = true;
						break;
					}
				}
				
				System.out.println(changePhoto);
				
			}
		});
		btnCapture.setFont(new Font("宋体", Font.PLAIN, 16));
		
		JPanel panelCapture1 = new JPanel();
		panelCapture1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelCapture1.setLayout(null);
		panelCapture1.setBounds(167, 43, 128, 128);
		panelCaptureBox.add(panelCapture1);
		
		buttonCapture2 = new JButton("暂无捕获头像");
		buttonCapture2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawNikeOnObject(buttonCapture2, 1);
			}
		});
		buttonCapture2.setBounds(0, 0, 128, 128);
		panelCapture1.add(buttonCapture2);
		
		JPanel panelResultBox = new JPanel();
		panelResultBox.setBorder(new TitledBorder(null, "\u8BC6\u522B\u7ED3\u679C", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelResultBox.setBounds(436, 245, 319, 173);
		contentPane.add(panelResultBox);
		panelResultBox.setLayout(null);
		
		JLabel label_2 = new JLabel("识别结果：");
		label_2.setBounds(93, 2, 115, 21);
		panelResultBox.add(label_2);
		label_2.setFont(new Font("华文细黑", Font.PLAIN, 16));
		
		panelResult = new JPanel();
		panelResult.setBounds(22, 33, 128, 128);
		panelResultBox.add(panelResult);
		panelResult.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelResult.setLayout(null);
		
		labelResult = new JLabel("暂无识别结果");
		labelResult.setHorizontalAlignment(SwingConstants.CENTER);
		labelResult.setBounds(0, 0, 128, 128);
		panelResult.add(labelResult);
		labelResult.setFont(new Font("华文细黑", Font.PLAIN, 16));
		
		btnSignIn = new JButton("签到");
		btnSignIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//将 5张照片 和 用户名 当做参数，传递给下一个窗口
				
				
				//(点击“签到”后，将5张照片当做参数，传递给下一个窗口。)
				frameSignIn.setVisible(false);
				FrameSignInConfirm.frameSignInConfirm = 
						new FrameSignInConfirm(userImageIcons,userAccount, userIdForSign);
				FrameSignInConfirm.frameSignInConfirm.setVisible(true);
				
				
			}
		});
		btnSignIn.setBounds(175, 78, 105, 27);
		panelResultBox.add(btnSignIn);
		btnSignIn.setFont(new Font("宋体", Font.PLAIN, 16));
		//设置“签到”按钮默认无法点击
		btnSignIn.setEnabled(false);
		
		buttonStart = new JButton("开始识别");
		buttonStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				labelResult.setIcon(null);
				
				//若图片尚未截取,则提示请先等待拍照
				System.out.println("requestNum:" + requestNum);
				if(requestNum != 0)
				{
					JOptionPane.showMessageDialog(null, "图片尚未截取完毕,等待摄像头截取面部信息。", "提示", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				
				
//				//从数据库中读取所有样本数据和WoptT
				try
				{
					conn = Oracle_Connect.getInstance().getConn();
					
					//设置每人的图片数量
					LdaFeatures.getInstance().setNum(5);
					
					//从数据库获取WoptT
					double[][] Wopt = ProjectDao.doselectWopt(conn);
//					System.out.println("Wopt的维数为:[" + Wopt.length + "])");//[" + Wopt[0].length + "]");
					//将WoptT保存进单例中
					LdaFeatures.getInstance().setLastProjectionT(Wopt);
					
					//从数据库获取所有样本的投影Z
					Project project = new Project();
					project = ProjectDao.doselectProject(conn);
					
					//用户id
					int[] userIds = project.getId();
					
					for(int i=0; i<userIds.length; i++)
					{
						System.out.println("第 "+ i + " 个userId:" + userIds[i]);
					}
					
					int peopleNum = userIds.length;
					//将peopleNum保存进单例中
					LdaFeatures.getInstance().setPeopleNum(peopleNum);
					
					//所有训练样例的投影Z
					double[][] modelP = project.getProject();
					
//					2.2张照片的投影（将拍到的图片，通过Wopt投影后,转成double[][]）
					double[][] testZ = new double[2][modelP[0].length];//[测试用例数量][C-1]
//					3.训练样例的投影（上面的modelP）
					//double[][] modelP
//					4.<2>的均值（从<2>处理）
					double[] testZMean = new double[testZ[0].length];
//					5.（投影Z的）N个人的，类内均值（每个人都有一个均值)
					int modelMeanSize;
					if( peopleNum == 1)
					{
						modelMeanSize = 1;
					}
					else
					{
						modelMeanSize = peopleNum - 1;
					}
					double[][] modelMean=new double[peopleNum][modelMeanSize];
//					6.（投影Z的）总体均值
					double[] allMean=new double[modelMeanSize];
					
					//2张照片的投影（将拍到的图片，通过Wopt投影后,转成double[][]）
					BufferedImage[] tempForTestBImages = new BufferedImage[2];
					for(int i=0; i<2; i++)
					{
						Image img = imageIconCaptures[i].getImage();
						tempForTestBImages[i] = ImageUtil.ImageToBufferedImage(img);
					}
					
					//然后，对tempForTestBImages进行小波变换，转成BufferedImage[2]
					BufferedImage[] waveTestBImages = Wavelet.Wavelet(tempForTestBImages);
					
					//计算Z时，需要获取m
					//获取m，并保存进单例中
					double[] m = ProjectDao.doselectmean(conn);
					LdaFeatures.getInstance().setAveVector(m);
					
					//计算2张经小波变换的测试图waveTestBImages的投影Z
					for(int i=0; i<2; i++)
					{
						testZ[i]=LDA.getInstance().calZ(waveTestBImages[i]);
					}
					
					//4.<2>的均值（从<2>处理）
					for(int i=0; i<(modelMeanSize); i++)
					{
						for(int j=0; j<2; j++)
						{
							testZMean[i] += testZ[j][i];
						}
						testZMean[i] /= 2;
					}
					
					int photoNum = 5;
					//5.（投影Z的）N个人的，类内均值（每个人都有一个均值)
					//6.（投影Z的）总体均值
					for(int i=0;i<peopleNum;i++){
						for(int k=0;k<modelMeanSize;k++){
							for(int j=0;j<photoNum;j++){
								modelMean[i][k]+=modelP[photoNum*i+j][k];
							}
							allMean[k]+=modelMean[i][k];
							modelMean[i][k]/=photoNum;
						}			
					}
					
					for(int i=0;i<modelMeanSize;i++)
						allMean[i]/=peopleNum*photoNum;
					
					System.out.println("modelP[0][0]" + modelP[0][0]);
					
					//从数据库中获取"x(m)的转置"，再经过转置，变成"x"(每个图像的差值图像[像素][n])
					double[][] xAveDeviationTrans = ProjectDao.doselectPeoplemean(conn);//
					double[][] xAveDeviation = Features.matrixTrans(xAveDeviationTrans);
					
					
					//将每副图的数据保存进temp，再装入ArrayList中
					//再保存进单例中
					ArrayList<double[][]> arrResult = new ArrayList<double[][]>();//存放结果的数组
					int length = xAveDeviation.length;
					int n = xAveDeviation[0].length;
					
					//将xAveDeviation，转成ArrayList<double[][]> 存入单例中，以供马氏距离计算使用。
					for(int i=0; i<n; i++)
					{
						double[][] temp = new double[length][1];
						for(int j=0; j<length; j++)
						{
							temp[j][0] = xAveDeviation[j][i];
						}
						arrResult.add(temp);
					}
					LdaFeatures.getInstance().setAveDeviation(arrResult);
					
					//从数据库中获取"mi的转置"，再经过转置，变成"mi" (每类的差值图像 [像素][n/num])
					double[][] miTrans = ProjectDao.doselectclassmean(conn);//= LdaFeatures.getInstance().getAveDeviationEach();
					double[][] mi = Features.matrixTrans(miTrans);
					//将mi保存进单例中，以供马氏距离计算使用。
					LdaFeatures.getInstance().setAveDeviationEach(mi);
					

					
					//验证（尝试识别，识别失败则需要重新获取图片）
					int idFind = Mark.identify(testZ, modelP, testZMean, modelMean, allMean);
					
//					System.out.println("idFind" + idFind);
//					System.out.println("userIds[]:" + userIds[idFind-1]);
//					System.out.println("idFind:" + idFind);
					//若没找到
					if( idFind == -1 || idFind == 1)
					{
						JOptionPane.showMessageDialog(null, "识别失败!点击确定后,系统将重新截图。", "提示", JOptionPane.INFORMATION_MESSAGE);
						
//						for(int i=0; i<2; i++)
//						{
//							isImageIconSelected[i] = true;
//						}
						buttonCapture1.doClick();
						buttonCapture2.doClick();
						changePhoto = true;
					}
					else
					{
						System.out.println("根据识别，用户ID：" + userIds[idFind-1]);
						
						userIdForSign = userIds[idFind-1];
						
						JOptionPane.showMessageDialog(null, "识别成功", "提示", JOptionPane.INFORMATION_MESSAGE);
						
						//根据ID,找出该人的5张照片
						BufferedImage[] findImages = 
								UserDao.selectImageById(userIds[idFind-1],conn);
						
						//根据ID,找出用户名
						User findUser = UserDao.doLoginByImage(userIds[idFind-1], conn);
						userAccount = findUser.getUsername();
						
						//将BufferedImage[] 转成 ImageIcon[]
						userImageIcons = new ImageIcon[5];
						for(int i=0; i<5; i++)
						{
							int[] iconsPix = ImageUtil.getPixes(findImages[i]);
							
							Image temp = ImageUtil.getImgByPixels(128, 128, iconsPix); 
							
							userImageIcons[i] = new ImageIcon(temp);
						}
						
						
						//将5张照片中的第1张图片显示在labelResult中
						labelResult.setIcon(userImageIcons[0]);
						

						
						//设置“签到按钮”可以点击
						btnSignIn.setEnabled(true);
						
					}

				}
				catch(Exception e1)
				{
					e1.printStackTrace();
				}
				
			}
		});
		buttonStart.setFont(new Font("宋体", Font.PLAIN, 16));
		buttonStart.setBounds(175, 33, 105, 27);
		panelResultBox.add(buttonStart);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(27, 386, 372, 85);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel labelUserId = new JLabel("用户名：");
		labelUserId.setFont(new Font("华文行楷", Font.PLAIN, 16));
		labelUserId.setBounds(10, 13, 70, 18);
		panel.add(labelUserId);
		
		txtUserId = new JTextField();
		txtUserId.setFont(new Font("宋体", Font.PLAIN, 16));
		txtUserId.setColumns(10);
		txtUserId.setBounds(90, 10, 102, 21);
		panel.add(txtUserId);
		
		JLabel labelUserPwd = new JLabel("密码：");
		labelUserPwd.setFont(new Font("华文行楷", Font.PLAIN, 16));
		labelUserPwd.setBounds(10, 54, 60, 21);
		panel.add(labelUserPwd);
		
		JButton buttonLoginByIdPwd = new JButton("登陆");
		buttonLoginByIdPwd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//获取用户名和密码
				String userId = txtUserId.getText();
//				String userPwd = txtUserPwd.getText();
				String userPwd = passwordField.getText();
				
				User user = new User();
				user.setUsername(userId);
				user.setPassword(userPwd);
				
				try {
					conn = Oracle_Connect.getInstance().getConn();
					int userIdInDb = UserDao.doLogin(user, conn);
					
					System.out.println("根据登陆，用户ID:" + userIdInDb);
					
					//若查找成功
					if( userIdInDb != -1)
					{
						userIdForSign = userIdInDb;
						
						BufferedImage[] findImages = UserDao.selectImageById(userIdInDb, conn);
						 
						//将BufferedImage[] 转成 ImageIcon[]
						userImageIcons = new ImageIcon[5];
						for(int i=0; i<5; i++)
						{
							int[] iconsPix = ImageUtil.getPixes(findImages[i]);
							
							Image temp = ImageUtil.getImgByPixels(128, 128, iconsPix); 
							
							userImageIcons[i] = new ImageIcon(temp);
						}
							
							
						//将5张照片中的第1张图片显示在labelResult中
						labelResult.setIcon(userImageIcons[0]);
							
						//设置“签到按钮”可以点击
						btnSignIn.setEnabled(true);
						
						userAccount = userId;
					}
					else
					{
						JOptionPane.showMessageDialog(null, "账号或密码错误！", "提示", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		buttonLoginByIdPwd.setBounds(218, 4, 110, 35);
		panel.add(buttonLoginByIdPwd);
		
		JLabel lblNewLabel = new JLabel("根据用户名和密码登陆");
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 14));
		lblNewLabel.setBounds(202, 54, 140, 19);
		panel.add(lblNewLabel);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(90, 53, 102, 21);
		panel.add(passwordField);
		
		//首先，检验一下数据库中有没有人
		//即检验一下数据库中是否有WpotT参数
		try
		{
			conn = Oracle_Connect.getInstance().getConn();
			//若不存在WoptT，则给出提示，系统自动返回主界面
			if( ProjectDao.firstORnot(conn) == true)
			{
				JOptionPane.showMessageDialog(null, "数据库中尚无数据,请先注册!", "提示",  JOptionPane.INFORMATION_MESSAGE);
				
				this.buttonStart.setEnabled(false);
//				//关闭此窗口
//				frameSignIn.dispose();
//				//打开主窗口
//				MainUI.frameMainUI = new MainUI();
//				MainUI.frameMainUI.setVisible(true);
			}
		}
		catch(Exception e1)
		{
			e1.printStackTrace();
		}
		
		//开启摄像头
//		new HarrCascadeParserTask(this).start();
		cif = new  CameraInterfaceImpl(this);
		cif.getCamera();
		findTask = new FindFaceForCameraInterfaceImpl(this);
		findTask.start();
		
//		frameSignIn.dispose();
	}

	@Override
	public void onRefresh(Object... objects) {
		// TODO Auto-generated method stub
				Integer result = (Integer) objects[0];
				switch (result) {
				case VideoStreamTask.OPEN_CAMERA_SUCCESS:
					Component component = (Component) objects[1];
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
//						Icon icon = (Icon) img;
//						Image image = img;
						ImageIcon imgIcon = new ImageIcon(img);
						imgIcon = UiImageHandle.ImageHandle(imgIcon, 128, 128);
						
						if(changePhoto == true)
						{
							for(int i=0; i<2; i++)
							{
								if(isImageIconSelected[i] == true)
								{
									isImageIconSelected[i] = false;
									switch(i)
									{
										case 0:
											this.buttonCapture1.setIcon(imgIcon);
											this.imageIconCaptures[0] = imgIcon;
											break;
										case 1:
											this.buttonCapture2.setIcon(imgIcon);
											this.imageIconCaptures[1] = imgIcon;
											break;
									}
									
									if( i == 1)
									{
										changePhoto = false;
										System.out.println("changePhoto = false;");
									}
									
									requestNum--;
									//判断是否已经2张照片都捕获完，都捕获完则进行识别
									if(requestNum == 0)
									{
										changePhoto = false;
										System.out.println("changePhoto = false;");
										
//										//从数据库中读取所有样本数据和WoptT
//										//读取Wopt
//										
//										//开始识别
//										
////									//若识别成功
//										if( 识别成功 == true)
//										{
//											//设置“签到按钮”可以点击
//										}
//										//若失败识别，则重新捕获2张照片。
//										else
//										{
//											
//										}
									}
									
									break;
								}
							}
							

							
						}
						
					}
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
		//判断是否已经打钩
		//若未打钩
		try
		{
			if(isImageIconSelected[objectIndex] == false)
			{
				btn.setIcon(FrameWindow.drawNike(imageIconCaptures[objectIndex]));
				isImageIconSelected[objectIndex] = true;
				requestNum++;
			}
			//若已打钩
			else
			{
				btn.setIcon(imageIconCaptures[objectIndex]);
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
