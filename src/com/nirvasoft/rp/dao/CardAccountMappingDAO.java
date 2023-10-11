package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CardAccountMappingDAO {
	private String mTable = "T00012";
	
	public String  getCardAccNumber(String pAccNo,Connection pConn)throws SQLException {
		String l_CardAccNo = "";
		String l_Query = "SELECT  T1 FROM "+ mTable +" WHERE T6= ?";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);
		pstmt.setString(1, pAccNo);
		ResultSet l_RS=pstmt.executeQuery();
		while(l_RS.next()) {
			l_CardAccNo = l_RS.getString(1);
		}
		return l_CardAccNo;
	}
	
}
