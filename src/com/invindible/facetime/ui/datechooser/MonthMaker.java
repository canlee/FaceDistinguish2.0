/**
 *  [MonthMaker.java]
 *  Download by http://www.codefans.net
 *  �·ݱ��㷨��
 *
 * �������ڣ�(2003-10-25)
 * @author��ONE_Fox
 * @author��ONE_Fox@163.com
 */
 
 
package com.invindible.facetime.ui.datechooser;
 
 
import java.util.Calendar;
 
 
 
class MonthMaker {
    
    private static Calendar maker = null;
 
 
/**
 * (����)���캯�������贴��ʵ��
 */
    private MonthMaker () {}
    
    
    
    
//----------------------------------------------------------------//
 
 
 
/**
 * �����������·ݱ�
 *
 * @param showMonth java.util.Calendar
 *
 * @retuen String[][]
 */
    public static String[][] makeMonth(Calendar showMonth) {
        
        maker = showMonth; //���� maker
        
        int dayCount = 1; //���������
        
        
        //��������������----------------//
        String[][] date = new String[6][7];
        for(int f = 0; f < 6; f++)  //����ʼ���
            java.util.Arrays.fill(date[f], "");
        
        
        maker.set(Calendar.DATE, dayCount);
        
        //�����·ݱ�� 1 ��---------------//
        for(int i = maker.get(Calendar.DAY_OF_WEEK) -1; i < 7; i++) {
            date[0][i] = "" + dayCount;
            dayCount++;
        }
        
        //�����·ݱ� 2��4 ��--------------//
        for(int i = 1; i < 4; i++) {
            for(int j = 0; j < 7; j++) {
                date[i][j] = "" + dayCount;
                dayCount++;
            }
        }
        
        //�����·ݱ�� 5 ��---------------//
        for(int i = dayCount, j = 0; 
              i <= maker.getActualMaximum(Calendar.DAY_OF_MONTH) && j < 7;
                                                                   i++,j++) {
                                                                    
            maker.set(Calendar.DATE, i);
            date[4][maker.get(Calendar.DAY_OF_WEEK)-1] = "" + dayCount;
            
            dayCount++;
        }
        
        //�����·ݱ�� 6 ��--------------//
        for(int i = dayCount; i <= maker.getActualMaximum(
                                               Calendar.DAY_OF_MONTH); i++) {
                                                                    
            maker.set(Calendar.DATE, i);
            date[5][maker.get(Calendar.DAY_OF_WEEK)-1] = "" + dayCount;
            
            dayCount++;
        }
        
        return date;
    }
}