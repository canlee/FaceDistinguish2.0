/**
 *  [TablePanel.java]
 *  Download by http://www.codefans.net
 *  ���������
 *
 *
 * �������ڣ�(2003-10-25)
 * @author��ONE_Fox
 * @author��ONE_Fox@163.com
 */
 
 
package com.invindible.facetime.ui.datechooser;
 
import java.awt.*;
import java.awt.event.*;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
 
 
 
 
class TablePanel extends JPanel {
    
 
    //�������------------------------//
    /**
     * ���������ã�ֻ�����˫�������������
     */
    DateChooser dateChooser = null;
    
    //��ǰ��ʾ���·�
    private Calendar showMonth = null;
    
    //��ѡ�������
    private Date selectedDate = null;
    
    //��ͷ��Ϣ
    private String[] colname = {"SUN","MON","TUE","WED","THU","FRI","SAT"};
    
    //�����ݣ���һ����ʾ��������
    //���� 6 ����ʾ���������
    private String[][] date = new String[7][7];
    
    
    //�������-----------------------//
    
    private DefaultTableModel model; //Ĭ�� ���ģ��
    private JTable table; //������ʾ������ ���
 
 
 
 
 
//------���췽��/����-----------------------------------------------//
 

 
/**
 * ���췽��
 *
 * @param dateChooser DateChooser
 * @param showMonth java.util.Calendar
 */
    TablePanel(DateChooser dateChooser, Calendar showMonth) {
        
        this.dateChooser = dateChooser;
        this.showMonth = showMonth;
        
        makeFace(); //��������
        addListener(); //����¼�����
    }
    
    
    
    
    
//------����/����--------------------------------------------------//
 
/**
 * ��������������
 */
    private void makeFace() {
        
        //�����������------------------------//
        date[0][0] = "日";  
        date[0][1] = "一";
        date[0][2] = "二";  
        date[0][3] = "三";
        date[0][4] = "四";  
        date[0][5] = "五";
        date[0][6] = "六";
        
        
        //���� Table -------------------------//
        
        table = new JTable(model = new DefaultTableModel(date,colname) {
                    public boolean isCellEditable(int rowIndex, int mColIndex) {
                        return false;
                    }
                });
        
        //���ù̶����壬������û����ı�Ӱ���������
        table.setFont(new Font("����", Font.PLAIN, 12));
        
        //table ����������--------------------//
        
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, 
                          Object value, boolean isSelected, boolean hasFocus, 
                                                     int row, int column) {
                
                //��Ԫ�������Ҷ���
                setHorizontalAlignment(JLabel.RIGHT);
                
                //���� ���� ��ɫ-----------//
                if(row == 0)  //���������б���ɫ
                    setBackground(Pallet.backGroundColor);
                else if((""+new GregorianCalendar().get(Calendar.DAY_OF_MONTH))
                                              .equals(date[row][column])) {
                    setBackground(Pallet.todayBackColor); //����񱳾�ɫ
                }
                else
                    setBackground(Pallet.palletTableColor); //��ͨ�񱳾�ɫ
                    
                
                //���� ǰ�� ��ɫ-----------//
                if((column == 0 && row != 0)||(column == 6 && row != 0))
                    setForeground(Pallet.weekendFontColor); //��ĩ������ɫ
                else if(row != 0 && column != 0 && column != 6)
                    setForeground(Pallet.dateFontColor); //��ͨ������ɫ
                else
                    setForeground(Pallet.weekFontColor); //�������ָ�����ɫ
                
                return super.getTableCellRendererComponent(table, value, 
                                          isSelected, hasFocus, row, column);
                                          
            }
        };
        
        
        
        //�����б�����------------------------//
        
        for(int i = 0; i < colname.length; i++) {
            table.getColumn(colname[i]).setCellRenderer(tcr);
        }
        
        
        //table ����--------------------------//
        
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        
        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(false);
        
        table.setIntercellSpacing(new Dimension(0, 0));
        
        
        //�����������ֵ----------------------//
        
        setMonth(showMonth);
        
        
        //���岼��---------------------------//
        setLayout(new BorderLayout());
        add(table, BorderLayout.CENTER);
    }
    
    
    
/**
 * ����������¼�����
 */
    private void addListener() {
        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                
                //˫���?Ԫ�񣬱���������� Date
                if(e.getClickCount() == 2) {
                    int selectedRow = table.getSelectedRow();
                    int selectedColumn = table.getSelectedColumn();
                    
                    if(selectedRow > 0 && 
                              !date[selectedRow][selectedColumn].equals("")) {
                        
                        showMonth.set(Calendar.DAY_OF_MONTH, Integer.valueOf(
                                date[selectedRow][selectedColumn]).intValue());
                        
                        selectedDate = showMonth.getTime();
                        
                        dateChooser.hideChooser();
                    }
                } //end if
            }
        });
    }
    
 
 
/**
 * �����������������ʾ�·�1
 *
 * @param showMonth java.util.Calendar
 */
    private void setMonth(Calendar showMonth) {
        
        this.showMonth = showMonth;
        
        //�·ݱ�ȡ��--------------------------//
        
        String[][] tmpDate = MonthMaker.makeMonth(showMonth);
        
        for(int i = 1; i < 7; i++) {
            for(int j = 0; j < 7; j++) {
                date[i][j] = tmpDate[i-1][j];
                table.setValueAt(""+tmpDate[i-1][j], i, j);
            }
        }
    }
    
    
    
    
/**
 * �����������������ʾ�·�2
 *
 * @param year int
 * @param month int
 */
    public void setMonth(int year, int month) {
        
        showMonth.set(year, month-1, 1);
        
        setMonth(showMonth);
    }
    
    
    
/**
 * ������ȡ��ѡ������
 */
    public Date getDate() {
        return selectedDate;
    }
}