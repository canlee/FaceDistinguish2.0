package com.invindible.facetime.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.invindible.facetime.database.Oracle_Connect;
import com.invindible.facetime.database.UserDao;
import com.invindible.facetime.model.User;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JPasswordField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class FrameIdPwd extends JFrame {

	static FrameIdPwd frameIdPwd;
	private JPanel contentPane;
	private JTextField txtUserId;
	private JPasswordField passwordField;
	private JButton buttonEnter;

//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					frameIdPwd = new FrameIdPwd();
//					frameIdPwd.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public FrameIdPwd() {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					buttonEnter.doClick();
				}
			}
		});
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		setTitle("1.用户注册");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 341, 257);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(20, 132, 281, 78);
		contentPane.add(panel);
		panel.setLayout(null);
		
		buttonEnter = new JButton("确认");

		
		buttonEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//判断用户名是否为空
				if(txtUserId.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null, "用户名不能为空！","警告", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				
				//判断密码是否为空
//				if(txtPassWd.getText().equals(""))
				if(passwordField.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null, "密码不能为空！", "警告", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				//获取用户名和密码
				String userId = txtUserId.getText();
//				String passWord = txtPassWd.getText();
				String passWord = passwordField.getText();
				
				//酱油的账号，不能注册
				if(userId.equals("none"))
				{
					JOptionPane.showMessageDialog(null, "该用户名已被注册！","警告", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				//数据库验证用户名是否存在
				//若用户已被注册（存在数据库中），则给提示已存在
				User user = new User();
				user.setUsername(userId);
				user.setPassword(passWord);
				
				Connection conn = null;
				try {
					conn = Oracle_Connect.getInstance().getConn();
					//若能注册,则打开FrameRegist窗口，进入下个注册阶段
					if ( UserDao.registerable( conn, user))
					{
						frameIdPwd.dispose();
//						frameIdPwd.setVisible(false);
						//将用户名和密码作为参数，传给下个窗体的构造函数
						FrameRegist.frameRegist = new FrameRegist(userId, passWord, user);
						FrameRegist.frameRegist.setVisible(true);
					}
					//若不能注册，则
					else
					{
						JOptionPane.showMessageDialog(null, "该用户名已被注册！","警告", JOptionPane.WARNING_MESSAGE);
						return;
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		buttonEnter.setBounds(10, 25, 110, 35);
		panel.add(buttonEnter);
		
		JButton buttonReturn = new JButton("返回");
		buttonReturn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameIdPwd.dispose();
				MainUI.frameMainUI = new MainUI();
				MainUI.frameMainUI.setVisible(true);
			}
		});
		buttonReturn.setBounds(170, 25, 110, 35);
		panel.add(buttonReturn);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(10, 26, 293, 96);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		txtUserId = new JTextField();
		txtUserId.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					buttonEnter.doClick();
				}
			}
		});
		txtUserId.setFont(new Font("宋体", Font.PLAIN, 16));
		txtUserId.setColumns(10);
		txtUserId.setBounds(124, 10, 102, 21);
		panel_1.add(txtUserId);
		
		JLabel label = new JLabel("用户名：");
		label.setFont(new Font("华文行楷", Font.PLAIN, 16));
		label.setBounds(44, 13, 70, 18);
		panel_1.add(label);
		
		JLabel label_1 = new JLabel("密码：");
		label_1.setFont(new Font("华文行楷", Font.PLAIN, 16));
		label_1.setBounds(44, 54, 60, 21);
		panel_1.add(label_1);
		
		passwordField = new JPasswordField();
		passwordField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					buttonEnter.doClick();
				}
				
			}
		});
		passwordField.setBounds(124, 53, 102, 21);
		panel_1.add(passwordField);
	}
}
