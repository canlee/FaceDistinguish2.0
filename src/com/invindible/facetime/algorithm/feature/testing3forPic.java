package com.invindible.facetime.algorithm.feature;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import com.invindible.facetime.util.image.ImageUtil;

public class testing3forPic extends JFrame {

	private JPanel contentPane;
	private static JLabel lbl1;
	private static JLabel lbl2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					testing3forPic frame = new testing3forPic();
					frame.setVisible(true);
					
					showPic();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public testing3forPic() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lbl1 = new JLabel("New label");
		lbl1.setBounds(56, 60, 128, 128);
		contentPane.add(lbl1);
		
		lbl2 = new JLabel("New label");
		lbl2.setBounds(244, 60, 128, 128);
		contentPane.add(lbl2);
	}
	
	public static void showPic() {
		ImageIcon imgIcon = new ImageIcon("Pictures/monkey-test/after-1.jpg");
		Image img = imgIcon.getImage();
		lbl1.setIcon(imgIcon);
		
		BufferedImage bimg = ImageUtil.ImageToBufferedImage(img);
//		ImageIcon te = new ImageIcon(bimg);
//		lbl2.setIcon(te);
		
		int[] in = ImageUtil.getPixes(bimg);
		for(int i =0; i< in.length; i++)
			System.out.print(in[i] + " ");
		System.out.println();
		
		//不知道为什么，128*128的图片，结果要用124*124才能显示出来
		Image img2 = ImageUtil.getImgByPixels(124, 124, in); //此处本来应该是128，现在暂时用124代替
		
		ImageIcon imgIcon2 = new ImageIcon(img2);
		lbl2.setIcon(imgIcon2);
	}
}
