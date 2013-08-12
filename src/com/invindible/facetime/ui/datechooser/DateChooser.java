/**
 *  [DateChooser.java]
 *
 *  Java ����ѡ��ؼ�(�������)
 *
 *
 * �������ڣ�(2003-10-25)
 * @author��ONE_Fox
 * @author��ONE_Fox@163.com
 */
 
 
package com.invindible.facetime.ui.datechooser;
 
import java.awt.*;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
 
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
 
 
 
 
public class DateChooser extends JDialog {
    
    //״̬����-----------------------//
    
    private static boolean isShow = false;  //�Ƿ�������ʾ
    
    
    //�������-----------------------//
    private static Calendar showMonth = new GregorianCalendar(); //Ĭ�����Խ���
    
    private int startYear = 1950; //Ĭ�ϡ���С����ʾ���
    private int lastYear = 2050;  //Ĭ�ϡ������ʾ���
    
 
    //��������-----------------------//
    
    //��TablePanel�� ConfigLine �м��
    JPanel rootPanel = new JPanel(new BorderLayout(), true);
    
    //TablePanel ������ʾ���
    private TablePanel tablePanel = null;
 
    //ConfigLine ������
    private ConfigLine configLine = null;
    
    //���� ��С/λ��
    /**
     * �����Ľ����С����
     */
    public static final int width = 190;  //������
    public static final int height = 170; //����߶�
    
    private int local_X = 0;  //��ʾλ�� X ���
    private int local_Y = 0;  //��ʾλ�� Y ���
    
 
 
 
 
//------���췽��--------------------------------------------------//

/**
 * ���췽��1
 */
    public DateChooser() {
        
        makeFace(); //��������
    }
    
    
    
    
/**
 * ���췽��2
 *
 * @param owner java.awt.Frame
 */
    public DateChooser(Frame owner) {
        
        super(owner, "DateChooser", true); //���ø�����
        makeFace(); //��������

    }
    
 
 
 
/**
 * ���췽��3
 *
 * @param owner java.awt.Frame
 * @param showMonth java.util.Calendar
 * @param startYear int
 * @param lastYear int
 */
    public DateChooser(Frame owner, Calendar showMonth, int startYear, 
                                                         int lastYear) {
        super(owner, "DateChooser", true);
        
        this.showMonth = showMonth;
        this.startYear = startYear;
        this.lastYear = lastYear;
        
        makeFace(); //��������
    }
 
 
 
/**
 * ���췽��4
 *
 * @param showMonth java.util.Calendar
 * @param startYear int
 * @param lastYear int
 */
    public DateChooser(Calendar showMonth, int startYear, int lastYear) {
        
        super((Frame)null, "DateChooser", true);
        
        this.showMonth = showMonth;
        this.startYear = startYear;
        this.lastYear = lastYear;
        
        makeFace(); //��������
    }
    
    
    
    
//------��������---------------------------------------------------//
 
/**
 * ����:��������
 */
    private void makeFace() {
        
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        setResizable(false);  //�����С�޷��ı�
        
        //���湹��---------------------------//
        tablePanel = new TablePanel(this, showMonth);
        tablePanel.setBackground(Color.PINK);
        configLine = new ConfigLine(tablePanel, showMonth, 
                                    startYear, lastYear); 
        configLine.setBackground(Color.PINK);
        
        
        //������������-----------------------//
        setSize(width, height);
        
        rootPanel.setBorder(new LineBorder(Pallet.backGroundColor, 2));
        rootPanel.setBackground(Pallet.backGroundColor);
        
        
        
        //���岼��---------------------------//
        rootPanel.add(tablePanel, BorderLayout.CENTER);
        rootPanel.add(configLine, BorderLayout.SOUTH);
        
        getContentPane().add(rootPanel, BorderLayout.CENTER);
    }
 
 
 
/**
 * ��������ʾ����
 *
 * @param invoker javax.swing.Component
 * @param x int
 * @param y int
 *
 * @return Date
 */
    public Date showChooser(JComponent invoker, int x, int y) {
        
        Point invokerOrigin;
        
        if (invoker != null) {
            
            if(isShow == true)
                setVisible(false);
                
            invokerOrigin = invoker.getLocationOnScreen();
                
            setLocation(invokerOrigin.x + x, invokerOrigin.y + y);
            
        } else {
            
            if(isShow == true)
                setVisible(false);
                
            setLocation(x, y);
        }
        
        setVisible(true);
        isShow = true;
        
        
        return tablePanel.getDate();
    }
    
    
    
/**
 * ��������������
 */
    public void hideChooser() {
        setVisible(false);
    }
    
    
    
/**
 * ������ȡ��ѡ������
 */
    public Date getDate() {
        return tablePanel.getDate();
    }
}