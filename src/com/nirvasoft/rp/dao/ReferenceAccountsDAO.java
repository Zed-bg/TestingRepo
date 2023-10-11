package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.nirvasoft.rp.shared.ReferenceAccountsCriteriaData;
import com.nirvasoft.rp.shared.TransactionData;
import com.nirvasoft.rp.shared.icbs.ReferenceAccountsData;

public class ReferenceAccountsDAO {	
	private ArrayList<ReferenceAccountsData> lstReferenceAccountsData = new ArrayList<ReferenceAccountsData>();
	private ReferenceAccountsData ReferenceAccountsDataBean;
	private TransactionData TransData;
	
	public ReferenceAccountsData getReferenceAccountsDataBean() {
		return ReferenceAccountsDataBean;
	}
	
	public boolean getReferenceAccCode(String pCode, Connection conn) throws SQLException {
		boolean ret = false;
		PreparedStatement pstmt = null;

		pstmt = conn.prepareStatement("SELECT * FROM ReferenceAccounts WHERE GLAccNumber = ? ");
		pstmt.setString(1, pCode);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			ReferenceAccountsDataBean = new ReferenceAccountsData();
			readRecord(ReferenceAccountsDataBean, rs);
			ret = true;
		}
		pstmt.close();

		return ret;
	}
	
	private void readRecord(ReferenceAccountsData aReferenceAccountsDataBean, ResultSet aRS) throws SQLException {
		aReferenceAccountsDataBean.setFunctionID(aRS.getInt("FunctionID"));
		aReferenceAccountsDataBean.setDescription(aRS.getString("Description"));
		aReferenceAccountsDataBean.setGLCode(aRS.getString("GLCode"));
		aReferenceAccountsDataBean.setGLAccNumber(aRS.getString("GLAccNumber"));
	}
	
	public HashMap<String, ReferenceAccountsData> getReferenceAccCodeList(Connection conn) throws SQLException {
		HashMap<String, ReferenceAccountsData> ret = new HashMap<String, ReferenceAccountsData>();
		PreparedStatement pstmt = null;

		pstmt = conn.prepareStatement("SELECT * FROM ReferenceAccounts ");
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			String glacc = rs.getString("GLAccNumber");
			ReferenceAccountsDataBean = new ReferenceAccountsData();
			readRecord(ReferenceAccountsDataBean, rs);
			ret.put(glacc, ReferenceAccountsDataBean);
		}
		pstmt.close();

		return ret;
	}
	
	public ReferenceAccountsData getReferenceAccount(ReferenceAccountsCriteriaData pCri, Connection conn) throws SQLException{
		ReferenceAccountsData ret = new ReferenceAccountsData();		
		String Query = "";
		StringBuffer l_query = new StringBuffer();
		l_query.append("SELECT FunctionId, Description, GLCode, GLAccNumber,BranchCode FROM REFERENCEACCOUNTS WHERE 1=1");	
		
		prepareWhereClause(pCri, l_query);
		PreparedStatement pstmt = conn.prepareStatement(l_query.toString());
		prepareCriteria(pCri, pstmt);
		
		ResultSet rs = pstmt.executeQuery();
		while (rs.next())
			readRecord(ret, rs);
		
		pstmt.close();
			
		
		return ret;
	}
	
	private void prepareWhereClause(ReferenceAccountsCriteriaData aCriBean, StringBuffer aQuery){
		if (aCriBean.FunctionID != 9999){
			aQuery.append(" AND FunctionId = ?");
		}
		if (!aCriBean.GLAccNumber.equals("")) {
			aQuery.append(" AND GLAccNumber = ?");
		}
	}
	
	private void prepareCriteria(ReferenceAccountsCriteriaData aCriBean, PreparedStatement aPS) throws SQLException{
		int l_paraindex = 0;
		if (aCriBean.FunctionID != 9999){
			l_paraindex += 1;
			aPS.setInt(l_paraindex, aCriBean.FunctionID);
		}
		if (!aCriBean.GLAccNumber.equals("")) {
			l_paraindex += 1;
			aPS.setString(l_paraindex, aCriBean.GLAccNumber);
		}
	}
}
