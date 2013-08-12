package test;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import com.invindible.facetime.util.image.ImageUtil;

/**
 * 训练的准备
 * @author 李亮灿
 *
 */
public class TrainReady {
	
	public static int scaleImage(String mainPath) {
		File path = new File(mainPath);
//		String[] allPic = path.list();
		File[] allFile = path.listFiles();
		int size = allFile.length;
		int count = 0;
		for(int i = 0; i < size; i++) {
			String pic = allFile[i].getAbsolutePath();
			if(pic.endsWith(".db")) {
				continue;
			}
			if(allFile[i].isFile()) {
				BufferedImage img = ImageUtil.getImageFromFile(pic);
				img = ImageUtil.imageScale(img, 20, 20);
				ImageUtil.saveImage(img, pic);
				count++;
				System.out.println(pic);
			}
			else {
				count += scaleImage(pic);
			}
		}
		return count;
	}
	
	public static int writePostImageTxt(String mainPath, PrintWriter pw, String txtPath) {
		int count = 0;
		File path = new File(mainPath);
		File[] allFile = path.listFiles();
		int size = allFile.length;
		for(int i = 0; i < size; i++) {
			String pic = allFile[i].getAbsolutePath();
			if(pic.endsWith(".db")) {
				continue;
			}
			if(allFile[i].isFile()) {
				String txt = txtPath + "/" + allFile[i].getName() + " 1 0 0 20 20";
				pw.println(txt);
				count++;
				System.out.println(txt);
			}
			else {
				String newTxtPath = txtPath + "/" + allFile[i].getName();
				count += writePostImageTxt(pic, pw, newTxtPath);
			}
		}
		return count;
	}
	
	public static void negImageProcess(String mainPath) {
		File path = new File(mainPath);
		File[] allFile = path.listFiles();
		int size = allFile.length;
		for(int i = 0; i < size; i++) {
			if(allFile[i].isDirectory()) {
				continue;
			}
			String pic = allFile[i].getAbsolutePath();
			BufferedImage tmp = ImageUtil.getImageFromFile(pic);
			tmp = ImageUtil.imageScale(tmp, 20, 20);
			tmp = ImageUtil.getImgByRGB(ImageUtil.imgToGray(ImageUtil.getRGBMat(tmp)));
			ImageUtil.saveImage(tmp, mainPath + "/result/" +allFile[i].getName().replace("jpg", "bmp"));
		}
	}
	
	public static int writeNegImageTxt(String mainPath, PrintWriter pw, String txtPath) {
		int count = 0;
		File path = new File(mainPath);
		File[] allFile = path.listFiles();
		int size = allFile.length;
		for(int i = 0; i < size; i++) {
			String pic = allFile[i].getAbsolutePath();
			if(pic.endsWith(".db")) {
				continue;
			}
			if(allFile[i].isFile()) {
				String txt = txtPath + "/" + allFile[i].getName();
				pw.println(txt);
				count++;
				System.out.println(txt);
			}
			else {
				String newTxtPath = txtPath + "/" + allFile[i].getName();
				count += writeNegImageTxt(pic, pw, newTxtPath);
			}
		}
		return count;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		System.out.println(scaleImage(mainPath));
//		try {
//			OutputStream out = new FileOutputStream(new File("F:/资料/训练/pos_image.txt"));
//			PrintWriter pw = new PrintWriter(out);
//			System.out.println(writePostImageTxt("F:/资料/训练/pos_image", pw, "pos_image"));
//			pw.flush();
//			pw.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//		try {
//			OutputStream out = new FileOutputStream(new File("F:/资料/训练/neg_image.txt"));
//			PrintWriter pw = new PrintWriter(out);
//			System.out.println(writeNegImageTxt("F:/资料/训练/neg_image", pw, "neg_image"));
//			pw.flush();
//			pw.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//		negImageProcess("F:/资料/nofaces");
	}

}
