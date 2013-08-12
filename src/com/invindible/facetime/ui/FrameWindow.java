package com.invindible.facetime.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Component;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.UIManager;

import com.invindible.facetime.org.eclipse.wb.swing.FocusTraversalOnArray;

import java.awt.Color;

import com.invindible.facetime.service.implement.CameraInterfaceImpl;
import com.invindible.facetime.task.interfaces.Context;
import com.invindible.facetime.task.video.VideoStreamTask;
import com.invindible.facetime.ui.widget.image.ImagePanel;
import javax.swing.border.LineBorder;


public class FrameWindow extends JFrame implements Context{

	static FrameWindow frame;
	private JPanel contentPane;
	private boolean clickflag = true, flagswitch = true, flagend=true;
	private int pW=9,pH=20,bX=8;//pW为导航栏的宽度，pH为导航栏的高度，bX为伸缩按钮的X坐标。
	//pW-=(93-10)/10, pH-=(205-10)/10, bX-=(89-9)/10)
	private int poW=18,poH=7,pooW=25,pooH=15;//po为PanelObject,poo为PanelOneObject.W为Width,H为Height
	private JPanel panelNevigate;
	private JPanel panelObjects, panelOneObject;
	private JButton btnClick = new JButton("New button");
	private JLabel labelPageIndex;
	private JButton btnUpPage,btnDownPage;
	private Timer timer,timer1;
	private ImageIcon arrowLeft = ImageHandle(new ImageIcon("Pictures/Arrow Left.png"), 30, 30);
	private ImageIcon arrowRight = ImageHandle(new ImageIcon("Pictures/Arrow Right.png"), 30, 30);
	private ArrayList<Targets> objects = new ArrayList<Targets>();//视频处理时，所有对象存放的数组。
	//private ImageIcon[] objects;//视频处理时，所有对象存放的数组变量。
	private int pageIndex = 0;//视频处理对象页码索引
	private JButton btnObject1,btnObject2,btnObject3;//显示视频处理对象图片的按钮
	private JButton btnOneObject;//个人处理对象图片的按钮
	private ArrayList<Integer> selectedObjectIndex = new ArrayList<Integer>();//在Button被选中的Object的序号
	private JPanel panelCamera;
		
//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					frame = new FrameWindow();
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
	public FrameWindow() {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 798, 498);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//final JPanel panelNevigate = new JPanel();
		panelNevigate = new JPanel();
		panelNevigate.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "导航"));
		panelNevigate.setBounds(0, 95, 93, 205);
		contentPane.add(panelNevigate);
		panelNevigate.setLayout(null);
		
		//启动摄像头
		CameraInterfaceImpl camera = new  CameraInterfaceImpl(this);
		camera.getCamera();
		
		
		JButton btnSwith = new JButton("切换");
		btnSwith.addActionListener(new ActionListener() {//切换按钮事件监听
			public void actionPerformed(ActionEvent e) {
				if (flagswitch) {
					timer = new Timer(10, new TimerListener1());
					timer.start();
					
					timer1 = new Timer(25, new TimerListener2());
					timer1.start();
					//panelObjects.setVisible(false);
					//panelOneObject.setVisible(true);
				}
				else
				{
					timer = new Timer(10, new TimerListener1());
					timer.start();
					
					timer1 = new Timer(25, new TimerListener2());
					timer1.start();
					
					//panelObjects.setVisible(true);
					//panelOneObject.setVisible(false);	
				}
				flagswitch = !flagswitch;
				
			}
		});
		btnSwith.setBounds(10, 45, 73, 32);
		panelNevigate.add(btnSwith);
		
		JButton btnReturn = new JButton("返回");
		btnReturn.addActionListener(new ActionListener() {//返回按钮事件监听
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				frame.dispose();
				MainUI.frameMainUI.setVisible(true);
			}
		});
		btnReturn.setBounds(10, 130, 73, 32);
		panelNevigate.add(btnReturn);
		panelNevigate.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{btnReturn, btnSwith}));
		
		JPanel panelMain = new JPanel();
		//panelMain.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder()));
		panelMain.setBounds(118, 32, 641, 406);
		contentPane.add(panelMain);
		panelMain.setLayout(null);
		
		panelCamera = new JPanel();
		panelCamera.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelCamera.setBounds(0, 0, 314, 229);
		panelMain.add(panelCamera);
		
		ImageIcon cam = new ImageIcon("Pictures/Camera.jpg");
		//JLabel lblCamera = new JLabel(ImageHandle(cam, 314, 229));
		
		GroupLayout gl_panelCamera = new GroupLayout(panelCamera);
		/*
		gl_panelCamera.setHorizontalGroup(
			gl_panelCamera.createParallelGroup(Alignment.LEADING)
				.addComponent(lblCamera, GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
		);
		gl_panelCamera.setVerticalGroup(
			gl_panelCamera.createParallelGroup(Alignment.LEADING)
				.addComponent(lblCamera, GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
		);
		*/
		panelCamera.setLayout(gl_panelCamera);
		
		panelObjects = new JPanel();
		panelObjects.setBounds(0, 239, 362, 157);
		panelMain.add(panelObjects);
		panelObjects.setLayout(null);
		
		JPanel panelObject1 = new JPanel();
		panelObject1.setBounds(10, 10, 107, 107);
		panelObjects.add(panelObject1);
		panelObject1.setLayout(null);
		
		btnObject1 = new JButton("");	//Object1
		btnObject1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int objectIndex = pageIndex * 3;
				BtnObjectDrawNike(btnObject1, objectIndex);
			}
		});
		
		btnObject1.setBounds(0, 0, 107, 107);
		panelObject1.add(btnObject1);
		
		JPanel panelObject2 = new JPanel();
		panelObject2.setBounds(127, 10, 107, 107);
		panelObjects.add(panelObject2);
		panelObject2.setLayout(null);
		
		btnObject2 = new JButton("");
		btnObject2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int objectIndex = pageIndex * 3 +1;
				BtnObjectDrawNike(btnObject2, objectIndex);
			}
		});
		btnObject2.setBounds(0, 0, 107, 107);
		panelObject2.add(btnObject2);
		
		JPanel panelObject3 = new JPanel();
		panelObject3.setBounds(244, 10, 107, 107);
		panelObjects.add(panelObject3);
		panelObject3.setLayout(null);
		
		btnObject3 = new JButton("");
		btnObject3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int objectIndex = pageIndex * 3 +2;
				BtnObjectDrawNike(btnObject3, objectIndex);
			}
		});
		btnObject3.setBounds(0, 0, 107, 107);
		panelObject3.add(btnObject3);
		
		btnUpPage = new JButton("上一页");
		btnUpPage.setEnabled(false);
		btnUpPage.addActionListener(new ActionListener() {		//上一页按钮点击事件监听
			public void actionPerformed(ActionEvent e) {
				if(pageIndex == 0)
					JOptionPane.showMessageDialog(null, "没有上一页了！");
				else {
					pageIndex--;
					btnDownPage.setEnabled(true);
					BtnObjectsShow(objects, pageIndex);
					labelPageIndex.setText("第 " + pageIndex + " 页");
					if(pageIndex ==0)
						btnUpPage.setEnabled(false);
				}
			}
		});
		btnUpPage.setBounds(46, 124, 93, 23);
		panelObjects.add(btnUpPage);
		
		btnDownPage = new JButton("下一页");
		btnDownPage.addActionListener(new ActionListener() {	 //下一页按钮点击事件监听
			public void actionPerformed(ActionEvent e) {
				if ((pageIndex+1)*3+1 > objects.size() ) {
					JOptionPane.showMessageDialog(null, "没有下一页了！");
					btnDownPage.setEnabled(false);
				}
				else {
					pageIndex++;
					btnUpPage.setEnabled(true);
					BtnObjectsShow(objects, pageIndex);
					labelPageIndex.setText("第" + pageIndex + " 页");
					if ((pageIndex+1)*3+1 > objects.size() )
						btnDownPage.setEnabled(false);
				}
			}
		});
		btnDownPage.setBounds(149, 124, 93, 23);
		panelObjects.add(btnDownPage);
		
		labelPageIndex = new JLabel("\u7B2C 0 \u9875");
		labelPageIndex.setForeground(new Color(51, 0, 204));
		labelPageIndex.setFont(new Font("华文隶书", Font.PLAIN, 18));
		labelPageIndex.setBounds(272, 123, 62, 24);
		panelObjects.add(labelPageIndex);
		
		JPanel panelMessage = new JPanel();
		panelMessage.setBounds(373, 239, 238, 146);
		panelMain.add(panelMessage);
		panelMessage.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("     \u6E23\u6E23\u6587\u5B57\u8BF4\u660E\u533A");
		lblNewLabel.setBounds(0, 0, 238, 146);
		panelMessage.add(lblNewLabel);
		
		JPanel panelHandle = new JPanel();
		panelHandle.setBounds(318, 0, 314, 229);
		panelMain.add(panelHandle);
		panelHandle.setLayout(null);
		
		ImageIcon camhandle = new ImageIcon("Pictures/CameraHandle.jpg");
		JLabel lblHandle = new JLabel(ImageHandle(camhandle, 314, 228));
		lblHandle.setBounds(0, 0, 314, 228);
		panelHandle.add(lblHandle);
		
		panelOneObject = new JPanel();
		panelOneObject.setBounds(35, 239, 258, 157);
		panelMain.add(panelOneObject);
		panelOneObject.setLayout(null);
		
		btnOneObject = new JButton("");
		btnOneObject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnOneObject.setBounds(30, 8, 135, 135);
		panelOneObject.add(btnOneObject);
		
		JButton btnStart = new JButton("开始识别");
		btnStart.setBounds(10, 345, 93, 23);
		contentPane.add(btnStart);
		
		btnClick = new JButton("");
		btnClick.setBounds(87, 189, 30 ,30);
		btnClick.setIcon(arrowLeft);
		contentPane.add(btnClick);
		btnClick.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timer = new Timer(10, new TimerListener());
				timer.start();
			}
		});
		contentPane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{btnUpPage, btnDownPage, btnSwith, panelNevigate, btnReturn, panelMessage, panelHandle, panelObject2, panelObject3, panelObjects, panelObject1, panelMain, panelCamera}));
	
	
		//--------------------------------------以下为测试代码-----------------------------------
		ImageIcon[] objs = new ImageIcon[100];
		for(int i=0; i<8; i++)
		{
			String str = new String("Pictures/objects/object" + i + ".jpg");
			objs[i] = new ImageIcon(str);
			//ImageIcon cam = new ImageIcon("Pictures/Camera.jpg");
		}
		ObjectsHandle(objs, 8);
		BtnObjectsShow(objects, pageIndex);
		BtnOneObjectShow(new ImageIcon("Pictures/objects/object1.jpg"));
		panelOneObject.setVisible(false);
		
	}
	
	//图片等比例处理方法,width和height为宽度和高度
	public ImageIcon ImageHandle(ImageIcon imageicon,int width,int height){
		Image image = imageicon.getImage();
		Image smallimage = image.getScaledInstance(width, height, image.SCALE_FAST);
		ImageIcon smallicon = new ImageIcon(smallimage);
		return smallicon;
	}
	
	//导航栏动态伸缩方法
	public void NevigateShrink() {
		
		int pw_now = panelNevigate.getWidth();
		int ph_now = panelNevigate.getHeight();
		int bx_now = btnClick.getX();
		if(pw_now == 3)//若已抵达伸缩位置，则timer停止。
		{
			timer.stop();
			clickflag = !clickflag;
			//btnClick.setIcon(new ImageIcon("Pictures/Arrow Right.gif"));
			//btnClick.setBounds(9, 192, 50, 50);
			btnClick.setIcon(arrowRight);
			return;
		}
		panelNevigate.setBounds(0, 95, pw_now - pW, pH);
		//panelNevigate.setBounds(0, 95, pw_now - pW, ph_now - pH);
		btnClick.setBounds(bx_now - bX, 192, 30, 30);
		
		/*//原来的最终目标
		panelNevigate.setBounds(0, 95, 3, 5);
		btnClick.setBounds(9, 192, 19, 23);
		*/	
	}
	
	public void NevigateEnlarge() {
		
		int pw_now = panelNevigate.getWidth();
		int ph_now = panelNevigate.getHeight();
		int bx_now = btnClick.getX();
		if(pw_now == 93)//若已抵达伸缩位置，则timer停止。
		{
			timer.stop();
			clickflag = !clickflag;
			btnClick.setIcon(arrowLeft);
			return;
		}
		panelNevigate.setBounds(0, 95, pw_now + pW, ph_now + pH);
		btnClick.setBounds(bx_now + bX, 192, 30, 30);
		/*//原来的最终目标
		panelNevigate.setBounds(0, 95, 93, 205);
		btnClick.setBounds(89, 192, 19, 23);
		*/
	}
	
	public void NevigateSwitch() {
		int pw_now = panelNevigate.getWidth();
		int ph_now = panelNevigate.getHeight();
		int bx_now = btnClick.getX();

		if(pw_now == 3)
		{
			flagend = !flagend;
			btnClick.setIcon(arrowRight);
		}
		if(!flagend && pw_now == 93)//若已抵达伸缩位置，则timer停止。
		{
			timer.stop();
			flagend = !flagend;
			btnClick.setIcon(arrowLeft);
			return;
		}
		if(flagend) {
			panelNevigate.setBounds(0, 95, pw_now - pW, pH);
			btnClick.setBounds(bx_now - bX, 192, 30, 30);
		}
		else {
			panelNevigate.setBounds(0, 95, pw_now + pW, ph_now + pH);
			btnClick.setBounds(bx_now + bX, 192, 30, 30);
		}

	}
	
	public void JButtonPanelSwitch() {
		
		int pow_now = panelObjects.getWidth();
		int poh_now = panelObjects.getHeight();
		int poow_now = panelOneObject.getWidth();
		int pooh_now = panelOneObject.getHeight();
		
		if(!flagswitch)
		{
			if(pow_now == 2 && poow_now == 258)//若已伸缩完毕，则结束
			{
				timer1.stop();
				//panelObjects.setBounds(0, 239, 362, 157);
				return;
			}
			if(pow_now == 2)//若panelObjects已收缩，则开始伸展panelOneObject
			{
				panelOneObject.setBounds(35, 239, poow_now + pooW, pooh_now + pooH);
				//panelOneObject.setBounds(35, 239, 258, 157);
				//(258-8)/10=25,(157-7)/10=15;
				return;
			}
			panelObjects.setBounds(0, 239, pow_now - poW, poh_now - poH);
			if(pow_now - poW == 2)//当panelObjects收缩完毕，则将panelOneObject的先设置成收缩状态
			{
				panelOneObject.setBounds(35, 239, 8, 7);
				panelObjects.setVisible(false);
				panelOneObject.setVisible(true);
			}
		}
		else
		{
			if(poow_now == 8 && pow_now == 362)
			{
				timer1.stop();
				return;
			}
			if(poow_now == 8)
			{
				panelObjects.setBounds(0, 239, pow_now + poW, poh_now + poH);
				return;
			}
			panelOneObject.setBounds(35, 239, poow_now - pooW, pooh_now - pooH);
			if(poow_now - pooW == 8)
			{
				panelOneObject.setVisible(false);
				panelObjects.setVisible(true);
			}
			
		}
		//panelObjects.setBounds(0, 239, pow_now + poW, poh_now + poH);
		//panelObjects.setBounds(0, 239, 362, 157);
		//(362-2)/20 = 18,(157-17)/20 = 7;
	}
	
	class TimerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (clickflag)//若是伸展状态
			{
				NevigateShrink();
			}
			else//若是收缩状态
			{
				NevigateEnlarge();
			}
		}
	}
	
	class TimerListener1 implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			NevigateSwitch();
		}
		
	}
	
	class TimerListener2 implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			JButtonPanelSwitch();
			
		}
		
	}
	
	//视频处理对象添加方法
	public void ObjectsHandle(ImageIcon[] objects,int number) {
		for(int i=0; i<number; i++)
		{
			this.objects.add(new Targets(ImageHandle(objects[i], 107, 107), false));
//			this.objects.add(ImageHandle(objects[i], 107, 107));
		}
	}
	
	//在btnObject上显示对象的方法
	public void BtnObjectsShow(ArrayList<Targets> arr, int pageIndex) {
		int index = pageIndex * 3;
		int maxIndex = arr.size() % 3;

		//测试加上打钩
//		BufferedImage img = drawNike(arr.get(index));
//		ImageIcon imageicon = new ImageIcon(img);
//		btnObject1.setIcon((Icon)img);
//		btnObject1.setIcon(imageicon);
//		//-------------
		BtnCheckDrawNike(btnObject1, pageIndex * 3);
//		BtnObjectDrawNike(btnObject1, pageIndex * 3);
//		btnObject1.setIcon(arr.get(index).getImageicon());
		try {
			BtnCheckDrawNike(btnObject2, pageIndex * 3 + 1);
//			btnObject2.setIcon(arr.get(index+1).getImageicon());
			btnObject2.setEnabled(true);
		} catch (Exception e) {
			//IndexOutOfBoundsException
			BtnCheckDrawNike(btnObject3, pageIndex * 3 + 2);
//			btnObject2.setIcon(new ImageIcon("Pictures/X.jpg"));
			btnObject2.setEnabled(false);
		}
		try {
			btnObject3.setIcon(arr.get(index+2).getImageicon());
			btnObject3.setEnabled(true);
		} catch (Exception e) {
			btnObject3.setIcon(new ImageIcon("Pictures/X.jpg"));
			btnObject3.setEnabled(false);
		}
		
	}
	
	//在btnOneObject上显示对象的方法
	public void BtnOneObjectShow(ImageIcon img) {
		btnOneObject.setIcon(ImageHandle(img, btnOneObject.getWidth(), btnOneObject.getHeight()));
		//btnOneObject.setIcon(ImageHandle(img, 135, 135));
	}
	
	//在JButtonObjects上打钩(在图片上画画的方法)
	public static ImageIcon drawNike(ImageIcon img) {
		int width = img.getIconWidth();
		int height = img.getIconHeight();
		BufferedImage buffima = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D)buffima.getGraphics();
//		Graphics2D g = (Graphics2D)img.getImage().getGraphics(); //无法从ImageIcon直接转换成Graphics2D
		g.setColor(Color.RED);
		g.drawImage(img.getImage(), 0, 0, img.getIconWidth(), img.getIconHeight(), img.getImageObserver());
		
		//画画：打钩
		g.drawLine(70, 30, 80, 40);
		g.drawLine(80, 40, 100, 10);
//		//加粗线条
//		g.drawLine(71, 31, 81, 41);	
//		g.drawLine(81, 41, 101, 11);
		
		ImageIcon img_return = new ImageIcon(buffima);
		return img_return;
	}
	
	//Objects的类，成员包括：1.object的图像；2.object是否被选中；
	public class Targets {
		ImageIcon imageicon; //对象的图像(头像)
		boolean selected; //是否被选中
		
		Targets() {
		}
		
		Targets(ImageIcon img , boolean select) {
			this.imageicon = img;
			this.selected = select;
		}

		public ImageIcon getImageicon() {
			return imageicon;
		}

		public void setImageicon(ImageIcon imageicon) {
			this.imageicon = imageicon;
		}

		public boolean isSelected() {
			return selected;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}

	}
	
	//点击BtnObject对象后打钩 或 取消打钩；
	public void BtnObjectDrawNike(JButton btn,int objectIndex) {
		if(objects.get(objectIndex).isSelected()) {	//若已经打钩，则取消打钩
			btn.setIcon(objects.get(objectIndex).getImageicon());
			objects.get(objectIndex).setSelected(false);
			}
		else {	//若没打钩，则打钩
			btn.setIcon(drawNike(objects.get(objectIndex).getImageicon()));
			objects.get(objectIndex).setSelected(true);
		}
	}
	
	//翻页时，检查对象图片打钩处理的方法。确认图片是否需要加打钩。
	public void BtnCheckDrawNike(JButton btn, int objectIndex) {
		if(objects.get(objectIndex).isSelected()) {	//需要打钩，则打钩
			btn.setIcon(drawNike(objects.get(objectIndex).getImageicon()));
		}
		else {	//无需打钩，则不打钩
			btn.setIcon(objects.get(objectIndex).getImageicon());
		}
	}

	@Override
	public void onRefresh(Object... objects) {
		// TODO Auto-generated method stub
		Integer result = (Integer) objects[0];
		switch (result) {
		case VideoStreamTask.OPEN_CAMERA_SUCCESS:
			Component component = (Component) objects[1];
			component.setBounds(0, 0, 314, 229);
			panelCamera.add(component);
			break;
			
			
		default:
			break;
		}
	}

}

