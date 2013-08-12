/**
 *  [RoundBox.java]
 *  Download by http://www.codefans.net
 *  �޶�ѡ��ؼ�
 *
 * �������ڣ�(2003-10-27)
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
 
import java.util.Vector;
 
 
 
class RoundBox extends JPanel {
    
    //�������----------------------//
    public int showMin = 0;  //��С��ʾֵ
    public int showMax = 0;  //�����ʾֵ
    
    public int showNow = 0;  //Ĭ������ֵ
    
    
    //�������/����----------------------//
    public JLabel showing = new JLabel("", JLabel.RIGHT);
        private int showWidth = 35;
        private int showHeight = 15;
        
    public JButton bt_DOWN = new JButton("-");
    public JButton bt_UP = new JButton("+");
    
    
    
    
//------���췽��---------------------------------------------------//
 
 
/**
 * ���췽��
 */
    RoundBox(int showNow, int showMin, int showMax) {
        
        if(showNow >= showMin && showNow <= showMax) {
            
            this.showNow = showNow;
            this.showMin = showMin;
            this.showMax = showMax;
        }
        
        makeFace();  //��������
    }
    
    
    
    
//------����/����--------------------------------------------------//
 
 
 
/**
 * ��������������
 */
    private void makeFace() {
        
        Font txtFont = new Font("����", Font.PLAIN, 12);
        
        //��������----------------------------//
        this.setLayout(new FlowLayout(1, 2, 1));
        this.setBackground(Pallet.configLineColor);
        this.setBorder(null);
        
        
        //showing ����------------------------//
        showing.setText("" + showNow);
        showing.setBorder(new LineBorder(Pallet.rbBorderColor, 1));
        showing.setForeground(Pallet.rbFontColor);
        showing.setPreferredSize(new Dimension(showWidth, showHeight));
        showing.setFont(txtFont);
        
        
        //bt_UP & bt_DOWN ����----------------//
        bt_UP.setBorder(new LineBorder(Pallet.rbBorderColor, 1));
        bt_UP.setBackground(Pallet.rbButtonColor);
        bt_UP.setForeground(Pallet.rbBtFontColor);
        bt_UP.setPreferredSize(new Dimension(15, 7));
        bt_UP.setFont(txtFont);
        bt_UP.setFocusable(false);
        
        bt_DOWN.setBorder(new LineBorder(Pallet.rbBorderColor, 1));
        bt_DOWN.setBackground(Pallet.rbButtonColor);
        bt_DOWN.setForeground(Pallet.rbBtFontColor);
        bt_DOWN.setPreferredSize(new Dimension(15, 7));
        bt_DOWN.setFont(txtFont);
        bt_DOWN.setFocusable(false);
        
        
        //���岼��----------------------------//
        JPanel btPanel = new JPanel(new BorderLayout(0, 1));
        btPanel.setBorder(null);
        btPanel.setBackground(Pallet.configLineColor);
        
        btPanel.add(bt_UP, BorderLayout.NORTH);
        btPanel.add(bt_DOWN, BorderLayout.SOUTH);
        
        
        add(showing);
        add(btPanel);
    }
 
 
 
 
/**
 * ������������ʾ��ǩ���
 *
 * @param showWidth int
 */
    public void setShowWidth(int showWidth) {
        
        if(showWidth > 0)
            this.showWidth = showWidth;
            
        showing.setPreferredSize(new Dimension(showWidth, showHeight));
    }
    
}