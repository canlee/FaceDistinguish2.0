package com.invindible.facetime.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.invindible.facetime.model.Project;
import com.invindible.facetime.model.Wopt;

public class ProjectDao {
	/**
	 * 插入wopt矩阵
	 * @param conn
	 * @param wopt
	 * @throws SQLException
	 */
	public static void doinsertWopt(Connection conn,Wopt wopt) throws SQLException{
		clean(conn);
		String save=procedure(wopt.getWopt());
		PreparedStatement pst=conn.prepareStatement("insert into Wopt values(?)");
		pst.setString(1, save);
		pst.executeUpdate();
		pst.close();
		 
	}
	
	/**
	 * 更新前，先把原来的删除
	 * @param conn
	 * @throws SQLException
	 */
	public static void clean(Connection conn) throws SQLException{
		PreparedStatement pst=conn.prepareStatement("select * from Wopt");
		ResultSet rs=pst.executeQuery();
		if(rs.next()){
			pst=conn.prepareStatement("delete from Wopt");
			pst.execute();
		}
		pst.close();
		
	}
	
	/**
	 * 插入前的修改
	 * @param array
	 * @return
	 */
	public static String procedure(double[][] array){
		String save="";
		for(int i=0;i<array.length;i++){
			for(int j=0;j<array[0].length;j++){
				save+=String.valueOf(array[i][j])+" ";
			}
		}
		return save;
	}
	
	/**
	 * 判断opt是否存在
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public static boolean firstORnot(Connection conn) throws SQLException{
		PreparedStatement pst=conn.prepareStatement("select * from Wopt");
		ResultSet rs=pst.executeQuery();
		if(rs.next())
		{
			pst.close();
			
			return false;
		}
		pst.close();
		
		return true;
		
	}
	
	/**
	 * 获取wopt
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public static double[][] doselectWopt(Connection conn) throws SQLException{
		PreparedStatement pst=conn.prepareStatement("select * from Wopt");
		ResultSet rs=pst.executeQuery();
		String save = "";
		if(rs.next()){
			save=rs.getString("array");
		}
		String[] arr=save.split(" ");
		System.out.println(arr.length);
		pst=conn.prepareStatement("select id from userinfo",ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		rs=pst.executeQuery();
		rs.last();
		int peoplenum=rs.getRow();
		int row=-1;
		if(peoplenum==1)
			row=1;
		else
			row=peoplenum-1;
		int column=arr.length/row;
		double[][] array=new double[row][column];
		for(int i=0;i<row;i++){
			for(int j=0;j<column;j++)
				array[i][j]=Double.valueOf(arr[i*column+j]);
		}
		pst.close();
		rs.close();
		return array;
	}
	
	/**
	 * 插入投影
	 * @param conn
	 * @param project
	 * @throws SQLException
	 */
	public static void doinsertProject(Connection conn,Project project) throws SQLException{
		double[][] projectArray=project.getProject();
		int[] id=project.getId();
		int tmp=0;
		PreparedStatement pst= conn.prepareStatement("update project set pro=? where id=?");
		for(int i=0;i<id.length;i++){
			String save="";
			double[][] tmpProject=new double[5][projectArray[0].length];
			for(int j=0;j<5;j++)
			{
				tmpProject[j]=projectArray[tmp++];
			}
			save=procedure(tmpProject);
			System.out.println(save);
			pst.setString(1, save);
			pst.setInt(2, id[i]);
			pst.executeUpdate();
			pst.clearParameters();
		}
		pst.close();
	}
	
	/**
	 * 获取投影
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public static Project doselectProject(Connection conn) throws SQLException{
		Project pro=new Project();
		PreparedStatement pst=conn.prepareStatement("select id,pro from project order by id asc",ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		ResultSet rs=pst.executeQuery();
		rs.last();
		int[] id=new int[rs.getRow()];
		double[][] modelProject=new double[rs.getRow()*5][rs.getString("pro").split(" ").length/5];
		int tmp=0;
		int projectTmp=0; 
		rs.beforeFirst(); 
		while(rs.next()){
			String project=rs.getString("pro");		
			id[tmp++]=rs.getInt("id");
			
			String[] proj=project.split(" ");	
			for(int j=0;j<5;j++){  //5个投影
			for(int i=0;i<modelProject[0].length;i++)  //每个投影的列数
				{	
					modelProject[projectTmp][i]=Double.valueOf(proj[i+modelProject[0].length*j]);
					          
				}				
				projectTmp++;
			}
		}
		pro.setId(id);
		pro.setProject(modelProject);
		pst.close();
		return pro;		
	}
	
	/**
	 * 总体均值
	 * @param conn
	 * @param mean
	 * @throws SQLException
	 */
	public static void doinsertmean(Connection conn,double[] mean) throws SQLException{
		PreparedStatement pst=conn.prepareStatement("select *from mean");
		ResultSet rs=pst.executeQuery();
		String sql="";
		if(rs.next())
			sql="update mean set allmean=?";
		else
			sql="insert into mean values(?)";
		pst=conn.prepareStatement(sql);
		double[][] tmp=new double[1][mean.length];
		tmp[0]=mean;
		String save=procedure(tmp);
		pst.setString(1, save);
		pst.executeUpdate();
		pst.close();
		rs.close();
	}
	
	/**
	 * 获取总体均值
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public static double[] doselectmean(Connection conn) throws SQLException{
		PreparedStatement pst=conn.prepareStatement("select *from mean");
		ResultSet rs=pst.executeQuery();
		rs.next();
		String save=rs.getString("allmean");
		String[] smean=save.split(" ");
		double[] mean=new double[smean.length];
		for(int i=0;i<smean.length;i++){
			mean[i]=Double.valueOf(smean[i]);
		}
		pst.close();
		rs.close();
		return mean;
		
	}
	
	/**
	 * 插入类均值
	 * @param conn
	 * @param mean
	 * @param id
	 * @throws SQLException
	 */
	public static void doinsertclassmean(Connection conn,double[][] mean,int[] id) throws SQLException{
		PreparedStatement pst=conn.prepareStatement("update classmean set mean=? where id=?");
		String save="";
		double[][] tmp=new double[1][mean[0].length];
		for(int i=0;i<id.length;i++){
			tmp[0]=mean[i];
			save=procedure(tmp);
			
			pst.setString(1, save);
			pst.setInt(2, id[i]);
			pst.executeUpdate();
			save="";
			pst.clearParameters();
		}	
		pst.close();
	}
	
	/**
	 * 获取类均值
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public static double[][] doselectclassmean(Connection conn) throws SQLException{
		PreparedStatement pst=conn.prepareStatement("select id,mean from classmean order by id asc",ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		ResultSet rs=pst.executeQuery();
		rs.last();
		int row=rs.getRow();
	//	System.out.println("row:" + row);
		rs.beforeFirst();
		rs.next();
		System.out.println(rs.getString("mean"));
		int column=rs.getString("mean").split(" ").length;
//		System.out.println("row:" + row + "   column" + column);
		double[][] mean=new double[row][column];
		int index=0;
		rs.beforeFirst();
		while(rs.next()){
			String[] value=rs.getString("mean").split(" ");
			for(int i=0;i<value.length;i++){
//				System.out.println("index:" + index + "  i:" + i );
				mean[index][i]=Double.valueOf(value[i]);
			}
				index++;
		} 
		pst.close();
		rs.close();
		return mean;
	}
	
	/**
	 * 
	 * @param conn
	 * @param mean
	 * @param id
	 * @throws SQLException
	 */
	public static void doinsertPeoplemean(Connection conn,double[][] mean,int[] id) throws SQLException{
		PreparedStatement pst=conn.prepareStatement("update peoplemean set mean=? where id=?");
		String save="";
		double[][] tmp;
		for(int i=0;i<id.length;i++){
			tmp=new double[5][mean[0].length];
			for(int j=0;j<5;j++){
				tmp[0]=mean[i*5+j];
			}
			save=procedure(tmp);
			pst.setString(1, save);
			pst.setInt(2, id[i]);
			pst.executeUpdate();
			save="";
			pst.clearParameters();
		}
		pst.close();
	}
	
	/**
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public static double[][] doselectPeoplemean(Connection conn) throws SQLException{
		PreparedStatement pst=conn.prepareStatement("select id,mean from peoplemean order by id asc",ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		ResultSet rs=pst.executeQuery();
		rs.last();
		int row=rs.getRow();
	//	System.out.println("row:" + row);
		rs.beforeFirst();
		rs.next();
		//System.out.println(rs.getString("mean"));
		int column=rs.getString("mean").split(" ").length/5;
//		System.out.println("row:" + row + "   column" + column);
		double[][] mean=new double[row*5][column];
		int index=0;
		rs.beforeFirst();
		while(rs.next()){
			String[] value=rs.getString("mean").split(" ");
			for(int j=0;j<5;j++){
			for(int i=0;i<column;i++){
//				System.out.println("index:" + index + "  i:" + i );
				mean[index][i]=Double.valueOf(value[i+j*column]);
			}
			index++;
			}
		} 
		pst.close();
		rs.close();
		return mean;
	}
	
	public static void deleteUserById(Connection conn,int id) throws SQLException{
		PreparedStatement pst=conn.prepareStatement("delete from project where id=?");
		pst.setInt(1, id);
		pst.execute();
		pst=conn.prepareStatement("delete from peoplemean where id=?");
		pst.setInt(1, id);
		pst.execute();
		pst=conn.prepareStatement("delete from classmean where id=?");
		pst.setInt(1, id);
		pst.execute();
		pst=conn.prepareStatement("delete from mean");
		pst.execute();
		clean(conn);
		pst=conn.prepareStatement("delete from sign where id=?");
		pst.setInt(1, id);
		pst.execute();
		pst=conn.prepareStatement("delete from imageinfo where id=?");
		pst.setInt(1, id);
		pst.execute();
		pst=conn.prepareStatement("delete from userinfo where id=?");
		pst.setInt(1, id);
		pst.execute();
		pst.close();
	}
}
