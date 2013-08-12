package com.invindible.facetime.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.JSplitPane;
import javax.swing.JButton;

import com.invindible.facetime.algorithm.UiAlgorithm.UiImageHandle;
import com.invindible.facetime.database.ApplicationConfig;
import com.invindible.facetime.database.OnAndOff;
import com.invindible.facetime.database.OracleConfig;
import com.invindible.facetime.org.eclipse.wb.swing.FocusTraversalOnArray;
import com.invindible.facetime.service.implement.CameraInterfaceImpl;
import com.invindible.facetime.task.init.CopyDllTask;
import com.invindible.facetime.task.init.HarrCascadeParserTask;
import com.invindible.facetime.task.interfaces.Context;
import com.invindible.facetime.util.Debug;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.awt.Font;

public class MainUI extends JFrame implements Context{
	static JPanel contentPane;
	public static MainUI frameMainUI;
	private boolean isFirstTime = true;
	
	/**
	 * Launch the application. 
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frameMainUI = new MainUI();
					frameMainUI.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainUI() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		setTitle("\u4EBA\u8138\u8BC6\u522B\u7A0B\u5E8F");
		//setUndecorated(true);
		//getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		//getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
//		setExtendedState(JFrame.ICONIFIED);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 580, 383);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panelPic = new JPanel();
		panelPic.setBounds(0, 0, 564, 345);
		
		contentPane.add(panelPic);
		panelPic.setLayout(null);
		
		JPanel panelMessage = new JPanel();
		panelMessage.setBounds(268, 0, 247, 129);
		panelMessage.setOpaque(false);//设置成透明的
		panelPic.add(panelMessage);
		panelMessage.setLayout(null);
		
		JLabel label = new JLabel("请选择一个功能");
		label.setFont(label.getFont().deriveFont(label.getFont().getStyle() | Font.BOLD));
		label.setBounds(10, 57, 167, 35);
		panelMessage.add(label);
		
		JPanel panelButton = new JPanel();
		panelButton.setBounds(40, 216, 499, 97);
		panelButton.setOpaque(false);
		panelPic.add(panelButton);
		panelButton.setLayout(null);
		
		//启动数据库
		OnAndOff.getInstance().Start();
		
		
		JButton btnRegist = new JButton("1.注册");
		btnRegist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameMainUI.dispose();
//				frameMainUI.setVisible(false);
				try{
					FrameIdPwd.frameIdPwd = new FrameIdPwd();
					FrameIdPwd.frameIdPwd.setVisible(true);
				}
				catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		btnRegist.setBounds(56, 15, 115, 32);
		panelButton.add(btnRegist);
		
		final JButton btnEnter = new JButton("2.登陆签到");
		btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameMainUI.dispose();
//				frameMainUI.setVisible(false);
				FrameSignIn.frameSignIn = new FrameSignIn();
				FrameSignIn.frameSignIn.setVisible(true);
			}
		});
		btnEnter.setBounds(276, 15, 115, 32);
		panelButton.add(btnEnter);
		
		JButton btnVideo = new JButton("4.视频监视");
		btnVideo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameMainUI.dispose();
				
				FrameVideo.frameVideo = new FrameVideo();
				FrameVideo.frameVideo.setVisible(true);
			}
		});
		btnVideo.setBounds(276, 57, 115, 32);
		panelButton.add(btnVideo);
		
		JButton buttonManage = new JButton("3.管理员管理");
		buttonManage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				frameMainUI.dispose();
				
				//打开管理员登陆界面
				FrameManagerLogin.frameManagerLogin = new FrameManagerLogin();
				FrameManagerLogin.frameManagerLogin.setVisible(true);
				
			}
		});
		buttonManage.setBounds(56, 57, 115, 32);
		panelButton.add(buttonManage);
		
		JLabel lblWallPaper = new JLabel("New label");
		lblWallPaper.setBounds(0, 0, 564, 345);
		int num = (int) (Math.random()*4);//产生[0,4)的随机数.
		
		JPanel panelProgramIcon = new JPanel();
		panelProgramIcon.setBounds(91, 85, 159, 121);
		panelPic.add(panelProgramIcon);
		panelProgramIcon.setLayout(null);
		
		JLabel lblProgramIcon = new JLabel("");
		lblProgramIcon.setBounds(0, 0, 159, 121);
		lblProgramIcon.setIcon(UiImageHandle.ImageHandle(new ImageIcon("Pictures/facetime.jpg"), 159, 121));
		panelProgramIcon.add(lblProgramIcon);
		lblWallPaper.setIcon(UiImageHandle.ImageHandle(new ImageIcon("Pictures/wallpaper/" + num + ".jpg"), 564, 345));
		panelPic.add(lblWallPaper);
		contentPane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{panelMessage, panelPic, btnRegist, btnEnter, panelButton}));
		panelPic.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{panelMessage}));
		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{contentPane, panelPic, lblWallPaper, panelButton, btnRegist, btnEnter, panelMessage}));
	
		
		
		//查看是否是第一次运行
		while( true)
		{
			try {
				if (ApplicationConfig.first() == true)
				{
					String port = JOptionPane.showInputDialog(null, "请输入Oracle数据库端口（默认：1521）", "程序初始化配置-1",  JOptionPane.INFORMATION_MESSAGE);
	//				
					String dbName = JOptionPane.showInputDialog(null, "请输入Oracle数据库名称（默认：orcl）", "程序初始化配置-2",  JOptionPane.INFORMATION_MESSAGE);
					
					String managerId = JOptionPane.showInputDialog(null, "请输入Oracle数据库管理员用户名（默认：system）", "程序初始化配置-3",  JOptionPane.INFORMATION_MESSAGE);
					
					String managerPwd = JOptionPane.showInputDialog(null, "请输入Oracle数据库管理员密码", "程序初始化配置-4",  JOptionPane.INFORMATION_MESSAGE);
					
					//开始配置数据库
					ApplicationConfig.setupConfig(port, dbName);
					
					//建立连接
					ApplicationConfig.setupLink();
					
					//用管理员建立ai_face数据库用户
					OracleConfig.config(managerId, managerPwd);
					
					//为ai_face建立 数据库的表
					OracleConfig.configtable();
					
					System.out.println("数据库配置成功");
					//复制必要的.dll文件
					new CopyDllTask(this).start();
					
					break;
				}
				else
				{
					//建立数据库连接
					ApplicationConfig.setupLink();
					
					System.out.println("walk u");
					break;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
				
				File f=new File("oracle\\config");
				f.delete();
				JOptionPane.showMessageDialog(null, "数据库配置错误，无法连接数据库，请重新输入配置数据。", "警告", JOptionPane.WARNING_MESSAGE);
			}
		}
		
		//读取adaboost文件
		new HarrCascadeParserTask(MainUI.this).start();
	
	
	}

	@Override
	public void onRefresh(Object... objects) {
		// TODO Auto-generated method stub
		Integer result = (Integer) objects[0];
		switch (result) {
			case HarrCascadeParserTask.PARSER_SUCCESS:
				Debug.print("读取adaboost文件成功！");
				break;
				
			default:
				break;
		}
	}
	
//	//图片等比例处理方法,width和height为宽度和高度
//	public ImageIcon ImageHandle(ImageIcon imageicon,int width,int height){
//		Image image = imageicon.getImage();
//		Image smallimage = image.getScaledInstance(width, height, image.SCALE_FAST);
//		ImageIcon smallicon = new ImageIcon(smallimage);
//		return smallicon;
//	}
}
