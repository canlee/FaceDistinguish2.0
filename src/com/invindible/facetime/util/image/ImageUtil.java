package com.invindible.facetime.util.image;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * 图片的帮助类
 * @author 李亮灿
 *
 */
public class ImageUtil {
	
	private ImageUtil() {;}
	
	/**
     * This method returns true if the specified image has transparent pixels
     * @param image
     * @return
     */
    public static boolean hasAlpha(Image image) {
        // If buffered image, the color model is readily available
        if (image instanceof BufferedImage) {
            BufferedImage bimage = (BufferedImage) image;
            return bimage.getColorModel().hasAlpha();
        }

        // Use a pixel grabber to retrieve the image's color model;
        // grabbing a single pixel is usually sufficient
        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
        }

        // Get the image's color model
        ColorModel cm = pg.getColorModel();
        return cm.hasAlpha();
    }

	/**
	 * image 类转化为 bufferedImage 类<br>
	 * @param img
	 * @return
	 */
	public static BufferedImage ImageToBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }

        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        // Determine if the image has transparent pixels; for this method's
        // implementation, see e661 Determining If an Image Has Transparent Pixels
        boolean hasAlpha = hasAlpha(image);

        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                    image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }

        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics g = bimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bimage;
	}
	
	/**
	 * 把该像素转化为RGB
	 * @param pixel	某个像素
	 * @return
	 */
	public static int[] getSplitRGB(int pixel) {
        int[] rgbs = new int[3];
        rgbs[ 0] = (pixel & 0xff0000) >> 16;
        rgbs[ 1] = (pixel & 0xff00) >> 8;
        rgbs[ 2] = (pixel & 0xff);
        return rgbs;
    }
	
	/**
	 * 把一张图片的像素点转化为RGB值
	 * @param pixel	格式是pixel[height][width]
	 * @return 返回的数组是[0][height][width] = R 
	 * 			<br>[1][height][width] = G 
	 * 			<br>[2][height][width] = B 
	 */
	public static int[][][] getSplitRGB(int[][] pixel) {
		int h = pixel.length;
		int w = pixel[0].length;
		int[][][] rgb = new int[3][h][w];
		int[] tmp;
		for(int i = 0;i < h;i++) {
			for(int j = 0;j < w;j++) {
				tmp = getSplitRGB(pixel[i][j]);
				rgb[0][i][j] = tmp[0];
				rgb[1][i][j] = tmp[1];
				rgb[2][i][j] = tmp[2];
			}
		}
		return rgb;
	}
	
	/**
	 * 把rgb值转化为像素点值
	 * @param rgb
	 * @return	
	 */
	public static int getSplitPixel(int[] rgb) {
		int pixel = rgb[2];
		pixel |= (rgb[1] << 8);
		pixel |= (rgb[0] << 16);
		pixel |= 0xff000000;
		return pixel;
	}
	
	/**
	 * 把一张图片的rgb值转化为像素点值
	 * @param rgb	数组是[0][height][width] = R 
	 * 			<br>[1][height][width] = G 
	 * 			<br>[2][height][width] = B 
	 * @return	返回的格式是pixel[height][width]
	 */
	public static int[][] getSplitPixel(int[][][] rgb) {
		int h = rgb[0].length;
		int w = rgb[0][0].length;
		int[][] pixel = new int[h][w];
		int tmp[] = new int[3];
		for(int i = 0;i < h;i++) {
			for(int j = 0;j < w;j++) {
				tmp[0] = rgb[0][i][j];
				tmp[1] = rgb[1][i][j];
				tmp[2] = rgb[2][i][j];
				pixel[i][j] = getSplitPixel(tmp);
			}
		}
		return pixel;
	}
	
	/**
	 * 获取某张图的所有的像素信息
	 * @param bimg
	 * @return
	 */
	public static int[] getPixes(BufferedImage bimg) {
        int w = bimg.getWidth();
        int h = bimg.getHeight();
        int[] rgbs = new int[h * w];
        bimg.getRGB(0, 0, w, h, rgbs, 0, w);
        return rgbs;
    }
	
	/**
	 * 获取某张图片的所有的像素的RGB值
	 * @param bimg
	 * @return	返回的数组是[0][height][width] = R 
	 * 			<br>[1][height][width] = G 
	 * 			<br>[2][height][width] = B 
	 */
	public static int[][][] getRGBMat(BufferedImage bimg) {
        int w = bimg.getWidth();
        int h = bimg.getHeight();
        int[][][] rgbmat = new int[3][h][w];
        int[] pixes = getPixes(bimg);
        int k = 0;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int[] rgb = getSplitRGB(pixes[k++]);
                rgbmat[0][y][x] = rgb[0];
                rgbmat[1][y][x] = rgb[1];
                rgbmat[2][y][x] = rgb[2];
            }
        }
        return rgbmat;
    }
	
	/**
	 * 根据图片的像素来获取图片
	 * @param width
	 * @param height
	 * @param pixels
	 * @return
	 */
	public static Image getImgByPixels(int width, int height, int[] pixels) {
		MemoryImageSource source;
        Image image = null;
        source = new MemoryImageSource(width, height, pixels, 0, width);
        image = Toolkit.getDefaultToolkit().createImage(source);
        return image;
	}
	
	/**
     * 根据rgb阵返回图片
     * @param rgbmat
     * @return
     */
    public static BufferedImage getImgByRGB(int[][][] rgbmat) {
        int w = rgbmat[0][0].length, h = rgbmat[0].length;
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int r = rgbmat[0][y][x]<<16, g = rgbmat[1][y][x]<<8, b = rgbmat[2][y][x];
                int pixel = 0xff000000 | r | g | b;
                //int pixel = r << 16 | g << 8 | b;
                image.setRGB(x, y, pixel);
            }
        }
        return image;
    }
    
    /**
     * rgb矩阵彩图转化为YCbCr矩阵图像
     * @param rgbmat	数组是[0][height][width] = R 
	 * 			<br>[1][height][width] = G 
	 * 			<br>[2][height][width] = B 
	 * 
     * @return	数组是[0][height][width] = Y 
	 * 			<br>[1][height][width] = Cb 
	 * 			<br>[2][height][width] = Cr 
     */
    public static int[][][] RGBToYCbCr(int[][][] rgbmat) {
    	int h = rgbmat[0].length;
		int w = rgbmat[0][0].length;
		int[][][] result = new int[3][h][w];
		for(int i = 0;i < h;i++) {
			for(int j = 0;j < w;j++) {
				result[0][i][j] = (30 * rgbmat[0][i][j] + 
						59 * rgbmat[1][i][j] + 
						11 * rgbmat[2][i][j]) / 100;
				result[1][i][j] = (-17 * rgbmat[0][i][j] - 
						33 * rgbmat[1][i][j] + 
						50 * rgbmat[2][i][j]) / 100 + 128;
				result[2][i][j] = (50 * rgbmat[0][i][j] - 
						42 * rgbmat[1][i][j] - 
						8 * rgbmat[2][i][j]) / 100 + 128;
			}
		}
		return result;
    }
    
    /**
	 * 图片灰度处理
	 * @param imgRGB	图片的RGB矩阵
	 * @return 返回的数组是[0][height][width] = R 
	 * 			<br>[1][height][width] = G 
	 * 			<br>[2][height][width] = B 
	 */
	public static int[][][] imgToGray(int[][][] imgRGB) {
		int h = imgRGB[0].length;
		int w = imgRGB[0][0].length;
		int[][][] grayMat = new int[3][h][w];
		int gray;
		for(int i = 0;i < h;i++) {
			for(int j = 0;j < w;j++) {
				gray = (imgRGB[0][i][j] * 30 + 
						imgRGB[1][i][j] * 59 + 
						imgRGB[2][i][j] * 11) / 100;
				for(int k = 0;k < 3;k++) {
					grayMat[k][i][j] = gray;
				}
			}
		}
		return grayMat;
	}
	
	/**
	 * 图片灰度处理，主要用户中间算法实现，节省空间用
	 * @param imgRGB	图片的RGB矩阵
	 * @return  返回的数组为中每个数为对应坐标上的像素灰度值
	 */
	public static int[][] imgToGrayByTest(int[][][] imgRGB) {
		int h = imgRGB[0].length;
		int w = imgRGB[0][0].length;
		int[][] grayMat = new int[h][w];
		int gray;
		for(int i = 0;i < h;i++) {
			for(int j = 0;j < w;j++) {
				gray = (imgRGB[0][i][j] * 30 + 
						imgRGB[1][i][j] * 59 + 
						imgRGB[2][i][j] * 11) / 100;
				grayMat[i][j] = gray;
			}
		}
		return grayMat;
	}
	
	
	/**
	 * 从文件中读取图片
	 * @param path
	 * @return
	 */
	public static BufferedImage getImageFromFile(String path) {
		try {
			BufferedImage bfi = ImageIO.read(new File(path));
			return bfi;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 图片缩放
	 * @param in
	 * @param sx	横坐标缩放比例
	 * @param sy	纵坐标缩放比例
	 * @param interpolationType
	 * @return
	 */
	public static BufferedImage imageScale(
			BufferedImage in, double sx, double sy, int interpolationType){  
        AffineTransform matrix=new AffineTransform(); //仿射变换   
        matrix.scale(sx,sy);  
        AffineTransformOp op = null;  
        if (interpolationType==1){  
            op=new AffineTransformOp(matrix, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);  
        }else if (interpolationType==2){  
            op=new AffineTransformOp(matrix, AffineTransformOp.TYPE_BILINEAR);  
        }else if (interpolationType==3){  
            op=new AffineTransformOp(matrix, AffineTransformOp.TYPE_BICUBIC);  
        }else{  
            try {  
                throw new Exception("input interpolation type from 1-3 !");  
            } catch (Exception e) {  
                // TODO Auto-generated catch block   
                e.printStackTrace();  
            }  
        }  
        int width = (int)((double)in.getWidth() * sx);  
        int height = (int)((double)in.getHeight() * sy);  
        BufferedImage dstImage = new BufferedImage(width, height, in.getType());  
        //System.out.println(in.getType());   
        //BufferedImage dstImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);   
        //BufferedImage out=op.filter(in, dstImage);   
        op.filter(in, dstImage);  
        //注意下面的代码不同, 结果背景颜色不同   
        //BufferedImage out=op.filter(in, null);   
        return dstImage;  
    }
	
	/**
	 * 图片缩放
	 * @param in
	 * @param sx	横坐标缩放比例
	 * @param sy	纵坐标缩放比例
	 * @return
	 */
	public static BufferedImage imageScale(BufferedImage in, double sx, double sy){
		return imageScale(in, sx, sy, 3);
    }
	
	/**
	 * 图片缩放
	 * @param in
	 * @param width	目标图片的宽度
	 * @param height	目标图片的高度
	 * @return
	 */
	public static BufferedImage imageScale(BufferedImage in, int width, int height) {
		double originW = in.getWidth();
		double originH = in.getHeight();
		double sx = width / originW;
		double sy = height / originH;
		return imageScale(in, sx, sy);
	}
	
	
	/**
	 * 保存图片到本地硬盘
	 * @param bi
	 * @param path	图片路径，格式为jpg命名，例如D:/123.jpg
	 * @return	保存成功返回true
	 */
	public static boolean saveImage(BufferedImage bi, String path) {
		try {
			ImageIO.write(bi, "jpg", new File(path));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
