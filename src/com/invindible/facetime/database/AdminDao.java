package com.invindible.facetime.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.invindible.facetime.model.Sign;
import com.invindible.facetime.model.UserByAdmin;

public class AdminDao {
	public static ArrayList<UserByAdmin> doselect(String name,Connection conn) throws SQLException, IOException{
		ArrayList<UserByAdmin> user=new ArrayList<UserByAdmin>();
		String sql="";
		if(name!=null)
			sql="select *from userinfo where username like '%"+name+"%'";
		else
			sql="select *from userinfo ";
		PreparedStatement pst=conn.prepareStatement(sql);
		ResultSet rs=pst.executeQuery();
		ResultSet rstmp;
		int id;
		while(rs.next()){
			UserByAdmin uba=new UserByAdmin();
			id=rs.getInt("id");
			uba.setId(id);
			pst=conn.prepareStatement("select image from iamgeinfo where id=?");
			pst.setInt(1, id);
			rstmp=pst.executeQuery();
			if(rstmp.next())
				uba.setBfi(ImageIO.read(rstmp.getBinaryStream("image")));
			uba.setUsername(rs.getString("username"));
			user.add(uba);
		}
		return user;
	}
	
	public static void deleteUser(int id,Connection conn) throws SQLException{
		PreparedStatement pst=conn.prepareStatement("delete from sign where id=?");
		pst.setInt(1, id);
		pst.execute();
		pst=conn.prepareStatement("delete from project where id=?");
		pst.setInt(1, id);
		pst.execute();
		pst=conn.prepareStatement("delete from imageinfo where id=?");
		pst.setInt(1, id);
		pst.execute();
		pst=conn.prepareStatement("delete from userinfo where id=?");
		pst.setInt(1, id);
		pst.execute();
	}
}
