package com.invindible.facetime.task.init;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.invindible.facetime.CommonData;
import com.invindible.facetime.model.adaboost.Detector;
import com.invindible.facetime.model.adaboost.Rectangle;
import com.invindible.facetime.model.adaboost.StrongClassifier;
import com.invindible.facetime.model.adaboost.WeekClassifier;
import com.invindible.facetime.task.Task;
import com.invindible.facetime.task.interfaces.Context;

/**
 * 训练器的解析导入
 * @author 李亮灿
 *
 */
public class HarrCascadeParserTask extends Task {
	
	/**
	 * 解析错误
	 */
	public static final int PARSER_FAIL = 11000;

	/**
	 * 解析成功
	 */
	public static final int PARSER_SUCCESS = 11001;
	
	private static final String CONFIG_XML = "config/haarcascade_frontalface_default.xml";
	
	private Detector detector;
	private Document document;
	
	public HarrCascadeParserTask(Context context) {
		super(context);
	}
	
	/**
	 * 读取配置xml文件
	 * @return	成功返回true
	 */
	private boolean loadDocument() {
		try {
			InputStream in = new FileInputStream(new File(CONFIG_XML));
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.parse(in);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (SAXException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 解析size
	 * @return	解析成功返回true
	 */
	private boolean parserSize() {
		NodeList nodeList = document.getElementsByTagName("size");
		if(nodeList == null || nodeList.getLength() == 0) {
			return false;
		}
		Element sizeEle = (Element) nodeList.item(0);
		Scanner scanner = new Scanner(sizeEle.getTextContent());
		detector.setWidth(scanner.nextInt());
		detector.setHeight(scanner.nextInt());
		return true;
	}
	
	/**
	 * 解析强分类器的阈值
	 * @param ele
	 * @param scf
	 * @return
	 */
	private boolean parserStrongThreshold(Node ele, StrongClassifier scf) {
		try {
			double threshold = Double.parseDouble(ele.getTextContent());
			scf.setThreshold(threshold);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 解析弱分类器里的特征矩阵
	 * @param root
	 * @param wcf
	 * @return
	 */
	private boolean parserRectangle(Node root, WeekClassifier wcf) {
		NodeList nodeList = root.getChildNodes();
		if(nodeList == null || nodeList.getLength() == 0) {
			return false;
		}
		Node node;
		for(int i = 0; i < nodeList.getLength(); i++) {
			node = nodeList.item(i);
			if(node.getNodeName().equals("rects")) {
				node = node.getFirstChild();
				while(node != null) {
					if(node.getNodeName().equals("_")) {
						Rectangle rect = new Rectangle();
						Scanner sc = new Scanner(node.getTextContent().replace(".", ""));
						try {
							rect.setX(sc.nextInt());
							rect.setY(sc.nextInt());
							rect.setWidth(sc.nextInt());
							rect.setHeight(sc.nextInt());
							rect.setWeight(sc.nextInt());
						} catch (Exception e) {
							e.printStackTrace();
							return false;
						}
						wcf.addRectangle(rect);
					}
					node = node.getNextSibling();
				}
			}
		}
		return true;
	}
	
	/**
	 * 解析弱分类器
	 * @param node
	 * @param scf
	 * @return
	 */
	private boolean parserWeekClassifier(Node root, StrongClassifier scf) {
		NodeList nodeList = root.getChildNodes();
		if(nodeList == null || nodeList.getLength() == 0) {
			return false;
		}
		for(int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if(node.getNodeName().equals("_")) {
				if(node.getChildNodes() == null || node.getChildNodes().getLength() == 0) {
					return false;
				}
				node = node.getFirstChild();
				while(node != null) {
					if(node.getNodeName().equals("_")) {
						break;
					}
					node = node.getNextSibling();
				}
				if(node.getChildNodes() == null || node.getChildNodes().getLength() == 0) {
					return false;
				}
				WeekClassifier wcf = new WeekClassifier();
				node = node.getFirstChild();
				while(node != null) {
					if(node.getNodeName().equals("threshold")) {
						try {
							double threshold = Double.parseDouble(node.getTextContent());
							wcf.setThreshold(threshold);
						} catch (Exception e) {
							e.printStackTrace();
							return false;
						}
					}
					else if(node.getNodeName().equals("left_val")) {
						try {
							double leftVal = Double.parseDouble(node.getTextContent());
							wcf.setLeftValue(leftVal);
						} catch (Exception e) {
							e.printStackTrace();
							return false;
						}
					}
					else if(node.getNodeName().equals("right_val")) {
						try {
							double rightVal = Double.parseDouble(node.getTextContent());
							wcf.setRightValue(rightVal);
						} catch (Exception e) {
							e.printStackTrace();
							return false;
						}
					}
					else if(node.getNodeName().equals("feature")) {
						if(!parserRectangle(node, wcf)) {
							return false;
						}
					}
					node = node.getNextSibling();
				}
				scf.addWeekClassifier(wcf);
			}
		}
		return true;
	}
	
	/**
	 * 解析强分类器
	 * @return	解析成功返回true
	 */
	private boolean parserStrongClassifier() {
		NodeList nodeList = document.getElementsByTagName("stages");
		if(nodeList == null || nodeList.getLength() == 0) {
			return false;
		}
		nodeList = ((Element) nodeList.item(0)).getChildNodes();
		if(nodeList == null || nodeList.getLength() == 0) {
			return false;
		}
		for(int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if(node.getNodeName().equals("_")) {
				if(node.getChildNodes() == null || node.getChildNodes().getLength() == 0) {
					return false;
				}
				StrongClassifier scf = new StrongClassifier();
				node = node.getFirstChild();
				while(node != null) {
					if(node.getNodeName().equals("stage_threshold")) {
						if(!parserStrongThreshold(node, scf)) {
							return false;
						}
					}
					else if(node.getNodeName().equals("trees")) {
						if(!parserWeekClassifier(node, scf)) {
							return false;
						}
					}
					node = node.getNextSibling();
				}
				detector.addStrongClassifier(scf);
			}
		}
		return true;
	}
	

	@Override
	protected void doTask() {
		synchronized (CommonData.HarrCascade.lock) {
			CommonData.HarrCascade.isParsering = true;
			CommonData.HarrCascade.detector = new Detector();
			detector = CommonData.HarrCascade.detector;
			if(loadDocument()) {
				if(parserSize()) {
					if(parserStrongClassifier()) {
						context.onRefresh(PARSER_SUCCESS);
					}
					else {
						context.onRefresh(PARSER_FAIL);
					}
				}
				else {
					context.onRefresh(PARSER_FAIL);
				}
			}
			else {
				context.onRefresh(PARSER_FAIL);
			}
			CommonData.HarrCascade.isParsering = false;
			CommonData.HarrCascade.lock.notifyAll();
		}
	}

	/**
	 * 不实现
	 */
	@Override
	public void stopTask() {
		
	}

}
