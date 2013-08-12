/**
 *  [ConfigLine.java]
 *  Download by http://www.codefans.net
 *  ��������
 *
 * �������ڣ�(2003-10-25)
 * @author��ONE_Fox
 * @author��ONE_Fox@163.com
 */
 
 
package com.invindible.facetime.ui.datechooser;
 
import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;
 
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.text.SimpleDateFormat;
 
 
class ConfigLine extends JPanel {
    
    //�������--------------------//
    private TablePanel tablePanel = null;
    
    private Calendar showMonth = null; //��ǰ��ʾ���·�
    private int startYear = 0; //����С����ʾ���
    private int lastYear = 0;  //�������ʾ���
    
    private int nowYear = 0;   //��ǰ���
    private int nowMonth = 0;  //��ǰ�·�
    
    /**
     * ��ʱ����RoundBox���ٷ���
     */
    Timer timer = new Timer(true);
    
    
    //�������------------------//
    private RoundBox yearBox = null;
    private RoundBox monthBox = null;
    
    private JLabel txtYear = new JLabel("年");
    private JLabel txtMonth = new JLabel("月");
 
 
 
 
//------���췽��/����-----------------------------------------------//
 
 
/**
 * ���췽��
 *
 * @param tablePanel TablePanel
 * @param showMonth java.util.Calendar
 * @param startYear int
 * @param lastYear int
 */
    ConfigLine(TablePanel tablePanel, Calendar showMonth, 
                                      int startYear, int lastYear) {
                                        
        this.tablePanel = tablePanel;
        
        this.showMonth = showMonth;
        this.startYear = startYear;
        this.lastYear = lastYear;
        
        nowYear = Integer.valueOf(new SimpleDateFormat("yyyy")
                                    .format(showMonth.getTime())).intValue();
        nowMonth = Integer.valueOf(new SimpleDateFormat("M")
                                    .format(showMonth.getTime())).intValue();
        
        yearBox = new RoundBox(nowYear, startYear, lastYear);
        monthBox = new RoundBox(nowMonth, 1, 12);
                                             
        makeFace();    //��������
        addListener(); //����¼�����
    }
    
    
    
    
//------����/����---------------------------------------------------//
 
/**
 * ��������������
 */
    private void makeFace() {
        
        Font txtFont = new Font("����", Font.PLAIN, 12);
        
        //��������-----------------------//
        this.setBorder(null);
        this.setBackground(Pallet.configLineColor);
        this.setLayout(new FlowLayout(1, 7, 1));
        this.setPreferredSize(new Dimension(50, 19));
        
        //��ǩ����---------------//
        txtYear.setForeground(Pallet.cfgTextColor);
        txtYear.setPreferredSize(new Dimension(14, 14));
        txtYear.setFont(txtFont);
        
        txtMonth.setForeground(Pallet.cfgTextColor);
        txtMonth.setPreferredSize(new Dimension(14, 14));
        txtMonth.setFont(txtFont);
        
        monthBox.setShowWidth(17);
        
        //���岼��-----------------------//
        add(yearBox);
        add(txtYear);
        add(monthBox);
        add(txtMonth);
    }
 
 
 
 
/**
 * ����������¼�����
 */
    private void addListener() {
        
        //yearBox �¼�����-------------------------//
        
        yearBox.bt_UP.addMouseListener(new MouseAdapter() {
            
            //yearBox.bt_UP ����
            public void mousePressed(MouseEvent e) {
                btPressed(yearBox, 1);
            }
            
            //yearBox.bt_UP ����
            public void mouseReleased(MouseEvent e) {
                btReleased(yearBox, 1);
                nowYear = yearBox.showNow;
                
                tablePanel.setMonth(nowYear, nowMonth);
            }
        });
        
        yearBox.bt_DOWN.addMouseListener(new MouseAdapter() {
            
            //yearBox.bt_DOWN ����
            public void mousePressed(MouseEvent e) {
                btPressed(yearBox, 2);
            }
            
            //yearBox.bt_DOWN ����
            public void mouseReleased(MouseEvent e) {
                btReleased(yearBox, 2);
                nowYear = yearBox.showNow;
                
                tablePanel.setMonth(nowYear, nowMonth);
            }
        });
        
        
        
        //monthBox �¼�����------------------------//
        
        monthBox.bt_UP.addMouseListener(new MouseAdapter() {
            
            //monthBox.bt_UP ����
            public void mousePressed(MouseEvent e) {
                btPressed(monthBox, 1);
            }
            
            //monthBox.bt_UP ����
            public void mouseReleased(MouseEvent e) {
                btReleased(monthBox, 1);
                nowMonth = monthBox.showNow;
                
                tablePanel.setMonth(nowYear, nowMonth);
            }
        });
        
        monthBox.bt_DOWN.addMouseListener(new MouseAdapter() {
            
            //monthBox.bt_DOWN ����
            public void mousePressed(MouseEvent e) {
                btPressed(monthBox, 2);
            }
            
            //monthBox.bt_DOWN ����
            public void mouseReleased(MouseEvent e) {
                btReleased(monthBox, 2);
                nowMonth = monthBox.showNow;
                
                tablePanel.setMonth(nowYear, nowMonth);
            }
        });
    }
    
    
    
    
/**
 * RoundBox ͳһ��ť��������
 *
 * @param box RoundBox
 * @param theBT int
 */
    private void btPressed(RoundBox box, int theBT) {
        final RoundBox theBox = box;
        
        if(theBT == 1) {  //"+"��ť
            timer = new Timer(true);
            timer.schedule(new TimerTask() {
                public void run() {
                    if(theBox.showNow < theBox.showMax) {
                        theBox.showing.setText("" + (theBox.showNow+1));
                        
                        theBox.showNow++;
                    }
                }
            }, 500, 100);
        }
        else if(theBT == 2) {  //"-"��ť
            timer = new Timer(true);
            timer.schedule(new TimerTask() {
                public void run() {
                    if(theBox.showNow > theBox.showMin) {
                        theBox.showing.setText("" + (theBox.showNow-1));
                        
                        theBox.showNow--;
                    }
                }
            }, 500, 100);
        }
    }
 
 
 
 
/**
 * RoundBox ͳһ��ť��������
 *
 * @param box RoundBox
 * @param theBT int
 */
    private void btReleased(RoundBox box, int theBT) {
        
        final RoundBox theBox = box;
        
        timer.cancel();
        
        if(theBT == 1) {  //"+"��ť
            if(theBox.showNow < theBox.showMax) {
                theBox.showing.setText("" + (theBox.showNow+1));
                        
                theBox.showNow++;
            }
        }
        
        else if(theBT == 2) {  //"-"��ť
            if(theBox.showNow > theBox.showMin) {
                theBox.showing.setText("" + (theBox.showNow-1));
                        
                theBox.showNow--;
            }
        }
    }
}