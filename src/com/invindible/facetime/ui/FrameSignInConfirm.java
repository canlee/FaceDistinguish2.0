package com.invindible.facetime.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import com.invindible.facetime.database.Oracle_Connect;
import com.invindible.facetime.database.SignDao;

import sun.applet.Main;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

public class FrameSignInConfirm extends JFrame {

	
	static FrameSignInConfirm frameSignInConfirm;
	private JPanel contentPane;
	private String signTime;

//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					FrameSignInConfirm frame = new FrameSignInConfirm();
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
	public FrameSignInConfirm(ImageIcon[] userImages,String userAccount,final int userIdForSign) {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		setTitle("2.登陆签到-登陆确认");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 673, 482);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(95, 361, 406, 55);
		contentPane.add(panel);
		
		JButton buttonSign = new JButton("确认签到");
		buttonSign.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//签到
				
				try {
					
					Connection conn = Oracle_Connect.getInstance().getConn();
					signTime = SignDao.doSign(userIdForSign, conn);
					//获取签到时间
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				
				//显示签到成功，和签到的时间
				JOptionPane.showMessageDialog(null, "成功签到！签到的时间为：" + signTime  , "提示", JOptionPane.INFORMATION_MESSAGE);
				
				FrameSignIn.frameSignIn.dispose();
				FrameSignIn.findTask.stop();
				
//				FrameSignIn.cif.
				
				frameSignInConfirm.dispose();
				MainUI.frameMainUI = new MainUI();
				MainUI.frameMainUI.setVisible(true);
				
				
			}
		});
		buttonSign.setBounds(71, 10, 110, 35);
		panel.add(buttonSign);
		
		JButton buttonReturn = new JButton("返回");
		buttonReturn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//询问是否确认返回
				//若确认返回
				if( JOptionPane.YES_OPTION == 
						JOptionPane.showConfirmDialog(null, "确认返回？", "提示", JOptionPane.YES_NO_CANCEL_OPTION,  JOptionPane.QUESTION_MESSAGE))
				{
				frameSignInConfirm.dispose();
				
				FrameSignIn.frameSignIn.setVisible(true);
				}
				else
				{
					return;
				}
				
			}
		});
		buttonReturn.setBounds(231, 10, 110, 35);
		panel.add(buttonReturn);
		
		JPanel panelBox = new JPanel();
		panelBox.setBorder(new TitledBorder(null, "\u4FE1\u606F\u786E\u8BA4", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelBox.setBounds(10, 10, 637, 341);
		contentPane.add(panelBox);
		panelBox.setLayout(null);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(10, 26, 435, 292);
		panel_2.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "\u7167\u7247", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelBox.add(panel_2);
		panel_2.setLayout(null);
		
		JLabel label1 = new JLabel("暂无识别结果");
		label1.setHorizontalAlignment(SwingConstants.CENTER);
		label1.setFont(new Font("华文细黑", Font.PLAIN, 16));
		label1.setBounds(10, 10, 128, 128);
		panel_2.add(label1);
		
		JLabel label2 = new JLabel("暂无识别结果");
		label2.setHorizontalAlignment(SwingConstants.CENTER);
		label2.setFont(new Font("华文细黑", Font.PLAIN, 16));
		label2.setBounds(148, 10, 128, 128);
		panel_2.add(label2);
		
		JLabel label3 = new JLabel("暂无识别结果");
		label3.setHorizontalAlignment(SwingConstants.CENTER);
		label3.setFont(new Font("华文细黑", Font.PLAIN, 16));
		label3.setBounds(286, 10, 128, 128);
		panel_2.add(label3);
		
		JLabel label4 = new JLabel("暂无识别结果");
		label4.setHorizontalAlignment(SwingConstants.CENTER);
		label4.setFont(new Font("华文细黑", Font.PLAIN, 16));
		label4.setBounds(10, 154, 128, 128);
		panel_2.add(label4);
		
		JLabel label5 = new JLabel("暂无识别结果");
		label5.setHorizontalAlignment(SwingConstants.CENTER);
		label5.setFont(new Font("华文细黑", Font.PLAIN, 16));
		label5.setBounds(148, 154, 128, 128);
		panel_2.add(label5);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(467, 36, 140, 128);
		panel_1.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "ID", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelBox.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblUserId = new JLabel("你的用户名：");
		lblUserId.setFont(new Font("华文行楷", Font.PLAIN, 16));
		lblUserId.setBounds(20, 38, 110, 18);
		panel_1.add(lblUserId);
		
		JLabel lblUserAccount = new JLabel("New label");
		lblUserAccount.setFont(new Font("宋体", Font.PLAIN, 16));
		lblUserAccount.setBounds(20, 82, 125, 18);
		panel_1.add(lblUserAccount);
		lblUserAccount.setText(userAccount);
		
		//将用户的5张图片显示在界面上
		label1.setIcon(userImages[0]);
		label2.setIcon(userImages[1]);
		label3.setIcon(userImages[2]);
		label4.setIcon(userImages[3]);
		label5.setIcon(userImages[4]);
		
		
	}
}
