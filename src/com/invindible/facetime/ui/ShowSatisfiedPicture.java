package com.invindible.facetime.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.invindible.facetime.ui.widget.image.ImagePanel;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;

import oracle.net.aso.l;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

public class ShowSatisfiedPicture extends JFrame {

	static ShowSatisfiedPicture frameShowSatisfiedPicture;
	private JPanel contentPane;
	private int pageNum;
	private JButton btnPageUp;
	private JButton btnPageDown;
	private ImagePanel ipObjectOriginal;
	private ImagePanel ipObjectFound;
	private JLabel label_1;
	private JLabel lblObjectNum;
	private JLabel lblPage;

//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					ShowSatisfiedPicture frame = new ShowSatisfiedPicture();
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
	public ShowSatisfiedPicture(int objectsSatisfiedCount,final BufferedImage[] buffImgObjectsSatisfied,boolean[] isObjectsSatisfied,final String[] timeFound,final BufferedImage[] objectsToFind) {
		setTitle("4.视频监视-结果显示");
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		pageNum = 1;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 661, 345);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u5BF9\u8C61  ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 17, 438, 263);
		contentPane.add(panel);
		panel.setLayout(null);
		
		ipObjectOriginal = new ImagePanel();
		ipObjectOriginal.setBounds(45, 75, 128, 128);
		panel.add(ipObjectOriginal);
		
		ipObjectFound = new ImagePanel();
		ipObjectFound.setBounds(257, 75, 128, 128);
		panel.add(ipObjectFound);
		
		lblObjectNum = new JLabel("1");
		lblObjectNum.setFont(new Font("宋体", Font.PLAIN, 16));
		lblObjectNum.setBounds(35, 0, 54, 15);
		panel.add(lblObjectNum);
		
		JLabel lblNewLabel = new JLabel("对象原图：");
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 14));
		lblNewLabel.setBounds(43, 35, 87, 30);
		panel.add(lblNewLabel);
		
		JLabel label = new JLabel("视频中的截图：");
		label.setFont(new Font("宋体", Font.PLAIN, 14));
		label.setBounds(257, 35, 113, 30);
		panel.add(label);
		
		JLabel lblNewLabel_1 = new JLabel("截图时间：");
		lblNewLabel_1.setFont(new Font("宋体", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(83, 213, 199, 15);
		panel.add(lblNewLabel_1);
		
		label_1 = new JLabel("0时 0分 0秒");
		label_1.setFont(new Font("宋体", Font.PLAIN, 14));
		label_1.setBounds(137, 238, 199, 15);
		panel.add(label_1);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBounds(483, 66, 140, 133);
		contentPane.add(panel_1);
		
		lblPage = new JLabel("第 [1] 页");
		lblPage.setFont(new Font("宋体", Font.PLAIN, 16));
		lblPage.setBounds(25, 5, 83, 19);
		panel_1.add(lblPage);
		
		btnPageUp = new JButton("上一页");
		btnPageUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				pageNum--;
				btnPageDown.setEnabled(true);
				
				if(pageNum == 1)
				{
					btnPageUp.setEnabled(false);
				}
				RefreshUi(buffImgObjectsSatisfied, timeFound, objectsToFind);
			}
		});
		btnPageUp.setFont(new Font("宋体", Font.PLAIN, 14));
		btnPageUp.setEnabled(false);
		btnPageUp.setBounds(25, 34, 83, 28);
		panel_1.add(btnPageUp);
		
		btnPageDown = new JButton("下一页");
		btnPageDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				pageNum++;
				btnPageUp.setEnabled(true);
				
				if(pageNum == 1)
				{
					btnPageDown.setEnabled(false);
				}
				RefreshUi(buffImgObjectsSatisfied, timeFound, objectsToFind);
			}
		});
		btnPageDown.setFont(new Font("宋体", Font.PLAIN, 14));
		btnPageDown.setBounds(25, 82, 83, 28);
		panel_1.add(btnPageDown);
		
		JButton btnClose = new JButton("关闭此窗口");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				frameShowSatisfiedPicture.dispose();
			}
		});
		btnClose.setBounds(493, 209, 98, 36);
		contentPane.add(btnClose);
		
		//刷新页面
		RefreshUi(buffImgObjectsSatisfied, timeFound, objectsToFind);
	}
	
	/**
	 * 根据页数，刷新界面，显示对象
	 */
	private void RefreshUi(BufferedImage[] buffImgObjectsSatisfied,String[] timeFound,BufferedImage[] objectsToFind)
	{
		switch(pageNum)
		{
			case 1:
				ipObjectOriginal.setBufferImage(objectsToFind[0]);
				ipObjectFound.setBufferImage(buffImgObjectsSatisfied[0]);
				label_1.setText(timeFound[0]);
				lblObjectNum.setText("1");
				lblPage.setText("第 [" + 1 + "] 页");
				break;
			case 2:
				ipObjectOriginal.setBufferImage(objectsToFind[1]);
				ipObjectFound.setBufferImage(buffImgObjectsSatisfied[1]);
				label_1.setText(timeFound[1]);
				lblObjectNum.setText("2");
				lblPage.setText("第 [" + 2 + "] 页");
				break;
			case 3:
				ipObjectOriginal.setBufferImage(objectsToFind[2]);
				ipObjectFound.setBufferImage(buffImgObjectsSatisfied[2]);
				label_1.setText(timeFound[2]);
				lblObjectNum.setText("3");
				lblPage.setText("第 [" + 3 + "] 页");
				break;
			case 4:
				ipObjectOriginal.setBufferImage(objectsToFind[3]);
				ipObjectFound.setBufferImage(buffImgObjectsSatisfied[3]);
				label_1.setText(timeFound[3]);
				lblObjectNum.setText("4");
				lblPage.setText("第 [" + 4 + "] 页");
				break;
			case 5:
				ipObjectOriginal.setBufferImage(objectsToFind[4]);
				ipObjectFound.setBufferImage(buffImgObjectsSatisfied[4]);
				label_1.setText(timeFound[4]);
				lblObjectNum.setText("5");
				lblPage.setText("第 [" + 5 + "] 页");
				break;
			case 6:
				ipObjectOriginal.setBufferImage(objectsToFind[5]);
				ipObjectFound.setBufferImage(buffImgObjectsSatisfied[5]);
				label_1.setText(timeFound[5]);
				lblObjectNum.setText("6");
				lblPage.setText("第 [" + 6 + "] 页");
				break;
			case 7:
				ipObjectOriginal.setBufferImage(objectsToFind[6]);
				ipObjectFound.setBufferImage(buffImgObjectsSatisfied[6]);
				label_1.setText(timeFound[6]);
				lblObjectNum.setText("7");
				lblPage.setText("第 [" + 7 + "] 页");
				break;
			case 8:
				ipObjectOriginal.setBufferImage(objectsToFind[7]);
				ipObjectFound.setBufferImage(buffImgObjectsSatisfied[7]);
				label_1.setText(timeFound[7]);
				lblObjectNum.setText("8");
				lblPage.setText("第 [" + 8 + "] 页");
				break;
			case 9:
				ipObjectOriginal.setBufferImage(objectsToFind[8]);
				ipObjectFound.setBufferImage(buffImgObjectsSatisfied[8]);
				label_1.setText(timeFound[8]);
				lblObjectNum.setText("9");
				lblPage.setText("第 [" + 9 + "] 页");
				break;
			default :
				break;
		}
	}
}
