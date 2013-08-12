package com.invindible.facetime.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.invindible.facetime.model.Sign;
import com.invindible.facetime.model.User;

public class SignDao {
	public static String doSign(int id,Connection conn) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		java.util.Date date=new java.util.Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String snow = sdf.format(date); 
		PreparedStatement pst = conn.prepareStatement("insert into sign values(?,?)");
		pst.setInt(1, id);
		pst.setString(2, snow);
		pst.executeUpdate();
		pst.close();
		return snow;
	}
	
	public static ArrayList<Sign> doselect(String datetime,Connection conn) throws SQLException, IOException{
		ArrayList<Sign> sign=new ArrayList<Sign>();
		PreparedStatement pst=conn.prepareStatement("select * from sign where signdate like '"+datetime+"%' order by id asc ");
		ResultSet rs=pst.executeQuery();
		ResultSet rstmp;
		int id;
		while(rs.next()){
			Sign si=new Sign();
			id=rs.getInt("id");
			si.setSigndate(rs.getString("signdate"));
			pst=conn.prepareStatement("select image from imageinfo where id=?");
			pst.setInt(1, id);
			rstmp=pst.executeQuery();
			if(rstmp.next())
				si.setBfi(ImageIO.read(rstmp.getBinaryStream("image")));
			pst=conn.prepareStatement("select username from userinfo where id=?");
			pst.setInt(1, id);
			rstmp=pst.executeQuery();
			if(rstmp.next())
				si.setUsername(rstmp.getString("username"));
			sign.add(si);
		}
		pst.close();
		rs.close();
		return sign;
	}
}
