package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.nirvasoft.rp.shared.LoanAllowSetupData;

public class LoanAllowSetupDAO {

	public boolean updateLoanAllowSetupForTrans(int type,String accNumber,Connection conn) throws SQLException{
		boolean ret=false;
		String query = "UPDATE LOANALLOWSETUP SET STATUS=1 WHERE TYPE=? AND ACCNUMBER=?";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, type);
		pstmt.setString(2, accNumber);
		if (pstmt.executeUpdate()>0){
			ret=true;
		}
		return ret;
	}
	
	public ArrayList<LoanAllowSetupData> getLoanAllowDataForAllow(String accNumber, int i, Connection conn) throws SQLException {
		ArrayList<LoanAllowSetupData> l_Data = new ArrayList<LoanAllowSetupData>();
		String query = "";
		if (i != 1) {
			query = "SELECT A.GLCode, A.TransType, A.LimitAmount, A.CusAccNumber, A.Type, A.Status, A.AccNumber, C.YearlyRate, Convert(varchar,SanctionDateBr,103) as SanctionDateBr" +
				" FROM LoanAllowSetup A INNER JOIN LAODF B ON A.AccNumber=B.AccNumber INNER JOIN ProductInterestRate C ON C.Tenure=B.LoanType " +
				" AND C.AccType='04' WHERE A.AccNumber=?";
		}else{
			query = "SELECT A.GLCode, A.TransType, A.LimitAmount, A.CusAccNumber, A.Type, A.Status, A.AccNumber" +
					" FROM LoanAllowSetup A  WHERE A.AccNumber=?";
		}
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setString(1, accNumber);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			LoanAllowSetupData l_AllowSetupData = new LoanAllowSetupData();
			l_AllowSetupData.setGlCode(rs.getString("GLCode"));
			l_AllowSetupData.setCusAccNumber(rs.getString("CusAccNumber"));
			l_AllowSetupData.setTransType(rs.getString("TransType"));
			l_AllowSetupData.setLimitAmount(rs.getDouble("LimitAmount"));
			l_AllowSetupData.setType(rs.getInt("Type"));
			l_AllowSetupData.setStatus(rs.getInt("Status"));
			l_AllowSetupData.setAccNumber(rs.getString("AccNumber"));
			l_AllowSetupData.setStatus(0);
			if (i!=1){
				l_AllowSetupData.setYearlyRate(rs.getInt("YearlyRate"));
				l_AllowSetupData.setT1(rs.getString("SanctionDateBr"));
			}
			l_Data.add(l_AllowSetupData);
		}
		return l_Data;
	}
	
}
