package com.invindible.facetime.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.JProgressBar;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

import com.sun.media.rtsp.TimerListener;

import java.awt.Font;
import java.awt.Color;

public class ProgressBarSignIn extends JFrame {

	public static ProgressBarSignIn frameProgressBarSignIn;
	private JPanel contentPane;
	private JProgressBar progressBar;
	private JLabel lblNewLabel;
	private JLabel label;
	private Timer timer;
	private int addTimes;//progressBar增加的次数，[0,addRange]次
	private int addRange = 25;//每一次增加的幅度，即progressBar一次增加多少为止

//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					ProgressBarSignIn frame = new ProgressBarSignIn();
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
	public ProgressBarSignIn() {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 381, 216);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		progressBar = new JProgressBar();
		progressBar.setForeground(new Color(0, 255, 0));
		progressBar.setBounds(10, 128, 338, 29);
//		progressBar.setForeground(fg)
		contentPane.add(progressBar);
		
		lblNewLabel = new JLabel("注册成功！请稍等，");
		lblNewLabel.setFont(new Font("华文行楷", Font.PLAIN, 20));
		lblNewLabel.setBounds(10, 10, 323, 55);
		contentPane.add(lblNewLabel);
		
		label = new JLabel("正在将数据插入数据库中.");
		label.setFont(new Font("华文行楷", Font.PLAIN, 20));
		label.setBounds(73, 63, 275, 55);
		contentPane.add(label);
		

	}
	
	public void startAddProgressBar()
	{
//		this.progressBar.setValue( progressBar.getValue() + 25);
		this.addTimes = 0;
		this.timer = new Timer(10, new TimerListener());
		this.timer.start();
	}
	
	public void addProgressBar()
	{
		this.progressBar.setValue( progressBar.getValue() +1);
	}
	
	class TimerListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			addProgressBar();
			addTimes++;
			if( addTimes == addRange)
			{
				timer.stop();
			}
			
		}
		
	}
}
