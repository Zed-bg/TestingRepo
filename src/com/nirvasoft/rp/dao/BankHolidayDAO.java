package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.nirvasoft.rp.shared.icbs.BankHolidayData;
import com.nirvasoft.rp.util.SharedUtil;

public class BankHolidayDAO {
	private BankHolidayData BankHolidayDataBean;

	public BankHolidayDAO() {
		BankHolidayDataBean = new BankHolidayData();
		new ArrayList<BankHolidayData>();
	}

	public BankHolidayData getBankHoliday(String pDate, Connection conn) throws SQLException {
		BankHolidayData bankHolidayData = new BankHolidayData();
		PreparedStatement pstmt = null;
		pstmt = conn.prepareStatement("SELECT * from BankHoliday WHERE CONVERT(DATETIME,HolidayDate) = ?");
		pstmt.setString(1, pDate);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			readRecord(bankHolidayData, rs);
		}
		pstmt.close();
		return bankHolidayData;
	}

	/*
	 * public boolean getBankHoliday(String pDate, Connection conn){ boolean
	 * result = false; try { String l_Query =
	 * "SELECT * FROM BankHoliday WHERE CONVERT(DATETIME,HolidayDate) = ?";
	 * PreparedStatement pstmt = conn.prepareStatement(l_Query);
	 * pstmt.setString(1, pDate); ResultSet rs = pstmt.executeQuery(); while
	 * (rs.next()){ result = true; readRecord(BankHolidayDataBean, rs); } }
	 * catch (SQLException e) { // TODO: handle exception e.printStackTrace(); }
	 * return result; }
	 */
	public boolean getBankHolidayCheck(String pDate, Connection conn) throws SQLException {
		boolean result = false;
		BankHolidayData bankHolidayData = new BankHolidayData();
		PreparedStatement pstmt = conn
				.prepareStatement("SELECT * FROM BankHoliday WHERE CONVERT(DATETIME,HolidayDate) = ?");
		pstmt.setString(1, pDate);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			readRecord(bankHolidayData, rs);
			result = true;
		}
		return result;
	}

	public BankHolidayData getBankHolidayData() {
		return BankHolidayDataBean;
	}

	private void readRecord(BankHolidayData pData, ResultSet aRS) throws SQLException {
		pData.setHolidayDate(SharedUtil.formatDBDate2MIT(aRS.getString("HolidayDate")));
		pData.setDescription(aRS.getString("Description"));
		/*
		 * pData.setT1(aRS.getString("T1")); pData.setT2(aRS.getString("T2"));
		 * pData.setT3(aRS.getString("T3")); pData.setN1(aRS.getDouble("N1"));
		 * pData.setN2(aRS.getDouble("N2")); pData.setN3(aRS.getDouble("N3"));
		 */
	}

	public void setBankHolidayData(BankHolidayData p) {
		BankHolidayDataBean = p;
	}

}
