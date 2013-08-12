package com.invindible.facetime.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class FrameManagerLogin extends JFrame {

	static FrameManagerLogin frameManagerLogin;
	private JPanel contentPane;
	private JTextField txtId;
	private JPasswordField passwordField;
	private JButton buttonEnter;

//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					FrameManagerLogin frame = new FrameManagerLogin();
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
	public FrameManagerLogin() {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		setTitle("3.管理员管理-登陆");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 350, 254);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(12, 21, 293, 96);
		contentPane.add(panel);
		
		txtId = new JTextField();
		txtId.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					buttonEnter.doClick();
				}
				
			}
		});
		txtId.setFont(new Font("宋体", Font.PLAIN, 16));
		txtId.setColumns(10);
		txtId.setBounds(124, 10, 102, 21);
		panel.add(txtId);
		
		JLabel labelId = new JLabel("管理员账号：");
		labelId.setFont(new Font("华文行楷", Font.PLAIN, 16));
		labelId.setBounds(10, 13, 104, 18);
		panel.add(labelId);
		
		JLabel labelPwd = new JLabel("密码：");
		labelPwd.setFont(new Font("华文行楷", Font.PLAIN, 16));
		labelPwd.setBounds(44, 54, 60, 21);
		panel.add(labelPwd);
		
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
		panel.add(passwordField);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBounds(22, 127, 281, 78);
		contentPane.add(panel_1);
		
		buttonEnter = new JButton("确认");
		buttonEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//获取账号和密码
				String id = txtId.getText();
//				String pwd = txtPwd.getText();
				String pwd = passwordField.getText();
				
				//若账号和密码正确，则进入管理员主界面
				if( id.equals("admin") && pwd.equals("administrator"))
				{
					frameManagerLogin.dispose();
					
					//打开管理员主界面
					FrameManagerMainUI.frameManagerMainUI = new FrameManagerMainUI();
					FrameManagerMainUI.frameManagerMainUI.setVisible(true);
				}
				else 
				{
					if( id.equals("") || pwd.equals(""))
					{
						JOptionPane.showMessageDialog(null, "账号或密码不能为空！", "警告", JOptionPane.WARNING_MESSAGE);
					}
					else if( !id.equals("admin") )
					{
						JOptionPane.showMessageDialog(null, "账号错误！", "警告", JOptionPane.WARNING_MESSAGE);
					}
					else if( !pwd.equals("administrator") )
					{
						JOptionPane.showMessageDialog(null, "密码错误！", "警告", JOptionPane.WARNING_MESSAGE);
					}
					
				}
					
					
				
				
			}
		});
		buttonEnter.setBounds(10, 25, 110, 35);
		panel_1.add(buttonEnter);
		
		JButton buttonReturn = new JButton("返回");
		buttonReturn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameManagerLogin.dispose();
				MainUI.frameMainUI = new MainUI();
				MainUI.frameMainUI.setVisible(true);
				
			}
		});
		buttonReturn.setBounds(170, 25, 110, 35);
		panel_1.add(buttonReturn);
	}
}
