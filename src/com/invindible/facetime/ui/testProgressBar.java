package com.invindible.facetime.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class testProgressBar extends JFrame {

	private JPanel contentPane;

//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					testProgressBar frame = new testProgressBar();
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
	public testProgressBar() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("打开进度条");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProgressBarSignIn.frameProgressBarSignIn = new ProgressBarSignIn();
				ProgressBarSignIn.frameProgressBarSignIn.setVisible(true);
				
				ProgressBarSignIn.frameProgressBarSignIn.startAddProgressBar();
				System.out.println("1");
				ProgressBarSignIn.frameProgressBarSignIn.startAddProgressBar();
				int a = 1;
				System.out.println("2");
				ProgressBarSignIn.frameProgressBarSignIn.startAddProgressBar();
				System.out.println("3");
				ProgressBarSignIn.frameProgressBarSignIn.startAddProgressBar();
				System.out.println("4");
			}
		});
		btnNewButton.setBounds(62, 58, 93, 23);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("进度条增加");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProgressBarSignIn.frameProgressBarSignIn.startAddProgressBar();
			}
		});
		btnNewButton_1.setBounds(135, 141, 93, 23);
		contentPane.add(btnNewButton_1);
		

	}

}
