package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.nirvasoft.rp.shared.PBFixedDepositAccountData;
import com.nirvasoft.rp.util.SharedUtil;

public class PBFixedDepositAccountDAO {
	
	private PBFixedDepositAccountData pbFixedAccData;
	private ArrayList<PBFixedDepositAccountData> lstpbFixedAccData;
	
	public PBFixedDepositAccountData getPbFixedAccData() {
		return pbFixedAccData;
	}
	public void setPbFixedAccData(PBFixedDepositAccountData pbFixedAccData) {
		this.pbFixedAccData = pbFixedAccData;
	}
	public ArrayList<PBFixedDepositAccountData> getLstpbFixedAccData() {
		return lstpbFixedAccData;
	}
	public void setLstpbFixedAccData(
			ArrayList<PBFixedDepositAccountData> lstpbFixedAccData) {
		this.lstpbFixedAccData = lstpbFixedAccData;
	}
	
	public PBFixedDepositAccountDAO() {
		pbFixedAccData=new PBFixedDepositAccountData();
		lstpbFixedAccData=new ArrayList<PBFixedDepositAccountData>();
	}
	
	public PBFixedDepositAccountData getFixedAccounts(String AccNumber,Connection conn)
	{
		PBFixedDepositAccountData result=new PBFixedDepositAccountData();
		try {
			PreparedStatement pstmt=conn.prepareStatement("SELECT * FROM PBFixedDepositAccount WHERE AccNumber=?");
			pstmt.setString(1,AccNumber);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next())
			{
				pbFixedAccData=new PBFixedDepositAccountData();
				readRecord(pbFixedAccData,rs);
				result=pbFixedAccData;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private void readRecord(PBFixedDepositAccountData pbData,ResultSet rs) {
		try {
			pbData.setAccNumber(rs.getString("AccNumber"));
			pbData.setRefNo(rs.getString("RefNo"));
			pbData.setTenure(rs.getShort("Tenure"));
			pbData.setStartDate(SharedUtil.formatDBDate2MIT(rs.getString("StartDate")));
			pbData.setDueDate(SharedUtil.formatDBDate2MIT(rs.getString("DueDate")));
			pbData.setIntToBePaid(rs.getDouble("IntToBePaid"));
			pbData.setIntPayCode(rs.getByte("IntPayCode"));
			pbData.setAccToBePaid(rs.getString("AccToBePaid"));
			pbData.setAutoRenew(rs.getBoolean("AutoRenew"));
			pbData.setReceiptNo(rs.getString("ReceiptNo"));
			pbData.setOtherFacility(rs.getByte("OtherFacility"));
			pbData.setAmount(rs.getDouble("Amount"));
			pbData.setLastTransDate(SharedUtil.formatDBDate2MIT(rs.getString("LastTransDate")));
			pbData.setStatus(rs.getByte("Status"));
			pbData.setOpeningDate(SharedUtil.formatDBDate2MIT(rs.getString("openingDate")));
			pbData.setClosingDate(SharedUtil.formatDBDate2MIT(rs.getString("ClosingDate")));
			pbData.setApprovalNo(rs.getString("ApprovalNo"));
			pbData.setTotalDays(rs.getInt("TotalDays"));
			pbData.setYearRate(rs.getDouble("YearlyRate"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public boolean getAccountToBePaid(String pAccNumber,Connection conn) {
		boolean result=false;
		try {
			 PreparedStatement pstmt=conn.prepareStatement("SELECT AccNumber FROM [PBFixedDepositAccount] WHERE AccToBePaid=? AND Status<9");
			 pstmt.setString(1, pAccNumber);
			 
			 ResultSet rs=pstmt.executeQuery();
			 
			
			 while(rs.next()) {
				 pbFixedAccData=new PBFixedDepositAccountData();				 
				 pbFixedAccData.setAccNumber(rs.getString("AccNumber"));
				 result=true;
			 }
			 pstmt.close();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			result=false;
		}
		return result;
	}
	
	public String getMaxRefNo(String pAccNumber, Connection conn){
		String ret = "";
		try {
			int no =0;
			String l_Query = "SELECT MAX(Convert(int,SUBSTRING(RefNo,3,8))) MRN FROM PBFixedDepositAccount " +
							 "WHERE AccNumber= ?";
			PreparedStatement pstmt = conn.prepareStatement(l_Query);
			pstmt.setString(1, pAccNumber);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()){
				no = rs.getInt("MRN");
			}
			ret = "R-" +(no + 1);
			pstmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
}
