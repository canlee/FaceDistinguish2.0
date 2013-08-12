package com.invindible.facetime.ui.datechooser;



import javax.swing.JFrame;
import javax.swing.JTextField;

import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;

public class Test extends JFrame {
	
	 private JTextField showField = new JTextField(15);
	 private DateChooser dateChooser;
	 private String datetime; 
	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public Test() {
		 dateChooser = new DateChooser(this);
		 final JButton btnNewButton = new JButton("New button");
		 btnNewButton.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent e) {
		 	}
		 });
		 
		 btnNewButton.addMouseListener(new MouseAdapter() {
	            public void mousePressed(MouseEvent e) {
	                
	                dateChooser.showChooser(btnNewButton,  e.getX() - DateChooser.width, e.getY());
	                if(dateChooser.getDate() != null)
	                { 
	                	datetime=new SimpleDateFormat("yyyy-MM-dd").format(dateChooser.getDate());
	                	System.out.println(datetime);
	                }
	            }
	        });
		 
		 getContentPane().add(btnNewButton, BorderLayout.SOUTH);
		 getContentPane().addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					
				}
			});
		 
		 this.addWindowListener(new WindowAdapter(){  //添加窗口关闭事件
	            public void windowClosing(WindowEvent e){
	                
	                setVisible(false);
	                dispose();
	                
	                System.exit(0);
	            }
	        });
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Test t=new Test();
		t.setVisible(true);
		//t.dateChooser.setVisible(true);
		System.out.println("ok");
	}

}
