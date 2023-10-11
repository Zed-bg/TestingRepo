package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.nirvasoft.rp.shared.BudgetYearData;
import com.nirvasoft.rp.util.SharedUtil;

public class LastDatesDAO {

	// APH 11/09/2014
	public String addDayToLastProcessedDate(String pDate, int pNoOfDate, Connection pConn) throws SQLException {
		String ret = "";

		String l_Query = "SELECT DateAdd(d," + pNoOfDate + ",'" + pDate + "')";
		PreparedStatement pstmt = pConn.prepareStatement(l_Query);
		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {
			ret = SharedUtil.formatDBDate2MIT(rs.getString(1));
		}
		return ret;
	}

	public boolean addLastDatesEOD(String pProcessedDate, String pTodayDate, Connection conn) throws SQLException {
		boolean result = false;
		String l_Query = "INSERT INTO LastDates (LastProcessedDate, StartedAt) " + "VALUES (?, ?)";
		PreparedStatement pstmt = conn.prepareStatement(l_Query);
		pstmt.setString(1, pProcessedDate);
		pstmt.setString(2, pTodayDate);

		pstmt.execute();
		result = true;
		pstmt.close();
		return result;
	}

	public String getLastProcessedDate(Connection pConn) throws SQLException {
		String ret = "";
		String l_Query = "SELECT MAX(LASTPROCESSEDDATE) AS LASTPROCESSEDDATE FROM LASTDATES";

		PreparedStatement pstmt = pConn.prepareStatement(l_Query);
		ResultSet rs = pstmt.executeQuery();

		while (rs.next())
			ret = SharedUtil.formatDBDate2MIT(rs.getString(1));

		return ret;
	}

	public boolean isRunnedEOD(String pProcessedDate, Connection conn) throws SQLException {
		boolean result = false;
		String l_Query = "SELECT * FROM LastDates WHERE CONVERT(DateTime,LastProcessedDate) = ?";
		PreparedStatement pstmt = conn.prepareStatement(l_Query);
		pstmt.setString(1, pProcessedDate);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			result = true;
		}
		return result;
	}
	
	public BudgetYearData getBudgetYearData(int pType, Connection conn) throws SQLException{
		BudgetYearData l_data=new BudgetYearData();
		
		String l_Query = "SELECT * FROM T00070 WHERE N1 = ?";
		
		PreparedStatement pstmt = conn.prepareStatement(l_Query);
		pstmt.setInt(1, pType);
		
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()){
			readRecordSQL(l_data, rs);
		}
		
		return l_data;
	}
	
	private static void readRecordSQL(BudgetYearData adataBean,ResultSet aRS)
	{
		try {
			adataBean.setpDescription(aRS.getString("description"));
			adataBean.setpT1(aRS.getString("T1"));
			adataBean.setpT2(aRS.getString("T2"));
			adataBean.setpT3(aRS.getString("T3"));
			adataBean.setpT4(aRS.getString("T4"));
			adataBean.setpT5(aRS.getString("T5"));
			adataBean.setpT6(aRS.getString("T6"));
			adataBean.setpT7(aRS.getString("T7"));
			adataBean.setpT8(aRS.getString("T8"));
			adataBean.setpT9(aRS.getString("T9"));
			adataBean.setpT10(aRS.getString("T10"));
			adataBean.setpT11(aRS.getString("T11"));
			adataBean.setpT12(aRS.getString("T12"));
			adataBean.setpN1(aRS.getInt("N1"));
			adataBean.setpN2(aRS.getInt("N2"));
			adataBean.setpN3(aRS.getInt("N3"));
			adataBean.setpN4(aRS.getInt("N4"));
			adataBean.setpN5(aRS.getInt("N5"));
			adataBean.setpN6(aRS.getInt("N6"));
			adataBean.setpN7(aRS.getInt("N7"));
			adataBean.setpN8(aRS.getInt("N8"));
			adataBean.setpN9(aRS.getInt("N9"));
			adataBean.setpN10(aRS.getInt("N10"));
			adataBean.setpN11(aRS.getInt("N11"));
			adataBean.setpN12(aRS.getInt("N12"));	
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
