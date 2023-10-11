package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.nirvasoft.rp.shared.TempData;

public class LoanServiceChargesExtendedDAO {

	private String mT00039 = "T00039";
	
	public boolean addLoanServiceCharges(TempData pTempData, Connection pConn) {
		boolean ret = false;		
		try {
			String l_Query = "INSERT INTO "+mT00039+"(T1,T2,T3,T4,T5,N1,N2,N3,N4,N5) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
			PreparedStatement pstmt;		
			pstmt = pConn.prepareStatement(l_Query);			
			updateRecords(pTempData , pstmt);
			if(pstmt.executeUpdate() > 0) {
				ret = true ;			
			}
			pstmt.close();
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		return ret;
	}
	
	private void updateRecords(TempData pData, PreparedStatement pPS) throws SQLException {
		int i = 1;
		pPS.setString(i++, pData.getT1());
		pPS.setString(i++, pData.getT2());
		pPS.setString(i++, pData.getT3());
		pPS.setString(i++, pData.getT4());
		pPS.setString(i++, pData.getT5());
		pPS.setDouble(i++, pData.getN1());
		pPS.setInt(i++, (int) pData.getN2());
		pPS.setInt(i++, (int) pData.getN3());
		pPS.setInt(i++, (int) pData.getN4());
		pPS.setInt(i++, (int) pData.getN5());
	}
	
	public boolean getLoanServiceCharges(TempData pTempData, Connection pConn) {
		boolean ret = false;		
		try {
			int i = 1;
			String l_Query = "SELECT * FROM "+mT00039+" WHERE T1 = ? AND T2 = ? ";
			PreparedStatement pstmt;		
			pstmt = pConn.prepareStatement(l_Query);
			pstmt.setString(i++, pTempData.getT1());
			pstmt.setString(i++, pTempData.getT2());
			ResultSet l_RS = pstmt.executeQuery();
			while(l_RS.next()){
				ret = true ;			
			}
			l_RS.close();
			pstmt.close();
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		return ret;
	}
	
	public boolean updateLoanInterestByAccNumberAndDate(String aAccNumber,String aDate,double aServiceCharges,Connection conn){
		boolean l_result = false;
		try{
			String query = "update "+mT00039+" set N1 = N1 + ? where T1 = ? and T2 = ? ";
			PreparedStatement l_pstmt = conn.prepareStatement(query);
			int l_index = 1;
			l_pstmt.setDouble(l_index++, aServiceCharges);
			l_pstmt.setString(l_index++, aAccNumber);
			l_pstmt.setString(l_index++, aDate);
			
			if(l_pstmt.executeUpdate() > 0){
				l_result = true;
			}			
			l_pstmt.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return l_result;
	}
}
