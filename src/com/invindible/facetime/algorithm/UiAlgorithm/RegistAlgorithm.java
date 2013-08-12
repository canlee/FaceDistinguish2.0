package com.invindible.facetime.algorithm.UiAlgorithm;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import jmapps.jmstudio.SaveAsDialog;

import com.invindible.facetime.algorithm.LDA;
import com.invindible.facetime.algorithm.Mark;
import com.invindible.facetime.algorithm.feature.Features;
import com.invindible.facetime.algorithm.feature.GetFeatureMatrix;
import com.invindible.facetime.algorithm.feature.GetPcaLda;
import com.invindible.facetime.database.Oracle_Connect;
import com.invindible.facetime.database.ProjectDao;
import com.invindible.facetime.database.UserDao;
import com.invindible.facetime.model.Imageinfo;
import com.invindible.facetime.model.LdaFeatures;
import com.invindible.facetime.model.Project;
import com.invindible.facetime.model.User;
import com.invindible.facetime.model.Wopt;
import com.invindible.facetime.ui.FrameRegist;
import com.invindible.facetime.ui.MainUI;
import com.invindible.facetime.ui.ProgressBarSignIn;
import com.invindible.facetime.util.image.ImageUtil;
import com.invindible.facetime.wavelet.Wavelet;

public class RegistAlgorithm {

//	private BufferedImage[] soyBufferedImages;
	private static int testNum = 2;//测试样例的数量(默认为2)
//	private int photoNum = 5;//每个人的照片数量(默认为5)
	private static boolean haveSoy;// = false;//是否有加入“酱油”进投影Z中的标志
	private static BufferedImage[] soyBufferedImages;
	
	/**
	 * 点击注册后
	 * 尝试用2张测试样例识别5张用户照片
	 * 若识别成功，则将数据存入数据库中
	 * 不成功，则给出提示
	 * @return 
	 */
	public static boolean startDistinguish(User user, ImageIcon[] imageIcons, ImageIcon[] testIcons, int requestNum,boolean[] isImageIconSelected,boolean startChangeSelectedIcon)
	{
		haveSoy = false;
		soyBufferedImages =  new BufferedImage[5];
		//此处加入注册处理，例如将注册者的图片保存进数据库之类的操作
		
		
		//设置User的用户名、密码和5张照片。
		//用户名 userId
		//密码 passWord
		//5张照片 imageIcons
		
		//设置人数，和每人的照片数（此处默认每人5张）
		int peopleNum = 2;//2是暂定的，需要根据数据库进行修改
		int photoNum = 5;
		
		//WoptT矩阵
		double[][] WoptT;
		//Project数据
		Project pr;
		//保存从数据库获取的图片的数组
		BufferedImage[] bImages = null;
//		//临时保存单人图片的数组
//		BufferedImage[] tempForOneManBImages = null;
		//用来PCA、LDA计算的数组
		ImageIcon[] icon = null;// = new ImageIcon[2*5];//[peopleNum*photoNum]
		
		Connection conn = null;
		try
		{
			conn = Oracle_Connect.getInstance().getConn();
			//1.首先判断数据库中是否已有WoptT矩阵的数据（即看是否已经有样本数据）
			
			//若存在WoptT
			if( ProjectDao.firstORnot(conn) == false)
			{
				//读取所有样本数据，以便对所有样本和自己进行训练
				
//				//读取WoptT矩阵
//				WoptT = ProjectDao.doselectWopt(conn);
				
				//(读取所有样本的投影Z 和 id)
				//读取所有样本的图片
				BufferedImage[] bimg = UserDao.doSelectAll(conn);
				
				for(int i=0; i<bimg.length; i++)
				{
					int[] le = ImageUtil.getPixes(bimg[i]);

					System.out.println("长度:" + le.length);
				}
				
				//------------------------------peopleNum需要从数据库中获取---------------------------------------------
				//获取peopleNum
				peopleNum = bimg.length / 5;
				peopleNum++;//因为注册多了一个人，所以peopleN+1
				//实例化bImages图片数组
				bImages = new BufferedImage[peopleNum * photoNum];
//				icon = new ImageIcon[peopleNum * photoNum];
				
				for(int i=0; i<bimg.length; i++)
				{
					bImages[i] = bimg[i];
				}
				for(int i=0; i<5; i++)
				{
//					icon[bimg.length + i] = imageIcons[i];
					Image img = imageIcons[i].getImage();
					bImages[bimg.length + i] = ImageUtil.ImageToBufferedImage(img);
				}
				
			}
			//若不存在，则直接对自己的数据进行训练
			else
			{
				haveSoy = true;
				//设置peopleNum为2（酱油&自己)
				peopleNum = 2;
//				//实例化icon图片数组
//				icon = new ImageIcon[peopleNum * photoNum];//[2 * 5]
				//实例化bImages图片数组
				bImages = new BufferedImage[peopleNum * photoNum];
//				tempForOneManBImages = new BufferedImage[photoNum];
				
//				读取Pictures文件夹里面的"酱油"的图片，并赋值给bImages[0-4];
				String source = "Pictures/none/";
				for(int i=0; i<5; i++)
				{
					String source2 = "after37-" + (i+1) + ".jpg";
					ImageIcon imageIcon = new ImageIcon(source + source2);
					Image img = imageIcon.getImage();
					bImages[i] = ImageUtil.ImageToBufferedImage(img);
					soyBufferedImages[i] = ImageUtil.ImageToBufferedImage(img);
				}
				
				//将自己的图片赋值给bImages[5-9];
				for(int i=5; i<10; i++)
				{
					Image img = imageIcons[i-5].getImage();
					bImages[i] = ImageUtil.ImageToBufferedImage(img);
				}
				
			}
			
			
		}
		catch(Exception e1)
		{
			e1.printStackTrace();
		}
		
//		//16348维，128*128
//		int[] vector1 = GetFeatureMatrix.getPixes(bImages[0]);
//		System.out.println("维数：" + vector1.length);
		
		//对bImages[]的图片进行小波变换
		BufferedImage[] waveBImages;
		waveBImages = Wavelet.Wavelet(bImages);
		
		//2.训练（将 本人的5张照片 和 数据库中的所有照片（每人5张） 投影到WoptT上)
		GetPcaLda.getResult(waveBImages);
		
		//1024维，32*32
		int[] vector = GetFeatureMatrix.getPixes(waveBImages[0]);
		System.out.println("维数：" + vector.length);
		
		double[][] modelP=new double[peopleNum*photoNum][peopleNum-1];
		for(int i=0;i<peopleNum*photoNum;i++){
			modelP[i]=LDA.getInstance().calZ(waveBImages[i]);//投影
		}
		
//		验证需要5个数据，前3个都是double[][]
//		1.(为了计算<2>所用)WoptT（从单例中获取）
		//double[] WoptT
//		2.2张照片的投影（将拍到的图片，通过Wopt投影后,转成double[][]）
		double[][] testZ = new double[testNum][peopleNum-1];//[测试用例数量][C-1]
//		3.训练样例的投影（上面的modelP）
		//double[][] modelP
//		4.<2>的均值（从<2>处理）
		double[] testZMean = new double[peopleNum-1];
//		5.（投影Z的）N个人的，类内均值（每个人都有一个均值)
		double[][] modelMean=new double[peopleNum][peopleNum-1];
//		6.（投影Z的）总体均值
		double[] allMean=new double[peopleNum-1];
		
		//1.WoptT（从单例中获取）
		WoptT = LdaFeatures.getInstance().getLastProjectionT();
		
		//2.2张照片的投影（将拍到的图片，通过Wopt投影后,转成double[][]）
		//首先，将ImageIcon[2] testIcons转换成BufferedImage[2]
		BufferedImage[] tempForTestBImages = new BufferedImage[testNum];
		for(int i=0; i<testNum; i++)
		{
			Image img = testIcons[i].getImage();
			tempForTestBImages[i] = ImageUtil.ImageToBufferedImage(img);
		}
		
		//然后，对tempForTestBImages进行小波变换，转成BufferedImage[2]
		BufferedImage[] waveTestBImages = Wavelet.Wavelet(tempForTestBImages);
		
		//计算2张经小波变换的测试图waveTestBImages的投影Z
		for(int i=0; i<testNum; i++)
		{
			testZ[i]=LDA.getInstance().calZ(waveTestBImages[i]);
		}
		
		//4.<2>的均值（从<2>处理）
		for(int i=0; i<(peopleNum-1); i++)
		{
			for(int j=0; j<testNum; j++)
			{
				testZMean[i] += testZ[j][i];
			}
			testZMean[i] /= testNum;
		}
		
		//5.（投影Z的）N个人的，类内均值（每个人都有一个均值)
		//6.（投影Z的）总体均值
		for(int i=0;i<peopleNum;i++){
			for(int k=0;k<peopleNum-1;k++){
				for(int j=0;j<photoNum;j++){
					modelMean[i][k]+=modelP[photoNum*i+j][k];
				}
				allMean[k]+=modelMean[i][k];
				modelMean[i][k]/=photoNum;
			}			
		}
		
		for(int i=0;i<peopleNum-1;i++)
			allMean[i]/=peopleNum*photoNum;
		
		
		//验证（尝试识别，识别失败则需要重新获取图片）
		if( Mark.domark(testZ, modelP, testZMean, modelMean, allMean) == false)
		{
			
			JOptionPane.showMessageDialog(null, "照片样例识别失败！正在重新获取所有图片", "提示", JOptionPane.INFORMATION_MESSAGE);
//			//将数据初始化，以开始重新获取图片
//			requestNum = 7;
//			for(int i=0; i<7;i++)
//			{
//				isImageIconSelected[i] = true;
//			}
//			startChangeSelectedIcon = true;
			
			return false;
		}
		//若可以，则注册成功，将用户名、密码、5张照片存入数据库
		else{
			
			
			ProgressBarSignIn.frameProgressBarSignIn = new ProgressBarSignIn();
			ProgressBarSignIn.frameProgressBarSignIn.setVisible(true);
			
			JOptionPane.showMessageDialog(null, "注册成功！点击确定后，数据将存入数据库中。", "注册成功", JOptionPane.INFORMATION_MESSAGE);

			
			SaveDataToDB(conn, WoptT, imageIcons,user,modelP);
			
			
			//关闭进度条窗口
			ProgressBarSignIn.frameProgressBarSignIn.dispose();
			
			return true;
			
//			FrameRegist.frameRegist.dispose();
//			MainUI.frameMainUI = new MainUI();
//			MainUI.frameMainUI.setVisible(true);
//			
//			//最终注册成功后，将寻找人脸的方法暂停
//			FrameRegist.findTask.stop();
		}
		
	}
	
	/**
	 * 将用户名、密码、5张照片存入数据库
	 */
	public static void SaveDataToDB(Connection conn, double[][] WoptT, ImageIcon[] imageIcons,User user, double[][] modelP)
	{
		//将数据：用户名、密码、5张照片存入数据库
		try
		{
			conn = Oracle_Connect.getInstance().getConn();
			//封装double[][] Wopt 进 Wopt wopt
			Wopt wopt = new Wopt();
			wopt.setWopt(WoptT);
			
			
			//将Wopt插入数据库中
			ProjectDao.doinsertWopt(conn, wopt);
			
			//第一次,增加进度条
			ProgressBarSignIn.frameProgressBarSignIn.startAddProgressBar();
			
			//将总体均值m插入数据库中
			double[] m = LdaFeatures.getInstance().getAveVector();
			ProjectDao.doinsertmean(conn, m);

			
			//第二次,增加进度条
			ProgressBarSignIn.frameProgressBarSignIn.startAddProgressBar();
			
			//将插入数据库的图片,封装进inputStream
			Imageinfo imageInfo = new Imageinfo();
			InputStream[] inputStream = new InputStream[5];
			
			int[] userIds;
			
			//如果数据库中没人,先插酱油的
			if (haveSoy)
			{
				//把酱油封装进User
				User userSoy = new User();
				userSoy.setUsername("none");
				userSoy.setPassword("123456");
				
				//将ImageIcon转成InpustStream
				for(int i=0; i<5; i++)
				{
					//inputStream[i] = imageIcons[i];
//					Image img = imageIcons[i].getImage();
//					BufferedImage tempBImg = ImageUtil.ImageToBufferedImage(img);
					ByteArrayOutputStream os = new ByteArrayOutputStream();   
					ImageIO.write(soyBufferedImages[i], "jpg", os);   
					int[] le2 = ImageUtil.getPixes(soyBufferedImages[i]);
					System.out.println("酱油图片的长度:" + le2.length);
					inputStream[i] = new ByteArrayInputStream(os.toByteArray());  
				}
				imageInfo.setInputstream(inputStream);
				
				//插入账户、密码和图片（返回插入的id）
				userIds = UserDao.doInsert(userSoy, conn, imageInfo);
			}

			
			//将ImageIcon转成InpustStream
			for(int i=0; i<5; i++)
			{
				//inputStream[i] = imageIcons[i];
				Image img = imageIcons[i].getImage();
				BufferedImage tempBImg = ImageUtil.ImageToBufferedImage(img);
				ByteArrayOutputStream os = new ByteArrayOutputStream();   
				ImageIO.write(tempBImg, "jpg", os);   
				inputStream[i] = new ByteArrayInputStream(os.toByteArray());  
			}
			imageInfo.setInputstream(inputStream);
			
			//插入用户的账户、密码和图片（返回插入的id）
			userIds = UserDao.doInsert(user, conn, imageInfo);
			
			
			//将每个图像的差值图像[像素][n] 转置成 [n][像素]
			double[][] mAveDeviation = LdaFeatures.getInstance().getAveDeviationDouble();
			double[][] mAveDeviationTrans = Features.matrixTrans(mAveDeviation);
			//将转置后的每个图像的差值图像存进数据库中
			ProjectDao.doinsertPeoplemean(conn, mAveDeviationTrans, userIds);
			
			
			//将每类的差值图像 [像素][n/num] 转置成 [n/num][像素]
			double[][] mi = LdaFeatures.getInstance().getAveDeviationEach();
			double[][] miTrans = Features.matrixTrans(mi);
			//将转置后的mi存进数据库中
			ProjectDao.doinsertclassmean(conn, miTrans, userIds);
			
			
			//第三次,增加进度条
			ProgressBarSignIn.frameProgressBarSignIn.startAddProgressBar();
			
//			//若有“酱油”，则将“酱油”的投影移除
//			double[][] insertModelP = new double[modelP.length-5][modelP[0].length];
//			if( haveSoy == true)
//			{
//				int cloneLength = modelP.length-5;
//				for(int i=0; i<cloneLength; i++)
//				{
//					insertModelP[i] = modelP[i+5];
//				}
//			}
			
			//封装用户Id和投影Z 进 Project
			Project project = new Project();
			project.setId(userIds);
//			if(haveSoy == true)
//			{
////				project.setProject(insertModelP);
//			}
//			else
			{
				project.setProject(modelP);
			}
			//插入所有投影
			ProjectDao.doinsertProject(conn, project);
			
			//第四次,增加进度条
			ProgressBarSignIn.frameProgressBarSignIn.startAddProgressBar();
			
		}
		catch(Exception e1)
		{
			e1.printStackTrace();
		}
		
		//提示数据库操作成功
		JOptionPane.showMessageDialog(null, "数据已插入数据库中！", "操作成功", JOptionPane.INFORMATION_MESSAGE);
		
	}
}
