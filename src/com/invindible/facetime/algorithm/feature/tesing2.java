package com.invindible.facetime.algorithm.feature;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

public class tesing2 extends JFrame {

	private JPanel contentPane;
	private static Image[] img = new Image[10];
	private static JLabel lblNewLabel;
	private static JLabel label1;
	private static JLabel label2;
	private static JLabel label3;
	private static JLabel label4;
	private static JLabel label5;
	private static JLabel label6;
	private static JLabel label7;
	private static JLabel label8;
	private static JLabel label9;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					

					
//					GetFeatureMatrix gfm1 = new GetFeatureMatrix();
//					Image[] icon = new Image[10];
//					icon = gfm1.getFeatureFaces();
//		
//					ImageIcon iicon = new ImageIcon(icon[0]);
//					lblNewLabel.setIcon(iicon);
					//
					
					tesing2 frame = new tesing2();
					frame.setVisible(true);
					
					test();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
	}

	/**
	 * Create the frame.
	 */
	public tesing2() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 784, 452);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 768, 404);
		contentPane.add(panel);
		panel.setLayout(null);
		
		lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(10, 10, 128, 128);
		panel.add(lblNewLabel);
		
		label1 = new JLabel("New label");
		label1.setBounds(163, 10, 128, 128);
		panel.add(label1);
		
		label2 = new JLabel("New label");
		label2.setBounds(301, 10, 128, 128);
		panel.add(label2);
		
		label3 = new JLabel("New label");
		label3.setBounds(439, 10, 128, 128);
		panel.add(label3);
		
		label4 = new JLabel("New label");
		label4.setBounds(577, 10, 128, 128);
		panel.add(label4);
		
		label5 = new JLabel("New label");
		label5.setBounds(10, 177, 128, 128);
		panel.add(label5);
		
		label6 = new JLabel("New label");
		label6.setBounds(163, 177, 128, 128);
		panel.add(label6);
		
		label7 = new JLabel("New label");
		label7.setBounds(301, 177, 128, 128);
		panel.add(label7);
		
		label8 = new JLabel("New label");
		label8.setBounds(439, 177, 128, 128);
		panel.add(label8);
		
		label9 = new JLabel("New label");
		label9.setBounds(577, 177, 128, 128);
		panel.add(label9);
		
		
	}
	

	public static void test() {
		//创建图片数组
//		GetFeatureMatrix gfm = new GetFeatureMatrix();
		ImageIcon[] icon = new ImageIcon[10];
		
		String source = "C:\\Users\\Administrator\\Desktop\\完成品\\";
//		String source = "C:\\Users\\Administrator\\Desktop\\1\\ttt";
		
//		System.out.println(source);
		for(int i=0; i<10; i++)
		{
			String target = i+1 + ".jpg";
			icon[i] = new ImageIcon(source + target);
			System.out.println(source+ target);
		}

//		String source = "Pictures/monkey-test/after";
//		for(int i=0; i<6; i++)
//		{
//			String target = "-" + (i+1) + ".jpg";
//			icon[i] = new ImageIcon(source + target);
//			System.out.println(source + target);
//		}
//		icon[6] = new ImageIcon(source + "1.jpg");
//		System.out.println(source + "1.jpg");
//		for(int i=7; i<10; i++)
//		{
//			String target = (i-4) + ".jpg";
//			icon[i] = new ImageIcon(source + target);
//			System.out.println(source + target);
////			System.out.println("i:" + i + "  i-4:" + (i-4));
//		}			
//		BufferedImage bimg = ImageUtil.icon[0].getImage();
		//计算图片数组的特征值和特征向量
		//将结果保存在Features类中
		GetFeatureMatrix.getInstance().imageToResult(icon);
		
		double[][] a = new double[1][1];
		a = Features.getInstance().getResultFeatureVector();
		System.out.println("[" + a.length +"]" + "[" + a[0].length + "]");
		
		//获取特征脸
		img = GetFeatureMatrix.getInstance().getFeatureFaces();
		System.out.println("img:" + img.length);
		ImageIcon imgIcon = new ImageIcon(img[0]);
		ImageIcon imgIcon2 = new ImageIcon(img[1]);
		ImageIcon imgIcon3 = new ImageIcon(img[2]);
		ImageIcon imgIcon4 = new ImageIcon(img[3]);
		ImageIcon imgIcon5 = new ImageIcon(img[4]);
		ImageIcon imgIcon6 = new ImageIcon(img[5]);
		ImageIcon imgIcon7 = new ImageIcon(img[6]);
		ImageIcon imgIcon8 = new ImageIcon(img[7]);
		ImageIcon imgIcon9 = new ImageIcon(img[8]);
		ImageIcon imgIcon10 = new ImageIcon(img[9]);
		
		lblNewLabel.setIcon(imgIcon);
		label1.setIcon(imgIcon2);
		label2.setIcon(imgIcon3);
		label3.setIcon(imgIcon4);
		label4.setIcon(imgIcon5);
		label5.setIcon(imgIcon6);
		label6.setIcon(imgIcon7);
		label7.setIcon(imgIcon8);
		label8.setIcon(imgIcon9);
		System.out.println( imgIcon10 == null);
		label9.setIcon(imgIcon10); //第10个人的图片居然挂
//		label9.setIcon(icon[9]);
		
		//从单例中获取数据
		double[] eigenValue = Features.getInstance().getEigenValue();
		double[][] featureVector = Features.getInstance().getFeatureVector();
		
		System.out.println("特征值的个数：" + eigenValue.length);
		
		System.out.println("特征向量的维数：" +
				"[" + Features.getInstance().getResultFeatureVector().length + "]" +
				"[" + Features.getInstance().getResultFeatureVector()[0].length + "]");
//		System.out.println("特征向量的维数：" + 
//				"[" + featureVector.length +"]" + "[" + featureVector[0].length+ "]");
		
		//输出测试
		System.out.println("特征值：");
		for(int i=0; i<eigenValue.length; i++) {
			System.out.print(eigenValue[i] + " ");
		}
		System.out.println();
		System.out.println("---------分割线------------");
		
		System.out.println("featureVector.length:" + featureVector.length);
//		System.out.println("特征向量：");
//		for(int i=0; i<featureVector.length; i++)
//		{
//			for(int j=0; j<featureVector[i].length; j++)
//			{
//				System.out.print(featureVector[i][j] + " ");
//			}
//			System.out.println();
//		}
	}
}
