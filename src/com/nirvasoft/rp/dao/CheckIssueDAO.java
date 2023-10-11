package com.nirvasoft.rp.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.nirvasoft.rp.core.ccbs.LogicMgr;
import com.nirvasoft.rp.shared.CheckIssueCriteriaData;
import com.nirvasoft.rp.shared.CheckIssueData;
import com.nirvasoft.rp.util.SharedUtil;

public class CheckIssueDAO {

	public ArrayList<CheckIssueData> getCheckBooks(CheckIssueCriteriaData pCheckIssueCriteriaBean, Connection conn){
		ArrayList<CheckIssueData> ret = new ArrayList<CheckIssueData>();
		try {
			StringBuffer  l_query = new StringBuffer("");
			l_query.append("SELECT CheckIssue.AccNumber, CheckIssue.ChkNoFrom, CheckIssue.DateIssued, CheckIssue.DateApproved, CheckIssue.Status,"
					+ " CheckIssue.Sheets, CheckIssue.ChkNoChar,CheckIssue.CurrencyCode,CheckIssue.T2 FROM CheckIssue WHERE 1=1");

			PrepareWhereClause(pCheckIssueCriteriaBean, l_query);
			PreparedStatement pstmt = conn.prepareStatement(l_query.toString());
			prepareCriteria(pCheckIssueCriteriaBean, pstmt);
			
			ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
            	CheckIssueData object = new CheckIssueData();
            	readRecord(object, rs);
            	ret.add(object);
            }
             pstmt.close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
       return ret;
	}
	
	private void PrepareWhereClause(CheckIssueCriteriaData aCheckIssueCriteriaBean, StringBuffer aQuery){
		 if (!aCheckIssueCriteriaBean.AccountNumber.equals("")) {
			 aQuery.append(" AND CheckIssue.AccNumber = ?");
		 }
		 if (aCheckIssueCriteriaBean.ChkNoFrom != 0) {
			 aQuery.append(" AND CheckIssue.ChkNoFrom = ?");
		 }
		 if (!aCheckIssueCriteriaBean.ChkNoChar.equals("")) {
			 aQuery.append(" AND CheckIssue.ChkNoChar = ?");
		 }
		 if (aCheckIssueCriteriaBean.Status != 255) {
			 aQuery.append(" AND CheckIssue.Status = ?");
		 }
		 if (!aCheckIssueCriteriaBean.RequestNo.equals("")) {
			 aQuery.append(" AND CheckIssue.T1 = ?");
		 }
		 if (!aCheckIssueCriteriaBean.displayStatus.equals("")) {
			 aQuery.append(" AND CheckIssue.Sheets  like '%0%' ");
		 }
	 }	
	
	private void prepareCriteria(CheckIssueCriteriaData aCheckIssueCriteriaBean, PreparedStatement aPS) throws SQLException{
		int l_paraIndex = 0;
        if (!aCheckIssueCriteriaBean.AccountNumber.equals("")) {
        	l_paraIndex += 1;
	        aPS.setString(l_paraIndex, aCheckIssueCriteriaBean.AccountNumber);       		
		}
        if (aCheckIssueCriteriaBean.ChkNoFrom != 0) {
        	l_paraIndex += 1;
	        aPS.setInt(l_paraIndex, aCheckIssueCriteriaBean.ChkNoFrom);       		
		}
        if (!aCheckIssueCriteriaBean.ChkNoChar.equals("")) {
        	l_paraIndex += 1;
	        aPS.setString(l_paraIndex, aCheckIssueCriteriaBean.ChkNoChar);       		
		}
        if (aCheckIssueCriteriaBean.Status != 255) {
        	l_paraIndex += 1;
	        aPS.setInt(l_paraIndex, aCheckIssueCriteriaBean.Status);       		
		}
        if (!aCheckIssueCriteriaBean.RequestNo.equals("")) {
        	l_paraIndex += 1;
	        aPS.setString(l_paraIndex, aCheckIssueCriteriaBean.RequestNo);       		
		}
   }
	
	private void readRecord(CheckIssueData aCheckIssueBean,ResultSet aRS) {
		try {
			aCheckIssueBean.setAccountNumber(aRS.getString("AccNumber"));
			aCheckIssueBean.setChkNoFrom(aRS.getInt("ChkNoFrom"));
			aCheckIssueBean.setDateIssued(SharedUtil.formatDBDate2MIT(aRS.getString("DateIssued")));
			aCheckIssueBean.setDateApproved(SharedUtil.formatDBDate2MIT(aRS.getString("DateApproved")));
			aCheckIssueBean.setStatus(aRS.getInt("Status"));
			aCheckIssueBean.setSheets(aRS.getString("Sheets"));			
			aCheckIssueBean.setChkNoChar(aRS.getString("ChkNoChar"));
			aCheckIssueBean.setCurrencyCode(aRS.getString("CurrencyCode"));
			aCheckIssueBean.setBranchCode(aRS.getString("T2"));
		} catch (SQLException e) {			
			e.printStackTrace();
		}
	}
	
	public boolean updateCheckStatus(String pAccountNumber, String pCheckNo, int pStatus, Connection conn){
		boolean result=false;
		StringBuffer l_ChequeDigit = new StringBuffer();
        StringBuffer l_ChequeNoChar = new StringBuffer();
        
        AccountsDAO l_AccDAO = new AccountsDAO(conn);
        String l_CurrencyCode = "";
        
		try {
			if (l_AccDAO.getAccount(pAccountNumber, conn)) {
				l_CurrencyCode = l_AccDAO.getAccountsBean().getCurrencyCode();
			}
			
			LogicMgr.seperateCheckNo(pCheckNo, l_ChequeNoChar, l_ChequeDigit);
			
			PreparedStatement pstmt=conn.prepareStatement("UPDATE [CheckIssue] " +
                        "SET [Sheets] = STUFF([Sheets], ? - [ChkNoFrom] + 1, 1, ?) " +
                        "WHERE [ChkNoChar] = ? AND [Status] = ? " +
                        "AND [ChkNoFrom] <= ? AND ? <= ([ChkNoFrom] + LEN([Sheets]) - 1) AND [AccNumber] = ? " +
                        "AND [CheckIssue].[CurrencyCode] = ?");
			int i = 1;
			pstmt.setInt(i++, Integer.parseInt(l_ChequeDigit.toString()));
			
			// Cheque Status
            // 0 - Unpaid, 1 - Paid, 2 - Stopped, 3 - Cancel
			pstmt.setInt(i++, pStatus);
			pstmt.setString(i++, l_ChequeNoChar.toString());
			pstmt.setInt(i++, 0);	// Check book Status 0 - Approve, 1 - Save
			pstmt.setInt(i++, Integer.parseInt(l_ChequeDigit.toString()));
			pstmt.setInt(i++, Integer.parseInt(l_ChequeDigit.toString()));
			pstmt.setString(i++, pAccountNumber);
			pstmt.setString(i++, l_CurrencyCode);
			
			pstmt.setQueryTimeout(DAOManager.getNormalTime());
		    int ret=pstmt.executeUpdate();
            result = ret > 0 ? true : false;
            pstmt.close();
		} catch (Exception e) {
			
			e.printStackTrace();
			result=false;
		}
       return result;
	}
	
	public int getCheckStatus(String pAccountNumber, String pCheckNo,Connection conn) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, SQLException{
		int ret = 255;
		StringBuffer l_ChequeDigit = new StringBuffer();
        StringBuffer l_ChequeNoChar = new StringBuffer();
        AccountsDAO l_AccDAO = new AccountsDAO(conn);
        String l_CurrencyCode = "";        

		
		if (l_AccDAO.getAccount(pAccountNumber, conn)) {
			l_CurrencyCode = l_AccDAO.getAccountsBean().getCurrencyCode();
		}
		
		// Seperate CheckNo
		LogicMgr.seperateCheckNo(pCheckNo, l_ChequeNoChar, l_ChequeDigit);
		
		PreparedStatement pstmt=conn.prepareStatement(
                    "SELECT SUBSTRING(Sheets, ? - ChkNoFrom + 1, 1) As ChequeStatus " +
                    "FROM [CheckIssue] " +
                    "WHERE [CheckIssue].[ChkNoChar] = ? AND [CheckIssue].[Status] = 0 " +
                    "AND [CheckIssue].[ChkNoFrom] <= ? AND ? <= ([CheckIssue].[ChkNoFrom] + LEN(Sheets) - 1) AND [CheckIssue].[AccNumber] = ? " +
                    "AND [CheckIssue].[CurrencyCode] = ?");
        
		int i = 1;
		int chkdigit = 0;
		if (!l_ChequeDigit.toString().equals("")){
			chkdigit = Integer.parseInt(l_ChequeDigit.toString());
		}
		pstmt.setInt(i++, chkdigit);
		pstmt.setString(i++, l_ChequeNoChar.toString());
		pstmt.setInt(i++, chkdigit);
		pstmt.setInt(i++, chkdigit);
		pstmt.setString(i++, pAccountNumber);
		pstmt.setString(i++, l_CurrencyCode);
		
        ResultSet rs = pstmt.executeQuery();
        while(rs.next())
        	ret = rs.getInt(1);
        
        pstmt.close();
		
		return ret;
	}
}
