package com.invindible.facetime.task.video;

import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Vector;

import javax.media.Buffer;
import javax.media.CannotRealizeException;
import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.control.FrameGrabbingControl;
import javax.media.format.VideoFormat;
import javax.media.util.BufferToImage;

import com.invindible.facetime.CommonData;
import com.invindible.facetime.task.Task;
import com.invindible.facetime.task.interfaces.Context;
import com.invindible.facetime.util.image.ImageUtil;


/**
 * 打开摄像头，并且不断把视频流储存到一个队列里面
 * 
 * @author canlee
 * 
 */
public class VideoStreamTask extends Task {

	/**
	 * 打开摄像头成功<br>
	 * 在onRefresh中会返回相应显示摄像视频的控件Component类
	 * @see Component
	 */
	public final static int OPEN_CAMERA_SUCCESS = 10000;
	
	/**
	 * 打开摄像头失败
	 */
	public final static int OPEN_CAMERA_FAIL = 10001;
	
	/**
	 * 摄像头
	 */
	private Player player = null;

	public VideoStreamTask(Context context) {
		super(context);
		if(CommonData.Camera.player != null) {
			player = CommonData.Camera.player;
		}
//		else {
//			DLLFactory.loadJmfDLL(VideoPrintScreenTask.class);
//		}
	}

	/**
	 * 打开摄像头，打开失败则抛出异常
	 * @return 打开成功返回true，否则返回false
	 * @throws NoPlayerException
	 * @throws CannotRealizeException
	 * @throws IOException
	 */
	private boolean openCamera() {
		CaptureDeviceInfo deviceInfo = null;
		MediaLocator mediaLocator = null;
		VideoFormat currentFormat = null;
		if(player != null && player.getState() == Player.Started) {
			return true;
		}
		if (player == null || player.getState() != Player.Started) {
			try {
				@SuppressWarnings("rawtypes")
				Vector deviceList = CaptureDeviceManager.getDeviceList(null);
				if (deviceList != null) {
					// 获取驱动
					for (int i = 0; i < deviceList.size(); i++) {
						deviceInfo = (CaptureDeviceInfo) deviceList
								.elementAt(i);
						if (deviceInfo.getName().startsWith("vfw")) {
							Format[] vedioFormat = deviceInfo.getFormats();
							for (int j = 0; j < vedioFormat.length; i++) {
								if (vedioFormat[i] instanceof VideoFormat) {
									currentFormat = (VideoFormat) vedioFormat[i];
									break;
								}
							}
							if (currentFormat != null) {
								// 开启摄像头
								mediaLocator = deviceInfo.getLocator();
								player = Manager.createRealizedPlayer(mediaLocator);
								player.start();
								CommonData.Camera.player = player;
								return true;
							}
						}
					}
				}
			} catch (NoPlayerException e) {
				e.printStackTrace();
			} catch (CannotRealizeException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * 关闭摄像头
	 */
	public void closeCamera() {
		if(player != null) {
			player.stop();
			player.close();
			player = null;
		}
	}
	
	/**
	 * 获取当前摄像头此刻所照到的照片
	 * @return	如果摄像头已经开启，则返回图片，否则返回null
	 */
	public Image getCurrentImage() {
		if(player.getState() == Player.Started) {
			FrameGrabbingControl fgc = (FrameGrabbingControl) player.getControl(
					"javax.media.control.FrameGrabbingControl");
			Buffer buffer = fgc.grabFrame();
			BufferToImage bti = new BufferToImage((VideoFormat) buffer.getFormat());
			Image img = bti.createImage(buffer);
			return img;
		}
		return null;
	}
	
	/**
	 * 获取当前摄像头此刻所照到的照片缓冲
	 * @return	如果摄像头已经开启，则返回图片，否则返回null
	 */
	public BufferedImage getCurrentBufferedImage() {
		Image img = getCurrentImage();
		if(img != null) {
			return ImageUtil.ImageToBufferedImage(img);
		}
		return null;
	}
	

	/**
	 * 打开摄像头，并获取显示视频的控件
	 */
	@Override
	protected void doTask() {
		if (openCamera()) {
			//打开摄像头成功
			Component component = player.getVisualComponent();
			context.onRefresh(OPEN_CAMERA_SUCCESS, component);
		}
		else {
			//打开摄像头失败
			context.onRefresh(OPEN_CAMERA_FAIL);
		}
	}

	/**
	 * 关闭摄像头
	 */
	@Override
	public void stopTask() {
		if(player != null && player.getState() == Player.Started) {
			player.stop();
			player.close();
		}
	}

}
