package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.nirvasoft.rp.shared.icbs.AccountData;

public class AccountExtendedDAO {
	private static String mTableName = "T00005";
	
	public AccountData getData(AccountData pData, Connection pConn) {
		try {
			String l_Query = "SELECT N1, T1, T2, T3, T4, T5, T6, T7 FROM " + getTableName() + " WHERE T1 = ?";
			PreparedStatement pstmt = pConn.prepareStatement(l_Query);
			pstmt.setString(1, pData.getAccountNumber());

			ResultSet l_RS = pstmt.executeQuery();
			while (l_RS.next()) {
				readRecord(pData, l_RS);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return pData;
	}
	
	public static String getTableName() {
		return mTableName;
	}
	
	private static void readRecord(AccountData pData, ResultSet pRS) throws SQLException {
		pData.setZoneCode(pRS.getInt("N1"));
		pData.setProduct(pRS.getString("T2"));
		pData.setType(pRS.getString("T3"));
		pData.setCurrencyCode(pRS.getString("T4"));
		pData.setBranchCode(pRS.getString("T5"));
		pData.setProductGL(pRS.getString("T6"));
		pData.setCashInHandGL(pRS.getString("T7"));
	}
	
	public boolean addData(AccountData pData, Connection pConn) throws SQLException{
		boolean l_Result = false;
		
		String l_Query = "INSERT INTO " + getTableName() + " (N1, T1, T2, T3, T4, T5, T6, T7, T8) VALUES " +
							"(?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);
		updateRecord(pData, pstmt, "Insert");
		if(pstmt.executeUpdate()>0)
			l_Result = true;
		pstmt.close();
		
		return l_Result;
	}
	
	private void updateRecord(AccountData pData, PreparedStatement pstmt, String pFrom) throws SQLException{
		int l_ParaIndex = 1;
		if (pFrom.equals("Insert")) {
			pstmt.setInt(l_ParaIndex++, pData.getZoneCode());
			pstmt.setString(l_ParaIndex++, pData.getAccountNumber());
			pstmt.setString(l_ParaIndex++, pData.getProduct());
			pstmt.setString(l_ParaIndex++, pData.getType());
			pstmt.setString(l_ParaIndex++, pData.getCurrencyCode());
			pstmt.setString(l_ParaIndex++, pData.getBranchCode());
			pstmt.setString(l_ParaIndex++, pData.getProductGL());
			pstmt.setString(l_ParaIndex++, pData.getCashInHandGL());
			pstmt.setString(l_ParaIndex++, pData.getShortAccountCode());
		} else {
			pstmt.setInt(l_ParaIndex++, pData.getZoneCode());
			pstmt.setString(l_ParaIndex++, pData.getProduct());
			pstmt.setString(l_ParaIndex++, pData.getType());
			pstmt.setString(l_ParaIndex++, pData.getCurrencyCode());
			pstmt.setString(l_ParaIndex++, pData.getBranchCode());
			pstmt.setString(l_ParaIndex++, pData.getProductGL());
			pstmt.setString(l_ParaIndex++, pData.getCashInHandGL());
			pstmt.setString(l_ParaIndex++, pData.getShortAccountCode());
			pstmt.setString(l_ParaIndex++, pData.getAccountNumber());
		}
	
	}
	
	public boolean updateData(AccountData pData, Connection pConn) throws SQLException {
		boolean l_Result = false;
		
		String l_Query = "UPDATE " + getTableName() + " SET N1=?, T2 = ?, T3 = ?, T4 = ?, T5 = ?, T6 = ?, T7 = ?, T8 = ? WHERE T1 = ?";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);
		updateRecord(pData, pstmt, "Update");
		if (pstmt.executeUpdate() > 0 ){
			l_Result = true;
		}
		
		return l_Result;
	}
	
}
