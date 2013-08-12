package com.invindible.facetime;

import javax.media.Player;

import com.invindible.facetime.model.adaboost.Detector;

/**
 * 公共数据
 * <br>各种单例
 * @author 李亮灿
 *
 */
public class CommonData {
	
	/**
	 * 训练器的数据
	 * @author 李亮灿
	 *
	 */
	public static class HarrCascade {
		
		/**
		 * 人脸检测训练器
		 */
		public static Detector detector;
		
		/**
		 * 是否正在解析训练器
		 */
		public static boolean isParsering = false;
		
		/**
		 * 解析时候的同步锁同步锁
		 */
		public static Byte lock = 0;
		
	}
	
	public static class Camera {
		
		/**
		 * 摄像头的播放器
		 */
		public static Player player;
		
	}
	
}
