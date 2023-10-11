package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.nirvasoft.rp.shared.TransAmtLimitData;

public class TransAmtLimitDAO {
	public ArrayList<TransAmtLimitData> getTransAmtLimits(String pProductID,String pAccCode,int pTransType, Connection l_conn) throws SQLException {
		TransAmtLimitData l_data = new TransAmtLimitData();
		ArrayList<TransAmtLimitData> lstData = new ArrayList<TransAmtLimitData>();
		String l_query = "SELECT [ProductID],[AccountCode],[TenureType],[TransTypeFrom],[TransTypeTo],[TransCount],[TransAmtLimit] " +
				"FROM TransAmtLimit WHERE ProductID= ? AND (AccountCode='-' OR AccountCode=?) AND ? BETWEEN TransTypeFrom AND TransTypeTo Order By TenureType";
		PreparedStatement l_pstmt = l_conn.prepareStatement(l_query);
		l_pstmt.setString(1, pProductID);
		l_pstmt.setString(2, pAccCode);
		l_pstmt.setInt(3, pTransType);		
		ResultSet l_RS = l_pstmt.executeQuery();
		while (l_RS.next()) {
			l_data = new TransAmtLimitData();
			readRecords(l_RS, l_data);
			lstData.add(l_data);
		}
		l_pstmt.close();
		return lstData;
	}
	
	private void readRecords(ResultSet pRS, TransAmtLimitData pData) throws SQLException{
		pData.setProductID(pRS.getString("ProductID"));
		pData.setAccountCode(pRS.getString("AccountCode"));
		pData.setTenureType(pRS.getInt("TenureType"));
		pData.setTransTypeFrom(pRS.getInt("TransTypeFrom"));
		pData.setTransTypeTo(pRS.getInt("TransTypeTo"));
		pData.setTransCount(pRS.getInt("TransCount"));
		pData.setTransAmount(pRS.getDouble("TransAmtLimit"));		
	}
	
}
