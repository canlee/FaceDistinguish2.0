package com.invindible.facetime.algorithm.UiAlgorithm;

import java.awt.image.BufferedImage;
import java.sql.Connection;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.invindible.facetime.algorithm.LDA;
import com.invindible.facetime.algorithm.feature.Features;
import com.invindible.facetime.algorithm.feature.GetPcaLda;
import com.invindible.facetime.database.Oracle_Connect;
import com.invindible.facetime.database.ProjectDao;
import com.invindible.facetime.database.UserDao;
import com.invindible.facetime.model.LdaFeatures;
import com.invindible.facetime.model.Project;
import com.invindible.facetime.model.Wopt;
import com.invindible.facetime.wavelet.Wavelet;

public class ManagerMainUiAlgorithm {

	//删除数据后，对数据库中剩余的样本重新训练
	public static void TrainAfterDelete(Connection conn)
	{
		//设置人数，和每人的照片数（此处默认每人5张）
		int peopleNum = 2;//2是暂定的，需要根据数据库进行修改
		int photoNum = 5;
		
		//WoptT矩阵
		double[][] WoptT;
		//Project数据
		Project pr;
//		//保存从数据库获取的图片的数组
//		BufferedImage[] bImages = null;
		
		//用来PCA、LDA计算的数组
		ImageIcon[] icon = null;// = new ImageIcon[2*5];//[peopleNum*photoNum]
		
		try
		{
			conn = Oracle_Connect.getInstance().getConn();
			
			//1.读取所有样本的图片
			BufferedImage[] bImg = UserDao.doSelectAll(conn);
			
//			for(int i=0; i<bimg.length; i++)
//			{
//				int[] le = ImageUtil.getPixes(bimg[i]);
//
//				System.out.println("长度:" + le.length);
//			}
			
			//------------------------------peopleNum需要从数据库中获取---------------------------------------------
			//获取peopleNum
			peopleNum = bImg.length / 5;
			
			
			//对bImages[]的图片进行小波变换
			BufferedImage[] waveBImages = Wavelet.Wavelet(bImg);
			
			//2.训练（将 本人的5张照片 和 数据库中的所有照片（每人5张） 投影到WoptT上)
			GetPcaLda.getResult(waveBImages);
			
			//3.计算所有人的投影[N][C-1]
			double[][] modelP=new double[peopleNum*photoNum][peopleNum-1];
			for(int i=0;i<peopleNum*photoNum;i++){
				modelP[i]=LDA.getInstance().calZ(waveBImages[i]);//投影
			}
			
//			需要插入数据库的4个数据
//			1.(为了计算<2>所用)WoptT（从单例中获取）
			//double[] WoptT
			WoptT = LdaFeatures.getInstance().getLastProjectionT();
//			2.训练样例的投影（上面的modelP）
			//double[][] modelP
//			3.（投影Z的）N个人的，类内均值（每个人都有一个均值)
			double[][] modelMean=new double[peopleNum][peopleNum-1];
//			4.（投影Z的）总体均值
			double[] allMean=new double[peopleNum-1];
			
			//3.（投影Z的）N个人的，类内均值（每个人都有一个均值)
			//4.（投影Z的）总体均值
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
			
			//以下将所有训练完的数据存入数据库中
			
			//封装double[][] Wopt 进 Wopt wopt
			Wopt wopt = new Wopt();
			wopt.setWopt(WoptT);
			
			
			//将Wopt插入数据库中
			ProjectDao.doinsertWopt(conn, wopt);
			
			//将总体均值m插入数据库中
			double[] m = LdaFeatures.getInstance().getAveVector();
			ProjectDao.doinsertmean(conn, m);
			
			//获取所有用户ID
			int[] userIds = UserDao.selectAllIds(conn);
			
			
			//将每个图像的差值图像[像素][n] 转置成 [n][像素]
			double[][] mAveDeviation = LdaFeatures.getInstance().getAveDeviationDouble();
			double[][] mAveDeviationTrans = Features.matrixTrans(mAveDeviation);
			//将转置后的每个图像的差值图像存进数据库中
			ProjectDao.doinsertPeoplemean(conn, mAveDeviationTrans, userIds);
			
			System.out.println("userId如下：");
			for(int i=0; i<userIds.length; i++)
			{
				System.out.println("第 " + i + " 个userId: " + userIds[i]);
			}
			
			
			System.out.println("peopleMean的维数:" + mAveDeviationTrans.length + " " + mAveDeviationTrans[0].length);
			
			
			
			
			//将每类的差值图像 [像素][n/num] 转置成 [n/num][像素]
			double[][] mi = LdaFeatures.getInstance().getAveDeviationEach();
			double[][] miTrans = Features.matrixTrans(mi);
			//将转置后的mi存进数据库中
			ProjectDao.doinsertclassmean(conn, miTrans, userIds);
			
			System.out.println("classMean的维数：" + miTrans.length + " " + miTrans[0].length);
			
			//封装用户Id和投影Z 进 Project
			Project project = new Project();
			project.setId(userIds);
			project.setProject(modelP);
			//插入所有投影
			ProjectDao.doinsertProject(conn, project);
			
			System.out.println("modelP的维数:" + modelP.length + " " + modelP[0].length);
			
			//提示用户，已训练完
			JOptionPane.showMessageDialog(null, "数据库中剩余用户的特征已训练完!", "提示", JOptionPane.INFORMATION_MESSAGE	);
			
		}
		catch(Exception e1)
		{
			e1.printStackTrace();
		}
	}
	
}
