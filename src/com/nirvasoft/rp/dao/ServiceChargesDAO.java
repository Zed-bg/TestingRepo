package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.nirvasoft.rp.shared.ServiceChargesData;
import com.nirvasoft.rp.util.SharedUtil;

public class ServiceChargesDAO {
	
	private String mODServiceCharges = "ODServiceCharges";
	private String mLoanServiceCharges = "LoanServiceCharges";
	
	public ArrayList<ServiceChargesData> getLoanServiceChargesForRateChanges(String pAccNumber,String pTenure, Connection pConn) throws SQLException{
		int l_index = 1;
		ArrayList<ServiceChargesData> lstIntRate = new ArrayList<ServiceChargesData>();
		String l_Query = "SELECT S.* FROM "+mLoanServiceCharges+" S INNER JOIN T00005 ON S.AccType = T00005.T2 "
					    +"WHERE T00005.T1= ? ";
		if(!pTenure.trim().equalsIgnoreCase("")){
			l_Query += "AND S.LoanType = ? ";
		}
		l_Query += "Order By S.LoanType,DateFrom,DateTo ";
		PreparedStatement pstmts = pConn.prepareStatement(l_Query);
		pstmts.setString(l_index++, pAccNumber);
		if(!pTenure.trim().equalsIgnoreCase("")){
			pstmts.setString(l_index++, pTenure);
		}		
		ResultSet rs = pstmts.executeQuery();
		while(rs.next()){
			ServiceChargesData pData = new ServiceChargesData();
			pData.setRate(rs.getDouble("Rate"));
			pData.setExtensionFee(rs.getDouble("ExtFee"));
			pData.setDateFrom(SharedUtil.formatDBDate2MIT(rs.getString("DateFrom")));
			pData.setDateTo(rs.getString("DateTo"));
			pData.setValid(rs.getBoolean("Valid"));
			pData.setODType(rs.getInt("LoanType"));
			lstIntRate.add(pData);
		}
		return lstIntRate;
	}
	
}
