package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.nirvasoft.rp.shared.FixedDepositAccountData;
import com.nirvasoft.rp.util.SharedUtil;

public class FixedDepositAccountDAO {
	
	private FixedDepositAccountData FixedDepositAccountDataBean;
	private ArrayList<FixedDepositAccountData> lstFixedDepositAccountDatasBean;
	
	public FixedDepositAccountData getFixedDepositAccountDataBean(){return FixedDepositAccountDataBean;}
	public void setFixedDepositAccountDataBean(FixedDepositAccountData p){FixedDepositAccountDataBean = p;}
	
	public ArrayList<FixedDepositAccountData> getFixedDepositAccountDatasBean(){return lstFixedDepositAccountDatasBean;}
	public void getFixedDepositAccountDatasBean(ArrayList<FixedDepositAccountData> p){lstFixedDepositAccountDatasBean = p;}
	
	public FixedDepositAccountDAO(){
		FixedDepositAccountDataBean = new FixedDepositAccountData();
		lstFixedDepositAccountDatasBean = new ArrayList<FixedDepositAccountData>();
	}
	
	
	public boolean getFixedDepositAccount(String pAccNumber, Connection conn){
		boolean result = false;
		try {
			String l_Query =  "SELECT FD.AccNumber,FD.Tenure,FD.StartDate,FD.DueDate,FD.intToBePaid,PB.IntPayCode,FD.AccToBePaid,FD.AutoRenew,FD.ReceiptNo,"
							+ "PB.RefNo,PB.OtherFacility,PB.Amount,PB.LastTransDate,PB.Status,PB.OpeningDate,PB.ClosingDate,PB.ApprovalNo,PB.TotalDays, PB.YearlyRate, T.T3 "
							+ " FROM FixedDepositAccount FD INNER JOIN PBFixedDepositAccount PB ON FD.AccNumber=PB.AccNumber INNER JOIN T00005 T ON PB.AccNumber = T.T1 WHERE FD.AccNumber=?";
			PreparedStatement pstmt = conn.prepareStatement(l_Query);
			pstmt.setString(1, pAccNumber);
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				FixedDepositAccountDataBean = new FixedDepositAccountData();
				readRecord(rs, FixedDepositAccountDataBean,true);
				result = true;
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}
	
	private void readRecord(ResultSet rs, FixedDepositAccountData pData,Boolean pIsPBFixedData) throws SQLException{
		pData.setAccountNumber(rs.getString("AccNumber"));
		pData.setTenure(rs.getShort("Tenure"));
		pData.setStartDate(SharedUtil.formatDBDate2MIT(rs.getString("StartDate")));
		pData.setDueDate(SharedUtil.formatDBDate2MIT(rs.getString("DueDate")));
		pData.setIntToBePaid(rs.getDouble("IntToBePaid"));
		pData.setIntPayCode(rs.getShort("IntPayCode"));
		pData.setAccToBePaid(rs.getString("AccToBePaid"));
		pData.setAutoRenew(rs.getBoolean("AutoRenew"));
		pData.setReceiptNo(rs.getString("ReceiptNo"));
		pData.setAccType(rs.getString("T3"));
		
		if(pIsPBFixedData) {
			pData.setRefNo(rs.getString("RefNo"));
			pData.setOtherFacility(rs.getByte("OtherFacility"));
			pData.setAmount(rs.getDouble("Amount"));
			pData.setLastTransDate(SharedUtil.formatDBDate2MIT(rs.getString("LastTransDate")));
			pData.setStatus(rs.getByte("Status"));
			pData.setOpeningDate(SharedUtil.formatDBDate2MIT(rs.getString("OpeningDate")));
			pData.setClosingDate(SharedUtil.formatDBDate2MIT(rs.getString("ClosingDate")));
			pData.setApprovalNo(rs.getString("ApprovalNo"));
			pData.setTotalDays(rs.getInt("TotalDays"));
			pData.setYearlyRate(rs.getDouble("YearlyRate"));
		}
	}
	
	public boolean addFixedDepositAccount(FixedDepositAccountData pData, Connection conn) throws SQLException{
		boolean result = false;
		
	String l_Query = "INSERT INTO FixedDepositAccount (AccNumber, Tenure, StartDate, DueDate, " +
			"IntToBePaid, IntPayCode, AccToBePaid, AutoRenew, ReceiptNo) VALUES " +
			"(?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	PreparedStatement pstmt = conn.prepareStatement(l_Query);
	updateRecord(pstmt, pData, "insert");
	
	if(pstmt.executeUpdate()>0)
		result = true; 
		
		return result;
	}
	
	private void updateRecord(PreparedStatement ps, FixedDepositAccountData pData, String pFrom) throws SQLException{
		int i = 1;
		if (pFrom.equals("insert")){
			ps.setString(i++, pData.getAccountNumber());
			ps.setShort(i++, pData.getTenure());
			ps.setString(i++, pData.getStartDate());
			ps.setString(i++, pData.getDueDate());
			ps.setDouble(i++, pData.getIntToBePaid());
			ps.setShort(i++, pData.getIntPayCode());
			ps.setString(i++, pData.getAccToBePaid());
			ps.setBoolean(i++, pData.getAutoRenew());
			ps.setString(i++, pData.getReceiptNo());
		} else if (pFrom.equals("update")) {
			ps.setShort(i++, pData.getTenure());
			ps.setString(i++, pData.getStartDate());
			ps.setString(i++, pData.getDueDate());
			ps.setDouble(i++, pData.getIntToBePaid());
			ps.setShort(i++, pData.getIntPayCode());
			ps.setString(i++, pData.getAccToBePaid());
			ps.setBoolean(i++, pData.getAutoRenew());
			ps.setString(i++, pData.getReceiptNo());
			ps.setString(i++, pData.getAccountNumber());
			ps.setString(i++, pData.getRefNo());
		} else if (pFrom.equals("updatepb")) {
			ps.setShort(i++, pData.getTenure());
			ps.setString(i++, pData.getStartDate());
			ps.setString(i++, pData.getDueDate());
			ps.setDouble(i++, pData.getIntToBePaid());
			ps.setShort(i++, pData.getIntPayCode());
			ps.setString(i++, pData.getAccToBePaid());
			ps.setBoolean(i++, pData.getAutoRenew());
			ps.setString(i++, pData.getReceiptNo());
			ps.setByte(i++, pData.getOtherFacility());
			ps.setDouble(i++, pData.getAmount());
			ps.setString(i++, pData.getLastTransDate());
			ps.setByte(i++, pData.getStatus());
			ps.setString(i++, pData.getOpeningDate());
			ps.setString(i++, pData.getClosingDate());
			ps.setString(i++, pData.getApprovalNo());
			ps.setDouble(i++, pData.getYearlyRate());
			ps.setString(i++, pData.getStartDate());
			ps.setString(i++, pData.getDueDate());
			
			ps.setString(i++, pData.getAccountNumber());
			ps.setString(i++, pData.getRefNo());
		}else if(pFrom.equals("insertpb")) {
			ps.setString(i++,pData.getAccountNumber());
			ps.setString(i++, pData.getRefNo());
			ps.setShort(i++, pData.getTenure());
			ps.setString(i++, pData.getStartDate());
			ps.setString(i++,pData.getDueDate());
			ps.setDouble(i++, pData.getIntToBePaid());
			ps.setShort(i++,pData.getIntPayCode());
			ps.setString(i++,pData.getAccToBePaid());
			ps.setBoolean(i++, pData.getAutoRenew());
			ps.setString(i++,pData.getReceiptNo());
			ps.setByte(i++, pData.getOtherFacility());
			ps.setDouble(i++, pData.getAmount());
			ps.setString(i++, pData.getLastTransDate());
			ps.setByte(i++, pData.getStatus());
			ps.setString(i++, pData.getOpeningDate());
			ps.setString(i++, pData.getClosingDate());
			ps.setString(i++, pData.getApprovalNo());
			ps.setInt(i++, pData.getTotalDays());
			ps.setDouble(i++, pData.getYearlyRate());
		}else if (pFrom.equals("updatepbNoTrans")) {
			ps.setShort(i++, pData.getTenure());
			ps.setString(i++, pData.getStartDate());
			ps.setString(i++, pData.getDueDate());
			ps.setDouble(i++, pData.getIntToBePaid());
			ps.setShort(i++, pData.getIntPayCode());
			ps.setString(i++, pData.getAccToBePaid());
			ps.setBoolean(i++, pData.getAutoRenew());
			ps.setString(i++, pData.getReceiptNo());
			ps.setByte(i++, pData.getOtherFacility());
			ps.setDouble(i++, pData.getAmount());
			ps.setByte(i++, pData.getStatus());
			ps.setString(i++, pData.getOpeningDate());
			ps.setString(i++, pData.getClosingDate());
			ps.setString(i++, pData.getApprovalNo());
			//ps.setDouble(i++, pData.getYearlyRate());
			ps.setString(i++, pData.getStartDate());
			ps.setString(i++, pData.getDueDate());
			
			ps.setString(i++, pData.getAccountNumber());
			ps.setString(i++, pData.getRefNo());
		}else if (pFrom.equals("updatepbNoTransnew")) {
			ps.setShort(i++, pData.getTenure());
			ps.setString(i++, pData.getStartDate());
			ps.setString(i++, pData.getDueDate());
			ps.setDouble(i++, pData.getIntToBePaid());
			ps.setShort(i++, pData.getIntPayCode());
			ps.setString(i++, pData.getAccToBePaid());
			ps.setBoolean(i++, pData.getAutoRenew());
			ps.setString(i++, pData.getReceiptNo());
			ps.setByte(i++, pData.getOtherFacility());
			ps.setDouble(i++, pData.getAmount());
			ps.setByte(i++, pData.getStatus());
			ps.setString(i++, pData.getOpeningDate());
			ps.setString(i++, pData.getClosingDate());
			ps.setString(i++, pData.getApprovalNo());
			ps.setDouble(i++, pData.getYearlyRate());
			ps.setString(i++, pData.getStartDate());
			ps.setString(i++, pData.getDueDate());
			
			ps.setString(i++, pData.getAccountNumber());
			ps.setString(i++, pData.getRefNo());
		}
		
	}
	
	public boolean addPBFixedDepositAccount(FixedDepositAccountData pData, Connection conn) throws SQLException{
		boolean result = false;
		
		String l_Query = "INSERT INTO PBFixedDepositAccount (AccNumber, RefNo, Tenure, StartDate, DueDate, IntToBePaid, " +
				"IntPayCode, AccToBePaid, AutoRenew, ReceiptNo, OtherFacility, Amount, LastTransDate, Status, OpeningDate, " +
				"ClosingDate, ApprovalNo, TotalDays,YearlyRate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
		
		PreparedStatement pstmt = conn.prepareStatement(l_Query);
		updateRecord(pstmt,pData, "insertpb");
		if(pstmt.executeUpdate() > 0)
			result = true;
		pstmt.close();			
		
		return result;
	}
}
