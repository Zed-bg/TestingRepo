package com.nirvasoft.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.nirvasoft.rp.shared.LinkDatabasesCriteriaData;
import com.nirvasoft.rp.shared.LinkDatabasesData;

public class LinkDatabasesDAO {
	public LinkDatabasesData getLinkDataBase(LinkDatabasesCriteriaData pData) {
		LinkDatabasesData ret = new LinkDatabasesData();
		DAOManager l_DAOManager = new DAOManager();
		Connection l_Conn = l_DAOManager.CreateConnection();		
		try {
			ret = getLinkDataBase(pData,l_Conn);
			l_Conn.close();
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		return ret;
	}
	
	public LinkDatabasesData getLinkDataBase(LinkDatabasesCriteriaData pData, Connection conn) throws SQLException {
		LinkDatabasesData ret = new LinkDatabasesData();
		
		StringBuffer l_query = new StringBuffer();
		l_query.append("SELECT * FROM LinkDataBases WHERE 1=1");
		prepareWhereClause(pData, l_query);
		PreparedStatement pstmt = conn.prepareStatement(l_query.toString());
		prepareCriteria(pData, pstmt);
		
		ResultSet rs = pstmt.executeQuery();
		while(rs.next())
			readRecord(ret, rs);
		pstmt.close();

		
		return ret;
	}
	
	private void prepareWhereClause(LinkDatabasesCriteriaData pData, StringBuffer pQuery){
		if (pData.TypeNo != 0){
			pQuery.append(" AND [TypeNo] = ?");
		}
	}
	
	private void prepareCriteria(LinkDatabasesCriteriaData pData, PreparedStatement pPS) throws SQLException{
		int l_paraIndex = 0;
		if (pData.TypeNo != 0){
			l_paraIndex += 1;
			pPS.setShort(l_paraIndex, pData.TypeNo);
		}
	}
	
	private void readRecord(LinkDatabasesData pData,ResultSet pRS) throws SQLException{
		pData.setID(pRS.getInt("ID"));
		pData.setDatabaseType(pRS.getShort("DatabaseType"));
		pData.setTypeNo(pRS.getShort("TypeNo"));
		pData.setTypeDescription(pRS.getString("TypeDescription"));
		pData.setDataSource(pRS.getString("DataSource"));
		pData.setCatalog(pRS.getString("Catalog"));
		pData.setUserID(pRS.getString("UserID"));
		pData.setPassword(pRS.getString("Password"));
		pData.setTableName(pRS.getString("TableName"));
		pData.setT1(pRS.getString("T1"));
		pData.setT2(pRS.getString("T2"));
		pData.setT3(pRS.getString("T3"));
	}
	
}
