package com.invindible.facetime.model.adaboost;

import java.util.ArrayList;
import java.util.List;

/**
 * 强分类器
 * @author 李亮灿
 *
 */
public class StrongClassifier {
	
	private List<WeekClassifier> weekClassifierList;	//该强分类器的弱分类器集合
	private double threshold;							//该强分类器的阈值
	
	public StrongClassifier() {
		weekClassifierList = new ArrayList<WeekClassifier>();
	}
	
	/**
	 * 该强分类器的弱分类器集合
	 * @return
	 */
	public List<WeekClassifier> getWeekClassifierList() {
		return weekClassifierList;
	}
	
	/**
	 * 该强分类器的弱分类器集合
	 * @param weekClassifierList
	 */
	public void setWeekClassifierList(List<WeekClassifier> weekClassifierList) {
		this.weekClassifierList = weekClassifierList;
	}
	
	/**
	 * 该强分类器的阈值
	 * @return
	 */
	public double getThreshold() {
		return threshold;
	}
	
	/**
	 * 该强分类器的阈值
	 * @param threshold
	 */
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}
	
	/**
	 * 在弱分类器类表后面添加一个新的弱分类器
	 * @param weekClassifier
	 */
	public void addWeekClassifier(WeekClassifier weekClassifier) {
		this.weekClassifierList.add(weekClassifier);
	}
	
	/**
	 * 返回指定下标位置的弱分类器
	 * @param index
	 * @return
	 */
	public WeekClassifier getWeekClassifier(int index) {
		return this.weekClassifierList.get(index);
	}
	
}
