package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.nirvasoft.rp.shared.icbs.AccountData;
import com.nirvasoft.rp.util.SharedUtil;

public class CurrentAccountDAO {
	private String mCurrentAccount = "CurrentAccount";
	private AccountData CurrentAccBean;
	
	public AccountData getCurrentAccBean() {
		return CurrentAccBean;
	}
	public void setCurrentAccBean(AccountData currentAccBean) {
		CurrentAccBean = currentAccBean;
	}
	
	public boolean getAccount(String pAccNumber,Connection conn) throws SQLException {
		boolean result=false;
		
		PreparedStatement pstmt=conn.prepareStatement("SELECT [AccNumber],[OdLimit] ,[OdExpDate] ,[IntToBeCollected] ," +
				"[BusinessType] ,[Collateral] ,[ApproveNo], [ApproveId],[SecurityType] ,[IsOD] ,[SanctionDate] ,[ODStatus]," +
				"[TODLimit],[TODExpDate],[TODSanctionDate],[SystemTOD],[IsCommit] FROM ["+mCurrentAccount+"] WHERE AccNumber=? ");
		pstmt.setString(1, pAccNumber);
		pstmt.setQueryTimeout(DAOManager.getNormalTime());
		ResultSet rs=pstmt.executeQuery();
		while(rs.next()) {
			CurrentAccBean=new AccountData();
			readRecord(CurrentAccBean, rs);
			result=true;
		}	
		pstmt.close();
		
		return result;
	}
	
	private void readRecord(AccountData CurrentAcc,ResultSet rs) throws SQLException {
		
		CurrentAcc.setAccountNumber(rs.getString("AccNumber"));
		CurrentAcc.setOdLimit(rs.getDouble("OdLimit"));
		CurrentAcc.setOdExpDate(SharedUtil.formatDBDate2MIT(rs.getString("OdExpDate")));
		CurrentAcc.setIntToBeCollected(rs.getDouble("IntToBeCollected"));
		CurrentAcc.setBusinessType(rs.getInt("BusinessType"));
		CurrentAcc.setCollateral(rs.getDouble("Collateral"));
		CurrentAcc.setApproveNo(rs.getString("ApproveNo"));
		CurrentAcc.setApproveID(rs.getString("ApproveId"));
		CurrentAcc.setSecurityType(rs.getByte("SecurityType"));
		CurrentAcc.setIsOD(rs.getInt("IsOD"));
		CurrentAcc.setSanctionDate(SharedUtil.formatDBDate2MIT(rs.getString("SanctionDate")));
		CurrentAcc.setODStatus(rs.getInt("ODStatus"));
		CurrentAcc.setTODLimit(rs.getDouble("TODLimit"));
		CurrentAcc.setTODExpDate(SharedUtil.formatDBDate2MIT(rs.getString("TODExpDate")));
		CurrentAcc.setTODSanctionDate(SharedUtil.formatDBDate2MIT(rs.getString("TODSanctionDate")));
		CurrentAcc.setSystemTOD(rs.getDouble("SystemTOD"));
		CurrentAcc.setIsCommit(rs.getInt("IsCommit"));				
		
	}
	
	public boolean isCreditCardAccount(String pAccNumber, Connection pConn) throws SQLException {
		boolean ret = false;
		String l_Query = "Select * From AODF Where AccNumber = ? And ODType > 100 ";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);
		pstmt.setString(1, pAccNumber);
		
		ResultSet l_RS = pstmt.executeQuery();
		while (l_RS.next()) {
			ret = true;
		}			
		return ret;
	}	
	
	public double getLimitAmount(String pAccNumber, String pTransDate, Connection pConn) throws SQLException {
		double limitAmount = 0;
		String l_Query = "Select CB.ODLimit From CurrentAccount CB Inner Join AODF A On CB.AccNumber = A.AccNumber " +
						"Where CB.AccNumber = ? And A.ODType > 100 And A.ExpDate >= ? ";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);
		pstmt.setString(1, pAccNumber);
		pstmt.setString(2, pTransDate);
		
		ResultSet l_RS = pstmt.executeQuery();
		while (l_RS.next()) {
			limitAmount= l_RS.getDouble("OdLimit");
		}		
		return limitAmount;
	}
}
