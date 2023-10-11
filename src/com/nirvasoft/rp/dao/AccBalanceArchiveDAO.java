package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.nirvasoft.rp.shared.AccBalanceArchiveData;

public class AccBalanceArchiveDAO {

	public ArrayList<AccBalanceArchiveData> getLoanInterestInABA(String pAccNumber,String pYear,String[] result,String pCurrentDate,Connection conn) throws SQLException{
		ArrayList<AccBalanceArchiveData> l_dataList = new ArrayList<AccBalanceArchiveData>();
		AccBalanceArchiveData l_data = null;
		String query = "";
		PreparedStatement pstmt = null;
		
			query = "select * from accbalancearchive where accnumber = ? and asdate >= (select min(sanctiondatebr) from laodf where accnumber = ?)"
					+ " and year(asdate) = ? and month(asdate) in ( ";
			for (int i = 0; i < result.length; i++) {
				if (i == result.length - 1)
					query += result[i] + " )";
				else
					query += result[i] + ",";
			}

			query += " and asdate <= ? ";
			query += " order by asdate desc ";
			/* query += " order by asdate "; */

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, pAccNumber);
			pstmt.setString(2, pAccNumber);
			pstmt.setString(3, pYear);
			pstmt.setString(4, pCurrentDate);
			
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			l_data = new AccBalanceArchiveData();
			l_data.setAccountNumber(rs.getString("AccNumber"));
			l_data.setAsDate(rs.getString("AsDate"));
			l_data.setCurrentBalance(rs.getDouble("CurrentBalance"));
			l_dataList.add(l_data);
		}
		rs.close();
		pstmt.close();
		return l_dataList;
	}
	
	public ArrayList<AccBalanceArchiveData> getBalanceForLoanSystemOdInABA(String pAccNumber,String pSanctionDate, String pCancelDate,Connection pConn){
		int index=1;
		
		ArrayList<AccBalanceArchiveData> lstABAdata=new ArrayList<AccBalanceArchiveData>();
		String sql="SELECT * FROM ACCBALANCEARCHIVE WHERE ACCNUMBER = ? ";
		if (!pSanctionDate.equalsIgnoreCase("")){
			sql+="AND ASDATE >= ? ";
		}else{
			sql+="AND ASDATE >= (select min(sanctiondatebr) from aodf where accnumber = ?) ";
		}
				sql+="AND ASDATE <= ? ";
		try {
			PreparedStatement pstmt=pConn.prepareStatement(sql);
			pstmt.setString(index++, pAccNumber);
			if (!pSanctionDate.equalsIgnoreCase("")){
				pstmt.setString(index++, pSanctionDate);
			}else{
				pstmt.setString(index++, pAccNumber);
			}
			
			pstmt.setString(index++, pCancelDate);
			ResultSet rs=pstmt.executeQuery();		
			
			while(rs.next()){
				AccBalanceArchiveData l_ABAData=new AccBalanceArchiveData();
				l_ABAData.setAccountNumber(rs.getString("AccNumber"));
				l_ABAData.setAsDate(rs.getString("AsDate"));
				l_ABAData.setCurrentBalance(rs.getDouble("CurrentBalance"));
				lstABAdata.add(l_ABAData);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return lstABAdata;		
	}
}
