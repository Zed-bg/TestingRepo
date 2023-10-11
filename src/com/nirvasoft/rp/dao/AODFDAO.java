package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.nirvasoft.rp.shared.ODData;
import com.nirvasoft.rp.shared.TempData;

public class AODFDAO {
	String mAODF = "AODF";
	ODData l_ODData;
	
	
	public AODFDAO() {
		l_ODData=new ODData();
	} 
	
	public boolean isActiveBatchNo(String pAccountNo, String pBatchNo, Connection pConn) throws SQLException {
		boolean result = false;
		
		PreparedStatement pstmt = pConn.prepareStatement("SELECT * FROM "+mAODF+" WHERE AccNumber = ? AND BatchNo = ? AND STATUS = 1 ");
		pstmt.setString(1, pAccountNo);
		pstmt.setString(2, pBatchNo);
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()){
			l_ODData = new ODData();
			l_ODData.setBusinessClass(rs.getShort("BusinessClass")) ;
			result = true;
		}
		rs.close();
		pstmt.close();
		return result;
	}
	
	public boolean isActiveTODBatchNo(String pAccountNo, String ODType, Connection pConn) throws SQLException {
		boolean result = false;
		
		PreparedStatement pstmt = pConn.prepareStatement("SELECT * FROM "+mAODF+" WHERE AccNumber = ? AND ODType = ? AND STATUS = 1 ");
		pstmt.setString(1, pAccountNo);
		pstmt.setString(2, ODType);
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()){
			l_ODData = new ODData();
			l_ODData.setBusinessClass(rs.getShort("BusinessClass")) ;
			result = true;
		}
		rs.close();
		pstmt.close();
		return result;
	}
	
	public ArrayList<TempData> getODType(String pAccNumber,Connection pConn)throws SQLException {
		ArrayList<TempData> l_Result = new ArrayList<TempData>();			
		PreparedStatement pstmt = pConn.prepareStatement("select distinct aodf.odtype from aodf where aodf.accnumber = ? and aodf.status <=2 and aodf.odtype not in (0,100)");
		pstmt.setString(1,pAccNumber);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){	
			TempData l_Temp = new TempData();
			l_Temp.setT1(rs.getString("ODType"));
			l_Result.add(l_Temp);
		}
		return l_Result;
	}
	
	
	public ODData getODData() {
		return l_ODData;
	}
	
	public void setODData(ODData oDBean) {
		l_ODData = oDBean;
	}
	
}
