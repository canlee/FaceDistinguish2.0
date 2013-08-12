package com.invindible.facetime.ui.widget.image;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * 用于显示image图片的控件
 * @author 李亮灿
 *
 */
public class ImagePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private Image image;					//要显示的图片
	private BufferedImage bufferImage;
	
	public ImagePanel() {
	}
	
	/**
	 * 
	 * @param img	要显示出来的图片
	 */
	public ImagePanel(Image img) {
		this.image = img;
		this.bufferImage = null;
	}
	
	/**
	 *
	 * @param buffImg	要显示出来的图片缓冲
	 */
	public ImagePanel(BufferedImage buffImg) {
		this.bufferImage = buffImg;
		this.image = null;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
		this.bufferImage = null;
		updateUI();
	}

	public BufferedImage getBufferImage() {
		return bufferImage;
	}

	public void setBufferImage(BufferedImage bufferImage) {
		this.bufferImage = bufferImage;
		this.image = null;
		updateUI();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
        try {
        	if(image != null && bufferImage == null) {
        		g2.drawImage(image, 0, 0, null);
        	}
        	else if(bufferImage != null && image == null) {
        		g2.drawImage(bufferImage, 0, 0, null);
        	}
        } finally {
            g2.dispose();
        }
	}
	
}
