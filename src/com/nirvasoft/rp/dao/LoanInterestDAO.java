package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoanInterestDAO {
	public double[] getLoanAccrueAmtByAccount(String pAccNumber, String pStartDate,String pEndDate, Connection pConn) throws SQLException{
		double[] l_Amt = new double[3];
		String query = "SELECT SUM(Interest) ImtAmt,SUM(ServiceCharges) ServiceCharges, SUM(CommitmentFee) CommitmentFee FROM LoanInterest " +
				"WHERE AccNumber = ? And Status=2 AND Date>=? AND Date< ? ";
		PreparedStatement l_pstmt = pConn.prepareStatement(query);
		int l_index = 1;			
		l_pstmt.setString(l_index++, pAccNumber);
		l_pstmt.setString(l_index++, pStartDate);
		l_pstmt.setString(l_index++, pEndDate);
		ResultSet rs = l_pstmt.executeQuery();
		while(rs.next()){
			l_Amt[0] = rs.getDouble(1);
			l_Amt[1] = rs.getDouble(2);
			l_Amt[2] = rs.getDouble(3);
		}			
		l_pstmt.close();
		
		return l_Amt;
	}
	
	public boolean getLoanInterestByAccNumberAndDate(String aAccNumber,String aDate,Connection conn){
		boolean l_result = false;
		try{
			String query = "select * from LoanInterest a where a.accnumber = ? and a.date = ? ";
			PreparedStatement l_pstmt = conn.prepareStatement(query);
			int l_index = 1;			
			l_pstmt.setString(l_index++, aAccNumber);
			l_pstmt.setString(l_index++, aDate);
			
			ResultSet l_rs = l_pstmt.executeQuery();
			while(l_rs.next()) {
				l_result = true;
			}
			l_rs.close();
			l_pstmt.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return l_result;
	}
	
	public boolean updateLoanInterestByAccNumberAndDate(String aAccNumber,String aDate,Connection conn){
		boolean l_result = false;
		try{
			String query = " update LoanInterest set n1 = 1 where accnumber = ? and date = ? ";
			PreparedStatement l_pstmt = conn.prepareStatement(query);
			int l_index = 1;			
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
	
	public double checkAccrueAmt(String accNumber, String accrueDate,Connection conn) {
		// TODO Auto-generated method stub
		double accrueAmt=0.0;
		String l_Query = " set dateformat dmy;Select Sum(Interest) Interest from LoanInterest where AccNumber='"+accNumber+"' and Date<='"+accrueDate+"' and Status=2";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(l_Query);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()){
				accrueAmt = rs.getDouble("Interest");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return accrueAmt;
	}
	
	public boolean updateLoanInterestStatusByAccNOAndLessThenRepayDate(String aAccNumber,String aDate,Connection conn) throws SQLException{
		boolean l_result = false;
		
		String query = " set dateformat dmy;update LoanInterest set Status= 1 where accnumber = ? and date < ? ";
		PreparedStatement l_pstmt = conn.prepareStatement(query);
		int l_index = 1;			
		l_pstmt.setString(l_index++, aAccNumber);
		l_pstmt.setString(l_index++, aDate);
		
		if(l_pstmt.executeUpdate() >= 0){
			l_result = true;
		}			
		l_pstmt.close();
		
		return l_result;
	}
	
}
