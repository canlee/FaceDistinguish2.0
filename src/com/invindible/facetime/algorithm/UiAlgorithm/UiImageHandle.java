package com.invindible.facetime.algorithm.UiAlgorithm;

import java.awt.Image;

import javax.swing.ImageIcon;

public class UiImageHandle {

	//图片等比例处理方法,width和height为宽度和高度
	public static ImageIcon ImageHandle(ImageIcon imageicon,int width,int height){
		Image image = imageicon.getImage();
		Image smallimage = image.getScaledInstance(width, height, image.SCALE_FAST);
		ImageIcon smallicon = new ImageIcon(smallimage);
		return smallicon;
	}
}
