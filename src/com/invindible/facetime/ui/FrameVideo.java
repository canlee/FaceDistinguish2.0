package com.invindible.facetime.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.UIManager;
import javax.swing.JLabel;

import org.omg.CORBA.Environment;

import com.invindible.facetime.algorithm.LDA;
import com.invindible.facetime.algorithm.VideoMark;
import com.invindible.facetime.algorithm.feature.GetPcaLda;
import com.invindible.facetime.model.FaceImage;
import com.invindible.facetime.model.VideoMarkModel;
import com.invindible.facetime.service.implement.FindFaceInterfaceImpl;
import com.invindible.facetime.service.implement.FindVideoFaceImpl;
import com.invindible.facetime.service.interfaces.FindFaceInterface;
import com.invindible.facetime.service.interfaces.FindVideoFaceInterface;
import com.invindible.facetime.task.init.HarrCascadeParserTask;
import com.invindible.facetime.task.interfaces.Context;
import com.invindible.facetime.util.Debug;
import com.invindible.facetime.util.image.ImageUtil;
import com.invindible.facetime.wavelet.Wavelet;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import com.invindible.facetime.ui.widget.image.ImagePanel;

public class FrameVideo extends JFrame implements Context {

	static FrameVideo frameVideo;
	private JPanel contentPane;
	private JTextField txtPath1;
//	private ImageIcon[] imageObjectsInVideo;//视频里找到的人,人数在窗体构造函数中初始化
//	private ImageIcon[] imageObjectToFind = new ImageIcon[9];//要找的对象，最多9人
//	private BufferedImage[] imageObjectsInVideoBuffImg;
//	private BufferedImage[] imageObjectToFindBuffImg = new BufferedImage[9];
	private ArrayList<VideoMarkModel> arrVideoMarkModel = new ArrayList<VideoMarkModel>();//存储9个对象的视频检测数据(包括原图、人脸出现的视频时间、人脸图)
	private VideoMarkModel tempVideoMarkModel = new VideoMarkModel();//临时保存一次视频人脸检测数据，以供距离对比
	
//	private ImageIcon[] 
	private BufferedImage[] objectsToFind;//9个要在视频中查找的对象
	private boolean[] isObjectsSelected;//对象是否被选中的标志
	private boolean[] isObjectsSatisfied;//视频中找到的对象，用户是否满意的标志
	private int objectsSelectedCount;//选中的对象的数量
	private BufferedImage buffImgNoObject;//无对象的图片
	private int objectsSatisfiedCount;//用户满意的对象数量
	private BufferedImage[] buffImgObjectsSatisfied;//用户满意的对象图片
	private String[] timeFoundSatisfied;//用户满意的对象图片在视频中的出现时间
	
	private static double[][] modelP;//训练样本的投影Z
	private static double[] allMean;
	
	private ImagePanel panelOriginalPicture;//识别成功后，将视频的原图截图记录在这里
	private ImagePanel panelFacePicture;//识别成功后，将视频中截取的人脸记录在这里
	private ImagePanel panelFaceInVideo;//视频中截取的人脸（亮灿部分）
	private ImagePanel panelFaceOriginalInObjects;//识别成功后，显示用户提供的对象原图的地方
	private JLabel lblUserFindId;
	private JLabel lblUserFindTime;
	
	
	private JLabel lblObject1Num;
	private JLabel lblObject2Num;
	private JLabel lblObject3Num;
	private JLabel lblPageNum;
	
	private int pageIndex;
	
	private JButton btnPageDown;
	private JButton btnPageUp;
	
//	private int[] userIdsInOriginal;//用户ID与训练样本中的对应位置
//									//由于训练样例需要拼接起来，假如用户插入的是对象4和对象5
//									//那么训练样例0对应对象4，样例1对应5
//									//userIdsInOriginal[0] = object4,userIdsInOriginal[1] = object5;
//									//userIdsInOriginal.length = objectsSelectedCount;
	
	private FindFaceInterface findTask;
	private FindVideoFaceInterface fvfi;
	
	private ImagePanel lblObject1;
	private ImagePanel lblObject2;
	private ImagePanel lblObject3;
	
	private String videoPath1 = "";
	private String videoPath2;
	private String videoPath3;

//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					FrameVideo frame = new FrameVideo();
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
	public FrameVideo() {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		setTitle("4.视频监视");
		
		pageIndex = 1;
		objectsToFind = new BufferedImage[9];
		
		for(int i=0; i<9; i++)
		{
			ImageIcon imageNoObject = new ImageIcon("Pictures/noObject.jpg");
			objectsToFind[i] = ImageUtil.ImageToBufferedImage(imageNoObject.getImage());
		}
		ImageIcon imageNoObject = new ImageIcon("Pictures/noObject.jpg");
		buffImgNoObject = ImageUtil.ImageToBufferedImage(imageNoObject.getImage());
		isObjectsSelected = new boolean[9];
		isObjectsSatisfied = new boolean[9];
		buffImgObjectsSatisfied = new BufferedImage[9];
		timeFoundSatisfied = new String[9];
		
//		//初始化arrVideoMarkModel
//		VideoMarkModel vmmTemp = new VideoMarkModel();
//		vmmTemp.setMark(-1);
//		double disMinMax = Double.MAX_VALUE;
//		double[] dis={disMinMax,disMinMax,disMinMax,disMinMax};
//		vmmTemp.setDis(dis);
//		for(int i=0; i<9; i++)
//		{
//			arrVideoMarkModel.add(vmmTemp);
//		}
		
		for(int i=0; i<9; i++)
		{
			//初始化arrVideoMarkModel
			VideoMarkModel vmmTemp = new VideoMarkModel();
			vmmTemp.setMark(-1);
			double disMinMax = Double.MAX_VALUE;
			double[] dis={disMinMax,disMinMax,disMinMax,disMinMax};
			vmmTemp.setDis(dis);
			
			arrVideoMarkModel.add(vmmTemp);
		}
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 915, 754);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panelVideo1 = new JPanel();
		panelVideo1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u89C6\u98911", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelVideo1.setBounds(39, 37, 287, 92);
		contentPane.add(panelVideo1);
		panelVideo1.setLayout(null);
		
		JButton btnPath1 = new JButton("选择视频1");
		btnPath1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ChooseWmvVideo(txtPath1, 1);
			}
		});
		btnPath1.setBounds(153, 47, 115, 23);
		btnPath1.setFont(new Font("宋体", Font.PLAIN, 14));
		panelVideo1.add(btnPath1);
		
		txtPath1 = new JTextField();
		txtPath1.setBounds(54, 16, 214, 21);
		panelVideo1.add(txtPath1);
		txtPath1.setColumns(10);
		
		JLabel lblPath1 = new JLabel("路径：");
		lblPath1.setBounds(10, 19, 45, 15);
		lblPath1.setFont(new Font("宋体", Font.PLAIN, 14));
		panelVideo1.add(lblPath1);
		
		JPanel panelVideos = new JPanel();
		panelVideos.setBorder(new TitledBorder(null, "\u89C6\u9891\u9009\u62E9", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelVideos.setBounds(6, 16, 385, 356);
		contentPane.add(panelVideos);
		panelVideos.setLayout(null);
		
		JPanel panelObjects = new JPanel();
		panelObjects.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u68C0\u6D4B\u5BF9\u8C61\u9009\u62E9", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelObjects.setBounds(432, 17, 456, 440);
		contentPane.add(panelObjects);
		panelObjects.setLayout(null);
		
		JPanel panelObject1 = new JPanel();
		panelObject1.setBounds(6, 17, 208, 184);
		panelObjects.add(panelObject1);
		panelObject1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u5BF9\u8C61 ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelObject1.setLayout(null);
		
		lblObject1 = new ImagePanel();
		lblObject1.setBounds(43, 14, 128, 128);
		panelObject1.add(lblObject1);
		
		JButton btnObject1 = new JButton("选择该对象");
		btnObject1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ChooseJpgPicture(lblObject1, 0);
			}
		});
		btnObject1.setFont(new Font("宋体", Font.PLAIN, 12));
		btnObject1.setBounds(3, 151, 95, 23);
		panelObject1.add(btnObject1);
		
		JButton btnCancleObject1 = new JButton("取消该对象");
		btnCancleObject1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				CancleChooseJpgPicture(lblObject1, 0);
			}
		});
		btnCancleObject1.setFont(new Font("宋体", Font.PLAIN, 12));
		btnCancleObject1.setBounds(108, 151, 95, 23);
		panelObject1.add(btnCancleObject1);
		
		lblObject1Num = new JLabel("1");
		lblObject1Num.setFont(new Font("宋体-方正超大字符集", Font.PLAIN, 16));
		lblObject1Num.setBounds(32, 0, 21, 15);
		panelObject1.add(lblObject1Num);
		
		JPanel panelObject2 = new JPanel();
		panelObject2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u5BF9\u8C61 ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelObject2.setLayout(null);
		panelObject2.setBounds(224, 17, 208, 184);
		panelObjects.add(panelObject2);
		
		lblObject2 = new ImagePanel();
		lblObject2.setBounds(43, 14, 128, 128);
		panelObject2.add(lblObject2);
		
		JButton btnObject2 = new JButton("选择该对象");
		btnObject2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ChooseJpgPicture(lblObject2, 1);
			}
		});
		btnObject2.setFont(new Font("宋体", Font.PLAIN, 12));
		btnObject2.setBounds(3, 151, 95, 23);
		panelObject2.add(btnObject2);
		
		JButton btnCalcleObject2 = new JButton("取消该对象");
		btnCalcleObject2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				CancleChooseJpgPicture(lblObject2, 1);
			}
		});
		btnCalcleObject2.setFont(new Font("宋体", Font.PLAIN, 12));
		btnCalcleObject2.setBounds(103, 151, 95, 23);
		panelObject2.add(btnCalcleObject2);
		
		lblObject2Num = new JLabel("2");
		lblObject2Num.setFont(new Font("宋体-方正超大字符集", Font.PLAIN, 16));
		lblObject2Num.setBounds(32, 0, 21, 15);
		panelObject2.add(lblObject2Num);
		
		JPanel panelObject3 = new JPanel();
		panelObject3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u5BF9\u8C61 ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelObject3.setLayout(null);
		panelObject3.setBounds(6, 212, 208, 184);
		panelObjects.add(panelObject3);
		
		lblObject3 = new ImagePanel();
		lblObject3.setBounds(43, 14, 128, 128);
		panelObject3.add(lblObject3);
		
		JButton btnObject3 = new JButton("选择该对象");
		btnObject3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChooseJpgPicture(lblObject3, 2);
				
			}
		});
		btnObject3.setFont(new Font("宋体", Font.PLAIN, 12));
		btnObject3.setBounds(3, 151, 95, 23);
		panelObject3.add(btnObject3);
		
		JButton btnCalcleObject3 = new JButton("取消该对象");
		btnCalcleObject3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				CancleChooseJpgPicture(lblObject3, 2);
			}
		});
		btnCalcleObject3.setFont(new Font("宋体", Font.PLAIN, 12));
		btnCalcleObject3.setBounds(108, 151, 95, 23);
		panelObject3.add(btnCalcleObject3);
		
		lblObject3Num = new JLabel("3");
		lblObject3Num.setFont(new Font("宋体-方正超大字符集", Font.PLAIN, 16));
		lblObject3Num.setBounds(32, 0, 21, 15);
		panelObject3.add(lblObject3Num);
		
		JButton btnTrain = new JButton("2.训练样本特征");
		btnTrain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				TrainSelectedObjects();
			}
		});
		btnTrain.setFont(new Font("宋体", Font.PLAIN, 14));
		btnTrain.setBounds(293, 394, 133, 25);
		panelObjects.add(btnTrain);
		
		JPanel panelPage = new JPanel();
		panelPage.setBounds(260, 213, 140, 133);
		panelObjects.add(panelPage);
		panelPage.setLayout(null);
		
		lblPageNum = new JLabel("第 [1] 页");
		lblPageNum.setBounds(25, 5, 83, 19);
		panelPage.add(lblPageNum);
		lblPageNum.setFont(new Font("宋体", Font.PLAIN, 16));
		
		btnPageUp = new JButton("上一页");
		btnPageUp.setEnabled(false);
		btnPageUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				pageIndex--;
				lblPageNum.setText("第 [" + pageIndex +"] 页");
				btnPageDown.setEnabled(true);
				//若已经到了第一页，则令“上一页”无法点击
				if(pageIndex == 1)
				{
					btnPageUp.setEnabled(false);
				}
				//根据页数，刷新ImagePanel
				RefreshImagePanel();
			}
		});
		btnPageUp.setBounds(25, 34, 83, 28);
		panelPage.add(btnPageUp);
		btnPageUp.setFont(new Font("宋体", Font.PLAIN, 14));
		
		btnPageDown = new JButton("下一页");
		btnPageDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				pageIndex++;
				lblPageNum.setText("第 [" + pageIndex +"] 页");
				btnPageUp.setEnabled(true);
				//若已经到了第3页，则令“下一页”无法点击
				if(pageIndex == 3)
				{
					btnPageDown.setEnabled(false);
				}
				//根据页数，刷新ImagePanel
				RefreshImagePanel();
			}
		});
		btnPageDown.setBounds(25, 82, 83, 28);
		panelPage.add(btnPageDown);
		btnPageDown.setFont(new Font("宋体", Font.PLAIN, 14));
		
		JButton button = new JButton("1.获取头像");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				GetFacesFromObjects();
				
				
			}
		});
		button.setFont(new Font("宋体", Font.PLAIN, 14));
		button.setBounds(293, 356, 133, 25);
		panelObjects.add(button);
		
		//视频查找人脸的线程
//		new HarrCascadeParserTask(this).start();
//		FindVideoFaceInterface fvfi = new FindVideoFaceImpl(this, "E:\\VideoTest\\testVideo.wmv", 0);
		//------------------------------------问题，视频如何定位----------------------------------
		
		JButton btnStartFindFace = new JButton("3.开始在视频中查找人脸");
		btnStartFindFace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//若视频的路径为空，则提示必须选择一个视频
				if(videoPath1.isEmpty())
				{
					JOptionPane.showMessageDialog(null, "尚未选择视频路径！请点击上方选择视频按钮！", "提示", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				
				if(objectsSelectedCount == 0)
				{
					JOptionPane.showMessageDialog(null, "尚未选择对象！请先选择对象！", "提示", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				
//				尚未训练
				if(modelP == null)
				{
					JOptionPane.showMessageDialog(null, "尚未训练！请在选择对象后，训练样本特征！", "提示", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				
				//确认询问是否开始查找人脸
				if( JOptionPane.YES_OPTION == 
				JOptionPane.showConfirmDialog(null, "确认开始查找人脸？这个过程可能会持续几分钟，系统开销较大。", "提示", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE))
				{
					//路径检测
//					if( 路径正确 )
					{
						//用户满意对象计数器清零
						objectsSatisfiedCount = 0;
						//用户满意对象图片清空
						buffImgObjectsSatisfied = new BufferedImage[9];
						//清空满意图时间
						timeFoundSatisfied = new String[9];
						for(int i=0; i<9; i++)
						{
							buffImgObjectsSatisfied[i] = buffImgNoObject;
							timeFoundSatisfied[i] = "无该对象";
						}
					
						//根据选择的路径
						//开始在视频中 查找人脸
//						fvfi = new FindVideoFaceImpl(FrameVideo.this, "C:\\VideoTest\\testVideo.wmv", 0);
						fvfi = new FindVideoFaceImpl(FrameVideo.this, videoPath1, 0);
//						FindVideoFaceInterface fvfi = new FindVideoFaceImpl(FrameVideo.this, txtPath1.getText(), 0);
						fvfi.findFace();
						
						
						//查找结束后，销毁查找线程的实例(fvfi.stop)
					
					}
//					else
//					{
//						JOptionPane.showMessageDialog(null, "视频路径选择错误！", "提示", JOptionPane.WARNING_MESSAGE	);
//					}
					
				}
			
			}
		});
		btnStartFindFace.setFont(new Font("宋体", Font.PLAIN, 14));
		btnStartFindFace.setBounds(199, 382, 187, 25);
		contentPane.add(btnStartFindFace);
		
		JButton btnReturn = new JButton("返回主界面");
		btnReturn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//要考虑线程暂停问题
				//应该给出用户提示
				try
				{
					findTask.stop();
					fvfi.stop();
				}
				catch(Exception e1)
				{
					e1.printStackTrace();
				}
				frameVideo.dispose();
				
				MainUI.frameMainUI = new MainUI();
				MainUI.frameMainUI.setVisible(true);
			}
		});
		btnReturn.setBounds(25, 422, 110, 35);
		contentPane.add(btnReturn);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u89C6\u9891\u68C0\u6D4B\u7ED3\u679C", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_4.setBounds(39, 467, 837, 245);
		contentPane.add(panel_4);
		panel_4.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(6, 17, 322, 203);
		panel_4.add(panel_1);
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u539F\u59CB\u622A\u56FE", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setLayout(null);
		
		panelOriginalPicture = new ImagePanel();
		panelOriginalPicture.setBounds(6, 17, 300, 180);
		panel_1.add(panelOriginalPicture);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(338, 33, 140, 152);
		panel_4.add(panel_3);
		panel_3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u89C6\u9891\u8BC6\u522B\u51FA\u6765\u7684\u4EBA\u8138", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.setLayout(null);
		
		panelFacePicture = new ImagePanel();
		panelFacePicture.setBounds(6, 17, 128, 128);
		panel_3.add(panelFacePicture);
		
		JLabel lblId = new JLabel("找到的用户ID：");
		lblId.setBounds(510, 183, 93, 15);
		panel_4.add(lblId);
		
		lblUserFindId = new JLabel("尚未识别");
		lblUserFindId.setBounds(547, 205, 81, 15);
		panel_4.add(lblUserFindId);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u89C6\u9891\u4E2D\u622A\u53D6\u4FE1\u606F", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(676, 33, 140, 152);
		panel_4.add(panel_2);
		panel_2.setLayout(null);
		
		panelFaceInVideo = new ImagePanel();
		panelFaceInVideo.setBounds(6, 17, 128, 128);
		panel_2.add(panelFaceInVideo);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u8BC6\u522B\u51FA\u6765\u7684\u7528\u6237\u539F\u56FE", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_5.setBounds(500, 33, 140, 152);
		panel_4.add(panel_5);
		panel_5.setLayout(null);
		
		panelFaceOriginalInObjects = new ImagePanel();
		panelFaceOriginalInObjects.setBounds(6, 17, 128, 128);
		panel_5.add(panelFaceOriginalInObjects);
		
		JLabel lblTime = new JLabel("找到的时间：");
		lblTime.setBounds(338, 183, 93, 15);
		panel_4.add(lblTime);
		
		lblUserFindTime = new JLabel("尚未识别");
		lblUserFindTime.setBounds(361, 205, 117, 15);
		panel_4.add(lblUserFindTime);
		
		JButton btnStopFindFace = new JButton("(可选)4.停止视频检测");
		btnStopFindFace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(JOptionPane.YES_OPTION == 
						JOptionPane.showConfirmDialog(null, "确定要暂停线程？", "暂停提示", JOptionPane.YES_NO_CANCEL_OPTION))
				{
					try
					{
						//暂停找脸线程和识别线程
						findTask.stop();
						fvfi.stop();
						
						//初始化视频检测数据(将已检测的数据清零)
						arrVideoMarkModel = new ArrayList<VideoMarkModel>();
						for(int i=0; i<9; i++)
						{
							//初始化arrVideoMarkModel
							VideoMarkModel vmmTemp = new VideoMarkModel();
							vmmTemp.setMark(-1);
							double disMinMax = Double.MAX_VALUE;
							double[] dis={disMinMax,disMinMax,disMinMax,disMinMax};
							vmmTemp.setDis(dis);
							
							arrVideoMarkModel.add(vmmTemp);
						}
						JOptionPane.showMessageDialog(null, "已暂停视频检测！", "视频提示", JOptionPane.INFORMATION_MESSAGE);
						
						//若已寻找的满意对象>0
						if( objectsSatisfiedCount > 0)
						{
							//询问用户是否要查看满意的检测结果
							if( JOptionPane.YES_OPTION == 
									JOptionPane.showConfirmDialog(null, "是否要查看已检测的满意结果？",
											"检测结果提示", JOptionPane.YES_NO_CANCEL_OPTION))
							{
								ShowSatisfiedPicture.frameShowSatisfiedPicture 
									= new ShowSatisfiedPicture(objectsSatisfiedCount, buffImgObjectsSatisfied, isObjectsSatisfied, timeFoundSatisfied, objectsToFind);
								ShowSatisfiedPicture.frameShowSatisfiedPicture.setVisible(true);
							
							}
						
						}
					}
					catch(Exception e1)
					{
						JOptionPane.showMessageDialog(null, "检测线程尚未开始！无须暂停！", "提示", JOptionPane.INFORMATION_MESSAGE);
					}
				}
				
			}
		});
		btnStopFindFace.setFont(new Font("宋体", Font.PLAIN, 14));
		btnStopFindFace.setBounds(199, 432, 187, 25);
		contentPane.add(btnStopFindFace);
		
		//根据页数，刷新页面
		RefreshImagePanel();
	}

	@Override
	public void onRefresh(Object... objects) {
		// TODO Auto-generated method stub
		switch ((Integer) objects[0]) {
		
		case FindVideoFaceInterface.FIND_VIDEO_FACE_SUCCESS:
			FaceImage fi = (FaceImage) objects[1];
			
			//尝试识别 截取到的所有人脸
			//若找到相应方法，则保存进结果中
			tryToDistinguish(fi);
			
			
			
//			Debug.print("现在时间：" + fi.getTime());
//			originPanel.setBufferImage(fi.getOriginImage());
//			originPanel.setSize(fi.getOriginImage().getWidth(), fi.getOriginImage().getHeight());
//			imagePanel.setLocation(fi.getOriginImage().getWidth() + 10, 0);
//			BufferedImage img = ImageUtil.getImgByRGB(fi.getFacesRgb().get(0).getRgbMat());
//			imagePanel.setBufferImage(img);
//			imagePanel.setSize(img.getWidth(), img.getHeight());
			break;
			
		case FindVideoFaceInterface.FIND_VIDEO_FACE_FINISH:
			Debug.print("完成了");
			JOptionPane.showMessageDialog(null, "视频已检测完毕！", "视频提示", JOptionPane.INFORMATION_MESSAGE);
			break;
			
		case FindVideoFaceInterface.OPEN_VIDEO_FAIL:
			Debug.print("打开视频失败");
			break;
		
			//获取人脸
		case FindFaceInterface.FIND_FACE_SUCCESS:
			FaceImage fiFace = (FaceImage) objects[1];
			if(fiFace.getFacesRgb().size() > 0) {
				BufferedImage img = ImageUtil.getImgByRGB(fiFace.getFacesRgb().get(0).getRgbMat());
				//等比例处理
				img = ImageUtil.imageScale(img, 128, 128);
				
				
				//获取id(此处Time用作index，来标示用户ID)
				long id =fiFace.getTime();
				
				//将获取的头像设置回对象原图位置
//				imageObjectToFindBuffImg[(int) id] = img;
				objectsToFind[(int) id] = img;
				
				RefreshImagePanel();
//				switch((int) id)
//				{
//					case 0:
//						lblObject1.setBufferImage(img);
//						break;
//					case 1:
//						lblObject2.setBufferImage(img);
//						break;
//					case 2:
//						lblObject3.setBufferImage(img);
//						break;
//					default:
//						break;
//				}
				
//				imagePanel.setBufferImage(img);
//				Debug.print(img.getWidth() + ", " + img.getHeight());
//				imagePanel.setBounds(originPanel.getWidth() + 10, 0, img.getWidth(), img.getHeight());
			}
			else {
				//获取id(此处Time用作index，来标示用户ID)
				long id = fiFace.getTime();
				JOptionPane.showMessageDialog(null, "对象[" + (id+1) + "]的图片中无法找到人脸或更细致的人脸！", "提示", JOptionPane.INFORMATION_MESSAGE);
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

	/**
	 * 尝试识别 截取到的所有人脸
	 * @param fi
	 */
	private void tryToDistinguish(FaceImage fi) {
		// TODO Auto-generated method stub
		
		int findFaceListSize = fi.getFacesRgb().size();
		
		//(将所有找到的人脸，分别拿去小波变换，进行识别)
		
		//将找到的所有人脸图，分别进行等比例变换 
		BufferedImage[] waveBeforeBuffImgs = new BufferedImage[findFaceListSize];
		for(int i=0; i<findFaceListSize; i++)
		{
			//获取人脸图
			waveBeforeBuffImgs[i] = ImageUtil.getImgByRGB(fi.getFacesRgb().get(i).getRgbMat());
			
			//等比例变换
			waveBeforeBuffImgs[i] = ImageUtil.imageScale(waveBeforeBuffImgs[i], 128, 128);
			panelFaceInVideo.setBufferImage(waveBeforeBuffImgs[i]);
//			JOptionPane.showMessageDialog(null, "第" + i +"张图", "提示", JOptionPane.INFORMATION_MESSAGE);
		}
		
		//对所有等比例变换后的人脸图，进行小波变换
		BufferedImage[] waveAfterBuffImgs = Wavelet.Wavelet(waveBeforeBuffImgs);
		
		for(int i=0; i<findFaceListSize; i++)
		{
			//找到一张人脸图以后
//			//对其进行等比例变换
//			BufferedImage waveTempBuffImg = 
//					ImageUtil.imageScale(fi.getFacesRgb().get(i), 128, 128);
//			
//			//将人脸图进行小波变换
//			waveTempBuffImg = Wavelet.Wavelet(fi.getFacesRgb().get(0));
			
			//计算这张图片的投影Z
			double[] tempZ = LDA.getInstance().calZ(waveAfterBuffImgs[i]);
			//将tempZ的维数扩大
			double[][] testZ = new double[1][tempZ.length];
			for(int j=0; j<tempZ.length; j++)
			{
				testZ[0][j] = tempZ[j]; 
			}
			
			//开始识别
			//若找不到，则vmm为空
			VideoMarkModel vmm = VideoMark.mark(testZ, modelP, allMean);
			
			//首先，判断vmm是否为空（即test测试样例，与model训练样例不匹配
			//若为空，则说明识别不匹配，直接对下一个进行识别
			if( vmm == null)
			{
				continue;
			}
			else
			{
				//根据识别结果的ID号，保存进ArrayList<VideoMarkModel>相应的位置中
				//若 新识别的距离 < 原识别结果的距离，则替换原识别结果
				//若找到的ID不是1，即不是酱油，则显示
				//若用户对该对象不满意，则显示
				if ( (vmm.getMark()!=1) && (!IsSatisFied(vmm)) && CompareDistance(vmm)  )
				{
					
					//暂时将图片显示在界面上
					//原图
					BufferedImage originalBImg = fi.getOriginImage();
					originalBImg = ImageUtil.imageScale(originalBImg, 300, 180);
//					panelOriginalPicture.setBufferImage(fi.getOriginImage());
					panelOriginalPicture.setBufferImage(originalBImg);
					//人脸图(视频中识别出来的人脸，记录在这里)
					panelFacePicture.setBufferImage(waveBeforeBuffImgs[i]);
					//用户ID提示
					lblUserFindId.setText("对象[ " + (vmm.getMark()-1) + " ]");
					//用户找到的视频时间显示：
					long hour = fi.getTime() / 1000 / 60 / 60 % 60;
					long minute = fi.getTime() / 1000 / 60 %60;
					long second = fi.getTime() / 1000 % 60;
					String timeFound = "[ " + hour + "时 "+ minute + "分  " + second + "秒 ]";
					lblUserFindTime.setText( timeFound);
					//用户提供的查找对象的原图显示
					panelFaceOriginalInObjects.setBufferImage(objectsToFind[vmm.getMark()-2]);//vmm.getMark()最小为1,1为酱油，故-2才是所找目标
					
					//将距离，保存进arrVideoMarkModel中
//					int objectIndex = vmm.getMark();
					int objectIndex = vmm.getMark()-2;
					arrVideoMarkModel.get(objectIndex).setMark(objectIndex);
					arrVideoMarkModel.get(objectIndex).setDis(vmm.getDis());
					
					System.out.println("-----------------------===成功找到一个人！===------------------------------");
//					JOptionPane.showMessageDialog(null, "成功识别对象["+ (vmm.getMark()-1) +"]！\n\r" + "视频截取时间：" +
//							"[ " + hour + "时 "+ minute + "分  " + second + "秒 ]"
//							, "视频识别提示", JOptionPane.INFORMATION_MESSAGE);
					
					//询问用户是否满意
					//若满意，则停止在视频中对该目标的查找
					if( JOptionPane.YES_OPTION == 
							JOptionPane.showConfirmDialog(null, "成功识别对象["+ (vmm.getMark()-1) +"]！\n\r" + "视频截取时间：" +
							"[ " + hour + "时 "+ minute + "分  " + second + "秒 ]\n\r" +
							"该结果是否满意？（是，视频将不再检测该对象）"
							, "视频识别提示", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE))
					{
//						isObjectsSatisfied[vmm.getMark()-2] = true;
						//标记用户对该对象已满意
						isObjectsSatisfied[objectIndex] = true;
						//用户满意计数器+1
						objectsSatisfiedCount++;
						//获取满意的对象图片
						buffImgObjectsSatisfied[objectIndex] = waveBeforeBuffImgs[i];
						//获取对象截图时间
						timeFoundSatisfied[objectIndex] = timeFound;
						
						
						//若已满意对象数 等于 要找的对象数量，则停止搜索
						if( objectsSatisfiedCount == objectsSelectedCount)
						{
							//寻找线程暂停
							findTask.stop();
							fvfi.stop();
							
							//给出提示
							JOptionPane.showMessageDialog(null, "全部对象已找到！", "提示", JOptionPane.INFORMATION_MESSAGE);
						
							//将所有找到的对象，显示在新窗口上
							//满意的对象数，满意图，满意标志，找到的时间，原图
//							showSatisfiedPictures(objectsSatisfiedCount, buffImgObjectsSatisfied, isObjectsSatisfied,timeFound,objectsToFind);
//							显示
							ShowSatisfiedPicture.frameShowSatisfiedPicture 
								= new ShowSatisfiedPicture(objectsSatisfiedCount,buffImgObjectsSatisfied,isObjectsSatisfied,timeFoundSatisfied,objectsToFind);
							ShowSatisfiedPicture.frameShowSatisfiedPicture.setVisible(true);
							
						}
					}
					
				}
				//否则，若 ... > ...，则不替换结果
				else
				{
					continue;
				}
			}
			
		}
		
	}

	
	/**
	 * 查看验证的对象，是不是用户已经满意的对象
	 * 若满意，则停止查找
	 * @param vmm
	 * @return
	 */
	private boolean IsSatisFied(VideoMarkModel vmm) {
		// TODO Auto-generated method stub
		
		int objectIndex = vmm.getMark() - 2;
		//若用户已满意，则返回true
		if( isObjectsSatisfied[objectIndex] == true)
		{
			return true;
		}
		{
			return false;
		}
		
	}

	/**
	 * 根据传进来的临时识别ID
	 * 去arrVideoMarkModel中查找第id个，并比较其距离
	 * 若 临时数据（共4个距离）中有3个距离 均小于 原数据中的3个距离，则返回true，代表可替换
	 * 否则，返回false，代表不可替换
	 */
	private boolean CompareDistance(VideoMarkModel vmm)
	{
		//临时距离 小于 原距离 的计数器([0,4])
		int lessCount = 0;
		
		//临时数据符合第objectIndex个对象
		//( vmm.getMark() 最小为1，但arrVideoMarkModel最小为0，故-1)
		//"1-3"为酱油，"4"为arrVideoMarkModel中的"0"
		System.out.println("vmm.getMark():  " + vmm.getMark());
		
//		int objectIndex =vmm.getMark()-4;
		int objectIndex =vmm.getMark()-2;
//		int objectIndex =vmm.getMark()-1;
		
		//获取原数据
		VideoMarkModel originalVmm = arrVideoMarkModel.get(objectIndex);
		
		//比较 临时数据 与 原数据 的4个距离
		double[] originalDis = originalVmm.getDis();//原数据的4个距离
		double[] newDis = vmm.getDis();//临时数据的4个距离
		System.out.println("===========比较数据:============");
		for(int i=0; i<4; i++)
		{
			System.out.print("原: " + originalDis[i] + " 新: " + newDis[i]);
			if( newDis[i] < originalDis[i])
			{
				lessCount++;
			}
		}
		System.out.println("=============================");
		
		//若有3个或3个以上距离 更小，说明临时数据结果更符合该识别对象
		//可以用临时数据替换原数据，返回true

		System.out.println("lessCount:" + lessCount);
		if( lessCount >= 4)
//		if( lessCount >= 3)
		{
			originalVmm.setMark(objectIndex);//可有可无
			originalVmm.setDis(newDis);
			return true;
		}
		else
		{
			return false;
		}
		
	}
	
//	/**
//	 * 查看指定用户是否已创立VideoMarkModel，并加入arrVideoMarkModel中的方法
//	 * @return
//	 */
//	private boolean ObjectVideoMarkModelExist(int id) {
//		// TODO Auto-generated method stub
//		
//		//若存在，则返回true
//		if( id <= arrVideoMarkModel.size() )
//		{
//			return true;
//		}
//		else
//		{
//			return false;
//		}
//	}
	
	/**
	 * 对选中的对象进行训练
	 */
	private void TrainSelectedObjects()
	{
		if( objectsSelectedCount == 0)
		{
			JOptionPane.showMessageDialog(null, "对象为空，无法训练！请先选择对象！", "提示", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		
		//计算前，先设置GetPcaLda的每人照片数量
		GetPcaLda.setNum(1);
		
		//选中对象的初始化，对他们进行小波变换
		//由于加入“酱油”（放在第1位），故人数要+1
		BufferedImage[] waveBeforeObjectBImages = new BufferedImage[objectsSelectedCount+1];
		
		System.out.println("objectsSelectedCount:" + objectsSelectedCount);
		//获取酱油的1张图片
		ImageIcon imgIcon = new ImageIcon("Pictures/none/after37-1.jpg");
		waveBeforeObjectBImages[0] = 
				ImageUtil.ImageToBufferedImage(imgIcon.getImage());
		//获取3张酱油的图片
//		for(int i=0; i<3; i++)
//		{
//			System.out.println("i:" + i);
//			String source = "Pictures/none/after37-" + (i+1) + ".jpg";
//			ImageIcon imgIcon = new ImageIcon(source);
//			waveBeforeObjectBImages[i] = 
//					ImageUtil.ImageToBufferedImage(imgIcon.getImage());
//		}
		
		
		int index = 1;// 第0个 给了“酱油”
		for(int i=0; i<9; i++)
		{
			//若该对象被选中，则添加到waveObjectBImages里面
			if( isObjectsSelected[i] == true)
			{
//				System.out.println("i:" + i);
//				System.out.println("！！！！！！！！进来一次！！！！！！！！！！！");
				waveBeforeObjectBImages[index] = objectsToFind[i];
				index++;
			}
		}
		
		//对获取的选中对象的图片进行小波变换
		BufferedImage[] waveAfterObjectBImages = Wavelet.Wavelet(waveBeforeObjectBImages);
		
		//训练（将 选中对象的图片 投影到WoptT上)
		GetPcaLda.getResult(waveAfterObjectBImages);
		
		//计算 选中对象的图片的 投影Z	
		int peopleNum = objectsSelectedCount+1;//人数（+3为酱油）
		int photoNum = 1;//每人1张图
		modelP=new double[peopleNum*photoNum][peopleNum-1];
		for(int i=0;i<peopleNum;i++){
			modelP[i]=LDA.getInstance().calZ(waveAfterObjectBImages[i]);//投影
		}
		
//		//（投影Z的）总体均值
		allMean=new double[peopleNum-1];
		
		for(int i=0; i<peopleNum - 1; i++) //遍历维数[C-1]
		{
			for(int j=0; j<peopleNum; j++) //遍历人数
			{
				allMean[i] += modelP[j][i]; 
			}
			allMean[i] /= peopleNum;
		}
		
		JOptionPane.showMessageDialog(null, "样本训练完毕!", "提示", JOptionPane.INFORMATION_MESSAGE);
	
		//计算后，将GetPcaLda的每人照片数量设置回默认的"5"
		GetPcaLda.setNum(5);
	}
	
	/**
	 * 从用户提供的图片中获取头像
	 */
	private void GetFacesFromObjects()
	{
		//-------------------------------测试阶段，尝试对2张人脸进行搜索-（这一部分应由界面给出)------------------------------
		
		if(objectsSelectedCount == 0)
		{
			JOptionPane.showMessageDialog(null, "没有对象图片！请先提供对象图片！", "提示", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		findTask = new FindFaceInterfaceImpl(this);
		findTask.start();
		
		
		//将空余的位置顺位补齐
		//比如：用户选择了对象2、4，则自动补齐成对象1、2.
		int index = 0;
		for(int i=0; i<9; i++)
		{
			if( isObjectsSelected[i] == false)
			{
				for(int j=i; j<9; j++)
				{
					if( isObjectsSelected[j] == true)
					{
						System.out.println("交换时:i=" + i + ",j=" + j);
						objectsToFind[i] = objectsToFind[j];
						
						//改变选中的标志
						isObjectsSelected[i] = true;
						isObjectsSelected[j] = false;
						
						//将第j张图片清空
						objectsToFind[j] = buffImgNoObject;
						
						//改变ImagePanel的显示
//						ChangeImagePanelPic(i,j);
						
						break;
					}
				}
			}
		}
		//自动转到第一页，并且刷新页面
		pageIndex = 1;
		lblPageNum.setText("第 [1] 页]");
		btnPageUp.setEnabled(false);
		btnPageDown.setEnabled(true);
		RefreshImagePanel();
		
		
		for(int i=0; i<9; i++)
		{
			//若对象i选中了，则在视频中开始查找对象i
			if( isObjectsSelected[i] == true)
			{
				//第二个参数i，本来是time，在此处当做id使用
				findTask.findFace(objectsToFind[i], i);
			}
		}
		
//		//等比例缩放成128*128
//		objectsToFind[0] = ImageUtil.imageScale(objectsToFind[0], 128, 128);
//		objectsToFind[1] = ImageUtil.imageScale(objectsToFind[1], 128, 128);
		
		
		//-------------------------------测试阶段，尝试对2张人脸进行搜索-------------------------------
		
	}
	
	/**
	 * 改变ImagePanel的显示
	 * 把第j个panel的图，显示在第i个上
	 * 同时第j个panel的图清空
	 */
	private void ChangeImagePanelPic(int to, int from)
	{
		//清空第j个panel的图
		switch(from)
		{
			case 0:
				lblObject1.setBufferImage(null);
				break;
			case 1:
				lblObject2.setBufferImage(null);
				break;
			case 2:
				lblObject3.setBufferImage(null);
				break;
			default :
				break;
		}
		
		//将第j个图复制给第i个
		//由于在调用此方法前，已经将第j个的图移至第i个，故直接赋值自己的即可
		switch(to)
		{
			case 0:
				lblObject1.setBufferImage(objectsToFind[0]);
				break;
			case 1:
				lblObject2.setBufferImage(objectsToFind[1]);
				break;
			case 2:
				lblObject3.setBufferImage(objectsToFind[2]);
			break;
			default :
				break;
		}
	}
	
	/**
	 * 选择视频功能
	 * 将选中的视频记录在内存中，并且显示在对应JTextField上
	 * 第一个参数代表要显示的JTextField
	 * 第二个参数代表视频i，[1,3]
	 */
	private void ChooseWmvVideo(JTextField txtField, int i)
	{
		JFileChooser chooser=new JFileChooser() ;
		chooser.setMultiSelectionEnabled(false);//禁止多选
//		chooser.setFileFilter(new FileFilter());
//		chooser.setFileFilter(new FileFilter("java"));//设置过滤器，仅限jpg(jpeg)图   
		//若选择了一个文件，则获取路径，并且将该路径的图片获取出来
		try
		{
			if( JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(FrameVideo.this))
			{
				//获取选择路径
				String pathVideoObject = chooser.getSelectedFile().getPath();
				System.out.println(pathVideoObject);
				//将路径显示在JTextField上
				txtField.setText(pathVideoObject);
				//将视频赋值给静态变量
				switch(i)
				{
					case 1:
						videoPath1 = pathVideoObject;
						break;
					case 2:
						videoPath2 = pathVideoObject;
						break;
					case 3:
						videoPath2 = pathVideoObject;
						break;
					default :
						break;
				}
			}
			else
			{
				return;
			}
		}
		catch(Exception e1)
		{
			JOptionPane.showMessageDialog(null, "你选择的图片不正确!请检查格式!", "提示", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	/**
	 * 为对象选择图片功能
	 * 将选择的图片显示在对应ImagePanel上
	 * 第一个参数代表要该图要显示的ImagePanel
	 * 第二个参数代表这张图在Buffered[] objectsToFind中的位置,[0,8]
	 */
	private void ChooseJpgPicture(ImagePanel imagePanel, int i)
	{
		JFileChooser chooser=new JFileChooser() ;
		chooser.setMultiSelectionEnabled(false);//禁止多选
//		chooser.setFileFilter(new FileFilter());
//		chooser.setFileFilter(new FileFilter("java"));//设置过滤器，仅限jpg(jpeg)图   
		//若选择了一个文件，则获取路径，并且将该路径的图片获取出来
		try
		{
			if( JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(FrameVideo.this))
			{
				//获取选择路径
				String pathObject = chooser.getSelectedFile().getPath();
				System.out.println(pathObject);
				//获取给objectsToFind
				ImageIcon imageIcon = new ImageIcon(pathObject);
				int index = (pageIndex - 1) * 3 + i;//根据页数，据顶objectsToFind的索引index
				objectsToFind[index] = ImageUtil.ImageToBufferedImage(imageIcon.getImage());
				//将图像显示在ImagePanel上
				imagePanel.setBufferImage(objectsToFind[index]);
				
				if( isObjectsSelected[index] == false)
				{
					objectsSelectedCount++;
					isObjectsSelected[index] = true;
				}
			}
			else
			{
				return;
			}
		}
		catch(Exception e1)
		{
			JOptionPane.showMessageDialog(null, "你选择的图片不正确!请检查格式!", "提示", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	/**
	 * 取消选中的对象的图片
	 * 并且将其对应 已选中标志：isObjectsSelected[i]设置回false
	 * 选中对象计数器objectsSelectedCount--
	 * (objectToFind[i]要清楚)
	 */
	private void CancleChooseJpgPicture(ImagePanel imagePanel, int i)
	{
		//若已选中，则取消选中
		int index =(pageIndex - 1) * 3 + i;
		if( isObjectsSelected[index] == true)
		{
			isObjectsSelected[index] = false;
			objectsSelectedCount--;
			objectsToFind[index] = buffImgNoObject;
			imagePanel.setBufferImage(buffImgNoObject);
		}
		else
		{
			return;
		}
	}
	
	/**
	 * 根据目前页数，刷新对象图片显示的方法
	 */
	private void RefreshImagePanel()
	{
//		int i = pageIndex - 1;
		System.out.println("pageNum:" + pageIndex);
//		System.out.println("i*3:" + i*3);
		switch (pageIndex) 
		{
			
			case 1:
				lblObject1.setBufferImage(objectsToFind[0]);
				lblObject1Num.setText("1");
				lblObject2.setBufferImage(objectsToFind[1]);
				lblObject2Num.setText("2");
				lblObject3.setBufferImage(objectsToFind[2]);
				lblObject3Num.setText("3");
				break;
	
			case 2:
				lblObject1.setBufferImage(objectsToFind[3]);
				lblObject1Num.setText("4");
				lblObject2.setBufferImage(objectsToFind[4]);
				lblObject2Num.setText("5");
				lblObject3.setBufferImage(objectsToFind[5]);
				lblObject3Num.setText("6");	
				break;
			case 3:
				lblObject1.setBufferImage(objectsToFind[6]);
				lblObject1Num.setText("7");
				lblObject2.setBufferImage(objectsToFind[7]);
				lblObject2Num.setText("8");
				lblObject3.setBufferImage(objectsToFind[8]);
				lblObject3Num.setText("9");
				break;
				
			default:
				break;
		}
		
	}
	
	/**
	 * 调用新窗口
	 * 显示所有已找到的，用户满意的图片
	 */
	private void showSatisfiedPictures(int objectsSatisfiedCount,BufferedImage[] buffImgObjectsSatisfied,boolean[] isObjectsSatisfied,String[] timeFound,BufferedImage[] objectsToFind)
	{
		
	}
	
}
